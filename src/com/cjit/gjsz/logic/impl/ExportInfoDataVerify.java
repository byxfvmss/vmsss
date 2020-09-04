/**
 * 
 * t_customs_decl
 */
package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.FinanceDomExport;
import com.cjit.gjsz.logic.model.FinanceExport;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class ExportInfoDataVerify extends FinanceDataVerify implements
		DataVerify{

	public final String parentTableId1 = "t_fini_export"; // 出口收汇核销专用联（境外收入）
	public final String parentTableId2 = "t_fini_dom_export"; // 出口收汇核销专用联（境内收入）

	// private VerifyConfig verifyConfig;
	public ExportInfoDataVerify(){
	}

	public ExportInfoDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		UserInterfaceConfigService service2 = (UserInterfaceConfigService) SpringContextUtil
				.getBean("userInterfaceConfigService");
		SearchService service = (SearchService) SpringContextUtil
				.getBean("searchService");
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				ExportInfo exportInfo = (ExportInfo) verifylList.get(i);
				FinanceExport financeExport = (FinanceExport) service
						.getDataVerifyModel(parentTableId1, exportInfo
								.getBusinessid());
				if(financeExport != null){
					exportInfo.setFinanceExport(financeExport);
				}else{
					FinanceDomExport financeDomExport = (FinanceDomExport) service
							.getDataVerifyModel(parentTableId2, exportInfo
									.getBusinessid());
					exportInfo.setFinanceDomExport(financeDomExport);
				}
				if(exportInfo.getRefno() == null){
					map.put("REFNO", "出口收汇核销单号码 [] 长度必须为9位。\n");
				}else if(exportInfo.getRefno().trim().length() != 9){
					map.put("REFNO", "出口收汇核销单号码 [" + exportInfo.getRefno()
							+ "] 长度不正确，必须为9位。\n");
				}else if(StringUtil.isChiness(exportInfo.getRefno())){
					map.put("REFNO", "[出口收汇核销单号码] 不能含中文.\n");
				}
				Long size = service2.subKeySize("t_export_info", "REFNO",
						exportInfo.getRefno(), "BUSINESSID", exportInfo
								.getBusinessid());
				if(size != null && size.longValue() > 1){
					map.put("REFNO", "出口收汇核销单不允许重复。\n");
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 申报日期 必填项，按申报主体真实申报日期填写。
	 * @param rptdate 报日期申
	 * @return
	 */
	public boolean verifyRptdate(String rptdate){
		if(StringUtil.isNotEmpty(rptdate) && rptdate.length() == 8){
			return true;
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
