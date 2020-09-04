package com.cjit.vms.taxdisk.servlet.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.model.parseXml.BillCancelReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

/**
 * @author tom
 * 发票作废
 */
public class BillCancel  extends BaseModel{
	/**
	 *  发票类型代码
	 */
	private static final String  fapiao_type_ch="fplxdm";
	/**
	 * 开票终端标识
	 */
	protected static final String bill_terminal_flag="kpzdbs";
	/**
	 * 作废类型 0：空白票作废1：已开票作废

	 */
	private static final String bill_cancel_type_ch="zflx";
	/**
	 * 发票代码
	 */
	private static final String bill_code_ch="fpdm";
	/**
	 * 发票号码
	 */
	private static final String bill_no_ch="fphm";
	/**
	 * 合计金额
	 */
	private static final String amt_sum_ch="hjje";
	/**
	 * 作废人
	 */
	private static final String cancel_people_ch="zfr";
	private static final String BillEmptyCancelXmlFile="发票空白作废.xml";
	private static final String BillCancelXmlFile="发票作废.xml";
	/**
	 * 开票终端标识
	 */
	private String billTerminalFlag;
	/**
	 * 发票类型
	 */
	private String fapiaoType;
	/**
	 *  作废类型 0：空白票作废 1：已开票作废

	 */
	private String CancelType;

	
	/**
	 *  发票代码
	 */
	private String billCode;
	/**
	 * 发票号码
	 */
	private String billNo;
	/**
	 * 合计金额
	 */
	private String AmtSum;
	/**
	 * 作废人
	 */
	private String CancelPeople;
	
	
	public BillCancel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param bill  票据信息学
	 * @param fapiaoType 发票类型
	 * @param cancelPeople 作废人呢
	 * @param cancelType 作废类型
	 */
	public BillCancel(BillInfo bill,String fapiaoType,String cancelPeople,String cancelType,VmsTaxKeyInfo taxKeyInfo) {
		super();
		this.CancelType = cancelType;
		this.fapiaoType = "0".equals(fapiaoType)?"004":"007";
		this.billCode = bill.getBillCode();
		this.billNo = bill.getBillNo();
		this.AmtSum = bill.getAmtSum();
		this.CancelPeople = cancelPeople;
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
		this.Id=TaxSelvetUtil.id_Cancel;
		this.comment=TaxSelvetUtil.comment_bill_Cancel;
		this.billTerminalFlag=taxKeyInfo.getBilTerminalFlag();
		
	}

	/**
	 * @return 发票空白作废字符串 并输出xml
	 * 文件
	 * @throws Exception
	 */
	public String createBillCancelXml(String falg) throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		BillCancelDataAssemble(elements);//
		root.addContent(elements);
		String outString="";
		if(falg.endsWith("0")){
		 outString=CreateDocumentFormt(Doc, path_ch,BillEmptyCancelXmlFile);
		}else{
			outString=CreateDocumentFormt(Doc, path_ch,BillCancelXmlFile);
		}
		return outString;
	}
	/**
	 * @param elements 向body里装子元素
	 */
	public void BillCancelDataAssemble(Element elements){
		addChildElementText(elements,bill_terminal_flag,billTerminalFlag);
		addChildElementText(elements,fapiao_type_ch,fapiaoType);
		addChildElementText(elements,bill_cancel_type_ch,CancelType);
		addChildElementText(elements,bill_code_ch,billCode);
		addChildElementText(elements,bill_no_ch,billNo);
		addChildElementText(elements,amt_sum_ch,AmtSum);
		addChildElementText(elements,cancel_people_ch,CancelPeople);
		
	}
	
	/**
	 * @param paramSet
	 * @param falg 是否是空白作废 empty 为空白作废
	 * @throws Exception 
	 */
	public void outBillCancelXmlFile(String paramSet,String falg) throws Exception{
		if(falg.equals("0")){
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,BillEmptyCancelXmlFile);
		}else{
			CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,BillCancelXmlFile);
		}
	}
	/*<?xml version="1.0" encoding="gbk"?>
	<business id="10009" comment="发票作废">
	<body yylxdm="1">
	<returncode>返回代码</returncode>
	<returnmsg>返回信息</returnmsg>
	<returndata>
	<fpdm>发票代码</fpdm>
	<fphm>发票号码</fphm>
	<zfrq>作废日期</zfrq>
	</returndata>
	</body>
*/

}
