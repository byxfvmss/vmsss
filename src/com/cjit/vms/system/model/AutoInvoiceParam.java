package com.cjit.vms.system.model;

/**
 * 自动开票参数
 * 
 * @author Larry
 */
public class AutoInvoiceParam {

	private String paramId;// ID
	private String custTaxNo;// 客户纳税识别号（可为空，即适用于所有客户）
	private String bussType;// 业务种类（对于保险行业之险种）
	private String costType;// 费用类型（首期、续期···）
	private String payFreq;// 缴费频率（月、季···）
	private String invoiceType;// 发票类型（增专、增普···）
	private String remark;// 备注
	private String beginDate;// 生效日期范围
	private String endDate;// 生效日期范围
	private String annual;// 年度（1、2、3、4、5、以上）
	private String periods;// 期数（1、2、3、4、5、6、7、8、9、10、11、12）
	private String specialMark;// 特殊标记（月寄、定期邮寄、平信）
	private String weekYearDay;// 是否启用周年日（Y、N）

	private String custName;// 客户名称

	public AutoInvoiceParam() {

	}

	public AutoInvoiceParam(String paramId) {
		this.paramId = paramId;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getCustTaxNo() {
		return custTaxNo == null ? "" : custTaxNo.trim();
	}

	public void setCustTaxNo(String custTaxNo) {
		this.custTaxNo = custTaxNo;
	}

	public String getBussType() {
		return bussType == null ? "" : bussType.trim();
	}

	public void setBussType(String bussType) {
		this.bussType = bussType;
	}

	public String getCostType() {
		return costType == null ? "" : costType.trim();
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getPayFreq() {
		return payFreq == null ? "" : payFreq.trim();
	}

	public void setPayFreq(String payFreq) {
		this.payFreq = payFreq;
	}

	public String getInvoiceType() {
		return invoiceType == null ? "" : invoiceType.trim();
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getRemark() {
		return remark == null ? "" : remark.trim();
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBeginDate() {
		return beginDate == null ? "" : beginDate.trim();
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate == null ? "" : endDate.trim();
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCustName() {
		return custName == null ? "" : custName.trim();
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getAnnual() {
		return annual == null ? "" : annual.trim();
	}

	public void setAnnual(String annual) {
		this.annual = annual;
	}

	public String getPeriods() {
		return periods == null ? "" : periods.trim();
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getSpecialMark() {
		return specialMark == null ? "" : specialMark.trim();
	}

	public void setSpecialMark(String specialMark) {
		this.specialMark = specialMark;
	}

	public String getWeekYearDay() {
		return weekYearDay == null ? "" : weekYearDay.trim();
	}

	public void setWeekYearDay(String weekYearDay) {
		this.weekYearDay = weekYearDay;
	}
}
