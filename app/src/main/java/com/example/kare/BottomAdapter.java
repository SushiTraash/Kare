package com.example.kare;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BottomAdapter extends FragmentPagerAdapter {
    Context mContext;
    public BottomAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext =context;
    }
    @Override
    public Fragment getItem(int position) {
        if(position ==0){
            return new FunctFragment();

        }else if(position ==1){
            return new CourseFragment();

        }else if(position ==2){
            return new SettingFragment();

        }else{
            return new AccountFragment();

        }
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.title_funct);
        } else if (position == 1) {
            return mContext.getString(R.string.title_course);
        } else if (position == 2) {
            return mContext.getString(R.string.title_setting);
        } else {
            return mContext.getString(R.string.title_account);
        }
    }
}
