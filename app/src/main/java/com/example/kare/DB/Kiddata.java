package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class Kiddata extends DataSupport {
    public static int selected=1;// 取值为ID 1 2 3



    private int id;//id 1 2 3

    private String name;
    private int year;
    private int month;
    private int day;
    private long year_age;
    private long month_age;
    private long day_age;
    private long age;//11111 "岁"+ "月" +"天"
    private int sex;//0为女 1为男
    private int bone_age;//骨龄
    private int height_area=0;//划分 为1  到6
    private int weight_area=0;
    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getAge() {
        return age;
    }

    public int getId(){
        return  id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year =year ;
    }
    public int getMonth(){
        return month;
    }
    public void setMonth(int month){
        this.month =month ;
    }
    public int getDay(){
        return day;
    }
    public void setDay(int day){
        this.day =day ;
    }

    public int getBone_age() {
        return bone_age;
    }

    public void setBone_age(int bone_age) {
        this.bone_age = bone_age;
    }

    public void setYear_age(long year_age){
        this.year_age = year_age;
    }
    public long getYear_age(){
        return this.year_age;
    }
    public void setMonth_age(long month_age){
        this.month_age = month_age;
    }
    public long getMonth_age(){
        return this.month_age;
    }
    public void setDay_age(long day_age){
        this.day_age = day_age;
    }
    public long getDay_age(){
        return this.day_age;
    }

    public int getHeight_area() {
        return height_area;
    }

    public void setHeight_area(int height_area) {
        this.height_area = height_area;
    }

    public int getWeight_area() {
        return weight_area;
    }

    public void setWeight_area(int weight_area) {
        this.weight_area = weight_area;
    }
}
