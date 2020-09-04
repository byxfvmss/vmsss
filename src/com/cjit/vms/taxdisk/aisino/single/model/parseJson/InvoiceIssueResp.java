package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

/**
 * 发票开具（返回报文）
 * @author john
 *
 */
public class InvoiceIssueResp extends CommonResp{

	private double InfoAmount;//开具的金额
	private double InfoTaxAmount;//开具的税额
	private String InfoDate;//(格式:yyyy-mm-dd)//开票日期
	private String InfoTypeCode;//开具的发票代码
	private String InfoNumber;//开具的发票号码
	private String hisInfoTypeCode;//上一张发票代码
	private String hisInfoNumber;//上一张发票号码
	private String hisInfoKind;//上一张发票种类
	
	public double getInfoAmount() {
		return InfoAmount;
	}
	public void setInfoAmount(double infoAmount) {
		InfoAmount = infoAmount;
	}
	public double getInfoTaxAmount() {
		return InfoTaxAmount;
	}
	public void setInfoTaxAmount(double infoTaxAmount) {
		InfoTaxAmount = infoTaxAmount;
	}
	public String getInfoDate() {
		return InfoDate;
	}
	public void setInfoDate(String infoDate) {
		InfoDate = infoDate;
	}
	public String getInfoTypeCode() {
		return InfoTypeCode;
	}
	public void setInfoTypeCode(String infoTypeCode) {
		InfoTypeCode = infoTypeCode;
	}
	public String getInfoNumber() {
		return InfoNumber;
	}
	public void setInfoNumber(String infoNumber) {
		InfoNumber = infoNumber;
	}
	public String getHisInfoTypeCode() {
		return hisInfoTypeCode;
	}
	public void setHisInfoTypeCode(String hisInfoTypeCode) {
		this.hisInfoTypeCode = hisInfoTypeCode;
	}
	public String getHisInfoNumber() {
		return hisInfoNumber;
	}
	public void setHisInfoNumber(String hisInfoNumber) {
		this.hisInfoNumber = hisInfoNumber;
	}
	public String getHisInfoKind() {
		return hisInfoKind;
	}
	public void setHisInfoKind(String hisInfoKind) {
		this.hisInfoKind = hisInfoKind;
	}
}
