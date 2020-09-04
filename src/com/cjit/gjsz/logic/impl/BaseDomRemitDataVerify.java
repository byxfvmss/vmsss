/**
 * 境内汇款申请书—基础信息 t_base_dom_remit
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.AddRunBank;
import com.cjit.gjsz.logic.model.BaseDomRemit;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class BaseDomRemitDataVerify extends BaseDataVerify implements
		DataVerify{

	public static final String METHOD_VERIFY = "T,D,M";

	// private VerifyConfig verifyConfig;
	public BaseDomRemitDataVerify(){
	}

	public BaseDomRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				BaseDomRemit baseDomRemit = (BaseDomRemit) verifylList.get(i);
				if(!verifyActiontype(baseDomRemit.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(baseDomRemit.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(baseDomRemit.getActiontype(), baseDomRemit
						.getRptno())){
					map
							.put("RPTNO",
									"当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n");
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(baseDomRemit.getActiontype(), baseDomRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(baseDomRemit.getActiontype(), baseDomRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCustype(baseDomRemit.getCustype(), CUSTYPE_VERIFY)){
					String type = getKey(baseDomRemit.getCustype(), CUSTYPE2);
					map.put("CUSTYPE", "[汇款人类型] [" + type + "] 无效.\n");
				}
				if(!verifyIdcode(baseDomRemit.getIdcode(), baseDomRemit
						.getCustype())){
					String type = getKey(baseDomRemit.getCustype(), CUSTYPE2);
					// DFHL: 个人身份证件号码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型为C时必须为空的校验
					if("C".equals(baseDomRemit.getCustype())){
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 必须为空\n");
					}else{
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 不能为空\n");
					}
					// DFHL: 个人身份证件号码校验 end
				}
				if(!verifyCustcode(baseDomRemit.getCustcod(), baseDomRemit
						.getCustype())){
					String type = getKey(baseDomRemit.getCustype(), CUSTYPE2);
					// DFHL: 组织机构代码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型不为C时必须为空的校验
					if("C".equals(baseDomRemit.getCustype())){
						map
								.put(
										"CUSTCOD",
										"当 [汇款人类型] 为 ["
												+ type
												+ "] 时, [组织机构代码] 不能为空；并且必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。\n");
					}else{
						map.put("CUSTCOD", "当 [汇款人类型] 为 [" + type
								+ "] 时, [组织机构代码] 必须为空.\n");
					}
					// DFHL: 组织机构代码校验 end
				}
				if(!verifyCustnm(baseDomRemit.getCustnm())){
					map.put("CUSTNM", "[汇款人名称] 不能为空.\n");
				}
				if(!verifyOppuser(baseDomRemit.getOppuser(), "t_base_dom_remit")){
					map.put("OPPUSER", "[收款人名称] 不能为空.\n");
				}
				if(!verifyTxccy(baseDomRemit.getTxccy(), CURRENCY)){
					map.put("TXCCY", "[汇款币种] 不能为空且必须在币种代码表里存在.\n");
				}
				AddRunBank addRunBank = new AddRunBank();
				addRunBank.setTableId("t_base_dom_remit");
				addRunBank.setBusinessid(baseDomRemit.getBusinessid());
				if(verifyTxamt(baseDomRemit.getTxamt())){
					if(!verifySum(baseDomRemit.getOthamt(), baseDomRemit
							.getOthacc(), baseDomRemit.getLcyamt(),
							baseDomRemit.getFcyamt(), baseDomRemit.getTxamt(),
							addRunBank)){
						map.put("TXAMT", "[付款金额] 必须大于0并且无小数位。[购汇金额 "
								+ StringUtil.cleanBigInteger(baseDomRemit
										.getLcyamt())
								+ "]、[现汇金额 "
								+ StringUtil.cleanBigInteger(baseDomRemit
										.getFcyamt())
								+ "]、[其它金额 "
								+ StringUtil.cleanBigInteger(baseDomRemit
										.getOthamt())
								+ "] 之和应该等于 [汇款金额 "
								+ StringUtil.cleanBigInteger(baseDomRemit
										.getTxamt()) + "]。\n");
					}
				}else{
					map
							.put("TXAMT",
									"[汇款金额]  必须大于0并且无小数位。[购汇金额]、[现汇金额]、[其它金额] 之和应该等于 [汇款金额]。\n");
				}
				if(!verifyExrate(baseDomRemit.getExrate(), baseDomRemit
						.getLcyamt())){
					map.put("EXRATE", "当 [购汇金额] 大于0时必填 [购汇汇率]，否则不应该填写.\n");
				}else if(!verifyExrate1(baseDomRemit.getExrate())){
					map.put("EXRATE", " [购汇汇率]必须大于0\n");
				}
				if(!verifyLcyamt(baseDomRemit.getLcyamt(), baseDomRemit
						.getLcyacc())){
					map
							.put(
									"LCYAMT",
									"[购汇金额] 可以为空，但不能小于0。若 [人民币帐号/银行卡号] 不为空则对应 [购汇金额] 必须>0；若 [购汇金额] >0，则对应 [人民币帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyLcyacc(baseDomRemit.getLcyamt(), baseDomRemit
						.getExrate(), baseDomRemit.getLcyacc())){
					map.put("LCYACC", "[购汇金额]、[购汇汇率]、[购汇帐号] 三个或同时空或同时有值。\n");
				}
				if(!verifyFcyamt(baseDomRemit.getFcyamt(), baseDomRemit
						.getFcyacc())){
					map
							.put(
									"FCYAMT",
									"[现汇金额] 可以为空，但不能小于0。若 [外汇帐号/银行卡号] 不为空则对应 [现汇金额] 必须>0；若金额>0，则对应 [外汇帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyFcyacc(baseDomRemit.getFcyamt(), baseDomRemit
						.getFcyacc())){
					map
							.put("FCYACC",
									"如果有 [现汇金额]，则 [外汇帐号/银行卡号] 不能为空。[现汇金额]、[现汇帐号] 或同时空，或同时有值。\n");
				}
				if(!verifyOthamt(baseDomRemit.getOthamt(), baseDomRemit
						.getOthacc(), baseDomRemit.getLcyamt(), baseDomRemit
						.getFcyamt(), baseDomRemit.getTxamt())){
					map
							.put(
									"OTHAMT",
									"[其它金额] 可以为空，但不能小于0。若 [其它帐号/银行卡号] 不为空则对应 [其它金额] 必须>0；若 [其它金额] >0，则对应 [其它帐号/银行卡号]不能为空。[购汇金额], [现汇金额], [其它金额] 至少输入一项。\n");
				}
				if(!verifyOthacc(baseDomRemit.getOthamt(), baseDomRemit
						.getOthacc())){
					map
							.put("OTHACC",
									"如果有 [其他金额]，则该字段不能为空，[其他金额] 为0，则该字段不应该填写，[其它金额]、[其它帐号/银行卡号] 或同时空，或同时有值。\n");
				}
				if(!verifyMethod(baseDomRemit.getMethod(), METHOD_VERIFY)){
					map.put("METHOD", "[结算方式] 不能为空且必须在结算方式代码表里存在。\n");
				}
				if(!verifyBuscode(baseDomRemit.getBuscode())){
					map.put("BUSCODE", "[银行业务编号] 不能为空。\n");
				}
				if(!verifyTradeDate(baseDomRemit.getTradedate())){
					map.put("TRADEDATE", "交易日期不能晚于当前日期\n");
				}
				if("1.2".equals(this.interfaceVer)){
					if(StringUtil.isEmpty(baseDomRemit.getOppacc())){
						map.put("OPPACC", "[收款人帐号] 不能为空。\n");
					}
				}else{
					// 无此属性
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 覆盖父类，特殊需求
	 */
	public boolean verifySum(BigInteger othamt, String othacc,
			BigInteger lcyamt, BigInteger fcyamt, BigInteger txamt){
		lcyamt = (lcyamt == null) ? BigInteger.valueOf(0) : lcyamt;
		fcyamt = (fcyamt == null) ? BigInteger.valueOf(0) : fcyamt;
		othamt = (othamt == null) ? BigInteger.valueOf(0) : othamt;
		txamt = (txamt == null) ? BigInteger.valueOf(0) : txamt;
		if(lcyamt != null && fcyamt != null && othamt != null && txamt != null){
			BigInteger tmp = othamt.add(lcyamt).add(fcyamt);
			if(txamt.compareTo(tmp) == 0){
				return true;
			}
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
