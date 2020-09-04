package com.cjit.gjsz.datadeal.model;

import java.util.List;
import java.util.Map;

public class TableDataVO {
	private RptTableInfo rptTableInfo;
	private List rptColumnList;
	private RptData rptData;
	private List rptDataList;
	private Map dictListMap;

	public RptTableInfo getRptTableInfo() {
		return rptTableInfo;
	}

	public void setRptTableInfo(RptTableInfo rptTableInfo) {
		this.rptTableInfo = rptTableInfo;
	}

	public List getRptColumnList() {
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList) {
		this.rptColumnList = rptColumnList;
	}

	public RptData getRptData() {
		return rptData;
	}

	public void setRptData(RptData rptData) {
		this.rptData = rptData;
	}

	public Map getDictListMap() {
		return dictListMap;
	}

	public void setDictListMap(Map dictListMap) {
		this.dictListMap = dictListMap;
	}

	public List getRptDataList() {
		return rptDataList;
	}

	public void setRptDataList(List rptDataList) {
		this.rptDataList = rptDataList;
	}

}