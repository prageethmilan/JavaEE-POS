package dto;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:32 AM
 **/
public class ItemDTO {
    private String code;
    private String name;
    private double unitPrice;
    private int qty;

    public ItemDTO() {
    }

    public ItemDTO(String code, String name, double unitPrice, int qty) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
