package com.cjit.vms.trans.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.SelectTag;

public class DataUtil {

	// 是否
	public static final String YES = "Y";
	public static final String NO = "N";
	// 交易记录状态
	public static final String TRANS_STATUS_1 = "1";
	public static final String TRANS_STATUS_1_CH = "未开票";
	public static final String TRANS_STATUS_2 = "2";
	public static final String TRANS_STATUS_2_CH = "开票编辑锁定中";
	public static final String TRANS_STATUS_3 = "3";
	public static final String TRANS_STATUS_3_CH = "开票中";
	public static final String TRANS_STATUS_4 = "4";
	public static final String TRANS_STATUS_4_CH = "删除";
	public static final String TRANS_STATUS_5 = "5";
	public static final String TRANS_STATUS_5_CH = "被冲账";
	public static final String TRANS_STATUS_6 = "6";
	public static final String TRANS_STATUS_6_CH = "已冲账";
	public static final String TRANS_STATUS_7 = "7";
	public static final String TRANS_STATUS_7_CH = "冲抵待锁定";
	public static final String TRANS_STATUS_8 = "8";
	public static final String TRANS_STATUS_8_CH = "冲抵锁定中";
	public static final String TRANS_STATUS_9 = "9";
	public static final String TRANS_STATUS_9_CH = "已冲抵";
	public static final String TRANS_STATUS_99 = "99";
	public static final String TRANS_STATUS_99_CH = "开票完成";
	// 票据记录状态
	public static final String BILL_STATUS_1 = "1";
	public static final String BILL_STATUS_1_CH = "编辑待提交";
	public static final String BILL_STATUS_2 = "2";
	public static final String BILL_STATUS_2_CH = "提交待审核";
	public static final String BILL_STATUS_3 = "3";
	public static final String BILL_STATUS_3_CH = "审核通过";
	public static final String BILL_STATUS_4 = "4";
	public static final String BILL_STATUS_4_CH = "无需审核";
	public static final String BILL_STATUS_5 = "5";
	public static final String BILL_STATUS_5_CH = "已开具";
	public static final String BILL_STATUS_6 = "6";
	public static final String BILL_STATUS_6_CH = "已开具";
	public static final String BILL_STATUS_7 = "7";
	public static final String BILL_STATUS_7_CH = "开具失败";
	public static final String BILL_STATUS_8 = "8";
	public static final String BILL_STATUS_8_CH = "已打印";
	public static final String BILL_STATUS_9 = "9";
	public static final String BILL_STATUS_9_CH = "打印失败";
	public static final String BILL_STATUS_10 = "10";
	public static final String BILL_STATUS_10_CH = "已快递";
	public static final String BILL_STATUS_11 = "11";
	public static final String BILL_STATUS_11_CH = "已签收";
	public static final String BILL_STATUS_12 = "12";
	public static final String BILL_STATUS_12_CH = "已抄报";
	public static final String BILL_STATUS_13 = "13";
	public static final String BILL_STATUS_13_CH = "作废待审核";
	public static final String BILL_STATUS_14 = "14";
	public static final String BILL_STATUS_14_CH = "作废已审核";
	public static final String BILL_STATUS_15 = "15";
	public static final String BILL_STATUS_15_CH = "已作废";
	public static final String BILL_STATUS_16 = "16";
	public static final String BILL_STATUS_16_CH = "红冲待审核";
	public static final String BILL_STATUS_17 = "17";
	public static final String BILL_STATUS_17_CH = "红冲已审核";
	public static final String BILL_STATUS_18 = "18";
	public static final String BILL_STATUS_18_CH = "已红冲";
	public static final String BILL_STATUS_19 = "19";
	public static final String BILL_STATUS_19_CH = "已收回";
	public static final String BILL_STATUS_20 = "20";
	public static final String BILL_STATUS_20_CH = "红冲待审核";
	public static final String BILL_STATUS_21 = "21";
	public static final String BILL_STATUS_21_CH = "红冲审核通过";
	public static final String BILL_STATUS_22 = "22";
	public static final String BILL_STATUS_22_CH = "红冲待开票";
	public static final String BILL_STATUS_23= "23";
	public static final String BILL_STATUS_23_CH = "录入发票";
	public static final String BILL_STATUS_99 = "99";
	public static final String BILL_STATUS_99_CH = "生效完成";
	// 发票状态
	public static final String INPUT_INVOICE_STATUS_1 = "1";
	public static final String INPUT_INVOICE_1_CH = "已扫描未认证";
	public static final String INPUT_INVOICE_STATUS_2 = "2";
	public static final String INPUT_INVOICE_2_CH = "认证未收到回执";
	public static final String INPUT_INVOICE_STATUS_3 = "3";
	public static final String INPUT_INVOICE_3_CH = "首次认证通过";
	public static final String INPUT_INVOICE_STATUS_4 = "4";
	public static final String INPUT_INVOICE_4_CH = "首次认证未通过";
	public static final String INPUT_INVOICE_STATUS_5 = "5";
	public static final String INPUT_INVOICE_5_CH = "再次认证通过";
	public static final String INPUT_INVOICE_STATUS_6 = "6";
	public static final String INPUT_INVOICE_6_CH = "再次认证未通过";
	public static final String INPUT_INVOICE_STATUS_7 = "7";
	public static final String INPUT_INVOICE_7_CH = "税务局当场认证通过";
	public static final String INPUT_INVOICE_STATUS_8 = "8";
	public static final String INPUT_INVOICE_8_CH = "税务局当场认证未通过";
	public static final String INPUT_INVOICE_STATUS_9 = "9";
	public static final String INPUT_INVOICE_9_CH = "票退回";
	public static final String INPUT_INVOICE_STATUS_10 = "10";
	public static final String INPUT_INVOICE_10_CH = "已抵扣";
	public static final String INPUT_INVOICE_STATUS_11 = "11";
	public static final String INPUT_INVOICE_11_CH = "不可抵扣";
	public static final String INPUT_INVOICE_STATUS_12 = "12";
	public static final String INPUT_INVOICE_12_CH = "红冲待审核";
	public static final String INPUT_INVOICE_STATUS_13 = "13";
	public static final String INPUT_INVOICE_13_CH = "红冲已审核";
	public static final String INPUT_INVOICE_STATUS_14 = "14";
	public static final String INPUT_INVOICE_14_CH = "已红冲";
	public static final String INPUT_INVOICE_STATUS_15 = "15";
	public static final String INPUT_INVOICE_15_CH = "认证提交";
	public static final String INPUT_INVOICE_STATUS_16 = "16";
	public static final String INPUT_INVOICE_16_CH = "转出提交";

	// 特殊登记-后续处理类型标识
	public static final String SIGN_A = "Y";
	public static final String SIGN_A_CH = "月寄";
	public static final String SIGN_B = "Y";
	public static final String SIGN_B_CH = "快递";
	public static final String SIGN_C = "Y";
	public static final String SIGN_C_CH = "难字";
	public static final String SIGN_D = "Y";
	public static final String SIGN_D_CH = "转代理人";
	public static final String SIGN_E = "Y";
	public static final String SIGN_E_CH = "临时地址";
	public static final String SIGN_F = "Y";
	public static final String SIGN_F_CH = "平信";
	public static final String SIGN_G = "Y";
	public static final String SIGN_G_CH = "挂号";
	public static final String SIGN_H = "Y";
	public static final String SIGN_H_CH = "集汇件";
	public static final String SIGN_I = "Y";
	public static final String SIGN_I_CH = "客户自取";
	public static final String SIGN_J = "Y";
	public static final String SIGN_J_CH = "送前电话通知";
	public static final String SIGN_K = "Y";
	public static final String SIGN_K_CH = "拒收";
	public static final String SIGN_L = "Y";
	public static final String SIGN_L_CH = "定期邮寄";
	
	

