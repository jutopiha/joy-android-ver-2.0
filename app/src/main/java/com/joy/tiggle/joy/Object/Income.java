package com.joy.tiggle.joy.Object;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 조현정 on 2017-08-28.
 */

public class Income extends Dealing {

    private int mIncomeId;
    private Drawable iconDrawable;

    public Income(JSONObject jsonIncome) throws JSONException {

        mIncomeId = jsonIncome.optInt("incomeId");
        mUserId = jsonIncome.optString("userId");
        mDate = jsonIncome.optInt("date");
        mTime = jsonIncome.optInt("time");
        mMoney = jsonIncome.optInt("money");
        mMemo = jsonIncome.optString("memo");
        mCategory = jsonIncome.optString("category");
    }


    public int getmIncomeId() {
        return mIncomeId;
    }

    public void setmIncomeId(int mIncomeId) {
        this.mIncomeId = mIncomeId;
    }

    public void setIcon(Drawable icon){ iconDrawable = icon;}

    public Drawable getIcon(){return this.iconDrawable;}



}
