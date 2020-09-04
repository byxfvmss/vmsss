package com.cjit.vms.taxdisk.web.action;

import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.taxdisk.service.TaxKeyInterfaceService;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;

/**
 * 税控钥匙信息管理
 * @author john
 *
 */
public class TaxKeyInfoAction extends DataDealAction{

	private static final long serialVersionUID = 6325813421844912984L;

	private String taxKeyNo;
	private String taxNo;
	private String selectTaxKeys[];
	private String operType;
	private VmsTaxKeyInfo taxKeyInfo;
	/**
	 * 税控钥匙信息管理
	 * @return
	 */
	public String taxKeyInfoList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			VmsTaxKeyInfo taxKeyInfo=new VmsTaxKeyInfo();
			taxKeyInfo.setTaxKeyNo(taxKeyNo);
			taxKeyInfo.setTaxNo(taxNo);
			taxKeyInterfaceService.getTaxKeyInfoList(taxKeyInfo, paginationList);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxKeyInfoAction-taxKeyInfoList", e);
		}
		return ERROR;
	}
	
	/**
	 * 税控钥匙信息新增
	 * @return
	 */
	public String addTaxKeyInfo() {
		try{
			this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxKeyInfoAction-addTaxKeyInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 税控钥匙信息修改
	 * @return
	 */
	public String editTaxKeyInfo() {
		try {
			String taxKeyNo = request.getParameter("taxKeyNo");
			String taxNo = request.getParameter("taxNo");
			taxKeyInfo = taxKeyInterfaceService.getTaxKeyInfoDetail(taxKeyNo,taxNo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxKeyInfoAction-editTaxKeyInfo", e);
			return ERROR;
		}
		
		this.setOperType("edit");
		return SUCCESS;
	}
	
	/**
	 * 税控钥匙信息更新或新增
	 * @return
	 */
	public String saveTaxKeyInfo() {
		try{
			String operType = request.getParameter("operType");

			VmsTaxKeyInfo taxKeyInfo = new VmsTaxKeyInfo();
			taxKeyInfo.setTaxNo(request.getParameter("taxNo"));
			taxKeyInfo.setServletPort(request.getParameter("servletPort"));
			taxKeyInfo.setTaxKeyNo(request.getParameter("taxKeyNo"));
			taxKeyInfo.setIpAddress(request.getParameter("ipAddress"));
			taxKeyInfo.setBilTerminalFlag(request.getParameter("bilTerminalFlag"));
			int result=taxKeyInterfaceService.saveTaxKeyInfo(operType,taxKeyInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxKeyInfoAction-saveTaxKeyInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 删除税控钥匙信息
	 * @return
	 */
	public  String deleteTaxKeyInfo(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxKeyInterfaceService.deleteTaxKey(selectTaxKeys);;
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxKeyInfoAction-deleteTaxKeyInfo", e);
		}
		return ERROR;
	}
	
	/*service 声明*/
	private TaxKeyInterfaceService taxKeyInterfaceService;

	public TaxKeyInterfaceService getTaxKeyInterfaceService() {
		return taxKeyInterfaceService;
	}

	public void setTaxKeyInterfaceService(TaxKeyInterfaceService taxKeyInterfaceService) {
		this.taxKeyInterfaceService = taxKeyInterfaceService;
	}

	public String getTaxKeyNo() {
		return taxKeyNo;
	}

	public void setTaxKeyNo(String taxKeyNo) {
		this.taxKeyNo = taxKeyNo;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String[] getSelectTaxKeys() {
		return selectTaxKeys;
	}

	public void setSelectTaxKeys(String[] selectTaxKeys) {
		this.selectTaxKeys = selectTaxKeys;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public VmsTaxKeyInfo getTaxKeyInfo() {
		return taxKeyInfo;
	}

	public void setTaxKeyInfo(VmsTaxKeyInfo taxKeyInfo) {
		this.taxKeyInfo = taxKeyInfo;
	}

	
}
