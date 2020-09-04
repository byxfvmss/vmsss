package com.cjit.vms.trans.service.config;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.AutoInvoice;
import com.cjit.vms.trans.model.config.VerificationInfo;

/***
 * 科目税率
 * @author yang
 *
 */
public interface ItemRateService {
	/***
	 * 
	 * @param verificationInfo
	 * @return
	 */
	public List selectItemRate(VerificationInfo verificationInfo, PaginationList paginationList,boolean itemCodeIsEmpty);
	
	
	
	public List selectItemRateBase(VerificationInfo verificationInfo);
	/***
	 * 
	 * @param verificationInfo
	 */
	public void insertItemRate(VerificationInfo verificationInfo);
	
	/***
	 * 
	 * @param verificationInfo
	 */
	public void removeItemRate(VerificationInfo verificationInfo);
	
	
	
	//参数预警
	public List alertInvoice(String  instId,PaginationList paginationList);
	//编辑参数
	public List editInvoice(String instId,String type,PaginationList paginationList); 
	//保存修改参数
	public  void updateParam(String instId,String type,String alertNum,String invoicePercent);
	
	
}
