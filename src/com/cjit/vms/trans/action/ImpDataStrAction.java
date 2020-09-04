package com.cjit.vms.trans.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.VmsTransInfo;
import com.cjit.vms.trans.model.VmsTransInfoStr;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.ImpDataService;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.config.ItemRateService;
import com.cjit.vms.trans.service.config.TransTypeService;

public class ImpDataStrAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private VmsTransInfo transInfo;
	private VmsTransInfoStr transInfoStr;
	private ImpDataService impDataService;
	private TransTypeService transTypeService;
	private ItemRateService itemRateService;
	private CustomerService customerService;
	private TransInfoService transInfoService;

	/**
	 * 查询数据详细信息
	 */
	public String showDataDetailStr() {
		List takeList = new ArrayList();
		List takeList1 = new ArrayList();
		String batchID = this.request.getParameter("impBatchId");
		VmsTransInfo vmsTransInfo = new VmsTransInfo();
		VmsTransInfoStr vmsTransInfoStr = new VmsTransInfoStr();
		vmsTransInfo.setBatchId(batchID);
		if (batchID != null) {
			takeList = impDataService.findVmsTransInfoBybatchID(vmsTransInfo,paginationList);
			// takeList1 = checkTranInfo(takeList);
			for (int i = 0; i < takeList.size(); i++) {
				VmsTransInfo transInfo = (VmsTransInfo) takeList.get(i);
				String status = transInfo.getdStatus();
				String[] info = status.split(",");
				transInfo.setArry(info);
				takeList1.add(transInfo);
			}
			request.setAttribute("transInfoImp", takeList1);
		}
		return SUCCESS;
	}

	public VmsTransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(VmsTransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public VmsTransInfoStr getTransInfoStr() {
		return transInfoStr;
	}

	public void setTransInfoStr(VmsTransInfoStr transInfoStr) {
		this.transInfoStr = transInfoStr;
	}

	public ImpDataService getImpDataService() {
		return impDataService;
	}

	public void setImpDataService(ImpDataService impDataService) {
		this.impDataService = impDataService;
	}

	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public ItemRateService getItemRateService() {
		return itemRateService;
	}

	public void setItemRateService(ItemRateService itemRateService) {
		this.itemRateService = itemRateService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public static String ArrayToString(String[] str) {
		StringBuffer str1 = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			str1.append(str[i]);
			if (i < str.length - 1) {
				str1.append("|");
			}
		}
		return str1.toString();
	}

	public static void main(String[] args) {
		
		String arry="0|0|0|1|0";
		String[] info=arry.split("|");
	}
}
