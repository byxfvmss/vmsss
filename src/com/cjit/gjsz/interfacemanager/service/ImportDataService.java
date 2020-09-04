/**
 * UserInterface Service
 */
package com.cjit.gjsz.interfacemanager.service;

import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.gjsz.interfacemanager.model.ImportModel;

/**
 * @author huboA
 */
public interface ImportDataService extends GenericService{

	/**
	 * 保存导入的记录(注意,有事务)
	 * @param strs
	 * @param nums
	 * @param records
	 * @param sql
	 * @return 导入的记录数
	 */
	public String saveImportDate(ImportModel importModel);

	/**
	 * 返回某个table表下的所有列信息
	 */
	public List getColumnInfos(String tableId);

	/**
	 * 根据tableId得到所有字典项
	 * @return
	 */
	public List getDictionarys(String tableId);

	/**
	 * 得到所有字典项(需要缓存起来)
	 * @return
	 */
	public List getAllDictionarys();
}
