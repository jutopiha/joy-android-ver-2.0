package com.joy.tiggle.joy.Fragment;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 조현정 on 2017-08-27.
 *
 * 지출 입력하기 관련 java
 */

public class ExpenseFragment extends Fragment {
    public static final String PAGE_TITLE = "지출";

    InputMethodManager imm;

    DatePicker mDate;       //입력 날짜를 받기 위한 DatePicker 변수
    EditText mDay;          //DatePicker로 입력한 날짜를 받는 EditText변수
    int year, month, day;   //DatePicker로 받은 날짜를 각각 연, 월, 일로 저장하는 변수

    TimePicker mTime;   //입력 시간을 받기 위한 TimePicker 변수
    EditText mClock;    //TimePicker로 입력한 시간을 받는 EditText변수
    int hour, minute;   //TimePicker로 받은 시간을 각각 시간, 분으로 저장하는 변수

    private EditText mExpenseMoneyEt;
    private EditText mExpenseMemoEt;
    private Spinner mExpenseCategorySp;
    private Spinner mExpensePayMethodSp;
    private Button mObjectBtn;
    private JSONObject jsonObject = new JSONObject(); // for temp
    private RelativeLayout mR;  //imm쓰기 위해서

    public ExpenseFragment(){

    }

    public static Fragment newInstance(){
        ExpenseFragment fragment = new ExpenseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        //필요한 findVieById
        mDate = (DatePicker)view.findViewById(R.id.expenseDatePicker);
        mDay = (EditText)view.findViewById(R.id.expenseDayEt);
        mTime = (TimePicker)view.findViewById(R.id.expenseTimePicker);
        mClock = (EditText)view.findViewById(R.id.expenseTimeEt);
        mExpenseMoneyEt = (EditText)view.findViewById(R.id.expenseMoneyEt);
        mExpenseMemoEt = (EditText)view.findViewById(R.id.expenseMemoEt);
        mExpenseCategorySp = (Spinner) view.findViewById(R.id.expenseCategorySp);
        mExpensePayMethodSp = (Spinner)view.findViewById(R.id.expensePayMethodSp);
        mObjectBtn = (Button) view.findViewById(R.id.objectBtn);
        mR = (RelativeLayout)view.findViewById(R.id.relativeExpense);

        //현재 날짜로 picker 초기 설정
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDay.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
            }
        });

        //선택된 날짜 editText로 보여주기
        year = mDate.getYear();
        month = mDate.getMonth();
        day = mDate.getDayOfMonth();
        mDay.setText(String.format("%d-%d-%d", year, month + 1, day));


        //현재 시간으로 picker초기설정
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = mTime.getHour();
            minute = mTime.getMinute();
            mClock.setText(String.format("%d:%d", hour, minute));
        } else {
            hour = mTime.getCurrentHour();
            minute = mTime.getCurrentMinute();
            mClock.setText(String.format("%d:%d", hour, minute));
        }

        //선택된 시간 editText로 보여주기
        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mClock.setText(String.format("%d:%d", hourOfDay, minute));
            }
        });

        //화면 클릭시 키보드 내려가는 것
        mR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(mExpenseMemoEt.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mExpenseMoneyEt.getWindowToken(), 0);
            }
        });


        //버튼리스너
        mObjectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendObject();
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.onFragmentChagned(0);
            }
        });

        // Inflate the layout for this fragment
       return view;
    }

    private void sendObject() {

        try {
            int expenseDay; //day를 20170528 형식으로 받기 위해
            int expenseTime;    //time을 1200 형식으로 받기 위해
            expenseDay = mDate.getYear()*10000+(mDate.getMonth()+1)*100 + mDate.getDayOfMonth();

            //time 만들기
            if(Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.M){
                expenseTime = mTime.getHour()+100 + mTime.getMinute();
            }else {
                expenseTime = mTime.getCurrentHour()*100 + mTime.getCurrentMinute();
            }

            //jsonObject.put("day", mJsonExpenseDayEt.getText().toString());  //day받기 위한

            jsonObject.put("date", expenseDay); //day받기 위한
            jsonObject.put("time", expenseTime); //time받기 위한
            jsonObject.put("money", mExpenseMoneyEt.getText().toString());
            jsonObject.put("memo", mExpenseMemoEt.getText().toString());
            jsonObject.put("category", mExpenseCategorySp.getSelectedItem().toString()); //category받기 위한
            jsonObject.put("payMethod", mExpensePayMethodSp.getSelectedItem().toString()); //결제수단 받기위한


        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SaveNewExpense request = new SaveNewExpense();
        request.run();

    }

    private class SaveNewExpense extends Thread
    {
        @Override
        public void run() {

            postData("ljh", jsonObject);

        }
    }

    public String postData(String uid, JSONObject data) {

        String msg = MainActivity.urlString + "/post/expense";

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
        HttpPost httpPost = new HttpPost(requestUrl.toString());
        Log.d("msg is :", requestUrl.toString());

        try {

            String json="";
            json=data.toString();

            // loglog
            Log.v("^^^^^json", json);

            StringEntity stringEntity=new StringEntity(json, "utf-8");
            httpPost.setEntity(stringEntity);

            //answer객체 서버로 전송하고 survey객체 받아오는 과정

            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.v("******server", "send msg successed");

            inputStream = httpResponse.getEntity().getContent();
            rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.v("Main::bring success", "result:" + result.toString());

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
}
