package com.precize.orders.model;

public class Item {
    private String itemId;
    private int qty;

    public Item(String itemId, int qty) {
        this.itemId = itemId;
        this.qty = qty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", qty=" + qty +
                '}';
    }
}
