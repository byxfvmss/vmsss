package com.cjit.vms.trans.model.taxDisk;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.parseXml.TaxItemInfo;
import com.cjit.vms.trans.model.taxDisk.parseXml.TaxItemInfoQueryReturnXml;


public class TaxItemInfoQuery extends BaseDiskModel{

	/**
	 * 纳税人识别号
	 */
	private static final String tax_no_ch ="nsrsbh";
	/**
	 * 税控盘编号
	 */
	private static final String tax_disk_no_ch = "skpbh";
	/**
	 * 税控盘口令
	 */
	private static final String tax_disk_pwd_ch ="skpkl";
	/**
	 * 税务数字证书密码
	 */
	private static final String tax_data_cert_pwd_ch = "keypwd";
	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch = "fplxdm";
	private static final String paramXmlFile="税种税目信息查询.xml";
	
	
	
	/**
	 * 纳税人识别号&nbsp;&nbsp;
	 * n
	 */
	private String taxNo;
	/**
	 * 税控盘编号&nbsp;&nbsp;
	 * n
	 */
	private String taxDiskNo;
	/**
	 * 税控盘口令&nbsp;&nbsp;
	 * n
	 */
	private String taxDiskPwd;
	/**
	 * 税务数字证书密码&nbsp;&nbsp;
	 * n
	 */
	private String taxDataCertPwd;
	/**
	 * 发票类型代码&nbsp;&nbsp;
	 * n
	 */
	private String fapiaoType;
	
	/**
	 * @return 创建xml 文件 返回xml字符串
	 * @throws Exception
	 */
	public String createTaxItemInfoQueryXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,tax_no_ch,taxNo);
		addChildElementText(elements,tax_disk_no_ch,taxDiskNo);
		addChildElementText(elements,tax_disk_pwd_ch,taxDiskPwd);
		addChildElementText(elements,tax_data_cert_pwd_ch,taxDataCertPwd);
		addChildElementText(elements,fapiao_type_ch,fapiaoType);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,paramXmlFile);
		System.out.println(outString);
		return outString;
	}

	
	/**
	 * @param paramSet
	 * @throws Exception 输出税种税目信息查询的xml 文件
	 */
	public void outTaxItemInfoQueryXmlFile(String paramSet) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,paramXmlFile);
	}
	
	public TaxItemInfoQuery(String taxNo, String taxDiskNo, String taxDiskPwd,
			String taxDataCertPwd, String fapiaoType) {
		super();
		this.taxNo = taxNo;
		this.taxDiskNo = taxDiskNo;
		this.taxDiskPwd = taxDiskPwd;
		this.taxDataCertPwd = taxDataCertPwd;
		this.fapiaoType = fapiaoType;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}

	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
	}

	public String getTaxDataCertPwd() {
		return taxDataCertPwd;
	}

	public void setTaxDataCertPwd(String taxDataCertPwd) {
		this.taxDataCertPwd = taxDataCertPwd;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}


}