	public static String getSignType(String signType){
		if(signType != null && SIGN_A.equals(signType)){
			return SIGN_A_CH;
		}else if(signType != null && SIGN_B.equals(signType)){
			return SIGN_B_CH;
		}else if(signType != null && SIGN_C.equals(signType)){
			return SIGN_C_CH;
		}else if(signType != null && SIGN_D.equals(signType)){
			return SIGN_D_CH;
		}else if(signType != null && SIGN_E.equals(signType)){
			return SIGN_E_CH;
		}else if(signType != null && SIGN_F.equals(signType)){
			return SIGN_F_CH;
		}else if(signType != null && SIGN_G.equals(signType)){
			return SIGN_G_CH;
		}else if(signType != null && SIGN_H.equals(signType)){
			return SIGN_H_CH;
		}else if(signType != null && SIGN_I.equals(signType)){
			return SIGN_I_CH;
		}else if(signType != null && SIGN_J.equals(signType)){
			return SIGN_J_CH;
		}else if(signType != null && SIGN_K.equals(signType)){
			return SIGN_K_CH;
		}else if(signType != null && SIGN_L.equals(signType)){
			return SIGN_L_CH;
		}
		return "";
	}
	// 匹配投标单号表状态
	public static final Integer MATCH_FLAG_1 = 1;
	public static final String MATCH_FLAG_1_CH = "匹配成功";
	public static final Integer MATCH_FLAG_2 = 2;
	public static final String MATCH_FLAG_2_CH = "匹配失败";
	public static final Integer MATCH_FLAG_3 = 3;
	public static final String MATCH_FLAG_3_CH = "是并已开票";

	/**
	 * Abel:Metlife Begin
	 */
	/**
	 * 费用类型
	 */
	public static final String CHARGES_TYPE_001 = "001";
	public static final String CHARGES_TYPE_001_CH = "首期";
	public static final String CHARGES_TYPE_002 = "002";
	public static final String CHARGES_TYPE_002_CH = "续期";
	public static final String CHARGES_TYPE_003 = "003";
	public static final String CHARGES_TYPE_003_CH = "保全";
	public static final String CHARGES_TYPE_004 = "004";
	public static final String CHARGES_TYPE_004_CH = "利息收入";
	public static final String CHARGES_TYPE_005 = "005";
	public static final String CHARGES_TYPE_005_CH = "保单相关收入";
	public static final String CHARGES_TYPE_006 = "006";
	public static final String CHARGES_TYPE_006_CH = "保单相关收入-月扣";
	public static final String CHARGES_TYPE_007 = "007";
	public static final String CHARGES_TYPE_007_CH = "保费收入-万能首期";
	public static final String CHARGES_TYPE_008 = "008";
	public static final String CHARGES_TYPE_008_CH = "保费收入-万能续期";
	public static final String CHARGES_TYPE_009 = "009";
	public static final String CHARGES_TYPE_009_CH = "保费收入-万能保全";
	public static final String CHARGES_TYPE_010 = "010";
	public static final String CHARGES_TYPE_010_CH = "月月加";
	public static final String CHARGES_TYPE_011 = "011";
	public static final String CHARGES_TYPE_011_CH = "首期投资";
	public static final String CHARGES_TYPE_012 = "012";
	public static final String CHARGES_TYPE_012_CH = "续期投资";
	public static final String CHARGES_TYPE_013 = "013";
	public static final String CHARGES_TYPE_013_CH = "一次性投资";
	public static final String CHARGES_TYPE_014 = "014";
	public static final String CHARGES_TYPE_014_CH = "保全投资";
	public static final String CHARGES_TYPE_015 = "015";
	public static final String CHARGES_TYPE_015_CH = "定结";
	public static final String CHARGES_TYPE_016 = "016";
	public static final String CHARGES_TYPE_016_CH = "保全加人";
	public static final String CHARGES_TYPE_017 = "017";
	public static final String CHARGES_TYPE_017_CH = "计划变更";
	public static final String CHARGES_TYPE_018 = "018";
	public static final String CHARGES_TYPE_018_CH = "增额";
	public static final String CHARGES_TYPE_019 = "019";
	public static final String CHARGES_TYPE_019_CH = "保全减人";
	public static final String CHARGES_TYPE_020 = "020";
	public static final String CHARGES_TYPE_020_CH = "减额";

	public static String getFeeTyp(String feeTyp) {
		if (feeTyp != null && CHARGES_TYPE_001.equals(feeTyp)) {
			return CHARGES_TYPE_001_CH;
		} else if (feeTyp != null && CHARGES_TYPE_002.equals(feeTyp)) {
			return CHARGES_TYPE_002_CH;
		} else if (feeTyp != null && CHARGES_TYPE_003.equals(feeTyp)) {
			return CHARGES_TYPE_003_CH;
		} else if (feeTyp != null && CHARGES_TYPE_004.equals(feeTyp)) {
			return CHARGES_TYPE_004_CH;
		} else if (feeTyp != null && CHARGES_TYPE_005.equals(feeTyp)) {
			return CHARGES_TYPE_005_CH;
		} else if (feeTyp != null && CHARGES_TYPE_006.equals(feeTyp)) {
			return CHARGES_TYPE_006_CH;
		} else if (feeTyp != null && CHARGES_TYPE_007.equals(feeTyp)) {
			return CHARGES_TYPE_007_CH;
		} else if (feeTyp != null && CHARGES_TYPE_008.equals(feeTyp)) {
			return CHARGES_TYPE_008_CH;
		} else if (feeTyp != null && CHARGES_TYPE_009.equals(feeTyp)) {
			return CHARGES_TYPE_009_CH;
		} else if (feeTyp != null && CHARGES_TYPE_010.equals(feeTyp)) {
			return CHARGES_TYPE_010_CH;
		} else if (feeTyp != null && CHARGES_TYPE_011.equals(feeTyp)) {
			return CHARGES_TYPE_011_CH;
		} else if (feeTyp != null && CHARGES_TYPE_012.equals(feeTyp)) {
			return CHARGES_TYPE_012_CH;
		} else if (feeTyp != null && CHARGES_TYPE_013.equals(feeTyp)) {
			return CHARGES_TYPE_013_CH;
		} else if (feeTyp != null && CHARGES_TYPE_014.equals(feeTyp)) {
			return CHARGES_TYPE_014_CH;
		} else if (feeTyp != null && CHARGES_TYPE_015.equals(feeTyp)) {
			return CHARGES_TYPE_015_CH;
		} else if (feeTyp != null && CHARGES_TYPE_016.equals(feeTyp)) {
			return CHARGES_TYPE_016_CH;
		} else if (feeTyp != null && CHARGES_TYPE_017.equals(feeTyp)) {
			return CHARGES_TYPE_017_CH;
		} else if (feeTyp != null && CHARGES_TYPE_018.equals(feeTyp)) {
			return CHARGES_TYPE_018_CH;
		} else if (feeTyp != null && CHARGES_TYPE_019.equals(feeTyp)) {
			return CHARGES_TYPE_019_CH;
		} else if (feeTyp != null && CHARGES_TYPE_020.equals(feeTyp)) {
			return CHARGES_TYPE_020_CH;
		}
		return "";
	}

