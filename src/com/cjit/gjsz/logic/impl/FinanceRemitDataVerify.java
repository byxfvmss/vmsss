/**
 * 境外汇款申请书—核销专用信息 t_fini_remit
 */
package com.cjit.gjsz.logic.impl;

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
import com.cjit.gjsz.logic.model.BaseRemit;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.DeclareRemit;
import com.cjit.gjsz.logic.model.FinanceRemit;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinanceRemitDataVerify extends FinanceDataVerify implements
		DataVerify{

	public final String baseTableId = "t_base_remit";
	public final String parentTableId = "t_decl_remit";
	public final String childTableId = "t_customs_decl";
	private VerifyConfig verifyConfig;

	public FinanceRemitDataVerify(){
	}

	public FinanceRemitDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinanceRemit financeRemit = (FinanceRemit) verifylList.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				DeclareRemit declareRemit = (DeclareRemit) service
						.getDataVerifyModel(parentTableId, financeRemit
								.getBusinessid());
				financeRemit.setDeclareRemit(declareRemit);
				BaseRemit baseRemit = (BaseRemit) service.getDataVerifyModel(
						baseTableId, financeRemit.getBusinessid());
				financeRemit.setBaseRemit(baseRemit);
				boolean has = false;
				if(StringUtil.isEmpty(this.interfaceVer)
						|| "1.1".equals(this.interfaceVer)){
					long size = service.getDataVerifyModelSize(childTableId,
							financeRemit.getBusinessid());
					has = size > 0 ? true : false;
				}
				if(!verifyActiontype(financeRemit.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financeRemit.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financeRemit.getActiontype(), financeRemit
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
				if(!verifyAReasion(financeRemit.getActiontype(), financeRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(financeRemit.getActiontype(), financeRemit
						.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				// 外管1.2之前版本校验
				if(StringUtil.isEmpty(this.interfaceVer)
						|| "1.1".equals(this.interfaceVer)){
					// 付款类型
					if(!verifyImpdate(financeRemit.getImpdate(), financeRemit
							.getDeclareRemit().getPaytype())){
						map.put("IMPDATE",
								"付款类型为[A-预付货款]时,[最迟装运日期] 必须输入并且必须为日期格式及合法性。\n");
					}
					// 境外汇款申请书—核销专用信息 的子类验证
					if(verifyCusmno(financeRemit.getCusmno(), has)){
						List list = new ArrayList();
						List children = service.getChildren(childTableId,
								financeRemit.getBusinessid());
						if(CollectionUtil.isNotEmpty(children)){
							CustomDeclareDataVerify customDeclareDataVerify = null;
							for(int j = 0; j < children.size(); j++){
								CustomDeclare customDeclare = (CustomDeclare) children
										.get(j);
								List tmp = new ArrayList();
								tmp.add(customDeclare);
								customDeclareDataVerify = new CustomDeclareDataVerify(
										dictionarys, tmp, interfaceVer);
								VerifyModel vm = customDeclareDataVerify
										.execute();
								if(vm.getFatcher() != null
										&& !vm.getFatcher().isEmpty()){
									vm.getFatcher().put(SUBID,
											customDeclare.getSubid());
									list.add(vm.getFatcher());
								}
							}
							if(CollectionUtil.isNotEmpty(list)){
								verifyModel.setChildren(list);
							}
						}
					}else{
						map.put("CUSMNO", "如果填写了 [报关单信息] ，此代码必须输入。 \n");
					}
					// DFHL: 增加校验 add by wangxin 20091123
					/*
					 * 核销信息_境内汇款申请书中，付款类型为“P-货到付款”时， 接口程序对“是否已经填写报关单信息”没有校验功能，
					 * 如果不填写报关单信息直接保存，接口程序依然可以校验通过。
					 * 但是在SAFE端，对此付款类型进行报关单信息验证，若无报关单信息，
					 * 则会反馈一个错误报文。同样的情况，申报信息_境外汇款申请书中，
					 * 付款方式为“P-货到付款”，输入核销信息时因系统无法显示“报关单信息” 一栏，
					 * 所以在输入核销信息的同时无法输入报关单信息，在保存逻辑校验通过OK 后， 系统会自动从输入界面跳到初始的查询界面，
					 * 必须要在查询界面点击刚刚输入的核销信息后才能重新进入到核销信息界面来输入报关单信息。
					 * 请修改：付款方式为“P-货到付款”，报关单信息必须输入。
					 */
					if("P".equals(declareRemit.getPaytype()) && !has){
						if(!map.containsKey("CUSMNO")){
							map
									.put("CUSMNO",
											"申报信息中付款类型为[P-货到付款]，必须填写报关单信息.\n");
						}
					}
				}
				if(!verifyContrno(financeRemit.getContrno())){
					map.put("CONTRNO", "[合同号] 不能为空.\n");
				}
				if(!verifyInvoino(financeRemit.getInvoino())){
					map.put("INVOINO", "[发票号] 不能为空.\n");
				}
				if(!verifyCrtuser(financeRemit.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financeRemit.getInptelc())){
					map.put("INPTELC", "[填报人电话] 不能为空。\n");
				}
				if(!verifyRptdate(financeRemit.getRptdate())){
					map.put("RPTDATE",
							"[申报日期] 不能为空，按申报主体真实申报日期填写,必须小于等于当前日期。\n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(baseRemit.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(baseTableId,
								declareRemit.getBusinessid())){
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