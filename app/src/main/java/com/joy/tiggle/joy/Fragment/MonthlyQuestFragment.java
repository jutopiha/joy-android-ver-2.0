package com.joy.tiggle.joy.Fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by 조현정 on 2017-08-27.
 *
 * 수입 입력하기 관련 java
 */

public class MonthlyQuestFragment extends Fragment {

    public static final String PAGE_TITLE = "월간 지출 목표";

    InputMethodManager imm;

    private RelativeLayout mR;

    public MonthlyQuestFragment(){

    }

    public static MonthlyQuestFragment newInstance(){
        MonthlyQuestFragment fragment = new MonthlyQuestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View currentView = (View)inflater.inflate(R.layout.fragment_monthly_quest, container, false);



        //필요한 findViewById






        return currentView;
    }


    //화면 터치시 키보드 내리기
    public void linearOnClick(View view) {
        //imm.hideSoftInputFromWindow(mIncomeMemoEt.getWindowToken(), 0);
    }

}
