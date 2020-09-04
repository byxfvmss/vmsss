/**
 * UserInterface Service
 */
package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.UserInterface;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceService;

/**
 * @author huboA
 */
public class UserInterfaceServiceImpl extends GenericServiceImpl implements
		UserInterfaceService{

	public List findUserInterface(UserInterface userInterface){
		Map map = new HashMap();
		map.put("userInterface", userInterface);
		return this.find("findUserInterface", map);
	}

	public boolean checkUserInterface(UserInterface userInterface){
		Map map = new HashMap();
		map.put("userInterface", userInterface);
		List userInterfaces = this.find("checkUserInterface", map);
		if(CollectionUtil.isNotEmpty(userInterfaces)){
			UserInterface tmp = (UserInterface) userInterfaces.get(0);
			if(userInterface.getId() != tmp.getId()){
				return false;
			}
		}
		return true;
	}

	public void deleteUserInterface(UserInterface userInterface){
		throw new UnsupportedOperationException("还未实现");
	}

	public void deleteUserInterface(List ids){
		if(CollectionUtil.isNotEmpty(ids)){
			this.delete("deleteUserInterfaces", CollectionUtil
					.getNumberSQLIds(ids));
		}
	}

	public List findUserInterface(UserInterface userInterface,
			PaginationList paginationList){
		UserInterface tmp = new UserInterface();
		BeanUtils.copyProperties(userInterface, tmp);
		Map map = new HashMap();
		if(StringUtil.isNotEmpty(tmp.getName())){
			tmp.setName("%" + tmp.getName() + "%");
		}
		map.put("userInterface", tmp);
		return find("findUserInterface", map, paginationList);
	}

	public UserInterface getUserInterface(UserInterface userInterface){
		Map map = new HashMap();
		map.put("userInterface", userInterface);
		List list = this.find("getUserInterface", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (UserInterface) list.get(0);
		}else{
			return null;
		}
	}

	public UserInterface getUserInterfaceByFullName(UserInterface userInterface){
		Map map = new HashMap();
		map.put("userInterface", userInterface);
		List list = this.find("getUserInterfaceByFullName", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (UserInterface) list.get(0);
		}else{
			return null;
		}
	}

	public void saveUserInterface(UserInterface userInterface){
		Map map = new HashMap();
		map.put("userInterface", userInterface);
		if(userInterface.getId() > 0){
			this.update("updateUserInterface", map);
		}else{
			this.save("saveUserInterface", map);
		}
	}
}
