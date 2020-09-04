package com.cjit.vms.trans.action.createBill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.model.createBill.TransInfo;

/**
 * 开票申请交易合理性检查结果对象
 * 
 * @author Dylan
 *
 */
public class CheckResult {

	public static final String CHECK_FAIL = "N";// 校验失败
	public static final String CHECK_OK = "Y";// 校验通过

	public static final String INST_UNIQUE = "INST_UNIQUE";
	public static final String INST_UNIQUE_MSG = "交易机构不唯一";
	public static final String MANY_CUSTOMER = "MANY_CUSTOMER";
	public static final String MANY_CUSTOMER_MSG = "所选交易为多个客户";
	public static final String MANY_GOODS_INFO = "MANY_GOODS_INFO";
	public static final String MANY_GOODS_INFO_MSG = "存在对应商品信息有误的交易";
	public static final String MANY_INST = "MANY_INST";
	public static final String MANY_INST_MSG = "所选交易发生在多个机构中";
	public static final String NAME_ILLEGAL = "NAME_ILLEGAL";
	public static final String NAME_ILLEGAL_MSG = "专票客户名称不合法";
	public static final String NO_ADDR_PHONE = "NO_ADDR_PHONE";
	public static final String NO_ADDR_PHONE_MSG = "专票客户地址、电话为空";
	public static final String NO_BANK_ACCOUNT = "NO_BANK_ACCOUNT";
	public static final String NO_BANK_ACCOUNT_MSG = "专票客户银行及帐号为空";
	public static final String NO_CUSTOMER = "NO_CUSTOMER";
	public static final String NO_CUSTOMER_MSG = "存在无客户信息的交易";
	public static final String NO_GOODS_INFO = "NO_GOODS_INFO";
	public static final String NO_GOODS_INFO_MSG = "存在无对应商品信息的交易";
	public static final String NO_TAX_INFO = "NO_TAX_INFO";
	public static final String NO_TAX_INFO_MSG = "存在无对应税目信息的交易";
	public static final String NORMAL_INVOICE = "N";// 普票
	public static final String SPECIAL_INVOICE = "S";// 专票
	public static final String TAXNO_ILLEGAL = "TAXNO_ILLEGAL";
	public static final String TAXNO_ILLEGAL_MSG = "专票客户税号不合法";
	public static final String NO_INST = "NO_INST";
	public static final String NO_INST_TRANS_MSG = "存在无机构信息的交易";
	public static final String DB_NO_INST_DB_MSG = "交易机构信息不存在";
	public static final String NO_TAX_NO = "NO_TAX_NO";
	public static final String NO_TAX_NO_MSG = "存在发生机构无税号的交易";

	private String checkFlag;//
	private String checkResultType;// 校验类型
	private String checkResultMsg;// 标准消息
	private TransInfo transInfo;// 

	public CheckResult() {

	}

	public CheckResult(String checkFlag, String checkResultType,
			String checkResultMsg) {
		this.setCheckFlag(checkFlag);
		this.setCheckResultType(checkResultType);
		this.setCheckResultMsg(checkResultMsg);

	}
	
	public CheckResult(String checkFlag, String checkResultType,
			String checkResultMsg, TransInfo transInfo) {
		this.setCheckFlag(checkFlag);
		this.setCheckResultType(checkResultType);
		this.setCheckResultMsg(checkResultMsg);
		this.setTransInfo(transInfo);

	}
	public CheckResult( String checkResultType,
			String checkResultMsg, TransInfo transInfo) {
		this.setCheckFlag(CHECK_FAIL);
		this.setCheckResultType(checkResultType);
		this.setCheckResultMsg(checkResultMsg);
		this.setTransInfo(transInfo);
	}
	public CheckResult( String checkResultType,
			String checkResultMsg) {
		this.setCheckFlag(CHECK_FAIL);
		this.setCheckResultType(checkResultType);
		this.setCheckResultMsg(checkResultMsg);
		
	}

	
	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getCheckResultType() {
		return checkResultType;
	}

	public void setCheckResultType(String checkResultType) {
		this.checkResultType = checkResultType;
	}

	public String getCheckResultMsg() {
		return checkResultMsg;
	}

	public void setCheckResultMsg(String checkResultMsg) {
		this.checkResultMsg = checkResultMsg;
	}

	public TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	

	

}
