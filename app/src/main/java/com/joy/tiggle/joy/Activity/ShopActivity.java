package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Lee Juha on 2017-08-27.
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userPoint;
    private TextView items[] = new TextView[11];
    private TextView itembuy[] = new TextView[11];
    private int selectItems[] = {0,0,0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        bindView();
        sendObject();

        setCustomActionBar();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnadd1:
                Toast.makeText(getApplicationContext(), "플러스", Toast.LENGTH_SHORT).show();
                selectItems[0]++;
                itembuy[0].setText(String.valueOf(selectItems[0]));
                break;
            case R.id.btnminus1:
                if(selectItems[0] >=1) selectItems[0]--;
                itembuy[0].setText(String.valueOf(selectItems[0]));
                break;
            case R.id.btnadd2:
                selectItems[1]++;
                itembuy[1].setText(String.valueOf(selectItems[1]));
                break;
            case R.id.btnminus2:
                if(selectItems[1] >=1) selectItems[1]--;
                itembuy[1].setText(String.valueOf(selectItems[1]));
                break;
            case R.id.btnadd3:
                selectItems[2]++;
                itembuy[2].setText(String.valueOf(selectItems[2]));
                break;
            case R.id.btnminus3:
                if(selectItems[2] >=1) selectItems[2]--;
                itembuy[2].setText(String.valueOf(selectItems[2]));
                break;
            case R.id.btnadd4:
                selectItems[3]++;
                itembuy[3].setText(String.valueOf(selectItems[3]));
                break;
            case R.id.btnminus4:
                if(selectItems[3] >=1) selectItems[3]--;
                itembuy[3].setText(String.valueOf(selectItems[3]));
                break;
            case R.id.btnadd5:
                selectItems[4]++;
                itembuy[4].setText(String.valueOf(selectItems[4]));
                break;
            case R.id.btnminus5:
                if(selectItems[4] >=1) selectItems[4]--;
                itembuy[4].setText(String.valueOf(selectItems[4]));
                break;
            case R.id.btnadd6:
                selectItems[5]++;
                itembuy[5].setText(String.valueOf(selectItems[5]));
                break;
            case R.id.btnminus6:
                if(selectItems[5] >=1) selectItems[5]--;
                itembuy[5].setText(String.valueOf(selectItems[5]));
                break;
            case R.id.btnadd7:
                selectItems[6]++;
                itembuy[6].setText(String.valueOf(selectItems[6]));
                break;
            case R.id.btnminus7:
                if(selectItems[6] >=1) selectItems[6]--;
                itembuy[6].setText(String.valueOf(selectItems[6]));
                break;
            case R.id.btnadd8:
                selectItems[7]++;
                itembuy[7].setText(String.valueOf(selectItems[7]));
                break;
            case R.id.btnminus8:
                if(selectItems[7] >=1) selectItems[7]--;
                itembuy[7].setText(String.valueOf(selectItems[7]));
                break;
            case R.id.btnadd9:
                selectItems[8]++;
                itembuy[8].setText(String.valueOf(selectItems[8]));
                break;
            case R.id.btnminus9:
                if(selectItems[8] >=1) selectItems[8]--;
                itembuy[8].setText(String.valueOf(selectItems[8]));
                break;
            case R.id.btnadd10:
                selectItems[9]++;
                itembuy[9].setText(String.valueOf(selectItems[9]));
                break;
            case R.id.btnminus10:
                if(selectItems[9] >=1) selectItems[9]--;
                itembuy[9].setText(String.valueOf(selectItems[9]));
                break;
            case R.id.btnadd11:
                selectItems[10]++;
                itembuy[10].setText(String.valueOf(selectItems[10]));
                break;
            case R.id.btnminus11:
                if(selectItems[10] >=1) selectItems[10]--;
                itembuy[10].setText(String.valueOf(selectItems[10]));
                break;
        }
    }

    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }

    public void bindView(){

        userPoint = (TextView)findViewById(R.id.tvUserPoint);
        items[0] = (TextView)findViewById(R.id.tvItem1);
        items[1] = (TextView)findViewById(R.id.tvItem2);
        items[2] = (TextView)findViewById(R.id.tvItem3);
        items[3] = (TextView)findViewById(R.id.tvItem4);
        items[4] = (TextView)findViewById(R.id.tvItem5);
        items[5] = (TextView)findViewById(R.id.tvItem6);
        items[6] = (TextView)findViewById(R.id.tvItem7);
        items[7] = (TextView)findViewById(R.id.tvItem8);
        items[8] = (TextView)findViewById(R.id.tvItem9);
        items[9] = (TextView)findViewById(R.id.tvItem10);
        items[10] = (TextView)findViewById(R.id.tvItem11);

        itembuy[0] = (TextView)findViewById(R.id.numOfItem1);
        itembuy[1] = (TextView)findViewById(R.id.numOfItem2);
        itembuy[2] = (TextView)findViewById(R.id.numOfItem3);
        itembuy[3] = (TextView)findViewById(R.id.numOfItem4);
        itembuy[4] = (TextView)findViewById(R.id.numOfItem5);
        itembuy[5] = (TextView)findViewById(R.id.numOfItem6);
        itembuy[6] = (TextView)findViewById(R.id.numOfItem7);
        itembuy[7] = (TextView)findViewById(R.id.numOfItem8);
        itembuy[8] = (TextView)findViewById(R.id.numOfItem9);
        itembuy[9] = (TextView)findViewById(R.id.numOfItem10);
        itembuy[10] = (TextView)findViewById(R.id.numOfItem11);

    }
    private void sendObject(){
        Log.d("sendOjbect","started");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ShopActivity.GetItemMain request = new ShopActivity.GetItemMain();
        request.run();
    }

    private class GetItemMain extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    public String postData(){
        Log.d("postData","started.");
        String msg = MainActivity.urlString+"/item";

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
            showItemMain(result.toString());

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

    public void showItemMain(String jsonString) {
        Log.d("showItemMain", "started");
        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            userPoint.setText(String.valueOf(stringToJson.getInt("point")));
            items[0].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("1")));
            items[1].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("2")));
            items[2].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("3")));
            items[3].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("4")));
            items[4].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("5")));
            items[5].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("6")));
            items[6].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("7")));
            items[7].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("8")));
            items[8].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("9")));
            items[9].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("10")));
            items[10].setText(String.valueOf(stringToJson.getJSONObject("list").getInt("11")));

        }
        catch(JSONException e){
            Log.d("showItemMain", "unexpected error");
        }
    }
}
