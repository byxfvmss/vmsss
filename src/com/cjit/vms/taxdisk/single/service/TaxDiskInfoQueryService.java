package com.cjit.vms.taxdisk.single.service;

import java.util.List;
import java.util.Map;

import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.taxdisk.tools.TaxDict;
import com.cjit.vms.trans.model.UBaseInst;

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
/**
 * @author me
 *
 */
public interface TaxDiskInfoQueryService {

	/**
	 * @param taxDiskNo  查看税控盘
	 * @return
	 */
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo);
	
	
	/**
	 * @param instID 根据机构编号查询纳税人识别号
	 * @return
	 */
	public UBaseInst findTaxDiskInfoByinstID(String instID);
	
	/**
	 * @param StringXml instID 
	 * 
	 * 税控盘信息返回xml 文件
	 * 校验税控盘纳税人识别号与机构纳税人识别号是否存在
	 * @return
	 */
	public  String checkTaxDiskTaxNoAndInstNo(String StringXml,String instID);
	
	/**
	 * @return 创建税控盘信息查询
	 * @throws Exception
	 */
	public String createTaxDiskInfoQueryXml() throws Exception;
	/**
	 * @return 发现注册码信息
	 */
	public String findTaxDiskRegInfo(String diskNo);
	
	/**
	 * 查询税控钥匙信息
	 * @param taxKeyNo 税控钥匙编号
	 * @return
	 */
	public VmsTaxKeyInfo findVmstaxKeyInfo(String taxKeyNo);
	
	/*获取参数*/
	public Map findSysParam(String itemKey);
	
	/*获取字典*/
	public List findCodeDictionaryList(String codeType, String codeSym);

}
