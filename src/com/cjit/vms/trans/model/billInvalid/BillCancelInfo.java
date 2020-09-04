package com.cjit.vms.trans.model.billInvalid;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.vms.trans.model.RedReceiptApplyInfo;
import com.cjit.vms.trans.util.DataUtil;

public class BillCancelInfo{
	private String billId;// 票据ID
	private String transDate;// 交易时间
	private String transId;// 交易流水号
	private String billDate;// 开票日期
	private String billCode;// 票据代码
	private String billNo;// 票据号码
	private String customerName;// 客户纳税人名称
	private String customerTaxno;// 客户纳税人识别号
	private String goodsName;// 商品名称
	private String specandmodel;// 规格型号
	private String goodsUnit;// 单位
	private BigDecimal goodsNo;// 商品数量
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private BigDecimal discountRate;// 折扣率
	private String fapiaoType;//发票类型
	private String cancelTime;//流程作废时间
	private String dataStatus;// 状态
	private String applyDate;//申请开票日期
	private String instCode;// 所属机构
	private String cancelInitiator;// 撤销发起人
	private String cancelAuditor;// 撤销审核人
	private String operateStatus;// 原票据状态(13-作废待审核,14-作废已审核,15-已作废)
	private String billBeginDate;// 开票日期起始
	private String billEndDate;// 开票日期终止
	private String transBeginDate;// 交易日期起始
	private String transEndDate;// 交易日期终止
	private String drawer;// 开票人(数据库存储用户ID)
	private String reviewer;// 复核人(数据库存储人员ID)
	private String payee;// 收款人(数据库存储人员姓名)
	private String taxDiskNo;//税控盘号
	private String machineNo;// 开票机号
	private BigDecimal taxRate; //税率
	private String isHandiwork;// 是否手工录入 1-自动开票;2-人工审核;3-人工开票
	private String issueType;//开具类型1-单笔;2-合并;3-拆分
	private String searchFlag;// 查询标志
	private String customerId;// 客户号
	
	private String userId;// 用户ID
	private List lstAuthInstId;
	
	
	private String insureId;//INSURE_ID	保单号
	private String cherNum;//INSURE_ID	保单号
	private String repnum;//REPNUM	旧保单号
	private String ttmpRcno;//TTMPRCNO	投保单号
	private String feeTyp;//FEETYP	费用类型
	private String billFreq;//BILLFREQ	交费频率
	private String hissDte;//HISSDTE	承保日期
	private String dSource;//DSOURCE	数据来源
	private String channel;//CHANNEL	渠道
	private String hissBeginDte;//检索承保开始日期
	private String hissEndDte;//
	
		
	public String getChannelCh(){
		return DataUtil.getChanNel(channel);
	}
	public String getBillFreqCh(){
			return DataUtil.getBillFreq(billFreq);
		}
	public String getdSourceCh(){
			return DataUtil.getDsouRce(dSource);
		}
	public String getFeeTypCh(){
			return DataUtil.getFeeTyp(feeTyp);
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
	public String getTtmpRcno() {
		return ttmpRcno;
	}
	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}
	public String getFeeTyp() {
		return feeTyp;
	}
	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}
	public String getBillFreq() {
		return billFreq;
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
	public String getHissBeginDte() {
		return hissBeginDte;
	}

	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}

	public String getHissEndDte() {
		return hissEndDte;
	}

	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 


	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerTaxno() {
		return customerTaxno;
	}
	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getSpecandmodel() {
		return specandmodel;
	}
	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}
	public String getGoodsUnit() {
		return goodsUnit;
	}
	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	public BigDecimal getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(BigDecimal goodsNo) {
		this.goodsNo = goodsNo;
	}
	public BigDecimal getAmtSum() {
		return amtSum;
	}
	public void setAmtSum(BigDecimal amtSum) {
		this.amtSum = amtSum;
	}
	public BigDecimal getTaxAmtSum() {
		return taxAmtSum;
	}
	public void setTaxAmtSum(BigDecimal taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}
	public BigDecimal getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getCancelInitiator() {
		return cancelInitiator;
	}
	public void setCancelInitiator(String cancelInitiator) {
		this.cancelInitiator = cancelInitiator;
	}
	public String getCancelAuditor() {
		return cancelAuditor;
	}
	public void setCancelAuditor(String cancelAuditor) {
		this.cancelAuditor = cancelAuditor;
	}
	public String getOperateStatus() {
		return operateStatus;
	}
	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
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
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getMachineNo() {
		return machineNo;
	}
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public String getIsHandiwork() {
		return isHandiwork;
	}
	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}
	public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getSearchFlag() {
		return searchFlag;
	}
	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
}
