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

public class WeightLineActivity extends AppCompatActivity {
    private LineChart weight_chart;
    private LineDataSet line3;
    private LineDataSet line10;
    private LineDataSet line50;
    private LineDataSet line90;
    private LineDataSet line97;
    private LineDataSet lineKid;
    private Cursor kidcur;
    private Cursor weightcur;
    private int sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_line);
        kidcur= DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+ Kiddata.selected);
        if(kidcur.moveToFirst()) {
            sex = kidcur.getInt(kidcur.getColumnIndex("sex"));
        }else{
            finish();
        }
        weight_chart=(LineChart)findViewById(R.id.Weight_LineChart);
        XAxis xaix= weight_chart.getXAxis();
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
        weight_chart.setDescription(description);

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
        weight_chart.setPinchZoom(true);
        weight_chart.setAutoScaleMinMaxEnabled(true);
        weight_chart.setData(lineData);
        weight_chart.invalidate();
    }
    public void SetKidSet(){
        weightcur=DataSupport.findBySQL("SELECT * FROM Weight WHERE kid = ? and age >=?",""+Kiddata.selected,"24");
        if(weightcur.moveToFirst()){
            List<Entry> entrykid=new ArrayList<>();
            while(weightcur.getPosition()<weightcur.getCount()){

                entrykid.add(new Entry((float) weightcur.getInt(weightcur.getColumnIndex("age")), (float) weightcur.getDouble(weightcur.getColumnIndex("weight"))));
                Log.v("weightcur",""+weightcur.getInt(weightcur.getColumnIndex("weight")));

                weightcur.moveToNext();
            }
            lineKid =new LineDataSet(entrykid,"孩子体重");
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
        double[] weight3=new double[]{10.22,11.11,11.94,12.73,13.52,14.37,15.26,16.09,16.08,17.53,18.48,19.43,20.32,21.18,22.04,22.95,23.89,24.96,26.21,27.59,29.09,30.74,32.82,35.03,37.36,39.53,41.43,43.05,44.28,45.30,46.04,47.01};
        double[] weight10=new double[]{10.90,11.85,12.74,13.58,14.43,15.35,16.33,17.26,18.06,18.92,20.04,21.17,22.24,23.28,24.31,25.42,26.55,27.83,29.33,30.97,32.77,34.71,37.04,39.42,41.80,43.94,45.77,47.31,48.47,49.42,50.11,51.02};
        double[] weight50=new double[]{12.54,13.64,14.65,15.63,16.64,17.75,18.98,20.18,21.26,22.45,24.06,25.27,27.33,28.91,30.46,32.09,33.74,35.58,37.69,39.98,42.49,45.13,48.08,50.85,53.37,55.43,57.08,58.39,59.35,60.12,60.68,61.40};
        double[] weight90=new double[]{14.64,15.73,16.92,18.08,19.29,20.67,22.23,23.81,25.29,27.00,29.35,31.84,34.31,36.74,39.08,41.49,43.85,46.40,49.20,52.21,55.50,58.90,62.57,65.80,68.53,70.55,72.00,73.03,73.74,74.25,74.62,75.08};
        double[] weight97=new double[]{15.46,16.83,18.12,19.38,20.71,22.24,24.00,25.81,27.55,29.57,32.41,35.45,38.49,41.49,44.35,47.24,50.01,52.93,56.07,59.40,63.04,66.81,70.83,74.33,77.20,79.24,80.60,81.49,82.05,82.44,82.70,83.00};
        //-------------line 3
        List<Entry> entry3 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            if(age[i]!=7.5&&age[i]!=10)
                entry3.add(new Entry((float) age[i]*12, (float)weight3[i]));
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
            entry10.add(new Entry((float) age[i]*12, (float)weight10[i]));
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
            entry50.add(new Entry((float) age[i]*12, (float)weight50[i]));
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
            entry90.add(new Entry((float) age[i]*12, (float)weight90[i]));
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

            entry97.add(new Entry((float) age[i]*12, (float)weight97[i]));
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
        double[] weight3=new double[]{9.76,10.65,11.50,12.32,13.10,13.89,14.64,15.39,16.10,16.80,17.58,18.39,19.20,20.02,20.93,21.89,22.98,24.22,25.74,27.43,29.33,31.22,33.09,34.82,36.38,37.71,38.73,39.51,39.96,40.29,40.44,40.71};
        double[] weight10=new double[]{10.39,11.35,12.27,13.14,13.99,14.85,15.68,16.52,17.32,18.12,18.01,18.95,20.89,21.88,22.93,24.08,25.36,26.80,28.53,30.39,32.42,34.39,36.29,38.01,39.55,40.84,41.83,42.58,43.01,43.32,43.37,43.73};
        double[] weight50=new double[]{11.92,13.05,14.13,15.16,16.17,17.22,18.26,19.33,20.37,21.44,22.64,23.93,25.25,26.67,28.19,29.87,31.76,33.80,36.10,38.40,40.77,42.89,44.79,46.42,47.83,48.97,49.82,50.45,50.81,51.07,51.20,51.41};
        double[] weight90=new double[]{13.74,15.08,16.36,17.59,18.81,20.10,21.41,22.81,24.19,25.62,27.28,29.08,30.95,33.00,35.26,37.79,40.63,43.61,46.78,49.73,52.49,54.71,54.46,57.81,58.88,59.70,60.28,60.69,60.91,61.07,61.15,61.22};
        double[] weight97=new double[]{14.71,16.16,17.55,18.89,20.24,21.67,23.14,24.72,26.30,27.96,29.89,32.01,34.23,36.69,39.41,42.51,45.59,49.59,53.33,56.67,59.64,61.86,63.45,64.55,65.36,65.93,66.30,66.55,66.59,66.76,66.82,66.89};
        //-------------line 3
        List<Entry> entry3 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            if(age[i]!=7.5&&age[i]!=10)
                entry3.add(new Entry((float) age[i]*12, (float)weight3[i]));
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
            entry10.add(new Entry((float) age[i]*12, (float)weight10[i]));
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
            entry50.add(new Entry((float) age[i]*12, (float)weight50[i]));
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
            entry90.add(new Entry((float) age[i]*12, (float)weight90[i]));
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

            entry97.add(new Entry((float) age[i]*12, (float)weight97[i]));
        line97 =new LineDataSet(entry97,"97%");
        line97.setColor(Color.parseColor("#0000FF"));
        line97.setDrawValues(false);
        line97.setCubicIntensity(0.15f);
        line97.setDrawCircleHole(false);
        line97.setDrawCircles(false);
        line97.setMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线

    }
}