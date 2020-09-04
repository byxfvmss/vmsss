package com.cjit.gjsz.common.model;

import java.util.List;

public class ExportSheet {

	private String sheetName;
	private List titleList, sourceDataList;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List getTitleList() {
		return titleList;
	}

	public void setTitleList(List titleList) {
		this.titleList = titleList;
	}

	public List getSourceDataList() {
		return sourceDataList;
	}

	public void setSourceDataList(List sourceDataList) {
		this.sourceDataList = sourceDataList;
	}
}
