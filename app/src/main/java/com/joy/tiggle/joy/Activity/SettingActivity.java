package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.joy.tiggle.joy.CustomDialog;
import com.joy.tiggle.joy.R;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Lee Juha on 2017-08-25.
 */


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSetting;

    private Switch switchAutoParse;
    private static final String URL = "http://1...";

    public static CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setCustomActionBar();

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
            case R.id.btnAsk:
                finish();
                break;
            case R.id.btnAbout:
                customDialog = new CustomDialog(this, "Hongik Univ. CE", mClickCloseListener);
                customDialog.show();

                break;
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



}
