package com.cjit.vms.system.model;

import java.math.BigDecimal;
import java.util.List;


/**
 * 
 * @author 进项税交易信息+票据 实体类
 *
 */
public class MonitorInput {
//	private	String	startDate;			//开票日期 startDate endDate instName vendorName transName
//	private	String	endDate;			//开票日期
//	private	String	instCode;			//机构       
//	private	String	instName;			//机构名称
//	private	String	vendorTaxno;   		//供应商纳税人识别号
//	private	String	vendorName;   		//供应商名称
//	private String 	transType; 			// TRANS_TYPE 交易类型
//	private String 	transName; 			// TRANS_TYPE 交易类型名字
//	private BigDecimal amt; 				//	AMT	已交易金额
//	private	BigDecimal taxAmt;   			//	TAX_AMT	已交易税额
//	private	BigDecimal income;  			//	AMT-TAX_AMT	已交易收入
//	private	BigDecimal billAmt;   			//		发票总额
//	private	BigDecimal billTax;   			//		发票税额
//	private	BigDecimal billIncome;  		//		已开票收入
//	private	BigDecimal ductAmt;			//科蒂后金额
//	private	BigDecimal	outAmt;     		//		转出金额
	private String Number;//序号
//	private	BigDecimal Selltax ;  //销向稅额
//	private	BigDecimal Inputtax ;  //进向稅额
//	private	BigDecimal paytax ;  //应缴稅额
//	private	String flag ;  //
	
	// -------------------------交易--------------------------------
	private String dealNo; // 交易编号
	
	private String billCode;// 发票代码
	private String billNo;// 发票号码
	private BigDecimal amtCny;// 金额_人民币
	private BigDecimal taxAmtCny;// 税额_人民币
	private BigDecimal incomeCny;// 收入_人民币
	private BigDecimal surtax1AmtCny;// 附加税1（城市建设）金额
	private BigDecimal surtax2AmtCny;// 附加税2（教育附加）金额
	private BigDecimal surtax3AmtCny;// 附加税3（地方教育附加）金额
	private BigDecimal surtax4AmtCny;// 附加税4（其他）金额
	private String vendorId;// 供应商ID
	private String bankCode;// 交易发生机构
	private String remark;// 其他
	
	//-----------------------------交易---------------------------------
	
	// ---------------------------票据--与交易字段重复的起别名xxx_b--------------------------------
	private String billId;// 票据ID
	private String billCode_b;// 发票代码
	private String billNo_b;// 发票号码
	private String billDate;// 开票日期
	private String name;// 购方纳税人名称
	private String taxNo;// 购方纳税人识别号
	private String addressAndPhone;// 购方地址电话
	private String bankAndAccount;// 购方银行账号
	private BigDecimal amtSum;// 合计金额
	private BigDecimal taxAmtSum;// 合计税额
	private BigDecimal sumAmt;// 价税合计
	private String remark_b;// 备注
	private String drawer;// 开票人
	private String reivewer;// 复核人
	private String payee;// 收款人
	private String vendorName;// 销方纳税人名称
	private String vendorTaxNo;// 销方纳税人识别号
	private String vendorAddressAndPhone;// 销方地址电话
	private String vendorBankAndAccount;// 销方银行账号
	private String instCode;// 所属机构
	private String noticeNo;// 通知单编号
	private String dataStatus;// 状态
	private String description;// 操作说明
	private BigDecimal vatOutProportion;// 转出比例
	private BigDecimal vatOutAmt;// 转出金额
	private String conformFlg;// 是否勾稽
	private BigDecimal balance;// 发票余额
	private String faPiaoType;// 发票类型
	
	// ---------------------------票据--与交易字段重复的起别名xxx_b--------------------------------
	
