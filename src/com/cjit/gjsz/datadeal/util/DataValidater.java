package com.cjit.gjsz.datadeal.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.CharacterEncoding;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;

public class DataValidater{

	public static void main(String[] args) throws Exception{
		// String s = ".";
		// String[] a = s.split("\\.");
		// for (int i = 0; i < a.length; i++) {
		// System.out.println(a[i]);
		// }
		// System.out.println("gggg");
		System.out.println(validateDataType("  ", "n, 0,6,1 ", false, null));
	}

	public static boolean validateDataType(String data, String dataType,
			boolean isSkipBlanks, RptColumnInfo column) throws Exception{
		return validateDataType(data, dataType, new StringBuffer(),
				isSkipBlanks, column);
	}

	public static boolean validateDataType(String data, String dataType,
			StringBuffer sb, boolean isSkipBlanks, RptColumnInfo column)
			throws Exception{
		// 校验规则不存在／为空字符串／为table，直接校验通过
		if(StringUtil.isEmpty(dataType) || "table".equalsIgnoreCase(dataType)){
			return true;
		}
		boolean sj = false;
		// 待校验的字符串和数据类型trim处理
		data = (data == null) ? "" : data.trim();
		dataType = dataType.trim();
		// 数据类型各部分trim处理
		String[] rules = dataType.split(",");
		int n = rules.length;
		for(int i = 0; i < n; i++){
			rules[i] = rules[i].trim();
		}
		if(isSkipBlanks){
			rules[1] = "0";
		}
		// 具体校验
		if(n == 2 && "d".equalsIgnoreCase(rules[0])){
			return dateCheck(data, Integer.parseInt(rules[1]), sb);
		}else if(n == 3 && "s".equalsIgnoreCase(rules[0])){
			if(StringUtils.isNotEmpty(column.getConsRule())){
				sj = stringCheck(data, Integer.parseInt(rules[1]), Integer
						.parseInt(rules[2]), sb);
				if(StringUtil.isEmpty(data) && "0".equals(rules[1])){
				}else{
					boolean consRule = Pattern.compile(
							column.getConsRule().trim()).matcher(data)
							.matches();
					if(!consRule){
						sb.append(column.getDataTypeVDesc());
						sj = false;
					}
				}
			}else{
				sj = stringCheck(data, Integer.parseInt(rules[1]), Integer
						.parseInt(rules[2]), sb);
			}
		}else if(n == 4 && "s".equalsIgnoreCase(rules[0])){
			if(rules[2].equals(rules[3])){
				sj = stringFixCheck(data, Integer.parseInt(rules[1]), Integer
						.parseInt(rules[3]), sb, column.getTagType(), column
						.getDataTypeVDesc());
			}else{
				sj = stringFixCheck(data, Integer.parseInt(rules[1]), Integer
						.parseInt(rules[2]), Integer.parseInt(rules[3]), sb,
						column.getTagType(), column.getDataTypeVDesc());
			}
			if(StringUtils.isNotEmpty(column.getConsRule())){
				if(!StringUtil.isEmpty(data) && !"0".equals(rules[1])){
					boolean consRule = Pattern.compile(
							column.getConsRule().trim()).matcher(data)
							.matches();
					if(!consRule){
						sb.append(column.getDataTypeVDesc());
						sj = false;
					}
				}
			}
		}else if(n == 4 && "n".equalsIgnoreCase(rules[0])){
			return decimalCheck(data, Integer.parseInt(rules[1]), Integer
					.parseInt(rules[2]), Integer.parseInt(rules[3]), sb);
		}else{
			sj = false;
			sb.append("不支持的数据类型，请认真核对。");
		}
		return sj;
	}

