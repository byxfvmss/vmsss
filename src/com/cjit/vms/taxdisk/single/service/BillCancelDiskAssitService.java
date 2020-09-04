package com.cjit.vms.taxdisk.single.service;

import java.util.List;

import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.tools.AjaxReturn;

public interface BillCancelDiskAssitService {

	
	/**查看票据
	 *   @param billId  
	 *   @return
	 */
	 public BillInfo findBillInfo(String billId);
	 

	/**查看税控盘
	 * @param taxDiskNo 
	 * @return
	 */
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo);
	
	
	/**更改票据状态
	 * @param StringXml
	 * @throws Exception 
	 */
	public AjaxReturn updateBillCancelResult(String billId,String returnMsg,boolean flag) throws Exception;
	
	
	/**发票作废交易类型
	 * @param billId
	 * @return
	 */
	public List billCancelTransInfo(String billId);
	
	
	/**释放交易
	 * @param billId
	 * @param transId
	 */
	public void openBillCancelTrans(String billId);
	
}
