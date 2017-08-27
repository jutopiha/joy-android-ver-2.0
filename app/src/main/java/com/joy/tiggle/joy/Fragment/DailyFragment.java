package com.joy.tiggle.joy.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.R;

/**
 * Created by Lee Juha on 2017-08-26.
 */

public class DailyFragment extends Fragment {
    public static android.support.v4.app.Fragment newInstance(){
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }
}