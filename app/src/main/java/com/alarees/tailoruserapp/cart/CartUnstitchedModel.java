package com.alarees.tailoruserapp.cart;

public class CartUnstitchedModel {
    String userId;
    String cartId;
    String orderId;
    String clothName;
    String price;
    String cuffType;
    String placketType;
    String pocketType;
    String collarType;
    String quantity;
    String clothimg;
    String orderdate;
    String totalpayment;
    String measurementId;
    String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMeasurementId() { return measurementId;}

    public void setMeasurementId(String measurementId) {this.measurementId = measurementId;}

    public String getOrderdate() { return orderdate; }

    public String getTotalpayment() { return totalpayment;}

    public void setTotalpayment(String totalpayment) { this.totalpayment = totalpayment; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getClothName() {
        return clothName;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCuffType() {
        return cuffType;
    }

    public void setCuffType(String cuffType) {
        this.cuffType = cuffType;
    }

    public String getPlacketType() {
        return placketType;
    }

    public void setPlacketType(String placketType) {
        this.placketType = placketType;
    }

    public String getPocketType() {
        return pocketType;
    }

    public void setPocketType(String pocketType) {
        this.pocketType = pocketType;
    }

    public String getCollarType() {
        return collarType;
    }

    public void setCollarType(String collarType) {
        this.collarType = collarType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setClothimg(String clothimg) {
        this.clothimg = clothimg;
    }

    public String getClothimg() {
        return clothimg;
    }

    public void setOrderdate(String orderdate) { this.orderdate = orderdate; }


}
