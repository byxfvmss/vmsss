package com.cjit.vms.trans.model.taxDisk.parseXml;

import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class BillBatch extends BaseDiskModel {
	public static void main(String[] args) {
		String data="bill_code_ch          , billCode        ,fpdm	       ,发票代码	   	,否,	长度根据发票类型确定 ;"+
		"begin_no_ch           , beginNo         ,qshm	       ,起始号码	    ,否, ;"+
		"end_no_ch             , endNo           ,zzhm	       ,终止号码	    ,否, ;"+
		"num_ch                , num             ,fpfs	       ,发票份数	    ,否,	整数 ;"+
		"sur_num_ch            , surNum          ,syfs	       ,剩余份数	    ,否,	整数 ;"+
		"reci_pur_date_ch      , reciPurDate     ,lgrq	       ,领购日期	    ,否,	YYYYMMDD ;"+
		"reci_pur_people_ch    , reciPurPeople   ,lgry	       ,领购人员	    ,否, ;";
new BillBatch().createEntityUtil(data);

	}

	public BillBatch (Element e){
		this.billCode = e.getChildText(bill_code_ch);
		this.beginNo = e.getChildText(begin_no_ch);
		this.endNo = e.getChildText(end_no_ch);
		this.num = e.getChildText(num_ch);
		this.surNum = e.getChildText(sur_num_ch);
		this.reciPurDate = e.getChildText(reci_pur_date_ch);
		this.reciPurPeople = e.getChildText(reci_pur_people_ch);
	}

	

	public BillBatch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	*发票代码  是否必须：否
	长度根据发票类型确定
	*/
	private static final String bill_code_ch="fpdm";
	/**
	*起始号码  是否必须：否

	*/
	private static final String begin_no_ch="qshm";
	/**
	*终止号码  是否必须：否

	*/
	private static final String end_no_ch="zzhm";
	/**
	*发票份数  是否必须：否
	整数
	*/
	private static final String num_ch="fpfs";
	/**
	*剩余份数  是否必须：否
	整数
	*/
	private static final String sur_num_ch="syfs";
	/**
	*领购日期  是否必须：否
	YYYYMMDD
	*/
	private static final String reci_pur_date_ch="lgrq";
	/**
	*领购人员  是否必须：否

	*/
	private static final String reci_pur_people_ch="lgry";
	/**
	*发票代码 是否必须：否
	长度根据发票类型确定
	*/
	private String billCode;
	/**
	*起始号码 是否必须：否

	*/
	private String beginNo;
	/**
	*终止号码 是否必须：否

	*/
	private String endNo;
	/**
	*发票份数 是否必须：否
	整数
	*/
	private String num;
	/**
	*剩余份数 是否必须：否
	整数
	*/
	private String surNum;
	/**
	*领购日期 是否必须：否
	YYYYMMDD
	*/
	private String reciPurDate;
	/**
	*领购人员 是否必须：否

	*/
	private String reciPurPeople;
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBeginNo() {
		return beginNo;
	}
	public void setBeginNo(String beginNo) {
		this.beginNo = beginNo;
	}
	public String getEndNo() {
		return endNo;
	}
	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getSurNum() {
		return surNum;
	}
	public void setSurNum(String surNum) {
		this.surNum = surNum;
	}
	public String getReciPurDate() {
		return reciPurDate;
	}
	public void setReciPurDate(String reciPurDate) {
		this.reciPurDate = reciPurDate;
	}
	public String getReciPurPeople() {
		return reciPurPeople;
	}
	public void setReciPurPeople(String reciPurPeople) {
		this.reciPurPeople = reciPurPeople;
	}


}
