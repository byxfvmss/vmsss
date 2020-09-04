package com.cjit.vms.taxServer.model;



import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;


public class ParamSet extends BaseModel {
	
	/**
	 * @return 构造参数设置模型
	 * @throws Exception 
	 */
	public String createParamSetXml() throws Exception{
		
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		addChildElementText(elements,servle_ip_address,SelverIpAddress);
		addChildElementText(elements,servlet_port,servletport);
		addChildElementText(elements,key_pwd,TaxKeyPwd);
		addChildElementText(elements,aqmXML, aqm);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,paramXmlFile);
		return outString;
	}
	
	/**
	 * @param paramSetXml
	 * @return 单独的返回数据 
	 * @throws Exception
	 */
	public BaseReturnXml ParserParamSetToMap(String paramSetXml) throws Exception{
		Document doc =StringToDocument(paramSetXml);
		Element body=getBodyElement(doc);
		String returncode =body.getChildText("returncode");
		String returnmsg=body.getChildText("returnmsg");
		BaseReturnXml result=new BaseReturnXml();
		result.setReturncode(returncode);
		result.setReturnmsg(returnmsg);
		return result;
		
	}
	
	public void outPramaXmlFile(String paramSet) throws Exception{
		
		CreateDocumentFormt(StringToDocument(paramSet), path_out_ch,paramXmlFile);
	}
	

	/**
	 *  服务器IP地址
	 */
	private String SelverIpAddress;
	private String servletport;
	/**
	 * 税控钥匙口令
	 */
	private String TaxKeyPwd;
	private String aqm;
	
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
