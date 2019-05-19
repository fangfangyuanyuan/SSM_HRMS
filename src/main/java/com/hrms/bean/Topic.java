package com.hrms.bean;

public class Topic {
    String topId;
    String topName;
    String topNature;
    String topIntroduce;
    String curId;
    String topGiver;
    int topStatus;
    String stuName;
    String startTime;
    String endTime;
    String couId;
    String teaId;
    String couName;
    String couNature;
    float couCredit;
    String couIntroduce;
    int curYear;

    public Topic() {
    }

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getTopNature() {
        return topNature;
    }

    public void setTopNature(String topNature) {
        this.topNature = topNature;
    }

    public String getTopIntroduce() {
        return topIntroduce;
    }

    public void setTopIntroduce(String topIntroduce) {
        this.topIntroduce = topIntroduce;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getTopGiver() {
        return topGiver;
    }

    public void setTopGiver(String topGiver) {
        this.topGiver = topGiver;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public int getTopStatus() {
        return topStatus;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCouId() {
        return couId;
    }

    public void setCouId(String couId) {
        this.couId = couId;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouNature() {
        return couNature;
    }

    public void setCouNature(String couNature) {
        this.couNature = couNature;
    }

    public float getCouCredit() {
        return couCredit;
    }

    public void setCouCredit(float couCredit) {
        this.couCredit = couCredit;
    }

    public String getCouIntroduce() {
        return couIntroduce;
    }

    public void setCouIntroduce(String couIntroduce) {
        this.couIntroduce = couIntroduce;
    }

    public int getCurYear() {
        return curYear;
    }

    public void setCurYear(int curYear) {
        this.curYear = curYear;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topId='" + topId + '\'' +
                ", topName='" + topName + '\'' +
                ", topNature='" + topNature + '\'' +
                ", topIntroduce='" + topIntroduce + '\'' +
                ", curId='" + curId + '\'' +
                ", topGiver='" + topGiver + '\'' +
                ", topStatus=" + topStatus +
                ", stuName='" + stuName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", couId='" + couId + '\'' +
                ", teaId='" + teaId + '\'' +
                ", couName='" + couName + '\'' +
                ", couNature='" + couNature + '\'' +
                ", couCredit=" + couCredit +
                ", couIntroduce='" + couIntroduce + '\'' +
                ", curYear=" + curYear +
                '}';
    }

    public boolean equals(Topic t) {
        if(this.getCurId().equals(t.curId)&&
                this.getTopName().equals(t.getTopName()))
            return true;
        else
            return false;
    }

}
