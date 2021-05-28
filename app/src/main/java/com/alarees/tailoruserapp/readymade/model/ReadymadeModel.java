package com.alarees.tailoruserapp.readymade.model;

import java.io.Serializable;

public class ReadymadeModel implements Serializable {
    String imgUrl;
    String itemName;
    String itemDes;
    String itemPrice;
    String smallQty;
    String mediumQty;
    String largeQty;
    String totalQty;
    String itemid;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDes() {
        return itemDes;
    }

    public void setItemDes(String itemDes) {
        this.itemDes = itemDes;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getSmallQty() {
        return smallQty;
    }

    public void setSmallQty(String smallQty) {
        this.smallQty = smallQty;
    }

    public String getMediumQty() {
        return mediumQty;
    }

    public void setMediumQty(String mediumQty) {
        this.mediumQty = mediumQty;
    }

    public String getLargeQty() {
        return largeQty;
    }

    public void setLargeQty(String largeQty) {
        this.largeQty = largeQty;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }
}