	// ---------------------查询条件/页面显示 用到的属性----------------------------------------------------
	private	String	startDate;			//开票日期  
	private	String	endDate;			//开票日期
 	private	String flag ;
 	private String transName;
 	private String instName;
 	
 	
 	//-------------------------------------应缴税金部分属性-----------------------------------------
 	private BigDecimal outTaxAmtSum;
 	private BigDecimal inputTaxAmtSum;
 	private BigDecimal billVatOutAmtSum;
 	//-------------------------------------应缴税金部分属性-----------------------------------------
	private String userId;
	private List lstAuthInstId;
	
	
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	} 


	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
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
	public BigDecimal getAmtCny() {
		return amtCny;
	}
	public void setAmtCny(BigDecimal amtCny) {
		this.amtCny = amtCny;
	}
	public BigDecimal getTaxAmtCny() {
		return taxAmtCny;
	}
	public void setTaxAmtCny(BigDecimal taxAmtCny) {
		this.taxAmtCny = taxAmtCny;
	}
	public BigDecimal getIncomeCny() {
		return incomeCny;
	}
	public void setIncomeCny(BigDecimal incomeCny) {
		this.incomeCny = incomeCny;
	}
	public BigDecimal getSurtax1AmtCny() {
		return surtax1AmtCny;
	}
	public void setSurtax1AmtCny(BigDecimal surtax1AmtCny) {
		this.surtax1AmtCny = surtax1AmtCny;
	}
	public BigDecimal getSurtax2AmtCny() {
		return surtax2AmtCny;
	}
	public void setSurtax2AmtCny(BigDecimal surtax2AmtCny) {
		this.surtax2AmtCny = surtax2AmtCny;
	}
	public BigDecimal getSurtax3AmtCny() {
		return surtax3AmtCny;
	}
	public void setSurtax3AmtCny(BigDecimal surtax3AmtCny) {
		this.surtax3AmtCny = surtax3AmtCny;
	}
	public BigDecimal getSurtax4AmtCny() {
		return surtax4AmtCny;
	}
	public void setSurtax4AmtCny(BigDecimal surtax4AmtCny) {
		this.surtax4AmtCny = surtax4AmtCny;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillCode_b() {
		return billCode_b;
	}
	public void setBillCode_b(String billCode_b) {
		this.billCode_b = billCode_b;
	}
	public String getBillNo_b() {
		return billNo_b;
	}
	public void setBillNo_b(String billNo_b) {
		this.billNo_b = billNo_b;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getAddressAndPhone() {
		return addressAndPhone;
	}
	public void setAddressAndPhone(String addressAndPhone) {
		this.addressAndPhone = addressAndPhone;
	}
	public String getBankAndAccount() {
		return bankAndAccount;
	}
	public void setBankAndAccount(String bankAndAccount) {
		this.bankAndAccount = bankAndAccount;
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
	public String getRemark_b() {
		return remark_b;
	}
	public void setRemark_b(String remark_b) {
		this.remark_b = remark_b;
	}
	public String getDrawer() {
		return drawer;
	}
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}
	public String getReivewer() {
		return reivewer;
	}
	public void setReivewer(String reivewer) {
		this.reivewer = reivewer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorTaxNo() {
		return vendorTaxNo;
	}
	public void setVendorTaxNo(String vendorTaxNo) {
		this.vendorTaxNo = vendorTaxNo;
	}
	public String getVendorAddressAndPhone() {
		return vendorAddressAndPhone;
	}
	public void setVendorAddressAndPhone(String vendorAddressAndPhone) {
		this.vendorAddressAndPhone = vendorAddressAndPhone;
	}
	public String getVendorBankAndAccount() {
		return vendorBankAndAccount;
	}
	public void setVendorBankAndAccount(String vendorBankAndAccount) {
		this.vendorBankAndAccount = vendorBankAndAccount;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getVatOutProportion() {
		return vatOutProportion;
	}
	public void setVatOutProportion(BigDecimal vatOutProportion) {
		this.vatOutProportion = vatOutProportion;
	}
	public BigDecimal getVatOutAmt() {
		return vatOutAmt;
	}
	public void setVatOutAmt(BigDecimal vatOutAmt) {
		this.vatOutAmt = vatOutAmt;
	}
	public String getConformFlg() {
		return conformFlg;
	}
	public void setConformFlg(String conformFlg) {
		this.conformFlg = conformFlg;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getFaPiaoType() {
		return faPiaoType;
	}
	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}
	public BigDecimal getOutTaxAmtSum() {
		return outTaxAmtSum;
	}
	public void setOutTaxAmtSum(BigDecimal outTaxAmtSum) {
		this.outTaxAmtSum = outTaxAmtSum;
	}
	public BigDecimal getInputTaxAmtSum() {
		return inputTaxAmtSum;
	}
	public void setInputTaxAmtSum(BigDecimal inputTaxAmtSum) {
		this.inputTaxAmtSum = inputTaxAmtSum;
	}
	public BigDecimal getBillVatOutAmtSum() {
		return billVatOutAmtSum;
	}
	public void setBillVatOutAmtSum(BigDecimal billVatOutAmtSum) {
		this.billVatOutAmtSum = billVatOutAmtSum;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
