package com.hrms.bean;

import java.util.List;

public class Group {
    String groId;
    String curId;
    String stuId;
    String stuName;
    String stuClass;
    String stuMajor;
    String topId;
    int curYear;
    String teaId;
    String teaName;
    String couName;
    String topName;
    Double score1;
    Double score2;
    Double score3;
    Double sco1Count;
    Double sco2Count;
    Double sco3Count;
    Double total;
    String report;
    String optResult;
    Double avg1;
    Double avg2;
    Double avg3;
    Double avgTotal;
    int rowNo;
    int totalPeople;
    int absentees;
    int actualPeople;
    double maxSco;
    double minSco;
    double stdTotal;//标准差
    double stdScore1;
    double stdScore2;
    double stdScore3;
    int great;
    int good;
    int general;
    int weak;
    double score1Rate;
    double score2Rate;
    double score3Rate;
    double totalRate;
    String scoStart;
    String scoEnd;

    List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Group() {
    }

    public Group(String curId, String stuId) {
        this.curId = curId;
        this.stuId = stuId;
    }

    public String getGroId() {
        return groId;
    }

    public void setGroId(String groId) {
        this.groId = groId;
    }

    public String getStuName() {return stuName;}

    public void setStuName(String stuName) {this.stuName = stuName;}

    public String getCurId() { return curId; }

    public void setCurId(String curId) { this.curId = curId; }

    public String getStuId() { return stuId; }

    public void setStuId(String stuId) { this.stuId = stuId; }

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public int getCurYear() {
        return curYear;
    }

    public void setCurYear(int curYear) {
        this.curYear = curYear;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public Double getScore1() {
        return score1;
    }

    public void setScore1(Double score1) {
        this.score1 = score1;
    }

    public Double getScore2() {
        return score2;
    }

    public void setScore2(Double score2) {
        this.score2 = score2;
    }

    public Double getScore3() {
        return score3;
    }

    public void setScore3(Double score3) {
        this.score3 = score3;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuMajor() {
        return stuMajor;
    }

    public void setStuMajor(String stuMajor) {
        this.stuMajor = stuMajor;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getOptResult() {
        return optResult;
    }

    public void setOptResult(String optResult) {
        this.optResult = optResult;
    }

    public Double getAvg1() {
        return avg1;
    }

    public void setAvg1(Double avg1) {
        this.avg1 = avg1;
    }

    public Double getAvg2() {
        return avg2;
    }

    public void setAvg2(Double avg2) {
        this.avg2 = avg2;
    }

    public Double getAvg3() {
        return avg3;
    }

    public void setAvg3(Double avg3) {
        this.avg3 = avg3;
    }

    public Double getAvgTotal() {
        return avgTotal;
    }

    public void setAvgTotal(Double avgTotal) {
        this.avgTotal = avgTotal;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public Double getSco1Count() {
        return sco1Count;
    }

    public void setSco1Count(Double sco1Count) {
        this.sco1Count = sco1Count;
    }

    public Double getSco2Count() {
        return sco2Count;
    }

    public void setSco2Count(Double sco2Count) {
        this.sco2Count = sco2Count;
    }

    public Double getSco3Count() {
        return sco3Count;
    }

    public void setSco3Count(Double sco3Count) {
        this.sco3Count = sco3Count;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    public int getAbsentees() {
        return absentees;
    }

    public void setAbsentees(int absentees) {
        this.absentees = absentees;
    }

    public int getActualPeople() {
        return actualPeople;
    }

    public void setActualPeople(int actualPeople) {
        this.actualPeople = actualPeople;
    }

    public double getMaxSco() {
        return maxSco;
    }

    public void setMaxSco(double maxSco) {
        this.maxSco = maxSco;
    }

    public double getMinSco() {
        return minSco;
    }

    public void setMinSco(double minSco) {
        this.minSco = minSco;
    }

    public double getStdTotal() {
        return stdTotal;
    }

    public void setStdTotal(double stdTotal) {
        this.stdTotal = stdTotal;
    }

    public double getStdScore1() {
        return stdScore1;
    }

    public void setStdScore1(double stdScore1) {
        this.stdScore1 = stdScore1;
    }

    public double getStdScore2() {
        return stdScore2;
    }

    public void setStdScore2(double stdScore2) {
        this.stdScore2 = stdScore2;
    }

    public double getStdScore3() {
        return stdScore3;
    }

    public void setStdScore3(double stdScore3) {
        this.stdScore3 = stdScore3;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public int getWeak() {
        return weak;
    }

    public void setWeak(int weak) {
        this.weak = weak;
    }

    public double getScore1Rate() {
        return score1Rate;
    }

    public void setScore1Rate(double score1Rate) {
        this.score1Rate = score1Rate;
    }

    public double getScore2Rate() {
        return score2Rate;
    }

    public void setScore2Rate(double score2Rate) {
        this.score2Rate = score2Rate;
    }

    public double getScore3Rate() {
        return score3Rate;
    }

    public void setScore3Rate(double score3Rate) {
        this.score3Rate = score3Rate;
    }

    public double getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(double totalRate) {
        this.totalRate = totalRate;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
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

    @Override
    public String toString() {
        return "Group{" +
                "groId='" + groId + '\'' +
                ", curId='" + curId + '\'' +
                ", stuId='" + stuId + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuClass='" + stuClass + '\'' +
                ", stuMajor='" + stuMajor + '\'' +
                ", topId='" + topId + '\'' +
                ", curYear=" + curYear +
                ", teaId='" + teaId + '\'' +
                ", teaName='" + teaName + '\'' +
                ", couName='" + couName + '\'' +
                ", topName='" + topName + '\'' +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", score3=" + score3 +
                ", sco1Count=" + sco1Count +
                ", sco2Count=" + sco2Count +
                ", sco3Count=" + sco3Count +
                ", total=" + total +
                ", report='" + report + '\'' +
                ", optResult='" + optResult + '\'' +
                ", avg1=" + avg1 +
                ", avg2=" + avg2 +
                ", avg3=" + avg3 +
                ", avgTotal=" + avgTotal +
                ", rowNo=" + rowNo +
                ", totalPeople=" + totalPeople +
                ", absentees=" + absentees +
                ", actualPeople=" + actualPeople +
                ", maxSco=" + maxSco +
                ", minSco=" + minSco +
                ", stdTotal=" + stdTotal +
                ", stdScore1=" + stdScore1 +
                ", stdScore2=" + stdScore2 +
                ", stdScore3=" + stdScore3 +
                ", great=" + great +
                ", good=" + good +
                ", general=" + general +
                ", weak=" + weak +
                ", score1Rate=" + score1Rate +
                ", score2Rate=" + score2Rate +
                ", score3Rate=" + score3Rate +
                ", totalRate=" + totalRate +
                ", scoStart='" + scoStart + '\'' +
                ", scoEnd='" + scoEnd + '\'' +
                '}';
    }
}
