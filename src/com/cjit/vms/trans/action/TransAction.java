package com.cjit.vms.trans.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.trans.ConnCust;
import com.cjit.vms.trans.util.DataUtil;

public class TransAction extends DataDealAction {

	protected static final long serialVersionUID = 1L;
	protected List transInfoList;
	protected List transEditList;
	protected BillInfo billInfo = new BillInfo();
	protected TransInfo transInfo = new TransInfo();
	protected TransInfoYS transInfoYS = new TransInfoYS();
	// 交易类型列表
	protected List businessList = new ArrayList();
	// 交易状态列表
	protected List transDataStatusList = new ArrayList();
	// 记录类型列表
	protected List transDataKindList = new ArrayList();
	// 客户纳税人类别列表
	protected List custTaxPayerTypeList = new ArrayList();
	// 交易类型
	protected String transType;
	// 客户信息
	protected String customerName;
	protected String customerAccont;
	// 运行逻辑信息
	protected String message;
	protected String[] money;
	protected String transId;
	protected String userId;
	protected String reverseTransId;
	protected String[] selectTransIds;
	//关联客户对象
	protected ConnCust connCust = new ConnCust();
	// 数据来源列表
	protected List kbcDataSouceList = new ArrayList();
	
	/**
	 * 菜单进入，查询交易信息列表（且冲账标识字段为N的记录）
	 * 
	 * @return String
	 */
	public String listTrans() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		User user = this.getCurrentUser();
		try {
			if ("bill".equalsIgnoreCase(fromFlag)) {
				this.setTransInfo(transInfo);
			}
			// 构造查询条件
			TransInfo transInfo = new TransInfo();
			transInfo = this.getTransInfo();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			if (user != null) {
				transInfo.setUserId(user.getId());
			}
			transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
			custTaxPayerTypeList = this.createSelectList(
					DataUtil.TAXPAYER_TYPE, null, true);
			transDataStatusList = DataUtil
					.getTransDataStatusListForPageListTrans();
			// 查询kbc数据来源
			kbcDataSouceList = DataUtil.getKbcDataSouceList();
			// 查询 符合条件的customer_id
			String customerName = transInfo.getCustomerName();
			List customerObjs = new ArrayList();
			List customerIds = new ArrayList();
			if (null != customerName && !customerName.equals("")) {
				customerObjs = customerService
						.findTransByCustomers(customerName);
			}
			for (int i = 0; i < customerObjs.size(); i++) {
				String customerId = ((Customer) customerObjs.get(i))
						.getCustomerID();
				customerIds.add(customerId);
			}
			transInfo.setCustomerIds(customerIds);
			// 查询符合条件的transType
			String transName = transInfo.getTransTypeName();
			List transNameObjs = new ArrayList();
			List transTypeList = new ArrayList();
			if (null != transName && !transName.equals("")) {
				Map map = new HashMap();
				map.put("transName", transName);
				map.put("customerTaxPayerType", transInfo
						.getCustomerTaxPayerType());
				transNameObjs = transInfoService.findTransTypeList(map);
			}
			for (int i = 0; i < transNameObjs.size(); i++) {
				TransTypeInfo transType = ((TransTypeInfo) transNameObjs.get(i));
				String transTypeId = ((TransTypeInfo) transNameObjs.get(i))
						.getTransTypeId();
				transTypeList.add(transTypeId);
			}
			transInfo.setTransTypeList(transTypeList);
			
			this.paginationList.setShowCount("false");
			transInfoList = transInfoService.findTransInfoList(transInfo,
					paginationList);
			this.request.setAttribute("message", this.message);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			logManagerService.writeLog(request, user, "0001.0010", "查询开票",
					"查询", "查询可操作的交易记录信息", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, user, "0001.0010", "查询开票",
					"查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTrans", e);
		}
		return ERROR;
	}


	public String listTransBack() {
		return SUCCESS;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}

	public List getTransDataStatusList() {
		return transDataStatusList;
	}

	public void setTransDataStatusList(List transDataStatusList) {
		this.transDataStatusList = transDataStatusList;
	}

	public List getTransDataKindList() {
		return transDataKindList;
	}

	public void setTransDataKindList(List transDataKindList) {
		this.transDataKindList = transDataKindList;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAccont() {
		return customerAccont;
	}

	public void setCustomerAccont(String customerAccont) {
		this.customerAccont = customerAccont;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getMoney() {
		return money;
	}

	public void setMoney(String[] money) {
		this.money = money;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public TransInfoYS getTransInfoYS() {
		return transInfoYS;
	}

	public void setTransInfoYS(TransInfoYS transInfoYS) {
		this.transInfoYS = transInfoYS;
	}

	public String getReverseTransId() {
		return reverseTransId;
	}

	public void setReverseTransId(String reverseTransId) {
		this.reverseTransId = reverseTransId;
	}

	public List getCustTaxPayerTypeList() {
		return custTaxPayerTypeList;
	}

	public void setCustTaxPayerTypeList(List custTaxPayerTypeList) {
		this.custTaxPayerTypeList = custTaxPayerTypeList;
	}

	public List getTransEditList() {
		return transEditList;
	}

	public void setTransEditList(List transEditList) {
		this.transEditList = transEditList;
	}


	public ConnCust getConnCust() {
		return connCust;
	}


	public void setConnCust(ConnCust connCust) {
		this.connCust = connCust;
	}


	public List getKbcDataSouceList() {
		return kbcDataSouceList;
	}


	public void setKbcDataSouceList(List kbcDataSouceList) {
		this.kbcDataSouceList = kbcDataSouceList;
	}

}
