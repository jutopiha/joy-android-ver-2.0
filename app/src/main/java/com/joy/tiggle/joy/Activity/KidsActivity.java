package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.joy.tiggle.joy.R;

/**
 * Created by CE-L-17 on 2017-11-06.
 */

public class KidsActivity extends AppCompatActivity {
    //Back Button 2번 클릭시 앱 종료, 1번누르면 main으로
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids);
        setCustomActionBar();
    }

    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.relative1Link1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/ArlG-WwwbUs"));
                startActivity(intent);
                break;
            case R.id.relative1Link2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/f9lbIftP5MI"));
                startActivity(intent);
                break;
            case R.id.relative1Link3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/J0DFRbafPms"));
                startActivity(intent);
                break;
            case R.id.relative2Link1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/6OIFpf2X1MU"));
                startActivity(intent);
                break;
            case R.id.relative2Link2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/AoVhcRCr4y4"));
                startActivity(intent);
                break;
            case R.id.relative2Link3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/6pZQqwBFjWQ"));
                startActivity(intent);
                break;
            case R.id.relative3Link1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/bKWN00nBzA0"));
                startActivity(intent);
                break;
            case R.id.relative3Link2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/VCx0BXY0iSI"));
                startActivity(intent);
                break;
            case R.id.relative4Link1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/1SsR8Xv93-0"));
                startActivity(intent);
                break;
            case R.id.relative4Link2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/mRAUkWXdKOc"));
                startActivity(intent);
                break;
            case R.id.relative5Link1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/3iQuSZPTcn8"));
                startActivity(intent);
                break;
            case R.id.relative5Link2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/tedwFJY8OPM"));
                startActivity(intent);
                break;
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
