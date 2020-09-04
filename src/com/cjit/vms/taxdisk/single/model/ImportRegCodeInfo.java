package com.cjit.vms.taxdisk.single.model;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;


public class ImportRegCodeInfo extends BaseDiskModel {

	/**
	 * 注册码信息
	 */
	private static final String reg_code_info_ch = "zcmxx";
	private static final String paramXmlFile = "注册码信息导入.xml";

	/**
	 * 注册码信息&nbsp;&nbsp; n
	 */
	private String regCodeInfo;

	public String getRegCodeInfo() {
		return regCodeInfo;
	}

	public void setRegCodeInfo(String regCodeInfo) {
		this.regCodeInfo = regCodeInfo;
	}

	/**
	 * @return 创建xml 文件 返回xml字符串
	 * @throws Exception
	 */
	public String createImportRegCodeInfoXml() throws Exception {
		Element root = CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements = CreateBodyElement();
		addChildElementText(elements, reg_code_info_ch, regCodeInfo);
		root.addContent(elements);
		String outString = CreateDocumentFormt(Doc, path_ch, paramXmlFile);
		System.out.println(outString);
		return outString;
	}

	public ImportRegCodeInfo(String regCodeInfo) {
		super();
		this.regCodeInfo = regCodeInfo;
		this.applyTypeCode=TaxDiskUtil.Application_type_code_1;
		this.comment=TaxDiskUtil.comment_Import_registration_code_information;
		this.Id=TaxDiskUtil.id_Import_registration_code_information;
	}

	// ------------------------------------

	public ImportRegCodeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param paramSet
	 * @throws Exception
	 *             输出 注册码信息的xml 文件
	 */
	public void outImportRegCodeInfoXmlFile(String paramSet) throws Exception {

		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,
				paramXmlFile);
	}
}
