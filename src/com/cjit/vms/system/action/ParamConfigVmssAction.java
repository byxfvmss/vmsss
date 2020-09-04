package com.cjit.vms.system.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.trans.action.DataDealAction;
import com.jcraft.jsch.Session;




public class ParamConfigVmssAction extends DataDealAction{

	private static final long serialVersionUID = 1L;
	
	private ParamConfigVmssService paramConfigVmssService;
	private Map paramMaps;
	private String selectTab;
	private UBaseSysParamVmss  uBaseSysParamVmss=new UBaseSysParamVmss();
	private UBaseSysParamVmss  ut=new UBaseSysParamVmss();
	private UBaseSysParamVmss  ul=new UBaseSysParamVmss();
	private String innerHtml;
	private String taxParam;//税控参数
	private String message;

	


	/**
	 * <p> listParamConfig|显示参数</p>
	 * @return
	 */
	public String listParamConfigVmss(){ 
		try{  
			List systemIds = this.paramConfigVmssService.findSystemId();
			
			this.paramMaps = this.paramConfigVmssService.getParamsMap(systemIds);
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			if(StringUtils.isEmpty(this.selectTab)){
				this.selectTab = "00804";
			}
				return "disk";
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	public String listPageList(){
		try{
			List systemIds = this.paramConfigVmssService.findSystemId();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.request.getSession().removeAttribute("message");
			}
			this.paramMaps = this.paramConfigVmssService.getParamsMap(systemIds);
			 ul=paramConfigVmssService.getUBaseSysParamVmssByName("左边距");
			 ut=paramConfigVmssService.getUBaseSysParamVmssByName("上边距");
			taxParam = paramConfigVmssService.findvaluebyName("税控参数");
			message=(String) this.request.getSession().getAttribute("message");
			if(StringUtils.isEmpty(this.selectTab)){
				this.selectTab = "00802";
			}
			if(taxParam.equals(TaxSelvetUtil.tax_Server_ch)){
				return "tax";
			}else{
				return "disk";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	public void savePageMargins(){
		String lvalue=request.getParameter("lvalue");
		String tvalue=request.getParameter("tvalue");
		String tparamId=request.getParameter("tparamId");
		String lparamId=request.getParameter("lparamId");
		paramConfigVmssService.updateUBaseSysParamVmssByParamId(lvalue, lparamId);
		paramConfigVmssService.updateUBaseSysParamVmssByParamId(tvalue, tparamId);
		message="保存成功";
		this.request.getSession().setAttribute("message",
				message);
		
	}
	public String saveParamVmssConfig(){
		try{
//
				List paramList = new ArrayList();
				Map paramMaps  = this.request.getParameterMap();
				Set reqKey = paramMaps.keySet();
				for(Iterator iterator = reqKey.iterator(); iterator.hasNext();){
					String key = (String) iterator.next();
					if(key.startsWith("param_")){
						UBaseSysParamVmss param = new UBaseSysParamVmss();
						param.setParamId(new Integer(Integer.parseInt(key
								.replaceAll("param_", ""))));
						String[] reqParam = (String[]) paramMaps.get(key);
						if(reqParam != null && reqParam.length == 1){
							if(reqParam[0].trim().getBytes().length > 200){
								this.innerHtml = "cannot";
								return "isOvered";
							}
							param.setSelectedValue(reqParam[0].trim());
							
							paramList.add(param);
						}
					}
				}
				
					paramConfigVmssService.saveParamConfig(paramList);
					if(StringUtils.isEmpty(this.selectTab)){
						this.selectTab = "00804";
					}
				this.innerHtml = "success";
				return SUCCESS;
			//}
		}catch (Exception e){
			
		}
		return ERROR;
	}

	/**
	 * @param paramConfigVmssService Ҫ���õ� paramConfigVmssService
	 */
	public void setparamConfigVmssService(ParamConfigVmssService paramConfigVmssService){
		this.paramConfigVmssService = paramConfigVmssService;
	}

	/**
	 * @return selectTab
	 */
	public String getSelectTab(){
		return selectTab;
	}

	/**
	 * @param selectTab Ҫ���õ� selectTab
	 */
	public void setSelectTab(String selectTab){
		this.selectTab = selectTab;
	}




	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public Map getParamMaps() {
		return paramMaps;
	}

	public void setParamMaps(Map paramMaps) {
		this.paramMaps = paramMaps;
	}



	public UBaseSysParamVmss getuBaseSysParamVmss() {
		return uBaseSysParamVmss;
	}



	public void setuBaseSysParamVmss(UBaseSysParamVmss uBaseSysParamVmss) {
		this.uBaseSysParamVmss = uBaseSysParamVmss;
	}

	public String getInnerHtml() {
		return innerHtml;
	}

	public void setInnerHtml(String innerHtml) {
		this.innerHtml = innerHtml;
	}

	public String getTaxParam() {
		return taxParam;
	}

	public void setTaxParam(String taxParam) {
		this.taxParam = taxParam;
	}
	public UBaseSysParamVmss getUt() {
		return ut;
	}
	public void setUt(UBaseSysParamVmss ut) {
		this.ut = ut;
	}
	public UBaseSysParamVmss getUl() {
		return ul;
	}
	public void setUl(UBaseSysParamVmss ul) {
		this.ul = ul;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
