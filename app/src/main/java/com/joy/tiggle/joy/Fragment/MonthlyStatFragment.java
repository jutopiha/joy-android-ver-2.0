package com.joy.tiggle.joy.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.joy.tiggle.joy.Activity.MainActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 조현정 on 2017-08-28.
 */

public class MonthlyStatFragment extends Fragment {

    int test = 0;
    private NumberPicker yearPicker;
    private NumberPicker monthPicker;
    private int selectDay;

    //파이차트 관련
    private PieChart mChart;
    private ArrayList<Integer> incomeExpenseMoneyData = new ArrayList<Integer>();
    private ArrayList<String> incomeExpenseLabel = new ArrayList<String>();
    public static final int[] MY_COLORS = {
            Color.rgb(247, 120, 107), Color.rgb(145, 168, 208)};

    //레이더차트 관련
    private RadarChart rChart;
    private ArrayList<Integer> mineExpenseMoneyData = new ArrayList<Integer>();
    private ArrayList<Integer> wholeExpenseMoneyData = new ArrayList<Integer>();

    /*현재시간 저장하기*/
    long now = System.currentTimeMillis(); //현재시간을 msec로 구함.
    Date date = new Date(now);  // 현재 시간을 date변수에 저장
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMM");   //시간 나타낼 포맷 정함
    String formatDate = sdfNow.format(date);    //nowDate변수에 값을 저장

    //버튼
    private Button mChartBtn;
    /*기본*/
    public static android.support.v4.app.Fragment newInstance(){
        Log.d("어디부터","1");
        MonthlyStatFragment fragment = new MonthlyStatFragment();
        return fragment;
    }

