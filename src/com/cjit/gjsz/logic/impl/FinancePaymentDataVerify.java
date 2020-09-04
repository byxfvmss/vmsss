/**
 * 对外付款/承兑通知书－核销专用信息 t_fini_payment
 */
package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.BasePayment;
import com.cjit.gjsz.logic.model.FinancePayment;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class FinancePaymentDataVerify extends FinanceDataVerify implements
		DataVerify{

	public final String parentTableId = "t_base_payment";
	private VerifyConfig verifyConfig;

	public FinancePaymentDataVerify(){
	}

	public FinancePaymentDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		super(dictionarys, verifylList, interfaceVer);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				FinancePayment financePayment = (FinancePayment) verifylList
						.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				BasePayment basePayment = (BasePayment) service
						.getDataVerifyModel(parentTableId, financePayment
								.getBusinessid());
				if(!verifyActiontype(financePayment.getActiontype(),
						ACTIONTYPE_VERIFY)){
					String type = getKey(financePayment.getActiontype(),
							ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + type + "] 无效.\n");
				}
				if(!verifyRptno(financePayment.getActiontype(), financePayment
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
				if(!verifyAReasion(financePayment.getActiontype(),
						financePayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 新增时,修改/删除原因必须为空.\n");
				}
				// DFHL: 东方汇理增加 在状态为‘修改和删除’是修改和删除原因不能为空。
				if(!verifyReasion(financePayment.getActiontype(),
						financePayment.getActiondesc())){
					map.put("ACTIONDESC", "当时 [操作类型] 修改或删除时,修改/删除原因不能为空.\n");
				}
				// TODO: 编号003 需要注意一下
				if(!"1.2".equals(this.interfaceVer)){
					if(!verifyImpdate(financePayment.getImpdate())){
						map.put("IMPDATE", "[最迟装运日期]必须输入并且必须为日期格式及合法性。\n");
					}
				}
				if(!verifyContrno(financePayment.getContrno())){
					map.put("CONTRNO", "[合同号] 不能为空。\n");
				}
				if(!verifyInvoino(financePayment.getInvoino())){
					map.put("INVOINO", "[发票号] 不能为空。\n");
				}
				if(!verifyCrtuser(financePayment.getCrtuser())){
					map.put("CRTUSER", "[填报人] 不能为空。\n");
				}
				if(!verifyInptelc(financePayment.getInptelc())){
					map.put("INPTELC", "[填报人电话]不能为空。 \n");
				}
				// TODO: 编号001 有问题，申报申请号后来才生成啊
				if(!verifyRptdate(financePayment.getRptdate())){
					map
							.put("RPTDATE",
									" 按申报主体真实 [申报日期] 填写，不能为空,必须小于等于当前日期。 \n");
				}
				// TODO 金宏工程外汇局子项与银行业务系统数据接口规范
				if("C".equals(basePayment.getCustype())
						&& !verifyConfig.verifyCustCodeOfCompany(parentTableId,
								financePayment.getBusinessid())){
					map.put("ACTIONTYPE",
							"该用户基本信息[组织机构代码]应在已报送的单位基本情况表或本批次报送的单位基本情况表中");
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 最迟装运日期 必须输入 系统检查日期格式及合法性
	 * @param impdate 最迟装运日期
	 * @return
	 */
	public boolean verifyImpdate(String impdate){
		// return verifyRptdate(impdate);
		if(StringUtil.isNotEmpty(impdate)){
			if(impdate.trim().length() == 8){
				return true;
			}
		}
		return false;
	}

	public void setVerifyConfig(VerifyConfig vc){
		verifyConfig = vc;
	}
}