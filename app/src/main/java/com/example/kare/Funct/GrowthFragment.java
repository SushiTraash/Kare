package com.example.kare.Funct;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kare.DB.KidQue1;
import com.example.kare.DB.KidQue2;
import com.example.kare.DB.KidQue3;
import com.example.kare.DB.Kiddata;
import com.example.kare.Funct.Exam.GrowthtTestActivity;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrowthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrowthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView;
    //private Cursor selectedcursor= DataSupport.findBySQL("select * from kiddata");
    private int selectedkid;
    private TextView nextageview;
    private TextView ageview;
    private TextView nameview;

    public GrowthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrowthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrowthFragment newInstance(String param1, String param2) {
        GrowthFragment fragment = new GrowthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Cursor selectedcursor= DataSupport.findBySQL("select * from Kiddata");
        if(selectedcursor.moveToFirst()) {

            selectedkid = Kiddata.selected;
        }

        Log.v("selectedkid  ",""+selectedkid);
        mview =inflater.inflate(R.layout.fragment_growth, container, false);
        Button begintest =(Button)mview.findViewById(R.id.begintest);
        ageview = (TextView)mview.findViewById(R.id.kid_age);
        nameview  = (TextView)mview.findViewById(R.id.kid_name);
        nextageview=(TextView)mview.findViewById(R.id.next_test_age);
        begintest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                    OpenTest(getView());
            }
        });
        ClearOutdatedQue();
        ShowKid();
        return mview;
    }

    @Override
    public void onResume() {
        Cursor selectedcursor= DataSupport.findBySQL("select * from kiddata");
        if(selectedcursor.moveToFirst()) {
            selectedkid = Kiddata.selected;

        }
        Log.v("resumeselectedkid1  ",""+selectedkid);
        ClearOutdatedQue();
        ShowKid();
        super.onResume();
    }
    public void ShowKid(){
        if(Kiddata.selected!=0) {
        CalAge(Kiddata.selected);
    }
        Cursor quecur;
        if(Kiddata.selected==1) {
            quecur= DataSupport.findBySQL("SELECT * FROM KidQue1");
        }else if(Kiddata.selected==2){
            quecur= DataSupport.findBySQL("SELECT * FROM KidQue2");
        }else quecur= DataSupport.findBySQL("SELECT * FROM KidQue3");
        if(quecur.moveToFirst()){
            if(quecur.getInt(quecur.getColumnIndex("tested"))==1){
                long next_year=quecur.getLong(quecur.getColumnIndex("maxage"))/10000;
                long next_month=(quecur.getLong(quecur.getColumnIndex("maxage"))%10000)/100;
                long next_day=quecur.getLong(quecur.getColumnIndex("minage"))%100;
                Log.v("next_month1", "" + next_month);
                nextageview.setText("此阶段最后一次测试年龄为："+next_year+"岁"+next_month+"月 到"+ next_year+"岁"+next_month+"月 " +next_day+"天");
            }else {
                long next_year=quecur.getLong(quecur.getColumnIndex("minage"))/10000;
                long next_month=(quecur.getLong(quecur.getColumnIndex("minage"))%10000)/100;
                long next_day=quecur.getLong(quecur.getColumnIndex("minage"))%100;
                Log.v("next_month1", "" + next_month);
                nextageview.setText("下次测试年龄为："+next_year+"岁"+next_month+"月"+next_day+"天");
            }

        }else{
                nextageview.setText("下次测试年龄为：" + 0 + "岁" + 0 + "月" + 0 + "天");
        }

        Cursor kidcur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ? ",""+Kiddata.selected);

        if(kidcur.moveToFirst()) {
            String name = kidcur.getString(kidcur.getColumnIndex("name"));


            ageview.setText(kidcur.getLong(kidcur.getColumnIndex("year_age"))+"岁 "+kidcur.getLong(kidcur.getColumnIndex("month_age"))+" 月"+kidcur.getLong(kidcur.getColumnIndex("day_age"))+"天");
            nameview.setText(name);
        }
    }
    public void ClearOutdatedQue(){
        Cursor kidcur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+Kiddata.selected);
        kidcur.moveToFirst();
        if(Kiddata.selected==1){
            DataSupport.deleteAll(KidQue1.class,"maxage< ?",""+kidcur.getLong(kidcur.getColumnIndex("age")));
        }else if(Kiddata.selected==2){
            DataSupport.deleteAll(KidQue2.class,"maxage< ?",""+kidcur.getLong(kidcur.getColumnIndex("age")));
        }else {
            DataSupport.deleteAll(KidQue3.class,"maxage< ?",""+kidcur.getLong(kidcur.getColumnIndex("age")));
        }
    }
    public void OpenTest(View view){
        Cursor kidcur =DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+Kiddata.selected);
        Cursor quecur;

        if(kidcur.moveToFirst()) {
            if (Kiddata.selected == 1) {
                quecur = DataSupport.findBySQL("SELECT * FROM KidQue1 WHERE minage <= ? and maxage >= ? ", "" +kidcur.getLong(kidcur.getColumnIndex("age")), "" +kidcur.getLong(kidcur.getColumnIndex("age")));
                DataSupport.deleteAll(KidQue1.class, "maxage < ?", "" +kidcur.getLong(kidcur.getColumnIndex("age")));
            } else if (Kiddata.selected == 2) {
                quecur = DataSupport.findBySQL("SELECT * FROM KidQue2 WHERE minage <= ? and maxage >= ? ", "" + kidcur.getLong(kidcur.getColumnIndex("age")), "" + kidcur.getLong(kidcur.getColumnIndex("age")));
                DataSupport.deleteAll(KidQue1.class, "maxage < ?", "" +kidcur.getLong(kidcur.getColumnIndex("age")));
            } else {
                quecur = DataSupport.findBySQL("SELECT * FROM KidQue3 WHERE minage <= ? and maxage >= ? ", "" + kidcur.getLong(kidcur.getColumnIndex("age")), "" + kidcur.getLong(kidcur.getColumnIndex("age")));
                DataSupport.deleteAll(KidQue1.class, "maxage < ?", "" +kidcur.getLong(kidcur.getColumnIndex("age")));
            }
            if (quecur.moveToFirst()) {
                Intent intent = new Intent(getActivity(), GrowthtTestActivity.class);
                startActivity(intent);
            } else {
                if(kidcur.getLong(kidcur.getColumnIndex("age"))>516){
                    Toast ts = Toast.makeText(getActivity(), "全部测试已通过", Toast.LENGTH_LONG);
                    ts.show();
                }else {
                    Toast ts = Toast.makeText(getActivity(), "测试已通过", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }else{
            Toast ts = Toast.makeText(getActivity(), "未输入孩子信息", Toast.LENGTH_LONG);
            ts.show();
        }

    }
    public void CalAge(int selected) {
        Calendar cd = Calendar.getInstance();
        int yearnow = cd.get(Calendar.YEAR);
        int monthnow = cd.get(Calendar.MONTH);
        int daynow = cd.get(Calendar.DAY_OF_MONTH);
        int age = 0;
        long year_age;
        long month_age;
        long day_age;

        Cursor kidcur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?", "" + Kiddata.selected);
        if (kidcur.moveToFirst()) {
            String inputname = kidcur.getString(kidcur.getColumnIndex("name"));
            int year = kidcur.getInt(kidcur.getColumnIndex("year"));
            int month = kidcur.getInt(kidcur.getColumnIndex("month"));
            int day = kidcur.getInt(kidcur.getColumnIndex("day"));

            Kiddata kiddata = new Kiddata();
            if(year==0&&month==0&&day==0){
                kiddata.setToDefault("year_age");
                kiddata.setToDefault("month_age");
                kiddata.setToDefault("day_age");
                kiddata.updateAll("id = ?", "" + selected);
                return;
            }
            Calendar BirthCal = Calendar.getInstance();
            BirthCal.set(year, month - 1, day,0,0,0);//month 要 减一
            long days_pass = (cd.getTimeInMillis() - BirthCal.getTimeInMillis()) / (60 * 60 * 24 * 1000);
            long month_pass = days_pass / 30;
            long year_pass = month_pass / 12;

            year_age = year_pass;
            month_age = month_pass % 12;
            day_age = days_pass % 30;
            //Log.v("days_pass showkid", "CD year" + cd.get(Calendar.YEAR) + "  mon" + cd.get(Calendar.MONTH) + "   day " + cd.get(Calendar.DAY_OF_MONTH) +"  "+cd.getTimeInMillis()+  "days_pass"+days_pass +""+BirthCal.getTimeInMillis());
            Log.v("days_pass showkid", "" + cd.get(Calendar.YEAR) + "  " + cd.get(Calendar.MONTH) + "   " + cd.get(Calendar.DAY_OF_MONTH) + days_pass + "  year:  " + year_age + "month:  " + month_age + "day:  " + day_age);
            if(year_age==0){
                kiddata.setToDefault("year_age");
            }else {
                kiddata.setYear_age(year_age);
            }
            kiddata.setMonth_age(month_age);
            kiddata.setDay_age(day_age);
            kiddata.setAge(year_age*10000+month_age*100+day_age);
            Cursor agecur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?", "" + selected);
            if (agecur.moveToFirst()) {
                Log.v("year_age", "" + agecur.getLong(agecur.getColumnIndex("year_age")));
            }
            kiddata.updateAll("id = ?", "" + selected);


        }
    }

}
