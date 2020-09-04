package com.cjit.vms.system.model;

public class AccEntry {
	//List
	private String accEntryId;// 分录ID
	private String businessCode;// 交易码
	private String businessCname;// 交易码中文名
	private String gl_code;// 科目编号
	private String isReverse;// 是否冲账
	private String isReverseName;// 是否冲账
	private String currency;// 币种
	private String currencyName;// 币种
	private String cdFlag;// 借贷标识
	private String cdFlagName;// 借贷标识
	private String accTitleCode;// 科目编号
	private String accTitleName;// 科目中文名
	private String transNumTyp;// 取值类型
	private String transNumTypName;// 取值类型

	//form
	private String accTitD;
	private String transNumTypD;
	
	private String accTitC1;
	private String transNumTypC1;
	
	private String accTitC2;
	private String transNumTypC2;
	
	private String accTitB;
	private String transNumTypB;
	
	public String getAccEntryId() {
		return accEntryId;
	}

	public String getAccTitB() {
		return accTitB;
	}

	public void setAccTitB(String accTitB) {
		this.accTitB = accTitB;
	}

	public String getTransNumTypB() {
		return transNumTypB;
	}

	public void setTransNumTypB(String transNumTypB) {
		this.transNumTypB = transNumTypB;
	}

	public void setAccEntryId(String accEntryId) {
		this.accEntryId = accEntryId;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}




	public String getAccTitleCode() {
		return accTitleCode;
	}

	public void setAccTitleCode(String accTitleCode) {
		this.accTitleCode = accTitleCode;
	}

	public String getTransNumTyp() {
		return transNumTyp;
	}

	public void setTransNumTyp(String transNumTyp) {
		this.transNumTyp = transNumTyp;
	}

	public String getAccTitD() {
		return accTitD;
	}

	public void setAccTitD(String accTitD) {
		this.accTitD = accTitD;
	}

	public String getTransNumTypD() {
		return transNumTypD;
	}

	public void setTransNumTypD(String transNumTypD) {
		this.transNumTypD = transNumTypD;
	}

	public String getAccTitC1() {
		return accTitC1;
	}

	public void setAccTitC1(String accTitC1) {
		this.accTitC1 = accTitC1;
	}

	public String getTransNumTypC1() {
		return transNumTypC1;
	}

	public void setTransNumTypC1(String transNumTypC1) {
		this.transNumTypC1 = transNumTypC1;
	}

	public String getAccTitC2() {
		return accTitC2;
	}

	public String getBusinessCname() {
		return businessCname;
	}

	public void setBusinessCname(String businessCname) {
		this.businessCname = businessCname;
	}

	public String getIsReverseName() {
		return isReverseName;
	}

	public void setIsReverseName(String isReverseName) {
		this.isReverseName = isReverseName;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCdFlagName() {
		return cdFlagName;
	}

	public void setCdFlagName(String cdFlagName) {
		this.cdFlagName = cdFlagName;
	}

	public String getAccTitleName() {
		return accTitleName;
	}

	public void setAccTitleName(String accTitleName) {
		this.accTitleName = accTitleName;
	}

	public String getTransNumTypName() {
		return transNumTypName;
	}

	public void setTransNumTypName(String transNumTypName) {
		this.transNumTypName = transNumTypName;
	}

	public void setAccTitC2(String accTitC2) {
		this.accTitC2 = accTitC2;
	}

	public String getTransNumTypC2() {
		return transNumTypC2;
	}

	public String getCdFlag() {
		return cdFlag;
	}

	public void setCdFlag(String cdFlag) {
		this.cdFlag = cdFlag;
	}

	public void setTransNumTypC2(String transNumTypC2) {
		this.transNumTypC2 = transNumTypC2;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getGl_code() {
		return gl_code;
	}

	public void setGl_code(String gl_code) {
		this.gl_code = gl_code;
	}

}
