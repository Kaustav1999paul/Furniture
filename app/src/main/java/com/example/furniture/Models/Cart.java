package com.example.furniture.Models;

public class Cart {

    private String ID, image,name, Date, Qty, category,price, cartID, state, is_Cancelled;

    public Cart() {
    }

    public Cart(String ID, String image, String name, String date, String qty, String category, String price, String cartID, String state, String is_Cancelled) {
        this.ID = ID;
        this.is_Cancelled = is_Cancelled;
        this.state = state;
        this.image = image;
        this.name = name;
        Date = date;
        Qty = qty;
        this.category = category;
        this.price = price;
        this.cartID = cartID;
    }

    public String getIs_Cancelled() {
        return is_Cancelled;
    }

    public void setIs_Cancelled(String is_Cancelled) {
        this.is_Cancelled = is_Cancelled;
    }

    public String getCartID() {
        return cartID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
