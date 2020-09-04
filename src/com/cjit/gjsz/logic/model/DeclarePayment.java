package com.cjit.gjsz.logic.model;

public class DeclarePayment extends DeclareEntity{

	private BasePayment basePayment;

	public BasePayment getBasePayment(){
		return basePayment;
	}

	public void setBasePayment(BasePayment basePayment){
		this.basePayment = basePayment;
	}
}
