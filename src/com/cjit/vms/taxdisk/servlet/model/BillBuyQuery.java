package com.cjit.vms.taxdisk.servlet.model;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.model.parseXml.Bill;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillBuyQueryReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;

public class BillBuyQuery extends BaseModel {
	/**
	 * 发票类型代码
	 */
	private String fapiaoType;
	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch="fplxdm";
	/**
	 * 开票终端标识
	 */
	protected static final String bill_terminal_flag="kpzdbs";
	private static final String billQueryXml="发票领购信息查询.xml";
	/**
	 * 开票终端标识
	 */
	protected String billTerminalFlag;
	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	/**
	 * @return 发票领购信息查询 xml
	 * @throws Exception
	 */
	public String createBillBuyQueryXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_type_ch,fapiaoType);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,fapiaoType+billQueryXml);
		return outString;
	}
	/**
	 * @param paramSetXml
	 * @return
	 * @throws Exception
	 */
	/*public BillBuyQueryReturnXml ParserBillBuyQueryXml(String paramSetXml) throws Exception{
		Document doc =StringToDocument(paramSetXml);
		Element body=getBodyElement(doc);
		String returncode =body.getChildText(return_code_ch);
		String returnmsg=body.getChildText(return_Msg_ch);
		Element returndata=body.getChild(return_data_ch);
		BillBuyQueryReturnXml result=new BillBuyQueryReturnXml();
		result.setReturncode(returncode);
		result.setReturnmsg(returnmsg);
		if(returncode.equals("0")){
		String CurbillNo=returndata.getChildText(cur_no_bill_No_ch);
		String CurbillCode=returndata.getChildText(cur_no_bill_code_ch);
		String sumsurcount=returndata.getChildText(sum_sur_count_ch);
		Element lgxx=returndata.getChild(sum_Type_ch);
		result.setCurrentBillCode(CurbillCode);
		result.setCurrentBillNo(CurbillNo);
		result.setSumsurplusCount(sumsurcount);
		List group=lgxx.getChildren(group_ch);
		List list=new ArrayList();
		for(int i=0;i<group.size();i++){
			Element element=(Element) group.get(i);
			String billCode=element.getChildText(bill_code_ch);
			String startbillNo=element.getChildText(bill_start_no_ch);
			String endbillNo=element.getChildText(bill_end_no_ch);
			String billCount=element.getChildText(bill_count_ch);	
			String billsurCount=element.getChildText(bill_sur_count);
			String buyDate=element.getChildText(buy_date_ch);
			String buyPeople=element.getChildText(buy_people_ch);
			Bill bill=new Bill();
			bill.setBillCode(billCode);
			bill.setBillCount(billCount);
			bill.setBuyDate(buyDate);
			bill.setBuyPeople(buyPeople);
			bill.setEndBillNo(endbillNo);
			bill.setStartBillNo(startbillNo);
			bill.setSurplusCount(billsurCount);
			list.add(bill);
			
		}
		result.setBillList(list);
		}
		return result;
		
	}*/
	public void outBillBuyXmlFile(String paramSet) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,fapiaoType+billQueryXml);
	}
	/*	<?xml version="1.0" encoding="gbk"?>
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
	
	/**
	 * 当前未开发票代码
	 */
	private static final String cur_no_bill_code_ch="dqfpdm";
	/**当前未开发票号码
	 * 
	 */
	private static final String cur_no_bill_No_ch="dqfphm";
	/**
	 * 总剩余份数
	 */
	private static final String sum_sur_count_ch="zsyfs";
	/**
	 * 组数
	 */
	private static final String sum_Type_ch="lgxx";
	/**
	 * group
	 */
	private static final String group_ch="group";
	/**
	 * 发票 组数
	 */
	private static final String count_ch="count";
	/**
	 *  发票 组顺序
	 */
	private static final String xh_ch="xh";
	/**
	 * 发票代码
	 */
	private static final String bill_code_ch="fpdm";
	/**发票起始号码
	 * 
	 */
	private static final String bill_start_no_ch="qshm";
	/**发票终止号码
	 * 
	 */
	private static final String bill_end_no_ch="zzhm";
	/**发票份数
	 * 
	 */
	private static final String bill_count_ch="fpfs";
	/**剩余份数
	 * 
	 */
	private static final String bill_sur_count="syfs";
	/**
	 * 领购人员
	 */
	private static final String buy_people_ch="lgry";
	/**领购日期
	 * 
	 */
	private static final String buy_date_ch="lgrq";
	public String getBillTerminalFlag() {
		return billTerminalFlag;
	}

	public void setBillTerminalFlag(String billTerminalFlag) {
		this.billTerminalFlag = billTerminalFlag;
	}
	
}
