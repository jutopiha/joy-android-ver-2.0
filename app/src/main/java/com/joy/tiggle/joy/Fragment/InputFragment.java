package com.joy.tiggle.joy.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.R;

/**
 * Created by 조현정 on 2017-08-25.
 */

public class ItemOneFragment extends Fragment {


    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    public static android.support.v4.app.Fragment newInstance(){
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        View view = inflater.inflate(R.layout.fragment_item_one, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.pagerview);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        return view;
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_ITEMS = 2;

        public ViewPagerAdapter(FragmentManager fm) {super(fm);}

        @Override
        public int getCount() {return NUM_ITEMS; }

        @Override
        public Fragment getItem(int position){
            if(position == 0){
                return IncomeFragment.newInstance();
            }
            else{
                return ExpenseFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position){
            if(position == 0){
                return IncomeFragment.PAGE_TITLE;
            }
            else{
                return ExpenseFragment.PAGE_TITLE;
            }
        }
    }
}
