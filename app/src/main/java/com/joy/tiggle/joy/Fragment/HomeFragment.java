package com.joy.tiggle.joy.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.github.mikephil.charting.charts.PieChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.Activity.SigninActivity;
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
    private ImageView userProfilePicture;
    private Bitmap bitmap;

    private TextView name, point, recentExpense, todayExpense, weekExpense, cardTv;
    private RelativeLayout relative2;
    private View view1, view2;
    private int birth;

    //프로필 사진

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
        relative2 = (RelativeLayout)currentView.findViewById(R.id.relative2);
        view1 = (View)currentView.findViewById(R.id.view1);
        view2 = (View)currentView.findViewById(R.id.view2);
        cardTv = (TextView)currentView.findViewById(R.id.cardTv);

        // profile 사진
        userProfilePicture = (ImageView)currentView.findViewById(R.id.userCharacter);

        Thread mThread = new Thread() {
            public void run() {
                try{
                    URL url = new URL("https://graph.facebook.com/" + MainActivity.currentUserId + "/picture?type=large");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch(IOException ex) {

                }

            }
        };
        mThread.start();
        try {
            mThread.join();
            //userProfilePicture.setImageBitmap(bitmap);
            userProfilePicture.setBackground(new ShapeDrawable(new OvalShape()));


        } catch(InterruptedException e) {

        }

        // 로그인 여부 확인
        if(AccessToken.getCurrentAccessToken() == null) {
            Log.d("로그인여부확인","true");
        }
        else {
            Log.d("uid_test: ",MainActivity.currentUserId);
            Log.d("로그인여부확인","false");
            sendObject();
        }


        //piechart설정
        mChart = (PieChart)currentView.findViewById(R.id.idPieChart);

        mChart.setDescription(null);
        mChart.setRotationEnabled(true);
        mChart.setHoleRadius(70f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText("11월");
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

        List<LegendEntry> entries = new ArrayList<>();
        ArrayList<String>titleList = new ArrayList<String>();
        titleList.add("수입");
        titleList.add("지출");
        for (int i = 0; i < 2; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = MY_COLORS[i];
            entry.label = titleList.get(i);
            entries.add(entry);
        }

        l.setCustom(entries);

        if(birth >= 2005){
            //랜덤 값 만들기
            double randomValue = Math.random();
            int intValue = (int)(randomValue *4)+1;
            if(intValue == 1){
                cardTv.setText("꼼꼼한 기록이 성공을 만든다구~!");
            }
            else if(intValue == 2){
                cardTv.setText("퀘스트는 잘 진행하고 있지??");
            }
            else if(intValue == 3){
                cardTv.setText("오늘의 지출을 잊지말고 입력해줘~");
            }
            else{
                cardTv.setText("오늘의 금융지식 체크하러 가자~");
            }

            relative2.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }
        else{
            relative2.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
        }

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
            birth = Integer.parseInt(stringToJson.getString("birth"));
            name.setText(stringToJson.getString("name"));
            if(stringToJson.getString("point") == "null")
                point.setText("0 P");
            else
                point.setText(stringToJson.getString("point")+" Point");


            if(stringToJson.getString("recentExpense") == "null")
                recentExpense.setText("0원");
            else
                recentExpense.setText(stringToJson.getString("recentExpense")+"원");

            if(stringToJson.getString("todayExpense") == "null")
                todayExpense.setText("0원");
            else
                todayExpense.setText(stringToJson.getString("todayExpense")+"원");

            if(stringToJson.getString("weeklyExpense") == "null")
                weekExpense.setText("0원");
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
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "수입 / 지출");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(12);


        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(247, 120, 107));
        colors.add(Color.rgb(145, 168, 208));

        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        mChart.setData(pieData);
        mChart.invalidate();
    }
}
