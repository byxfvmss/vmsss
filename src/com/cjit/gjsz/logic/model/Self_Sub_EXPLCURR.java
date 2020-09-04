package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:41:28 PM
 * @描述: [Self_Sub_EXPLCURR]质押外汇金额信息
 */
public class Self_Sub_EXPLCURR extends SelfSubEntity{

	private String explcurr; // 质押外汇币种
	private BigDecimal explamount; // 质押外汇金额

	public String getExplcurr(){
		return explcurr;
	}

	public void setExplcurr(String explcurr){
		this.explcurr = explcurr;
	}

	public BigDecimal getExplamount(){
		return explamount;
	}

	public void setExplamount(BigDecimal explamount){
		this.explamount = explamount;
	}
}