	/**
	 * 交费频率
	 */
	public static final String PAYMENT_FREQUENCY_00 = "00";
	public static final String PAYMENT_FREQUENCY_00_CH = "趸交";
	public static final String PAYMENT_FREQUENCY_01 = "01";
	public static final String PAYMENT_FREQUENCY_01_CH = "年交";
	public static final String PAYMENT_FREQUENCY_02 = "02";
	public static final String PAYMENT_FREQUENCY_02_CH = "半年交";
	public static final String PAYMENT_FREQUENCY_03 = "03";
	public static final String PAYMENT_FREQUENCY_03_CH = "3年交";
	public static final String PAYMENT_FREQUENCY_04 = "04";
	public static final String PAYMENT_FREQUENCY_04_CH = "季交";
	public static final String PAYMENT_FREQUENCY_05 = "05";
	public static final String PAYMENT_FREQUENCY_05_CH = "5年交";
	public static final String PAYMENT_FREQUENCY_12 = "12";
	public static final String PAYMENT_FREQUENCY_12_CH = "月交";
	public static final String PAYMENT_FREQUENCY_99 = "99";
	public static final String PAYMENT_FREQUENCY_99_CH = "不定期";

	public static String getBillFreq(String billFreq) {
		if (billFreq != null && PAYMENT_FREQUENCY_00.equals(billFreq)) {
			return PAYMENT_FREQUENCY_00_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_01.equals(billFreq)) {
			return PAYMENT_FREQUENCY_01_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_02.equals(billFreq)) {
			return PAYMENT_FREQUENCY_02_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_03.equals(billFreq)) {
			return PAYMENT_FREQUENCY_03_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_04.equals(billFreq)) {
			return PAYMENT_FREQUENCY_04_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_05.equals(billFreq)) {
			return PAYMENT_FREQUENCY_05_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_12.equals(billFreq)) {
			return PAYMENT_FREQUENCY_12_CH;
		} else if (billFreq != null && PAYMENT_FREQUENCY_99.equals(billFreq)) {
			return PAYMENT_FREQUENCY_99_CH;
		}
		return "";
	}

	/**
	 * 渠道
	 */
	public static final String CHANNEL_TYPE_01 = "AGY";
	public static final String CHANNEL_TYPE_01_CH = "代理人";
	public static final String CHANNEL_TYPE_02 = "TM";
	public static final String CHANNEL_TYPE_02_CH = "电话销售";
	public static final String CHANNEL_TYPE_03 = "BXS";
	public static final String CHANNEL_TYPE_03_CH = "银行代销";
	public static final String CHANNEL_TYPE_04 = "RN";
	public static final String CHANNEL_TYPE_04_CH = "续收";
	public static final String CHANNEL_TYPE_05 = "DGT";
	public static final String CHANNEL_TYPE_05_CH = "网销";
	public static final String CHANNEL_TYPE_99 = "GRP";
	public static final String CHANNEL_TYPE_99_CH = "团险";

	
	public static String getChanNel(String chanNel){
		if(chanNel != null && CHANNEL_TYPE_01.equals(chanNel)){
			return CHANNEL_TYPE_01_CH;
		}else if(chanNel != null && CHANNEL_TYPE_02.equals(chanNel)){
			return CHANNEL_TYPE_02_CH;
		}else if(chanNel != null && CHANNEL_TYPE_03.equals(chanNel)){
			return CHANNEL_TYPE_03_CH;
		}else if(chanNel != null && CHANNEL_TYPE_04.equals(chanNel)){
			return CHANNEL_TYPE_04_CH;
		}else if(chanNel != null && CHANNEL_TYPE_05.equals(chanNel)){
			return CHANNEL_TYPE_05_CH;
		}else if(chanNel != null && CHANNEL_TYPE_99.equals(chanNel)){
			return CHANNEL_TYPE_99_CH;
		}
		return chanNel;
	}
	/**
	 * 数据来源
	 */
	public static final String DATA_SOURCE_LSP = "LSP";
	public static final String DATA_SOURCE_LSP_CH = "个险（LSP)";
	//public static final String DATA_SOURCE_EBSI = "EBSI";
	//public static final String DATA_SOURCE_EBSI_CH = "个险（EBSI）";
	public static final String DATA_SOURCE_LIS = "LIS";
	public static final String DATA_SOURCE_LIS_CH = "团险（LIS）";

	public static String getDsouRce(String dsouRce) {
		if (dsouRce != null && DATA_SOURCE_LSP.equals(dsouRce)) {
			return DATA_SOURCE_LSP_CH;
		}/* else if (dsouRce != null && DATA_SOURCE_EBSI.equals(dsouRce)) {
			return DATA_SOURCE_EBSI_CH;
		}*/else if (dsouRce != null && DATA_SOURCE_LIS.equals(dsouRce)) {
			return DATA_SOURCE_LIS_CH;
		}
		return "";
	}
	
	
	/**
	 * 历史销项数据是否移送标识
	 */

	public static final String TRANSFERFLAG_0 = "0";
	public static final String TRANSFERFLAG_0_CH = "未移送";
	public static final String TRANSFERFLAG_1 = "1";
	public static final String TRANSFERFLAG_1_CH = "已移送";
	
	
	public static String getTransFerFlag(String transFerFlag){
		if(transFerFlag != null && TRANSFERFLAG_0.equals(transFerFlag)){
			return TRANSFERFLAG_0_CH;
		}else if(transFerFlag != null && TRANSFERFLAG_1.equals(transFerFlag)){
				return TRANSFERFLAG_1_CH;
			}
		return "";
	}
	
	/**
	 * 收入会计科目
	 */
	public static final String ACCOUNT_CODE_D1_5370000010 = "5370000010";
	public static final String ACCOUNT_CODE_C1_8260000000 = "8260000000";
	public static final String ACCOUNT_CODE_C1_8260000010 = "8260000010";
	
