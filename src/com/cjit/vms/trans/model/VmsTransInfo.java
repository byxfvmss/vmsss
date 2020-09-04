package com.cjit.vms.trans.model;

import java.util.List;
import java.util.Map;

import com.cjit.vms.trans.util.DataUtil;

/**
 * 数据导入情报表
 */
public class VmsTransInfo {
	// VMS_TRANS_BATCH
	private String status; // 状态：0 未校验 | 1 校验未通过 | 2 校验通过 | 3 审核通过
	private String batchId; // 批次 ID
	private String impTime; // 导入时间
	private String impInst; // 导入机构
	private String impUser; // 导入用户
	private String userId; // 当前用户

	// VMS_TRANS_INFO_IMP_DATA
	private String impId; // 逻辑主键
	private String impStatus; // 状态：01 未校验 | 02 校验通过 | 03 校验未通过
	private String customerId; // 客户号
	private String transId; // 交易流水号，与 VMS_TRANS_INFO 表 TRANS_ID 匹配
	private String transDate; // 交易时间
	private String transType; // 交易类型
	private String transAmt; // 交易金额
	private String income;//收入
	private String taxAmt;//税额
	private String taxFlag; // 是否含税，Y 是 | N 否
	private String bankCode; // 交易发生机构
	private String fapiaoType; // 发票类型，0 增值税专用发票 | 1 增值税普通发票
	private String isReverse; // 是否冲账，Y 是 | N 否
	private String reverseTransId; // 原始业务流水号
	private String remark; // 备注
	private String message; // 存储各类信息，如校验信息

	// 是否开票TRANS_FAPIAO_FLAG
	private String transFapiaoType;
	
	private String taxRate;//税率
	private String shortAndOver;//尾差;
	// 辅助字段
	private String count; // 总数
	private String passCount; // 通过数
	private String unPassCount; // 未通过数
	private List authInsts; // 机构列表
	private String startTime;
	private String endTime;
	private String transAmtStart;
	private String transAmtEnd;
	private List statusList;
	private List impStatusList;
	private String dStatus;

	private String[] arry;
	
	//退回原因
	private String reason;
	

	public String getStatusStr() {
		return DataUtil.getBatchStatus(status);
	}
	
	public String getImpStatusStr() {
		return DataUtil.getBatchStatus(impStatus);
	}
	
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String[] getArry() {
		return arry;
	}

	public void setArry(String[] arry) {
		this.arry = arry;
	}



	public String getdStatus() {
		return dStatus;
	}

	public void setdStatus(String dStatus) {
		this.dStatus = dStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getImpTime() {
		return impTime;
	}

	public void setImpTime(String impTime) {
		this.impTime = impTime;
	}

	public String getImpInst() {
		return impInst;
	}

	public void setImpInst(String impInst) {
		this.impInst = impInst;
	}

	public String getImpUser() {
		return impUser;
	}

	public void setImpUser(String impUser) {
		this.impUser = impUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	public String getImpStatus() {
		return impStatus;
	}

	public void setImpStatus(String impStatus) {
		this.impStatus = impStatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getTaxFlag() {
		return taxFlag;
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPassCount() {
		return passCount;
	}

	public void setPassCount(String passCount) {
		this.passCount = passCount;
	}

	public String getUnPassCount() {
		return unPassCount;
	}

	public void setUnPassCount(String unPassCount) {
		this.unPassCount = unPassCount;
	}

	public List getAuthInsts() {
		return authInsts;
	}

	public void setAuthInsts(List authInsts) {
		this.authInsts = authInsts;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTransAmtStart() {
		return transAmtStart;
	}

	public void setTransAmtStart(String transAmtStart) {
		this.transAmtStart = transAmtStart;
	}

	public String getTransAmtEnd() {
		return transAmtEnd;
	}

	public void setTransAmtEnd(String transAmtEnd) {
		this.transAmtEnd = transAmtEnd;
	}

	public List getStatusList() {
		return statusList;
	}

	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}

	public List getImpStatusList() {
		return impStatusList;
	}

	public void setImpStatusList(List impStatusList) {
		this.impStatusList = impStatusList;
	}

	public String getTransFapiaoType() {
		return transFapiaoType;
	}

	public void setTransFapiaoType(String transFapiaoType) {
		this.transFapiaoType = transFapiaoType;
	}

	public VmsTransInfo() {
		super();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getShortAndOver() {
		return shortAndOver;
	}

	public void setShortAndOver(String shortAndOver) {
		this.shortAndOver = shortAndOver;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
}
