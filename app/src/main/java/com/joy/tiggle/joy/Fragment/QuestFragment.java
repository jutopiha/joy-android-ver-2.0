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
 *
 * 수입/지출 Fragment를 갖고 있는 부모Fragment
 */

public class QuestFragment extends Fragment {

    ViewPager viewPager;
    ViewPagerAdapterVer2 viewPagerAdapter;

    public static android.support.v4.app.Fragment newInstance(){
        QuestFragment fragment = new QuestFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        View view = inflater.inflate(R.layout.fragment_quest, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.pagerview);
        viewPagerAdapter = new ViewPagerAdapterVer2(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        return view;
    }

    public static class ViewPagerAdapterVer2 extends FragmentStatePagerAdapter {
        private static final int NUM_ITEMS = 2;

        public ViewPagerAdapterVer2(FragmentManager fm) {super(fm);}

        @Override
        public int getCount() {return NUM_ITEMS; }

        @Override
        public Fragment getItem(int position){
            if(position == 0){
                return WeeklyQuestFragment.newInstance();
            }
            else{
                return MonthlyQuestFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position){
            if(position == 0){
                return WeeklyQuestFragment.PAGE_TITLE;
            }
            else{
                return MonthlyQuestFragment.PAGE_TITLE;
            }
        }
    }

}
