package com.cjit.gjsz.common.homenote.xml;

import java.util.List;

public class HomeDataDO{

	private String resultCode;
	private String resultUserId;
	private String tableUrl;
	private List label;
	private List thead;
	private List tbody;

	public String getResultCode(){
		return resultCode;
	}

	public void setResultCode(String resultCode){
		this.resultCode = resultCode;
	}

	public String getResultUserId(){
		return resultUserId;
	}

	public void setResultUserId(String resultUserId){
		this.resultUserId = resultUserId;
	}

	public String getTableUrl(){
		return tableUrl;
	}

	public void setTableUrl(String tableUrl){
		this.tableUrl = tableUrl;
	}

	public List getLabel(){
		return label;
	}

	public void setLabel(List label){
		this.label = label;
	}

	public List getThead(){
		return thead;
	}

	public void setThead(List thead){
		this.thead = thead;
	}

	public List getTbody(){
		return tbody;
	}

	public void setTbody(List tbody){
		this.tbody = tbody;
	}
}