	/**
	 * Abel:Metlife End
	 */
	// 交易查询标识
	public static final String CHECK = "check"; // 首页对账查询校验通过的非冲账交易
	public static final String MAKE_INVOICE = "makeInvoice"; // 查看可供开票的交易
	public static final String DETAIL = "detail"; // 查看交易明细
	public static final String TRANS_BILL_NO_PASS = "transBillNoPass"; // 查询交易是否对应未审核通过的票据
	public static final String DELETE_BILL = "deleteBill"; // 删除编辑中票据时，查询对应交易信息
	public static final String AUTO_INVOICE = "autoInvoice"; // 自动开票
	// 票据查询标识
	public static final String MODIFY_BILL = "modify"; // 查询可供编辑的票据(与菜单对应)
	public static final String AUDIT_BILL = "audit"; // 查询可供审核的票据(与菜单对应)
	public static final String PRINT_BILL = "print"; // 查询可供打印的票据(与菜单对应)
	public static final String TRACK_BILL = "track"; // 查询可进行废票/红冲的票据(与菜单对应)
	// 票据CancelFlag
	public static final String HONGCHONG = "H";// 红冲
	public static final String FEIPIAO = "F";// 废票
	// 票据是否手工录入 0-自动开票;1-人工审核;3-人工开票
	public static final String BILL_ISHANDIWORK_1 = "1";// 自动开票
	public static final String BILL_ISHANDIWORK_2 = "2";// 人工审核
	public static final String BILL_ISHANDIWORK_3 = "3";// 人工开票
	public static final String BILL_ISHANDIWORK_ch1 = "自动开票";//
	public static final String BILL_ISHANDIWORK_ch2 = "人工审核";//
	public static final String BILL_ISHANDIWORK_ch3 = "人工开票";//
	// 是否打票 A-自动打印/M-手动打印/N-永不打印
	public static final String FAPIAO_FLAG_A = "A";// 自动打印
	public static final String FAPIAO_FLAG_M = "M";// 手动打印
	public static final String FAPIAO_FLAG_N = "N";// 永不打印
	// 交易标志 1-权责发生/2-实收实付
	public static final String TRANS_FLAG_1 = "1";// 权责发生
	public static final String TRANS_FLAG_2 = "2";// 实收实付
	// 发票类型
	public static final String VAT_TYPE_0 = "0";// 增值税专用发票
	public static final String VAT_TYPE_1 = "1";// 增值税普通发票
	// 发票开具类型 1-单笔;2-合并;3-拆分
	public static final String ISSUE_TYPE_1 = "1";// 单笔
	public static final String ISSUE_TYPE_2 = "2";// 合并
	public static final String ISSUE_TYPE_3 = "3";// 拆分
	// 字典项类型
	public static final String TAXPAYER_TYPE = "TAXPAYER_TYPE";// 纳税人类别
	public static final String VAT_TYPE = "VAT_TYPE";// 发票类型
	public static final String AUTHENTICATION_FLAG = "AUTHENTICATION_FLAG";// 状态
	// 税控默认金额常量
	public static BigDecimal different = new BigDecimal(0.0625);// 轧差金额 暂默认六分二厘五
	public static BigDecimal billMaxAmt = new BigDecimal(9.99);// 单笔发票最大金额
	// 进项税发票认证
	public static final String deductedDaysFlag = "逾期";
	// 客户纳税人类型； S/G/O/I，S-小规模纳税人，G-一般纳税人，O-其他，I-个体纳税人
	public static final String VAL_CUSTOMER_TAX_TYPE_1 = "S";
	public static final String VAL_CUSTOMER_TAX_TYPE_1_CH = "小规模纳税人";
	public static final String VAL_CUSTOMER_TAX_TYPE_2 = "G";
	public static final String VAL_CUSTOMER_TAX_TYPE_2_CH = "一般纳税人";
	public static final String VAL_CUSTOMER_TAX_TYPE_4 = "O";
	public static final String VAL_CUSTOMER_TAX_TYPE_4_CH = "其他";
	public static final String VAL_CUSTOMER_TAX_TYPE_3 = "I";
	public static final String VAL_CUSTOMER_TAX_TYPE_3_CH = "个体纳税人";

	public static String getCustomerTaxPayerTypeCh(String customerTaxPayerType){
		if(customerTaxPayerType != null && !"".equals(customerTaxPayerType) && VAL_CUSTOMER_TAX_TYPE_1.equals(customerTaxPayerType)){
			return VAL_CUSTOMER_TAX_TYPE_1_CH;
		}else if(customerTaxPayerType != null && !"".equals(customerTaxPayerType) && VAL_CUSTOMER_TAX_TYPE_2.equals(customerTaxPayerType)){
			return VAL_CUSTOMER_TAX_TYPE_2_CH;
		}else if(customerTaxPayerType != null && !"".equals(customerTaxPayerType) && VAL_CUSTOMER_TAX_TYPE_3.equals(customerTaxPayerType)){
			return VAL_CUSTOMER_TAX_TYPE_3_CH;
		}else if(customerTaxPayerType != null && !"".equals(customerTaxPayerType) && VAL_CUSTOMER_TAX_TYPE_4.equals(customerTaxPayerType)){
			return VAL_CUSTOMER_TAX_TYPE_4_CH;
		}
		return customerTaxPayerType;
	}
	// 自动开票相关参数
	public static final String W_S = "S";// 开票方式-单笔
	public static final String W_M = "M";// 开票方式-合并
	public static final String K_A = "A";// 开票种类-全部
	public static final String K_G = "G";// 开票种类-普票
	public static final String K_S = "S";// 开票种类-专票
	
	
	// KBC数据来源  1-所有/2-LoanQ/3-Eximbills/4-FXMM/5-CAS/6-P&R
	public static final String KBC_DATASOURCE_1 = "1";
	public static final String KBC_DATASOURCE_1_CH = "所有";
	public static final String KBC_DATASOURCE_2 = "2";
	public static final String KBC_DATASOURCE_2_CH = "LoanQ";
	public static final String KBC_DATASOURCE_3 = "3";
	public static final String KBC_DATASOURCE_3_CH = "Eximbills";
	public static final String KBC_DATASOURCE_4 = "4";
	public static final String KBC_DATASOURCE_4_CH = "FXMM";
	public static final String KBC_DATASOURCE_5 = "5";
	public static final String KBC_DATASOURCE_5_CH = "CAS";
	public static final String KBC_DATASOURCE_6 = "6";
	public static final String KBC_DATASOURCE_6_CH = "P&R";
	public static final String KBC_DATASOURCE_99 = "99";
	public static final String KBC_DATASOURCE_99_CH = "手工";
	
	public static String getDataSrarus(String datastatus) {
		if (BILL_STATUS_15.equals(datastatus)
				|| BILL_STATUS_18.equals(datastatus)) {
			return "是";
		} else {
			return "否";
		}
	}

