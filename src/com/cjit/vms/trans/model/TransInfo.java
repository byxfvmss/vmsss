package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cjit.crms.util.DateUtil;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.util.DataUtil;

/**
 * 交易信息类
 * 
 * @author Larry
 */
public class TransInfo {

	// 数据库属性
	private String billCode;
	private String billNo;
	private String transId; // TRANS_ID VARCHAR2(50) N 交易ID
	private String transDate; // TRANS_DATE VARCHAR2(20) N 交易时间（MUFG:VALDAT）
	private String transType; // TRANS_TYPE VARCHAR2(50) N 交易类型（MUFG）
	private String customerAccount; // CUSTOMER_ACCOUNT VARCHAR2(50) Y 客户账号
	private String taxFlag; // TAX_FLAG CHAR(1) Y 含税标志 Y-含/N-不含
	private BigDecimal taxRate; // TAX_RATE NUMBER(16,4) Y 税率
	private String isReverse; // IS_REVERSE CHAR(1) Y 是否冲账
	// Y-是/N-否（MUFG:ISREVERSE）
	private String bankCode; // BANK_CODE VARCHAR2(20) Y 交易发生机构（关联我方开票机构信息）
	private String remark; // REMARK VARCHAR2(200) Y 备注
	private String orgCurrCode; // ORG_CURR_CODE VARCHAR2(10) Y 本体交易币种
	private String orgAccNo; // ORG_ACC_NO VARCHAR2(10) Y 本体交易账号
	private BigDecimal amtCcy; // AMT_CCY NUMBER(16,2) N 交易原币金额
	private BigDecimal sumAmt;
	private String reverseTransId; // REVERSE_TRANS_ID VARCHAR2(50) Y 冲账对应交易ID
	private BigDecimal shortAndOver; // SHORT_AND_OVER NUMBER(16,2) Y 尾差
	private BigDecimal balance; // BALANCE NUMBER(16,2) Y 未开票金额（人民币）
	private String instCode; // INSTCODE VARCHAR2(100) Y 所属机构
	private String dataStatus; // DATASTATUS VARCHAR2(2) Y 状态
	private String billDatastatus; // DATASTATUS VARCHAR2(2) Y 状态
	// 1-未开票/2-开票编辑锁定中/3-开票中/4-删除/5-被冲账/6-已冲账/99-开票完成
	private String customerId; // CUSTOMER_ID VARCHAR2(50) Y 客户号
	private BigDecimal amt; // AMT_CNY NUMBER(16,2) Y 金额_人民币
	private BigDecimal taxAmt; // TAX_AMT_CNY NUMBER(16,2) Y 税额_人民币
	private BigDecimal income; // INCOME_CNY NUMBER(16,2) Y 收入_人民币
	private String orgTransType; // ORG_TRANS_TYPE VARCHAR2(10) Y 本体交易类型
	private String orgTransSubType; // ORG_TRANS_SUB_TYPE VARCHAR2(10) Y 本体交易子类型
	private String reverseTransDate; // REVERSE_TRANS_DATE VARCHAR2(20) Y
	// 原始交易日期
	private String reverseTransBudId; // REVERSE_TRANS_BUS_ID VARCHAR2(20) Y
	// 原始交易业务编号
	private String transBusId; // TRANS_BUS_ID VARCHAR2(50) N 交易业务编号
	private String transCurr; // TRANS_CURR VARCHAR2(3) Y 交易币种
	private BigDecimal taxAmtCcy; // TAX_AMT_CCY NUMBER(16,2) Y 交易原币税额
	private BigDecimal incomeCcy; // INCOME_CCY NUMBER(16,2) Y 交易原币收入
	private BigDecimal surtax1; // SURTAX1_AMT_CNY NUMBER(16,2) Y 附加税1（城市建设）金额
	private BigDecimal surtax2; // SURTAX2_AMT_CNY NUMBER(16,2) Y 附加税2（教育附加）金额
	private BigDecimal surtax3; // SURTAX3_AMT_CNY NUMBER(16,2) Y 附加税3（地方教育附加）金额
	private BigDecimal surtax4; // SURTAX4_AMT_CNY NUMBER(16,2) Y 附加税4（其他）金额
	private String transFapiaoFlag; // TRANS_FAPIAO_FLAG VARCHAR2(1) N 交易是否打票
	// A-自动打印/M-手动打印/N-用不打印
	private String fapiaoDate; // FAPIAO_DATE VARCHAR2(20) Y 开票日期
	private String fapiaoType; // FAPIAO_TYPE VARCHAR2(1) N 发票类型
	// 0-增值税专用发票/1-增值税普通发票
	private String transFlag; // TRANS_FLAG VARCHAR2(1) N 交易标志 1-权责发生/2-实收实付
	private String vatRateCode; // VAT_RATE_CODE VARCHAR2(3) N 增值税种类
	private BigDecimal taxCnyBalance;
	// S-6%/Z-0%/F-免税
	// 辅助属性
	private String customerName;// 客户名称
	private String customerTaxno;// 客户纳税人识别号
	private String customerAddress;// 客户地址
	private String customerTel;// 客户电话
	private String customerTaxPayerType; // 客户纳税人类别
	private String customerFaPiaoType;// 发票类型 0-普票 1-专票
	private String customerFaPiaoFlag;// 客户是否打票 A-自动打印 M-手动打印 N-永不打印
	private String transTypeName;// 交易类型名称
	private String billId;// 对应票据ID
	private String transBeginDate;// 交易日期起始
	private String transEndDate;// 交易日期终止
	private List billList; // 对应票据
	private String drawerName;// 开票人姓名
	private String searchFlag;// 查询标志
	private String userId;// 当前用户ID
	private BigDecimal transBillAmt;// 交易对应票据中的金额
	private String instName;// 机构名称
	private String bankTaxperNumber; // 交易发生机构纳税人识别号
	private BigDecimal amtMin;// 交易金额范围
	private BigDecimal amtMax;// 交易金额范围
	private BigDecimal balanceMin;// 未开票金额范围
	private BigDecimal balanceMax;// 未开票金额范围
	private GoodsInfo goodsInfo; // 交易对应商品信息
	private String goodsKey; // 对应商品唯一标识
	private String goodsName; // 对应商品名称
	private String billEndDate;// 开票结束日期
	private String billBeginDate;// 开票开始日期
	private String billDate;// 开票日期
	private BigDecimal tbincomeCny;// czl收入_人民币用于作废
	private String taxId;// 税目信息
	private String customerNationality;// 客户国籍

