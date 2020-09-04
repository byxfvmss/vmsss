package com.cjit.vms.trans.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * 票据明细类
 * 
 * @author Larry
 */
public class BillItemInfo implements Comparator {

	// 数据库属性
	private String billId;// 票据ID
	private String billItemId;// 票据明细ID
	private String goodsName;// 商品名称
	private String specandmodel;// 规格型号
	private String goodsUnit;// 单位
	private BigDecimal goodsNo;// 商品数量
	private BigDecimal goodsPrice;// 商品单价
	private String taxFlag;// 含税标志
	private BigDecimal amt;// 金额
	private BigDecimal taxRate;// 税率
	private BigDecimal taxAmt;// 税额
	private BigDecimal sumAmt;//价税合计
	private String taxItem; // 商品税目
	private String isMainGoods;// 是否主商品
	private String rowNature;// 票据行性质(百旺fphxz:0-正常行;1-折扣行;2-被折扣行)
	private String disItemId;// 被折扣明细ID
	private BigDecimal discountRate;// 折扣率
	// 辅助属性
	private String transTypeName;// 交易类型名称
	private String transTypeAutoPrint;// 交易对应类型是否可自动打印
	private TransInfo transInfo;// 对应交易信息
	private List transList;// 对应交易信息(多笔交易合并一条明细)
	private String transId;
	private BigDecimal different;// 施舍五入后与真实之间的差值
	private BigDecimal transSumTaxAmt;// 对应交易计算合计税额
	
	private String amtStr;
	private String taxRateStr;
	private String taxAmtStr;
	private String goodsNoStr;
	private String goodsPriceStr;
	
	
	private String goodsId;
	private String goodsFullName;
	private BigDecimal  income;
	
	public BillItemInfo() {
	}

	public BillItemInfo(String billId, String billItemId) {
		this.billId = billId;
		this.billItemId = billItemId;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillItemId() {
		return billItemId;
	}

	public void setBillItemId(String billItemId) {
		this.billItemId = billItemId;
	}

	public String getGoodsName() {
		return goodsName == null ? "" : goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSpecandmodel() {
		return specandmodel == null ? "" : specandmodel;
	}

	public void setSpecandmodel(String specandmodel) {
		this.specandmodel = specandmodel;
	}

	public String getGoodsUnit() {
		return goodsUnit == null ? "" : goodsUnit.trim();
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

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getTaxFlag() {
		return taxFlag == null ? "" : taxFlag;
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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public String getTaxItem() {
		return taxItem == null ? "" : taxItem.trim();
	}

	public void setTaxItem(String taxItem) {
		this.taxItem = taxItem;
	}

	public String getIsMainGoods() {
		return isMainGoods;
	}

	public void setIsMainGoods(String isMainGoods) {
		this.isMainGoods = isMainGoods;
	}

	public String getRowNature() {
		return rowNature;
	}

	public void setRowNature(String rowNature) {
		this.rowNature = rowNature;
	}

	public String getDisItemId() {
		return disItemId;
	}

	public void setDisItemId(String disItemId) {
		this.disItemId = disItemId;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getTransTypeName() {
		return transTypeName;
	}

	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}

	public String getTransTypeAutoPrint() {
		return transTypeAutoPrint;
	}

	public void setTransTypeAutoPrint(String transTypeAutoPrint) {
		this.transTypeAutoPrint = transTypeAutoPrint;
	}

	public TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public List getTransList() {
		return transList;
	}

	public void setTransList(List transList) {
		this.transList = transList;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public BigDecimal getDifferent() {
		return different;
	}

	public void setDifferent(BigDecimal amt, BigDecimal taxRate) {
		this.different = (amt.multiply(taxRate).divide(
				new BigDecimal(1).add(taxRate)).setScale(2,
				BigDecimal.ROUND_HALF_UP).subtract(amt.multiply(taxRate)
				.divide(new BigDecimal(1).add(taxRate))));
	}

	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		BillItemInfo bill = (BillItemInfo) arg0;
		BillItemInfo bill1 = (BillItemInfo) arg1;
		return (bill.getDifferent().compareTo(bill1.getDifferent()));

	}

	public BigDecimal getTransSumTaxAmt() {
		return transSumTaxAmt;
	}

	public void setTransSumTaxAmt(BigDecimal transSumTaxAmt) {
		this.transSumTaxAmt = transSumTaxAmt;
	}

	public String getAmtStr() {
		return amtStr;
	}

	public String getTaxRateStr() {
		return taxRateStr;
	}

	public String getTaxAmtStr() {
		return taxAmtStr;
	}

	public String getGoodsNoStr() {
		return goodsNoStr;
	}

	public String getGoodsPriceStr() {
		return goodsPriceStr;
	}

	public void setAmtStr(String amtStr) {
		this.amtStr = amtStr;
	}

	public void setTaxRateStr(String taxRateStr) {
		this.taxRateStr = taxRateStr;
	}

	public void setTaxAmtStr(String taxAmtStr) {
		this.taxAmtStr = taxAmtStr;
	}

	public void setGoodsNoStr(String goodsNoStr) {
		this.goodsNoStr = goodsNoStr;
	}

	public void setGoodsPriceStr(String goodsPriceStr) {
		this.goodsPriceStr = goodsPriceStr;
	}

	public BigDecimal getSumAmt() {
		if(this.amt!=null&&this.taxAmt!=null){
			return this.amt.add(this.taxAmt);
		}
		if(this.taxAmt==new BigDecimal(0)){
			return this.amt;
		}
		return sumAmt;
	}

	public void setSumAmt(BigDecimal sumAmt) {
		this.sumAmt = sumAmt;
	}
public static void main(String[] args) {
	int [] x=new int[25];
	//System.out.println(x[25]);
	System.out.println(x[24]);
	System.out.println(x[0]);
}

public String getGoodsId() {
	return goodsId;
}

public void setGoodsId(String goodsId) {
	this.goodsId = goodsId;
}

public String getGoodsFullName() {
	return goodsFullName;
}

public void setDifferent(BigDecimal different) {
	this.different = different;
}

public void setGoodsFullName(String goodsFullName) {
	this.goodsFullName = goodsFullName;
}

public BigDecimal getIncome() {
	return income;
}

public void setIncome(BigDecimal income) {
	this.income = income;
}
	
	
}
