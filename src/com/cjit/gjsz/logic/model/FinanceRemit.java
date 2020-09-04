package com.cjit.gjsz.logic.model;

public class FinanceRemit extends FinanceEntity{

	private DeclareRemit declareRemit;
	private BaseRemit baseRemit;

	public DeclareRemit getDeclareRemit(){
		return declareRemit;
	}

	public void setDeclareRemit(DeclareRemit declareRemit){
		this.declareRemit = declareRemit;
	}

	public BaseRemit getBaseRemit(){
		return baseRemit;
	}

	public void setBaseRemit(BaseRemit baseRemit){
		this.baseRemit = baseRemit;
	}
}
