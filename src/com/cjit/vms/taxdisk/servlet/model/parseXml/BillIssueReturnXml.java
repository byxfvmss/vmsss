package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;
public class BillIssueReturnXml extends BaseReturnXml {
	/**
	 * 发票代码
	 */
	private static final String bill_Code_ch="fpdm";
	/**
	 * 发票号码
	 */
	private static final String bill_No_ch="fphm";
	/**
	 * 开票日期
	 */
	private static final String bill_issue_date_ch="kprq";
	/**税控码
	 * 
	 */
	private static final String tax_control_code="skm";
	/**校验码
	 * 
	 */
	private static final String check_code_pwd="ewm";
	/**
	 * 发票代码
	 */
	private String billCode;
	/**
	 * 发票号码
	 */
	private String billNo;
	/**
	 * 开具日期
	 */
	private String billIssueDate;
	/**
	 * 税控码
	 */
	private String taxControlCode;
	/**
	 * 校验码
	 */
	private String checkCode;
	public BillIssueReturnXml (String paramSetXml) throws Exception{
		
		if(paramSetXml.indexOf("<kpxx")==-1){
		//20160501  次段代码解决 二维码内容中带尖括号时  解析开票完成后的XML失败	
			paramSetXml = paramSetXml.substring(0,paramSetXml.indexOf("<skm>"))+paramSetXml.substring(paramSetXml.indexOf("</skm>")+6,paramSetXml.length());
			Document doc =StringToDocument(paramSetXml);
			Element body=getBodyElement(doc);
			
			Element returndata=body.getChild(return_data_ch);
			   returncode =body.getChildText(return_code_ch);
			   returnmsg=body.getChildText(return_Msg_ch);
			   this.Id=getReturnId(doc);
			if(returncode.equals(return_success)){
				billCode=returndata.getChildText(bill_Code_ch);
				billNo=returndata.getChildText(bill_No_ch);
				billIssueDate=returndata.getChildText(bill_issue_date_ch);
				taxControlCode=returndata.getChildText(tax_control_code);
				checkCode=returndata.getChildText(check_code_pwd);
			}
			
		}else{
			Document doc =StringToDocument(paramSetXml);
			Element body=getBodyElement(doc);
			
			Element returndata=body.getChild(return_data_ch);
			   returncode =body.getChildText(return_code_ch);
			   returnmsg=body.getChildText(return_Msg_ch);
			   this.Id=getReturnId(doc);
			
			Element kpxx = returndata.getChild("kpxx");
			
			Element group = kpxx.getChild("group");
			billCode = group.getChildText(bill_Code_ch);
			billNo = group.getChildText(bill_No_ch);
			billIssueDate = group.getChildText(bill_issue_date_ch);
			taxControlCode = group.getChildText(tax_control_code);
			
			
		}
		
		
		
		
	}
	public String getBillCode() {
		return billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public String getBillIssueDate() {
		return billIssueDate;
	}
	public String getTaxControlCode() {
		return taxControlCode;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public void setBillIssueDate(String billIssueDate) {
		this.billIssueDate = billIssueDate;
	}
	public void setTaxControlCode(String taxControlCode) {
		this.taxControlCode = taxControlCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	
}
