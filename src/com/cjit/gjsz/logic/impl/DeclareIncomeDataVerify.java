/**
 * 涉外收入申报单—申报信息 t_decl_income
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
import com.cjit.gjsz.logic.model.BaseIncome;
import com.cjit.gjsz.logic.model.DeclareIncome;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class DeclareIncomeDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_income";
	private VerifyConfig verifyConfig;

	public DeclareIncomeDataVerify(){
	}

	public DeclareIncomeDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				DeclareIncome declareIncome = (DeclareIncome) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BaseIncome baseIncome = (BaseIncome) service
						.getDataVerifyModel(parentTableId, declareIncome
								.getBusinessid());
				declareIncome.setBaseIncome(baseIncome);
				if(!verifyActiontype(declareIncome.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(declareIncome.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(declareIncome.getActiontype(), declareIncome
						.getRptno())){
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 start
					/*
					 * map .put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]。否则当时
					 * [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n" );
					 */
					map.put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为 [新建]");
					// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 end
				}
				// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
				if(!verifyAReasion(declareIncome.getActiontype(), declareIncome
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(declareIncome.getActiontype(), declareIncome
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCountry(declareIncome.getCountry(), COUNTRY)){
					String type = getKey(declareIncome.getCountry(), COUNTRY);
					map.put("COUNTRY", "[国家/地区代码] [" + type + "] 无效.\n");
				}
				if(!verifyPaytype(declareIncome.getPaytype(), PAYTYPE_VERIFY)){
					String type = getKey(declareIncome.getPaytype(), PAYTYPE);
					map.put("PAYTYPE", "[收款性质] [" + type + "] 无效.\n");
				}
				if(!verifyTxcode(declareIncome.getTxcode(), BOP_INCOME)){
					String type = getKey(declareIncome.getTxcode(), BOP_INCOME);
					map.put("TXCODE", "[交易编码1] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyTc1amt(declareIncome.getTc1amt())){
					map.put("TC1AMT", "[相应金额1] 不能为空。 \n");
				}
				if(!verifyTxrem(declareIncome.getTxrem())){
					map.put("TXREM", "[交易附言1] 不能为空。 \n");
				}
				if(!verifyTxcode2(declareIncome.getTxcode2(), BOP_INCOME,
						declareIncome.getTxcode(), declareIncome.getTc2amt(),
						declareIncome.getTx2rem())){
					map
							.put(
									"TXCODE2",
									"[交易编码2] 必须在国际收支交易编码表中存在，不能与 [交易编码1] 相同；没有输入交易编码时，相应金额及交易附言不应该填写；有交易金额2或交易附言2时必填。 \n");
				}else{
					// TODO: 编号002 重点部份之一
					if(!verifyTc2amt(
							declareIncome.getTc2amt(),
							declareIncome.getTxcode2(),
							declareIncome.getTc1amt(),
							declareIncome.getBaseIncome() == null ? BigInteger.ZERO
									: declareIncome.getBaseIncome().getTxamt())){
						map.put("TC2AMT", "有 [交易编码2] 时必填；[相应金额1 "
								+ StringUtil.cleanBigInteger(declareIncome
										.getTc1amt())
								+ "] 和 [相应金额2 "
								+ StringUtil.cleanBigInteger(declareIncome
										.getTc2amt())
								+ "] 金额之和必须等于 [收入款金额 "
								+ StringUtil.cleanBigInteger(declareIncome
										.getBaseIncome().getTxamt()) + "]。 \n");
					}
					if(!verifyTx2rem(declareIncome.getTx2rem(), declareIncome
							.getTxcode2())){
						map.put("TX2REM", "有 [交易编码2] 时必填。 \n");
					}
				}
				if(!verifyIsref(declareIncome.getIsref(), ISREF_VERIFY)){
					map.put("ISREF", "[是否出口核销项下收汇] 无效。\n");
				}
				if(!verifyCrtuser(declareIncome.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。 \n");
				}
				if(!verifyInptelc(declareIncome.getInptelc())){
					map.put("INPTELC", "[填报人电话] 不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				StringBuffer sb = new StringBuffer();
				if(!verifyRptdate(declareIncome.getRptdate(), declareIncome
						.getRptno(), baseIncome.getTradedate(), sb)){
					map.put("RPTDATE", " 按申报主体真实申报日期填写, 必须小于等于当前系统日期。并且[申报日期 "
							+ declareIncome.getRptdate() + "] 必须大于等于 [申报号码中日期 "
							+ sb.toString()
							+ "]。如果 [申报号码日期] 为空, 则与基础信息交易日期比较, 必须大于等于交易日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(baseIncome.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								declareIncome.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
				if("1.2".equals(this.interfaceVer)){
					if(!verifyConfig.verifyRegno(declareIncome.getBillno(),
							declareIncome.getTxcode(), declareIncome
									.getTxcode2(), "income")){
						map
								.put(
										"BILLNO",
										"[外汇局批件号/备案表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空。 \n");
					}
				}else{
					// 无此属性
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}