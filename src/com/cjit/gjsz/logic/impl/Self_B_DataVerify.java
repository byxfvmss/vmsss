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
import com.cjit.gjsz.logic.model.Self_B_EXGUARAN;
import com.cjit.gjsz.logic.model.Self_Sub_BENEFICIARY;
import com.cjit.gjsz.logic.model.Self_Sub_GUARANTOR;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_B_EXGUARAN]银行自身外债－对外担保信息校验类
 */
public class Self_B_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_B_DataVerify(){
	}

	public Self_B_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_B_EXGUARAN exguaran = (Self_B_EXGUARAN) verifylList.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				if(!verifyDictionaryValue(ACTIONTYPE, exguaran.getActiontype())){
					String value = getKey(exguaran.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D
						.equalsIgnoreCase(exguaran.getActiontype())){
					if(StringUtil.isEmpty(exguaran.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(exguaran
						.getActiontype())){
					if(!isNull(exguaran.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 对外担保编号
				if(StringUtil.isNotEmpty(exguaran.getExguarancode())){
					String vErrInfo = verifyRptNo(exguaran.getExguarancode(),
							exguaran.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("EXGUARANCODE", "[对外担保编号] " + vErrInfo);
					}
				}
				// 签约信息校验
				if("BA".equals(exguaran.getFiletype())){
					if(ACTIONTYPE_D.equals(exguaran.getActiontype())){
						if(!verifyCannotDelete("T_CFA_B_EXGUARAN", exguaran
								.getFiletype(), exguaran.getExguarancode())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该对外担保编号 ["
									+ exguaran.getExguarancode()
									+ "] 下的责任余额或履约信息，本信息不可删除 ");
						}
					}
					// GUARANTORCODE 担保人代码 字符型，12 必填项，金融机构标识码。
					if(isNull(exguaran.getGuarantorcode())
							|| exguaran.getGuarantorcode().length() != 12){
						map.put("GUARANTORCODE", "[担保人代码] 不能为空且应为12位金融机构标识码 ");
					}else{
						String strDistrictCode = exguaran.getGuarantorcode()
								.substring(0, 6);
						if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode)){
							map.put("GUARANTORCODE", "[担保人代码] 前6位数字地区标识码有误 ");
						}
						// 行内校验
						if(limitBranchCode(exguaran.getGuarantorcode(),
								exguaran.getFiletype(), exguaran
										.getBusinessid(), exguaran
										.getInstcode())){
							map.put("GUARANTORCODE",
									"[担保人代码] 与当前记录所属机构对应的申报号码不匹配 ");
						}else if(checkBranchCode(exguaran.getGuarantorcode())){
							map.put("GUARANTORCODE",
									"[担保人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
						}
					}
					// CONTRACTDATE 签约日期 日期型，8 必填项，格式YYYYMMDD。
					if(isNull(exguaran.getContractdate())){
						map.put("CONTRACTDATE", "[签约日期] 不能为空且格式YYYYMMDD ");
					}else if(!verifyTwoDates(
							exguaran.getContractdate(),
							DateUtils
									.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("CONTRACTDATE", "[签约日期] 不能晚于当前日期 ");
					}
					// 保函金额
					if(isNull(exguaran.getGuaranamount())){
						map.put("GUARANAMOUNT", "[保函金额] 不能为空 ");
					}else if(exguaran.getGuaranamount().compareTo(
							new BigDecimal(0.0)) < 0){
						map.put("GUARANAMOUNT", "[保函金额] 应大于等于0 ");
					}
					// MATURITY 到期日 日期型,8 必填项，格式YYYYMMDD。
					if(isNull(exguaran.getMaturity())){
						map.put("MATURITY", "[到期日] 不能为空且格式YYYYMMDD ");
					}else if(!verifyTwoDates(exguaran.getContractdate(),
							exguaran.getMaturity())){
						map.put("MATURITY", "[到期日] 不能早于签约日期 ");
					}else if(!verifyTwoDates(DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT),
							exguaran.getMaturity())){
						// 为方便现场补录，且校验工具中未对到期日与当前日期做比较，故去掉此校验
						// map.put("MATURITY", "[到期日] 应晚于当前日期 ");
					}
					// 主债务金额
					if(exguaran.getMaindebtamount() != null
							&& exguaran.getMaindebtamount().compareTo(
									new BigDecimal(0.0)) < 0){
						map.put("MAINDEBTAMOUNT", "[主债务金额] 应大于等于0 ");
					}
					// 担保申请人中文名称/担保申请人英文名称
					if(isNull(exguaran.getGuappname())
							&& isNull(exguaran.getGuappnamen())){
						map.put("GUAPPNAME", "[担保申请人中文名称] 担保申请人中英文名称至少填写一个 ");
						map.put("GUAPPNAMEN", "[担保申请人英文名称] 担保申请人中英文名称至少填写一个 ");
					}
					// 子表信息校验
					List list = new ArrayList();
					// 受益人信息
					List sub1List = service.getCfaChildren(
							"T_CFA_SUB_BENEFICIARY_INFO", exguaran
									.getBusinessid());
					if(CollectionUtil.isNotEmpty(sub1List)){
						Self_Sub_BeneficiaryVerify subVerify = null;
						for(int j = 0; j < sub1List.size(); j++){
							Self_Sub_BENEFICIARY sub = (Self_Sub_BENEFICIARY) sub1List
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_BeneficiaryVerify(
									dictionarys, tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_BENEFICIARY_INFO");
								list.add(vm.getFatcher());
							}
						}
					}else{
						map.put("T_CFA_SUB_BENEFICIARY_INFO", "[受益人信息] 不能为空 ");
					}
					// 被担保人信息
					List sub2List = service.getCfaChildren(
							"T_CFA_SUB_GUARANTOR_INFO", exguaran
									.getBusinessid());
					if(CollectionUtil.isNotEmpty(sub2List)){
						Self_Sub_GuarantorVerify subVerify = null;
						for(int j = 0; j < sub2List.size(); j++){
							Self_Sub_GUARANTOR sub = (Self_Sub_GUARANTOR) sub2List
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_GuarantorVerify(
									dictionarys, tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_GUARANTOR_INFO");
								list.add(vm.getFatcher());
							}
						}
					}else{
						map.put("T_CFA_SUB_GUARANTOR_INFO", "[被担保人信息] 不能为空 ");
					}
					if(CollectionUtil.isNotEmpty(list)){
						verifyModel.setChildren(list);
					}
					if(isNull(exguaran.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(exguaran.getTradedate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
				}
				// 若当前校验报文为责任余额信息或履约信息，则需查询对应上级签约信息
				else if("BB".equals(exguaran.getFiletype())
						|| "BC".equals(exguaran.getFiletype())){
					Self_B_EXGUARAN contractExguaran = (Self_B_EXGUARAN) service
							.getDataVerifyModel("T_CFA_B_EXGUARAN", exguaran
									.getExguarancode(), exguaran
									.getBusinessno());
					if(contractExguaran == null){
						map.put("EXGUARANCODE", "未发现此对外担保编号对应的签约信息 ");
						continue;
					}else if(ACTIONTYPE_A.equals(exguaran.getActiontype())
							&& contractExguaran.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("EXGUARANCODE", "对应签约信息尚未校验通过");
						continue;
					}else if(ACTIONTYPE_D.equals(contractExguaran
							.getActiontype())
							&& contractExguaran.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("EXGUARANCODE", "对应签约信息已报送删除");
						continue;
					}
					if("BB".equals(exguaran.getFiletype())){
						// 对外担保－责任余额信息校验
						// 担保责任余额变动日期
						if(contractExguaran != null){
							if(StringUtil.isNotEmpty(contractExguaran
									.getContractdate())){
								if(!verifyTwoDates(contractExguaran
										.getContractdate(), exguaran
										.getWabachandate())){
									map.put("WABACHANDATE",
											"[担保责任余额变动日期] 不能早于签约日期 ["
													+ contractExguaran
															.getContractdate()
													+ "] ");
								}
							}else{
								if(!verifyTwoDates(contractExguaran
										.getTradedate(), exguaran
										.getWabachandate())){
									map.put("WABACHANDATE",
											"[担保责任余额变动日期] 不能早于签约信息交易日期 ["
													+ contractExguaran
															.getTradedate()
													+ "] ");
								}
							}
						}
						// BASERE 担保责任余额 数值型，22.2 必填项，0<=担保责任余额<=保函金额。
						if(isNull(exguaran.getBasere())){
							map.put("BASERE", "[担保责任余额] 不能为空 ");
						}else if(exguaran.getBasere().compareTo(
								new BigDecimal(0.0)) < 0){
							map.put("BASERE", "[担保责任余额] 应大于等于0 ");
						}else if(contractExguaran != null
								&& !isNull(contractExguaran.getGuaranamount())
								&& exguaran.getBasere().compareTo(
										contractExguaran.getGuaranamount()) > 0){
							map
									.put("BASERE", "[担保责任余额] 应小于等于保函金额 ["
											+ contractExguaran
													.getGuaranamount() + "] ");
						}
					}else if("BC".equals(exguaran.getFiletype())){
						// 对外担保－履约信息校验
						if(checkByeRptNoRepeat(exguaran.getFiletype(), exguaran
								.getComplianceno(), exguaran.getBusinessno())){
							map.put("COMPLIANCENO", "[履约编号] 已经存在 ");
							continue;
						}
						if(checkFormerByeRptNo(exguaran.getFiletype(), exguaran
								.getComplianceno(), exguaran.getBusinessno())){
							map.put("COMPLIANCENO", "[履约编号] 存在尚未审核通过的往期记录 ");
							continue;
						}
						// GUARANTORCODE 担保人代码 定长数字型，12 必填项，金融机构标识码。
						if(isNull(exguaran.getGuarantorcode())
								|| exguaran.getGuarantorcode().length() != 12){
							map.put("GUARANTORCODE",
									"[担保人代码] 不能为空且应为12位金融机构标识码 ");
						}else{
							String strDistrictCode = exguaran
									.getGuarantorcode().substring(0, 6);
							if(!verifyDictionaryValue(DISTRICTCO,
									strDistrictCode)){
								map.put("GUARANTORCODE",
										"[担保人代码] 前6位数字地区标识码有误 ");
							}
							// 行内校验
							if(limitBranchCode(exguaran.getGuarantorcode(),
									exguaran.getFiletype(), exguaran
											.getBusinessid(), exguaran
											.getInstcode())){
								map.put("GUARANTORCODE",
										"[担保人代码] 与当前记录所属机构对应的申报号码不匹配 ");
							}else if(checkBranchCode(exguaran
									.getGuarantorcode())){
								map.put("GUARANTORCODE",
										"[担保人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
							}
						}
						// BENAME 受益人中文名称 字符型，128 非必填项，中英文名称至少填写一个。
						// BENAMEN 受益人英文名称 字符型，128 非必填项。
						if(isNull(exguaran.getBename())
								&& isNull(exguaran.getBenamen())){
							map.put("BENAME", "[受益人中文名称] 中英文名称至少填写一个 ");
							map.put("BENAMEN", "[受益人英文名称] 中英文名称至少填写一个 ");
						}
						// 履约日期
						if(isNull(exguaran.getGuperdate())){
							map.put("GUPERDATE", "[履约日期] 不能为空 ");
						}else if(contractExguaran != null
								&& StringUtil.isNotEmpty(contractExguaran
										.getContractdate())){
							if(!verifyTwoDates(contractExguaran
									.getContractdate(), exguaran.getGuperdate())){
								map.put("GUPERDATE", "[履约日期] 不能早于签约日期 ["
										+ contractExguaran.getContractdate()
										+ "] ");
							}
						}else{
							if(contractExguaran != null
									&& !verifyTwoDates(contractExguaran
											.getTradedate(), exguaran
											.getGuperdate())){
								map.put("GUPERDATE", "[履约日期] 不能早于签约信息交易日期 ["
										+ contractExguaran.getTradedate()
										+ "] ");
							}
						}
						// 履约币种
						if(StringUtil.isEmpty(exguaran.getGupercurr())){
							map.put("GUPERCURR", "[履约币种] 不能为空且必须在字典表中存在 ");
						}else if(!verifyDictionaryValue(CURRENCY, exguaran
								.getGupercurr())){
							String value = getKey(exguaran.getGupercurr(),
									CURRENCY);
							map.put("GUPERCURR", "[履约币种] [" + value + "] 无效 ");
						}
						// 履约金额
						if(isNull(exguaran.getGuperamount())){
							map.put("GUPERAMOUNT", "[履约金额] 不能为空 ");
						}else if(exguaran.getGuperamount().compareTo(
								new BigDecimal(0.0)) < 0){
							map.put("GUPERAMOUNT", "[履约金额] 应大于等于0 ");
						}else if(contractExguaran != null
								&& !isNull(contractExguaran.getGuaranamount())
								&& exguaran.getGuperamount().compareTo(
										contractExguaran.getGuaranamount()) > 0){
							map
									.put("GUPERAMOUNT", "[履约金额] 应小于等于保函金额[ "
											+ contractExguaran
													.getGuaranamount() + "] ");
						}
						// 购汇履约金额
						if(isNull(exguaran.getPguperamount())){
							map.put("PGUPERAMOUNT", "[购汇履约金额]不能为空 ");
						}else if(exguaran.getPguperamount().compareTo(
								new BigDecimal(0.0)) < 0){
							map.put("PGUPERAMOUNT", "[购汇履约金额]应大于等于0 ");
						}else if(exguaran.getPguperamount().compareTo(
								exguaran.getGuperamount()) > 0){
							map.put("PGUPERAMOUNT", "[购汇履约金额]应小于等于履约金额[ "
									+ exguaran.getGuperamount() + "] ");
						}
						/* 子表信息校验
						List list = new ArrayList();
						List subList = service.getCfaChildren(
								"T_CFA_SUB_GUPER_INFO", exguaran
										.getBusinessid());
						if(CollectionUtil.isNotEmpty(subList)){
							Self_Sub_GuperVerify subVerify = null;
							for(int j = 0; j < subList.size(); j++){
								Self_Sub_GUPER sub = (Self_Sub_GUPER) subList
										.get(j);
								List tmp = new ArrayList();
								tmp.add(sub);
								subVerify = new Self_Sub_GuperVerify(
										dictionarys, tmp, contractExguaran);
								VerifyModel vm = subVerify.execute();
								if(vm.getFatcher() != null
										&& !vm.getFatcher().isEmpty()){
									vm.getFatcher().put(SUBID, sub.getSubid());
									vm.getFatcher().put(INNERTABLEID,
											"T_CFA_SUB_GUPER_INFO");
									list.add(vm.getFatcher());
								}
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							verifyModel.setChildren(list);
						}
						*/
					}
				}
				// 业务编号校验
				// 不能重复
				if("BA".equals(exguaran.getFiletype())
						&& checkBusinessNoRepeat(exguaran.getBusinessno(),
								"T_CFA_B_EXGUARAN", exguaran.getFiletype(),
								exguaran.getBusinessid())){
					map.put("BUSINESSNO", "[业务编号] 对外担保签约信息中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(exguaran.getBusinessno())){
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
