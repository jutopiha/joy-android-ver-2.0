package com.joy.tiggle.joy.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
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
 * Implementation of App Widget functionality.
 */
public class QuestListWidget extends AppWidgetProvider {

    static String resultType1, resultType2;
    static int weeklyGoal, weeklyReal, monthlyGoal, monthlyReal;
    static RelativeLayout layoutRegister1, layoutInfo1, layoutRegister2,layoutInfo2;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.quest_list_widget_title);
        // Construct the RemoteViews object
        sendObject();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quest_list_widget);
        if(resultType1 == null){
            views.setViewVisibility(R.id.weekly_quest_relative1, View.INVISIBLE);
            views.setViewVisibility(R.id.weekly_quest_relative2, View.VISIBLE);
        }
        else{
            //주간 퀘스트가 있을때
            views.setTextViewText(R.id.widget_weekly_quest_goal_money, String.valueOf(weeklyGoal));
            views.setTextViewText(R.id.widget_weekly_quest_real_money, String.valueOf(weeklyReal));
            views.setViewVisibility(R.id.weekly_quest_relative1, View.VISIBLE);
            views.setViewVisibility(R.id.weekly_quest_relative2, View.INVISIBLE);
        }

        if(resultType2 == null){
            views.setViewVisibility(R.id.monthly_quest_relative1, View.INVISIBLE);
            views.setViewVisibility(R.id.monthly_quest_relative2, View.VISIBLE);
        }
        else{
            //월간 퀘스트가 있을때
            views.setTextViewText(R.id.widget_monthly_quest_goal_money, String.valueOf(monthlyGoal));
            views.setTextViewText(R.id.widget_monthly_quest_real_money, String.valueOf(monthlyReal));
            views.setViewVisibility(R.id.monthly_quest_relative1, View.VISIBLE);
            views.setViewVisibility(R.id.monthly_quest_relative2, View.INVISIBLE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quest_list_widget);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pe = PendingIntent.getActivity(context,0,intent, 0);
        views.setOnClickPendingIntent(R.id.relative,pe);
        appWidgetManager.updateAppWidget(appWidgetIds,views);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static private void sendObject(){
        Log.d("sendObject","started.");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        QuestListWidget.GetQuestInfo request = new QuestListWidget.GetQuestInfo();
        request.run();
    }

    static private class GetQuestInfo extends Thread{
        @Override
        public void run(){
            postData();
        }
    }

    static public String postData() {
        Log.d("postData", "started");
        String msg = MainActivity.urlString + "/quest";

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
            showQuestInfo(result.toString());

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

    static public void showQuestInfo(String jsonString){
        Log.d("showDailyInfo","started");

        try {
            JSONObject stringToJson = new JSONObject(jsonString);   //서버에서 string으로 받은 결과를 json객체로 바꿈


            //데이터 뽑아내서 필요한 곳에 저장하는 부분
            resultType1 = stringToJson.getJSONObject("weekly").getString("type");
            weeklyGoal = stringToJson.getJSONObject("weekly").getInt("goalMoney");
            weeklyReal = stringToJson.getJSONObject("weekly").getInt("nowMoney");

            resultType2 = stringToJson.getJSONObject("monthly").getString("type");
            monthlyGoal = stringToJson.getJSONObject("monthly").getInt("goalMoney");
            monthlyReal = stringToJson.getJSONObject("monthly").getInt("nowMoney");
        }
        catch (JSONException e) {
        }

    }
}

