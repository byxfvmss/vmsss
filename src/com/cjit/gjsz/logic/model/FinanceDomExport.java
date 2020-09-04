package com.cjit.gjsz.logic.model;

import java.math.BigInteger;

public class FinanceDomExport extends DeclareEntity{

	private String payattr;
	private BigInteger chkamt;
	private BaseDomPayment baseDomPayment;
	private BaseDomRemit baseDomRemit;
	private BaseExport baseExport;
	private String contrno;
	private String invoino;
	private String regno;
	private String cusmno;
	private String billno;
	private BigInteger contamt;

	public String getContrno(){
		return contrno;
	}

	public void setContrno(String contrno){
		this.contrno = contrno;
	}

	public String getInvoino(){
		return invoino;
	}

	public void setInvoino(String invoino){
		this.invoino = invoino;
	}

	public String getRegno(){
		return regno;
	}

	public void setRegno(String regno){
		this.regno = regno;
	}

	public String getCusmno(){
		return cusmno;
	}

	public void setCusmno(String cusmno){
		this.cusmno = cusmno;
	}

	public String getBillno(){
		return billno;
	}

	public void setBillno(String billno){
		this.billno = billno;
	}

	public BigInteger getContamt(){
		return contamt;
	}

	public void setContamt(BigInteger contamt){
		this.contamt = contamt;
	}

	public String getPayattr(){
		return payattr;
	}

	public void setPayattr(String payattr){
		this.payattr = payattr;
	}

	public BigInteger getChkamt(){
		return chkamt;
	}

	public void setChkamt(BigInteger chkamt){
		this.chkamt = chkamt;
	}

	public BaseDomPayment getBaseDomPayment(){
		return baseDomPayment;
	}

	public void setBaseDomPayment(BaseDomPayment baseDomPayment){
		this.baseDomPayment = baseDomPayment;
	}

	public BaseExport getBaseExport(){
		return baseExport;
	}

	public void setBaseExport(BaseExport baseExport){
		this.baseExport = baseExport;
	}

	public BaseDomRemit getBaseDomRemit(){
		return baseDomRemit;
	}

	public void setBaseDomRemit(BaseDomRemit baseDomRemit){
		this.baseDomRemit = baseDomRemit;
	}
}
