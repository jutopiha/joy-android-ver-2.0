package com.joy.tiggle.joy.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.R;

/**
 * Created by 조현정 on 2017-08-27.
 *
 * 수입 입력하기 관련 java
 */

public class IncomeFragment extends Fragment {
    public static final String PAGE_TITLE = "수입";

    public IncomeFragment(){

    }

    public static IncomeFragment newInstance(){
        IncomeFragment fragment = new IncomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

}
