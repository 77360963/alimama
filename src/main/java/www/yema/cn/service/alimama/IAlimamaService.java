package www.yema.cn.service.alimama;

import java.util.List;

import www.yema.cn.request.ConponRequest;
import www.yema.cn.response.ConponDetailResponse;
import www.yema.cn.response.ConponResponse;
import www.yema.cn.response.ProductResponse;

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
	public ProductResponse getProductInfo(String productId);
	
	/**
	 * 根据条件查询获取优惠券信息
	 * @param productContext
	 * @return
	 */
	public List<ConponResponse> getConpon(ConponRequest conponRequest);
	
	
	/**
	 * 获取优惠券详细信息
	 * @param productId
	 * @param couponId
	 * @return
	 */
	public ConponDetailResponse getConponDetail(String productId,String couponId);
	
	
	/**
	 * 生成推荐者的淘口令
	 * @param productHref
	 * @return
	 */
	public String generateShare(String text,String logo,String productHref);
	
	
	/**
	 * 查询阿里妈妈订单
	 * @param startTime
	 * @param session
	 */
	public void queryAimamaOrder(String startTime,String session);
	

}
