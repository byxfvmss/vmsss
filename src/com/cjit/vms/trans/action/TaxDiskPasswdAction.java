package com.cjit.vms.trans.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.service.TaxDiskPasswdService;


/**
 * 参数配置_税控盘密码管理管理Action类
 *
 * @author jobell
 */
public class TaxDiskPasswdAction extends DataDealAction{

	/**
	 * @Action 税控盘密码管理 查询页面
	 * 
	 * @author jobell
	 * @return
	 */
	public String taxDiskPasswdList(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			InstInfo in = new InstInfo();
			in.setUserId(this.getCurrentUser().getId());
			
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			List instTaxno = taxDiskPasswdService.getInstInfoTaxNoList(in,paginationList);
			this.setTaxperLists(instTaxno);
			
			
			TaxDiskInfo info = new TaxDiskInfo();
			info.setTaxpayerNo(taxPerNumber);
			info.setTaxpayerNam(taxperName);
			info.setUser_id(this.getCurrentUser().getId());
			info.setTaxDiskNo(taxDiskNo);
			taxDiskPasswdService.findTaxDiskPasswdInfoList(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskPasswdAction-taxDiskPasswdList", e);
		}
		return ERROR;
	}
	
	
	/**
	 * @Action 税控盘密码管理 添加页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String initAddTaxDiskPasswd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskPasswdAction-initAddTaxDiskPasswd", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘密码管理 添加逻辑处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String saveTaxDiskPasswd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			taxDiskPasswdService.saveTaxDiskInfo(taxDiskInfo); 
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskPasswdAction-saveTaxDiskPasswd", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘密码管理 编辑页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public String editTaxDiskPasswd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			taxDiskInfo=taxDiskPasswdService.findTaxDiskInfoDetail(taxDiskNo, taxPerNumber);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskPasswdAction-editTaxDiskPasswd", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘密码管理 编辑更新逻辑处理
	 * 
	 * @author jobell
	 * @return
	 */
	public String updateTaxDiskPasswd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			taxDiskPasswdService.updateTaxDiskInfo(taxDiskInfo); 
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskPasswdAction-updateTaxDiskPasswd", e);
		}
		return ERROR;
	}
	
	/**
	 * @Action 税控盘监控信息管理 删除处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String deleteTaxDiskPasswd(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			taxDiskPasswdService.deleteTaxDiskPasswd(checked_tax_disk_no);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TaxDiskMonitorAction-listTaxDiskMonitor", e);
		}
		return ERROR;
	}
	
	
	/**
	 * @Action 添加验证数据存在
	 * 
	 * @author jobell
	 * @return
	 */
	public void checkTaxDiskInfo(){
		JSONObject jsonObject=new JSONObject();
		try{
			Long infoCount=taxDiskPasswdService.CountTaxDiskInfo(taxDiskInfo.getTaxDiskNo(), taxDiskInfo.getTaxpayerNo());
			if(infoCount.longValue()>0){
				jsonObject.put("msg","ng");
			}else{
				jsonObject.put("msg","ok");	
			}
		}catch(Exception e){
			jsonObject.put("msg","ng");
		}
		
		try {
			response.getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//参数传递声明
	private String taxPerNumber;
	private String taxperName;
	private String taxDiskNo;
	private List lstAuthInstId = new ArrayList();
	private TaxDiskInfo taxDiskInfo;
	private String checked_tax_disk_no[];
	public String getTaxPerNumber() {
		return taxPerNumber;
	}
	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}
	public String getTaxperName() {
		return taxperName;
	}
	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public TaxDiskInfo getTaxDiskInfo() {
		return taxDiskInfo;
	}
	public void setTaxDiskInfo(TaxDiskInfo taxDiskInfo) {
		this.taxDiskInfo = taxDiskInfo;
	}
	public String[] getChecked_tax_disk_no() {
		return checked_tax_disk_no;
	}
	public void setChecked_tax_disk_no(String[] checked_tax_disk_no) {
		this.checked_tax_disk_no = checked_tax_disk_no;
	}

	/*Service 注入声明*/
	private TaxDiskPasswdService taxDiskPasswdService;
	public TaxDiskPasswdService getTaxDiskPasswdService() {
		return taxDiskPasswdService;
	}
	public void setTaxDiskPasswdService(TaxDiskPasswdService taxDiskPasswdService) {
		this.taxDiskPasswdService = taxDiskPasswdService;
	}
}
