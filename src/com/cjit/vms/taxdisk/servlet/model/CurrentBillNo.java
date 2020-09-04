package com.cjit.vms.taxdisk.servlet.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.model.parseXml.CurrentBillNoReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

public class CurrentBillNo extends BaseModel{
	/**
	 * 开票终端标识
	 */
	protected static final String bill_terminal_flag="kpzdbs";
	
	/**
	 * 发票类型代码
	 */
	public static final String fapiao_Type_code="fplxdm";
	private static final String paramXmlFile="当前未开票号码.xml";
	/**
	 * 开票终端标识
	 */
	protected String billTerminalFlag;
	/**
	 * 发票类型代码
	 */
	protected String fapiaoType;
	
	

public CurrentBillNo(String fapiaoType,VmsTaxKeyInfo taxKeyInfo) {
		super();
		this.billTerminalFlag = taxKeyInfo.getBilTerminalFlag();
		this.fapiaoType = fapiaoType.equals("0")?"004":"007";
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
		this.Id=TaxSelvetUtil.id_current_bill;
		this.comment=TaxSelvetUtil.comment_current_bill;
	}

public CurrentBillNo() {
	super();
	// TODO Auto-generated constructor stub
}

/**
 * @return 创建xml 文件 返回xml字符串
 * @throws Exception
 */
public String createCurrentBillXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_Type_code,fapiaoType);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,paramXmlFile);
		return outString;
	}
	

/**
 * @param paramSet
 * @throws Exception 输出 当前票号的xml 文件
 */
public void outCurrentBillXmlFile(String paramSet) throws Exception{
	
	CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,paramXmlFile);
}
public String getBillTerminalFlag() {
	return billTerminalFlag;
}

public String getFapiaoType() {
	return fapiaoType;
}

public void setFapiaoType(String fapiaoType) {
	this.fapiaoType = fapiaoType;
}

}
