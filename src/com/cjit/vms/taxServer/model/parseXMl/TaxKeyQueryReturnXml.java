package com.cjit.vms.taxServer.model.parseXMl;

public class TaxKeyQueryReturnXml extends BaseReturnXml{
	/*<?xml version="1.0" encoding="gbk"?>
	<business id="20002" comment="税控钥匙信息查询">
	<body yylxdm="1">
	<returncode>返回代码</returncode>
	<returnmsg>返回信息</returnmsg>
	<returndata>
	<nsrsbh>纳税人识别号</nsrsbh>
	<keyno>税控钥匙编号</keyno>
	</returndata>
	</body>
	</business>*/
/**
 * 纳税人识别号
 */
private  String taxNo;
/**税控钥匙编号
 * 
 */
private  String taxKey;
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
public String CreateTaxKeyToString(){
	String result="";
	if(this.getReturncode().equals("0")){
		result="1"+"|"+taxNo+"|"+taxKey;
	}else{
		result="0"+"|"+this.getReturnmsg();
	}
	
	return result;
}

}
