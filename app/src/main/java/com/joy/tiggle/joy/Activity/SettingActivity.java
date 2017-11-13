package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.joy.tiggle.joy.Dialog.ButtonsCustomDialog;
import com.joy.tiggle.joy.Dialog.CustomDialog;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lee Juha on 2017-08-25.
 */


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch switchAutoParse, switchAutoPush;
    private int parseInfo, pushInfo;

    public static CustomDialog customDialog;

    //Back Button 2번 클릭시 앱 종료, 1번누르면 main으로
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchAutoParse = (Switch)findViewById(R.id.switchAutoParse);

        switchAutoParse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(parseInfo == 0) parseInfo = 1;
                else    parseInfo = 0;
                sendParseUpdate();
                sendObject();
            }
        });
        switchAutoPush = (Switch)findViewById(R.id.switchParseComplete);
        switchAutoPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pushInfo == 0) pushInfo = 1;
                else pushInfo = 0;
                sendPushUpdate();
                sendObject();
            }
        });


        setCustomActionBar();
        sendObject();
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnSignOut:
                intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnModifyInfo:
                intent = new Intent(getApplicationContext(), ModifyInfoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnPCMode:
                intent = new Intent(getApplicationContext(), PCGuideActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnAbout:
                customDialog = new CustomDialog(this, "Hongik Univ. CE", mClickCloseListener);
                customDialog.show();
                break;
            case R.id.btnTutorial:
                intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                finish();
        }

    }


    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }

    Button.OnClickListener mClickCloseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customDialog.dismiss();     // Custom Dialog 종료
        }
    };


    @Override
    public void onBackPressed() {
        //Back Button 2번 클릭시 앱 종료

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            //2번 눌렀을때
            super.onBackPressed();
        }
        else
        {
            //한번 눌렀을때
            backPressedTime = tempTime;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void sendObject(){
        Log.d("sendOjbect","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SettingActivity.GetSettingMain request = new SettingActivity.GetSettingMain();
        request.run();
    }

    private class GetSettingMain extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public String postData(){
        Log.d("postData","started.");
        String msg = MainActivity.urlString+"/setting";

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
            showSettingMain(result.toString());

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

    public void showSettingMain(String jsonString) {
        Log.d("showSettingMain", "started");

        try {

            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            int temp_value;
            temp_value = stringToJson.getJSONObject("DATA").getInt("onAutoParse");
            parseInfo = temp_value;
            if(temp_value == 0) switchAutoParse.setChecked(false);   //해제되어있음
            else switchAutoParse.setChecked(true);

            temp_value = stringToJson.getJSONObject("DATA").getInt("onAutoAlarm");
            pushInfo = temp_value;
            if(temp_value == 0) switchAutoPush.setChecked(false);
            else    switchAutoPush.setChecked(true);

        }
        catch(JSONException e){
            Log.d("showItemMain", "unexpected error");
        }
    }

    //자동파싱 변경 서버에 보내기
    private void sendParseUpdate(){
        Log.d("sendParseUpdate","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SettingActivity.UpdateParse request = new SettingActivity.UpdateParse();
        request.run();
    }

    private class UpdateParse extends Thread{
        @Override
        public void run(){
            updateParseData();
        }
    }

    public String updateParseData(){
        Log.d("postData","started.");
        String msg = MainActivity.urlString+"/setting/onautoparse";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("value", String.valueOf(parseInfo)));
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
            showSettingMain(result.toString());

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

    //푸쉬알림 설정변경 서버에 보내기
    private void sendPushUpdate(){
        Log.d("sendPushUpdate","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SettingActivity.UpdatePush request = new SettingActivity.UpdatePush();
        request.run();
    }

    private class UpdatePush extends Thread{
        @Override
        public void run(){
            updatePushData();
        }
    }

    public String updatePushData(){
        Log.d("updatePushData","started.");
        String msg = MainActivity.urlString+"/setting/onautoalarm";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("value", String.valueOf(pushInfo)));
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
            showSettingMain(result.toString());

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
