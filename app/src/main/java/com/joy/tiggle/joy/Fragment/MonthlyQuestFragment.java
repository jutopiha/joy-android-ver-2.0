package com.joy.tiggle.joy.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
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
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by 조현정 on 2017-08-27.
 *
 * 수입 입력하기 관련 java
 */

public class MonthlyQuestFragment extends Fragment {

    public static final String PAGE_TITLE = "월간 지출 목표";
    private int userGoalMoney;    //사용자 목표 금액 입력받아서 저장.

    private Button mObjectBtn,mDeleteBtn, mCompleteBtn;
    private TextView mStartDay, mEndDay, mObjectMoney, mRealMoney;
    private RelativeLayout layoutRegister, layoutInfo;
    private JSONObject jsonObject = new JSONObject(); // for temp
    private String tempType, tempStartDay, tempEndDay, tempObjectMoney, tempRealMoney, tempClear;
    long now = System.currentTimeMillis(); //현재시간을 msec로 구함.
    Date date = new Date(now);  // 현재 시간을 date변수에 저장
    SimpleDateFormat sdfNow = new SimpleDateFormat("MMdd");   //시간 나타낼 포맷 정함
    String formatDate = sdfNow.format(date);    //nowDate변수에 값을 저장
    private int nowDate = Integer.parseInt(formatDate);
    private int endDate;

    public MonthlyQuestFragment(){

    }

