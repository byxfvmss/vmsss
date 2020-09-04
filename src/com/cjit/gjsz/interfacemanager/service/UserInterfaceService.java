/**
 * UserInterface Service
 */
package com.cjit.gjsz.interfacemanager.service;

import java.util.List;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.UserInterface;

/**
 * @author huboA
 */
public interface UserInterfaceService extends GenericService{

	/**
	 * 查找接口
	 * @param user
	 * @return
	 */
	public List findUserInterface(UserInterface userInterface);

	/**
	 * 新建接口
	 */
	public void saveUserInterface(UserInterface userInterface);

	/**
	 * 验证接口
	 * @param userInterface
	 * @return
	 */
	public boolean checkUserInterface(UserInterface userInterface);

	/**
	 * 删除接口
	 * @param userInterface
	 */
	public void deleteUserInterface(UserInterface userInterface);

	/**
	 * 删除接口
	 * @param userInterface
	 */
	public void deleteUserInterface(List ids);

	/**
	 * 查找接口
	 * @param userInterface
	 * @return
	 */
	public List findUserInterface(UserInterface userInterface,
			PaginationList paginationList);

	/**
	 * 取得单个接口
	 * @param userInterface
	 * @return
	 */
	public UserInterface getUserInterface(UserInterface userInterface);

	public UserInterface getUserInterfaceByFullName(UserInterface userInterface);
}
