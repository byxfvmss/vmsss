/**
 * 对外付款/承兑通知书－申报信息　t_decl_payment
 */
package com.cjit.gjsz.logic.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BasePayment;
import com.cjit.gjsz.logic.model.DeclarePayment;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class DeclarePaymentDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_payment";
	public static final String PAYTYPE_VERIFY = "A,O";
	private VerifyConfig verifyConfig;

	public DeclarePaymentDataVerify(){
	}

	public DeclarePaymentDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				DeclarePayment declarePayment = (DeclarePayment) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BasePayment basePayment = (BasePayment) service
						.getDataVerifyModel(parentTableId, declarePayment
								.getBusinessid());
				declarePayment.setBasePayment(basePayment);
				if(!verifyActiontype(declarePayment.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(declarePayment.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(declarePayment.getActiontype(), declarePayment
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
				if(!verifyAReasion(declarePayment.getActiontype(),
						declarePayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(declarePayment.getActiontype(),
						declarePayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCountry(declarePayment.getCountry(), COUNTRY)){
					String type = getKey(declarePayment.getCountry(), COUNTRY);
					map.put("COUNTRY", "[收款人常驻国家/地区代码] [" + type + "] 无效.\n");
				}
				if(!verifyPaytype(declarePayment.getPaytype(), PAYTYPE_VERIFY)){
					String type = getKey(declarePayment.getPaytype(), PAYTYPE);
					map.put("PAYTYPE", "[付款类型] [" + type + "] 无效.\n");
				}
				if(!verifyTxcode(declarePayment.getTxcode(), BOP_PAYOUT)){
					String type = getKey(declarePayment.getTxcode(), BOP_PAYOUT);
					map.put("TXCODE", "[交易编码1] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyTc1amt(declarePayment.getTc1amt())){
					map.put("TC1AMT", "[相应金额1] 不能为空。 \n");
				}
				if(!verifyTxrem(declarePayment.getTxrem())){
					map.put("TXREM", "[交易附言1] 不能为空。 \n");
				}
				if(!verifyTxcode2(declarePayment.getTxcode2(), BOP_PAYOUT,
						declarePayment.getTxcode(), declarePayment.getTc2amt(),
						declarePayment.getTx2rem())){
					map
							.put(
									"TXCODE2",
									"[交易编码2] 必须在国际收支交易编码表中存在不能与 [交易编码1] 相同，没有输入交易编码时，相应金额及交易附言不应该填写。有交易金额2或交易附言2时必填。 \n");
				}
				// TODO: 编号002 重点部份之一
				if(!verifyTc2amt(declarePayment.getTc2amt(), declarePayment
						.getTxcode2(), declarePayment.getTc1amt(),
						declarePayment.getBasePayment().getTxamt())){
					map.put("TC2AMT", "有 [交易编码2] 时必填。[相应金额1 "
							+ StringUtil.cleanBigInteger(declarePayment
									.getTc1amt())
							+ "] 和 [相应金额2 "
							+ StringUtil.cleanBigInteger(declarePayment
									.getTc2amt())
							+ "] 金额之和必须等于 [收入款金额 "
							+ StringUtil.cleanBigInteger(declarePayment
									.getBasePayment().getTxamt()) + "]。 \n");
				}
				if(!verifyTx2rem(declarePayment.getTx2rem(), declarePayment
						.getTxcode2())){
					map.put("TX2REM", "有 [交易编码2] 时必填。 \n");
				}
				if(!verifyIsref(declarePayment.getIsref(), ISREF_VERIFY)){
					map.put("ISREF", "[是否进口核销项下付款] 无效。\n");
				}
				if(!verifyCrtuser(declarePayment.getCrtuser())){
					map.put("CRTUSER", "[申请人] 不能为空。 \n");
				}
				if(!verifyInptelc(declarePayment.getInptelc())){
					map.put("INPTELC", "[申请人电话] 不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				if(!verifyRptdate(declarePayment.getRptdate())){
					map.put("RPTDATE", " 按申报主体真实申报日期填写,必须小于等于当前日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(basePayment.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								declarePayment.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
				if("1.2".equals(this.interfaceVer)){
					if(!verifyConfig.verifyRegno(declarePayment.getRegno(),
							declarePayment.getTxcode(), declarePayment
									.getTxcode2(), "payout")){
						map
								.put(
										"REGNO",
										"[外汇局批件号/备案表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空。 \n");
					}
				}else{
					// 无此属性
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
		if(StringUtil.isNotEmpty(rptdate)){
			if(rptdate.trim().length() == 8){
				Date date1 = new Date();
				Date date2 = DateUtils.stringToDate(rptdate,
						DateUtils.ORA_DATE_FORMAT);
				if(date1.getTime() >= date2.getTime()){
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
