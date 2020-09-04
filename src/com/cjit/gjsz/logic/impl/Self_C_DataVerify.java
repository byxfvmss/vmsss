package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.Self_C_DOFOEXLO;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_C_DOFOEXLO]银行自身外债－国内外汇贷款（含外债转贷款）信息校验类
 */
public class Self_C_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_C_DataVerify(){
	}

	public Self_C_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_C_DOFOEXLO dofoexlo = (Self_C_DOFOEXLO) verifylList.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				if(!verifyDictionaryValue(ACTIONTYPE, dofoexlo.getActiontype())){
					String value = getKey(dofoexlo.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D
						.equalsIgnoreCase(dofoexlo.getActiontype())){
					if(StringUtil.isEmpty(dofoexlo.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(dofoexlo
						.getActiontype())){
					if(!isNull(dofoexlo.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 国内外汇贷款
				if(StringUtil.isNotEmpty(dofoexlo.getDofoexlocode())){
					String vErrInfo = verifyRptNo(dofoexlo.getDofoexlocode(),
							dofoexlo.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("DOFOEXLOCODE", "[国内外汇贷款编号] " + vErrInfo);
					}
				}
				// 签约信息校验
				if("CA".equals(dofoexlo.getFiletype())){
					if(ACTIONTYPE_D.equals(dofoexlo.getActiontype())){
						if(!verifyCannotDelete("T_CFA_C_DOFOEXLO", dofoexlo
								.getFiletype(), dofoexlo.getDofoexlocode())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该国内外汇贷款编号 ["
									+ dofoexlo.getDofoexlocode()
									+ "] 下的变动信息，本信息不可删除 ");
						}
					}
					// CREDITORCODE 债权人代码 字符型，12 必填项，金融机构标识码
					if(dofoexlo.getCreditorcode() != null){
						if(dofoexlo.getCreditorcode().length() != 12){
							map.put("CREDITORCODE", "[债权人代码] 应为12位金融机构标识码 ");
						}else{
							String strDistrictCode = dofoexlo.getCreditorcode()
									.substring(0, 6);
							if(!verifyDictionaryValue(DISTRICTCO,
									strDistrictCode)){
								map
										.put("CREDITORCODE",
												"[债权人代码] 前6位数字地区标识码有误 ");
							}
							// 行内校验
							if(limitBranchCode(dofoexlo.getCreditorcode(),
									dofoexlo.getFiletype(), dofoexlo
											.getBusinessid(), dofoexlo
											.getInstcode())){
								map.put("CREDITORCODE",
										"[债权人代码] 与当前记录所属机构对应的申报号码不匹配 ");
							}else if(checkBranchCode(dofoexlo.getCreditorcode())){
								map.put("CREDITORCODE",
										"[债权人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
							}
						}
					}
					// 此校验方法未必准确，故暂且屏蔽
					// if(!verifyCustcode(dofoexlo.getDebtorcode(), "C")){
					// map
					// .put("DEBTORCODE",
					// "[债务人代码] 不能为空；并且必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。\n");
					// }
					// DOFOEXLOTYPE 国内外汇贷款类型 字符型，4 必填项，见国内外汇贷款类型代码表。
					if(isNull(dofoexlo.getDofoexlotype())){
						map.put("DOFOEXLOTYPE", "[国内外汇贷款类型] 不能为空 ");
					}else if(!verifyDictionaryValue(DOFOEXLOTYPE, dofoexlo
							.getDofoexlotype())){
						String value = getKey(dofoexlo.getDofoexlotype(),
								DOFOEXLOTYPE);
						map.put("DOFOEXLOTYPE", "[国内外汇贷款类型] [" + value
								+ "] 无效 ");
					}
					// LENPRONAME 转贷项目名称 字符型，128 非必填项，当国内外汇贷款类型为“外债转贷款”时为必填。
					if(isNull(dofoexlo.getLenproname())
							&& "1300".equals(dofoexlo.getDofoexlotype())){
						map
								.put("LENPRONAME",
										"[转贷项目名称] 当国内外汇贷款类型为“外债转贷款”时为必填 ");
					}
					// LENAGREE 转贷协议号 字符型，128 非必填项，当国内外汇贷款类型为“外债转贷款”时为必填。
					if(isNull(dofoexlo.getLenagree())
							&& "1300".equals(dofoexlo.getDofoexlotype())){
						map.put("LENAGREE", "[转贷协议号] 当国内外汇贷款类型为“外债转贷款”时为必填 ");
					}
					if(isNull(dofoexlo.getValuedate())){
						map.put("VALUEDATE", "[起息日] 不能为空 ");
					}
					if(!isNull(dofoexlo.getValuedate())
							&& !isNull(dofoexlo.getMaturity())){
						if(!verifyTwoDates(dofoexlo.getValuedate(), dofoexlo
								.getMaturity())){
							map.put("MATURITY", "[到期日] 不能早于起息日 ");
						}
					}
					// CURRENCE 贷款币种 字符型，3 必填项，见币种代码表
					if(isNull(dofoexlo.getCurrence())){
						map.put("CURRENCE", "[贷款币种] 不能为空 ");
					}else if(!verifyDictionaryValue(CURRENCY, dofoexlo
							.getCurrence())){
						String value = getKey(dofoexlo.getCurrence(), CURRENCY);
						map.put("CURRENCE", "[贷款币种] [" + value + "] 无效 ");
					}
					// CONTRACTAMOUNT 签约金额 数值型，22.2 必填项，大于等于0
					if(isNull(dofoexlo.getContractamount())){
						map.put("CONTRACTAMOUNT", "[签约金额] 不能为空 ");
					}else if(dofoexlo.getContractamount().compareTo(
							new BigDecimal(0.0)) < 0){
						map.put("CONTRACTAMOUNT", "[签约金额] 应大于等于0 ");
					}
					// ANNINRATE 年化利率值 数值型，13.8 必填项，大于等于0
					if(isNull(dofoexlo.getAnninrate())){
						map.put("ANNINRATE", "[年化利率值] 不能为空 ");
					}else if(dofoexlo.getAnninrate().compareTo(
							new BigDecimal(0.0)) < 0){
						map.put("ANNINRATE", "[年化利率值] 应大于等于0 ");
					}
					if(isNull(dofoexlo.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(dofoexlo.getTradedate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
					// 特殊校验
					if(configMap != null){
						String checkCurrenceNotCNY = (String) configMap
								.get("config.check.CA.currence.notCNY");
						// 国内外汇贷款签约信息贷款币种不能是CNY
						if("yes".equalsIgnoreCase(checkCurrenceNotCNY)){
							if("CNY".equals(dofoexlo.getCurrence())){
								map.put("CURRENCE", "[贷款币种] 不能是CNY人民币 ");
							}
						}
					}
				}
				// 若当前校验报文为变动信息，则需查询对应上级签约信息和往期变动信息
				else if("CB".equals(dofoexlo.getFiletype())){
					// 上级签约信息
					Self_C_DOFOEXLO contractDofoexl = (Self_C_DOFOEXLO) service
							.getDataVerifyModel("T_CFA_C_DOFOEXLO", dofoexlo
									.getDofoexlocode(), dofoexlo
									.getBusinessno());
					if(contractDofoexl == null){
						map.put("DOFOEXLOCODE", "未发现此国内外汇贷款编号对应的签约信息 ");
						continue;
					}else if(ACTIONTYPE_A.equals(dofoexlo.getActiontype())
							&& contractDofoexl.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("DOFOEXLOCODE", "对应签约信息尚未校验通过");
						continue;
					}else if(ACTIONTYPE_D.equals(contractDofoexl
							.getActiontype())
							&& contractDofoexl.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("DOFOEXLOCODE", "对应签约信息已报送删除");
						continue;
					}
					if(checkByeRptNoRepeat(dofoexlo.getFiletype(), dofoexlo
							.getChangeno(), dofoexlo.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 已经存在 ");
						continue;
					}
					if(checkFormerByeRptNo(dofoexlo.getFiletype(), dofoexlo
							.getChangeno(), dofoexlo.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 存在尚未审核通过的往期记录 ");
						continue;
					}
					// 往期变动信息
					SearchModel searchModel = new SearchModel();
					searchModel.setTableId("T_CFA_C_DOFOEXLO");
					StringBuffer sbCondition = new StringBuffer();
					sbCondition.append(
							" filetype = 'CB' and actiontype <> 'D' ").append(
							" and datastatus in (").append(
							DataUtil.SHYTG_STATUS_NUM).append(",").append(
							DataUtil.YSC_STATUS_NUM).append(",").append(
							DataUtil.YBS_STATUS_NUM).append(",").append(
							DataUtil.LOCKED_STATUS_NUM).append(
							") and BUSINESSNO = '").append(
							dofoexlo.getBusinessno()).append(
							"' and businessid <> '").append(
							dofoexlo.getBusinessid()).append("' ");
					searchModel.setSearchCondition(sbCondition.toString());
					searchModel
							.setOrderBy(" order by CHANGEDATE desc, CHANGENO desc ");
					List changeList = service.search(searchModel);
					// LOANOPENBALAN 期初余额 数值型，22.2 必填项，期初余额=上期末余额。
					if(isNull(dofoexlo.getLoanopenbalan())){
						map.put("LOANOPENBALAN", "[期初余额] 不能为空 ");
					}else{
						if(changeList != null && changeList.size() > 0){
							// 存在审核通过或更高级的前期变动信息
							Self_C_DOFOEXLO lastChange = (Self_C_DOFOEXLO) changeList
									.get(0);
							if(lastChange != null){
								if(lastChange.getEndbalan() != null
										&& lastChange.getEndbalan().compareTo(
												dofoexlo.getLoanopenbalan()) != 0){
									map.put("LOANOPENBALAN", "[期初余额] 应等于上期末余额");
								}
							}
						}else{
							if(dofoexlo.getLoanopenbalan().compareTo(
									new BigDecimal("0.00")) != 0){
								// map.put("LOANOPENBALAN", "[期初余额] 应等于0");
							}
						}
					}
					// CHANGEDATE 变动日期 日期型，8 必填项，格式YYYYMMDD，大于等于起息日。
					if(isNull(dofoexlo.getChangedate())){
						map.put("CHANGEDATE", "[变动日期] 不能为空 ");
					}else if(!isNull(contractDofoexl.getValuedate())
							&& !verifyTwoDates(contractDofoexl.getValuedate(),
									dofoexlo.getChangedate())){
						map.put("CHANGEDATE", "[变动日期] 不能早于签约信息起息日 ["
								+ contractDofoexl.getValuedate() + "] ");
					}
					// WITHCURRENCE 提款币种 字符型，3 非必填项，见币种代码表。如果“提款金额”>0，则必填。
					if(isNull(dofoexlo.getWithcurrence())){
						if(!isNull(dofoexlo.getWithamount())
								&& dofoexlo.getWithamount().compareTo(
										new BigDecimal("0.00")) > 0){
							map.put("WITHCURRENCE", "[提款币种] 提款金额大于0时，提款币种必填 ");
						}
						if(!isNull(dofoexlo.getSettamount())
								&& dofoexlo.getSettamount().compareTo(
										new BigDecimal("0.00")) > 0){
							map.put("WITHCURRENCE",
									"[提款币种] 结汇金额按照提款币种对应的金额填报，大于0时，提款币种必填 ");
						}
					}
					boolean haveWithAmount = false;
					boolean haveSettAmount = false;
					// WITHAMOUNT 提款金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(dofoexlo.getWithamount())){
						if(dofoexlo.getWithamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("WITHAMOUNT", "[提款金额] 可为空，否则应大于等于0 ");
						}else if(dofoexlo.getWithamount().compareTo(
								new BigDecimal("0.00")) > 0){
							haveWithAmount = true;
						}
					}
					// SETTAMOUNT 结汇金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(dofoexlo.getSettamount())){
						if(dofoexlo.getSettamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("SETTAMOUNT", "[结汇金额] 可为空，否则应大于等于0 ");
						}else if(dofoexlo.getSettamount().compareTo(
								new BigDecimal("0.00")) > 0){
							haveSettAmount = true;
						}
					}
					// USEOFUNDS 资金用途 字符型，2
					// 非必填项，见国内外汇贷款资金用途代码表。如果“提款金额”或“结汇金额”>0，则必填。
					if(isNull(dofoexlo.getUseofunds())){
						if(haveWithAmount || haveSettAmount){
							map.put("USEOFUNDS", "“提款金额”或“结汇金额”>0时，资金用途必填 ");
						}
					}else if(!verifyDictionaryValue(USEOFUNDS, dofoexlo
							.getUseofunds())){
						String value = getKey(dofoexlo.getUseofunds(),
								USEOFUNDS);
						map.put("USEOFUNDS", "[资金用途] [" + value + "] 无效 ");
					}
					boolean haveRepayamount = false;
					boolean havePrepayamount = false;
					// PRINCURR 还本币种 字符型，3 非必填项，见币种代码表。如果“还本金额”>0，则必填。
					if(!isNull(dofoexlo.getPrincurr())
							&& !verifyDictionaryValue(CURRENCY, dofoexlo
									.getPrincurr())){
						String value = getKey(dofoexlo.getPrincurr(), CURRENCY);
						map.put("PRINCURR", "[还本币种] [" + value + "] 无效 ");
					}
					// REPAYAMOUNT 还本金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(dofoexlo.getRepayamount())){
						if(dofoexlo.getRepayamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("REPAYAMOUNT", "[还本金额] 可为空，否则应大于等于0 ");
						}else if(dofoexlo.getRepayamount().compareTo(
								new BigDecimal("0.00")) > 0){
							haveRepayamount = true;
						}
						if(isNull(dofoexlo.getPrincurr())){
							map.put("PRINCURR", "“还本金额”>0，还本币种必填 ");
						}
					}
					// PREPAYAMOUNT 购汇还本金额 数值型，22.2 非必填项，大于等于0。购汇还本金额<=还本金额
					if(!isNull(dofoexlo.getPrepayamount())){
						if(dofoexlo.getPrepayamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("PREPAYAMOUNT", "[购汇还本金额] 可为空，否则应大于等于0 ");
						}else if(!isNull(dofoexlo.getRepayamount())
								&& dofoexlo.getPrepayamount().compareTo(
										dofoexlo.getRepayamount()) > 0){
							map.put("PREPAYAMOUNT", "[购汇还本金额] 应小于等于还本金额 ");
						}
						if(dofoexlo.getPrepayamount().compareTo(
								new BigDecimal("0.00")) > 0){
							havePrepayamount = true;
						}
						if(isNull(dofoexlo.getPrincurr())){
							map.put("PRINCURR", "“购汇还本金额”>0，还本币种必填 ");
						}
					}
					// INPAYCURR 付息币种 字符型，3 非必填项，见币种代码表。如果“付息金额”或“购汇付息金额”>0，则必填。
					if(!isNull(dofoexlo.getInpaycurr())
							&& !verifyDictionaryValue(CURRENCY, dofoexlo
									.getInpaycurr())){
						String value = getKey(dofoexlo.getInpaycurr(), CURRENCY);
						map.put("INPAYCURR", "[付息币种] [" + value + "] 无效 ");
					}
					boolean haveInpayamount = false;
					boolean havePinpayamount = false;
					// INPAYAMOUNT 付息金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(dofoexlo.getInpayamount())){
						if(dofoexlo.getInpayamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("INPAYAMOUNT", "[付息金额] 可为空，否则应大于等于0 ");
						}else if(dofoexlo.getInpayamount().compareTo(
								new BigDecimal("0.00")) > 0){
							haveInpayamount = true;
						}
					}
					// PINPAYAMOUNT 购汇付息金额 数值型，22.2 非必填项，大于等于0。购汇付息金额<=付息金额
					if(!isNull(dofoexlo.getPinpayamount())){
						if(dofoexlo.getPinpayamount().compareTo(
								new BigDecimal("0.00")) < 0){
							map.put("PINPAYAMOUNT", "[购汇付息金额] 可为空，否则应大于等于0 ");
						}else if(dofoexlo.getPinpayamount().compareTo(
								new BigDecimal("0.00")) > 0){
							havePinpayamount = true;
						}
						if(!isNull(dofoexlo.getPinpayamount())
								&& !isNull(dofoexlo.getInpayamount())
								&& dofoexlo.getPinpayamount().compareTo(
										dofoexlo.getInpayamount()) > 0){
							map.put("PINPAYAMOUNT", "[购汇付息金额] 应小于等于付息金额 ");
						}
					}
					if(haveInpayamount || havePinpayamount){
						if(isNull(dofoexlo.getInpaycurr())){
							map.put("INPAYCURR",
									"[付息币种] “付息金额”或“购汇付息金额”>0，付息币种必填 ");
						}
					}
					// 特殊校验
					if(configMap != null){
						String checkLoanopenbalan = (String) configMap
								.get("config.check.CB.loanopenbalan");
						String checkMustInputChangeMoney = (String) configMap
								.get("config.check.CB.mustInputChangeMoney");
						String checkUseofundsMustNull = (String) configMap
								.get("config.check.CB.useofunds.mustnull");
						String checkMoneyLogicCalculation = (String) configMap
								.get("config.check.CB.moneyLogicCalculation");
						String checkLoanopenbalanMust0 = (String) configMap
								.get("config.check.CB.loanopenbalan.must0");
						// 第一期期初需等于签约金额
						if("yes".equalsIgnoreCase(checkLoanopenbalan)){
							// 不存在上期变动信息时，当前为第一期变动信息
							if(CollectionUtil.isEmpty(changeList)){
								if(!isNull(contractDofoexl.getContractamount())
										&& !isNull(dofoexlo.getLoanopenbalan())
										&& contractDofoexl
												.getContractamount()
												.compareTo(
														dofoexlo
																.getLoanopenbalan()) != 0){
									map.put("LOANOPENBALAN",
											"[期初余额] 应等于签约信息中签约金额 ");
								}
							}
						}
						// 提款金额、还本金额、付息金额必填其一
						if("yes".equalsIgnoreCase(checkMustInputChangeMoney)){
							if(isNull(dofoexlo.getWithamount())
									&& isNull(dofoexlo.getRepayamount())
									&& isNull(dofoexlo.getInpayamount())){
								map.put("WITHAMOUNT",
										"[提款金额]、[还本金额]、[付息金额]中至少有一个不为空 ");
								map.put("REPAYAMOUNT",
										"[提款金额]、[还本金额]、[付息金额]中至少有一个不为空 ");
								map.put("INPAYAMOUNT",
										"[提款金额]、[还本金额]、[付息金额]中至少有一个不为空 ");
							}
						}
						// 当提款、结汇金额都不大于0时，资金用途需置空
						if("yes".equalsIgnoreCase(checkUseofundsMustNull)){
							if(!isNull(dofoexlo.getUseofunds())
									&& !haveWithAmount
									&& !haveSettAmount
									&& (haveRepayamount || havePrepayamount
											|| haveInpayamount || havePinpayamount)){
								map
										.put("USEOFUNDS",
												"[资金用途] 仅当提款或结汇金额都大于0时可选择");
							}
						}
						// 当币种一致时，期初余额+提款金额—还本金额=期末余额
						if("yes".equalsIgnoreCase(checkMoneyLogicCalculation)){
							if(!isNull(dofoexlo.getLoanopenbalan())
									&& (!isNull(dofoexlo.getWithamount()) || !isNull(dofoexlo
											.getRepayamount()))
									&& !isNull(dofoexlo.getEndbalan())){
								boolean sameCurrence = true;// 币种是否一致
								BigDecimal bd1 = dofoexlo.getLoanopenbalan();
								BigDecimal bd2 = dofoexlo.getEndbalan();
								// 判断是否填写提款金额
								if(!isNull(dofoexlo.getWithamount())){
									// 判断签约币种是否和提款币种一致
									if(contractDofoexl.getCurrence().equals(
											dofoexlo.getWithcurrence())){
										bd1 = dofoexlo.getLoanopenbalan().add(
												dofoexlo.getWithamount());
									}else{
										sameCurrence = false;
									}
								}
								// 判断是否填写还款金额
								if(!isNull(dofoexlo.getRepayamount())){
									// 判断签约币种是否和还款币种一致
									if(contractDofoexl.getCurrence().equals(
											dofoexlo.getPrincurr())){
										bd2 = dofoexlo.getEndbalan().add(
												dofoexlo.getRepayamount());
									}else{
										sameCurrence = false;
									}
								}
								if(sameCurrence && bd1.compareTo(bd2) != 0){
									map.put("ENDBALAN",
											"[期末余额]应等于 期初余额+提款金额—还本金额 ");
								}
							}
						}
						// 变动为提款时，是否限制期初余额必须为0
						if("yes".equalsIgnoreCase(checkLoanopenbalanMust0)){
							if(!isNull(dofoexlo.getWithamount())
									&& dofoexlo.getWithamount().compareTo(
											new BigDecimal(0)) != 0
									&& dofoexlo.getLoanopenbalan().compareTo(
											new BigDecimal(0)) != 0){
								map.put("LOANOPENBALAN",
										"[期初余额] 变动为提款时，期初余额必须为0 ");
							}
						}
					}
				}
				// 业务编号校验
				// 签约不能重复
				if("CA".equals(dofoexlo.getFiletype())
						&& checkBusinessNoRepeat(dofoexlo.getBusinessno(),
								"T_CFA_C_DOFOEXLO", dofoexlo.getFiletype(),
								dofoexlo.getBusinessid())){
					map.put("BUSINESSNO", "[业务编号] 国内外汇贷款签约信息中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(dofoexlo.getBusinessno())){
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
