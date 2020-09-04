package com.cjit.vms.metlife.model;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:收费凭证 实体类  metlife
*/
import java.math.BigDecimal;

import com.cjit.vms.trans.util.DataUtil;

public class ChargesVoucherInfo {
	private String vcvId;//序列号
	private String zipCode;//邮编 
	private String feeAdd;//收费地址
	private String cownNum;//投保人姓名
	private String lifCnum;//被保人姓名
	private String instId;//分支机构
	private String channel;//VCV_CHANNEL渠道
	private String cherNum;//VCV_CHERNUM保单号
	private String ttmprcno;//VCV_TTMPRCNO投保单号
	private String planLongDesc;//VCV_LONGDESC主险名称
	private String billFreq;//VCV_BILLFREQ交费频率
	private String feeTyp;//VCV_FEETYP收费项目
	private String instFrom;//VCV_INSTFROM交费起始日期
	private String instTo;//VCV_INSTTO交费终止日期
	private BigDecimal acctAmt;//VCV_ACCTAMT金额
	private String trdt;// VCV_TRDT本期保费到帐日
	private String createDate;//VCV_CREATEDATE	生成日期
	private String znprjTcd;//VCV_ZNPRJTCD项目代码
	private String longDesc;//VCV_LONGDESC项目描述
	private String ccardYn;//VCV_CCARDYN是否为信用卡
	private String trdtBegin;//
	private String trdtEnd;//
	private String loanBank;//VCV_LOANBANK
	private String repNum;//旧保单号VCV_REPNUM
	private String sign;//后续处理类型	VCV_SIGN
	private String reMarks;//后续处理的具体内容	VCV_REMARKS

	  
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getReMarks() {
		return reMarks;
	}
	public void setReMarks(String reMarks) {
		this.reMarks = reMarks;
	}
	public String getRepNum() {
		return repNum;
	}
	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	
	public String getLoanBank() {
		return loanBank;
	}
	public void setLoanBank(String loanBank) {
		this.loanBank = loanBank;
	}
	public String getVcvId() {
		return vcvId;
	}
	public void setVcvId(String vcvId) {
		this.vcvId = vcvId;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getFeeAdd() {
		return feeAdd;
	}
	public void setFeeAdd(String feeAdd) {
		this.feeAdd = feeAdd;
	}
	public String getCownNum() {
		return cownNum;
	}
	public void setCownNum(String cownNum) {
		this.cownNum = cownNum;
	}
	public String getLifCnum() {
		return lifCnum;
	}
	public void setLifCnum(String lifCnum) {
		this.lifCnum = lifCnum;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getChannel() {
		return DataUtil.getChanNel(channel);
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCherNum() {
		return cherNum;
	}
	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
	public String getTtmprcno() {
		return ttmprcno;
	}
	public void setTtmprcno(String ttmprcno) {
		this.ttmprcno = ttmprcno;
	}
	
	public String getPlanLongDesc() {
		return planLongDesc;
	}
	public void setPlanLongDesc(String planLongDesc) {
		this.planLongDesc = planLongDesc;
	}
	public String getBillFreq() {
		return DataUtil.getBillFreq(billFreq);
	}
	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}
	
	public String getFeeTyp() {
		return DataUtil.getFeeTyp(feeTyp);
	}
	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}
	public String getInstFrom() {
		return instFrom;
	}
	public void setInstFrom(String instFrom) {
		this.instFrom = instFrom;
	}
	public String getInstTo() {
		return instTo;
	}
	public void setInstTo(String instTo) {
		this.instTo = instTo;
	}
	public BigDecimal getAcctAmt() {
		return acctAmt;
	}
	public void setAcctAmt(BigDecimal acctAmt) {
		this.acctAmt = acctAmt;
	}
	public String getTrdt() {
		return trdt;
	}
	public void setTrdt(String trdt) {
		this.trdt = trdt;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getZnprjTcd() {
		return znprjTcd;
	}
	public void setZnprjTcd(String znprjTcd) {
		this.znprjTcd = znprjTcd;
	}
	public String getCcardYn() {
		return ccardYn;
	}
	public void setCcardYn(String ccardYn) {
		this.ccardYn = ccardYn;
	}
	public String getTrdtBegin() {
		return trdtBegin;
	}
	public void setTrdtBegin(String trdtBegin) {
		this.trdtBegin = trdtBegin;
	}
	public String getTrdtEnd() {
		return trdtEnd;
	}
	public void setTrdtEnd(String trdtEnd) {
		this.trdtEnd = trdtEnd;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	

}
