package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
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
import com.cjit.gjsz.logic.model.Self_F_STRDE;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_F_STRDE]银行自身外债－商业银行人民币结构性存款业务校验类
 */
public class Self_F_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_F_DataVerify(){
	}

	public Self_F_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_F_STRDE strde = (Self_F_STRDE) verifylList.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				Self_F_STRDE contractStrde = null;
				// ACTIONTYPE 操作类型
				if(!verifyDictionaryValue(ACTIONTYPE, strde.getActiontype())){
					String value = getKey(strde.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D.equalsIgnoreCase(strde.getActiontype())){
					if(StringUtil.isEmpty(strde.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(strde.getActiontype())){
					if(!isNull(strde.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 人民币结构性存款编号
				if(StringUtil.isNotEmpty(strde.getStrdecode())){
					String vErrInfo = verifyRptNo(strde.getStrdecode(), strde
							.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("STRDECODE", "[人民币结构性存款编号] " + vErrInfo);
					}
				}
				// BRANCHCODE 金融机构标识码 字符型，12 必填项，商业银行金融机构标识码。
				if(isNull(strde.getBranchcode())
						|| strde.getBranchcode().length() != 12){
					map.put("BRANCHCODE", "[金融机构标识码] 不能为空且应为12位金融机构标识码 ");
				}else{
					String strDistrictCode = strde.getBranchcode().substring(0,
							6);
					if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode)){
						map.put("BRANCHCODE", "[金融机构标识码] 前6位数字地区标识码有误 ");
					}
					// 行内校验
					if(limitBranchCode(strde.getBranchcode(), strde
							.getFiletype(), strde.getBusinessid(), strde
							.getInstcode())){
						map.put("BRANCHCODE", "[金融机构标识码] 与当前记录所属机构对应的申报号码不匹配 ");
					}else if(checkBranchCode(strde.getBranchcode())){
						map.put("BRANCHCODE",
								"[金融机构标识码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
					}
				}
				// 签约信息校验
				if("FA".equals(strde.getFiletype())){
					if(ACTIONTYPE_D.equals(strde.getActiontype())){
						if(!verifyCannotDelete("T_CFA_F_STRDE", strde
								.getFiletype(), strde.getStrdecode())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该人民币结构性存款编号 ["
									+ strde.getStrdecode()
									+ "] 下的终止信息或利息给付信息，本信息不可删除 ");
						}
					}
					// CONTRACTDATE 签约日期 日期型，8 必填项，格式YYYYMMDD，小于等于当前日期。
					if(isNull(strde.getContractdate())){
						map.put("CONTRACTDATE", "[签约日期] 不能为空 ");
					}else if(!verifyTwoDates(strde.getContractdate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("CONTRACTDATE", "[签约日期] 不能晚于当前日期 ");
					}
					// CONTRACTAMOUNT 签约金额 数值型，22.2 必填项，大于等于0。
					if(isNull(strde.getContractamount())){
						map.put("CONTRACTAMOUNT", "[签约金额] 不能为空 ");
					}else if(strde.getContractamount().compareTo(
							new BigDecimal(0.0)) < 0){
						map.put("CONTRACTAMOUNT", "[签约金额] 应大于等于0 ");
					}
					// MATURITY 到期日 日期型,8 必填项，格式YYYYMMDD，大于等于签约日期。
					if(isNull(strde.getMaturity())){
						map.put("MATURITY", "[到期日] 不能为空 ");
					}else if(!isNull(strde.getContractdate())
							&& !verifyTwoDates(strde.getContractdate(), strde
									.getMaturity())){
						map.put("MATURITY", "[到期日] 不能早于签约日期 ");
					}
					// CLIENTNAME 客户名称 字符型，128
					// 必填项，由组织机构代码映射带出，若为外汇局规定的统一编码999999999，则映射显示为“个人”
					if(isNull(strde.getClientname())){
						map.put("CLIENTNAME", "[客户名称] 不能为空 ");
					}else{
						if("999999999".equalsIgnoreCase(strde.getClientcode())
								&& !"个人"
										.equalsIgnoreCase(strde.getClientname())){
							map.put("CLIENTNAME",
									"[客户名称] 客户代码为“999999999”时，客户名称应为“个人” ");
						}
					}
					// AGINRAUP 约定的利率上限 数值型，13.8
					// 必填项，按小数填写，如利率为3.21%，则填写0.0321。可能小于0
					// AGINRALO 约定的利率下限 数值型，13.8
					// 必填项，按小数填写，如利率为3.21%，则填写0.0321。可能小于0
					if(!isNull(strde.getAginraup())
							&& !isNull(strde.getAginralo())){
						if(strde.getAginraup().compareTo(strde.getAginralo()) < 0){
							map.put("AGINRAUP", "[约定的利率上限] 不能小于约定的利率下限 ");
						}
					}else{
						if(isNull(strde.getAginraup())){
							map.put("AGINRAUP", "[约定的利率上限] 不能为空 ");
						}
						if(isNull(strde.getAginralo())){
							map.put("AGINRALO", "[约定的利率下限] 不能为空 ");
						}
					}
					if(isNull(strde.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(strde.getTradedate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
				}else if("FB".equals(strde.getFiletype())
						|| "FC".equals(strde.getFiletype())){
					contractStrde = (Self_F_STRDE) service.getDataVerifyModel(
							"T_CFA_F_STRDE", strde.getStrdecode(), strde
									.getBusinessno());
					if(contractStrde == null){
						map.put("STRDECODE", "未发现此人民币结构性存款编号对应的签约信息 ");
						continue;
					}else if(ACTIONTYPE_A.equals(strde.getActiontype())
							&& contractStrde.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("STRDECODE", "对应签约信息尚未校验通过");
						continue;
					}else if(ACTIONTYPE_D.equals(contractStrde.getActiontype())
							&& contractStrde.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("STRDECODE", "对应签约信息已报送删除");
						continue;
					}
					// 终止信息校验
					if("FB".equals(strde.getFiletype())){
						if(checkByeRptNoRepeat(strde.getFiletype(), strde
								.getTerpaycode(), strde.getBusinessno())){
							map.put("TERPAYCODE", "[终止支付编号] 已经存在 ");
							continue;
						}
						if(checkFormerByeRptNo(strde.getFiletype(), strde
								.getTerpaycode(), strde.getBusinessno())){
							map.put("TERPAYCODE", "[终止支付编号] 存在尚未审核通过的往期记录 ");
							continue;
						}
						// TERDATE 终止日期 日期型，8 必填项，格式YYYYMMDD。
						if(isNull(strde.getTerdate())){
							map.put("TERDATE", "[终止日期] 不能为空 ");
						}else if(!verifyTwoDates(contractStrde
								.getContractdate(), strde.getTerdate())){
							map.put("TERDATE", "[终止日期] 不能早于签约日期 ");
						}
						// TERPAYAMTORMB 终止支付金额合计折人民币 数值型，22.2 必填项，大于等于0。
						if(isNull(strde.getTerpayamtormb())){
							map.put("TERPAYAMTORMB", "[终止支付金额合计折人民币] 不能为空 ");
						}else if(strde.getTerpayamtormb().compareTo(
								new BigDecimal(0.0)) < 0){
							map.put("TERPAYAMTORMB", "[终止支付金额合计折人民币] 应大于等于0 ");
						}
						// TERRMBPAYAM 终止人民币支付金额 数值型，22.2
						// 非必填项，大于等于0，终止人民币支付金额与终止外币支付金额至少填一个。
						if(!isNull(strde.getTerrmbpayam())
								&& strde.getTerrmbpayam().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("TERRMBPAYAM", "[终止人民币支付金额] 应大于等于0 ");
						}
						if(isNull(strde.getTerrmbpayam())
								&& isNull(strde.getTerpaycurram())){
							map.put("TERRMBPAYAM",
									"[终止人民币支付金额] 与终止外币支付金额至少填一个 ");
							map.put("TERPAYCURRAM",
									"[终止外币支付币种] 与终止人民币支付金额至少填一个 ");
						}
						// TERPAYCURR 终止外币支付币种 字符型，3
						// 非必填项，见币种代码表。终止外币支付币种和终止外币支付金额为一组数据，两者同时为空或者不为空。
						if(!isNull(strde.getTerpaycurr())){
							if(!verifyDictionaryValue(CURRENCY, strde
									.getTerpaycurr())){
								String value = getKey(strde.getTerpaycurr(),
										CURRENCY);
								map.put("TERPAYCURR", "[终止外币支付币种] [" + value
										+ "] 无效 ");
							}
							if(isNull(strde.getTerpaycurram())){
								map
										.put("TERPAYCURRAM",
												"[终止外币支付币种]和[终止外币支付金额] 为一组数据，两者同时为空或者不为空 ");
								map
										.put("TERPAYCURR",
												"[终止外币支付币种]和[终止外币支付金额] 为一组数据，两者同时为空或者不为空 ");
							}
						}else{
							if(!isNull(strde.getTerpaycurram())){
								map
										.put("TERPAYCURRAM",
												"[终止外币支付币种]和[终止外币支付金额] 为一组数据，两者同时为空或者不为空 ");
								map
										.put("TERPAYCURR",
												"[终止外币支付币种]和[终止外币支付金额] 为一组数据，两者同时为空或者不为空 ");
							}
						}
						// TERPAYCURRAM 终止外币支付金额 数值型，22.2 非必填项，大于等于0。
						if(!isNull(strde.getTerpaycurram())
								&& strde.getTerpaycurram().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("TERPAYCURRAM", "[终止外币支付金额] 应大于等于0 ");
						}
					}
					// 利息给付信息校验
					else if("FC".equals(strde.getFiletype())){
						if(checkByeRptNoRepeat(strde.getFiletype(), strde
								.getInpaycode(), strde.getBusinessno())){
							map.put("INPAYCODE", "[付息编号] 已经存在 ");
							continue;
						}
						if(checkFormerByeRptNo(strde.getFiletype(), strde
								.getInpaycode(), strde.getBusinessno())){
							map.put("INPAYCODE", "[付息编号] 存在尚未审核通过的往期记录 ");
							continue;
						}
						// INPAYMONTH 付息年月 数值型，6 必填项，格式为YYYYMM。
						if(isNull(strde.getInpaymonth())
								|| strde.getInpaymonth().length() != 6){
							map.put("INPAYMONTH", "[付息年月] 不能为空，格式为YYYYMM ");
						}else{
							Date date = DateUtils.stringToDate(strde
									.getInpaymonth(), "yyyyMM");
							if(date == null){
								map.put("INPAYMONTH", "[付息年月] 格式不正确，应为YYYYMM ");
							}else if(!strde.getInpaymonth().startsWith("19")
									&& !strde.getInpaymonth().startsWith("20")
									&& !strde.getInpaymonth().startsWith("21")){
								map.put("INPAYMONTH", "[付息年月] 格式不正确，应为YYYYMM ");
							}else{
								String month = strde.getInpaymonth().substring(
										4, 6);
								if(Integer.valueOf(month).intValue() > 12
										|| Integer.valueOf(month).intValue() == 0){
									map.put("INPAYMONTH",
											"[付息年月] 格式不正确，应为YYYYMM ");
								}
							}
						}
						// INPAYRMBAM 付息人民币支付金额 数值型，22.2
						// 非必填项，大于等于0。付息人民币支付金额与付息外币支付金额至少填一个。
						if(!isNull(strde.getInpayrmbam())
								&& strde.getInpayrmbam().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("INPAYRMBAM", "[付息人民币支付金额] 应大于等于0 ");
						}
						if(isNull(strde.getInpayrmbam())
								&& isNull(strde.getInpaycurram())){
							map
									.put("INPAYRMBAM",
											"[付息人民币支付金额] 与付息外币支付金额至少填一个 ");
							map.put("INPAYCURRAM",
									"[付息外币支付金额] 与付息人民币支付金额至少填一个 ");
						}
						// INPAYCURR 付息外币支付币种 字符型，3
						// 非必填项。付息外币支付币种和付息外币支付金额为一组数据，两者同时为空或者不为空。
						if(!isNull(strde.getInpaycurr())){
							if(!verifyDictionaryValue(CURRENCY, strde
									.getInpaycurr())){
								String value = getKey(strde.getInpaycurr(),
										CURRENCY);
								map.put("INPAYCURR", "[付息外币支付币种] [" + value
										+ "] 无效 ");
							}
							if(isNull(strde.getInpaycurram())){
								map
										.put("INPAYCURRAM",
												"[付息外币支付币种]和[付息外币支付金额] 为一组数据，两者同时为空或者不为空 ");
								map
										.put("INPAYCURR",
												"[付息外币支付币种]和[付息外币支付金额] 为一组数据，两者同时为空或者不为空 ");
							}
						}else{
							if(!isNull(strde.getInpaycurram())){
								map
										.put("INPAYCURRAM",
												"[付息外币支付币种]和[付息外币支付金额] 为一组数据，两者同时为空或者不为空 ");
								map
										.put("INPAYCURR",
												"[付息外币支付币种]和[付息外币支付金额] 为一组数据，两者同时为空或者不为空 ");
							}
						}
						// INPAYCURRAM 付息外币支付金额 数值型，22.2 非必填项，大于等于0。
						if(!isNull(strde.getInpaycurram())
								&& strde.getInpaycurram().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("INPAYCURRAM", "[付息外币支付金额] 应大于等于0 ");
						}
					}
				}
				// 资金流出入和结购汇信息校验
				else if("FD".equals(strde.getFiletype())){
					// BUOCMONTH 报告期 数值型，6 必填项，格式为YYYYMM。
					if(isNull(strde.getBuocmonth())
							|| strde.getBuocmonth().length() != 6){
						map.put("BUOCMONTH", "[报告期] 不能为空，格式为YYYYMM ");
					}else{
						Date date = DateUtils.stringToDate(
								strde.getBuocmonth(), "yyyyMM");
						if(date == null){
							map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
						}else if(!strde.getBuocmonth().startsWith("19")
								&& !strde.getBuocmonth().startsWith("20")
								&& !strde.getBuocmonth().startsWith("21")){
							map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
						}else{
							String month = strde.getBuocmonth().substring(4, 6);
							if(Integer.valueOf(month).intValue() > 12
									|| Integer.valueOf(month).intValue() == 0){
								map.put("BUOCMONTH", "[报告期] 格式不正确，应为YYYYMM ");
							}
						}
					}
					// CURRENCY 币种 字符型，3 必填项，默认美元。
					if(isNull(strde.getCurrency())){
						map.put("CURRENCY", "[币种] 不能为空 ");
					}else if(!verifyDictionaryValue(CURRENCY, strde
							.getCurrency())){
						String value = getKey(strde.getCurrency(), CURRENCY);
						map.put("CURRENCY", "[币种] [" + value + "] 无效 ");
					}
					// MOEXAMUSD 本月汇出金额折美元 数值型，22.2 非必填项，大于等于0
					if(!isNull(strde.getMoexamusd())
							&& strde.getMoexamusd().compareTo(
									new BigDecimal(0.0)) < 0){
						map.put("MOEXAMUSD", "[本月汇出金额折美元] 应大于等于0 ");
					}
					// MOAMREUSD 本月汇入金额折美元 数值型，22.2 非必填项，大于等于0
					if(!isNull(strde.getMoamreusd())
							&& strde.getMoamreusd().compareTo(
									new BigDecimal(0.0)) < 0){
						map.put("MOAMREUSD", "[本月汇入金额折美元] 应大于等于0 ");
					}
					// MOPFEXAMUSD 本月购汇金额折美元 数值型，22.2 非必填项，大于等于0
					if(!isNull(strde.getMopfexamusd())
							&& strde.getMopfexamusd().compareTo(
									new BigDecimal(0.0)) < 0){
						map.put("MOPFEXAMUSD", "[本月购汇金额折美元] 应大于等于0 ");
					}
					// MOSETTAMUSD 本月结汇金额折美元 数值型，22.2 非必填项，大于等于0
					if(!isNull(strde.getMosettamusd())
							&& strde.getMosettamusd().compareTo(
									new BigDecimal(0.0)) < 0){
						map.put("MOSETTAMUSD", "[本月结汇金额折美元] 应大于等于0 ");
					}
					// 汇出、汇入、购汇、结汇金额折美元至少填一个
					if(isNull(strde.getMoexamusd())
							&& isNull(strde.getMoamreusd())
							&& isNull(strde.getMopfexamusd())
							&& isNull(strde.getMosettamusd())){
						map.put("MOEXAMUSD", "本月汇出、汇入、购汇、结汇金额折美元至少填一个 ");
						map.put("MOAMREUSD", "本月汇出、汇入、购汇、结汇金额折美元至少填一个 ");
						map.put("MOPFEXAMUSD", "本月汇出、汇入、购汇、结汇金额折美元至少填一个 ");
						map.put("MOSETTAMUSD", "本月汇出、汇入、购汇、结汇金额折美元至少填一个 ");
					}
				}
				// 业务编号校验
				// 不能重复
				if(!"FB".equals(strde.getFiletype())
						&& !"FC".equals(strde.getFiletype())
						&& checkBusinessNoRepeat(strde.getBusinessno(),
								"T_CFA_F_STRDE", strde.getFiletype(), strde
										.getBusinessid())){
					map
							.put("BUSINESSNO",
									"[业务编号] 在商业银行人民币结构性存款业务的 签约信息 或 资金流出入和结购汇信息 中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(strde.getBusinessno())){
					map.put("BUSINESSNO", "[业务编号] 不能为空 ");
				}
				// 特殊校验
				if(configMap != null){
					String checkBranchCodeSameAsFA = (String) configMap
							.get("config.check.FBFC.branchCode.sameFA");
					String checkContractSameAsFA = (String) configMap
							.get("config.check.FBFC.contract.sameFA");
					String checkTerpayamtormbEqualContractamount = (String) configMap
							.get("config.check.FB.terpayamtormb.equalContractamount");
					String checkInpaymonthEarlyMaturity = (String) configMap
							.get("config.check.FC.inpaymonth.earlyMaturity");
					// 终止和利息给付信息中金融机构标识码需与签约信息中相同
					if("yes".equalsIgnoreCase(checkBranchCodeSameAsFA)){
						if("FB".equals(strde.getFiletype())
								|| "FC".equals(strde.getFiletype())){
							if(!isNull(strde.getBranchcode())
									&& contractStrde != null
									&& !isNull(contractStrde.getBranchcode())
									&& !strde.getBranchcode().equals(
											contractStrde.getBranchcode())){
								map.put("BRANCHCODE",
										"[金融机构标识码] 应与签约信息中金融机构标识码一致 ");
							}
						}
					}
					// 终止和利息给付信息中合同号需与签约信息中相同
					if("yes".equalsIgnoreCase(checkContractSameAsFA)){
						if("FB".equals(strde.getFiletype())
								|| "FC".equals(strde.getFiletype())){
							if(!isNull(strde.getContract())
									&& contractStrde != null
									&& !isNull(contractStrde.getContract())
									&& !strde.getContract().equals(
											contractStrde.getContract())){
								map.put("CONTRACT", "[合同号] 应与签约信息中合同号一致 ");
							}
						}
					}
					// 终止信息中终止支付金额合计折人民币等于签约信息中签约金额
					if("yes"
							.equalsIgnoreCase(checkTerpayamtormbEqualContractamount)){
						if("FB".equals(strde.getFiletype())){
							if(!isNull(strde.getTerpayamtormb())
									&& contractStrde != null
									&& !isNull(contractStrde
											.getContractamount())
									&& strde.getTerpayamtormb().compareTo(
											contractStrde.getContractamount()) != 0){
								map.put("TERPAYAMTORMB",
										"[终止支付金额合计折人民币] 应等于签约信息中签约金额 ");
							}
						}
					}
					// 利息给付信息中付息年月需早于签约信息中到期日
					if("yes".equalsIgnoreCase(checkInpaymonthEarlyMaturity)){
						if("FC".equals(strde.getFiletype())){
							if(!isNull(strde.getInpaymonth())
									&& strde.getInpaymonth().length() == 6
									&& contractStrde != null
									&& !isNull(contractStrde.getMaturity())){
								if(!verifyTwoDates(
										strde.getInpaymonth() + "01",
										contractStrde.getMaturity())){
									map.put("INPAYMONTH",
											"[付息年月] 不能晚于签约信息到期日[ "
													+ contractStrde
															.getMaturity()
													+ "]");
								}
							}
						}
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
