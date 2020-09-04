package com.cjit.vms.taxdisk.single.service;

public interface BillIssueDiskService {
	/**
	 * @param diskNo
	 * @param fapiaoType
	 * @return 找到分发表中未使用的数量的数量
	 */
	public long findBillBalanceCancelNum(String diskNo,String fapiaoType);
	/**
	 * @param diskNo
	 * @param fapiaoType
	 * @return 校验该税控盘下的该发票类型的数量
	 */
	public boolean checkStockNum(String diskNo,String fapiaoType,int num);
	
	/**
	 * @param StriingXml
	 * @return 将空白作废 xml 或 开具作废封装到messge 中 返回json 字符串
	 * @throws Exception 
	 */
	public String createBillIssueXml(String StringXml,String diskNo,String billId,String userId) throws Exception;
	public void updateBillIssueXml(String StringXml,String id) throws Exception;
}
