package com.cjit.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * Excel文件工具类
 * 用于辅助本系统中固定导出格式的excel文件样式，每次创建时使用新的workbook
 */
public class ExcelHelper {
	
	private HSSFWorkbook wb;
	
	//以下变量根据workbook动态生成


	public ExcelHelper(HSSFWorkbook workbook)
	{
		this.wb=workbook;
	}
	
	public HSSFFont getBoldFont()
	{
		HSSFFont font=wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		return font;
	}

	public HSSFCellStyle getDataCS() 
	{
		return getDataCS((short)-1);
	}
	public HSSFCellStyle getDataCS(short bg) 
	{
		HSSFCellStyle cs=wb.createCellStyle();
		cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cs.setWrapText(true);
		if(bg!=-1)
		{
			cs.setFillForegroundColor(bg);  
			cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		return cs;
	}
	public HSSFCellStyle getTitleCS(short bg)
	{
		HSSFCellStyle cs=wb.createCellStyle();
		cs.setFont(getBoldFont());
		cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cs.setWrapText(true);
		if(bg!=-1)
		{
			cs.setFillForegroundColor(bg);  
			cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		return cs;
	}
	public HSSFDataFormat getDataFormatText()
	{
		return wb.createDataFormat();
	}
	
	public HSSFCellStyle getTitleCS()
	{
		return getTitleCS((short)-1);
	}
	
	//固定单元格
	public void setCell(HSSFRow row,short column,HSSFCellStyle cs,String text)
	{
		HSSFCell cell = row.createCell(column);
		cell.setCellStyle(cs);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
		cell.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
		cell.setCellValue(new HSSFRichTextString(text));
	}

	public HSSFFont getFont(short color) {
		HSSFFont font=wb.createFont();
		font.setColor(color);
		return font;
	}

	public HSSFCellStyle getSafeDataCS() {
		HSSFCellStyle cs=wb.createCellStyle();
		//自定义颜色
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex((short)41,(byte)0 ,(byte)102, (byte)204);
		//左右居中和垂直居中对齐
		cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//边框和边框颜色
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cs.setBottomBorderColor((short)41);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cs.setLeftBorderColor((short)41);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cs.setTopBorderColor((short)41);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cs.setRightBorderColor((short)41);
		cs.setWrapText(true);
		HSSFDataFormat df = getDataFormatText();			
		cs.setDataFormat(df.getFormat("@"));// 文本型
		return cs;
	}
	public HSSFCellStyle getSafeBottemDataCS() {
		HSSFCellStyle cs=getSafeDataCS();
		cs.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM); //下边框
		return cs;
	}
	
    /** 
     * @param args the command line arguments 
     */  
    public static void main(String[] args) throws FileNotFoundException, IOException {  
        // TODO code application logic here  
        FileInputStream myxls = new FileInputStream("c:\\workbook.xls");  
        HSSFWorkbook wb = new HSSFWorkbook(myxls);  
        HSSFSheet sheet= wb.getSheetAt(0);  
        int startRow=2;  
        int rows =10;  
  
        insertRow(sheet,startRow,rows);  
         
        FileOutputStream myxlsout = new FileOutputStream("c:\\workbook.xls");  
        wb.write(myxlsout);  
        myxlsout.close();  
          
    }  
  
    private static void insertRow(HSSFSheet sheet, int startRow, int rows) {  
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);  
  
  
        for (int i = 0; i < rows; i++) {  
            HSSFRow sourceRow = null;//原始位置  
            HSSFRow targetRow = null;//移动后位置  
            HSSFCell sourceCell = null;  
            HSSFCell targetCell = null;  
            sourceRow = sheet.createRow(startRow);  
            targetRow = sheet.getRow(startRow + rows);  
            sourceRow.setHeight(targetRow.getHeight());  
  
            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {  
                sourceCell = sourceRow.createCell((short) m);  
                targetCell = targetRow.getCell(m);  
                sourceCell.setCellStyle(targetCell.getCellStyle());  
                sourceCell.setCellType(targetCell.getCellType());  
            }  
            startRow++;  
        }  
  
    }
    
    public boolean hasMerged(HSSFSheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    // 判断指定区域内是否含有合并单元格
    public boolean hasMerged(Region region,HSSFSheet sheet) 
    {
    	for (int row = region.getRowFrom(); row < region.getRowTo(); row++) {
            for (short col = region.getColumnFrom(); col < region.getColumnTo(); col++){
                for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                    Region r = sheet.getMergedRegionAt(i);
                    if (r.contains(row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isMerged(int row ,short col,HSSFSheet sheet) 
    {
    	for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            Region r = sheet.getMergedRegionAt(i);
            if (r.contains(row, col)) {
                return true;
            }
        }
    	return false;
    }

}
