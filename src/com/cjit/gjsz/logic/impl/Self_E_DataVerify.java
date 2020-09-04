package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_E_EXPLRMBLO;
import com.cjit.gjsz.logic.model.Self_Sub_EXPLBALA;
import com.cjit.gjsz.logic.model.Self_Sub_EXPLCURR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_E_EXPLRMBLO]银行自身外债－外汇质押人民币贷款校验类
 */
public class Self_E_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_E_DataVerify(){
	}

	public Self_E_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_E_EXPLRMBLO explrmblo = (Self_E_EXPLRMBLO) verifylList
						.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				if(!verifyDictionaryValue(ACTIONTYPE, explrmblo.getActiontype())){
					String value = getKey(explrmblo.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D.equalsIgnoreCase(explrmblo
						.getActiontype())){
					if(StringUtil.isEmpty(explrmblo.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(explrmblo
						.getActiontype())){
					if(!isNull(explrmblo.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 外汇质押人民币贷款编号
				if(StringUtil.isNotEmpty(explrmblo.getExplrmblono())){
					String vErrInfo = verifyRptNo(explrmblo.getExplrmblono(),
							explrmblo.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("EXPLRMBLONO", "[外汇质押人民币贷款编号] " + vErrInfo);
					}
				}
				// 签约信息校验
				if("EA".equals(explrmblo.getFiletype())){
					if(ACTIONTYPE_D.equals(explrmblo.getActiontype())){
						if(!verifyCannotDelete("T_CFA_E_EXPLRMBLO", explrmblo
								.getFiletype(), explrmblo.getExplrmblono())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该外汇质押人民币贷款编号 ["
									+ explrmblo.getExplrmblono()
									+ "] 下的变动信息，本信息不可删除 ");
						}
					}
					// CREDITORCODE 债权人代码 字符型，12 必填项，金融机构标识码。
					if(isNull(explrmblo.getCreditorcode())
							|| explrmblo.getCreditorcode().length() != 12){
						map.put("CREDITORCODE", "[债权人代码] 应为12位金融机构标识码 ");
					}else{
						String strDistrictCode = explrmblo.getCreditorcode()
								.substring(0, 6);
						if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode)){
							map.put("CREDITORCODE", "[债权人代码] 前6位数字地区标识码有误 ");
						}
						// 行内校验
						if(limitBranchCode(explrmblo.getCreditorcode(),
								explrmblo.getFiletype(), explrmblo
										.getBusinessid(), explrmblo
										.getInstcode())){
							map.put("CREDITORCODE",
									"[债权人代码] 与当前记录所属机构对应的申报号码不匹配 ");
						}else if(checkBranchCode(explrmblo.getCreditorcode())){
							map.put("CREDITORCODE",
									"[债权人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
						}
					}
					// VALUEDATE 贷款起息日 日期型，8 必填项，格式YYYYMMDD。
					// MATURITY 贷款到期日 日期型,8 必填项，格式YYYYMMDD，大于等于起息日。
					if(!isNull(explrmblo.getValuedate())
							&& !isNull(explrmblo.getMaturity())){
						if(!verifyTwoDates(explrmblo.getValuedate(), explrmblo
								.getMaturity())){
							map.put("MATURITY", "贷款[到期日] 不能早于贷款起息日 ");
						}
					}else{
						if(isNull(explrmblo.getValuedate())){
							map.put("VALUEDATE", "[贷款起息日] 不能为空 ");
						}
						if(isNull(explrmblo.getMaturity())){
							map.put("MATURITY", "[贷款到期日] 不能为空 ");
						}
					}
					// CREDCONCURR 贷款签约币种 字符型，3 必填项，默认为人民币。
					if(isNull(explrmblo.getCredconcurr())){
						map.put("CREDCONCURR", "[贷款签约币种] 不能为空 ");
					}else if(!verifyDictionaryValue(CURRENCY, explrmblo
							.getCredconcurr())){
						String value = getKey(explrmblo.getCredconcurr(),
								CURRENCY);
						map.put("CREDCONCURR", "[贷款签约币种] [" + value + "] 无效 ");
					}
					// CREDCONAMOUNT 贷款签约金额 数值型，22.2 必填项，大于等于0。
					if(isNull(explrmblo.getCredconamount())
							|| explrmblo.getCredconamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("CREDCONAMOUNT", "[贷款签约金额] 不能为空且大于等于0 ");
					}
					// 质押外汇金额信息
					List subList = service.getCfaChildren(
							"T_CFA_SUB_EXPLCURR_INFO", explrmblo
									.getBusinessid());
					List list = new ArrayList();
					if(CollectionUtil.isNotEmpty(subList)){
						Self_Sub_ExplcurrVerify subVerify = null;
						for(int j = 0; j < subList.size(); j++){
							Self_Sub_EXPLCURR sub = (Self_Sub_EXPLCURR) subList
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_ExplcurrVerify(
									dictionarys, tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_EXPLCURR_INFO");
								list.add(vm.getFatcher());
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							verifyModel.setChildren(list);
						}
					}else{
						map.put("T_CFA_SUB_EXPLCURR_INFO", "[质押外汇金额信息] 不能为空 ");
					}
					if(isNull(explrmblo.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(
							explrmblo.getTradedate(),
							DateUtils
									.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
					// 特殊校验
					if(configMap != null){
						String checkCredconAmountIntJPY = (String) configMap
								.get("config.check.DAEA.credconamount.intJPY");
						// 当贷款币种为JPY时金额不能有小数
						if("yes".equalsIgnoreCase(checkCredconAmountIntJPY)){
							if("JPY".equals(explrmblo.getCredconcurr())
									&& !isNull(explrmblo.getCredconamount())
									&& explrmblo.getCredconamount()
											.doubleValue() > Math
											.floor(explrmblo.getCredconamount()
													.doubleValue())){
								map.put("CREDCONAMOUNT",
										"[贷款签约金额] 不能是含有小数的JPY日元金额 ");
							}
						}
						String limitEACredconcurrCNY = (String) configMap
								.get("config.limit.EA.credconcurr.CNY");
						// 是否限制外汇质押人民币贷款签约币种必需为人民币
						if("yes".equalsIgnoreCase(limitEACredconcurrCNY)){
							if(!"CNY".equals(explrmblo.getCredconcurr())){
								map.put("CREDCONCURR", "[贷款签约币种] 必需是CNY人民币 ");
							}
						}
					}
				}
				// 若当前校验报文为变动信息，则需查询对应上级签约信息
				else if("EB".equals(explrmblo.getFiletype())){
					Self_E_EXPLRMBLO contractExplrmblo = (Self_E_EXPLRMBLO) service
							.getDataVerifyModel("T_CFA_E_EXPLRMBLO", explrmblo
									.getExplrmblono(), explrmblo
									.getBusinessno());
					if(contractExplrmblo == null){
						map.put("EXPLRMBLONO", "未发现此外汇质押人民币贷款编号对应的签约信息 ");
						continue;
					}else if(ACTIONTYPE_A.equals(explrmblo.getActiontype())
							&& contractExplrmblo.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("EXPLRMBLONO", "对应签约信息尚未校验通过");
						continue;
					}else if(ACTIONTYPE_D.equals(contractExplrmblo
							.getActiontype())
							&& contractExplrmblo.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("EXPLRMBLONO", "对应签约信息已报送删除");
						continue;
					}
					if(checkByeRptNoRepeat(explrmblo.getFiletype(), explrmblo
							.getChangeno(), explrmblo.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 已经存在 ");
						continue;
					}
					if(checkFormerByeRptNo(explrmblo.getFiletype(), explrmblo
							.getChangeno(), explrmblo.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 存在尚未审核通过的往期记录 ");
						continue;
					}
					// BUOCMONTH 报告期 数值型，6 必填项，格式为YYYYMM。
					if(isNull(explrmblo.getBuocmonth())
							|| explrmblo.getBuocmonth().length() != 6){
						map.put("BUOCMONTH", "[报告期] 不能为空，格式为YYYYMM ");
					}else{
						Date date = DateUtils.stringToDate(explrmblo
								.getBuocmonth(), "yyyyMM");
						if(date == null){
							map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
						}else if(!explrmblo.getBuocmonth().startsWith("19")
								&& !explrmblo.getBuocmonth().startsWith("20")
								&& !explrmblo.getBuocmonth().startsWith("21")){
							map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
						}else{
							String month = explrmblo.getBuocmonth().substring(
									4, 6);
							if(Integer.valueOf(month).intValue() > 12
									|| Integer.valueOf(month).intValue() == 0){
								map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
							}
						}
					}
					// MONBELOADBAL 月初贷款余额 数值型，22.2 必填项，大于等于0。
					if(isNull(explrmblo.getMonbeloadbal())
							|| explrmblo.getMonbeloadbal().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("MONBELOADBAL", "[月初贷款余额] 不能为空且大于等于0 ");
					}
					// CREDWITHAMOUNT 本月提款金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(explrmblo.getCredwithamount())
							&& explrmblo.getCredwithamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("CREDWITHAMOUNT", "[本月提款金额] 应大于等于0 ");
					}
					// CREDREPAYAMOUNT 本月还本金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(explrmblo.getCredrepayamount())
							&& explrmblo.getCredrepayamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("CREDREPAYAMOUNT", "[本月还本金额] 应大于等于0 ");
					}
					// PICAMOUNT 本月付息费金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(explrmblo.getPicamount())
							&& explrmblo.getPicamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("PICAMOUNT", "[本月付息费金额] 应大于等于0 ");
					}
					// MONENLOADBAL 月末贷款余额 数值型，22.2 必填项，大于等于0。
					if(isNull(explrmblo.getMonenloadbal())
							|| explrmblo.getMonenloadbal().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("MONENLOADBAL", "[月末贷款余额] 不能为空且大于等于0 ");
					}
					// 质押外汇余额信息
					List subList = service.getCfaChildren(
							"T_CFA_SUB_EXPLBALA_INFO", explrmblo
									.getBusinessid());
					List list = new ArrayList();
					if(CollectionUtil.isNotEmpty(subList)){
						Self_Sub_ExplbalaVerify subVerify = null;
						for(int j = 0; j < subList.size(); j++){
							Self_Sub_EXPLBALA sub = (Self_Sub_EXPLBALA) subList
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_ExplbalaVerify(
									dictionarys, tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_EXPLBALA_INFO");
								list.add(vm.getFatcher());
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							StringBuffer text = new StringBuffer();
							for(Iterator s = list.iterator(); s.hasNext();){
								Map innerResult = (HashMap) s.next();
								for(Iterator j = innerResult.keySet()
										.iterator(); j.hasNext();){
									String wrongColumnId = (String) j.next();
									if(!"SUBID".equals(wrongColumnId)
											&& !"INNERTABLEID"
													.equals(wrongColumnId)){
										text.append(
												innerResult.get(wrongColumnId))
												.append("\n");
									}
								}
							}
							if(StringUtil.isNotEmpty(text.toString())){
								map.put("T_CFA_SUB_EXPLBALA_INFO",
										"[质押外汇余额信息] \n" + text.toString());
							}
						}
					}else{
						map.put("T_CFA_SUB_EXPLBALA_INFO", "[质押外汇余额信息] 不能为空 ");
					}
					// 特殊校验
					if(configMap != null){
						String checkMustInputChangeMoney = (String) configMap
								.get("config.check.EB.mustInputChangeMoney");
						// 本月提款金额、本月还本金额、本月付息费金额必填其一
						if("yes".equalsIgnoreCase(checkMustInputChangeMoney)){
							if(isNull(explrmblo.getCredwithamount())
									&& isNull(explrmblo.getCredrepayamount())
									&& isNull(explrmblo.getPicamount())){
								map
										.put("CREDWITHAMOUNT",
												"[本月提款金额]、[本月还本金额]、[本月付息费金额]中至少有一个不为空 ");
								map
										.put("CREDREPAYAMOUNT",
												"[本月提款金额]、[本月还本金额]、[本月付息费金额]中至少有一个不为空 ");
								map
										.put("PICAMOUNT",
												"[本月提款金额]、[本月还本金额]、[本月付息费金额]中至少有一个不为空 ");
							}
						}
						String checkMoneyLogicCalculation = (String) configMap
								.get("config.check.EB.moneyLogicCalculation");
						if("yes".equalsIgnoreCase(checkMoneyLogicCalculation)){
							if(!isNull(explrmblo.getMonbeloadbal())
									&& (!isNull(explrmblo.getCredwithamount()) || !isNull(explrmblo
											.getCredrepayamount()))
									&& !isNull(explrmblo.getMonenloadbal())){
								BigDecimal bd1 = explrmblo.getMonbeloadbal();
								BigDecimal bd2 = explrmblo.getMonenloadbal();
								if(!isNull(explrmblo.getCredwithamount())){
									bd1 = explrmblo.getMonbeloadbal().add(
											explrmblo.getCredwithamount());
								}
								if(!isNull(explrmblo.getCredrepayamount())){
									bd2 = explrmblo.getMonenloadbal().add(
											explrmblo.getCredrepayamount());
								}
								if(bd1.compareTo(bd2) != 0){
									map
											.put("MONENLOADBAL",
													"[月末贷款余额]应等于 月初贷款余额＋本月提款金额－本月还本金额 ");
								}
							}
						}
					}
				}
				// 业务编号校验
				// 不能重复
				if("EA".equals(explrmblo.getFiletype())
						&& checkBusinessNoRepeat(explrmblo.getBusinessno(),
								"T_CFA_E_EXPLRMBLO", explrmblo.getFiletype(),
								explrmblo.getBusinessid())){
					map.put("BUSINESSNO", "[业务编号] 外汇质押人民币贷款签约信息中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(explrmblo.getBusinessno())){
					map.put("BUSINESSNO", "[业务编号] 不能为空 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