	public static String getDataStatusCH(String datastatus, String bussFlag) {
		if ("TRANS".equalsIgnoreCase(bussFlag)) {
			if (TRANS_STATUS_1.equals(datastatus)) {
				return TRANS_STATUS_1_CH;
			} else if (TRANS_STATUS_2.equals(datastatus)) {
				return TRANS_STATUS_2_CH;
			} else if (TRANS_STATUS_3.equals(datastatus)) {
				return TRANS_STATUS_3_CH;
			} else if (TRANS_STATUS_4.equals(datastatus)) {
				return TRANS_STATUS_4_CH;
			} else if (TRANS_STATUS_5.equals(datastatus)) {
				return TRANS_STATUS_5_CH;
			} else if (TRANS_STATUS_6.equals(datastatus)) {
				return TRANS_STATUS_6_CH;
			} else if (TRANS_STATUS_7.equals(datastatus)) {
				return TRANS_STATUS_7_CH;
			} else if (TRANS_STATUS_8.equals(datastatus)) {
				return TRANS_STATUS_8_CH;
			} else if (TRANS_STATUS_9.equals(datastatus)) {
				return TRANS_STATUS_9_CH;
			} else if (TRANS_STATUS_99.equals(datastatus)) {
				return TRANS_STATUS_99_CH;
			}
		} else if ("BILL".equalsIgnoreCase(bussFlag)) {
			if (BILL_STATUS_1.equals(datastatus)) {
				return BILL_STATUS_1_CH;
			} else if (BILL_STATUS_2.equals(datastatus)) {
				return BILL_STATUS_2_CH;
			} else if (BILL_STATUS_3.equals(datastatus)) {
				return BILL_STATUS_3_CH;
			} else if (BILL_STATUS_4.equals(datastatus)) {
				return BILL_STATUS_4_CH;
			} else if (BILL_STATUS_5.equals(datastatus)) {
				return BILL_STATUS_5_CH;
			} else if (BILL_STATUS_7.equals(datastatus)) {
				return BILL_STATUS_7_CH;
			} else if (BILL_STATUS_8.equals(datastatus)) {
				return BILL_STATUS_8_CH;
			} else if (BILL_STATUS_9.equals(datastatus)) {
				return BILL_STATUS_9_CH;
			} else if (BILL_STATUS_10.equals(datastatus)) {
				return BILL_STATUS_10_CH;
			} else if (BILL_STATUS_11.equals(datastatus)) {
				return BILL_STATUS_11_CH;
			} else if (BILL_STATUS_12.equals(datastatus)) {
				return BILL_STATUS_12_CH;
			} else if (BILL_STATUS_13.equals(datastatus)) {
				return BILL_STATUS_13_CH;
			} else if (BILL_STATUS_14.equals(datastatus)) {
				return BILL_STATUS_14_CH;
			} else if (BILL_STATUS_15.equals(datastatus)) {
				return BILL_STATUS_15_CH;
			} else if (BILL_STATUS_16.equals(datastatus)) {
				return BILL_STATUS_16_CH;
			} else if (BILL_STATUS_17.equals(datastatus)) {
				return BILL_STATUS_17_CH;
			} else if (BILL_STATUS_18.equals(datastatus)) {
				return BILL_STATUS_18_CH;
			} else if (BILL_STATUS_19.equals(datastatus)) {
				return BILL_STATUS_19_CH;
			} else if (BILL_STATUS_20.equals(datastatus)) {
				return BILL_STATUS_20_CH;
			} else if (BILL_STATUS_21.equals(datastatus)) {
				return BILL_STATUS_21_CH;
			} else if (BILL_STATUS_99.equals(datastatus)) {
				return BILL_STATUS_99_CH;
			}
		} else if ("INPUT_INVOICE".equalsIgnoreCase(bussFlag)) {
			if (INPUT_INVOICE_STATUS_1.equals(datastatus)) {
				return INPUT_INVOICE_1_CH;
			} else if (INPUT_INVOICE_STATUS_2.equals(datastatus)) {
				return INPUT_INVOICE_2_CH;
			} else if (INPUT_INVOICE_STATUS_3.equals(datastatus)) {
				return INPUT_INVOICE_3_CH;
			} else if (INPUT_INVOICE_STATUS_4.equals(datastatus)) {
				return INPUT_INVOICE_4_CH;
			} else if (INPUT_INVOICE_STATUS_5.equals(datastatus)) {
				return INPUT_INVOICE_5_CH;
			} else if (INPUT_INVOICE_STATUS_6.equals(datastatus)) {
				return INPUT_INVOICE_6_CH;
			} else if (INPUT_INVOICE_STATUS_7.equals(datastatus)) {
				return INPUT_INVOICE_7_CH;
			} else if (INPUT_INVOICE_STATUS_8.equals(datastatus)) {
				return INPUT_INVOICE_8_CH;
			} else if (INPUT_INVOICE_STATUS_9.equals(datastatus)) {
				return INPUT_INVOICE_9_CH;
			} else if (INPUT_INVOICE_STATUS_10.equals(datastatus)) {
				return INPUT_INVOICE_10_CH;
			} else if (INPUT_INVOICE_STATUS_11.equals(datastatus)) {
				return INPUT_INVOICE_11_CH;
			} else if (INPUT_INVOICE_STATUS_12.equals(datastatus)) {
				return INPUT_INVOICE_12_CH;
			} else if (INPUT_INVOICE_STATUS_13.equals(datastatus)) {
				return INPUT_INVOICE_13_CH;
			} else if (INPUT_INVOICE_STATUS_14.equals(datastatus)) {
				return INPUT_INVOICE_14_CH;
			} else if (INPUT_INVOICE_STATUS_15.equals(datastatus)) {
				return INPUT_INVOICE_15_CH;
			} else if (INPUT_INVOICE_STATUS_16.equals(datastatus)) {
				return INPUT_INVOICE_16_CH;
			}
		}
		return "";
	}

	public static String getDataStatus_code(String datastatus) {
		if (INPUT_INVOICE_1_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_1;
		} else if (INPUT_INVOICE_2_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_2;
		} else if (INPUT_INVOICE_3_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_3;
		} else if (INPUT_INVOICE_4_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_4;
		} else if (INPUT_INVOICE_5_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_5;
		} else if (INPUT_INVOICE_6_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_6;
		} else if (INPUT_INVOICE_7_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_7;
		} else if (INPUT_INVOICE_8_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_8;
		} else if (INPUT_INVOICE_9_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_9;
		} else if (INPUT_INVOICE_10_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_10;
		} else if (INPUT_INVOICE_11_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_11;
		} else if (INPUT_INVOICE_12_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_12;
		} else if (INPUT_INVOICE_13_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_13;
		} else if (INPUT_INVOICE_14_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_14;
		} else if (INPUT_INVOICE_15_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_15;
		} else if (INPUT_INVOICE_16_CH.equals(datastatus)) {
			return INPUT_INVOICE_STATUS_16;
		} else {
			return "";
		}
	}

	public static String getFlagDesc(String flag) {
		if (YES.equals(flag)) {
			return "是";
		} else if (NO.equals(flag)) {
			return "否";
		}
		return "";
	}

	// ys ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 是否打票
	public static String getFapiaoFlagCH(String fapiaoFlag) {
		if ("A".equals(fapiaoFlag)) {
			return "自动打印";
		} else if ("M".equals(fapiaoFlag)) {
			return "手动打印";
		} else if ("N".equals(fapiaoFlag)) {
			return "永不打印";
		}
		return "";
	}

	// 增值税种类
	public static String getVatRateCodeCH(String vatRateCode) {
		if ("S".equals(vatRateCode)) {
			return "6%";
		} else if ("Z".equals(vatRateCode)) {
			return "0%";
		} else if ("F".equals(vatRateCode)) {
			return "免税";
		}
		return "";
	}

	// 是否冲账
	public static String getIsReverseCH(String isReverseCH) {
		if ("Y".equals(isReverseCH)) {
			return "是";
		} else if ("N".equals(isReverseCH)) {
			return "否";
		}
		return "";
	}

	// ys ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑ ↑

	// dcg 添加开始
	public static String getTransFlag(String flag) {
		if ("1".equals(flag)) {
			return "权责发生";
		} else if ("2".equals(flag)) {
			return "实收实付";
		}
		return "";
	}

	public static List getAllBillDataStatusList() {
		List l = new ArrayList();
		SelectTag s = new SelectTag("1", "编辑待提交");
		l.add(s);
		s = new SelectTag("2", "提交待审核");
		l.add(s);
		s = new SelectTag("3", "审核通过");
		l.add(s);
		s = new SelectTag("4", "无需审核");
		l.add(s);
		s = new SelectTag("5", "已开具");
		l.add(s);
		s = new SelectTag("7", "开具失败");
		l.add(s);
		s = new SelectTag("8", "已打印");
		l.add(s);
		s = new SelectTag("9", "打印失败");
		l.add(s);
		s = new SelectTag("10", "已快递");
		l.add(s);
		s = new SelectTag("11", "已签收");
		l.add(s);
		s = new SelectTag("12", "已抄报");
		l.add(s);
		s = new SelectTag("13", "作废待审核");
		l.add(s);
		s = new SelectTag("14", "作废已审核");
		l.add(s);
		s = new SelectTag("15", "已作废");
		l.add(s);
		s = new SelectTag("16", "红冲待审核");
		l.add(s);
		s = new SelectTag("17", "红冲已审核");
		l.add(s);
		s = new SelectTag("18", "已红冲");
		l.add(s);
		s = new SelectTag("99", "生效完成");
		l.add(s);
		return l;
	}

