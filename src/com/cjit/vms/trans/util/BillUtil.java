package com.cjit.vms.trans.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.action.MakingBillAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;

public class BillUtil extends MakingBillAction{
	
	/**
	 * 合并开票时，基于交易信息列表构造票据信息
	 * 
	 * @param transList
	 * @return List<BillInfo>
	 */
	public List createBillListByTransList(List transList, User currentUser,
			Customer customer, Organization org) {
		List billList = new ArrayList();
		List billTransList = new ArrayList();
		BigDecimal billAmt = new BigDecimal(0);// 当前票据总金额

		int billGoodsNum = 0;// 当前票据总商品种类数
		String currTransGoods = "";// 当前商品
		// 从第一笔交易获取相关信息
		TransInfo transInfo = (TransInfo) transList.get(0);
		//税额补正
		BigDecimal taxAmtErr = transInfoService.selectTransTaxAmt(transInfo.getTransId());
		if (taxAmtErr != null) {
			transInfo.setTaxAmt(transInfo.getTaxAmt().subtract(taxAmtErr));
		}
		billAmt = billAmt.add(transInfo.getBalance());
		billGoodsNum = 1;
		billTransList.add(transInfo);
		currTransGoods = transInfo.getGoodsKey();
		BigDecimal billItemAmt = transInfo.getBalance();// 总金额
		BigDecimal billItemTaxAmt = new BigDecimal(0);// 总税额
		BigDecimal transSumTaxAmt = transInfo.getTaxAmt();// 各明细税额相加

		for (int i = 1; i < transList.size(); i++) {
			boolean createNewBill = false;
			// boolean appendNewGood = false;
			TransInfo trans = (TransInfo) transList.get(i);
			//税额补正
			taxAmtErr = transInfoService.selectTransTaxAmt(trans.getTransId());
			if (taxAmtErr != null) {
				trans.setTaxAmt(trans.getTaxAmt().subtract(taxAmtErr));
			}
			billAmt = billAmt.add(trans.getBalance());// 票据总金额
			if (billAmt.compareTo(DataUtil.billMaxAmt) >= 0) {
				// 发票总金额达到或超出最大值 将现有billTransList中的交易信息进行开票处理
				BillInfo bill = this.createBillByTransList(billTransList,
						currentUser, customer, org);
				billList.add(bill);
				createNewBill = true;
				billItemAmt = trans.getBalance(); // 总金额
				transSumTaxAmt = trans.getTaxAmt(); // 各明细税额相加
			} else {
				// 如果商品信息不一致
				if (!currTransGoods.equals(trans.getGoodsKey())) {
					// 判断是否重新开新发票
					// 交易对应商品不在现有开票交易集billTransList中
					if (billGoodsNum == 8) {
						// 已有8种商品 将现有billTransList中的交易信息进行开票处理
						BillInfo bill = this.createBillByTransList(billTransList,
								currentUser, customer, org);
						billList.add(bill);
						createNewBill = true;
						// 判断发票总金额
					} else {
						// 新商品
						billGoodsNum = billGoodsNum + 1;
						// 需要另起一行开新的商品信息
						currTransGoods = trans.getGoodsKey();
						// appendNewGood = true;
					}
					billItemAmt = trans.getBalance(); // 总金额
					transSumTaxAmt = trans.getTaxAmt(); // 各明细税额相加
				} else {
					// 交易对应商品在现有开票交易集billTransList中
					// 即使商品信息相同，当误差过大时，也需换行。
					// 判断轧差金额
					// 依票据明细金额和税率计算得到的税额
					billItemAmt = billItemAmt.add(trans.getBalance());
					billItemTaxAmt = DataUtil.calculateTaxAmt(billItemAmt, trans.getTaxRate(),
							"base");
					// 票据明细对应交易加和计算得到的税额
					transSumTaxAmt = transSumTaxAmt.add(trans.getTaxAmt());
					// 以上两税额之差
					BigDecimal diff = (billItemTaxAmt.subtract(transSumTaxAmt))
							.abs();
					if (diff.compareTo(DataUtil.different) >= 0) {
						// 判断是否重新开新发票
						// 交易对应商品不在现有开票交易集billTransList中
						if (billGoodsNum == 8) {
							// 已有8种商品 将现有billTransList中的交易信息进行开票处理
							BillInfo bill = this.createBillByTransList(billTransList,
									currentUser, customer, org);
							billList.add(bill);
							createNewBill = true;
							// 判断发票总金额
						} else {
							// 新商品
							billGoodsNum = billGoodsNum + 1;
							// 需要另起一行开新的商品信息
							currTransGoods = trans.getGoodsKey();
							// appendNewGood = true;
						}
						billItemAmt = trans.getBalance(); // 总金额
						transSumTaxAmt = trans.getTaxAmt(); // 各明细税额相加
					}
				}
			}

			// 判断当前交易是否为新票据中交易
			if (createNewBill) {
				// 新票据
				billGoodsNum = 1;// 当前票据总商品种类数
				currTransGoods = trans.getGoodsKey();// 当前商品
				billTransList = new ArrayList();
			}
			billTransList.add(trans);
		}
		if (CollectionUtil.isNotEmpty(billTransList)) {
			BillInfo bill = this.createBillByTransList(billTransList,
					currentUser, customer, org);
			billList.add(bill);
		}
		return billList;
	}
	
