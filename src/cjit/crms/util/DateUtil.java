package cjit.crms.util;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Created by WangJm on 2015/8/14.
 */
public class DateUtil {
    public static final DateUtil NULL = new DateUtil();
    public static final int YEAR = Calendar.YEAR;
    public static final int MONTH = Calendar.MONTH;
    public static final int WEEK_OF_YEAR = Calendar.WEEK_OF_YEAR;
    public static final int WEEK_OF_MONTH = Calendar.WEEK_OF_MONTH;
    public static final int DATE = Calendar.DATE;
    public static final int DATE_OF_YEAR = Calendar.DAY_OF_YEAR;
    public static final int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;
    public static final int DAY_OF_WEEK = Calendar.DAY_OF_WEEK;
    public static final int DAY_OF_WEEK_IN_MONTH = Calendar.DAY_OF_WEEK_IN_MONTH;
    public static final int HOUR = Calendar.HOUR;
    public static final int HOUR_OF_DAY = Calendar.HOUR_OF_DAY;
    public static final int MINUTE = Calendar.MINUTE;
    public static final int SECOND = Calendar.SECOND;
    public static final int MILLISECOND = Calendar.MILLISECOND;
    public static final int SUNDAY = Calendar.SUNDAY;
    public static final int MONDAY = Calendar.MONDAY;
    public static final int TUESDAY = Calendar.TUESDAY;
    public static final int WEDNESDAY = Calendar.WEDNESDAY;
    public static final int THURSDAY = Calendar.THURSDAY;
    public static final int FRIDAY = Calendar.FRIDAY;
    public static final int SATURDAY = Calendar.SATURDAY;
    public static final int JANUARY = Calendar.JANUARY;
    public static final int FEBRUARY = Calendar.FEBRUARY;
    public static final int MARCH = Calendar.MARCH;
    public static final int APRIL = Calendar.APRIL;
    public static final int MAY = Calendar.MAY;
    public static final int JUNE = Calendar.JUNE;
    public static final int JULY = Calendar.JULY;
    public static final int AUGUST = Calendar.AUGUST;
    public static final int SEPTEMBER = Calendar.SEPTEMBER;
    public static final int OCTOBER = Calendar.OCTOBER;
    public static final int NOVEMBER = Calendar.NOVEMBER;
    public static final int DECEMBER = Calendar.DECEMBER;
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_MONTH = "yyyy-MM";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_HOUR = "yyyy-MM-dd HH:";
    public static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_MONTH_DATE = "MM-dd";
    public static final String FORMAT_MONTH_HOUR = "MM-dd HH:";
    public static final String FORMAT_DATE_HOUR = "-dd HH:";
    public static final String FORMAT_HOUR_MINUTE = "HH:mm";
    public static final String FORMAT_MINUTE_SECOND = "mm:ss";
    public static final String FORMAT_HOUR_SECOND = "HH:mm:ss";
    public static final String FORMAT_MONTH_MINUTE = "MM-dd HH:mm";
    private static SimpleDateFormat sdf;


    public static Date getDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(YEAR);
    }

    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(MONTH) + 1;
    }

    public static Date addDays(Date date, int amonut) {
        return DateUtils.addDays(date, amonut);
    }

    public static Date addHours(Date date, int amonut) {
        return DateUtils.addHours(date, amonut);
    }

    public static Date addMinutes(Date date, int amonut) {
        return DateUtils.addMinutes(date, amonut);
    }

    public static Date addWeeks(Date date, int amonut) {
        return DateUtils.addWeeks(date, amonut);
    }

    public static Date addYears(Date date, int amonut) {
        return DateUtils.addYears(date, amonut);
    }

    public static Long getFragmentInHours(int fragment) {
        return DateUtils.getFragmentInHours(DateUtil.getDate(), fragment);
    }

    public static Long getFragmentInHours(Date date, int fragment) {
        return DateUtils.getFragmentInHours(date, fragment);
    }

    public static Long getFragmentInDays(int fragment) {
        return DateUtils.getFragmentInDays(DateUtil.getDate(), fragment);
    }

    public static Long getFragmentInDays(Date date, int fragment) {
        return DateUtils.getFragmentInDays(date, fragment);
    }

    public static Long getFragmentInMinutes(int fragment) {
        return DateUtils.getFragmentInMinutes(DateUtil.getDate(), fragment);
    }

    public static Long getFragmentInMinutes(Date date, int fragment) {
        return DateUtils.getFragmentInMinutes(date, fragment);
    }

    public static Long getFragmentInSeconds(int fragment) {
        return DateUtils.getFragmentInSeconds(DateUtil.getDate(), fragment);
    }

    public static Long getFragmentInSeconds(Date date, int fragment) {
        return DateUtils.getFragmentInSeconds(date, fragment);
    }


    public static Date addMonth(Date date, int amonut) {
        return DateUtils.addMonths(date, amonut);
    }

    public static int getWeekOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(WEEK_OF_MONTH);
    }

    public static int getDayOfWeekInMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(DAY_OF_WEEK_IN_MONTH);
    }

    public static int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

//    public static String parseDateToString(String formatArg) {
//        sdf = new SimpleDateFormat(formatArg);
//        return sdf.format(getDate());
//    }
    
    public static String parseDateToString(Date date) {
    	sdf = new SimpleDateFormat(FORMAT_DATE);
    	return sdf.format(date);
    }

    public static String parseDateToString(Date date, String formatArg) {
        sdf = new SimpleDateFormat(formatArg);
        return sdf.format(date);
    }

    public static Date parseStringToDate(String date, String formatArg) throws java.text.ParseException {
        sdf = new SimpleDateFormat(formatArg);
        return sdf.parse(date);
    }
    public static Date parseStringToDate(String date) throws java.text.ParseException {
    	sdf = new SimpleDateFormat(FORMAT_DATE);
    	return sdf.parse(date);
    }

    public static int compareToDate(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date2);
        return cal.compareTo(ca);
    }
}