	private String balancetwo;
	private String amttwo;

	private String amtMin2;// 交易金额范围
	private String amtMax2;// 交易金额范围
	private String balanceMin2;// 未开票金额范围
	private String balanceMax2;// 未开票金额范围

	private String billingTime;
	private String billingBeginTime;
	private String billingEndTime;

	private String instTaxperName; // 机构纳税人名称
	private String instTaxperNumber; // 机构纳税人识别号，即税务登记号
	private String instAccount; // 机构纳税人账号
	private String instTaxAddress; // 机构纳税人地址
	private String instTaxTel; // 机构纳税人电话
	private String instTaxBank; // 机构纳税人开户行
	private String taxType;// 机构纳税人类型

	private String customerType; // 客户类型
	private String ghoClass;
	private String origCapWorkstation;
	private String associateAccountNo;
	private List lstAuthInstId;
	private List customerIds;
	private List transTypeList;
	private Object transCustomerList[];
	private String itemCode; // 科目信息

	private String insureId;
	private String payerId;
	private String vtiCherNum;// 保单号
	private String vtiRepNum;// 旧保单号
	private String vtiTtmprcon;// 投保号
	private String vtiBatctrcde;// 交易描述
	private String vtiFeeTyp;// 费用类型
	private String vtiBillFreq;// 交费频率
	private String vtiHissDte;// 承保日期
	private String vtiDSource;// 数据来源
	private String vtiChannel;// 渠道
	private String isHandwork;// 是否手工录入
	private String vtiHissDteBegin;
	private String vtiHissDteEnd;
	private String noticeNo;

	private String cherNum;// 保单号
	private String repNum;// 旧保单号
	private String ttmpRcno;// 投保单号
	private String feeTyp;// 费用类型
	private String billFreq;// 交费频率
	private Integer polYear;// 保单年度
	private String dsouRce;// 数据来源
	private String chanNel;// 渠道
	private Integer premTerm;// 期数
	private String hissBeginDte;// 开始
	private String hissEndDte;// 结束
	private String altref;// 收入会计科目
	private String[] selectTransIds;

	private String hissDte;// 承保日期
	private String instFrom;// 交费起始日期
	private String instTo;// 交费终止日期
	private String occDate;// 生效日期

	private String dataSources; // 数据来源，99 收入录入
	private String glCode; //总帐科目号

