package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class Height extends DataSupport {
    private int id;
    private int kid;
    private double height;
    private int age;//月龄
    public void setKid(int kid) {
        this.kid = kid;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getKid() {
        return kid;
    }

    public double getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
