package com.cjit.ws.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cjit.ws.entity.VmsTransType;

public class VmsTransTypeDaoImpl  extends SqlMapClientDaoSupport implements VmsTransTypeDao{

	@Override
	public String insert(VmsTransType vmsTransType){
		try{
			getSqlMapClientTemplate().insert("insertVmsTransType",vmsTransType);
		}catch(Exception e){
			return "插入数据失败,请检查报文是否正确，稍后重试！";
		}
		return "Y";
	}

}
