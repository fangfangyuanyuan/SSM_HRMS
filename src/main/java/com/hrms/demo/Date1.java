package com.hrms.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Date1 {
    public static void main(String[] args) {
        Date current = new Date();
        System.out.println(current);
        String beginTime = "2019-05-03 17:34:04";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bt= null;
        try {
            bt = sdf.parse(beginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(bt);

    }
}
