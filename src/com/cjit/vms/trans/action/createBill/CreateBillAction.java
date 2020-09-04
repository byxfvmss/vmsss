package com.cjit.vms.trans.action.createBill;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;

import cjit.crms.util.StringUtil;

import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
//import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

public class CreateBillAction extends DataDealAction {

	private int scale = 10;
	/***
	 * 开票查询移植-------------------------
	 */
	protected com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
	protected List transInfoList;
	// 客户纳税人类别列表
	protected List custTaxPayerTypeList = new ArrayList();
	// 交易状态列表
	protected List transDataStatusList = new ArrayList();
	protected String message;

	/***
	 * 开票查询移植 end-----------------------
	 */

	/***
	 * 开票单张循环移值-------------------start
	 */
	BillValidationService billValidationService;
	CreateBillService createBillService;
	protected String[] selectTransIds;

	/***
	 * 拆分开票
	 */
	protected String transId;
	protected String[] money;

	protected String cherNum;// 保单号
	protected String repNum;// 旧保单号
	protected String ttmpRcno;// 投保单号
	protected String feeTyp;// 费用类型
	protected String billFreq;// 交费频率
	protected String polYear;// 保单年度
	protected String hissDte;// 承保日期
	protected String dsouRce;// 数据来源
	protected String chanNel;// 渠道
	protected String premTerm;// 期数
	protected String hissBeginDte;// 开始
	protected String hissEndDte;// 结束
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private Map<String, String> applyFeeTypList;
	private VmsCommonService vmsCommonService;
	private String premTermArray;

	/***
	 * 开票单张循环移值-------------------end
	 */

