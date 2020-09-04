package com.cjit.gjsz.print.action;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;

import com.cjit.common.util.PropertiesUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.print.service.BasePrintService;
import com.cjit.gjsz.system.model.User;

public class PrintAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3722497036273212241L;
	private BasePrintService basePrintService;
	private DataDealService dataDealService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private String businessid;
	private String tableId;
	private String printType;
	private Map map;
	public static final LinkedMap PRINT_MAP = new LinkedMap();
	static{
		PRINT_MAP.put("t_base_income", "baseIncomeService");// 已经配置了:涉外收入申报单
		PRINT_MAP.put("t_base_remit", "baseRemitService"); // 已经配置了:境外汇款申请书
		PRINT_MAP.put("t_base_payment", "basePaymentService"); // 已经配置了:对外付款/承兑通知书
		PRINT_MAP.put("t_base_export", "baseExportService"); // 已经配置了:出口收汇核销专用联信息申报表（境内收入）+出口收汇核销专用联（境内收入）>>>>>>>
		PRINT_MAP.put("t_base_dom_remit", "baseDomRemitService"); // 已经配置了:境内汇款申请书
		PRINT_MAP.put("t_base_dom_pay", "baseDomPaymentService"); // 已经配置了:境内付款/承兑通知书>>>>>>>
		// PRINT_MAP.put("t_decl_income", "getDeclareIncomes");
		// PRINT_MAP.put("t_decl_remit", "getDeclareRemits");
		// PRINT_MAP.put("t_decl_payment", "getDeclarePayments");
		PRINT_MAP.put("t_fini_export", "financeExportService"); // 已经配置了:(涉外收入申报单对应的核销信息)出口收汇核销专用联信息申报表（境外收入）+出口收汇核销专用联（境外收入）
		// PRINT_MAP.put("t_fini_remit", "getFinanceRemits");
		// PRINT_MAP.put("t_fini_payment", "getFinancePayments");
		PRINT_MAP.put("t_fini_payment", "basePaymentService");
		// PRINT_MAP.put("t_fini_dom_remit", "getFinanceDomRemits");
		// PRINT_MAP.put("t_fini_dom_pay", "getFinanceDomPayments");
		PRINT_MAP.put("t_fini_dom_export", "financeDomExportService"); // 已经配置了:(出口收汇核销专用联信息申报表（境内收入）单对应的核销信息)出口收汇核销专用联信息申报表（境内收入）+出口收汇核销专用联（境内收入）
		// PRINT_MAP.put("t_customs_decl", "getCustomDeclares");
		// PRINT_MAP.put("t_export_info", "getExportInfos");
		PRINT_MAP.put("t_company_info", "companyInfoService");// 已经配置了:单位基础情况表
		PRINT_MAP.put("t_company_info_RBI", "companyInfoService");// 已经配置了:单位基础情况表
		// PRINT_MAP.put("t_company_openinfo", "getCompanyOpenInfos");
		// PRINT_MAP.put("t_invcountrycode_info", "getInvcountrycodeInfos");
	}

	public PrintAction(){
	}

	// DFHL:
	private String convertTableName(String tableId){
		// 申报3个
		if("t_decl_income".equals(tableId)){
			return "t_base_income";
		}
		if("t_decl_remit".equals(tableId)){
			return "t_base_remit";
		}
		if("t_decl_payment".equals(tableId)){
			return "t_base_payment";
		}
		// 核销4个
		if("t_fini_remit".equals(tableId)){
			return "t_base_remit";
		}
		if("t_fini_payment".equals(tableId)){
			// if("1.2".equals(this.interfaceVer)){
			// return "t_fini_payment";
			// }
			return "t_base_payment";
		}
		if("t_fini_dom_remit".equals(tableId)){
			return "t_base_dom_remit";
		}
		if("t_fini_dom_pay".equals(tableId)){
			return "t_base_dom_pay";
		}
		// 核销-需判断对应单据是否报送
		if("t_fini_export".equals(tableId)
				|| "t_fini_dom_export".equals(tableId)){
			if("1.2".equals(this.interfaceVer)){
				if("t_fini_dom_export".equals(tableId)){
					return "t_base_export";
				}
			}
			RptData rptData = new RptData();
			rptData.setTableId(tableId);
			rptData.setBusinessId(businessid);
			List list = dataDealService.findRptDataReduce(rptData);
			boolean bSendCommit = false;
			if(list != null && list.size() > 0){
				rptData = (RptData) list.get(0);
				// 核销信息已报送
				if(String.valueOf(DataUtil.YBS_STATUS_NUM).equals(
						rptData.getDataStatus())){
					bSendCommit = true;
				}
			}
			if(bSendCommit){
				if("t_fini_dom_export".equals(tableId)){
					// 出口收汇核销专用联（境内收入）
					return "t_base_export";
				}
			}else{
				if("t_fini_export".equals(tableId)){
					// 出口收汇核销专用联信息申报表（境外收入）
					return "t_fini_export_shenbao_out";
				}
			}
		}
		return tableId;
	}

	// DFHL:
	public String printReport(){
		try{
			URL uri = this.getClass().getResource("/config/config.properties");
			String path = uri.getFile();
			if(path.startsWith("/")){
				path = path.substring(1);
			}
			path = path.replaceAll("%20", " ");
			printType = PropertiesUtil.readProperties(path, "printType").trim();
			if(printType == null || "".equals(printType)){
				printType = "default";
			}
			userInterfaceConfigService = (UserInterfaceConfigService) SpringContextUtil
					.getBean("userInterfaceConfigService");
			configMap = userInterfaceConfigService.initConfigParameters();
			if(configMap != null){
				String printType = (String) configMap.get("printType");
				if(StringUtils.isNotEmpty(printType)){
					this.printType = printType;
				}
				String interfaceVer = (String) configMap
						.get("config.interface.ver");
				if(StringUtils.isNotEmpty(interfaceVer)){
					this.interfaceVer = interfaceVer;
					if("1.2".equals(this.interfaceVer)){
						this.printType += "12";
					}
				}
			}
			String searchTable = convertTableName(tableId);
			if("zzz".equals(tableId)){
				basePrintService = (BasePrintService) SpringContextUtil
						.getBean("financeExportService");
			}else{
				// if (StringUtil.contains(tableId,
				// "t_fini_export_shenbao_out")) {
				if(StringUtil
						.contains(searchTable, "t_fini_export_shenbao_out")){
					searchTable = "t_fini_export";
				}
				basePrintService = (BasePrintService) SpringContextUtil
						.getBean((String) PRINT_MAP.get(searchTable));
			}
			map = basePrintService.generator(businessid, searchTable,
					this.interfaceVer);
			// if(printType.equals("1")){
			// return "success2";
			// }
			if(this.printType.toUpperCase().indexOf("RBI") >= 0){
				User user = this.getCurrentUser();
				if(user != null){
					map.put("开户行名称", user.getOrgFullName());
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public String getTableId(){
		return tableId;
	}

	public String getConvertTableId(){
		return convertTableName(tableId);
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public Map getMap(){
		return map;
	}

	public String getPrintType(){
		return printType;
	}

	public void setPrintType(String printType){
		this.printType = printType;
	}

	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}
}