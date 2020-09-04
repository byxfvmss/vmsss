package com.cjit.vms.customer.action;

import java.util.List;

import cjit.crms.util.DictionaryCodeType;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.customer.model.CustomerAddress;
import com.cjit.vms.customer.model.CustomerReceiver;
import com.cjit.vms.customer.service.CustomerReceiveService;
import com.cjit.vms.trans.action.DataDealAction;

/***
 * 客户收件信息维护
 * 
 * @author yang
 *
 */
public class CustomerReceiveAction extends DataDealAction {

	public CustomerReceiveService customerReceiveService;

	private CustomerAddress customerAddressSearch;// 条件
	private CustomerAddress customerAddress;
	private CustomerReceiver customerReceiverSearch;// 条件
	private CustomerReceiver customerReceiver;

	private String[] checkedlineNo;

	private List documentsTypeList;
	private List receiverTypeList;

	/*******************************
	 * 地址************************************** /***
	 * 
	 * @return
	 */
	public String listCustomerAddress() {
		customerReceiveService.selectCustomerAddress(customerAddressSearch,
				paginationList);
		return SUCCESS;
	}

	public String editCustomerAddress() {
		if (StringUtil.isNotEmpty(customerAddressSearch.getId())) {
			List list = customerReceiveService.selectCustomerAddress(
					customerAddressSearch, null);
			if (null != list && list.size() > 0) {
				customerAddress = (CustomerAddress) list.get(0);
			}
		}
		return SUCCESS;
	}

	public String saveCustomerAddress() {
		if (StringUtil.isNotEmpty(customerAddress.getId())) {
			customerReceiveService.updateCustomerAddress(customerAddress);
		} else {
			String resultMessages = verifyCustomerAddress(
					customerAddress.getCustomerId(),
					customerAddress.getAddressTag());
			if (StringUtil.isNotEmpty(resultMessages)) {
				setResultMessages(resultMessages);
				return ERROR;
			}

			customerReceiveService.insertCustomerAddress(customerAddress);

		}
		return SUCCESS;
	}

	public String deleteCustomerAddress() {
		for (int i = 0; i < checkedlineNo.length; i++) {
			CustomerAddress cus = new CustomerAddress();
			cus.setId(checkedlineNo[i]);
			customerReceiveService.removeCustomerAddress(cus);
		}
		return SUCCESS;
	}

	public String verifyCustomerAddress(String customerId, String addressTag) {
		CustomerAddress address = new CustomerAddress();
		address.setCustomerId(customerId);
		address.setAddressTag(addressTag);

		List list = customerReceiveService.selectCustomerAddress(address, null);
		if (null != list && list.size() > 0) {
			return "地址别名已存在";
		}

		return null;
	}

	/***************************************************************************
	 * 收件人
	 */
	public String listCustomerReceiver() {
		customerReceiveService.selectCustomerReceiver(customerReceiverSearch,
				paginationList);
		return SUCCESS;
	}

	public String editCustomerReceiver() {
		if (StringUtil.isNotEmpty(customerReceiverSearch.getId())) {
			List list = customerReceiveService.selectCustomerReceiver(
					customerReceiverSearch, null);
			if (null != list && list.size() > 0) {
				customerReceiver = (CustomerReceiver) list.get(0);
			}
		}
		return SUCCESS;
	}

	public String saveCustomerReceiver() {
		if (StringUtil.isNotEmpty(customerReceiver.getId())) {
			customerReceiveService.updateCustomerReceiver(customerReceiver);
		} else {
			customerReceiveService.insertCustomerReceiver(customerReceiver);
		}
		return SUCCESS;
	}

	public String deleteCustomerReceiver() {
		for (int i = 0; i < checkedlineNo.length; i++) {
			CustomerReceiver customerReceiver = new CustomerReceiver();
			customerReceiver.setId(checkedlineNo[i]);
			customerReceiveService.removeCustomerReceiver(customerReceiver);
		}
		return SUCCESS;
	}

	public CustomerReceiveService getCustomerReceiveService() {
		return customerReceiveService;
	}

	public void setCustomerReceiveService(
			CustomerReceiveService customerReceiveService) {
		this.customerReceiveService = customerReceiveService;
	}

	public CustomerAddress getCustomerAddressSearch() {
		return customerAddressSearch;
	}

	public void setCustomerAddressSearch(CustomerAddress customerAddressSearch) {
		this.customerAddressSearch = customerAddressSearch;
	}

	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}

	public CustomerReceiver getCustomerReceiverSearch() {
		return customerReceiverSearch;
	}

	public void setCustomerReceiverSearch(
			CustomerReceiver customerReceiverSearch) {
		this.customerReceiverSearch = customerReceiverSearch;
	}

	public CustomerReceiver getCustomerReceiver() {
		return customerReceiver;
	}

	public void setCustomerReceiver(CustomerReceiver customerReceiver) {
		this.customerReceiver = customerReceiver;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public List getDocumentsTypeList() {
		if (null == documentsTypeList) {
			documentsTypeList = this.userInterfaceConfigService.getDictionarys(
					DictionaryCodeType.DOCUMENT_TYPE, null);
		}
		return documentsTypeList;
	}

	public void setDocumentsTypeList(List documentsTypeList) {
		this.documentsTypeList = documentsTypeList;
	}

	public List getReceiverTypeList() {
		if (null == receiverTypeList) {
			receiverTypeList = this.userInterfaceConfigService.getDictionarys(
					DictionaryCodeType.RECEIVER_TYPE, null);
		}
		return receiverTypeList;
	}

	public void setReceiverTypeList(List receiverTypeList) {
		this.receiverTypeList = receiverTypeList;
	}

}
