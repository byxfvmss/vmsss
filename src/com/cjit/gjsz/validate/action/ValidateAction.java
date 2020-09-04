package com.cjit.gjsz.validate.action;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.validate.service.ValidateService;

public class ValidateAction extends BaseListAction {

	private ValidateService validateService;

	public ValidateService getValidateService() {
		return validateService;
	}

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

	public String insert() {

		return SUCCESS;
	}
}
