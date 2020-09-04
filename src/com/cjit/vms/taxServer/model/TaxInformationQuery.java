package com.cjit.vms.taxServer.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxServer.model.parseXMl.TaxKeyQueryReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;


/**
 * @author tom
 * 税控信息查询 
 */
public class TaxInformationQuery extends BaseModel {
	
	 /**
	 * @return 创建税控钥匙信息查询 xml
	 * @throws Exception
	 */
	public String createTaxKeyQueryXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,tax_key_pwd_ch,taxKeypwd);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,taxkeyReturnXml);
		return outString;
	}
	 
	 public TaxKeyQueryReturnXml ParserTaxKeyQueryXml(String paramSetXml) throws Exception{
			Document doc =StringToDocument(paramSetXml);
			Element body=getBodyElement(doc);
			String returncode =body.getChildText(TaxSelvetUtil.return_code_ch);
			String returnmsg=body.getChildText(TaxSelvetUtil.return_Msg_ch);
			Element returndata=body.getChild(TaxSelvetUtil.return_data_ch);
			TaxKeyQueryReturnXml result=new TaxKeyQueryReturnXml();
			if(returncode.equals("0")){
			String taxNo=returndata.getChildText(tax_No_ch);
			String taxKeyNo=returndata.getChildText(tax_key__no_ch);
			result.setTaxKey(taxKeyNo);
			result.setTaxNo(taxNo);
			}
			result.setReturncode(returncode);
			result.setReturnmsg(returnmsg);
			return result;
			
		}
	 /**
	 * @param paramSet 输出返回文件
	 * @throws Exception
	 */
	public void outCurrentBillXmlFile(String paramSet) throws Exception{
			
			CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,taxkeyReturnXml);
		}
	/**
	 * 税控钥匙口令
	 */
	private String taxKeypwd;
	private static final String  tax_key_pwd_ch="keypwd";
	private static final String  taxkeyReturnXml="税控钥匙信息查询.xml";
	/**
	 * 纳税人识别号
	 */
	private static final String  tax_No_ch="nsrsbh";
	/**税控钥匙编号
	 * 
	 */
	private static final String  tax_key__no_ch="keyno";
	public String getTaxKeypwd() {
		return taxKeypwd;
	}
	public void setTaxKeypwd(String taxKeypwd) {
		this.taxKeypwd = taxKeypwd;
	}

}
