package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:41:12 PM
 * @描述: [Self_Sub_EXPLBALA]质押外汇余额信息
 */
public class Self_Sub_EXPLBALA extends SelfSubEntity{

	private String explcurr; // 质押外汇币种
	private BigDecimal explbala; // 质押外汇余额
	private BigDecimal explperamount; // 质押外汇履约金额
	private BigDecimal plcoseamount; // 质押履约结汇金额

	public String getExplcurr(){
		return explcurr;
	}

	public void setExplcurr(String explcurr){
		this.explcurr = explcurr;
	}

	public BigDecimal getExplbala(){
		return explbala;
	}

	public void setExplbala(BigDecimal explbala){
		this.explbala = explbala;
	}

	public BigDecimal getExplperamount(){
		return explperamount;
	}

	public void setExplperamount(BigDecimal explperamount){
		this.explperamount = explperamount;
	}

	public BigDecimal getPlcoseamount(){
		return plcoseamount;
	}

	public void setPlcoseamount(BigDecimal plcoseamount){
		this.plcoseamount = plcoseamount;
	}
}
