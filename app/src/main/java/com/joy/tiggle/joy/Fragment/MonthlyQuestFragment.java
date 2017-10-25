package com.joy.tiggle.joy.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    private int userGoalMoney;    //사용자 목표 금액 입력받아서 저장.

    private Button mObjectBtn;
    private TextView mStartDay, mEndDay, mObjectMoney, mRealMoney;
    private RelativeLayout layoutRegister, layoutInfo;
    private JSONObject jsonObject = new JSONObject(); // for temp
    private String temp;

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
        mObjectBtn = (Button) currentView.findViewById(R.id.btnAddQuest);
        mStartDay = (TextView)currentView.findViewById(R.id.tvStartDay);
        mEndDay = (TextView) currentView.findViewById(R.id.tvEndDay);
        mObjectMoney = (TextView)currentView.findViewById(R.id.tvQuestMoney);
        mRealMoney = (TextView)currentView.findViewById(R.id.tvRealMoney);
        layoutRegister = (RelativeLayout)currentView.findViewById(R.id.relativeQuestRegister);
        layoutInfo = (RelativeLayout)currentView.findViewById(R.id.relativeQuestDetail);

        //sendRequest();

        //버튼리스너
        mObjectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //AlertDialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("월간 지출 목표 설정");
                alert.setMessage("목표 금액을 입력해주세요");

                final EditText money = new EditText(getActivity());
                money.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(money);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        userGoalMoney = Integer.parseInt(money.getText().toString());
                        //sendUserObjectMoney();
                    }
                });

                alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });

        if(WeeklyQuestFragment.newMonthlyQuest.getType() == null)  {
            layoutInfo.setVisibility(View.GONE);
        }
        else{
            layoutRegister.setVisibility(View.GONE);
            mStartDay.setText(WeeklyQuestFragment.newMonthlyQuest.getStartDate());
            mEndDay.setText(WeeklyQuestFragment.newMonthlyQuest.getEndDate());
            mObjectMoney.setText(WeeklyQuestFragment.newMonthlyQuest.getGoalMoney());
            mRealMoney.setText(WeeklyQuestFragment.newMonthlyQuest.getNowMoney());
        }

        return currentView;
    }
}
