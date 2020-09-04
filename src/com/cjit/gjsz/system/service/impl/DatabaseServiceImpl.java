/**
 * Authority Service
 */
package com.cjit.gjsz.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.service.DatabaseService;

/**
 * @author huboA
 */
public class DatabaseServiceImpl extends GenericServiceImpl implements
		DatabaseService{

	private String databaseName;

	public void setDatabaseName(String databaseName){
		this.databaseName = databaseName;
	}

	public void backup(String path){
		Map map = new HashMap();
		map.put("databaseName", databaseName);
		Date today = new Date();
		String fileName = databaseName + "_"
				+ DateUtils.toString(today, "yyyy-MM-dd-HH-mm-ss");
		map.put("databasePath", path + "/" + fileName);
		this.update("backupDatabase", map);
	}

	public void restore(String path){
		Map map = new HashMap();
		map.put("databaseName", databaseName);
		map.put("databasePath", path + databaseName + ".bak");
		this.update("restoreDatabase", map);
	}
}
