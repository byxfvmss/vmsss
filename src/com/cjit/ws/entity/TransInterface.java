package com.cjit.ws.entity;

public class TransInterface {
	private String receiveId;
	private String receiveTime;
	private String businessId;  //交易ID
	private String cherNum;  //保单号,固定保单号（个人）,团单是团单合同号码
	private String payNo; //PAYNO收费号码,收费对应核心系统的实收号码,付费对应核心的实付号码
	private String ttmprcNo; //TTMPRCNO投保单号
	private String origcurr; //ORIGCURR交易币种
	private String taxRate;   //TAX_RATE税率
	private String origAmt; //ORIG_AMT价税合计金额(原币)
	private String origIncome; //ORIG_INCOME收入金额(原币)
	private String origTaxAmt;  //ORIG_TAX_AMT税额(原币)
	private String amtCny;  //AMT_CNY价税合计金额(本币)
	private String incomeCny;  //INCOME_CNY收入金额(本币)
	private String taxAmtCny;  //TAX_AMT_CNY税额(本币)
	private String traDt;  //TRA_DT交易日期,业务生效日期,新契约,保全,续期
	private String traTyp;  //TRA_TYP该笔交易对应在业务系统de交易类型
	private String batctrcde;  //BATCTRCDE对该笔交易的详细描述
	private String traBra;  //TRA_BRA交易发生机构
	private String invTyp;  //INVTYP发票类型,0-增值税专用发票,1-增值税普通发票,用于识别开具增值税专用发票,可通过交易认定规则判定
	private String billFreq;  //BILLFREQ交费频率,A-月交B-季交C-半年交D-年交E-趸交F-不定期
	private String polYear;  //POLYEAR保单年度
	private String hissDte ; //HISSDTE承保日期
	private String dsource;  //DSOURCE数据来源
	private String productCod;  //PRODUCT_COD产品代码
	private String productName;  //PRODUCT_NAME产品名称
	private String insCod; //INS_COD险别,A万能险B投连险C传统寿险D分红寿险E健康险F意外险G其他
	private String instFrom;  //INSTFROM交费起始日期
	private String instTo; //INSTTO交费终止日期
	private String occdate; //OCCDATE 保单生效日期

	private String premterm; //PREMTERM期数
	private String vicCustomerCName; //VIC_CUSTOMER_CNAME 客户纳税人中文名称
	private String vicCustomerTaxNo; //VIC_CUSTOMER_TAXNO客户纳税人识别号

	private String vicCustomerAccount; //VIC_CUSTOMER_ACCOUNT客户开户账号
	private String vicCustomerCBank;//VIC_CUSTOMER_CBANK客户开户银行中文名称
	private String vicCustomerPhone; //VIC_CUSTOMER_PHONE客户电话
	private String vicCustomerEmail;  //VIC_CUSTOMER_EMAIL客户邮箱地址
	private String vicCustomerAddress;  //VIC_CUSTOMER_ADDRESS客户地址
	private String vicTaxPayerType; //VIC_TAXPAYER_TYPE客户纳税人类别,S小规模纳税人G一般纳税人O其他
	private String vicCustomerType;  //VIC_CUSTOMER_TYPE客户类型,I私人客户C公司客户

	private String vicCustomerNationality; //VIC_CUSTOMER_NATIONALITY客户国籍
	private String vicLinkName; //VIC_LINK_NAME联系人名称
	private String vicLinkPhone;  //VIC_LINK_PHONE联系人电话
	private String vicLinkAddress;  //VIC_LINK_ADDRESS联系人地址
	private String vicCustomerZipCode;  //VIC_CUSTOMER_ZIP_CODE客户邮编
	private String resultStatus;  //ResultStatus发票状态,1已推送未打印2发票开具成功3发票开具失败4发票打印成功6发票打印失败
	private String goodsId;                            //GOODS_ID
	private String goodsName;  //GOODS_NAME货物或应税劳务名称
	private String isHandIWork;  //IS_HANDIWORK是否开票,1是0否
	private String specandmodel;  //SPECANDMODEL规格型号
	private String goodsUnit;  //GOODS_UNIT单位
	private String goodsNo; //GOODS_NO数量
	private String goodsPrice;  //GOODS_PRICE单价,默认不含税保费

	private String remark;  //REMARK备注
	private String applyDate;  //APPLYDATE发票申请日期
	private String printDate; //PRINTDATE发票打印日期
	private String chequeNo; //CHEQUENO发票号码
	private String chequeId;  //CHEQUEID发票代码
	private String invoiceNum;  //INVOICE_NUM待打印发票流水号
    private String taxFlag;  //TAXFLAG含税标志,Y含税N不含税
    private String customerId; //  CUSTOMER_ID
    private String feetyp;      //     FEETYP 费用类型
    private String issueType;      
    
