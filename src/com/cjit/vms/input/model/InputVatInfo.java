package com.cjit.vms.input.model;

import java.math.BigDecimal;

/**
 * 进项税信息类
 * 
 * @author Larry
 */
public class InputVatInfo {

	// 数据库属性
	private String inVatId;// 进项税ID
	private String billCode;// 发票代码(左上代码，可多张相同)
	private String billNo;// 发票号码(右上打印号码，唯一)
	private String valueDate;// 交易日期
	private String bookingDate;// 记账日期
	private String billType;// 发票种类
	private String transType;// 交易类型
	private String goodsName;// 商品名称
	private String specandmodel;// 规格型号
	private BigDecimal goodsNo;// 商品数量
	private BigDecimal goodsPrice;// 商品单价
	private String taxFlag;// 含税标志
	private BigDecimal amt;// 金额
	private BigDecimal taxAmt;// 税额
	private BigDecimal taxRate;// 税率
	private String suppName;// 供应商名称
	private String suppTaxNo;// 供应商税务登记号
	private String suppBank;// 供应商银行
	private String suppAccount;// 供应商账号
	private String suppAddress;// 供应商地址
	private String suppPhone;// 供应商电话
	private String bookingCourse;// 记账科目
	private String instCode;// 所属部门
	private String bussVouchersCode;// 业务凭证编号
	private String contractCode;// 合同管理编号
	private String deductionCode;// 抵扣联管理编号
	private String authenticationFlag;// 认证结果
	private String authenticationDate;// 认证日期
	private String remark;// 备注
	private String drawer;// 开票人
	private String reviewer;// 复核人
	private String payee;// 收款人
	private BigDecimal outAmt;// 转出金额
	// 辅助属性
	private String valueBeginDate;
	private String valueEndDate;
	private String bookingBeginDate;
	private String bookingEndDate;
	private BigDecimal sumAmt;// 价税合计(金额与税额之和)

	public String getInVatId() {
		return inVatId;
	}

	public void setInVatId(String inVatId) {
		this.inVatId = inVatId;
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

	public String getValueDate() {
		return valueDate == null ? "" : valueDate.trim();
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getBookingDate() {
		return bookingDate == null ? "" : bookingDate.trim();
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getBillType() {
		return billType;
	}

	public String getBillTypeView() {
		if ("1".equals(billType)) {
			return "增值税专用发票";
		} else if ("2".equals(billType)) {
			return "海关进口增值税专用缴款书";
		} else if ("3".equals(billType)) {
			return "代扣代缴税收通用缴款书";
		} else if ("4".equals(billType)) {
			return "运输费用结算单据";
		}
		return "";
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
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

	public BigDecimal getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(BigDecimal goodsNo) {
		this.goodsNo = goodsNo;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getTaxFlag() {
		return taxFlag;
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getSuppName() {
		return suppName == null ? "" : suppName.trim();
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppTaxNo() {
		return suppTaxNo == null ? "" : suppTaxNo.trim();
	}

	public void setSuppTaxNo(String suppTaxNo) {
		this.suppTaxNo = suppTaxNo;
	}

	public String getSuppBank() {
		return suppBank == null ? "" : suppBank.trim();
	}

	public void setSuppBank(String suppBank) {
		this.suppBank = suppBank;
	}

	public String getSuppAccount() {
		return suppAccount == null ? "" : suppAccount.trim();
	}

	public void setSuppAccount(String suppAccount) {
		this.suppAccount = suppAccount;
	}

	public String getSuppAddress() {
		return suppAddress == null ? "" : suppAddress.trim();
	}

	public void setSuppAddress(String suppAddress) {
		this.suppAddress = suppAddress;
	}

	public String getSuppPhone() {
		return suppPhone == null ? "" : suppPhone.trim();
	}

	public void setSuppPhone(String suppPhone) {
		this.suppPhone = suppPhone;
	}

	public String getBookingCourse() {
		return bookingCourse == null ? "" : bookingCourse.trim();
	}

	public void setBookingCourse(String bookingCourse) {
		this.bookingCourse = bookingCourse;
	}

	public String getInstCode() {
		return instCode == null ? "" : instCode.trim();
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getBussVouchersCode() {
		return bussVouchersCode == null ? "" : bussVouchersCode.trim();
	}

	public void setBussVouchersCode(String bussVouchersCode) {
		this.bussVouchersCode = bussVouchersCode;
	}

	public String getContractCode() {
		return contractCode == null ? "" : contractCode.trim();
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getDeductionCode() {
		return deductionCode == null ? "" : deductionCode.trim();
	}

	public void setDeductionCode(String deductionCode) {
		this.deductionCode = deductionCode;
	}

	public String getAuthenticationFlag() {
		return authenticationFlag == null ? "" : authenticationFlag.trim();
	}

	public String getAuthenticationFlagView() {
		if ("0".equals(authenticationFlag)) {
			return "未通过";
		} else if ("1".equals(authenticationFlag)) {
			return "已通过";
		}
		return "";
	}

	public void setAuthenticationFlag(String authenticationFlag) {
		this.authenticationFlag = authenticationFlag;
	}

	public String getAuthenticationDate() {
		return authenticationDate == null ? "" : authenticationDate.trim();
	}

	public void setAuthenticationDate(String authenticationDate) {
		this.authenticationDate = authenticationDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public BigDecimal getOutAmt() {
		return outAmt;
	}

	public void setOutAmt(BigDecimal outAmt) {
		this.outAmt = outAmt;
	}

	public String getValueBeginDate() {
		return valueBeginDate;
	}

	public void setValueBeginDate(String valueBeginDate) {
		this.valueBeginDate = valueBeginDate;
	}

	public String getValueEndDate() {
		return valueEndDate;
	}

	public void setValueEndDate(String valueEndDate) {
		this.valueEndDate = valueEndDate;
	}

	public String getBookingBeginDate() {
		return bookingBeginDate;
	}

	public void setBookingBeginDate(String bookingBeginDate) {
		this.bookingBeginDate = bookingBeginDate;
	}

	public String getBookingEndDate() {
		return bookingEndDate;
	}

	public void setBookingEndDate(String bookingEndDate) {
		this.bookingEndDate = bookingEndDate;
	}

	public BigDecimal getSumAmt() {
		if (sumAmt == null) {
			sumAmt = new BigDecimal(0.00);
		}
		if (this.amt != null) {
			sumAmt = sumAmt.add(this.amt);
		}
		if (this.taxAmt != null) {
			sumAmt = sumAmt.add(this.taxAmt);
		}
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}

}