	private String type;

	
	private BigDecimal reverseAmt; // AMT_CNY NUMBER(16,2) Y 金额_人民币
	private BigDecimal reverseTaxAmt; // TAX_AMT_CNY NUMBER(16,2) Y 税额_人民币
	private BigDecimal reverseIncome; // INCOME_CNY NUMBER(16,2) Y 收入_人民币

	public String getChanNelCh(){
		return DataUtil.getChanNel(chanNel);
	}
	public String getFeeTypCh(){
		return DataUtil.getFeeTyp(feeTyp);
	}
	public String getBillFreqCh(){
		return DataUtil.getBillFreq(billFreq);
	}
	public String getDsouRceCh(){
		return DataUtil.getDsouRce(dsouRce);
	}
	public String getDataStatusCH(){
		return DataUtil.getDataStatusCH(dataStatus, "TRANS");
	}
	public List getTransTypeList() {
		return transTypeList;
	}

	public void setTransTypeList(List transTypeList) {
		this.transTypeList = transTypeList;
	}

	public List getLstAuthInstId() {
		return lstAuthInstId;
	}

	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public TransInfo() {

	}

	public String getCustomerNationality() {
		return customerNationality;
	}

	public void setCustomerNationality(String customerNationality) {
		this.customerNationality = customerNationality;
	}

