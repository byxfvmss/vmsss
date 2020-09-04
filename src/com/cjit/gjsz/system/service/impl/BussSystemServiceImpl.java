package com.cjit.gjsz.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.model.TableSQL;
import com.cjit.gjsz.system.service.BussSystemService;

/**
 * @author Larry
 */
public class BussSystemServiceImpl extends GenericServiceImpl implements
		BussSystemService {

	public List findTableSQLList(TableSQL tableSQL, String userId) {
		Map map = new HashMap();
		map.put("tableSQL", tableSQL);
		map.put("userId", userId);
		return this.find("findTableSQLList", map);
	}

	public void saveTableSQL(TableSQL tableSQL, boolean isUpdate) {
		Map param = new HashMap();
		param.put("tableSQL", tableSQL);
		if (isUpdate) {
			this.save("updateRptTableSQL", param);
		} else {
			this.save("saveRptTableSQL", param);
		}
	}
}
