package com.example.kare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kare.DB.Kiddata;
import com.example.kare.Funct.Exam.GrowthtTestActivity;
import com.example.kare.Funct.HeightLineActivity;
import com.example.kare.Funct.Kid.SelectKidsActivity;
import com.example.kare.Funct.WeightLineActivity;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import static org.litepal.LitePalApplication.getContext;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout root;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root=findViewById(R.id.main_root);
        bottomNavigationView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_funct:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_corse:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_setting:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_account:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });
        BottomAdapter bottomAdapter = new BottomAdapter(this,getSupportFragmentManager());
        viewPager = findViewById(R.id.MainViewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int i, float v, int i1) {


            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(bottomAdapter);


    }
    public void SelectKids(View view){
        Intent intent = new Intent(this, SelectKidsActivity.class);
        startActivity(intent);
    }

    public void OpenTest(View view){
        Intent intent = new Intent(this, GrowthtTestActivity.class);
        startActivity(intent);
    }
    public void OpenHeight(View view){
        Intent intent = new Intent(this, HeightLineActivity.class);
        startActivity(intent);
    }
    public void OpenWeight(View view){
        Intent intent = new Intent(this, WeightLineActivity.class);
        startActivity(intent);
    }
}
