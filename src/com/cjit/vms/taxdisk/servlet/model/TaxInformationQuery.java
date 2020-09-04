package com.cjit.vms.taxdisk.servlet.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
/**
 * @author tom
 * 税控信息查询 
 */
public class TaxInformationQuery extends BaseModel {
	/**
	 * 税控钥匙口令
	 */
	private String taxKeypwd;
	private static final String  tax_key_pwd_ch="keypwd";
	private static final String  taxkeyReturnXml="税控钥匙信息查询.xml";
	
	 public TaxInformationQuery(String taxKeypwd) {
		super();
		this.taxKeypwd = taxKeypwd;
		this.Id=TaxSelvetUtil.id_key;
		this.comment=TaxSelvetUtil.comment_servlet_key;
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
	}

	 
	public TaxInformationQuery() {
		super();
		// TODO Auto-generated constructor stub
	}


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
	 
	
	 /**
	 * @param paramSet 输出返回文件
	 * @throws Exception
	 */
	public void outCurrentBillXmlFile(String paramSet) throws Exception{
			
			CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,taxkeyReturnXml);
		}


	public String getTaxKeypwd() {
		return taxKeypwd;
	}
	public void setTaxKeypwd(String taxKeypwd) {
		this.taxKeypwd = taxKeypwd;
	}

}
