package com.cjit.gjsz.logic;

import com.cjit.gjsz.logic.model.VerifyModel;

public interface DataVerify {

	/**
	 * 操作类型
	 */
	public static final String ACTIONTYPE = "ACTIONTYPE";
	public static final String ACTIONTYPE_A = "A";
	public static final String ACTIONTYPE_C = "C";
	public static final String ACTIONTYPE_D = "D";
	/**
	 * 收款人类型
	 */
	public static final String CUSTYPE = "CUSTYPE";
	/**
	 * 收款人类型1
	 */
	public static final String CUSTYPE1 = "CUSTYPE1";
	/**
	 * 汇款人类型
	 */
	public static final String CUSTYPE2 = "CUSTYPE2";
	/**
	 * 付款人类型
	 */
	public static final String CUSTYPE3 = "CUSTYPE3";
	/**
	 * 个人身份证件号码
	 */
	public static final String IDCODE = "IDCODE";
	/**
	 * 币种类型
	 */
	public static final String CURRENCY = "CURRENCY";
	public static final String CURRENCY_EUR = "EUR"; //欧元
	/**
	 * 国家/地区代码
	 */
	public static final String COUNTRY = "COUNTRY";
	public static final String COUNTRY_CHN = "CHN"; // 中国
	public static final String COUNTRY_DEU = "DEU"; // 德国
	/**
	 * 国际收支交易编码表（收入）
	 */
	public static final String BOP_INCOME = "BOP_INCOME";
	/**
	 * 国际收支交易编码表（支入）
	 */
	public static final String BOP_PAYOUT = "BOP_PAYOUT";
	/**
	 * 行政区划代码
	 */
	public static final String DISTRICTCO = "DISTRICTCO";
	/**
	 * 行业属性代码
	 */
	public static final String INDUSTRY = "INDUSTRY";
	/**
	 * 7.6 经济类型代码表 经济类型代码
	 */
	public static final String ECONOMICTY = "ECONOMICTY"; // cfa
	public static final String ECONOMICTYPE = "ECONOMICTYPE"; // fal
	/**
	 * 收款性质
	 */
	public static final String PAYTYPE = "PAYTYPE";
	/**
	 * 付汇性质
	 */
	public static final String PAYATTR1 = "PAYATTR1";
	/**
	 * 境内收入类型
	 */
	public static final String PAYATTR_D_12 = "PAYATTR_D_12";
	/**
	 * 收款性质2
	 */
	public static final String PAYTYPE2 = "PAYTYPE2";
	/**
	 * 是否类型
	 */
	public static final String ISREF = "ISREF";
	/**
	 * 结汇用途
	 */
	public static final String SETTLEMENT_USERTYPE = "SETTLEMENT_USERTYPE";
	// 
	public static final String SUBID = "SUBID";
	//
	public static final String INNERTABLEID = "INNERTABLEID";
	/**
	 * 16.3 境内主体类型代码表 / 16.4 境外主体类型代码表
	 */
	public static final String MAINBODYTYPE = "MAINBODYTYPE";
	/**
	 * 16.5.1 债务类型
	 */
	public static final String DEBTYPE = "DEBTYPE";
	/**
	 * 16.6.2 担保方式
	 */
	public static final String GUARANTEETYPE = "GUARANTEETYPE";
	/**
	 * 16.7.1 国内外汇贷款类型
	 */
	public static final String DOFOEXLOTYPE = "DOFOEXLOTYPE";
	/**
	 * 16.7.2 国内外汇贷款资金用途类型
	 */
	public static final String USEOFUNDS = "USEOFUNDS";
	/**
	 * 是否类型
	 */
	public static final String YESORNO = "YESORNO";
	public static final String YESORNO_Y = "Y";
	public static final String YESORNO_N = "N";
	public static final String YESORNO_1 = "1";
	public static final String YESORNO_0 = "0";

