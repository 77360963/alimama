package www.yema.cn.response;

public class ProductResponse {
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 商品主图
	 */
	private String pictUrl;
	
    /**
         * 商品一口价格
     */
	private String reservePrice;
	
	
	/**
	 * 折扣价格
	 */
	private String zkFinalPrice;
	
	/**
	 * 商品URL
	 */
	private String itemUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPictUrl() {
		return pictUrl;
	}

	public void setPictUrl(String pictUrl) {
		this.pictUrl = pictUrl;
	}

	public String getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(String reservePrice) {
		this.reservePrice = reservePrice;
	}

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

	public String getZkFinalPrice() {
		return zkFinalPrice;
	}

	public void setZkFinalPrice(String zkFinalPrice) {
		this.zkFinalPrice = zkFinalPrice;
	}
    

}
