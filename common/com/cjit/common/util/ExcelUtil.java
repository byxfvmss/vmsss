package com.cjit.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Excel文件工具类
 * @作者 李海波A
 * @日期 Oct 19, 2010
 */
public class ExcelUtil{

	/**
	 * 解析Excel文件
	 * @param strExcelUrl 文件路径
	 * @param file excel文件
	 * @param maxSheet 最大Sheet数
	 * @return Hashtable
	 * @throws ActionException
	 */
	public static Hashtable parseExcel(String strExcelUrl, File file,
			int maxSheet) throws Exception{
		FileInputStream fis = null;
		Hashtable hsRtn = new Hashtable();
		int nSheet = 0;
		String[][] cellValues = null;
		try{
			if(strExcelUrl != null && !"".equals(strExcelUrl)){
				fis = new FileInputStream(strExcelUrl);
			}else if(file != null){
				fis = new FileInputStream(file);
			}else{
				return null;
			}
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			// 循环读取每一工作表
			int nSheetCount = workbook.getNumberOfSheets();
			if(maxSheet != 0 && maxSheet > nSheetCount){
				maxSheet = nSheetCount;
			}
			for(int i = 0; i < maxSheet; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
				if(sheet != null){
					nSheet = i;
					int nRowCount = sheet.getLastRowNum() + 1;
					cellValues = new String[nRowCount][];
					// 循环读取每一行
					for(int j = 0; j < nRowCount; j++){
						HSSFRow row = sheet.getRow(j);
						if(row != null){
							int nColumnCount = row.getLastCellNum() + 1;
							cellValues[j] = new String[nColumnCount];
							// 循环读取每一格
							for(int k = 0; k < nColumnCount; k++){
								HSSFCell cell = row.getCell((short) k);
								if(cell != null){
									int nCellType = cell.getCellType();
									switch(nCellType){
										case HSSFCell.CELL_TYPE_NUMERIC:
											cellValues[j][k] = new BigDecimal(
													cell.getNumericCellValue())
													.toString().trim();
											break;
										case HSSFCell.CELL_TYPE_STRING:
											cellValues[j][k] = cell
													.getRichStringCellValue()
													.getString()
													// .getStringCellValue()
													.trim();
											break;
										case HSSFCell.CELL_TYPE_FORMULA:
											cellValues[j][k] = new BigDecimal(
													cell.getNumericCellValue())
													.toString().trim();
											break;
										case HSSFCell.CELL_TYPE_BLANK:
											cellValues[j][k] = "";
											break;
										case HSSFCell.CELL_TYPE_BOOLEAN:
											cellValues[j][k] = null;
											break;
										case HSSFCell.CELL_TYPE_ERROR:
											cellValues[j][k] = null;
											break;
									}
								}
							}
						}
					}
				}
				hsRtn.put(nSheet + "", cellValues);
				nSheet = 0;
				cellValues = null;
			}
		}catch (Exception ex){
			throw ex;
		}finally{
			try{
				if(fis != null){
					fis.close();
				}
			}catch (Exception ex){
				throw ex;
			}
		}
		return hsRtn;
	}

