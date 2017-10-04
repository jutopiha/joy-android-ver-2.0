package com.joy.tiggle.joy.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joy.tiggle.joy.R;

/**
 * Created by CE-L-17 on 2017-10-04.
 */

public class QuestFragment extends Fragment {
    /*기본*/
    public static android.support.v4.app.Fragment newInstance(){
        QuestFragment fragment = new QuestFragment();
        return fragment;
    }

    /*기본*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    /*기본*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        View currentView = inflater.inflate(R.layout.fragment_quest, container, false);
        
        return currentView;
    }
}
