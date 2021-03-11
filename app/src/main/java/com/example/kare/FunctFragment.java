package com.example.kare;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kare.DB.Kiddata;
import com.example.kare.Funct.Kid.SelectKidsActivity;
import com.example.kare.Funct.TabFragmentAdapter;

import org.litepal.crud.DataSupport;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FunctFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunctFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mview;
    private ImageView selectkid;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FunctFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FunctFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FunctFragment newInstance(String param1, String param2) {
        FunctFragment fragment = new FunctFragment();
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
        Cursor  cursor = DataSupport.findBySQL("select * from Kiddata ");
        Log.v("cursor",""+!(cursor.moveToFirst()));
        if(cursor.getCount()==0){
            CreateKiddata();
            Log.v("cursor", "" +"ok");
            Log.v("cursor", "" +cursor.getCount());
        }else {
            Log.v("cursor", "" +"setup");
        }

        cursor.close();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mview = inflater.inflate(R.layout.fragment_funct, container, false);
        ViewPager viewPager = (ViewPager) mview.findViewById(R.id.functviewpager);
        // Create an adapter that knows which fragment should be shown on each page
        TabFragmentAdapter adapter = new TabFragmentAdapter(getActivity(),getChildFragmentManager());
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) mview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        selectkid= (ImageView) mview.findViewById(R.id.SelectKids);
        selectkid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectKidsActivity.class);
                startActivity(intent);

            }
        });
        return mview;
    }
    public  void CreateKiddata(){
        Kiddata kid1 = new Kiddata();
        kid1.setId(1);

        kid1.save();
        Kiddata kid2 = new Kiddata();
        kid2.setId(2);
        kid2.save();
        Kiddata kid3 = new Kiddata();
        kid3.setId(3);
        kid3.save();
    }
}
