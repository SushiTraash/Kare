package com.example.kare.Funct.Exam;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kare.DB.KidQue1;
import com.example.kare.DB.KidQue2;
import com.example.kare.DB.KidQue3;
import com.example.kare.DB.Kiddata;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

public class GrowthtTestActivity extends AppCompatActivity {

    private View view;
    private TextView que ;
    private  Button next ;
    private  Button last;
    private RadioGroup rg;
    private  RadioButton yes ;
    private  RadioButton no ;
    private  Cursor quecur;
    private Cursor kidcur;
    private  int  [] selection;
    private int[]answer;
    private int[]weight;
    private int tested;
    int selected=Kiddata.selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_growtht_test);

        que = (TextView) findViewById(R.id.test_que);
         next = (Button) findViewById(R.id.test_next);
         last = (Button) findViewById(R.id.test_last);
         yes = (RadioButton) findViewById(R.id.test_yes);
         no = (RadioButton) findViewById(R.id.test_no);
        rg=(RadioGroup)findViewById(R.id.test_rg);
        SelectQue();//使quecur 指向当前孩子题库


        que.setText(quecur.getString(quecur.getColumnIndex("que")));
        Log.v("curposition",""+quecur.getPosition()+quecur.getString(quecur.getColumnIndex("que")));

        for(;quecur.getPosition()<quecur.getCount();quecur.moveToNext()){
            answer[quecur.getPosition()]=quecur.getInt(quecur.getColumnIndex("answer"));
            weight[quecur.getPosition()]=quecur.getInt(quecur.getColumnIndex("weight"));
            Log.v("answerweight",""+answer[quecur.getPosition()]+""+weight[quecur.getPosition()]);

        }
        quecur.moveToFirst();
        tested=quecur.getInt(quecur.getColumnIndex("tested"));
        Log.v("answerweight",""+quecur.getPosition()+quecur.getInt(quecur.getColumnIndex("weight"))+quecur.getString(quecur.getColumnIndex("answer")));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quecur.getPosition()<quecur.getCount()-2) {

                    quecur.moveToNext();
                    que.setText(quecur.getString(quecur.getColumnIndex("que")));
                    GetSelection();
                }else if(quecur.getPosition()==quecur.getCount()-2){
                    quecur.moveToNext();
                    next.setText("完成");
                    que.setText(quecur.getString(quecur.getColumnIndex("que")));
                    GetSelection();
                }else{
                    int CheckPass=0;
                    int allcheck=0;
                    for(int i = 0;i<quecur.getCount();i++){
                        if(selection[i]==0)allcheck++;
                    }
                    if(allcheck==0) {
                        for (quecur.moveToFirst(); quecur.getPosition() < quecur.getCount(); quecur.moveToNext()) {
                            int i = quecur.getPosition();
                            CheckPass += Math.abs(((answer[i] - selection[i]) * weight[i]));
                            Log.v("vtest", "" + CheckPass);
                        }
                        if (CheckPass >= 4) {
                                if(tested==0) {
                                    if(Kiddata.selected==1){
                                        KidQue1 kidQue1 = new KidQue1();
                                        kidQue1.setTested(1);
                                        kidQue1.updateAll("minage <= ? and maxage >= ?", "" + kidcur.getLong(kidcur.getColumnIndex("age")), "" + kidcur.getLong(kidcur.getColumnIndex("age")));
                                    }else if(Kiddata.selected==2){
                                        KidQue2 kidQue2 = new KidQue2();
                                        kidQue2.setTested(1);
                                        kidQue2.updateAll("minage <= ? and maxage >= ?", "" + kidcur.getLong(kidcur.getColumnIndex("age")), "" + kidcur.getLong(kidcur.getColumnIndex("age")));
                                    }else{
                                        KidQue3 kidQue3 = new KidQue3();
                                        kidQue3.setTested(1);
                                        kidQue3.updateAll("minage <= ? and maxage >= ?", "" + kidcur.getLong(kidcur.getColumnIndex("age")), "" + kidcur.getLong(kidcur.getColumnIndex("age")));
                                    }

                                } else{
                                    Toast ts = Toast.makeText(getBaseContext(), "这部分成绩不及格，请带孩子去医院检查", Toast.LENGTH_LONG);
                                    ts.show();
                                }


                        }else if(CheckPass==0){
                                ClearPassedQue();
                        }

                            finish();
                            //  Log.v("answerweight",""+answer[quecur.getPosition()]+""+weight[quecur.getPosition()]);

                    }else{
                        Toast ts = Toast.makeText(getBaseContext(), "有题目未完成", Toast.LENGTH_LONG);
                        ts.show();
                    }

                }

                Log.v("curposition",""+quecur.getPosition());
            }

        });
        last.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(quecur.getPosition()!=0) {
                    next.setText("下一题");
                    quecur.moveToPrevious();
                    que.setText(quecur.getString(quecur.getColumnIndex("que")));
                }
                GetSelection();
                Log.v("curposition",""+quecur.getPosition());
            }
        });

    yes.setOnClickListener( new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            selection[quecur.getPosition()]=1;
        }
    });
    no.setOnClickListener( new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            selection[quecur.getPosition()]=-1;
        }
    });
    }
    public void ClearPassedQue(){
        if(Kiddata.selected==1) {
            DataSupport.deleteAll(KidQue1.class, " minage <= ? and maxage >= ?", "" + kidcur.getInt(kidcur.getColumnIndex("age")), "" + kidcur.getInt(kidcur.getColumnIndex("age")));
        }else if(Kiddata.selected==2){
            DataSupport.deleteAll(KidQue2.class, " minage <= ? and maxage >= ?", "" + kidcur.getInt(kidcur.getColumnIndex("age")), "" + kidcur.getInt(kidcur.getColumnIndex("age")));
        }else{
            DataSupport.deleteAll(KidQue3.class, " minage <= ? and maxage >= ?", "" + kidcur.getInt(kidcur.getColumnIndex("age")), "" + kidcur.getInt(kidcur.getColumnIndex("age")));
        }
    }
    public void GetSelection(){
        rg.clearCheck();
        if(selection[quecur.getPosition()]==-1){
            no.setChecked(true);
        }else if(selection[quecur.getPosition()]==1){
            yes.setChecked(true);
        }
    }
    public void SelectQue(){
        kidcur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+ Kiddata.selected);
        if(kidcur.moveToFirst()) {
            quecur=DataSupport.findBySQL("SELECT * FROM KidQue1");
            if (Kiddata.selected == 1) {
                quecur =DataSupport.findBySQL("SELECT * FROM KidQue1 WHERE minage <= ? and maxage >=? ",""+kidcur.getLong(kidcur.getColumnIndex("age")),""+kidcur.getLong(kidcur.getColumnIndex("age")));
            } else if (Kiddata.selected == 2) {
                quecur =DataSupport.findBySQL("SELECT * FROM KidQue2 WHERE minage <= ? and maxage >=? ",""+kidcur.getLong(kidcur.getColumnIndex("age")),""+kidcur.getLong(kidcur.getColumnIndex("age")));
            } else if (Kiddata.selected == 3) {
                quecur =DataSupport.findBySQL("SELECT * FROM KidQue3 WHERE minage <= ? and maxage >=? ",""+kidcur.getLong(kidcur.getColumnIndex("age")),""+kidcur.getLong(kidcur.getColumnIndex("age")));
            } else {
                Toast ts = Toast.makeText(getBaseContext(), "出现错误", Toast.LENGTH_LONG);
                ts.show();
            }

        }
        int quenum =quecur.getCount();
        selection= new int[quenum];//用户的选择
        answer = new int[quenum];//题目的正确答案
        weight =new int[quenum];//各题的权重
        quecur.moveToFirst();
    }


}
