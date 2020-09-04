package com.cjit.vms.trans.service.taxDisk.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.UBaseInst;
import com.cjit.vms.trans.model.taxDisk.TaxDiskInfoQuery;
import com.cjit.vms.trans.model.taxDisk.parseXml.TaxDiskInfoQueryReturnXml;
import com.cjit.vms.trans.service.taxDisk.TaxDiskInfoQueryService;
import com.cjit.vms.trans.service.taxDisk.util.Message;
import com.cjit.vms.trans.util.taxDisk.TaxDiskUtil;

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
	public TaxDiskInfoQueryReturnXml queryTaxDiskInfo(String taxDiskNo) {

		Map param = new HashMap();
		param.put("taxDiskNo", taxDiskNo);
		return (TaxDiskInfoQueryReturnXml) this.findForObject(
				"findTaxDiskInfoXmlByTaxDiskNo", param);

	}

	/**
	 * 根据机构编号查询纳税人识别号 taxperName; 纳税人名称 taxperNumber;
	 */
	@Override
	public UBaseInst queryTaxDiskInfoByinstID(String instID) {
		Map param = new HashMap();
		param.put("instID", instID);
		List list = (List) findForObject("findUbaseInstTaxNo", param);
		UBaseInst inst = null;
		if (list.size() == 1) {
			inst = (UBaseInst) list.get(0);
		}
		return inst;  
	}

	/**
	 * 校验税控盘纳税人识别号与机构纳税人识别号是否存在 null 代表成功
	 * 
	 */
	@Override
	public String checkTaxDiskTaxNoAndInstNo(String StringXml, String instID) {
		String result = null;
		try {
			UBaseInst inst = queryTaxDiskInfoByinstID(instID);
			if (inst.getTaxperNumber().isEmpty()) {
				return Message.blank_inst_tax_no;
			}
			TaxDiskInfoQueryReturnXml taxDiskInfoQRXml = new TaxDiskInfoQueryReturnXml(
					StringXml);

			if (Integer.parseInt(taxDiskInfoQRXml.getReturnCode()) != 0) {
				if (!taxDiskInfoQRXml.getTaxNo().equals(inst.getTaxperNumber())) {
					result = Message.tax_no_and_inst_tax_no_not;
				} else {
					result = taxDiskInfoQRXml.getReturnMsg();
				}
			}
		} catch (Exception e) {
			result = Message.system_exception;
			e.printStackTrace();
		}
		return result;
	}
	
	public String createTaxDiskInfoQueryXml() throws Exception{
	
		Map map=new HashMap();
		map.put("aa", "1");
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
	
	
}
