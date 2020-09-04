package com.cjit.gjsz.logic.model;

import java.math.BigInteger;

public class CustomDeclare extends Entity{

	private String customn;
	private String custccy;
	private BigInteger custamt;
	private BigInteger offamt;
	private String crtuser;
	private String inptelc;
	private String rptdate;
	private String subid;
	private String type;
	private FinanceRemit financeRemit;
	private FinanceDomRemit financeDomRemit;

	public FinanceRemit getFinanceRemit(){
		return financeRemit;
	}

	public void setFinanceRemit(FinanceRemit financeRemit){
		this.financeRemit = financeRemit;
	}

	/**
	 * Description:
	 * @author
	 * @see
	 * @return the financeDomRemit
	 */
	public FinanceDomRemit getFinanceDomRemit(){
		return financeDomRemit;
	}

	/**
	 * Description:
	 * @author
	 * @see
	 * @param financeDomRemit the financeDomRemit to set
	 */
	public void setFinanceDomRemit(FinanceDomRemit financeDomRemit){
		this.financeDomRemit = financeDomRemit;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getCustomn(){
		return customn;
	}

	public void setCustomn(String customn){
		this.customn = customn;
	}

	public String getCustccy(){
		return custccy;
	}

	public void setCustccy(String custccy){
		this.custccy = custccy;
	}

	public BigInteger getCustamt(){
		return custamt;
	}

	public void setCustamt(BigInteger custamt){
		this.custamt = custamt;
	}

	public BigInteger getOffamt(){
		return offamt;
	}

	public void setOffamt(BigInteger offamt){
		this.offamt = offamt;
	}

	public String getCrtuser(){
		return crtuser;
	}

	public void setCrtuser(String crtuser){
		this.crtuser = crtuser;
	}

	public String getInptelc(){
		return inptelc;
	}

	public void setInptelc(String inptelc){
		this.inptelc = inptelc;
	}

	public String getRptdate(){
		return rptdate;
	}

	public void setRptdate(String rptdate){
		this.rptdate = rptdate;
	}

	public String getSubid(){
		return subid;
	}

	public void setSubid(String subid){
		this.subid = subid;
	}
}
