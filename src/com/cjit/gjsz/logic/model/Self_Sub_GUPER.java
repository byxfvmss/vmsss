package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:42:15 PM
 * @描述: [Self_Sub_GUPER]履约信息
 */
public class Self_Sub_GUPER extends SelfSubEntity{

	private String guperdate; // 履约日期
	private String gupercurr; // 履约币种
	private BigDecimal guperamount; // 履约金额
	private BigDecimal pguperamount; // 购汇履约金额

	public String getGuperdate(){
		return guperdate;
	}

	public void setGuperdate(String guperdate){
		this.guperdate = guperdate;
	}

	public String getGupercurr(){
		return gupercurr;
	}

	public void setGupercurr(String gupercurr){
		this.gupercurr = gupercurr;
	}

	public BigDecimal getGuperamount(){
		return guperamount;
	}

	public void setGuperamount(BigDecimal guperamount){
		this.guperamount = guperamount;
	}

	public BigDecimal getPguperamount(){
		return pguperamount;
	}

	public void setPguperamount(BigDecimal pguperamount){
		this.pguperamount = pguperamount;
	}
}
