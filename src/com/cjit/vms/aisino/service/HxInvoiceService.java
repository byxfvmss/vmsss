package com.cjit.vms.aisino.service;

/**
 * 航天信息的发票操作接口
 * @author xiezhp
 *
 */
public interface HxInvoiceService {

	/**
	 * 获取库存信息
	 * @param xml
	 * @return
	 */
	public String queryInventory(String xml); 
	
	
	/**
	 * 发票开具
	 * @param xml
	 * @return
	 */
	public String issueInvoice(String xml); 
	
	/**
	 * 发票打印
	 * @param xml
	 * @return
	 */
	public String printInvoice(String xml); 

	/**
	 * 发票作废
	 * @param xml
	 * @return
	 */
	public String invalidInvoice(String xml); 

	/**
	 * 清单打印
	 * @param xml
	 * @return
	 */
	public String printList (String xml); 

	/**
	 * 发票上传
	 * @param xml
	 * @return
	 */
	public String uploadInvoice (String xml); 

	/**
	 * 发票状态更新
	 * @param xml
	 * @return
	 */
	public String updateInvoiceStatus (String xml); 

	/**
	 * 空白发票作废
	 * @param xml
	 * @return
	 */
	public String invalidBlankInvoice (String xml);

	/**
	 * 金税盘状态查询
	 * @param xml
	 * @return
	 */
	public String queryJSPInfo (String xml); 

	/**
	 * 打印参数设置
	 * @param xml
	 * @return
	 */
	public String printParams (String xml); 

	/**
	 * 红字信息表上传（发票红冲）
	 * @param xml
	 * @return
	 */
	public String uploadRedInfo (String xml); 

	/**
	 * 汇总抄报
	 * @param xml
	 * @return
	 */
	public String copyTax (String xml); 

	/**
	 * 远程清卡
	 * @param xml
	 * @return
	 */
	public String clearCard (String xml); 
	
	
}
