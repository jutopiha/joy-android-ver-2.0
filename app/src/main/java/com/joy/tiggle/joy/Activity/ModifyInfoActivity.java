package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CE-L-17 on 2017-11-12.
 */

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView userProfile;
    private EditText editUserName, editUserBirth;
    private Spinner editUserGender;
    private Bitmap bitmap;
    private JSONObject jsonObject = new JSONObject();

    private String male="Male", female="Female";
    InputMethodManager imm; //화면 터치시 키보드 내리기 위한

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        setCustomActionBar();   //상단 바 숨기기
        bindView(); //findViewById
        getProfile(); //프로필 사진 받기
        sendObject();
    }

    //상단 바 숨기기
    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }

    //findViewById
    private void bindView(){
        userProfile = (ImageView)findViewById(R.id.userProfile);
        editUserName = (EditText)findViewById(R.id.etUserName);
        editUserBirth = (EditText)findViewById(R.id.etUserBirth);
        editUserGender = (Spinner)findViewById(R.id.etUserGender);
    }

    //프로필 사진 받기
    private void getProfile(){
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
            userProfile.setImageBitmap(bitmap);
            userProfile.setBackground(new ShapeDrawable(new OvalShape()));


        } catch(InterruptedException e) {

        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSubmit:
                //null값이 있을 경우 처리
                String getEditName = editUserName.getText().toString();
                String getEditBirth = editUserBirth.getText().toString();
                getEditName = getEditName.trim();   //공백만 있을 경우도 안되게
                getEditBirth = getEditBirth.trim();

                if(getEditName.getBytes().length <= 0){//빈값이 넘어올때의 처리

                    Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(getEditBirth.getBytes().length <= 0){
                    Toast.makeText(getApplicationContext(), "출생년도를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendUserInfoUpdate();
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
        }
    }


    //처음 정보 받아오기
    private void sendObject(){
        Log.d("sendObject","started.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ModifyInfoActivity.GetUserInfo request = new ModifyInfoActivity.GetUserInfo();
        request.run();
    }

    //처음 정보 받아오기
    private class GetUserInfo extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    //처음 정보 받아오기
    public String postData(){
        Log.d("postData","started.");
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
            showUserInfo(result.toString());

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

    //받아온 정보 보여주기
    public void showUserInfo(String jsonString){
        Log.d("showUserInfo","started");

        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            //데이터 뽑아내서 필요한 곳에 저장하는 부분
            if(stringToJson.getString("name") != "null")
                editUserName.setText(stringToJson.getString("name"));

            if(stringToJson.getString("birth") != "null")
                editUserBirth.setText(stringToJson.getString("birth"));

            String test=stringToJson.getString("gender");
            Log.d("gender_test : ",test);
            if(male.equals(stringToJson.getString("gender")))
                editUserGender.setSelection(0);
            else if(female.equals(stringToJson.getString("gender")))
                editUserGender.setSelection(1);

        }
        catch (JSONException e) {
        }

    }

    //update정보 받아오기
    private void sendUserInfoUpdate(){
        try{
            String name = editUserName.getText().toString();
            String birth = editUserBirth.getText().toString();
            String gender = editUserGender.getSelectedItem().toString();

            if(gender.equals("남자")) jsonObject.put("gender","Male");
            else jsonObject.put("gender","Female");
            jsonObject.put("name",name);
            jsonObject.put("birth",birth);

        }catch(JSONException e){
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ModifyInfoActivity.UpdateUserInfo request = new ModifyInfoActivity.UpdateUserInfo();
        request.run();
    }

    //update정보 받아오기
    private class UpdateUserInfo extends Thread{
        @Override
        public void run(){

            updateUserInfoData(jsonObject);
        }
    }

    //update정보 저장하기
    public String updateUserInfoData(JSONObject data){

        String msg = MainActivity.urlString+ "/userinfo";

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

    //화면 터치시 키보드 내리기
    public void linearOnClick(View view){
        imm.hideSoftInputFromWindow(editUserName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editUserBirth.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(intent);
        finish();

    }
}
