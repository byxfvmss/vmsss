package cjit.crms.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.ServletActionContext;

import com.cjit.common.util.JXLTool;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

public class ExcelIOUtil {
	/**
	 * 文件导入
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public List doImportFile(File file, List<Dictionary> headList) throws Exception {
		//生成批次主健,作为标识
		String batchId = StringUtil.getUUID();
		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		// 创建数据List对象
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		if (hs != null&&!headList.isEmpty()) {
			String[][] sheet = (String[][]) hs.get("0");
			// 获取表头列表
			String[] heads = sheet[0];
			// 获取每行
			for(int i = 1; i < sheet.length; i ++){
				Map rowMap = new HashMap();
				String[] row = sheet[i];
				//循环获取字典表头
				for(int j = 0; j < headList.size(); j++){
					//获取表头中文名称
					String headName = headList.get(j).getTypeName();
					String headCode = headList.get(j).getName();
					//与excel表头作匹配，获取列位置j并抓取数据
					for(int k = 0; k < heads.length; k++){
						String head = heads[k];
						if(headName.equals(head)){
							String cellValue = "";
							if(null!=row[k]){
								cellValue = row[k];
							}
							rowMap.put(headCode, cellValue);
						}
					}
				}
				//生成逻辑主健,用于标识
				String dataId = StringUtil.getUUID();
				String transId = StringUtil.getUUID();
				rowMap.put("dataId", dataId);
				//关联批次主健
				rowMap.put("batchId", batchId);
				//交易ID
				rowMap.put("transId", transId);
				dataList.add(rowMap);
			}
		}
		file = null;
		return dataList;
	}
	/**
	 * 文件扫描
	 * @param attachment  文件对象
	 * @param attachmentFileName  文件名称
	 * @return
	 */
	public Map checkFile(File attachment,String attachmentFileName) {
		Map map = new HashMap();
		// 文件校验
		ServletContext sc = ServletActionContext.getServletContext();
		if (attachment != null) {
			String dir = sc.getRealPath("/WEB-INF");
			System.out.println("存放路径为：" + dir);
			System.out.println("文件名称为：" + attachmentFileName);
			File file = new File(new File(dir), attachmentFileName);
			map.put("file", file);
			if (file.exists()) {
				file.delete(); // 若文件已存在，删除原文件
				file = new File(new File(dir), attachmentFileName);
			}
			attachment.renameTo(file);
			DataFileParser dataFileParserUtil = DataFileParserUtil
					.createDataFileParser(file);
			if (dataFileParserUtil == null) {
				map.put("resultMessages", "请选择后缀为 .xls 的 Excel 文件导入");
				map.put("flag", false);
				return map;
			}
			if (!dataFileParserUtil.hasNextLine()) {
				map.put("resultMessages", "上传的文件为空");
				map.put("flag", false);
				return map;
			}
			map.put("flag", true);
			return map;
		} else {
			map.put("resultMessages", "请选择上传文件.");
			map.put("flag", false);
			return map;
		}
	}
}
