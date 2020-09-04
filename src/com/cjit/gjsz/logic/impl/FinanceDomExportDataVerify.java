/**
 * 出口收汇核销专用联（境内收入）—核销专用信息 t_fini_dom_export
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
import com.cjit.gjsz.logic.model.BaseExport;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.FinanceDomExport;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinanceDomExportDataVerify extends DeclareDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_export";
	public final String childTableId = "t_export_info";
	public static final String PAYTYPE_VERIFY = "A,O";
	private VerifyConfig verifyConfig;

	public FinanceDomExportDataVerify(){
	}

	public FinanceDomExportDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceDomExport financeDomExport = (FinanceDomExport) verifylList
						.get(i);
				if(financeDomExport != null){
					SearchService service = (SearchService) SpringContextUtil
							.getBean("searchService");
					BaseExport baseExport = (BaseExport) service
							.getDataVerifyModel(parentTableId, financeDomExport
									.getBusinessid());
					financeDomExport.setBaseExport(baseExport);
					boolean has = false;
					if(StringUtil.isEmpty(this.interfaceVer)
							|| "1.1".equals(this.interfaceVer)){
						long size = service.getDataVerifyModelSize(
								childTableId, financeDomExport.getBusinessid());
						has = size > 0 ? true : false;
					}
					if(!verifyActiontype(financeDomExport.getActiontype(),
							ACTIONTYPE_VERIFY)){
						String type = getKey(financeDomExport.getActiontype(),
								ACTIONTYPE);
						map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
					}
					if(!verifyRptno(financeDomExport.getActiontype(),
							financeDomExport.getRptno())){
						// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 start
						/*
						 * map .put("RPTNO", "当时 [申报申请号] 为空时, [操作类型] 必需为
						 * [新建]。否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]\n" );
						 */
						map.put("RPTNO", "当时 [申报号码] 为空时, [操作类型] 必需为 [新建]");
						// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 end
					}
					// DFHL: 东方汇理增加 在状态为‘新增’时修改和删除原因不必须为空。
					if(!verifyAReasion(financeDomExport.getActiontype(),
							financeDomExport.getActiondesc())){
						map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
					}
					// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
					if(!verifyReasion(financeDomExport.getActiontype(),
							financeDomExport.getActiondesc())){
						map
								.put("ACTIONDESC",
										"当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
					}
					if(!verifyIsref(financeDomExport.getIsref(), ISREF_VERIFY)){
						String type = getKey(financeDomExport.getIsref(), ISREF);
						String columnName = "是否出口核销项下收汇";
						if("1.2".equals(this.interfaceVer)){
							columnName = "是否保税货物项下收汇";
						}
						map.put("ISREF", "[" + columnName + "] [" + type
								+ "] 无效.\n");
					}
					if(!verifyPaytype(financeDomExport.getPaytype(),
							PAYTYPE_VERIFY)){
						String type = getKey(financeDomExport.getPaytype(),
								PAYTYPE);
						map.put("PAYTYPE", "[收款性质] [" + type + "] 无效.\n");
					}
					if("1.2".equals(this.interfaceVer)){
						if(!verifyPayattr(financeDomExport.getPayattr(),
								PAYATTR_VERIFY_12)){
							String type = getKey(financeDomExport.getPayattr(),
									PAYATTR_D_12);
							map.put("PAYATTR", "[境内收入类型] [" + type + "] 无效.\n");
						}
					}else{
						if(!verifyPayattr(financeDomExport.getPayattr(),
								PAYATTR_VERIFY)){
							String type = getKey(financeDomExport.getPayattr(),
									PAYATTR1);
							map.put("PAYATTR", "[境内收汇类型] [" + type + "] 无效.\n");
						}
					}
					if(!verifyTxcode(financeDomExport.getTxcode(), BOP_INCOME)){
						String type = getKey(financeDomExport.getTxcode(),
								BOP_INCOME);
						map.put("TXCODE", "[交易编码1] [" + type
								+ "] 无效.必须在国际收支交易编码表中存在 \n");
					}
					if(!verifyTc1amt(financeDomExport.getTc1amt())){
						map.put("TC1AMT", "[相应金额1] 不能为空。 \n");
					}
					if(!verifyTxrem(financeDomExport.getTxrem())){
						map.put("TXREM", "[交易附言1] 不能为空。 \n");
					}
					if(!verifyTxcode2(financeDomExport.getTxcode2(),
							BOP_INCOME, financeDomExport.getTxcode(),
							financeDomExport.getTc2amt())){
						map
								.put("TXCODE2",
										"[交易编码2] 必须在国际收支交易编码表中存在不能与 [交易编码1] 相同，没有输入交易编码时，相应金额及交易附言不应该填写。有交易金额2时必填。 \n");
					}
					if(!verifyTc2amt(financeDomExport.getTc2amt(),
							financeDomExport.getTxcode2(), financeDomExport
									.getTc1amt(), financeDomExport
									.getBaseExport().getTxamt())){
						map.put("TC2AMT", "有 [交易编码2] 时必填。[相应金额1 "
								+ StringUtil.cleanBigInteger(financeDomExport
										.getTc1amt())
								+ "] 和 [相应金额2 "
								+ StringUtil.cleanBigInteger(financeDomExport
										.getTc2amt())
								+ "] 金额之和必须等于 [收入款金额 "
								+ StringUtil.cleanBigInteger(financeDomExport
										.getBaseExport().getTxamt()) + "]。 \n");
					}
					if(!verifyTx2rem(financeDomExport.getTx2rem(),
							financeDomExport.getTxcode2())){
						map.put("TX2REM", "有 [交易编码2] 时必填。 \n");
					}
					if(!verifyRefno(financeDomExport.getRptno())){
						map.put("REFNO", "[出口收汇核销单号码] ["
								+ financeDomExport.getRptno() + "] 无效.\n");
					}
					if(StringUtil.isEmpty(this.interfaceVer)
							|| "1.1".equals(this.interfaceVer)){
						if(verifyChkamt(financeDomExport.getChkamt(), has)){
							if(financeDomExport.getChkamt() != null
									&& financeDomExport.getBaseExport()
											.getTxamt().compareTo(
													financeDomExport
															.getChkamt()) < 0){
								map.put("CHKAMT",
										"[收汇总金额中用于出口核销的金额] 不能大于基础信息中的 [收入款金额:"
												+ baseExport.getTxamt() + "]");
							}
							// 境外汇款申请书—核销专用信息 的子类验证
							List list = new ArrayList();
							List children = service.getChildren(childTableId,
									financeDomExport.getBusinessid());
							if(CollectionUtil.isNotEmpty(children)){
								ExportInfoDataVerify exportInfoDataVerify = null;
								for(int j = 0; j < children.size(); j++){
									ExportInfo exportInfo = (ExportInfo) children
											.get(j);
									List tmp = new ArrayList();
									tmp.add(exportInfo);
									exportInfoDataVerify = new ExportInfoDataVerify(
											dictionarys, tmp, interfaceVer);
									VerifyModel vm = exportInfoDataVerify
											.execute();
									if(vm.getFatcher() != null
											&& !vm.getFatcher().isEmpty()){
										vm.getFatcher().put(SUBID,
												exportInfo.getSubid());
										list.add(vm.getFatcher());
									}
								}
								if(CollectionUtil.isNotEmpty(list)){
									verifyModel.setChildren(list);
								}
							}
							if(financeDomExport != null
									&& financeDomExport.getChkamt() != null
									&& financeDomExport.getChkamt().compareTo(
											BigInteger.ZERO) == 0
									&& children.size() == 0){
								map
										.put(
												"CHKAMT",
												"若 [出口收汇核销单号码] 不为空，则 [收汇总金额中用于出口核销的金额] 必须>0；若 [收汇总金额中用于出口核销的金额] >0，则出口收汇核销单号码不能为空。 \n");
							}
						}else{
							map
									.put(
											"CHKAMT",
											"若 [出口收汇核销单号码] 不为空，则 [收汇总金额中用于出口核销的金额] 必须>0；若 [收汇总金额中用于出口核销的金额] >0，则出口收汇核销单号码不能为空。 \n");
						}
					}
					if(!verifyCrtuser(financeDomExport.getCrtuser())){
						map.put("CRTUSER", "[填报人] 不能为空。 \n");
					}
					if(!verifyInptelc(financeDomExport.getInptelc())){
						map.put("INPTELC", "[填报人电话] 不能为空。 \n");
					}
					if(!verifyRptdate(financeDomExport.getRptdate())){
						map.put("RPTDATE", "[申报日期] 不能为空且必须合法,必须小于等于当前日期。 \n");
					}
					// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
					if("C".equals(baseExport.getCustype())
							&& !verifyConfig.verifyCustCodeOfCompany(
									parentTableId, financeDomExport
											.getBusinessid())){
						map.put("ACTIONTYPE",
								"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
					}
					if("1.2".equals(this.interfaceVer)){
						if(!verifyConfig.verifyRegno(financeDomExport
								.getRegno(), financeDomExport.getTxcode(),
								financeDomExport.getTxcode2(), "income")){
							map
									.put(
											"REGNO",
											"[外汇局批件号/登记表号/业务编号] 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/登记表号/业务编号”不能为空。 \n");
						}
					}else{
						// 无此属性
					}
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
