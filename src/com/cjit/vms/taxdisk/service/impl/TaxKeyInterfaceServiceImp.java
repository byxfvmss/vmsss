package com.cjit.vms.taxdisk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.taxdisk.service.TaxKeyInterfaceService;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

/**
 * 税控盼钥匙管理接口实现类
 * @author john
 *
 */
public class TaxKeyInterfaceServiceImp extends GenericServiceImpl implements TaxKeyInterfaceService{

	@Override
	public List getTaxKeyInfoList(VmsTaxKeyInfo taxKeyInfo, PaginationList paginationList) throws Exception {
		Map map = new HashMap();
		map.put("taxKeyNo", taxKeyInfo.getTaxKeyNo()); // 税控钥匙编号
		map.put("taxNo", taxKeyInfo.getTaxNo()); // 纳税人识别号
		return this.find("findTaxKeyInfoList", map,paginationList);
	}

	@Override
	public int updateTaxKey(VmsTaxKeyInfo taxKeyInfo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteTaxKey(String[] selectTaxKeys) throws Exception {
		if(null != selectTaxKeys){
			for(int i=0;i<selectTaxKeys.length;i++){
				String ids=selectTaxKeys[i];
				String[] arr=StringUtils.split(ids, ",");
				Map params = new HashMap();
				params.put("taxKeyNo", arr[0]);
				params.put("taxNo", arr[1]);
				this.delete("deleteTaxKeyInfo", params);
			}
		}
	}

	@Override
	public int saveTaxKeyInfo(String operType, VmsTaxKeyInfo taxKeyInfo) throws Exception {
		int result_flag = 0;
		Map map = new HashMap();
		map.put("taxKeyInfo", taxKeyInfo); 
		if("edit".equals(operType)){
			this.update("updateTaxKeyInfo", map);
		}else{
			int add_result=this.save("saveTaxKeyInfo", map);
			if(add_result<1){
				return -1;
			}
		}
		return result_flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VmsTaxKeyInfo getTaxKeyInfoDetail(String taxKeyNo, String taxNo) {
		Map map = new HashMap();
		map.put("taxKeyNo", taxKeyNo);
		map.put("taxNo", taxNo);
		return (VmsTaxKeyInfo) this.findForObject("findTaxKeyInfoDetail", map);
	}

}
