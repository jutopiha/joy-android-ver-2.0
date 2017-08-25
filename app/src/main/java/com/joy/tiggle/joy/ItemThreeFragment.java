package com.joy.tiggle.joy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 조현정 on 2017-08-25.
 */

public class ItemThreeFragment extends Fragment {
    public static android.support.v4.app.Fragment newInstance(){
        ItemThreeFragment fragment = new ItemThreeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        return inflater.inflate(R.layout.fragment_item_three, container, false);
    }
}
