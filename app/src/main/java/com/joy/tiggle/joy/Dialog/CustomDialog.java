package com.joy.tiggle.joy.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joy.tiggle.joy.R;

/**
 * Created by CE-L-05 on 2017-08-25.
 */

public class CustomDialog extends Dialog {

    private Activity activity;

    private String title;   // 상단 타이틀 내용
    private TextView dialogTitle;   // 상단 타이틀뷰

    private View.OnClickListener checkBtListener;  // 버튼 리스너
    private Button btnClose;         // 닫기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 메인 layout
        setContentView(R.layout.custom_dialog);

        btnClose = (Button) findViewById(R.id.listview_bt);
        //dialogTitle = (TextView) findViewById(R.id.list_title);

        // 제목 설정
        //dialogTitle.setText(title);

        // 버튼 리스너 설정
        btnClose.setOnClickListener(checkBtListener);

    }

    public CustomDialog(Activity activity, String title, View.OnClickListener checkBtListener) {
        super(activity, android.R.style.Theme_Translucent_NoTitleBar);
        this.activity = activity;
        this.title = title;
        this.checkBtListener = checkBtListener;
    }
}