    public static MonthlyQuestFragment newInstance(){
        MonthlyQuestFragment fragment = new MonthlyQuestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View currentView = (View)inflater.inflate(R.layout.fragment_monthly_quest, container, false);

        //필요한 findViewById
        mObjectBtn = (Button) currentView.findViewById(R.id.btnAddQuest);
        mDeleteBtn = (Button) currentView.findViewById(R.id.btnDelete);
        mCompleteBtn = (Button) currentView.findViewById(R.id.btnClear);
        mStartDay = (TextView)currentView.findViewById(R.id.tvStartDay);
        mEndDay = (TextView) currentView.findViewById(R.id.tvEndDay);
        mObjectMoney = (TextView)currentView.findViewById(R.id.tvQuestMoney);
        mRealMoney = (TextView)currentView.findViewById(R.id.tvRealMoney);
        layoutRegister = (RelativeLayout)currentView.findViewById(R.id.relativeQuestRegister);
        layoutInfo = (RelativeLayout)currentView.findViewById(R.id.relativeQuestDetail);

        //버튼리스너
        mObjectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //AlertDialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("월간 지출 목표 설정");
                alert.setMessage("목표 금액을 입력해주세요");

                final EditText money = new EditText(getActivity());
                money.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(money);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userGoalMoney = Integer.parseInt(money.getText().toString());
                        sendUserObjectMoney();
                        sendObject();
                        if(tempType == null)  {
                            layoutInfo.setVisibility(View.GONE);
                            layoutRegister.setVisibility(View.VISIBLE);
                        }
                        else{
                            layoutRegister.setVisibility(View.GONE);
                            mStartDay.setText(tempStartDay);
                            mEndDay.setText(tempEndDay);
                            mObjectMoney.setText(tempObjectMoney);
                            mRealMoney.setText(tempRealMoney);
                            layoutInfo.setVisibility(View.VISIBLE);

                            if(nowDate < endDate) {
                                mCompleteBtn.setEnabled(false);
                            }
                            else{
                                mCompleteBtn.setEnabled(true);
                            }
                        }
                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        sendObject();

        if(nowDate < endDate) {
            mCompleteBtn.setEnabled(false);
        }
        else{
            mCompleteBtn.setEnabled(true);
        }

        if(tempType == null)  {
            layoutInfo.setVisibility(View.GONE);
        }
        else{
            layoutRegister.setVisibility(View.GONE);
            mStartDay.setText(tempStartDay);
            mEndDay.setText(tempEndDay);
            mObjectMoney.setText(tempObjectMoney);
            mRealMoney.setText(tempRealMoney);
            if(nowDate < endDate) {
                mCompleteBtn.setEnabled(false);
            }
            else{
                mCompleteBtn.setEnabled(true);
            }
        }

        //삭제버튼 리스너
        mDeleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "삭제완료", Toast.LENGTH_LONG).show();
                deleteMonthlyQuestObject();
                sendObject();
                layoutInfo.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);
            }
        });

        //완료버튼 리스너
        mCompleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearMonthlyQuestObject();

                if(tempClear == "success")  Toast.makeText(getApplicationContext(), "퀘스트 성공! 아이템과 포인트를 확인하세요", Toast.LENGTH_LONG).show();
                else   Toast.makeText(getApplicationContext(), "퀘스트 실패! 다시 도전해 보세요", Toast.LENGTH_LONG).show();

                layoutInfo.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);

            }
        });

        return currentView;
    }

    private void sendUserObjectMoney(){
        Log.d("sendUserObjectMoney","started.");

        try {
            jsonObject.put("type","monthly");
            jsonObject.put("money",userGoalMoney);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MonthlyQuestFragment.SaveNewMonthlyQuest request = new MonthlyQuestFragment.SaveNewMonthlyQuest();
        request.run();
    }

    private class SaveNewMonthlyQuest extends Thread
    {
        @Override
        public void run() {

            postWeeklyQuestMoneyData(jsonObject);

        }
    }

    public String postWeeklyQuestMoneyData(JSONObject data) {

        String msg = MainActivity.urlString + "/quest/start";

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
        HttpPost httpPost = new HttpPost(requestUrl.toString());
        Log.d("msg is :", requestUrl.toString());

        try {

            String json="";
            json=data.toString();

            // loglog
            Log.v("^^^^^json", json);

            StringEntity stringEntity=new StringEntity(json, "utf-8");
            httpPost.setEntity(stringEntity);

            //answer객체 서버로 전송하고 survey객체 받아오는 과정

            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.v("******server", "send msg successed");

            inputStream = httpResponse.getEntity().getContent();
            rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.v("Main::bring success", "result:" + result.toString());

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

    private void sendObject(){
        Log.d("QuestsendObject","started.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MonthlyQuestFragment.GetQuestInfo request = new MonthlyQuestFragment.GetQuestInfo();
        request.run();
    }

    private class GetQuestInfo extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public String postData(){
        Log.d("QuestpostData","started");
        String msg = MainActivity.urlString+"/quest";

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
            showQuestInfo(result.toString());

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

    public void showQuestInfo(String jsonString){
        Log.d("QuestshowQuestInfo","started");

        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            //데이터 뽑아내서 필요한 곳에 저장하는 부분
            int temp, year, month, day;

            /*월간 퀘스트*/
            tempType = stringToJson.getJSONObject("monthly").getString("type");

            temp = Integer.parseInt(stringToJson.getJSONObject("monthly").getString("startDate"));
            year = temp/10000;
            month = (temp - year*10000)/100;
            day = temp - year*10000 - month*100;
            tempStartDay = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

            temp = Integer.parseInt(stringToJson.getJSONObject("monthly").getString("endDate"));
            year = temp/10000;
            month = (temp - year*10000)/100;
            day = temp - year*10000 - month*100;
            endDate = month*100 + day;
            tempEndDay = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

            tempObjectMoney = stringToJson.getJSONObject("monthly").getString("goalMoney");
            tempRealMoney = stringToJson.getJSONObject("monthly").getString("nowMoney");

        }
        catch (JSONException e) {
        }
    }

    private void deleteMonthlyQuestObject(){
        Log.d("deleteQuestObject","start");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MonthlyQuestFragment.DeleteQuest request = new MonthlyQuestFragment.DeleteQuest();
        request.run();
    }

    private class DeleteQuest extends Thread{
        @Override
        public void run(){
            deleteData();
        }
    }

    public String deleteData(){
        Log.d("deleteData","started");
        String msg = MainActivity.urlString+"/quest/giveup";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("type", "monthly"));
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
            //showQuestInfo(result.toString());

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

    //완료하기
    private void clearMonthlyQuestObject(){
        Log.d("clearMonthlyQuestObject","started.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MonthlyQuestFragment.ClearQuest request = new MonthlyQuestFragment.ClearQuest();
        request.run();
    }

    private class ClearQuest extends Thread{
        @Override
        public void run(){
            clearData();
        }
    }

    public String clearData(){
        Log.d("clearData","started");
        String msg = MainActivity.urlString+"/complete";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("type", "monthly"));
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

            try{
                JSONObject stringToJson = new JSONObject(result.toString());
                tempClear = stringToJson.getString("DATA");
            } catch(JSONException e){

            }

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
}