	public void appendBillInfo(List billList, String billFapiaoType,
			String billDataStatus) throws Exception {
		try {
			if (CollectionUtil.isNotEmpty(billList)) {
				for (Iterator t = billList.iterator(); t.hasNext();) {
					BillInfo bill = (BillInfo) t.next();
					if (bill != null) {
						List billItemList = bill.getBillItemList();
						bill.setFapiaoType(billFapiaoType);// 发票类型
						if (StringUtil.isNotEmpty(billDataStatus)) {
							bill.setDataStatus(billDataStatus);// 发票状态
						}
						// 插入票据信息
						billInfoService.saveBillInfo(bill, false);
						if (CollectionUtil.isNotEmpty(billItemList)) {
							for (Iterator it = billItemList.iterator(); it
									.hasNext();) {
								BillItemInfo billItem = (BillItemInfo) it
										.next();
								if (billItem != null) {
									List biTransList = billItem.getTransList();
									// 插入票据明细信息
									billInfoService.saveBillItemInfo(billItem,
											false);
									if (CollectionUtil.isNotEmpty(biTransList)) {
										for (Iterator bit = biTransList
												.iterator(); bit.hasNext();) {
											TransInfo trans = (TransInfo) bit
													.next();
											// 插入交易票据对应信息
											transInfoService.saveTransBill(
													trans.getTransId(),
													billItem.getBillId(),
													billItem.getBillItemId(),
													trans.getBalance(),
													trans.getTaxAmt(),
													trans.getBalance());
											// 更新所选交易状态为开票编辑锁定中、未开票金额为零
											trans.setBalance(new BigDecimal(
													0.00));
											trans
													.setDataStatus(DataUtil.TRANS_STATUS_2);
											transInfoService
													.updateTransInfoForCancel(trans);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 基于已整理好的交易列表，构造票据信息
	 * 
	 * @param transList
	 * @return BillInfo
	 */
	private BillInfo createBillByTransList(List transList, User currentUser,
			Customer customer, Organization org) {
		BillInfo billInfo = new BillInfo();// 票据实体
		List billItemList = new ArrayList();// 票据明细列表
		if (CollectionUtil.isNotEmpty(transList)) {
			String billId = createBillId("B");// 票据ID
			BigDecimal billAmt = new BigDecimal(0);// 票据总金额
			BigDecimal billTaxAmt = new BigDecimal(0);// 票据总税额
			TransInfo transInfo = (TransInfo) transList.get(0);

			BillItemInfo billItem = new BillItemInfo();
			List billItemTransList = new ArrayList();
			billItem.setBillId(billId);// 票据ID
			billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
			billItem.setGoodsName(transInfo.getGoodsInfo().getGoodsName());// 商品名称
			billItem.setSpecandmodel(transInfo.getGoodsInfo().getModel());// 商品型号
			billItem.setGoodsUnit(transInfo.getGoodsInfo().getUnit());// 商品单位
			billItem.setGoodsNo(new BigDecimal(1));// 商品数量
			billItem.setTaxFlag(transInfo.getTaxFlag());// 含税标志 Y/N
			billItem.setAmt(transInfo.getBalance());// 金额(使用该笔交易未开票金额)
			billItem.setTaxRate(transInfo.getTaxRate());// 税率
			billItem.setTaxAmt(transInfo.getTaxAmt());// 税额
			billItem.setTaxItem(transInfo.getTaxId());// 税目
			billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
			billItem.setGoodsPrice(transInfo.getBalance());// 商品单价
			billItemTransList.add(transInfo);

			billAmt = transInfo.getBalance();
			billTaxAmt = transInfo.getTaxAmt();

			BigDecimal billItemTaxAmt = new BigDecimal(0);// 总税额
			BigDecimal transSumTaxAmt = billTaxAmt;// 各明细税额相加
			String goodsKey = transInfo.getGoodsKey();
			for (int i = 1; i < transList.size(); i++) {
				TransInfo trans = (TransInfo) transList.get(i);
				billAmt = billAmt.add(trans.getBalance());
				billTaxAmt = billTaxAmt.add(trans.getTaxAmt());
				if (!goodsKey.equalsIgnoreCase(trans.getGoodsKey())) {
					billItem.setTransList(billItemTransList);
					billItemList.add(billItem);

					billItem = new BillItemInfo();
					billItemTransList = new ArrayList();
					billItem.setBillId(billId);// 票据ID
					billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
					billItem.setGoodsName(trans.getGoodsInfo().getGoodsName());// 商品名称
					billItem.setGoodsNo(new BigDecimal(1));// 商品数量
					billItem.setTaxFlag(trans.getTaxFlag());// 含税标志 Y/N
					billItem.setAmt(trans.getBalance());// 金额(使用该笔交易未开票金额)
					billItem.setTaxRate(trans.getTaxRate());// 税率
					billItem.setTaxAmt(trans.getTaxAmt());// 税额
					billItem.setTaxItem(trans.getTaxId());// 税目
					billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
					billItem.setGoodsPrice(trans.getBalance());// 商品单价
					billItemTransList.add(trans);
					goodsKey = trans.getGoodsKey();
					// 误差清零，各明细税额相加
					transSumTaxAmt = trans.getTaxAmt();
				} else {
					// 交易对应商品在现有开票交易集billTransList中
					// 即使商品信息相同，当误差过大时，也需换行。
					// 判断轧差金额
					// 依票据明细金额和税率计算得到的税额
					billItemTaxAmt = DataUtil.calculateTaxAmt(billItem.getAmt()
							.add(trans.getBalance()), billItem.getTaxRate(),
							"base");
					// 票据明细对应交易加和计算得到的税额
					transSumTaxAmt = transSumTaxAmt.add(trans.getTaxAmt());
					// 以上两税额之差
					BigDecimal diff = (billItemTaxAmt.subtract(transSumTaxAmt))
							.abs();
					if (diff.compareTo(DataUtil.different) >= 0) {
						// 需要另起一行开新的商品信息
						billItem.setTransList(billItemTransList);
						billItemList.add(billItem);

						billItem = new BillItemInfo();
						billItemTransList = new ArrayList();
						billItem.setBillId(billId);// 票据ID
						billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
						billItem.setGoodsName(trans.getGoodsInfo()
								.getGoodsName());// 商品名称
						billItem.setGoodsNo(new BigDecimal(1));// 商品数量
						billItem.setTaxFlag(trans.getTaxFlag());// 含税标志 Y/N
						billItem.setAmt(trans.getBalance());// 金额(使用该笔交易未开票金额)
						billItem.setTaxRate(trans.getTaxRate());// 税率
						billItem.setTaxAmt(trans.getTaxAmt());// 税额
						billItem.setTaxItem(trans.getTaxId());// 税目
						billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
						billItem.setGoodsPrice(trans.getBalance());// 商品单价
						billItemTransList.add(trans);

						goodsKey = trans.getGoodsKey();
						// 误差清零，各明细税额相加
						transSumTaxAmt = trans.getTaxAmt();
					} else {
						billItem.setGoodsPrice(billItem.getGoodsPrice().add(
								trans.getBalance()));// 商品单价
						billItem.setAmt(billItem.getAmt().add(
								trans.getBalance()));// 金额
						billItem.setTaxAmt(billItem.getTaxAmt().add(trans.getTaxAmt()));// 税额
						billItemTransList.add(trans);
					}
				}
			}
			if (billItem != null && !billItemList.contains(billItem)) {
				if (CollectionUtil.isEmpty(billItem.getTransList())
						&& CollectionUtil.isNotEmpty(billItemTransList)) {
					billItem.setTransList(billItemTransList);
				}
				billItemList.add(billItem);
			}
			// 查询交易对应客户信息 以及 当前用户所属机构信息
			if (customer == null) {
				customer = customerService.findCustomer(transInfo
						.getCustomerId(), null, null, null, null);
			}
			if (org == null) {
				org = organizationService.getOrganization(new Organization(
						transInfo.getInstCode()));
			}
			billInfo.setBillId(billId);
			billInfo.setApplyDate(DateUtils.toString(new Date(),
					DateUtils.ORA_DATES_FORMAT));// 申请开票日期(在界面体现为申请开票日期)
			//billInfo.setApplyDate(billInfo.getBillDate());// 应用日期
			billInfo.setCustomerId(customer.getCustomerID());
			billInfo.setCustomerName(customer.getCustomerCName());// 客户纳税人名称
			billInfo.setCustomerTaxno(customer.getCustomerTaxno());// 客户纳税人识别号
			billInfo.setCustomerAddressandphone(customer.getCustomerAddress()
					+ " " + customer.getCustomerPhone());// 客户地址电话
			billInfo.setCustomerBankandaccount(customer.getCustomerCBank() + transInfo
					.getCustomerAccount());// 客户银行账号
			billInfo.setAmtSum(billAmt);// 合计金额
			billInfo.setTaxAmtSum(billTaxAmt);// 合计税额
			billInfo.setSumAmt(billAmt.add(billTaxAmt));// 价税合计
			if (currentUser != null) {
				billInfo.setDrawer(currentUser.getId());// 开票人
				billInfo.setDrawerName(currentUser.getName());// 开票人
			}
			billInfo.setName(org.getTaxperName());// 我方纳税人名称
			billInfo.setTaxno(org.getTaxperNumber());// 我方纳税人识别号
			billInfo.setAddressandphone(org.getAddress() + " " + org.getTel());// 我方地址电话
			billInfo.setBankandaccount(org.getTaxBank() + org.getAccount());// 我方银行账号
			billInfo.setInstCode(org.getId());// 所属机构
			billInfo.setDataStatus(DataUtil.BILL_STATUS_1);// 状态-待提交
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_2);// 是否手工录入2-人工审核
			billInfo.setIssueType(DataUtil.ISSUE_TYPE_2); // 发票开具类型2-合并
			billInfo.setBillItemList(billItemList);
		}
		return billInfo;
	}

	public String getBillFapiaoType(TransInfo transInfo) {
		boolean isVAT = false;// 为专票
		//交易是否为专票
		if (DataUtil.VAT_TYPE_0.equals(transInfo.getFapiaoType())) {
			isVAT = true;
		}
		// 客户为传票且为国内客户 最终确定为专票
		
		String chnFlg = "CHN,Z01,Z02,Z03";//国内标识
		
		if (isVAT) {
			if (DataUtil.VAT_TYPE_0.equals(transInfo.getCustomerFaPiaoType())) {

				String nationality = transInfo.getCustomerNationality();
				if(null!=nationality){
					if (chnFlg.indexOf(nationality) > -1) {
						return DataUtil.VAT_TYPE_0; // 发票类型-增值税专用发票
					}
				}
				
			}
		}

		return DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票

	}
	
	
	public String getTaxType(String instCode){
		Organization org = new Organization();
		org.setId(instCode);
		org = this.baseDataService.getOrganization(org);
		return org.getTaxType();
	}
}
