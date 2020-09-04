package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;
public class CurrentBillNoReturnXml extends BaseReturnXml{
/**
 * 发票代码
 */
private String billCode;
/**
 * 发票号码
 */
private String billNo;
/**
 * 当前发票代码
 */
private static final String curr_bill_code = "dqfpdm";
/**
 * 当前前发票号码
 */
private static final String curr_bill_no = "dqfphm";

/**
 * @param paramSetXml
 * @return 将返回的xml 字符串转化为实体类 CurrentBillNoReturnXml 
 * @throws Exception
 */
public CurrentBillNoReturnXml (String paramSetXml) throws Exception{
	Document doc =StringToDocument(paramSetXml);
	Element body=getBodyElement(doc);
	this.Id=getReturnId(doc);
	Element returndata=body.getChild(return_data_ch);
	returncode=body.getChildText(returncode_ch);
	returnmsg=body.getChildText(returnmsg_ch);
	if(returncode.equals(return_success)){
		 billNo=returndata.getChildText(curr_bill_no);
		 billCode=returndata.getChildText(curr_bill_code);
	}
	
	
}
public String getBillCode() {
	return billCode;
}
public String getBillNo() {
	return billNo;
}
public void setBillCode(String billCode) {
	this.billCode = billCode;
}
public void setBillNo(String billNo) {
	this.billNo = billNo;
}

}
