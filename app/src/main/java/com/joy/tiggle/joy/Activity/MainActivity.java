package com.joy.tiggle.joy.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.joy.tiggle.joy.Fragment.InputFragment;
import com.joy.tiggle.joy.Fragment.HomeFragment;
import com.joy.tiggle.joy.Fragment.DailyFragment;
import com.joy.tiggle.joy.Fragment.MonthlyStatFragment;
import com.joy.tiggle.joy.R;

public class MainActivity extends AppCompatActivity {

    public static final String urlString = "http://18.220.36.184:9000";
    public static String currentUserId;

    private TextView mTextMessage;

    HomeFragment fragment_home = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_home);


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_save_new:
                    //InputFragment.java를 불러온다
                    selectedFragment = InputFragment.newInstance();
                    break;
                case R.id.navigation_daily:
                    selectedFragment = DailyFragment.newInstance();
                    break;
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_monthly:
                    selectedFragment = MonthlyStatFragment.newInstance();
                    break;
                case R.id.navigation_quest:
                    mTextMessage.setText("퀘스트 startActivity");
                    break;
            }
            if(selectedFragment==null)
            {
                //nullPointer방지
                selectedFragment = InputFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 임시
        //mTextMessage = (TextView) findViewById(R.id.message);

        // 로그인 여부 확인
        if(AccessToken.getCurrentAccessToken() == null) {
            // 로그인이 되어 있지 않은 경우, SigninActivity로 간다
            startActivity(new Intent(this, SigninActivity.class));
            finish();
            return;
        }
        else {
            currentUserId = AccessToken.getCurrentAccessToken().getUserId();
        }

        // 상단 액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        // 커스터마이징 상단 액션바
        View mActionBarView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        // 하단 네비게이션
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);                     // 아이콘 색상 살리기
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);    // 홈 화면으로 시작
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 상단 액션바 우측 메뉴 생성
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 상단 액션바 우측 메뉴 클릭 시
        switch (item.getItemId()) {
            case R.id.menu_info:
                mTextMessage.setText("info startActivity");
                Intent intent = new Intent(this, CharacterActivity.class);
                startActivity(intent);
                finish();

                return true;
            case R.id.menu_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                finish();

                return true;
        }

       return super.onOptionsItemSelected(item);
    }

    public void onFragmentChagned(int index){
        if(index ==0 )
        {
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setSelectedItemId(R.id.navigation_home);    // 홈 화면 불러옴

        }
    }

}
