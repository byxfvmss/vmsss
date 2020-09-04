package com.cjit.vms.taxdisk.single.action;

import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;

/**
 * @author tom 发票 打印辅助类
 *
 */
public class BillPrintDiskAssistAction extends BaseDiskAction  {
	private static final long serialVersionUID = 1L;
	private BillPrintDiskAssistService billPrintDiskAssistService;
	/**
	 *  得到打印的ids  经过message 的封装     校验打印限定值 
	 */
	public void getBillPrintIds(){
		
	}
	
	public BillPrintDiskAssistService getBillPrintDiskAssistService() {
		return billPrintDiskAssistService;
	}
	public void setBillPrintDiskAssistService(
			BillPrintDiskAssistService billPrintDiskAssistService) {
		this.billPrintDiskAssistService = billPrintDiskAssistService;
	}
	

	
}
