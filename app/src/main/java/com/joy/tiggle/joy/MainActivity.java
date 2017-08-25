package com.joy.tiggle.joy;


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

public class MainActivity extends AppCompatActivity {

    public static final String urlString = "http://18.220.36.184:9000";
    public static String currentUserId;

    private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                /*
                case R.id.navigation_home:
                    mTextMessage.setText("메인 화면");
                    return true;
                */
                case R.id.navigation_income:
                    //ItemOneFragment.java를 불러온다
                    selectedFragment = ItemOneFragment.newInstance();
                    break;
                case R.id.navigation_expense:
                    mTextMessage.setText("지출 입력하기 startActivity");
                    break;
                case R.id.navigation_daily:
                    selectedFragment = ItemThreeFragment.newInstance();
                    break;
                case R.id.navigation_monthly:
                    mTextMessage.setText("월별 통계 startActivity");
                    break;
                case R.id.navigation_quest:
                    mTextMessage.setText("퀘스트 startActivity");
                    break;
            }
            if(selectedFragment==null)
            {
                //nullPointer방지
                selectedFragment = ItemOneFragment.newInstance();
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
        mTextMessage = (TextView) findViewById(R.id.message);

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
        navigation.setSelectedItemId(R.id.navigation_daily);    // 기본 선택

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
                return true;
            case R.id.menu_setting:
                mTextMessage.setText("setting startActivity");
                return true;
        }

       return super.onOptionsItemSelected(item);
    }
}
