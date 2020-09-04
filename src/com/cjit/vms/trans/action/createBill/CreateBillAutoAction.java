package com.cjit.vms.trans.action.createBill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.util.DataUtil;

public class CreateBillAutoAction extends DataDealAction {

	CreateBillService createBillService;
	BillValidationService billValidationService;
	
	List mergeFlagList = new ArrayList();

	public String createBillAuto() {
		
		
		// 获取合并标识
		this.getMergeFlagList();
		
		for(int j=0;j<this.mergeFlagList.size();j++)
		{
			TransInfo mergeflag = (TransInfo) this.mergeFlagList.get(j);
			
			String strMergeFlag=mergeflag.getMergeFlag();
			String strFlag = strMergeFlag.substring(0, 2);
			String strMergeSql=strMergeFlag.substring(2);
		
		TransInfo trans = new TransInfo();
		// 状态：未开票
		trans.setDatastatus(DataUtil.TRANS_STATUS_1);
		// 非冲账
		trans.setIsReverse(DataUtil.NO);
		// 交易标识：2-实收实付
		trans.setTransFlag(DataUtil.TRANS_FLAG_2);
		// 交易是否打票：A-自动打印
		trans.setTransFapiaoFlag(DataUtil.FAPIAO_FLAG_A);
		// 客户是否打票：A-自动打印
		// trans.setCustomerFaPiaoFlag(DataUtil.FAPIAO_FLAG_A);
		// 查询
		// trans.setSearchFlag(DataUtil.AUTO_INVOICE);
		// 开票日期
		trans.setFapiaoDate(DateUtils.toString(new Date(),
				DateUtils.ORA_DATES_FORMAT));

		paginationList.setShowCount("false");
		paginationList.setPageSize(5000);

		List transInfoList = new ArrayList();
		do {

			User currentUser = new User();
			transInfoList = createBillService.findTransList(trans,
					paginationList,strFlag,strMergeSql);

			List<CheckResult> checkResultList = billValidationService
					.validationAll(transInfoList);
			for (int i = 0; i < checkResultList.size(); i++) {
				CheckResult checkResult = checkResultList.get(i);
				if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
					TransInfo transInfo = checkResult.getTransInfo();
					if (null!=transInfo) {
						log.error(transInfo.getTransId()
								+ checkResult.getCheckResultMsg());
					}
					transInfoList.remove(transInfo);
				}

			}

			createBillService.constructBillAndSaveAsMerge(transInfoList,
					currentUser);
			paginationList.setCurrentPage(paginationList.getCurrentPage() + 1);
		} while (transInfoList.size() == paginationList.getPageSize());
		
		}
		return SUCCESS;
	}

	// 获取合并标识
	private void getMergeFlagList()
	{
		mergeFlagList = createBillService.findTransMergeFlagList();
	}
	
	public String listTransAuto(){
		
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

	public void setBillValidationService(
			BillValidationService billValidationService) {
		this.billValidationService = billValidationService;
	}
}
