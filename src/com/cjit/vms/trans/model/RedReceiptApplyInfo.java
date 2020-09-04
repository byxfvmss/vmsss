package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.vms.trans.util.DataUtil;

/**
 * @author XHY
 * 
 */
public class RedReceiptApplyInfo {

	private String billId;
	private String transId;
	private String transDate;
	private String billDate;// 开票日期
	private String applyDate;// 申请日期
	private String billCode;// 发票代码
	private String billNo;// 发票号码
	private String oriBillCode;// 原发票代码
	private String oriBillNo;// 原发票号码
	private String customerName;// 客户纳税人名称
	private String customerTaxno;// 客户纳税人识别号
	private String goodsName;// 商品名称
	private String specandmodel;// 规格型号
	private String goodsUnit;// 单位
	private BigDecimal goodsNo;// 商品数量
	private BigDecimal goodsPrice;
	private BigDecimal taxRate;
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private BigDecimal discountRate;// 折扣率
	private String fapiaoType;
	private String datastatus;// 状态
	private String name;
	private String taxno;
	private String customerAccount;
	private String transType;
	private String bankCode;
	private String customerId;
	private String transFlag;
	private String isReverse;
	private String operateStatus;
	private String billBeginDate;// 开票日期起始
	private String billEndDate;// 开票日期终止
	private String billApplyBeginDate;// 申请开票日期起始
	private String billApplyEndDate;// 申请开票日期终止
	private String transBeginDate;
	private String transEndDate;
	private String isHandiwork;// 是否手工录入
	private String issueType;// 开具类型
	private String drawer;//
	private String taxDiskNo;// 税控盘号
	private String machineNo;
	private BigDecimal rate;
	private String datastatusName;
	private String isHandiworkName;
	private String issueTypeName;
	private String fapiaoTypeName;
	private List lstAuthInstId;

	private String reverseBillId;
	private String reverseAmtSum;// 红票对应金额
	private String reverseTaxAmtSum;// 红票对应金额
	private String reverseSumAmt;// 红票对应金额
	
	
	
	//metlife begin
	private String insureId;//INSURE_ID	保单号
	private String repnum;//REPNUM	旧保单号
	private String ttmprcno;//TTMPRCNO	投保单号
	private String feeTyp;//FEETYP	费用类型
	private String billFreq;//BILLFREQ	交费频率
	private String hissDte;//HISSDTE	承保日期
	private String dSource;//DSOURCE	数据来源
	private String channel;//CHANNEL	渠道
	private String hissDteBegin;//检索承保开始日期
	private String hissDteEnd;//
	private String channelch;
	private String feeTypCh;
	
	
	
	public String getFeeTypCh() {
		return DataUtil.getFeeTyp(feeTyp);
	}

	public void setFeeTypCh(String feeTypCh) {
		this.feeTypCh = feeTypCh;
	}

	public String getChannelch() {
		return DataUtil.getChanNel(channel);
	}

	public void setChannelch(String channelch) {
		this.channelch = channelch;
	}

	public String getHissDteBegin() {
		return hissDteBegin;
	}

	public void setHissDteBegin(String hissDteBegin) {
		this.hissDteBegin = hissDteBegin;
	}

	public String getHissDteEnd() {
		return hissDteEnd;
	}

	public void setHissDteEnd(String hissDteEnd) {
		this.hissDteEnd = hissDteEnd;
	}

	public String getInsureId() {
		return insureId;
	}

	public void setInsureId(String insureId) {
		this.insureId = insureId;
	}

	public String getRepnum() {
		return repnum;
	}

	public void setRepnum(String repnum) {
		this.repnum = repnum;
	}

	public String getTtmprcno() {
		return ttmprcno;
	}

	public void setTtmprcno(String ttmprcno) {
		this.ttmprcno = ttmprcno;
	}

