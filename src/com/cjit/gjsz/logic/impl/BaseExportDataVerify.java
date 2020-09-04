/**
 * 出口收汇核销专用联（境内收入）—基础信息 t_base_export
 */
package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseExport;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class BaseExportDataVerify extends BaseDataVerify implements DataVerify{

	// private VerifyConfig verifyConfig;
	public BaseExportDataVerify(){
	}

	public BaseExportDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				BaseExport baseExport = (BaseExport) verifylList.get(i);
				if(!verifyActiontype(baseExport.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(baseExport.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(baseExport.getActiontype(), baseExport
						.getRptno())){
					map
							.put("RPTNO",
									"当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n");
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(baseExport.getActiontype(), baseExport
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(baseExport.getActiontype(), baseExport
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCustype(baseExport.getCustype(), CUSTYPE_VERIFY)){
					String type = getKey(baseExport.getCustype(), CUSTYPE1);
					map.put("CUSTYPE", "[收款人类型] [" + type + "] 无效.\n");
				}
				if(!verifyIdcode(baseExport.getIdcode(), baseExport
						.getCustype())){
					String type = getKey(baseExport.getCustype(), CUSTYPE1);
					// DFHL: 个人身份证件号码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型为C时必须为空的校验
					if(StringUtil.isEmpty(baseExport.getIdcode())){
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 不能为空\n");
					}else{
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 必须为空\n");
					}
					// DFHL: 个人身份证件号码校验 end
				}
				if(!verifyCustcode(baseExport.getCustcod(), baseExport
						.getCustype())){
					String type = getKey(baseExport.getCustype(), CUSTYPE1);
					// DFHL: 组织机构代码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型不为C时必须为空的校验
					if("C".equals(baseExport.getCustype())){
						map
								.put(
										"CUSTCOD",
										"当 [收款人类型] 为 ["
												+ type
												+ "] 时, [组织机构代码] 不能为空；并且必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。\n");
					}else{
						map.put("CUSTCOD", "当 [收款人类型] 为 [" + type
								+ "] 时, [组织机构代码] 必须为空.\n");
					}
					// DFHL: 组织机构代码校验 end
				}
				if(!verifyCustnm(baseExport.getCustnm())){
					map.put("CUSTNM", "[收款人名称] 不能为空.\n");
				}
				if(!verifyOppuser(baseExport.getOppuser(), "t_base_export")){
					map.put("OPPUSER", "[付款人名称] 不能为空.\n");
				}
				if(!verifyTxccy(baseExport.getTxccy(), CURRENCY)){
					map.put("TXCCY", "[收入款币种] 不能为空且必须在币种代码表里存在.\n");
				}
				if(verifyTxamt(baseExport.getTxamt())){
					if(!verifySum(baseExport.getOthamt(), baseExport
							.getOthacc(), baseExport.getLcyamt(), baseExport
							.getFcyamt(), baseExport.getTxamt())){
						map.put("TXAMT", "[收入款金额] 必须大于0并且无小数位。[结汇金额 "
								+ StringUtil.cleanBigInteger(baseExport
										.getLcyamt())
								+ "]、[现汇金额 "
								+ StringUtil.cleanBigInteger(baseExport
										.getFcyamt())
								+ "]、[其它金额 "
								+ StringUtil.cleanBigInteger(baseExport
										.getOthamt())
								+ "] 之和不能大于 [收入款金额 "
								+ StringUtil.cleanBigInteger(baseExport
										.getTxamt()) + "]。\n");
					}
				}else{
					map
							.put("TXAMT",
									"[收入款金额] 必须大于0并且无小数位。[结汇金额]、[现汇金额]、[其它金额] 之和不能大于 [收入款金额]。\n");
				}
				if(!verifyExrate(baseExport.getExrate(), baseExport.getLcyamt())){
					map.put("EXRATE", "当 [结汇金额] 大于0时必填 [结汇汇率]，否则不应该填写.\n");
				}else if(!verifyExrate1(baseExport.getExrate())){
					map.put("EXRATE", " [购汇汇率]必须大于0\n");
				}
				if(!verifyLcyamt(baseExport.getLcyamt(), baseExport.getLcyacc())){
					map
							.put(
									"LCYAMT",
									"[结汇金额] 可以为空，但不能小于0。若 [人民币帐号/银行卡号] 不为空则对应 [结汇金额] 必须>0；若 [结汇金额] >0，则对应 [人民币帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyLcyacc(baseExport.getLcyamt(),
						baseExport.getExrate(), baseExport.getLcyacc())){
					map.put("LCYACC", "[结汇金额]、[结汇汇率]、[结汇帐号] 三个或同时空或同时有值。\n");
				}
				if(!verifyFcyamt(baseExport.getFcyamt(), baseExport.getFcyacc())){
					map
							.put(
									"FCYAMT",
									"[现汇金额] 可以为空，但不能小于0。若 [外汇帐号/银行卡号] 不为空则对应 [现汇金额] 必须>0；若金额>0，则对应 [外汇帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyFcyacc(baseExport.getFcyamt(), baseExport.getFcyacc())){
					map
							.put("FCYACC",
									"如果有 [现汇金额]，则 [外汇帐号/银行卡号] 不能为空。[现汇金额]、[现汇帐号] 或同时空，或同时有值。\n");
				}
				if(!verifyOthamt(baseExport.getOthamt(),
						baseExport.getOthacc(), baseExport.getLcyamt(),
						baseExport.getFcyamt(), baseExport.getTxamt())){
					map
							.put(
									"OTHAMT",
									"[其它金额] 可以为空，但不能小于0。若 [其它帐号/银行卡号] 不为空则对应 [其它金额] 必须>0；若 [其它金额] >0，则对应 [其它帐号/银行卡号]不能为空。[结汇金额], [现汇金额], [其它金额] 至少输入一项。\n");
				}
				if(!verifyOthacc(baseExport.getOthamt(), baseExport.getOthacc())){
					map
							.put("OTHACC",
									"如果有 [其他金额]，则该字段不能为空，[其他金额] 为0，则该字段不应该填写，[其它金额]、[其它帐号/银行卡号] 或同时空，或同时有值。\n");
				}
				if(!verifyMethod(baseExport.getMethod(), METHOD_VERIFY)){
					map.put("METHOD", "[结算方式] 不能为空且必须在结算方式代码表里存在。\n");
				}
				if(!verifyBuscode(baseExport.getBuscode())){
					map.put("BUSCODE", "[银行业务编号] 不能为空。\n");
				}
				// if (!verifyInchargeccy(baseExport.getInchargeccy(),
				// CURRENCY)) {
				// map.put("INCHARGECCY", "[国内银行扣费币种] 不能为空且必须在币种代码表里存在。\n");
				// }
				if(!verifyInchargeamt(baseExport.getInchargeamt(), baseExport
						.getInchargeccy())){
					map
							.put(
									"INCHARGEAMT",
									"若输入，则输入的 [国内银行扣费金额] 必须大于0，且没有小数位。[国内扣费币种]、[国内银行扣费金额] 必须同时输入。若币种不为空则对应 [国内银行扣费金额] 必须>0；若 [国内银行扣费金额]>0，则对应币种 [国内扣费币种] 不能为空。\n");
				}
				if(!verifyTradeDate(baseExport.getTradedate())){
					map.put("TRADEDATE", "交易日期不能晚于当前日期\n");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
