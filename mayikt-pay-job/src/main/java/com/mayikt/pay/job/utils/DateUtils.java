package com.mayikt.pay.job.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    /**
     * 获取昨日时间
     *
     * @return
     */
    public static String yesterdayTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        return yesterday;
    }
}