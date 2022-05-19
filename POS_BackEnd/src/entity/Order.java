package entity;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:18 AM
 **/
public class Order {
    private String orderId;
    private String orderDate;
    private String customerId;
    private double total;

    public Order() {
    }

    public Order(String orderId, String orderDate, String customerId, double total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
