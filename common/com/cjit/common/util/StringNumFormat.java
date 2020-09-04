/**
 * 
 */
package com.cjit.common.util;

import java.text.DecimalFormat;

/**
 * @author yulubin
 */
public class StringNumFormat{

	public static String PERCT_FORMAT = "#.##%";
	public static String DOUBLE_FORMAT = "#.##";
	public static String PERCT_STRING = "0.00%";
	public static String DOUBLE_STRING = "0.00";

	public static String getPerct(double d){
		return getFormatString(d, PERCT_STRING);
	}

	public static String getPerct(String d){
		try{
			Double.valueOf(d);
		}catch (Exception e){
			return "";
		}
		DecimalFormat fmt = new DecimalFormat(PERCT_STRING);
		fmt.setDecimalSeparatorAlwaysShown(false);
		return fmt.format(Double.parseDouble(d));
	}

	public static double getDouble(double d){
		String s = getFormatString(d, DOUBLE_FORMAT);
		return Double.parseDouble(s);
	}

	public static String getDouble(String d){
		try{
			Double.valueOf(d);
		}catch (Exception e){
			return "";
		}
		DecimalFormat fmt = new DecimalFormat(DOUBLE_STRING);
		fmt.setDecimalSeparatorAlwaysShown(false);
		return fmt.format(Double.parseDouble(d));
	}

	public static String getDoubleString(double d){
		return getFormatString(d, DOUBLE_FORMAT);
	}

	public static String getFormatString(double d, String format){
		DecimalFormat fmt = new DecimalFormat(format);
		fmt.setDecimalSeparatorAlwaysShown(false);
		return fmt.format(d);
	}

	public static String getFormatLong(long d, String format){
		DecimalFormat fmt = new DecimalFormat(format);
		return fmt.format(d);
	}

	public static void main(String[] args){
		// NumberFormat fmt = NumberFormat.getInstance();
		// NumberFormat fmt2 = NumberFormat.getIntegerInstance();
		// NumberFormat fmt3 = NumberFormat.getPercentInstance();
		// NumberFormat fmt4 = NumberFormat.getCurrencyInstance();
		System.out.println(getDouble(1123232.54525));
	}
}
