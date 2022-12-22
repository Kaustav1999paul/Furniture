package com.example.furniture.Models;

public class Cart {

    private String ID, image,name, Date, Qty, category,price, cartID;

    public Cart() {
    }

    public Cart(String ID, String image, String name, String date, String qty, String category, String price, String cartID) {
        this.ID = ID;
        this.image = image;
        this.name = name;
        Date = date;
        Qty = qty;
        this.category = category;
        this.price = price;
        this.cartID = cartID;
    }

    public String getCartID() {
        return cartID;
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
