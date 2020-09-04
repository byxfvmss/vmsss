package com.cjit.vms.trans.model.createBill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cjit.crms.util.DateUtil;

import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.util.DataUtil;

public class TransInfo {

	// 交易基础信息
	private String transId; // 交易ID
	private String transDate; // 交易时间（MUFG:VALDAT）
	private String transType; // 交易类型（MUFG）
	private String customerAccount; // 客户账号
	private String customerName;
	private String taxFlag; // 含税标志 Y-含/N-不含
	private BigDecimal taxRate; // 税率
	private String isReverse; // 是否冲账 Y-是/N-否（MUFG:ISREVERSE）
	private String bankCode; // 交易发生机构（关联我方开票机构信息）
	private String remark; // 备注
	private String orgCurrCode; // 本体交易币种
	private String orgAccNo; // 本体交易账号
	private BigDecimal amtCcy; // 交易原币金额
	private String reverseTransId; // 冲账对应交易ID
	private BigDecimal shortAndOver; // 尾差
	private BigDecimal balance; // 未开票金额（人民币）
	private BigDecimal taxCnyBalance; // 未开票税额（人民币）
	private String instCode; // 所属机构
	private String datastatus; // 状态
	private String customerId; // 客户号
	private BigDecimal amtCny; // 金额_人民币
	private BigDecimal taxAmtCny; // 税额_误差_人民币
	private BigDecimal incomeCny; // 收入_人民币
	private String orgTransType; // 本体交易类型
	private String orgTransSubType; // 本体交易子类型
	private String reverseTransDate; // 原始交易日期
	private String reverseTransBusId; // 原始交易业务编号
	private String transBusId; // 交易业务编号
	private String transCurr; // 交易币种
	private BigDecimal taxAmtCcy; // 交易原币税额
	private BigDecimal incomeCcy; // 交易原币收入
	private BigDecimal surtax1AmtCny; // 附加税1（城市建设）金额
	private BigDecimal surtax2AmtCny; // 附加税2（教育附加）金额
	private BigDecimal surtax3AmtCny; // 附加税3（地方教育附加）金额
	private BigDecimal surtax4AmtCny; // 附加税4（其他）金额
	private String transFapiaoFlag; // 交易是否打票 A-自动打印/M-手动打印/N-用不打印
	private String fapiaoDate; // 开票日期
	private String fapiaoType; // 发票类型 0-增值税专用发票/1-增值税普通发票
	private String transFlag; // 交易标志 1-权责发生/2-实收实付
	private String vatRateCode; // 增值税种类 S-6%/Z-0%/F-免税
	private String billingTime; // 开票时点
	private String associateAccountNo; // 交易账号
	private String origCapWorkstation; // 本体交易捕获终端号
	private String itemCode; // 交易认定类型
	private String instname; // 机构名称
	private String dataSources; // 数据来源，99 手工录入
	private String glCode; //总帐科目号
	// ----------------------------------------------------------------
	// 查询附加交易信息
	// private String transTypeName;
	// //
	// private String customerTaxPayerType;
	// private String customerFaPiaoType;
	// //private String customerTaxno;
	// private String customerTel;
	// private String customerNationality;
	// private String customerName;
	// private String customerAddress;
	// --------------------------------------------------------------------
	// 处理流程信息
	private String transClass;// 交易处理分类
	private String transIdx;// 交易序号

	// 附加商品信息
	private String goodsId;// 商品ID

	// 附加销方信息
	private String taxNo;// 所属机构税号

	// 附加购方信息
	private String customerTaxno;// 客户税号
	//2019
	private String payee;
	private String reviewer;
	private String  drawer;
	
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getDrawer() {
		return drawer;
	}
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	// 机构信息
	private Organization organization;
	// 客户信息
	private Customer customer;
	// 商品（交易类型中包含）
	private VerificationInfo verificationInfo;
	// 税控对应税率ID
	private VmsTaxInfo taxinfo;

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

	private String planLongDesc;//主险名称

	private String premTermArrayMin;

	private String premTermArrayMax;

