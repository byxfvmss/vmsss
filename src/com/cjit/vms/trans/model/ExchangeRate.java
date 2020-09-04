package com.cjit.vms.trans.model;

import java.math.BigDecimal;

import com.cjit.common.util.StringUtil;

public class ExchangeRate {

	private int exchangeRateId;	           //汇率ID
	private String dataDt;                 //数据日期
	private String basicCcy;               //基准币种
	private String ccyDate;	               //汇率日期
	private String forwardCcy;             //折算币种
	private String convertTyp;             //折算类型
	private BigDecimal ccyRate;	               //汇率

	public ExchangeRate() {
		
	}

	public int getExchangeRateId() {
		return exchangeRateId;
	}

	public void setExchangeRateId(int exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	public String getBasicCcy() {
		return basicCcy;
	}

	public void setBasicCcy(String basicCcy) {
		this.basicCcy = basicCcy;
	}

	public String getCcyDate() {
		return ccyDate;
	}

	public void setCcyDate(String ccyDate) {
		this.ccyDate = ccyDate;
	}

	public String getForwardCcy() {
		return forwardCcy;
	}

	public void setForwardCcy(String forwardCcy) {
		this.forwardCcy = forwardCcy;
	}

	public String getConvertTyp() {
		return convertTyp;
	}

	public void setConvertTyp(String convertTyp) {
		this.convertTyp = convertTyp;
	}

	public BigDecimal getCcyRate() {
		return ccyRate;
	}

	public void setCcyRate(BigDecimal ccyRate) {
		this.ccyRate = ccyRate;
	}

	public void setCcyRateStr(String ccyRate) {
		if (StringUtil.isNotEmpty(ccyRate)) {
			this.ccyRate = new BigDecimal(ccyRate);
		}
	}
	
	public void setDataDt(String dataDt) {
		this.dataDt = dataDt;
	}

	public String getDataDt() {
		return dataDt;
	}
	
	
}