	public String getFeeTyp() {
		return feeTyp;
	}

	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}

	public String getBillFreq() {
		return DataUtil.getBillFreq(billFreq);
	}

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

	public String getdSource() {
		return dSource;
	}

	public void setdSource(String dSource) {
		this.dSource = dSource;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	//metlife end
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	// 24：发票类型 来源于VMS_BILL_ITEM_INFO
	// 25：状态 来源于VMS_BILL_ITEM_INFO
	public RedReceiptApplyInfo() {
	}

	public String getMachineNo() {
		return machineNo;
	}

	public String getDatastatusName() {
		return datastatusName;
	}

	public void setDatastatusName(String datastatusName) {
		this.datastatusName = datastatusName;
	}

	public String getIsHandiworkName() {
		return isHandiworkName;
	}

	public void setIsHandiworkName(String isHandiworkName) {
		this.isHandiworkName = isHandiworkName;
	}

	public String getIssueTypeName() {
		return issueTypeName;
	}

	public void setIssueTypeName(String issueTypeName) {
		this.issueTypeName = issueTypeName;
	}

	public String getFapiaoTypeName() {
		return fapiaoTypeName;
	}

	public void setFapiaoTypeName(String fapiaoTypeName) {
		this.fapiaoTypeName = fapiaoTypeName;
	}

	public String getBillApplyBeginDate() {
		return billApplyBeginDate;
	}

	public void setBillApplyBeginDate(String billApplyBeginDate) {
		this.billApplyBeginDate = billApplyBeginDate;
	}

	public String getBillApplyEndDate() {
		return billApplyEndDate;
	}

	public void setBillApplyEndDate(String billApplyEndDate) {
		this.billApplyEndDate = billApplyEndDate;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * @return the billId
	 */
	public String getBillId() {
		return billId;
	}

	/**
	 * @param billId
	 *            the billId to set
	 */
	public void setBillId(String billId) {
		this.billId = billId;
	}

	/**
	 * @return the transId
	 */
	public String getTransId() {
		return transId;
	}

	/**
	 * @param transId
	 *            the transId to set
	 */
	public void setTransId(String transId) {
		this.transId = transId;
	}

	/**
	 * @param transDate
	 *            the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * @return the billDate
	 */
	public String getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate
	 *            the billDate to set
	 */
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	/**
	 * @return the billCode
	 */
	public String getBillCode() {
		return billCode;
	}

	/**
	 * @param billCode
	 *            the billCode to set
	 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo
	 *            the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	public String getReverseBillId() {
		return reverseBillId;
	}

	public void setReverseBillId(String reverseBillId) {
		this.reverseBillId = reverseBillId;
	}

	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the customerTaxno
	 */
	public String getCustomerTaxno() {
		return customerTaxno;
	}

	/**
	 * @param customerTaxno
	 *            the customerTaxno to set
	 */
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public String getReverseAmtSum() {
		return reverseAmtSum;
	}

	public void setReverseAmtSum(String reverseAmtSum) {
		this.reverseAmtSum = reverseAmtSum;
	}

	public String getReverseTaxAmtSum() {
		return reverseTaxAmtSum;
	}

	public void setReverseTaxAmtSum(String reverseTaxAmtSum) {
		this.reverseTaxAmtSum = reverseTaxAmtSum;
	}

	public String getReverseSumAmt() {
		return reverseSumAmt;
	}

	public void setReverseSumAmt(String reverseSumAmt) {
		this.reverseSumAmt = reverseSumAmt;
	}

	/**
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * @param goodsName
	 *            the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * @return the specandmodel
	 */
	public String getSpecandmodel() {
		return specandmodel;
	}

	/**
	 * @param specandmodel
	 *            the specandmodel to set
	 */
	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}

	/**
	 * @return the goodsUnit
	 */
	public String getGoodsUnit() {
		return goodsUnit;
	}

	/**
	 * @param goodsUnit
	 *            the goodsUnit to set
	 */
	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	/**
	 * @return the goodsNo
	 */
	public BigDecimal getGoodsNo() {
		return goodsNo;
	}

	/**
	 * @param goodsNo
	 *            the goodsNo to set
	 */
	public void setGoodsNo(BigDecimal goodsNo) {
		this.goodsNo = goodsNo;
	}

	/**
	 * @return the amtSum
	 */
	public BigDecimal getAmtSum() {
		return amtSum;
	}

	/**
	 * @param amtSum
	 *            the amtSum to set
	 */
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}

	/**
	 * @return the taxAmtSum
	 */
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}

	/**
	 * @param taxAmtSum
	 *            the taxAmtSum to set
	 */
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	/**
	 * @return the sumAmt
	 */
	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	/**
	 * @param sumAmt
	 *            the sumAmt to set
	 */
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	/**
	 * @return the discountRate
	 */
	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate
	 *            the discountRate to set
	 */
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getTransBeginDate() {
		return transBeginDate;
	}

	public void setTransBeginDate(String transBeginDate) {
		this.transBeginDate = transBeginDate;
	}

	public String getTransEndDate() {
		return transEndDate;
	}

	public void setTransEndDate(String transEndDate) {
		this.transEndDate = transEndDate;
	}

	public String getFapiaoType() {
		
		return DataUtil.getFapiaoTypeCH(fapiaoType);
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getDatastatus() {
		return DataUtil.getDataStatusCH(datastatus, "bill");
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getOriBillCode() {
		return oriBillCode;
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo;
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getOperateStatus() {
		return operateStatus;
	}

	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getDrawer() {
		return drawer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/*
	 * public String getDatastatusName() { if(this.datastatus==null){ return "";
	 * } if("1".equals(datastatus)){ return "编辑待提交"; }
	 * if("2".equals(datastatus)){ return "提交待审核"; } if("3".equals(datastatus)){
	 * return "审核通过"; } if("4".equals(datastatus)){ return "无需审核"; }
	 * if("5".equals(datastatus)){ return "已开具"; } if("7".equals(datastatus)){
	 * return "开具失败"; } if("8".equals(datastatus)){ return "已打印"; }
	 * if("9".equals(datastatus)){ return "打印失败"; } if("10".equals(datastatus)){
	 * return "已快递"; } if("11".equals(datastatus)){ return "已签收"; }
	 * if("12".equals(datastatus)){ return "已抄报"; } if("13".equals(datastatus)){
	 * return "作废待审核"; } if("14".equals(datastatus)){ return "作废已审核"; }
	 * if("15".equals(datastatus)){ return "已作废"; } if("16".equals(datastatus)){
	 * return "红冲待审核"; } if("17".equals(datastatus)){ return "红冲已审核"; }
	 * if("18".equals(datastatus)){ return "已红冲"; } if("19".equals(datastatus)){
	 * return "已收回"; } if("99".equals(datastatus)){ return "生效完成"; } return
	 * datastatusName; } //是否手工录入 1-自动开票;2-人工审核;3-人工开票 public String
	 * getIsHandiworkName(){ if(isHandiwork.equals("")){ return ""; }
	 * 
	 * if(isHandiwork.equals("1")){ return "自动开票"; }
	 * if(isHandiwork.equals("2")){ return "人工审核"; }
	 * if(isHandiwork.equals("3")){ return "人工开票"; } return isHandiworkName; }
	 * //开具类型1-单笔;2-合并;3-拆分 public String getIssueTypeName(){
	 * if(issueType.equals("")){ return ""; } if(issueType.equals("1")){ return
	 * "单笔"; } if(issueType.equals("2")){ return "合并"; }
	 * if(issueType.equals("3")){ return "拆分"; } return issueTypeName;
	 * }//FAPIAO_TYPE VARCHAR2(1) N 发票类型 0-增值税专用发票;1-增值税普通发票 public String
	 * getFapiaoTypeName(){ if(fapiaoType.equals("")){ return ""; }
	 * if(fapiaoType.equals("0")){ return "增值税专用发票"; }
	 * if(fapiaoType.equals("1")){ return "增值税普通发票"; } return fapiaoTypeName; }
	 */
}