	private String hisToryFlag;

	private String insureId;

	private BigDecimal reverseAmt; // AMT_CNY NUMBER(16,2) Y 金额_人民币
	private BigDecimal reverseTaxAmt; // TAX_AMT_CNY NUMBER(16,2) Y 税额_人民币
	private BigDecimal reverseIncome; // INCOME_CNY NUMBER(16,2) Y 收入_人民币
	
	
	private String mergeFlag;

	public String getMergeFlag() {
		return mergeFlag;
	}
	public void setMergeFlag(String mergeFlag) {
		this.mergeFlag = mergeFlag;
	}
	
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
	public BigDecimal getBalanceIncomeCny() {
		BigDecimal a = getBalance();
		BigDecimal b = getTaxCnyBalance();
		BigDecimal c = a.subtract(b);
		return c;
	}

	public String getTransId() {
		return transId;
	}

	public String getTransDate() {
		return transDate;
	}

	public String getTransType() {
		return transType;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public String getTaxFlag() {
		return taxFlag;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public String getIsReverse() {
		return isReverse;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getRemark() {
		return remark;
	}

	public String getOrgCurrCode() {
		return orgCurrCode;
	}

	public String getOrgAccNo() {
		return orgAccNo;
	}

	public BigDecimal getAmtCcy() {
		return amtCcy;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public BigDecimal getShortAndOver() {
		return shortAndOver;
	}

	public BigDecimal getBalance() {
		if (null == balance) {
			balance = new BigDecimal("0");
		}
		return balance;
	}

	public BigDecimal getTaxCnyBalance() {
		if (null == taxCnyBalance) {
			taxCnyBalance = new BigDecimal("0");
		}
		return taxCnyBalance;
	}

	public String getInstCode() {
		return instCode;
	}

	public String getDatastatus() {
		return datastatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public BigDecimal getAmtCny() {
		return amtCny;
	}

	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}

	public BigDecimal getIncomeCny() {
		return incomeCny;
	}

	public String getOrgTransType() {
		return orgTransType;
	}

	public String getOrgTransSubType() {
		return orgTransSubType;
	}

	public String getReverseTransDate() {
		return reverseTransDate;
	}

	public String getReverseTransBusId() {
		return reverseTransBusId;
	}

	public String getTransBusId() {
		return transBusId;
	}

	public String getTransCurr() {
		return transCurr;
	}

	public BigDecimal getTaxAmtCcy() {
		return taxAmtCcy;
	}

	public BigDecimal getIncomeCcy() {
		return incomeCcy;
	}

	public BigDecimal getSurtax1AmtCny() {
		return surtax1AmtCny;
	}

	public BigDecimal getSurtax2AmtCny() {
		return surtax2AmtCny;
	}

	public BigDecimal getSurtax3AmtCny() {
		return surtax3AmtCny;
	}

	public BigDecimal getSurtax4AmtCny() {
		return surtax4AmtCny;
	}

	public String getTransFapiaoFlag() {
		return transFapiaoFlag;
	}

	public String getFapiaoDate() {
		return fapiaoDate;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public String getVatRateCode() {
		return vatRateCode;
	}

	public String getBillingTime() {
		return billingTime;
	}

	public String getAssociateAccountNo() {
		return associateAccountNo;
	}

	public String getOrigCapWorkstation() {
		return origCapWorkstation;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getInstname() {
		return instname;
	}

	public String getDataSources() {
		return dataSources;
	}

	public String getTransClass() {
		return transClass;
	}

	public String getTransIdx() {
		return transIdx;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public Organization getOrganization() {
		return organization;
	}

	public Customer getCustomer() {
		return customer;
	}

	public VerificationInfo getVerificationInfo() {
		return verificationInfo;
	}

	public VmsTaxInfo getTaxinfo() {
		return taxinfo;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setOrgCurrCode(String orgCurrCode) {
		this.orgCurrCode = orgCurrCode;
	}

	public void setOrgAccNo(String orgAccNo) {
		this.orgAccNo = orgAccNo;
	}

	public void setAmtCcy(BigDecimal amtCcy) {
		this.amtCcy = amtCcy;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	public void setShortAndOver(BigDecimal shortAndOver) {
		this.shortAndOver = shortAndOver;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setTaxCnyBalance(BigDecimal taxCnyBalance) {
		this.taxCnyBalance = taxCnyBalance;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public void setDatastatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}

	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}

	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}

	public void setOrgTransType(String orgTransType) {
		this.orgTransType = orgTransType;
	}

	public void setOrgTransSubType(String orgTransSubType) {
		this.orgTransSubType = orgTransSubType;
	}

	public void setReverseTransDate(String reverseTransDate) {
		this.reverseTransDate = reverseTransDate;
	}

	public void setReverseTransBusId(String reverseTransBusId) {
		this.reverseTransBusId = reverseTransBusId;
	}

	public void setTransBusId(String transBusId) {
		this.transBusId = transBusId;
	}

	public void setTransCurr(String transCurr) {
		this.transCurr = transCurr;
	}

	public void setTaxAmtCcy(BigDecimal taxAmtCcy) {
		this.taxAmtCcy = taxAmtCcy;
	}

	public void setIncomeCcy(BigDecimal incomeCcy) {
		this.incomeCcy = incomeCcy;
	}

	public void setSurtax1AmtCny(BigDecimal surtax1AmtCny) {
		this.surtax1AmtCny = surtax1AmtCny;
	}

	public void setSurtax2AmtCny(BigDecimal surtax2AmtCny) {
		this.surtax2AmtCny = surtax2AmtCny;
	}

	public void setSurtax3AmtCny(BigDecimal surtax3AmtCny) {
		this.surtax3AmtCny = surtax3AmtCny;
	}

	public void setSurtax4AmtCny(BigDecimal surtax4AmtCny) {
		this.surtax4AmtCny = surtax4AmtCny;
	}

	public void setTransFapiaoFlag(String transFapiaoFlag) {
		this.transFapiaoFlag = transFapiaoFlag;
	}

	public void setFapiaoDate(String fapiaoDate) {
		this.fapiaoDate = fapiaoDate;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public void setVatRateCode(String vatRateCode) {
		this.vatRateCode = vatRateCode;
	}

	public void setBillingTime(String billingTime) {
		this.billingTime = billingTime;
	}

	public void setAssociateAccountNo(String associateAccountNo) {
		this.associateAccountNo = associateAccountNo;
	}

	public void setOrigCapWorkstation(String origCapWorkstation) {
		this.origCapWorkstation = origCapWorkstation;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setInstname(String instname) {
		this.instname = instname;
	}

	public void setDataSources(String dataSources) {
		this.dataSources = dataSources;
	}

	public void setTransClass(String transClass) {
		this.transClass = transClass;
	}

	public void setTransIdx(String transIdx) {
		this.transIdx = transIdx;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void setVerificationInfo(VerificationInfo verificationInfo) {
		this.verificationInfo = verificationInfo;
	}

	public void setTaxinfo(VmsTaxInfo taxinfo) {
		this.taxinfo = taxinfo;
	}

	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
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

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
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

	public String getPlanLongDesc() {
		return planLongDesc;
	}

	public void setPlanLongDesc(String planLongDesc) {
		this.planLongDesc = planLongDesc;
	}

	public String getPremTermArrayMin() {
		return premTermArrayMin;
	}

	public void setPremTermArrayMin(String premTermArrayMin) {
		this.premTermArrayMin = premTermArrayMin;
	}

	public String getPremTermArrayMax() {
		return premTermArrayMax;
	}

	public void setPremTermArrayMax(String premTermArrayMax) {
		this.premTermArrayMax = premTermArrayMax;
	}

	public String getHisToryFlag() {
		return hisToryFlag;
	}

	public void setHisToryFlag(String hisToryFlag) {
		this.hisToryFlag = hisToryFlag;
	}

	public String getInsureId() {
		return insureId;
	}

	public void setInsureId(String insureId) {
		this.insureId = insureId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}