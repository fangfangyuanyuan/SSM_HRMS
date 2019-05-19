package com.hrms.bean;

public class Student {
//    String id;
    String StuId;
    String StuName;
    Integer StuClass;
    String StuMajor;
    String StuDept;
    String StuPass;
    String curId;

    public Student() {
    }

    public String getCurId() { return curId; }

    public void setCurId(String curId) { this.curId = curId; }

    public String getStuId() {
        return StuId;
    }

    public void setStuId(String stuId) {
        StuId = stuId;
    }

    public String getStuName() {
        return StuName;
    }

    public void setStuName(String stuName) {
        StuName = stuName;
    }

    public Integer getStuClass() {
        return StuClass;
    }

    public void setStuClass(Integer stuClass) {
        StuClass = stuClass;
    }

    public String getStuMajor() {
        return StuMajor;
    }

    public void setStuMajor(String stuMajor) {
        StuMajor = stuMajor;
    }

    public String getStuDept() {
        return StuDept;
    }

    public void setStuDept(String stuDept) {
        StuDept = stuDept;
    }

    public String getStuPass() {
        return StuPass;
    }

    public void setStuPass(String stuPass) {
        StuPass = stuPass;
    }

    @Override
    public String toString() {
        return "Student{" +
                "StuId='" + StuId + '\'' +
                ", StuName='" + StuName + '\'' +
                ", StuClass=" + StuClass +
                ", StuMajor='" + StuMajor + '\'' +
                ", StuDept='" + StuDept + '\'' +
                ", StuPass='" + StuPass + '\'' +
                ", curId='" + curId + '\'' +
                '}';
    }

    public boolean equals(Student s) {
        if(this.getStuId().equals(s.getStuId()) &&
                this.getStuName().equals(s.getStuName()) &&
                this.getStuClass().equals(s.getStuClass()) &&
                this.getStuMajor().equals(s.getStuMajor()) &&
                this.getStuDept().equals(s.getStuDept()))
            return true;
        else
            return false;
    }
}
