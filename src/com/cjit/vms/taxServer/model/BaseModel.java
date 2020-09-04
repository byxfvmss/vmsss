package com.cjit.vms.taxServer.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;


import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.service.impl.VmsCommonServiceImpl;

public class BaseModel {
	public static final String path_ch=new BaseModel().GetPathsty()+"//Taxselver//";
	public static final String path_out_ch=new BaseModel().GetPathsty()+"//Taxoutselver//";
	public static final String body_cH="body"; //身体
	public static final String id_cH="id"; //身体
	public static final String business_cH="business"; //身体
	public static final String comment_cH="comment"; //
	public static final String returncode_ch="returncode"; //
	public static final String returnmsg_ch="returnmsg"; //
	public static final String returndata_ch="returndata"; //
	
	/**
	 * 交易编号
	 */
	protected String Id; 
	/**
	 * 应用类型代码
	 */
	protected String applyTypeCode;
	/**
	 *   交易描述
	 */
	protected String comment;
	public String getId() {
		return Id;
	}
	public String getApplyTypeCode() {
		return applyTypeCode;
	}
	public String getComment() {
		return comment;
	}
	public void setId(String id) {
		Id = id;
	}
	public void setApplyTypeCode(String applyTypeCode) {
		this.applyTypeCode = applyTypeCode;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 应用类型代码
	 * 
	 */
	
	public static final String Application_type_code="yylxdm";
	/**
	 * //开票终端唯一标识
	 */
	public static final String open_bill_port_marking="kpzdbs";
	/**
	 * @param applyCode
	 * @return body根元素
	 */
	public Element CreateBodyElement(){
		return new Element(body_cH).setAttribute(Application_type_code,applyTypeCode);
		
	}
	public Element CreateElement(String element_ch,String falg,String value){
		return new Element(element_ch).setAttribute(falg,value);
		
	}
	
	/**
	 * @param elements 元素
	 * @param label 标签 
	 * @param value text值 添加子元素
	 */
	public void addChildElementText(Element elements,String label,String value){
		elements.addContent(new Element(label).setText(value));
	}
	/**
	 * @param Id
	 * @param comment
	 * @return 设置交易信息 表头
	 */
	public Element CreateDoocumentHeard(){
		Element root=new Element(business_cH).setAttribute(id_cH,Id).setAttribute(comment_cH, comment);
		return root;
	}
	/**构造模型 尾部
	 * @param Doc dom 结构
	 * @param path 输出xml 路径 文档格式化   文件格式化输出
	 * @throws Exception
	 */
	public String CreateDocumentFormt(Document Doc,String path,String fileName) throws Exception{
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
		String outString = xmlOut.outputString(Doc);
		/*Format format = Format.getPrettyFormat();
		XMLOutputter XMLOut = new XMLOutputter(format);
		createFile(path);
		XMLOut.output(Doc, new FileOutputStream(path+fileName));
		System.out.println();*/
		return outString;
	}
	/**
	 * @param issueRes
	 * @return 转化为dom文件
	 * @throws Exception
	 */
	public Document StringToDocument(String issueRes) throws Exception{
		StringReader read = new StringReader(issueRes);
		InputSource source = new InputSource(read);//利用字节流创建新的输入源
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(source);
		return doc;
	}
	/**
	 * @param doc
	 * @return 获取身体元素
	 */
	public Element getBodyElement(Document doc){
		Element  root=doc.getRootElement();
		return root.getChild(body_cH);
	}
	public void createFile(String path) throws Exception{

		File file =new File(path);    
 
		if  (!file .exists()  && !file .isDirectory())      
		{       
			//file.setReadable(true);
			//file.setWritable(true);
			//Runtime.getRuntime().exec(new String[]{"chmod-R777",path});
		    System.out.println("//不存在");  
		    file .mkdir();    
		} else   
		{  
		    System.out.println("//目录存在");  
		} 

	}
	/**
	 * @param billPrintXml
	 * @return   
	 * @throws Exception
	 */
	public  BaseReturnXml ParseBaseReurnXml(String billPrintXml) throws Exception{
		Document doc =StringToDocument(billPrintXml);
		Element body=getBodyElement(doc);
		String returncode =body.getChildText(TaxSelvetUtil.return_code_ch);
		String returnmsg=body.getChildText(TaxSelvetUtil.return_Msg_ch);
		
		BaseReturnXml result=new BaseReturnXml();
		result.setReturncode(returncode);
		result.setReturnmsg(returnmsg);
		
		return result;
		}
	public  String  GetPathsty(){
		//HttpServletRequest request=ServletActionContext.getRequest(); 
		String classPath=VmsCommonServiceImpl.getClassPath(this.getClass());
		String rooPath = classPath.replaceAll("WEB-INF/classes/","");
		int j=rooPath.indexOf("WEB-INF");
		String rootPath=rooPath.substring(0,j);
		//System.out.println(rooPath);
		//System.out.println(request.getRequestURI());
		//System.out.println( request.getSession().getServletContext().getRealPath("/"));
		return rooPath;
	}
	
}
