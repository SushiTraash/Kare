package com.example.kare.Funct;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kare.DB.Kiddata;
import com.example.kare.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeightLineActivity extends AppCompatActivity {
    private LineChart height_chart;
    private LineDataSet line3;
    private LineDataSet line10;
    private LineDataSet line50;
    private LineDataSet line90;
    private LineDataSet line97;
    private LineDataSet lineKid;
    private Cursor kidcur;
    private Cursor heightcur;
    private int sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_line);
        kidcur= DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+ Kiddata.selected);
        if(kidcur.moveToFirst()) {
            sex = kidcur.getInt(kidcur.getColumnIndex("sex"));
        }else{
            finish();
        }
        height_chart=(LineChart)findViewById(R.id.Height_LineChart);
        XAxis xaix= height_chart.getXAxis();
        xaix.setDrawLabels(true);
        xaix.setGranularity(0.5f);
        xaix.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaix.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int year =(int)value/12;
                int month=((int)value)%12;
                String xaix ;
                if(year==0){
                    xaix= month+"月";
                }else{
                    if(month==0){
                        xaix=year+"岁"  ;
                    }else{
                        xaix=year+"岁"+month+"月"  ;
                    }
                }
                return xaix;
            }
        });

        Description description= new Description();
        description.setEnabled(false);
        height_chart.setDescription(description);

        //1.设置x轴和y轴的点

        if(sex==0){
            SetStdSet_Girl();
        }else {
            SetStdSet_Boy();
        }

       SetKidSet();
        LineData lineData;
       if(lineKid==null) {
           lineData = new LineData(line3, line10, line50, line90, line97);

       }else  lineData = new LineData(line3, line10, line50, line90, line97, lineKid);
        height_chart.setPinchZoom(true);
        height_chart.setAutoScaleMinMaxEnabled(true);
        height_chart.setData(lineData);
        height_chart.invalidate();
    }

    public void SetKidSet(){
        heightcur=DataSupport.findBySQL("SELECT * FROM Height WHERE kid = ? and age >=?",""+Kiddata.selected,"24");
        if(heightcur.moveToFirst()){
            List<Entry> entrykid=new ArrayList<>();
            while(heightcur.getPosition()<heightcur.getCount()){

                    entrykid.add(new Entry((float) heightcur.getInt(heightcur.getColumnIndex("age")), (float) heightcur.getDouble(heightcur.getColumnIndex("height"))));
                    Log.v("heightcur",""+heightcur.getInt(heightcur.getColumnIndex("height")));

                heightcur.moveToNext();
            }
            lineKid =new LineDataSet(entrykid,"孩子身高");
            lineKid.setColor(Color.parseColor("#DC143C"));
            lineKid.setCircleColor(Color.parseColor("#DC143C"));
            lineKid.setCircleColorHole(Color.parseColor("#DC143C"));
            lineKid.setValueTextColor(Color.parseColor("#DC143C"));
            lineKid.setCubicIntensity(0.15f);
            lineKid.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线
        }
    }
    public void SetStdSet_Boy(){
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] height3=new double[]{82.1,86.4,89.7,93.4,96.7,100,103.3,106.4,109.1,111.7,114.6,119.4,119.9,122.3,124.6,126.7,128.7,130.7,132.9,135.3,138.1,141.1,145,148.8,152.3,155.3,157.5,159.1,159.9,160.5,160.9,161.3};
        double[] height10=new double[]{84.1,88.6,91.9,95.7,99.1,102.4,105.8,109.0,111.8,114.5,117.6,120.5,123.1,125.6,128.0,130.3,132.3,134.5,136.6,139.5,142.5,145.7,149.6,153.3,156.7,159.4,161.4,162.9,163.6,164.2,164.5,164.9};
        double[] height50=new double[]{88.5,93.3,96.8,100.6,104.1,107.7,111.3,114.7,117.7,120.7,124.0,127.1,130.0,132.7,135.4,137.9,140.2,142.6,145.3,148.4,151.9,155.6,159.5,163.0,165.9,168.2,169.8,170.0,171.6,172.1,172.3,172.7};
        double[] height90=new double[]{93.1,98.2,101.8,105.7,109.3,113.1,116.9,120.5,123.7,126.9,130.5,133.9,137.1,140.1,142.9,145.7,148.2,150.9,154.0,157.4,161.5,165.5,169.5,172.7,175.1,176.9,178.2,179.1,179.5,179.9,180.1,180.4};
        double[] height97=new double[]{95.3,100.5,104.1,108.1,111.8,115.7,119.6,123.3,126.6,129.9,133.7,137.2,140.4,143.6,146.5,149.4,152.0,154.9,158.1,161.7,166.0,170.2,174.2,177.2,179.4,181.0,182.0,182.8,183.2,183.5,183.7,183.9};
       //-------------line 3
        List<Entry> entry3 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            if(age[i]!=7.5&&age[i]!=10)
                entry3.add(new Entry((float) age[i]*12, (float)height3[i]));
        line3 =new LineDataSet(entry3,"3%");
        line3.setColor(Color.parseColor("#FF7449"));
        line3.setDrawValues(false);
        line3.setCubicIntensity(0.15f);
        line3.setDrawCircleHole(false);
        line3.setDrawCircles(false);
        line3.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 10
        List<Entry> entry10 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry10.add(new Entry((float) age[i]*12, (float)height10[i]));
        line10 =new LineDataSet(entry10,"10%");
        line10.setColor(Color.parseColor("#87CEEB"));
        line10.setDrawValues(false);
        line10.setCubicIntensity(0.15f);
        line10.setDrawCircleHole(false);
        line10.setDrawCircles(false);
        line10.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 50
        List<Entry> entry50 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry50.add(new Entry((float) age[i]*12, (float)height50[i]));
        line50 =new LineDataSet(entry50,"50%");
        line50.setColor(Color.parseColor("#FFDEAD"));
        line50.setDrawValues(false);
        line50.setCubicIntensity(0.15f);
        line50.setDrawCircleHole(false);
        line50.setDrawCircles(false);
        line50.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线
        //-----------------line 90
        List<Entry> entry90 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry90.add(new Entry((float) age[i]*12, (float)height90[i]));
        line90 =new LineDataSet(entry90,"90%");
        line90.setColor(Color.parseColor("#90EE90"));
        line90.setDrawValues(false);
        line90.setCubicIntensity(0.15f);
        line90.setDrawCircleHole(false);
        line90.setDrawCircles(false);
        line90.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 97
        List<Entry> entry97 = new ArrayList<>();

        for (int i = 0; i < 32; i++)

                entry97.add(new Entry((float) age[i]*12, (float)height97[i]));
        line97 =new LineDataSet(entry97,"97%");
        line97.setColor(Color.parseColor("#0000FF"));
        line97.setDrawValues(false);
        line97.setCubicIntensity(0.15f);
        line97.setDrawCircleHole(false);
        line97.setDrawCircles(false);
        line97.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

    }
    public void SetStdSet_Girl(){
        double[] age=new double[]{2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,18};
        double[] height3=new double[]{80.9,85.2,88.6,92.4,95.8,99.2,102.3,105.4,108.1,110.6,113.3,116.0,118.5,121.0,123.3,125.7,128.3,131.1,134.2,137.2,140.2,142.9,145.0,146.7,147.9,148.9,149.5,149.9,149.8,149.9,150.1,150.4};
        double[] height10=new double[]{82.9,87.4,90.8,94.6,98.1,101.5,104.8,108.0,110.8,113.4,116.2,119.0,121.6,124.2,126.7,129.3,132.1,135.0,138.2,141.2,144.1,146.6,148.6,150.2,151.3,152.2,152.8,153.1,153.1,153.2,153.4,153.7};
        double[] height50=new double[]{87.2,92.1,95.6,99.4,103.1,106.7,110.2,113.5,116.6,119.4,122.5,125.6,128.5,131.3,134.1,137.0,140.1,143.3,146.6,149.7,152.4,154.6,156.3,157.6,158.6,159.4,159.8,160.1,160.1,160.2,160.3,160.6};
        double[] height90=new double[]{91.7,97.0,100.5,104.4,108.2,112.1,115.7,119.3,122.5,125.6,129.0,132.3,135.4,138.5,141.6,144.8,148.2,151.6,155.2,158.2,160.7,162.7,164.0,165.1,165.9,166.5,166.8,167.1,167.1,167.1,167.3,167.6};
        double[] height97=new double[]{93.9,99.3,102.9,106.8,110.6,114.7,118.4,122.0,125.4,128.6,132.1,135.5,138.7,141.9,145.1,148.5,152.0,155.6,159.2,162.1,164.5,166.3,167.6,168.6,169.3,169.8,170.1,170.3,170.3,170.4,170.5,170.7};
        //-------------line 3
        List<Entry> entry3 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            if(age[i]!=7.5&&age[i]!=10)
                entry3.add(new Entry((float) age[i]*12, (float)height3[i]));
        line3 =new LineDataSet(entry3,"3%");
        line3.setColor(Color.parseColor("#FF7449"));
        line3.setDrawValues(false);
        line3.setCubicIntensity(0.15f);
        line3.setDrawCircleHole(false);
        line3.setDrawCircles(false);
        line3.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 10
        List<Entry> entry10 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry10.add(new Entry((float) age[i]*12, (float)height10[i]));
        line10 =new LineDataSet(entry10,"10%");
        line10.setColor(Color.parseColor("#87CEEB"));
        line10.setDrawValues(false);
        line10.setCubicIntensity(0.15f);
        line10.setDrawCircleHole(false);
        line10.setDrawCircles(false);
        line10.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 50
        List<Entry> entry50 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry50.add(new Entry((float) age[i]*12, (float)height50[i]));
        line50 =new LineDataSet(entry50,"50%");
        line50.setColor(Color.parseColor("#FFDEAD"));
        line50.setDrawValues(false);
        line50.setCubicIntensity(0.15f);
        line50.setDrawCircleHole(false);
        line50.setDrawCircles(false);
        line50.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线
        //-----------------line 90
        List<Entry> entry90 = new ArrayList<>();

        for (int i = 0; i < 32; i++)
            entry90.add(new Entry((float) age[i]*12, (float)height90[i]));
        line90 =new LineDataSet(entry90,"90%");
        line90.setColor(Color.parseColor("#90EE90"));
        line90.setDrawValues(false);
        line90.setCubicIntensity(0.15f);
        line90.setDrawCircleHole(false);
        line90.setDrawCircles(false);
        line90.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

        //-----------------line 97
        List<Entry> entry97 = new ArrayList<>();

        for (int i = 0; i < 32; i++)

            entry97.add(new Entry((float) age[i]*12, (float)height97[i]));
        line97 =new LineDataSet(entry97,"97%");
        line97.setColor(Color.parseColor("#0000FF"));
        line97.setDrawValues(false);
        line97.setCubicIntensity(0.15f);
        line97.setDrawCircleHole(false);
        line97.setDrawCircles(false);
        line97.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

    }
}
