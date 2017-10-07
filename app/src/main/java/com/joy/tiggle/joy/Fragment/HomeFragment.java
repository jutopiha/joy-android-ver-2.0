package com.joy.tiggle.joy.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/**
 * Created by 조현정 on 2017-08-25.
 */

public class HomeFragment extends Fragment {

    /*파이차트 관련*/
    private PieChart mChart;
    private ArrayList<Integer>incomeExpenseMoneyData = new ArrayList<Integer>();    //수입,지출 금액
    private ArrayList<String> incomeExpenseLabel = new ArrayList<String>(); //수입, 지출

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
        View currentView = inflater.inflate(R.layout.fragment_home, container, false);

        //필요한 것들 findViewById

        name = (TextView)currentView.findViewById(R.id.userName);
        point = (TextView)currentView.findViewById(R.id.userPoint);
        recentExpense = (TextView)currentView.findViewById(R.id.recentExpenseContent);
        todayExpense = (TextView)currentView.findViewById(R.id.todayExpenseContent);
        weekExpense = (TextView)currentView.findViewById(R.id.weekExpenseContent);

        sendObject();

        //piechart설정
        mChart = (PieChart)currentView.findViewById(R.id.idPieChart);

        mChart.setDescription(null);
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(70f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("10월");
        mChart.setCenterTextSize(10);

        addDataSet();

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

        return currentView;
    }


    private void sendObject(){
        Log.d("sendOjbect","들어왔다.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HomeFragment.GetMainInfo request = new HomeFragment.GetMainInfo();
        request.run();
    }

    private class GetMainInfo extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public String postData(){
        Log.d("postData","들어왔다.");
        String msg = MainActivity.urlString+"/main";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
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
            showMainInfo(result.toString());

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

    public void showMainInfo(String jsonString){
        Log.d("showMainInfo","started");

        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            //데이터 뽑아내서 필요한 곳에 저장하는 부분
            name.setText(stringToJson.getString("name"));
            if(stringToJson.getString("point") == "null")
                point.setText("0 P");
            else
                point.setText(stringToJson.getString("point")+" Point");

            if(stringToJson.getString("recentExpense") == "null")
                recentExpense.setText("-");
            else
                recentExpense.setText(stringToJson.getString("recentExpense")+"원");

            if(stringToJson.getString("todayExpense") == "null")
                todayExpense.setText("-");
            else
                todayExpense.setText(stringToJson.getString("todayExpense")+"원");

            if(stringToJson.getString("weeklyExpense") == "null")
                weekExpense.setText("-");
            else
                weekExpense.setText(stringToJson.getString("weeklyExpense")+"원");


            //pieChart data채우기
            incomeExpenseMoneyData.clear();
            incomeExpenseLabel.clear();
            incomeExpenseMoneyData.add(stringToJson.getInt("monthlyIncome"));
            incomeExpenseMoneyData.add(stringToJson.getInt("monthlyExpense"));
            incomeExpenseLabel.add("수입");
            incomeExpenseLabel.add("지출");


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
}
