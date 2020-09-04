package com.cjit.vms.taxdisk.servlet.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

public class ParamSetReturnXml extends BaseReturnXml{

	public ParamSetReturnXml() {
		super();
	}

	public ParamSetReturnXml(String ParamSetReturnXml) throws Exception {
		super();
		Document doc =StringToDocument(ParamSetReturnXml);
		Element body=getBodyElement(doc);
		this.returncode =body.getChildText(return_code_ch);
		this. returnmsg=body.getChildText(return_Msg_ch);
	}
	
}
