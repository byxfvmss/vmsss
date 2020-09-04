package com.cjit.vms.trans.model.createBill;

import java.util.List;

import com.cjit.vms.trans.model.config.VerificationInfo;

/**
 * 商品集合信息
 * 
 * @author Dylan
 *
 */
public class GoodsCollectionInfo {
	private String goodsId;
	private String taxno;
	private String amcCny;// 交易金额(价税合计)
	private String taxAmyCny;// 税额
	private String incomeCny;// 收入
	private String taxRate;// 税率
	private List transInfoList;

	public GoodsCollectionInfo() {

//		this.goodsId = verificationInfo.getGoodsId();

	}

	public GoodsCollectionInfo(VerificationInfo verificationInfo) {

		this.goodsId = verificationInfo.getGoodsId();

	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getAmcCny() {
		return amcCny;
	}

	public void setAmcCny(String amcCny) {
		this.amcCny = amcCny;
	}

	public String getTaxAmyCny() {
		return taxAmyCny;
	}

	public void setTaxAmyCny(String taxAmyCny) {
		this.taxAmyCny = taxAmyCny;
	}

	public String getIncomeCny() {
		return incomeCny;
	}

	public void setIncomeCny(String incomeCny) {
		this.incomeCny = incomeCny;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}
}
