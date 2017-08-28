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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private NumberPicker yearPicker;
    private NumberPicker monthPicker;
    private int selectDay;

    //파이차트 관련
    private PieChart mChart;
    private ArrayList<Integer> incomeExpenseMoneyData = new ArrayList<Integer>();
    private ArrayList<String> incomeExpenseLabel = new ArrayList<String>();
    public static final int[] MY_COLORS = {
            Color.rgb(247, 120, 107), Color.rgb(145, 168, 208)};

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
        mChart.setCenterText("8월");
        mChart.setCenterTextSize(10);

        addDataSet();


        //버튼리스너
        mChartBtn = (Button)view.findViewById(R.id.chartBtn);

        mChartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int year = yearPicker.getValue();
                int month = monthPicker.getValue();
                selectDay = year*100 + month;

                sendObject();

                mChart.setRotationEnabled(true);
                mChart.setHoleRadius(70f);
                mChart.setTransparentCircleAlpha(0);
                mChart.setCenterText(month+"월");
                mChart.setCenterTextSize(10);
                addDataSet();
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
        //showChart();
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
            Iterator<?> keys = stringToJson.keys();


            while(keys.hasNext()){
                String key = (String) keys.next();
                Log.d("fieldddd :", stringToJson.get(key).toString());  //label 따로 저장해야함
                //compareInExLabel.add(stringToJson.get(key).toString());
                //compaareInExUserMoneyData.add(stringToJson.getInt(key));
            }


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

        //add legend to chart
        Legend legend = mChart.getLegend();
        legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(5f);
        legend.setYEntrySpace(5f);

        legend.setCustom(MY_COLORS, new String[]{"수입","지출"});


        //create pie data object
        ArrayList<String>xVals = new ArrayList<String>();
        xVals.add("수입");
        xVals.add("지출");
        PieData pieData = new PieData(pieDataSet);
        mChart.setData(pieData);
        mChart.invalidate();
    }
}
