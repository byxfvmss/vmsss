package com.cjit.vms.trans.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.util.BillUtil;
import com.cjit.vms.trans.util.DataUtil;

public class MakingBillAction extends TransAction{
	
	private BillUtil billUtil;
	
	public void selectTransToOneBill() throws Exception {
		String transIds = "";
		int count = 0;
		int counts = 1;
		String result = "";
		try {
			List transList = new ArrayList();// 选中合并交易列表
			User currentUser = this.getCurrentUser();
			StringBuffer sbMessage = new StringBuffer();
			String billFapiaoType = null; // 发票类型
			String[] selectCustomers = request.getParameter("selectCustomers").split(",");
			if(selectCustomers!=null&&selectCustomers.length>0){
				for(int i=0;i<selectCustomers.length;i++){
					if(!selectCustomers[0].equals(selectCustomers[i])){
						counts ++ ;
					}
				}
			}
				
			
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String[] selectTransIds = request.getParameter("selectTransIds").split(",");
			this.setSelectTransIds(selectTransIds);
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				BigDecimal balance = new BigDecimal(0.0);
				TreeMap treeMap = new TreeMap();
				// 循环获取交易信息
				for (int i = 0; i < this.selectTransIds.length; i++) {
					BillInfo info = new BillInfo();
					info.setTransId(this.selectTransIds[i]);
					info.setLstAuthInstId(lstAuthInstId);
//					//获取相关联的客户ID
//					TransInfo transInfo = new TransInfo();
//					transInfo.setTransId(info.getTransId());
//					List list = transInfoService.findTransCustomerList(transInfo);
//					//去重复
//					Map map = new HashMap();
//					for(int j = 0; j < list.size(); j ++){
//						String customerId = ((Customer)list.get(j)).getCustomerID();
//						map.put(customerId, customerId);
//					}
//					Object transCustomerList[] = map.keySet().toArray();
////					transInfo.setTransCustomerList(transCustomerList);
//					info.setCustomerList(transCustomerList);
					TransInfo trans = this.transInfoService
							.findTransInfo(info);
					if (trans != null) {
						transIds += trans.getTransId() + ",";
						// 判断交易信息状态
						if (!DataUtil.TRANS_STATUS_1.equals(trans
								.getDataStatus())) {
							if (sbMessage.toString()
									.indexOf("TransStatusError") < 0) {
								sbMessage.append(" TransStatusError ");
							}
							continue;
						}
						if (trans.getBalance() != null) {
							// 计算开票合计
							balance = balance.add(trans.getBalance());
						}
						// 判断交易金额是否超出票据最大金额限制
						if (balance.compareTo(DataUtil.billMaxAmt) >= 0) {
							// 交易金额大于票据最大金额限制，需对该笔进行拆分开票
							if (sbMessage.toString().indexOf("AmtOverFull") < 0) {
								sbMessage.append(" AmtOverFull ");
							}
							continue;
						}
						String billFapiaoTypeOld = billFapiaoType;
//						if (DataUtil.VAT_TYPE_0.equals(trans.getFapiaoType())
//								&& DataUtil.VAT_TYPE_0.equals(trans
//										.getCustomerFaPiaoType())) {
//							billFapiaoType = DataUtil.VAT_TYPE_0;// 发票类型-增值税专用发票
//						} else {
//							billFapiaoType = DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票
//						}
						billFapiaoType = billUtil.getBillFapiaoType(trans);
						if (billFapiaoTypeOld != null && !billFapiaoType.equals(billFapiaoTypeOld)) {
							// 所选交易之发票类型不一致，不能合并开票！
							if (sbMessage.toString()
									.indexOf(" ErrorBillFapiaoType ") < 0) {
								sbMessage.append(" ErrorBillFapiaoType ");
							}
							continue;
						}
						trans.setUserId(this.getCurrentUser().getId());
						// 税目校验，验证税率存在与税目信息表中
						String taxId = checkTaxRate(trans, null, billFapiaoType);
						if (StringUtil.IsEmptyStr(taxId)) {
							// 当前交易税率不在税目信息表中，不能进行开票
							if (sbMessage.toString()
									.indexOf("NotExistsTaxRate") < 0) {
								sbMessage.append(" NotExistsTaxRate ");
							}
							continue;
						} else {
							trans.setTaxId(taxId);
						}
						// 获取交易对应商品信息
						GoodsInfo goods = this.findGoodsInfo(trans, null);
						if (goods != null) {
							trans.setGoodsInfo(goods);
						} else {
							// 当前商品信息不存在，不能进行开票
							if (sbMessage.toString()
									.indexOf("NotExistsGoods") < 0) {
								sbMessage.append(" NotExistsGoods ");
							}
							continue;
						}
						treeMap.put(trans.getGoodsKey() + "-"
								+ trans.getTransId(), trans);
					} else {
						sbMessage.append(" NotExistsTrans ");
					}
				}
				// 依交易对应商品信息对交易列表进行排序
				Collection coll = treeMap.values();
				Object[] objects = new Object[coll.size()];
				coll.toArray(objects);
				count += objects.length;
				if (objects != null && objects.length > 0) {
					for (int i = 0; i < objects.length; i++) {
						TransInfo ti = (TransInfo) objects[i];
						transList.add(ti);
					}
				}
			}
			if (sbMessage != null && sbMessage.toString().length() > 0) {
				this.message = sbMessage.toString();
				this.request.setAttribute("message", sbMessage.toString());
				this.setMessage(sbMessage.toString());
			}
			List billList = new ArrayList();
			if (CollectionUtil.isNotEmpty(transList)) {
				billList = billUtil.createBillListByTransList(transList,
						currentUser, null, null);
			}
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			if(billList.size()!=0){
				result = "预计 :  \n客户数 : "+counts+"个 , 交易数 : "+ 
						transList.size()+"条\n预计开票数 : " 
						+ billList.size() +"条 . 请确认是否继续!";
			}else{
				result = "N";
			}
			out.print(result);
			out.close();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "申请开票", "合并开票", "将交易流水号为("
							+ transIds.substring(0, transIds.length())
							+ ")的交易合并开票", "1");
		} catch (Exception e) {
			
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print("error");
			out.close();
			
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "申请开票", "合并开票", "将交易流水号为("
							+ transIds.substring(0, transIds.length())
							+ ")的交易合并开票", "0");
			log.error("TransInfoAction-transToOneBill", e);
			throw e;
			
		
		}
	}

	/**
	 * 开票（选中多笔交易，每笔交易开具一张发票）
	 * 
	 * @return String
	 */
	public String transToEachBill() throws Exception {
		String transIds = "";
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			List billItemList = new ArrayList();
			User currentUser = this.getCurrentUser();
			StringBuffer sbMessage = new StringBuffer();
			String billFapiaoType = "";
			int transSuccess = 0;
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				// 循环查询选中交易信息
				for (int i = 0; i < this.selectTransIds.length; i++) {
					BillInfo info = new BillInfo();
					info.setTransId(this.selectTransIds[i]);
					info.setLstAuthInstId(lstAuthInstId);
					
//					//获取相关联的客户ID
//					TransInfo trans = new TransInfo();
//					transInfo.setTransId(info.getTransId());
//					List list = transInfoService.findTransCustomerList(trans);
//					//去重复
//					Map map = new HashMap();
//					for(int j = 0; j < list.size(); j ++){
//						String customerId = ((Customer)list.get(j)).getCustomerID();
//						map.put(customerId, customerId);
//					}
//					Object transCustomerList[] = map.keySet().toArray();
////					transInfo.setTransCustomerList(transCustomerList);
//					info.setCustomerList(transCustomerList);
					TransInfo transInfo = transInfoService
							.findTransInfo(info);
					if (transInfo != null) {
						transIds += transInfo.getTransId() + ",";
						// 判断交易信息状态
						if (!DataUtil.TRANS_STATUS_1.equals(transInfo
								.getDataStatus())) {
							if (sbMessage.toString()
									.indexOf("TransStatusError") < 0) {
								sbMessage.append(" TransStatusError ");
							}
							continue;
						}
						// 依交易金额判断是否超过单张发票最大限额 暂默认100000
						if (transInfo.getBalance().compareTo(
								DataUtil.billMaxAmt) > 0) {
							if (sbMessage.toString().indexOf("AmtOverFull") < 0) {
								sbMessage.append(" AmtOverFull ");
							}
							continue;
						}
						// 依交易和客户的发票类型，综合判断所开发票的发票类型
						/*
						 * if (DataUtil.VAT_TYPE_0.equals(transInfo
						 * .getFapiaoType()) &&
						 * DataUtil.VAT_TYPE_0.equals(transInfo
						 * .getCustomerFaPiaoType())) { billFapiaoType =
						 * DataUtil.VAT_TYPE_0;// 发票类型-增值税专用发票 } else {
						 * billFapiaoType = DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票
						 * }
						 */
						billFapiaoType = billUtil.getBillFapiaoType(transInfo);
						
						transInfo.setUserId(this.getCurrentUser().getId());
						// 税目校验，验证税率存在与税目信息表中
						String taxId = checkTaxRate(transInfo, null,
								billFapiaoType);
						if (StringUtil.IsEmptyStr(taxId)) {
							// 当前交易税率不在税目信息表中，不能进行开票
							if (sbMessage.toString()
									.indexOf("NotExistsTaxRate") < 0) {
								sbMessage.append(" NotExistsTaxRate ");
							}
							continue;
						}
						// 根据交易类型和纳税人识别号获取发票商品信息
						GoodsInfo goods = this.findGoodsInfo(transInfo,
								transInfo.getBankTaxperNumber());
						if (goods == null) {
							if (sbMessage.toString().indexOf("NotExistsGoods") < 0) {
								sbMessage.append(" NotExistsGoods ");
							}
							continue;
						}
						//税额补正
						BigDecimal taxAmtErr = transInfoService.selectTransTaxAmt(transInfo.getTransId());
						if (taxAmtErr != null) {
							transInfo.setTaxAmt(transInfo.getTaxAmt().subtract(taxAmtErr));
						}
//						System.out.println(this.getCurrentUser().getOrgId());
						String billId = createBillId("B");// 票据ID
						BillItemInfo billItem = new BillItemInfo();
						billItem.setBillId(billId);// 票据ID
						billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
						billItem.setGoodsName(goods.getGoodsName());// 商品名称
						billItem.setSpecandmodel(goods.getModel());// 商品型号
						billItem.setGoodsUnit(goods.getUnit());// 商品单位
						billItem.setGoodsNo(new BigDecimal(1));// 商品数量
						billItem.setTaxFlag(transInfo.getTaxFlag());// 含税标志 Y/N
						billItem.setAmt(transInfo.getBalance());// 金额(使用该笔交易未开票金额)
						billItem.setTaxRate(transInfo.getTaxRate());// 税率
						billItem.setTaxAmt(transInfo.getTaxAmt());// 税额
						billItem.setTaxItem(taxId);// 税目
						billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
						billItem.setGoodsPrice(transInfo.getBalance());// 商品单价
						billItem.setTransInfo(transInfo);
						billItemList.add(billItem);
						// 查询交易对应客户信息 以及 当前用户所属机构信息
						Customer customer = customerService.findCustomer(
								transInfo.getCustomerId(), null, null, null,
								null);
						Organization org = organizationService
								.getOrganization(new Organization(transInfo
										.getInstCode()));
						if (billItemList != null && billItemList.size() > 0
								&& customer != null && org != null) {
							// 构造票据信息
							BillInfo billInfo = new BillInfo();
							billInfo.setBillId(billId);
							billInfo.setApplyDate(DateUtils.toString(new Date(),
									DateUtils.ORA_DATES_FORMAT));// 申请开票日期(在界面体现为申请开票日期)
							//billInfo.setApplyDate(billInfo.getBillDate());// 应用日期
							billInfo.setCustomerId(customer.getCustomerID());
							billInfo.setCustomerName(customer
									.getCustomerCName());// 客户纳税人名称
							billInfo.setCustomerTaxno(customer
									.getCustomerTaxno());// 客户纳税人识别号
							billInfo.setCustomerAddressandphone(customer
									.getCustomerAddress()
									+ " " + customer.getCustomerPhone());// 客户地址电话
							billInfo.setCustomerBankandaccount(customer.getCustomerCBank() + transInfo
									.getCustomerAccount());// 客户银行账号
							billInfo.setAmtSum(transInfo.getBalance());// 合计金额
							billInfo.setTaxAmtSum(transInfo.getTaxAmt());// 合计税额
							billInfo.setSumAmt(billInfo.getAmtSum().add(
									billInfo.getTaxAmtSum()));// 价税合计
							if (currentUser != null) {
								billInfo.setDrawer(currentUser.getId());// 开票人
								billInfo.setDrawerName(currentUser.getName());// 开票人
							}
							billInfo.setName(org.getTaxperName());// 我方纳税人名称
							billInfo.setTaxno(org.getTaxperNumber());// 我方纳税人识别号
							billInfo.setAddressandphone(org.getAddress() + " "
									+ org.getTel());// 我方地址电话
							billInfo.setBankandaccount(org.getTaxBank() + org.getAccount());// 我方银行账号
							billInfo.setInstCode(org.getId());// 所属机构
							billInfo.setDataStatus(DataUtil.BILL_STATUS_1);// 状态-待提交
							billInfo
									.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_2);// 是否手工录入2-人工审核
							billInfo.setIssueType(DataUtil.ISSUE_TYPE_1); // 发票开具类型1-单笔
							billInfo.setFapiaoType(billFapiaoType);// 发票类型
							// 插入票据信息
							billInfoService.saveBillInfo(billInfo, false);
							// 插入票据明细信息
							billInfoService.saveBillItemInfo(billItem, false);
							// 插入交易票据对应信息
							transInfoService.saveTransBill(transInfo
									.getTransId(), billItem.getBillId(),
									billItem.getBillItemId(),
									billItem.getAmt(), billItem.getTaxAmt(),
									billItem.getAmt());
							// 更新所选交易状态为开票编辑锁定中、未开票金额为零
							transInfo.setBalance(new BigDecimal(0.00));
							transInfo.setDataStatus(DataUtil.TRANS_STATUS_2);
							transInfoService.updateTransInfoForCancel(transInfo);
							transSuccess++;
						}
					} else {
						sbMessage.append(" NotExistsTrans ");
					}
				}
			}
			if (sbMessage != null && sbMessage.toString().length() > 0) {
				this.message = sbMessage.toString();
				this.request.setAttribute("message", sbMessage.toString());
				this.setMessage(sbMessage.toString());
				this.setFromFlag("bill");
				return ERROR;
			}
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "开票", "对交易ID为("
							+ transIds.substring(0, transIds.length() - 1)
							+ ")的交易开票 成功" + transSuccess + "笔", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "开票", "对交易ID为("
							+ transIds.substring(0, transIds.length() - 1)
							+ ")的交易开票", "0");
			log.error("TransInfoAction-transToEachBill", e);
			throw e;
		}
	}
	
	/**
	 * 合并开票(选中多笔交易，合并到一张或几张发票中) 本方法根据交易类型将多笔交易合并成一条票据明细
	 * 
	 * @return String
	 */
	public String transToOneBill() throws Exception {
		String transIds = "";
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			List transList = new ArrayList();// 选中合并交易列表
			User currentUser = this.getCurrentUser();
			StringBuffer sbMessage = new StringBuffer();
			String billFapiaoType = null; // 发票类型
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				BigDecimal balance = new BigDecimal(0.0);
				TreeMap treeMap = new TreeMap();
				// 循环获取交易信息
				for (int i = 0; i < this.selectTransIds.length; i++) {
					BillInfo info = new BillInfo();
					info.setTransId(this.selectTransIds[i]);
					info.setLstAuthInstId(lstAuthInstId);
//					//获取相关联的客户ID
//					TransInfo transInfo = new TransInfo();
//					transInfo.setTransId(info.getTransId());
//					List list = transInfoService.findTransCustomerList(transInfo);
//					//去重复
//					Map map = new HashMap();
//					for(int j = 0; j < list.size(); j ++){
//						String customerId = ((Customer)list.get(j)).getCustomerID();
//						map.put(customerId, customerId);
//					}
//					Object transCustomerList[] = map.keySet().toArray();
////					transInfo.setTransCustomerList(transCustomerList);
//					info.setCustomerList(transCustomerList);
					TransInfo trans = this.transInfoService
							.findTransInfo(info);
					if (trans != null) {
						transIds += trans.getTransId() + ",";
						// 判断交易信息状态
						if (!DataUtil.TRANS_STATUS_1.equals(trans
								.getDataStatus())) {
							if (sbMessage.toString()
									.indexOf("TransStatusError") < 0) {
								sbMessage.append(" TransStatusError ");
							}
							continue;
						}
						if (trans.getBalance() != null) {
							// 计算开票合计
							balance = balance.add(trans.getBalance());
						}
						// 判断交易金额是否超出票据最大金额限制
						if (balance.compareTo(DataUtil.billMaxAmt) >= 0) {
							// 交易金额大于票据最大金额限制，需对该笔进行拆分开票
							if (sbMessage.toString().indexOf("AmtOverFull") < 0) {
								sbMessage.append(" AmtOverFull ");
							}
							continue;
						}
						String billFapiaoTypeOld = billFapiaoType;
						/*if (DataUtil.VAT_TYPE_0.equals(trans.getFapiaoType())
								&& DataUtil.VAT_TYPE_0.equals(trans
										.getCustomerFaPiaoType())) {
							billFapiaoType = DataUtil.VAT_TYPE_0;// 发票类型-增值税专用发票
						} else {
							billFapiaoType = DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票
						}*/
						billFapiaoType = billUtil.getBillFapiaoType(trans);
						if (billFapiaoTypeOld != null && !billFapiaoType.equals(billFapiaoTypeOld)) {
							// 所选交易之发票类型不一致，不能合并开票！
							if (sbMessage.toString()
									.indexOf(" ErrorBillFapiaoType ") < 0) {
								sbMessage.append(" ErrorBillFapiaoType ");
							}
							continue;
						}
						trans.setUserId(this.getCurrentUser().getId());
						// 税目校验，验证税率存在与税目信息表中
						String taxId = checkTaxRate(trans, null, billFapiaoType);
						if (StringUtil.IsEmptyStr(taxId)) {
							// 当前交易税率不在税目信息表中，不能进行开票
							if (sbMessage.toString()
									.indexOf("NotExistsTaxRate") < 0) {
								sbMessage.append(" NotExistsTaxRate ");
							}
							continue;
						} else {
							trans.setTaxId(taxId);
						}
						// 获取交易对应商品信息
						GoodsInfo goods = this.findGoodsInfo(trans, null);
						if (goods != null) {
							trans.setGoodsInfo(goods);
						} else {
							// 当前商品信息不存在，不能进行开票
							if (sbMessage.toString()
									.indexOf("NotExistsGoods") < 0) {
								sbMessage.append(" NotExistsGoods ");
							}
							continue;
						}
						treeMap.put(trans.getGoodsKey() + "-"
								+ trans.getTransId(), trans);
					} else {
						sbMessage.append(" NotExistsTrans ");
					}
				}
				// 依交易对应商品信息对交易列表进行排序
				Collection coll = treeMap.values();
				Object[] objects = new Object[coll.size()];
				coll.toArray(objects);
				if (objects != null && objects.length > 0) {
					for (int i = 0; i < objects.length; i++) {
						TransInfo ti = (TransInfo) objects[i];
						transList.add(ti);
					}
				}
			}
			if (sbMessage != null && sbMessage.toString().length() > 0) {
				this.message = sbMessage.toString();
				this.request.setAttribute("message", sbMessage.toString());
				this.setMessage(sbMessage.toString());
				return ERROR;
			}
			List billList = new ArrayList();
			if (CollectionUtil.isNotEmpty(transList)) {
				billList = billUtil.createBillListByTransList(transList,
						currentUser, null, null);
				billUtil.appendBillInfo(billList, billFapiaoType,
						DataUtil.BILL_STATUS_1);
			}
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "申请开票", "合并开票", "将交易流水号为("
							+ transIds.substring(0, transIds.length() - 1)
							+ ")的交易合并开票", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "申请开票", "合并开票", "将交易流水号为("
							+ transIds.substring(0, transIds.length() - 1)
							+ ")的交易合并开票", "0");
			log.error("TransInfoAction-transToOneBill", e);
			throw e;
		}
	}
	
	/**
	 * 打开拆分开票弹出窗体
	 * 
	 * @return String
	 */
	public String splitTrans() {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if (this.selectTransIds != null && this.selectTransIds.length == 1) {
				BillInfo info = new BillInfo();
				info.setTransId(this.selectTransIds[0]);
				info.setLstAuthInstId(lstAuthInstId);
//				//获取相关联的客户ID
//				TransInfo transInfo = new TransInfo();
//				transInfo.setTransId(info.getTransId());
//				List list = transInfoService.findTransCustomerList(transInfo);
//				//去重复
//				Map map = new HashMap();
//				for(int j = 0; j < list.size(); j ++){
//					String customerId = ((Customer)list.get(j)).getCustomerID();
//					map.put(customerId, customerId);
//				}
//				Object transCustomerList[] = map.keySet().toArray();
////				transInfo.setTransCustomerList(transCustomerList);
//				info.setCustomerList(transCustomerList);
				TransInfo trans = transInfoService
						.findTransInfo(info);
				if (trans != null && trans.getBalance() != null) {
					BigDecimal amt = trans.getBalance();
					amt = amt.add(amt.multiply(trans.getTaxRate()));
					amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
					this.request.setAttribute("amt", amt.toString());
					this.request.setAttribute("transId", trans.getTransId());
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request
								.setAttribute("userId", currentUser.getId());
					}
				}
			}
			if (!"error".equalsIgnoreCase(fromFlag)) {
				this.setMessage(null);
			} else {
				fromFlag = null;
			}
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "拆分开票", "选中交易流水号为("
							+ this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "拆分开票", "选中交易流水号为("
							+ this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "0");
			log.error("TransInfoAction-transToManyBill", e);
		}
		return SUCCESS;
	}

	
	/**
	 * 执行拆分开票保存功能
	 * 
	 * @return String
	 */
	public String transToManyBill() throws Exception {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			User currentUser = this.getCurrentUser();
			StringBuffer sbMessage = new StringBuffer();
			if (StringUtil.isNotEmpty(this.transId) && this.money != null
					&& this.money.length > 0) {
				BillInfo info = new BillInfo();
				info.setTransId(this.transId);
				info.setLstAuthInstId(lstAuthInstId);
//				//获取相关联的客户ID
//				TransInfo trans = new TransInfo();
//				transInfo.setTransId(info.getTransId());
//				List list = transInfoService.findTransCustomerList(trans);
//				//去重复
//				Map map = new HashMap();
//				for(int j = 0; j < list.size(); j ++){
//					String customerId = ((Customer)list.get(j)).getCustomerID();
//					map.put(customerId, customerId);
//				}
//				Object transCustomerList[] = map.keySet().toArray();
////				transInfo.setTransCustomerList(transCustomerList);
//				info.setCustomerList(transCustomerList);
				// 查询交易信息
				TransInfo transInfo = transInfoService
						.findTransInfo(info);
				for(int i = 0; i < money.length; i++){
					BigDecimal amtSum = new BigDecimal(money[i]);
					BigDecimal taxRate = transInfo.getTaxRate();
					BigDecimal rate = new BigDecimal(1).add(taxRate);
					BigDecimal subSum = amtSum.multiply(taxRate);
					subSum = subSum.divide(rate,6);
					BigDecimal taxAmt = subSum.setScale(2,BigDecimal.ROUND_HALF_UP);
					amtSum = amtSum.subtract(taxAmt);
					String subMoney = amtSum.toString();
					money[i] = subMoney;
				}
				// 判断交易信息状态
				if (transInfo == null) {
					sbMessage.append(" NotExistsTrans ");
				} else {

					this.request.setAttribute("amt", transInfo.getBalance()
							.toString());
					this.request
							.setAttribute("transId", transInfo.getTransId());
					currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request
								.setAttribute("userId", currentUser.getId());
					}

					if (!DataUtil.TRANS_STATUS_1.equals(transInfo
							.getDataStatus())) {
						BigDecimal amt = transInfo.getBalance();
						amt = amt.add(amt.multiply(transInfo.getTaxRate()));
						amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
						this.request.setAttribute("amt", amt.toString());
						sbMessage.append(" TransStatusError ");
					}
				}
				
				// 依交易和客户的发票类型，综合判断所开发票的发票类型
				String billFapiaoType = "";
//				if (DataUtil.VAT_TYPE_0.equals(transInfo.getFapiaoType())
//						&& DataUtil.VAT_TYPE_0.equals(transInfo
//								.getCustomerFaPiaoType())) {
//					billFapiaoType = DataUtil.VAT_TYPE_0;// 发票类型-增值税专用发票
//				} else {
//					billFapiaoType = DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票
//				}
				billFapiaoType = billUtil.getBillFapiaoType(transInfo);
				transInfo.setUserId(this.getCurrentUser().getId());
				// 税目校验，验证税率存在与税目信息表中
				String taxId = checkTaxRate(transInfo, null, billFapiaoType);
				if (StringUtil.IsEmptyStr(taxId)) {
					// 当前交易税率不在税目信息表中，不能进行开票
					sbMessage.append(" NotExistsTaxRate ");
				}
				// 根据交易类型和纳税人识别号获取发票商品信息
				GoodsInfo goods = this.findGoodsInfo(transInfo, transInfo
						.getBankTaxperNumber());
				if (goods == null) {
					sbMessage.append(" NotExistsGoods ");
				}
				// 查询交易对应客户信息
				Customer customer = customerService.findCustomer(transInfo
						.getCustomerId(), null, null, null, null);
				Organization org = organizationService
						.getOrganization(new Organization(currentUser
								.getOrgId()));
				BigDecimal splitAmtSum = new BigDecimal(0.0);// 拆分金额合计
				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				for (int i = 0; i < this.money.length; i++) {
					BigDecimal transAmt = new BigDecimal(this.money[i]);
					// 拆分金额格式不对，请输入小数位最多2位的正数数字。
					if (transAmt.scale() > 2 || transAmt.compareTo(new BigDecimal(0.0)) < 0) {
						if (sbMessage.toString()
								.indexOf(" MoneyError ") < 0) {
							sbMessage.append(" MoneyError ");
						}
						continue;
					}
					// 依交易金额判断是否超过单张发票最大限额 暂默认100000
					if (transAmt.compareTo(DataUtil.billMaxAmt) > 0) {
						if (sbMessage.toString()
								.indexOf(" AmtOverFull ") < 0) {
							sbMessage.append(" AmtOverFull ");
						}
						continue;
					}
				}
				if (sbMessage != null && sbMessage.toString().length() > 0) {
					this.message = sbMessage.toString();
					this.request.setAttribute("message", sbMessage.toString());
					this.setMessage(sbMessage.toString());
					return ERROR;
				}
				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				// 合计税额
				BigDecimal taxAmt = new BigDecimal(0);
				// 税额计算用
				BigDecimal tax = new BigDecimal(0);
				for (int i = 0; i < this.money.length; i++) {
					BigDecimal transAmt = new BigDecimal(this.money[i]);
					splitAmtSum = splitAmtSum.add(transAmt);
					// 税额计算
					if (splitAmtSum.compareTo(transInfo.getBalance()) == 0) {
						BigDecimal taxAmtErr = transInfoService.selectTransTaxAmt(transInfo.getTransId());
						tax = transInfo.getTaxAmt().subtract(taxAmtErr);
					} else {
						tax = DataUtil.calculateTaxAmt(transAmt, transInfo.getTaxRate(), "base");
					}

					BigDecimal taxErr = DataUtil.calculateTaxAmt(transAmt, transInfo.getTaxRate(), null)
							.subtract(tax).divide((new BigDecimal(0.01)), 1, BigDecimal.ROUND_HALF_UP);
					// 抽取交易信息税额误差表
					if (transInfoService.selectTransTaxAmtErr(transInfo.getTransId()) != null) {
						taxErr = taxErr.add(transInfoService.selectTransTaxAmtErr(transInfo.getTransId()));
						// 如果误差过大则修改税额
						if (taxErr.compareTo(new BigDecimal(1)) >= 0) {
							tax = tax.subtract(new BigDecimal(0.01));
							taxErr = taxErr.subtract(new BigDecimal(1));
						} else if (taxErr.compareTo(new BigDecimal(-1)) <= 0) {
							tax = tax.subtract(new BigDecimal(-0.01));
							taxErr = taxErr.subtract(new BigDecimal(-1));
						}
						// 重新更新交易信息税额误差表
						transInfoService.updateTransTaxAmtErr(transInfo.getTransId(), taxErr);
					} else if (taxErr.compareTo(new BigDecimal(0)) != 0) {
						transInfoService.insertTransTaxAmtErr(transInfo.getTransId(), taxErr);
					}

					// 构造并保存票据信息
					BillInfo billInfo = new BillInfo();
					billInfo.setBillId(createBillId("B"));
					billInfo.setApplyDate(DateUtils.toString(new Date(),
							DateUtils.ORA_DATES_FORMAT));// 申请开票日期
					//billInfo.setApplyDate(billInfo.getBillDate());// 应用日期
					billInfo.setCustomerId(customer.getCustomerID());
					billInfo.setCustomerName(customer.getCustomerCName());// 客户纳税人名称
					billInfo.setCustomerTaxno(customer.getCustomerTaxno());// 客户纳税人识别号
					billInfo.setCustomerAddressandphone(customer
							.getCustomerAddress()
							+ " " + customer.getCustomerPhone());// 客户地址电话
					billInfo.setCustomerBankandaccount(customer
							.getCustomerCBank()
							+ " " + customer.getCustomerAccount());// 客户银行账号
					billInfo.setAmtSum(transAmt);// 合计金额=拆分金额
					billInfo.setTaxAmtSum(tax);// 合计税额
					billInfo.setSumAmt(billInfo.getAmtSum().add(
							billInfo.getTaxAmtSum()));// 价税合计
					if (currentUser != null) {
						billInfo.setDrawer(currentUser.getId());// 开票人
						billInfo.setDrawerName(currentUser.getName());// 开票人
					}
					billInfo.setName(org.getTaxperName());// 我方纳税人名称
					billInfo.setTaxno(org.getTaxperNumber());// 我方纳税人识别号
					billInfo.setAddressandphone(org.getAddress() + " "
							+ org.getTel());// 我方地址电话
					billInfo.setBankandaccount(org.getTaxBank() + org.getAccount());// 我方银行账号
					billInfo.setInstCode(org.getId());// 所属机构
					billInfo.setDataStatus(DataUtil.BILL_STATUS_1);// 状态-待提交
					billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_2);// 是否手工录入2-人工审核
					billInfo.setIssueType(DataUtil.ISSUE_TYPE_3); // 发票开具类型3-拆分
					billInfo.setFapiaoType(billFapiaoType);// 发票类型
					billInfoService.saveBillInfo(billInfo, false);

					// 合计税额累加
					taxAmt = taxAmt.add(billInfo.getTaxAmtSum());

					// 构造并保存票据明细信息
					BillItemInfo billItem = new BillItemInfo();
					billItem.setBillId(billInfo.getBillId());// 票据ID
					billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
					billItem.setGoodsName(goods.getGoodsName());// 商品名称
					billItem.setSpecandmodel(goods.getModel());// 商品型号
					billItem.setGoodsUnit(goods.getUnit());// 商品单位
					billItem.setGoodsNo(new BigDecimal(1));// 商品数量
					billItem.setTaxFlag(transInfo.getTaxFlag());// 含税标志 Y/N
					billItem.setAmt(transAmt);// 金额(使用该笔交易未开票金额)
					billItem.setTaxRate(transInfo.getTaxRate());// 税率
					billItem.setTaxAmt(billInfo.getTaxAmtSum());// 税额
					billItem.setTaxItem(taxId);// 税目
					billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
					billItem.setGoodsPrice(transAmt);// 商品单价
					billItem.setTransInfo(transInfo);
					billInfoService.saveBillItemInfo(billItem, false);
					// 插入交易票据对应信息
					transInfoService.saveTransBill(this.transId, billItem.getBillId(),
									billItem.getBillItemId(), transAmt,
									billItem.getTaxAmt(),
									transAmt);
				}
				// 未开票金额，剩余
				transInfo.setBalance(transInfo.getBalance().subtract(
						splitAmtSum));

				// 如果未开票金额为0，则更新状态为“开票编辑锁定中”
				// 如果未开票金额大于0，则更新状态为“未开票”
				if (transInfo.getBalance().compareTo(new BigDecimal(0.0)) == 0) {
					transInfo.setDataStatus(DataUtil.TRANS_STATUS_2);
				} else {
					transInfo.setDataStatus(DataUtil.TRANS_STATUS_1);
				}

				transInfoService.updateTransInfo(transInfo);
			}
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "拆分开票", "将交易流水号(" + this.transId
							+ ")的交易进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"00802:0003", "开票申请", "拆分开票", "将交易流水号(" + this.transId
							+ ")的交易进行拆分开票处理", "0");
			log.error("TransInfoAction-transToManyBill", e);
			throw e;
		}
		return SUCCESS;
	}

	public BillUtil getBillUtil() {
		return billUtil;
	}

	public void setBillUtil(BillUtil billUtil) {
		this.billUtil = billUtil;
	}
	
	
}
