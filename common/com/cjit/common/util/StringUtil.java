package com.cjit.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.id.UUIDHexGenerator;

/**
 * 字符串工具类
 * 
 * @since Jun 13, 2008
 */
public class StringUtil extends StringUtils{

	private static Logger logger = Logger.getLogger(StringUtil.class);
	public static final String COMMA = ",";
	public static final String DIV = "、";
	public static final String BLANK = " ";

	private StringUtil(){
	}

	/**
	 * 字符编码函数，把ISO8859-1编码的字符串转成GBK编码
	 * @param str 字符串
	 * 
	 * @return String 如果是null类型或者值为null(不分大小写)的字符串,一切返回空字符串,返回GBK编码的字符串
	 * @since Jun 13, 2008
	 */
	public static String getISOGBK(String chi){
		String result = null;
		try{
			if(chi == null || chi.equals("null")){
				result = null;
			}else{
				result = new String(chi.getBytes(CharacterEncoding.ISO_8859_1),
						CharacterEncoding.GBK);
			}
		}catch (UnsupportedEncodingException e){
			logger.error("getGBKStr is error", e);
		}
		return result;
	}

	/**
	 * 字符编码函数，把ISO8859-1编码的字符串转成UTF-8编码
	 * @param str 字符串
	 * 
	 * @return String 如果是null类型或者值为null(不分大小写)的字符串,一切返回空字符串,返回GBK编码的字符串
	 * @since Jun 13, 2008
	 */
	public static String getISOUTF(String chi){
		String result = null;
		try{
			if(chi == null || chi.equals("null")){
				result = null;
			}else{
				result = new String(chi.getBytes(CharacterEncoding.ISO_8859_1),
						CharacterEncoding.UTF8);
			}
		}catch (UnsupportedEncodingException e){
			logger.error("getGBKStr is error", e);
		}
		return result;
	}

	/**
	 * 字符编码函数，把GBK编码的字符串转成ISO8859-1编码
	 * @param text 字符串
	 * 
	 * @return String 如果是null类型或者值为null(不分大小写)的字符串,一切返回空字符串
	 * @since Jun 13, 2008
	 */
	public static String getGBKISO(String text){
		String result = "";
		try{
			result = new String(text.getBytes(CharacterEncoding.GBK),
					CharacterEncoding.ISO_8859_1);
		}catch (UnsupportedEncodingException e){
			logger.error("getGBKStr is error", e);
		}
		return result;
	}

	/**
	 * MD5编码初始化方法,取得MessageDigest实例
	 * 
	 * @return MessageDigest 实例
	 * @since Jun 17, 2008
	 */
	private static MessageDigest getMD5DigestAlgorithm()
			throws NoSuchAlgorithmException{
		return MessageDigest.getInstance("MD5");
	}

