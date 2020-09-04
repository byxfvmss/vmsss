package com.cjit.vms.aisino.util;

public class WebXmlFunc {
	private WebXmlFunc() {
	}

	/**
	 * 将输入的XML串转义
	 * 
	 * @return
	 */
	public static String escapeXml(String xml) {
		if (xml == null) {
			return "";
		}
		int length = xml.length();
		StringBuilder result = new StringBuilder(length + 50);
		for (int i = 0; i < length; i++) {
			char c = xml.charAt(i);
			switch (c) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			// case ' ':
			// if (ignoreBlank)
			// result.append(c);
			// else
			// result.append("&#xA0;");
			// break;
			default:
				result.append(c);
			}
		}
		return result.toString();
	}
}
