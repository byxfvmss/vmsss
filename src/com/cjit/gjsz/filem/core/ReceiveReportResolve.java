/**
 *  报文生成的解析类
 */
package com.cjit.gjsz.filem.core;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.interfacemanager.model.UserInterface;

/**
 * @author huboA
 */
public class ReceiveReportResolve{

	public static final String MSG = "MSG";
	public static final String APPTYPE = "APPTYPE";
	public static final String CURRENTFILE = "CURRENTFILE";
	public static final String INOUT = "INOUT";
	public static final String FORMATERRS = "FORMATERRS";
	public static final String FORMATS = "FORMATS";
	public static final String FORMAT = "FORMAT";
	public static final String TOTALRECORDS = "TOTALRECORDS";
	public static final String SUCRECORDS = "SUCRECORDS";
	public static final String FALRECORDS = "FALRECORDS";
	public static final String ERRRECORDS = "ERRRECORDS";
	public static final String REC = "REC";
	public static final String RPTNO = "RPTNO";
	public static final String ERRFIELDS = "ERRFIELDS";
	public static final String ERR = "ERR";
	public static final String ERRFIELD = "ERRFIELD";
	public static final String ERRFIELDCN = "ERRFIELDCN";
	public static final String ERRDESC = "ERRDESC";

	/**
	 * 根据用户数据File取得要导记录
	 * @param file
	 * @return
	 */
	public static List getDate(File file, UserInterface userInterface){
		// if (StringUtil.equalsIgnoreCase(userInterface.getFileType(), "txt"))
		// {
		// return importTxtFile(file, userInterface);
		// } else if (StringUtil.equalsIgnoreCase(userInterface.getFileType(),
		// "xls")) {
		// return importExcelFile(file, userInterface);
		// }
		throw new RuntimeException("导入文件的格式无法识别.");
	}

	/**
	 * 解析文件格式错误
	 * @param root
	 */
	private static String resolveFileError(Element root){
		StringBuffer sb = new StringBuffer();
		Element appType = root.element(APPTYPE);
		System.out.println(appType.getText());
		// sb.append("<p>应用类型为 [" + appType.getText() + "] </p>");
		Element currentFile = root.element(CURRENTFILE);
		System.out.println(currentFile.getText());
		// sb.append("<p>当前文件类型为 [" + currentFile.getText() + "] </p>");
		Element inOut = root.element(INOUT);
		System.out.println(inOut.getText());
		// sb.append("<p>输入/输出为 [" + inOut.getText() + "] </p>");
		Element formatErrs = root.element(FORMATERRS);
		System.out.println(formatErrs.getText());
		Element formats = root.element(FORMATS);
		if(formats != null){
			List formatList = formats.elements(FORMAT);
			if(CollectionUtil.isNotEmpty(formatList)){
				sb.append("<p>文件格式错误数 [" + inOut.getText() + "] 个</p>");
				sb.append("文件格式错误描述如下：<br />");
				sb.append("<ol>");
				for(int i = 0; i < formatList.size(); i++){
					Element format = (Element) formatList.get(i);
					System.out.println("\t" + format.getText());
					sb.append("<li>[" + format.getText() + "]</li>");
				}
				sb.append("</ol>");
			}
		}
		return sb.toString();
	}

	/**
	 * 解析失败记录错误
	 * @param root
	 */
	private static String resolveMsgError(Element root){
		StringBuffer sb = new StringBuffer();
		Element totalRecords = root.element(TOTALRECORDS);
		System.out.println(totalRecords.getText());
		sb.append("<p>总记录数 [" + totalRecords.getText() + "] </p> ");
		Element sucRecords = root.element(SUCRECORDS);
		System.out.println(sucRecords.getText());
		sb.append("<p>成功的记录数 [" + sucRecords.getText() + "] </p> ");
		Element falRecords = root.element(FALRECORDS);
		System.out.println(falRecords.getText());
		sb.append("<p>失败的记录数 [" + falRecords.getText() + "] </p> ");
		Element errRecords = root.element(ERRRECORDS);
		if(errRecords != null){
			List recList = errRecords.elements(REC);
			if(CollectionUtil.isNotEmpty(recList)){
				sb.append("<p>失败的记录详细信息描述如下 </p> ");
				sb.append("<ul>");
				for(int i = 0; i < recList.size(); i++){
					Element rec = (Element) recList.get(i);
					if(rec != null){
						Element rptno = rec.element(RPTNO);
						System.out.println("\t" + rptno.getText());
						sb.append("<li>申报号 [" + falRecords.getText()
								+ "] </li> ");
						Element errFields = rec.element(ERRFIELDS);
						if(errFields != null){
							List errFieldsList = errFields.elements(ERR);
							if(CollectionUtil.isNotEmpty(errFieldsList)){
								sb.append("<p>失败的记录字段详细信息描述如下 </p> ");
								sb.append("<ol>");
								for(int j = 0; j < errFieldsList.size(); j++){
									Element err = (Element) errFieldsList
											.get(j);
									if(err != null){
										Element errField = err
												.element(ERRFIELD);
										System.out.println("\t\t"
												+ errField.getText());
										sb.append("<li>出错字段英文标识 ["
												+ errField.getText()
												+ "] </li> ");
										Element errFieldCn = err
												.element(ERRFIELDCN);
										System.out.println("\t\t"
												+ errFieldCn.getText());
										sb.append("<li>出错字段中文标识 ["
												+ errField.getText()
												+ "] </li> ");
										Element errDesc = err.element(ERRDESC);
										System.out.println("\t\t"
												+ errDesc.getText());
										sb.append("<li>出错原因 ["
												+ errField.getText()
												+ "] </li> ");
									}
								}
								sb.append("</ol>");
							}
						}
					}
					sb.append("<p />");
				}
				sb.append("</ul>");
			}
		}
		return sb.toString();
	}

	public static void importXmlFile(File file){
		StringBuffer sb = new StringBuffer();
		try{
			Document doc = XmlUtil.parse(file);
			Element root = doc.getRootElement();
			if(!StringUtil.equals(root.getName(), MSG)){
				sb.append("文件格式有误根目录 [" + root.getName() + "] 不是 [" + MSG
						+ "] ");
			}else{
				String fileErros = resolveFileError(root);
				String msgErrors = resolveMsgError(root);
				sb.append(fileErros + msgErrors);
				System.out.println(sb.toString());
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (DocumentException e){
			e.printStackTrace();
		}
	}
}
