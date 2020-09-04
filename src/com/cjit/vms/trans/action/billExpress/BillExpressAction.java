package com.cjit.vms.trans.action.billExpress;

import java.util.List;

import com.cjit.vms.customer.model.CustomerAddress;
import com.cjit.vms.customer.model.CustomerReceiver;
import com.cjit.vms.customer.service.CustomerReceiveService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.service.billExpress.BillExpressService;

public class BillExpressAction extends DataDealAction{
	
	private CustomerReceiveService customerReceiveService;

	private List customerReceiverList;
	private List customerAddressList;
	
	private List receiveTypeList;//收件类型
	private List receiveStatusList;//收件状态
	
	private List receiverTypeList;//收件人List
	private BillExpressService billExpressService;
	
	public String listBillExpress(){
		
		return SUCCESS;
	}

	/***
	 * 初始化客户初始化信息
	 * @param customerId
	 */
	private void initCustomerReceiveInfo(String customerId){
		CustomerReceiver customerReceiver = new CustomerReceiver();
		customerReceiver.setCustomerId(customerId);
		customerReceiverList = customerReceiveService.selectCustomerReceiver(customerReceiver, null);
		
		CustomerAddress customerAddress = new CustomerAddress();
		customerAddress.setCustomerId(customerId);
		customerAddressList = customerReceiveService.selectCustomerAddress(customerAddress, null);
	}
	public CustomerReceiveService getCustomerReceiveService() {
		return customerReceiveService;
	}

	public void setCustomerReceiveService(
			CustomerReceiveService customerReceiveService) {
		this.customerReceiveService = customerReceiveService;
	}

	public List getCustomerReceiverList() {
		return customerReceiverList;
	}

	public void setCustomerReceiverList(List customerReceiverList) {
		this.customerReceiverList = customerReceiverList;
	}

	public List getCustomerAddressList() {
		return customerAddressList;
	}

	public void setCustomerAddressList(List customerAddressList) {
		this.customerAddressList = customerAddressList;
	}

	public List getReceiveTypeList() {
		return receiveTypeList;
	}

	public void setReceiveTypeList(List receiveTypeList) {
		this.receiveTypeList = receiveTypeList;
	}

	public List getReceiveStatusList() {
		return receiveStatusList;
	}

	public void setReceiveStatusList(List receiveStatusList) {
		this.receiveStatusList = receiveStatusList;
	}

	public List getReceiverTypeList() {
		return receiverTypeList;
	}

	public void setReceiverTypeList(List receiverTypeList) {
		this.receiverTypeList = receiverTypeList;
	}

	public BillExpressService getBillExpressService() {
		return billExpressService;
	}

	public void setBillExpressService(BillExpressService billExpressService) {
		this.billExpressService = billExpressService;
	}
}
