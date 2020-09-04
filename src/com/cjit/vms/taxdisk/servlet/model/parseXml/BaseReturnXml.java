package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;
import com.cjit.vms.taxdisk.servlet.model.BaseModel;
public class BaseReturnXml extends BaseModel {
protected String returncode;
protected String returnmsg;
/**
 * 返回代码
 */
public static final String return_code_ch = "returncode";
/**
 * 返回信息
 */
public static final String return_Msg_ch = "returnmsg";
/**
 * 返回数据
 */
public static final String return_data_ch = "returndata";
public static final String return_success = "0";

/**
 * @param billPrintXml
 * @return   
 * @throws Exception
 */
public  BaseReturnXml (String BaseXml) throws Exception{
	super();
	Document doc =StringToDocument(BaseXml);
	Element body=getBodyElement(doc);
	this.returncode =body.getChildText(return_code_ch);
	this. returnmsg=body.getChildText(return_Msg_ch);
	}
	public String getReturnId(Document doc){
		return doc.getRootElement().getAttributeValue(id_cH);
	}
	public String getReturncode() {
		return returncode;
	}
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
}
	public BaseReturnXml() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

