package com.cjit.gjsz.logic.model;

public class FinanceSettlement extends FinanceEntity{

	private String txcode;
	private String usetype;
	private String usedetail;

	public String getTxcode(){
		return txcode;
	}

	public void setTxcode(String txcode){
		this.txcode = txcode;
	}

	public String getUsetype(){
		return usetype;
	}

	public void setUsetype(String usetype){
		this.usetype = usetype;
	}

	public String getUsedetail(){
		return usedetail;
	}

	public void setUsedetail(String usedetail){
		this.usedetail = usedetail;
	}
}