    public String getIssueType() {
		return issueType;
	}
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	public String getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getCherNum() {
		return cherNum;
	}
	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getTtmprcNo() {
		return ttmprcNo;
	}
	public void setTtmprcNo(String ttmprcNo) {
		this.ttmprcNo = ttmprcNo;
	}
	public String getOrigcurr() {
		return origcurr;
	}
	public void setOrigcurr(String origcurr) {
		this.origcurr = origcurr;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public String getOrigAmt() {
		return origAmt;
	}
	public void setOrigAmt(String origAmt) {
		this.origAmt = origAmt;
	}
	public String getOrigIncome() {
		return origIncome;
	}
	public void setOrigIncome(String origIncome) {
		this.origIncome = origIncome;
	}
	public String getOrigTaxAmt() {
		return origTaxAmt;
	}
	public void setOrigTaxAmt(String origTaxAmt) {
		this.origTaxAmt = origTaxAmt;
	}
	public String getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(String amtCny) {
		this.amtCny = amtCny;
	}
	public String getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(String incomeCny) {
		this.incomeCny = incomeCny;
	}
	public String getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(String taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public String getTraDt() {
		return traDt;
	}
	public void setTraDt(String traDt) {
		this.traDt = traDt;
	}
	public String getTraTyp() {
		return traTyp;
	}
	public void setTraTyp(String traTyp) {
		this.traTyp = traTyp;
	}
	public String getBatctrcde() {
		return batctrcde;
	}
	public void setBatctrcde(String batctrcde) {
		this.batctrcde = batctrcde;
	}
	public String getTraBra() {
		return traBra;
	}
	public void setTraBra(String traBra) {
		this.traBra = traBra;
	}
	public String getInvTyp() {
		return invTyp;
	}
	public void setInvTyp(String invTyp) {
		this.invTyp = invTyp;
	}
	public String getBillFreq() {
		return billFreq;
	}
	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}
	
	public String getPolYear() {
		return polYear;
	}
	public void setPolYear(String polYear) {
		this.polYear = polYear;
	}
	public String getHissDte() {
		return hissDte;
	}
	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}
	public String getDsource() {
		return dsource;
	}
	public void setDsource(String dsource) {
		this.dsource = dsource;
	}
	public String getProductCod() {
		return productCod;
	}
	public void setProductCod(String productCod) {
		this.productCod = productCod;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInsCod() {
		return insCod;
	}
	public void setInsCod(String insCod) {
		this.insCod = insCod;
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
	public String getOccdate() {
		return occdate;
	}
	public void setOccdate(String occdate) {
		this.occdate = occdate;
	}
	public String getPremterm() {
		return premterm;
	}
	public void setPremterm(String premterm) {
		this.premterm = premterm;
	}
	public String getVicCustomerCName() {
		return vicCustomerCName;
	}
	public void setVicCustomerCName(String vicCustomerCName) {
		this.vicCustomerCName = vicCustomerCName;
	}
	public String getVicCustomerTaxNo() {
		return vicCustomerTaxNo;
	}
	public void setVicCustomerTaxNo(String vicCustomerTaxNo) {
		this.vicCustomerTaxNo = vicCustomerTaxNo;
	}
	public String getVicCustomerAccount() {
		return vicCustomerAccount;
	}
	public void setVicCustomerAccount(String vicCustomerAccount) {
		this.vicCustomerAccount = vicCustomerAccount;
	}
	public String getVicCustomerCBank() {
		return vicCustomerCBank;
	}
	public void setVicCustomerCBank(String vicCustomerCBank) {
		this.vicCustomerCBank = vicCustomerCBank;
	}
	public String getVicCustomerPhone() {
		return vicCustomerPhone;
	}
	public void setVicCustomerPhone(String vicCustomerPhone) {
		this.vicCustomerPhone = vicCustomerPhone;
	}
	public String getVicCustomerEmail() {
		return vicCustomerEmail;
	}
	public void setVicCustomerEmail(String vicCustomerEmail) {
		this.vicCustomerEmail = vicCustomerEmail;
	}
	public String getVicCustomerAddress() {
		return vicCustomerAddress;
	}
	public void setVicCustomerAddress(String vicCustomerAddress) {
		this.vicCustomerAddress = vicCustomerAddress;
	}
	public String getVicTaxPayerType() {
		return vicTaxPayerType;
	}
	public void setVicTaxPayerType(String vicTaxPayerType) {
		this.vicTaxPayerType = vicTaxPayerType;
	}
	public String getVicCustomerType() {
		return vicCustomerType;
	}
	public void setVicCustomerType(String vicCustomerType) {
		this.vicCustomerType = vicCustomerType;
	}
	public String getVicCustomerNationality() {
		return vicCustomerNationality;
	}
	public void setVicCustomerNationality(String vicCustomerNationality) {
		this.vicCustomerNationality = vicCustomerNationality;
	}
	public String getVicLinkName() {
		return vicLinkName;
	}
	public void setVicLinkName(String vicLinkName) {
		this.vicLinkName = vicLinkName;
	}
	public String getVicLinkPhone() {
		return vicLinkPhone;
	}
	public void setVicLinkPhone(String vicLinkPhone) {
		this.vicLinkPhone = vicLinkPhone;
	}
	public String getVicLinkAddress() {
		return vicLinkAddress;
	}
	public void setVicLinkAddress(String vicLinkAddress) {
		this.vicLinkAddress = vicLinkAddress;
	}
	public String getVicCustomerZipCode() {
		return vicCustomerZipCode;
	}
	public void setVicCustomerZipCode(String vicCustomerZipCode) {
		this.vicCustomerZipCode = vicCustomerZipCode;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getIsHandIWork() {
		return isHandIWork;
	}
	public void setIsHandIWork(String isHandIWork) {
		this.isHandIWork = isHandIWork;
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
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getChequeId() {
		return chequeId;
	}
	public void setChequeId(String chequeId) {
		this.chequeId = chequeId;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getTaxFlag() {
		return taxFlag;
	}
	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getFeetyp() {
		return feetyp;
	}
	public void setFeetyp(String feetyp) {
		this.feetyp = feetyp;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
    
}