	// 发票状态
	public static List getInputInvoiceDataStatusList() {
		List l = new ArrayList();
		SelectTag s = new SelectTag("1", "已扫描未认证");
		l.add(s);
		s = new SelectTag("2", "认证未收到回执");
		l.add(s);
		s = new SelectTag("3", "首次认证通过");
		l.add(s);
		s = new SelectTag("4", "首次认证未通过");
		l.add(s);
		s = new SelectTag("5", "再次认证通过");
		l.add(s);
		s = new SelectTag("6", "再次认证未通过");
		l.add(s);
		s = new SelectTag("7", "税务局当场认证通过");
		l.add(s);
		s = new SelectTag("8", "税务局当场认证未通过");
		l.add(s);
		s = new SelectTag("9", "票退回");
		l.add(s);
		s = new SelectTag("10", "已抵扣");
		l.add(s);
		s = new SelectTag("11", "不可抵扣");
		l.add(s);
		s = new SelectTag("12", "红冲待审核");
		l.add(s);
		s = new SelectTag("13", "红冲已审核");
		l.add(s);
		s = new SelectTag("14", "已红冲");
		l.add(s);
		s = new SelectTag("15", "认证提交");
		l.add(s);
		s = new SelectTag("16", "转出提交");
		l.add(s);
		return l;
	}

	// 是否手工录入
	public static List getBillIsHandiWorklist() {
		List l = new ArrayList();
		SelectTag s = new SelectTag(BILL_ISHANDIWORK_1, BILL_ISHANDIWORK_ch1);
		l.add(s);
		s = new SelectTag(BILL_ISHANDIWORK_2, BILL_ISHANDIWORK_ch2);
		l.add(s);
		s = new SelectTag(BILL_ISHANDIWORK_3, BILL_ISHANDIWORK_ch3);
		l.add(s);

		return l;
	}

	// 发票状态
	public static List getInputInvoiceDataStatusListPart() {
		List l = new ArrayList();
		SelectTag s = new SelectTag("3", "首次认证通过");
		l.add(s);
		s = new SelectTag("5", "再次认证通过");
		l.add(s);
		s = new SelectTag("7", "税务局当场认证通过");
		l.add(s);
		s = new SelectTag("10", "已抵扣");
		l.add(s);
		s = new SelectTag("11", "不可抵扣");
		l.add(s);
		s = new SelectTag("15", "认证提交");
		l.add(s);
		s = new SelectTag("16", "转出提交");
		l.add(s);
		return l;
	}

	// 发票类型
	public static String getFapiaoTypeCH(String fapiaoType) {
		if ("0".equals(fapiaoType)) {
			return "增值税专用发票";
		} else if ("1".equals(fapiaoType)) {
			return "增值税普通发票";
		}
		return "";
	}
	
	/**
	 * Abel:Metlife Brgin
	 * @param fapiaoType
	 * @return
	 */
	
	//发票类型
	public static String getFapiaoTypeCH_M(String fapiaoType) {
		if ("0".equals(fapiaoType)) {
			return "增值税专用发票";
		} else if ("1".equals(fapiaoType)) {
			return "增值税普通发票";
		}
		return "";
	}

	/**
	 * Adbel:Metlife End
	 * @param fapiaoType
	 * @return
	 */

	public static String getFapiaoTypeCode(String fapiaoType) {
		if ("增值税专用发票".equals(fapiaoType)) {
			return "0";
		} else if ("增值税普通发票".equals(fapiaoType)) {
			return "1";
		}
		return "";
	}

	/**
	 * 为自动开票配置显示对应票据状态
	 * 
	 * @return List
	 */
	public static List getBillDataStatusListForAutoInvoice() {
		List l = new ArrayList();
		// 提交待审核
		SelectTag s = new SelectTag(BILL_STATUS_2, BILL_STATUS_2_CH);
		l.add(s);
		// 无需审核
		s = new SelectTag(BILL_STATUS_4, BILL_STATUS_4_CH);
		l.add(s);
		// 开具
		s = new SelectTag(BILL_STATUS_5, BILL_STATUS_5_CH);
		l.add(s);
		return l;
	}

	public static String getYOrNCH(String flag) {
		if ("Y".equals(flag)) {
			return "是";
		} else if ("N".equals(flag)) {
			return "否";
		}
		return "";
	}

	// 开具类型
	public static String getIssueTypeCH(String issueType) {
		if ("1".equals(issueType)) {
			return "单笔";
		} else if ("2".equals(issueType)) {
			return "合并";
		} else if ("3".equals(issueType)) {
			return "拆分";
		}
		return "";
	}

	// 供应商类别
	public static String getTaxpayerTypeCH(String type) {
		if ("0".equals(type)) {
			return "独立纳税主体";
		} else if ("1".equals(type)) {
			return "开票网点";
		} else if ("2".equals(type)) {
			return "其他管理机构";
		}
		return type;
	}

	// 是否手工录入
	public static String getIsHandiworkCH(String isHandiwork) {
		if ("1".equals(isHandiwork)) {
			return "自动开票";
		} else if ("2".equals(isHandiwork)) {
			return "人工审核";
		} else if ("3".equals(isHandiwork)) {
			return "人工开票";
		}
		return "";
	}

	// 是否含税
	public static String getTaxFlagCH(String taxFlag) {
		if ("Y".equals(taxFlag)) {
			return "含税";
		} else if ("N".equals(taxFlag)) {
			return "不含税";
		}
		return "";
	}

	// 折算类型
	public static String getConvertTypCH(String convertTyp) {
		if ("D".equals(convertTyp)) {
			return "D:除";
		} else if ("M".equals(convertTyp)) {
			return "M:乘";
		}
		return "";
	}

	// 折算类型
	public static String getEmsStatusCH(String emsStatus) {
		if ("1".equals(emsStatus)) {
			return "打印已快递";
		} else if ("2".equals(emsStatus)) {
			return "打印未快递";
		} else if ("3".equals(emsStatus)) {
			return "已签收";
		}
		return "";
	}

	public static String getcustomerFapiaoFlagCH(String customerFapiaoFlag) {
		if ("A".equals(customerFapiaoFlag)) {
			return "自动打印";
		} else if ("M".equals(customerFapiaoFlag)) {
			return "手动打印";
		} else if ("N".equals(customerFapiaoFlag)) {
			return "永不打印";
		}
		return "";
	}

	public static String getCustomerTypeCH(String customerCH) {

		if ("I".equals(customerCH)) {
			return "私人客户";
		} else if ("C".equals(customerCH)) {
			return "公司客户";

		}
		return "";
	}

	public static List getBillDataStatusListForPageListTrans() {
		List l = new ArrayList();
		SelectTag s0 = new SelectTag("0", "删除待审核");
		l.add(s0);
		SelectTag s4 = new SelectTag("4", "上报待审核");
		l.add(s4);
		return l;
	}

	/**
	 * 【开票申请】界面，查询所有状态的交易记录信息
	 * 
	 * @return List
	 */
	public static List getTransDataStatusListForPageListTrans() {
		List l = new ArrayList();
		SelectTag s1 = new SelectTag(TRANS_STATUS_1, TRANS_STATUS_1_CH);// 未开票
		l.add(s1);
		SelectTag s2 = new SelectTag(TRANS_STATUS_2, TRANS_STATUS_2_CH);// 开票编辑锁定中
		l.add(s2);
		SelectTag s3 = new SelectTag(TRANS_STATUS_3, TRANS_STATUS_3_CH);// 开票中
		l.add(s3);
		return l;
	}
	
