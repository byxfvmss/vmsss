package com.cjit.vms.taxdisk.aisino.single.model;


/**
 * 查询领用存信息（请求报文）
 * @author john
 *
 */
public class QuerAndReceInfoReq {

	private Integer beginMonth;//起月份
	private Integer endMonth;//止月份
	
	public QuerAndReceInfoReq(){}
	
	public QuerAndReceInfoReq(Integer beginMonth, Integer endMonth) {
		super();
		this.beginMonth = beginMonth;
		this.endMonth = endMonth;
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
