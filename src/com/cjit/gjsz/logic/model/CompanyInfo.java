package com.cjit.gjsz.logic.model;

public class CompanyInfo extends Entity{

	private String custcode;
	private String custname;
	private String areacode;
	private String custaddr;
	private String postcode;
	private String industrycode;
	private String attrcode;
	private String countrycode;
	private String istaxfree;
	private String taxfreecode;
	private String invcountrycode;
	private String email;
	private String rptmethod;
	private String ecexaddr;
	private String remarks;
	private String rptno;
	private String batchno;
	private String datastatus;
	private String instcode;

	public String getInstcode(){
		return instcode;
	}

	public void setInstcode(String instcode){
		this.instcode = instcode;
	}

	public String getDatastatus(){
		return datastatus;
	}

	public void setDatastatus(String datastatus){
		this.datastatus = datastatus;
	}

	public String getBatchno(){
		return batchno;
	}

	public void setBatchno(String batchno){
		this.batchno = batchno;
	}

	public String getRptno(){
		return rptno;
	}

	public void setRptno(String rptno){
		this.rptno = rptno;
	}

	public String getCustcode(){
		return custcode;
	}

	public void setCustcode(String custcode){
		this.custcode = custcode;
	}

	public String getCustname(){
		return custname;
	}

	public void setCustname(String custname){
		this.custname = custname;
	}

	public String getAreacode(){
		return areacode;
	}

	public void setAreacode(String areacode){
		this.areacode = areacode;
	}

	public String getCustaddr(){
		return custaddr;
	}

	public void setCustaddr(String custaddr){
		this.custaddr = custaddr;
	}

	public String getPostcode(){
		return postcode;
	}

	public void setPostcode(String postcode){
		this.postcode = postcode;
	}

	public String getIndustrycode(){
		return industrycode;
	}

	public void setIndustrycode(String industrycode){
		this.industrycode = industrycode;
	}

	public String getAttrcode(){
		return attrcode;
	}

	public void setAttrcode(String attrcode){
		this.attrcode = attrcode;
	}

	public String getCountrycode(){
		return countrycode;
	}

	public void setCountrycode(String countrycode){
		this.countrycode = countrycode;
	}

	public String getIstaxfree(){
		return istaxfree;
	}

	public void setIstaxfree(String istaxfree){
		this.istaxfree = istaxfree;
	}

	public String getTaxfreecode(){
		return taxfreecode;
	}

	public void setTaxfreecode(String taxfreecode){
		this.taxfreecode = taxfreecode;
	}

	public String getInvcountrycode(){
		return invcountrycode;
	}

	public void setInvcountrycode(String invcountrycode){
		this.invcountrycode = invcountrycode;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getRptmethod(){
		return rptmethod;
	}

	public void setRptmethod(String rptmethod){
		this.rptmethod = rptmethod;
	}

	public String getEcexaddr(){
		return ecexaddr;
	}

	public void setEcexaddr(String ecexaddr){
		this.ecexaddr = ecexaddr;
	}

	public String getRemarks(){
		return remarks;
	}

	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

	private String areacodename;

	public String getAreacodename(){
		return areacodename;
	}

	public void setAreacodename(String areacodename){
		this.areacodename = areacodename;
	}
}
