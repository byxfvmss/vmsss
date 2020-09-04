package com.cjit.vms.trans.model;

import java.util.List;
import java.util.Map;

/**
 * 数据导入情报表
 */
public class VmsTransInfoStr extends VmsTransInfo {
	// VMS_TRANS_INFO_IMP_DATA
	private String impIdStr; // 逻辑主键
	private String impBatchIdStr; // 导入批次主键
	private String customerIdStr; // 客户号
	private String impStatusStr; // 状态：01 未校验 | 02 校验通过 | 03 校验未通过
	private String transIdStr; // 交易流水号，与 VMS_TRANS_INFO 表 TRANS_ID 匹配
	private String transDateStr; // 交易时间
	private String transTypeStr; // 交易类型
	private String transAmtStr; // 交易金额
	private String taxFlagStr; // 是否含税，Y 是 | N 否
	private String bankCodeStr; // 交易发生机构
	private String fapiaoTypeStr; // 发票类型，0 增值税专用发票 | 1 增值税普通发票
	private String isReverseStr; // 是否冲账，Y 是 | N 否
	private String reverseTransIdStr; // 原始业务流水号
	private String narrative1Str; // 交易描述 1
	private String narrative2Str; // 交易描述 2
	private String messageStr; // 存储各类信息，如校验信息
	private String transFapiaoTypeStr;// 是否开票TRANS_FAPIAO_FLAG
	
	public String getImpIdStr() {
		return impIdStr;
	}
	public void setImpIdStr(String impIdStr) {
		this.impIdStr = impIdStr;
	}
	public String getImpBatchIdStr() {
		return impBatchIdStr;
	}
	public void setImpBatchIdStr(String impBatchIdStr) {
		this.impBatchIdStr = impBatchIdStr;
	}
	public String getCustomerIdStr() {
		return customerIdStr;
	}
	public void setCustomerIdStr(String customerIdStr) {
		this.customerIdStr = customerIdStr;
	}
	public String getImpStatusStr() {
		return impStatusStr;
	}
	public void setImpStatusStr(String impStatusStr) {
		this.impStatusStr = impStatusStr;
	}
	public String getTransIdStr() {
		return transIdStr;
	}
	public void setTransIdStr(String transIdStr) {
		this.transIdStr = transIdStr;
	}
	public String getTransDateStr() {
		return transDateStr;
	}
	public void setTransDateStr(String transDateStr) {
		this.transDateStr = transDateStr;
	}
	public String getTransTypeStr() {
		return transTypeStr;
	}
	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
	}
	public String getTransAmtStr() {
		return transAmtStr;
	}
	public void setTransAmtStr(String transAmtStr) {
		this.transAmtStr = transAmtStr;
	}
	public String getTaxFlagStr() {
		return taxFlagStr;
	}
	public void setTaxFlagStr(String taxFlagStr) {
		this.taxFlagStr = taxFlagStr;
	}
	public String getBankCodeStr() {
		return bankCodeStr;
	}
	public void setBankCodeStr(String bankCodeStr) {
		this.bankCodeStr = bankCodeStr;
	}
	public String getFapiaoTypeStr() {
		return fapiaoTypeStr;
	}
	public void setFapiaoTypeStr(String fapiaoTypeStr) {
		this.fapiaoTypeStr = fapiaoTypeStr;
	}
	public String getIsReverseStr() {
		return isReverseStr;
	}
	public void setIsReverseStr(String isReverseStr) {
		this.isReverseStr = isReverseStr;
	}
	public String getReverseTransIdStr() {
		return reverseTransIdStr;
	}
	public void setReverseTransIdStr(String reverseTransIdStr) {
		this.reverseTransIdStr = reverseTransIdStr;
	}
	public String getNarrative1Str() {
		return narrative1Str;
	}
	public void setNarrative1Str(String narrative1Str) {
		this.narrative1Str = narrative1Str;
	}
	public String getNarrative2Str() {
		return narrative2Str;
	}
	public void setNarrative2Str(String narrative2Str) {
		this.narrative2Str = narrative2Str;
	}
	public String getMessageStr() {
		return messageStr;
	}
	public void setMessageStr(String messageStr) {
		this.messageStr = messageStr;
	}
	public String getTransFapiaoTypeStr() {
		return transFapiaoTypeStr;
	}
	public void setTransFapiaoTypeStr(String transFapiaoTypeStr) {
		this.transFapiaoTypeStr = transFapiaoTypeStr;
	}
}
