package com.cjit.vms.taxServer.action;

import com.cjit.vms.taxServer.model.BillBuyQuery;
import com.cjit.vms.taxServer.model.parseXMl.BillBuyQueryReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;

/**
 * @author tom
 * 发票领购信息查询  
 */
public class BillBuyQueryServletAction extends BaseTaxSelverAction{
	private static final long serialVersionUID = 1L;
	/**
	 * @throws Exception 创建领购信息查询xml 文件
	 */
	public  void createBillBuyQueryXml() throws Exception{
		String fapiaoType=request.getParameter("fapiaoType").equals("0")?"004":"007";
		BillBuyQuery billBuyQuery=new BillBuyQuery();
		billBuyQuery.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		billBuyQuery.setComment(TaxSelvetUtil.comment_bill_buy);
		billBuyQuery.setBillTerminalFlag(TaxSelvetUtil.bill_TerminalFlag_ch);
		billBuyQuery.setFapiaoType(fapiaoType);
		billBuyQuery.setId(TaxSelvetUtil.id_buy_query);
		String result=billBuyQuery.createBillBuyQueryXml();
		printWriterResult(result);
	}
	/**
	 * @throws Exception 解析xml 文件
	 */
	public void parseBillBuyQueryReturnXml() throws Exception{
		String param=request.getParameter("param");
		String fapiaoType=request.getParameter("fapiaoType").equals("0")?"004":"007";
		BillBuyQuery billBuyQuery=new BillBuyQuery();
		billBuyQuery.setFapiaoType(fapiaoType);
		BillBuyQueryReturnXml bill=billBuyQuery.ParserBillBuyQueryXml(param);
		String result=bill.createBillBuyQueryString();
		billBuyQuery.outBillBuyXmlFile(param);
		printWriterResult(result);
	}	
}
