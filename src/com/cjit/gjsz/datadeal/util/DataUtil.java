/**
 * 
 */
package com.cjit.gjsz.datadeal.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cjit.gjsz.datadeal.model.SelectTag;

/**
 * @author yulubin
 */
public class DataUtil {

	public static final String BUSIDATATYPE_1 = "1";
	public static final String BUSIDATATYPE_2 = "2";
	public static final String BUSIDATATYPE_1_DESC = "自身业务";
	public static final String BUSIDATATYPE_2_DESC = "代客业务";
	public static final String JICHU_INFO = "基础信息";
	public static final String SHENBAO_INFO = "申报信息";
	public static final String HEXIAO_INFO = "核销信息";
	public static final String GUANLI_INFO = "管理信息";
	public static final String DCL_INFO = "待处理";
	public static final String UNKNOWN_INFO = "";
	public static final String JICHU_INFO_MODIFY = "修改";
	public static final String SHENBAO_INFO_MODIFY = "申报";
	public static final String HEXIAO_INFO_MODIFY = "核销";
	public static final String UNKNOWN_INFO_MODIFY = "";
	// 数据状态信息
	public static final String DELETE_STATUS_CH = "逻辑删除";
	public static final String WJY_STATUS_CH = "未校验";
	public static final String JYWTG_STATUS_CH = "校验未通过";
	public static final String JYYTG_STATUS_CH = "校验已通过";
	public static final String YTJDSH_STATUS_CH = "已提交待审核";
	public static final String SHWTG_STATUS_CH = "审核未通过";
	public static final String SHYTG_STATUS_CH = "审核通过";
	public static final String YSC_STATUS_CH = "已生成";
	public static final String YBS_STATUS_CH = "已报送";
	public static final String LOCKED_STATUS_CH = "已锁定";
	public static final int DELETE_STATUS_NUM = 0;// 逻辑删除
	public static final int WJY_STATUS_NUM = 1;// 未校验
	public static final int JYWTG_STATUS_NUM = 2;// 校验未通过
	public static final int JYYTG_STATUS_NUM = 3;// 校验已通过
	public static final int YTJDSH_STATUS_NUM = 4;// 已提交待审核
	public static final int SHWTG_STATUS_NUM = 5;// 审核未通过
	public static final int SHYTG_STATUS_NUM = 6;// 审核通过
	public static final int YSC_STATUS_NUM = 7;// 已生成
	public static final int YBS_STATUS_NUM = 8;// 已报送
	public static final int LOCKED_STATUS_NUM = 9;// 已锁定
	public static final int DSC_STATUS_NUM = 99; // 待生成报文
	public static final String CFA_SELF_TABLE_A = "T_CFA_A_EXDEBT";// 外债信息
	public static final String CFA_SELF_TABLE_B = "T_CFA_B_EXGUARAN";// 对外担保信息
	public static final String CFA_SELF_TABLE_C = "T_CFA_C_DOFOEXLO";// 国内外汇贷款（含外债转贷款）信息
	public static final String CFA_SELF_TABLE_D = "T_CFA_D_LOUNEXGU";// 境外担保项下境内贷款
	public static final String CFA_SELF_TABLE_E = "T_CFA_E_EXPLRMBLO";// 外汇质押人民币贷款
	public static final String CFA_SELF_TABLE_F = "T_CFA_F_STRDE";// 商业银行人民币结构性存款业务
	public static final String TABLE_UNIQUE_SPLIT_SYMBOL = "#";// 报表唯一标示分解符号
	public static final String PACKTYPE_FEEDBACK = "REC"; // packType标识
	public static final String PACKTYPE_ERRORFILES = "ERR"; // packType标识
	public static final String PACKTYPE_HISTORYSEND = "HIS"; // packType标识
	public static final String TASK_AUTOCREATEANDRECEIVYE = "TCREATE"; // taskType标识
	public static final String TASK_AUTOCHECK = "TCHECK"; // taskType标识
	public static final int BATCH_SIZE_MAX = 300; // 批量执行，最大执行条数

