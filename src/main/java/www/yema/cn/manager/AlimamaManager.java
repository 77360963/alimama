package www.yema.cn.manager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import www.yema.cn.service.alimama.IAlimamaService;
import www.yema.cn.vo.ConponVo;
import www.yema.cn.vo.ProductVo;

@Component
public class AlimamaManager {
	
	@Autowired
	private IAlimamaService alimamaService;
	
	public String getProductShareUrl(String outShareContext) {
		
		String productId=alimamaService.getProductIdByOutShareUrl(outShareContext);
		if(StringUtils.isBlank(productId)) {
			throw new RuntimeException("获取商品Id异常");
		}
		ProductVo productVo=alimamaService.getProductTitle(productId);
		if(productVo==null) {
			throw new RuntimeException("获取商品标题异常");
		}
		ConponVo conponVo=alimamaService.getConpon(productVo.getTitle(), productId);
		if(conponVo==null) {
			throw new RuntimeException("获取商品优惠券异常");
		}
		String shareProductHref=alimamaService.generateShare(productVo.getTitle(), productVo.getPictUrl(), conponVo.getCouponShareUrl());
		if(StringUtils.isBlank(productId)) {
			throw new RuntimeException("生成商品优惠券异常");
		}
		StringBuffer sb=new StringBuffer();
		sb.append("－－－－查询结果－－－－").append("\\n");
		sb.append("【标题】").append(productVo.getTitle()).append("\\n");
		sb.append("【宝贝原价】：").append(productVo.getReservePrice()).append("\\n");
		sb.append("【优惠券信息】：").append(conponVo.getCouponInfo()).append("\\n");
		sb.append("－－－－－－－－").append("\\n");
		sb.append(shareProductHref).append("\\n");
		sb.append("－－－－－－－－").append("\\n");
		sb.append("下单注意事项：").append("\\n");
		sb.append("1.下单注意事项：").append("\\n");
		sb.append("2.下单注意事项：").append("\\n");
		
		return sb.toString();
	}

}
