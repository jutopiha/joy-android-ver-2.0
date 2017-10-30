package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joy.tiggle.joy.ButtonsCustomDialog;
import com.joy.tiggle.joy.CustomDialog;
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
import java.util.Objects;

import static com.joy.tiggle.joy.Activity.SettingActivity.customDialog;


/**
 * Created by Lee Juha on 2017-08-27.
 */

public class CharacterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainCharacter;


    private ImageView characterImageView1, characterImageView2, characterImageView3, characterImageView4, characterImageView5, characterImageView6;
    private TextView characterName1, characterName2, characterName3, characterName4, characterName5, characterName6;
    private TextView mainCharacterName;
    private int characterHas[] = {0,0,0,0,0,0};
    private ButtonsCustomDialog mCustomDialog;
    private int mainCharacterNumber, selectCharacterNumber;
    private String unlockResult;

    //Back Button 2번 클릭시 앱 종료, 1번누르면 main으로
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        setCustomActionBar();
        mainCharacter = (ImageView)findViewById(R.id.imageMainCharacter);
        characterImageView1 = (ImageView)findViewById(R.id.characterImageView1);
        characterImageView2 = (ImageView)findViewById(R.id.characterImageView2);
        characterImageView3 = (ImageView)findViewById(R.id.characterImageView3);
        characterImageView4 = (ImageView)findViewById(R.id.characterImageView4);
        characterImageView5 = (ImageView)findViewById(R.id.characterImageView5);
        characterImageView6 = (ImageView)findViewById(R.id.characterImageView6);

        characterName1 = (TextView)findViewById(R.id.characterName1);
        characterName2 = (TextView)findViewById(R.id.characterName2);
        characterName3 = (TextView)findViewById(R.id.characterName3);
        characterName4 = (TextView)findViewById(R.id.characterName4);
        characterName5 = (TextView)findViewById(R.id.characterName5);
        characterName6 = (TextView)findViewById(R.id.characterName6);

        mainCharacterName = (TextView)findViewById(R.id.tvCharacterName);


        sendObject();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.relativeShop:
                intent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.relativeCharacter1:
                if(characterHas[0] == 0){
                    //캐릭터가 잠금 되어있을때, 아이템 수를 보고 잠금 해제 여부를 판단한다.
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 1;
                    mCustomDialog.show();
                }
                else{
                    //캐릭터가 잠금 해제 되어있을때, 대표캐릭터 설정 여부를 물어본다.
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 1;
                    mCustomDialog.show();
                }
                break;
            case R.id.relativeCharacter2:
                if(characterHas[1] == 0){
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 2;
                    mCustomDialog.show();
                }
                else{
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 2;
                    mCustomDialog.show();
                }
                break;
            case R.id.relativeCharacter3:
                Log.d("relativeCharacter3","started");
                if(characterHas[2] == 0){
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 3;
                    mCustomDialog.show();
                }
                else{
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 3;
                    mCustomDialog.show();
                }
                break;
            case R.id.relativeCharacter4:
                if(characterHas[3] == 0){
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 4;
                    mCustomDialog.show();
                }
                else{
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 4;
                    mCustomDialog.show();
                }
                break;
            case R.id.relativeCharacter5:
                if(characterHas[4] == 0){
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 5;
                    mCustomDialog.show();
                }else{
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 5;
                    mCustomDialog.show();
                }
                break;
            case R.id.relativeCharacter6:
                if(characterHas[5] == 0){
                    mCustomDialog = new ButtonsCustomDialog(this, "캐릭터를 해제하시겠어요?", unlockListener, rightListener);
                    selectCharacterNumber = 6;
                    mCustomDialog.show();
                }else {
                    mCustomDialog = new ButtonsCustomDialog(this, "대표캐릭터로 설정하시겠어요?", leftListener, rightListener);
                    mainCharacterNumber = 6;
                    mCustomDialog.show();
                }
                break;
        }
    }

    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }

    /*
    public void onClickShop(View v){
        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
        startActivity(intent);
        finish();
    }*/

    private void sendObject(){
        Log.d("sendOjbect","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CharacterActivity.GetChracterMain request = new CharacterActivity.GetChracterMain();
        request.run();
    }

    public String postData(){
        Log.d("postData","들어왔다.");
        String msg = MainActivity.urlString+"/character";

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
            showCharecterMain(result.toString());

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

    private class GetChracterMain extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public void showCharecterMain(String jsonString) {
        Log.d("showMonthlyStat", "started");
        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈
            mainCharacterNumber = stringToJson.getInt("main");

            //메인캐릭터 넘버에 따라 캐릭터 출력이 달라짐.
            switch(mainCharacterNumber){
                case 1:
                    //Log.d("test", "case1 income");
                    mainCharacter.setImageResource(R.drawable.character1);
                    mainCharacterName.setText("아메리카노");
                    break;
                case 2:
                    mainCharacter.setImageResource(R.drawable.character2);
                    mainCharacterName.setText("캐릭터2");
                    break;
                case 3:
                    mainCharacter.setImageResource(R.drawable.character3);
                    mainCharacterName.setText("캐릭3");
                    break;
                case 4:
                    mainCharacter.setImageResource(R.drawable.character4);
                    mainCharacterName.setText("캐릭터4");
                    break;
                case 5:
                    mainCharacter.setImageResource(R.drawable.character5);
                    mainCharacterName.setText("캐릭터5");
                    break;
                case 6:
                    mainCharacter.setImageResource(R.drawable.character6);
                    mainCharacterName.setText("캐릭터6");
                    break;
                default:
                    mainCharacter.setImageResource(R.drawable.image_waterdrop);
            }

            //보유 캐릭터에 따라 출력이 달라짐.
            if(stringToJson.getJSONObject("list").getInt("1") == 0)     characterImageView1.setImageResource(R.drawable.question_mark);
            else{
                characterHas[0] = 1;
                characterImageView1.setImageResource(R.drawable.character1);
                characterName1.setText("아메리카노");
            }
            if(stringToJson.getJSONObject("list").getInt("2") == 0)     characterImageView2.setImageResource(R.drawable.question_mark);
            else{
                characterHas[1] = 1;
                characterImageView2.setImageResource(R.drawable.character2);
                characterName2.setText("캐릭터2");
            }
            if(stringToJson.getJSONObject("list").getInt("3") == 0)     characterImageView3.setImageResource(R.drawable.question_mark);
            else{
                characterHas[2]  =1;
                characterImageView3.setImageResource(R.drawable.character3);
                characterName3.setText("캐릭터3");
            }
            if(stringToJson.getJSONObject("list").getInt("4") == 0)     characterImageView4.setImageResource(R.drawable.question_mark);
            else{
                characterHas[3] = 1;
                characterImageView4.setImageResource(R.drawable.character4);
                characterName4.setText("캐릭터4");
            }
            if(stringToJson.getJSONObject("list").getInt("5") == 0)     characterImageView5.setImageResource(R.drawable.question_mark);
            else{
                characterHas[4] = 1;
                characterImageView5.setImageResource(R.drawable.character5);
                characterName5.setText("캐릭터 5");
            }
            if(stringToJson.getJSONObject("list").getInt("6") == 0)     characterImageView6.setImageResource(R.drawable.question_mark);
            else{
                characterHas[5] = 1;
                characterImageView6.setImageResource(R.drawable.character6);
                characterName6.setText("캐릭터 6");
            }

        }
        catch(JSONException e){
            Log.d("showMonthlyStat", "unexpected error");
        }
    }

    private View.OnClickListener leftListener = new View.OnClickListener(){
        public void onClick(View v){
            //선택한 대표 캐릭터 정보를 서버에게 보내고
            sendUpdateMainCharacter();

            //다시 로딩한다.
            sendObject();
            mCustomDialog.dismiss();

        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener(){
        public void onClick(View v){
            mCustomDialog.dismiss();
        }

    };

    private View.OnClickListener unlockListener = new View.OnClickListener(){
        public void onClick(View v){
            //Toast.makeText(getApplicationContext(), "캐릭터 잠금 해제", Toast.LENGTH_SHORT).show();
            //선택한 캐릭터 정보를 서버에게 보내고
            sendUnlockCharacter();
            Log.d("unlockResult",unlockResult);

            String temp = "\"success\"";
            if(unlockResult.equals("\"success\"")) sendObject();
            else    Toast.makeText(getApplicationContext(), "캐릭터 잠금 해제 실패", Toast.LENGTH_SHORT).show();

            mCustomDialog.dismiss();
        }
    };

    private void sendUpdateMainCharacter(){
        Log.d("sendUpdateMainCharacter","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CharacterActivity.GetcharcterMainUpdate request = new CharacterActivity.GetcharcterMainUpdate();
        request.run();
    }

    private class GetcharcterMainUpdate extends Thread{
        @Override
        public void run(){
            postDataUpdate();
        }
    }

    public String postDataUpdate(){
        Log.d("postDataUpdate","started");
        String msg = MainActivity.urlString+"/character/main";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("type", String.valueOf(mainCharacterNumber)));
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
            showCharecterMain(result.toString());

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

    private void sendUnlockCharacter(){
        Log.d("sendUnlockCharacter","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CharacterActivity.GetCharacterUnlock request = new CharacterActivity.GetCharacterUnlock();
        request.run();
    }

    private class GetCharacterUnlock extends Thread{
        @Override
        public void run(){
            postDataUnlock();
        }
    }

    public String postDataUnlock(){
        Log.d("postDataUnlock","started.");
        String msg = MainActivity.urlString+"/character/unlock";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("type", String.valueOf(selectCharacterNumber)));
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
            //showCharecterMain(result.toString());
            unlockResult = result.toString();

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
}