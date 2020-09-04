package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;
public class TaxKeyQueryReturnXml extends BaseReturnXml{
/**
 * 纳税人识别号
 */
private  String taxNo;
/**税控钥匙编号
 * 
 */
private  String taxKey;
/**
 * 纳税人识别号
 */
private static final String  tax_No_ch="nsrsbh";
/**税控钥匙编号
 * 
 */
private static final String  tax_key__no_ch="keyno";

public TaxKeyQueryReturnXml (String paramSetXml) throws Exception{
	super();
	Document doc =StringToDocument(paramSetXml);
	Element body=getBodyElement(doc);
	returncode =body.getChildText(return_code_ch);
	returnmsg=body.getChildText(return_Msg_ch);
	Element returndata=body.getChild(return_data_ch);
	if(returncode.equals(return_success)){
	taxNo=returndata.getChildText(tax_No_ch);
	taxKey=returndata.getChildText(tax_key__no_ch);
	}
	
	
}

public TaxKeyQueryReturnXml() {
	super();
	// TODO Auto-generated constructor stub
}

public String getTaxNo() {
	return taxNo;
}
public String getTaxKey() {
	return taxKey;
}
public void setTaxNo(String taxNo) {
	this.taxNo = taxNo;
}
public void setTaxKey(String taxKey) {
	this.taxKey = taxKey;
}

}
