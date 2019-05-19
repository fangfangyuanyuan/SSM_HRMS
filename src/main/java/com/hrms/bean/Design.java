package com.hrms.bean;

public class Design {
    private Integer id;
    private Integer stuyear;
    private Integer term;
    private Integer c_id;
    private Integer t_id;
    private Integer s_id;
    private Integer d_score;
    private Integer a_score;
    private Integer r_score;
    private Integer total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStuyear() {

        return stuyear;
    }

    public void setStuyear(Integer stuyear) {
        this.stuyear = stuyear;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }

    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }

    public Integer getD_score() {
        return d_score;
    }

    public void setD_score(Integer d_score) {
        this.d_score = d_score;
    }

    public Integer getA_score() {
        return a_score;
    }

    public void setA_score(Integer a_score) {
        this.a_score = a_score;
    }

    public Integer getR_score() {
        return r_score;
    }

    public void setR_score(Integer r_score) {
        this.r_score = r_score;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Design(Integer id, Integer year, Integer term, Integer c_id, Integer t_id, Integer s_id, Integer d_score, Integer a_score, Integer r_score, Integer total) {
        this.id = id;
        this.stuyear = year;
        this.term = term;
        this.c_id = c_id;
        this.t_id = t_id;
        this.s_id = s_id;
        this.d_score = d_score;
        this.a_score = a_score;
        this.r_score = r_score;
        this.total = total;
    }

    public Design() {
    }

    @Override
    public String toString() {
        return "Design{" +
                "id=" + id +
                ", year=" + stuyear +
                ", term=" + term +
                ", c_id=" + c_id +
                ", t_id=" + t_id +
                ", s_id=" + s_id +
                ", d_score=" + d_score +
                ", a_score=" + a_score +
                ", r_score=" + r_score +
                ", total=" + total +
                '}';
    }
}
