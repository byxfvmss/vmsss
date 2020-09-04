package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDiskMonitorInfo {
	private String  taxDiskNo;//税控盘号
	private String  fapiaoType;//发票类型
	private String  billEndDateS;//开票截止日期
	private String  dataRepStrDateS;//数据报送起始日期
	private String  dataRepEndDateS ;//数据报送终止日期
	private BigDecimal  billLimitAmtS ;//单张发票开票金额限额（元）
	private BigDecimal  billLimitAmtPS;//正数发票累计金额限额（元）
	private BigDecimal  billLimitAmtNS;//负数发票累计金额限额（元）
	private String  nBillFlgS ;//负数发票标志
	private String  nBilDayS;//负数发票天数
	private String  newReportDateS ;//最新报税日期
	private BigDecimal  residualCapacityS ;//剩余容量
	private String  uploadDeadlineS ;//上传截止日期
	private String  limitFunctionS;//限定功能标识
	private BigDecimal  offLineDayS;//离线开票时长
	private BigDecimal  offLineBillS ;//离线开票张数
	private BigDecimal  offLineAmtPS;//离线正数累计金额
	private BigDecimal  offLineAmtNS;//离线负数累计金额
	private String  offLineOtsS ;//离线扩展信息
	
	
	private String instId;
	private String taxPerNumber;
	private List lstAuthInstId = new ArrayList();
	
	private String user_id;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	/**
	 * @param taxDiskNo 税控盘号
	 */
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	/**
	 * @param fapiaoType 发票类型
	 */
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getBillEndDateS() {
		return billEndDateS;
	}
	/**
	 * @param billEndDateS 开票截止日期
	 */
	public void setBillEndDateS(String billEndDateS) {
		this.billEndDateS = billEndDateS;
	}
	public String getDataRepStrDateS() {
		return dataRepStrDateS;
	}
	/**
	 * @param dataRepStrDateS 数据报送起始日期
	 */
	public void setDataRepStrDateS(String dataRepStrDateS) {
		this.dataRepStrDateS = dataRepStrDateS;
	}
	public String getDataRepEndDateS() {
		return dataRepEndDateS;
	}
	/**
	 * @param dataRepEndDateS 数据报送终止日期
	 */
	public void setDataRepEndDateS(String dataRepEndDateS) {
		this.dataRepEndDateS = dataRepEndDateS;
	}
	public String getnBillFlgS() {
		return nBillFlgS;
	}
	/**
	 * @param nBillFlgS /负数发票标志
	 */
	public void setnBillFlgS(String nBillFlgS) {
		this.nBillFlgS = nBillFlgS;
	}
	public String getnBilDayS() {
		return nBilDayS;
	}
	/**
	 * @param nBilDayS 负数发票天数
	 */
	public void setnBilDayS(String nBilDayS) {
		this.nBilDayS = nBilDayS;
	}
	public String getNewReportDateS() {
		return newReportDateS;
	}
	/**
	 * @param newReportDateS 最新报税日期
	 */
	public void setNewReportDateS(String newReportDateS) {
		this.newReportDateS = newReportDateS;
	}
	public String getUploadDeadlineS() {
		return uploadDeadlineS;
	}
	/**
	 * @param uploadDeadlineS 上传截止日期
	 */
	public void setUploadDeadlineS(String uploadDeadlineS) {
		this.uploadDeadlineS = uploadDeadlineS;
	}
	public String getLimitFunctionS() {
		return limitFunctionS;
	}
	/**
	 * @param limitFunctionS 限定功能标识
	 */
	public void setLimitFunctionS(String limitFunctionS) {
		this.limitFunctionS = limitFunctionS;
	}
	public String getOffLineOtsS() {
		return offLineOtsS;
	}
	/**
	 * @param offLineOtsS 离线扩展信息
	 */
	public void setOffLineOtsS(String offLineOtsS) {
		this.offLineOtsS = offLineOtsS;
	}
	public BigDecimal getBillLimitAmtS() {
		return billLimitAmtS;
	}
	/**
	 * @param billLimitAmtS 单张发票开票金额限额（元）
	 */
	public void setBillLimitAmtS(BigDecimal billLimitAmtS) {
		this.billLimitAmtS = billLimitAmtS;
	}
	public BigDecimal getBillLimitAmtPS() {
		return billLimitAmtPS;
	}
	/**
	 * @param billLimitAmtPS 正数发票累计金额限额（元）
	 */
	public void setBillLimitAmtPS(BigDecimal billLimitAmtPS) {
		this.billLimitAmtPS = billLimitAmtPS;
	}
	public BigDecimal getBillLimitAmtNS() {
		return billLimitAmtNS;
	}
	/**
	 * @param billLimitAmtNS 负数发票累计金额限额（元）
	 */
	public void setBillLimitAmtNS(BigDecimal billLimitAmtNS) {
		this.billLimitAmtNS = billLimitAmtNS;
	}
	public BigDecimal getResidualCapacityS() {
		return residualCapacityS;
	}
	/**
	 * @param residualCapacityS 剩余容量
	 */
	public void setResidualCapacityS(BigDecimal residualCapacityS) {
		this.residualCapacityS = residualCapacityS;
	}
	public BigDecimal getOffLineDayS() {
		return offLineDayS;
	}
	/**
	 * @param offLineDayS 离线开票时长
	 */
	public void setOffLineDayS(BigDecimal offLineDayS) {
		this.offLineDayS = offLineDayS;
	}
	public BigDecimal getOffLineBillS() {
		return offLineBillS;
	}
	/**
	 * @param offLineBillS 离线开票张数
	 */
	public void setOffLineBillS(BigDecimal offLineBillS) {
		this.offLineBillS = offLineBillS;
	}
	public BigDecimal getOffLineAmtPS() {
		return offLineAmtPS;
	}
	/**
	 * @param offLineAmtPS 离线正数累计金额
	 */
	public void setOffLineAmtPS(BigDecimal offLineAmtPS) {
		this.offLineAmtPS = offLineAmtPS;
	}
	public BigDecimal getOffLineAmtNS() {
		return offLineAmtNS;
	}
	/**
	 * @param offLineAmtNS 离线负数累计金额
	 */
	public void setOffLineAmtNS(BigDecimal offLineAmtNS) {
		this.offLineAmtNS = offLineAmtNS;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getTaxPerNumber() {
		return taxPerNumber;
	}
	/**
	 * @param taxPerNumber
	 */
	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	/**
	 * @param lstAuthInstId
	 */
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
}
