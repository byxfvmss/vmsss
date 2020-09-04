package com.cjit.vms.taxdisk.single.model.parseXml;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class BillPrintReturnXML extends BaseDiskModel{

/*
*发票代码  是否必须：是

*/
private static final String bill_code_ch="fpdm";
/*
*发票号码  是否必须：是

*/
private static final String bill_no_ch="fphm";

/*
*返回代码 是否必须：是
00000000成功，其它失败
*/
private String returnCode;
/*
*返回信息 是否必须：是

*/

public BillPrintReturnXML   (String StringXml) throws Exception{
	  Document doc =StringToDocument(StringXml);
	  Element root=doc.getRootElement();
	  this.comment=root.getAttributeValue(comment_cH);
	  this.Id=root.getAttributeValue(id_cH);
	  Element body=getBodyElement(doc);
	  Element output=body.getChild(out_put_ch);
	  Element printNum=output.getChild(print_bill_num);
	 
	  List listEemrnt=printNum.getChildren();
	  List  billlist=new ArrayList();
	  Bill bill=null;
	  for(int i=0;i<listEemrnt.size();i++){
		  bill=new Bill();
		  Element e=(Element) listEemrnt.get(i);
		  bill.setBillCode(e.getChildText(bill_code_ch));
		  bill.setBillNo(e.getChildText(bill_no_ch));
		  billlist.add(bill);
	  }
	  
	  this.listBill=billlist;
	  this.returnCode=output.getChildText(return_code_ch);
	  this.returnMsg=output.getChildText(return_msg_ch);
	}
private static final String out_put_ch="output";
/*
*返回代码  是否必须：是
00000000成功，其它失败
*/
private static final String return_code_ch="returncode";
/*
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";
private static final String print_bill_num="dyfpfs";
private String returnMsg;
private List<Bill> listBill;

public String getReturnCode() {
	return returnCode;
}
public void setReturnCode(String returnCode) {
	this.returnCode = returnCode;
}
public String getReturnMsg() {
	return returnMsg;
}
public void setReturnMsg(String returnMsg) {
	this.returnMsg = returnMsg;
}
public List<Bill> getListBill() {
	return listBill;
}
public void setListBill(List<Bill> listBill) {
	this.listBill = listBill;
}


}
