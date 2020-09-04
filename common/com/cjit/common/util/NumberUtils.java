package com.cjit.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字工具类
 * 
 * @since Jun 13, 2008
 */
public class NumberUtils {

	private NumberUtils() {
	}

	public static final String DEFAULT_DOUBLE_PATTERN = "#,##0.00";
	public static final String DEFAULT_LONG_PATTERN = "#,##0";
	public static final String DEFAULT_CURRENCY_PATTERN = "#,##0.00";
	public static final String DEFAULT_PERCENT_PATTERN = "#,##0.00%";
	public static final String DEFAULT_SEQNUMBER = "######000000";

	/**
	 * 按标准化的数字表现方式格式化浮点型数字样式,默认格式（"#,##0.00")
	 * 
	 * @param d
	 *            被格式化的double型数字
	 * 
	 * @return String 返回标准化的长型数字样式（"#,##0.00")
	 * @since Jun 16, 2008
	 */
	public static String format(double d) {
		return format(d, "#,##0.00");
	}

	/**
	 * 按标准化的数字表现方式格式化浮点型数字样式
	 * 
	 * @param d
	 *            被格式化的double型数字
	 * @param pattern
	 *            格式化的模板
	 * 
	 * @return String 返回标准化的长型数字样式（如####,##)
	 * @since Jun 16, 2008
	 */
	public static String format(double d, String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return format(d);
		} else {
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(d);
		}
	}

	public static String format(BigDecimal bd, String pattern, int i) {
		if (bd == null) {
			return "";
		}
		if (i == 2) {
			double d = new Double(bd.toString()).doubleValue();
			DecimalFormat df = new DecimalFormat("#,##0.00");
			return df.format(d);
		} else if (i == 4) {
			double d = new Double(bd.toString()).doubleValue();
			DecimalFormat df = new DecimalFormat("#,##0.0000");
			return df.format(d);
		} else {
			if (pattern == null || "".equals(pattern)) {
				return bd.toString();
			} else {
				double d = new Double(bd.toString()).doubleValue();
				DecimalFormat df = new DecimalFormat(pattern);
				return df.format(d);
			}
		}
	}
	public static String format(BigDecimal bd, String pattern, int i,int j) {
		if (bd == null) {
			return "";
		}
		double d1 = new Double(bd.toString()).doubleValue();
		DecimalFormat df1 = new DecimalFormat("#,##0.000000");
		d1=new Double(df1.format(d1)).doubleValue()+0.000001;
		df1 = new DecimalFormat("#,##0.00000");
		double d2=new Double(df1.format(d1)).doubleValue()+0.00001;
		DecimalFormat df2 = new DecimalFormat("#,##0.0000");
		double d3=new Double(df2.format(d2)).doubleValue()+0.0001;
		DecimalFormat  df3 = new DecimalFormat("#,##0.000");
		double d4=new Double(df3.format(d3)).doubleValue()+0.001;
		DecimalFormat df4 = new DecimalFormat("#,##0.00");
		return df4.format(d4);
	
	}
	
	/**
	 * 按标准化的数字表现方式格式化长型数字样式，默认格式(#,##0)
	 * 
	 * @param number
	 *            被格式化的数字
	 * 
	 * @return String 返回标准化的长型数字样式(#,##0)
	 * @since Jun 13, 2008
	 */
	public static String format(long l) {
		return format(l, "#,##0");
	}

	/**
	 * 按标准化的数字表现方式格式化长型数字样式
	 * 
	 * @param number
	 *            被格式化的数字
	 * @param pattern
	 *            格式化的模板
	 * 
	 * @return String 返回标准化的长型数字样式（如####,##)
	 * @since Jun 13, 2008
	 */
	public static String format(long l, String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return format(l);
		} else {
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(l);
		}
	}

	/**
	 * 格式化数字(浮点型)，返回本地货币类型
	 * 
	 * @param number
	 *            被格式化的数字
	 * 
	 * @return String 返回标准化货币类型样式（如￥8,888.88)
	 * @since Jun 13, 2008
	 */
	public static String currencyFormat(double number) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		return currencyFormat.format(number);
	}

	/**
	 * 格式化数字(整型)，返回本地货币类型
	 * 
	 * @param number
	 *            被格式化的数字
	 * 
	 * @return String 返回标准化货币类型样式（如￥8,888)
	 * @since Jun 13, 2008
	 */
	public static String currencyFormat(long number) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		return currencyFormat.format(number);
	}

	/**
	 * 格式化数字(整型)，返回本地百分比类型
	 * 
	 * @param number
	 *            被格式化的数字
	 * 
	 * @return String 返回本地百分比类型（如150%)
	 * @since Jun 13, 2008
	 */
	public static String percentFormat(long number) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		return percentFormat.format(number);
	}

	/**
	 * 格式化数字(浮点型)，返回本地百分比类型
	 * 
	 * @param number
	 *            被格式化的数字
	 * 
	 * @return String 返回本地百分比类型（如参数为0.5，返回50%)
	 * @since Jun 13, 2008
	 */
	public static String percentFormat(double number) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		return percentFormat.format(number);
	}
}