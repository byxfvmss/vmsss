/**
 * 对外付款/承兑通知书－基础信息 t_base_payment
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
import com.cjit.gjsz.logic.model.BasePayment;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class BasePaymentDataVerify extends BaseDataVerify implements DataVerify{

	public static final String METHOD_VERIFY = "L,G,C,O";

	// private VerifyConfig verifyConfig;
	public BasePaymentDataVerify(){
	}

	public BasePaymentDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				BasePayment basePayment = (BasePayment) verifylList.get(i);
				if(!verifyActiontype(basePayment.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(basePayment.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(basePayment.getActiontype(), basePayment
						.getRptno())){
					map
							.put("RPTNO",
									"当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n");
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(basePayment.getActiontype(), basePayment
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(basePayment.getActiontype(), basePayment
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCustype(basePayment.getCustype(), CUSTYPE_VERIFY)){
					String type = getKey(basePayment.getCustype(), CUSTYPE3);
					map.put("CUSTYPE", "[付款人类型] [" + type + "] 无效.\n");
				}
				if(!verifyIdcode(basePayment.getIdcode(), basePayment
						.getCustype())){
					String type = getKey(basePayment.getCustype(), CUSTYPE3);
					// DFHL: 个人身份证件号码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型为C时必须为空的校验
					if(StringUtil.isEmpty(basePayment.getIdcode())){
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 不能为空\n");
					}else{
						map.put("IDCODE", "当 [收款人类型] 为 [" + type
								+ "]  时, [个人身份证件号码] 必须为空\n");
					}
					// DFHL: 个人身份证件号码校验 end
				}
				if(!verifyCustcode(basePayment.getCustcod(), basePayment
						.getCustype())){
					String type = getKey(basePayment.getCustype(), CUSTYPE3);
					// DFHL: 组织机构代码校验 start
					// modified by yuanshihong 20090612 增加了对收款人类型不为C时必须为空的校验
					if("C".equals(basePayment.getCustype())){
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
				if(!verifyCustnm(basePayment.getCustnm())){
					map.put("CUSTNM", "[付款人名称] 不能为空.\n");
				}
				if(!verifyOppuser(basePayment.getOppuser(), "t_base_payment")){
					map
							.put(
									"OPPUSER",
									"[收款人名称] 不能为空。境内申报主体对境外支付款项，境内付款银行应当在基础信息中的对方收款人名称前添加“（JW）”字样；如果为境内居民向境内非居民付款，境内付款银行应当在基础信息中的对方收款人名称前添加“（JN）”字样。\n");
				}
				if(!verifyTxccy(basePayment.getTxccy(), CURRENCY)){
					map.put("TXCCY", "[付款币种] 不能为空且必须在币种代码表里存在.\n");
				}
				AddRunBank addRunBank = new AddRunBank();
				addRunBank.setTableId("t_base_payment");
				addRunBank.setBusinessid(basePayment.getBusinessid());
				if(verifyTxamt(basePayment.getTxamt())){
					if(!verifySum(basePayment.getOthamt(), basePayment
							.getOthacc(), basePayment.getLcyamt(), basePayment
							.getFcyamt(), basePayment.getTxamt(), addRunBank)){
						map.put("TXAMT", "[付款金额] 必须大于0并且无小数位。[购汇金额 "
								+ StringUtil.cleanBigInteger(basePayment
										.getLcyamt())
								+ "]、[现汇金额 "
								+ StringUtil.cleanBigInteger(basePayment
										.getFcyamt())
								+ "]、[其它金额 "
								+ StringUtil.cleanBigInteger(basePayment
										.getOthamt())
								+ "] 之和应该等于 [付款金额 "
								+ StringUtil.cleanBigInteger(basePayment
										.getTxamt()) + "]。\n");
					}
				}else{
					map
							.put("TXAMT",
									"[付款金额]  必须大于0并且无小数位。[购汇金额]、[现汇金额]、[其它金额] 之和应该等于 [付款金额]。\n");
				}
				if(!verifyExrate(basePayment.getExrate(), basePayment
						.getLcyamt())){
					map.put("EXRATE", "当 [购汇金额] 大于0时必填 [购汇汇率]，否则不应该填写.\n");
				}else if(!verifyExrate1(basePayment.getExrate())){
					map.put("EXRATE", " [购汇汇率]必须大于0\n");
				}
				if(!verifyLcyamt(basePayment.getLcyamt(), basePayment
						.getLcyacc())){
					map
							.put(
									"LCYAMT",
									"[购汇金额] 可以为空，但不能小于0。若 [人民币帐号/银行卡号] 不为空则对应 [购汇金额] 必须>0；若 [购汇金额] >0，则对应 [人民币帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyLcyacc(basePayment.getLcyamt(), basePayment
						.getExrate(), basePayment.getLcyacc())){
					map.put("LCYACC", "[购汇金额]、[购汇汇率]、[购汇帐号] 三个或同时空或同时有值。\n");
				}
				if(!verifyFcyamt(basePayment.getFcyamt(), basePayment
						.getFcyacc())){
					map
							.put(
									"FCYAMT",
									"[现汇金额] 可以为空，但不能小于0。若 [外汇帐号/银行卡号] 不为空则对应 [现汇金额] 必须>0；若金额>0，则对应 [外汇帐号/银行卡号] 不能为空。\n");
				}
				if(!verifyFcyacc(basePayment.getFcyamt(), basePayment
						.getFcyacc())){
					map
							.put("FCYACC",
									"如果有 [现汇金额]，则 [外汇帐号/银行卡号] 不能为空。[现汇金额]、[现汇帐号] 或同时空，或同时有值。\n");
				}
				if(!verifyOthamt(basePayment.getOthamt(), basePayment
						.getOthacc(), basePayment.getLcyamt(), basePayment
						.getFcyamt(), basePayment.getTxamt())){
					map
							.put(
									"OTHAMT",
									"[其它金额] 可以为空，但不能小于0。若 [其它帐号/银行卡号] 不为空则对应 [其它金额] 必须>0；若 [其它金额] >0，则对应 [其它帐号/银行卡号]不能为空。[购汇金额], [现汇金额], [其它金额] 至少输入一项。\n");
				}
				if(!verifyOthacc(basePayment.getOthamt(), basePayment
						.getOthacc())){
					map
							.put("OTHACC",
									"如果有 [其他金额]，则该字段不能为空，[其他金额] 为0，则该字段不应该填写，[其它金额]、[其它帐号/银行卡号] 或同时空，或同时有值。\n");
				}
				if(!verifyMethod(basePayment.getMethod(), METHOD_VERIFY)){
					map.put("METHOD", "[结算方式] 不能为空且必须在结算方式代码表里存在。\n");
				}
				if(!verifyBuscode(basePayment.getBuscode())){
					map.put("BUSCODE", "[银行业务编号] 不能为空。\n");
				}
				if(!verifyActuccy(basePayment.getActuccy(), CURRENCY)){
					map.put("ACTUCCY", "[实际付款币种] 不能为空且必须在币种代码表里存在.\n");
				}
				if(!verifyActuamt(basePayment.getActuamt())){
					map.put("ACTUAMT", "[实际付款金额] 不能为空且必须大于0.\n");
				}
				// if (!verifyOutchargeccy(basePayment.getOutchargeccy(),
				// CURRENCY)) {
				// map.put("OUTCHARGECCY", "[扣费币种] 不能为空且必须在币种代码表里存在。\n");
				// }
				if(!verifyOutchargeamt(basePayment.getOutchargeamt(),
						basePayment.getOutchargeccy())){
					map
							.put(
									"OUTCHARGEAMT",
									"若输入，则输入的 [扣费金额] 必须大于0，且没有小数位。[扣费币种]、[扣费金额] 必须同时输入。若币种不为空则对应 [扣费金额] 必须>0；若金额>0，则对应币种 [扣费币种] 不能为空。\n");
				}
				if(!verifyLcbgno(basePayment.getLcbgno(), basePayment
						.getIssdate(), basePayment.getTenor())){
					map.put("LCBGNO", "当有 [开证日期] 或 [期限] 时必输。");
				}
				if(!verfifyIssdate(basePayment.getLcbgno(), basePayment
						.getIssdate())){
					map.put("ISSDATE", "当 [信用证/保函编号] 输入时必须输入，否则不可输入。");
				}else{
					if(basePayment.getIssdate() != null
							&& !"".equals(basePayment.getIssdate())){
						if(basePayment.getIssdate().indexOf("-") > 0){
							map.put("ISSDATE", "[开证日期]格式不正确，应为yyyymmdd");
						}
					}
				}
				if(!verfifyTenor(basePayment.getLcbgno(), basePayment
						.getTenor())){
					map.put("TENOR", "仅当有 [信用证/保函编号] 时才可有值。");
				}
				if(!verifyTradeDate(basePayment.getTradedate())){
					map.put("TRADEDATE", "交易日期不能晚于当前日期\n");
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

	/** ************************************************************************** */
	/**
	 * 信用证/保函编号 当有开证日期或期限时必输。
	 * @param lcbgno 信用证/保函编号
	 * @param issdate 开证日期
	 * @param tenor 期限
	 */
	public boolean verifyLcbgno(String lcbgno, String issdate, Long tenor){
		if(StringUtil.isEmpty(lcbgno) && issdate == null && tenor == null){
			return true;
		}
		if(issdate != null || tenor != null){
			return lcbgno == null ? false : true;
		}
		return true;
	}

	/**
	 * 开证日期 当信用证/保函编号输入时必须输入，否则不可输入
	 * @param lcbgno 信用证/保函编号
	 * @param issdate 开证日期
	 * @return
	 */
	public boolean verfifyIssdate(String lcbgno, String issdate){
		if(StringUtil.isEmpty(lcbgno) && issdate == null){
			return true;
		}
		if(StringUtil.isNotEmpty(lcbgno) && issdate != null){
			return true;
		}
		return false;
	}

	/**
	 * 期限 仅当有信用证/保函编号时才可有值
	 * @param lcbgno 信用证/保函编号
	 * @param tenor 期限
	 * @return
	 */
	public boolean verfifyTenor(String lcbgno, Long tenor){
		if(StringUtil.isEmpty(lcbgno) && tenor != null){
			return false;
		}
		return true;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
