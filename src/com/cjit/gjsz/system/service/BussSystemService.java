package com.cjit.gjsz.system.service;

import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.gjsz.system.model.TableSQL;

/**
 * 
 * @author Larry
 */
public interface BussSystemService extends GenericService {

	public List findTableSQLList(TableSQL tableSQL, String userId);

	public void saveTableSQL(TableSQL tableSQL, boolean isUpdate);

}
