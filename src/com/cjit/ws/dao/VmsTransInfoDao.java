package com.cjit.ws.dao;

import java.util.Map;

import com.cjit.ws.entity.VmsTransInfo;


public interface VmsTransInfoDao {
	public String insert(VmsTransInfo vmsTransInfo);

	public String find(Map map);
}
