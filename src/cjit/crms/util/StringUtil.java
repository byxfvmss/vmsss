package cjit.crms.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cjit.crms.util.date.DateUtil;
import cjit.crms.util.encode.Base64;

public class StringUtil extends StringUtils {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StringUtil.class);

	/**
	 * 函数功能说明： 判断字符串内容是否为数字
	 * 
	 * 参数说明： 数字型的字符串
	 * 
	 * 返回值说明: (是数字则返回true 否则返回false) true:字符串全为数字 false:字符串中存在有非数字的字符
	 * 
	 * 使用案例：checkNumber("123") 返回结果：true
	 * 
	 * 注释编写人： 陈维蜂 注释审核人: 李自安 使用案例测试人：张恩维
	 */

	public static boolean checkNumber(String s) {
		if (s == null)
			return false;
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException numberformatexception) {
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println(getPercent("1", "5"));
	}

	/**
	 * 函数功能说明： 判断字符串内容是否为数字 参数说明： 数字型的字符串 返回值说明:返回百分比 (x/y*100%)
	 * 使用案例：getPercent("1","5") 返回结果：20% x<=y 时能正常显示 注释编写人： 袁金锋 使用案例测试人：袁金锋
	 */
	public static String getPercent(String x, String y) {
		int round = 2;
		double a = Double.valueOf(x).doubleValue();
		double b = Double.valueOf(y).doubleValue();
		double z = (a / b) * 100;
		String temp = String.valueOf(z);
		if (temp.endsWith("0")) {
			round--;
			temp = temp.substring(0, temp.length() - 1);
		}
		if (temp.endsWith(".")) {
			round--;
		}
		String result = StringUtil.doubleToString(z, round) + "%";
		return result;
	}

	public static String checkStr(String s) {
		if (IsEmptyStr(s)) {
			return null;
		} else {
			return "'" + s + "'";
		}
	}

	/**
	 * 清理字符串上的链接 mxz 20090521 for 总分校验 导出.TXT
	 * 
	 * @param s
	 * @return
	 */
	public static String clearHrefLink(String s) {
		String s1 = "";
		if (IsEmptyStr(s))
			return s;
		// 简单先来一个
		int lloc = s.indexOf("</a>");
		int floc = s.indexOf(">");
		s1 = s.substring(floc + 1, lloc);

		return s1;
	}

	/**
	 * 函数功能说明： 去掉字符串中的空格 使用案例： StringUtil.count_char(" aedws sdewq ")
	 * 返回结果："aedwssdewq" 注释编写人： 袁金锋 注释审核人:
	 * 
	 * @param str
	 *            字符串
	 * @return String 字符串
	 */
	public static String clearSpace(String str) {
		String s1 = "";
		if (IsEmptyStr(str))
			return str;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n')
				s1 = s1 + c;
		}

		return s1;
	}

	/**
	 * 函数功能说明： 统计字符串中存在某个字符的次数 参数说明： s 任意字符串 c 一个字符 返回值说明: 返回字符c在字符串s中出现的次数
	 * 使用案例： StringUtil.count_char(" aedwssdewq ", 'w') 返回结果：2 注释编写人： 陈维蜂 注释审核人:
	 * 付锐涛 使用案例测试人：李自安
	 */
	public static int count_char(String s, char c) {
		int j = 0;
		if (s == null)
			return 0;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == c)
				j++;
		return j;
	}

	/**
	 * 函数功能说明： 返回字符串在字符串数组中的位置，如不存在返回-1（不区分大小写,返回第一个匹配字符串的位置） 使用案例：
	 * StringUtil.darkFindField("abc",new String[]{"c","a","ABC"}) 返回结果：2 注释编写人：
	 * 袁金锋 注释审核人:
	 * 
	 * @param str
	 *            字符串
	 * @param as
	 *            字符串数组
	 * @return 所在位置
	 */
	public static int darkFindField(String str, String as[]) {
		str = str.toUpperCase();
		String as1[] = new String[as.length];
		for (int i = 0; i < as.length; i++)
			as1[i] = as[i].toUpperCase();

		return findField(str, as1);
	}

	/**
	 * <p>
	 * 描述：双精度浮点数转换成指定标度的字符串，其采用舍入模式为向上舍入模式
	 * </p>
	 * 
	 * 注释编辑人： tongxiaoming 注释审核人：
	 * 
	 * @param d
	 *            双精度浮点数
	 * @param i
	 *            标度
	 * @return String
	 */
	public static String doubleToString(double d, int i) {

		BigDecimal bd = new BigDecimal(Double.toString(d));
		return bd.setScale(i, BigDecimal.ROUND_HALF_UP).toString();

		//
		// if (i != 0) {
		// String s = "##0";
		// String s1 = "0";
		// for (int j = 0; j < i - 1; j++)
		// s1 = "0" + s1;
		//
		// String s2 = s + "." + s1;
		// DecimalFormat decimalformat = (DecimalFormat) NumberFormat
		// .getInstance();
		// decimalformat.applyPattern(s2);
		// return decimalformat.format(d /* + Math.pow(0.1, i + 3) */);
		// } else {
		// String s = "##0";
		// DecimalFormat decimalformat = (DecimalFormat) NumberFormat
		// .getInstance();
		// decimalformat.applyPattern(s);
		//
		// return decimalformat.format(d + 0.001);
		// }

	}

	/**
	 * 函数功能说明： 金额格式化(如果小数不部分都为0，那就值返回整数部分) 使用案例：
	 * StringUtil.doubleToStringNotEndWithZero(33.0001,1) 返回结果：33 注释编写人： 袁金锋
	 * 注释审核人:
	 * 
	 * @param d
	 *            金额
	 * @param i
	 *            保留几位小数
	 * @return
	 */
	public static String doubleToStringNotEndWithZero(double d, int i) {
		String s = doubleToString(d, i);
		if (i > 0) {
			s = s.replaceAll("0+$", "");
			if (s.charAt(s.length() - 1) == '.') {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	public static String doubleToStringWithThNotZeroEnd(double d, int i) {
		String s = doubleToStringWithTh(d, i);
		if (i > 0) {
			s = s.replaceAll("0+$", "");
			if (s.charAt(s.length() - 1) == '.') {
				s = s.substring(0, s.length() - 1);
			}
		}
		return s;
	}

	public static String doubleToStringWithTh(double d, int i) {
		String ss = doubleToString(d, i);
		int indexOfPoint = ss.indexOf(".");
		String s = "#,##0";
		DecimalFormat decimalformat = (DecimalFormat) NumberFormat
				.getInstance();
		decimalformat.applyPattern(s);
		if (indexOfPoint == -1) { // 是整数
			return decimalformat.format(d);
		}
		String intString = ss.substring(0, indexOfPoint); // 整数部分
		String smallString = ss.substring(indexOfPoint + 1); // 小数部分
		// fixme ...
		return (d < 0 ? "-" : "")
				+ decimalformat.format(Math.abs(Long.parseLong(intString)))
				+ "." + smallString;

	}

	public static String[] explainTagStr(String s, char c) {
		if (IsEmptyStr(s))
			return null;
		if (s.charAt(0) == c)
			s = s.substring(1);
		int i = s.length();
		if (s.charAt(i - 1) == c)
			s = s.substring(0, i - 1);
		String as[] = null;
		String s1 = "";
		int k = count_char(s, c);
		if (k == 0) {
			as = new String[1];
			as[0] = s.trim();
			return as;
		}
		k++;
		as = new String[k];
		k = 0;
		s = s + c;
		for (int j = 0; j < s.length(); j++)
			if (s.charAt(j) == c) {
				s1 = s1.trim();
				as[k] = s1;
				s1 = "";
				k++;
			} else {
				s1 = s1 + s.charAt(j);
			}

		return as;
	}

	public static String[][] explainTagStr(String s, char c, char c1) {
		if (s.charAt(0) == c)
			s = s.substring(1);
		int i = s.length();
		if (s.charAt(i - 1) == c)
			s = s.substring(0, i - 1);
		String as[] = explainTagStr(s, c);
		String as1[][] = new String[as.length][2];
		for (int j = 0; j < as.length; j++) {
			String as2[] = explainTagStr(as[j], c1);
			if (as2 == null)
				as1[j] = new String[2];
			else
				as1[j] = as2;
		}
		return as1;
	}

	static public String[][] explainTagStr(String str, String sp1, char sp2) {
		int fromIndex = 0;
		ArrayList a1 = new ArrayList();
		while (true) {
			int pos = str.indexOf(" " + sp1 + " ", fromIndex);
			if (pos == -1)
				break;
			a1.add(replaceStr(str.substring(fromIndex, pos), " ", ""));
			fromIndex = pos + sp1.length() + 2;
		}
		String last = replaceStr(str.substring(fromIndex), " ", "");
		if (!IsEmptyStr(last))
			a1.add(last);
		String result[][] = new String[a1.size()][2];
		for (int i = 0; i < a1.size(); i++) {
			String stmp = (String) a1.get(i);
			int pos = stmp.indexOf(sp2);
			result[i][0] = stmp.substring(0, pos);
			result[i][1] = stmp.substring(pos + 1);
		}
		return result;
	}

	/**
	 * 函数功能说明： 返回字符串在字符串数组中的位置，如不存在返回-1（区分大小写,返回第一个匹配字符串的位置） 使用案例：
	 * StringUtil.darkFindField("abc",new String[]{"c","a","ABC"}) 返回结果：-1
	 * 注释编写人： 袁金锋 注释审核人:
	 * 
	 * @param str
	 *            字符串
	 * @param as
	 *            字符串数组
	 * @return 所在位置
	 */
	public static int findField(String s, String as[]) {
		if (as == null)
			return -1;
		for (int i = 0; i < as.length; i++) {
			if (as[i].equals(s))
				return i;
		}
		return -1;
	}

	public static String getCurrentDate() {
		return getCurrentDate(0);
	}

	public static String getCurrentDate(int i) {
		Calendar calendar = Calendar.getInstance();
		int j = calendar.get(1);
		int k = calendar.get(2) + 1;
		int l = calendar.get(5);
		String s = "" + j;
		String s1 = "" + k;
		String s2 = "" + l;
		if (k < 10)
			s1 = "0" + k;
		if (l < 10)
			s2 = "0" + l;
		switch (i) {
		case 0: // '\0'
		default:
			return s + "-" + s1 + "-" + s2;

		case 1: // '\001'
			return s + s1 + s2;

		case 2: // '\002'
			return null;

		case 3: // '\003'
			return null;
		}
	}

	public static String getMD5Encrypt(String input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			return Base64.encode(md.digest(input.getBytes("ISO-8859-1")));
		} catch (Exception e) {
			try {
				return Base64.encode(input.getBytes("ISO-8859-1"));
			} catch (UnsupportedEncodingException e1) {
				return input;
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: getIntValue|描述: 将字符串转换成整型值
	 * </p>
	 * <p>
	 * 注释编写人 lihaiboA <br>
	 * 注释审核人 tongxiaoming<br>
	 * </p>
	 * 
	 * @param value
	 *            输入字符串
	 * @return int型值
	 */
	public static int getIntValue(String value) {
		int v;
		try {
			v = Integer.parseInt(value);
		} catch (Exception e) {
			v = 0;
		}
		return v;
	}

	/**
	 * <p>
	 * 方法名称: getNumericString|描述: 将double转换成字符串输出
	 * </p>
	 * <p>
	 * 注释编写人 lihaiboA <br>
	 * 注释审核人 tongxiaoming <br>
	 * </p>
	 * 
	 * @param d
	 *            输入double
	 * @return 转换后的String
	 */
	public static String getNumericString(double d) {
		long l = new Double(d).longValue();
		if (l == d) {
			return String.valueOf(l);
		} else {
			return doubleToString(d, 2);
		}
	}

	public static String getNumericString(double d, int round) {
		long l = new Double(d).longValue();
		if (l == d) {
			return String.valueOf(l);
		} else {
			return doubleToString(d, round);
		}
	}

	/**
	 * Finds first index of a substring in the given source string with ignored
	 * case. This seems to be the fastest way doing this, with common string
	 * length and content (of course, with no use of Boyer-Mayer type of
	 * algorithms). Other implementations are slower: getting char array frist,
	 * lowercasing the source string, using String.regionMatch etc.
	 * 
	 * @param src
	 *            source string for examination
	 * @param subS
	 *            substring to find
	 * @param startIndex
	 *            starting index from where search begins
	 * @return index of founded substring or -1 if substring is not found
	 */
	public static int indexOfIgnoreCase(String src, String subS, int startIndex) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		int total = src.length() - sublen + 1;

		for (int i = startIndex; i < total; i++) {
			int j = 0;

			while (j < sublen) {
				char source = Character.toLowerCase(src.charAt(i + j));

				if (sub.charAt(j) != source) {
					break;
				}

				j++;
			}

			if (j == sublen) {
				return i;
			}
		}

		return -1;
	}

	public static boolean IsEmptyStr(String s) {
		int i = 0;
		if (s == null)
			return true;
		if (s.length() == 0)
			return true;
		i = s.length();
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				return false;
		}
		return true;
	}

	public static double java_truncate(double dd, int posi) {
		return Double.parseDouble(StringUtil.runTruncat(dd + "", posi));
	}

	/**
	 * 函数功能说明： 截取字符串左边的空格 参数说明： s 任意字符串 返回值说明: 返回截取字符串左边的空格后的字符串 使用案例：
	 * StringUtil.LeftTrim("\r1234\r") 返回结果："1234\r" 注释编写人： 陈维蜂 注释审核人: 张恩维
	 * 使用案例测试人：李自安
	 */
	public static String LeftTrim(String s) {
		int i = 0;
		if (s == null)
			return null;
		for (i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				break;
		}

		return s.substring(i);
	}

	public static String newDate(String s, int i, int j, int k)
			throws Exception {
		// if(!checkDate(s))
		// throw new Exception("[" + s + "] is not correct date format,please
		// use 'YYYY-MM-DD' format");
		String s1 = null;
		s1 = s.substring(0, 4);
		int l = Integer.parseInt(s1);
		s1 = s.substring(5, 7);
		int i1 = Integer.parseInt(s1);
		s1 = s.substring(8);
		int j1 = Integer.parseInt(s1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(l, i1 - 1, j1);
		calendar.add(1, i);
		calendar.add(2, j);
		calendar.add(5, k);
		i = calendar.get(1);
		j = calendar.get(2) + 1;
		k = calendar.get(5);
		String s2 = "" + i;
		String s3 = "" + j;
		String s4 = "" + k;
		if (j < 10)
			s3 = "0" + j;
		if (k < 10)
			s4 = "0" + k;
		return s2 + "-" + s3 + "-" + s4;
	}

	public static String replaceStr(String s, int i, char c) {
		return s.substring(0, i) + c + s.substring(i + 1);
	}

	public static String replaceStr(String s, String s1, String s2) {
		if (IsEmptyStr(s))
			return s;
		int i = s1.length();
		StringBuffer stringbuffer = new StringBuffer();
		String s3 = s;
		do {
			int j = s3.indexOf(s1);
			if (j >= 0) {
				stringbuffer = stringbuffer.append(s3.substring(0, j)).append(
						s2);
				s3 = s3.substring(j + i);
			} else {
				return stringbuffer.append(s3).toString();
			}
		} while (true);
	}

	/**
	 * 函数功能说明： 截取字符串右边的空格 参数说明： s 任意字符串 返回值说明: 返回截取字符串右边的空格后的字符串 使用案例：
	 * StringUtil.RightTrim("\n1234\n"), 返回结果："\n1234"; 注释编写人： 陈维蜂 注释审核人: 付锐涛
	 * 使用案例测试人：张恩维
	 */
	public static String RightTrim(String s) {
		int i = 0;
		if (s == null)
			return "";
		for (i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);
			if (c != '\t' && c != '\n' && c != '\r' && c != ' ')
				break;
		}
		return s.substring(0, i + 1);
	}

	public static String runTruncat(String s, int i) {
		int j = s.indexOf(46);
		if (j < 0)
			return s;
		String s1 = s.substring(j + 1);
		if (s1.length() <= i)
			return s;
		else
			return s.substring(0, j + i + 1);
	}

	/**
	 * 从一个表变量中获取以关键字开头的数据 参数 传递过来的表变量 要取值的表关键字 返回 返回执行后得到的数据
	 */

	public static String splitTableVar(String as_tablevar, String as_keyword) {
		String pattern = as_keyword + "\\[([^\\[]*)\\]";
		Pattern patt = Pattern.compile(pattern);
		Matcher matcher = patt.matcher(as_tablevar);
		if (matcher.find()) {
			String tag = matcher.group(1);
			return tag;
		}
		return "";
	}

	public static double stringToDouble(String s) {
		if (!checkNumber(s)) {
			return 0.0D;
		}
		return Double.parseDouble(s);
	}

	public static double stringToDouble2(String s) {
		return Double.parseDouble(s);
	}

	/**
	 * 函数功能说明： 格式化日期字符串 当日期字符串为yymmdd时，格式化为yy-mm-dd
	 * 当日期字符串为yyyymmdd时，格式化为yyyy-MM-dd 参数说明： s 日期字符串 格式：yyyyMMdd 或者 yyMMdd
	 * 返回值说明: 返回格式化后的日期字符串 使用案例： StringUtil.strToDate("100101");
	 * 返回的结果为："10-01-01"; 注释编写人： 陈维蜂 注释审核人: 李自安 使用案例测试人：付锐涛
	 */
	public static String strToDate(String s) {
		String s1;
		String s2;
		s1 = "yyyymmdd";
		s2 = "yymmdd";
		if (s.length() == s1.length())
			return s.substring(0, 4) + "-" + s.substring(4, 6) + "-"
					+ s.substring(6);
		if (s.length() == s2.length())
			return s.substring(0, 2) + "-" + s.substring(2, 4) + "-"
					+ s.substring(4);
		return s;
	}

	public static String trim(String str) {
		if (null == str) {
			return null;
		} else {
			return str.trim();
		}

	}

	public static double truncate(double d, int i) {
		String pattern = "\\d*.\\d{X}";
		pattern = pattern.replaceAll("X", String.valueOf(i));
		Pattern patt = Pattern.compile(pattern);
		Matcher matcher = patt.matcher(String.valueOf(d));
		if (matcher.find()) {
			String value = matcher.group(0);
			return Double.parseDouble(value);
		}
		return d;
	}

	public static String valueOf(Class clazz, Object obj) {
		if (Date.class == clazz) {
			return DateUtil.format((Date) obj);
		} else if (String.class == clazz) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

	public static String getErrorStackString(Throwable e) {
		java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(cw, true);
		e.printStackTrace(pw);
		return "错误信息：" + e.getMessage() + " 错误堆栈：" + cw.toString();
	}

	/**
	 * 功能说明: 填充动态SQL语句的参数,参数以#号标示<br>
	 * 创建者: 刘岩松<br>
	 * 创建时间: 2007-6-29 上午11:30:45<br>
	 * 
	 * @param sqlStr
	 *            带有#号变量的sql语句
	 * @param map
	 *            变量值的map
	 * @return
	 */
	public static String fillSqlVariable(String sqlStr, Map map) {

		Iterator iter = map.keySet().iterator();

		while (iter.hasNext()) {

			String key = iter.next().toString();
			String patternStr = "#" + key + "#";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(sqlStr);

			String value = null;
			try {
				value = map.get(key).toString();
			} catch (NullPointerException e) {
				value = "";
			}
			sqlStr = matcher.replaceAll(value);
		}
		return sqlStr;
	}

	public static String stringList2Query(List stringList) {
		String result = "";
		for (int i = 0; i < stringList.size(); i++) {
			result += "\'" + (String) stringList.get(i) + "\',";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	public static String string2Query(String param) {
		return "\'" + param.replaceAll("'", "''").replaceAll("\r\n", "\t")
				+ "\'";
	}

	public static String stringArray2StringJoinWithUserDefine(
			String[] stringArray, String joinString) {
		String result = "";
		for (int i = 0; i < stringArray.length; i++) {
			result += stringArray[i] + ",";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	public static String[] generateCode(String bank_id, int i) {
		String[] result = new String[2];
		String GbankGroupCode = "G_" + bank_id;
		String GSbankGroupCode = "GS_" + bank_id;
		result[0] = GbankGroupCode;
		result[1] = GSbankGroupCode;
		return result;
	}

	public static String encrypt(String s) {
		if (s == null) {
			s = "";
		}
		return Base64.encode(s.getBytes());
	}

	public static String decrypt(String s) {
		byte[] decode = Base64.decode(s);
		if (null == decode) {
			return s;
		}
		return new String(decode);
	}

	/**
	 * 通过指定列 输出html表格
	 * 
	 * @return
	 */
	public static String outPutterTableByHTML(List dataList, int cols) {
		StringBuffer sbf = new StringBuffer();
		if (dataList.size() <= 0) {
			throw new RuntimeException("输出表格样式是时出错" + dataList + " 中没有可用数据!");
		} else {
			sbf.append("<table>");
			for (int i = 1; i < dataList.size() + 1; i++) {
				String str = (String) dataList.get(i - 1);

				if (i == 1) {
					sbf.append("<tr>");
				}
				sbf.append("<td>" + str + "</td>");
				if (i % cols <= 0) {
					if (i == dataList.size()) {
						sbf.append("</tr>");
					} else {
						sbf.append("</tr><tr>");
					}
				}
			}
			sbf.append("</table>");
		}
		return sbf.toString();
	}

	public static String stringArray2Query(String[] stringArray) {
		String result = "";
		for (int i = 0; i < stringArray.length; i++) {
			result += "\'" + stringArray[i] + "\',";
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1)
				: result;
	}

	/**
	 * 通过指定列 输出html表格
	 * 
	 * @return
	 */
	public static String outPutterTableByTxt(List dataList, int cols) {
		StringBuffer sbf = new StringBuffer();
		if (dataList.size() <= 0) {
			throw new RuntimeException("输出表格样式是时出错" + dataList + " 中没有可用数据!");
		} else {
			for (int i = 1; i < dataList.size() + 1; i++) {
				String str = (String) dataList.get(i - 1);

				if (i == 1) {
					sbf.append("\r\n");
				}
				sbf.append(str);
				if (i % cols <= 0) {
					if (i == dataList.size()) {
					} else {
						sbf.append("\r\n");
					}
				}
			}
		}
		return sbf.toString();
	}

	public static String getDeletedStringNewLineEnterSign(String willString) {
		// only trim start and end's whitespace.
		String finalStr = willString.trim();
		if (willString.indexOf("\n") != -1) {
			finalStr = StringUtils.replace(finalStr, "\n", " ");
		}
		if (willString.indexOf("\r") != -1) {
			finalStr = StringUtils.replace(finalStr, "\r", " ");
		}
		if (willString.indexOf("\t") != -1) {
			finalStr = StringUtils.replace(finalStr, "\t", " ");
		}
		return finalStr;
	}

	public static String getListString4SqlInStatement(List l) {
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			String bank = (String) iterator.next();
			sb.append('\'').append(bank).append("',");
		}
		if (sb.toString().endsWith(",")) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String windowPathSplit2Uniform(String s) {
		return "";
	}

	/**
	 * 为String去空格，同时判断是否为null的情况 如果不是空也不是null返回true
	 * 
	 * @param oldStr
	 * @return
	 */
	public static boolean isRightString(String oldStr) {

		return !(oldStr == null || "".equals(trimSBCSpace(oldStr)));
	}

	/**
	 * 比较两个字符串大小 如果前面的大 则true
	 * 
	 * @param
	 * @return
	 */
	public static boolean isMore(String str1, String str2) {
		return Integer.parseInt(str1) > Integer.parseInt(str2);
	}

	/**
	 * @description:由一个字符串数组得到一个逗号分割的串
	 * @author:zhangjunzhan
	 * @param arg
	 * @return Dec 5, 2006
	 */
	public static String strJoin(String[] arg) {
		StringBuffer sb = new StringBuffer();
		if (arg == null)
			return null;
		for (int i = 0; i < arg.length; i++) {
			sb.append(arg[i]).append(',');
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * describe:标准化日期字符串中月份格式
	 * <p>
	 * 
	 * @param
	 * @author yuanlinying
	 * @since Dec 11, 2006
	 */
	public static String standerdMonth(String month) {
		if (month == null) {
			return null;
		}
		int i = Integer.parseInt(month);
		if (i < 10) {
			month = "0" + i;
		} else {
			month = "" + i;
		}
		return month;
	}

	/**
	 * @description:将字符串中的+号替换为" " zhangjunzhan
	 * @param s
	 * @return Dec 19, 2006
	 */
	public static String strExcludePlus(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '+')
				sb.append("%20");
			else
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * describe:给字符串(a,b,c)加引号('a','b','c')
	 * <p>
	 * 
	 * @param str
	 * @return
	 */
	public static String addQuotes(String str) {
		String result = "";
		if (isRightString(str)) {
			StringBuffer strarray = new StringBuffer();
			String[] temp = str.split(",");
			if (temp.length > 0) {
				for (int i = 0; i < temp.length; i++) {
					strarray.append('\'').append(temp[i]).append("',");
				}
				result = strarray.deleteCharAt(strarray.length() - 1)
						.toString();
				if (logger.isDebugEnabled()) {
					logger
							.debug("addQuotes(String) - ==============add auotes string is: " + result + " ========"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return result;
	}

	/**
	 * describe:给数据集list 返回加引号字符串('a','b','c')
	 * <p>
	 * 
	 * @param list
	 * @return
	 */
	public static String addQuotes(List list) {
		String result = "";
		if (list != null && list.size() > 0) {
			StringBuffer strarray = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				strarray.append('\'').append(list.get(i).toString()).append(
						"',");
			}
			result = strarray.deleteCharAt(strarray.length() - 1).toString();
			if (logger.isDebugEnabled()) {
				logger
						.debug("addQuotes(List) - ==============add auotes string is: " + result + " ========"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return result;
	}

	/**
	 * describe:给字符串('a','b','c')去引号(a,b,c)
	 * <p>
	 * 
	 * @param str
	 * @return
	 */
	public static String removeQuotes(String str) {
		String result = "";
		if (isRightString(str)) {
			StringBuffer strarray = new StringBuffer();
			String[] temp = str.split(",");
			if (temp.length > 0) {
				for (int i = 0; i < temp.length; i++) {
					strarray.append(temp[i].trim().substring(1, 2)).append(',');
				}
				result = strarray.deleteCharAt(strarray.length() - 1)
						.toString();
				if (logger.isDebugEnabled()) {
					logger
							.debug("removeQuotes(String) - ==============remove auotes string is: " + result + " ========"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		return result;
	}

	/**
	 * describe:根据jdbcDriver字符串判断数据库类型
	 * <p>
	 * 
	 * @param
	 * @author yuanlinying
	 * @since Sep 26, 2007
	 */
	public static String whichDBMS(String dbType) {

		if ((dbType == null) || ("".equals(dbType.trim()))) {
			return "db2";
		}
		String result = "";
		if (dbType.toLowerCase().indexOf("db2") != -1) {
			result = "db2";
		}
		if (dbType.toLowerCase().indexOf("oracle") != -1) {
			result = "oracle";
		}
		if (dbType.toLowerCase().indexOf("informix") != -1) {
			result = "informix";
		}
		return result;
	}

	/**
	 * describe:判断是否显示查询录入按钮
	 * <p>
	 * 
	 * @param
	 * @author liboyue
	 * @since Mar 11, 2008
	 */
	public static String isShowSel(String reportId) {
		String is = "y";
		reportId = reportId.toLowerCase();
		if (reportId.endsWith("service") || reportId.endsWith("manage")) {
			is = "n";
		}
		return is;
	}

	/**
	 * describe:判断是manage还是service
	 * <p>
	 * 
	 * @param
	 * @author liboyue
	 * @since Mar 11, 2008 JSY 认为不需要 public static String
	 *        isServiceOrManage(String reportId){ String is = "m"; reportId =
	 *        reportId.toLowerCase(); if(reportId.endsWith("service")){ is =
	 *        "s"; } return is; }
	 */

	/**
	 * describe:去除字符串前后的全角,半角空格
	 * <p>
	 * 
	 * @param
	 * @author yuanlinying
	 * @since Apr 16, 2008
	 */
	public static String trimSBCSpace(String res) {
		if (res == null) {
			return null;
		} else {
			// 先去半角
			res = res.trim();
			// 再去全角
			return res.replaceAll("^[　]+", "").replaceAll("[　]+$", "");
		}

	}

	private static String toChineseDigit1(String n) {
		String num1[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", };
		String num2[] = { "", "拾", "佰", "仟", "万", "亿", "兆", "吉", "太", "拍", "艾" };
		int len = n.length();

		if (len <= 5) {
			String ret = "";
			for (int i = 0; i < len; ++i) {
				if (n.charAt(i) == '0') {
					int j = i + 1;
					while (j < len && n.charAt(j) == '0')
						++j;
					if (j < len)
						ret += "零";
					i = j - 1;
				} else {
					ret = ret + num1[n.substring(i, i + 1).charAt(0) - '0']
							+ num2[len - i - 1];
				}
			}
			return ret;
		} else if (len <= 8) {
			String ret = toChineseDigit1(n.substring(0, len - 4));
			if (ret.length() != 0)
				ret += num2[4];
			return ret + toChineseDigit1(n.substring(len - 4));
		} else {
			String ret = toChineseDigit1(n.substring(0, len - 8));
			if (ret.length() != 0)
				ret += num2[5];
			return ret + toChineseDigit1(n.substring(len - 8));
		}
	}

	public static String toChineseDigit(String n) {
		String r = toChineseDigit1(n);
		if (r.startsWith("壹拾")) {
			r = r.replaceFirst("壹拾", "拾");
		}
		return r;
	}

	public static int getExcelColNum(String colName) {

		// remove any whitespace
		colName = colName.trim();

		StringBuffer buff = new StringBuffer(colName);

		// string to lower case, reverse then place in char array
		char chars[] = buff.reverse().toString().toLowerCase().toCharArray();

		int retVal = 0, multiplier = 0;

		for (int i = 0; i < chars.length; i++) {
			// retrieve ascii value of character, subtract 96 so number
			// corresponds to place in alphabet. ascii 'a' = 97
			multiplier = (int) chars[i] - 96;
			// mult the number by 26^(position in array)
			retVal += multiplier * Math.pow(26, i);
		}
		return retVal;
	}
}
