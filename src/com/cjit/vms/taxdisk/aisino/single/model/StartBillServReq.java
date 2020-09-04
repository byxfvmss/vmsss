package com.cjit.vms.taxdisk.aisino.single.model;


/**
 * 启动开票服务（请求报文）
 * @author john
 *
 */
public class StartBillServReq {

	private Integer Web;//0客户端发起请求 1服务端发起请求
	
	public StartBillServReq(){}

	public StartBillServReq(Integer web) {
		super();
		Web = web;
	}

	public Integer getWeb() {
		return Web;
	}

	public void setWeb(Integer web) {
		Web = web;
	}
}
