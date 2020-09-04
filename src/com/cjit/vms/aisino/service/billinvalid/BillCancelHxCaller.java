package com.cjit.vms.aisino.service.billinvalid;

/**
 * 发票作废服务
 * 
 * @author weishuang
 * 
 */
public interface BillCancelHxCaller {
	/**
	 * 作废发票
	 * 
	 * @return
	 * @throws Exception
	 */
	public BillCancelHxResult invalidBills(String ip, String port,String[] biilIds,
			String[] fpzl, String[] fphm, String[] fpdm) throws Exception;
}
