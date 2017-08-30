package com.joy.tiggle.joy.Object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 조현정 on 2017-08-28.
 */

public class Expense extends Dealing{

    int mExpenseId;
    String mPayMethod;

    public Expense(JSONObject jsonExpense) throws JSONException {

        mExpenseId = jsonExpense.optInt("expenseId");
        mUserId = jsonExpense.optString("userId");
        mDate = jsonExpense.optInt("date");
        mTime = jsonExpense.optInt("time");
        mMoney = jsonExpense.optInt("money");
        mMemo = jsonExpense.optString("memo");
        mCategory = jsonExpense.optString("category");
        mPayMethod = jsonExpense.optString("payMethod");
    }

    public int getmExpenseId() {
        return mExpenseId;
    }

    public void setmExpenseId(int mExpenseId) {
        this.mExpenseId = mExpenseId;
    }

    public String getmPayMethod() {
        return mPayMethod;
    }

    public void setmPayMethod(String mPayMethod) {
        this.mPayMethod = mPayMethod;
    }

}
