package www.yema.cn.manager;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import www.yema.cn.request.ConponRequest;
import www.yema.cn.response.ConponDetailResponse;
import www.yema.cn.response.ConponResponse;
import www.yema.cn.response.ProductResponse;
import www.yema.cn.service.alimama.IAlimamaService;

@Component
public class AlimamaManager {
	
	 private static final Logger logger = LoggerFactory.getLogger(AlimamaManager.class);
	
	@Autowired
	private IAlimamaService alimamaService;
	
	
	/**
	 * 判断内容中是否有淘口令
	 * @param context
	 * @return
	 */
	public String getTaoKouLing(String context) {
		int firstIndex=context.indexOf("￥");
		int lastIndex=context.lastIndexOf("￥")+1;
		if(firstIndex==-1) {
			return "";
		}
		if(firstIndex==lastIndex) {
			return "";
		}
		String taokouling=context.substring(firstIndex, lastIndex);
		return taokouling;
	}
	
	/**
	 * 获取自己的淘口令
	 * @param outShareContext
	 * @return
	 */
	public String getProductShareUrl(String outShareContext) {
	    logger.info("集成查询,outShareContext={}",outShareContext);
		String message="无优惠信息\\n";
		ProductResponse productVo;
        ConponResponse productConpon;
        ConponDetailResponse conponDetailResponse=null;
        String shareProductHref;       
        try {
            String productId=alimamaService.getProductIdByOutShareUrl(outShareContext);
            if(StringUtils.isBlank(productId)) {
            	throw new RuntimeException("获取商品Id异常");
            }
            productVo = alimamaService.getProductInfo(productId);
            if(productVo==null) {
            	throw new RuntimeException("获取商品标题异常");
            }
            ConponRequest conponRequest=new ConponRequest();
            conponRequest.setQuery(productVo.getItemUrl());
            List<ConponResponse> conponList=alimamaService.getConpon(conponRequest);
            if(conponList.size()==0) {
            	throw new RuntimeException("获取商品优惠券异常");
            }
            productConpon = null;
            for(ConponResponse conpon:conponList){
                if(conpon.getProductId().equals(productId)){
                    productConpon=conpon;
                }
            }
            if(productConpon==null) {
                throw new RuntimeException("获取商品优惠券异常");
            }		
            
            String productShareUrl=productVo.getItemUrl();
            if(StringUtils.isNotBlank(productConpon.getCouponShareUrl())){
                productShareUrl=productConpon.getCouponShareUrl();               
                //查询优惠券优惠金额
                conponDetailResponse=alimamaService.getConponDetail(productId, productConpon.getCouponId()); 
                logger.info("交易金额"+productVo.getZkFinalPrice());
                logger.info("交易佣金比率"+productConpon.getCommissionRate());
                logger.info("交易佣金"+(new BigDecimal(productVo.getZkFinalPrice()).subtract(conponDetailResponse.getCouponAmount())).multiply(productConpon.getCommissionRate()));                 
            }else{
                productShareUrl=productConpon.getShareUrl();
            }
            System.out.println("productShareUrl="+productShareUrl);
            shareProductHref = alimamaService.generateShare(productVo.getTitle(), productVo.getPictUrl(), productShareUrl);
            if(StringUtils.isBlank(productId)) {
            	throw new RuntimeException("生成商品优惠券异常");
            }
            
            message=getMessage(productVo,productConpon, conponDetailResponse,shareProductHref);
                
        } catch (Exception e) {
        	logger.error("获取优惠券异常",e);
        }      
			
		return message;
	}
	
	
	public String getMessage(ProductResponse productVo,ConponResponse productConpon, ConponDetailResponse conponDetailResponse,String shareProductHref) {
		 StringBuffer sb=new StringBuffer();
	     sb.append("－－－－查询结果－－－－").append("\\n");
	     sb.append("【标题】").append(productVo.getTitle()).append("\\n");
         sb.append("【宝贝原价】：").append(productVo.getZkFinalPrice()).append("元\\n");
         if(conponDetailResponse!=null) {
        	 sb.append("【领优惠券】：").append(conponDetailResponse.getCouponAmount()).append("元\\n");
             sb.append("【付费参考】：").append(new BigDecimal(productVo.getZkFinalPrice()).subtract(conponDetailResponse.getCouponAmount())).append("元\\n");
         }
        
        //sb.append("【约返微信红包】：").append(productConpon.getCouponInfo()).append("\\n");
         sb.append("－－－－－－－－").append("\\n");
         sb.append(shareProductHref).append("\\n");
         sb.append("－－－－－－－－").append("\\n");
         sb.append("下单注意事项：").append("\\n");
         sb.append("复制上面内容到淘宝即可").append("\\n");
         //sb.append("①不要使用红包抵扣 （会使返利失效）").append("\\n");
         //sb.append("②下单后没提示【付款成功】的（复制订单号来绑定）").append("\\n");
         return sb.toString();
	}
	
	
	

}
