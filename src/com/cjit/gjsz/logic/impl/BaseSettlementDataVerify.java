package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseSettlement;
import com.cjit.gjsz.logic.model.VerifyModel;

public class BaseSettlementDataVerify extends BaseDataVerify implements
		DataVerify{

	public BaseSettlementDataVerify(){
	}

	public BaseSettlementDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				BaseSettlement baseSettlement = (BaseSettlement) verifylList
						.get(i);
				if(!verifyActiontype(baseSettlement.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(baseSettlement.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(baseSettlement.getActiontype(), baseSettlement
						.getRptno())){
					map
							.put("RPTNO",
									"当[申报号码]为空时, [操作类型] 必需为 [新建]。否则当时 [申报号码] 为不为空时, [操作类型] 不允许为 [新建]\n");
				}
				if(!verifyAReasion(baseSettlement.getActiontype(),
						baseSettlement.getActiondesc())){
					map.put("ACTIONDESC", "当[操作类型]新增时,修改/删除原因必须为空.\n");
				}
				if(!verifyCustype(baseSettlement.getCustype(), CUSTYPE_VERIFY)){
					String type = getKey(baseSettlement.getCustype(), CUSTYPE);
					map.put("CUSTYPE", "[结汇申请人主体类型] [" + type + "] 无效.\n");
				}
				if(!verifyCustcode(baseSettlement.getCustcod(), baseSettlement
						.getCustype())){
					String type = getKey(baseSettlement.getCustype(), CUSTYPE);
					if("C".equals(baseSettlement.getCustype())){
						map
								.put(
										"CUSTCOD",
										"当 [结汇申请人主体类型] 为 ["
												+ type
												+ "] 时, [结汇申请人组织机构代码] 不能为空；并且必须符合技监局的机构代码编制规则。\n");
					}else{
						map.put("CUSTCOD", "当 [结汇申请人主体类型] 为 [" + type
								+ "] 时, [结汇申请人组织机构代码] 必须为空.\n");
					}
				}
				if(!verifyIdcode(baseSettlement.getIdcode(), baseSettlement
						.getCustype())){
					String type = getKey(baseSettlement.getCustype(), CUSTYPE);
					if(StringUtil.isEmpty(baseSettlement.getIdcode())){
						map.put("IDCODE", "当 [结汇申请人主体类型] 为 [" + type
								+ "] 时, [结汇申请人个人身份证件号码] 不能为空\n");
					}else{
						map.put("IDCODE", "当 [结汇申请人主体类型] 为 [" + type
								+ "] 时, [结汇申请人个人身份证件号码] 必须为空\n");
					}
				}
				if(!verifyCustnm(baseSettlement.getCustnm())){
					map
							.put(
									"CUSTNM",
									"[结汇申请人名称] 不能为空。对公项下指结汇人预留银行印鉴或国家质量监督检验检疫总局颁发的组织机构代码证或国家外汇管理局及其分支局签发的特殊机构代码赋码通知书上的名称；对私项下指个人身份证件上的名称.\n");
				}
				if(!verifyOppBank(baseSettlement.getLcyacc(), baseSettlement
						.getOppbank())){
					map.put("OPPBANK", " [人民币账户开户行]有人民币账户时必填\n");
				}
				if(!verifyExrate1(baseSettlement.getExrate())){
					map.put("EXRATE", " [汇率]必须大于0\n");
				}
				if(!verifyTradeDate(baseSettlement.getTradedate())){
					map.put("TRADEDATE", "交易日期不能晚于当前日期\n");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}

	public String getInterfaceVer(){
		return interfaceVer;
	}
}
