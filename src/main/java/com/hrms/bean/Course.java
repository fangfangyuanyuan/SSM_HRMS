package com.hrms.bean;


public class Course {
    String couId;
    String couName;
    String couNature;
    float couCredit;
    String couIntroduce;
    String couDept;


    public Course() { }

    public String getCouId() {
        return couId;
    }

    public void setCouId(String couId) {
        this.couId = couId;
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

    public String getCouDept() {
        return couDept;
    }

    public void setCouDept(String couDept) {
        this.couDept = couDept;
    }



    @Override
    public String toString() {
        return "Course{" +
                "couId='" + couId + '\'' +
                ", couName='" + couName + '\'' +
                ", couNature='" + couNature + '\'' +
                ", couCredit=" + couCredit +
                ", couIntroduce='" + couIntroduce + '\'' +
                ", couDept='" + couDept + '\'' +
                '}';
    }
}
