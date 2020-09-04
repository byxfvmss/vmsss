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
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.FinanceDomRemit;
import com.cjit.gjsz.logic.model.FinanceRemit;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class CustomDeclareDataVerify extends FinanceDataVerify implements
		DataVerify{

	public final String parentTableId1 = "t_fini_remit"; // 境外汇款申请书
	public final String parentTableId2 = "t_fini_dom_remit"; // 境内汇款申请书

	// private VerifyConfig verifyConfig;
	public CustomDeclareDataVerify(){
	}

	public CustomDeclareDataVerify(List dictionarys, List verifylList,
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
				CustomDeclare customDeclare = (CustomDeclare) verifylList
						.get(i);
				FinanceRemit financeRemit = (FinanceRemit) service
						.getDataVerifyModel(parentTableId1, customDeclare
								.getBusinessid());
				// DFHL: 报关金额小于等于汇款金额的校验 start
				// BaseEntity baseEntity = (BaseRemit)
				// service.getDataVerifyModel(
				// "t_base_remit", customDeclare.getBusinessid());
				// //境外
				// if(baseEntity != null){
				// if(baseEntity.getTxamt().compareTo(customDeclare.getCustamt())
				// > 0){
				// map.put("CUSTAMT", "[报关金额] 必须大于等于基础信息中的[汇款金额:" +
				// baseEntity.getTxamt() + "]");
				// }
				// }else{//境内
				// baseEntity = (BaseDomRemit) service.getDataVerifyModel(
				// "t_base_dom_remit", customDeclare.getBusinessid());
				// if(baseEntity != null){
				// if(baseEntity.getTxamt().compareTo(customDeclare.getCustamt())
				// > 0){
				// map.put("CUSTAMT", "[报关金额] 必须大于等于基础信息中的[汇款金额:" +
				// baseEntity.getTxamt() + "]");
				// }
				// }
				// }
				// DFHL: 报关金额小于等于汇款金额的校验 end
				if(financeRemit != null){
					customDeclare.setFinanceRemit(financeRemit);
				}else{
					FinanceDomRemit financeDomRemit = (FinanceDomRemit) service
							.getDataVerifyModel(parentTableId2, customDeclare
									.getBusinessid());
					customDeclare.setFinanceDomRemit(financeDomRemit);
				}
				if(customDeclare.getFinanceDomRemit() != null){
					if(verifyCustomn(customDeclare.getCustomn(), customDeclare
							.getCustccy(), customDeclare.getCustamt(),
							customDeclare.getOffamt(), customDeclare
									.getFinanceDomRemit().getPayattr())){
						// 对同一笔境外或境内汇款，报关单号不允许重复。
						Long size = service2.subKeySize("t_customs_decl",
								"CUSTOMN", customDeclare.getCustomn(),
								"BUSINESSID", customDeclare.getBusinessid());
						if(size != null && size.longValue() > 1){
							map
									.put("CUSTOMN",
											"报关单不允许重复。付款方式为货到付款必须输入当输入报关单，则以下各项报关单信息都必须输入。\n");
						}
					}
				}else{
					if(verifyCustomn(customDeclare.getCustomn(), customDeclare
							.getCustccy(), customDeclare.getCustamt(),
							customDeclare.getOffamt(), customDeclare
									.getFinanceRemit().getPayattr())){
						// 对t_company_openinfo需要判断customid
						// DFHL:系统BUG 增加BUSINESSID判断开始
						Long size = service2.subKeySize("t_customs_decl",
								"CUSTOMN", customDeclare.getCustomn(),
								"BUSINESSID", customDeclare.getBusinessid());
						// DFHL:系统BUG 增加BUSINESSID判断结束
						if(size != null && size.longValue() > 1){
							map
									.put("CUSTOMN",
											"报关单不允许重复。付款方式为货到付款必须输入当输入报关单，则以下各项报关单信息都必须输入。\n");
						}
					}
				}
				if(customDeclare.getCustomn() == null){
					map.put("CUSTOMN", "报关单号 [] 长度必须为18位。\n");
				}else if(customDeclare.getCustomn().length() != 18
						&& customDeclare.getCustomn().length() != 9){
					// 临时允许报关单号9位和18位共存
					map.put("CUSTOMN", "报关单号 [" + customDeclare.getCustomn()
							+ "] 长度不正确。\n");
				}
				// else if (customDeclare.getCustomn().length() != 18) {
				// map.put("CUSTOMN", "报关单号 [" + customDeclare.getCustomn()
				// + "] 长度不正确，必须为18位。\n");
				// }
				if(!verifyCustccy(customDeclare.getCustccy(), CURRENCY)){
					String type = getKey(customDeclare.getCustccy(), CURRENCY);
					map.put("CUSTCCY", "币种 [" + type + "] 在字典表中不存在。\n");
				}
				if(!verifyCustamt(customDeclare.getCustamt(), customDeclare
						.getCustccy())){
					map.put("CUSTAMT",
							"必须大于0。若币种不为空则对应金额必须>0；若金额>0，则对应币种不能为空。\n");
				}
				if(!verifyOffamt(customDeclare.getOffamt(), customDeclare
						.getCustamt())){
					map.put("OFFAMT", "系统校验本次核注金额应小于等于 [报关金额]。\n");
				}
				// if (!verifyCrtuser(customDeclare.getCrtuser())) {
				// map.put("CRTUSER", "[填报人] 不能为空。\n");
				// }
				//
				// if (!verifyInptelc(customDeclare.getInptelc())) {
				// map.put("INPTELC", "[填报人电话] 不能为空。\n");
				// }
				//
				// if (!verifyRptdate(customDeclare.getRptdate())) {
				// map.put("RPTDATE", "[申报日期] 不能为空，按申报主体真实申报日期填写。\n");
				// }
				if(!verifyByFinanceDomRemitPayType(customDeclare)){
					map.put("CUSTOMN", "[付款类型]为P-货到付款时，报关单信息需全部填写。\n");
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

	/**
	 * 当境内汇款申请书核销信息的"付款类型"字段值为"P-货到付款"时，报关单子表中四个字段都必须输入
	 * @param customDeclare
	 * @return boolean
	 */
	public boolean verifyByFinanceDomRemitPayType(CustomDeclare customDeclare){
		boolean bReturn = true;
		if(customDeclare != null){
			FinanceDomRemit financeDomRemit = customDeclare
					.getFinanceDomRemit();
			if(financeDomRemit != null
					&& "P".equals(financeDomRemit.getPaytype())){
				if(StringUtil.isEmpty(customDeclare.getCustomn())
						|| StringUtil.isEmpty(customDeclare.getCustccy())
						|| (customDeclare.getCustamt() == null || customDeclare
								.getCustamt().signum() <= 0)
						|| (customDeclare.getOffamt() == null || customDeclare
								.getOffamt().signum() <= 0)){
					bReturn = false;
				}
			}
		}
		return bReturn;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
