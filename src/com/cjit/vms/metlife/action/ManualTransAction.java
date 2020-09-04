package com.cjit.vms.metlife.action;

import java.util.List;

import com.cjit.vms.metlife.model.ManualTrans;
import com.cjit.vms.metlife.service.ManualTransService;
import com.cjit.vms.trans.action.DataDealAction;

public class ManualTransAction  extends DataDealAction{
	
	
	private ManualTrans trans;
	private ManualTransService manualTransService;
	
	
	public String findManualTrans(){
		
		try {
			if(trans==null){
				trans = new ManualTrans();
			}
			List l = manualTransService.findManualTrans(trans, paginationList);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}


	public ManualTrans getTrans() {
		return trans;
	}


	public void setTrans(ManualTrans trans) {
		if (trans != null) {
			this.trans = trans;
		}
	}


	public ManualTransService getManualTransService() {
		return manualTransService;
	}


	public void setManualTransService(ManualTransService manualTransService) {
			this.manualTransService = manualTransService;
	}
	
	
	
}
