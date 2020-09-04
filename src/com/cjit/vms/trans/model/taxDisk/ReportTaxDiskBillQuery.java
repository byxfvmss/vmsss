package com.cjit.vms.trans.model.taxDisk;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;


import com.cjit.vms.trans.model.taxDisk.parseXml.BillBatch;
import com.cjit.vms.trans.model.taxDisk.parseXml.ReportTaxDiskBillQueryReturnXml;


/**
 * @author tom 报稅盘 发票查询
 *
 */
public class ReportTaxDiskBillQuery extends BaseDiskModel{

	/**
	*纳税人识别号  是否必须：是

	*/
	private static final String tax_no_ch="nsrsbh";
	/**
	*报税盘口令  是否必须：是

	*/
	private static final String report_tax_disk_ch="bspkl";
	/**
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	private static final String bill_field_info="fpdxx";
	private static final String group_ch="group";
	private static final String filename="报税盘发票查询.xml";

/**
 * @return 报税盘发票查询.xml
 * @throws Exception 
 */
public String createReportTaxDiskBillQueryXML () throws Exception{
	  Element root =CreateDoocumentHeard();
	  Document Doc = new Document(root);
	  Element elements =CreateBodyElement();
	  Element input=createInputElement();
	  addChildElementText(input,tax_no_ch,taxNo);
	  addChildElementText(input,report_tax_disk_ch,reportTaxDisk);
	  addChildElementText(input,fapiao_type_ch,fapiaoType);
	  elements.addContent(input);
	  root.addContent(elements);
	  String outString=CreateDocumentFormt(Doc, path_ch,filename);
	  System.out.println(outString);
	  return outString;
	}


/**
*返回代码  是否必须：是
0成功，其它失败
*/
private static final String return_code_ch="returncode";
/**
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";
/**
*纳税人识别号 是否必须：是

*/
private String taxNo;
/**
*报税盘口令 是否必须：是

*/
private String reportTaxDisk;
/**
*发票类型代码 是否必须：是

*/
private String fapiaoType;
public String getTaxNo() {
	return taxNo;
}
public void setTaxNo(String taxNo) {
	this.taxNo = taxNo;
}
public String getReportTaxDisk() {
	return reportTaxDisk;
}
public void setReportTaxDisk(String reportTaxDisk) {
	this.reportTaxDisk = reportTaxDisk;
}
public String getFapiaoType() {
	return fapiaoType;
}
public void setFapiaoType(String fapiaoType) {
	this.fapiaoType = fapiaoType;
}



}
