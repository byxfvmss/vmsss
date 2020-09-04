package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

public class BillCancelReturnXml extends BaseReturnXml{
	/**
	 * 发票代码
	 */
	private String billCode;
	/**
	 * 发票号码
	 */
	private String billNo;
	/**
	 * 作废日期
	 */
	private String billCancelDate;
	/**
	 * 发票代码
	 */
	private static final String bill_code_ch="fpdm";
	/**
	 * 发票号码
	 */
	private static final String bill_no_ch="fphm";
	/**
	 * 作废日期
	 */
	private static final String bill_cancel_date_ch="zfrq";
	/**
	 * @param BillCancelXml
	 * @return    返回实体类 BillCancelReturnCancel 
	 * @throws Exception
	 */
	public BillCancelReturnXml (String BillCancelXml) throws Exception{
		Document doc =StringToDocument(BillCancelXml);
		Element body=getBodyElement(doc);
		returncode =body.getChildText(returncode_ch);
		returnmsg=body.getChildText(returnmsg_ch);
		this.Id=getReturnId(doc);
		Element returndata=body.getChild(returndata_ch);
		if(returncode.equals(return_success)){
			billNo=returndata.getChildText(bill_no_ch);
			billCode=returndata.getChildText(bill_code_ch);
			billCancelDate=returndata.getChildText(bill_cancel_date_ch);
		}
		
	}
	public String getBillCancelDate() {
		return billCancelDate;
	}

	public void setBillCancelDate(String billCancelDate) {
		this.billCancelDate = billCancelDate;
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
