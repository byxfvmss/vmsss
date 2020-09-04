package com.cjit.vms.trans.action.createBillAuto;

import java.util.Date;
import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.util.DataUtil;

public class createBillAutoAction extends DataDealAction {
	
	
	CreateBillService createBillService;
	BillValidationService billValidationService;

	public String createBillAuto() {
		log.info("-------------------------------------------------------");
		TransInfo trans = new TransInfo();
		// 状态：未开票
		trans.setDatastatus(DataUtil.TRANS_STATUS_1);
		// 非冲账
		trans.setIsReverse(DataUtil.NO);
		// 交易标识：2-实收实付
		trans.setTransFlag(DataUtil.TRANS_FLAG_2);
		// 开票日期
		trans.setFapiaoDate(DateUtils.toString(new Date(),
				DateUtils.ORA_DATES_FORMAT));
		
		paginationList.setShowCount("false");
		List transInfoList = createBillService.findTransList(trans, paginationList);
//		paginationList.setPageSize(30);
//		while (null !=transInfoList) {
//			transInfoList  = createBillService.findTransListAuto(trans, paginationList);
//			CheckResult checkResult =  billValidationService.shortCircuitValidation(transInfoList);
//			if (null!=checkResult) {
//				User currentUser = new User();
////				currentUser.setId(id);
//				createBillService.constructBillAndSaveAsMerge(transInfoList, currentUser);
//			}
//			paginationList.setCurrentPage(paginationList.getCurrentPage()+1);
//		}
		User currentUser = new User();
		CheckResult checkResult =  billValidationService.shortCircuitValidation(transInfoList);
		if (null==checkResult||"Y".equals(checkResult.getCheckFlag())) {
			createBillService.constructBillAndSaveAsMerge(transInfoList, currentUser);
		}
		
		
		log.info("-------------------------------------------------------");
		
		return SUCCESS;
	}

	public CreateBillService getCreateBillService() {
		return createBillService;
	}

	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}

	public BillValidationService getBillValidationService() {
		return billValidationService;
	}

	public void setBillValidationService(BillValidationService billValidationService) {
		this.billValidationService = billValidationService;
	}
}
