/**
 * 境外汇款申请书－申报信息　t_decl_remit
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
import com.cjit.gjsz.logic.model.BaseRemit;
import com.cjit.gjsz.logic.model.DeclareRemit;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class DeclareRemitDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_remit";
	public static final String PAYTYPE_VERIFY = "A,P,R,O";
	private VerifyConfig verifyConfig;

	public DeclareRemitDataVerify(){
	}

	public DeclareRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				DeclareRemit declareRemit = (DeclareRemit) verifylList.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BaseRemit baseRemit = (BaseRemit) service.getDataVerifyModel(
						parentTableId, declareRemit.getBusinessid());
				declareRemit.setBaseRemit(baseRemit);
				if(!verifyActiontype(declareRemit.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(declareRemit.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(declareRemit.getActiontype(), declareRemit
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
				if(!verifyAReasion(declareRemit.getActiontype(), declareRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(declareRemit.getActiontype(), declareRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyCountry(declareRemit.getCountry(), COUNTRY)){
					String type = getKey(declareRemit.getCountry(), COUNTRY);
					map.put("COUNTRY", "[国家/地区代码] [" + type + "] 无效.\n");
				}
				if(!verifyPaytype(declareRemit.getPaytype(), PAYTYPE_VERIFY)){
					String type = getKey(declareRemit.getPaytype(), PAYTYPE);
					map.put("PAYTYPE", "[付款类型] [" + type + "] 无效.\n");
				}
				// TODO: 编号006 到底汇款算收入还是支出呢
				if(!verifyTxcode(declareRemit.getTxcode(), BOP_PAYOUT)){
					String type = getKey(declareRemit.getTxcode(), BOP_PAYOUT);
					map.put("TXCODE", "[交易编码1] [" + type
							+ "] 无效.必须在国际收支交易编码表中存在 \n");
				}
				if(!verifyTc1amt(declareRemit.getTc1amt())){
					map.put("TC1AMT", "[相应金额1] 不能为空且不能为负数。 \n");
				}
				if(!verifyTxrem(declareRemit.getTxrem())){
					map.put("TXREM", "[交易附言1] 不能为空。 \n");
				}
				if(!verifyTxcode2(declareRemit.getTxcode2(), BOP_PAYOUT,
						declareRemit.getTxcode(), declareRemit.getTc2amt(),
						declareRemit.getTx2rem())){
					map
							.put(
									"TXCODE2",
									"[交易编码2] 必须在国际收支交易编码表中存在不能与 [交易编码1] 相同，没有输入交易编码时，相应金额及交易附言不应该填写。有交易金额2或交易附言2时必填。 \n");
				}
				// TODO: 编号002 重点部份之一
				if(!verifyTc2amt(declareRemit.getTc2amt(), declareRemit
						.getTxcode2(), declareRemit.getTc1amt(), declareRemit
						.getBaseRemit().getTxamt())){
					map.put("TC2AMT", "有 [交易编码2] 时必填。[相应金额1 "
							+ StringUtil.cleanBigInteger(declareRemit
									.getTc1amt())
							+ "] 和 [相应金额2 "
							+ StringUtil.cleanBigInteger(declareRemit
									.getTc2amt())
							+ "] 金额之和必须等于 [收入款金额 "
							+ StringUtil.cleanBigInteger(declareRemit
									.getBaseRemit().getTxamt())
							+ "]，且[相应金额2]不能为负数。 \n");
				}
				if(!verifyTx2rem(declareRemit.getTx2rem(), declareRemit
						.getTxcode2())){
					map.put("TX2REM", "有 [交易编码2] 时必填。 \n");
				}
				if(!verifyIsref(declareRemit.getIsref(), ISREF_VERIFY)){
					String columnName = "是否进口核销项下付款";
					if("1.2".equals(this.interfaceVer)){
						columnName = "是否为保税货物项下付款";
					}
					map.put("ISREF", "[" + columnName + "] 无效。\n");
				}
				if(!verifyCrtuser(declareRemit.getCrtuser())){
					map.put("CRTUSER", "[申请人] 不能为空。 \n");
				}
				if(!verifyInptelc(declareRemit.getInptelc())){
					map.put("INPTELC", "[申请人电话] 不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				StringBuffer sb = new StringBuffer();
				if(!verifyRptdate(declareRemit.getRptdate(), declareRemit
						.getRptno(), baseRemit.getTradedate(), sb)){
					map.put("RPTDATE", " 按申报主体真实申报日期填写, 必须小于等于当前系统日期。并且[申报日期 "
							+ declareRemit.getRptdate() + "] 必须小于等于 [申报号码中日期 "
							+ sb.toString()
							+ "]，如果 [申报号码日期] 为空，则与基础信息交易日期比较, 必须小于等于交易日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(baseRemit.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								declareRemit.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
				if("1.2".equals(this.interfaceVer)){
					if(!verifyConfig.verifyRegno(declareRemit.getRegno(),
							declareRemit.getTxcode(),
							declareRemit.getTxcode2(), "payout")){
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
	 * 申报日期 必填项，按申报主体真实申报日期填写。申报日期>=申报号码中的日期。
	 * @param rptdate 申报日期
	 * @param repno 申报号码
	 * @param tradedate 基础信息交易日期
	 * @return
	 */
	public boolean verifyRptdate(String rptdate, String repno,
			String tradedate, StringBuffer sb){
		if(StringUtil.isNotEmpty(rptdate)){
			// 申报日期
			Date dtRptDate = DateUtils.stringToDate(rptdate,
					DateUtils.ORA_DATE_FORMAT);
			// 申报号码为空 与当前系统日期作比较
			Date dtSystem = new Date();
			if(dtRptDate.getTime() <= dtSystem.getTime()){
				// 必须不晚于当前系统日期
				if(StringUtil.isNotEmpty(repno)){
					// 申报号码不为空 与申报号码中日期作比较
					if(repno.trim().length() != 22){
						return false;
					}
					// 解析申报号码
					String repnodate = repno.substring(12, 18);
					// 申报号码中日期
					Date date2 = DateUtils.stringToDate(repnodate,
							DateUtils.ORA_DATE_FORMAT_SIMPLE);
					if(dtRptDate.getTime() <= date2.getTime()){
						// 必须小于等于 申报号码日期
						return true;
					}
					sb.append(repnodate);
				}else if(StringUtil.isNotEmpty(tradedate)){
					// 在没有申报号的情况下（还没生成），比较基础信息中的交易日期(TRADEDATE)
					Date dtTradeDate = DateUtils.stringToDate(tradedate,
							DateUtils.ORA_DATE_FORMAT);
					if(dtRptDate.getTime() <= dtTradeDate.getTime()){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
