package com.cjit.vms.trans.service.taxDisk;

import com.cjit.vms.trans.model.UBaseInst;
import com.cjit.vms.trans.model.taxDisk.parseXml.TaxDiskInfoQueryReturnXml;

/**
 * 税控盘service
 * 
 * @author Tinna
 * 
 */
/**
 * @author Tinna
 *
 */
public interface TaxDiskInfoQueryService {

	/**
	 * @param taxDiskNo  查看税控盘
	 * @return
	 */
	public TaxDiskInfoQueryReturnXml queryTaxDiskInfo(String taxDiskNo);
	
	
	/**
	 * @param instID 根据机构编号查询纳税人识别号
	 * @return
	 */
	public UBaseInst queryTaxDiskInfoByinstID(String instID);
	
	/**
	 * @param StringXml instID 
	 * 
	 * 税控盘信息返回xml 文件
	 * 校验税控盘纳税人识别号与机构纳税人识别号是否存在
	 * @return
	 */
	public  String checkTaxDiskTaxNoAndInstNo(String StringXml,String instID);
	
	public String createTaxDiskInfoQueryXml() throws Exception;

}
