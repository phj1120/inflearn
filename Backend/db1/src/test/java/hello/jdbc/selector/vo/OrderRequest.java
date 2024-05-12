package hello.jdbc.selector.vo;

public class OrderRequest {
    private String systemType;
    private String orderType;

    public OrderRequest(String systemType, String orderType) {
        this.systemType = systemType;
        this.orderType = orderType;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getOrderType() {
        return orderType;
    }
}
