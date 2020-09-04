package com.cjit.vms.trans.action.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Session;



import cjit.crms.util.DictionaryCodeType;

import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.AutoInvoice;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.ItemRateService;
import com.cjit.vms.trans.service.config.TransTypeService;
import com.opensymphony.xwork2.ActionContext;

public class ItemRateAction extends DataDealAction implements SessionAware{

	private ItemRateService itemRateService;
	private VerificationInfo verificationInfo;
	private List taxRateSelList;// 税率下拉框
	private String[] checkedlineNo;
	private String instCode;
	HttpServletRequest request  =ServletActionContext.getRequest();
	//交易类型查询条件对象
	TransTypeInfo transTypeInfoPram;//
	private TransTypeService transTypeService;
	
	
	public String mainItemRateInfo() {
		return SUCCESS;
	}

	public String itemRateTreeInst() {
		return SUCCESS;
	}

	public String frameHeadItemRate() {
		return SUCCESS;
	}
	
	public String selectItemRate() {
		//
		itemRateService.selectItemRate(verificationInfo, paginationList, false);
		return SUCCESS;
	}

	public String selectItemList() {
		verificationInfo.setTaxRate("");
		itemRateService.selectItemRate(verificationInfo, paginationList, true);
		return SUCCESS;
	}

	public String removeItemRate() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			for (int i = 0; i < checkedlineNo.length; i++) {
				VerificationInfo daObj = new VerificationInfo();
				daObj.setItemCode(checkedlineNo[i]);
				daObj.setTaxNo(verificationInfo.getTaxNo());
				daObj.setTaxRate(verificationInfo.getTaxRate());
				itemRateService.removeItemRate(daObj);
			}
		}

		return SUCCESS;
	}

	public String updateItemRate() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			for (int i = 0; i < checkedlineNo.length; i++) {
				VerificationInfo daObj = new VerificationInfo();
				daObj.setItemCode(checkedlineNo[i]);
				daObj.setTaxNo(verificationInfo.getTaxNo());
				daObj.setTaxRate(verificationInfo.getTaxRate());
				itemRateService.insertItemRate(daObj);
			}
		}
		return SUCCESS;
	}

	public String selectItemTransTypeList() {
		transTypeService.selectTransType(transTypeInfoPram, paginationList);
		return SUCCESS;
	}
		
		
	/*	
	 * 20160321
	 * 	
	 * screen
	 * 	
	 * wait
	 * 	
	 * */
		
	public String  itemRateTreeInst1(){
		return SUCCESS;
	}	
	public String alertParam(){
		return SUCCESS;
	}	
	public String alertParam1(){
		return SUCCESS;
	}	
	//加载票存数据  
	private List<AutoInvoice> list= new ArrayList<AutoInvoice>();
	public String alertParam2(){
		//拿到tree中节点的id
		list =  itemRateService.alertInvoice(instCode,paginationList);//返回结果已经封装到paginationList.recordList
		return SUCCESS;
	}	
	//显示编辑页面， instCode,invoiceType
	 private String  invoiceType;
	public String ParamEdit(){
		
	    list =  itemRateService.editInvoice(instCode, invoiceType, paginationList);
	    
		return SUCCESS;
	}
	//保存编辑参数
	private String alertNum;
	private String invoicePercent;
	public void	CommitEdit(){
		itemRateService.updateParam(instCode, invoiceType, alertNum, invoicePercent);
	}
	
	
//get set
	public ItemRateService getItemRateService() {
		return itemRateService;
	}
	
	public void setItemRateService(ItemRateService itemRateService) {
		this.itemRateService = itemRateService;
	}

	public VerificationInfo getVerificationInfo() {
		return verificationInfo;
	}

	public void setVerificationInfo(VerificationInfo verificationInfo) {
		this.verificationInfo = verificationInfo;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public List getTaxRateSelList() {
		if (null == taxRateSelList) {
			taxRateSelList = this.createSelectList(
					DictionaryCodeType.TAXRATE_TYPE, null, false);
		}
		return taxRateSelList;
	}

	public void setTaxRateSelList(List taxRateSelList) {
		this.taxRateSelList = taxRateSelList;
	}

	public TransTypeInfo getTransTypeInfoPram() {
		return transTypeInfoPram;
	}

	public void setTransTypeInfoPram(TransTypeInfo transTypeInfoPram) {
		this.transTypeInfoPram = transTypeInfoPram;
	}

	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public List<AutoInvoice> getList() {
		return list;
	}

	public void setList(List<AutoInvoice> list) {
		this.list = list;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getAlertNum() {
		return alertNum;
	}

	public void setAlertNum(String alertNum) {
		this.alertNum = alertNum;
	}

	public String getInvoicePercent() {
		return invoicePercent;
	}

	public void setInvoicePercent(String invoicePercent) {
		this.invoicePercent = invoicePercent;
	}

	 
}
