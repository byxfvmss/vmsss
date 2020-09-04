package com.cjit.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cjit.crms.util.StringUtil;

public class ValidatorUtil {
	public static boolean isValidPattern(String compile, String strMatch) {
		if (compile == null)
			return false;
		Pattern pattern = Pattern.compile(compile);
		Matcher matcher = pattern.matcher(strMatch);
		return matcher.matches();
	}

	/**
	 * 校验输入的ip是否正确 正确形式是xxx.xxx.xxx.xxx，且不能有001.这样的列
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isValidIP(String ip) {
		if (StringUtil.IsEmptyStr(ip))
			return false;
		return isValidPattern(ValidatorUtil.IP_PATTERN, ip);
	}

	/**
	 * 验证路径是否正确,未实现
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isValidPath(String path) {
		if (StringUtil.IsEmptyStr(path))
			return false;
		return isValidPattern(ValidatorUtil.PATH_PATTERN, path);
	}

	public static boolean isValidIpPort(String port) {
		if (StringUtil.IsEmptyStr(port))
			return false;
		try {
			if (!isValidPattern(ValidatorUtil.POSITIVE_INTERGER_PATTERN, port))
				return false;

			int ip_port = Integer.parseInt(port);
			return isValidIpPort(ip_port);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidIpPort(int port) {
		return port >= 0 && port <= 65535;
	}

	private static final String IP_PATTERN = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
	private static final String PATH_PATTERN = ".";
	private static final String POSITIVE_INTERGER_PATTERN = "^[1-9]\\d*|0$";// 匹配非负整数（正整数
																			// + 0）
}
