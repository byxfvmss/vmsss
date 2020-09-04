package com.cjit.vms.trans.service.createBill.billContex;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cjit.crms.util.DateUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.GoodsCollectionInfo;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.util.DataUtil;

public class BillContext {
	protected BillInfo billInfo = new BillInfo();
	protected List<BillGoodsInfo> goodsInfoList = new ArrayList<BillGoodsInfo>(); // 商品信息列表

	// public BillContext() {
	// // TODO Auto-generated constructor stub
	// }

	/***
	 * 通过交易直接生成一张票据 （包括商品和交易关联）
	 * 
	 * @param billId
	 * @param billItemId
	 * @param billStatus
	 *            状态-待提交
	 * @param isHandiwork
	 *            是否手工录入2-人工审核
	 * @param isSueType
	 *            发票开具类型1-单笔
	 * @param transInfo
	 */
	public BillContext(String billId, String billItemId, String billStatus,
			String isHandiwork, String isSueType, TransInfo transInfo) {

		billInfo.setBillId(billId);
		billInfo.setFapiaoType(transInfo.getFapiaoType());// 发票类型
		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));

		Customer customer = transInfo.getCustomer();
		Organization org = transInfo.getOrganization();

		BillGoodsInfo billGoodsInfo = new BillGoodsInfo(billItemId, transInfo);

		setBillCustomerInfo(customer);
		setBillOrgInfo(org);

		addGoodsInfo(billGoodsInfo);
		writeBackBillAmt(transInfo);
	}

	/***
	 * 
	 * @param billId
	 * @param billGoodsInfo
	 * @param transInfo
	 */
	public BillContext(String billId, BillGoodsInfo billGoodsInfo,
			TransInfo transInfo) {
		
		billInfo.setBillId(billId);
		billInfo.setInsureId(transInfo.getCherNum());
		//billInfo.setCherNum(transInfo.getCherNum());
		billInfo.setFeeTyp(transInfo.getFeeTyp());
		billInfo.setTtmpRcno(transInfo.getTtmpRcno());
		billInfo.setRepNum(transInfo.getRepNum());
		billInfo.setBillFreq(transInfo.getBillFreq());
		billInfo.setHissDte(transInfo.getHissDte() == null ? "" : transInfo.getHissDte());
		billInfo.setDsouRce(transInfo.getDsouRce());
		billInfo.setChanNel(transInfo.getChanNel());
		billInfo.setHisToryFlag(DataUtil.TRANSFERFLAG_0);
		billInfo.setRemark(transInfo.getRemark());
		
		billInfo.setFapiaoType(transInfo.getFapiaoType());// 发票类型
		this.billInfo.setPayee(transInfo.getPayee());
		//2019-同步生产
		billInfo.setPayee(transInfo.getPayee());
	    billInfo.setReviewer(transInfo.getReviewer());
		billInfo.setDrawer(transInfo.getDrawer());
		Customer customer = transInfo.getCustomer();
		Organization org = transInfo.getOrganization();
		 if (customer.getCustomerType().equals("I"))
		    {
		     customer.setCustomerTaxno("");    
		     customer.setCustomerAccount("");
		     customer.setCustomerCBank("");
	         customer.setCustomerAddress("");
		     customer.setCustomerPhone("");
		  }
		setBillCustomerInfo(customer);
		setBillOrgInfo(org);

		addGoodsInfo(billGoodsInfo);
		writeBackBillAmt(transInfo);
	}

	/***
	 * 取得票据当前金额不含税
	 * 
	 * @return
	 */
	public BigDecimal getAmtSum() {

		if (null != billInfo.getAmtSum()) {
			return new BigDecimal("0");
		} else {

			return billInfo.getAmtSum();
		}

	}

	/***
	 * 取得票据当前税额
	 * 
	 * @return
	 */
	public BigDecimal getTaxAmtSum() {
		if (null != billInfo.getTaxAmtSum()) {
			return new BigDecimal("0");
		} else {
			return billInfo.getTaxAmtSum();
		}
	}

	/***
	 * 取得票据当前价税合计
	 * 
	 * @return
	 */
	public BigDecimal getSumAmt() {
		if (null == billInfo.getSumAmt()) {
			return new BigDecimal("0");
		} else {
			return billInfo.getSumAmt();
		}
	}

