package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.joy.tiggle.joy.R;

/**
 * Created by Lee Juha on 2017-08-27.
 */

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        setCustomActionBar();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
    }
}
