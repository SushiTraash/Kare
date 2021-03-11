package com.example.kare.Funct;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kare.DB.Kiddata;
import com.example.kare.R;

import org.litepal.crud.DataSupport;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrescribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrescribeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Cursor kidcur ;

    private int mage;
    private int msex;
    private int mba;
    private String mname;

    private TextView ageview;
    private TextView nameview;
    private TextView sexview ;



    public PrescribeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrescribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrescribeFragment newInstance(String param1, String param2) {
        PrescribeFragment fragment = new PrescribeFragment();
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
        mview= inflater.inflate(R.layout.fragment_prescribe, container, false);

        ageview=mview.findViewById(R.id.kid_age);
        nameview=mview.findViewById(R.id.kid_name);
        sexview = mview.findViewById(R.id.kid_sex);

        GetInfo();
        ShowKid();
        return mview;
    }
    @Override
    public void onResume() {
        super.onResume();
        GetInfo();
        ShowKid();
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

        }
    }
    public void ShowKid(){

        if(msex==0){
            sexview.setText("女");
        }else sexview.setText("男");
        nameview.setText(mname);
        Cursor heightcur =DataSupport.findBySQL("SELECT * FROM Height WHERE kid = ? AND age =?",""+Kiddata.selected,""+mage);
        Cursor weightcur =DataSupport.findBySQL("SELECT * FROM Weight WHERE kid = ? AND age =?",""+Kiddata.selected,""+mage);
        ageview.setText(kidcur.getLong(kidcur.getColumnIndex("year_age"))+"岁 "+kidcur.getLong(kidcur.getColumnIndex("month_age"))+" 月"+kidcur.getLong(kidcur.getColumnIndex("day_age"))+"天");

    }
}