//	public void addTransInfo(String billItemId, TransInfo transInfo) {
//		BigDecimal amtSum = billInfo.getAmtSum();// 合计金额
//		BigDecimal taxAmtSum = billInfo.getTaxAmtSum();// 合计税额
//		BigDecimal sumAmt = billInfo.getSumAmt();// 价税合计
//
//		BigDecimal balanceIncomeCny = transInfo.getBalanceIncomeCny();
//		BigDecimal taxCnyBalance = transInfo.getTaxCnyBalance();
//		BigDecimal balance = transInfo.getBalance();
//
//		billInfo.setAmtSum(balanceIncomeCny.add(amtSum));// 合计金额
//		billInfo.setTaxAmtSum(taxCnyBalance.add(taxAmtSum));// 合计税额
//		billInfo.setSumAmt(balance.add(sumAmt));// 价税合计
//
//	}

	/***
	 * 取得商品数量
	 * 
	 * @return
	 */
	public int getGoodsInfoSize() {
		return getGoodsInfoList().size();
	}

	public List<BillGoodsInfo> getSameGoodsInfoList(String goodsKey) {
		List<BillGoodsInfo> listGoods = new ArrayList<BillGoodsInfo>();
		for (int i = 0; i < getGoodsInfoList().size(); i++) {
			BillGoodsInfo billGoodsInfo = getGoodsInfoList().get(i);
			String billGoodsId = billGoodsInfo.getGoodsKey();
			if (billGoodsId.equals(goodsKey)) {
				listGoods.add(billGoodsInfo);
			}
		}
		return listGoods;
	}

	/***
	 * 取得当前所有商品税率的合计
	 * 
	 * @return
	 */
	public BigDecimal getGoodsTaxAmtSum() {
		BigDecimal goodsTaxAmtSum = new BigDecimal("0");
		for (int i = 0; i < getGoodsInfoList().size(); i++) {
			BillGoodsInfo billGoodsInfo = getGoodsInfoList().get(i);
			goodsTaxAmtSum = goodsTaxAmtSum.add(billGoodsInfo.getTaxAmt());
		}
		return goodsTaxAmtSum;
	}

	public void addGoodsInfo(BillGoodsInfo billGoodsInfo) {
		
		billGoodsInfo.setBillId(billInfo.getBillId());
		getGoodsInfoList().add(billGoodsInfo);

	}

	// 回写金额
	public void writeBackBillAmt(TransInfo transInfo) {
//		billInfo.setTaxAmtSum(transInfo.getTaxCnyBalance());// 合计税额
//		billInfo.setSumAmt(transInfo.getBalance());// 价税合计
//		billInfo.setAmtSum(transInfo.getBalanceIncomeCny());// 合计金额
		
		BigDecimal amtSum = billInfo.getAmtSum();// 合计金额
		BigDecimal taxAmtSum = billInfo.getTaxAmtSum();// 合计税额
		BigDecimal sumAmt = billInfo.getSumAmt();// 价税合计

		BigDecimal balanceIncomeCny = transInfo.getBalanceIncomeCny();
		BigDecimal taxCnyBalance = transInfo.getTaxCnyBalance();
		BigDecimal balance = transInfo.getBalance();

		billInfo.setAmtSum(balanceIncomeCny.add(amtSum));// 合计金额
		billInfo.setTaxAmtSum(taxCnyBalance.add(taxAmtSum));// 合计税额
		billInfo.setSumAmt(balance.add(sumAmt));// 价税合计
	}

	// public BillContext(Customer customer, Organization org) {
	// setBillCustomerInfo(customer);
	// setBillOrgInfo(org);
	// }

	/***
	 * 构造票据购买方客户信息
	 * 
	 * @param customer
	 */
	public void setBillCustomerInfo(Customer customer) {
		   if (customer.getCustomerType().equals("I"))
		   {
		      this.billInfo.setTaxno("");
		      this.billInfo.setAddressandphone("");
		       this.billInfo.setBankandaccount("");
		    }

		billInfo.setCustomerId(customer.getCustomerID());
		billInfo.setCustomerName(customer.getCustomerCName());// 客户纳税人名称
		billInfo.setCustomerTaxno(customer.getCustomerTaxno());// 客户纳税人识别号
		billInfo.setCustomerAddressandphone(customer.getCustomerAddress() + " "
				+ customer.getCustomerPhone());// 客户地址电话
		billInfo.setCustomerBankandaccount(customer.getCustomerCBank()
				+ customer.getCustomerAccount());// 客户银行账号
	}

	/***
	 * 构造票据销方信息
	 * 
	 * @param org
	 */
	public void setBillOrgInfo(Organization org) {
		billInfo.setName(org.getTaxperName());// 我方纳税人名称
		billInfo.setTaxno(org.getTaxperNumber());// 我方纳税人识别号
		billInfo.setAddressandphone(org.getTaxAddress() + " " + org.getTaxTel());// 我方地址电话
		billInfo.setBankandaccount(org.getTaxBank() + org.getAccount());// 我方银行账号
		billInfo.setInstcode(org.getId());// 所属机构

	}

	public BillInfo getBillInfo() {
		if (null == billInfo) {
			billInfo = new BillInfo();
		}
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public List<BillGoodsInfo> getGoodsInfoList() {
		if (null == goodsInfoList) {
			goodsInfoList = new ArrayList<BillGoodsInfo>();
		}
		return goodsInfoList;
	}

	public void setGoodsInfoList(List<BillGoodsInfo> goodsInfoList) {
		this.goodsInfoList = goodsInfoList;
	}

}
