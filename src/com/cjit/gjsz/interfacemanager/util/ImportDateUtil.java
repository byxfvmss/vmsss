/**
 * 
 */
package com.cjit.gjsz.interfacemanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cjit.common.util.CharacterEncoding;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringNumFormat;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.ImportModel;
import com.cjit.gjsz.interfacemanager.model.KeyInfo;
import com.cjit.gjsz.interfacemanager.model.TableRelation;
import com.cjit.gjsz.interfacemanager.model.UserInterface;

/**
 * @author huboA
 */
public class ImportDateUtil{

	/**
	 * 根据用户数据File取得要导记录
	 * @param file
	 * @return
	 */
	public static List getDate(File file, UserInterface userInterface){
		if(StringUtil.equalsIgnoreCase(userInterface.getFileType(), "txt")){
			return importTxtFile(file, userInterface);
		}else if(StringUtil
				.equalsIgnoreCase(userInterface.getFileType(), "xls")){
			return importExcelFile(file, userInterface);
		}
		throw new RuntimeException("导入文件的格式无法识别.");
	}

	private static List importTxtFile(File file, UserInterface userInterface){
		InputStreamReader read = null;
		BufferedReader in = null;
		List list = new ArrayList();
		try{
			read = new InputStreamReader(new FileInputStream(file),
					CharacterEncoding.GB18030);
			in = new BufferedReader(read);
			String s = null;
			int line = 1;
			while((s = in.readLine()) != null){
				if(userInterface.getStartLine() > line++){
					continue;
				}
				String[] arr = null;
				if(StringUtil.equals("|", userInterface.getSeparator())){
					arr = s.split("\\|");
				}else{
					arr = s.split(userInterface.getSeparator());
				}
				LinkedMap map = new LinkedMap();
				for(int i = 0; i < arr.length; i++){
					map.put(i + "", arr[i]);
				}
				list.add(map);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				in.close();
			}catch (IOException e){
				e.printStackTrace();
			}
			try{
				read.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		return list;
	}

	private static List importExcelFile(File file, UserInterface userInterface){
		InputStream is;
		List list = new ArrayList();
		try{
			is = new FileInputStream(file);
			// 创建excel文档的引用
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			// 引用表
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rowNumber = sheet.getLastRowNum();
			for(int i = 0; i <= rowNumber; i++){
				LinkedMap map = new LinkedMap();
				if(userInterface.getStartLine() > (i + 1)){
					continue;
				}
				// 引用行
				HSSFRow row = sheet.getRow(i);
				// 得到列数
				if(row != null){
					int size = row.getLastCellNum();
					for(int j = 0; j < size; j++){
						HSSFCell cell = row.getCell((short) j);
						if(cell != null){
							switch(cell.getCellType()){
								case HSSFCell.CELL_TYPE_NUMERIC:
									double d = cell.getNumericCellValue();
									HSSFCellStyle style = cell.getCellStyle();
									String str = null;
									if(StringUtil.contains(style
											.getDataFormatString(), "m/d/yy")){
										Date date = cell.getDateCellValue();
										str = DateUtils.toString(date,
												DateUtils.ORA_DATES_FORMAT);
									}else{
										str = String.valueOf(d);
										if(str.lastIndexOf(".0") > -1){ // 如果不带小数点
											str = NumberUtils.format(d, "#");
										}else{ // 如果带小数点
											str = NumberUtils.format(d,
													"#.########");
										}
									}
									map.put(j + "", str);
									break;
								case HSSFCell.CELL_TYPE_STRING:
									map.put(j + "", cell
											.getRichStringCellValue()
											.getString());
									break;
								case HSSFCell.CELL_TYPE_BOOLEAN:
									map.put(j + "", cell.getBooleanCellValue()
											+ "");
									break;
								case HSSFCell.CELL_TYPE_ERROR:
									map.put(j + "", cell.getErrorCellValue()
											+ "");
									break;
								case HSSFCell.CELL_TYPE_FORMULA:
									map.put(j + "", cell.getCellFormula());
									break;
								default: // HSSFCell.CELL_TYPE_BLANK:
									map.put(j + "", cell
											.getRichStringCellValue()
											.getString());
									break;
							}
						}
					}
					list.add(map);
				}
			}
			is.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 取得数据类型列表
	 * @param columnInfos
	 * @return
	 */
	public static String[] getDateType(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] arr = new String[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getDataType();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 取得数据类型列表
	 * @param columnInfos
	 * @return
	 */
	public static String[] getColumnInfoIds(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] arr = new String[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getColumnId();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 取得数据类型中文列表
	 * @param columnInfos
	 * @return
	 */
	public static String[] getColumnInfoNames(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] arr = new String[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getColumnName();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 取得数据类型描述信息
	 * @param columnInfos
	 * @return
	 */
	public static String[] getColumnInfoDescriptions(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] arr = new String[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getConstrainDescription();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 取得数据的字典对应
	 * @param columnInfos
	 * @return
	 */
	public static String[] getDictionary(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			String[] arr = new String[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getDictionaryTypeId();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 取得数据编号列表
	 * @param columnInfos
	 * @return
	 */
	public static int[] getDateNum(List columnInfos){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			int[] arr = new int[columnInfos.size()];
			for(int i = 0; i < columnInfos.size(); i++){
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				arr[i] = columnInfo.getTxtColumnId();
			}
			return arr;
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	/**
	 * 生成insertSQL语句
	 * @param columnInfos
	 * @param tableId
	 * @return
	 */
	public static String getSQL(List columnInfos, String tableId,
			List baseTables, ImportModel model){
		if(CollectionUtil.isNotEmpty(columnInfos)){
			StringBuffer main = new StringBuffer();
			StringBuffer sub = new StringBuffer();
			// 根据 columnInfos和tableId动态生成insert语句
			main.append("insert into " + tableId + "( ");
			sub.append(" values( ");
			for(int i = 0; i < columnInfos.size(); i++){ // 本来到columnInfos.size()-1，要增加RPTNO
				ColumnInfo columnInfo = (ColumnInfo) columnInfos.get(i);
				main.append(columnInfo.getColumnId() + ",");
				sub.append("?,");
			}
			if(StringUtil.equalsIgnoreCase(tableId, "t_company_info")){
				main.append("INSTCODE, DATASTATUS, BUSINESSID, IMPORTDATE");
				sub.append("?, ?, ?, ?");
				model.importType = 0;
			}else if(CollectionUtil.isEmpty(baseTables)){
				// 以前有关系，现在导入的时候不需要生成RPTNO
				// main.append("RPTNO, INSTCODE, DATASTATUS, BUSINESSID,
				// IMPORTDATE");
				main.append("INSTCODE, DATASTATUS, BUSINESSID, IMPORTDATE");
				sub.append("?, ?, ?, ?");
				model.importType = 0;
			}else if(CollectionUtil.isNotEmpty(baseTables)){
				for(int k = 0; k < baseTables.size(); k++){
					TableRelation tableRelation = (TableRelation) baseTables
							.get(k);
					// t_fini_dom_export, t_fini_dom_pay, t_fini_dom_remit,
					// t_fini_export,t_fini_payment,t_fini_remit
					if(StringUtil.equalsIgnoreCase(tableRelation
							.getTableColumn(), "RPTNO")
							&& tableRelation.getSubRelation() == 0){
						main
								.append("INSTCODE, DATASTATUS, BUSINESSID, IMPORTDATE");
						sub.append("?, ?, ?, ?");
						model.importType = 0;
					}else if(StringUtil.equalsIgnoreCase(tableRelation
							.getTableColumn(), "RPTNO")
							&& tableRelation.getSubRelation() == 1){
						// t_customs_decl,t_export_info
						main.append("BUSINESSID, SUBID");
						sub.append("?, ?");
						model.importType = 1;
					}else if(StringUtil.equalsIgnoreCase(tableRelation
							.getTableColumn(), "CUSTCODE")
							&& tableRelation.getSubRelation() == 1){
						// t_company_openinfo
						main.append("BUSINESSID, SUBID");
						sub.append("?, ?");
						model.importType = 1;
					}
					break; // 只要一次就可以搞定?
				}
			}
			main.append(" )");
			sub.append(" ) ");
			main.append(sub);
			return main.toString();
		}
		throw new RuntimeException("传递的参数columnInfos不能为空List");
	}

	public static String charKey(KeyInfo keyInfo){
		// 涉外收入对公：N001～N999，P001～T999，涉外收入对私：U001～W999
		// C对公, D,F对私
		if(StringUtil.equalsIgnoreCase(keyInfo.getCustomType(), "C")){
			return getKey(keyInfo, keyInfo.getPersonalCode());
		}else if(StringUtil.equalsIgnoreCase(keyInfo.getCustomType(), "D")
				|| StringUtil.equalsIgnoreCase(keyInfo.getCustomType(), "F")){
			return getKey(keyInfo, keyInfo.getPublicCode());
		}
		throw new RuntimeException("找不到类型为 " + keyInfo.getCustomType()
				+ " 的账户类型");
	}

	public static int getKeyType(KeyInfo keyInfo){
		String tableId = keyInfo.getTableId();
		int type = 0;
		for(int i = 0; i <= 2; i++){
			if(StringUtil.equalsIgnoreCase(KeyInfo.tables[i], tableId)){
				type = 1; // 规则一
			}
		}
		for(int i = 3; i <= 3; i++){
			if(StringUtil.equalsIgnoreCase(KeyInfo.tables[i], tableId)){
				type = 2;// 规则二
			}
		}
		for(int i = 4; i <= 7; i++){
			if(StringUtil.equalsIgnoreCase(KeyInfo.tables[i], tableId)){
				type = 3;// 规则三
			}
		}
		return type;
	}

	// N001-N999,P001-T999 U001-W999 (规则一)
	private static String getKey(KeyInfo keyInfo, String customType){
		String maxSize = keyInfo.getMaxType();
		if(StringUtil.isEmpty(maxSize)){
			return new String("000001");
		}
		int ints = Integer.valueOf(maxSize).intValue();
		if(ints >= 1 && ints < 999999){
			int max = ints + 1;
			String finalChar = StringNumFormat.getFormatLong(max, "000000");
			return finalChar;
		}
		throw new RuntimeException("已经超过当天最大的流水号了之间");
	}
	
	public static String getKey(KeyInfo keyInfo){
		String maxSize = keyInfo.getMaxType();
		if(StringUtil.isEmpty(maxSize)){
			return new String("0000001");
		}
		int ints = Integer.valueOf(maxSize).intValue();
		if(ints >= 1 && ints < 9999999){
			int max = ints + 1;
			String finalChar = StringNumFormat.getFormatLong(max, "0000000");
			return finalChar;
		}
		throw new RuntimeException("已经超过当天最大的流水号了之间");
	}

	private static String getTypeKey(int type, String maxSize, String[] strs){
		if(strs != null && strs.length > 0){
			for(int i = 0; i < strs.length; i++){
				String[] chars = strs[0].split("-");
				if(chars != null && chars.length == 2){
					char start = chars[0].charAt(0);
					char end = chars[1].charAt(0);
					if(type == 1){
						if(StringUtil.isEmpty(maxSize)){
							return new String(start + "001");
						}
						char ch = maxSize.charAt(0);
						if(ch >= start && ch <= end){
							String number = maxSize.substring(1, maxSize
									.length());
							int max = (Integer.valueOf(number).intValue() + 1);
							if(max > 999){
								ch = (char) (ch + 1);
								max = 1;
								if(ch > end){
									// 已经超过当天最大的流水号了之间
									return null;
								}
							}
							String finalChar = StringNumFormat.getFormatLong(
									max, "000");
							return new String(ch + "" + finalChar);
						}
					}else if(type == 3){
						if(StringUtil.isEmpty(maxSize)){
							return new String("001" + start);
						}
						char ch = maxSize.charAt(3);
						if(ch >= start && ch <= end){
							String number = maxSize.substring(0, maxSize
									.length() - 1);
							int max = (Integer.valueOf(number).intValue() + 1);
							if(max > 999){
								ch = (char) (ch + 1);
								max = 1;
								if(ch > end){
									// 已经超过当天最大的流水号了之间
									return null;
								}
							}
							String finalChar = StringNumFormat.getFormatLong(
									max, "000");
							return new String(finalChar + "" + ch);
						}
					}
					throw new RuntimeException("已经超过当天最大的流水号了之间");
				}
			}
		}
		throw new RuntimeException("数据字典设置不正确。");
	}
}