	/**
	 * 解决注入式攻击的问题
	 * @param str
	 * @return
	 */
	public static boolean checkStr(String str){
		String checkKey = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		String checkstr[] = checkKey.split("\\|");
		for(int i = 0; i < checkstr.length; i++){
			if(str.indexOf(checkstr[i]) >= 0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 取得二进制信息摘要
	 * @param source 取得摘要的二进制参数
	 * 
	 * @return String byte[]数组
	 * @since Jun 17, 2008
	 */
	private static byte[] getMD5Digest(byte[] source)
			throws NoSuchAlgorithmException{
		return getMD5DigestAlgorithm().digest(source);
	}

	/**
	 * 取得二进制信息摘要
	 * @param source 取得摘要的字符串参数
	 * 
	 * @return String byte[]数组
	 * @since Jun 17, 2008
	 */
	private static byte[] getMD5Digest(String source)
			throws NoSuchAlgorithmException{
		return getMD5Digest(source.getBytes());
	}

	/**
	 * 把字符进行MD5加密算法编码
	 * @param source 准备编码的字符串
	 * 
	 * @return String 返回MD5编码以后的字符串
	 * @since Jun 17, 2008
	 */
	public static String getMD5DigestHex(String source){
		String tmp = "";
		try{
			tmp = new String(Hex.encodeHex(getMD5Digest(source)));
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 判断传入字符是否包含中文字符
	 * @param str 待判断的字符
	 * @return boolean 不包含中文返回false,包含中文返回true
	 * 
	 * @since Jun 17, 2008
	 */
	public static boolean isChiness(String str){
		if(str == null){
			return false;
		}
		String pattern = "[\u4e00-\u9fa5]+";
		Pattern p = Pattern.compile(pattern);
		Matcher result = p.matcher(str);
		return result.find();
	}

	/**
	 * 将传入字符串转换UTF-8编码
	 * @param str 待转换的字符
	 * @return String 转换成UTF-8编码的字符串
	 * 
	 * @since Jun 17, 2008
	 */
	public static String utf8Code(String str){
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if(c >= 0 && c <= 255){
				result.append(c);
			}else{
				byte[] b = new byte[0];
				try{
					b = Character.toString(c).getBytes(CharacterEncoding.UTF8);
				}catch (Exception ex){
					ex.printStackTrace();
				}
				for(int j = 0; j < b.length; j++){
					int k = b[j];
					if(k < 0){
						k += 256;
					}
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

	/**
	 * UTF-8解码，把UTF-8编码字符解码
	 * @param str 待转换的字符
	 * @return String UTF-8编码字符解码
	 * 
	 * @since Jun 17, 2008
	 */
	public static String utf8deCode(String str){
		String result = "";
		int p = 0;
		if(str != null && str.length() > 0){
			str = str.toLowerCase();
			p = str.indexOf("%e");
			if(p == -1){
				return str;
			}
			while(p != -1){
				result += str.substring(0, p);
				str = str.substring(p, str.length());
				if("".equals(str) || str.length() < 9){
					return result;
				}
				result += CodeToWord(str.substring(0, 9));
				str = str.substring(9, str.length());
				p = str.indexOf("%e");
			}
		}
		return result + str;
	}

	/**
	 * 判断给定字符是否UTF-8编码
	 * @param str 待转换的字符
	 * @return boolean 判断是否utf-8编码,是返回true,否返回false
	 * 
	 * @since Jun 17, 2008
	 */
	public static boolean isUtf8(String str){
		str = str.toLowerCase();
		int p = str.indexOf("%");
		if(p != -1 && str.length() - p > 9){
			str = str.substring(p, p + 9);
		}
		return Utf8codeCheck(str);
	}

	/**
	 * utf-8编码转成字符
	 * @param text 待转换的字符
	 * @return String 转换完成以后的字符
	 * 
	 * @since Jun 17, 2008
	 */
	private static String CodeToWord(String text){
		String result;
		if(Utf8codeCheck(text)){
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try{
				result = new String(code, CharacterEncoding.UTF8);
			}catch (UnsupportedEncodingException ex){
				result = null;
			}
		}else{
			result = text;
		}
		return result;
	}

	/**
	 * 判断给定字符编码是否有效
	 * @param text 待判断的字符
	 * @return boolean 有效返回true,无效返回false
	 * 
	 * @since Jun 17, 2008
	 */
	private static boolean Utf8codeCheck(String text){
		String sign = "";
		if(text.startsWith("%e")){
			for(int i = 0, p = 0; p != -1; i++){
				p = text.indexOf("%", p);
				if(p != -1)
					p++;
				sign += p;
			}
		}
		return sign.equals("147-1");
	}

	/**
	 * 判断给定字符是否为数字
	 * @param str 待判断的字符
	 * @return boolean 是数字返回true,否则返回false
	 * 
	 * @since Jun 17, 2008
	 */
	public static boolean isNumLegal(String str){
		if(str == null){
			return false;
		}
		for(int i = 0; i < str.getBytes().length; i++){
			char ch = str.charAt(i);
			if(ch < '0' || ch > '9')
				return false;
		}
		return true;
	}

	/**
	 * 判断给定字符是否为日期格式（必须符合yyyy-MM-dd格式)
	 * @param str 待判断的字符
	 * @return boolean 是日期格式返回true,否则返回false
	 * 
	 * @since Jun 17, 2008
	 */
	public static boolean isDateLegal(String dateStr){
		if(dateStr == null){
			return false;
		}
		if(dateStr.length() != 10){
			return false;
		}
		String strArr[] = dateStr.split("-");
		if(strArr.length != 3){
			return false;
		}
		for(int i = 0; i < strArr.length; i++){
			if(!isNumLegal(strArr[i])){
				return false;
			}
		}
		return true;
	}

	/**
	 * 格式化定长字符串,前面不足补'0'
	 * @param str 要格式化的字符串
	 * @param size 格式化后的位数
	 * @return
	 */
	public static String formateString(String str, int size){
		return formateString(str, size, "0");
	}

	/**
	 * 格式化定长字符串,前面不足补自定义的字符
	 * @param str 要格式化的字符串
	 * @param size 格式化后的位数
	 * @param pattern 要填补的字符
	 * @return
	 */
	public static String formateString(String str, int size, String pattern){
		if(StringUtils.isBlank(str))
			return "";
		StringBuffer sb = new StringBuffer();
		for(int i = size; i > str.length(); i--){
			sb.append(pattern);
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * 随机生成一个UUID字符串
	 * @return
	 */
	public static String generateUUID(){
		return UUID.randomUUID().toString();
	}

	/**
	 * 将一个字符串数据拼成一个逗号间隔的字符串，例如： <p> <code>
	 * String [] arg = {"1", "2", "3"};
	 * <p>
	 * String test = StringUtilTools.getStringFromArray(arg);
	 * <p>
	 * test = "1,2,3";
	 * </code>
	 * @param str
	 * @return
	 */
	public static String getStringFromArray(String[] str){
		StringBuffer temp = new StringBuffer();
		if(str != null){
			for(int i = 0; i < str.length; i++){
				if(i == 0){
					temp.append(str[i].trim());
				}else{
					temp.append(COMMA + str[i].trim());
				}
			}
		}
		return temp.toString();
	}

	/**
	 * 将一个字符串数据拼成一个逗号间隔的字符串，例如： <p> <code>
	 * String [] arg = {"1", "2", "3"};
	 * <p>
	 * String test = StringUtilTools.getStringFromArray(arg);
	 * <p>
	 * test = "1,2,3";
	 * </code>
	 * @param str
	 * @return
	 */
	public static String getStringFromArrayByDiv(String[] str, String div){
		StringBuffer temp = new StringBuffer();
		if(str != null){
			for(int i = 0; i < str.length; i++){
				if(i == 0){
					temp.append(div + str[i].trim() + div);
				}else{
					temp.append(COMMA + div + str[i].trim() + div);
				}
			}
		}
		return temp.toString();
	}

	/**
	 * 将一个字符串数据拼成一个逗号间隔的字符串，例如： <p> <code>
	 * String　test = "1,2,3";
	 * <p>
	 * String []arg = StringUtilTools.getArrayFromString(test, ",");
	 * <p>
	 * 
	 * arg = {"1", "2", "3"};
	 * </code>
	 * @param str 要分割的字符串
	 * @param order 分割规则采用正则表达式
	 * @return
	 */
	public static String[] getArrayFromString(String str, String order){
		String[] temp = null;
		if(str != null){
			temp = str.split(order);
		}
		return temp;
	}

	/**
	 * 将一个字符串数据拼成一个逗号间隔的字符串，例如：
	 * @param str 要分割的字符串
	 * @return
	 */
	public static String[] getArrayFromString(String str){
		return getArrayFromString(str, DIV);
	}

	/**
	 * 得到一个类的全名：
	 * @param cls 类名
	 * @return
	 */
	public static String getClass(Class cls){
		return getArrayFromString(cls.toString(), BLANK)[1];
	}

	/**
	 * 将整数转换为中文小写字符串，各个数字依次转换， 比如整数102将被转换为"一○二"
	 * @param number 整数
	 * @return 转换后的汉字小写字符串
	 */
	public static String getStringNumber(int number){
		// 中文数字字符数组
		String[] chineseNumber = new String[] {"○", "一", "二", "三", "四", "五",
				"六", "七", "八", "九"};
		if(number < 0){
			return "负" + getStringNumber(-number);
		}else if(number < 10){
			return chineseNumber[number];
		}else{
			return getStringNumber(number / 10) + getStringNumber(number % 10);
		}
	}

	/**
	 * 将整数转换为中文的整数字符串，按汉语习惯的称呼各个数字依次转换， 比如整数20将被转换为"二十"
	 * @param number 整数(暂不支持绝对值大于99的转换)
	 * @return 转换后的中文的整数字符串
	 */
	public static String getChineseNumber(int number){
		// 中文数字字符数组
		String[] chineseNumber = new String[] {"零", "一", "二", "三", "四", "五",
				"六", "七", "八", "九"};
		// 中文单位数组
		String[] chineseUnit = new String[] {"", "十", "百", "千", "万", "十", "百",
				"千", "亿", "十", "百", "千"};
		// String sNumber = "";
		if(number < 0){
			// 负几
			return "负" + getChineseNumber(-number);
		}else if(number < 10){
			// 几
			return chineseNumber[number];
		}else if(number < 20){
			if(number % 10 == 0){
				// "十"
				return chineseUnit[1];
			}else{
				// 十几
				return chineseUnit[1] + chineseNumber[number % 10];
			}
		}else if(number < 100){
			if(number % 10 == 0){
				// 几十
				return chineseNumber[number / 10] + chineseUnit[1];
			}else{
				// 几十几
				return chineseNumber[number / 10] + chineseUnit[1]
						+ chineseNumber[number % 10];
			}
		}else{
			throw new java.lang.IllegalArgumentException("暂不支持绝对值大于99的转换");
		}
	}

	/**
	 * 随机生成指定位数且不重复的字符串.去除了部分容易混淆的字符，如1和l，o和0等， 随机范围1-9 a-z A-Z
	 * @param length 指定字符串长度
	 * @return 返回指定位数且不重复的字符串
	 */
	public static String getRandomString(int length){
		StringBuffer bu = new StringBuffer();
		String[] arr = {"2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
				"d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		Random random = new Random();
		while(bu.length() < length){
			String temp = arr[random.nextInt(57)];
			if(bu.indexOf(temp) == -1)
				bu.append(temp);
		}
		return bu.toString();
	}

	/**
	 * 获取某个范围内的随机整数
	 * @param sek 随机种子
	 * @param start 最小范围
	 * @param max 最大范围
	 * @return 整数
	 */
	public static int getRandomInt(int sek, int min, int max){
		Random random = new Random();
		int temp = 0;
		do{
			temp = random.nextInt(sek);
		}while(temp < min || temp > max);
		return temp;
	}

	/**
	 * 清除多余空格
	 * @param str 要格式化的字符串
	 * @return
	 */
	public static String cleanString(String str){
		return str == null ? "" : str.trim();
	}

	/**
	 * 清除多余空格
	 * @param str 要格式化的字符串
	 * @return
	 */
	public static String cleanLong(Long lg){
		return lg == null ? "" : lg.longValue() + "";
	}

	/**
	 * 清除多余空格
	 * @param str 要格式化的字符串
	 * @return
	 */
	public static String cleanInteger(Integer lg){
		return lg == null ? "" : lg.intValue() + "";
	}

	/**
	 * 清除多余空格
	 * @param str 要格式化的字符串
	 * @return
	 */
	public static String cleanDouble(Double dl){
		return dl == null ? "" : dl.doubleValue() + "";
	}

	/**
	 * 清除多余空格
	 * @param str 要格式化的字符串
	 * @return
	 */
	public static String cleanBigInteger(BigInteger lg){
		return lg == null ? "" : lg.toString();
	}

	/**
	 * 校验str是否为数字或字母
	 * @param str
	 * @return boolean
	 * @author tongxiaoming 2010-11-29
	 * @modify by lihaiboA 增加空格、"."、","
	 */
	public static boolean isNumberOrLatter_(String str){
		if(str == null){
			return true;
		}
		String pattern = "[a-zA-Z0-9 .,]+";
		Pattern p = Pattern.compile(pattern);
		Matcher result = p.matcher(str);
		return result.matches();
	}

	/**
	 * 校验str是否为数字或字母
	 * @param str
	 * @return boolean
	 * @author tongxiaoming 2010-11-29
	 */
	public static boolean isNumberOrLatter(String str){
		if(str == null){
			return true;
		}
		String pattern = "[a-zA-Z0-9]+";
		Pattern p = Pattern.compile(pattern);
		Matcher result = p.matcher(str);
		return result.matches();
	}

	public static boolean isLatter(String str){
		String pattern = "[a-zA-Z]+";
		Pattern p = Pattern.compile(pattern);
		Matcher result = p.matcher(str);
		return result.matches();
	}
	


	/**
	   * UUID
	   * @return
	   */
	public static String getUUID(){
		  return (String) new UUIDHexGenerator().generate(null, null);
	}
	/**
	 * @return 批次号
	 */
	public static String getBatchNo(){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHMMss"); 
		return sdf.format(new Date());
	}
	public static String getCurrentDate() throws ParseException{
		Timestamp t = new Timestamp(new Date().getTime());
		return t.toString() ;
	}

}
