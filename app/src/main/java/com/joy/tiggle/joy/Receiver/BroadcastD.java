package com.joy.tiggle.joy.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.joy.tiggle.joy.Activity.MainActivity;
import com.joy.tiggle.joy.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by CE-L-17 on 2017-11-12.
 */

public class BroadcastD extends BroadcastReceiver {

    //String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        long now = System.currentTimeMillis(); //현재시간을 msec로 구함.
        Date date = new Date(now);  // 현재 시간을 date변수에 저장
        SimpleDateFormat sdfNow = new SimpleDateFormat("dd");   //시간 나타낼 포맷 정함
        String formatDate = sdfNow.format(date);    //nowDate변수에 값을 저장

        if(formatDate.equals("01")){
            Log.d("매월1일","onReceive 호출");
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.icon_logo).setTicker("티끌모아 태산").setWhen(System.currentTimeMillis())
                    .setContentTitle("월별통계알림").setContentText("지난 달의 월별 통계를 확인해보세요.")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
            notificationmanager.notify(1, builder.build());
        }
        else{
            Log.d("1일 아닐때","onReceive 호출");
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.icon_logo).setTicker("티끌모아 태산").setWhen(System.currentTimeMillis())
                    .setContentTitle("내역 입력 알림").setContentText("오늘의 지출 내역을 입력해 볼까요?")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
            notificationmanager.notify(1, builder.build());
        }

    }
}
