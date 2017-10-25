package com.joy.tiggle.joy.Fragment;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.Object.Quest;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