	public TransInfo(String transId) {
		this.transId = transId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getTaxFlag() {
		return taxFlag;
	}

	public String getTaxFlagDesc() {
		if (DataUtil.YES.equals(taxFlag)) {
			return "是";
		} else {
			return "否";
		}
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public BigDecimal getTaxRate() {
		return taxRate == null ? new BigDecimal(0) : taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrgCurrCode() {
		return orgCurrCode;
	}

	public void setOrgCurrCode(String orgCurrCode) {
		this.orgCurrCode = orgCurrCode;
	}

	public BigDecimal getReverseAmt() {
		return reverseAmt;
	}
	public void setReverseAmt(BigDecimal reverseAmt) {
		this.reverseAmt = reverseAmt;
	}
	public BigDecimal getReverseTaxAmt() {
		return reverseTaxAmt;
	}
	public void setReverseTaxAmt(BigDecimal reverseTaxAmt) {
		this.reverseTaxAmt = reverseTaxAmt;
	}
	public BigDecimal getReverseIncome() {
		return reverseIncome;
	}
	public void setReverseIncome(BigDecimal reverseIncome) {
		this.reverseIncome = reverseIncome;
	}
	public String getOrgAccNo() {
		return orgAccNo;
	}

	public void setOrgAccNo(String orgAccNo) {
		this.orgAccNo = orgAccNo;
	}

	public BigDecimal getAmtCcy() {
		return amtCcy == null ? new BigDecimal(0) : amtCcy;
	}

	public void setAmtCcy(BigDecimal amtCcy) {
		this.amtCcy = amtCcy;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	public BigDecimal getShortAndOver() {
		return shortAndOver == null ? new BigDecimal(0) : shortAndOver;
	}

	public void setShortAndOver(BigDecimal shortAndOver) {
		this.shortAndOver = shortAndOver;
	}

	public BigDecimal getBalance() {
		return balance == null ? new BigDecimal(0) : balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getAmt() {
		return amt == null ? new BigDecimal(0) : amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getTaxAmt() {
		return taxAmt == null ? new BigDecimal(0) : taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public BigDecimal getIncome() {
		return income == null ? new BigDecimal(0) : income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public String getOrgTransType() {
		return orgTransType;
	}

	public void setOrgTransType(String orgTransType) {
		this.orgTransType = orgTransType;
	}

	public String getOrgTransSubType() {
		return orgTransSubType;
	}

	public void setOrgTransSubType(String orgTransSubType) {
		this.orgTransSubType = orgTransSubType;
	}

	public String getReverseTransDate() {
		return reverseTransDate;
	}

	public void setReverseTransDate(String reverseTransDate) {
		this.reverseTransDate = reverseTransDate;
	}

	public String getReverseTransBudId() {
		return reverseTransBudId;
	}

	public void setReverseTransBudId(String reverseTransBudId) {
		this.reverseTransBudId = reverseTransBudId;
	}

	public String getTransBusId() {
		return transBusId;
	}

	public void setTransBusId(String transBusId) {
		this.transBusId = transBusId;
	}

	public String getTransCurr() {
		return transCurr;
	}

	public void setTransCurr(String transCurr) {
		this.transCurr = transCurr;
	}

	public BigDecimal getTaxAmtCcy() {
		return taxAmtCcy == null ? new BigDecimal(0) : taxAmtCcy;
	}

	public void setTaxAmtCcy(BigDecimal taxAmtCcy) {
		this.taxAmtCcy = taxAmtCcy;
	}

	public BigDecimal getIncomeCcy() {
		return incomeCcy == null ? new BigDecimal(0) : incomeCcy;
	}

	public void setIncomeCcy(BigDecimal incomeCcy) {
		this.incomeCcy = incomeCcy;
	}

	public BigDecimal getSurtax1() {
		return surtax1 == null ? new BigDecimal(0) : surtax1;
	}

	public void setSurtax1(BigDecimal surtax1) {
		this.surtax1 = surtax1;
	}

	public BigDecimal getSurtax2() {
		return surtax2 == null ? new BigDecimal(0) : surtax2;
	}

	public void setSurtax2(BigDecimal surtax2) {
		this.surtax2 = surtax2;
	}

	public BigDecimal getSurtax3() {
		return surtax3 == null ? new BigDecimal(0) : surtax3;
	}

	public void setSurtax3(BigDecimal surtax3) {
		this.surtax3 = surtax3;
	}

	public BigDecimal getSurtax4() {
		return surtax4 == null ? new BigDecimal(0) : surtax4;
	}

	public void setSurtax4(BigDecimal surtax4) {
		this.surtax4 = surtax4;
	}

	public String getTransFapiaoFlag() {
		return transFapiaoFlag;
	}

	public void setTransFapiaoFlag(String transFapiaoFlag) {
		this.transFapiaoFlag = transFapiaoFlag;
	}

	public String getFapiaoDate() {
		return fapiaoDate;
	}

	public void setFapiaoDate(String fapiaoDate) {
		this.fapiaoDate = fapiaoDate;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getVatRateCode() {
		return vatRateCode;
	}

	public void setVatRateCode(String vatRateCode) {
		this.vatRateCode = vatRateCode;
	}

	public String getCustomerName() {
		if (this.customerName == null) {
			return "";
		}
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

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerTaxPayerType() {
		return customerTaxPayerType;
	}

	public void setCustomerTaxPayerType(String customerTaxPayerType) {
		this.customerTaxPayerType = customerTaxPayerType;
	}

	public String getCustomerFaPiaoType() {
		return customerFaPiaoType;
	}

	public void setCustomerFaPiaoType(String customerFaPiaoType) {
		this.customerFaPiaoType = customerFaPiaoType;
	}

	public String getCustomerFaPiaoFlag() {
		return customerFaPiaoFlag;
	}

	public void setCustomerFaPiaoFlag(String customerFaPiaoFlag) {
		this.customerFaPiaoFlag = customerFaPiaoFlag;
	}

	public String getTransTypeName() {
		if (this.transTypeName == null) {
			return "";
		}
		return transTypeName;
	}

	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
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

	public List getBillList() {
		return billList;
	}

	public void setBillList(List billList) {
		this.billList = billList;
	}

	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
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

	public BigDecimal getTransBillAmt() {
		return transBillAmt == null ? new BigDecimal(0) : transBillAmt;
	}

	public void setTransBillAmt(BigDecimal transBillAmt) {
		this.transBillAmt = transBillAmt;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getBankTaxperNumber() {
		return bankTaxperNumber;
	}

	public void setBankTaxperNumber(String bankTaxperNumber) {
		this.bankTaxperNumber = bankTaxperNumber;
	}

	public BigDecimal getAmtMin() {
		return amtMin;
	}

	public void setAmtMin(BigDecimal amtMin) {
		this.amtMin = amtMin;
	}

	public void setAmtMinStr(String amtMin) {
		if (StringUtil.isNotEmpty(amtMin)) {
			this.amtMin = new BigDecimal(amtMin);
		} else {
			this.amtMin = null;
		}
	}

	public BigDecimal getAmtMax() {
		return amtMax;
	}

	public void setAmtMax(BigDecimal amtMax) {
		this.amtMax = amtMax;
	}

	public void setAmtMaxStr(String amtMax) {
		if (StringUtil.isNotEmpty(amtMax)) {
			this.amtMax = new BigDecimal(amtMax);
		} else {
			this.amtMax = null;
		}
	}

	public BigDecimal getBalanceMin() {
		return balanceMin;
	}

	public void setBalanceMin(BigDecimal balanceMin) {
		this.balanceMin = balanceMin;
	}

	public void setBalanceMinStr(String balanceMin) {
		if (StringUtil.isNotEmpty(balanceMin)) {
			this.balanceMin = new BigDecimal(balanceMin);
		} else {
			this.balanceMin = null;
		}
	}

	public BigDecimal getBalanceMax() {
		return balanceMax;
	}

	public void setBalanceMax(BigDecimal balanceMax) {
		this.balanceMax = balanceMax;
	}

	public void setBalanceMaxStr(String balanceMax) {
		if (StringUtil.isNotEmpty(balanceMax)) {
			this.balanceMax = new BigDecimal(balanceMax);
		} else {
			this.balanceMax = null;
		}
	}

	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public String getGoodsKey() {
		if (StringUtil.isEmpty(goodsKey) && this.goodsInfo != null) {
			// 商品联合主键
			// goodsKey = this.goodsInfo.getGoodsName() + "-"
			// + this.goodsInfo.getGoodsNo() + "-"
			// + this.goodsInfo.getTaxNo();
			goodsKey = goodsInfo.getGoodsName() + "-" + getTaxRate();
		}
		return goodsKey;
	}

	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getTbincomeCny() {
		return tbincomeCny;
	}

	public void setTbincomeCny(BigDecimal tbincomeCny) {
		this.tbincomeCny = tbincomeCny;
	}

	public String getTaxId() {
		return taxId == null ? "" : taxId.trim();
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getBalancetwo() {
		return balancetwo;
	}

	public String getAmttwo() {
		return amttwo;
	}

	public void setBalancetwo(String balancetwo) {
		this.balancetwo = balancetwo;
	}

	public void setAmttwo(String amttwo) {
		this.amttwo = amttwo;
	}

	public BigDecimal getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getAmtMin2() {
		return amtMin2;
	}

	public void setAmtMin2(String amtMin2) {
		this.amtMin2 = amtMin2;
	}

	public String getAmtMax2() {
		return amtMax2;
	}

	public void setAmtMax2(String amtMax2) {
		this.amtMax2 = amtMax2;
	}

	public String getBalanceMin2() {
		return balanceMin2;
	}

	public void setBalanceMin2(String balanceMin2) {
		this.balanceMin2 = balanceMin2;
	}

	public String getBalanceMax2() {
		return balanceMax2;
	}

	public void setBalanceMax2(String balanceMax2) {
		this.balanceMax2 = balanceMax2;
	}

	public String getBillingTime() {
		return billingTime;
	}

	public void setBillingTime(String billingTime) {
		this.billingTime = billingTime;
	}

	public String getBillingBeginTime() {
		return billingBeginTime;
	}

	public void setBillingBeginTime(String billingBeginTime) {
		this.billingBeginTime = billingBeginTime;
	}

	public String getBillingEndTime() {
		return billingEndTime;
	}

	public void setBillingEndTime(String billingEndTime) {
		this.billingEndTime = billingEndTime;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getInstTaxperName() {
		return instTaxperName;
	}

	public void setInstTaxperName(String instTaxperName) {
		this.instTaxperName = instTaxperName;
	}

	public String getInstTaxperNumber() {
		return instTaxperNumber;
	}

	public void setInstTaxperNumber(String instTaxperNumber) {
		this.instTaxperNumber = instTaxperNumber;
	}

	public String getInstAccount() {
		return instAccount;
	}

	public void setInstAccount(String instAccount) {
		this.instAccount = instAccount;
	}

	public String getInstTaxAddress() {
		return instTaxAddress;
	}

	public void setInstTaxAddress(String instTaxAddress) {
		this.instTaxAddress = instTaxAddress;
	}

	public String getInstTaxTel() {
		return instTaxTel;
	}

	public void setInstTaxTel(String instTaxTel) {
		this.instTaxTel = instTaxTel;
	}

	public String getInstTaxBank() {
		return instTaxBank;
	}

	public void setInstTaxBank(String instTaxBank) {
		this.instTaxBank = instTaxBank;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getGhoClass() {
		return ghoClass;
	}

	public void setGhoClass(String ghoClass) {
		this.ghoClass = ghoClass;
	}

	public String getOrigCapWorkstation() {
		return origCapWorkstation;
	}

	public void setOrigCapWorkstation(String origCapWorkstation) {
		this.origCapWorkstation = origCapWorkstation;
	}

	public String getAssociateAccountNo() {
		return associateAccountNo;
	}

	public void setAssociateAccountNo(String associateAccountNo) {
		this.associateAccountNo = associateAccountNo;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public List getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List customerIds) {
		this.customerIds = customerIds;
	}

	public Object[] getTransCustomerList() {
		return transCustomerList;
	}

	public void setTransCustomerList(Object[] transCustomerList) {
		this.transCustomerList = transCustomerList;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Abel 新加大都会保险字段
	 * 
	 * @return
	 * @throws ParseException
	 */

	public String getInsureId() {
		return insureId;
	}

	public void setInsureId(String insureId) {
		this.insureId = insureId;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getVtiCherNum() {
		return vtiCherNum;
	}

	public void setVtiCherNum(String vtiCherNum) {
		this.vtiCherNum = vtiCherNum;
	}

	public String getVtiRepNum() {
		return vtiRepNum;
	}

	public void setVtiRepNum(String vtiRepNum) {
		this.vtiRepNum = vtiRepNum;
	}

	public String getVtiTtmprcon() {
		return vtiTtmprcon;
	}

	public void setVtiTtmprcon(String vtiTtmprcon) {
		this.vtiTtmprcon = vtiTtmprcon;
	}

	public String getVtiBatctrcde() {
		return vtiBatctrcde;
	}

	public void setVtiBatctrcde(String vtiBatctrcde) {
		this.vtiBatctrcde = vtiBatctrcde;
	}

	public String getVtiFeeTyp() {
		return vtiFeeTyp;
	}

	public void setVtiFeeTyp(String vtiFeeTyp) {
		this.vtiFeeTyp = vtiFeeTyp;
	}

	public String getVtiBillFreq() {
		return vtiBillFreq;
	}

	public void setVtiBillFreq(String vtiBillFreq) {
		this.vtiBillFreq = vtiBillFreq;
	}

	public String getVtiHissDte() {
		return vtiHissDte;
	}

	public void setVtiHissDte(String vtiHissDte) {
		this.vtiHissDte = vtiHissDte;
	}

	public String getVtiDSource() {
		return vtiDSource;
	}

	public void setVtiDSource(String vtiDSource) {
		this.vtiDSource = vtiDSource;
	}

	public String getVtiChannel() {
		return vtiChannel;
	}

	public void setVtiChannel(String vtiChannel) {
		this.vtiChannel = vtiChannel;
	}

	public String getIsHandwork() {
		return isHandwork;
	}

	public void setIsHandwork(String isHandwork) {
		this.isHandwork = isHandwork;
	}

	public String getVtiHissDteBegin() {
		return vtiHissDteBegin;
	}

	public void setVtiHissDteBegin(String vtiHissDteBegin) {
		this.vtiHissDteBegin = vtiHissDteBegin;
	}

	public String getVtiHissDteEnd() {
		return vtiHissDteEnd;
	}

	public void setVtiHissDteEnd(String vtiHissDteEnd) {
		this.vtiHissDteEnd = vtiHissDteEnd;
	}

	public String getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getCherNum() {
		if (this.cherNum == null) {
			return "";
		}
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
		if (this.repNum == null) {
			return "";
		}
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
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

	public Integer getPolYear() {
		return polYear;
	}

	public void setPolYear(Integer polYear) {
		this.polYear = polYear;
	}

	public String getDsouRce() {
		return dsouRce;
	}

	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}

	public String getChanNel() {
		return chanNel;
	}

	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}

	public Integer getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(Integer premTerm) {
		this.premTerm = premTerm;
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

	public String getAltref() {
		return altref;
	}

	public void setAltref(String altref) {
		this.altref = altref;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
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

	public String getOccDate() {
		return occDate;
	}

	public void setOccDate(String occDate) {
		this.occDate = occDate;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
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

	public String getBillDatastatus() {
		return billDatastatus;
	}

	public void setBillDatastatus(String billDatastatus) {
		this.billDatastatus = billDatastatus;
	}

	public String getDataSources() {
		return dataSources;
	}

	public void setDataSources(String dataSources) {
		this.dataSources = dataSources;
	}

	public BigDecimal getTaxCnyBalance() {
		return taxCnyBalance == null ? new BigDecimal(0) : taxCnyBalance;
	}

	public void setTaxCnyBalance(BigDecimal taxCnyBalance) {
		this.taxCnyBalance = taxCnyBalance;
	}
	public String getGlCode() {
		return glCode;
	}

	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
