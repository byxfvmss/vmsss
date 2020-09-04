package com.cjit.vms.trans.action;

import java.util.List;

import com.cjit.vms.trans.model.IntegrityCheckAccount;
import com.cjit.vms.trans.service.IntegrityCheckAccountService;

public class IntegrityCheckAccountAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private List integrityCheckAccountList;
	private IntegrityCheckAccountService integrityCheckAccountService;
	private IntegrityCheckAccount account = new IntegrityCheckAccount();
	private List checkAccount;
	private String instCode;// 机构号
	private String transId;// 交易ID

	public String listIntegrityCheckAccount() {
		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}
			account.setInstCode(this.getInstCode());
			integrityCheckAccountService.getCheckAccountList(account,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public String integrityCheckAccountType() {
		try {
			String instCode = request.getParameter("instCode");
			System.out.println(instCode);
			integrityCheckAccountService.getCheckGoodsInfoList(instCode,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String integrityCheckAccountDetail() {
		try {
			String instCode = request.getParameter("instCode");
			String goodsNo = request.getParameter("goodsNo");
			account = new IntegrityCheckAccount();
			account.setInstCode(instCode);
			account.setGoodsNo(goodsNo);
			integrityCheckAccountService.getCheckTransInfoList(account,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public List getIntegrityCheckAccountList() {
		return integrityCheckAccountList;
	}

	public void setIntegrityCheckAccountList(List integrityCheckAccountList) {
		this.integrityCheckAccountList = integrityCheckAccountList;
	}

	public IntegrityCheckAccountService getIntegrityCheckAccountService() {
		return integrityCheckAccountService;
	}

	public void setIntegrityCheckAccountService(
			IntegrityCheckAccountService integrityCheckAccountService) {
		this.integrityCheckAccountService = integrityCheckAccountService;
	}

	public IntegrityCheckAccount getAccount() {
		return account;
	}

	public void setAccount(IntegrityCheckAccount account) {
		this.account = account;
	}

	public List getCheckAccount() {
		return checkAccount;
	}

	public void setCheckAccount(List checkAccount) {
		this.checkAccount = checkAccount;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

}
