package com.cjit.vms.trans.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.TaxDiffCheckAccountInfo;
import com.cjit.vms.trans.service.TaxDiffCheckAccountService;


public class ForeignCurrencyTransDiffAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private List integrityCheckAccountList;
	private TaxDiffCheckAccountService taxDiffCheckAccountService;
	private TaxDiffCheckAccountInfo taxDiffCheckAccountInfo=new TaxDiffCheckAccountInfo();
	private String instcode;
	private String contractNo;
	private String goodsNo;
	private String expiryDate;
	private String customerCname;
	private String customerId;
	private Double glConfirmAmt;
	private Double taxConfirmAmt;
	private Double devAmt;
	private String receiveInstId;
	private String receiveInstName;
	private String goodsName;
	private String currency;
	private String transId;
	public String listForeignCurrencyTransDiff() {
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				taxDiffCheckAccountInfo=new  TaxDiffCheckAccountInfo();
			}
			taxDiffCheckAccountInfo=new  TaxDiffCheckAccountInfo();
			List InstId=new ArrayList();
			this.getAuthInstList(InstId);
			this.setAuthInstList(InstId);
			Map map = new HashMap();
			taxDiffCheckAccountInfo.setGoodsName(this.getGoodsName());
			taxDiffCheckAccountInfo.setReceiveInstId(this.getReceiveInstId());
			taxDiffCheckAccountInfo.setInstcode(this.getInstcode());
			map.put("goodsName", taxDiffCheckAccountInfo.getGoodsName());
			map.put("instcode", taxDiffCheckAccountInfo.getReceiveInstId());
			taxDiffCheckAccountService.findlistForeignCurrencyTransDiff(map, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public List getIntegrityCheckAccountList() {
		return integrityCheckAccountList;
	}
	public void setIntegrityCheckAccountList(List integrityCheckAccountList) {
		this.integrityCheckAccountList = integrityCheckAccountList;
	}

	public TaxDiffCheckAccountService getTaxDiffCheckAccountService() {
		return taxDiffCheckAccountService;
	}

	public void setTaxDiffCheckAccountService(
			TaxDiffCheckAccountService taxDiffCheckAccountService) {
		this.taxDiffCheckAccountService = taxDiffCheckAccountService;
	}

	public TaxDiffCheckAccountInfo getTaxDiffCheckAccountInfo() {
		return taxDiffCheckAccountInfo;
	}

	public void setTaxDiffCheckAccountInfo(
			TaxDiffCheckAccountInfo taxDiffCheckAccountInfo) {
		this.taxDiffCheckAccountInfo = taxDiffCheckAccountInfo;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCustomerCname() {
		return customerCname;
	}

	public void setCustomerCname(String customerCname) {
		this.customerCname = customerCname;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getGlConfirmAmt() {
		return glConfirmAmt;
	}

	public void setGlConfirmAmt(Double glConfirmAmt) {
		this.glConfirmAmt = glConfirmAmt;
	}

	public Double getTaxConfirmAmt() {
		return taxConfirmAmt;
	}

	public void setTaxConfirmAmt(Double taxConfirmAmt) {
		this.taxConfirmAmt = taxConfirmAmt;
	}

	public Double getDevAmt() {
		return devAmt;
	}

	public void setDevAmt(Double devAmt) {
		this.devAmt = devAmt;
	}

	public String getReceiveInstId() {
		return receiveInstId;
	}

	public void setReceiveInstId(String receiveInstId) {
		this.receiveInstId = receiveInstId;
	}

	public String getReceiveInstName() {
		return receiveInstName;
	}

	public void setReceiveInstName(String receiveInstName) {
		this.receiveInstName = receiveInstName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
	

	
}
