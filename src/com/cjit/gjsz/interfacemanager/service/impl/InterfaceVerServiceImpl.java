package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.interfacemanager.service.InterfaceVerService;

/**
 * 
 * @作者: lihaiboA
 * @日期: Jan 10, 2012
 * @描述: [InterfaceVerServiceImpl]国际收支网上申报系统与银行业务系统数据接口规范（1.2版）升级和还原类
 */
public class InterfaceVerServiceImpl extends GenericServiceImpl implements
		InterfaceVerService{

	public boolean upgradeInterfaceVer12(){
		boolean bReturn = true;
		try{
			Map params = new HashMap();
			// 涉外收入申报单-申报信息
			this.save("upgrade_DeclIncome_ISREF", params);
			this.save("upgrade_DeclIncome_BILLNO", params);
			this.save("upgrade_DeclIncome_PAYATTR", params);
			// 境内收入申报单-基础信息
			this.save("upgrade_BaseExport_RPTNO", params);
			// 出口收汇核销专用联信息（境内收入）-核销信息
			this.save("upgrade_FiniDomExport_RPTNO", params);
			this.save("upgrade_FiniDomExport_ISREF", params);
			this.save("upgrade_FiniDomExport_ExportInfo", params);
			this.save("upgrade_FiniDomExport_CHKAMT", params);
			this.save("upgrade_FiniDomExport_PAYATTR", params);
			this.save("upgrade_FiniDomExport_REGNO", params);
			// 境外汇款申请书-申报信息
			this.save("upgrade_DeclRemit_ISREF", params);
			this.save("upgrade_DeclRemit_REGNO", params);
			// 境外汇款申请书-核销信息
			this.save("upgrade_FiniRemit_CUSMNO", params);
			this.save("upgrade_FiniRemit_IMPDATE", params);
			this.save("upgrade_FiniRemit_REGNO", params);
			this.save("upgrade_FiniRemit_CustomsDecl", params);
			// 对外付款/承兑通知书-申报信息
			this.save("upgrade_DeclPayment_ISREF", params);
			this.save("upgrade_DeclPayment_REGNO", params);
			// 对外付款/承兑通知书-核销信息
			this.save("upgrade_FiniPayment_IMPDATE", params);
			this.save("upgrade_FiniPayment_REGNO", params);
			// 境内汇款申请书-基础信息
			this.save("upgrade_BaseDomRemit_RPTNO", params);
			this.save("upgrade_BaseDomRemit_OPPACC", params);
			// 境内汇款申请书-核销信息
			this.save("upgrade_FiniDomRemit_RPTNO", params);
			this.save("upgrade_FiniDomRemit_ISREF", params);
			this.save("upgrade_FiniDomRemit_REGNO", params);
			this.save("upgrade_FiniDomRemit_CUSMNO", params);
			this.save("upgrade_FiniDomRemit_IMPDATE", params);
			this.save("upgrade_FiniDomRemit_CustomsDecl", params);
			this.save("upgrade_FiniDomRemit_PAYATTR", params);
			// 字典
			this.save("upgrade_Dictionary_PAYATTR1_A", params);
			this.save("upgrade_Dictionary_PAYATTR1_S", params);
			this.save("upgrade_Dictionary_PAYATTR", params);
			this.save("upgrade_Dictionary_PAYATTR_D_12", params);
			// 境内付款/承兑通知书-基础信息
			this.save("upgrade_BaseDomPay_RPTNO", params);
			// 境内付款/承兑通知书-核销信息
			this.save("upgrade_FiniDomPay_RPTNO", params);
			this.save("upgrade_FiniDomPay_ISREF", params);
			this.save("upgrade_FiniDomPay_IMPDATE", params);
			this.save("upgrade_FiniDomPay_REGNO", params);
			this.save("upgrade_FiniDomPay_PAYATTR", params);
			// 涉外收入申报单-STOB全接口
			this.save("upgrade_StobWG_ISWRITEOFF", params);
			this.save("upgrade_StobWG_BILLNO", params);
			this.save("upgrade_StobWG_PAYATTR", params);
			this.save("upgrade_StobWG_stobsh", params);
			// 境内收入申报单-STOB全接口
			this.save("upgrade_StobWR_RPTNO", params);
			this.save("upgrade_StobWR_ISWRITEOFF", params);
			this.save("upgrade_StobWR_PAYATTR", params);
			this.save("upgrade_StobWR_stobsh", params);
			this.save("upgrade_StobWR_REGISTNO", params);
			this.save("upgrade_StobWR_tableName", params);
			// 反馈错误信息
			this.save("upgrade_errorFeedBack_infoType", params);
			// FMSS首页统计
			this.save("upgrade_homeDic_dicName", params);
			this.save("upgrade_homeDic_dicTypeName", params);
		}catch (Exception ex){
			bReturn = false;
			ex.printStackTrace();
		}
		return bReturn;
	}

	public boolean degradeInterfaceVer12(){
		boolean bReturn = true;
		try{
			Map params = new HashMap();
			// 涉外收入申报单-申报信息
			this.save("degrade_DeclIncome_ISREF", params);
			this.save("degrade_DeclIncome_BILLNO", params);
			this.save("degrade_DeclIncome_PAYATTR", params);
			// 境内收入申报单-基础信息
			this.save("degrade_BaseExport_RPTNO", params);
			// 境内收入申报单-管理信息
			this.save("degrade_FiniDomExport_RPTNO", params);
			this.save("degrade_FiniDomExport_ISREF", params);
			this.save("degrade_FiniDomExport_ExportInfo", params);
			this.save("degrade_FiniDomExport_CHKAMT", params);
			this.save("degrade_FiniDomExport_PAYATTR", params);
			this.save("degrade_FiniDomExport_REGNO", params);
			// 境外汇款申请书-申报信息
			this.save("degrade_DeclRemit_ISREF", params);
			this.save("degrade_DeclRemit_REGNO", params);
			// 境外汇款申请书-管理信息
			this.save("degrade_FiniRemit_CUSMNO", params);
			this.save("degrade_FiniRemit_IMPDATE", params);
			this.save("degrade_FiniRemit_REGNO", params);
			this.save("degrade_FiniRemit_CustomsDecl", params);
			// 对外付款/承兑通知书-申报信息
			this.save("degrade_DeclPayment_ISREF", params);
			this.save("degrade_DeclPayment_REGNO", params);
			// 境外汇款申请书-管理信息
			this.save("degrade_FiniPayment_IMPDATE", params);
			this.save("degrade_FiniPayment_REGNO", params);
			// 境内汇款申请书-基础信息
			this.save("degrade_BaseDomRemit_RPTNO", params);
			this.save("degrade_BaseDomRemit_OPPACC", params);
			// 境内汇款申请书-管理信息
			this.save("degrade_FiniDomRemit_RPTNO", params);
			this.save("degrade_FiniDomRemit_ISREF", params);
			this.save("degrade_FiniDomRemit_REGNO", params);
			this.save("degrade_FiniDomRemit_CUSMNO", params);
			this.save("degrade_FiniDomRemit_IMPDATE", params);
			this.save("degrade_FiniDomRemit_CustomsDecl", params);
			this.save("degrade_FiniDomRemit_PAYATTR", params);
			// 字典
			this.save("degrade_Dictionary_PAYATTR1_A", params);
			this.save("degrade_Dictionary_PAYATTR1_S", params);
			this.save("degrade_Dictionary_PAYATTR", params);
			this.save("degrade_Dictionary_PAYATTR_D_12", params);
			// 境内付款/承兑通知书-基础信息
			this.save("degrade_BaseDomPay_RPTNO", params);
			// 境内付款/承兑通知书-管理信息
			this.save("degrade_FiniDomPay_RPTNO", params);
			this.save("degrade_FiniDomPay_ISREF", params);
			this.save("degrade_FiniDomPay_IMPDATE", params);
			this.save("degrade_FiniDomPay_REGNO", params);
			this.save("degrade_FiniDomPay_PAYATTR", params);
			// 涉外收入申报单-STOB全接口
			this.save("degrade_StobWG_ISWRITEOFF", params);
			this.save("degrade_StobWG_BILLNO", params);
			this.save("degrade_StobWG_PAYATTR", params);
			this.save("degrade_StobWG_stobsh", params);
			// 境内收入申报单-STOB全接口
			this.save("degrade_StobWR_RPTNO", params);
			this.save("degrade_StobWR_ISWRITEOFF", params);
			this.save("degrade_StobWR_PAYATTR", params);
			this.save("degrade_StobWR_stobsh", params);
			this.save("degrade_StobWR_REGISTNO", params);
			this.save("degrade_StobWR_tableName", params);
			// 反馈错误信息
			this.save("degrade_errorFeedBack_infoType", params);
			// FMSS首页统计
			this.save("degrade_homeDic_dicName", params);
			this.save("degrade_homeDic_dicTypeName", params);
		}catch (Exception ex){
			bReturn = false;
			ex.printStackTrace();
		}
		return bReturn;
	}
}