package com.cjit.ws.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.cjit.vms.trans.model.BillInfo;


public class XmlUtil {

	public static Element getRootElement(String xmlStr) {
		Element root = null;
		try {
			StringReader read = new StringReader(xmlStr);
			InputSource source = new InputSource(read);//利用字节流创建新的输入源
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(source);
			root = doc.getRootElement();
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return root;
	}
	
//	public static String getResponseXml(TransListInfo transList, String errorInfo) {
//		Element response = new Element("RESPONSE");
//		
//		Element head = new Element("HEAD");
//		head.addContent(new Element("REQSERIALNO").setText(transList.getReqSerialno()));
//		head.addContent(new Element("REQUESTTYPE").setText(transList.getRequestType()));
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		head.addContent(new Element("FLOWINTIME").setText(sdf.format(new Date())));
//		response.addContent(head);
//		
//		Element result = new Element("RESULT");
//		if ("".equals(errorInfo)) {
//			result.addContent(new Element("RESULTTYPE").setText("1"));
//		} else {
//			result.addContent(new Element("RESULTTYPE").setText("0"));
//			result.addContent(new Element("ERRORINFO").setText(errorInfo));
//		}
//		response.addContent(result);
//		
//		Document doc = new Document(response);
//		return xmlToString(doc);
//	}
//	
//	public static String xmlToString(Document doc) {
//		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
//		return xmlOut.outputString(doc);
//	}
//	
//	protected static URL url;
//
//	private static HttpURLConnection conn;
//
//	private static String URL = "http://10.75.41.1:9002/payment/PaymentForVATServlet";
//	 
//    public static String send (BillInfo billInfo, String reason) throws Exception {
//    	String xmlStr = setXmlStr(billInfo, reason);
//        String html = request(xmlStr, URL);
////        JSONObject var = new JSONObject(html);
//        System.out.println(html);
//        return html;
//    }
//
//
//	private static String setXmlStr(BillInfo billInfo, String reason) {
//		Element request = new Element("REQUEST");
//		
//		Element head = new Element("HEAD");
//		head.addContent(new Element("REQSERIALNO").setText(System.currentTimeMillis() + ""));
//		head.addContent(new Element("REQUESTTYPE").setText("0004"));
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		head.addContent(new Element("FLOWINTIME").setText(sdf.format(new Date())));
//		request.addContent(head);
//		
//		Element data = new Element("DATA");
//		data.addContent(new Element("BILL_ID").setText(billInfo.getBillId()));
//		data.addContent(new Element("BILL_CODE").setText(billInfo.getBillCode()));
//		data.addContent(new Element("BILL_NO").setText(billInfo.getBillNo()));
//		data.addContent(new Element("FAPIAO_TYPE").setText(billInfo.getFapiaoType()));
//		data.addContent(new Element("BILL_STATUS").setText(billInfo.getDataStatus()));
//		data.addContent(new Element("REASON").setText(reason));
//		data.addContent(new Element("Reviewperson").setText(billInfo.getReviewer()));
//		data.addContent(new Element("Billingdate").setText(billInfo.getBillDate()));
//		data.addContent(new Element("returncanldate").setText(billInfo.getBillBeginDate()));
//		request.addContent(data);
//		Document doc = new Document(request);
//		
//		return xmlToString(doc);
//	}
//
//	public static String request(String requestXML,String strURL) throws Exception {		 
//		ByteArrayOutputStream baos  = null;	    
//		String strReader = "";	     
//		try {	    	 
//			baos = new ByteArrayOutputStream();	         
//			byte[] vbytes = requestXML.getBytes();	        
//			if(vbytes!=null && vbytes.length>0) {	             
//				baos.write(vbytes);	         
//			}		         
//			//1、开启服务器	         
//			checkHTTP(strURL);	    	 
//			URL dataUrl = new URL(strURL);
//			conn = (HttpURLConnection) dataUrl.openConnection();		  	 
//			conn.setDoOutput(true);		  	
//			conn.setRequestMethod("POST");			
//			//2、发送报文信息		    
//			OutputStream raw = conn.getOutputStream();			
//			OutputStream out = new BufferedOutputStream(raw);				 			
//			out.write(baos.toByteArray());			
//			out.flush();			
//			out.close();	    	 
//			//3、获取返回报文信息	    	 
//			strReader = getReceiverMessage();	    	 
//			if (strReader == null || strReader.length() < 1){	            
//				throw new Exception("交互失败!");	         }	          	    
//		} catch (Exception ex) {	          
//			throw ex;	      
//		} finally {	          
//			if (baos != null) {	           	 
//				baos.flush();		    	 
//				baos.close();	          
//			}	     
//		}	      
//		return strReader;	 
//	}
//	/**
//	 * 检查URL合法性
//	 * @author zhanglina
//	 * @param urlString
//	 * @throws ProtocolException
//	 */
//
//	protected static void checkHTTP(String strURL) throws Exception {
//		try {
//			URL url = new URL(strURL);
//			if (url == null || !url.getProtocol().toUpperCase().equals("HTTP"))
//				throw new Exception();
//
//		} catch (MalformedURLException m) {
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 从输入流中读取回执文件
//	 * @author zhanglina
//	 * @return
//	 */
//
//	public static String getReceiverMessage() throws Exception {
//		int c;
//		String xmlString    = "";
//		try {
//			InputStream raw = conn.getInputStream();
////			InputStream in  = new BufferedInputStream(raw);
////			Reader reader   = new InputStreamReader(in);
//			BufferedInputStream bis = new BufferedInputStream(raw);
//
//			// 输出在后台
//			System.out.println("==================Beging====================");
//			byte[] b = new byte[50];
//			
////			while ((c = bufreader.readLine()) != -1) {
////				 (char) c;
////			}
//			while ((c=bis.read(b)) != -1) {
//				xmlString += new String(b, 0, c);
//			}
//			bis.close();
//			System.out.println("===================End======================");
//
//		} catch (IOException e) {
//			new IOException("无法取得回执文件");
////			throw e;
//			return null;
//		}
//		return new String(xmlString);
//	}
	
}
