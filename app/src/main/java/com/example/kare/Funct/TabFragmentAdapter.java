package com.example.kare.Funct;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kare.R;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public TabFragmentAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext =context;
    }
    @Override
    public Fragment getItem(int position) {
        if(position ==0){
            return new GrowthFragment();

        }else if(position ==1){
            return new HeightFragment();

        }else if(position ==2){
            return new NutritonFragment();

        }else{
            return new PrescribeFragment();

        }
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.funct_growth);
        } else if (position == 1) {
            return mContext.getString(R.string.funct_height);
        } else if (position == 2) {
            return mContext.getString(R.string.funct_nutrition);
        } else {
            return mContext.getString(R.string.funct_prescribe);
        }
    }
}
