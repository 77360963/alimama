package www.yema.cn.service.alimama;

import www.yema.cn.vo.ConponVo;
import www.yema.cn.vo.ProductVo;

public interface IAlimamaService {
	
	/**
	 * 根据淘口令获取商品Id
	 * @param outShareContext
	 * @return
	 */
	public String getProductIdByOutShareUrl(String outShareContext);
	
	/**
	 * 根据商品Id获取商品信息
	 * @param productId
	 * @return
	 */
	public ProductVo getProductTitle(String productId);
	
	/**
	 * 根据商品名称,商品Id获取优惠券信息
	 * @param productTitle
	 * @param productId
	 * @return
	 */
	public ConponVo getConpon(String productTitle,String productId);
	
	/**
	 * 生成推荐者的淘口令
	 * @param productHref
	 * @return
	 */
	public String generateShare(String text,String logo,String productHref);

}
