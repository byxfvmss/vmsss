package com.cjit.vms.aisino.service;

import com.cjit.vms.aisino.service.impl.HxInvoiceServiceImpl;

public class HxServiceFactory {

	/**
	 * 获取发票操作处理的实现
	 * @return
	 */
	public static HxInvoiceService createHxInvoiceService(){
		return new HxInvoiceServiceImpl();
	}
}