	public static String getDataStatus(String s) {
		int n = -1;
		try {
			if (s != null && !"".equals(s)) {
				n = Integer.valueOf(s).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (WJY_STATUS_NUM == n) {// 1
			return "<font color='blue'>" + WJY_STATUS_CH + "</font>";
		} else if (JYWTG_STATUS_NUM == n) {// 2
			return "<font color='red'>" + JYWTG_STATUS_CH + "</font>";
		} else if (JYYTG_STATUS_NUM == n) {// 3
			return "<font color='green'>" + JYYTG_STATUS_CH + "</font>";
		} else if (YTJDSH_STATUS_NUM == n) {// 4
			return "<font color='green'>" + YTJDSH_STATUS_CH + "</font>";
		} else if (SHWTG_STATUS_NUM == n) {// 5
			return "<font color='orange'>" + SHWTG_STATUS_CH + "</font>";
		} else if (SHYTG_STATUS_NUM == n) {// 6
			return "<font color='green'>" + SHYTG_STATUS_CH + "</font>";
		} else if (YBS_STATUS_NUM == n) {// 7
			return "<font color='green'>" + YBS_STATUS_CH + "</font>";
		} else if (YSC_STATUS_NUM == n) {// 8
			return "<font color='green'>" + YSC_STATUS_CH + "</font>";
		} else if (LOCKED_STATUS_NUM == n) {// 9
			return "<font color='black'>" + LOCKED_STATUS_CH + "</font>";
		} else if (DELETE_STATUS_NUM == n) {// 0
			return "<font color='black'>" + DELETE_STATUS_CH + "</font>";
		} else {
			return "";
		}
	}

	public static String getDataStatus(SelectTag selectTag) {
		int n = -1;
		try {
			if (selectTag.getValue() != null
					&& !"".equals(selectTag.getValue())) {
				n = Integer.valueOf(selectTag.getValue()).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (WJY_STATUS_NUM == n) {// 1
			return "<font color='blue'>" + selectTag.getText() + "</font>";
		} else if (JYWTG_STATUS_NUM == n) {// 2
			return "<font color='red'>" + selectTag.getText() + "</font>";
		} else if (JYYTG_STATUS_NUM == n) {// 3
			return "<font color='green'>" + selectTag.getText() + "</font>";
		} else if (YTJDSH_STATUS_NUM == n) {// 4
			return "<font color='green'>" + selectTag.getText() + "</font>";
		} else if (SHWTG_STATUS_NUM == n) {// 5
			return "<font color='orange'>" + selectTag.getText() + "</font>";
		} else if (SHYTG_STATUS_NUM == n) {// 6
			return "<font color='green'>" + selectTag.getText() + "</font>";
		} else if (YBS_STATUS_NUM == n) {// 7
			return "<font color='green'>" + selectTag.getText() + "</font>";
		} else if (YSC_STATUS_NUM == n) {// 8
			return "<font color='green'>" + selectTag.getText() + "</font>";
		} else if (LOCKED_STATUS_NUM == n) {// 9
			return "<font color='black'>" + selectTag.getText() + "</font>";
		} else if (DELETE_STATUS_NUM == n) {// 0
			return "<font color='black'>" + selectTag.getText() + "</font>";
		} else {
			return "";
		}
	}

	public static String getDataStatusChinese(String s) {
		int n = -1;
		try {
			if (s != null && !"".equals(s))
				n = Integer.valueOf(s).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (WJY_STATUS_NUM == n) {// 1
			return WJY_STATUS_CH;
		} else if (JYWTG_STATUS_NUM == n) {// 2
			return JYWTG_STATUS_CH;
		} else if (JYYTG_STATUS_NUM == n) {// 3
			return JYYTG_STATUS_CH;
		} else if (YTJDSH_STATUS_NUM == n) {// 4
			return YTJDSH_STATUS_CH;
		} else if (SHWTG_STATUS_NUM == n) {// 5
			return SHWTG_STATUS_CH;
		} else if (SHYTG_STATUS_NUM == n) {// 6
			return SHYTG_STATUS_CH;
		} else if (YBS_STATUS_NUM == n) {// 7
			return YBS_STATUS_CH;
		} else if (YSC_STATUS_NUM == n) {// 8
			return YSC_STATUS_CH;
		} else if (LOCKED_STATUS_NUM == n) {// 9
			return LOCKED_STATUS_CH;
		} else if (DELETE_STATUS_NUM == n) {// 0
			return DELETE_STATUS_CH;
		} else {
			return "";
		}
	}

	public static String getInfoType(String infoTypeCode, String interfaceVer) {
		if ("1".equals(infoTypeCode)) {
			return JICHU_INFO;
		} else if ("2".equals(infoTypeCode)) {
			return SHENBAO_INFO;
		} else if ("3".equals(infoTypeCode)) {
			if (StringUtils.isEmpty(interfaceVer)) {
				return HEXIAO_INFO;
			} else {
				return GUANLI_INFO;
			}
		} else if ("4".equals(infoTypeCode)) {
			return "报关单信息";
		} else if ("5".equals(infoTypeCode)) {
			return "单位基本信息";
		} else if ("6".equals(infoTypeCode)) {
			return "开户信息";
		} else if ("7".equals(infoTypeCode)) {
			return "出口收汇核销单号码";
		} else if ("9".equals(infoTypeCode)) {
			return "外汇局反馈";
		} else {
			return UNKNOWN_INFO;
		}
	}

	public static String getInfoTypeModify(String infoTypeCode) {
		if ("1".equals(infoTypeCode) || "5".equals(infoTypeCode)) {
			return JICHU_INFO_MODIFY;
		} else if ("2".equals(infoTypeCode)) {
			return SHENBAO_INFO_MODIFY;
		} else if ("3".equals(infoTypeCode)) {
			return HEXIAO_INFO_MODIFY;
		} else {
			return UNKNOWN_INFO_MODIFY;
		}
	}

	/**
	 * 如果信息类型为"基础信息"/"申报信息"/"核销信息"，则返回TRUE
	 * 
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isJcSbHx(String infoTypeCode) {
		return "1".equals(infoTypeCode) || "2".equals(infoTypeCode)
				|| "3".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为"基础信息"/"单位基本信息"，则返回TRUE;为CFA增加对的判断
	 * 
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isJCDW(String infoTypeCode) {
		return "1".equals(infoTypeCode) || "5".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为"申报信息"/"核销信息"，则返回TRUE
	 * 
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isSBHX(String infoTypeCode) {
		return "2".equals(infoTypeCode) || "3".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为普通类型（非内嵌表），即"基础信息"/"单位基本信息"/"申报信息"/"核销信息"，则返回TRUE
	 * 
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isJCDWSBHX(String infoTypeCode) {
		return "1".equals(infoTypeCode);
	}

	/**
	 * 如果信息类型为除了投资国别表以外的内嵌表，则返回TRUE
	 * 
	 * @param infoTypeCode
	 * @return
	 */
	public static boolean isInner(String infoTypeCode) {
		return "".equals(infoTypeCode);
	}

	public static String formatStringQuot(String s, String type) {
		if (s != null) {
			String fs = s.replaceAll("'", "&#39;");
			fs = fs.replaceAll("\"", "&#34;");
			if (type != null && type.startsWith("n")) {
				if (fs.indexOf(".") > -1) {
					if (Pattern.compile("(\\-)?(\\.)?[0-9]+").matcher(fs)
							.matches()) {
						fs = String.valueOf(new BigDecimal(fs));
					}
				}
			}
			return fs;
		} else {
			return null;
		}
	}

	public static String formatStringQuot(String s) {
		if (s != null) {
			String fs = s.replaceAll("'", "&#39;");
			fs = fs.replaceAll("\"", "&#34;");
			return fs;
		} else {
			return null;
		}
	}

	public static String getInfoTypeCodeByTableId(String tableId) {
		if ("t_base_income".equals(tableId) || "t_base_remit".equals(tableId)
				|| "t_base_payment".equals(tableId)
				|| "t_base_export".equals(tableId)
				|| "t_base_dom_remit".equals(tableId)
				|| "t_base_dom_pay".equals(tableId)
				|| "t_base_settlement".equals(tableId)
				|| "t_base_purchase".equals(tableId)) {
			return "1";
		} else if ("t_decl_income".equals(tableId)
				|| "t_decl_remit".equals(tableId)
				|| "t_decl_payment".equals(tableId)) {
			return "2";
		} else if ("t_fini_export".equals(tableId)
				|| "t_fini_remit".equals(tableId)
				|| "t_fini_payment".equals(tableId)
				|| "t_fini_dom_remit".equals(tableId)
				|| "t_fini_dom_pay".equals(tableId)
				|| "t_fini_dom_export".equals(tableId)
				|| "t_fini_settlement".equals(tableId)
				|| "t_fini_purchase".equals(tableId)) {
			return "3";
		} else if ("t_customs_decl".equals(tableId)) {
			return "4";
		} else if ("t_company_info".equals(tableId)) {
			return "5";
		} else if ("t_company_openinfo".equals(tableId)) {
			return "6";
		} else if ("t_export_info".equals(tableId)) {
			return "7";
		} else if ("t_invcountrycode_info".equals(tableId)) {
			return "8";
		} else {
			return "1";
		}
	}

	public static boolean isColumnFromJt(String columnId, String tableId) {
		if (DataUtil.isJCDW(getInfoTypeCodeByTableId(tableId))) {
			return false;
		} else if (DataUtil.isSBHX(getInfoTypeCodeByTableId(tableId))) {
			if ("BUSCODE".equalsIgnoreCase(columnId)
					|| "CUSTNM".equalsIgnoreCase(columnId)
					|| "CUSTCOD".equalsIgnoreCase(columnId)
					|| "METHOD".equalsIgnoreCase(columnId)
					|| "TXCCY".equalsIgnoreCase(columnId)
					|| "TXAMT".equalsIgnoreCase(columnId)
					|| "OPPUSER".equalsIgnoreCase(columnId)
					|| "TXAMT".equalsIgnoreCase(columnId)
					|| "TRADEDATE".equalsIgnoreCase(columnId)
					|| "IDCODE".equalsIgnoreCase(columnId)
					|| "CUSTYPE".equalsIgnoreCase(columnId)
					|| "LCYAMT".equalsIgnoreCase(columnId)
					|| "EXRATE".equalsIgnoreCase(columnId)
					|| "ISSDATE".equalsIgnoreCase(columnId)
					|| "TENOR".equalsIgnoreCase(columnId)
					|| "LCBGNO".equalsIgnoreCase(columnId)
					|| "OUTCHARGEAMT".equalsIgnoreCase(columnId)
					|| "OUTCHARGECCY".equalsIgnoreCase(columnId)
					|| "ACTUAMT".equalsIgnoreCase(columnId)
					|| "ACTUCCY".equalsIgnoreCase(columnId)
					|| "OTHACC".equalsIgnoreCase(columnId)
					|| "OTHAMT".equalsIgnoreCase(columnId)
					|| "FCYACC".equalsIgnoreCase(columnId)
					|| "FCYAMT".equalsIgnoreCase(columnId)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static String getOrderColumn(String columnId, String tableId) {
		if (isColumnFromJt(columnId, tableId)) {
			return "jt." + columnId;
		} else {
			return "t." + columnId;
		}
	}

	public static void main(String[] args) {
		System.out.println(formatStringQuot("\"waeggew'awg''", ""));
	}

	public static String getTableIdByFileType(String fileType) {
		if (fileType == null) {
			return null;
		}
		/*
		 * if(fileType.startsWith("A")){ return "T_CFA_A_EXDEBT"; }else
		 * if(fileType.startsWith("B")){ return "T_CFA_B_EXGUARAN"; }else
		 * if(fileType.startsWith("C")){ return "T_CFA_C_DOFOEXLO"; }else
		 * if(fileType.startsWith("D")){ return "T_CFA_D_LOUNEXGU"; }else
		 * if(fileType.startsWith("E")){ return "T_CFA_E_EXPLRMBLO"; }else
		 * if(fileType.startsWith("F")){ return "T_CFA_F_STRDE"; }
		 */
		if ("ZA".equals(fileType)) {
			return "T_FAL_Z01";
		} else if ("ZB".equals(fileType)) {
			return "T_FAL_Z02";
		} else if ("ZC".equals(fileType)) {
			return "T_FAL_Z03";
		} else if ("AA".equals(fileType)) {
			return "T_FAL_A01_1";
		} else if ("AB".equals(fileType)) {
			return "T_FAL_A01_2";
		} else if ("AC".equals(fileType)) {
			return "T_FAL_A02_1";
		} else if ("AD".equals(fileType)) {
			return "T_FAL_A02_2";
		} else if ("AE".equals(fileType)) {
			return "T_FAL_A02_3";
		} else if ("BA".equals(fileType)) {
			return "T_FAL_B01";
		} else if ("BB".equals(fileType)) {
			return "T_FAL_B02";
		} else if ("BC".equals(fileType)) {
			return "T_FAL_B03";
		} else if ("BD".equals(fileType)) {
			return "T_FAL_B04";
		} else if ("BE".equals(fileType)) {
			return "T_FAL_B05";
		} else if ("BF".equals(fileType)) {
			return "T_FAL_B06";
		} else if ("CA".equals(fileType)) {
			return "T_FAL_C01";
		} else if ("DA".equals(fileType)) {
			return "T_FAL_D01";
		} else if ("DB".equals(fileType)) {
			return "T_FAL_D02";
		} else if ("DC".equals(fileType)) {
			return "T_FAL_D03";
		} else if ("DD".equals(fileType)) {
			return "T_FAL_D04";
		} else if ("DE".equals(fileType)) {
			return "T_FAL_D05_1";
		} else if ("DF".equals(fileType)) {
			return "T_FAL_D05_2";
		} else if ("DG".equals(fileType)) {
			return "T_FAL_D06_1";
		} else if ("DH".equals(fileType)) {
			return "T_FAL_D07";
		} else if ("DJ".equals(fileType)) {
			return "T_FAL_D09";
		} else if ("EA".equals(fileType)) {
			return "T_FAL_E01";
		} else if ("FA".equals(fileType)) {
			return "T_FAL_F01";
		} else if ("GA".equals(fileType)) {
			return "T_FAL_G01";
		} else if ("GB".equals(fileType)) {
			return "T_FAL_G02";
		} else if ("HA".equals(fileType)) {
			return "T_FAL_H01";
		} else if ("HB".equals(fileType)) {
			return "T_FAL_H02";
		} else if ("IA".equals(fileType)) {
			return "T_FAL_I01";
		} else if ("IB".equals(fileType)) {
			return "T_FAL_I02";
		} else if ("IC".equals(fileType)) {
			return "T_FAL_I03";
		} else if ("XA".equals(fileType)) {
			return "T_FAL_X01";
		} else {
			return "";
		}
	}

	public static String getOperationByTableId(String tableId) {
		if ("t_base_income".equals(tableId) || "t_base_remit".equals(tableId)
				|| "t_base_payment".equals(tableId)
				|| "t_base_export".equals(tableId)
				|| "t_base_dom_remit".equals(tableId)
				|| "t_base_dom_pay".equals(tableId)
				|| "t_decl_income".equals(tableId)
				|| "t_decl_remit".equals(tableId)
				|| "t_decl_payment".equals(tableId)
				|| "t_fini_export".equals(tableId)
				|| "t_fini_remit".equals(tableId)
				|| "t_fini_payment".equals(tableId)
				|| "t_fini_dom_remit".equals(tableId)
				|| "t_fini_dom_pay".equals(tableId)
				|| "t_fini_dom_export".equals(tableId)) {
			return "BOP";
		} else if ("t_base_settlement".equals(tableId)
				|| "t_base_purchase".equals(tableId)
				|| "t_fini_settlement".equals(tableId)
				|| "t_fini_purchase".equals(tableId)) {
			return "JSH";
		} else if ("t_company_info".equals(tableId)) {
			return "COMPANY";
		} else {
			return "";
		}
	}

	public static boolean isDaiKeTableId(String tableId) {
		if (tableId == null) {
			return false;
		}
		if (tableId.equalsIgnoreCase("T_CFA_A_EXDEBT")) {
			return false;
		} else if (tableId.equalsIgnoreCase("T_CFA_B_EXGUARAN")) {
			return false;
		} else if (tableId.equalsIgnoreCase("T_CFA_C_DOFOEXLO")) {
			return false;
		} else if (tableId.equalsIgnoreCase("T_CFA_D_LOUNEXGU")) {
			return false;
		} else if (tableId.equalsIgnoreCase("T_CFA_E_EXPLRMBLO")) {
			return false;
		} else if (tableId.equalsIgnoreCase("T_CFA_F_STRDE")) {
			return false;
		} else if (tableId.indexOf("_SUB_") > 0) {
			return false;
		} else {
			return true;
		}
	}

	// //////////////////////////////////////////////////////
	public static List getInfoTypeList(String busiDataType) {
		List list = new ArrayList();
		if (BUSIDATATYPE_1.equals(busiDataType)) {
			list.add("外债");
			list.add("对外担保");
			list.add("国内外汇贷款");
			list.add("境外担保项下境内贷款");
			list.add("外汇质押人民币贷款");
			list.add("商业银行人民币结构性存款");
		} else if (BUSIDATATYPE_2.equals(busiDataType)) {
			list.add("合格境外机构投资者境内证券投资");
			list.add("");
			list.add("");
			list.add("");
		}
		return list;
	}

	public static String getTableNameByFileType(String fileType) {
		if ("AA".equals(fileType)) {
			return "A01-1 对外直接投资（资产负债表及市值）";
		} else if ("AB".equals(fileType)) {
			return "A01-2 对外直接投资（流量）";
		} else if ("AC".equals(fileType)) {
			return "A02-1 外国来华直接投资（资产负债表）";
		} else if ("AD".equals(fileType)) {
			return "A02-2 境外直接投资者名录";
		} else if ("AE".equals(fileType)) {
			return "A02-3 外国来华直接投资（流量）";
		} else  if ("BA".equals(fileType)) {
			return "B01 投资境外股本证券和投资基金份额（资产）";
		} else if ("BB".equals(fileType)) {
			return "B02 投资境外债务证券（资产）";
		} else if ("BC".equals(fileType)) {
			return "B03 投资非居民境内发行股本证券和债务证券";
		} else if ("BD".equals(fileType)) {
			return "B04 吸收境外股权和基金份额投资（负债）";
		} else if ("BE".equals(fileType)) {
			return "B05 境外发行债务证券（负债）";
		} else if ("BF".equals(fileType)) {
			return "B06 非居民投资境内发行股本证券和债务证券";
		} else if ("CA".equals(fileType)) {
			return "C01 金融衍生产品及雇员认股权";
		} else if ("DA".equals(fileType)) {
			return "D01 货币与存款（含存放银行同业和联行）（资产）";
		} else if ("DB".equals(fileType)) {
			return "D02 贷款（含拆放银行同业及联行）（资产）";
		} else if ("DC".equals(fileType)) {
			return "D03 持有境外非公司制机构10%以下表决权和国际组织股权（资产）";
		} else if ("DD".equals(fileType)) {
			return "D04 应收款（不含应收利息）（资产）";
		} else if ("DE".equals(fileType)) {
			return "D05-1 存款（含银行同业和联行存放）（负债）-境外机构存款";
		} else if ("DF".equals(fileType)) {
			return "D05-2 存款（含银行同业和联行存放）（负债）-非居民个人存款";
		} else if ("DG".equals(fileType)) {
			return "D06-1 贷款（含银行同业和联行拆借）（负债）月末存量";
		} else if ("DH".equals(fileType)) {
			return "D07 非居民持有本机构（非公司制）10%以下表决权（负债）";
		} else if ("DJ".equals(fileType)) {
			return "D09 对非居民的贷款和应收款减值准备余额";
		} else if ("EA".equals(fileType)) {
			return "E01 货物、服务、薪资及债务减免等其他各类往来";
		} else if ("FA".equals(fileType)) {
			return "F01 买断出口票据、单证业务";
		} else if ("GA".equals(fileType)) {
			return "G01 境内银行卡境外消费提现";
		} else if ("GB".equals(fileType)) {
			return "G02 境外银行卡境内消费提现";
		} else if ("HA".equals(fileType)) {
			return "H01 为非居民托管业务统计（QFII、RQFII 相关）";
		} else if ("HB".equals(fileType)) {
			return "H02 为居民托管业务统计（QDII 相关）";
		} else if ("IA".equals(fileType)) {
			return "I01 为非居民提供直接保险服务";
		} else if ("IB".equals(fileType)) {
			return "I02 为非居民提供再保险服务（分入保险）";
		} else if ("IC".equals(fileType)) {
			return "I03 从非居民保险机构获得再保险服务（分出保险）";
		} else if ("XA".equals(fileType)) {
			return "X01 银行进出口贸易融资余额";
		} else if ("ZA".equals(fileType)) {
			return "Z01 填报单位基本信息";
		} else if ("ZB".equals(fileType)) {
			return "Z02 业务概览及联系方式表";
		} else if ("ZC".equals(fileType)) {
			return "Z03 填报单位投资关系（组织架构）";
		} else {
			return "";
		}
		// if ("AA".equals(fileType)) {
		// return "双边贷款-签约信息";
		// } else if ("AB".equals(fileType)) {
		// return "买方信贷-签约信息";
		// } else if ("AC".equals(fileType)) {
		// return "境外同业拆借-签约信息";
		// } else if ("AD".equals(fileType)) {
		// return "海外代付-签约信息";
		// } else if ("AE".equals(fileType)) {
		// return "卖出回购-签约信息";
		// } else if ("AF".equals(fileType)) {
		// return "远期信用证-签约信息";
		// } else if ("AG".equals(fileType)) {
		// return "银团贷款-签约信息";
		// } else if ("AH".equals(fileType)) {
		// return "贵金属拆借-签约信息";
		// } else if ("AI".equals(fileType)) {
		// return "其他贷款-签约信息";
		// } else if ("AJ".equals(fileType)) {
		// return "货币市场工具-签约信息";
		// } else if ("AK".equals(fileType)) {
		// return "债券和票据-签约信息";
		// } else if ("AL".equals(fileType)) {
		// return "境外同业存放-签约信息";
		// } else if ("AM".equals(fileType)) {
		// return "境外联行及附属机构往来-签约信息";
		// } else if ("AN".equals(fileType)) {
		// return "非居民机构存款-签约信息";
		// } else if ("AP".equals(fileType)) {
		// return "非居民个人存款-签约信息";
		// } else if ("AQ".equals(fileType)) {
		// return "其他外债-签约信息";
		// } else if ("AR".equals(fileType)) {
		// return "变动信息";
		// } else if ("AS".equals(fileType)) {
		// return "余额信息";
		// } else if ("BA".equals(fileType)) {
		// return "签约信息";
		// } else if ("BB".equals(fileType)) {
		// return "责任余额信息";
		// } else if ("BC".equals(fileType)) {
		// return "履约信息";
		// } else if ("CA".equals(fileType)) {
		// return "签约信息";
		// } else if ("CB".equals(fileType)) {
		// return "变动信息";
		// } else if ("DA".equals(fileType)) {
		// return "签约信息";
		// } else if ("DB".equals(fileType)) {
		// return "变动及履约信息";
		// } else if ("EA".equals(fileType)) {
		// return "签约信息";
		// } else if ("EB".equals(fileType)) {
		// return "变动信息";
		// } else if ("FA".equals(fileType)) {
		// return "签约信息";
		// } else if ("FB".equals(fileType)) {
		// return "终止信息";
		// } else if ("FC".equals(fileType)) {
		// return "利息给付信息";
		//		} else if ("FD".equals(fileType)) {
		//			return "资金流出入和结购汇信息";
		//		} else {
		//			return "";
		//		}
	}

	public static String getRptNoColumnIdByFileType(String fileType) {
		if (fileType == null) {
			return "";
		}
		// FAL
		if (1 == 1) {
			return "SNOCODE";
		}
		// CFA
		if (fileType.startsWith("A")) {
			// 外债编号
			return "EXDEBTCODE";
		} else if (fileType.startsWith("B")) {
			// 对外担保编号
			return "EXGUARANCODE";
		} else if (fileType.startsWith("C")) {
			// 国内外汇贷款编号
			return "DOFOEXLOCODE";
		} else if (fileType.startsWith("D")) {
			// 外保内贷编号
			return "LOUNEXGUCODE";
		} else if (fileType.startsWith("E")) {
			// 外汇质押人民币贷款编号
			return "EXPLRMBLONO";
		} else if (fileType.startsWith("F")) {
			if (!"FD".equals(fileType)) {
				// 人民币结构性存款编号
				return "STRDECODE";
			} else {
				// 金融机构标识码
				return "BRANCHCODE";
			}
		} else {
			return "";
		}
	}

	public static String getByeRptNoColumnIdByFileType(String fileType) {
		// FAL - 无副申报号
		if (1 == 1) {
			return null;
		}
		// FAL - 无副申报号
		if (fileType == null) {
			return "";
		}
		if ("AR".equals(fileType) || "AS".equals(fileType)
				|| "CB".equals(fileType) || "DB".equals(fileType)
				|| "EB".equals(fileType)) {
			// 变动编号
			return "CHANGENO";
		} else if ("BB".equals(fileType)) {
			// 担保责任余额变动日期
			return "WABACHANDATE";
		} else if ("BC".equals(fileType)) {
			// 履约编号
			return "COMPLIANCENO";
		} else if ("FB".equals(fileType)) {
			// 终止支付编号
			return "TERPAYCODE";
		} else if ("FC".equals(fileType)) {
			// 付息编号
			return "INPAYCODE";
		} else if ("FD".equals(fileType)) {
			// 报告期
			return "BUOCMONTH";
		} else {
			return "";
		}
	}

	public static String getInfoTypeName(String infoType, String tableId,
			String fileType) {
		if ("A".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_A"))
				|| (fileType != null && fileType.startsWith("A"))) {
			return "A.直接投资（10%及以上股权）";
		} else if ("B".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_B"))
				|| (fileType != null && fileType.startsWith("B"))) {
			return "B.证券投资（10%以下股权和债务证券）";
		} else if ("C".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_C"))
				|| (fileType != null && fileType.startsWith("C"))) {
			return "C.金融衍生产品及雇员认股权";
		} else if ("D".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_D"))
				|| (fileType != null && fileType.startsWith("D"))) {
			return "D.存贷款、应收应付款及非公司制机构股权等其他投资";
		} else if ("E".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_E"))
				|| (fileType != null && fileType.startsWith("E"))) {
			return "E.货物、服务、薪资及债务减免等其他各类往来";
		} else if ("F".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_F"))
				|| (fileType != null && fileType.startsWith("F"))) {
			return "F.与进出口票据、单证有关业务";
		} else if ("G".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_G"))
				|| (fileType != null && fileType.startsWith("G"))) {
			return "G.涉外银行卡相关统计";
		} else if ("H".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_H"))
				|| (fileType != null && fileType.startsWith("H"))) {
			return "H.涉外托管业务";
		} else if ("I".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_I"))
				|| (fileType != null && fileType.startsWith("I"))) {
			return "I.涉外保险业务";
		} else if ("X".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_X"))
				|| (fileType != null && fileType.startsWith("X"))) {
			return "X.补充报表：银行进出口贸易融资余额";
		} else if ("Z".equals(infoType)
				|| (tableId != null && tableId.startsWith("T_FAL_Z"))
				|| (fileType != null && fileType.startsWith("Z"))) {
			return "Z.填报单位基本信息";
		} else {
			return "";
		}
		// if ("A".equals(infoType) || "T_CFA_A_EXDEBT".equals(tableId)
		// || (fileType != null && fileType.startsWith("A"))) {
		// return "外债";
		// } else if ("B".equals(infoType) || "T_CFA_B_EXGUARAN".equals(tableId)
		// || (fileType != null && fileType.startsWith("B"))) {
		// return "对外担保";
		// } else if ("C".equals(infoType) || "T_CFA_C_DOFOEXLO".equals(tableId)
		// || (fileType != null && fileType.startsWith("C"))) {
		// return "国内外汇贷款";
		// } else if ("D".equals(infoType) || "T_CFA_D_LOUNEXGU".equals(tableId)
		// || (fileType != null && fileType.startsWith("D"))) {
		// return "境外担保项下境内贷款";
		// } else if ("E".equals(infoType) ||
		// "T_CFA_E_EXPLRMBLO".equals(tableId)
		// || (fileType != null && fileType.startsWith("E"))) {
		// return "外汇质押人民币贷款";
		// } else if ("F".equals(infoType) || "T_CFA_F_STRDE".equals(tableId)
		// || (fileType != null && fileType.startsWith("F"))) {
		// return "商业银行人民币结构性存款";
		//		} else {
		//			return "";
		//		}
	}

	/**
	 * 根据外汇账户反馈数据文件中业务数据主键，获取对应金融机构标识码
	 * 
	 * @param bussNo
	 * @return String
	 */
	public static String getKeyRptNoForBussNo(String bussNo) {
		String keyRptNo = "";
		// 资本项目数据反馈xml中"业务数据主键" 在接口反馈文件中构成业务数据主键的字段他们之间由逗号（英文）分割
		if (bussNo != null && bussNo.indexOf(",") > 0) {
			String[] bussInfos = bussNo.split(",");
			if (bussInfos != null && bussInfos.length == 2) {
				keyRptNo = bussInfos[0];
			}
			return keyRptNo;
		} else {
			return bussNo;
		}
	}

	/**
	 * 根据外汇账户反馈数据文件中业务数据主键，获取对应发生日期
	 * 
	 * @param bussNo
	 * @return String
	 */
	public static String getByeRptNoForBussNo(String bussNo) {
		String byeRptNo = "";
		// 资本项目数据反馈xml中"业务数据主键" 在接口反馈文件中构成业务数据主键的字段他们之间由逗号（英文）分割
		if (bussNo != null && bussNo.indexOf(",") > 0) {
			String[] bussInfos = bussNo.split(",");
			if (bussInfos != null && bussInfos.length == 2) {
				byeRptNo = bussInfos[1];
			}
			return byeRptNo;
		} else {
			return byeRptNo;
		}
	}

	/**
	 * 根据接口文件类型代码 获取 银行银行自身外债业务类型代码
	 * 
	 * @param fileType
	 * @return String
	 */
	public static String getSelfBussTypeCodeByFileType(String fileType) {
		// 参照采集规范文档16.2 银行银行自身外债业务类型代码表
		if ("AA".equalsIgnoreCase(fileType)) {
			// 双边贷款
			return "99";
		} else if ("AB".equalsIgnoreCase(fileType)) {
			// 买方信贷
			return "98";
		} else if ("AC".equalsIgnoreCase(fileType)) {
			// 境外同业拆借
			return "97";
		} else if ("AD".equalsIgnoreCase(fileType)) {
			// 海外代付
			return "96";
		} else if ("AE".equalsIgnoreCase(fileType)) {
			// 卖出回购
			return "95";
		} else if ("AF".equalsIgnoreCase(fileType)) {
			// 远期信用证
			return "94";
		} else if ("AG".equalsIgnoreCase(fileType)) {
			// 银团贷款
			return "93";
		} else if ("AH".equalsIgnoreCase(fileType)) {
			// 贵金属拆借
			return "92";
		} else if ("AI".equalsIgnoreCase(fileType)) {
			// 其他贷款
			return "91";
		} else if ("AJ".equalsIgnoreCase(fileType)) {
			// 货币市场工具
			return "90";
		} else if ("AK".equalsIgnoreCase(fileType)) {
			// 债券和票据
			return "89";
		} else if ("AL".equalsIgnoreCase(fileType)) {
			// 境外同业存放
			return "88";
		} else if ("AM".equalsIgnoreCase(fileType)) {
			// 境外联行及附属机构往来
			return "87";
		} else if ("AN".equalsIgnoreCase(fileType)) {
			// 非居民机构存款
			return "86";
		} else if ("AP".equalsIgnoreCase(fileType)) {
			// 非居民个人存款
			return "85";
		} else if ("AQ".equalsIgnoreCase(fileType)) {
			// 其他外债
			return "84";
		} else if ("BA".equalsIgnoreCase(fileType)) {
			// 对外担保
			return "83";
		} else if ("CA".equalsIgnoreCase(fileType)) {
			// 国内外汇贷款（含外债转贷款）
			return "82";
		} else if ("DA".equalsIgnoreCase(fileType)) {
			// 境外担保项下境内贷款
			return "81";
		} else if ("EA".equalsIgnoreCase(fileType)) {
			// 外汇质押人民币贷款
			return "80";
		} else if ("FA".equalsIgnoreCase(fileType)) {
			// 商业银行人民币结构性存款
			return "79";
		} else {
			return "--";
		}
	}

	/**
	 * 根据银行银行自身外债业务类型代码 获取 接口文件类型代码
	 * 
	 * @param selfBussTypeCode
	 * @return String
	 */
	public static String getFileTypeBySelfBussTypeCode(String selfBussTypeCode) {
		// 参照采集规范文档16.2 银行银行自身外债业务类型代码表
		if ("99".equalsIgnoreCase(selfBussTypeCode)) {
			// 双边贷款
			return "AA";
		} else if ("98".equalsIgnoreCase(selfBussTypeCode)) {
			// 买方信贷
			return "AB";
		} else if ("97".equalsIgnoreCase(selfBussTypeCode)) {
			// 境外同业拆借
			return "AC";
		} else if ("96".equalsIgnoreCase(selfBussTypeCode)) {
			// 海外代付
			return "AD";
		} else if ("95".equalsIgnoreCase(selfBussTypeCode)) {
			// 卖出回购
			return "AE";
		} else if ("94".equalsIgnoreCase(selfBussTypeCode)) {
			// 远期信用证
			return "AF";
		} else if ("93".equalsIgnoreCase(selfBussTypeCode)) {
			// 银团贷款
			return "AG";
		} else if ("92".equalsIgnoreCase(selfBussTypeCode)) {
			// 贵金属拆借
			return "AH";
		} else if ("91".equalsIgnoreCase(selfBussTypeCode)) {
			// 其他贷款
			return "AI";
		} else if ("90".equalsIgnoreCase(selfBussTypeCode)) {
			// 货币市场工具
			return "AJ";
		} else if ("89".equalsIgnoreCase(selfBussTypeCode)) {
			// 债券和票据
			return "AK";
		} else if ("88".equalsIgnoreCase(selfBussTypeCode)) {
			// 境外同业存放
			return "AL";
		} else if ("87".equalsIgnoreCase(selfBussTypeCode)) {
			// 境外联行及附属机构往来
			return "AM";
		} else if ("86".equalsIgnoreCase(selfBussTypeCode)) {
			// 非居民机构存款
			return "AN";
		} else if ("85".equalsIgnoreCase(selfBussTypeCode)) {
			// 非居民个人存款
			return "AP";
		} else if ("84".equalsIgnoreCase(selfBussTypeCode)) {
			// 其他外债
			return "AQ";
		} else if ("83".equalsIgnoreCase(selfBussTypeCode)) {
			// 对外担保
			return "BA";
		} else if ("82".equalsIgnoreCase(selfBussTypeCode)) {
			// 国内外汇贷款（含外债转贷款）
			return "CA";
		} else if ("81".equalsIgnoreCase(selfBussTypeCode)) {
			// 境外担保项下境内贷款
			return "DA";
		} else if ("80".equalsIgnoreCase(selfBussTypeCode)) {
			// 外汇质押人民币贷款
			return "EA";
		} else if ("79".equalsIgnoreCase(selfBussTypeCode)) {
			// 商业银行人民币结构性存款
			return "FA";
		} else {
			return "--";
		}
	}

	public static String getDebtTypeByFileType(String fileType) {
		// 参照采集规范文档16.5 外债业务代码表
		if ("AA".equalsIgnoreCase(fileType)) {
			// 双边贷款
			return "1101";
		} else if ("AB".equalsIgnoreCase(fileType)) {
			// 买方信贷
			return "1103";
		} else if ("AC".equalsIgnoreCase(fileType)) {
			// 境外同业拆借
			return "1105";
		} else if ("AD".equalsIgnoreCase(fileType)) {
			// 海外代付
			return "1106";
		} else if ("AE".equalsIgnoreCase(fileType)) {
			// 卖出回购
			return "1107";
		} else if ("AF".equalsIgnoreCase(fileType)) {
			// 远期信用证
			return "1108";
		} else if ("AG".equalsIgnoreCase(fileType)) {
			// 银团贷款
			return "1110";
		} else if ("AH".equalsIgnoreCase(fileType)) {
			// 贵金属拆借
			return "1117";
		} else if ("AI".equalsIgnoreCase(fileType)) {
			// 其他贷款
			return "1199";
		} else if ("AJ".equalsIgnoreCase(fileType)) {
			// 货币市场工具
			return "1201";
		} else if ("AK".equalsIgnoreCase(fileType)) {
			// 债券和票据
			return "1202";
		} else if ("AL".equalsIgnoreCase(fileType)) {
			// 境外同业存放
			return "1301";
		} else if ("AM".equalsIgnoreCase(fileType)) {
			// 境外联行及附属机构往来
			return "1302";
		} else if ("AN".equalsIgnoreCase(fileType)) {
			// 非居民机构存款
			return "1303";
		} else if ("AP".equalsIgnoreCase(fileType)) {
			// 非居民个人存款
			return "1304";
		} else if ("AQ".equalsIgnoreCase(fileType)) {
			// 其他外债
			return "9900";
		} else {
			return "--";
		}
	}

	public static String getRptCodeByFileType(String fileType, int returnSize) {
		String rptCode = "";
		if ("ZA".equalsIgnoreCase(fileType)) {
			rptCode = "Z01";
		} else if ("ZB".equalsIgnoreCase(fileType)) {
			rptCode = "Z02";
		} else if ("ZC".equalsIgnoreCase(fileType)) {
			rptCode = "Z03";
		} else if ("AA".equalsIgnoreCase(fileType)) {
			rptCode = "A01-1";
		} else if ("AB".equalsIgnoreCase(fileType)) {
			rptCode = "A01-2";
		} else if ("AC".equalsIgnoreCase(fileType)) {
			rptCode = "A02-1";
		} else if ("AD".equalsIgnoreCase(fileType)) {
			rptCode = "A02-2";
		} else if ("AE".equalsIgnoreCase(fileType)) {
			rptCode = "A02-3";
		} else if ("BA".equalsIgnoreCase(fileType)) {
			rptCode = "B01";
		} else if ("BB".equalsIgnoreCase(fileType)) {
			rptCode = "B02";
		} else if ("BC".equalsIgnoreCase(fileType)) {
			rptCode = "B03";
		} else if ("BD".equalsIgnoreCase(fileType)) {
			rptCode = "B04";
		} else if ("BE".equalsIgnoreCase(fileType)) {
			rptCode = "B05";
		} else if ("BF".equalsIgnoreCase(fileType)) {
			rptCode = "B06";
		} else if ("CA".equalsIgnoreCase(fileType)) {
			rptCode = "C01";
		} else if ("DA".equalsIgnoreCase(fileType)) {
			rptCode = "D01";
		} else if ("DB".equalsIgnoreCase(fileType)) {
			rptCode = "D02";
		} else if ("DC".equalsIgnoreCase(fileType)) {
			rptCode = "D03";
		} else if ("DD".equalsIgnoreCase(fileType)) {
			rptCode = "D04";
		} else if ("DE".equalsIgnoreCase(fileType)) {
			rptCode = "D05-1";
		} else if ("DF".equalsIgnoreCase(fileType)) {
			rptCode = "D05-2";
		} else if ("DG".equalsIgnoreCase(fileType)) {
			rptCode = "D06-1";
		} else if ("DH".equalsIgnoreCase(fileType)) {
			rptCode = "D07";
		} else if ("DJ".equalsIgnoreCase(fileType)) {
			rptCode = "D09";
		} else if ("EA".equalsIgnoreCase(fileType)) {
			rptCode = "E01";
		} else if ("FA".equalsIgnoreCase(fileType)) {
			rptCode = "F01";
		} else if ("GA".equalsIgnoreCase(fileType)) {
			rptCode = "G01";
		} else if ("GB".equalsIgnoreCase(fileType)) {
			rptCode = "G02";
		} else if ("HA".equalsIgnoreCase(fileType)) {
			rptCode = "H01";
		} else if ("HB".equalsIgnoreCase(fileType)) {
			rptCode = "H02";
		} else if ("IA".equalsIgnoreCase(fileType)) {
			rptCode = "I01";
		} else if ("IB".equalsIgnoreCase(fileType)) {
			rptCode = "I02";
		} else if ("IC".equalsIgnoreCase(fileType)) {
			rptCode = "I03";
		} else if ("XA".equalsIgnoreCase(fileType)) {
			rptCode = "X01";
		}
		while (rptCode.length() < returnSize) {
			rptCode += "a";
		}
		return rptCode;
	}

	public static String replaceForXml(String data) {
		if (data == null) {
			return null;
		} else {
			data = data.replaceAll("&", "&amp;");
			data = data.replaceAll("<", "&lt;");
			data = data.replaceAll(">", "&gt;");
			data = data.replaceAll("\"", "&quot;");
			data = data.replaceAll("'", "&apos;");
			return data;
		}
	}
}
