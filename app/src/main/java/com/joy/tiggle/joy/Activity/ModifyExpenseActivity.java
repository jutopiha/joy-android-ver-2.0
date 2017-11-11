package com.joy.tiggle.joy.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.joy.tiggle.joy.Object.Expense;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
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
 * Created by 조현정 on 2017-08-28.
 */

public class ModifyExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mDay,mExpenseDay, mExpenseTime, mExpenseMoney,mExpenseMemo;
    private Spinner mExpenseCategory, mExpensePayMethod;

    private Expense selectExpense;
    private String expenseCat1 ="식비", expenseCat2="교통비",expenseCat3="문화", expenseCat4="생활", expenseCat5="음료/간식", expenseCat6="교육", expenseCat7="공과금", expenseCat8="기타";
    private String expenseCat13="현금", expenseCat14="카드";

    InputMethodManager imm; //화면 터치시 키보드 내리기 위한

    DatePicker mDate;
    int year, month, day;

    TimePicker mTime;
    int hour, minute;

    private Button mObjectBtn;
    private JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_modify_expense);

        setCustomActionBar();

        mDate = (DatePicker)findViewById(R.id.expenseDatePicker);
        mTime = (TimePicker)findViewById(R.id.expenseTimePicker);
        mExpenseDay = (EditText)findViewById(R.id.expenseDayEt);
        mExpenseTime = (EditText)findViewById(R.id.expenseTimeEt);
        mExpenseCategory = (Spinner) findViewById(R.id.expenseCategorySp);
        mExpenseMoney = (EditText)findViewById(R.id.expenseMoneyEt);
        mExpensePayMethod = (Spinner) findViewById(R.id.expensePayMethodSp);
        mExpenseMemo = (EditText)findViewById(R.id.expenseMemoEt);

        //ShowDetailActivity에서 보낸 intent(Expense정보를 갖고 있음)을 받는다
        Intent intet = getIntent();
        selectExpense = (Expense) getIntent().getSerializableExtra("selectExpense");

        //숫자를 년,월,일로 분리하는 코드
        year = selectExpense.getmDate()/10000;
        month = (selectExpense.getmDate()-year*10000)/100;
        day = selectExpense.getmDate()-year*10000-month*100;

        mExpenseDay.setText(String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day));
        //mExpenseDay.setText(String.valueOf(makeDateForm(selectExpense.getmDate())));
        mExpenseTime.setText(String.valueOf(makeTimeForm(selectExpense.getmTime())));

        Log.d("*************요기요기",selectExpense.getmCategory());

        if(expenseCat1.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(0);
        else if(expenseCat2.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(1);
        else if(expenseCat3.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(2);
        else if(expenseCat4.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(3);
        else if(expenseCat5.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(4);
        else if(expenseCat6.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(5);
        else if(expenseCat7.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(6);
        else if(expenseCat8.equals(selectExpense.getmCategory()))
            mExpenseCategory.setSelection(7);


        mExpenseMoney.setText(String.valueOf(selectExpense.getmMoney()));


        if(expenseCat13.equals(selectExpense.getmPayMethod()))
            mExpensePayMethod.setSelection(0);
        else if(expenseCat14.equals(selectExpense.getmPayMethod()))
            mExpensePayMethod.setSelection(1);

        mExpenseMemo.setText(selectExpense.getmMemo());

        //spinner divider색깔 조정
        colorizeDatePicker(mDate);
        applyStyLing(mTime);
        //여기까지 기존의 사용자가 입력했던 값을 불러와 editText로 띄워준다.

        //datePicker가 변할 때 editText의 값을 바꿔주기 위한 코드들

        int tempDate = selectExpense.getmDate();

        mDate.init(mDate.getYear(),mDate.getMonth(),mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mExpenseDay.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                selectExpense.setmDate(year*10000+(monthOfYear+1)*100+dayOfMonth);
            }
        });

        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mExpenseTime.setText(String.format("%d:%d", hourOfDay, minute));
                selectExpense.setmTime(hourOfDay*100+minute);
            }
        });

        bindView();

        //화면 터치시 키보드 내리기
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.objectBtn:
                sendObject();
                finish();
                break;
        }
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    //화면 터치시 키보드 내리기
    public void linearOnClick(View view){
        imm.hideSoftInputFromWindow(mExpenseMoney.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mExpenseMemo.getWindowToken(), 0);
    }

    public String makeDateForm(int date) {
        int dateOfYear, dateOfMonth, dateOfday;
        dateOfYear = date/10000;
        dateOfMonth = (dateOfYear*10000-date)/100;
        dateOfday = dateOfYear*10000-dateOfMonth*100-date;

        String result = dateOfYear+"-"+dateOfMonth+"-"+dateOfday;
        return result;
    }

    public String makeTimeForm(int time){
        int timeOfHour, timeOfMinute;
        timeOfHour = time/100;
        timeOfMinute = time-timeOfHour*100;

        String result = timeOfHour+":"+timeOfMinute;
        return result;
    }

    private void bindView(){
        mObjectBtn = (Button)findViewById(R.id.objectBtn);

        mObjectBtn.setOnClickListener(this);

    }

    private void sendObject(){
        try{
            int expenseDay = selectExpense.getmDate();
            int expenseTime = selectExpense.getmTime();

            jsonObject.put("date",expenseDay);
            jsonObject.put("time",expenseTime);
            jsonObject.put("category",mExpenseCategory.getSelectedItem().toString());
            jsonObject.put("money",mExpenseMoney.getText().toString());
            jsonObject.put("payMethod",mExpensePayMethod.getSelectedItem().toString());
            jsonObject.put("memo",mExpenseMemo.getText().toString());
        }catch(JSONException e){
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SaveNewExpense request = new SaveNewExpense();
        request.run();
    }

    private class SaveNewExpense extends Thread{
        @Override
        public void run(){

            postData("ljh",jsonObject);
        }
    }

    public String postData(String uid, JSONObject data){

        String msg = MainActivity.urlString+ "/put/expense";

        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("eid", String.valueOf(selectExpense.getmExpenseId())));
        String querystring = URLEncodedUtils.format(nvps, "utf-8");

        requestUrl.append("?");
        requestUrl.append(querystring);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(requestUrl.toString());
        Log.d("msg is :", requestUrl.toString());

        try {
            String json="";
            json=data.toString();

            // loglog
            Log.v("^^^^^json", json);

            StringEntity stringEntity=new StringEntity(json, "utf-8");
            httpPut.setEntity(stringEntity);

            //answer객체 서버로 전송하고 survey객체 받아오는 과정

            HttpResponse httpResponse = httpClient.execute(httpPut);
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

    private boolean setCustomActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();       // 기본 액션바 숨기기

        return true;
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