	public static List getKbcDataSouceList(){
		//  1-所有/2-LoanQ/3-Eximbills/4-FXMM/5-CAS/6-P&R
		List l = new ArrayList();
		SelectTag s1 = new SelectTag(KBC_DATASOURCE_1, KBC_DATASOURCE_1_CH);// 所有
		l.add(s1);
		SelectTag s2 = new SelectTag(KBC_DATASOURCE_2, KBC_DATASOURCE_2_CH);// LoanQ
		l.add(s2);
		SelectTag s3 = new SelectTag(KBC_DATASOURCE_3, KBC_DATASOURCE_3_CH);// Eximbills
		l.add(s3);
		SelectTag s4 = new SelectTag(KBC_DATASOURCE_4, KBC_DATASOURCE_4_CH);// FXMM
		l.add(s4);
		SelectTag s5 = new SelectTag(KBC_DATASOURCE_5, KBC_DATASOURCE_5_CH);// CAS
		l.add(s5);
		SelectTag s6 = new SelectTag(KBC_DATASOURCE_6, KBC_DATASOURCE_6_CH);// P&R
		l.add(s6);
		SelectTag s99 = new SelectTag(KBC_DATASOURCE_99, KBC_DATASOURCE_99_CH);// 手工
		l.add(s99);
		return l;
	}

	/**
	 * 【综合查询】交易界面，查询所有状态的交易记录信息
	 * 
	 * @return List状态 1-未开票/2-开票编辑锁定中/3-开票中/4-删除/5-被冲账/6-已冲账/99-开票完成
	 */
	public static List getTransDataStatusListForPageListMainTrans() {
		List l = new ArrayList();
		SelectTag s1 = new SelectTag(TRANS_STATUS_1, TRANS_STATUS_1_CH);// 未开票
		l.add(s1);
		SelectTag s2 = new SelectTag(TRANS_STATUS_2, TRANS_STATUS_2_CH);// 开票编辑锁定中
		l.add(s2);
		SelectTag s3 = new SelectTag(TRANS_STATUS_3, TRANS_STATUS_3_CH);// 开票中
		l.add(s3);
		SelectTag s4 = new SelectTag(TRANS_STATUS_4, TRANS_STATUS_4_CH);// 删除
		l.add(s4);
		SelectTag s5 = new SelectTag(TRANS_STATUS_5, TRANS_STATUS_5_CH);// 被冲账
		l.add(s5);
		SelectTag s6 = new SelectTag(TRANS_STATUS_6, TRANS_STATUS_6_CH);// 已冲账
		l.add(s6);
		SelectTag s7 = new SelectTag(TRANS_STATUS_99, TRANS_STATUS_99_CH);// 开票完成
		l.add(s7);
		SelectTag s8 = new SelectTag(TRANS_STATUS_8, TRANS_STATUS_8_CH);// 开票完成
		l.add(s8);
		return l;
	}

	public static List getYesOrNoListForPageListTrans() {
		List l = new ArrayList();
		SelectTag s0 = new SelectTag(NO, "否");
		l.add(s0);
		SelectTag s1 = new SelectTag(YES, "是");
		l.add(s1);
		return l;
	}

	public static List getCustomerTaxTypePageList() {
		List l = new ArrayList();

		SelectTag s1 = new SelectTag(VAL_CUSTOMER_TAX_TYPE_1,
				VAL_CUSTOMER_TAX_TYPE_1_CH);// 未开票
		l.add(s1);
		SelectTag s2 = new SelectTag(VAL_CUSTOMER_TAX_TYPE_2,
				VAL_CUSTOMER_TAX_TYPE_2_CH);// 开票编辑锁定中
		l.add(s2);
		SelectTag s3 = new SelectTag(VAL_CUSTOMER_TAX_TYPE_3,
				VAL_CUSTOMER_TAX_TYPE_3_CH);// 开票中
		l.add(s3);
		SelectTag s4 = new SelectTag(VAL_CUSTOMER_TAX_TYPE_4,
				VAL_CUSTOMER_TAX_TYPE_4_CH);// 删除
		l.add(s4);
		return l;
	}

	/**
	 * 百分比方式显示数值
	 * 
	 * @param number
	 * @return
	 */
	public static String showPercent(BigDecimal number) {
		String percent = "";
		if (number != null) {
			BigDecimal bdPercent = number.multiply(new BigDecimal(100.00));
			percent = NumberUtils.format(bdPercent, "", 2) + "%";
		}
		return percent;
	}

	/**
	 * 计算税额
	 * 
	 * @param amt
	 *            金额
	 * @param taxRate
	 *            税率
	 * @param formula
	 *            公式
	 * @return BigDecimal
	 */
	public static BigDecimal calculateTaxAmt(BigDecimal amt,
			BigDecimal taxRate, String formula) {
		BigDecimal taxAmt = null;
		if (amt != null && taxRate != null) {
			if (StringUtil.isEmpty(formula)) {
				// 默认计算方式：税额 = 金额 * 税率，保留2位小数
				taxAmt = (amt.multiply(taxRate)).divide((new BigDecimal(1)), 2,
						BigDecimal.ROUND_HALF_UP);
			} else if ("base".equalsIgnoreCase(formula)) {
				// 基本计算方式：税额 = 金额 * 税率
				taxAmt = amt.multiply(taxRate);
			} else {

			}
		}
		return taxAmt;
	}

	public static String getFapiaoTypeName(String getFapiaoType) {
		if ("".equals(getFapiaoType) || null == getFapiaoType) {
			return "";
		} else if ("0".equals(getFapiaoType)) {
			return "增值税专用发票";
		} else if ("1".equals(getFapiaoType)) {
			return "增值税普通发票";
		} else {
			return "其他";
		}
	}

	public static String getIssueTypeName(String issueType) {
		if ("".equals(issueType) || null == issueType) {
			return "";
		} else if ("1".equals(issueType)) {
			return "单笔";
		} else if ("2".equals(issueType)) {
			return "合并";
		} else if ("3".equals(issueType)) {
			return "拆分";
		} else {
			return "";
		}
	}

	/**
	 * @param date
	 * @return 日期格式化
	 */
	public static String getDateFormat(String date) {
		if (StringUtil.isEmpty(date)) {
			return "";
		} else if (date.contains("/")) {
			Date date1 = DateUtils.stringToDate(date, "yyyy/MM/dd");
			date = DateUtils.toString(date1, "yyyy/MM/dd");
			return date.substring(0, 10);
		} else {
			if(date.length() == 8){
				Date date1 = DateUtils.stringToDate(date, "yyyyMMdd");
				date = DateUtils.toString(date1, "yyyy-MM-dd");
				return date.substring(0, 10);
			}
			Date date1 = DateUtils.stringToDate(date, "yyyy-MM-dd");
			date = DateUtils.toString(date1, "yyyy-MM-dd");
			return date.substring(0, 10);
		}

	}

	public static String getTransFlagName(String transFalg) { // 交易标志
																// 1-权责发生/2-实收实付
		if ("".equals(transFalg) || null == transFalg) {
			return "";
		} else if ("1".equals(transFalg)) {
			return "权责发生";
		} else if ("2".equals(transFalg)) {
			return "实收实付";
		} else {
			return "";
		}
	}

	/**
	 * @param dataSource
	 * @return 数据来源的 汉文翻译
	 */
	public static String getdataSourceCh(String dataSource) {
		if ("".equals(dataSource) || null == dataSource) {
			return "";
		} else if ("1".equals(dataSource)) {
			return "手工";
		} else if ("2".equals(dataSource)) {
			return "系统";
		} else {
			return "";
		}
	}

	public static List getdataSourceList() {
		List l = new ArrayList();
		SelectTag s = new SelectTag("1", "手工");
		l.add(s);
		s = new SelectTag("2", "系统");
		l.add(s);
		return l;
	}

