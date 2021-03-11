package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class BulkyQue1 extends DataSupport {
    private int id;
    private String que;
    private String a;
    private String b;
    private String c;
    private String d;
    private int minage;
    private int maxage;
    private int pass;
    private int ordernumber;

    public int getId(){
        return id;
    }
    public  void setId(int id){
        this.id=id;
    }
    public String getQue(){
        return que;
    }
    public void setQue(String que){
        this.que=que;
    }
    public String getA(){
        return a;
    }
    public void setA(String a){
        this.a=a;
    }
    public String getB(){
        return b;
    }
    public void setB(String b){
        this.b=b;
    }
    public String getC(){
        return c;
    }
    public void setC(String c){
        this.c=c;
    }
    public String getD(){
        return d;
    }
    public void setD(String d){
        this.d=d;
    }
    public int getMinage(){
        return  minage;
    }
    public void setMinage(int minage){
        this.minage=minage;
    }
    public int getMaxage(){
        return  maxage;
    }
    public void setMaxage(int maxage){
        this.maxage=maxage;
    }
    public int getPass(){
        return  pass;
    }
    public void setPass(int pass){
        this.pass=pass;
    }
    public int getOrdernumber(){
        return ordernumber;
    }
    public void setOrdernumber   (int ordernumber){
        this.ordernumber=ordernumber;
    }





}
