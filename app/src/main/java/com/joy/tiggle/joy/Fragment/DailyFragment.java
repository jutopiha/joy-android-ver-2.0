package com.joy.tiggle.joy.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.Activity.ShowExpenseDetailActivity;
import com.joy.tiggle.joy.Activity.ShowIncomeDetailActivity;
import com.joy.tiggle.joy.Adapter.ExpenseListViewAdapter;
import com.joy.tiggle.joy.Adapter.IncomeListViewAdapter;
import com.joy.tiggle.joy.Object.Expense;
import com.joy.tiggle.joy.Object.Income;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Lee Juha on 2017-08-26.
 */

public class DailyFragment extends Fragment {

    private Button mObjectBtn;
    private int selectDay;
    private int sYear, sMonth, sDay;
    private int positionNumber;
    final static int ACT_DELETE = 1;
    private int allIncome;
    private int allExpense;
    private TextView mAllIncome, mAllExpense;
    private RelativeLayout mR;
    private ListView mIncomeList, mExpenseList;
    private ScrollView mScrollV;


    IncomeListViewAdapter incomeAdapter= new IncomeListViewAdapter();
    ExpenseListViewAdapter expenseAdapter = new ExpenseListViewAdapter();

    ArrayList<Expense> expenseArrayList = new ArrayList<Expense>(); //JSONObject에서 expense 부분만을 저장하기 위한 arrayList
    ArrayList<Income> incomeArrayList = new ArrayList<Income>();    //JSONObject에서 income 부분만을 저장하기 위한 arrayList

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());
    Date date = new Date();
    int currentDate = Integer.parseInt(dateFormat.format(date));


    /*기본*/
    public static android.support.v4.app.Fragment newInstance(){
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }

    /*기본*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    /*기본*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        View currentView = inflater.inflate(R.layout.fragment_daily, container, false);

        //필요한 것들 findViewById
        mObjectBtn = (Button)currentView.findViewById(R.id.dailyBtn);
        mAllIncome = (TextView)currentView.findViewById(R.id.allIncomeTv);
        mAllExpense = (TextView)currentView.findViewById(R.id.allExpenseTv);
        CalendarView calendar = (CalendarView)currentView.findViewById(R.id.calendar);
        mR = (RelativeLayout)currentView.findViewById(R.id.lists);
        mIncomeList = (ListView)currentView.findViewById(R.id.incomeList);
        mExpenseList = (ListView)currentView.findViewById(R.id.expenseList);
        mScrollV = (ScrollView)currentView.findViewById(R.id.scrollV);

        //리스너 등록
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){


            //선택할 때마다 selectDay 변수에 선택된 날짜를 int형으로 저장한다.
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                selectDay = year*10000+(month+1)*100+dayOfMonth;
                sYear = year;
                sMonth = month+1;
                sDay = dayOfMonth;
            }
        });

        //캘린더 스크롤 살리기
        calendar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                Log.d("CUSTOM_MAP", "start onTouch");
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:// MapView에서 터치를 발생할 때, 부모뷰(ScrollView)가 TouchEvent를 가로채는 것을 막음
                        mScrollV.requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP: // MapView에서 터치를 뗄때, 부모뷰(ScrollView)가 TouchEvent를 가로채는 것을 허용함
                        mScrollV.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return true;
            }
        });
        // 수입리스트 스크롤 살리기
        mIncomeList.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v,MotionEvent event){
                mScrollV.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //지출리스트 스크롤 살리기
        mExpenseList.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v,MotionEvent event){
                mScrollV.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        selectDay = currentDate;


        mObjectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                allIncome = 0;
                allExpense = 0;
                sendObject();
            }
        });
        return currentView;
    }

    private void sendObject(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SaveNewShow request = new SaveNewShow();
        request.run();
    }

    private class SaveNewShow extends Thread
    {
        @Override
        public void run() {

            postData("ljh", selectDay);

        }
    }

    public String postData(String uid, int data) {

        String msg = MainActivity.urlString + "/get/dealing";


        InputStream inputStream = null;
        BufferedReader rd = null;
        StringBuilder result = new StringBuilder();

        StringBuilder requestUrl = new StringBuilder(msg);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("uid", MainActivity.currentUserId));
        nvps.add(new BasicNameValuePair("date", Integer.toString(selectDay)));
        String querystring = URLEncodedUtils.format(nvps, "utf-8");

        requestUrl.append("?");
        requestUrl.append(querystring);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl.toString());
        Log.d("msg is :", requestUrl.toString());



        try {

            //answer객체 서버로 전송하고 survey객체 받아오는 과정

            HttpResponse httpResponse = httpClient.execute(httpGet);

            Log.v("******server", "send msg successed");

            inputStream = httpResponse.getEntity().getContent();
            rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            Log.v("Main::bring success", "result:" + result.toString());
            showDealingList(result.toString());

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

    public void showDealingList(String jsonString) {  //json객체 string으로 받아 파싱하여 income/expense object를 생성한 뒤, 리스트뷰로 만들어서 show함



        //listView를 위한
        ListView incomeListView;
        ListView expenseListView;

        //해당하는 날짜의 값들만을 JSONObject에서 골라내어 저장함.
        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈

            JSONArray incomeArr =  stringToJson.getJSONArray("incomeList"); //incomeList JSONArray를 파싱.
            JSONArray expenseArr = stringToJson.getJSONArray("expenseList");//expenseList JSONArray를 파싱.

            incomeArrayList.clear();
            expenseArrayList.clear();
            for(int i=0; i<incomeArr.length();i++)
            {
                //jsonArray에서 jsonObject를 뽑아냄
                JSONObject currentIncomeJson = incomeArr.getJSONObject(i);

                //뽑아낸 jsonObject를 arrayList에 추가해 준다.
                //각각의 income list를 Income class의 object를 생성하여 저장함.
                Income newIncome = new Income(currentIncomeJson);
                allIncome += newIncome.getmMoney();
                incomeArrayList.add(newIncome);
            }

            for(int j=0 ; j<expenseArr.length();j++)
            {
                JSONObject currentExpenseJson = expenseArr.getJSONObject(j);

                Expense newExpense = new Expense(currentExpenseJson);
                allExpense += newExpense.getmMoney();
                expenseArrayList.add(newExpense);
            }
        }
        catch (JSONException e) {
        }

        //리스트뷰 참조 및 adapter달기
        incomeListView = (ListView)mR.findViewById(R.id.incomeList);
        incomeListView.setAdapter(incomeAdapter);
        //income의 리스트가 눌렸을때 리스너를 생성하고 달아줌
        IncomeListViewClickListener incomeListViewClickListener = new IncomeListViewClickListener();
        incomeListView.setOnItemClickListener(incomeListViewClickListener);

        expenseListView = (ListView)mR.findViewById(R.id.expenseList);
        expenseListView.setAdapter(expenseAdapter);

        //expense의 리스트가 눌렸을때 리스너를 생성하고 달아줌
        ExpenseListViewClickListener expenseListViewClickListener = new ExpenseListViewClickListener();
        expenseListView.setOnItemClickListener(expenseListViewClickListener);

        incomeAdapter.addItem(incomeArrayList);     //jsonObject를 파싱하여 만든 income들의 list를 incomeArrayList로 어뎁터에 추가함.
        expenseAdapter.addItem(expenseArrayList);

        mAllIncome.setText("총 수입:   "+String.valueOf(allIncome)+"원");
        mAllExpense.setText("총 지출:   "+String.valueOf(allExpense)+"원");

    }

    private class IncomeListViewClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            Intent intent = new Intent(DailyFragment.this.getActivity(), ShowIncomeDetailActivity.class);
            intent.putExtra("selectIncome", incomeArrayList.get(position));
            positionNumber = position;
            startActivityForResult(intent, ACT_DELETE);
        }
    }

    private class ExpenseListViewClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            Intent intent = new Intent(DailyFragment.this.getActivity(), ShowExpenseDetailActivity.class);
            Log.d("*********보내기전", String.valueOf(expenseArrayList.get(position).getmMoney()));
            intent.putExtra("selectExpense", expenseArrayList.get(position));
            positionNumber = position;
            startActivityForResult(intent, ACT_DELETE);
        }
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACT_DELETE:
                if (resultCode == RESULT_OK) {  //수입내역 삭제
                    allIncome = 0;
                    allExpense = 0;
                    Log.d("수입삭제","수입삭제");
                    // incomeArrayList.remove(positionNumber);
                    // incomeAdapter.notifyDataSetChanged();
                    sendObject();
                }

                else if(resultCode == RESULT_FIRST_USER){   //지출내역 삭제
                    allIncome = 0;
                    allExpense = 0;
                    Log.d("지출삭제","지출삭제");
                    //expenseArrayList.remove(positionNumber);
                    // expenseAdapter.notifyDataSetChanged();
                    sendObject();
                }

                else if(resultCode == RESULT_CANCELED){ //상세내역조회에서 뒤로가기를 누르거나 수정했을때
                    allIncome = 0;
                    allExpense = 0;
                    Log.d("뒤로가기수정","뒤로가기수정");
                    sendObject();
                }
                else {
                    Log.d("***************", "여기야");
                    sendObject();
                }

                break;
        }
    }
}