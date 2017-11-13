package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.joy.tiggle.joy.Adapter.CustomViewPagerAdapter;
import com.joy.tiggle.joy.R;

/**
 * Created by CE-L-17 on 2017-11-13.
 */
//CustomViewPagerAdapter

public class TutorialActivity extends AppCompatActivity{

    ViewPager pager;

    //Back Button 2번 클릭시 앱 종료, 1번누르면 main으로
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime=0;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        pager= (ViewPager)findViewById(R.id.pager);

        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(getLayoutInflater());
        pager.setAdapter(adapter);
        setCustomActionBar();

    }

    public void onClick(View v){
        int position;

        switch( v.getId() ){
            case R.id.btn_previous://이전버튼 클릭
                position=pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                //현재치(position)에서 -1 을 해서 이전 position으로 변경
                //이전 Item으로 현재의 아이템 변경 설정(가장 처음이면 더이상 이동하지 않음)
                //첫번째 파라미터: 설정할 현재 위치
                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜
                pager.setCurrentItem(position-1,true);
                break;

            case R.id.btn_next://다음버튼 클릭
                position=pager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                //현재 위치(position)에서 +1 을 해서 다음 position으로 변경
                //다음 Item으로 현재의 아이템 변경 설정(가장 마지막이면 더이상 이동하지 않음)
                //첫번째 파라미터: 설정할 현재 위치
                //두번째 파라미터: 변경할 때 부드럽게 이동하는가? false면 팍팍 바뀜
                pager.setCurrentItem(position+1,true);

                break;
            case R.id.btnBack:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
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
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            finish();
        }

    }


}