	/**
	 * 开票（选中多笔交易，每笔交易开具一张发票）
	 * 
	 * @return String
	 */
	public String transToEachBill() throws Exception {
		String transIds = "";
		List transInfoList = new ArrayList();
		User currentUser = this.getCurrentUser();
		try {
			StringBuffer sbMessage = new StringBuffer();
			int transSuccess = 0;
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {

					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);
					if (searPar != null) {
						searPar.setSelectTransIds(this.selectTransIds);
					}
					Integer count = billValidationService.checkingTransByCherNum(searPar, false).size();
					if (count > 0) {
						this.setRESULT_MESSAGE(
								URLDecoder.decode("客户【" + searPar.getCustomerName() + "】保单号【" + searPar.getCherNum()
										+ "】下,有期数小于【" + searPar.getPremTerm() + "】的交易且未开票,不允许开票!", "utf-8"));
						return ERROR;
					}

					transIds += this.selectTransIds[i];
					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append(
							(searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno()) ? searPar.getTtmpRcno()
									: "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
//					sb.append("】-【");
//					sb.append((searPar.getRepNum() != null && !"".equals(searPar.getRepNum()) ? searPar.getRepNum() : "        "));
//					sb.append("】\n主险名称：【");
//					sb.append((searPar.getPlanLongDesc() != null && !"".equals(searPar.getPlanLongDesc()) ? searPar.getPlanLongDesc() : ""));
//					sb.append("】\n交费起止日期：【");
//					sb.append((searPar.getInstFrom() != null ? searPar.getInstFrom() : ""));
//					sb.append("】-【");
//					sb.append((searPar.getInstTo() != null ? searPar.getInstTo() : ""));
					sb.append("】\n");
					searPar.setRemark(sb.toString());
					transIds += this.selectTransIds[i];
					transInfoList.add(searPar);
				}
				// 校验
				CheckResult result = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);

				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(result.getCheckFlag())) {
					sbMessage = sbMessage.append(result.getCheckResultMsg());
				} else {
					for (int i = 0; i < transInfoList.size(); i++) {
						List dataList = new ArrayList();
						dataList.add(transInfoList.get(i));
						createBillService.constructBillAndSaveAsSingle(dataList, currentUser);
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
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票 成功" + transSuccess + "笔", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票", "0");
			log.error("TransInfoAction-transToEachBill", e);
			throw e;
		}
	}

	/**
	 * 菜单进入，查询交易信息列表（且冲账标识字段为N的记录）
	 * 
	 * @return String
	 */
	public String listTrans() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		User user = this.getCurrentUser();
		try {
			if ("bill".equalsIgnoreCase(fromFlag)) {
				this.setTransInfo(transInfo);

			}
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");
			applyFeeTypList = new HashMap<String, String>();
			for (Iterator iterator = feeTypList.keySet().iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// 只检索首期、续期、定结、保全
				if ("001".equals(type) || "002".equals(type) || "003".equals(type) || "015".equals(type)) {
					applyFeeTypList.put(type, (String) feeTypList.get(type));
				}
			}

			// 构造查询条件
			com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
			transInfo = this.getTransInfo();
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			if (user != null) {
				transInfo.setUserId(user.getId());
			}
			transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
			custTaxPayerTypeList = this.createSelectList(DataUtil.TAXPAYER_TYPE, null, true);
			transDataStatusList = DataUtil.getTransDataStatusListForPageListTrans();
			// 查询 符合条件的customer_id
			String customerName = transInfo.getCustomerName();
			List customerObjs = new ArrayList();
			List customerIds = new ArrayList();
			if (null != customerName && !customerName.equals("")) {
				customerObjs = customerService.findTransByCustomers(customerName);
			}
			for (int i = 0; i < customerObjs.size(); i++) {
				String customerId = ((Customer) customerObjs.get(i)).getCustomerID();
				customerIds.add(customerId);
			}
			transInfo.setCustomerIds(customerIds);
			// 查询符合条件的transType
			String transName = transInfo.getTransTypeName();
			List transNameObjs = new ArrayList();
			List transTypeList = new ArrayList();
			if (null != transName && !transName.equals("")) {
				Map map = new HashMap();
				map.put("transName", transName);
				map.put("customerTaxPayerType", transInfo.getCustomerTaxPayerType());
				transNameObjs = transInfoService.findTransTypeList(map);
			}
			for (int i = 0; i < transNameObjs.size(); i++) {
				TransTypeInfo transType = ((TransTypeInfo) transNameObjs.get(i));
				String transTypeId = ((TransTypeInfo) transNameObjs.get(i)).getTransTypeId();
				transTypeList.add(transTypeId);
			}
			transInfo.setTransTypeList(transTypeList);
			// // 获取相关联的客户ID
			// List list = transInfoService.findTransCustomerList(transInfo);
			// // 去重复
			// Map map = new HashMap();
			// for (int i = 0; i < list.size(); i++) {
			// String customerId = ((Customer) list.get(i)).getCustomerID();
			// map.put(customerId, customerId);
			// }
			// Object transCustomerList[] = map.keySet().toArray();
			// transInfo.setTransCustomerList(transCustomerList);
			this.paginationList.setShowCount("false");
			transInfoList = transInfoService.findTransInfoList(transInfo, paginationList);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("message", this.message);
			this.request.setAttribute("transInfoList", transInfoList);
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTrans", e);
		}
		return ERROR;
	}

	public void selectTransToOneBill() throws Exception {
		String transIds = "";
		List transInfoList = new ArrayList();
		User currentUser = this.getCurrentUser();
		selectTransIds = request.getParameter("selectTransIds").split(",");
		Date instFrom = null;
		Date instTo = null;
		CheckResult result;
		try {
			// StringBuffer sbMessage = new StringBuffer();
			// int transSuccess = 0;
			if (this.selectTransIds == null || this.selectTransIds.length == 0) {
				result = new CheckResult(CheckResult.CHECK_FAIL, "", "请选择数据");
			} else {
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {

					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);
					transIds += this.selectTransIds[i];
					transInfoList.add(searPar);
				}
				// 作全局校验
				CheckResult checkResult = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);

				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
					result = checkResult;
				} else {
					// 构建票据对象
					BillsTaxNoContext billsTaxNoContext = createBillService.constructBill(transInfoList, currentUser);

					String[] taxNos = billsTaxNoContext.getTaxNos();
					String[] customerIds = billsTaxNoContext.getCustomerIds(taxNos[0]);
					// int billSize = billsTaxNoContext
					int billsize = billsTaxNoContext.getTaxNoBillContext(taxNos[0]).size();

					if (billsize > 0) {
						String resultMsg = "";
						resultMsg = "预计 :  \n";
						resultMsg += "交易数 : " + transInfoList.size() + "条\n";
						resultMsg += "客户数 : " + customerIds.length + "个 \n";
						resultMsg += "预计开票数 : " + billsize + "条 \n";
						resultMsg += "请确认是否继续!";
						result = new CheckResult(CheckResult.CHECK_OK, "", resultMsg);
					} else {
						result = new CheckResult(CheckResult.CHECK_FAIL, "", "没有票据生成");
					}

				}

			}
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(JSONObject.fromObject(result).toString());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();

			CheckResult checkResult = new CheckResult(CheckResult.CHECK_FAIL, "", "票据生成发生异常");
			String resultMsg = JSONObject.fromObject(checkResult).toString();

			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(resultMsg);
			out.close();
			throw e;
		}
	}

	public String transToOneBill() throws Exception {
		String transIds = "";
		List transInfoList = new ArrayList();
		User currentUser = this.getCurrentUser();
		// selectTransIds = request.getParameter("selectTransIds").split(",");

		String instFrom = null;
		String instTo = null;
		CheckResult result = null;
		try {
			// StringBuffer sbMessage = new StringBuffer();
			int transSuccess = 0;
			if (this.selectTransIds == null || this.selectTransIds.length == 0) {
				result = new CheckResult(CheckResult.CHECK_FAIL, "", "请选择数据");
			} else {
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {

					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);

					if (this.premTermArray != null && !"".equals(this.premTermArray) && this.premTermArray.length() > 1
							&& instFrom == null && instTo == null) {
						String[] p = this.premTermArray.split(",");
						/*
						 * searPar.setPremTermArrayMin(p[0].toString().trim());
						 * searPar.setPremTermArrayMax(p[p.length-1].toString().trim());
						 */
						searPar.setSelectTransIds(this.selectTransIds);
						List<TransInfo> transByCherNum = billValidationService.checkingTransByCherNum(searPar, true);
						if (transByCherNum.size() > 0) {
							this.setRESULT_MESSAGE(URLDecoder.decode("客户【" + searPar.getCustomerName() + "】保单号【"
									+ searPar.getCherNum() + "】下,有期数为【" + searPar.getPremTermArrayMin() + " 至  "
									+ searPar.getPremTermArrayMax() + "】中的交易且未开票,不允许跨期开票!", "utf-8"));
							return ERROR;
						}
					}
					if (this.selectTransIds.length > 1 && instFrom == null && instTo == null) {
						List l = transInfoService.findTransInfoListByTransId(this.selectTransIds);
						TransInfo t = (TransInfo) l.get(0);
						TransInfo t1 = (TransInfo) l.get(l.size() - 1);
						instFrom = t.getInstFrom();
						instTo = t1.getInstTo();
					}
					if (this.selectTransIds.length <= 1 && instFrom == null && instTo == null) {
						instFrom = searPar.getInstFrom();
						instTo = searPar.getInstTo();
					}
					transIds += this.selectTransIds[i];
					transSuccess++;
					searPar.setInstFrom(instFrom);
					searPar.setInstTo(instTo);

					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append(
							(searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno()) ? searPar.getTtmpRcno()
									: "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
					// 去掉其他信息显示
					// sb.append("】-【");
//					sb.append((searPar.getRepNum() != null && !"".equals(searPar.getRepNum()) ? searPar.getRepNum() : "        "));
//					sb.append("】\n主险名称：【");
//					sb.append((searPar.getPlanLongDesc() != null && !"".equals(searPar.getPlanLongDesc()) ? searPar.getPlanLongDesc() : ""));
//					sb.append("】\n交费起止日期：【");
//					sb.append((searPar.getInstFrom() != null ? searPar.getInstFrom() : ""));
//					sb.append("】-【");
//					sb.append((searPar.getInstTo() != null ? searPar.getInstTo() : ""));
					sb.append("】\n");
					searPar.setRemark(sb.toString());
					transInfoList.add(searPar);
				}
				// 作全局校验
				CheckResult checkResult = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);

				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
					result = checkResult;
				} else {
					// 构建票据对象
					createBillService.constructBillAndSaveAsMerge(transInfoList, currentUser);

				}

			}

			if (null != result) {
				this.request.setAttribute("message", result.getCheckResultMsg());
				this.setMessage(result.getCheckResultMsg());
				this.setFromFlag("bill");
				return ERROR;
			} else {
				logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
						"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票 成功" + transSuccess + "笔",
						"1");
				return SUCCESS;

			}

		} catch (Exception e) {
			e.printStackTrace();
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
				com.cjit.vms.trans.model.TransInfo trans = transInfoService.findTransInfo(info);
				if (trans != null && trans.getBalance() != null) {
					BigDecimal amt = trans.getBalance();
					// amt = amt.add(amt.multiply(trans.getTaxRate()));
					// amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
					DecimalFormat df = new DecimalFormat("0.00");
					this.request.setAttribute("amt", df.format(amt));
					this.request.setAttribute("transId", trans.getTransId());
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request.setAttribute("userId", currentUser.getId());
					}
				}
			}
			if (!"error".equalsIgnoreCase(fromFlag)) {
				this.setMessage(null);
			} else {
				fromFlag = null;
			}
			logManagerService.writeLog(request, this.getCurrentUser(), "0001.0010", "查询开票", "拆分开票",
					"选中交易流水号为(" + this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0001.0010", "查询开票", "拆分开票",
					"选中交易流水号为(" + this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "0");
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
			List transInfoList = new ArrayList();
			if (StringUtil.isNotEmpty(this.transId) && this.money != null && this.money.length > 0) {

				TransInfo searPar = new TransInfo();
				searPar.setTransId(transId);
				// 查询交易信息
				searPar = createBillService.findTransInfo(searPar);

				// 判断交易信息状态
				if (searPar == null) {
					sbMessage.append(" NotExistsTrans ");
				} else {

					this.request.setAttribute("amt", searPar.getBalance().toString());
					this.request.setAttribute("transId", searPar.getTransId());
					currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request.setAttribute("userId", currentUser.getId());
					}

				}

				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				for (int i = 0; i < this.money.length; i++) {
					BigDecimal transAmt = new BigDecimal(this.money[i]);
					// 拆分金额格式不对，请输入小数位最多2位的正数数字。
					if (transAmt.scale() > 2 || transAmt.compareTo(new BigDecimal(0.0)) < 0) {
						if (sbMessage.toString().indexOf(" MoneyError ") < 0) {
							sbMessage.append(" MoneyError ");
						}
						continue;
					}

				}

				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				// 合计税额
				BigDecimal taxAmt = new BigDecimal("0.00");
				for (int i = 0; i < this.money.length; i++) {
					TransInfo cloneTrans = new TransInfo();
					cloneTrans = (TransInfo) BeanUtils.cloneBean(searPar);
					BigDecimal taxRate = cloneTrans.getTaxRate();
					BigDecimal balance = new BigDecimal(money[i]);
					BigDecimal OneAddRate = taxRate.add(BigDecimal.ONE);
					BigDecimal incomeTemp = balance.divide(OneAddRate, 10, BigDecimal.ROUND_HALF_UP);
					BigDecimal taxCnyBalanceTemp = incomeTemp.multiply(taxRate);
					BigDecimal taxCnyBalance = taxCnyBalanceTemp.setScale(2, BigDecimal.ROUND_HALF_UP);
					cloneTrans.setBalance(balance);
					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append(
							(searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno()) ? searPar.getTtmpRcno()
									: "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
//					sb.append("】-【");
//					sb.append((searPar.getRepNum() != null && !"".equals(searPar.getRepNum()) ? searPar.getRepNum() : "        "));
//					sb.append("】\n主险名称：【");
//					sb.append((searPar.getPlanLongDesc() != null && !"".equals(searPar.getPlanLongDesc()) ? searPar.getPlanLongDesc() : ""));
//					sb.append("】\n交费起止日期：【");
//					sb.append((searPar.getInstFrom() != null ? searPar.getInstFrom() : ""));
//					sb.append("】-【");
//					sb.append((searPar.getInstTo() != null ? searPar.getInstTo() : ""));
					sb.append("】\n");
					cloneTrans.setRemark(sb.toString());
					cloneTrans.setTaxCnyBalance(taxCnyBalance);
					transInfoList.add(cloneTrans);
				}

				// 校验
				CheckResult result = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);

				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(result.getCheckFlag())) {
					sbMessage = sbMessage.append(result.getCheckResultMsg());
				} else {
					for (int i = 0; i < transInfoList.size(); i++) {
						List dataList = new ArrayList();
						dataList.add(transInfoList.get(i));
						createBillService.constructBillAndSaveAsSplit(dataList, currentUser);
					}

				}

				if (sbMessage != null && sbMessage.toString().length() > 0) {
					this.message = sbMessage.toString();
					this.request.setAttribute("message", sbMessage.toString());
					this.setMessage(sbMessage.toString());
					return ERROR;
				}

			}
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "拆分开票",
					"将交易流水号(" + this.transId + ")的交易进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "拆分开票",
					"将交易流水号(" + this.transId + ")的交易进行拆分开票处理", "0");
			log.error("TransInfoAction-transToManyBill", e);
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 申请开票交易
	 * 
	 * @return String
	 */
	public String applyInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		User user = this.getCurrentUser();

		try {
			// 校验数据库中是否存在已开票的申请记录
			// String businessType = this.request.getParameter("business_type2");
			String businessCode = this.request.getParameter("business_code2");
			// String source = this.request.getParameter("source2");

			// 先要把界面的东西初始化出来
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");
			applyFeeTypList = new HashMap<String, String>();
			for (Iterator iterator = feeTypList.keySet().iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// 只检索首期、续期、定结、保全
				if ("001".equals(type) || "002".equals(type) || "003".equals(type) || "015".equals(type)) {
					applyFeeTypList.put(type, (String) feeTypList.get(type));
				}
			}
			custTaxPayerTypeList = this.createSelectList(DataUtil.TAXPAYER_TYPE, null, true);
			transDataStatusList = DataUtil.getTransDataStatusListForPageListTrans();

			Map applyMap = new HashMap();

			// 只有保全或者定结类型，才需要去数据库中校验，根据类型和业务号校验如果存在 状态<>'未开票' 的记录，则直接返回前端提示错误
			String newBusinessType = "";
			/*
			 * if("003".equals(businessType) || "015".equals(businessType)){
			 * applyMap.put("repnum",businessCode);
			 * 
			 * if("003".equals(businessType)){ //若为保全则拆分为 保全加人、保全减人、计划变更、增额几个保全项
			 * newBusinessType = "016','017','018','019"; }
			 * 
			 * //不需要区分某用户某机构下的数据，只要能查询到即认为不可删除 applyMap.put("businessType",
			 * newBusinessType); applyMap.put("source",source);
			 * applyMap.put("datastatus","1");
			 * 
			 * Long CheckNum = transInfoService.findCheckApplyTransInfoCount(applyMap);
			 * if(CheckNum > 0){ this.message = "该笔业务已存在已开票或开票中记录，不允许重新开票"; return SUCCESS;
			 * } }
			 */
			// 走接口
			String returnStr = transInfoService.applyInvoice(request, user, businessCode);
			if (returnStr != null && "1".equals(returnStr.split("\\|")[0])) {
				// 接口调用失败
				this.message = returnStr.split("\\|")[1];
				return SUCCESS;
			}

			if ("bill".equalsIgnoreCase(fromFlag)) {
				this.setTransInfo(transInfo);
			}

			// 构造查询条件
			com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
			transInfo = this.getTransInfo();

			// 添加发票申请的查询条件
			/*
			 * transInfo.setFeeTyp(businessType); if("001".equals(businessType) ||
			 * "002".equals(businessType)){ transInfo.setCherNum(businessCode); }else
			 * if("003".equals(businessType) || "015".equals(businessType)){
			 * transInfo.setRepNum(businessCode); } transInfo.setDsouRce(source);
			 */

			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			if (user != null) {
				transInfo.setUserId(user.getId());
			}
			transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
			// 查询 符合条件的customer_id
			String customerName = transInfo.getCustomerName();
			List customerObjs = new ArrayList();
			List customerIds = new ArrayList();
			if (null != customerName && !customerName.equals("")) {
				customerObjs = customerService.findTransByCustomers(customerName);
			}
			for (int i = 0; i < customerObjs.size(); i++) {
				String customerId = ((Customer) customerObjs.get(i)).getCustomerID();
				customerIds.add(customerId);
			}
			transInfo.setCustomerIds(customerIds);
			// 查询符合条件的transType
			String transName = transInfo.getTransTypeName();
			List transNameObjs = new ArrayList();
			List transTypeList = new ArrayList();
			if (null != transName && !transName.equals("")) {
				Map map = new HashMap();
				map.put("transName", transName);
				map.put("customerTaxPayerType", transInfo.getCustomerTaxPayerType());
				transNameObjs = transInfoService.findTransTypeList(map);
			}
			for (int i = 0; i < transNameObjs.size(); i++) {
				TransTypeInfo transType = ((TransTypeInfo) transNameObjs.get(i));
				String transTypeId = ((TransTypeInfo) transNameObjs.get(i)).getTransTypeId();
				transTypeList.add(transTypeId);
			}
			transInfo.setTransTypeList(transTypeList);
			// // 获取相关联的客户ID
			// List list = transInfoService.findTransCustomerList(transInfo);
			// // 去重复
			// Map map = new HashMap();
			// for (int i = 0; i < list.size(); i++) {
			// String customerId = ((Customer) list.get(i)).getCustomerID();
			// map.put(customerId, customerId);
			// }
			// Object transCustomerList[] = map.keySet().toArray();
			// transInfo.setTransCustomerList(transCustomerList);
			this.paginationList.setShowCount("false");
			transInfoList = transInfoService.findTransInfoList(transInfo, paginationList);
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("transInfoList", transInfoList);
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "1");
			this.message = "数据请求成功";
			return SUCCESS;
		} catch (Exception e) {
			this.message = "系统处理异常";
			e.printStackTrace();
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTrans", e);
		} finally {
			this.request.setAttribute("message", this.message);
		}
		return ERROR;
	}

	/**
	 * “VMS销项进税管理->开票管理->开票申请”中的删除方法
	 */
	public void deleteTransData() {
		// 用于记录判断删除是否成功
		PrintWriter out = null;
		try {
			// 删除的记录trans_id
			String selectTransIds = request.getParameter("selectTransIds");
			// 删除的接口方法
			transInfoService.deleteTransData(selectTransIds);
			out = response.getWriter();
			out.print("Y");
		} catch (Exception e) {
			out.print("N");
			e.printStackTrace();
		}
	}

	public com.cjit.vms.trans.model.TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(com.cjit.vms.trans.model.TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public List getCustTaxPayerTypeList() {
		return custTaxPayerTypeList;
	}

	public void setCustTaxPayerTypeList(List custTaxPayerTypeList) {
		this.custTaxPayerTypeList = custTaxPayerTypeList;
	}

	public List getTransDataStatusList() {
		return transDataStatusList;
	}

	public void setTransDataStatusList(List transDataStatusList) {
		this.transDataStatusList = transDataStatusList;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
	}

	public CreateBillService getCreateBillService() {
		return createBillService;
	}

	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}

	public BillValidationService getBillValidationService() {
		return billValidationService;
	}

	public void setBillValidationService(BillValidationService billValidationService) {
		this.billValidationService = billValidationService;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String[] getMoney() {
		return money;
	}

	public void setMoney(String[] money) {
		this.money = money;
	}

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}

	public String getTtmpRcno() {
		return ttmpRcno;
	}

	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}

	public String getFeeTyp() {
		return feeTyp;
	}

	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}

	public String getBillFreq() {
		return billFreq;
	}

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public String getPolYear() {
		return polYear;
	}

	public void setPolYear(String polYear) {
		this.polYear = polYear;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

	public String getDsouRce() {
		return dsouRce;
	}

	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}

	public String getChanNel() {
		return chanNel;
	}

	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}

	public String getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(String premTerm) {
		this.premTerm = premTerm;
	}

	public String getHissBeginDte() {
		return hissBeginDte;
	}

	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}

	public String getHissEndDte() {
		return hissEndDte;
	}

	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}

	public Map<String, String> getApplyFeeTypList() {
		return applyFeeTypList;
	}

	public void setApplyFeeTypList(Map<String, String> applyFeeTypList) {
		this.applyFeeTypList = applyFeeTypList;
	}

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public String getPremTermArray() {
		return premTermArray;
	}

	public void setPremTermArray(String premTermArray) {
		this.premTermArray = premTermArray;
	}
}
