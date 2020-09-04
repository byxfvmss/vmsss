package com.cjit.vms.metlife.model;

import java.math.BigDecimal;

public class ManualTrans {

	
	private String cownNum;//	客户号
	private String trdt;//	 交易时间
	private BigDecimal acctAmt;//	交易金额
	private String goodsName;//	商品名称
	private BigDecimal taxRate;//	税率
	private String altref;//	收入会计科目
	private String t1;//	T1
	private String t2;//	T2
	private String t3;//	T3
	private String t4;//	T4
	private String t5;//	T5
	private String t6;//	T6
	private String t7;//	T7
	private String t8;//	T8
	private String t9;//	T9
	private String t10;//	T10(机构)
	private String desCm;//	摘要
	
	public ManualTrans() {
		
	}
	
	public String getCownNum() {
		return cownNum != null ? cownNum : "";
	}
	public void setCownNum(String cownNum) {
		if (cownNum != null) {
			this.cownNum = cownNum;
		}
	}
	public String getTrdt() {
		return trdt != null ? trdt : "";
	}
	public void setTrdt(String trdt) {
		if (trdt != null) {
			this.trdt = trdt;
		}
	}
	public BigDecimal getAcctAmt() {
		return acctAmt;
	}
	public void setAcctAmt(BigDecimal acctAmt) {
		if (acctAmt != null) {
			this.acctAmt = acctAmt;
		}
	}
	public String getGoodsName() {
		return goodsName != null ? goodsName : "";
	}
	public void setGoodsName(String goodsName) {
		if (goodsName != null) {
			this.goodsName = goodsName;
		}
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		if (taxRate != null) {
			this.taxRate = taxRate;
		}
	}
	public String getAltref() {
		return altref != null ? altref : "";
	}
	public void setAltref(String altref) {
		if (altref != null) {
			this.altref = altref;
		}
	}
	public String getT1() {
		return t1 != null ? t1 : "";
	}
	public void setT1(String t1) {
		if (t1 != null) {
			this.t1 = t1;
		}
	}
	public String getT2() {
		return t2 != null ? t2 : "";
	}
	public void setT2(String t2) {
		if (t2 != null) {
			this.t2 = t2;
		}
	}
	public String getT3() {
		return t3 != null ? t3 : "";
	}
	public void setT3(String t3) {
		if (t3 != null) {
			this.t3 = t3;
		}
	}
	public String getT4() {
		return t4 != null ? t4 : "";
	}
	public void setT4(String t4) {
		if (t4 != null) {
			this.t4 = t4;
		}
	}
	public String getT5() {
		return t5 != null ? t5 : "";
	}
	public void setT5(String t5) {
		if (t5 != null) {
			this.t5 = t5;
		}
	}
	public String getT6() {
		return t6 != null ? t6 : "";
	}
	public void setT6(String t6) {
		if (t6 != null) {
			this.t6 = t6;
		}
	}
	public String getT7() {
		return t7 != null ? t7 : "";
	}
	public void setT7(String t7) {
		if (t7 != null) {
			this.t7 = t7;
		}
	}
	public String getT8() {
		return t8 != null ? t8 : "";
	}
	public void setT8(String t8) {
		if (t8 != null) {
			this.t8 = t8;
		}
	}
	public String getT9() {
		return t9 != null ? t9 : "";
	}
	public void setT9(String t9) {
		if (t9 != null) {
			this.t9 = t9;
		}
	}
	public String getT10() {
		return t10 != null ? t10 : "";
	}
	public void setT10(String t10) {
		if (t10 != null) {
			this.t10 = t10;
		}
	}
	public String getDesCm() {
		return desCm != null ? desCm : "";
	}
	public void setDesCm(String desCm) {
		if (desCm != null) {
			this.desCm = desCm;
		}
	}
	
	
	
}
