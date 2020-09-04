package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

public class BillPrintReturnXml extends BaseReturnXml{
	/**
	 * @param billPrintXml
	 * @return   
	 * @throws Exception
	 */
	public  BillPrintReturnXml (String billPrintXml) throws Exception{
		Document doc =StringToDocument(billPrintXml);
		Element body=getBodyElement(doc);
		 returncode =body.getChildText(return_code_ch);
		 returnmsg=body.getChildText(return_Msg_ch);
		 this.Id=getReturnId(doc);
		
		}

	public BillPrintReturnXml() {
		super();
	}
	

}
