package com.cjit.vms.trans.service.createBill.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.service.CustomerService;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.util.DataUtil;

public class BillValidationServiceImpl extends GenericServiceImpl implements
		BillValidationService {

	// private List transInfoList;
	// CheckResult checkResult;
	CustomerService customerService;
	CreateBillService createBillService;
	OrganizationService organizationService;

	/**
	 * 构造函数，初始化时直接传入交易信息List
	 *
	 */
	public BillValidationServiceImpl() {
		// checkResult = new CheckResult();
		// checkResult.setCheckFlag(CHECK_OK);
	}

	public void validationBeforLoadBaseData() {

	}

	/***
	 * 遇到异常结束校验返回消息
	 * 
	 * @param transInfoList
	 * @return
	 */
	public CheckResult shortCircuitValidation(List transInfoList) {
		// 交易机构不能为空
		List checkInstResultList = checkInst(transInfoList);
		if (checkInstResultList.size() > 0) {
			return (CheckResult) checkInstResultList.get(0);
		}

		CheckResult checkInstUniqueResult = checkInstUnique(transInfoList);
		if (null != checkInstUniqueResult) {
			return checkInstUniqueResult;
		}
		
		CheckResult result = null;
		for (int i = 0; i < transInfoList.size(); i++) {
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			result = checkCustomer(transInfo);
			if (null != result) {
				return result;
			}
			result = checkCustType(transInfo);
			if (null != result) {
				return result;
			}
			result = checkTaxNo(transInfo);
			if (null != result) {
				return result;
			}
			result = checkTaxInfo(transInfo);
			if (null != result) {
				return result;
			}
			result = checkGoodsInfo(transInfo);
			if (null != result) {
				return result;
			}

		}

		if (null == result) {
			result = new CheckResult(CheckResult.CHECK_OK,"", "校验通过");
		}
		return result;
	}

	/**
	 * 校验方法集合
	 * 
	 * @return
	 */
	public List<CheckResult> validation(List transInfoList) {
		// 交易机构校验
		List resultList = new ArrayList();
		// 交易机构不为空 并加载数据库机构信息
		List checkInstResultList = checkInst(transInfoList);
		if (checkInstResultList.size() > 0) {
			resultList.add(checkInstResultList.get(0));
		}
		// 交易机构唯一校验
		CheckResult checkInstUniqueResult = checkInstUnique(transInfoList);
		if (null != checkInstUniqueResult) {
			resultList.add(checkInstUniqueResult);
		}
		CheckResult result = null;
		for (int i = 0; i < transInfoList.size(); i++) {
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			// 1.判断所选交易是否存在客户信息并更新销方税号
			result = checkCustomer(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 确定客户是否有专票资质
			result = checkCustType(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 验证所选交易的机构信息并更新开票方税号
			result = checkTaxNo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 判断税率在税目中是否存在
			result = checkTaxInfo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 判断商品是否唯一存在并更新商品
			result = checkGoodsInfo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
		}

		return resultList;
	}
	
	@Override
	public List<CheckResult> validationAll(List transInfoList) {
		// 交易机构校验
		List resultList = new ArrayList();
		// 交易机构不为空 并加载数据库机构信息
		List checkInstResultList = checkInst(transInfoList);
		if (checkInstResultList.size() > 0) {
			resultList.add(checkInstResultList.get(0));
		}
		
		CheckResult result = null;
		for (int i = 0; i < transInfoList.size(); i++) {
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			// 1.判断所选交易是否存在客户信息并更新销方税号
			result = checkCustomer(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 确定客户是否有专票资质
			result = checkCustType(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 验证所选交易的机构信息并更新开票方税号
			result = checkTaxNo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 判断税率在税目中是否存在
			result = checkTaxInfo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
			// 判断商品是否唯一存在并更新商品
			result = checkGoodsInfo(transInfo);
			if (null != result) {
				resultList.add(result);
			}
		}

		return resultList;
	}

	/***
	 * 交易机构不为空 并加载数据库机构信息
	 * 
	 * @param transInfoList
	 * @return
	 */

	public List checkInst(List transInfoList) {

		List checkResultList = new ArrayList();
		for (int i = 0; i < transInfoList.size(); i++) {
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			String instCode = transInfo.getInstCode();
			// 校验交易信息中是否存在机构号
			if (null == instCode || "".equals(instCode)) {
				CheckResult checkResult = new CheckResult(CheckResult.NO_INST,
						CheckResult.NO_INST_TRANS_MSG, transInfo);
				checkResultList.add(checkResult);
			} else {
				Organization org = new Organization();
				org.setId(instCode);
				List orgList = organizationService.findOrganization(org);
				if (orgList.size() > 0) {
					transInfo.setOrganization((Organization) orgList.get(0));
				}

			}

		}

		return checkResultList;
	};

	/***
	 * 交易机构唯一校验
	 * 
	 * @param transInfoList
	 * @return
	 */
	public CheckResult checkInstUnique(List transInfoList) {
		CheckResult checkResult = null;
		Map map = new HashMap();
		for (int i = 0; i < transInfoList.size(); i++) {
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			String instCode = transInfo.getInstCode();
			map.put(transInfo.getInstCode(), transInfo.getInstCode());
		}
		List instList = Arrays.asList(map.keySet().toArray());
		int count = instList.size();
		// 判断交易信息中交易发生机构号是否唯一
		if (count > 1) {
			checkResult = new CheckResult(CheckResult.INST_UNIQUE,
					CheckResult.INST_UNIQUE_MSG);
		}
		return checkResult;
	}

	/**
	 * 验证所选交易的机构信息并更新开票方税号
	 * 
	 * @param transInfo
	 * @return
	 */
	public CheckResult checkTaxNo(TransInfo transInfo) {
		CheckResult checkResult = null;

		String instCode = transInfo.getInstCode();
		// 校验交易信息中是否存在客户号

		Organization org = transInfo.getOrganization();
		if (null == org) {
			org = organizationService.findTaxNoByInstCode(instCode);
			transInfo.setOrganization(org);
		}
		String taxNo = org.getTaxperNumber();
		if (null == taxNo || "".equals(taxNo)) {
			checkResult = new CheckResult(CheckResult.NO_TAX_NO,
					CheckResult.NO_TAX_NO_MSG, transInfo);
			// 保存税号到交易信息对象
		} else {

			transInfo.setTaxNo(taxNo);

		}
		return checkResult;
	}

	/**
	 * 1.判断所选交易是否存在客户信息并更新销方税号
	 * 
	 * @param transInfo
	 * @return
	 */
	public CheckResult checkCustomer(TransInfo transInfo) {
		CheckResult checkResult = null;
		List checkResultList = new ArrayList();

		String customerId = transInfo.getCustomerId();
		// 如果交易信息中不存在客户ID，直接返回校验失败
		if (null == customerId || "".equals(customerId)) {
			checkResult = new CheckResult(CheckResult.NO_CUSTOMER,
					CheckResult.NO_CUSTOMER_MSG, transInfo);
			checkResultList.add(checkResult);
		} else {

			Customer customer = customerService.findCustomer(customerId);
			// 如果客户信息表中不存在客户ID，返回校验失败
			if (null == customer) {
				checkResult = new CheckResult(CheckResult.NO_CUSTOMER,
						CheckResult.NO_CUSTOMER_MSG, transInfo);
				checkResultList.add(checkResult);
			} else {
				// 获取客户对象

				// 将客户税号存入交易信息中
				transInfo.setCustomerTaxno(customer.getCustomerTaxno());
				transInfo.setCustomer(customer);
				// // 判断客户是否符合专票条件
				// checkCustType(customer, transInfo);
			}
		}
		return checkResult;
	}

	/**
	 * 判断税率在税目中是否存在
	 * 
	 * @param transInfo
	 * @return
	 */
	public CheckResult checkTaxInfo(TransInfo transInfo) {
		CheckResult checkResult = null;
		// 判断根据机构税号、税率、发票类型判断是否存在对应税号
		List taxInfoList = createBillService.findTaxInfoList(transInfo);
		if (taxInfoList.size() < 1) {
			checkResult = new CheckResult(CheckResult.NO_TAX_INFO,
					CheckResult.NO_TAX_INFO_MSG, transInfo);
		} else {

			VmsTaxInfo taxinfo = (VmsTaxInfo) taxInfoList.get(0);
			transInfo.setTaxinfo(taxinfo);
		}
		return checkResult;
	}

	/**
	 * 判断商品是否唯一存在并更新商品
	 * 
	 * @param transInfo
	 * @return
	 */
	public CheckResult checkGoodsInfo(TransInfo transInfo) {
		CheckResult checkResult = null;
		List goodsInfoList = createBillService.findGoodsInfo(transInfo);

		if (goodsInfoList.size() != 1) {
			checkResult = new CheckResult(CheckResult.NO_GOODS_INFO,
					CheckResult.NO_GOODS_INFO_MSG, transInfo);
		} else {
			VerificationInfo verificationInfo = (VerificationInfo) goodsInfoList
					.get(0);
			transInfo.setVerificationInfo(verificationInfo);
		}
		return checkResult;
	}

	/**
	 * 是否符合专票条件符合专票条件
	 * 
	 * @return
	 */
	public CheckResult checkCustType(TransInfo transInfo) {
		CheckResult checkResult = null;
		Customer customer = transInfo.getCustomer();
		// 客户标记和交易标记均为专票
		if ("0".equals(customer.getFapiaoType())
				&& "0".equals(transInfo.getFapiaoType())) {
			String customerName = emptyStr(customer.getCustomerCName());// 名称
			String customerTaxno = emptyStr(customer.getCustomerTaxno());// 纳税人识别号
			String customerAddress = emptyStr(customer.getCustomerAddress());// 地址
			String customerPhone = emptyStr(customer.getCustomerPhone());// 电话
			String customerBank = emptyStr(customer.getCustomerCBank());// 开户行
			String customerAccunt = emptyStr(customer.getCustomerAccount());// 帐号
			// 判断客户名称为全中文
			Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
			Matcher matcher = pat.matcher(customerName);
			if (!matcher.find()) {
				checkResult = new CheckResult(CheckResult.NAME_ILLEGAL,
						CheckResult.NAME_ILLEGAL_MSG, transInfo);
			} else if (null == customerTaxno || "".equals(customerTaxno)
					|| customerTaxno.length() > 18
					|| customerTaxno.length() < 15) {
				checkResult = new CheckResult(CheckResult.TAXNO_ILLEGAL,
						CheckResult.TAXNO_ILLEGAL_MSG, transInfo);
			} else if ("".equals(customerAddress + customerPhone)) {
				checkResult = new CheckResult(CheckResult.NO_ADDR_PHONE,
						CheckResult.NO_ADDR_PHONE_MSG, transInfo);
			} else if ("".equals(customerBank + customerAccunt)) {
				checkResult = new CheckResult(CheckResult.NO_BANK_ACCOUNT,
						CheckResult.NO_BANK_ACCOUNT_MSG, transInfo);
			}
		} else {
			transInfo.setFapiaoType("1");
		}
		return checkResult;
	}

	/**
	 * 分步执行校验前，判断是否已经校验失败
	 * 
	 * @return
	 */
	// public boolean result() {
	// if ("Y".equals(checkResult.getCheckFlag())) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	/**
	 * 如果返回null，则替换为""
	 * 
	 * @param s
	 * @return
	 */
	public String emptyStr(String s) {
		return s == null ? "" : s;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public CreateBillService getCreateBillService() {
		return createBillService;
	}

	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Override
	public List checkingTransByCherNum(TransInfo transInfo, boolean flag) {
		Map map = new HashMap();
		if (flag == true) {
			transInfo.setPremTerm(null);
		}
		map.put("transInfo", transInfo);
		if(transInfo != null){
			map.put("selectTransId", transInfo.getSelectTransIds());
		}
		map.put("billFreq", DataUtil.PAYMENT_FREQUENCY_12);
		map.put("dataStatus", DataUtil.TRANS_STATUS_1);
		return this.find("checkingTransByCherNum", map);
	}

	public List findTransInfoListByTransId(String[] selectTransId) {
		Map map = new HashMap();
		map.put("selectTransId", selectTransId);
		return this.find("findTransInfoListByTransId",map);
	}
}
