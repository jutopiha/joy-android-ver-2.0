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

/**
 * Created by 조현정 on 2017-08-27.
 *
 * 수입 입력하기 관련 java
 */

public class IncomeFragment extends Fragment {

    public static final String PAGE_TITLE = "수입";

    InputMethodManager imm;

    DatePicker mDate;
    EditText mDay;
    int year, month, day;

    TimePicker mTime;
    EditText mClock;
    int hour, minute;

    private EditText mIncomeMoneyEt;
    private EditText mIncomeMemoEt;
    private Spinner mIncomeCategorySp;

    private Button mObjectBtn;

    private JSONObject jsonObject = new JSONObject();
    private RelativeLayout mR;

    public IncomeFragment(){

    }

    public static IncomeFragment newInstance(){
        IncomeFragment fragment = new IncomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = (View)inflater.inflate(R.layout.fragment_income, container, false);



        //필요한 findViewById
        mDate = (DatePicker)view.findViewById(R.id.incomeDatePicker);
        mDay = (EditText)view.findViewById(R.id.incomeDayEt);
        mTime = (TimePicker)view.findViewById(R.id.incomeTimePicker);
        mClock = (EditText)view.findViewById(R.id.incomeTimeEt);
        mIncomeMoneyEt = (EditText)view.findViewById(R.id.incomeMoneyEt);
        mIncomeMemoEt = (EditText)view.findViewById(R.id.incomeMemoEt);
        mIncomeCategorySp = (Spinner) view.findViewById(R.id.incomeCategorySp);
        mObjectBtn = (Button) view.findViewById(R.id.objectBtn);
        mR = (RelativeLayout)view.findViewById(R.id.relativeIncome);


        //spinner divider색깔 조정
        colorizeDatePicker(mDate);
        applyStyLing(mTime);


        //현재 날짜로 picker 초기설정
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

               imm.hideSoftInputFromWindow(mIncomeMemoEt.getWindowToken(), 0);
               imm.hideSoftInputFromWindow(mIncomeMoneyEt.getWindowToken(), 0);
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

    private void sendObject(){
        try {
            int incomeDay; //day를 20170528 형식으로 받기 위해
            int incomeTime;    //time을 1200 형식으로 받기 위해
            incomeDay = mDate.getYear() * 10000 + (mDate.getMonth() + 1) * 100 + mDate.getDayOfMonth();

            //time 만들기
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                incomeTime = mTime.getHour() + 100 + mTime.getMinute();
            } else {
                incomeTime = mTime.getCurrentHour() * 100 + mTime.getCurrentMinute();
            }

            //jsonObject.put("day", mJsonExpenseDayEt.getText().toString());  //day받기 위한
            jsonObject.put("date", incomeDay); //day받기 위한
            jsonObject.put("time", incomeTime); //time받기 위한
            jsonObject.put("category", mIncomeCategorySp.getSelectedItem().toString()); //category받기 위한
            jsonObject.put("money", mIncomeMoneyEt.getText().toString());
            jsonObject.put("memo", mIncomeMemoEt.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SaveNewIncome request = new SaveNewIncome();
        request.run();
    }

    //화면 터치시 키보드 내리기
    public void linearOnClick(View view) {
        imm.hideSoftInputFromWindow(mIncomeMemoEt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mIncomeMoneyEt.getWindowToken(), 0);
    }

    private class SaveNewIncome extends Thread {
        @Override
        public void run() {

            postData("ljh", jsonObject);

        }
    }

    public String postData(String uid, JSONObject data) {

        String msg = MainActivity.urlString + "/post/income";

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
            String json = "";
            json = data.toString();
            //json = URLEncoder.encode(json,"UTF-8"); //한글이 ??로 저장되는 거 바꿈
            //String koreanJson = new String(json.getBytes("UTF-8"));

            // loglog
            Log.v("^^^^^json", json);

            StringEntity stringEntity = new StringEntity(json, "utf-8");
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

    //date spinner divider색깔 조정
    public static void colorizeDatePicker(DatePicker datePicker) {
        Resources system = Resources.getSystem();
        int dayId = system.getIdentifier("day", "id", "android");
        int monthId = system.getIdentifier("month", "id", "android");
        int yearId = system.getIdentifier("year", "id", "android");

        NumberPicker dayPicker = (NumberPicker) datePicker.findViewById(dayId);
        NumberPicker monthPicker = (NumberPicker) datePicker.findViewById(monthId);
        NumberPicker yearPicker = (NumberPicker) datePicker.findViewById(yearId);

        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
        setDividerColor(yearPicker);
    }

    private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.colorAccent));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }
    }

    //time spinner color 조정
    private void applyStyLing(TimePicker timePickerDialog){
        Resources system = Resources.getSystem();
        int hourNumberPickerId = system.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = system.getIdentifier("minute", "id", "android");
        int ampmNumberPickerId = system.getIdentifier("amPm", "id", "android");

        NumberPicker hourNumberPicker = (NumberPicker) timePickerDialog.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) timePickerDialog.findViewById(minuteNumberPickerId);
        NumberPicker ampmNumberPicker = (NumberPicker) timePickerDialog.findViewById(ampmNumberPickerId);

        setDividerColor(hourNumberPicker);
        setDividerColor(minuteNumberPicker);
        setDividerColor(ampmNumberPicker);
    }



}
