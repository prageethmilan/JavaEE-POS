package entity;

/**
 * @author : M-Prageeth
 * @created : 19/05/2022 - 10:18 AM
 **/
public class OrderDetail {
    private String orderId;
    private String itemCode;
    private String itemName;
    private double unitPrice;
    private int buyQty;
    private double total;

    public OrderDetail() {
    }

    public OrderDetail(String orderId, String itemCode, String itemName, double unitPrice, int buyQty, double total) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.buyQty = buyQty;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(int buyQty) {
        this.buyQty = buyQty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
