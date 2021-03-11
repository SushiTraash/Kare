package com.example.kare.Funct.Kid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kare.DB.Kiddata;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

public class SelectKidsActivity extends AppCompatActivity {
    private RadioButton kid1button;
    private RadioButton kid2button;
    private RadioButton kid3button;
    private TextView kid1text;
    private TextView kid2text;
    private TextView kid3text;
    private Cursor cursor;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_kids);
        kid1button=(RadioButton)findViewById(R.id.kid1button);
        kid2button=(RadioButton)findViewById(R.id.kid2button);
        kid3button=(RadioButton)findViewById(R.id.kid3button);
        sure=(Button)findViewById(R.id.SureKid);
        kid1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kid2button.setChecked(false);
                kid3button.setChecked(false);
                Kiddata.selected=1;
            }
        });
        kid2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kid1button.setChecked(false);
                kid3button.setChecked(false);
                Kiddata.selected=2;
            }
        });
        kid3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kid2button.setChecked(false);
                kid1button.setChecked(false);
                Kiddata.selected=3;
            }
        });

        kid1text =(TextView)findViewById(R.id.kid1text);
        kid2text =(TextView)findViewById(R.id.kid2text);
        kid3text =(TextView)findViewById(R.id.kid3text);
        kid1text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectKidsActivity.this, EditKidsActivity.class);
                intent.putExtra("kidid",1);
                startActivity(intent);
            }
        });
        kid2text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectKidsActivity.this, EditKidsActivity.class);
                intent.putExtra("kidid",2);
                startActivity(intent);
            }
        });
        kid3text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectKidsActivity.this, EditKidsActivity.class);
                intent.putExtra("kidid",3);
                startActivity(intent);
            }
        });
        ShowSelect();
        ShowKid();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            finish();
            }
        });
    }
    //每次重新加入活动刷新Select界面孩子的信息
    @Override
    protected void onRestart() {
        cursor = DataSupport.findBySQL("select * from kiddata");
        if(cursor.moveToFirst()) {
            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid1text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid1text.setText("点击此处输入孩子信息");
        }
        if(cursor.moveToNext()){

            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid2text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid2text.setText("点击此处输入孩子信息");

        }if(cursor.moveToNext()){

            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid3text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid3text.setText("点击此处输入孩子信息");

        }
        ShowSelect();
        ShowKid();
        super.onRestart();
    }
    //展示孩子信息
    public void ShowKid(){
        cursor = DataSupport.findBySQL("select * from kiddata");
        if(cursor.moveToFirst()) {
            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid1text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid1text.setText("点击此处输入孩子信息");
        }
        if(cursor.moveToNext()){

            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid2text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid2text.setText("点击此处输入孩子信息");

        }if(cursor.moveToNext()){

            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kid3text.setText(cursor.getString(cursor.getColumnIndex("name")));
            }else kid3text.setText("点击此处输入孩子信息");

        }
    }
    public void ShowSelect(){

        Cursor cursor1 = DataSupport.findBySQL("select * from kiddata");
        if(cursor1.moveToFirst()){
            if(Kiddata.selected==1){
                kid1button.setChecked(true);
            }else if(Kiddata.selected==2){
                kid2button.setChecked(true);
            }else if(Kiddata.selected==3){
                kid3button.setChecked(true);
            }
        }
    }
}