    /*기본*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("어디부터","2");
    }

    /*기본*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        Log.d("어디부터","3");
        selectDay = Integer.parseInt(formatDate);
        Log.d("dayday :", formatDate);
        sendObject();
        View view = inflater.inflate(R.layout.fragment_monthly_stat, container, false);


        //2015~2017까지 연도 picker만들기
        yearPicker = (NumberPicker)view.findViewById(R.id.selectYear);
        yearPicker.setMinValue(2014);
        yearPicker.setMaxValue(2017);
        yearPicker.setWrapSelectorWheel(false);
        yearPicker.setValue(2017);

        //1월부터 12월까지 month picker만들기
        monthPicker = (NumberPicker)view.findViewById(R.id.selectMonth);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(false);
        monthPicker.setOnLongPressUpdateInterval(100);
        monthPicker.setValue(10);

        //piechart설정
        mChart = (PieChart)view.findViewById(R.id.idPieChart);

        mChart.setDescription(null);
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(70f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("11월");
        mChart.setCenterTextSize(10);

        addDataSet();


        //add legend to chart
        Legend legend = mChart.getLegend();
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(5f);
        legend.setYEntrySpace(5f);

        //radarChart설정
        rChart = (RadarChart)view.findViewById(R.id.idRadarChart);
        rChart.setRotationEnabled(false);
        rChart.getDescription().setEnabled(false);
        rChart.setWebLineWidth(1f);
        rChart.setWebColor(Color.BLACK);
        rChart.setWebColorInner(Color.BLACK);
        rChart.setWebAlpha(100);

        setRadarChartData();

        rChart.animateXY(
                1400,1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);
        XAxis xAxis = rChart.getXAxis();

        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"식비", "교통비", "문화", "생활", "음료/간식","교육","공과금","기타"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = rChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(10000f);  //max값
        yAxis.setDrawLabels(true);  //숫자들

        Legend l = rChart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

        //버튼리스너
        mChartBtn = (Button)view.findViewById(R.id.chartBtn);

        mChartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int year = yearPicker.getValue();
                int month = monthPicker.getValue();
                selectDay = year*100 + month;

                sendObject();

                //파이차트 관련
                mChart.setRotationEnabled(true);
                mChart.setHoleRadius(70f);
                mChart.setTransparentCircleAlpha(0);
                mChart.setCenterText(month+"월");
                mChart.setCenterTextSize(10);
                addDataSet();

                //레이더차트 관련
                rChart.clear();
                rChart.setRotationEnabled(false);
                rChart.getDescription().setEnabled(false);
                rChart.setWebLineWidth(1f);
                rChart.setWebColor(Color.BLACK);
                rChart.setWebColorInner(Color.BLACK);
                rChart.setWebAlpha(100);
                setRadarChartData();
            }
        });

        return view;
    }

    private void sendObject(){
        Log.d("sendOjbect","들어왔다.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetMonthlyStat request = new GetMonthlyStat();
        request.run();
    }

    public String postData(){
        Log.d("postData","들어왔다.");
        String msg = MainActivity.urlString+"/statistic/monthly";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("date", Integer.toString(selectDay)));
        String querystring = URLEncodedUtils.format(nvps, "utf-8");

        requestUrl.append("?");
        requestUrl.append(querystring);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl.toString());
        Log.d("msg is :", requestUrl.toString());

        try {

            //answer객체 서버로 전송하고 survey객체 받아오는 과정

            HttpResponse httpResponse = httpClient.execute(httpGet);

            Log.v("******server", "send msg successed");

            inputStream = httpResponse.getEntity().getContent();
            rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.v("Main::bring success", "result:" + result.toString());
            showMonthlyStat(result.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("******server", "send msg failed");
        }



        if (result != null) {
            return result.toString();
        } else {
            return null;
        }

    }


    private class GetMonthlyStat extends Thread{
        @Override
        public void run(){
            postData();
        }
    }


    public void showMonthlyStat(String jsonString){
        Log.d("showMonthlyStat","들어왔다.");

        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈
            //pieChart data채우기
            incomeExpenseMoneyData.clear();
            incomeExpenseLabel.clear();
            incomeExpenseMoneyData.add(stringToJson.getInt("income"));
            incomeExpenseMoneyData.add(stringToJson.getInt("expense"));
            incomeExpenseLabel.add("수입");
            incomeExpenseLabel.add("지출");

            //RadarChart data채우기

            mineExpenseMoneyData.clear();
            wholeExpenseMoneyData.clear();
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("식비"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("교통비"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("문화"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("생활"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("음료/간식"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("교육"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("공과금"));
            mineExpenseMoneyData.add(stringToJson.getJSONObject("mine").getInt("기타"));

            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("식비"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("교통비"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("문화"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("생활"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("음료/간식"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("교육"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("공과금"));
            wholeExpenseMoneyData.add(stringToJson.getJSONObject("whole").getInt("기타"));

            test++;

        }
        catch (JSONException e) {
        }

    }

    private void addDataSet(){
        Log.d("msg: ", "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < incomeExpenseLabel.size(); i++){
            yEntrys.add(new PieEntry(incomeExpenseMoneyData.get(i) , i));
        }

        for(int i = 1; i < incomeExpenseMoneyData.size(); i++){
            xEntrys.add(incomeExpenseLabel.get(i));
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "월통계");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(247, 120, 107));
        colors.add(Color.rgb(145, 168, 208));

        pieDataSet.setColors(colors);

        //create pie data object
        ArrayList<String>xVals = new ArrayList<String>();
        xVals.add("수입");
        xVals.add("지출");
        PieData pieData = new PieData(pieDataSet);
        mChart.setData(pieData);
        mChart.invalidate();
    }

    public void setRadarChartData(){

        Log.d("msg: ", "setRadarChartData started");
        int cnt = 8;    //꼭짓점 개수
        ArrayList<RadarEntry> entries_mine = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries_whole = new ArrayList<RadarEntry>();

        if(mineExpenseMoneyData.size()!= 0) {
            for (int i = 0; i < cnt; i++) {
                Log.d(i+"번째들어가는값들:",Integer.toString(mineExpenseMoneyData.get(i)));
                entries_mine.add(new RadarEntry(mineExpenseMoneyData.get(i)));
                entries_whole.add(new RadarEntry(wholeExpenseMoneyData.get(i)));
            }
        }



        RadarDataSet set1 = new RadarDataSet(entries_mine, "내 지출");
        set1.setColor(Color.rgb(145, 168, 208));
        set1.setFillColor(Color.rgb(145, 168, 208));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries_whole, "전체 사용자");
        set2.setColor(Color.rgb(239, 181, 88));
        set2.setFillColor(Color.rgb(239, 181, 88));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        //data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        rChart.setData(data);
        rChart.invalidate();
    }
}
