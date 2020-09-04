package com.cjit.vms.trans.util;

import jxl.biff.DisplayFormat;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class JXLTool {

	public static WritableCellFormat getHeader() throws WriteException {
		// 定义字体
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			// 左右居中
			format.setAlignment(jxl.format.Alignment.CENTRE);
			// 上下居中
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// 黑色边框
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
//			format.setBackground(Colour.YELLOW2);
			format.setWrap(false);
			format.setShrinkToFit(true);
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}

	public static WritableCellFormat getContentFormat() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.TEXT);
		try {
			format.setWrap(false);
			// 设置左右居中
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //设置边框  
  			format.setShrinkToFit(true);

		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	
	/***
	 * 返回数据格式
	 * NumberFormat nf = new NumberFormat("#0.00");  
	 * JXLTool.getContentFormat(nf)
	 * @param df
	 * @return
	 * @throws WriteException
	 */
	public static  WritableCellFormat getContentFormat(DisplayFormat df) throws WriteException {
		WritableCellFormat format = new WritableCellFormat(df);
		try {
			format.setWrap(false);
			format.setAlignment(jxl.format.Alignment.RIGHT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿� 
  			format.setShrinkToFit(true);

		} catch (WriteException e) {
			throw e;
		}
		return format;
	}

}
