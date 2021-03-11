package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class KidQue3 extends DataSupport{
    static int pass;
    private int id;
    private String que;
    private int tested;
    private int answer;
    private int weight;
    private long minage;
    private long maxage;

    public int getTested() {
        return tested;
    }

    public void setTested(int tested) {
        this.tested = tested;
    }

    public void setMinage(long minage) {
        this.minage = minage;
    }

    public void setMaxage(long maxage) {
        this.maxage = maxage;
    }

    public long getMinage() {
        return minage;
    }

    public long getMaxage() {
        return maxage;
    }

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



    public void setWeight(int weight){
        this.weight=weight;
    }
    public int getWeight (){
        return this.weight;
    }
    public void setAnswer(int answer){
        this.answer=answer;
    }
    public int getAnswer (){
        return this.answer;
    }


}