package com.joy.tiggle.joy.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.joy.tiggle.joy.R;

/**
 * Created by 조현정 on 2017-08-25.
 */

public class HomeFragment extends Fragment {

    /*파이차트 관련*/
    private PieChart mChart;
    private ArrayList<Integer>incomeExpenseMoneyData = new ArrayList<Integer>();    //수입,지출 금액
    private ArrayList<String> incomeExpenseLabel = new ArrayList<String>(); //수입, 지출

    /*현재 시간 저장 관련*/
    long now = System.currentTimeMillis();  //현재 시간을 msec로 구함.
    Date date = new Date(now);  //현재 시간을 date변수에 저장
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMM"); // 시간 나타낼 포맷 정함
    String formatDate = sdfNow.format(date); //formateDate변수에 값을 저장
    private int selectDay;  //현재날짜 저장할 변수

    public static final int[] MY_COLORS = {
            Color.rgb(247, 120, 107), Color.rgb(145, 168, 208)};

    /*캐릭터, 이름, 포인트, 지출내역*/
    private ImageView character;
    private TextView name, point, recentExpense, todayExpense, weekExpense;

    /*기본*/
    public static android.support.v4.app.Fragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /*기본*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //mainInfoSetting();
    }

    /*기본*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

/*
    //pie chart setting하는데 필요한 것들
    public void mainInfoSetting(){

        selectDay = Integer.parseInt(formatDate);   //서버에게 전달할 yyyyMM int형으로 저장
        sendObject();

        character = (ImageView)getView().findViewById(R.id.userCharacter);  //캐릭터
        name = (TextView)getView().findViewById(R.id.userName); //이름
        point = (TextView)getView().findViewById(R.id.userPoint);   //포인트트

       mChart = (PieChart)getView().findViewById(R.id.idPieChart); //getview는 onCreateView 후에 써져야함
        mChart.setDescription("이번달 총 수입, 지출");
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(70f);  //두께
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText(Integer.toString(selectDay%100)+"월");

        addDataSet();

    }
*/
}
