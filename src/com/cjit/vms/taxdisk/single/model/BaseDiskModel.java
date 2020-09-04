package com.cjit.vms.taxdisk.single.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.cjit.vms.taxdisk.servlet.model.BaseModel;
import com.cjit.vms.trans.service.impl.VmsCommonServiceImpl;

public class BaseDiskModel {
	public static final String path_ch=new BaseModel().GetPathsty()+"//TaxDisk//";
	public static final String path_out_ch=new BaseModel().GetPathsty()+"//TaxoutDisk//";
	public static final String body_cH="body"; //身体
	public static final String id_cH="id"; //身体
	public static final String comment_cH="comment"; //
	public static final String business_cH="business"; //身体
	public static final String input_ch="input"; //
	
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

	public Element createInputElement(){
		return new Element(input_ch);
	}

	/**
	 * @param 
	 * @return input根元素
	 */
	public Element CreateInputElement(){
		return new Element(input_ch);
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
		Element root=new Element(business_cH).setAttribute(comment_cH, comment).setAttribute(id_cH,Id);
		return root;
	}
	/**构造模型 尾部
	 * @param Doc dom 结构
	 * @param path 输出xml 路径 文档格式化   文件格式化输出
	 * @throws Exception
	 */
	public String CreateDocumentFormt(Document Doc,String path,String fileName) throws Exception{
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setEncoding("GBK"));
		String outString = xmlOut.outputString(Doc);
		Format format = Format.getPrettyFormat();
		XMLOutputter XMLOut = new XMLOutputter(format);
		createFile(path);
		XMLOut.output(Doc, new FileOutputStream(path+fileName));
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
		this.comment=root.getAttributeValue(comment_cH);
		this.Id=root.getAttributeValue(id_cH);
		return root.getChild(body_cH);
	}
	public void createFile(String path) throws Exception{

		File file =new File(path);    
 
		if  (!file .exists()  && !file .isDirectory())      
		{       
			
		    System.out.println("//不存在");  
		    file .mkdir();    
		} else   
		{  
		    System.out.println("//目录存在");  
		} 

	}
	
	public  String  GetPathsty(){
		String classPath=VmsCommonServiceImpl.getClassPath(this.getClass());
		String rooPath = classPath.replaceAll("WEB-INF/classes/","");
		int j=rooPath.indexOf("WEB-INF");
		String rootPath=rooPath.substring(0,j);
		
		return rooPath;
	}
	/**
	 * @param str
	 * @return 首字母大写
	 */
	public String FirstToUp(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase())  ; 
	}
	public String FirstToDown(String str){
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase())  ; 
	}
	/**
	 * @param StringXml xml 字符串
	 * @param data		拼接的字符串
	 * @param className  类名
	 * @return   解析 Xml 方法
	 */
	public String CreatParseXMl(String StringXml,String data,String className){
		StringBuffer parseXML=new StringBuffer();
		parseXML.append("public "+className+"   parse"+className+"(String StringXml) throws Exception{\r\n");
		parseXML.append(blank_ch+"Document doc =StringToDocument(StringXml);\r\n");
		parseXML.append(blank_ch+"Element body=getBodyElement(doc);\r\n");
		parseXML.append(blank_ch+"Element output=body.getChild(out_put_ch);\r\n");
		  String[] datas=data.split(";");
		  String[] d=new String[datas.length];
		  String[] value=new String[datas.length];
		  parseXML.append(blank_ch+className+" "+FirstToDown(className)+"=new "+className+"();\r\n");
		 for(int i=0;i<datas.length;i++){
			  String[] infos=datas[i].split(",");
			  d[i]=infos[0].trim();
			  value[i]=infos[1].trim();
			  parseXML.append("  "+FirstToDown(className)+".set"+FirstToUp(value[i])+"(output."+"getChildText("+d[i]+"));\r\n");
		  }
		 parseXML.append("return "+FirstToDown(className) +";\r\n}");
		 System.out.println(parseXML.toString());
		//Element output=output.getChild("output");
		return parseXML.toString();
	}

	private static final String blank_ch="  ";
	protected static final String out_put_ch="output";
	/**
	 * @param data
	 * @return 输出实体类属性
	 */
	public String createEntityUtil(String data){
		String[] datas=data.split(";");
		  StringBuffer entityString= new StringBuffer();
		  for(int i=0;i<datas.length;i++){
		    String[] infos=datas[i].split(",");
		     entityString.append("/**\r\n*"+infos[3].trim()+"  是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private static final String ").append(infos[0].trim()).append("="+"\"").append(infos[2].trim()).append("\";\r\n");
		  }
		  entityString.append("------------------------------------\r\n");
		  for(int i=0;i<datas.length;i++){
		    String[] infos=datas[i].split(",");
		    entityString.append("/**\r\n*"+infos[3].trim()+" 是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private String ").append(infos[1].trim()+";\r\n");

		  }
		  System.out.println(entityString);
		  return entityString.toString();
	}

public String CreateXmlMethodBase(String className,String data){
	String[] datas=data.split(";");
	 // StringBuffer entityString= new StringBuffer();
	  
	StringBuffer parseXML=new StringBuffer();
	parseXML.append("public String create"+className+"XML () throws Exception{\r\n");
	parseXML.append(blank_ch+"Element root =CreateDoocumentHeard();\r\n");
	parseXML.append(blank_ch+"Document Doc = new Document(root);\r\n");
	parseXML.append(blank_ch+"Element elements =CreateBodyElement();\r\n");
	parseXML.append(blank_ch+"Element input=createInputElement();\r\n");
	  for(int i=0;i<datas.length;i++){
		    String[] infos=datas[i].split(",");
	parseXML.append(blank_ch+"addChildElementText(input,"+infos[0].trim()+","+infos[1].trim()+");\r\n");
		  }
	parseXML.append(blank_ch+"elements.addContent(input);\r\n");
	parseXML.append(blank_ch+"root.addContent(elements);\r\n");
	parseXML.append(blank_ch+"String outString=CreateDocumentFormt(Doc, path_ch,filename);\r\n");
	parseXML.append(blank_ch+"System.out.println(outString);\r\n");
	parseXML.append(blank_ch+"return outString;\r\n");
	parseXML.append("}");
	System.out.println(parseXML.toString());
	return parseXML.toString();
	
	
}
}
