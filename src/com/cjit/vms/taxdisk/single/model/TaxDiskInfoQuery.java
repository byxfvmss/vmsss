package com.cjit.vms.taxdisk.single.model;

import org.jdom.Document;
import org.jdom.Element;


public class TaxDiskInfoQuery extends BaseDiskModel {

	/**
	 * 税控盘口令&nbsp;&nbsp; n
	 */
	private static final String tax_disk_pwd_ch = "skpkl";
	private static final String paramXmlFile = "税控盘信息查询.xml";

	/**
	 * 税控盘口令
	 */
	private String taxDiskPwd;

	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}

	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
	}

	/**
	 * @return 创建xml 文件 返回xml字符串
	 * @throws Exception
	 */
	public String createTaxDiskInfoQueryXml() throws Exception {
		Element root = CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements = CreateBodyElement();		
		Element input=createInputElement();
		addChildElementText(input, tax_disk_pwd_ch, taxDiskPwd);
		elements.addContent(input);
		root.addContent(elements);
		String outString = CreateDocumentFormt(Doc, path_ch, paramXmlFile);
		System.out.println(outString);
		return outString;
	}


	
	
	// ------------------------------------

	/**
	 * @param paramSet
	 * @throws Exception
	 *             输出税控盘信息查询的xml 文件
	 */
	public void outTaxDiskInfoQueryXmlFile(String paramSet) throws Exception {

		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,
				paramXmlFile);
	}
}
