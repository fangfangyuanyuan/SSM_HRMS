package com.hrms.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Curriculum {
    String curId;
    String couId;
    String teaId;
    int curYear;
    String startTime;
    String endTime;
    String  mould;
    String  scoStart;
    String scoEnd;
    String couName;
    String couNature;
    float couCredit;
    String couIntroduce;
    double sco1Count;
    double sco2Count;
    double sco3Count;
    String couDept;
    String teaName;
    int scoStatus;

    public Curriculum() {}

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCouId() { return couId; }

    public void setCouId(String couId) { this.couId = couId; }

    public String getTeaId() { return teaId; }

    public void setTeaId(String teaId) { this.teaId = teaId; }

    public int getCurYear() { return curYear; }

    public void setCurYear(int curYear) { this.curYear = curYear; }

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

    public String getCouName() {
        return couName;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getMould() {
        return mould;
    }

    public void setMould(String mould) {
        this.mould = mould;
    }

    public String getScoStart() {
        return scoStart;
    }

    public void setScoStart(String scoStart) {
        this.scoStart = scoStart;
    }

    public String getScoEnd() {
        return scoEnd;
    }

    public void setScoEnd(String scoEnd) {
        this.scoEnd = scoEnd;
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

    public String getCouDept() {
        return couDept;
    }

    public void setCouDept(String couDept) {
        this.couDept = couDept;
    }


    public double getSco1Count() {
        return sco1Count;
    }

    public void setSco1Count(double sco1Count) {
        this.sco1Count = sco1Count;
    }

    public double getSco2Count() {
        return sco2Count;
    }

    public void setSco2Count(double sco2Count) {
        this.sco2Count = sco2Count;
    }

    public double getSco3Count() {
        return sco3Count;
    }

    public void setSco3Count(double sco3Count) {
        this.sco3Count = sco3Count;
    }

    public int getScoStatus() {
        return scoStatus;
    }

    public void setScoStatus(int scoStatus) {
        this.scoStatus = scoStatus;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "curId='" + curId + '\'' +
                ", couId='" + couId + '\'' +
                ", teaId='" + teaId + '\'' +
                ", curYear=" + curYear +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", mould='" + mould + '\'' +
                ", scoStart='" + scoStart + '\'' +
                ", scoEnd='" + scoEnd + '\'' +
                ", couName='" + couName + '\'' +
                ", couNature='" + couNature + '\'' +
                ", couCredit=" + couCredit +
                ", couIntroduce='" + couIntroduce + '\'' +
                ", sco1Count=" + sco1Count +
                ", sco2Count=" + sco2Count +
                ", sco3Count=" + sco3Count +
                ", couDept='" + couDept + '\'' +
                ", teaName='" + teaName + '\'' +
                '}';
    }

    public Curriculum(String curId, String couId, String teaId, int curYear, String startTime, String endTime, String couName, String teaName) {
        this.curId = curId;
        this.couId = couId;
        this.teaId = teaId;
        this.curYear = curYear;
        this.startTime = startTime;
        this.endTime = endTime;
        this.couName = couName;
        this.teaName = teaName;
    }
}
