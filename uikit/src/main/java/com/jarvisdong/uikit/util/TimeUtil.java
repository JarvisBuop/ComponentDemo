package com.jarvisdong.uikit.util;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by abe on 2017/2/28.
 */

/**
 * 根据当前系统时间格式化成编号
 * m 分
 * s 秒
 * S 毫秒
 */
public class TimeUtil {
    public static SimpleDateFormat getUniqueFormat() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

    public static SimpleDateFormat getTargetFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf;
    }

    public static SimpleDateFormat getNormalFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf;
    }

    public static String setLong2StringByTargetFormat(long datams, SimpleDateFormat sdf) {
        return sdf.format(new Date(datams));
    }

    public static Date getDate(String str) {
        return TimeUtil.str2Date(str, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getTimeSampleNumber() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//用于生成隐患编号格式化输出
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }

    /**
     * @param calendar
     * @return yyyy-MM-dd HH:mm
     */
    public static String calendar2str(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(calendar.getTime());
    }

    public static Date str2Date(String str) {
        if (str != null && !str.equals("")) {
            try {
                SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return dateFormater.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String data2Str(Date date, SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat != null) {
            return simpleDateFormat.format(date);
        }
        return "";
    }

    /**
     * string->data by simpledataformat
     *
     * @param str
     * @return
     */
    public static Date str2Date(String str, SimpleDateFormat simpleDateFormat) {
        if (str != null && !str.equals("")) {
            try {
                return simpleDateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取毫秒值
     *
     * @param str
     * @param simpleDateFormat
     * @return
     */
    public static long getMillTimeFormat(String str, SimpleDateFormat simpleDateFormat) {
        if (str != null && !str.equals("")) {
            try {
                return simpleDateFormat.parse(str).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }


    /**
     * 比较日期;
     *
     * @param calendar
     * @return
     */
    public static int isAfterCurrentTimes(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        if (calendar.getTime().getTime() < now.getTime().getTime()) {
            return -1;
        } else if (calendar.getTime().getTime() == now.getTime().getTime()) {
            return 0;
        } else {
            return 1;
        }
    }

    //开始时间判断;
    public static boolean isAfter10minus(Calendar selectedTime) {
        if (selectedTime.getTimeInMillis() < System.currentTimeMillis() - 10 * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }

    //截止时间判断;
    public static boolean isAfter30hour(Calendar selectedTime) {
        if (selectedTime.getTimeInMillis() - System.currentTimeMillis() < 30 * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }

    public static long formatTimeToTime(String formatTime) {
        long time;
        if (TextUtils.isEmpty(formatTime)) {
            return 0;
        }
        int index = formatTime.indexOf(":");
        String hour = formatTime.substring(0, index);
        String minute = formatTime.substring(index + 1);
        time = Long.parseLong(hour) * 60 + Long.parseLong(minute);
        return time * 1000;
    }

    public static String secondToTime(int time) {
        int seconds = time / 1000;
        SimpleDateFormat formatter;
        if (seconds < 60) {
            formatter = new SimpleDateFormat("ss:S");//初始化Formatter的转换格式。
        } else {
            formatter = new SimpleDateFormat("mm:ss:S");//初始化Formatter的转换格式。
        }

        String hms = formatter.format(time);
        return hms;
    }
}
