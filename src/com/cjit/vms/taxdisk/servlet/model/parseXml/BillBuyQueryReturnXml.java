package com.cjit.vms.taxdisk.servlet.model.parseXml;

import java.util.List;

public class BillBuyQueryReturnXml extends BaseReturnXml{
	/**
	 * 当前未开发票代码
	 */
	private String currentBillCode;
	/**
	 * 当前未开发票号码
	 */
	private String currentBillNo;
	/**
	 * 总剩余份数
	 */
	private String SumsurplusCount;
	private List billList;
	public String getCurrentBillCode() {
		return currentBillCode;
	}
	public String getCurrentBillNo() {
		return currentBillNo;
	}
	public String getSumsurplusCount() {
		return SumsurplusCount;
	}
	public List getBillList() {
		return billList;
	}
	public void setCurrentBillCode(String currentBillCode) {
		this.currentBillCode = currentBillCode;
	}
	public void setCurrentBillNo(String currentBillNo) {
		this.currentBillNo = currentBillNo;
	}
	public void setSumsurplusCount(String sumsurplusCount) {
		SumsurplusCount = sumsurplusCount;
	}
	public void setBillList(List billList) {
		this.billList = billList;
	}
	public String createBillBuyQueryString(){
		
		String result="";
		if(this.getReturncode().equals("0")){
		result="1"+"|"+currentBillCode+"|"+currentBillNo+"|"+SumsurplusCount;
		if(billList!=null){
		for(int i=0;i<billList.size();i++){
			Bill bill=new Bill();
			bill=(Bill) billList.get(i);
			result=result+"|"+bill.getBillCode()
			+"^"+bill.getStartBillNo()
			+"^"+bill.getEndBillNo()
			+"^"+bill.getBillCount()
			+"^"+bill.getSurplusCount()
			+"^"+bill.getBuyDate()
			+"^"+bill.getBuyPeople()
			;
			}
		}
		}else{
			result="0"+this.getReturnmsg();
		}
		return result;
	}



}
/*<?xml version="1.0" encoding="gbk"?>
<business id="10005" comment="发票领购信息查询">
<body yylxdm="1">
<returncode>返回代码</returncode>
<returnmsg>返回信息</returnmsg>
<returndata>
<dqfpdm>当前未开发票代码</dqfpdm>
<dqfphm>当前未开发票号码</dqfphm>
<zsyfs>总剩余份数</zsyfs>
<lgxx count="2">
<group xh="1">
<fpdm>发票代码</fpdm>
<qshm>起始号码</qshm>
<zzhm>终止号码</zzhm>
<fpfs>发票份数</fpfs>
<syfs>剩余份数</syfs>
<lgrq>领购日期</lgrq>
<lgry>领购人员</lgry>
</group>
<group xh="2">
<fpdm>发票代码</fpdm>
<qshm>发票起始号码</qshm>
<zzhm>发票终止号码</zzhm>
<fpfs>发票份数</fpfs>
<syfs>剩余份数</syfs>
<lgrq>领购日期</lgrq>
<lgry>领购人员</lgry>
</group>
</lgxx>
</returndata>
</body>
</business>*/

