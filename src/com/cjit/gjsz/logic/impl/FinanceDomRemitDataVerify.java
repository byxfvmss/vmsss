/**
 * 境内汇款申请书－核销专用信息 t_fini_dom_remit
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseDomRemit;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.FinanceDomRemit;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinanceDomRemitDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_dom_remit";
	public final String childTableId = "t_customs_decl";
	private VerifyConfig verifyConfig;
	public static final String PAYTYPE_VERIFY = "A,P,R,O";
	public static final String PAYATTR_VERIFY = "X,E,D,S,M,O";
	public static final String PAYATTR_VERIFY_12 = "A,X,E,D,M,O";

	public FinanceDomRemitDataVerify(){
	}

	public FinanceDomRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceDomRemit financeDomRemit = (FinanceDomRemit) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BaseDomRemit baseDomRemit = (BaseDomRemit) service
						.getDataVerifyModel(parentTableId, financeDomRemit
								.getBusinessid());
				financeDomRemit.setBaseDomRemit(baseDomRemit);
				boolean has = false;
				if(StringUtil.isEmpty(this.interfaceVer)
						|| "1.1".equals(this.interfaceVer)){
					long size = service.getDataVerifyModelSize(childTableId,
							financeDomRemit.getBusinessid());
					has = size > 0 ? true : false;
				}
				if(!verifyActiontype(financeDomRemit.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financeDomRemit.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financeDomRemit.getActiontype(),
						financeDomRemit.getRptno())){
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 start
					/*
					 * map .put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时
					 * [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n" );
					 */
					map.put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]");
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 end
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(financeDomRemit.getActiontype(),
						financeDomRemit.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(financeDomRemit.getActiontype(),
						financeDomRemit.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCountry(financeDomRemit.getCountry(), COUNTRY)){
					String type = getKey(financeDomRemit.getCountry(), COUNTRY);
					map.put("COUNTRY", "[国家/地区代码] [" + type + "] 无效.\n");
				}
				if(!verifyIsref(financeDomRemit.getIsref(), ISREF_VERIFY)){
					String type = getKey(financeDomRemit.getIsref(), ISREF);
					if(StringUtil.isEmpty(this.interfaceVer)
							|| "1.1".equals(this.interfaceVer)){
						map.put("ISREF", "[是否出口核销项下收汇] [" + type + "] 无效.\n");
					}else{
						map.put("ISREF", "[是否为保税货物项下付款] [" + type + "] 无效.\n");
					}
				}
				if(!verifyPaytype(financeDomRemit.getPaytype(), PAYTYPE_VERIFY)){
					String type = getKey(financeDomRemit.getPaytype(), PAYTYPE);
					map.put("PAYTYPE", "[付款类型] [" + type + "] 无效.\n");
				}
				if(StringUtil.isNotEmpty(interfaceVer)){
					if(!verifyPayattr(financeDomRemit.getPayattr(),
							PAYATTR_VERIFY_12)){
						String type = getKey(financeDomRemit.getPayattr(),
								PAYATTR1);
						map.put("PAYATTR", "[付汇性质] [" + type + "] 无效.\n");
					}
				}else{
					if(!verifyPayattr(financeDomRemit.getPayattr(),
							PAYATTR_VERIFY)){
						String type = getKey(financeDomRemit.getPayattr(),
								PAYATTR1);
						map.put("PAYATTR", "[付汇性质] [" + type + "] 无效.\n");
					}
				}
				if(!verifyTxcode(financeDomRemit.getTxcode(), BOP_PAYOUT)){
					String type = getKey(financeDomRemit.getTxcode(),
							BOP_PAYOUT);
					map.put("TXCODE", "[交易编码1] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyTc1amt(financeDomRemit.getTc1amt())){
					map.put("TC1AMT", "[相应金额1] 不能为空。 \n");
				}
				if(!verifyTxcode2(financeDomRemit.getTxcode2(), BOP_PAYOUT,
						financeDomRemit.getTxcode(), financeDomRemit
								.getTc2amt())){
					map
							.put("TXCODE2",
									"[交易编码2] 必须在国际收支交易编码表中存在不能与 [交易编码1] 相同，没有输入交易编码时，相应金额及交易附言不应该填写。有交易金额2时必填。 \n");
				}
				if(!verifyTc2amt(financeDomRemit.getTc2amt(), financeDomRemit
						.getTxcode2(), financeDomRemit.getTc1amt(),
						financeDomRemit.getBaseDomRemit().getTxamt())){
					map.put("TC2AMT", "有 [交易编码2] 时必填。[相应金额1 "
							+ StringUtil.cleanBigInteger(financeDomRemit
									.getTc1amt())
							+ "] 和 [相应金额2 "
							+ StringUtil.cleanBigInteger(financeDomRemit
									.getTc2amt())
							+ "] 金额之和必须等于 [付款金额 "
							+ StringUtil.cleanBigInteger(financeDomRemit
									.getBaseDomRemit().getTxamt()) + "]。 \n");
				}
				if("1.2".equals(this.interfaceVer)){
					// 无此校验
				}else{
					if(!verifyImpdate(financeDomRemit.getImpdate(),
							financeDomRemit.getPaytype())){
						map
								.put("IMPDATE",
										"付款方式为[A-预付货款]时,[最迟装运日期] 必须输入并且必须为日期格式及合法性。 \n");
					}
				}
				if(!verifyContrno(financeDomRemit.getContrno())){
					map.put("CONTRNO", "[合同号] 不能为空。 \n");
				}
				if(!verifyInvoino(financeDomRemit.getInvoino())){
					map.put("INVOINO", "[发票号] 不能为空。 \n");
				}
				if(!verifyCrtuser(financeDomRemit.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financeDomRemit.getInptelc())){
					map.put("INPTELC", "[填报人电话] 不能为空。\n");
				}
				if(!verifyRptdate(financeDomRemit.getRptdate())){
					map.put("RPTDATE",
							"[申报日期] 不能为空，按申报主体真实申报日期填写,必须小于等于当前日期。\n");
				}
				// DFHL: 增加校验 add by wangxin 20091123
				/*
				 * 核销信息_境内汇款申请书中，付款类型为“P-货到付款”时， 接口程序对“是否已经填写报关单信息”没有校验功能，
				 * 如果不填写报关单信息直接保存，接口程序依然可以校验通过。
				 * 但是在SAFE端，对此付款类型进行报关单信息验证，若无报关单信息，
				 * 则会反馈一个错误报文。同样的情况，申报信息_境外汇款申请书中，
				 * 付款方式为“P-货到付款”，输入核销信息时因系统无法显示“报关单信息” 一栏，
				 * 所以在输入核销信息的同时无法输入报关单信息，在保存逻辑校验通过OK 后， 系统会自动从输入界面跳到初始的查询界面，
				 * 必须要在查询界面点击刚刚输入的核销信息后才能重新进入到核销信息界面来输入报关单信息。
				 * 请修改：付款方式为“P-货到付款”，报关单信息必须输入。
				 */
				if("1.2".equals(this.interfaceVer)){
					// 无如下校验
				}else{
					if("P".equals(financeDomRemit.getPaytype()) && !has){
						if(!map.containsKey("PAYTYPE"))
							map.put("PAYTYPE",
									"[付款类型] 如果付款类型为[P-货到付款]则必须填写报关单信息.\n");
					}
					if(verifyCusmno(financeDomRemit.getCusmno(), has)){
						// 境外汇款申请书—核销专用信息 的子类验证
						List list = new ArrayList();
						List children = service.getChildren(childTableId,
								financeDomRemit.getBusinessid());
						if(CollectionUtil.isNotEmpty(children)){
							CustomDeclareDataVerify customDeclareDataVerify = null;
							for(int j = 0; j < children.size(); j++){
								CustomDeclare customDeclare = (CustomDeclare) children
										.get(j);
								if(customDeclare != null){
									customDeclare
											.setFinanceDomRemit(financeDomRemit);
								}
								List tmp = new ArrayList();
								tmp.add(customDeclare);
								customDeclareDataVerify = new CustomDeclareDataVerify(
										dictionarys, tmp, interfaceVer);
								VerifyModel vm = customDeclareDataVerify
										.execute();
								if(vm.getFatcher() != null
										&& !vm.getFatcher().isEmpty()){
									vm.getFatcher().put(SUBID,
											customDeclare.getSubid());
									list.add(vm.getFatcher());
								}
							}
							if(CollectionUtil.isNotEmpty(list)){
								verifyModel.setChildren(list);
							}
						}
					}else{
						map.put("CUSMNO", "如果填写了 [报关单信息] ，此代码必须输入。 \n");
					}
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(baseDomRemit.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								financeDomRemit.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
				if("1.2".equals(this.interfaceVer)){
					if(!verifyConfig.verifyRegno(financeDomRemit.getRegno(),
							financeDomRemit.getTxcode(), financeDomRemit
									.getTxcode2(), "payout")){
						map
								.put(
										"REGNO",
										"[外汇局批件号/备案表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空。 \n");
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

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
