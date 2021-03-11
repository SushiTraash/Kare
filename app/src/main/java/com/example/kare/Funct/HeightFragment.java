package com.example.kare.Funct;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kare.DB.Height;
import com.example.kare.DB.Kiddata;
import com.example.kare.DB.Weight;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeightFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Cursor kidcur ;
    private Cursor heightcur;
    private Cursor weightcur;
    private int mage;
    private int msex;
    private int mba;
    private String mname;
    private int mheight_area;
    private int mweight_area;
    private LinearLayout root_view;
    private EditText heightview;
    private EditText weightview;
    private EditText baview;
    private Button ba_sure;
    private Button height_sure;
    private Button weight_sure;
    private TextView agetext;
    private TextView ageview;
    private TextView nameview;
    private TextView sexview ;
    private TextView height_pre;
    private TextView weight_pre;
    private LinearLayout input;
    public HeightFragment() {
        // Required empty public constructor
    }

    /**
            * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
             * @param param1 Parameter 1.
            * @param param2 Parameter 2.
            * @return A new instance of fragment HeightFragment.
            */
    // TODO: Rename and change types and number of parameters
    public static HeightFragment newInstance(String param1, String param2) {
        HeightFragment fragment = new HeightFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mview= inflater.inflate(R.layout.fragment_height, container, false);
        root_view=mview.findViewById(R.id.root_view);
        ageview=mview.findViewById(R.id.kid_age);
        nameview=mview.findViewById(R.id.kid_name);
        sexview = mview.findViewById(R.id.kid_sex);
        heightview=mview.findViewById(R.id.input_height);
        weightview=mview.findViewById(R.id.input_weight);
        height_sure=mview.findViewById(R.id.height_sure);
        weight_sure = mview.findViewById(R.id.weight_sure);
        agetext= mview.findViewById(R.id.age_text);
        height_pre=mview.findViewById(R.id.height_pre);
        weight_pre=mview.findViewById(R.id.weight_pre);
        input=mview.findViewById(R.id.invisible_root);
        GetInfo();
        root_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null){
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });

        //点击确定的时候先确定是否改变划分之后在提醒
        height_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double height;
                int newarea=0;
                if(!(heightview.getText().toString().isEmpty())) {
                    height = Double.parseDouble(heightview.getText().toString());
                    DataSupport.deleteAll(Height.class, "kid = ? and age = ?", "" + Kiddata.selected, "" + mage);
                    Height height1 = new Height();
                    height1.setHeight(height);
                    height1.setAge(mage);
                    height1.setKid(Kiddata.selected);
                    height1.save();
                    if (msex == 0) {
                        newarea=Divide_height_girl();
                    } else {
                        newarea = Divide_height_boy();
                    }
                    if (mheight_area != 0) {
                        if (mheight_area != newarea) {
                            //TODO: WARNING
                            Toast ts = Toast.makeText(getActivity(), "身高划分改变，请前往医院检查孩子发育情况", Toast.LENGTH_LONG);
                            ts.show();
                            Kiddata kiddata = new Kiddata();
                            kiddata.setHeight_area(newarea);
                            kiddata.updateAll("id = ?", "" + Kiddata.selected);
                            GetInfo();

                        }
                        Log.v("area", "  " + mheight_area);
                    } else {
                        Kiddata kiddata = new Kiddata();
                        kiddata.setHeight_area(newarea);
                        kiddata.updateAll("id = ?", "" + Kiddata.selected);
                        GetInfo();
                        Log.v("area", "  " + mheight_area);
                    }
                    InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    ShowKid();
                }
            }
        });

        weight_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double weight;
                int newarea =0;
                String weight_str=weightview.getText().toString();
                if(!weightview.getText().toString().isEmpty()) {
                    weight = Double.parseDouble(weightview.getText().toString());
                    DataSupport.deleteAll(Weight.class, "kid = ? and age = ?", "" + Kiddata.selected, "" + mage);
                    Weight weight1 = new Weight();
                    weight1.setWeight(weight);
                    weight1.setAge(mage);
                    weight1.setKid(Kiddata.selected);
                    weight1.save();
                    if (msex == 0) {
                        newarea=Divide_weight_girl();
                    } else {
                        newarea=Divide_weight_boy();
                    }
                    if (mweight_area != 0) {
                        if (mweight_area < newarea) {
                            //TODO: WARNING
                            Toast ts = Toast.makeText(getActivity(), "体重划分上升，请前往医院检查孩子发育情况", Toast.LENGTH_LONG);
                            ts.show();
                            Kiddata kiddata = new Kiddata();
                            kiddata.setHeight_area(newarea);
                            kiddata.updateAll("id = ?", "" + Kiddata.selected);
                            GetInfo();
                            Log.v("area1", "  " + mweight_area);
                        }
                        Log.v("area1", "  " + mweight_area);
                    } else {
                        Kiddata kiddata = new Kiddata();
                        kiddata.setWeight_area(newarea);
                        kiddata.updateAll("id = ?", "" + Kiddata.selected);
                        GetInfo();
                        Log.v("area2", "  " + mweight_area);
                    }
                    InputMethodManager manager = ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    ShowKid();
                }

            }
        });

        ShowKid();
        return mview;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetInfo();
        ShowKid();
    }

    public int Divide_weight_boy(){
        int divstage =-1;
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] weight3=new double[]{10.22,11.11,11.94,12.73,13.52,14.37,15.26,16.09,16.08,17.53,18.48,19.43,20.32,21.18,22.04,22.95,23.89,24.96,26.21,27.59,29.09,30.74,32.82,35.03,37.36,39.53,41.43,43.05,44.28,45.30,46.04,47.01};
        double[] weight10=new double[]{10.90,11.85,12.74,13.58,14.43,15.35,16.33,17.26,18.06,18.92,20.04,21.17,22.24,23.28,24.31,25.42,26.55,27.83,29.33,30.97,32.77,34.71,37.04,39.42,41.80,43.94,45.77,47.31,48.47,49.42,50.11,51.02};
        double[] weight50=new double[]{12.54,13.64,14.65,15.63,16.64,17.75,18.98,20.18,21.26,22.45,24.06,25.27,27.33,28.91,30.46,32.09,33.74,35.58,37.69,39.98,42.49,45.13,48.08,50.85,53.37,55.43,57.08,58.39,59.35,60.12,60.68,61.40};
        double[] weight90=new double[]{14.64,15.73,16.92,18.08,19.29,20.67,22.23,23.81,25.29,27.00,29.35,31.84,34.31,36.74,39.08,41.49,43.85,46.40,49.20,52.21,55.50,58.90,62.57,65.80,68.53,70.55,72.00,73.03,73.74,74.25,74.62,75.08};
        double[] weight97=new double[]{15.46,16.83,18.12,19.38,20.71,22.24,24.00,25.81,27.55,29.57,32.41,35.45,38.49,41.49,44.35,47.24,50.01,52.93,56.07,59.40,63.04,66.81,70.83,74.33,77.20,79.24,80.60,81.49,82.05,82.44,82.70,83.00};
        double weight=0;
        GetInfo();
        weightcur=DataSupport.findBySQL("SELECT * FROM Weight WHERE kid = ? and age =?",""+Kiddata.selected,""+mage);
        if(weightcur.moveToFirst()) {
            weight = weightcur.getDouble(weightcur.getColumnIndex("weight"));
        }else return divstage;
        double norm_age=(double) mage/12;
        if(norm_age - mage/12>=0.49){
            norm_age=mage/12+0.5;
        }else{
            norm_age=mage/12;
        }
        Log.v("norm_age","  "+norm_age);
        for(int i=0;i<32;i++){
            if(norm_age<=age[i]){
                if(weight<weight3[i]){
                    divstage=1;
                }else if(weight<weight10[i]) {
                    divstage=2;
                }else if(weight<weight50[i]) {
                    divstage=3;
                }else if(weight<weight90[i]) {
                    divstage=4;
                }else if(weight<weight97[i]) {
                    divstage=5;
                }else {
                    divstage=6;
                }
                Log.v("divstage","  "+divstage);
                break;

            }
        }
        return  divstage;
    }
    public int Divide_weight_girl(){
        int divstage =-1;
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] weight3=new double[]{10.22,11.11,11.94,12.73,13.52,14.37,15.26,16.09,16.08,17.53,18.48,19.43,20.32,21.18,22.04,22.95,23.89,24.96,26.21,27.59,29.09,30.74,32.82,35.03,37.36,39.53,41.43,43.05,44.28,45.30,46.04,47.01};
        double[] weight10=new double[]{10.90,11.85,12.74,13.58,14.43,15.35,16.33,17.26,18.06,18.92,20.04,21.17,22.24,23.28,24.31,25.42,26.55,27.83,29.33,30.97,32.77,34.71,37.04,39.42,41.80,43.94,45.77,47.31,48.47,49.42,50.11,51.02};
        double[] weight50=new double[]{12.54,13.64,14.65,15.63,16.64,17.75,18.98,20.18,21.26,22.45,24.06,25.27,27.33,28.91,30.46,32.09,33.74,35.58,37.69,39.98,42.49,45.13,48.08,50.85,53.37,55.43,57.08,58.39,59.35,60.12,60.68,61.40};
        double[] weight90=new double[]{14.64,15.73,16.92,18.08,19.29,20.67,22.23,23.81,25.29,27.00,29.35,31.84,34.31,36.74,39.08,41.49,43.85,46.40,49.20,52.21,55.50,58.90,62.57,65.80,68.53,70.55,72.00,73.03,73.74,74.25,74.62,75.08};
        double[] weight97=new double[]{15.46,16.83,18.12,19.38,20.71,22.24,24.00,25.81,27.55,29.57,32.41,35.45,38.49,41.49,44.35,47.24,50.01,52.93,56.07,59.40,63.04,66.81,70.83,74.33,77.20,79.24,80.60,81.49,82.05,82.44,82.70,83.00};
        double weight=0;
        GetInfo();
        weightcur=DataSupport.findBySQL("SELECT * FROM weight WHERE kid = ? and age =?",""+Kiddata.selected,""+mage);
        if(weightcur.moveToFirst()) {
            weight = weightcur.getDouble(weightcur.getColumnIndex("weight"));
        }else return divstage;
        double norm_age=(double) mage/12;
        if(norm_age - mage/12>=0.49){
            norm_age=mage/12+0.5;
        }else{
            norm_age=mage/12;
        }
        Log.v("norm_age","  "+norm_age);
        for(int i=0;i<32;i++){
            if(norm_age<=age[i]){
                if(weight<weight3[i]){
                    divstage=1;
                }else if(weight<weight10[i]) {
                    divstage=2;
                }else if(weight<weight50[i]) {
                    divstage=3;
                }else if(weight<weight90[i]) {
                    divstage=4;
                }else if(weight<weight97[i]) {
                    divstage=5;
                }else {
                    divstage=6;
                }
                Log.v("divstage","  "+divstage);
                break;

            }
        }
        return  divstage;
    }
    public int Divide_height_boy(){
        int divstage =-1;
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] height3=new double[]{82.1,86.4,89.7,93.4,96.7,100,103.3,106.4,109.1,111.7,114.6,119.4,119.9,122.3,124.6,126.7,128.7,130.7,132.9,135.3,138.1,141.1,145,148.8,152.3,155.3,157.5,159.1,159.9,160.5,160.9,161.3};
        double[] height10=new double[]{84.1,88.6,91.9,95.7,99.1,102.4,105.8,109.0,111.8,114.5,117.6,120.5,123.1,125.6,128.0,130.3,132.3,134.5,136.6,139.5,142.5,145.7,149.6,153.3,156.7,159.4,161.4,162.9,163.6,164.2,164.5,164.9};
        double[] height50=new double[]{88.5,93.3,96.8,100.6,104.1,107.7,111.3,114.7,117.7,120.7,124.0,127.1,130.0,132.7,135.4,137.9,140.2,142.6,145.3,148.4,151.9,155.6,159.5,163.0,165.9,168.2,169.8,170.0,171.6,172.1,172.3,172.7};
        double[] height90=new double[]{93.1,98.2,101.8,105.7,109.3,113.1,116.9,120.5,123.7,126.9,130.5,133.9,137.1,140.1,142.9,145.7,148.2,150.9,154.0,157.4,161.5,165.5,169.5,172.7,175.1,176.9,178.2,179.1,179.5,179.9,180.1,180.4};
        double[] height97=new double[]{95.3,100.5,104.1,108.1,111.8,115.7,119.6,123.3,126.6,129.9,133.7,137.2,140.4,143.6,146.5,149.4,152.0,154.9,158.1,161.7,166.0,170.2,174.2,177.2,179.4,181.0,182.0,182.8,183.2,183.5,183.7,183.9};
        double height=0;
        GetInfo();
        heightcur=DataSupport.findBySQL("SELECT * FROM Height WHERE kid = ? and age =?",""+Kiddata.selected,""+mage);
        if(heightcur.moveToFirst()) {
            height = heightcur.getDouble(heightcur.getColumnIndex("height"));
        }else return divstage;
        double norm_age=(double) mage/12;
        if(norm_age - mage/12>=0.49){
            norm_age=mage/12+0.5;
        }else{
            norm_age=mage/12;
        }
        Log.v("norm_age","  "+norm_age);
        for(int i=0;i<32;i++){
            if(norm_age<=age[i]){
                    if(height<height3[i]){
                        divstage=1;
                    }else if(height<height10[i]) {
                        divstage=2;
                    }else if(height<height50[i]) {
                        divstage=3;
                    }else if(height<height90[i]) {
                        divstage=4;
                    }else if(height<height97[i]) {
                        divstage=5;
                    }else {
                        divstage=6;
                    }
                    Log.v("divstage","  "+divstage);
                    break;

            }
        }
        return  divstage;
    }
    public int Divide_height_girl(){
        int divstage =-1;
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] height3=new double[]{80.9,85.2,88.6,92.4,95.8,99.2,102.3,105.4,108.1,110.6,113.3,116.0,118.5,121.0,123.3,125.7,128.3,131.1,134.2,137.2,140.2,142.9,145.0,146.7,147.9,148.9,149.5,149.9,149.8,149.9,150.1,150.4};
        double[] height10=new double[]{82.9,87.4,90.8,94.6,98.1,101.5,104.8,108.0,110.8,113.4,116.2,119.0,121.6,124.2,126.7,129.3,132.1,135.0,138.2,141.2,144.1,146.6,148.6,150.2,151.3,152.2,152.8,153.1,153.1,153.2,153.4,153.7};
        double[] height50=new double[]{87.2,92.1,95.6,99.4,103.1,106.7,110.2,113.5,116.6,119.4,122.5,125.6,128.5,131.3,134.1,137.0,140.1,143.3,146.6,149.7,152.4,154.6,156.3,157.6,158.6,159.4,159.8,160.1,160.1,160.2,160.3,160.6};
        double[] height90=new double[]{91.7,97.0,100.5,104.4,108.2,112.1,115.7,119.3,122.5,125.6,129.0,132.3,135.4,138.5,141.6,144.8,148.2,151.6,155.2,158.2,160.7,162.7,164.0,165.1,165.9,166.5,166.8,167.1,167.1,167.1,167.3,167.6};
        double[] height97=new double[]{93.9,99.3,102.9,106.8,110.6,114.7,118.4,122.0,125.4,128.6,132.1,135.5,138.7,141.9,145.1,148.5,152.0,155.6,159.2,162.1,164.5,166.3,167.6,168.6,169.3,169.8,170.1,170.3,170.3,170.4,170.5,170.7};
        double height=0;
        GetInfo();
        heightcur=DataSupport.findBySQL("SELECT * FROM Height WHERE kid = ? and age =?",""+Kiddata.selected,""+mage);
        if(heightcur.moveToFirst()) {
            height = heightcur.getDouble(heightcur.getColumnIndex("height"));
        }else return divstage;
        double norm_age=(double) mage/12;
        if(norm_age - mage/12>=0.49){
            norm_age=mage/12+0.5;
        }else{
            norm_age=mage/12;
        }
        Log.v("norm_age","  "+norm_age);
        for(int i=0;i<32;i++){
            if(norm_age<=age[i]){
                if(height<height3[i]){
                    divstage=1;
                }else if(height<height10[i]) {
                    divstage=2;
                }else if(height<height50[i]) {
                    divstage=3;
                }else if(height<height90[i]) {
                    divstage=4;
                }else if(height<height97[i]) {
                    divstage=5;
                }else {
                    divstage=6;
                }
                Log.v("divstage","  "+divstage);

                break;
            }
        }
        return  divstage;
    }
    public void GetInfo(){
        kidcur= DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ? ",""+ Kiddata.selected);
        if(kidcur.moveToFirst()) {
            mba=kidcur.getInt(kidcur.getColumnIndex("bone_age"));
            mname = kidcur.getString(kidcur.getColumnIndex("name"));
            msex= kidcur.getInt(kidcur.getColumnIndex("sex"));
            if(mba!=0) {
                mage = mba;
            }else mage = (int) (kidcur.getLong(kidcur.getColumnIndex("year_age")) * 12 + kidcur.getLong(kidcur.getColumnIndex("month_age")));
            mheight_area=kidcur.getInt(kidcur.getColumnIndex("height_area"));
            mweight_area=kidcur.getInt(kidcur.getColumnIndex("weight_area"));
        }
    }
    public void ShowKid(){

            if(mage<24){
                input.setVisibility(View.GONE);
            }else{
                input.setVisibility(View.VISIBLE);
            }

            if(msex==0){
                sexview.setText("女");
            }else sexview.setText("男");
            nameview.setText(mname);
            Cursor heightcur =DataSupport.findBySQL("SELECT * FROM Height WHERE kid = ? AND age =?",""+Kiddata.selected,""+mage);
            Cursor weightcur =DataSupport.findBySQL("SELECT * FROM Weight WHERE kid = ? AND age =?",""+Kiddata.selected,""+mage);
            if(heightcur.moveToFirst()){
                heightview.setText(""+heightcur.getDouble(heightcur.getColumnIndex("height")));
            }else{
                heightview.setText("");
            }
            if(weightcur.moveToFirst()){
                weightview.setText(""+weightcur.getDouble(weightcur.getColumnIndex("weight")));
            }else{
                weightview.setText("");
            }
            if(mba!=0){
                agetext.setText("骨龄:");
                ageview.setText(mba/12+"岁 "+mba%12+" 月");

            }else{
                agetext.setText("年龄:");
                ageview.setText(kidcur.getLong(kidcur.getColumnIndex("year_age"))+"岁 "+kidcur.getLong(kidcur.getColumnIndex("month_age"))+" 月"+kidcur.getLong(kidcur.getColumnIndex("day_age"))+"天");

            }
            Predict();
    }
    public void Predict(){
        String Strheight_pre;
        String Strweight_pre;
        int i;
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] height3;
        double[] height10;
        double[] height50;
        double[] height90;
        double[] height97;
        double[] weight3;
        double[] weight10;
        double[] weight50;
        double[] weight90;
        double[] weight97;

        if(msex==0){
                height3=new double[]{80.9,85.2,88.6,92.4,95.8,99.2,102.3,105.4,108.1,110.6,113.3,116.0,118.5,121.0,123.3,125.7,128.3,131.1,134.2,137.2,140.2,142.9,145.0,146.7,147.9,148.9,149.5,149.9,149.8,149.9,150.1,150.4};
                height10=new double[]{82.9,87.4,90.8,94.6,98.1,101.5,104.8,108.0,110.8,113.4,116.2,119.0,121.6,124.2,126.7,129.3,132.1,135.0,138.2,141.2,144.1,146.6,148.6,150.2,151.3,152.2,152.8,153.1,153.1,153.2,153.4,153.7};
                height50=new double[]{87.2,92.1,95.6,99.4,103.1,106.7,110.2,113.5,116.6,119.4,122.5,125.6,128.5,131.3,134.1,137.0,140.1,143.3,146.6,149.7,152.4,154.6,156.3,157.6,158.6,159.4,159.8,160.1,160.1,160.2,160.3,160.6};
                height90=new double[]{91.7,97.0,100.5,104.4,108.2,112.1,115.7,119.3,122.5,125.6,129.0,132.3,135.4,138.5,141.6,144.8,148.2,151.6,155.2,158.2,160.7,162.7,164.0,165.1,165.9,166.5,166.8,167.1,167.1,167.1,167.3,167.6};
                height97=new double[]{93.9,99.3,102.9,106.8,110.6,114.7,118.4,122.0,125.4,128.6,132.1,135.5,138.7,141.9,145.1,148.5,152.0,155.6,159.2,162.1,164.5,166.3,167.6,168.6,169.3,169.8,170.1,170.3,170.3,170.4,170.5,170.7};
                weight3=new double[]{9.76,10.65,11.50,12.32,13.10,13.89,14.64,15.39,16.10,16.80,17.58,18.39,19.20,20.02,20.93,21.89,22.98,24.22,25.74,27.43,29.33,31.22,33.09,34.82,36.38,37.71,38.73,39.51,39.96,40.29,40.44,40.71};
                weight10=new double[]{10.39,11.35,12.27,13.14,13.99,14.85,15.68,16.52,17.32,18.12,18.01,18.95,20.89,21.88,22.93,24.08,25.36,26.80,28.53,30.39,32.42,34.39,36.29,38.01,39.55,40.84,41.83,42.58,43.01,43.32,43.37,43.73};
                weight50=new double[]{11.92,13.05,14.13,15.16,16.17,17.22,18.26,19.33,20.37,21.44,22.64,23.93,25.25,26.67,28.19,29.87,31.76,33.80,36.10,38.40,40.77,42.89,44.79,46.42,47.83,48.97,49.82,50.45,50.81,51.07,51.20,51.41};
                weight90=new double[]{13.74,15.08,16.36,17.59,18.81,20.10,21.41,22.81,24.19,25.62,27.28,29.08,30.95,33.00,35.26,37.79,40.63,43.61,46.78,49.73,52.49,54.71,54.46,57.81,58.88,59.70,60.28,60.69,60.91,61.07,61.15,61.22};
                weight97=new double[]{14.71,16.16,17.55,18.89,20.24,21.67,23.14,24.72,26.30,27.96,29.89,32.01,34.23,36.69,39.41,42.51,45.59,49.59,53.33,56.67,59.64,61.86,63.45,64.55,65.36,65.93,66.30,66.55,66.59,66.76,66.82,66.89};

            }else {
                 height3=new double[]{82.1,86.4,89.7,93.4,96.7,100,103.3,106.4,109.1,111.7,114.6,119.4,119.9,122.3,124.6,126.7,128.7,130.7,132.9,135.3,138.1,141.1,145,148.8,152.3,155.3,157.5,159.1,159.9,160.5,160.9,161.3};
                 height10=new double[]{84.1,88.6,91.9,95.7,99.1,102.4,105.8,109.0,111.8,114.5,117.6,120.5,123.1,125.6,128.0,130.3,132.3,134.5,136.6,139.5,142.5,145.7,149.6,153.3,156.7,159.4,161.4,162.9,163.6,164.2,164.5,164.9};
                 height50=new double[]{88.5,93.3,96.8,100.6,104.1,107.7,111.3,114.7,117.7,120.7,124.0,127.1,130.0,132.7,135.4,137.9,140.2,142.6,145.3,148.4,151.9,155.6,159.5,163.0,165.9,168.2,169.8,170.0,171.6,172.1,172.3,172.7};
                 height90=new double[]{93.1,98.2,101.8,105.7,109.3,113.1,116.9,120.5,123.7,126.9,130.5,133.9,137.1,140.1,142.9,145.7,148.2,150.9,154.0,157.4,161.5,165.5,169.5,172.7,175.1,176.9,178.2,179.1,179.5,179.9,180.1,180.4};
                 height97=new double[]{95.3,100.5,104.1,108.1,111.8,115.7,119.6,123.3,126.6,129.9,133.7,137.2,140.4,143.6,146.5,149.4,152.0,154.9,158.1,161.7,166.0,170.2,174.2,177.2,179.4,181.0,182.0,182.8,183.2,183.5,183.7,183.9};
                 weight3=new double[]{10.22,11.11,11.94,12.73,13.52,14.37,15.26,16.09,16.08,17.53,18.48,19.43,20.32,21.18,22.04,22.95,23.89,24.96,26.21,27.59,29.09,30.74,32.82,35.03,37.36,39.53,41.43,43.05,44.28,45.30,46.04,47.01};
                 weight10=new double[]{10.90,11.85,12.74,13.58,14.43,15.35,16.33,17.26,18.06,18.92,20.04,21.17,22.24,23.28,24.31,25.42,26.55,27.83,29.33,30.97,32.77,34.71,37.04,39.42,41.80,43.94,45.77,47.31,48.47,49.42,50.11,51.02};
                 weight50=new double[]{12.54,13.64,14.65,15.63,16.64,17.75,18.98,20.18,21.26,22.45,24.06,25.27,27.33,28.91,30.46,32.09,33.74,35.58,37.69,39.98,42.49,45.13,48.08,50.85,53.37,55.43,57.08,58.39,59.35,60.12,60.68,61.40};
                 weight90=new double[]{14.64,15.73,16.92,18.08,19.29,20.67,22.23,23.81,25.29,27.00,29.35,31.84,34.31,36.74,39.08,41.49,43.85,46.40,49.20,52.21,55.50,58.90,62.57,65.80,68.53,70.55,72.00,73.03,73.74,74.25,74.62,75.08};
                 weight97=new double[]{15.46,16.83,18.12,19.38,20.71,22.24,24.00,25.81,27.55,29.57,32.41,35.45,38.49,41.49,44.35,47.24,50.01,52.93,56.07,59.40,63.04,66.81,70.83,74.33,77.20,79.24,80.60,81.49,82.05,82.44,82.70,83.00};
            }
        double norm_age=(double) mage/12;
        if(norm_age - mage/12>=0.49){
            norm_age=mage/12+0.5;
        }else{
            norm_age=mage/12;
        }
        for( i=0;i<32;i++){
            if(norm_age==age[i]){
                break;
            }
        }
        if(mheight_area==1){
            Strheight_pre="预计"+age[i+1]+"岁时，身高小于"+height3[i+1]+"cm";
        }else if(mheight_area==2){
            Strheight_pre="预计"+age[i+1]+"岁时，身高小于"+height10[i+1]+"cm,大于"+height3[i+1]+"cm";
        }else if(mheight_area==3){
            Strheight_pre="预计"+age[i+1]+"岁时，身高小于"+height50[i+1]+"cm,大于"+height10[i+1]+"cm";
        }else if(mheight_area==4){
            Strheight_pre="预计"+age[i+1]+"岁时，身高小于"+height90[i+1]+"cm,大于"+height50[i+1]+"cm";
        }else if(mheight_area==5){
            Strheight_pre="预计"+age[i+1]+"岁时，身高小于"+height97[i+1]+"cm,大于"+height90[i+1]+"cm";
        }else if(mheight_area==6){
            Strheight_pre="预计"+age[i+1]+"岁时，身高大于"+height97[i+1]+"cm";
        }else {
            Strheight_pre="暂时无法预测";
        }
        if(mweight_area==1){
            Strweight_pre="预计"+age[i+1]+"岁时，体重小于"+weight3[i+1]+"kg";
        }else if(mweight_area==2){
            Strweight_pre="预计"+age[i+1]+"岁时，体重小于"+weight10[i+1]+"kg,大于"+weight3[i+1]+"kg";
        }else if(mweight_area==3){
            Strweight_pre="预计"+age[i+1]+"岁时，体重小于"+weight50[i+1]+"kg,大于"+weight10[i+1]+"kg";
        }else if(mweight_area==4){
            Strweight_pre="预计"+age[i+1]+"岁时，体重小于"+weight90[i+1]+"kg,大于"+weight50[i+1]+"kg";
        }else if(mweight_area==5){
            Strweight_pre="预计"+age[i+1]+"岁时，体重小于"+weight97[i+1]+"kg,大于"+weight90[i+1]+"kg";
        }else if(mweight_area==6){
            Strweight_pre="预计"+age[i+1]+"岁时，体重大于"+weight97[i+1]+"kg";
        }else {
            Strweight_pre="暂时无法预测";
        }
        weight_pre.setText(Strweight_pre);
        height_pre.setText(Strheight_pre);
    }
}
