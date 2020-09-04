package com.cjit.vms.metlife.model;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:会计分录实体类  metlife
*/
import java.math.BigDecimal;
import java.sql.Date;

import com.cjit.vms.trans.util.DataUtil;

public class AccountingEntriesInfo {
	private String billCode;//BILL_CODE
	private String billNo;//BILL_NO
	private String budgetSubjet;//BUDGET_SUBJET
	private String budgetCode;//BUDGET_CODE
	private String budgetCostCenter;//BUDGET_COST_CENTER
	private String budgetCo;//BUDGET_CO
	private String transactionReference;
	private String expenseDocNum;
	private String journalType;
	private String journalSource;
	private String accountingPeriod;
	private String transactionDate;
	private String transactiondescription;
	private String accountCode;
	private String accountName;
	private String currency;
	private BigDecimal transactionAmount;
	private BigDecimal rate;
	private BigDecimal baseAmount;
	private String dc;
	private String la1Fund;
	private String la2Channel;
	private String la3Category;
	private String la5Plan;
	private String la6District;
	private String la7Unit;
	private String la10Branch;
	private String layoutIdenitifier;
	private String subjectType;
	private String mappingStatus;
	private String costRatio;
	private BigDecimal deductionAmount;
	private String vidiDate;
	private String dateBegin;
	private String dateEnd;
	private String status;
	private String transferAmount;
	private String sumdeductionamount;
	private String viadId;//VIAD_ID
	
	//销项
	private String vsadId;//VSAD_ID	序列号
	private String vsadDate;//VSAD_DATE	基准日	
	private String vsadT4;//VSAD_T4	T4	
	private String vsadT8;//VSAD_T8	T8
	private String vsadT9;//VSAD_T9	T9
	private String vsadFlg;//VSAD_FLG 
	private String cherNum;//VSAD_CHERNUM
	private String sumbaseAmount;
	private BigDecimal sumtransactionAmount;
	private String accountPeriodStart;;//VFM_ACCOUNT_PERIOD_START
	
	
	private String la2Channelch;
	
	public String getLa2Channelch() {
		return DataUtil.getChanNel(la2Channel);
	}
	public void setLa2Channelch(String la2Channelch) {
		this.la2Channelch = la2Channelch;
	}
	public String getAccountPeriodStart() {
		return accountPeriodStart;
	}
	public void setAccountPeriodStart(String accountPeriodStart) {
		this.accountPeriodStart = accountPeriodStart;
	}
	public String getSumbaseAmount() {
		return sumbaseAmount;
	}
	public void setSumbaseAmount(String sumbaseAmount) {
		this.sumbaseAmount = sumbaseAmount;
	}
	public BigDecimal getSumtransactionAmount() {
		return sumtransactionAmount;
	}
	public void setSumtransactionAmount(BigDecimal sumtransactionAmount) {
		this.sumtransactionAmount = sumtransactionAmount;
	}
	public String getCherNum() {
		return cherNum;
	}
	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
	public String getVsadId() {
		return vsadId;
	}
	public void setVsadId(String vsadId) {
		this.vsadId = vsadId;
	}
	public String getVsadDate() {
		return vsadDate;
	}
	public void setVsadDate(String vsadDate) {
		this.vsadDate = vsadDate;
	}
	public String getVsadT4() {
		return vsadT4;
	}
	public void setVsadT4(String vsadT4) {
		this.vsadT4 = vsadT4;
	}
	public String getVsadT8() {
		return vsadT8;
	}
	public void setVsadT8(String vsadT8) {
		this.vsadT8 = vsadT8;
	}
	public String getVsadT9() {
		return vsadT9;
	}
	public void setVsadT9(String vsadT9) {
		this.vsadT9 = vsadT9;
	}
	public String getVsadFlg() {
		return vsadFlg;
	}
	public void setVsadFlg(String vsadFlg) {
		this.vsadFlg = vsadFlg;
	}
	public String getViadId() {
		return viadId;
	}
	public void setViadId(String viadId) {
		this.viadId = viadId;
	}
	public String getSumdeductionamount() {
		return sumdeductionamount;
	}
	public void setSumdeductionamount(String sumdeductionamount) {
		this.sumdeductionamount = sumdeductionamount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
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
	public String getBudgetSubjet() {
		return budgetSubjet;
	}
	public void setBudgetSubjet(String budgetSubjet) {
		this.budgetSubjet = budgetSubjet;
	}
	public String getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	public String getBudgetCostCenter() {
		return budgetCostCenter;
	}
	public void setBudgetCostCenter(String budgetCostCenter) {
		this.budgetCostCenter = budgetCostCenter;
	}
	public String getBudgetCo() {
		return budgetCo;
	}
	public void setBudgetCo(String budgetCo) {
		this.budgetCo = budgetCo;
	}
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	public String getExpenseDocNum() {
		return expenseDocNum;
	}
	public void setExpenseDocNum(String expenseDocNum) {
		this.expenseDocNum = expenseDocNum;
	}
	public String getJournalType() {
		return journalType;
	}
	public void setJournalType(String journalType) {
		this.journalType = journalType;
	}
	public String getJournalSource() {
		return journalSource;
	}
	public void setJournalSource(String journalSource) {
		this.journalSource = journalSource;
	}
	public String getAccountingPeriod() {
		return accountingPeriod;
	}
	public void setAccountingPeriod(String accountingPeriod) {
		this.accountingPeriod = accountingPeriod;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getVidiDate() {
		return vidiDate;
	}
	public void setVidiDate(String vidiDate) {
		this.vidiDate = vidiDate;
	}
	public String getTransactiondescription() {
		return transactiondescription;
	}
	public void setTransactiondescription(String transactiondescription) {
		this.transactiondescription = transactiondescription;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	 
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	public BigDecimal getBaseAmount() {
		return baseAmount;
	}
	public void setBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = baseAmount;
	}
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public String getLa1Fund() {
		return la1Fund;
	}
	public void setLa1Fund(String la1Fund) {
		this.la1Fund = la1Fund;
	}
	public String getLa2Channel() {
		return la2Channel;
	}
	public void setLa2Channel(String la2Channel) {
		this.la2Channel = la2Channel;
	}
	public String getLa3Category() {
		return la3Category;
	}
	public void setLa3Category(String la3Category) {
		this.la3Category = la3Category;
	}
	public String getLa5Plan() {
		return la5Plan;
	}
	public void setLa5Plan(String la5Plan) {
		this.la5Plan = la5Plan;
	}
	public String getLa6District() {
		return la6District;
	}
	public void setLa6District(String la6District) {
		this.la6District = la6District;
	}
	public String getLa7Unit() {
		return la7Unit;
	}
	public void setLa7Unit(String la7Unit) {
		this.la7Unit = la7Unit;
	}
	public String getLa10Branch() {
		return la10Branch;
	}
	public void setLa10Branch(String la10Branch) {
		this.la10Branch = la10Branch;
	}
	public String getLayoutIdenitifier() {
		return layoutIdenitifier;
	}
	public void setLayoutIdenitifier(String layoutIdenitifier) {
		this.layoutIdenitifier = layoutIdenitifier;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getMappingStatus() {
		return mappingStatus;
	}
	public void setMappingStatus(String mappingStatus) {
		this.mappingStatus = mappingStatus;
	}
	public String getCostRatio() {
		return costRatio;
	}
	public void setCostRatio(String costRatio) {
		this.costRatio = costRatio;
	}
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}

	
}
