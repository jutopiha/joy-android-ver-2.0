package com.joy.tiggle.joy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by CE-L-17 on 2017-10-03.
 */

public class ButtonsCustomDialog extends Dialog {

    private TextView mTitleView;
    private TextView mContentView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mContent;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_custom_dialog_buttons);

        mTitleView = (TextView) findViewById(R.id.txt_title);
        mContentView = (TextView) findViewById(R.id.txt_content);
        mLeftButton = (Button) findViewById(R.id.btn_left);
        mRightButton = (Button) findViewById(R.id.btn_right);

        //제목과 내용을 생성자에게 셋팅한다.
        mTitleView.setText(mTitle);
        mContentView.setText(mContent);

        //클릭 이벤트 셋팅
        if(mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        }
        else if(mLeftClickListener != null && mRightClickListener == null){
            mLeftButton.setOnClickListener(mLeftClickListener);
        }
        else{
            Log.d("ButtonsCustomDialog","click event setting error");
        }
    }

    //클릭버튼이 하나일 때 생성자 함수로 클릭이벤트 받기
    public ButtonsCustomDialog(Context context, String title, View.OnClickListener singleListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
    }

    //클릭버튼이 두개일 때 생성자 함수로 클릭이벤트 받기
    public ButtonsCustomDialog(Context context, String title, String content, View.OnClickListener leftListener, View.OnClickListener rightListener){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}