	/**
	 * 解析Excel2007文件<br> jdk1.4使用纯Java的方式读取excel2007<br> <br>
	 * 首先介绍excel2007文件的格式，这里单只工作表文件，不包括加载宏的以及其他格式的，即.xlsx扩展名的<br>
	 * 把Book1.xlsx这个文件用解压缩文件打开，这是office2007的新格式，所有的该版本的文件都可以用解压缩文件打开。<br>
	 * 在解压到的文件夹里主要用到的是xl这个文件夹，这里也只介绍这个文件夹里的部分文件，其他信息可以在微软的msdn上找到。<br>
	 * xl文件夹下有sharedStrings.xml，styles.xml，workbook.xml三个文件，<br>
	 * 第一个是放共享字符的，在msdn上说在这里可以定义各种语言的字符，<br>
	 * 然后在sheet.xml里引用这个来达到国际化，而不用为每种语言建立一个excel，<br>
	 * styles可能是样式吧，workbook.xml是表的总体情况，有几个sheet等等。<br>
	 * 接着是该文件夹下的worksheets文件夹，里面放的是每个sheet的具体内容，<br>
	 * 比如在workbook.xml定义有3个sheet，那么在这里你就能看到三个文件，<br>
	 * 文件名对应workbook.xml中sheet节点的name属性，但是这里要注意大小写<br>
	 * 打开一个sheet1.xml文件，可以看到这里定义了该sheet中的行和列的信息,具体在程序里有介绍。<br>
	 * 读取简单的工作表文件的内容基本上用到这些，如果还要对文件进行操作的话，可能还可以修改字体，样式什么的。<br>
	 * @param strExcelUrl 文件路径
	 * @param file excel文件
	 * @param maxSheet 最大Sheet数
	 * @param maxCell 最大列数
	 * @return Hashtable
	 * @throws ActionException
	 */
	public static Hashtable parseExcel2007(String strExcelUrl, File file,
			int maxSheet, int maxCell) throws Exception{
		// FileInputStream fis = null;
		Hashtable hsRtn = new Hashtable();
		int nSheet = 0;
		String[][] cellValues = null;
		// 获取.xlsx文件
		ZipFile xlsxFile = null;
		if(strExcelUrl != null && !"".equals(strExcelUrl)){
			xlsxFile = new ZipFile(new File(strExcelUrl));
		}else if(file != null){
			xlsxFile = new ZipFile(file);
		}else{
			return null;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 先读取sharedStrings.xml这个文件备用
		ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
		String[] sharedStrings = null;
		if(sharedStringXML != null){
			InputStream sharedStringXMLIS = xlsxFile
					.getInputStream(sharedStringXML);
			org.w3c.dom.Document sharedString = dbf.newDocumentBuilder().parse(
					sharedStringXMLIS);
			org.w3c.dom.NodeList str = sharedString.getElementsByTagName("t");
			sharedStrings = new String[str.getLength()];
			for(int n = 0; n < str.getLength(); n++){
				org.w3c.dom.Element element = (org.w3c.dom.Element) str.item(n);
				sharedStrings[n] = element.getFirstChild().getNodeValue();
			}
		}else if(maxCell > 0){
			sharedStrings = new String[maxCell];
		}else{
			return null;
		}
		// 找到解压文件夹里的workbook.xml,此文件中包含了这张工作表中有几个sheet
		ZipEntry workbookXML = xlsxFile.getEntry("xl/workbook.xml");
		InputStream workbookXMLIS = xlsxFile.getInputStream(workbookXML);
		org.w3c.dom.Document doc = dbf.newDocumentBuilder()
				.parse(workbookXMLIS);
		// 获取一共有几个sheet
		org.w3c.dom.NodeList nl = doc.getElementsByTagName("sheet");
		if(maxSheet != 0 && maxSheet > nl.getLength()){
			maxSheet = nl.getLength();
		}
		for(int i = 0; i < maxSheet; i++){
			nSheet = i;
			org.w3c.dom.Element element = (org.w3c.dom.Element) nl.item(i); // 将node转化为element，用来得到每个节点的属性
			ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/"
					+ element.getAttribute("name").toLowerCase() + ".xml");
			InputStream sheetXMLIS = xlsxFile.getInputStream(sheetXML);
			org.w3c.dom.Document sheetdoc = dbf.newDocumentBuilder().parse(
					sheetXMLIS);
			org.w3c.dom.NodeList rowdata = sheetdoc.getElementsByTagName("row");
			cellValues = new String[rowdata.getLength()][];
			for(int j = 0; j < rowdata.getLength(); j++){
				org.w3c.dom.Element row = (org.w3c.dom.Element) rowdata.item(j);
				// 根据行得到每个行中的列
				if(row != null){
					org.w3c.dom.NodeList columndata = row
							.getElementsByTagName("c");
					if(columndata != null){
						cellValues[j] = new String[columndata.getLength()];
						for(int k = 0; k < columndata.getLength(); k++){
							org.w3c.dom.Element column = (org.w3c.dom.Element) columndata
									.item(k);
							if(column != null){
								org.w3c.dom.NodeList values = column
										.getElementsByTagName("v");
								if(values != null){
									org.w3c.dom.Element value = (org.w3c.dom.Element) values
											.item(0);
									if(value != null){
										if(column.getAttribute("t") != null
												&& column.getAttribute("t")
														.equals("s")){
											// 如果是共享字符串则在sharedstring.xml里查找该列的值
											String c = value.getFirstChild()
													.getNodeValue();
											cellValues[j][k] = sharedStrings[Integer
													.parseInt(c)];
										}else{
											cellValues[j][k] = value
													.getFirstChild()
													.getNodeValue();
										}
									}
								}
							}
						}
					}
				}
			}
			hsRtn.put(nSheet + "", cellValues);
			nSheet = 0;
			cellValues = null;
		}
		return hsRtn;
	}

