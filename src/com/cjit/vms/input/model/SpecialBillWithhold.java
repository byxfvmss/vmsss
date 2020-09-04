package com.cjit.vms.input.model;

public class SpecialBillWithhold {

	private String billNo;
	private String taxGov;//征收机关
	private String taxNo;//缴款单位-代码(纳税人识别号)
	private String taxInstChn;//缴款单位-全称(代缴人名称)
	private String bankandname;//缴款单位-开户银行
	private String bankandaccount;//缴款单位-账号
	private String subjectId;//预算科目-编码
	private String subjectName;//预算科目-名称
	private String subjectClass;//预算科目-级次
	private String nationalTre;//收款国库
	private String writeData;//填发日期
	private String belongDataS;//税款所属时间-开始日期
	private String belongDataE;//税款所属时间-结束日期
	private String payData;//税款限缴日期
	private String taxAmtSum;//合计税额
	private String dataStatus;//票据状态
	private String remark;//备注
	
	

	public String getTaxGov() {
		return taxGov;
	}
	public void setTaxGov(String taxGov) {
		this.taxGov = taxGov;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getTaxInstChn() {
		return taxInstChn;
	}
	public void setTaxInstChn(String taxInstChn) {
		this.taxInstChn = taxInstChn;
	}
	public String getBankandname() {
		return bankandname;
	}
	public void setBankandname(String bankandname) {
		this.bankandname = bankandname;
	}
	public String getBankandaccount() {
		return bankandaccount;
	}
	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSubjectClass() {
		return subjectClass;
	}
	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}
	public String getNationalTre() {
		return nationalTre;
	}
	public void setNationalTre(String nationalTre) {
		this.nationalTre = nationalTre;
	}
	public String getWriteData() {
		return writeData;
	}
	public void setWriteData(String writeData) {
		this.writeData = writeData;
	}
	public String getBelongDataS() {
		return belongDataS;
	}
	public void setBelongDataS(String belongDataS) {
		this.belongDataS = belongDataS;
	}
	public String getBelongDataE() {
		return belongDataE;
	}
	public void setBelongDataE(String belongDataE) {
		this.belongDataE = belongDataE;
	}
	public String getPayData() {
		return payData;
	}
	public void setPayData(String payData) {
		this.payData = payData;
	}
	public String getTaxAmtSum() {
		return taxAmtSum;
	}
	public void setTaxAmtSum(String taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


}
