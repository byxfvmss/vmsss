package com.cjit.gjsz.autotask;

import java.util.Date;

import com.cjit.common.util.DateUtils;

public class TaskInfo{

	private String id;
	private String type;
	private String endDate;
	private String step;
	private String info;
	private String rptTitle;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getStep(){
		return step;
	}

	public void setStep(String step){
		this.step = step;
	}

	public String getInfo(){
		return info;
	}

	public void setInfo(String info){
		this.info = info;
	}

	public Date getBeginDateTime(){
		return DateUtils.stringToDate(this.id, "yyyyMMddHHmm");
	}

	public Date getEndDateTime(){
		if(this.endDate != null){
			return DateUtils.stringToDate(this.endDate, "yyyyMMddHHmm");
		}else{
			return null;
		}
	}

	public String getRptTitle(){
		return rptTitle;
	}

	public void setRptTitle(String rptTitle){
		this.rptTitle = rptTitle;
	}
}
