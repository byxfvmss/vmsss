package com.cjit.vms.taxdisk.single.model.parseXml;

import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class BuyBillBatch extends BaseDiskModel{
	public static void main(String[] args) {
		String data="bill_code_ch                ,  billCode             ,fpdm	       ,发票代码		     ,是,	长度根据发票类型确定 ;"+
		"bill_begin_no_ch             ,  billBeginNo          ,qshm	       ,发票起始号码		 ,是, ;"+
		"bill_end_no_ch	             ,  billEndNo            ,zzhm	       ,发票终止号码		 ,是, ;"+
		"bill_rece_pur_num_ch	       ,  billRecePurNum       ,fpfs	       ,发票领购份数		 ,是,	整数 ;"+
		"sur_num_ch	                 ,  surNum               ,syfs	       ,剩余份数		     ,是,	整数 ;"+
		"rece_pur_date_ch	           ,  recePurDate          ,lgrq	       ,领购日期		     ,是,	YYYYMMDD ;"+
		"rece_pur_people_ch	         ,  recePurPeople        ,lgry	       ,领购人员		     ,是, ;";
		new BuyBillBatch().createEntityUtil(data);
		
	}
	
	public BuyBillBatch(Element e) {
		super();
		this.billCode = e.getChildText(bill_code_ch);
		this.billBeginNo = e.getChildText(bill_begin_no_ch);
		this.billEndNo = e.getChildText(bill_end_no_ch);
		this.billRecePurNum = e.getChildText(bill_rece_pur_num_ch);
		this.surNum = e.getChildText(sur_num_ch);
		this.recePurDate = e.getChildText(rece_pur_date_ch);
		this.recePurPeople = e.getChildText(rece_pur_people_ch);
	}

	public BuyBillBatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	*发票代码  是否必须：是
	长度根据发票类型确定
	*/
	private static final String bill_code_ch="fpdm";
	/**
	*发票起始号码  是否必须：是

	*/
	private static final String bill_begin_no_ch="qshm";
	/**
	*发票终止号码  是否必须：是

	*/
	private static final String bill_end_no_ch="zzhm";
	/**
	*发票领购份数  是否必须：是
	整数
	*/
	private static final String bill_rece_pur_num_ch="fpfs";
	/**
	*剩余份数  是否必须：是
	整数
	*/
	private static final String sur_num_ch="syfs";
	/**
	*领购日期  是否必须：是
	YYYYMMDD
	*/
	private static final String rece_pur_date_ch="lgrq";
	/**
	*领购人员  是否必须：是

	*/
	private static final String rece_pur_people_ch="lgry";
	/**
	*发票代码 是否必须：是
	长度根据发票类型确定
	*/
	private String billCode;
	/**
	*发票起始号码 是否必须：是

	*/
	private String billBeginNo;
	/**
	*发票终止号码 是否必须：是

	*/
	private String billEndNo;
	/**
	*发票领购份数 是否必须：是
	整数
	*/
	private String billRecePurNum;
	/**
	*剩余份数 是否必须：是
	整数
	*/
	private String surNum;
	/**
	*领购日期 是否必须：是
	YYYYMMDD
	*/
	private String recePurDate;
	/**
	*领购人员 是否必须：是

	*/
	private String recePurPeople;
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillBeginNo() {
		return billBeginNo;
	}
	public void setBillBeginNo(String billBeginNo) {
		this.billBeginNo = billBeginNo;
	}
	public String getBillEndNo() {
		return billEndNo;
	}
	public void setBillEndNo(String billEndNo) {
		this.billEndNo = billEndNo;
	}
	public String getBillRecePurNum() {
		return billRecePurNum;
	}
	public void setBillRecePurNum(String billRecePurNum) {
		this.billRecePurNum = billRecePurNum;
	}
	public String getSurNum() {
		return surNum;
	}
	public void setSurNum(String surNum) {
		this.surNum = surNum;
	}
	public String getRecePurDate() {
		return recePurDate;
	}
	public void setRecePurDate(String recePurDate) {
		this.recePurDate = recePurDate;
	}
	public String getRecePurPeople() {
		return recePurPeople;
	}
	public void setRecePurPeople(String recePurPeople) {
		this.recePurPeople = recePurPeople;
	}


}
