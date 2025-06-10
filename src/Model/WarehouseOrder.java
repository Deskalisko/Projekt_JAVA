package Model;

import java.util.Date;
import java.util.List;

public class WarehouseOrder {
    private int id;
    private int productId;
    private int quantity;
    private Date orderDate;
    private String supplier;
    private double totalCost;
    private List<WarehouseOrderItem> items;

    public WarehouseOrder(int id, int productId, int quantity, Date orderDate, String supplier, double totalCost) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate != null ? new Date(orderDate.getTime()) : null;
        this.supplier = supplier != null ? supplier : "";
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public List<WarehouseOrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<WarehouseOrderItem> items) {
        this.items = items;
    }
}