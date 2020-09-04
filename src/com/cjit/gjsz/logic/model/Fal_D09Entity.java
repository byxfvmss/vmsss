package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

public class Fal_D09Entity extends FalReportEntity {

	private String d0901;// 资产类别
	private String d0902;// 风险分类
	private String d0903;// 币种
	private BigDecimal d0904;// 本月末减值准备余额

	public String getD0901() {
		return d0901;
	}

	public void setD0901(String d0901) {
		this.d0901 = d0901;
	}

	public String getD0902() {
		return d0902;
	}

	public void setD0902(String d0902) {
		this.d0902 = d0902;
	}

	public String getD0903() {
		return d0903;
	}

	public void setD0903(String d0903) {
		this.d0903 = d0903;
	}

	public BigDecimal getD0904() {
		return d0904;
	}

	public void setD0904(BigDecimal d0904) {
		this.d0904 = d0904;
	}

}
