package com.hrms.bean;

public class Teacher {
    String teaId;
    String teaName;
    String teaMajor;
    String teaDept;
    String teaPass;

    public Teacher() {
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTeaMajor() {
        return teaMajor;
    }

    public void setTeaMajor(String teaMajor) {
        this.teaMajor = teaMajor;
    }

    public String getTeaDept() {
        return teaDept;
    }

    public void setTeaDept(String teaDept) {
        this.teaDept = teaDept;
    }

    public String getTeaPass() {
        return teaPass;
    }

    public void setTeaPass(String teaPass) {
        this.teaPass = teaPass;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teaId='" + teaId + '\'' +
                ", teaName='" + teaName + '\'' +
                ", teaMajor='" + teaMajor + '\'' +
                ", teaDept='" + teaDept + '\'' +
                ", teaPass='" + teaPass + '\'' +
                '}';
    }

    public boolean equals(Teacher t) {
        if(this.getTeaId().equals(t.getTeaId())&&
                this.getTeaName().equals(t.getTeaName()) &&
                this.getTeaDept().equals(t.getTeaDept())&&
                this.getTeaMajor().equals(t.getTeaMajor()))
            return true;
        else
            return false;
    }
}
