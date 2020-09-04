package com.cjit.gjsz.logic.model;

public class ExportInfo extends Entity{

	private String refno;
	private String subid;
	private FinanceExport financeExport;
	private FinanceDomExport financeDomExport;

	public String getRefno(){
		return refno;
	}

	public void setRefno(String refno){
		this.refno = refno;
	}

	public String getSubid(){
		return subid;
	}

	public void setSubid(String subid){
		this.subid = subid;
	}

	public FinanceExport getFinanceExport(){
		return financeExport;
	}

	public void setFinanceExport(FinanceExport financeExport){
		this.financeExport = financeExport;
	}

	public FinanceDomExport getFinanceDomExport(){
		return financeDomExport;
	}

	public void setFinanceDomExport(FinanceDomExport financeDomExport){
		this.financeDomExport = financeDomExport;
	}
}
