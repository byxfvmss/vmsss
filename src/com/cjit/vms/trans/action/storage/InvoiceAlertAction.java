package com.cjit.vms.trans.action.storage;

import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.storage.InvoiceAlertListInfo;
import com.cjit.vms.trans.service.storage.InvoiceAlertService;

/**
 * 发票库存预警Action类
 * 
 */
public class InvoiceAlertAction extends DataDealAction{

	public String listInstAlert(){

		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			
			lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			InvoiceAlertListInfo info= new InvoiceAlertListInfo();
			info.setLstAuthInstId(lstAuthInstId);
			info.setInstId(inst_id);
			info.setInvoiceType(invoice_type);
			invoiceAlertService.findInvoiceAlertList(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceAlertAction-listInstAlert", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action
	 * 
	 * 发票库存预警，设置纳税机构发票库存预警值 初始化
	 * 
	 * @return
	 */
	public String initInstAlert() throws Exception{

		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			instInvoiceAlert=invoiceAlertService.findInstInvoiceAlert(inst_id, invoice_type);
			return SUCCESS; 
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InvoiceAlertAction-initInstAlert", e);
		}
		return ERROR;
	}
	
	
	/**
	 * @Action
	 * 
	 * 发票库存预警，设置纳税机构发票库存预警值 初始化
	 * 
	 * @return
	 */
	public String saveInstAlert() throws Exception{
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try{
			invoiceAlertService.saveInstInvoiceAlertValue(inst_id, invoice_type, alert_num);
			return SUCCESS; 
		}catch (Exception e){
			e.printStackTrace();
			log.error("InvoiceAlertAction-saveInstAlert", e);
		}
		return ERROR;
	}
	private String invoice_type;
	private String inst_id;
	private String inst_Name;
	private Integer alert_num;
	private InvoiceAlertListInfo instInvoiceAlert;
	private List lstAuthInstId;
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public InvoiceAlertListInfo getInstInvoiceAlert() {
		return instInvoiceAlert;
	}

	public void setInstInvoiceAlert(InvoiceAlertListInfo instInvoiceAlert) {
		this.instInvoiceAlert = instInvoiceAlert;
	}
	public Integer getAlert_num() {
		return alert_num;
	}

	public void setAlert_num(Integer alert_num) {
		this.alert_num = alert_num;
	}
	/*service 声明*/
	private InvoiceAlertService invoiceAlertService;
	public InvoiceAlertService getInvoiceAlertService() {
		return invoiceAlertService;
	}
	public void setInvoiceAlertService(InvoiceAlertService invoiceAlertService) {
		this.invoiceAlertService = invoiceAlertService;
	}

	public String getInst_Name() {
		return inst_Name;
	}

	public void setInst_Name(String inst_Name) {
		this.inst_Name = inst_Name;
	}
}
