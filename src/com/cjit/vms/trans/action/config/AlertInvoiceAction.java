package com.cjit.vms.trans.action.config;

import com.cjit.vms.trans.action.DataDealAction;

public class AlertInvoiceAction extends DataDealAction {
	private static final long serialVersionUID = 1L;
	/*
	 * 20160321
	 * 
	 * screen
	 * 
	 * wait
	 * 
	 * */
	public String alertParam(){
		System.out.println("加載預警參數页面");
		return SUCCESS;
	}
	public String alertParam1(){
		System.out.println("打开头部文件");
		return SUCCESS;
	}
	//加载 全局票存数据  
	public String alertParam2(){
		System.out.println("打开右文件");
		return SUCCESS;
	}
	
	
}
