package com.cjit.vms.taxServer.action;

import com.cjit.vms.taxServer.model.TaxInformationQuery;
import com.cjit.vms.taxServer.model.parseXMl.TaxKeyQueryReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;

/**
 * @author tom
 * 税控 钥匙信息查询
 */
public class TaxKeyQueryAction extends BaseTaxSelverAction {

	private static final long serialVersionUID = 1L;
	public void createTaxKeyQueryXml() throws Exception{
		TaxInformationQuery taxkey=new TaxInformationQuery();
		taxkey.setId(TaxSelvetUtil.id_key);
		taxkey.setApplyTypeCode(TaxSelvetUtil.apply_type_ch);
		taxkey.setComment(TaxSelvetUtil.comment_servlet_key);
		taxkey.setTaxKeypwd(TaxSelvetUtil.Tax_key_pwd);
		String result=taxkey.createTaxKeyQueryXml();
		printWriterResult(result);
	}
	/**
	 * @throws Exception 解析xml 文件
	 */
	public void parseTaxKeyReturnXml() throws Exception{
		String Data=request.getParameter("param");
		TaxInformationQuery taxKey=new TaxInformationQuery();
		TaxKeyQueryReturnXml taxReturnXml=taxKey.ParserTaxKeyQueryXml(Data);
		taxKey.outCurrentBillXmlFile(Data);
		System.out.println(taxReturnXml.CreateTaxKeyToString());
		printWriterResult(taxReturnXml.CreateTaxKeyToString());
	}
}
