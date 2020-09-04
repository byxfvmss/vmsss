/**
 * UserInterfaceAction
 */
package com.cjit.gjsz.interfacemanager.action;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.model.UserInterface;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceService;

/**
 * @author huboA
 */
public class UserInterfaceAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 897373549820440415L;
	private UserInterfaceService userInterfaceService;
	private UserInterface userInterface = new UserInterface();

	public String list(){
		try{
			userInterfaceService.findUserInterface(userInterface,
					paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String create(){
		try{
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String save(){
		try{
			if(userInterfaceService.checkUserInterface(userInterface)){
				userInterfaceService.saveUserInterface(userInterface);
				return SUCCESS;
			}else{
				this.addActionMessage("该接口已存在，请重新输入");
				create();
				return INPUT;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete(){
		try{
			userInterfaceService.deleteUserInterface(ids);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		try{
			userInterface = userInterfaceService
					.getUserInterface(userInterface);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public void setUserInterfaceService(
			UserInterfaceService userInterfaceService){
		this.userInterfaceService = userInterfaceService;
	}

	public UserInterface getUserInterface(){
		return userInterface;
	}

	public void setUserInterface(UserInterface userInterface){
		this.userInterface = userInterface;
	}
}
