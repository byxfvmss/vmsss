package com.cjit.vms.trans.service.createBill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.CreateBillService;

public interface BillValidationService {

	public CheckResult shortCircuitValidation(List transInfoList) ;
	public List<CheckResult> validation(List transInfoList);
	public List<CheckResult> validationAll(List transInfoList);
	public List checkingTransByCherNum(TransInfo transInfo, boolean flag);

	public List findTransInfoListByTransId(String[] selectTransId);
	
}
