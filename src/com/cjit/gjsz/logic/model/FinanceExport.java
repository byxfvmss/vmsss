package com.cjit.gjsz.logic.model;

public class FinanceExport extends FinanceEntity{

	private BaseExport baseExport;

	public BaseExport getBaseExport(){
		return baseExport;
	}

	public void setBaseExport(BaseExport baseExport){
		this.baseExport = baseExport;
	}
}