	/**
	 * 解析Excel2007文件<br> jdk1.5 需引用如下jar文件<br>
	 * geronimo-stax-api_1.0_spec-1.0.jar<br> poi-3.7-20101029.jar<br>
	 * poi-ooxml-3.7-20101029.jar<br> poi-ooxml-schemas-3.7-20101029.jar<br>
	 * xmlbeans-2.3.0.jar
	 * @param strExcelUrl 文件路径
	 * @param file excel文件
	 * @param maxSheet 最大Sheet数
	 * @return Hashtable
	 * @throws ActionException
	 */
	public static Hashtable parseExcel2007Poi(String strExcelUrl, File file,
			int maxSheet) throws Exception{
		// FileInputStream fis = null;
		// Hashtable hsRtn = new Hashtable();
		// int nSheet = 0;
		// String[][] cellValues = null;
		//
		// try {
		// // 工作簿
		// if (strExcelUrl != null && !"".equals(strExcelUrl)) {
		// fis = new FileInputStream(strExcelUrl);
		// } else if (file != null) {
		// fis = new FileInputStream(file);
		// } else {
		// return null;
		// }
		//
		// Workbook workbook = WorkbookFactory.create(fis);
		//
		// // 循环读取每一工作表
		// int nSheetCount = workbook.getNumberOfSheets();
		// if (maxSheet != 0 && maxSheet > nSheetCount) {
		// maxSheet = nSheetCount;
		// }
		// for (int i = 0; i < maxSheet; i++) {
		// Sheet sheet = workbook.getSheetAt(i);
		// if (sheet != null) {
		// nSheet = i;
		// int nRowCount = sheet.getLastRowNum() + 1;
		// cellValues = new String[nRowCount][];
		// // 循环读取每一行
		// for (int j = 0; j < nRowCount; j++) {
		// Row row = sheet.getRow(j);
		// if (row != null) {
		// int nColumnCount = row.getLastCellNum() + 1;
		// cellValues[j] = new String[nColumnCount];
		// // 循环读取每一格
		// for (int k = 0; k < nColumnCount; k++) {
		// Cell cell = row.getCell(k);
		// if (cell != null) {
		// int nCellType = cell.getCellType();
		// switch (nCellType) {
		// case HSSFCell.CELL_TYPE_NUMERIC:
		// cellValues[j][k] = new BigDecimal(
		// cell.getNumericCellValue())
		// .toString().trim();
		// break;
		// case HSSFCell.CELL_TYPE_STRING:
		// cellValues[j][k] = cell
		// .getRichStringCellValue()
		// .getString()
		// // .getStringCellValue()
		// .trim();
		// break;
		// case HSSFCell.CELL_TYPE_FORMULA:
		// cellValues[j][k] = new BigDecimal(
		// cell.getNumericCellValue())
		// .toString().trim();
		// break;
		// case HSSFCell.CELL_TYPE_BLANK:
		// cellValues[j][k] = "";
		// break;
		// case HSSFCell.CELL_TYPE_BOOLEAN:
		// cellValues[j][k] = null;
		// break;
		// case HSSFCell.CELL_TYPE_ERROR:
		// cellValues[j][k] = null;
		// break;
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// hsRtn.put(nSheet + "", cellValues);
		// nSheet = 0;
		// cellValues = null;
		// }
		// } catch (Exception ex) {
		// throw ex;
		// } finally {
		// fis.close();
		// }
		// return hsRtn;
		return null;
	}

	static private NumberFormat format = new DecimalFormat("#");

	public static String getStringCellValue(HSSFCell cell, NumberFormat nf){
		if(cell != null){
			int nCellType = cell.getCellType();
			switch(nCellType){
				case HSSFCell.CELL_TYPE_NUMERIC:
				case HSSFCell.CELL_TYPE_FORMULA:
					if(HSSFDateUtil.isCellDateFormatted(cell)){
						return DateUtils.toString(cell.getDateCellValue(),
								"yyyyMMdd");
					}else if(nf != null){
						return nf.format(cell.getNumericCellValue());
					}else{
						return format.format(cell.getNumericCellValue());
					}
				case HSSFCell.CELL_TYPE_STRING:
					return cell.getRichStringCellValue().getString().trim();
				case HSSFCell.CELL_TYPE_BLANK:
					return "";
				case HSSFCell.CELL_TYPE_BOOLEAN:
					return new Boolean(cell.getBooleanCellValue()).toString();
				case HSSFCell.CELL_TYPE_ERROR:
				default:
					return null;
			}
		}
		return null;
	}
}
