package com.cjit.vms.taxdisk.servlet.model;



import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;


public class ParamSet extends BaseModel {

	/**
	 * 服务器IP地址
	 */
	public static final String servle_ip_address="servletip";
	
	/**
	 * 服务器端口号
	 */
	public static final String servlet_port="servletport";
	/**
	 * 税控钥匙口令
	 */
	public static final String key_pwd="keypwd";
	/**
	 * 注册码
	 */
	public static final String aqmXML = "aqm";
	
	/**
	 * 
	 */
	private static final String paramXmlFile ="参数设置.xml";

	/**
	 *  服务器IP地址
	 */
	private String SelverIpAddress;
	/**
	 * 服务器端口号
	 */
	private String servletport;
	/**
	 * 税控钥匙口令
	 */
	private String TaxKeyPwd;
	private String aqm;
	/**
	 * @return 构造参数设置模型
	 * @throws Exception 
	 */
	public String createParamSetXml(VmsTaxKeyInfo taxKeyInfo) throws Exception{
		
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,servle_ip_address,taxKeyInfo.getIpAddress());
		addChildElementText(elements,servlet_port,taxKeyInfo.getServletPort());
		addChildElementText(elements,key_pwd,TaxKeyPwd);
		addChildElementText(elements,aqmXML, aqm);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,paramXmlFile);
		return outString;
	}
	
	
	
	/**
	 * @param taxKeyPwd 税控钥匙口令
	 */
	public ParamSet(String taxKeyPwd) {
		super();
		this.SelverIpAddress = TaxSelvetUtil.selvet_Ip_address;
		this.servletport =TaxSelvetUtil.servlet_port;
		this.TaxKeyPwd = taxKeyPwd;
		this.aqm = TaxSelvetUtil.aqm_ch;
		this.Id=TaxSelvetUtil.id_param;
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
		this.comment=TaxSelvetUtil.comment_paramSet;
	}



	/*public void outPramaXmlFile(String paramSet) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,paramXmlFile);
	}*/

	
	public String getSelverIpAddress() {
		return SelverIpAddress;
	}
	public String getTaxKeyPwd() {
		return TaxKeyPwd;
	}
	
	public void setSelverIpAddress(String selverIpAddress) {
		SelverIpAddress = selverIpAddress;
	}
	public void setTaxKeyPwd(String taxKeyPwd) {
		TaxKeyPwd = taxKeyPwd;
	}
	


		public String getServletport() {
			return servletport;
		}

		public void setServletport(String servletport) {
			this.servletport = servletport;
		}

		public String getAqm() {
			return aqm;
		}

		public void setAqm(String aqm) {
			this.aqm = aqm;
		}

	

}
