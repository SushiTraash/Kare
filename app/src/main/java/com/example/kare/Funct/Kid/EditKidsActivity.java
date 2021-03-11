package com.example.kare.Funct.Kid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kare.DB.KidQue1;
import com.example.kare.DB.KidQue2;
import com.example.kare.DB.KidQue3;
import com.example.kare.DB.Kiddata;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

public class EditKidsActivity extends AppCompatActivity {
    private EditText kidname;
    private EditText kidyear;
    private EditText kidmonth;
    private EditText kidday;
    private EditText boneage_view;
    private Cursor cursor;
    private Button Editfinish;
    private int kidid;
    private int year;
    private int month;
    private  int day;
    private  String inputname;
    private int boneage;
    private int ba_reset;
    private RadioButton boy;
    private RadioButton girl;
    private RadioGroup sex_selection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kids);
        kidname=findViewById(R.id.kids_name);
        kidyear=findViewById(R.id.kids_year);
        kidmonth=findViewById(R.id.kids_month);
        kidday=findViewById(R.id.kids_day);
        Editfinish =findViewById(R.id.Edit_Finish);
        boneage_view=findViewById(R.id.Boneage);

        boy=findViewById(R.id.sex_boy);
        girl=findViewById(R.id.sex_girl);
        sex_selection=findViewById(R.id.sex_selection);
        Intent intent = getIntent();
        kidid =intent.getIntExtra("kidid",0);
        Log.v("kidid",""+kidid);

        String name = kidname.getText().toString().trim();
        Log.v("name1111",""+name);

        SetSexClickListener();
        ShowKid();
        boneage_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ba_reset=1;
                Log.v("ba_reset",""+ba_reset);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Log.v("kidyear",""+kidyear.getText());
        Editfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isRight = true;
                //检查用户输入是否有错
                if(kidname.getText().toString().equals("")) {
                    String wrong = getText(R.string.Edit_wrongname).toString();
                    Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                    ts.show();
                    isRight = false;
                }else {
                  inputname = kidname.getText().toString().trim();
                    if(inputname.length()==0){
                        String wrong = getText(R.string.Edit_wrongname).toString();
                        Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                        ts.show();
                        isRight = false;
                    }
                }
                Log.v("kidyear",""+kidyear.getText());
                    if (kidyear.getText().toString().equals(""))
                    {
                        String wrong = getText(R.string.Edit_wrongyear).toString();
                        Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                        ts.show();
                        isRight = false;
                    }else{
                        year = Integer.parseInt(kidyear.getText().toString());
                    }
                    if (kidmonth.getText().toString().equals(""))  {
                        String wrong = getText(R.string.Edit_wrongmonth).toString();
                        Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                        ts.show();
                        isRight = false;
                    }else {
                        month = Integer.parseInt(kidmonth.getText().toString());
                    }

                    if( kidday.getText().toString().equals("")){
                        String wrong = getText(R.string.Edit_wrongday).toString();
                        Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                        ts.show();
                        isRight = false;
                    } else {
                        day = Integer.parseInt(kidday.getText().toString());
                        if(day>31||day<=0||(month==2 &&day>28)||((month == 4 ||month==6||month==9||month==11 )&&day>30)){
                            String wrong = getText(R.string.Edit_wrongday).toString();
                            Toast ts = Toast.makeText(getBaseContext(),""+wrong,Toast.LENGTH_LONG);
                            ts.show();
                            isRight = false;
                        }
                        }

                if( boneage_view.getText().toString().equals("")){
                    boneage=0;

                } else {
                    boneage= Integer.parseInt(boneage_view.getText().toString());
                    Kiddata kiddata = new Kiddata();
                    if(boneage==0){
                        kiddata.setToDefault("bone_age");
                    }else {
                        kiddata.setBone_age(boneage);

                    }
                    kiddata.updateAll("id = ?", "" + Kiddata.selected);
                }


                if(isRight) {
                    CalAge(kidid);
                    if(kidid==1){
                        ResetQue1();
                    }else if(kidid==2){
                        ResetQue2();
                    }else {
                       ResetQue3();
                    }

                }

                ShowKid();
            }
        });

    }

    @Override
    protected void onDestroy() {
        cursor.close();
        super.onDestroy();
    }
    public void CalAge(int selected){
        Calendar cd = Calendar.getInstance();
        int yearnow = cd.get(Calendar.YEAR);
        int monthnow =cd.get(Calendar.MONTH);
        int daynow = cd.get(Calendar.DAY_OF_MONTH);
        int age=0;
        long year_age;
        long month_age;
        long day_age;
        int ba;
        long pre_year_age;
        long pre_month_age;


        Kiddata kiddata = new Kiddata();
        kiddata.setName(inputname);
        kiddata.setYear(year);
        kiddata.setMonth(month);
        kiddata.setDay(day);

        Calendar BirthCal= Calendar.getInstance();
        BirthCal.set(year,month-1,day,0,0,0);//month 要 减一
        long days_pass=(cd.getTimeInMillis()- BirthCal.getTimeInMillis())/(60*60*24*1000);
        long month_pass=days_pass/30;
        long year_pass=month_pass/12;

        year_age=year_pass;
        month_age=month_pass%12;
        day_age=days_pass%30;

        Log.v("days_pass",""+cd.get(Calendar.YEAR)+"  "+cd.get(Calendar.MONTH) +"   "+cd.get(Calendar.DAY_OF_MONTH)+"  daypass "+days_pass+"  year:  "+year_age+"month:  "+month_age+"day:  "+day_age);
        if(year_age<0||month_age<0||day_age<0) {
            Toast ts = Toast.makeText(getBaseContext(),"年龄为非正数，请重新输入",Toast.LENGTH_LONG);
            ts.show();
            kiddata.delete();
        }else if(year_age>=6&&month_age>=11&&day_age>15){
            Toast ts = Toast.makeText(getBaseContext(),"年龄过大，请重新输入",Toast.LENGTH_LONG);
            ts.show();
            kiddata.delete();
        } else {
            if(year_age==0){
                kiddata.setToDefault("year_age");
            }else {
                kiddata.setYear_age(year_age);
            }
            if(month_age==0){
                kiddata.setToDefault("month_age");
            }else {
                kiddata.setMonth_age(month_age);
            }
            if(day_age==0){
                kiddata.setToDefault("day_age");
            }else {
                kiddata.setDay_age(day_age);
            }



            kiddata.setAge(year_age*10000+month_age*100+day_age);
            Cursor agecur = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+selected);
            if(agecur.moveToFirst()) {


                pre_year_age=agecur.getLong(agecur.getColumnIndex("year_age"));
                pre_month_age=agecur.getLong(agecur.getColumnIndex("month_age"));
                ba=agecur.getInt(agecur.getColumnIndex("bone_age"));
                if(ba!=0&&ba_reset==0){
                    ba+=(year_age-pre_year_age)*12+(month_age- pre_month_age);
                    Log.v("ba_increase",""+year_age+"  "+pre_year_age+"  "+month_age+"  "+pre_month_age);
                    kiddata.setBone_age(ba);
                }

            }
            kiddata.updateAll("id = ?",""+selected);
            finish();
        }
    }
    public void ShowKid(){
        sex_selection.clearCheck();
        cursor = DataSupport.findBySQL("SELECT * FROM Kiddata WHERE id = ?",""+kidid);



        if(cursor.moveToFirst()) {
            year =cursor.getInt(cursor.getColumnIndex("year"));
            month = cursor.getInt(cursor.getColumnIndex("month"));
            day =cursor.getInt(cursor.getColumnIndex("day"));
            boneage=cursor.getInt(cursor.getColumnIndex("bone_age"));
            if(cursor.getString(cursor.getColumnIndex("name"))!=null) {
                kidname.setText(cursor.getString(cursor.getColumnIndex("name")));
            }
            if(cursor.getInt(cursor.getColumnIndex("year"))!=0) {
                if (year >= 0) {
                    kidyear.setText("" + year);
                }

            }
            if(cursor.getInt(cursor.getColumnIndex("month"))!=0) {
                if (month >= 0 && month <= 12) {
                    kidmonth.setText("" + month);
                }
            }if(cursor.getInt(cursor.getColumnIndex("day"))!=0){
                if(!(day>31||day<0||(month==2 &&day>28)||((month == 4 ||month==6||month==9||month==11 )&&day>30)) ){
                    kidday.setText("" + day);
                }
            }
            if (cursor.getInt(cursor.getColumnIndex("sex")) == 0) {
                girl.setChecked(true);
            } else {
                boy.setChecked(true);
            }
            if(cursor.getInt(cursor.getColumnIndex("bone_age"))!= 0){
                boneage_view.setText(""+ boneage);
            }
        }

    }
    public void SetSexClickListener(){
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kiddata kiddata = new Kiddata();
                kiddata.setSex(1);
                kiddata.updateAll("id = ?",""+Kiddata.selected);
            }
        });
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kiddata kiddata = new Kiddata();
                kiddata.setToDefault("sex");
                kiddata.updateAll("id = ?",""+Kiddata.selected);
            }
        });
    }
    public void ResetQue1(){
        DataSupport.deleteAll(KidQue1.class);
        KidQue1 kidque = new KidQue1();// 只有第一个需要Kidque1

        kidque.setQue("（仰）仰躺时双手手掌均能自然地张开，不再一直紧握");
        kidque.setMinage(416);//最小年龄  年龄 为 六位数 x   xx   xx   分别为年 月 日  例如 416  等价于 00416 即 4 月 16 天
        kidque.setMaxage(515);//最大年龄
        kidque.setAnswer(1);//答案为是  answer 为1  答案为否 answer 为 -1；
        kidque.setWeight(1);//打*号的题 weight 为 2  其余为 1
        kidque.save(); //记得保存


        kidque = new KidQue1();
        kidque.setQue("（仰）仰躺时双手会在胸前互相靠近（不一定要碰到）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）仰躺静止不动时，身体的姿势经常歪向固定一侧，无法维持在中线上");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）换尿布是感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）使用左右手或左右脚的次数和力量明显地不均匀");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）仰卧拉起是头无法跟着身体抬起来，一直向后仰");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）即使跟他玩，也很少发出声音");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("（仰）眼睛可以从左到右、从上到下来回追视没有声音的移动物体（可使用玩具发出声音或碰触脸吸引儿童注视，再移到眼前20公分左右不出声地移动，观察儿童反应）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("（趴）趴着时能以双肘支撑，将头抬起和地面垂直，且能维持数秒钟后头慢慢放下（如果头针扎抬起，重重掉下则不通过）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（直）抱在肩上直立时，头部和上半身能撑直至少10秒钟，不会摇来晃去");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("面对面时能持续注视人脸，表现出对人的兴趣");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();







        kidque=new KidQue1();
        kidque.setQue("（仰）换尿布时感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（趴）趴着时能用手掌撑着，将上半身抬起离开地面，头部可以上下左右自由移动（如果头针扎抬起、重重掉下、一直向后仰，无法自由转动则不通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（坐）能用双手撑着地面自己坐5秒，且头部稳定不下垂，眼睛看正前方");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（站）大人稍微用手在腋下扶着就能站得很挺（臀部不后翘），脚还可以偶尔自由地挪动，如蹬脚、原地踏步、抬一脚蹬");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能单手伸出碰到眼前15公分的玩具（左右手均能做到才算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能抓紧放在手里的玩具并稍微摇动（必须如图示：大拇指能开离手掌面，与其他手指一起参与抓握的动作，且左右手均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("两只手可以同时各自握紧一样东西至少3秒钟（如玩具、积木、食物等）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会转头寻找左后方和右方约20公分处的手摇铃声（必须左右边均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、玩具就可以把他逗笑");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();






        kidque=new KidQue1();
        kidque.setQue("（趴）翻身（趴着变成仰躺和仰躺变成趴着均能做到才通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("（坐）能自己坐稳数分钟，不会摇晃或跌倒（仍须双手撑地面、背部呈圆弓形无法挺直、或容易跌倒均不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（站）能手扶东西站立失少五秒钟（扶桌面、平台、大人均可）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("两只手可以同时各自握紧一样东西（如玩具、积木、食物等）5秒钟以上");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会重复地坐摇的动作让玩具发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("会转头向下寻找掉落不见的玩具");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以分辨熟人和陌生人;如喜欢熟人抱，看到陌生人会害羞或害怕");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("完全听不懂话，例如叫唤名字）（或小名）不会回头、说“不可以”没有反应等");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("（仰）能由躺的姿势（俯卧或仰躺均可）自己坐起来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("（站）能自己拉着东西站起来，然后扶着家具侧走两三步（仰）能由躺的姿势");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("只会把玩具放入嘴巴或丢到地上，没有其他玩法如摇、捏、敲、拉等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("完全不会自己发声;或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能听懂简单的日常生活指令（如过来、给我、再见等。是真的能听得懂语言，而不是根据大人手势、表情作反应）");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会在大人提示下（语言加上手势）模仿做一些手势如拍拍手、再见、拜拜等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("与大人有游戏的默契（如大人唱儿歌时能做出学习过的、固定的、简单的配合手势---例如去拍大人的手或伸出手指头等。若之前无此经验也可立即学习简单互动游戏如“give me five”） ");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


















        kidque=new KidQue1();
        kidque.setQue("能不须扶东西自己站起来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以放手自己走");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面（大人可协助固定容器）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会想办法把丢进容器里的小东西取出来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能听懂生活中常用的口头指令（如：喝奶奶、拍拍手、睡觉了、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("会在适当的情况下自己做出拍拍手、再见等手势");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、拿出玩具就可以把他逗笑");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue1();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();



        kidque=new KidQue1();
        kidque.setQue("能不须扶东西自己由坐或躺的姿势站起来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("走得很稳（步态怪异如垫脚尖、左右不对称、停不下来、无法转弯、双脚张开距离超过肩膀宽度、双臂弯曲在身体两侧而非自然下垂或常常跌倒均不算通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("在少许支撑下能蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("能听懂并且遵从日常生活中半数的口头指令（如：给我xx、过来、拿给爸爸、把xx丢掉、坐下、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("自己记得常用东西藏放的地点（如玩具放哪里，鞋子摆哪里），可以随时把需要的东西找出来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("自己会去找照顾大人陪他一起玩，大人说话、笑、玩玩具就可以把它逗乐");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();
        kidque=new KidQue1();
        kidque.setQue("高兴时会和别人分享喜悦：例如转头面对大人微笑，或把喜欢或得意的东西展示给大人看");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue1();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();



        kidque.setQue("在少数支撑下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够双手拿大东西—如搬小凳子或抱大玩具向前走一小段距离（约10步左右）不会跌倒");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("至少有10个稳定使用的词语（娃娃语“汪汪”为小狗亦可）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能正确只认至少一个图形（图1：大人依次问“哪个是笔？鞋子？钥匙？鱼？飞机？杯子？”全部问完再从头问一次，必须有两次均指对才算对，以避免儿童因乱指而猜对）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque = new KidQue1();
        kidque.setQue("能正确指出至少四个身体部位（大人依次问“头、手、脚、眼、耳、鼻、嘴在哪里”）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能模仿做家事或使用大多数的家用器具（如扫地、用纸巾擦东西、使用开关等）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("有主动探索学习动机，例如：转头面对大人微笑、或把喜欢或得意的东西展示给大人看 ");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("有主动学习的动机，例如：会自己把玩具找出来玩、或自己拿故事书出来翻看");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("无法模仿说单词，因为（1）根本没有仿说的动机，或（2）发音困难");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶东西轻易蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能扶栏杆或墙壁走上楼梯");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能双脚离地跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("会旋开小瓶盖（大人先旋开一点点让瓶盖不会太紧）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("可以一页一页地翻阅硬卡书或布书");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("可以说出来的词语已经很多了，而且不多数不是单音，例如说“苹果”，而不是“果”");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("大多数时候能够使用两个词语组成的句子表达意思（如：妈妈—抱抱、要—喝水等）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能正确说出至少四个图形名称（图1：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能正确指出至少六个身体部位（大人依次问“头、手、脚、眼、耳，鼻、嘴在哪里？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，说话连最清楚的大人也听不懂");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("可以模仿画一条平稳的垂直线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常可以和人一问一答持续对话，使用2~3个单词的句子，且回答问题切题");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能正确说出至少四个图形名称（图2：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够听懂至少两个图形的描述句（图2：大人依次问“哪个是用来开门的？在水里游的？用来写字的？穿在脚上的？用来喝水的？在天空中飞？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("可以配对一样的图形（图2：大人分别指左侧的钥匙和右侧的笔问“那个图和这个一样”两项均指对才能通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常无法正确使用代名词“你”、“我”，例如；（1）“你”、“我”颠倒，或（2）都用名字（或小名）表示自己而不说“我”");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("可以模仿画一条平稳的横线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常可以和人一问一答持续对话，使用3~4个单词的句子，且回答问题切题");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能说出至少三种东西的用途（图4：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”如果儿童第一题答不出，可以给提示“杯子是用来喝水的”。之后不在给提示）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够理解“大”（图2：问“哪个比较大”必须询问两次均正确才通过。图需要多次交换位置）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能正确指认一个颜色（图3：依次问“哪一个是红色？黄色？蓝色？绿色？”也可以替换为哪一个是苹果的红色？香蕉的黄色？天空的蓝色？树叶的绿色？全部问完再从头问一遍，必须两次均指对的颜色才对。）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("经常自言自语说出一些固定的话，和当时情景无关、也不具沟通能力");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能双脚离地跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常可以与人一问一答对话，使用4~5个单词的短句，且回答内容切题");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能说出一种颜色的名称（图1：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”说过1个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能听懂2个空间关系词（图2：先引导儿童注视图片上的牛头和四只小鸟，然后依次问“哪只小鸟在牛的上面？下面？前面？后面？”指对2个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("仿说“弟弟—想要—一辆—自行车”（大人念句子让儿童诵读，错误4个字及以上不通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能说出四种东西的用途（图3：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”说对四个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能一次一个点数到5（图4：问“数一数这边有几个黑点点？”要求儿童一边指点一遍唱数必须前面5个点手指动作和嘴巴唱数能做一对一的配合，唱数到5没有错误才算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能双脚离地连续跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("看图模仿画图1中三个图形（需线条不断裂、无严重越线或间隙、角数目正确且转弯无困难）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够向别人述说发生在自己身上的事情（如：转告老师交代的事情，描述学校发生的事件）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能说出四种颜色的名称（图2：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("有“七个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能念读阿拉伯数字（图4：用手依次指着5、8、7、4、6、3、9、2并问“这是什么数字”答对7个通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够单脚跳4下（两脚均能做到才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够将纸大致对折并压出一条线（大人先示范，没有完全对其也可以通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够模仿简单的字（图1：大人现在“人”字下的空格仿写，然后指着其他空格说“照着写跟上面一样的字”三个字全部写对才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("通常能头尾清楚地说一个简单的故事（图2：用手指图（1）说“你看，这边有块香蕉皮。说说看这几张图里小朋友发生了什么事”适时以手依次指图（2）（3）（4），引导儿童说故事，记下儿童的语言反应）\n计分：儿童能说出图片中至少两个因果关系（如AB, AC, BC, ABC, 皆可）才算通过\n因果A：【因】不小心、没看见、踩到香蕉皮（滑滑的东西）→【果】摔倒、滑到、跌倒等\n因果B：【因】摔倒、滑到、跌倒等→【果】哭、坐在地上、长包包、受伤等\n因果C：【最后】妈妈（医生、护士、姐姐）来了、救他、帮他治疗、擦药、贴起来、粘起来");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够从1数到30（能够仅提示和纠正一次下完成才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("有“7个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("有“13个”的数量概念（图3：要求儿童“请你数一数这里有几个黑点”儿童如果数12个或14个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够说出3个相对词（问“哥哥是男生，姐姐是__？夏天很热，冬天很__？”飞机在天空飞，汽车在__？大象的鼻子长长的，老鼠的鼻子__？答对3题通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("具备基本常识（问：“一只手有几只手指头？你有几个眼睛？小猫有几只脚？消防车是什么？一加一等于多少？”答对4题通过。）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue1();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        Log.v("createque", "que1 创建完成" );


    }
    public void ResetQue2(){
        DataSupport.deleteAll(KidQue2.class);
        KidQue2 kidque = new KidQue2();// 只有第一个需要Kidque1

        kidque.setQue("（仰）仰躺时双手手掌均能自然地张开，不再一直紧握");
        kidque.setMinage(416);//最小年龄  年龄 为 六位数 x   xx   xx   分别为年 月 日  例如 416  等价于 00416 即 4 月 16 天
        kidque.setMaxage(515);//最大年龄
        kidque.setAnswer(1);//答案为是  answer 为1  答案为否 answer 为 -1；
        kidque.setWeight(1);//打*号的题 weight 为 2  其余为 1
        kidque.save(); //记得保存


        kidque = new KidQue2();
        kidque.setQue("（仰）仰躺时双手会在胸前互相靠近（不一定要碰到）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）仰躺静止不动时，身体的姿势经常歪向固定一侧，无法维持在中线上");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）换尿布是感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）使用左右手或左右脚的次数和力量明显地不均匀");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）仰卧拉起是头无法跟着身体抬起来，一直向后仰");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）即使跟他玩，也很少发出声音");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("（仰）眼睛可以从左到右、从上到下来回追视没有声音的移动物体（可使用玩具发出声音或碰触脸吸引儿童注视，再移到眼前20公分左右不出声地移动，观察儿童反应）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("（趴）趴着时能以双肘支撑，将头抬起和地面垂直，且能维持数秒钟后头慢慢放下（如果头针扎抬起，重重掉下则不通过）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（直）抱在肩上直立时，头部和上半身能撑直至少10秒钟，不会摇来晃去");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("面对面时能持续注视人脸，表现出对人的兴趣");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();







        kidque=new KidQue2();
        kidque.setQue("（仰）换尿布时感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（趴）趴着时能用手掌撑着，将上半身抬起离开地面，头部可以上下左右自由移动（如果头针扎抬起、重重掉下、一直向后仰，无法自由转动则不通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（坐）能用双手撑着地面自己坐5秒，且头部稳定不下垂，眼睛看正前方");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（站）大人稍微用手在腋下扶着就能站得很挺（臀部不后翘），脚还可以偶尔自由地挪动，如蹬脚、原地踏步、抬一脚蹬");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能单手伸出碰到眼前15公分的玩具（左右手均能做到才算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能抓紧放在手里的玩具并稍微摇动（必须如图示：大拇指能开离手掌面，与其他手指一起参与抓握的动作，且左右手均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("两只手可以同时各自握紧一样东西至少3秒钟（如玩具、积木、食物等）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会转头寻找左后方和右方约20公分处的手摇铃声（必须左右边均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、玩具就可以把他逗笑");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();






        kidque=new KidQue2();
        kidque.setQue("（趴）翻身（趴着变成仰躺和仰躺变成趴着均能做到才通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("（坐）能自己坐稳数分钟，不会摇晃或跌倒（仍须双手撑地面、背部呈圆弓形无法挺直、或容易跌倒均不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（站）能手扶东西站立失少五秒钟（扶桌面、平台、大人均可）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("两只手可以同时各自握紧一样东西（如玩具、积木、食物等）5秒钟以上");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会重复地坐摇的动作让玩具发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("会转头向下寻找掉落不见的玩具");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以分辨熟人和陌生人;如喜欢熟人抱，看到陌生人会害羞或害怕");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("完全听不懂话，例如叫唤名字）（或小名）不会回头、说“不可以”没有反应等");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("（仰）能由躺的姿势（俯卧或仰躺均可）自己坐起来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("（站）能自己拉着东西站起来，然后扶着家具侧走两三步（仰）能由躺的姿势");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("只会把玩具放入嘴巴或丢到地上，没有其他玩法如摇、捏、敲、拉等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("完全不会自己发声;或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能听懂简单的日常生活指令（如过来、给我、再见等。是真的能听得懂语言，而不是根据大人手势、表情作反应）");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会在大人提示下（语言加上手势）模仿做一些手势如拍拍手、再见、拜拜等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("与大人有游戏的默契（如大人唱儿歌时能做出学习过的、固定的、简单的配合手势---例如去拍大人的手或伸出手指头等。若之前无此经验也可立即学习简单互动游戏如“give me five”） ");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


















        kidque=new KidQue2();
        kidque.setQue("能不须扶东西自己站起来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以放手自己走");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面（大人可协助固定容器）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会想办法把丢进容器里的小东西取出来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能听懂生活中常用的口头指令（如：喝奶奶、拍拍手、睡觉了、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("会在适当的情况下自己做出拍拍手、再见等手势");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、拿出玩具就可以把他逗笑");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue2();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();
















        kidque=new KidQue2();
        kidque.setQue("能不须扶东西自己由坐或躺的姿势站起来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("走得很稳（步态怪异如垫脚尖、左右不对称、停不下来、无法转弯、双脚张开距离超过肩膀宽度、双臂弯曲在身体两侧而非自然下垂或常常跌倒均不算通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("在少许支撑下能蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("能听懂并且遵从日常生活中半数的口头指令（如：给我xx、过来、拿给爸爸、把xx丢掉、坐下、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("自己记得常用东西藏放的地点（如玩具放哪里，鞋子摆哪里），可以随时把需要的东西找出来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("自己会去找照顾大人陪他一起玩，大人说话、笑、玩玩具就可以把它逗乐");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();
        kidque=new KidQue2();
        kidque.setQue("高兴时会和别人分享喜悦：例如转头面对大人微笑，或把喜欢或得意的东西展示给大人看");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue2();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();



        kidque.setQue("在少数支撑下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够双手拿大东西—如搬小凳子或抱大玩具向前走一小段距离（约10步左右）不会跌倒");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("至少有10个稳定使用的词语（娃娃语“汪汪”为小狗亦可）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能正确只认至少一个图形（图1：大人依次问“哪个是笔？鞋子？钥匙？鱼？飞机？杯子？”全部问完再从头问一次，必须有两次均指对才算对，以避免儿童因乱指而猜对）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque = new KidQue2();
        kidque.setQue("能正确指出至少四个身体部位（大人依次问“头、手、脚、眼、耳、鼻、嘴在哪里”）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能模仿做家事或使用大多数的家用器具（如扫地、用纸巾擦东西、使用开关等）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("有主动探索学习动机，例如：转头面对大人微笑、或把喜欢或得意的东西展示给大人看 ");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("有主动学习的动机，例如：会自己把玩具找出来玩、或自己拿故事书出来翻看");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("无法模仿说单词，因为（1）根本没有仿说的动机，或（2）发音困难");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶东西轻易蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能扶栏杆或墙壁走上楼梯");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能双脚离地跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("会旋开小瓶盖（大人先旋开一点点让瓶盖不会太紧）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("可以一页一页地翻阅硬卡书或布书");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("可以说出来的词语已经很多了，而且不多数不是单音，例如说“苹果”，而不是“果”");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("大多数时候能够使用两个词语组成的句子表达意思（如：妈妈—抱抱、要—喝水等）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能正确说出至少四个图形名称（图1：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能正确指出至少六个身体部位（大人依次问“头、手、脚、眼、耳，鼻、嘴在哪里？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，说话连最清楚的大人也听不懂");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("可以模仿画一条平稳的垂直线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常可以和人一问一答持续对话，使用2~3个单词的句子，且回答问题切题");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能正确说出至少四个图形名称（图2：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够听懂至少两个图形的描述句（图2：大人依次问“哪个是用来开门的？在水里游的？用来写字的？穿在脚上的？用来喝水的？在天空中飞？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("可以配对一样的图形（图2：大人分别指左侧的钥匙和右侧的笔问“那个图和这个一样”两项均指对才能通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常无法正确使用代名词“你”、“我”，例如；（1）“你”、“我”颠倒，或（2）都用名字（或小名）表示自己而不说“我”");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("可以模仿画一条平稳的横线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常可以和人一问一答持续对话，使用3~4个单词的句子，且回答问题切题");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能说出至少三种东西的用途（图4：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”如果儿童第一题答不出，可以给提示“杯子是用来喝水的”。之后不在给提示）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够理解“大”（图2：问“哪个比较大”必须询问两次均正确才通过。图需要多次交换位置）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能正确指认一个颜色（图3：依次问“哪一个是红色？黄色？蓝色？绿色？”也可以替换为哪一个是苹果的红色？香蕉的黄色？天空的蓝色？树叶的绿色？全部问完再从头问一遍，必须两次均指对的颜色才对。）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("经常自言自语说出一些固定的话，和当时情景无关、也不具沟通能力");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能双脚离地跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常可以与人一问一答对话，使用4~5个单词的短句，且回答内容切题");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能说出一种颜色的名称（图1：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”说过1个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能听懂2个空间关系词（图2：先引导儿童注视图片上的牛头和四只小鸟，然后依次问“哪只小鸟在牛的上面？下面？前面？后面？”指对2个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("仿说“弟弟—想要—一辆—自行车”（大人念句子让儿童诵读，错误4个字及以上不通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能说出四种东西的用途（图3：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”说对四个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能一次一个点数到5（图4：问“数一数这边有几个黑点点？”要求儿童一边指点一遍唱数必须前面5个点手指动作和嘴巴唱数能做一对一的配合，唱数到5没有错误才算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能双脚离地连续跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("看图模仿画图1中三个图形（需线条不断裂、无严重越线或间隙、角数目正确且转弯无困难）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够向别人述说发生在自己身上的事情（如：转告老师交代的事情，描述学校发生的事件）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能说出四种颜色的名称（图2：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("有“七个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能念读阿拉伯数字（图4：用手依次指着5、8、7、4、6、3、9、2并问“这是什么数字”答对7个通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够单脚跳4下（两脚均能做到才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够将纸大致对折并压出一条线（大人先示范，没有完全对其也可以通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够模仿简单的字（图1：大人现在“人”字下的空格仿写，然后指着其他空格说“照着写跟上面一样的字”三个字全部写对才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("通常能头尾清楚地说一个简单的故事（图2：用手指图（1）说“你看，这边有块香蕉皮。说说看这几张图里小朋友发生了什么事”适时以手依次指图（2）（3）（4），引导儿童说故事，记下儿童的语言反应）\n计分：儿童能说出图片中至少两个因果关系（如AB, AC, BC, ABC, 皆可）才算通过\n因果A：【因】不小心、没看见、踩到香蕉皮（滑滑的东西）→【果】摔倒、滑到、跌倒等\n因果B：【因】摔倒、滑到、跌倒等→【果】哭、坐在地上、长包包、受伤等\n因果C：【最后】妈妈（医生、护士、姐姐）来了、救他、帮他治疗、擦药、贴起来、粘起来");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够从1数到30（能够仅提示和纠正一次下完成才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("有“7个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("有“13个”的数量概念（图3：要求儿童“请你数一数这里有几个黑点”儿童如果数12个或14个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够说出3个相对词（问“哥哥是男生，姐姐是__？夏天很热，冬天很__？”飞机在天空飞，汽车在__？大象的鼻子长长的，老鼠的鼻子__？答对3题通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("具备基本常识（问：“一只手有几只手指头？你有几个眼睛？小猫有几只脚？消防车是什么？一加一等于多少？”答对4题通过。）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue2();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        Log.v("createque", "que1 创建完成" );


    }
    public void ResetQue3(){
        DataSupport.deleteAll(KidQue3.class);
        KidQue3 kidque = new KidQue3();// 只有第一个需要Kidque1

        kidque.setQue("（仰）仰躺时双手手掌均能自然地张开，不再一直紧握");
        kidque.setMinage(416);//最小年龄  年龄 为 六位数 x   xx   xx   分别为年 月 日  例如 416  等价于 00416 即 4 月 16 天
        kidque.setMaxage(515);//最大年龄
        kidque.setAnswer(1);//答案为是  answer 为1  答案为否 answer 为 -1；
        kidque.setWeight(1);//打*号的题 weight 为 2  其余为 1
        kidque.save(); //记得保存


        kidque = new KidQue3();
        kidque.setQue("（仰）仰躺时双手会在胸前互相靠近（不一定要碰到）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）仰躺静止不动时，身体的姿势经常歪向固定一侧，无法维持在中线上");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）换尿布是感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）使用左右手或左右脚的次数和力量明显地不均匀");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）仰卧拉起是头无法跟着身体抬起来，一直向后仰");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）即使跟他玩，也很少发出声音");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("（仰）眼睛可以从左到右、从上到下来回追视没有声音的移动物体（可使用玩具发出声音或碰触脸吸引儿童注视，再移到眼前20公分左右不出声地移动，观察儿童反应）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("（趴）趴着时能以双肘支撑，将头抬起和地面垂直，且能维持数秒钟后头慢慢放下（如果头针扎抬起，重重掉下则不通过）");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（直）抱在肩上直立时，头部和上半身能撑直至少10秒钟，不会摇来晃去");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("面对面时能持续注视人脸，表现出对人的兴趣");
        kidque.setMinage(416);
        kidque.setMaxage(515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();







        kidque=new KidQue3();
        kidque.setQue("（仰）换尿布时感觉双腿有明显不寻常的阻力，不容易打开、弯曲");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（仰）仰头不寻常地一直歪一边，无法回正或自由转动");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（趴）趴着时能用手掌撑着，将上半身抬起离开地面，头部可以上下左右自由移动（如果头针扎抬起、重重掉下、一直向后仰，无法自由转动则不通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（坐）能用双手撑着地面自己坐5秒，且头部稳定不下垂，眼睛看正前方");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（站）大人稍微用手在腋下扶着就能站得很挺（臀部不后翘），脚还可以偶尔自由地挪动，如蹬脚、原地踏步、抬一脚蹬");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能单手伸出碰到眼前15公分的玩具（左右手均能做到才算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能抓紧放在手里的玩具并稍微摇动（必须如图示：大拇指能开离手掌面，与其他手指一起参与抓握的动作，且左右手均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("两只手可以同时各自握紧一样东西至少3秒钟（如玩具、积木、食物等）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会转头寻找左后方和右方约20公分处的手摇铃声（必须左右边均能做到）");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、玩具就可以把他逗笑");
        kidque.setMinage(516);
        kidque.setMaxage(815);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();






        kidque=new KidQue3();
        kidque.setQue("（趴）翻身（趴着变成仰躺和仰躺变成趴着均能做到才通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("（坐）能自己坐稳数分钟，不会摇晃或跌倒（仍须双手撑地面、背部呈圆弓形无法挺直、或容易跌倒均不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（站）能手扶东西站立失少五秒钟（扶桌面、平台、大人均可）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("两只手可以同时各自握紧一样东西（如玩具、积木、食物等）5秒钟以上");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会重复地坐摇的动作让玩具发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会把玩具或东西，由一手平顺地换到另一手（用扯的不算通过）");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("会转头向下寻找掉落不见的玩具");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以分辨熟人和陌生人;如喜欢熟人抱，看到陌生人会害羞或害怕");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("即使跟他玩，也很少发出声音");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("完全听不懂话，例如叫唤名字）（或小名）不会回头、说“不可以”没有反应等");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(816);
        kidque.setMaxage(1115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("（仰）能由躺的姿势（俯卧或仰躺均可）自己坐起来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("（站）能自己拉着东西站起来，然后扶着家具侧走两三步（仰）能由躺的姿势");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("只会把玩具放入嘴巴或丢到地上，没有其他玩法如摇、捏、敲、拉等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("完全不会自己发声;或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能听懂简单的日常生活指令（如过来、给我、再见等。是真的能听得懂语言，而不是根据大人手势、表情作反应）");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会在大人提示下（语言加上手势）模仿做一些手势如拍拍手、再见、拜拜等");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("与大人有游戏的默契（如大人唱儿歌时能做出学习过的、固定的、简单的配合手势---例如去拍大人的手或伸出手指头等。若之前无此经验也可立即学习简单互动游戏如“give me five”） ");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以和人维持目光对视，大人说话、笑、玩躲猫猫、拿出玩具就可以把他逗笑");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("通常无法安静让大人抱着坐在大腿上，一直动来动去抱不住，手四处抓东西停不下来");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(1116);
        kidque.setMaxage(10215);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


















        kidque=new KidQue3();
        kidque.setQue("能不须扶东西自己站起来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以放手自己走");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面（大人可协助固定容器）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会想办法把丢进容器里的小东西取出来");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能听懂生活中常用的口头指令（如：喝奶奶、拍拍手、睡觉了、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("会在适当的情况下自己做出拍拍手、再见等手势");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("和照顾大人相处时可以维持目光对视，大人说话、笑、拿出玩具就可以把他逗笑");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();


        kidque=new KidQue3();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10216);
        kidque.setMaxage(10515);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();
















        kidque=new KidQue3();
        kidque.setQue("能不须扶东西自己由坐或躺的姿势站起来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("走得很稳（步态怪异如垫脚尖、左右不对称、停不下来、无法转弯、双脚张开距离超过肩膀宽度、双臂弯曲在身体两侧而非自然下垂或常常跌倒均不算通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("在少许支撑下能蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以拿笔随意涂涂画画（大人可先示范让小孩模仿）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("可以用一手拿小零食-如葡萄干、小馒头等，放入小容器-如底片盒里面");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能表达自己的意思（用说、比手势或眼神示意的方式-如点头摇头表示要不要，伸出手心向上表示“要”、用手指出需要的东西、要去的方向等。只会拉大人的手或衣服，且从来不用“指”的手势者不通过）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("能听懂并且遵从日常生活中半数的口头指令（如：给我xx、过来、拿给爸爸、把xx丢掉、坐下、妈妈抱抱等，必须在没有手势或表情的提示时也听懂）");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("自己记得常用东西藏放的地点（如玩具放哪里，鞋子摆哪里），可以随时把需要的东西找出来");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("自己会去找照顾大人陪他一起玩，大人说话、笑、玩玩具就可以把它逗乐");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();
        kidque=new KidQue3();
        kidque.setQue("高兴时会和别人分享喜悦：例如转头面对大人微笑，或把喜欢或得意的东西展示给大人看");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("完全不会自己发出声音；或只有嗯嗯啊啊的喉音；或能发出组合音种类（如ba、di、gu等）少于三种");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("持续出现不寻常的重复动作，如注视手、玩手、原地转圈等行为");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque=new KidQue3();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍然不理会，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(10516);
        kidque.setMaxage(11115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();



        kidque.setQue("在少数支撑下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够双手拿大东西—如搬小凳子或抱大玩具向前走一小段距离（约10步左右）不会跌倒");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("至少有10个稳定使用的词语（娃娃语“汪汪”为小狗亦可）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能正确只认至少一个图形（图1：大人依次问“哪个是笔？鞋子？钥匙？鱼？飞机？杯子？”全部问完再从头问一次，必须有两次均指对才算对，以避免儿童因乱指而猜对）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();


        kidque = new KidQue3();
        kidque.setQue("能正确指出至少四个身体部位（大人依次问“头、手、脚、眼、耳、鼻、嘴在哪里”）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能模仿做家事或使用大多数的家用器具（如扫地、用纸巾擦东西、使用开关等）");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("有主动探索学习动机，例如：转头面对大人微笑、或把喜欢或得意的东西展示给大人看 ");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("有主动学习的动机，例如：会自己把玩具找出来玩、或自己拿故事书出来翻看");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("无法模仿说单词，因为（1）根本没有仿说的动机，或（2）发音困难");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(11116);
        kidque.setMaxage(20515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶东西轻易蹲下或弯腰捡起地上的东西，然后恢复站的姿势");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能扶栏杆或墙壁走上楼梯");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能双脚离地跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("会旋开小瓶盖（大人先旋开一点点让瓶盖不会太紧）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("可以一页一页地翻阅硬卡书或布书");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("可以说出来的词语已经很多了，而且不多数不是单音，例如说“苹果”，而不是“果”");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("大多数时候能够使用两个词语组成的句子表达意思（如：妈妈—抱抱、要—喝水等）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能正确说出至少四个图形名称（图1：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能正确指出至少六个身体部位（大人依次问“头、手、脚、眼、耳，鼻、嘴在哪里？”）");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，说话连最清楚的大人也听不懂");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常自顾自玩，大人反复叫唤名字（或小名）多次仍不理睬，没有任何抬头、转头看、或回到大人身边的反应");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(20516);
        kidque.setMaxage(21115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("可以模仿画一条平稳的垂直线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常可以和人一问一答持续对话，使用2~3个单词的句子，且回答问题切题");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能正确说出至少四个图形名称（图2：大人依次指着笔、鞋子、钥匙、鱼、飞机、杯子的图形，并问“这是什么？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够听懂至少两个图形的描述句（图2：大人依次问“哪个是用来开门的？在水里游的？用来写字的？穿在脚上的？用来喝水的？在天空中飞？”）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("可以配对一样的图形（图2：大人分别指左侧的钥匙和右侧的笔问“那个图和这个一样”两项均指对才能通过）");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常无法正确使用代名词“你”、“我”，例如；（1）“你”、“我”颠倒，或（2）都用名字（或小名）表示自己而不说“我”");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(21116);
        kidque.setMaxage(30515);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("稍微扶栏杆或墙壁就能走上楼体");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能双脚离地连续跳跃（双脚必须同时能离地后同时落地，若明显的力量不对称而造成两脚不同时落地，则不算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("可以模仿画一条平稳的横线（图1：大人先做示范，在蜜蜂和花盆之间画一直线，然后让儿童模仿画；线条两端连接蜜蜂和花盆，为不断裂直线就算通过）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常可以和人一问一答持续对话，使用3~4个单词的句子，且回答问题切题");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能主动用至少一种句子问问题（例如：……是什么？为什么……？……在哪里？）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能说出至少三种东西的用途（图4：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”如果儿童第一题答不出，可以给提示“杯子是用来喝水的”。之后不在给提示）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够理解“大”（图2：问“哪个比较大”必须询问两次均正确才通过。图需要多次交换位置）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能正确指认一个颜色（图3：依次问“哪一个是红色？黄色？蓝色？绿色？”也可以替换为哪一个是苹果的红色？香蕉的黄色？天空的蓝色？树叶的绿色？全部问完再从头问一遍，必须两次均指对的颜色才对。）");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，说话连最亲近的大人也听不懂");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("经常自言自语说出一些固定的话，和当时情景无关、也不具沟通能力");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(30516);
        kidque.setMaxage(31115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能双脚离地跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常可以与人一问一答对话，使用4~5个单词的短句，且回答内容切题");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能说出一种颜色的名称（图1：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”说过1个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能听懂2个空间关系词（图2：先引导儿童注视图片上的牛头和四只小鸟，然后依次问“哪只小鸟在牛的上面？下面？前面？后面？”指对2个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("仿说“弟弟—想要—一辆—自行车”（大人念句子让儿童诵读，错误4个字及以上不通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能说出四种东西的用途（图3：大人用手依次指着杯子、鞋子、钥匙、铅笔的图形，并问“这个是做什么用的”说对四个通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能一次一个点数到5（图4：问“数一数这边有几个黑点点？”要求儿童一边指点一遍唱数必须前面5个点手指动作和嘴巴唱数能做一对一的配合，唱数到5没有错误才算通过）");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(31116);
        kidque.setMaxage(41115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶东西轻易地蹲下玩玩具，然后恢复站的姿势");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能跑（姿势怪异或长跌倒均不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能双脚离地连续跳跃（双脚必须能同时离地，然后同时落地，若明显的力量不对称而造成两脚高低不一，则不算通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能不须扶墙或栏杆走上楼体，而且一脚一阶");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("看图模仿画图1中三个图形（需线条不断裂、无严重越线或间隙、角数目正确且转弯无困难）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够向别人述说发生在自己身上的事情（如：转告老师交代的事情，描述学校发生的事件）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能说出四种颜色的名称（图2：用手依次指着红、黄、蓝、绿的圆圈并问“这是什么颜色？”）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("有“七个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能念读阿拉伯数字（图4：用手依次指着5、8、7、4、6、3、9、2并问“这是什么数字”答对7个通过）");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(41116);
        kidque.setMaxage(51115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够单脚跳4下（两脚均能做到才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够将纸大致对折并压出一条线（大人先示范，没有完全对其也可以通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够模仿简单的字（图1：大人现在“人”字下的空格仿写，然后指着其他空格说“照着写跟上面一样的字”三个字全部写对才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("通常能头尾清楚地说一个简单的故事（图2：用手指图（1）说“你看，这边有块香蕉皮。说说看这几张图里小朋友发生了什么事”适时以手依次指图（2）（3）（4），引导儿童说故事，记下儿童的语言反应）\n计分：儿童能说出图片中至少两个因果关系（如AB, AC, BC, ABC, 皆可）才算通过\n因果A：【因】不小心、没看见、踩到香蕉皮（滑滑的东西）→【果】摔倒、滑到、跌倒等\n因果B：【因】摔倒、滑到、跌倒等→【果】哭、坐在地上、长包包、受伤等\n因果C：【最后】妈妈（医生、护士、姐姐）来了、救他、帮他治疗、擦药、贴起来、粘起来");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够从1数到30（能够仅提示和纠正一次下完成才算通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("有“7个”的数量概念（图3：要求儿童“请你用笔一个一个数黑点，数到第7个就好”儿童如果圈6个或8个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("有“13个”的数量概念（图3：要求儿童“请你数一数这里有几个黑点”儿童如果数12个或14个，鼓励儿童再检查一次，以第二次表现计分）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够说出3个相对词（问“哥哥是男生，姐姐是__？夏天很热，冬天很__？”飞机在天空飞，汽车在__？大象的鼻子长长的，老鼠的鼻子__？答对3题通过）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("具备基本常识（问：“一只手有几只手指头？你有几个眼睛？小猫有几只脚？消防车是什么？一加一等于多少？”答对4题通过。）");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(1);
        kidque.setWeight(1);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("口齿不清，常需要要求再说一遍或由照顾的大人传译才听得到");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("能够用句子表达，但说话明显不流畅，十句话里有两个出现结巴的现象，且持续半年以上");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("常常自言自语，或像录音机一样重复说自己有兴趣的事，不管别人的反应");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(2);
        kidque.save();

        kidque = new KidQue3();
        kidque.setQue("检测过程中非常不合作，出现下列任一行为如（1）不听说明、不看示范（2）眼睛不跟随大人的手指方向（3）不肯指给大人看（4）把大人的东西抢过去自己玩（5）跑来跑去抓不住（6）似乎听不懂指令");
        kidque.setMinage(51116);
        kidque.setMaxage(61115);
        kidque.setAnswer(-1);
        kidque.setWeight(1);
        kidque.save();

        Log.v("createque", "que1 创建完成" );


    }


}