	private static boolean stringCheck(String s, int minLength, int maxLength,
			StringBuffer sb){
		try{
			int realLen = s.getBytes(CharacterEncoding.GBK).length;
			if(realLen >= minLength){
				if(maxLength == -1){
					return true;
				}else if(realLen <= maxLength){
					return true;
				}
			}
		}catch (Exception e){
		}
		if(minLength > 0){
			sb.append(" 字符串格式验证错误,最小长度值应为 [" + minLength + "],最大长度值应为 ["
					+ maxLength + "]，中文最大长度减半");
		}else{
			sb.append(" 字符串格式验证错误,最大长度值应为 [" + maxLength + "]，中文最大长度减半");
		}
		return false;
	}

	private static boolean decimalCheck(String s, int minLength, int maxLength,
			int precision, StringBuffer sb){
		/*try {
			String[] parts = s.split("\\.");
			int n = parts.length;
			// 判断字符串中是否包含了字母.不允许包含字母
			for (int i = 0; i < n; i++) {
				char[] cs = parts[i].toCharArray();
				for (int j = 0; j < cs.length; j++) {
					if (!Character.isDigit(cs[j])) {
						sb.append(" 数值型数据验证错误，不允许含有 [" + cs[j] + "] 字符。");
						return false;
					}
				}
			}

			// 只输入小数点，因无法保存到数据库，返回false
			if (".".equals(s)) {
				sb.append(" 数值型数据验证错误 [不能只有小数点]。");
				return false;
			}
			// 空字符串，并且最小长度为０
			if ("".equals(s) && 0 == minLength) {
				return true;
			}
			// 没有小数点，只有整数部分：总位数==整数位数。总位数大于等于minLength;总位数小于等于maxLength
			if (n == 0 && s.length() >= minLength && s.length() <= maxLength) {
				return true;
			}
			// 有小数点，只有整数部分:总位数==整数位数。总位数大于等于minLength;总位数小于等于maxLength
			else if (n == 1 && parts[0].length() >= minLength
					&& parts[0].length() <= maxLength) {
				return true;
			}
			// 有整数部分和小数部分：整数位数大于等于minLength;整数位数小于等于maxLength;
			// 小数部分位数小于等于precision
			else if (n == 2) {
				if (precision > 0) {
					if (parts[0].length() >= minLength
							&& parts[0].length() <= maxLength
							&& parts[1].length() <= precision) {
						return true;
					}
				} else {
					if (parts[0].length() >= minLength
							&& parts[0].length() <= maxLength) {
						return true;
					}
				}
			}

			Double.parseDouble(s);
		} catch (Exception e) {

		}
		sb.append(" 数值格式验证错误,最小长度值应为 [" + minLength + "],整数部分最大长度值应为 [" + maxLength
				+ "], 精度小于 [" + precision + "]");
		return false;*/
		try{
			String[] parts = s.split("\\.");
			int n = parts.length;
			if(n > 1 && precision == 0){
				sb.append(" 数值型数据验证错误，不允许含小数。");
				return false;
			}
			// 判断字符串中是否包含了字母.不允许包含字母
			for(int i = 0; i < n; i++){
				char[] cs = parts[i].toCharArray();
				for(int j = 0; j < cs.length; j++){
					if(!Character.isDigit(cs[j])){
						if(!("-".equals(String.valueOf(cs[j])) || ("+"
								.equals(String.valueOf(cs[j]))))){
							sb.append(" 数值型数据验证错误，不允许含有 [" + cs[j] + "] 字符。");
							return false;
						}
					}
				}
			}
			// 只输入小数点，因无法保存到数据库，返回false
			if(".".equals(s)){
				sb.append(" 数值型数据验证错误 [不能只有小数点]。");
				return false;
			}
			// 空字符串，并且最小长度为０
			if("".equals(s) && 0 == minLength){
				return true;
			}
			// 纯小数
			if(maxLength == precision){
				if(Double.parseDouble(s) >= 1 || Double.parseDouble(s) <= 0){
					sb.append(" 数值型数据验证错误 [数据只能为小数值]。");
					return false;
				}
				return true;
			}
			// 没有小数点，只有整数部分：总位数==整数位数。总位数大于等于minLength;总位数小于等于maxLength
			if(n == 0 && s.length() >= minLength && s.length() <= maxLength){
				return true;
			}
			// 有小数点，只有整数部分:总位数==整数位数。总位数大于等于minLength;总位数小于等于maxLength
			else if(n == 1 && parts[0].length() >= minLength
					&& parts[0].length() <= (maxLength - precision)){
				return true;
			}
			// 有整数部分和小数部分：整数位数大于等于minLength;整数位数小于等于maxLength;
			// 小数部分位数小于等于precision
			else if(n == 2){
				if(precision > 0){
					if(parts[0].length() >= minLength
							&& parts[0].length() <= (maxLength - precision)
							&& parts[1].length() <= precision){
						return true;
					}
				}else{
					if(parts[0].length() >= minLength
							&& parts[0].length() <= maxLength){
						return true;
					}
				}
			}
			Double.parseDouble(s);
		}catch (Exception e){
		}
		if(minLength > 0)
			sb.append(" 数值格式验证错误,数据不能为空且最小长度值应为 [" + minLength
					+ "],整数部分最大长度值应为 [" + (maxLength - precision) + "], 精度小于 ["
					+ precision + "]");
		else
			sb.append(" 数值格式验证错误,最小长度值应为 [" + minLength + "],整数部分最大长度值应为 ["
					+ (maxLength - precision) + "], 精度小于 [" + precision + "]");
		return false;
	}