	// --fal--------------------------------------
	/**
	 * 7.25.1 对外金融资产负债及交易数据表号和表名
	 */
	public static final String FAL_TABLE_INFO = "FAL_TABLE_INFO";
	/**
	 * 7.25.2 境外 （被）投资者所属行业表
	 */
	public static final String INVESTOR_INDUSTRY = "INVESTOR_INDUSTRY";
	/**
	 * 7.25.3 对方与本机构/被代理居民机构/委托人的关系
	 */
	public static final String OPPOSITERELA = "OPPOSITERELA";
	/**
	 * 7.25.4 投资者（被投资者）部门
	 */
	public static final String INVESTORINST = "INVESTORINST";
	/**
	 * 7.25.5 银行卡消费提现类型
	 */
	public static final String BANK_CARD_EXPENSE_TYPE = "BANK_CARD_EXPENSE_TYPE";
	/**
	 * 7.25.6 QFII/RQFII/QDII 投资工具类型
	 */
	public static final String INVESTTOOLTYPE = "INVESTTOOLTYPE";
	/**
	 * 7.25.7 QFII/RQFII 投资品种类型
	 */
	public static final String QFII_RQFII_INVTYPE = "QFII_RQFII_INVTYPE";
	/**
	 * 7.25.8 金融衍生产品的合约类别
	 */
	public static final String DERIVATIVECONTRACTCLASS = "DERIVATIVECONTRACTCLASS";
	/**
	 * 7.25.9 金融衍生产品的风险类别
	 */
	public static final String DERIVATIVERISKCLASS = "DERIVATIVERISKCLASS";
	/**
	 * 7.25.10 QDII 投资品种类型
	 */
	public static final String QDII_INVTYPE = "QDII_INVTYPE";
	/**
	 * 境外投资者与本机构的关系
	 */
	public static final String INVESTORRELA = "INVESTORRELA";
	/**
	 * 保险类别
	 */
	public static final String INSURANCE_TYPE = "INSURANCE_TYPE";
	/**
	 * 出资方式
	 */
	public static final String WAYSOFINVESTMENT = "WAYSOFINVESTMENT";
	/**
	 * 业务类别
	 */
	public static final String BUSITYPE = "BUSITYPE";
	/**
	 * 原始期限
	 */
	public static final String ORIDEADLINE = "ORIDEADLINE";
	/**
	 * 是否委托贷款
	 */
	public static final String IS_ENTRUSTED_LOAN = "IS_ENTRUSTED_LOAN";
	/**
	 * 资产类别
	 */
	public static final String ASSETS_TYPE = "ASSETS_TYPE";
	/**
	 * 风险分类
	 */
	public static final String RISK_CLASS = "RISK_CLASS";
	/**
	 * 相关业务类型
	 */
	public static final String RELEBUSSTYPE = "RELEBUSSTYPE";
	/**
	 * 银行卡清算渠道
	 */
	public static final String BANK_CARD_CLEAR_CHANNEL = "BANK_CARD_CLEAR_CHANNEL";
	/**
	 * 证照类别
	 */
	public static final String LICENSETYPE = "LICENSETYPE";
	/**
	 * 填报单位类型
	 */
	public static final String INSTTYPE = "INSTTYPE";
	/**
	 * 填报机构身份
	 */
	public static final String INST_IDENTITY = "INST_IDENTITY";
	/**
	 * 投资类型
	 */
	public static final String INVESTTYPE = "INVESTTYPE";
	/**
	 * 所投资非居民发行产品类型
	 */
	public static final String INVEST_NON_RESIDENT_ISSUE_PROD = "INVEST_NON_RESIDENT_ISSUE_PROD";
	/**
	 * 有或无
	 */
	public static final String ISORNOT = "ISORNOT";
	/**
	 * 7.25.12 货物、服务、薪资及债务减免等其他各类往来代码表
	 */
	public static final String TRANSACTION_CODE = "TRANSACTION_CODE";
	/**
	 * 7.25.13 银行进出口贸易融资项目指标代码表
	 */
	public static final String TRADE_FINANCE_PROJ_INDI = "TRADE_FINANCE_PROJ_INDI";
	/**
	 * 是否为中国居民
	 */
	public static final String ISCHINESE = "ISCHINESE";
	/**
	 * 投资者属性
	 */
	public static final String INVESTOR_TYPE = "INVESTOR_TYPE";

	public VerifyModel execute();

	public void setVerifyConfig(VerifyConfig vc);
}
