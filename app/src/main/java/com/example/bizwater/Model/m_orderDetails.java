package com.example.bizwater.Model;

public class m_orderDetails {

    public String id,name,price,img,qty,sub,status;

    public m_orderDetails(String id, String name, String price, String img, String qty, String sub, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
        this.qty = qty;
        this.sub = sub;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
