package com.joy.tiggle.joy.Object;

/**
 * Created by HyeonJeong on 2017-10-20.
 */

public class Quest {

    private String type;
    private String startDate;
    private String endDate;
    private String goalMoney;
    private String nowMoney;

    public void setType(String type) {
        this.type = type;
    }
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public void setGoalMoney(String goalMoney) {this.goalMoney = goalMoney;}
    public void setNowMoney(String nowMoney) {this.nowMoney = nowMoney;}

    public String getType(){return type;}
    public String getStartDate(){return startDate;}
    public String getEndDate(){return endDate;}
    public String getGoalMoney() { return goalMoney;}
    public String getNowMoney(){return nowMoney;}


}
