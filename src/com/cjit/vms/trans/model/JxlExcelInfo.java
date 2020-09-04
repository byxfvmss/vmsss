package com.cjit.vms.trans.model;

import jxl.format.Colour;

/**
 * 报表类
 * 
 * @author
 */
public class JxlExcelInfo {
	private int fontSize; // 字体大小
	private String alignMent; // 左右对齐方式
	private String vAlignMent; // 垂直对齐方式
	private Colour borderColor;// 边框颜色
	private Colour bgColor;// 背景颜色

	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getAlignMent() {
		return alignMent;
	}
	public void setAlignMent(String alignMent) {
		this.alignMent = alignMent;
	}
	public String getvAlignMent() {
		return vAlignMent;
	}
	public void setvAlignMent(String vAlignMent) {
		this.vAlignMent = vAlignMent;
	}

	public Colour getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(Colour borderColor) {
		this.borderColor = borderColor;
	}
	public Colour getBgColor() {
		return bgColor;
	}
	public void setBgColor(Colour bgColor) {
		this.bgColor = bgColor;
	}
}
