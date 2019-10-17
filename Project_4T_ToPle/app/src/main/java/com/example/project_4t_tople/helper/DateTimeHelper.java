package com.example.project_4t_tople.helper;

import java.util.Calendar;

public class DateTimeHelper {
    // 싱글톤 패턴 시작
    private static  DateTimeHelper instance = null;

    public static DateTimeHelper getInstance(){
        if(instance == null) instance = new DateTimeHelper();

        return instance;
    }

    public static  void freeInstance(){
        instance = null;
    }

    private DateTimeHelper(){   }

    public int[] getDate(){
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int[] result = {yy, mm, dd};
        return result;
    }

    public int[] getTime(){
        Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int nn = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        int[] result = {hh, nn, ss};
        return result;
    }
}
