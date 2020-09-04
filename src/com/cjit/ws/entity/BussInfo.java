package com.cjit.ws.entity;

public class BussInfo {
	
	private String bussinessId;//交易流水号
	
	private String repnum;//业务交易号
	
	private String taxStatus; //发票状态
	
	private String billCode;//发票代码
	
	private String billNo;//发票号码
	
	private String faPiaoType;//发票类型
	
	private String amtCny;
	
	private String taxAmtCny;
	
	private String incomeCny;
	
	private String printDate;
	
	private String printOperater;
	
	private String errorDesc;

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getRepnum() {
		return repnum;
	}

	public void setRepnum(String repnum) {
		this.repnum = repnum;
	}

	public String getTaxStatus() {
		return taxStatus;
	}

	public void setTaxStatus(String taxStatus) {
		this.taxStatus = taxStatus;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getFaPiaoType() {
		return faPiaoType;
	}

	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}

	public String getAmtCny() {
		return amtCny;
	}

	public void setAmtCny(String amtCny) {
		this.amtCny = amtCny;
	}

	public String getTaxAmtCny() {
		return taxAmtCny;
	}

	public void setTaxAmtCny(String taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public String getIncomeCny() {
		return incomeCny;
	}

	public void setIncomeCny(String incomeCny) {
		this.incomeCny = incomeCny;
	}

	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	public String getPrintOperater() {
		return printOperater;
	}

	public void setPrintOperater(String printOperater) {
		this.printOperater = printOperater;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	

}
