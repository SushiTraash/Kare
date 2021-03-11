package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class Weight extends DataSupport {
    private int id;
    private int kid;
    private double weight;
    private int age;
    public void setKid(int kid) {
        this.kid = kid;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getKid() {
        return kid;
    }

    public double getWeight() {
        return weight;
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