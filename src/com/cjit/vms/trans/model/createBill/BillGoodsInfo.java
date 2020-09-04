package com.cjit.vms.trans.model.createBill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.model.config.VerificationInfo;

public class BillGoodsInfo {
	
	private String goodsKey; //
	private String billId; // 票据ID
	private String billItemId; // 票据明细ID
	private String goodsName; // 商品名称
	private String specandmodel; // 规格型号
	private String goodsUnit; // 单位
	private String goodsNo; // 商品数量
	private BigDecimal goodsPrice = new BigDecimal("0"); // 商品单价
	private String taxFlag; // 含税标志 Y-含税N-不含税
	private BigDecimal amt = new BigDecimal("0"); // 金额
	private BigDecimal taxRate = new BigDecimal("0"); // 税率
	private BigDecimal taxAmt = new BigDecimal("0"); // 税额
	private String taxItem; // 商品税目
	private String isMaingoods; // 是否主商品(Y/N)
	private String rowNature; // 票据行性质 0-正常行;1-折扣行;2-被折扣行
	private String disItemId; // 被折扣明细ID
	private String discountRate; // 折扣率
	private String goodsId;// //

	protected List<BillTransInfo> transInfoList = new ArrayList<BillTransInfo>(); // 交易信息列表

	public BillGoodsInfo() {
		// TODO Auto-generated constructor stub
	}

	/***
	 * 
	 * 构造商品和 票据交易
	 * 
	 * @param billItemId
	 * @param transInfo
	 */
	public BillGoodsInfo(String billItemId, TransInfo transInfo) {
		VerificationInfo verificationInfo = transInfo.getVerificationInfo();
		// this.billId = billId;
		this.goodsKey = verificationInfo.getGoodsId()
				+ transInfo.getTaxRate().toString();
		this.billItemId = billItemId;
		this.goodsId = verificationInfo.getGoodsId();
		this.goodsName = verificationInfo.getGoodsName();
		this.specandmodel = verificationInfo.getModel();
		this.goodsUnit = verificationInfo.getUnit();
		this.goodsNo = "1";
		// this.goodsPrice = transInfo.getBalanceIncomeCny();
		this.taxFlag = transInfo.getTaxFlag();
		// this.amt = transInfo.getBalanceIncomeCny();
		this.taxRate = transInfo.getTaxRate();
		// this.taxAmt = transInfo.getTaxCnyBalance();
		this.taxItem = transInfo.getTaxinfo().getTaxId();
		// 现全正常 票据行性质 0-正常行;1-折扣行;2-被折扣行
		this.isMaingoods = "0";
		/***
		 * 折扣信息现无
		 */
		this.rowNature = "";
		this.disItemId = "";
		this.discountRate = "";
		BillTransInfo billTransInfo = new BillTransInfo(billId, billItemId,
				transInfo);
		addBillTransInfo(billTransInfo);
	}

	/***
	 * 构造商品和 票据交易
	 * 
	 * @param billId
	 * @param billItemId
	 * @param transInfo
	 */
	public BillGoodsInfo(String billId, String billItemId, TransInfo transInfo) {
		this(billItemId, transInfo);
		setBillId(billId);
	}

	public void addBillTransInfo(BillTransInfo billTransInfo) {
		billTransInfo.setBillId(billId);
		billTransInfo.setBillItemId(billItemId);
		writeBackGoodsAmt(billTransInfo);
		getTransInfoList().add(billTransInfo);
	}

	public void writeBackGoodsAmt(BillTransInfo billTransInfo) {
		this.goodsPrice = this.goodsPrice.add(billTransInfo.getIncomeCny());
		this.amt = this.amt.add(billTransInfo.getIncomeCny());
		this.taxAmt = this.taxAmt.add(billTransInfo.getTaxAmtCny());

	}

	public String getBillId() {
		return billId;
	}

	public String getBillItemId() {
		return billItemId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public String getSpecandmodel() {
		return specandmodel;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public String getTaxFlag() {
		return taxFlag;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public String getTaxItem() {
		return taxItem;
	}

	public String getIsMaingoods() {
		return isMaingoods;
	}

	public String getRowNature() {
		return rowNature;
	}

	public String getDisItemId() {
		return disItemId;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setBillId(String billId) {
		for (int i = 0; i < getTransInfoList().size(); i++) {
			BillTransInfo billTransInfo = getTransInfoList().get(i);
			billTransInfo.setBillId(billId);
		}
		this.billId = billId;
	}

	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public void setTaxFlag(String taxFlag) {
		this.taxFlag = taxFlag;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public void setTaxItem(String taxItem) {
		this.taxItem = taxItem;
	}

	public void setIsMaingoods(String isMaingoods) {
		this.isMaingoods = isMaingoods;
	}

	public void setRowNature(String rowNature) {
		this.rowNature = rowNature;
	}

	public void setDisItemId(String disItemId) {
		this.disItemId = disItemId;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public List<BillTransInfo> getTransInfoList() {
		if (null == transInfoList) {
			transInfoList = new ArrayList<BillTransInfo>();
		}
		return transInfoList;
	}

	public void setTransInfoList(List<BillTransInfo> transInfoList) {
		this.transInfoList = transInfoList;
	}

	public String getGoodsKey() {
		return goodsKey;
	}

	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}
	
	public void myAdd(BillGoodsInfo bgi){
		this.amt = amt.add(bgi.getAmt());
		this.taxAmt = taxAmt.add(bgi.getTaxAmt());
		this.goodsPrice = amt;
		this.goodsNo = "1";
	}
}
