/**
 * 
 */
package com.cjit.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author yulubin
 */
public class DictionaryUtil{

	public static final String JICHU_INFO = "基础信息";
	public static final String SHENBAO_INFO = "申报信息";
	public static final String HEXIAO_INFO = "核销信息";
	public static final String GUANLI_INFO = "管理信息";
	public static final String UNKNOWN_INFO = "";
	public static final String JICHU_INFO_MODIFY = "修改";
	public static final String SHENBAO_INFO_MODIFY = "申报";
	public static final String HEXIAO_INFO_MODIFY = "核销";
	public static final String UNKNOWN_INFO_MODIFY = "";

	public static String getDataStatus(String s){
		if("1".equals(s)){
			return "<font color='blue'>未校验</font>";
		}else if("2".equals(s)){
			return "<font color='red'>校验未通过</font>";
		}else if("3".equals(s)){
			return "<font color='green'>校验已通过</font>";
		}else if("4".equals(s)){
			return "<font color='red'>审核未通过</font>";
		}else if("5".equals(s)){
			return "<font color='green'>审核通过</font>";
		}else if("6".equals(s)){
			return "<font color='green'>已报送</font>";
		}else{
			return "";
		}
	}

	public static String getInfoType(String infoTypeCode, String interfaceVer){
		if("1".equals(infoTypeCode)){
			return JICHU_INFO;
		}else if("2".equals(infoTypeCode)){
			return SHENBAO_INFO;
		}else if("3".equals(infoTypeCode)){
			if(StringUtils.isEmpty(interfaceVer)){
				return HEXIAO_INFO;
			}else{
				return GUANLI_INFO;
			}
		}else if("4".equals(infoTypeCode)){
			return "报关单信息";
		}else if("5".equals(infoTypeCode)){
			return "单位基本情况表";
		}else if("6".equals(infoTypeCode)){
			return "开户信息";
		}else if("7".equals(infoTypeCode)){
			return "出口收汇核销单号码";
		}else if("9".equals(infoTypeCode)){
			return "外汇局反馈";
		}else if("10".equals(infoTypeCode)){
			return "外汇局反馈核销单号码";
		}else{
			return UNKNOWN_INFO;
		}
	}

	public static String getInfoTypeModify(String infoTypeCode){
		if("1".equals(infoTypeCode) || "5".equals(infoTypeCode)){
			return JICHU_INFO_MODIFY;
		}else if("2".equals(infoTypeCode)){
			return SHENBAO_INFO_MODIFY;
		}else if("3".equals(infoTypeCode)){
			return HEXIAO_INFO_MODIFY;
		}else{
			return UNKNOWN_INFO_MODIFY;
		}
	}

	/**
	 * 如果信息类型为"基础信息"/"单位基本信息"，则返回TRUE
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isJCDW(String infoTypeCode){
		return "1".equals(infoTypeCode) || "5".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为"申报信息"/"核销信息"，则返回TRUE
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isSBHX(String infoTypeCode){
		return "2".equals(infoTypeCode) || "3".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为普通类型（非内嵌表），即"基础信息"/"单位基本信息"/"申报信息"/"核销信息"，则返回TRUE
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isJCDWSBHX(String infoTypeCode){
		return "1".equals(infoTypeCode) || "5".equals(infoTypeCode)
				|| "2".equals(infoTypeCode) || "3".equals(infoTypeCode);
	}
}
