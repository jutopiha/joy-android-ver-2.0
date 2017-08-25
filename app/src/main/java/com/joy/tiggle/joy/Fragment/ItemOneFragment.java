package com.joy.tiggle.joy.Fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.R;

/**
 * Created by 조현정 on 2017-08-25.
 */

public class ItemOneFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_item_one, container, false);
    }
}
