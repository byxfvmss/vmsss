package com.cjit.vms.taxdisk.single.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.trans.model.UBaseInst;
import com.cjit.vms.taxdisk.single.model.TaxDiskInfoQuery;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.single.service.util.Message;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;
/**
 * 税控盘impl
 * 
 * @author Tinna
 * 
 */
public class TaxDiskInfoQueryServiceImpl extends GenericServiceImpl implements
		TaxDiskInfoQueryService {

	/**
	 * 根据taxDiskNo获取税控盘信息
	 */
	@Override
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo) {

		Map param = new HashMap();
		param.put("taxDiskNo", taxDiskNo);
		return (TaxDiskInfo) this.findForObject(
				"findTaxDiskInfoXmlByTaxDiskNo", param);

	}
	
	/**
	 * 根据机构编号查询纳税人识别号 taxperName; 纳税人名称 taxperNumber;
	 */
	@Override
	public UBaseInst findTaxDiskInfoByinstID(String instID) {
		Map param = new HashMap();
		param.put("instID", instID);
		List list = find("findUbaseInstTaxNo", param);
		UBaseInst inst = null;
		if (list.size() == 1) {
			inst = (UBaseInst) list.get(0);
		}
		return inst;  
	}

	/**
	 * 校验税控盘纳税人识别号与机构纳税人识别号是否存在  返回 messsge 类
	 * 
	 */
	@Override
	public String checkTaxDiskTaxNoAndInstNo(String StringXml, String instID) {
		Message message=new Message();
		String result = null;
		try {
			UBaseInst inst = findTaxDiskInfoByinstID(instID);
			if (inst.getTaxperNumber().isEmpty()) {
				message.setReturnMsg(Message.blank_inst_tax_no);
				message.setReturnCode(Message.error);
				JsonUtil.toJsonString(message);
			}
			TaxDiskInfoQueryReturnXml taxDiskInfoQRXml = new TaxDiskInfoQueryReturnXml(
					StringXml);

			if (Integer.parseInt(taxDiskInfoQRXml.getReturnCode()) != 0) {
				if (!taxDiskInfoQRXml.getTaxNo().equals(inst.getTaxperNumber())) {
					message.setReturnCode(Message.error);
					message.setReturnMsg(Message.tax_no_and_inst_tax_no_not);
					return	JsonUtil.toJsonString(message);
				} else {
					message.setReturnCode(Message.error);
					message.setReturnMsg(taxDiskInfoQRXml.getReturnMsg());
					return	JsonUtil.toJsonString(message);

				}
			}
			if(!inst.getTaxperName().equals(taxDiskInfoQRXml.getName())){
				message.setReturnCode(Message.error);
				message.setReturnMsg(Message.inst_name_Not_name);
				return	JsonUtil.toJsonString(message);

			}
		} catch (Exception e) {
			message.setReturnMsg(Message.system_exception);
			message.setReturnCode(Message.error);
			e.printStackTrace();
			return	JsonUtil.toJsonString(message);
		}
		message.setReturnCode(Message.success);
		return	JsonUtil.toJsonString(message);
	}
	
	public String createTaxDiskInfoQueryXml() throws Exception{
		Map map=new HashMap();
	     List list=	find("findTaxDiskapwd",map);
	     String pwd="";
	     if(list.size()==1){
	    	 pwd=(String) list.get(0);
	     }
	     TaxDiskInfoQuery taxInfoQuery=new TaxDiskInfoQuery();
	     taxInfoQuery.setId(TaxDiskUtil.id_tax_disk_information_query);
	     taxInfoQuery.setComment(TaxDiskUtil.comment_tax_disk_information_query);
	     taxInfoQuery.setApplyTypeCode(TaxDiskUtil.Application_type_code_1);
	     taxInfoQuery.setTaxDiskPwd(pwd);
	     return taxInfoQuery.createTaxDiskInfoQueryXml();
	     
	}

	@Override
	public String findTaxDiskRegInfo(String diskNo) {
		Map map=new HashMap();
		map.put("diskNo", diskNo);
		List list=find("findTaxDiskRegInfo", map);
		if(list.size()==1){
			return (String)list.get(0);
		}
		return null;
	}

	@Override
	public VmsTaxKeyInfo findVmstaxKeyInfo(String taxKeyNo) {
		Map<String,String> param = new HashMap<String,String>();
		param.put("taxKeyNo", taxKeyNo);
		return (VmsTaxKeyInfo) this.findForObject("findVmstaxKeyInfoXmlByTaxKeyNo", param);
	}


	public Map findSysParam(String itemKey) {
		Map param = new HashMap();
		param.put("ITEM_KEY", itemKey);
		Map map = new HashMap();
		List list = this.find("select_sys_param_vmss_tax", param);
		if (list != null && list.size() > 0) {
			return (Map) list.get(0);
		}
		return map;
	}
	
	public List findCodeDictionaryList(String codeType, String codeSym) {
		Map param = new HashMap();
		param.put("codeType", codeType);
		param.put("codeSym", codeSym);
		return this.find("findCodeDictionaryListTax", param);
	}
	
}
