package www.yema.cn.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import www.yema.cn.request.ConponRequest;
import www.yema.cn.response.ConponResponse;
import www.yema.cn.response.ProductResponse;
import www.yema.cn.service.alimama.IAlimamaService;

@Component
public class AlimamaManager {
	
	@Autowired
	private IAlimamaService alimamaService;
	
	public String getProductShareUrl(String outShareContext) {	
	    StringBuffer sb=new StringBuffer();
        sb.append("－－－－查询结果－－－－").append("\\n");
		ProductResponse productVo;
        ConponResponse productConpon;
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
            }else{
                productShareUrl=productConpon.getShareUrl();
            }
            System.out.println("productShareUrl="+productShareUrl);
            shareProductHref = alimamaService.generateShare(productVo.getTitle(), productVo.getPictUrl(), productShareUrl);
            if(StringUtils.isBlank(productId)) {
            	throw new RuntimeException("生成商品优惠券异常");
            }
            sb.append("【标题】").append(productVo.getTitle()).append("\\n");
            sb.append("【宝贝原价】：").append(productVo.getReservePrice()).append("\\n");
            sb.append("【优惠券信息】：").append(productConpon.getCouponInfo()).append("\\n");
            sb.append("－－－－－－－－").append("\\n");
            sb.append(shareProductHref).append("\\n");           
        } catch (Exception e) {
            sb.append("无优惠信息").append("\\n");
        }
        sb.append("－－－－－－－－").append("\\n");
        sb.append("下单注意事项：").append("\\n");
        sb.append("1.下单注意事项：").append("\\n");
        sb.append("2.下单注意事项：").append("\\n");
			
		return sb.toString();
	}

}
