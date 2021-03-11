package com.example.kare.DB;

import org.litepal.crud.DataSupport;

public class Scores extends DataSupport {
    //孩子的id

    private int kidid;
    private double   scores;
    private String time;
    // 1 大动作 2 精细 3 cogni 4 lang 5 social
    private int type;
    private int age;
    public int getkidid(){
        return kidid;
    }
    public void setkidid(int kidid ){
        this.kidid = kidid;
    }
    public double getScores(){
        return scores;
    }
    public void setScores(double scores){
        this.scores = scores;
    }
   public String getTime(){
        return time;
   }
   public void setTime(String time){
        this.time=time;
   }
   public int  getType(){
        return  type;
   }
   public void setType(int type){
        this.type=type;
   }
    public int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age=age;
    }
}
