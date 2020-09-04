/**
 * AuthorityAction
 */
package com.cjit.gjsz.system.action;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.service.DatabaseService;

/**
 * @author huboA
 */
public class DatabaseAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8036985650757519487L;
	private DatabaseService databaseService;

	public void setDatabaseService(DatabaseService databaseService){
		this.databaseService = databaseService;
	}

	public String view(){
		return SUCCESS;
	}

	public String backup(){
		String path = this.servletContext.getRealPath("/");
		databaseService.backup(path);
		return SUCCESS;
	}

	public String restore(){
		String path = this.servletContext.getRealPath("/");
		databaseService.restore(path);
		return SUCCESS;
	}
}
