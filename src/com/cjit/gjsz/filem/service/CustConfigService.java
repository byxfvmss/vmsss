package com.cjit.gjsz.filem.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.filem.model.CustomerConfigEntity;

//组织机构代码名称对照表 接口
public interface CustConfigService{

	public List findAll(String custId, String custCode, String custName,
			String instCode, PaginationList paginationList);

	public CustomerConfigEntity findCustomerConfigByCustCode(String instCode,
			String custCode);

	public void save(CustomerConfigEntity custConfig);

	public void delete(String instCode, String custCode);

	public boolean judgeRepeatCustCode(String instCode, String custCode);
}