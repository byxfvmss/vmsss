package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.cjit.gjsz.logic.model.Self_D_LOUNEXGU;
import com.cjit.gjsz.logic.model.Self_Sub_FOGUARANTOR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_D_LOUNEXGU]银行自身外债－境外担保项下境内贷款校验类
 */
public class Self_D_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_D_DataVerify(){
	}

	public Self_D_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_D_LOUNEXGU lounexgu = (Self_D_LOUNEXGU) verifylList.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				if(!verifyDictionaryValue(ACTIONTYPE, lounexgu.getActiontype())){
					String value = getKey(lounexgu.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D
						.equalsIgnoreCase(lounexgu.getActiontype())){
					if(StringUtil.isEmpty(lounexgu.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(lounexgu
						.getActiontype())){
					if(!isNull(lounexgu.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 外保内贷编号
				if(StringUtil.isNotEmpty(lounexgu.getLounexgucode())){
					String vErrInfo = verifyRptNo(lounexgu.getLounexgucode(),
							lounexgu.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("LOUNEXGUCODE", "[外保内贷编号] " + vErrInfo);
					}
				}
				// 签约信息校验
				if("DA".equals(lounexgu.getFiletype())){
					if(ACTIONTYPE_D.equals(lounexgu.getActiontype())){
						if(!verifyCannotDelete("T_CFA_D_LOUNEXGU", lounexgu
								.getFiletype(), lounexgu.getLounexgucode())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该外保内贷编号 ["
									+ lounexgu.getLounexgucode()
									+ "] 下的变动及履约信息，本信息不可删除 ");
						}
					}
					// CREDITORCODE 债权人代码 字符型，12 必填项，金融机构标识码。
					if(isNull(lounexgu.getCreditorcode())
							|| lounexgu.getCreditorcode().length() != 12){
						map.put("CREDITORCODE", "[债权人代码] 不能为空且应为12位金融机构标识码 ");
					}else{
						String strDistrictCode = lounexgu.getCreditorcode()
								.substring(0, 6);
						if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode)){
							map.put("CREDITORCODE", "[债权人代码] 前6位数字地区标识码有误 ");
						}
						// 行内校验
						if(limitBranchCode(lounexgu.getCreditorcode(), lounexgu
								.getFiletype(), lounexgu.getBusinessid(),
								lounexgu.getInstcode())){
							map.put("CREDITORCODE",
									"[债权人代码] 与当前记录所属机构对应的申报号码不匹配 ");
						}else if(checkBranchCode(lounexgu.getCreditorcode())){
							map.put("CREDITORCODE",
									"[债权人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
						}
					}
					// DEBTORCODE 债务人代码 字符型，9 必填项，组织机构代码。
					if(isNull(lounexgu.getDebtorcode())
							|| lounexgu.getDebtorcode().length() != 9){
						map.put("DEBTORCODE", "[债务人代码] 不能为空且应为9位组织机构代码 ");
					}
					// DEBTORTYPE 债务人类型 字符型，8 必填项，见境内主体类型代码表。
					if(isNull(lounexgu.getDebtortype())){
						map.put("DEBTORTYPE", "[债务人类型] 不能为空且应在字典表中存在 ");
					}else if(!verifyDictionaryValue(MAINBODYTYPE, lounexgu
							.getDebtortype())){
						String value = getKey(lounexgu.getDebtortype(),
								MAINBODYTYPE);
						map.put("DEBTORTYPE", "[债务人类型] [" + value + "] 无效. ");
					}
					// CFEOGUDAD 中资企业境外担保项下贷款业务批准文件号 字符型，128
					// 非必填项，债务人类型为“中资企业”时，为必填项。
					if(isNull(lounexgu.getCfeogudad())
							&& !isNull(lounexgu.getDebtortype())
							&& lounexgu.getDebtortype().startsWith("101105")){
						map.put("CFEOGUDAD",
								"[中资企业境外担保项下贷款业务批准文件号] 债务人类型为“中资企业”时不能为空 ");
					}
					// CFEOGUDCURR 中资企业境外担保项下境内贷款额度币种 字符型，3
					// 非必填项，见币种代码表。债务人类型为“中资企业”时，为必填项。
					if(isNull(lounexgu.getCfeogudcurr())){
						if(!isNull(lounexgu.getDebtortype())
								&& lounexgu.getDebtortype()
										.startsWith("101105")){
							map.put("CFEOGUDCURR",
									"[中资企业境外担保项下境内贷款额度币种] 债务人类型为“中资企业”时不能为空 ");
						}
					}else{
						if(!verifyDictionaryValue(CURRENCY, lounexgu
								.getCfeogudcurr())){
							String value = getKey(lounexgu.getCfeogudcurr(),
									CURRENCY);
							map.put("CFEOGUDCURR", "[中资企业境外担保项下境内贷款额度币种] ["
									+ value + "] 无效. ");
						}
					}
					// CFEOGUDAMOUNT 中资企业境外担保项下境内贷款额度金额 数值型，22.2
					// 非必填项，大于等于0。债务人类型为“中资企业”时，为必填项。
					if(isNull(lounexgu.getCfeogudamount())){
						if(!isNull(lounexgu.getDebtortype())
								&& lounexgu.getDebtortype()
										.startsWith("101105")){
							map.put("CFEOGUDAMOUNT",
									"[中资企业境外担保项下境内贷款额度金额] 债务人类型为“中资企业”时不能为空 ");
						}
					}else if(lounexgu.getCfeogudamount().compareTo(
							new BigDecimal("0.00")) < 0){
						map
								.put("CFEOGUDAMOUNT",
										"[中资企业境外担保项下境内贷款额度金额] 应大于等于0 ");
					}
					// CREDCURRCODE 贷款币种 字符型，3 必填项，见币种代码表
					if(isNull(lounexgu.getCredcurrcode())){
						map.put("CREDCURRCODE", "[贷款币种] 不能为空. ");
					}else if(!verifyDictionaryValue(CURRENCY, lounexgu
							.getCredcurrcode())){
						String value = getKey(lounexgu.getCredcurrcode(),
								CURRENCY);
						map.put("CREDCURRCODE", "[贷款币种] [" + value + "] 无效. ");
					}
					// CREDCONAMOUNT 贷款签约金额 数值型，22.2 必填项，大于等于0。
					if(isNull(lounexgu.getCredconamount())
							|| lounexgu.getCredconamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("CREDCONAMOUNT", "[贷款签约金额] 不能为空且应大于等于0 ");
					}
					// MATURITY 到期日 日期型,8 必填项，格式YYYYMMDD，大于等于起息日。
					if(lounexgu.getMaturity() != null
							&& lounexgu.getValuedate() != null){
						if(!verifyTwoDates(lounexgu.getValuedate(), lounexgu
								.getMaturity())){
							map.put("MATURITY", "[到期日] 不能早于起息日 ");
						}
					}
					// 境外担保人信息
					List subList = service.getCfaChildren(
							"T_CFA_SUB_FOGUARANTOR_INFO", lounexgu
									.getBusinessid());
					List list = new ArrayList();
					if(CollectionUtil.isNotEmpty(subList)){
						Self_Sub_FoguarantorVerify subVerify = null;
						for(int j = 0; j < subList.size(); j++){
							Self_Sub_FOGUARANTOR sub = (Self_Sub_FOGUARANTOR) subList
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_FoguarantorVerify(
									dictionarys, tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_FOGUARANTOR_INFO");
								list.add(vm.getFatcher());
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							verifyModel.setChildren(list);
						}
					}else{
						map
								.put("T_CFA_SUB_FOGUARANTOR_INFO",
										"[境外担保人信息] 不能为空 ");
					}
					if(isNull(lounexgu.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(lounexgu.getTradedate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
					// 特殊校验
					if(configMap != null){
						String checkCredconAmountIntJPY = (String) configMap
								.get("config.check.DAEA.credconamount.intJPY");
						String checkDofoexloCodeNotNull = (String) configMap
								.get("config.check.DA.dofoexloCode.notNull");
						// 当贷款币种为JPY时金额不能有小数
						if("yes".equalsIgnoreCase(checkCredconAmountIntJPY)){
							if("JPY".equals(lounexgu.getCredcurrcode())
									&& !isNull(lounexgu.getCredconamount())
									&& lounexgu.getCredconamount()
											.doubleValue() > Math
											.floor(lounexgu.getCredconamount()
													.doubleValue())){
								map.put("CREDCONAMOUNT",
										"[贷款签约金额] 不能是含有小数的JPY日元金额 ");
							}
						}
						// DOFOEXLOCODE 国内外汇贷款编号 字符型，32 非必填项，贷款币种不是“CNY”时，必填。
						if("yes".equalsIgnoreCase(checkDofoexloCodeNotNull)){
							if(!"CNY".equalsIgnoreCase(lounexgu
									.getCredcurrcode())
									&& isNull(lounexgu.getDofoexlocode())){
								map
										.put("DOFOEXLOCODE",
												"[国内外汇贷款编号] 贷款币种不是“CNY”时，国内外汇贷款编号不能为空. ");
							}
						}
					}
				}
				// 若当前校验报文为变动及履约信息，则需查询对应上级签约信息
				else if("DB".equals(lounexgu.getFiletype())){
					Self_D_LOUNEXGU contractLounexgu = (Self_D_LOUNEXGU) service
							.getDataVerifyModel("T_CFA_D_LOUNEXGU", lounexgu
									.getLounexgucode(), lounexgu
									.getBusinessno());
					if(contractLounexgu == null){
						map.put("LOUNEXGUCODE", "未发现此外保内贷编号对应的签约信息 ");
						continue;
					}
					if(ACTIONTYPE_A.equals(lounexgu.getActiontype())
							&& contractLounexgu.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("LOUNEXGUCODE", "对应签约信息尚未校验通过");
						continue;
					}
					if(ACTIONTYPE_D.equals(contractLounexgu.getActiontype())
							&& contractLounexgu.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("LOUNEXGUCODE", "对应签约信息已报送删除");
						continue;
					}
					if(checkByeRptNoRepeat(lounexgu.getFiletype(), lounexgu
							.getChangeno(), lounexgu.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 已经存在 ");
						continue;
					}
					if(checkFormerByeRptNo(lounexgu.getFiletype(), lounexgu
							.getChangeno(), lounexgu.getBusinessno())){
						map.put("CHANGENO", "[变动编号] 存在尚未审核通过的往期记录 ");
						continue;
					}
					if(isNull(lounexgu.getCredwithamount())
							&& isNull(lounexgu.getCredrepayamount())
							&& isNull(lounexgu.getPicamount())){
						map.put("CREDWITHAMOUNT",
								"[提款金额]、[还本金额]、[付息费金额]中至少有一个不为空 ");
						map.put("CREDREPAYAMOUNT",
								"[提款金额]、[还本金额]、[付息费金额]中至少有一个不为空 ");
						map.put("PICAMOUNT", "[提款金额]、[还本金额]、[付息费金额]中至少有一个不为空 ");
					}else{
						if(!isNull(lounexgu.getCredwithamount())
								&& lounexgu.getCredwithamount().compareTo(
										new BigDecimal("0.00")) < 0){
							map.put("CREDWITHAMOUNT", "[提款金额] 应大于等于0 ");
						}
						if(!isNull(lounexgu.getCredrepayamount())
								&& lounexgu.getCredrepayamount().compareTo(
										new BigDecimal("0.00")) < 0){
							map.put("CREDREPAYAMOUNT", "[还本金额] 应大于等于0 ");
						}
						if(!isNull(lounexgu.getPicamount())
								&& lounexgu.getPicamount().compareTo(
										new BigDecimal("0.00")) < 0){
							map.put("PICAMOUNT", "[付息费金额] 应大于等于0 ");
						}
					}
					if(isNull(lounexgu.getCredprinbala())
							|| lounexgu.getCredprinbala().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("CREDPRINBALA", "[贷款余额] 不能为空且应大于等于0 ");
					}
					if(!isNull(lounexgu.getGuperamount())
							&& lounexgu.getGuperamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("GUPERAMOUNT", "[担保履约金额] 应大于等于0 ");
					}
				}
				// 业务编号校验
				// 不能重复
				if("DA".equals(lounexgu.getFiletype())
						&& checkBusinessNoRepeat(lounexgu.getBusinessno(),
								"T_CFA_D_LOUNEXGU", lounexgu.getFiletype(),
								lounexgu.getBusinessid())){
					map.put("BUSINESSNO", "[业务编号] 境外担保项下境内贷款签约信息中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(lounexgu.getBusinessno())){
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
