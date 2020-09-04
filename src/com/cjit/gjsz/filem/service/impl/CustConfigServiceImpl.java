package com.cjit.gjsz.filem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.filem.model.CustomerConfigEntity;
import com.cjit.gjsz.filem.service.CustConfigService;

//组织机构代码名称对照表 接口实现类
public class CustConfigServiceImpl extends GenericServiceImpl implements
		CustConfigService{

	public List findAll(String custId, String custCode, String custName,
			String instCode, PaginationList paginationList){
		CustomerConfigEntity custConfig = new CustomerConfigEntity();
		custConfig.setCustId(custId);
		custConfig.setCustCode(custCode);
		custConfig.setCustName(custName);
		custConfig.setInstCode(instCode);
		Map map = new HashMap();
		map.put("custConfig", custConfig);
		return this.find("findCustConfigs", map, paginationList);
	}

	public CustomerConfigEntity findCustomerConfigByCustCode(String instCode,
			String custCode){
		CustomerConfigEntity custConfig = new CustomerConfigEntity();
		custConfig.setInstCode(instCode);
		custConfig.setCustCode(custCode);
		Map map = new HashMap();
		map.put("custConfig", custConfig);
		List l = this.find("findCustomerConfigByCustCode", map);
		if(CollectionUtils.isNotEmpty(l))
			custConfig = (CustomerConfigEntity) l.get(0);
		return custConfig;
	}

	public void save(CustomerConfigEntity custConfig){
		Map map = new HashMap();
		map.put("custConfig", custConfig);
		List l = this.find("findCustomerConfigByCustCode", map);
		if(CollectionUtils.isEmpty(l)){
			this.save("insertCustConfig", map);
		}else{
			this.save("updateCustConfig", map);
		}
	}

	public void delete(String instCode, String custCode){
		Map map = new HashMap();
		map.put("instCode", instCode);
		map.put("custCode", custCode);
		this.delete("deleteCustConfig", map);
	}

	public boolean judgeRepeatCustCode(String instCode, String custCode){
		CustomerConfigEntity custConfig = new CustomerConfigEntity();
		custConfig.setInstCode(instCode);
		custConfig.setCustCode(custCode);
		Map map = new HashMap();
		map.put("custConfig", custConfig);
		Long count = this.getRowCount("judgeRepeatCustCode", map);
		if(count != null && count.longValue() > 0){
			return true;
		}
		return false;
	}
}