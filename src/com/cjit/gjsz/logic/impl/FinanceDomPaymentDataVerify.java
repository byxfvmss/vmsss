/**
 * 境内付款/承兑通知书－核销专用信息 t_fini_dom_pay
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseDomPayment;
import com.cjit.gjsz.logic.model.FinanceDomPayment;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinanceDomPaymentDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_dom_pay";
	public static final String PAYTYPE_VERIFY = "A,O";
	public static final String PAYATTR_VERIFY = "X,E,D,S,M,O";
	public static final String PAYATTR_VERIFY_12 = "A,X,E,D,M,O";
	private VerifyConfig verifyConfig;

	public FinanceDomPaymentDataVerify(){
	}

	public FinanceDomPaymentDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceDomPayment financeDomPayment = (FinanceDomPayment) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BaseDomPayment baseDomPayment = (BaseDomPayment) service
						.getDataVerifyModel(parentTableId, financeDomPayment
								.getBusinessid());
				financeDomPayment.setBaseDomPayment(baseDomPayment);
				if(!verifyActiontype(financeDomPayment.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financeDomPayment.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financeDomPayment.getActiontype(),
						financeDomPayment.getRptno())){
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 start
					/*
					 * map .put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时
					 * [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n" );
					 */
					map.put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]");
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 end
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(financeDomPayment.getActiontype(),
						financeDomPayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(financeDomPayment.getActiontype(),
						financeDomPayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCountry(financeDomPayment.getCountry(), COUNTRY)){
					String type = getKey(financeDomPayment.getCountry(),
							COUNTRY);
					map.put("COUNTRY", "[国家/地区代码] [" + type + "] 无效.\n");
				}
				if(!verifyIsref(financeDomPayment.getIsref(), ISREF_VERIFY)){
					String type = getKey(financeDomPayment.getIsref(), ISREF);
					map.put("ISREF", "[是否出口核销项下收汇] [" + type + "] 无效.\n");
				}
				if(!verifyPaytype(financeDomPayment.getPaytype(),
						PAYTYPE_VERIFY)){
					String type = getKey(financeDomPayment.getPaytype(),
							PAYTYPE);
					map.put("PAYTYPE", "[付款类型] [" + type + "] 无效.\n");
				}
				if(StringUtil.isNotEmpty(interfaceVer)){
					if(!verifyPayattr(financeDomPayment.getPayattr(),
							PAYATTR_VERIFY_12)){
						String type = getKey(financeDomPayment.getPayattr(),
								PAYATTR1);
						map.put("PAYATTR", "[付汇性质] [" + type + "] 无效.\n");
					}
				}else{
					if(!verifyPayattr(financeDomPayment.getPayattr(),
							PAYATTR_VERIFY)){
						String type = getKey(financeDomPayment.getPayattr(),
								PAYATTR1);
						map.put("PAYATTR", "[付汇性质] [" + type + "] 无效.\n");
					}
				}
				if(!verifyTxcode(financeDomPayment.getTxcode(), BOP_PAYOUT)){
					String type = getKey(financeDomPayment.getTxcode(),
							BOP_PAYOUT);
					map.put("TXCODE", "[交易编码1] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyTc1amt(financeDomPayment.getTc1amt())){
					map.put("TC1AMT", "[相应金额1] 不能为空。 \n");
				}
				if(!verifyTxcode2(financeDomPayment.getTxcode2(), BOP_PAYOUT,
						financeDomPayment.getTxcode(), financeDomPayment
								.getTc2amt())){
					map
							.put("TXCODE2",
									"[交易编码2] 必须在国际收支交易编码表中存在不能与 [交易编码1] 相同，没有输入交易编码时，相应金额及交易附言不应该填写。有交易金额2时必填。 \n");
				}
				if(!verifyTc2amt(financeDomPayment.getTc2amt(),
						financeDomPayment.getTxcode2(), financeDomPayment
								.getTc1amt(), financeDomPayment
								.getBaseDomPayment().getTxamt())){
					map.put("TC2AMT", "有 [交易编码2] 时必填。[相应金额1 "
							+ StringUtil.cleanBigInteger(financeDomPayment
									.getTc1amt())
							+ "] 和 [相应金额2 "
							+ StringUtil.cleanBigInteger(financeDomPayment
									.getTc2amt())
							+ "] 金额之和必须等于 [付款金额 "
							+ StringUtil.cleanBigInteger(financeDomPayment
									.getBaseDomPayment().getTxamt()) + "]。 \n");
				}
				if("1.2".equals(this.interfaceVer)){
					// 无此校验
				}else{
					if(!verifyImpdate(financeDomPayment.getImpdate())){
						map.put("IMPDATE", "[最迟装运日期] 必须输入并且必须为日期格式及合法性。 \n");
					}
				}
				if(!verifyContrno(financeDomPayment.getContrno())){
					map.put("CONTRNO", "[合同号] 不能为空。 \n");
				}
				if(!verifyInvoino(financeDomPayment.getInvoino())){
					map.put("INVOINO", "[发票号] 不能为空。 \n");
				}
				if(!verifyCrtuser(financeDomPayment.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financeDomPayment.getInptelc())){
					map.put("INPTELC", "[填报人电话]不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				if(!verifyRptdate(financeDomPayment.getRptdate())){
					map
							.put("RPTDATE",
									" 按申报主体真实 [申报日期] 填写，不能为空,必须小于等于当前日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(baseDomPayment.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								financeDomPayment.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
				if("1.2".equals(this.interfaceVer)){
					if(!verifyConfig.verifyRegno(financeDomPayment.getRptno(),
							financeDomPayment.getTxcode(), financeDomPayment
									.getTxcode2(), "payout")){
						map
								.put(
										"REGNO",
										"[外汇局批件号/登记表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空。 \n");
					}
				}else{
					// 无此校验
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 交易编码2 选输 必须在国际收支交易编码表中存在 不能与交易编码1相同， 没有输入交易编码时，相应金额及交易附言不应该填写。
	 * 有交易金额2或交易附言2时必填。
	 * @param txcode2 交易编码2
	 * @param type 操作类型字典
	 * @param txcode1 交易编码1
	 * @param tc2amt 相应金额2
	 * @return
	 */
	public boolean verifyTxcode2(String txcode2, String type, String txcode1,
			BigInteger tc2amt){
		if(StringUtil.isEmpty(txcode2)){
			if(tc2amt != null){
				return false;
			}
		}
		if(StringUtil.isNotEmpty(txcode2)){
			if(this.findKey(dictionarys, type, txcode2)){
				if(StringUtil.equals(txcode2, txcode1)){
					return false;
				}
			}
		}
		if(tc2amt != null){
			if(StringUtil.isEmpty(txcode2)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 最迟装运日期 必须输入，系统检查日期格式及合法性
	 * @param impdate 最迟装运日期
	 * @return
	 */
	public boolean verifyImpdate(String impdate){
		if(StringUtil.isNotEmpty(impdate)){
			if(impdate.trim().length() == 8){
				return true;
			}
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