	private static boolean dateCheck(String s, int must, StringBuffer sb){
		// 如果是选输项，则为空时返回true
		if(must == 0){
			return true;
		}
		if(StringUtil.isEmpty(s)){
			sb.append("日期格式为必输项");
			return false;
		}
		if(StringUtil.isNotEmpty(s) && s.trim().length() > 8){
			sb.append(" 日期格式有误正确格式为 [yyyyMMdd]");
			return false;
		}
		try{
			DateUtils.stringToDate(s, DateUtils.ORA_DATE_FORMAT);
			return true;
		}catch (Exception ex){
			try{
				DateUtils.stringToDate(s, DateUtils.ORA_DATE_FORMAT_SIMPLE);
				return true;
			}catch (Exception ex2){
			}
		}
		sb.append(" 日期格式有误正确格式为 [yyyyMMdd]");
		return false;
	}

	// /////////
	private static boolean stringFixCheck(String s, int must, int fixLen,
			StringBuffer sb, String tagType, String strDataTypeDesc){
		try{
			int realLen = s.getBytes(CharacterEncoding.GBK).length;
			if(realLen == fixLen){
				return true;
			}else if((realLen == 0) && (must == 0)){
				return true;
			}
		}catch (Exception e){
		}
		// if (fixLen > 0)
		if(tagType != null && "3".equals(tagType) && strDataTypeDesc != null
				&& !"".equals(strDataTypeDesc)){
			sb.append(strDataTypeDesc);
		}else{
			sb.append(" 字符串格式验证错误,固定长度值应为 [" + fixLen + "]，中文最大长度减半");
		}
		return false;
	}

	private static boolean stringFixCheck(String s, int must, int minLen,
			int maxLen, StringBuffer sb, String tagType, String strDataTypeDesc){
		try{
			int realLen = s.getBytes(CharacterEncoding.GBK).length;
			if(realLen <= maxLen && realLen >= minLen){
				return true;
			}else if((realLen == 0) && (must == 0)){
				return true;
			}
		}catch (Exception e){
		}
		// if (fixLen > 0)
		if(tagType != null && "3".equals(tagType) && strDataTypeDesc != null
				&& !"".equals(strDataTypeDesc)){
			sb.append(strDataTypeDesc);
		}else{
			sb.append(" 字符串格式验证错误，最小长度值应为 [" + minLen + "]、最大长度值应为 [" + maxLen
					+ "]，中文最大长度减半");
		}
		return false;
	}
}
