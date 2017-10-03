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

import static com.joy.tiggle.joy.Activity.SettingActivity.customDialog;


/**
 * Created by Lee Juha on 2017-08-27.
 */

public class CharacterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mainCharacter;


    private ImageView characterImageView1, characterImageView2, characterImageView3, characterImageView4, characterImageView5, characterImageView6;
    private TextView characterName1, characterName2, characterName3, characterName4, characterName5, characterName6;
    private int characterHas[] = {0,0,0,0,0,0};
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
            /*case R.id.relativeCharacter1:
                if(characterHas[0] == 0){
                    //캐릭터가 잠금 되어있을때, 아이템 수를 보고 잠금 해제 여부를 판단한다.
                }
                else{
                    //캐릭터 잠금 해제 되어있을 때, 메인 캐릭터 변경 가능하도록 해준다.
                    //customDialog = new CustomDialog(this, "티끌모아 태산", "내용",mClickOKayListener, mClickCancleListener);
                    //customDialog.show();
                }
                break;
            case R.id.relativeCharacter2:
                if(characterHas[1] == 0){

                }
                else{

                }*/
        }
    }
    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }


    public void onClickShop(View v){
        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendObject(){
        Log.d("sendOjbect","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CharacterActivity.GetcharcterMain request = new CharacterActivity.GetcharcterMain();
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

    private class GetcharcterMain extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public void showCharecterMain(String jsonString) {
        Log.d("showMonthlyStat", "started");
        int mainCharacterNumber;
        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈
            mainCharacterNumber = stringToJson.getInt("main");

            //메인캐릭터 넘버에 따라 캐릭터 출력이 달라짐.
            switch(mainCharacterNumber){
                case 1:
                    //Log.d("test", "case1 income");
                    mainCharacter.setImageResource(R.drawable.character1);
                    break;
                case 2:
                    mainCharacter.setImageResource(R.drawable.character2);
                    break;
                case 3:
                    mainCharacter.setImageResource(R.drawable.character3);
                    break;
                case 4:
                    mainCharacter.setImageResource(R.drawable.character4);
                    break;
                case 5:
                    mainCharacter.setImageResource(R.drawable.character5);
                    break;
                case 6:
                    mainCharacter.setImageResource(R.drawable.character6);
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
}
