package dto;

import java.util.ArrayList;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:32 AM
 **/
public class OrderDTO {
    private String orderId;
    private String orderDate;
    private String customerId;
    private double total;
    private ArrayList<OrderDetailDTO> orderDetails;

    public OrderDTO() {
    }

    public OrderDTO(String orderId, String orderDate, String customerId, double total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.total = total;
    }

    public OrderDTO(String orderId, String orderDate, String customerId, double total, ArrayList<OrderDetailDTO> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.total = total;
        this.orderDetails = orderDetails;
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

    public ArrayList<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
