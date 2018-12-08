package www.yema.cn.response;

import java.math.BigDecimal;

public class ConponResponse {
    
    /**
     * 商品Id
     */
    private String productId;    
    
	
	/**
	 * 优惠券Id
	 */
	private String couponId;
	
	/**
	 * 优惠券地址
	 */
	private String couponShareUrl;
	
	/**
     * 无优惠券地址
     */
    private String ShareUrl;
	
	/**
	 * 优惠券金额
	 */
	private String couponAmount;
	
	/**
	 * 优惠券信息
	 */
	private String couponInfo;
	
	/**
	 * 佣金比例
	 */
	private BigDecimal commissionRate;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponShareUrl() {
        return couponShareUrl;
    }

    public void setCouponShareUrl(String couponShareUrl) {
        this.couponShareUrl = couponShareUrl;
    }

    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        ShareUrl = shareUrl;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	
	
    
    

}
