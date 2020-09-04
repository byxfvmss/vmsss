/**
 * 涉外收入申报单--基础信息 t_base_income
 */
package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseIncome;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class BaseIncomeDataVerify extends BaseDataVerify implements DataVerify{

	// private VerifyConfig verifyConfig;
	public BaseIncomeDataVerify(){
	}

	public BaseIncomeDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				BaseIncome baseIncome = (BaseIncome) verifylList.get(i);
				if(!verifyActiontype(baseIncome.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(baseIncome.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(baseIncome.getActiontype(), baseIncome
						.getRptno())){
					map
							.put("RPTNO",
									"当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n");
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(baseIncome.getActiontype(), baseIncome
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(baseIncome.getActiontype(), baseIncome
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCustype(baseIncome.getCustype(), CUSTYPE_VERIFY)){
					String type = getKey(baseIncome.getCustype(), CUSTYPE);
					map.put("CUSTYPE", "[收款人类型] [" + type + "] 无效.\n");
				}
				if(!verifyIdcode(baseIncome.getIdcode(), baseIncome
						.getCustype())){
					String type = getKey(baseIncome.getCustype(), CUSTYPE);
					// DFHL: 个人身份证件号码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型为C时必须为空的校验
					if(StringUtil.isEmpty(baseIncome.getIdcode())){
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 不能为空\n");
					}else{
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 必须为空\n");
					}
					// DFHL: 个人身份证件号码校验 end
				}
				if(!verifyCustcode(baseIncome.getCustcod(), baseIncome
						.getCustype())){
					String type = getKey(baseIncome.getCustype(), CUSTYPE);
					// DFHL: 组织机构代码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型不为C时必须为空的校验
					if("C".equals(baseIncome.getCustype())){
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
				if(!verifyCustnm(baseIncome.getCustnm())){
					map.put("CUSTNM", "[收款人名称] 不能为空.\n");
				}
				if(!verifyOppuser(baseIncome.getOppuser(), "t_base_income")){
					map
							.put(
									"OPPUSER",
									"[付款人名称] 不能为空。申报主体收到来自境外的款项，境内收款银行应当在基础信息中的对方付款人名称前添加“（JW）”字样；如果为境内居民收到来自境内非居民的款项，境内收款银行应当在基础信息中的对方付款人名称前添加“（JN）”字样。\n");
				}
				if(!verifyTxccy(baseIncome.getTxccy(), CURRENCY)){
					map.put("TXCCY", "[收入款币种] 不能为空且必须在币种代码表里存在。\n");
				}
				if(verifyTxamt(baseIncome.getTxamt())){
					if(!verifySum(baseIncome.getOthamt(), baseIncome
							.getOthacc(), baseIncome.getLcyamt(), baseIncome
							.getFcyamt(), baseIncome.getTxamt())){
						map.put("TXAMT", "[收入款金额] 必须大于0并且无小数位。[结汇金额 "
								+ StringUtil.cleanBigInteger(baseIncome
										.getLcyamt())
								+ "]、[现汇金额 "
								+ StringUtil.cleanBigInteger(baseIncome
										.getFcyamt())
								+ "]、[其它金额 "
								+ StringUtil.cleanBigInteger(baseIncome
										.getOthamt())
								+ "] 之和不能大于 [收入款金额 "
								+ StringUtil.cleanBigInteger(baseIncome
										.getTxamt()) + "]。\n");
					}
				}else{
					map
							.put("TXAMT",
									"[收入款金额] 必须大于0并且无小数位。[结汇金额]、[现汇金额]、[其它金额] 之和不能大于 [收入款金额]。\n");
				}
				if(!verifyExrate(baseIncome.getExrate(), baseIncome.getLcyamt())){
					map.put("EXRATE", "当 [结汇金额] 大于0时必填 [结汇汇率]，否则不应该填写.\n");
				}else if(!verifyExrate1(baseIncome.getExrate())){
					map.put("EXRATE", " [购汇汇率]必须大于0\n");
				}
				if(!verifyLcyamt(baseIncome.getLcyamt(), baseIncome.getLcyacc())){
					map
							.put(
									"LCYAMT",
									"[结汇金额] 可以为空，但不能小于0。若 [人民币帐号/银行卡号] 不为空则对应 [结汇金额] 必须>0；若 [结汇金额] >0，则对应 [人民币帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyLcyacc(baseIncome.getLcyamt(),
						baseIncome.getExrate(), baseIncome.getLcyacc())){
					map.put("LCYACC", "[结汇金额]、[结汇汇率]、[结汇帐号] 三个或同时空或同时有值。\n");
				}
				if(!verifyFcyamt(baseIncome.getFcyamt(), baseIncome.getFcyacc())){
					map
							.put(
									"FCYAMT",
									"[现汇金额] 可以为空，但不能小于0。若 [外汇帐号/银行卡号] 不为空则对应 [现汇金额] 必须>0；若金额>0，则对应 [外汇帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyFcyacc(baseIncome.getFcyamt(), baseIncome.getFcyacc())){
					map
							.put("FCYACC",
									"如果有 [现汇金额]，则 [外汇帐号/银行卡号] 不能为空。[现汇金额]、[现汇帐号] 或同时空，或同时有值。\n");
				}
				if(!verifyOthamt(baseIncome.getOthamt(),
						baseIncome.getOthacc(), baseIncome.getLcyamt(),
						baseIncome.getFcyamt(), baseIncome.getTxamt())){
					map
							.put(
									"OTHAMT",
									"[其它金额] 可以为空，但不能小于0。若 [其它帐号/银行卡号] 不为空则对应 [其它金额] 必须>0；若 [其它金额] >0，则对应 [其它帐号/银行卡号]不能为空。[结汇金额], [现汇金额], [其它金额] 至少输入一项。\n");
				}
				if(!verifyOthacc(baseIncome.getOthamt(), baseIncome.getOthacc())){
					map
							.put("OTHACC",
									"如果有 [其他金额]，则该字段不能为空，[其他金额] 为0，则该字段不应该填写，[其它金额]、[其它帐号/银行卡号] 或同时空，或同时有值。\n");
				}
				if(!verifyMethod(baseIncome.getMethod(), METHOD_VERIFY)){
					map.put("METHOD", "[结算方式] 不能为空且必须在结算方式代码表里存在。\n");
				}
				if(!verifyBuscode(baseIncome.getBuscode())){
					map.put("BUSCODE", "[银行业务编号] 不能为空。\n");
				}
				// if (!verifyInchargeccy(baseIncome.getInchargeccy(),
				// CURRENCY)) {
				// map.put("INCHARGECCY", "[国内银行扣费币种] 不能为空且必须在币种代码表里存在。\n");
				// }
				if(!verifyInchargeamt(baseIncome.getInchargeamt(), baseIncome
						.getInchargeccy())){
					map
							.put(
									"INCHARGEAMT",
									"若输入，则输入的 [国内银行扣费金额] 必须大于0，且没有小数位。[国内扣费币种]、[国内银行扣费金额] 必须同时输入。若币种不为空则对应 [国内银行扣费金额] 必须>0；若 [国内银行扣费金额]>0，则对应币种 [国内扣费币种] 不能为空。\n");
				}
				// if (!verifyOutchargeccy(baseIncome.getOutchargeccy(),
				// CURRENCY)) {
				// map.put("OUTCHARGECCY", "[国外银行扣费币种] 不能为空且必须在币种代码表里存在。\n");
				// }
				if(!verifyOutchargeamt(baseIncome.getOutchargeamt(), baseIncome
						.getOutchargeccy())){
					map
							.put(
									"OUTCHARGEAMT",
									"若输入，则输入的 [国外银行扣费金额] 必须大于0，且没有小数位。[国外扣费币种]、[国外银行扣费金额] 必须同时输入。若币种不为空则对应 [国外银行扣费金额] 必须>0；若金额>0，则对应币种 [国外扣费币种] 不能为空。\n");
				}
				if(!verifyTradeDate(baseIncome.getTradedate())){
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
