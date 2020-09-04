package com.cjit.vms.input.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 进项发票信息类
 * 
 * @author 中科软王春燕
 */
public class InputInvoiceNew
{
	private String instId;// 机构代码

	private String instName;// 机构名称

	private String billCode;// 发票代码

	private String billNo;// 发票号码

	private String industryType;// 行业类型
	
	private String porpuseCode ;  //用途
	
	private String porpuse;  //用途名称
	
	private String vendorName;// 供应商名称
	
	private String taxNo ;  //  供应商纳税人识别号
	
	private BigDecimal amt;// 金额

	private BigDecimal taxRate;// 税率

	private BigDecimal taxAmt;// 税额

	private BigDecimal amtTaxSum;// 发票总额

	private String billDate;// 开票日期

	private String dataStatus;// 是否认证通过
	
	private String directionId ;  //   科目明细编码
	
	private String directionName ; //  科目明细名称
	
	private BigDecimal transferAmt;// 转出金额
	
	private BigDecimal transferRatio;// 转出比例
	
	private BigDecimal unTransferAmt;// 未转出金额
	
	private String transferStatus;// 转出状态
	
	private String dataSource;// 数据来源

	private String deducDate;// 抵扣日期

//	private String ifEstateDeduc;// 是否属于购建不动产并以此性质抵扣的进项
	
	private String flag;  //  修改状态
	
	private String temp1;  // 预留字段1
	
	private String temp2;  // 预留字段2
	
	private String temp3;  // 预留字段3
	
	private String temp4;  // 预留字段4
	
	private String temp5;  // 预留字段5
	
	private String billType;  // 发票类型

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	private BigDecimal deducRatio;// 抵扣比例

	private BigDecimal deducAmt;// 抵扣金额

	private List lstAuthInstId;
	private String userId;// 当前用户ID
	// --------------------非数据库字段-----------------------
	private String deducBeginDate; // 抵扣日期起始日
	private String deducEndDate; // 抵扣日期结束日

	public List getLstAuthInstId()
	{
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId)
	{
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getInstId()
	{
		return instId;
	}

	public void setInstId(String instId)
	{
		this.instId = instId;
	}

	public String getInstName()
	{
		return instName;
	}

	public void setInstName(String instName)
	{
		this.instName = instName;
	}

	public String getBillCode()
	{
		return billCode;
	}

	public void setBillCode(String billCode)
	{
		this.billCode = billCode;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getIndustryType()
	{
		return industryType;
	}

	public void setIndustryType(String industryType)
	{
		this.industryType = industryType;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}

	public BigDecimal getAmt()
	{
		return amt;
	}

	public void setAmt(BigDecimal amt)
	{
		this.amt = amt;
	}

	public BigDecimal getTaxRate()
	{
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate)
	{
		this.taxRate = taxRate;
	}

	public BigDecimal getTaxAmt()
	{
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt)
	{
		this.taxAmt = taxAmt;
	}

	public BigDecimal getAmtTaxSum()
	{
		return amtTaxSum;
	}

	public void setAmtTaxSum(BigDecimal amtTaxSum)
	{
		this.amtTaxSum = amtTaxSum;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getDataStatus()
	{
		return dataStatus;
	}

	public void setDataStatus(String dataStatus)
	{
		this.dataStatus = dataStatus;
	}

	public String getDeducDate()
	{
		return deducDate;
	}

	public void setDeducDate(String deducDate)
	{
		this.deducDate = deducDate;
	}



//	public String getIfEstateDeduc()
//	{
//		return ifEstateDeduc;
//	}
//
//	public void setIfEstateDeduc(String ifEstateDeduc)
//	{
//		this.ifEstateDeduc = ifEstateDeduc;
//	}

	public BigDecimal getDeducRatio()
	{
		return deducRatio;
	}

	public void setDeducRatio(BigDecimal deducRatio)
	{
		this.deducRatio = deducRatio;
	}

	public BigDecimal getDeducAmt()
	{
		return deducAmt;
	}

	public void setDeducAmt(BigDecimal deducAmt)
	{
		this.deducAmt = deducAmt;
	}

	public String getDeducBeginDate()
	{
		return deducBeginDate;
	}

	public void setDeducBeginDate(String deducBeginDate)
	{
		this.deducBeginDate = deducBeginDate;
	}

	public String getDeducEndDate()
	{
		return deducEndDate;
	}

	public void setDeducEndDate(String deducEndDate)
	{
		this.deducEndDate = deducEndDate;
	}

	public String getPorpuseCode() {
		return porpuseCode;
	}

	public void setPorpuseCode(String porpuseCode) {
		this.porpuseCode = porpuseCode;
	}

	public String getPorpuse() {
		return porpuse;
	}

	public void setPorpuse(String porpuse) {
		this.porpuse = porpuse;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getDirectionId() {
		return directionId;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	public String getDirectionName() {
		return directionName;
	}

	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}

	public BigDecimal getTransferAmt() {
		return transferAmt;
	}

	public void setTransferAmt(BigDecimal transferAmt) {
		this.transferAmt = transferAmt;
	}

	public BigDecimal getTransferRatio() {
		return transferRatio;
	}

	public void setTransferRatio(BigDecimal transferRatio) {
		this.transferRatio = transferRatio;
	}

	public BigDecimal getUnTransferAmt() {
		return unTransferAmt;
	}

	public void setUnTransferAmt(BigDecimal unTransferAmt) {
		this.unTransferAmt = unTransferAmt;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getTemp3() {
		return temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}

	public String getTemp4() {
		return temp4;
	}

	public void setTemp4(String temp4) {
		this.temp4 = temp4;
	}

	public String getTemp5() {
		return temp5;
	}

	public void setTemp5(String temp5) {
		this.temp5 = temp5;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	

}
