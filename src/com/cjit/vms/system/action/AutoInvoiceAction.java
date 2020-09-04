package com.cjit.vms.system.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.AutoInvoiceParam;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.system.service.AutoInvoiceParamService;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.service.TaxDiskMonitorService;
import com.cjit.vms.trans.util.DataUtil;

public class AutoInvoiceAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private ParamConfigVmssService paramConfigVmssService;
	private TaxDiskMonitorService taxDiskMonitorService;
	public static final String WAY_OF_MAKE_OUT_AN_INVOICE = "WAY_OF_MAKE_OUT_AN_INVOICE";// 开票方式
	public static final String KIND_OF_MAKE_OUT_AN_INVOICE = "KIND_OF_MAKE_OUT_AN_INVOICE";// 开票种类
	public static final String BILL_STATUS = "BILL_STATUS";// 票据状态
	private static final String SYSTEM_ID = "00802";
	private final String AUTO_DRAWER = "system Auto";// 自动开票票据中开票人信息
	private List wayList;
	private List kindList;
	private List statusList;
	private String moaiWay = "";// 开票方式
	private String moaiKind = "";// 开票种类
	private String billStatus = "";// 票据状态
	// ist -------------
	public static final String BUSS_TYPE = "BUSS_TYPE";// 业务种类（对于保险行业之险种）
	public static final String COST_TYPE = "COST_TYPE";// 费用类型（首期、续期···）
	public static final String H_TRANS_BILLFREQ_TYPE = "H_TRANS_BILLFREQ_TYPE";// 缴费频率（月、季···）
	public static final String ANNUAL = "ANNUAL";// 年度（1、2、3、4、5、以上）
	public static final String PERIODS = "PERIODS";// 期数（1、2、3、4、5、6、7、8、9、10、11、12）
	public static final String SPECIALMARK = "SPECIALMARK";// 特殊标记（月寄、定期邮寄、平信）
	private List bussTypeList;// 业务种类（对于保险行业之险种）
	private List costTypeList;// 费用类型（首期、续期···）
	private List payFreqList;// 缴费频率（月、季···）
	private List invoiceTypeList;// 发票类型（增专、增普···）
	private List annualList;// 年度（1、2、3、4、5、以上）
	private List periodsList;// 期数（1、2、3、4、5、6、7、8、9、10、11、12）
	private List specialMarkList;// 特殊标记（月寄、定期邮寄、平信）
	private List wydList;// 是否启用周年日（Y、N）
	private AutoInvoiceParamService autoInvoiceParamService;
	private AutoInvoiceParam aipCondition;
	private AutoInvoiceParam autoInvoiceParam;
	private boolean createFlag = false;
	private String checkedlineNo[];// 画面选择的ids

	public AutoInvoiceAction() {
		init();
	}

	public synchronized boolean init() {
		// 初始化相关参数
		System.out
				.println("\n\n\n\n AutoInvoiceAction init -------------------------- \n\n\n\n");
		return true;
	}

	/**
	 * 执行自动开票
	 */
	public String execute() {
		System.out
				.println("\n\n\n\n AutoInvoiceAction execute -------------------------- \n\n\n\n");
		log.info("AutoInvoiceAction-execute 自动开票开始运行");
		try {
			this.makeOutAnInvoice();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("AutoInvoiceAction-execute 自动开票开始异常");
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 手工执行自动开票操作
	 */
	public String manualMakeOutInvoice() {
		try {
			this.makeOutAnInvoice();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 进入自动开票管理参数列表界面
	 * 
	 * @return String
	 */
	public String listParamAutoInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			List paramList = this.autoInvoiceParamService
					.findAutoInvoiceParamList(aipCondition, paginationList);
			// 逐一查询各参数对应字典项
			loadDictionaryList();
			// 为查询结果中字典项赋值中文
			this.setSelectTagValue(paramList);
			this.request.setAttribute("paramList", paramList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 进入自动开票管理参数编辑界面
	 * 
	 * @return String
	 */
	public String editParamAutoInvoice() {
		try {
			String paramId = this.request.getParameter("paramId");
			if (StringUtil.isEmpty(paramId)) {
				createFlag = true;
				autoInvoiceParam = new AutoInvoiceParam();
			} else {
				AutoInvoiceParam aip = new AutoInvoiceParam(paramId);
				autoInvoiceParam = this.autoInvoiceParamService
						.findAutoInvoiceParam(aip);
			}
			// 逐一查询各参数对应字典项
			loadDictionaryList();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 保存自动开票参数信息
	 * 
	 * @return String
	 */
	public String saveParamAutoInvoice() {
		try {
			if (autoInvoiceParam != null) {
				if (StringUtil.isEmpty(autoInvoiceParam.getParamId())) {
					autoInvoiceParam.setParamId(createBusinessId("AIP"));
					this.autoInvoiceParamService.saveAutoInvoiceParam(
							autoInvoiceParam, false);
				} else {
					this.autoInvoiceParamService.saveAutoInvoiceParam(
							autoInvoiceParam, true);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String deleteParamAutoInvoice() {
		try {
			if (null != checkedlineNo && checkedlineNo.length > 0) {
				for (int i = 0; i < checkedlineNo.length; i++) {
					this.autoInvoiceParamService
							.deleteAutoInvoiceParam(checkedlineNo[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	private void loadDictionaryList() {
		// 业务种类（对于保险行业之险种）
		bussTypeList = this.userInterfaceConfigService.getDictionarys(
				BUSS_TYPE, "INSURE");
		// 费用类型（首期、续期···）
		costTypeList = this.userInterfaceConfigService.getDictionarys(
				COST_TYPE, "INSURE");
		// 缴费频率（月、季···）
		payFreqList = this.userInterfaceConfigService.getDictionarys(
				H_TRANS_BILLFREQ_TYPE, "");
		// 发票类型（增专、增普···）
		invoiceTypeList = this.userInterfaceConfigService.getDictionarys(
				KIND_OF_MAKE_OUT_AN_INVOICE, "");
		// 年度（1、2、3、4、5、以上）
		annualList = this.userInterfaceConfigService.getDictionarys(ANNUAL,
				"INSURE");
		// 期数（1、2、3、4、5、6、7、8、9、10、11、12）
		periodsList = this.userInterfaceConfigService.getDictionarys(PERIODS,
				"INSURE");
		// 特殊标记（月寄、定期邮寄、平信）
		specialMarkList = this.userInterfaceConfigService.getDictionarys(
				SPECIALMARK, "INSURE");
		// 是否启用周年日（Y、N）
		wydList = DataUtil.getYesOrNoListForPageListTrans();
		this.request.setAttribute("bussTypeList", bussTypeList);
		this.request.setAttribute("costTypeList", costTypeList);
		this.request.setAttribute("payFreqList", payFreqList);
		this.request.setAttribute("invoiceTypeList", invoiceTypeList);
		this.request.setAttribute("annualList", annualList);
		this.request.setAttribute("periodsList", periodsList);
		this.request.setAttribute("specialMarkList", specialMarkList);
		this.request.setAttribute("wydList", wydList);
	}

	// ///////////////// private 方法统一存放区

	private String makeOutAnInvoice() throws Exception {
		try {
			// 获取自动开票配置参数信息
			this.getAutoInvoiceParamInfo();
			// 从系统参数管理处获取尾差数据
			this.getVmssParam();
			List transList = null;// 开票交易记录
			TransInfo trans = new TransInfo();
			// 判断开票方式和开票种类
			if (DataUtil.W_S.equals(this.moaiWay)
					&& DataUtil.K_S.equals(this.moaiKind)) {
				// 开票方式-单笔 & 开票种类-专票
				// 交易种类-专票
				trans.setFapiaoType(DataUtil.VAT_TYPE_0);
				// 客户种类-专票
				trans.setCustomerFaPiaoType(DataUtil.VAT_TYPE_0);
				transList = this.findTransInfoList(trans);
				List billItemList = this.getBillItemListFromTrans(transList);
				if (CollectionUtil.isNotEmpty(transList)) {
					for (Iterator i = billItemList.iterator(); i.hasNext();) {
						BillItemInfo billItem = (BillItemInfo) i.next();
						this.appendBillInfo(billItem, null);
					}
				}
			} else if (DataUtil.W_S.equals(this.moaiWay)
					&& DataUtil.K_G.equals(this.moaiKind)) {
				// 开票方式-单笔 & 开票种类-普票
				// 交易种类-普票
				trans.setFapiaoType(DataUtil.VAT_TYPE_1);
				transList = this.findTransInfoList(trans);
				List billItemList = this.getBillItemListFromTrans(transList);
				if (CollectionUtil.isNotEmpty(transList)) {
					for (Iterator i = billItemList.iterator(); i.hasNext();) {
						BillItemInfo billItem = (BillItemInfo) i.next();
						this.appendBillInfo(billItem, null);
					}
				}
				// 开票方式-单笔 & 开票种类-普票
				trans = new TransInfo();
				// 交易种类-专票
				trans.setFapiaoType(DataUtil.VAT_TYPE_0);
				// 客户种类-普票
				trans.setCustomerFaPiaoType(DataUtil.VAT_TYPE_1);
				transList = this.findTransInfoList(trans);
				billItemList = this.getBillItemListFromTrans(transList);
				if (CollectionUtil.isNotEmpty(transList)) {
					for (Iterator i = billItemList.iterator(); i.hasNext();) {
						BillItemInfo billItem = (BillItemInfo) i.next();
						this.appendBillInfo(billItem, null);
					}
				}
			} else if (DataUtil.W_S.equals(this.moaiWay)
					&& DataUtil.K_A.equals(this.moaiKind)) {
				// 开票方式-单笔 & 开票种类-全部
				transList = this.findTransInfoList(trans);
				List billItemList = this.getBillItemListFromTrans(transList);
				if (CollectionUtil.isNotEmpty(transList)) {
					for (Iterator i = billItemList.iterator(); i.hasNext();) {
						BillItemInfo billItem = (BillItemInfo) i.next();
						this.appendBillInfo(billItem, null);
					}
				}
			} else if (DataUtil.W_M.equals(this.moaiWay)
					&& DataUtil.K_S.equals(this.moaiKind)) {
				// 开票方式-合并 & 开票种类-专票
				trans = new TransInfo();
				// 交易种类-专票
				trans.setFapiaoType(DataUtil.VAT_TYPE_0);
				// 客户种类-专票
				trans.setCustomerFaPiaoType(DataUtil.VAT_TYPE_0);
				transList = this.findTransInfoList(trans);
				if (transList.size() != 0) {
					User user = new User(AUTO_DRAWER, AUTO_DRAWER);
					List billList = this.createBillListByTransList(transList,
							user);
					this.appendBillInfo(billList, billStatus);
				}
			} else if (DataUtil.W_M.equals(this.moaiWay)
					&& DataUtil.K_G.equals(this.moaiKind)) {
				// 开票方式-合并 & 开票种类-普票
				// 客户发票类型 为 增值税专用发票
				trans = new TransInfo();
				// 交易种类-普票
				trans.setFapiaoType(DataUtil.VAT_TYPE_1);
				// 客户种类-专票
				trans.setCustomerFaPiaoType(DataUtil.VAT_TYPE_0);
				transList = this.findTransInfoList(trans);
				if (transList.size() != 0) {
					User user = new User(AUTO_DRAWER, AUTO_DRAWER);
					List billList = this.createBillListByTransList(transList,
							user);
					this.appendBillInfo(billList, billStatus);
				}
				trans = new TransInfo();
				// 客户种类-普票
				trans.setCustomerFaPiaoType(DataUtil.VAT_TYPE_1);
				transList = this.findTransInfoList(trans);
				if (transList.size() != 0) {
					User user = new User(AUTO_DRAWER, AUTO_DRAWER);
					List billList = this.createBillListByTransList(transList,
							user);
					this.appendBillInfo(billList, billStatus);
				}
			} else if (DataUtil.W_M.equals(this.moaiWay)
					&& DataUtil.K_A.equals(this.moaiKind)) {
				// 开票方式-合并 & 开票种类-全部
				User user = new User(AUTO_DRAWER, AUTO_DRAWER);
				// 查询交易记录
				trans = new TransInfo();
				transList = this.findTransInfoList(trans);
				if (transList.size() != 0) {
					List billList0 = this.createBillListByTransList(transList,
							user);
					this.appendBillInfo(billList0, billStatus);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 根据交易信息创建票据明细信息
	 * 
	 * @param transList
	 * @return List<BillItemInfo>
	 * @throws Exception
	 */
	private List getBillItemListFromTrans(List transList) throws Exception {
		List billItemList = new ArrayList();
		try {
			if (CollectionUtil.isNotEmpty(transList)) {
				for (Iterator i = transList.iterator(); i.hasNext();) {
					TransInfo trans = (TransInfo) i.next();
					// 针对税目和商品信息进行验证
					if (!this.checkTransInfo(trans)) {
						continue;
					}
					// 依交易金额判断是否超过单张发票最大限额 暂默认100000
					BigDecimal transBalance = trans.getBalance();
					while (transBalance.compareTo(DataUtil.billMaxAmt) > 0) {
						BillItemInfo billItem = createBillItemByTrans(trans,
								DataUtil.billMaxAmt);
						billItemList.add(billItem);
						transBalance = trans.getBalance().subtract(
								DataUtil.billMaxAmt);
					}
					if (transBalance.compareTo(new BigDecimal(0)) > 0) {
						BillItemInfo billItem = createBillItemByTrans(trans,
								transBalance);
						billItemList.add(billItem);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return billItemList;
	}

	/**
	 * 对开票的交易记录进行校验，包括税率是否存在、商品是否存在
	 * 
	 * @param transInfo
	 * @return boolean
	 */
	private boolean checkTransInfo(TransInfo transInfo) {
		boolean isOK = true;
		// 税目校验，验证税率存在与税目信息表中
		String taxId = checkTaxRate(transInfo, null, transInfo.getFapiaoType());
		if (StringUtil.IsEmptyStr(taxId)) {
			// 当前交易税率不在税目信息表中，不能进行开票
			isOK = false;
		} else {
			transInfo.setTaxId(taxId);
		}
		// 根据交易类型和纳税人识别号获取发票商品信息
		GoodsInfo goods = this.findGoodsInfo(transInfo, transInfo
				.getBankTaxperNumber());
		if (goods == null) {
			// 发票商品信息不存在
			isOK = false;
		} else {
			transInfo.setGoodsInfo(goods);
		}
		return isOK;
	}

	private BillItemInfo createBillItemByTrans(TransInfo trans,
			BigDecimal billItemAmt) {
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillItemId(createBusinessId("BI"));// 票据明细ID
		billItem.setGoodsName(trans.getGoodsName());// 商品名称
		billItem.setGoodsNo(new BigDecimal(1));// 商品数量
		billItem.setTaxFlag(trans.getTaxFlag());// 含税标志 Y/N
		billItem.setAmt(billItemAmt);// 金额
		billItem.setGoodsPrice(billItemAmt);// 商品单价
		billItem.setTaxRate(trans.getTaxRate());// 税率
		billItem.setTaxAmt(DataUtil.calculateTaxAmt(billItemAmt, trans
				.getTaxRate(), "base"));// 税额
		billItem.setTaxItem(trans.getTaxId());// 税目
		billItem.setRowNature("0");// 票据行性质(百旺fphxz:0-正常行)
		billItem.setTransInfo(trans);
		return billItem;
	}

	private void appendBillInfo(BillItemInfo billItem, TransInfo trans)
			throws Exception {
		try {
			if (trans == null && billItem != null
					&& billItem.getTransInfo() != null) {
				trans = billItem.getTransInfo();
			}
			// 构造票据信息
			BillInfo billInfo = new BillInfo();
			billInfo.setBillId(createBillId("B"));
			billInfo.setApplyDate(DateUtils.toString(new Date(),
					DateUtils.ORA_DATES_FORMAT));// 开票日期(在界面体现为申请开票日期)
			// 增值税专用发票 票据购货单位信息按客户信息取值
			billInfo.setCustomerName(trans.getCustomerName());// 客户纳税人名称
			billInfo.setCustomerTaxno(trans.getCustomerTaxno());// 客户纳税人识别号
			billInfo.setCustomerAddressandphone(trans.getCustomerAddress()
					+ " " + trans.getCustomerTel());// 客户地址电话
			billInfo.setCustomerBankandaccount(trans.getCustomerAccount());// 客户银行账号
			billInfo.setAmtSum(billItem.getAmt());// 合计金额
			billInfo.setTaxAmtSum(DataUtil.calculateTaxAmt(billItem.getAmt(),
					billItem.getTaxRate(), "base"));// 合计税额
			billInfo.setSumAmt(billInfo.getAmtSum()
					.add(billInfo.getTaxAmtSum()));// 价税合计
			billInfo.setDrawer(AUTO_DRAWER);// 开票人
			billInfo.setDrawerName(AUTO_DRAWER);// 开票人
			billInfo.setName(trans.getInstTaxperName());// 我方纳税人名称
			billInfo.setTaxno(trans.getInstTaxperNumber());// 我方纳税人识别号
			billInfo.setAddressandphone(trans.getInstTaxAddress() + " "
					+ trans.getInstTaxTel());// 我方地址电话
			billInfo.setBankandaccount(trans.getInstTaxBank()
					+ trans.getInstAccount());// 我方银行账号
			billInfo.setInstCode(trans.getInstCode());// 所属机构
			billInfo.setDataStatus(this.billStatus);// 状态
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_1);// 是否手工录入2-人工审核
			billInfo.setIssueType(DataUtil.ISSUE_TYPE_1); // 发票开具类型1-单笔
			if (DataUtil.VAT_TYPE_0.equals(trans.getFapiaoType())
					&& DataUtil.VAT_TYPE_0
							.equals(trans.getCustomerFaPiaoType())) {
				billInfo.setFapiaoType(DataUtil.VAT_TYPE_0);// 发票类型-增值税专用发票
			} else {
				billInfo.setFapiaoType(DataUtil.VAT_TYPE_1);// 发票类型-增值税普通发票
			}
			// 票据ID
			billItem.setBillId(billInfo.getBillId());
			// 插入票据信息
			billInfoService.saveBillInfo(billInfo, false);
			// 插入票据明细信息
			billInfoService.saveBillItemInfo(billItem, false);
			// 插入交易票据对应信息
			transInfoService.saveTransBill(trans.getTransId(), billItem
					.getBillId(), billItem.getBillItemId(), billItem.getAmt(),
					billItem.getTaxAmt(), billItem.getAmt());
			// 更新所选交易状态为开票编辑锁定中、未开票金额为零
			trans.setBalance(new BigDecimal(0.00));
			trans.setDataStatus(DataUtil.TRANS_STATUS_2);
			transInfoService.updateTransInfo(trans);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取自动开票配置参数信息
	 * 
	 * @throws Exception
	 */
	private void getAutoInvoiceParamInfo() throws Exception {
		try {
			UBaseSysParamVmss uBaseSysParamVmss = new UBaseSysParamVmss();
			uBaseSysParamVmss.setSystemId(SYSTEM_ID);
			List list = this.paramConfigVmssService
					.findBaseVmssParamList(uBaseSysParamVmss);
			if (CollectionUtil.isNotEmpty(list)) {
				for (Iterator i = list.iterator(); i.hasNext();) {
					UBaseSysParamVmss param = (UBaseSysParamVmss) i.next();
					if (param.getItemKey().equalsIgnoreCase(
							WAY_OF_MAKE_OUT_AN_INVOICE)) {
						moaiWay = param.getSelectedValue();
					} else if (param.getItemKey().equalsIgnoreCase(
							KIND_OF_MAKE_OUT_AN_INVOICE)) {
						moaiKind = param.getSelectedValue();
					} else if (param.getItemKey().equalsIgnoreCase(BILL_STATUS)) {
						billStatus = param.getSelectedValue();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 从系统参数管理处获取尾差和单笔发票最大金额数据
	 * 
	 * @throws Exception
	 */
	private void getVmssParam() throws Exception {
		try {
			UBaseSysParamVmss uBaseSysParamVmss = this.paramConfigVmssService
					.getUBaseSysParamVmssByKey("NETTING");
			if (uBaseSysParamVmss != null
					&& StringUtil.isNotEmpty(uBaseSysParamVmss
							.getSelectedValue())) {
				BigDecimal bd = new BigDecimal(uBaseSysParamVmss
						.getSelectedValue());
				DataUtil.different = bd;
			}
			BigDecimal minLimitAmt = this.taxDiskMonitorService
					.getMinBillLimitAmtFromTaxDisk();
			if (minLimitAmt != null) {
				DataUtil.billMaxAmt = minLimitAmt;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private List findTransInfoList(TransInfo trans) throws Exception {
		List transInfoList = new ArrayList();
		try {
			if (trans == null) {
				trans = new TransInfo();
			}
			// 状态：未开票
			trans.setDataStatus(DataUtil.TRANS_STATUS_1);
			// 非冲账
			trans.setIsReverse(DataUtil.NO);
			// 交易标识：2-实收实付
			trans.setTransFlag(DataUtil.TRANS_FLAG_2);
			// 交易是否打票：A-自动打印
			trans.setTransFapiaoFlag(DataUtil.FAPIAO_FLAG_A);
			// 客户是否打票：A-自动打印
			trans.setCustomerFaPiaoFlag(DataUtil.FAPIAO_FLAG_A);
			// 查询
			trans.setSearchFlag(DataUtil.AUTO_INVOICE);
			// 开票日期
			trans.setFapiaoDate(DateUtils.toString(new Date(),
					DateUtils.ORA_DATES_FORMAT));
			List lstAuthInstId = new ArrayList();
			// this.getAuthInstList(lstAuthInstId);
			trans.setLstAuthInstId(lstAuthInstId);
			List tempList = this.transInfoService.findTransInfoList(trans);
			if (CollectionUtil.isNotEmpty(tempList)) {
				// 处理交易可开票金额，保证每笔交易对应金额不大于单笔发票最大金额
				for (Iterator i = tempList.iterator(); i.hasNext();) {
					TransInfo temp = (TransInfo) i.next();
					if (temp.getBalance().compareTo(DataUtil.billMaxAmt) <= 0) {
						transInfoList.add(temp);
					} else {
						BigDecimal transBalance = temp.getBalance();
						while (transBalance.compareTo(DataUtil.billMaxAmt) > 0) {
							transBalance = transBalance
									.subtract(DataUtil.billMaxAmt);

							// temp.setBalance(DataUtil.billMaxAmt);

							TransInfo tempSub = new TransInfo();
							BeanUtils.copyProperties(temp, tempSub);

							BigDecimal rateAmt = DataUtil.billMaxAmt
									.multiply(tempSub.getTaxRate());

							tempSub.setBalance(DataUtil.billMaxAmt);
							tempSub.setIncome(DataUtil.billMaxAmt);
							tempSub.setTaxAmt(rateAmt);
							transInfoList.add(tempSub);
						}
						if (transBalance.compareTo(new BigDecimal(0)) > 0) {
							TransInfo tempSub = new TransInfo();
							// BeanUtilsVMS.copyProperties(tempSub, temp);
							BeanUtils.copyProperties(temp, tempSub);

							BigDecimal rateAmt = transBalance.multiply(tempSub
									.getTaxRate());

							tempSub.setIncome(transBalance);
							tempSub.setBalance(transBalance);
							tempSub.setTaxAmt(rateAmt);
							transInfoList.add(tempSub);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return transInfoList;
	}

	/**
	 * 合并开票时，基于交易信息列表构造票据信息
	 * 
	 * @param transList
	 * @return List<BillInfo>
	 */
	protected List createBillListByTransList(List transList, User currentUser) {
		Customer customer = new Customer();
		List billList = new ArrayList();
		List billTransList = new ArrayList();
		BigDecimal billAmt = new BigDecimal(0);// 当前票据总金额

		String currBankCode = "";// 当前交易发生机构纳税人识别号
		String currCustomerId = "";// 当前客户ID
		String currFapiaoType = "";// 当前交易发票类型

		int billGoodsNum = 0;// 当前票据总商品种类数
		String currTransGoods = "";// 当前商品
		// 从第一笔交易获取相关信息
		TransInfo transInfo = (TransInfo) transList.get(0);
		// 税额补正
		billAmt = billAmt.add(transInfo.getBalance());
		billGoodsNum = 1;
		billTransList.add(transInfo);

		currBankCode = transInfo.getBankCode();// 当前交易发生机构纳税人识别号
		currCustomerId = transInfo.getCustomerId();// 当前客户ID
		currFapiaoType = transInfo.getFapiaoType();// 当前交易发票类型
		customer.setCustomerCName(transInfo.getCustomerName());// 客户纳税人名称
		customer.setCustomerTaxno(transInfo.getCustomerTaxno());// 客户纳税人识别号
		customer.setCustomerAddress(transInfo.getCustomerAddress());// 客户地址
		customer.setCustomerPhone(transInfo.getCustomerTel());// 客户电话
		customer.setCustomerCBank(transInfo.getCustomerAccount());// 客户银行账号

		currTransGoods = transInfo.getGoodsKey();
		BigDecimal billItemAmt = transInfo.getBalance();// 总金额
		BigDecimal billItemTaxAmt = new BigDecimal(0);// 总税额
		// BigDecimal transSumTaxAmt = transInfo.getTaxAmt();// 各明细税额相加
		BigDecimal transSumTaxAmt = transInfo.getTaxAmt();// 各明细税额相加

		for (int i = 1; i < transList.size(); i++) {
			boolean createNewBill = false;
			TransInfo trans = (TransInfo) transList.get(i);
			billAmt = billAmt.add(trans.getBalance());// 票据总金额
			if (billAmt.compareTo(DataUtil.billMaxAmt) >= 0
					|| !currBankCode.equals(trans.getBankCode())
					|| !currCustomerId.equals(trans.getCustomerId())
					|| (DataUtil.VAT_TYPE_0.equals(trans
							.getCustomerFaPiaoType()) && !currFapiaoType
							.equals(trans.getFapiaoType()))) {
				// 发票总金额达到或超出最大值 将现有billTransList中的交易信息进行开票处理
				// 满足其他新开发票条件时，也须重新开票。
				BillInfo bill = this.createBillByTransList(billTransList,
						currentUser, customer);
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
						BillInfo bill = this.createBillByTransList(
								billTransList, currentUser, customer);
						billList.add(bill);
						createNewBill = true;
						// 判断发票总金额
					} else {
						// 新商品
						billGoodsNum = billGoodsNum + 1;
						// 需要另起一行开新的商品信息
						currTransGoods = trans.getGoodsKey();
					}
					billItemAmt = trans.getBalance(); // 总金额
					transSumTaxAmt = trans.getTaxAmt(); // 各明细税额相加
				} else {
					// 交易对应商品在现有开票交易集billTransList中
					// 即使商品信息相同，当误差过大时，也需换行。
					// 判断轧差金额
					// 依票据明细金额和税率计算得到的税额
					billItemAmt = billItemAmt.add(trans.getBalance());
					billItemTaxAmt = DataUtil.calculateTaxAmt(billItemAmt,
							trans.getTaxRate(), "base");
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
							BillInfo bill = this.createBillByTransList(
									billTransList, currentUser, customer);
							billList.add(bill);
							createNewBill = true;
							// 判断发票总金额
						} else {
							// 新商品
							billGoodsNum = billGoodsNum + 1;
							// 需要另起一行开新的商品信息
							currTransGoods = trans.getGoodsKey();
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

				currBankCode = trans.getBankCode();// 当前交易发生机构纳税人识别号
				currCustomerId = trans.getCustomerId();// 当前客户ID
				currFapiaoType = trans.getFapiaoType();// 当前交易发票类型
				currTransGoods = trans.getGoodsKey();// 当前商品
				customer.setCustomerCName(trans.getCustomerName());// 客户纳税人名称
				customer.setCustomerTaxno(trans.getCustomerTaxno());// 客户纳税人识别号
				customer.setCustomerAddress(trans.getCustomerAddress());// 客户地址
				customer.setCustomerPhone(trans.getCustomerTel());// 客户电话
				customer.setCustomerCBank(trans.getCustomerAccount());// 客户银行账号

				billAmt = trans.getBalance();// 当前票据总金额
				billTransList = new ArrayList();
			}
			billTransList.add(trans);
		}
		if (CollectionUtil.isNotEmpty(billTransList)) {
			BillInfo bill = this.createBillByTransList(billTransList,
					currentUser, customer);
			billList.add(bill);
		}
		return billList;
	}

	/**
	 * 基于已整理好的交易列表，构造票据信息
	 * 
	 * @param transList
	 * @return BillInfo
	 */
	private BillInfo createBillByTransList(List transList, User currentUser,
			Customer customer) {
		String billFapiaoType = null; // 发票类型
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
			billItem.setGoodsName(transInfo.getGoodsName());// 商品名称
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
			billTaxAmt = billItem.getTaxAmt();

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
					billItem.setGoodsName(trans.getGoodsName());// 商品名称
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
						billItem.setGoodsName(trans.getGoodsName());// 商品名称
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
						billItem.setTaxAmt(billItem.getTaxAmt().add(
								trans.getTaxAmt()));// 税额
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
			// 依交易和客户的发票类型，综合判断所开发票的发票类型
			if (billFapiaoType == null) {
				if (DataUtil.VAT_TYPE_0.equals(transInfo.getFapiaoType())
						&& DataUtil.VAT_TYPE_0.equals(transInfo
								.getCustomerFaPiaoType())) {
					billFapiaoType = DataUtil.VAT_TYPE_0;// 发票类型-增值税专用发票
				} else {
					billFapiaoType = DataUtil.VAT_TYPE_1;// 发票类型-增值税普通发票
				}
			}
			billInfo.setBillId(billId);
			billInfo.setApplyDate(DateUtils.toString(new Date(),
					DateUtils.ORA_DATES_FORMAT));// 申请开票日期(在界面体现为申请开票日期)
			// billInfo.setApplyDate(billInfo.getBillDate());// 应用日期
			billInfo.setCustomerName(customer.getCustomerCName());// 客户纳税人名称
			billInfo.setCustomerTaxno(customer.getCustomerTaxno());// 客户纳税人识别号
			billInfo.setCustomerAddressandphone(customer.getCustomerAddress()
					+ " " + customer.getCustomerPhone());// 客户地址电话
			billInfo.setCustomerBankandaccount(customer.getCustomerCBank()
					+ transInfo.getCustomerAccount());// 客户银行账号
			billInfo.setAmtSum(billAmt);// 合计金额
			billInfo.setTaxAmtSum(billTaxAmt);// 合计税额
			billInfo.setSumAmt(billAmt.add(billTaxAmt));// 价税合计
			if (currentUser != null) {
				billInfo.setDrawer(currentUser.getId());// 开票人
				billInfo.setDrawerName(currentUser.getName());// 开票人
			}
			billInfo.setName(transInfo.getInstTaxperName());// 我方纳税人名称
			billInfo.setTaxno(transInfo.getInstTaxperNumber());// 我方纳税人识别号
			billInfo.setAddressandphone(transInfo.getInstTaxAddress() + " "
					+ transInfo.getInstTaxTel());// 我方地址电话
			billInfo.setBankandaccount(transInfo.getInstTaxBank()
					+ transInfo.getInstAccount());// 我方银行账号
			billInfo.setInstCode(transInfo.getInstCode());// 所属机构
			billInfo.setDataStatus(DataUtil.BILL_STATUS_1);// 状态-待提交
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_1);// 是否手工录入2-人工审核
			billInfo.setIssueType(DataUtil.ISSUE_TYPE_2); // 发票开具类型2-合并
			billInfo.setFapiaoType(billFapiaoType);
			billInfo.setBillItemList(billItemList);
		}
		return billInfo;
	}

	protected void appendBillInfo(List billList, String billDataStatus)
			throws Exception {
		try {
			if (CollectionUtil.isNotEmpty(billList)) {
				for (Iterator t = billList.iterator(); t.hasNext();) {
					BillInfo bill = (BillInfo) t.next();
					if (bill != null) {
						List transBillInfoList = new ArrayList();
						List billItemList = bill.getBillItemList();
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
											// 构造交易票据对应信息
											Map map = new HashMap();
											map.put("income", trans
													.getBalance());
											map.put("billId", billItem
													.getBillId());
											map.put("billItemId", billItem
													.getBillItemId());
											map.put("amt", trans.getBalance());
											map
													.put("taxAmt", trans
															.getTaxAmt());
											map.put("transId", trans
													.getTransId());
											transBillInfoList.add(map);
											StringBuffer sb = new StringBuffer();
											sb.append(trans.getTransId())
													.append("+");
											sb.append(billItem.getBillId())
													.append("+");
											sb.append(billItem.getBillItemId())
													.append("+");
											sb.append(trans.getBalance())
													.append("+");
											sb.append(trans.getTaxAmt())
													.append("+");
											System.out.println(sb.toString());
										}
									}
									// 插入交易票据对应信息
									transInfoService
											.saveTransBillForAuto(transBillInfoList);
								}
							}
						}
						// 更新所选交易状态为开票编辑锁定中、未开票金额为零
						transInfoService.updateTransInfoForAuto(
								DataUtil.TRANS_STATUS_2, bill.getBillId());
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected void setSelectTagValue(List paramList) {
		if (CollectionUtil.isNotEmpty(paramList)) {
			for (Iterator i = paramList.iterator(); i.hasNext();) {
				AutoInvoiceParam aip = (AutoInvoiceParam) i.next();
				// 客户纳税识别号（可为空，即适用于所有客户）
				if (StringUtil.isEmpty(aip.getCustTaxNo())
						&& StringUtil.isEmpty(aip.getCustName())) {
					aip.setCustName("全部");
				}
				// 业务种类（对于保险行业之险种）
				if (StringUtil.isNotEmpty(aip.getBussType())) {
					if (CollectionUtil.isNotEmpty(this.bussTypeList)) {
						for (Iterator j = this.bussTypeList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getBussType().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setBussType(dict.getName());
							}
						}
					}
				}
				// 费用类型（首期、续期···）
				if (StringUtil.isNotEmpty(aip.getCostType())) {
					if (CollectionUtil.isNotEmpty(this.costTypeList)) {
						for (Iterator j = this.costTypeList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getCostType().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setCostType(dict.getName());
							}
						}
					}
				}
				// 缴费频率（月、季···）
				if (StringUtil.isNotEmpty(aip.getPayFreq())) {
					if (CollectionUtil.isNotEmpty(this.payFreqList)) {
						for (Iterator j = this.payFreqList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getPayFreq().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setPayFreq(dict.getName());
							}
						}
					}
				}
				// 发票类型（增专、增普···）
				if (StringUtil.isNotEmpty(aip.getInvoiceType())) {
					if (CollectionUtil.isNotEmpty(this.invoiceTypeList)) {
						for (Iterator j = this.invoiceTypeList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getInvoiceType().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setInvoiceType(dict.getName());
							}
						}
					}
				}
				// 年度（1、2、3、4、5、以上）
				if (StringUtil.isNotEmpty(aip.getAnnual())) {
					if (CollectionUtil.isNotEmpty(this.annualList)) {
						for (Iterator j = this.annualList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getAnnual().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setAnnual(dict.getName());
							}
						}
					}
				}
				// 期数（1、2、3、4、5、6、7、8、9、10、11、12）
				if (StringUtil.isNotEmpty(aip.getAnnual())) {
					if (CollectionUtil.isNotEmpty(this.periodsList)) {
						for (Iterator j = this.periodsList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getPeriods().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setPeriods(dict.getName());
							}
						}
					}
				}
				// 特殊标记（月寄、定期邮寄、平信）
				if (StringUtil.isNotEmpty(aip.getAnnual())) {
					if (CollectionUtil.isNotEmpty(this.specialMarkList)) {
						for (Iterator j = this.specialMarkList.iterator(); j
								.hasNext();) {
							Dictionary dict = (Dictionary) j.next();
							if (aip.getSpecialMark().equalsIgnoreCase(
									dict.getValueStandardNum())) {
								aip.setSpecialMark(dict.getName());
							}
						}
					}
				}
				// 是否启用周年日（Y、N）
				if (StringUtil.isNotEmpty(aip.getWeekYearDay())) {
					if (DataUtil.YES.equals(aip.getWeekYearDay())) {
						aip.setWeekYearDay("启用");
					} else if (DataUtil.NO.equals(aip.getWeekYearDay())) {
						aip.setWeekYearDay("不启用");
					}
				}
			}
		}
	}

	// ///////////////// private 方法统一存放区

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public TaxDiskMonitorService getTaxDiskMonitorService() {
		return taxDiskMonitorService;
	}

	public void setTaxDiskMonitorService(
			TaxDiskMonitorService taxDiskMonitorService) {
		this.taxDiskMonitorService = taxDiskMonitorService;
	}

	public List getWayList() {
		return wayList;
	}

	public void setWayList(List wayList) {
		this.wayList = wayList;
	}

	public List getKindList() {
		return kindList;
	}

	public void setKindList(List kindList) {
		this.kindList = kindList;
	}

	public List getStatusList() {
		return statusList;
	}

	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}

	public String getMoaiWay() {
		return moaiWay;
	}

	public void setMoaiWay(String moaiWay) {
		this.moaiWay = moaiWay;
	}

	public String getMoaiKind() {
		return moaiKind;
	}

	public void setMoaiKind(String moaiKind) {
		this.moaiKind = moaiKind;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public AutoInvoiceParamService getAutoInvoiceParamService() {
		return autoInvoiceParamService;
	}

	public void setAutoInvoiceParamService(
			AutoInvoiceParamService autoInvoiceParamService) {
		this.autoInvoiceParamService = autoInvoiceParamService;
	}

	public AutoInvoiceParam getAutoInvoiceParam() {
		return autoInvoiceParam;
	}

	public void setAutoInvoiceParam(AutoInvoiceParam autoInvoiceParam) {
		this.autoInvoiceParam = autoInvoiceParam;
	}

	public List getBussTypeList() {
		return bussTypeList;
	}

	public void setBussTypeList(List bussTypeList) {
		this.bussTypeList = bussTypeList;
	}

	public List getCostTypeList() {
		return costTypeList;
	}

	public void setCostTypeList(List costTypeList) {
		this.costTypeList = costTypeList;
	}

	public List getPayFreqList() {
		return payFreqList;
	}

	public void setPayFreqList(List payFreqList) {
		this.payFreqList = payFreqList;
	}

	public List getInvoiceTypeList() {
		return invoiceTypeList;
	}

	public void setInvoiceTypeList(List invoiceTypeList) {
		this.invoiceTypeList = invoiceTypeList;
	}

	public AutoInvoiceParam getAipCondition() {
		return aipCondition;
	}

	public void setAipCondition(AutoInvoiceParam aipCondition) {
		this.aipCondition = aipCondition;
	}

	public boolean isCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public List getAnnualList() {
		return annualList;
	}

	public void setAnnualList(List annualList) {
		this.annualList = annualList;
	}

	public List getPeriodsList() {
		return periodsList;
	}

	public void setPeriodsList(List periodsList) {
		this.periodsList = periodsList;
	}

	public List getSpecialMarkList() {
		return specialMarkList;
	}

	public void setSpecialMarkList(List specialMarkList) {
		this.specialMarkList = specialMarkList;
	}

	public List getWydList() {
		return wydList;
	}

	public void setWydList(List wydList) {
		this.wydList = wydList;
	}
}
