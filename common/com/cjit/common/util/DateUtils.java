package com.cjit.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * 时间工具类，保存各种时间工具方法
 * 
 * @since Jun 13, 2008
 */
public class DateUtils {

	private static Logger logger = Logger.getLogger(DateUtils.class);
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm";
	public static final String ORA_DATE_FORMAT = "yyyyMMdd";
	public static final String ORA_DATE_FORMAT_SIMPLE = "yyMMdd";
	public static final String ORA_DATE_TIME_FORMAT = "yyyyMMddHHmm";
	public static final String ORA_DATE_TIMES_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
	public static final String ORA_DATES_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";
    public static final String ORA_DATE_TIMES_FORMAT2 = "yyyy-MM-dd HH:mm:ss";	//yangqm 20160415
	private static final int dayArray[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	private DateUtils() {
	}

	public static Date stringToDate(String dateString) {
		SimpleDateFormat df = new SimpleDateFormat();
		Date date = null;
		try {
			date = df.parse(dateString);
		} catch (Exception e) {
			logger.error("DateUtils.StringToDate error!");
			e.printStackTrace();
			// throw new RuntimeException("日期转换异常。");
		}
		return date;
	}

	/**
	 * 类型转换日期
	 * 
	 * @param dateString
	 *            日期格式
	 * @param partten
	 *            格式模板(如：yyyy-MM-dd)
	 * 
	 * @return StringToDate 类型转换后的日期
	 * @since Jun 13, 2008
	 */
	public static Date stringToDate(String dateString, String partten) {
		SimpleDateFormat df = new SimpleDateFormat(partten);
		Date date = null;
		try {
			if(StringUtil.isEmpty(dateString)){
				date=null;
			}else{
			date = df.parse(dateString);
			}
		} catch (Exception e) {
			logger.error("DateUtils.StringToDate error!");
			e.printStackTrace();
			// throw new RuntimeException("日期转换异常");
		}
		return date;
	}

	/**
	 * 日期类型转换成字符型
	 * 
	 * @param date
	 *            Date类型的给定日期
	 * @param partten
	 *            格式模板(如：yyyy-MM-dd)
	 * @return 类型转换后的字符型日期
	 * 
	 * @since Jun 13, 2008
	 */
	public static String toString(Date date, String partten) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat(partten);
			return df.format(date);
		}
	}

	/**
	 * 给定字符串日期按模板转换成Calendar类型
	 * 
	 * @param dateString
	 *            字符串形式的给定日期
	 * @param partten
	 *            格式模板(如：yyyy-MM-dd)
	 * @return 类型转换后的Calendar对象
	 * 
	 * @since Jun 13, 2008
	 */
	public static Calendar parseCalendarFormat(String dateString, String pattern) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
		Calendar cal = null;
		simpledateformat.applyPattern(pattern);
		try {
			simpledateformat.parse(dateString);
			cal = simpledateformat.getCalendar();
		} catch (Exception e) {
			logger.error("DateUtils.parseCalendarFormat error!");
			e.printStackTrace();
		}
		return cal;
	}

	/**
	 * Calendar类型转换成字符型
	 * 
	 * @param cal
	 *            Calendar日期类型
	 * @param partten
	 *            格式模板(如：yyyy-MM-dd)
	 * @return 类型转换后的字符型日期
	 * 
	 * @since Jun 13, 2008
	 */
	public static String getDateMilliFormat(Calendar cal, String pattern) {
		return toString(cal.getTime(), pattern);
	}

	/**
	 * 取得Calendar实例
	 * 
	 * @return Calendar类型实例
	 * 
	 * @since Jun 13, 2008
	 */
	public static Calendar getCalendar() {
		return GregorianCalendar.getInstance();
	}

	/**
	 * 判断当前日期是否为月末最后一天
	 * 
	 * @return boolean
	 */
	public static boolean isLastDayOfMonth() {
		Date date = new Date();
		int month = getMonth(date);
		int day = getDay(date);
		if (day == getLastDayOfMonth(month)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 取得当前年的某一个月的最后一天
	 * 
	 * @param month
	 *            数字类型的月份值
	 * @return 参数月份的最后一天
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getLastDayOfMonth(int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear()) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

	/**
	 * 取得当某年的某一个月的最后一天
	 * 
	 * @param year
	 *            数字类型的年份值
	 * @param month
	 *            数字类型的月份值
	 * @return 参数月份的最后一天
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month < 1 || month > 12) {
			return -1;
		}
		int retn = 0;
		if (month == 2) {
			if (isLeapYear(year)) {
				retn = 29;
			} else {
				retn = dayArray[month - 1];
			}
		} else {
			retn = dayArray[month - 1];
		}
		return retn;
	}

	/**
	 * 获取指定月最后一天日期
	 * 
	 * @param yyyymm
	 * @return String
	 */
	public static String getLastDayOfYearMonth(String yyyymm) {
		String lastDate = null;
		if (yyyymm != null && yyyymm.length() == 6) {
			String year = yyyymm.substring(0, 4);
			String month = yyyymm.substring(4, 6);
			int lastDay = getLastDayOfMonth(Integer.valueOf(year).intValue(),
					Integer.valueOf(month).intValue());
			lastDate = year + "-" + month + "-" + String.valueOf(lastDay);
		}
		return lastDate;
	}

	/**
	 * 取得某日期所在月的最后一天
	 * 
	 * @param date
	 *            Date类型的某日期
	 * @return 最后一天的日期型结果
	 * 
	 * @since Jun 13, 2008
	 */
	public static Date getLastDayOfMonth(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(2)) {
		case 0: // '\0'
			gc.set(5, 31);
			break;
		case 1: // '\001'
			gc.set(5, 28);
			break;
		case 2: // '\002'
			gc.set(5, 31);
			break;
		case 3: // '\003'
			gc.set(5, 30);
			break;
		case 4: // '\004'
			gc.set(5, 31);
			break;
		case 5: // '\005'
			gc.set(5, 30);
			break;
		case 6: // '\006'
			gc.set(5, 31);
			break;
		case 7: // '\007'
			gc.set(5, 31);
			break;
		case 8: // '\b'
			gc.set(5, 30);
			break;
		case 9: // '\t'
			gc.set(5, 31);
			break;
		case 10: // '\n'
			gc.set(5, 30);
			break;
		case 11: // '\013'
			gc.set(5, 31);
			break;
		}
		if (gc.get(2) == 1 && isLeapYear(gc.get(1))) {
			gc.set(5, 29);
		}
		return gc.getTime();
	}

	/**
	 * 取得某日期所在月的最后一天
	 * 
	 * @param date
	 *            Calendar类型的某日期
	 * @return 最后一天的日期型结果
	 * 
	 * @since Jun 13, 2008
	 */
	public static Calendar getLastDayOfMonth(Calendar gc) {
		switch (gc.get(2)) {
		case 0: // '\0'
			gc.set(5, 31);
			break;
		case 1: // '\001'
			gc.set(5, 28);
			break;
		case 2: // '\002'
			gc.set(5, 31);
			break;
		case 3: // '\003'
			gc.set(5, 30);
			break;
		case 4: // '\004'
			gc.set(5, 31);
			break;
		case 5: // '\005'
			gc.set(5, 30);
			break;
		case 6: // '\006'
			gc.set(5, 31);
			break;
		case 7: // '\007'
			gc.set(5, 31);
			break;
		case 8: // '\b'
			gc.set(5, 30);
			break;
		case 9: // '\t'
			gc.set(5, 31);
			break;
		case 10: // '\n'
			gc.set(5, 30);
			break;
		case 11: // '\013'
			gc.set(5, 31);
			break;
		}
		if (gc.get(2) == 1 && isLeapYear(gc.get(1))) {
			gc.set(5, 29);
		}
		return gc;
	}

	/**
	 * 取得某日期所在周的最后一天
	 * 
	 * @param date
	 *            Date类型日期参数
	 * @return 所在周的最后一天日期
	 * 
	 * @since Jun 13, 2008
	 */
	public static Date getLastDayOfWeek(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(7)) {
		case 1: // '\001'
			gc.add(5, 6);
			break;
		case 2: // '\002'
			gc.add(5, 5);
			break;
		case 3: // '\003'
			gc.add(5, 4);
			break;
		case 4: // '\004'
			gc.add(5, 3);
			break;
		case 5: // '\005'
			gc.add(5, 2);
			break;
		case 6: // '\006'
			gc.add(5, 1);
			break;
		case 7: // '\007'
			gc.add(5, 0);
			break;
		}
		return gc.getTime();
	}

	/**
	 * 取得某日期所在周的第一天
	 * 
	 * @param date
	 *            Date类型日期参数
	 * @return 当周的第一天日期
	 * 
	 * @since Jun 13, 2008
	 */
	public static Date getFirstDayOfWeek(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		switch (gc.get(7)) {
		case 1: // '\001'
			gc.add(5, 0);
			break;
		case 2: // '\002'
			gc.add(5, -1);
			break;
		case 3: // '\003'
			gc.add(5, -2);
			break;
		case 4: // '\004'
			gc.add(5, -3);
			break;
		case 5: // '\005'
			gc.add(5, -4);
			break;
		case 6: // '\006'
			gc.add(5, -5);
			break;
		case 7: // '\007'
			gc.add(5, -6);
			break;
		}
		return gc.getTime();
	}

	/**
	 * 当前年是否闰年
	 * 
	 * @return 返回true为润年，否则不是
	 * 
	 * @since Jun 13, 2008
	 */
	public static boolean isLeapYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		return isLeapYear(year);
	}

	/**
	 * 给定年是否闰年
	 * 
	 * @param 整数类型的年份
	 * @return 返回true为润年，否则不是
	 * 
	 * @since Jun 13, 2008
	 */
	public static boolean isLeapYear(int year) {
		if (year % 400 == 0) {
			return true;
		}
		if (year % 4 == 0) {
			return year % 100 != 0;
		} else {
			return false;
		}
	}

	/**
	 * 给定日期所在年是否闰年
	 * 
	 * @param Date
	 *            类型的日期
	 * @return 返回true为润年，否则不是
	 * 
	 * @since Jun 13, 2008
	 */
	public static boolean isLeapYear(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		int year = gc.get(1);
		return isLeapYear(year);
	}

	/**
	 * 给定日期所在年是否闰年
	 * 
	 * @param Calendar
	 *            类型的日期
	 * @return 返回true为润年，否则不是
	 * 
	 * @since Jun 13, 2008
	 */
	public static boolean isLeapYear(Calendar gc) {
		int year = gc.get(1);
		return isLeapYear(year);
	}

	/**
	 * 取得当前日期简体汉字的星期
	 * 
	 * @return 取得中文版本的星期,例如:星期三
	 * 
	 * @since Jun 13, 2008
	 */
	public static String getWeek() {
		SimpleDateFormat format = new SimpleDateFormat("E");
		Date date = new Date();
		String time = format.format(date);
		return time;
	}

	/**
	 * 取得给定日期简体汉字的星期
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 取得中文版本的星期,例如:星期三
	 * 
	 * @since Jun 13, 2008
	 */
	public static String getWeek(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("E");
		String time = format.format(date);
		return time;
	}

	/**
	 * 取得给定日期年份
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的年(如:2008)
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getYear(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(1);
	}

	/**
	 * 取得给定日期月份
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的月
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(2) + 1;
	}

	/**
	 * 取得给定日期是日
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的日
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getDay(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(5);
	}

	/**
	 * 取得给定日期是日
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的日
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(11);
	}

	/**
	 * 取得给定日期的分钟
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的分钟
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getMinute(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(12);
	}

	/**
	 * 取得给定日期的秒
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 整数类型的秒
	 * 
	 * @since Jun 13, 2008
	 */
	public static int getSecond(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(13);
	}

	/**
	 * 取得给定日期的下一天日期
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 下一天的日期
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date getNextDay(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(5, 1);
		return gc.getTime();
	}

	/**
	 * 返回指定日期 date 前count天的日期对象。
	 * 
	 * @param date
	 *            需要修改的基准日期
	 * @param count
	 *            调整的天数
	 * @return
	 */
	public static Date getPreData(Date date, int count) {
		Date d = null;
		d = new Date(date.getTime() - count * 24 * 60 * 60 * 1000l);
		return d;
	}

	public static String getPreDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(GregorianCalendar.DATE, -1);
		SimpleDateFormat df = new SimpleDateFormat(ORA_DATES_FORMAT);
		return df.format(calendar.getTime());
	}

	/**
	 * 返回指定日期 date 后count天的日期对象。
	 * 
	 * @param date
	 *            需要修改的基准日期
	 * @param count
	 *            调整的天数
	 * @return
	 */
	public static Date getAfterData(Date date, int count) {
		Date d = null;
		d = new Date(date.getTime() + count * 24 * 60 * 60 * 1000l);
		return d;
	}

	/**
	 * 取得给定日期的一个月后的日期
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 给定日期的一个月后的日期
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date getNextMonth(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(2, 1);
		return gc.getTime();
	}

	/**
	 * 取得给定日期的一个星期后的日期
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 给定日期的一个星期后的日期
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date getNextWeek(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(5, 7);
		return gc.getTime();
	}

	/**
	 * 取得给定日期的所在月的第一天
	 * 
	 * @param date
	 *            Date 类型的日期
	 * @return 给定日期的所在月的第一天
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date getFirstDayOfMonth(Date date) {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.set(5, 1);
		return gc.getTime();
	}

	/**
	 * 取得两个给定日期之差的天数
	 * 
	 * @param lowerLimitDate
	 *            Date 类型的前日期
	 * @param upperLimitDate
	 *            Date 类型的后日期
	 * @return 整数类型的两个日期之差的天数
	 * @exception java.lang.IllegalArgumentException
	 *                如果前日期大于后日期，抛出java.lang.IllegalArgumentException异常
	 * 
	 * @since Jun 14, 2008
	 */
	public static int getDayInRange(Date lowerLimitDate, Date upperLimitDate) {
		long upperTime, lowerTime;
		upperTime = upperLimitDate.getTime();
		lowerTime = lowerLimitDate.getTime();
		if (upperTime < lowerTime) {
			logger.error("param is error!",
					new java.lang.IllegalArgumentException());
		}
		Long result = new Long((upperTime - lowerTime) / (1000 * 60 * 60 * 24));
		return result.intValue();
	}

	/**
	 * 比较两个日期
	 * 
	 * @param lowerLimitDate
	 *            给定比较日期1
	 * @param upperLimitDate
	 *            给定比较日期2
	 * @return 如果 给定比较日期1大于给定比较日期2返回true，否则返回false
	 * @exception java.lang.IllegalArgumentException
	 *                如果给定参数为空，抛出java.lang.IllegalArgumentException异常
	 * 
	 * @since Jun 14, 2008
	 */
	public static boolean checkOverLimited(Date beginLimitDate,
			Date endLimitDate) {
		if (beginLimitDate == null || endLimitDate == null)
			logger.error("param is error!",
					new java.lang.IllegalArgumentException());
		if (beginLimitDate.compareTo(endLimitDate) > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算两个 calendar days 之间的天数
	 * 
	 * @param begin
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @author zhouxusheng
	 * @return 使用SimpleDateFormat，规定好格式，parser出错即为非法
	 *         使用DateToStr或者StrToDate格式化，返回的永远是大于零的数字
	 */
	public static int getDaysBetween(Calendar begin, Calendar end) {
		if (begin.after(end)) {
			Calendar swap = begin;
			begin = end;
			end = swap;
		}
		int days = end.get(Calendar.DAY_OF_YEAR)
				- begin.get(Calendar.DAY_OF_YEAR);
		int y2 = end.get(Calendar.YEAR);
		if (begin.get(Calendar.YEAR) != y2) {
			begin = (Calendar) begin.clone();
			do {
				days += begin.getActualMaximum(Calendar.DAY_OF_YEAR);
				begin.add(Calendar.YEAR, 1);
			} while (begin.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 计算给定日期与给定天数的计算结果（N天后，或N天前）
	 * 
	 * @param date
	 *            给定的日期类型
	 * @param count
	 *            整数天，如果为负数；代表day天前。如果为正数：代表day天后
	 * @return Date型计算后的日期对象
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date modDay(Date date, int day) {
		try {
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);
			cd.add(Calendar.DATE, day);
			return cd.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算给定日期与给定小时数的计算结果（N小时后，或N小时前）
	 * 
	 * @param date
	 *            给定的日期类型
	 * @param count
	 *            整数小时，如果为负数；代表time小时前。如果为正数：代表time小时后
	 * @return Date型计算后的日期对象
	 * 
	 * @since Jun 14, 2008
	 */
	public static Date modHour(Date date, int time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, time);
		return cal.getTime();
	}

	// 获得服务器当天日期yyMMdd格式
	public static String serverCurrentDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				ORA_DATE_FORMAT_SIMPLE);
		String str = formatter.format(date);
		return str;
	}

	// 获得服务器当天日期yyMMdd格式
	public static String serverCurrentDate(String dateFormat) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date);
		return str;
	}

	// 得到当前日期上个月的信息
	public static String getPreMonth() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(GregorianCalendar.MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		return df.format(calendar.getTime());
	}

	public static String getPreMonth(String strDate, String patterne) {
		Date dDate = stringToDate(strDate, patterne);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dDate);
		calendar.add(GregorianCalendar.MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		return df.format(calendar.getTime());
	}

	// 获得服务器当前时点yyyy-MM-dd HH:mm:ss.SSS格式
	public static String serverCurrentDateTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(ORA_DATE_TIMES_FORMAT);
		String str = formatter.format(date);
		return str;
	}
	
	/* 获得服务器当前时点yyyy-MM-dd HH:mm:ss格式
	*  yangqm 20160415
	*/
	public static String getCurrentDateTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(ORA_DATE_TIMES_FORMAT2);
		String str = formatter.format(date);
		return str;
	}

	// 获得服务器当前时点yyyyMMddHHmmssSSS格式
	public static String serverCurrentTimeStamp() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
		String str = formatter.format(date);
		return str;
	}

	// 获得服务器当前时点yyyyMMdd格式
	public static String serverCurrentDetailDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(ORA_DATE_FORMAT);
		String str = formatter.format(date);
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(String.valueOf(new Random().nextInt(1000)));
	}

	public static boolean isValidDate(String date, String pattern) {
		try {
			new SimpleDateFormat(pattern).parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 严格校验格式是否正确
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static boolean isValidDateStrict(String date, String pattern) {
		String time1 = date;
		String time2 = formatDate(constructDate(date, pattern), pattern);
		// System.out.println("time1 ="+ time1 + " , time2 = "+ time2 + " ,
		// equals ? ="+time1.equals(time2));
		return time2 != null && time1.equals(time2);
	}

	private static Date constructDate(String strDate, String pattern) {
		Date exitDate = null;
		if (strDate != null) {
			try {
				DateFormat df = new SimpleDateFormat(pattern);
				exitDate = df.parse(strDate);
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
		return exitDate;
	}

	private static String formatDate(Date exitDate, String pattern) {
		String s = null;
		if (exitDate != null) {
			DateFormat df = new SimpleDateFormat(pattern);
			s = df.format(exitDate);
		}
		return s;
	}
	/**
     * 
     * @return 上一个月的当前时间
     */
    public static String getBeforeDay(){
    	 Calendar c = Calendar.getInstance();
    	 c.add(Calendar.MONTH, -1);
    	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	 String preMonth = dateFormat.format(c.getTime()); 
    	 return preMonth;
    }
   
    /**
     * 
     * @return 获取今日日期
     */
    public static String getNowDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s);
        return str.toString();
}
   
}
