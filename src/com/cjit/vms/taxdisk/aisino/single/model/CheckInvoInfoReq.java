package com.cjit.vms.taxdisk.aisino.single.model;

/**
 * 查询发票信息（请求报文）
 * @author john
 *
 */
public class CheckInvoInfoReq{

	private Integer year;//年份
	private Integer beginMonth;//起月份
	private Integer endMonth;//止月份
	
	public CheckInvoInfoReq(){}
	
	public CheckInvoInfoReq(Integer year, Integer beginMonth, Integer endMonth) {
		super();
		this.year = year;
		this.beginMonth = beginMonth;
		this.endMonth = endMonth;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getBeginMonth() {
		return beginMonth;
	}
	public void setBeginMonth(Integer beginMonth) {
		this.beginMonth = beginMonth;
	}
	public Integer getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
}