	public static List getdataOperationLabelList() {
		List l = new ArrayList();
		// 数据操作标志1-新增2-修改3-删除
		SelectTag s1 = new SelectTag(data_Operation_Label_1,
				data_Operation_Label_add);
		l.add(s1);
		SelectTag s2 = new SelectTag(data_Operation_Label_2,
				data_Operation_Label_update);
		l.add(s2);
		SelectTag s3 = new SelectTag(data_Operation_Label_3,
				data_Operation_Label_delete);
		l.add(s3);

		return l;
	}// 数据审核状态0-待审核1-审核通过2-审核不通过

	public static List getdataAuditstatusList() {
		List l = new ArrayList();
		// /数据审核状态0-待审核1-审核通过2-审核不通过
		SelectTag s1 = new SelectTag(data_audit_status_0,
				data_audit_status_wait);
		l.add(s1);
		SelectTag s2 = new SelectTag(data_audit_status_1, data_audit_status_Yes);
		l.add(s2);
		SelectTag s3 = new SelectTag(data_audit_status_2, data_audit_status_No);
		l.add(s3);

		return l;
	}

	public static final String data_audit_status_0 = "0";
	public static final String data_audit_status_1 = "1";
	public static final String data_audit_status_2 = "2";
	public static final String data_Operation_Label_1 = "1";
	public static final String data_Operation_Label_2 = "2";
	public static final String data_Operation_Label_3 = "3";
	/**
	 * 待审核
	 */
	public static final String data_audit_status_wait = "待审核";
	/**
	 * 审核通过
	 */
	public static final String data_audit_status_Yes = "审核通过";
	/**
	 * 审核不通过
	 * 
	 */
	public static final String data_audit_status_No = "审核不通过";

	/**
	 * 删除
	 */
	public static final String data_Operation_Label_delete = "删除";
	/**
	 * 修改
	 */
	public static final String data_Operation_Label_update = "修改";
	/**
	 * 新增
	 */
	public static final String data_Operation_Label_add = "新增";

	/**
	 * 数据导入 Begin
	 */

	public static final String DATA_STATUS_0 = "0";
	public static final String DATA_STATUS_0_CH = "未校验";
	public static final String DATA_STATUS_1 = "1";
	public static final String DATA_STATUS_1_CH = "校验未通过";
	public static final String DATA_STATUS_2 = "2";
	public static final String DATA_STATUS_2_CH = "校验通过";

	public static final String DATA_D_STATUS_0 = "0";
	public static final String DATA_D_STATUS_0_CH = "成功";
	public static final String DATA_D_STATUS_1 = "1";
	public static final String DATA_D_STATUS_1_CH = "失败";

	public static final String BATCH_STATUS_3 = "3";
	public static final String BATCH_STATUS_3_CH = "待审核";
	public static final String BATCH_STATUS_4 = "4";
	public static final String BATCH_STATUS_4_CH = "审核通过";
	public static final String BATCH_STATUS_5 = "5";
	public static final String BATCH_STATUS_5_CH = "审核未通过";
	public static final String BATCH_STATUS_6 = "6";
	public static final String BATCH_STATUS_6_CH = "审核退回";

	public static String getDataStatus(String dataStatus) {
		if (dataStatus != null && DATA_STATUS_0.equals(dataStatus)) {
			return DATA_STATUS_0_CH;
		} else if (dataStatus != null && DATA_STATUS_1.equals(dataStatus)) {
			return DATA_STATUS_1_CH;
		} else if (dataStatus != null && DATA_STATUS_2.equals(dataStatus)) {
			return DATA_STATUS_2_CH;
		}
		return "";
	}

	public static String getBatchStatus(String batchStatus) {
		if (batchStatus != null && DATA_STATUS_0.equals(batchStatus)) {
			return DATA_STATUS_0_CH;
		} else if (batchStatus != null && DATA_STATUS_1.equals(batchStatus)) {
			return DATA_STATUS_1_CH;
		} else if (batchStatus != null && DATA_STATUS_2.equals(batchStatus)) {
			return DATA_STATUS_2_CH;
		}else if (batchStatus != null && BATCH_STATUS_3.equals(batchStatus)) {
			return BATCH_STATUS_3_CH;
		} else if (batchStatus != null && BATCH_STATUS_4.equals(batchStatus)) {
			return BATCH_STATUS_4_CH;
		} else if (batchStatus != null && BATCH_STATUS_5.equals(batchStatus)) {
			return BATCH_STATUS_5_CH;
		} else if (batchStatus != null && BATCH_STATUS_6.equals(batchStatus)) {
			return BATCH_STATUS_6_CH;
		}
		return "";
	}
	
	
	/***
	 * 发票类型List
	 * @return
	 */
	public static List getFapiaoTypeList(){
		List fapiaoTypeList = new ArrayList();
		SelectTag f0 = new SelectTag("0", "增值税专用发票");
		SelectTag f1 = new SelectTag("1", "增值税普通发票");
		fapiaoTypeList.add(f0);
		fapiaoTypeList.add(f1);
		return fapiaoTypeList;
	}
	
	public static List getBillTransRelateStatusList(){
		List list = new ArrayList();
		SelectTag s1 = new SelectTag("0", "未勾稽");
		SelectTag s2 = new SelectTag("1", "勾稽中");
		SelectTag s3 = new SelectTag("2", "勾稽完成");
		list.add(s1);
		list.add(s2);
		list.add(s3);
		return list;
	}
	
	// 发票状态
	public static List getInputInvoiceDataStatusListNew() {
		List l = new ArrayList();
		SelectTag s = new SelectTag("0", "待认证");
		l.add(s);
		s = new SelectTag("1", "已认证");
		l.add(s);
		s = new SelectTag("2", "已转出");
		l.add(s);
		s = new SelectTag("3", "已抵扣");
		l.add(s);
		s = new SelectTag("4", "已确认视同销售");
		l.add(s);
		
		return l;
	}
	
	public static final String INVOICE_NEW_STATUS_0  = "0";
	public static final String INVOICE_NEW_STATUS_0_CH  = "未认证";
	public static final String INVOICE_NEW_STATUS_1  = "1";
	public static final String INVOICE_NEW_STATUS_1_CH  = "已认证";
	public static final String INVOICE_NEW_STATUS_2 = "2";
	public static final String INVOICE_NEW_STATUS_2_CH  = "已转出";
	public static final String INVOICE_NEW_STATUS_3 = "3";
	public static final String INVOICE_NEW_STATUS_3_CH  = "已抵扣";
	public static final String INVOICE_NEW_STATUS_4 = "4";
	public static final String INVOICE_NEW_STATUS_4_CH  = "已确认视同销售";
	
	public static String getDataStatusCHNew(String datastatus, String bussFlag) {
		if ("INPUT_INVOICE".equalsIgnoreCase(bussFlag)) {
			if (INVOICE_NEW_STATUS_0.equals(datastatus)) {
				return INVOICE_NEW_STATUS_0_CH;
			} else if (INVOICE_NEW_STATUS_1.equals(datastatus)) {
				return INVOICE_NEW_STATUS_1_CH;
			} else if (INVOICE_NEW_STATUS_2.equals(datastatus)) {
				return INVOICE_NEW_STATUS_2_CH;
			} else if (INVOICE_NEW_STATUS_3.equals(datastatus)) {
				return INVOICE_NEW_STATUS_3_CH;
			} else if (INVOICE_NEW_STATUS_4.equals(datastatus)) {
				return INVOICE_NEW_STATUS_4_CH;
			}
		}
		return "";
	}
}
