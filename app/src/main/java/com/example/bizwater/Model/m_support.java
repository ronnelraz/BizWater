package com.example.bizwater.Model;

public class m_support {
   public String id,name,img,flag;

    public m_support(String id, String name,String img, String flag) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.flag = flag;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
