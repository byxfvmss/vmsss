package com.cjit.ws.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.ws.entity.VmsCustomerInfo;

public class VmsCustomerInfoDaoImpl  extends SqlMapClientDaoSupport implements VmsCustomerInfoDao{

	@Override
	public String insert(VmsCustomerInfo vmsCustomerInfo) {
		try{
			getSqlMapClientTemplate().insert("insertVmsCustomerInfo",vmsCustomerInfo);
		}catch(Exception e){
			return "插入数据失败,请检查报文是否正确，稍后重试！";
		}
		return "Y";
	}

}
