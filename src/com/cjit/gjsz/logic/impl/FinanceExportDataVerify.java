/**
 * 出口收汇核销专用联（境外收入）—核销专用信息 t_fini_export
 */
package com.cjit.gjsz.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BaseIncome;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.FinanceExport;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinanceExportDataVerify extends FinanceDataVerify implements
		DataVerify{

	// DFHL: 修改了parentTableId的表名
	public final String parentTableId = "t_base_income";
	public final String childTableId = "t_export_info";
	private VerifyConfig verifyConfig;

	public FinanceExportDataVerify(){
	}

	public FinanceExportDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceExport financeExport = (FinanceExport) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				// DFHL: 收汇总金额中用于出口核销的金额 校验 start
				BaseIncome baseExport = (BaseIncome) service
						.getDataVerifyModel(parentTableId, financeExport
								.getBusinessid());
				long size = service.getDataVerifyModelSize(childTableId,
						financeExport.getBusinessid());
				boolean has = size > 0 ? true : false;
				if(!verifyActiontype(financeExport.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financeExport.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financeExport.getActiontype(), financeExport
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
				if(!verifyAReasion(financeExport.getActiontype(), financeExport
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(financeExport.getActiontype(), financeExport
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				if(!verifyPayattr(financeExport.getPayattr(), PAYATTR_VERIFY)){
					String type = getKey(financeExport.getPaytype(), PAYTYPE);
					map.put("PAYTYPE", "[收汇类型] [" + type + "] 无效.\n");
				}
				if(!verifyChkprtd(financeExport.getChkprtd(), CHKPRTD_VERIFY)){
					map.put("CHKPRTD", "[已出具出口收汇核销专用联] ["
							+ financeExport.getChkprtd() + "] 无效.\n");
				}
				// DFHL: 已出具出口收汇核销专用联 为是时, 必须有出口收汇核销单 ,否则不能有出口核销单 start
				// if(!has && "Y".equalsIgnoreCase(financeExport.getChkprtd())){
				// map.put("CHKPRTD", "[已出具出口收汇核销专用联] 选择[是]时, [出口收汇核销单号码]
				// 不能为空");
				// }else if(has &&
				// "N".equalsIgnoreCase(financeExport.getChkprtd())){
				// map.put("CHKPRTD", "若[出口收汇核销单号码] 为空时, [已出具出口收汇核销专用联] 应该选择
				// [否]");
				// }
				// DFHL: 已出具出口收汇核销专用联 为是时, 必须有出口收汇核销单 ,否则不能有出口核销单 end
				if(!verifyOsamt(financeExport.getOsamt(), financeExport
						.getPayattr())){
					map.put("OSAMT", "本笔款为无追索权的出口保理融资项下收汇时才可选填.\n");
				}
				// TODO: 编号004 非常复杂的逻辑,客户确认
				if(verifyChkamt(financeExport.getChkamt(), has)){
					if(financeExport.getChkamt() != null)
						if(baseExport != null
								&& baseExport.getTxamt().compareTo(
										financeExport.getChkamt()) < 0){
							map.put("CHKAMT",
									"[收汇总金额中用于出口核销的金额] 不能大于基础信息中的 [收入款金额:"
											+ baseExport.getTxamt() + "]");
						}
					// DFHL: 收汇总金额中用于出口核销的金额 校验 end
					// 境外汇款申请书—核销专用信息 的子类验证
					List list = new ArrayList();
					List children = service.getChildren(childTableId,
							financeExport.getBusinessid());
					if(CollectionUtil.isNotEmpty(children)){
						ExportInfoDataVerify exportInfoDataVerify = null;
						for(int j = 0; j < children.size(); j++){
							ExportInfo exportInfo = (ExportInfo) children
									.get(j);
							List tmp = new ArrayList();
							tmp.add(exportInfo);
							exportInfoDataVerify = new ExportInfoDataVerify(
									dictionarys, tmp, interfaceVer);
							VerifyModel vm = exportInfoDataVerify.execute();
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
				}else{
					map
							.put(
									"CHKAMT",
									"若 [出口收汇核销单号码] 不为空，则 [收汇总金额中用于出口核销的金额] 必须>0；若 [收汇总金额中用于出口核销的金额] >0，则出口收汇核销单号码不能为空。 \n");
				}
				if(!verifyCrtuser(financeExport.getCrtuser())){
					map.put("CRTUSER", "[申请人] 不能为空。 \n");
				}
				if(!verifyInptelc(financeExport.getInptelc())){
					map.put("INPTELC", "[申请人电话] 不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				if(!verifyRptdate(financeExport.getRptdate())){
					map.put("RPTDATE", " 按申报主体真实申报日期填写,必须小于等于当前日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if(baseExport != null
						&& "C".equals(baseExport.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								financeExport.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}
