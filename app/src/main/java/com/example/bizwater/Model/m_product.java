package com.example.bizwater.Model;

public class m_product {

    public String id,name,price,qty,des,img,flg;

    public m_product(String id, String name, String price, String qty, String des, String img, String flg) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.des = des;
        this.img = img;
        this.flg = flg;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }
}
