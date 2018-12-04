package www.yema.cn.request;

public class ConponRequest {
    /**
     * 查询条件
     */
    private String query;
    
    /**
     * 是否有优惠券
     */
    private boolean hasCoupon;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(boolean hasCoupon) {
        this.hasCoupon = hasCoupon;
    }
    
    
}
