package com.cjit.vms.trans.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;

/**
 * <p>读取Excel的数据处理</p>
 * @author Albert Li
 * @date 2010-8-21 下午04:28:22
 * @version 1.0
 * <p>修改记录</p>
 * Albert Li    新建类    2010-8-21 下午04:28:22
 */
public class DataFileParserExcelImpl implements DataFileParser {
	
	public DataFileParserExcelImpl(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			HSSFWorkbook workBook = new HSSFWorkbook(is);
			sheet = workBook.getSheetAt(0);
			rowCount = sheet.getLastRowNum();
		} catch (Exception e) {
			throw new RuntimeException("���ļ����� " + e.getMessage());
		} finally{
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * <p>overwrite方法。</p>
	 * <p>是否有下一行数据</p>
	 * @author  Albert Li
	 * @date 2010-8-21 下午04:29:00
	 * @param 
	 */
	public boolean hasNextLine() {
		return currLineNo < rowCount;
	}

	/**
	 * <p>overwrite方法。</p>
	 * <p>获取每行内容。其中为数据列表，可为String或BigDecimal</p>
	 * @author  Albert Li
	 * @date 2010-8-21 下午04:29:00
	 * @param 
	 */
	public List next() {
		currLineNo ++;
		HSSFRow row = sheet.getRow(currLineNo);
		if(row == null) {
			return new ArrayList();
		} else {
			return getCurrentDataList(row);
		}		
	}
	
	/**
	 * <p>得到当前行的数据列表</p>
	 * @author  Albert Li
	 * @date 2010-8-21 下午04:38:35
	 * @param 
	 */
	private List getCurrentDataList(HSSFRow row) {
		short lastCellNum = row.getLastCellNum();
		List dataList = new ArrayList();
		for(short i=0;i<lastCellNum;i++){
			HSSFCell cell = row.getCell(i);
			if(cell == null) {
				dataList.add("");
			} else if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
				//�ַ��ʽ
				dataList.add(cell.getRichStringCellValue().getString());
			} else if(HSSFDateUtil.isCellDateFormatted(cell)) {
                //���ڸ�ʽ
                Date d = cell.getDateCellValue();
                if(d == null) {
                    dataList.add("");  
                } else {
                    dataList.add(new SimpleDateFormat("yyyy-MM-dd h:mm:ss").format(d));
                }
            } else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				dataList.add(BigDecimal.valueOf(cell.getNumericCellValue()));
			} else if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				dataList.add("");
			}
		}
		return dataList;
	}

	/**
	 * Excel表单
	 * */
	private HSSFSheet sheet;
	/**
	 * 总行数
	 * */
	private int rowCount;
	/**
	 * 当前行号
	 * */
	private int currLineNo = 0;
}
