package com.cjit.vms.aisino.action.billprint;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.namespace.QName;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CharacterEncoding;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.DocXmlUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.aisino.service.BillPrintAisinoService;
import com.cjit.vms.aisino.service.HxServiceFactory;
import com.cjit.vms.aisino.util.StrFunc;
import com.cjit.vms.filem.util.FileUtil;
import com.cjit.vms.filem.util.ThreadLock;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.action.BillAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.model.SpecialTicket;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.vms.trans.service.storage.PaperInvoiceService;
import com.cjit.vms.trans.service.storage.PaperInvoiceTrackService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class BillInfoAisinoAction extends BillAction {

	String goodsListJson;
	
	/**
	 * 查询可供进行废票/红冲处理的票据列表
	 * 
	 * @return String
	 */
	public String listBillTrack() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			this.message = (String) this.request.getAttribute("message");
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}// 获取单次打印的

			billInfo.setBillBeginDate(this.getBillBeginDate());
			billInfo.setBillEndDate(this.getBillEndDate());
			billInfoList = billInfoService.findBillInfoList(billInfo,
					currentUser.getId(), paginationList);
			this.request.setAttribute("billInfoList", billInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(),
					DateUtils.DATE_FORMAT_YYYYMM);
			this.request.setAttribute("currMonth", currMonth);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "废票红冲", "销项税管理", "查询可供进行废票/红冲处理的票据信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0020", "废票红冲", "销项税管理", "查询可供进行废票/红冲处理的票据信息列表", "0");
			log.error("BillInfoAction-listBillTrack", e);
		}
		return ERROR;
	}

	/**
	 * 票据执行废票操作
	 * 
	 * @return String
	 */
	public String cancelBill() {
		String billId = "";
		try {
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillInfo bill = billInfoService
							.findBillInfo(selectBillIds[i]);
					if (bill != null) {
						if (!DataUtil.BILL_STATUS_5
								.equals(bill.getDataStatus())
								&& !DataUtil.BILL_STATUS_6.equals(bill
										.getDataStatus())
								&& !DataUtil.BILL_STATUS_7.equals(bill
										.getDataStatus())
								&& !DataUtil.BILL_STATUS_8.equals(bill
										.getDataStatus())
								&& !DataUtil.BILL_STATUS_9.equals(bill
										.getDataStatus())) {
							// 非完成状态票据不可做废票和红冲
							continue;
						}
						billId = bill.getBillId();
						// bill.setCancelFlag(DataUtil.FEIPIAO);
						User currentUser = this.getCurrentUser();
						if (currentUser != null) {
							// 记录废票发起人，以便交叉审核
							bill.setCancelInitiator(currentUser.getId());
						}
						billInfoService.saveBillInfo(bill, true);
						logManagerService.writeLog(request, this
								.getCurrentUser(), "0001.0010", "销项税管理",
								"票据跟踪", "申请对票据ID为(" + billId + ")的票据进行废票处理",
								"1");
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "销项税管理", "票据跟踪", "申请对票据ID为(" + billId
							+ ")的票据进行废票处理", "0");
			log.error("BillInfoAction-cancelBill", e);
			return ERROR;
		}
	}



	
	

	/**
	 * 发票编辑页面查看交易
	 * 
	 * @return
	 */
	public String billEditToTrans() {
		try {
			billInfo.setBillId((String) this.request.getParameter("billId"));
			this.request.getSession().setAttribute("curPage",
					Integer.valueOf(this.getCurPage()));

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-billEditToTrans", e);
		}
		return ERROR;
	}

	/**
	 * 
	 * 红冲发票编辑页面查看票样
	 * 
	 * */
	public String viewImgFromBillEdit1() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById1(billId);

			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType() + "");
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
			// map.put("billPasswd",billInfo);
			map.put("billItemList", billItemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone", billInfo
					.getCustomerAddressandphone());
			map.put("cancelBankandaccount", billInfo
					.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());

			String imgName;
			if ("red".equalsIgnoreCase(fromFlag)) {
				imgName = vmsCommonService.createRedMark(map);
			} else {
				imgName = vmsCommonService.createMark(map);
			}

			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票编辑页面查看票样
	 */
	public String viewImgFromBillEdit() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);

			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
			// map.put("billPasswd",billInfo);
			map.put("billItemList", billItemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone", billInfo
					.getCustomerAddressandphone());
			map.put("cancelBankandaccount", billInfo
					.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());

			String imgName;
			if ("red".equalsIgnoreCase(fromFlag)) {
				for (int i = 0; i < billItemList.size(); i++) {
					BillItemInfo item = (BillItemInfo) billItemList.get(i);
					
					item.setAmt(item.getAmt().abs());
					item.setTaxAmt(item.getTaxAmt().abs());
					item.setGoodsNo(item.getGoodsNo().abs());
				
				}
				imgName = vmsCommonService.createRedMark(map);
			} else {
				imgName = vmsCommonService.createMark(map);
			}

			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 发票打印页面查看票样
	 */
	public String viewImgFromBillPrint() {
		try {
			String billId = (String) this.request.getParameter("billId");
			billInfo = billTrackService.findBillInfoById(billId);

			List billItemList = billTrackService.findBillItemByBillId(billId);
			if (billItemList.size() > 9) {
				this.setRESULT_MESSAGE("发票最多包含9条数据！");
				return ERROR;
			}

			Map map = new HashMap();
			map.put("vatType", billInfo.getFapiaoType());
			map.put("billCode", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("billDate", billInfo.getBillDate());
			map.put("customerName", billInfo.getName());
			map.put("customerTaxno", billInfo.getTaxno());
			map.put("customerAddressandphone", billInfo.getAddressandphone());
			map.put("customerBankandaccount", billInfo.getBankandaccount());
			// map.put("billPasswd",billInfo);
			map.put("billItemList", billItemList);
			map.put("cancelName", billInfo.getCustomerName());
			map.put("cancelTaxno", billInfo.getCustomerTaxno());
			map.put("cancelAddressandphone", billInfo
					.getCustomerAddressandphone());
			map.put("cancelBankandaccount", billInfo
					.getCustomerBankandaccount());
			map.put("payeeName", billInfo.getPayee());
			map.put("reviewerName", billInfo.getReviewerName());
			map.put("drawerName", billInfo.getDrawerName());
			map.put("remark", billInfo.getRemark());

			String imgName = vmsCommonService.createMark(map);
			Map retMap = vmsCommonService.findCodeDictionary("BILL_IMG_PATH");

			filePath = retMap.get("BILL_IMG_PATH") + imgName;

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillTrackAction-viewImgFromBill", e);
		}
		return ERROR;
	}

	/**
	 * 手工添加票据界面展示明细列表
	 * 
	 * @return String
	 */
	public String listBillItemYS() {
		try {
			String billId = request.getParameter("billId");
			String isHandiwork = request.getParameter("isHandiwork");
			if (StringUtil.isNotEmpty(billId)) {
				List billItemList = billInfoService
						.findBillItemInfoList(new BillItemInfo(billId, null));
				this.request.setAttribute("billItemList", billItemList);
				this.request.setAttribute("billId", billId);
			}
			this.request.setAttribute("isHandiwork", isHandiwork);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillItem", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 票据预开界面展示明细列表
	 * 
	 * @return String
	 */
	public String billItemPreListYS() {
		try {
			setResultMessages("");
			String billId = request.getParameter("billId");
			if (StringUtil.isNotEmpty(billId)) {
				List billItemList = billInfoService
						.findBillItemInfoList(new BillItemInfo(billId, null));
				this.request.setAttribute("billItemList", billItemList);
				this.request.setAttribute("billId", billId);
			}
			request.setAttribute("taxno", request.getParameter("taxno"));
			request.setAttribute("fapiaoType", billInfo.getFapiaoType());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-billItemPreListYS", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * @return 查看对应的交易列表
	 */
	public String inputBillTrans() {
		try {
			billInfo.setBillId((String) this.request.getParameter("billId"));
			this.request.getSession().setAttribute("curPage",
					Integer.valueOf(this.getCurPage()));

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-inputBillTrans", e);
		}
		return ERROR;
	}

	/**
	 * 保存/提交 票据
	 * 
	 * @return String
	 */
	public String UpdateRemarkAndPayee() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		String billId = billInfo.getBillId();
		String remark = billInfo.getRemark();
		String payee = billInfo.getPayee();
		billInfoService.UpdateRemarkAndPayee(billId, remark, payee);

		return SUCCESS;
	}

	

	/**
	 * 票据预开界面
	 * 
	 * @return String
	 */
	public String billPreInvoiceForm() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		setResultMessages("");

		InstInfo in = new InstInfo();
		in.setUserId(this.getCurrentUser().getId());
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		in.setLstAuthInstIds(lstAuthInstId);
		in.setTaxFlag("true");//筛除税号为空的机构
		authInstList = taxDiskInfoService.getInstInfoList(in);
		this.setCustomerId(this.getCustomerId());
		this.setFaPiaoType(this.getFaPiaoType());
		mapVatType = this.vmsCommonService.findCodeDictionary("VAT_TYPE");
		return SUCCESS;
	}

	

	public String queryBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {

			String transId = request.getParameter("transId");

			transInfo = new TransInfo();
			transInfo = billInfoService.findTransInById(transId);
			if (transInfo != null) {
				BillInfo bill = new BillInfo();
				bill.setTransId(transId);
				billInfoList = billInfoService.findBillInfoList(bill);
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-queryBill", e);
		}
		return ERROR;
	}

	/**
	 * 进入票据添加界面
	 * 
	 * @return String
	 */
	public String createBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			billInfo = new BillInfo();
			billInfo.setIsHandiwork(DataUtil.BILL_ISHANDIWORK_3);
			businessList = this.businessService
					.findBusinessList(new Business());
			User currentUser = this.getCurrentUser();
			if (currentUser != null) {
				// 开票人信息
				billInfo.setDrawer(currentUser.getId());
				billInfo.setDrawerName(currentUser.getName());
			}
			Organization org = new Organization();
			org.setId(currentUser.getOrgId());
			org = organizationService.getOrganization(org);
			if (org != null) {
				billInfo.setInstCode(org.getId());
				billInfo.setName(org.getTaxperName());
				billInfo.setTaxno(org.getTaxperNumber());
				billInfo.setAddressandphone(org.getTaxAddress() + " "
						+ org.getTaxTel());
				billInfo.setBankandaccount(org.getTaxBank() + " "
						+ org.getAccount());
			}
			this.request.setAttribute("flag", flag);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-createBill", e);
		}
		return ERROR;
	}

	/**
	 * 进入票据明细编辑界面
	 * 
	 * @return String
	 */
	public String editBillItem() {
		try {
			String billId = request.getParameter("billId");
			String billItemId = request.getParameter("billItemId");// 当前票据ID
			if (!"dis".equalsIgnoreCase(this.flag)) {
				// 正常编辑票据明细记录
				if (StringUtil.isEmpty(billItemId)) {
					// 手工添加新票据明细
					billItem = new BillItemInfo();
					billItem.setBillId(billId);
					this.request.setAttribute("billItemId", "");
					this.request.setAttribute("billId", billId);
				} else {
					billItem = this.billInfoService
							.findBillItemInfo(billItemId);
					if ("1".equals(billItem.getRowNature())
							&& StringUtil.isNotEmpty(billItem.getDisItemId())) {
						// 当前记录为折扣行记录，查询被折扣票据明细信息
						BillItemInfo oriBillItem = this.billInfoService
								.findBillItemInfo(billItem.getDisItemId());
						if (oriBillItem != null) {
						}
						this.request.setAttribute("flag", "dis");
					} else {
						billItem.setDisItemId(null);
					}
					this.request.setAttribute("billItemId", billItemId);
					this.request.setAttribute("billId", billId);
				}
				this.request.setAttribute("flag", "edit");
				businessList = this.businessService
						.findBusinessList(new Business());
			} else {
				// 创建票据折扣行记录
				billItem = this.billInfoService.findBillItemInfo(billItemId);
				billItem.setDisItemId(billItem.getBillItemId());
				billItem.setBillItemId(null);
				billItem.setRowNature("1");
				// this.request.setAttribute("billItemId", billItemId);
				// this.request.setAttribute("billId", billId);
				this.request.setAttribute("flag", "dis");
				// businessList = this.businessService
				// .findBusinessList(new Business(null, billItem
				// .getTransType()));
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-editBillItem", e);
		}
		return ERROR;
	}

	/**
	 * 票据预开 进入票据明细编辑界面
	 * 
	 * @return String
	 */
	public String editPreBillItem() {
		try {
			String billId = request.getParameter("billId"); // 票据ID
			String billItemId = request.getParameter("billItemId"); // 票据明细ID
			String taxno = request.getParameter("taxno"); // 纳税人识别号
			String fapiaoType = request.getParameter("fapiaoType"); // 发票类型
			this.request.removeAttribute("billItem");
			String type = request.getParameter("type");
			// 商品名称list取得
			mapGoodsList = new HashMap();
			GoodsInfo goods = new GoodsInfo();
			goods.setTaxNo(taxno);
			User user = getCurrentUser();
			Organization org = new Organization();
			org.setId(user.getOrgId());
			org = this.baseDataService.getOrganization(org);

			List goodsList = this.baseDataService.findGoodsInfoList(goods);
			if (goodsList != null && goodsList.size() > 0) {
				for (int i = 0; i < goodsList.size(); i++) {
					GoodsInfo info = (GoodsInfo) goodsList.get(i);
					mapGoodsList.put(info.getGoodsNo(), info.getGoodsName());
				}
			}
			this.request.setAttribute("mapGoodsList", mapGoodsList);

			// 是否含税list取得
			mapTaxList = this.vmsCommonService.findCodeDictionary("TAX_FLAG");
			this.request.setAttribute("mapTaxList", mapTaxList);

			// 税率的取得
			VmsTaxInfo vmsTaxInfo = new VmsTaxInfo();
			vmsTaxInfo.setTaxno(taxno);
			vmsTaxInfo.setFapiaoType(fapiaoType);
			List txtList = this.baseDataService.findVmsTaxInfoList(vmsTaxInfo);
			if (txtList != null && txtList.size() > 0) {
				VmsTaxInfo info = (VmsTaxInfo) txtList.get(0);
				this.setTaxId(info.getTaxId());
				this.setTaxRate(info.getTaxRate());
			}

			if (StringUtil.isNotEmpty(billId)
					&& StringUtil.isNotEmpty(billItemId)) {
				List billItemList = billInfoService
						.findBillItemInfoList(new BillItemInfo(billId,
								billItemId));
				if (billItemList != null && billItemList.size() > 0) {
					this.request.setAttribute("billItem", billItemList.get(0));
				}
			} else {
				BillItemInfo billItem = new BillItemInfo();
				billItem.setTaxItem(getTaxId());
				// billItem.setTaxRate(new BigDecimal(getTaxRate()));
				this.request.setAttribute("billItem", billItem);
			}
			this.request.setAttribute("billItem.billId", billId);
			if (billItemId == null || "".equals(billItemId)) {
				this.request.setAttribute("billItem.billItemId", "");
			} else {
				this.request.setAttribute("billItem.billItemId", billItemId);
			}
			this.request.setAttribute("taxno", taxno);
			this.request.setAttribute("fapiaoType", fapiaoType);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-editPreBillItem", e);
		}
		return ERROR;
	}

	public String saveBillItem() {
		try {
			if (StringUtil.IsEmptyStr(billItem.getBillItemId())) {
				billItem.setBillItemId(createBusinessId("BI"));
				billItem.setRowNature("0");// 默认添加正常
				billInfoService.saveBillItemInfo(billItem, false);
			} else {
				billInfoService.saveBillItemInfo(billItem, true);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-createBillItem", e);
		}
		return ERROR;
	}

	public String savePreBillItem() {
		try {
			// 含税是 票面金额 = 金额 - 税额
			String taxFlax = billItem.getTaxFlag();
			if ("Y".equals(taxFlax)) {
				BigDecimal amtBig = billItem.getAmt().subtract(
						billItem.getTaxAmt());
				billItem.setAmt(amtBig);
			}
			setResultMessages("");
			String open = request.getParameter("open");
			// 是否是票据预开修改商品信息
			if (!"".equals(open) && "open".equals(open)) {
				billInfoService.updatePreBillItemInfo(billItem, false);
				setResultMessages("修改成功");
				return "open";
			}
			if (StringUtil.IsEmptyStr(billItem.getBillItemId())) {
				billItem.setBillItemId(createBusinessId("BI"));
				billInfoService.savePreBillItemInfo(billItem, false);
			} else {
				billInfoService.savePreBillItemInfo(billItem, true);
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-createBillItem", e);
		}
		return ERROR;
	}

	/**
	 * 添加折扣行信息
	 * 
	 * @return String
	 */
	public String disBillItem() {
		try {
			if (StringUtil.IsEmptyStr(billItem.getBillItemId())
					&& StringUtil.isNotEmpty(billItem.getDisItemId())
					&& billItem.getDiscountRate() != null) {
				// 查询原票据明细
				BillItemInfo oriBillItem = billInfoService
						.findBillItemInfo(billItem.getDisItemId());
				// 修改原票据明细 【商品行性质：2-被折扣行】并保存
				oriBillItem.setRowNature("2");
				billInfoService.saveBillItemInfo(oriBillItem, true);
				// 构造折扣行信息 【商品行性质：1-折扣行】
				BillItemInfo disBillItem = new BillItemInfo();
				disBillItem.setBillItemId(createBusinessId("BI"));
				disBillItem.setDiscountRate(billItem.getDiscountRate());
				disBillItem.setBillId(oriBillItem.getBillId());
				disBillItem.setDisItemId(oriBillItem.getBillItemId());
				disBillItem.setRowNature("1");
				disBillItem.setGoodsName(oriBillItem.getGoodsName());
				disBillItem.setAmt(new BigDecimal(0.0).subtract(oriBillItem
						.getAmt().multiply(
								new BigDecimal(1).subtract(billItem
										.getDiscountRate()))));
				disBillItem.setTaxRate(oriBillItem.getTaxRate());
				disBillItem.setTaxAmt(new BigDecimal(0.0).subtract(oriBillItem
						.getTaxAmt().multiply(
								new BigDecimal(1).subtract(billItem
										.getDiscountRate()))));
				disBillItem.setTaxFlag(oriBillItem.getTaxFlag());
				billInfoService.saveBillItemInfo(disBillItem, false);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-createBillItem", e);
		}
		return ERROR;
	}

	public String deleteBillItem() {
		try {
			String billItemId = request.getParameter("billItemId");
			// 查询准备删除的票据明细
			BillItemInfo billItem = billInfoService
					.findBillItemInfo(billItemId);
			// 依该票据的商品行性质进行不同操作
			if ("0".equals(billItem.getRowNature())) {
				// 正常行，直接删除
				billInfoService.deleteBillItemInfo(null, billItemId);
			} else if ("1".equals(billItem.getRowNature())) {
				// 折扣行，删除后修改对应被折扣行的商品行性质为正常行
				if (StringUtil.isNotEmpty(billItem.getDisItemId())) {
					// 查询被折扣行票据明细信息
					BillItemInfo oriBillItem = billInfoService
							.findBillItemInfo(billItem.getDisItemId());
					oriBillItem.setRowNature("0");
					billInfoService.saveBillItemInfo(oriBillItem, true);
				}
				billInfoService.deleteBillItemInfo(null, billItemId);
			} else if ("2".equals(billItem.getRowNature())) {
				// 被折扣行，连同对应折扣行一起删除
				BillItemInfo billItemTemp = new BillItemInfo();
				billItemTemp.setBillId(billItem.getBillId());
				billItemTemp.setDisItemId(billItem.getBillItemId());
				billItemTemp.setRowNature("1");
				// 查询对应折扣行记录
				List billItemList = billInfoService
						.findBillItemInfoList(billItemTemp);
				if (CollectionUtil.isNotEmpty(billItemList)) {
					for (Iterator t = billItemList.iterator(); t.hasNext();) {
						BillItemInfo disBillItem = (BillItemInfo) t.next();
						if (disBillItem != null
								&& StringUtil.isNotEmpty(disBillItem
										.getBillItemId())) {
							// 删除折扣行记录
							billInfoService.deleteBillItemInfo(null,
									disBillItem.getBillItemId());
						}
					}
				}
				billInfoService.deleteBillItemInfo(null, billItemId);
			}
			return this.listBillItem();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-deleteBillItem", e);
		}
		return ERROR;
	}

	
	public String listBillItem() {
		try {
			String billId = request.getParameter("billId");
			String isHandiwork = request.getParameter("isHandiwork");
			if (StringUtil.isNotEmpty(billId)) {
				List billItemList = billInfoService
						.findBillItemInfoList(new BillItemInfo(billId, null));
				this.request.setAttribute("billItemList", billItemList);
				this.request.setAttribute("billId", billId);
			}
			this.request.setAttribute("isHandiwork", isHandiwork);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillItem", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 票据编辑界面，依所选机构加载我方信息
	 */
	public void loadOurInfo() {
		try {
			StringBuffer sb = new StringBuffer();
			String instCode = this.request.getParameter("instCode");
			Organization org = new Organization();
			org.setId(instCode);
			org = organizationService.getOrganization(org);
			if (org != null) {
				// 纳税人名称、纳税人识别号、地址、电话、银行、账号
				sb.append(org.getTaxperName()).append("###").append(
						org.getTaxperNumber()).append("###").append(
						org.getTaxAddress()).append("###").append(
						org.getTaxTel()).append("###").append(org.getTaxBank())
						.append("###").append(org.getAccount());
			}
			this.response.setContentType("text/html; charset=UTF-8");
			log.info("loadOurInfo : " + sb.toString());
			this.response.getWriter().print(sb.toString());
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadOurInfo : ", ex);
		}
	}

	/**
	 * 票据明细编辑界面，依所选交易类型，加载是否含税、税率，并计算金额、税额
	 */
	public void loadTransTypeInfo() {
		try {
			StringBuffer sb = new StringBuffer();
			String transType = this.request.getParameter("transType");
			String price = this.request.getParameter("price");
			String number = this.request.getParameter("number");
			if (StringUtil.isNotEmpty(transType)) {
				Business buss = businessService.findBusiness(new Business(null,
						transType));
				if (buss != null) {
					// 是否含税
					sb.append(buss.getHasTax()).append(";");
					if (buss.getTaxRate() != null) {
						// 税率
						sb.append(buss.getTaxRate()).append(";");
						if ("1".equals(buss.getHasTax())
								&& StringUtil.isNotEmpty(price)
								&& StringUtil.isNotEmpty(number)) {
							BigDecimal bdPrice = new BigDecimal(price);
							BigDecimal bdNumber = new BigDecimal(number);
							// 计算金额
							BigDecimal bdAmount = bdPrice.multiply(bdNumber);
							// 计算税额
							BigDecimal bdTaxAmt = bdAmount.multiply(buss
									.getTaxRate());
							// 金额;税额
							sb.append(bdAmount.toString()).append(";").append(
									bdTaxAmt.toString());
						} else {
							// 金额;税额
							sb.append("0.00;").append("0.00");
						}
					} else {
						// 税率;金额;税额
						sb.append("0.0000;").append("0.00;").append("0.00");
					}
				}
			}
			this.response.setContentType("text/html; charset=UTF-8");
			log.info("loadTransTypeInfo : " + sb.toString());
			this.response.getWriter().print(sb.toString());
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}

	/**
	 * 票据明细信息编辑保存后，自动计算更新票据主单中的金额等字段
	 */
	public void loadBillAmtInfo() {
		try {
			StringBuffer sb = new StringBuffer();
			String billId = this.request.getParameter("billId");
			if (StringUtil.isNotEmpty(billId)) {
				BigDecimal bdAmtSum = new BigDecimal(0.00);
				BigDecimal bdTaxAmtSum = new BigDecimal(0.00);
				BigDecimal bdSumAmt = new BigDecimal(0.00);
				List billItemList = billInfoService
						.findBillItemInfoList(new BillItemInfo(billId, null));
				for (Iterator t = billItemList.iterator(); t.hasNext();) {
					BillItemInfo billItem = (BillItemInfo) t.next();
					bdAmtSum = bdAmtSum.add(billItem.getAmt());
					bdTaxAmtSum = bdTaxAmtSum.add(billItem.getTaxAmt());
				}
				bdSumAmt = bdAmtSum.add(bdTaxAmtSum);
				sb.append(bdAmtSum.toString()).append("###").append(
						bdTaxAmtSum.toString()).append("###").append(
						bdSumAmt.toString());
			}
			this.response.setContentType("text/html; charset=UTF-8");
			log.info("loadBillAmtInfo : " + sb.toString());
			this.response.getWriter().print(sb.toString());
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadTransTypeInfo : ", ex);
		}
	}

	/**
	 * 进入销项税发票查询界面
	 * 
	 * @return String
	 */
	public String listBill() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			BillInfo billInfo = new BillInfo();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			if (StringUtil.isNotEmpty(flag)) {
				billInfo.setSearchFlag(flag);
			}
			billInfo.setBillBeginDate(this.getBillBeginDate());
			billInfo.setBillEndDate(this.getBillEndDate());
			billInfo.setSumAmtBegin(this.getSumAmtBegin());
			billInfo.setSumAmtEnd(this.getSumAmtEnd());
			if (StringUtil.isNotEmpty(this.getBillCode())) {
				billInfo.setBillCode(this.getBillCode());
			}
			if (StringUtil.isNotEmpty(this.getBillNo())) {
				billInfo.setBillNo(this.getBillNo());
			}
			billInfoList = billInfoService.findBillInfoList(billInfo,
					currentUser.getId(), paginationList);
			this.request.setAttribute("billInfoList", billInfoList);
			this.request.getSession().setAttribute("billBeginDate",
					billInfo.getBillBeginDate());
			this.request.getSession().setAttribute("billEndDate",
					billInfo.getBillEndDate());
			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBill", e);
		}
		return ERROR;
	}

	/**
	 * 进入发票红冲编辑界面
	 * 
	 * @return String
	 */
	public String billToWriteoff() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			String billId = request.getParameter("billId");
			billInfo = billInfoService.findBillInfo(billId);
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(billId);
			List itemList = billInfoService.findBillItemInfoList(billItem);
			List billItemList = new ArrayList();
			if (CollectionUtil.isNotEmpty(itemList)) {
				// 过滤掉打折行信息
				for (Iterator t = itemList.iterator(); t.hasNext();) {
					BillItemInfo item = (BillItemInfo) t.next();
					if ("0".equals(item.getRowNature())) {
						// 正常行
						billItemList.add(item);
					} else if ("1".equals(item.getRowNature())) {
						// 打折行
						continue;
					} else if ("2".equals(item.getRowNature())) {
						// 被打折行
						for (Iterator t1 = itemList.iterator(); t1.hasNext();) {
							BillItemInfo item1 = (BillItemInfo) t1.next();
							// 计算打折后金额和税金
							if ("1".equals(item1.getRowNature())
									&& item.getBillItemId().equals(
											item1.getDisItemId())) {
								item.setDiscountRate(item1.getDiscountRate());
								item.setAmt(item.getAmt().subtract(
										item1.getAmt()));
								item.setTaxAmt(item.getTaxAmt().subtract(
										item1.getTaxAmt()));
								billItemList.add(item);
								break;
							} else {
								continue;
							}
						}
					}
				}
			}
			this.request.setAttribute("billItemList", billItemList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-billToWriteoff", e);
		}
		return ERROR;
	}

	/**
	 * 对发票进行红冲操作
	 * 
	 * @return String
	 */
	public String writeOffBill() {
		try {
			// 原票据ID
			String billId = request.getParameter("billId");
			// 通知单编号
			String noticeNo = request.getParameter("noticeNo");
			// 备注
			String remark = request.getParameter("remark");
			// 票据明细ID
			String[] billItemIds = request.getParameterValues("billItemId");
			// 票据明细红冲数量
			String[] cancelGoodsNos = request
					.getParameterValues("cancelGoodsNo");
			// 票据折扣率
			String[] discountRates = request.getParameterValues("discountRate");
			// 获取票据明细信息
			List billItemList = null;
			if (billItemIds != null && billItemIds.length > 0) {
				billItemList = new ArrayList();
				for (int i = 0; i < billItemIds.length; i++) {
					BillItemInfo billItem = this.billInfoService
							.findBillItemInfo(billItemIds[i]);
					billItemList.add(billItem);
				}
			}
			// 调用方法 生成红冲票据
			boolean isSuccess = this.writeOffBill(billId, noticeNo, remark,
					billItemList, cancelGoodsNos, discountRates,
					DataUtil.BILL_STATUS_2);
			if (isSuccess) {
				this.message = "红冲成功！";
				this.request.setAttribute("message", this.message);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-writeOffBill", e);
		}
		return ERROR;
	}

	/**
	 * 进入发票打印列表界面
	 * 
	 * @return String billInfo.billBeginDate
	 */
	public String listBillPrint() {
		if (!sessionInit(true)) {
			request.setAttribute("message", "用户失效");
			return ERROR;
		}
		try {
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.billInfo.setBillBeginDate(null);
				this.billInfo.setBillEndDate(null);
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().setAttribute("curPage",
						new Integer(1));
				this.request.getSession().setAttribute("pageSize",
						new Integer(20));
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				this.request.getSession().removeAttribute("customerName");
				this.request.getSession().removeAttribute("customerTaxno");
				this.request.getSession().removeAttribute("isHandiwork");
				this.request.getSession().removeAttribute("issueType");
				this.request.getSession().removeAttribute("fapiaoType");
				this.request.getSession().removeAttribute("dataStatus");
				fromFlag = null;
			} else if ("view".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				// 申请开票日期(开始)
				if (!String
						.valueOf(
								this.request.getSession().getAttribute(
										"billBeginDate")).isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billBeginDate")))) {
					this.billInfo.setBillBeginDate(String.valueOf(this.request
							.getSession().getAttribute("billBeginDate")));
				}
				// 申请开票日期(结束)
				if (!String.valueOf(
						this.request.getSession().getAttribute("billEndDate"))
						.isEmpty()
						&& !"null".equals(String.valueOf(this.request
								.getSession().getAttribute("billEndDate")))) {
					this.billInfo.setBillEndDate(String.valueOf(this.request
							.getSession().getAttribute("billEndDate")));
				}
				// 客户名称
				if (!"".equals(this.request.getSession().getAttribute(
						"customerName"))) {
					this.billInfo.setCustomerName(String.valueOf(this.request
							.getSession().getAttribute("customerName")));
				}
				// 客户纳税人识别号
				if (!"".equals(this.request.getSession().getAttribute(
						"customerTaxno"))) {
					this.billInfo.setCustomerTaxno(String.valueOf(this.request
							.getSession().getAttribute("customerTaxno")));
				}
				// 是否手工录入
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("isHandiwork")))) {
					this.billInfo.setIsHandiwork(String.valueOf(this.request
							.getSession().getAttribute("isHandiwork")));
				}
				// 开具类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("issueType")))) {
					this.billInfo.setIssueType(String.valueOf(this.request
							.getSession().getAttribute("issueType")));
				}
				// 发票类型
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("fapiaoType")))) {
					this.billInfo.setFapiaoType(String.valueOf(this.request
							.getSession().getAttribute("fapiaoType")));
				}
				// 发票状态
				if (!"null".equals(String.valueOf(this.request.getSession()
						.getAttribute("dataStatus")))) {
					this.billInfo.setDataStatus(String.valueOf(this.request
							.getSession().getAttribute("dataStatus")));
				}
				fromFlag = null;
			} else {
				billInfo = new BillInfo();
				this.billInfo.setBillBeginDate(this.request
						.getParameter("billInfo.billBeginDate"));
				this.billInfo.setBillEndDate(this.request
						.getParameter("billInfo.billEndDate"));
				this.billInfo.setCustomerName(this.request
						.getParameter("billInfo.customerName"));
				this.billInfo.setCustomerTaxno(this.request
						.getParameter("billInfo.customerTaxno"));
				this.billInfo.setIsHandiwork(this.request
						.getParameter("billInfo.isHandiwork"));
				this.billInfo.setIssueType(this.request
						.getParameter("billInfo.issueType"));
				this.billInfo.setFapiaoType(this.request
						.getParameter("billInfo.fapiaoType"));
				this.billInfo.setDataStatus(this.request
						.getParameter("billInfo.dataStatus"));
			}

			try {
				printLimitValue = Integer.valueOf(
						paramConfigVmssService.findvaluebyName("单次打印限值（张）"))
						.intValue();
				taxParam = paramConfigVmssService.findvaluebyName("税控参数");

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			// billInfo.setBillBeginDate(this.getBillBeginDate());
			// billInfo.setBillEndDate(this.getBillEndDate());
			// 默认按照专用发票查询
			if (billInfo.getFapiaoType() == null
					|| billInfo.getFapiaoType().equals("0")) {
				billInfo.setFapiaoType("0");
			}
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			billInfo.setLstAuthInstId(lstAuthInstId);
			billInfo.setUserId(currentUser.getId());
			billInfoList = billInfoService.findBillInfoListNew(billInfo,
					currentUser.getId(), paginationList);

			this.request.setAttribute("billInfoList", billInfoList);
			if (this.billInfo.getBillBeginDate() != null) {
				this.request.getSession().setAttribute("billBeginDate",
						this.billInfo.getBillBeginDate());
			}
			if (this.billInfo.getBillEndDate() != null) {
				this.request.getSession().setAttribute("billEndDate",
						this.billInfo.getBillEndDate());
			}
			if (this.billInfo.getCustomerName() != null) {
				this.request.getSession().setAttribute("customerName",
						this.billInfo.getCustomerName());
			}
			if (this.billInfo.getCustomerTaxno() != null) {
				this.request.getSession().setAttribute("customerTaxno",
						this.billInfo.getCustomerTaxno());
			}
			if (this.billInfo.getIsHandiwork() != null) {
				this.request.getSession().setAttribute("isHandiwork",
						this.billInfo.getIsHandiwork());
			}
			if (this.billInfo.getIssueType() != null) {
				this.request.getSession().setAttribute("issueType",
						this.billInfo.getIssueType());
			}
			if (this.billInfo.getFapiaoType() != null) {
				this.request.getSession().setAttribute("fapiaoType",
						this.billInfo.getFapiaoType());
			}
			if (this.billInfo.getDataStatus() != null) {
				this.request.getSession().setAttribute("dataStatus",
						this.billInfo.getDataStatus());
			}

			String currMonth = DateUtils.toString(new Date(), "yyyy-MM");
			this.request.setAttribute("currMonth", currMonth);
			this.request.getSession().setAttribute("curPage",
					String.valueOf(paginationList.getCurrentPage()));
			this.request.getSession().setAttribute("pageSize",
					String.valueOf(paginationList.getPageSize()));

			// czl
			int zbillInfo = billInfoService.findz("G", "VMS_BILL_INFO");
			int pbillInfo = billInfoService.findp("G", "VMS_BILL_INFO");
			this.request.setAttribute("zbillInfo", Integer.toString(zbillInfo));
			this.request.setAttribute("pbillInfo", Integer.toString(pbillInfo));
			return "disk";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillPrint", e);
		}
		return ERROR;
	}

	/**
	 * 打印发票
	 * 
	 * @return String
	 */
	public String printBill() {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if (this.selectBillIds != null && this.selectBillIds.length > 0) {
				// User currentUser = this.getCurrentUser();
				// 循环查询选中票据信息
				for (int i = 0; i < this.selectBillIds.length; i++) {
					BillInfo bill = billInfoService
							.findBillInfo(selectBillIds[i]);
					String billId = selectBillIds[i];
					String clientNo = "KP001"; // 开票端编号,每个开票点对应唯一编号
					String taxMachineIP = "127.0.0.1"; // 税控机标识（税控盘所在机器IP地址，IP地址格式）
					if (bill != null) {
						// 查询发票明细
						List billItemList = billInfoService
								.findBillItemInfoList(new BillItemInfo(bill
										.getBillId(), null));
						bill.setBillItemList(billItemList);
						String sysInvNo = bill.getBillId(); // 单据号
						String invoiceList = "0"; // 是否打印清单 1 打印清单 0 不打印清单
						String invoiceSplit = "0"; // 是否自动拆分 1 拆分 0 不拆分
						String invoiceConsolidate = "0"; // 是否合并操作 1 合并 0 不合并
						String invoiceData = FileUtil
								.createXmlForBillPrint(bill); // 发票数据（XML格式）
						String callResult = callServiceAxis(clientNo,
								taxMachineIP, sysInvNo, invoiceList,
								invoiceSplit, invoiceConsolidate, invoiceData);
						if (StringUtil.isNotEmpty(callResult)) {
							Map map = receiveXml(callResult);
							if ("0".equals(map.get("OperateFlag"))) {
								// updateTransDatastatus()
								// 成功
								// 将交易状态改为99
								List transIds = billInfoService
										.findtransId(billId);
								for (int j = 0; j < transIds.size(); j++) {
									// tom 添加 余额不为零的条件
									BillInfo info = (BillInfo) transIds.get(j);
									info.setLstAuthInstId(lstAuthInstId);
									TransInfo transInfo = transInfoService
											.findTransInfo(info);
									if (transInfo.getBalance().equals(
											new BigDecimal(0))) {

										transInfoService.updateTransDatastatus(
												info.getTransId(),
												DataUtil.TRANS_STATUS_99);
									}
								}
								// 将票据状态改为99
								billInfoService.setBillStatus(billId);
								// 获取税控盘中的发票号码和发票号码
								String invoiceNo = (String) map.get("InvNo");
								String invoiceCode = (String) map
										.get("InvCode");
								Map paraMap = new HashMap();
								paraMap.put("invoiceNo", invoiceNo);
								paraMap.put("invoiceCode", invoiceCode);
								paraMap.put("invoiceCode", invoiceCode);
								paraMap.put("billId", billId);
								// 将发票信息更新到票据信息表中
								billInfoService.updateBillInfo(paraMap);
							} else if ("1".equals(map.get("OperateFlag"))) {
								// 失败
							}
						}
						// 提交待审核
						// 写入复核人为当前用户，修改票据状态为审核通过
						/**
						 * bill.setReviewer(currentUser.getId());
						 * bill.setDataStatus(DataUtil.BILL_STATUS_3);
						 * billInfoService.saveBillInfo(bill, true); //
						 * 查询对应交易信息，修改交易状态为开票中 TransInfo transInfo = new
						 * TransInfo(); transInfo.setBillId(bill.getBillId());
						 * List transList = transInfoService
						 * .findTransInfoList(transInfo); for (Iterator t =
						 * transList.iterator(); t.hasNext();) { TransInfo trans
						 * = (TransInfo) t.next(); if
						 * (DataUtil.TRANS_STATUS_2.equals(trans
						 * .getDataStatus())) {
						 * transInfoService.updateTransDatastatus(trans
						 * .getTransId(), DataUtil.TRANS_STATUS_3); } }
						 */
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-auditBill", e);
			return ERROR;
		}
	}

		/**
	 * 发票打印-EXCEL导出发票
	 * 
	 * @throws Exception
	 */
	public void xxsToExcel() throws Exception {
		try {
			String[] selectBillIds = (String[]) request
					.getAttribute("selectBillIds");
			List list = new ArrayList();
			User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			billInfo.setSearchFlag("print");
			billInfo.setBillBeginDate(this.getBillBeginDate());
			billInfo.setBillEndDate(this.getBillEndDate());
			billInfoList = billInfoService.findBillInfoList(billInfo,
					currentUser.getId(), selectBillIds);
			if (billInfoList != null && billInfoList.size() > 0) {
				for (int i = 0; i < billInfoList.size(); i++) {
					BillInfo bi = (BillInfo) billInfoList.get(i);
					Map m = new HashMap();
					// 客户纳税人名称 CUSTOMER_NAME
					m.put("customerName", bi.getCustomerName());
					// 客户纳税人识别号、CUSTOMER_TAXNO
					m.put("customerTaxno", bi.getCustomerTaxno());
					// 合计金额 AMT_SUM
					m.put("amtSum", bi.getAmtSum());
					// 合计税额、 TAX_AMT_SUM
					m.put("taxAmtSum", bi.getTaxAmtSum());
					// 价税合计 、SUM_AMT
					m.put("sumAmt", bi.getSumAmt());

					// m.put("userId", o[0]);
					list.add(m);
				}
			}

			StringBuffer fileName = new StringBuffer("销项税打印");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, list);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("销项税", 0);
		// 客户纳税人名称 、客户纳税人识别号、 合计金额 、合计税额、 价税合计
		Label header1 = new Label(0, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "合计金额", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "合计税额", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "价税合计", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		for (int i = 0; i < 5; i++) {
			ws.setColumnView(i, 18);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			Map o = (Map) content.get(i);
			int column = count++;
			setWritableSheet(ws, o, column);
		}
		wb.write();
		wb.close();
	}

	/**
	 * 发票打印-EXCEL导出发票
	 * 
	 * @throws Exception
	 */
	public void xxsToExcelNew() throws Exception {
		try {
			String[] selectBillIds = (String[]) request
					.getAttribute("selectBillIds");
			if ("menu".equalsIgnoreCase(fromFlag)) {
				billInfo = new BillInfo();
				this.setBillBeginDate(null);
				this.setBillEndDate(null);
				this.request.getSession().removeAttribute("billBeginDate");
				this.request.getSession().removeAttribute("billEndDate");
				fromFlag = null;
			}
			billInfo.setSearchFlag("print");
			// billInfo.setBillBeginDate(this.getBillBeginDate());
			// billInfo.setBillEndDate(this.getBillEndDate());
			billInfoList = billInfoService.billsToExcelNew(billInfo);
			StringBuffer fileName = new StringBuffer("发票打印");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcelNew(os, billInfoList);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	public void writeToExcelNew(OutputStream os, List billInfoList)
			throws IOException, RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("纸票打印", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "申请开票日期", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "开票日期", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "客户纳税人名称", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "客户纳税人识别号", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "发票代码", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "发票号码", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "开票人", JXLTool.getHeader());
		Label header9 = new Label(8, 0, "税控盘号", JXLTool.getHeader());
		Label header10 = new Label(9, 0, "开票机号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "合计金额", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "合计税额", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "价税合计", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "是否手工录入", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "开具类型", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "发票类型", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "状态", JXLTool.getHeader());

		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
		ws.addCell(header13);
		ws.addCell(header14);
		ws.addCell(header15);
		ws.addCell(header16);
		ws.addCell(header17);
		for (int i = 0; i < 17; i++) {
			ws.setColumnView(i, 18);
		}
		int count = 1;
		for (int i = 0; i < billInfoList.size(); i++) {
			BillInfo billInfo = (BillInfo) billInfoList.get(i);
			int column = count++;
			setWritableSheetNew(ws, billInfo, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheetNew(WritableSheet ws, BillInfo billInfo,
			int column) throws WriteException {
		Label cell1 = new Label(0, column, column + "", JXLTool
				.getContentFormat());
		Label cell2 = new Label(1, column, (String) billInfo.getApplyDate(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(2, column, (String) billInfo.getBillDate(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(3, column, (String) billInfo.getCustomerName(),
				JXLTool.getContentFormat());
		Label cell5 = new Label(4, column,
				(String) billInfo.getCustomerTaxno(), JXLTool
						.getContentFormat());
		Label cell6 = new Label(5, column, (String) billInfo.getBillCode(),
				JXLTool.getContentFormat());
		Label cell7 = new Label(6, column, (String) billInfo.getBillNo(),
				JXLTool.getContentFormat());
		Label cell8 = new Label(7, column, (String) billInfo.getDrawer(),
				JXLTool.getContentFormat());
		Label cell9 = new Label(8, column, (String) billInfo.getTaxDiskNo(),
				JXLTool.getContentFormat());
		Label cell10 = new Label(9, column, (String) billInfo.getMachineNo(),
				JXLTool.getContentFormat());
		Label cell11 = new Label(10, column, billInfo.getAmtSum() == null ? ""
				: billInfo.getAmtSum().toString(), JXLTool.getContentFormat());
		Label cell12 = new Label(11, column,
				billInfo.getTaxAmtSum() == null ? "" : billInfo.getTaxAmtSum()
						.toString(), JXLTool.getContentFormat());
		Label cell13 = new Label(12, column, billInfo.getSumAmt() == null ? ""
				: billInfo.getSumAmt().toString(), JXLTool.getContentFormat());
		// 是否手工录入 1-自动开票;2-人工审核;3-人工开票
		Label cell14 = new Label(13, column, billInfo.getIsHandiwork().equals(
				"1") ? "自动开票" : billInfo.getIsHandiwork().equals("2") ? "人工审核"
				: "人工开票", JXLTool.getContentFormat());
		// 开具类型1-单笔;2-合并;3-拆分
		Label cell15 = new Label(14, column, billInfo.getIssueType()
				.equals("1") ? "单笔"
				: billInfo.getIssueType().equals("2") ? "合并" : "拆分", JXLTool
				.getContentFormat());
		// 发票类型 0-增值税专用发票;1-增值税普通发票
		Label cell16 = new Label(15, column, billInfo.getFapiaoType().equals(
				"0") ? "增值税专用发票" : "增值税普通发票", JXLTool.getContentFormat());
		Label cell17 = new Label(16, column, getDataStatusC(billInfo
				.getDataStatus()), JXLTool.getContentFormat());
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
		ws.addCell(cell10);
		ws.addCell(cell11);
		ws.addCell(cell12);
		ws.addCell(cell13);
		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell17);
	}

	public String getDataStatusC(String dataStatus) {
		// 状态 1-编辑待提交
		// 2-提交待审核
		// 3-审核通过
		// 4-无需审核
		// 5-已开具
		// 7-开具失败
		// 8-已打印
		// 9-打印失败
		// 10-已快递
		// 11-已签收
		// 12-已抄报
		// 13-作废待审核
		// 14-作废已审核
		// 15-已作废
		// 16-红冲待审核
		// 17-红冲已审核
		// 18-已红冲
		// 19-已收回
		// 99-生效完成
		if (dataStatus.equals("1")) {
			dataStatus = "编辑待提交";
		} else if (dataStatus.equals("2")) {
			dataStatus = "提交待审核";
		} else if (dataStatus.equals("3")) {
			dataStatus = "审核通过";
		} else if (dataStatus.equals("4")) {
			dataStatus = "无需审核";
		} else if (dataStatus.equals("5")) {
			dataStatus = "已开具";
		} else if (dataStatus.equals("7")) {
			dataStatus = "开具失败";
		} else if (dataStatus.equals("8")) {
			dataStatus = "已打印";
		} else if (dataStatus.equals("9")) {
			dataStatus = "打印失败";
		} else if (dataStatus.equals("10")) {
			dataStatus = "已快递";
		} else if (dataStatus.equals("11")) {
			dataStatus = "已签收";
		} else if (dataStatus.equals("12")) {
			dataStatus = "已抄报";
		} else if (dataStatus.equals("13")) {
			dataStatus = "作废待审核";
		} else if (dataStatus.equals("14")) {
			dataStatus = "作废已审核";
		} else if (dataStatus.equals("15")) {
			dataStatus = "已作废";
		} else if (dataStatus.equals("16")) {
			dataStatus = "红冲待审核";
		} else if (dataStatus.equals("17")) {
			dataStatus = "红冲已审核";
		} else if (dataStatus.equals("18")) {
			dataStatus = "已红冲";
		} else if (dataStatus.equals("19")) {
			dataStatus = "已收回";
		} else if (dataStatus.equals("99")) {
			dataStatus = "生效完成";
		}
		return dataStatus;
	}

	/**
	 * 增值税专用发票 增值税普通发票 XML导出
	 */
	public String billsToXml() {
		StringBuffer sbMsg = new StringBuffer();
		String curDate = DateUtils.serverCurrentDate();
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String instCode = currentUser.getOrgId();
		try {
			// billType 区分是哪种类型的发票
			// 增值税专用发票 billType=1 增值税普通发票=4
			String billType = (String) request.getParameter("billType");
			String tableId = "";
			String subTableId = "";
			if (billType.equals("1") || billType.equals("4")
					|| billType.equals("5") || billType.equals("6")) {
				tableId = "VMS_BILL_INFO";
				subTableId = "VMS_BILL_ITEM_INFO";
			}
			// 用工厂类创建一个document实例
			Document doc = DocumentHelper.createDocument();
			if (billType.equals("1")) {
				// 根据发票类型初始化生成XML中的VMS_BILL_INFO表数据
				List dataList1 = initXmlDatas1("G", tableId); // czl
				if (dataList1 != null && dataList1.size() > 0) {
					// 创建根元素kp
					Element rootEle = doc.addElement("Kp");
					// 添加注释
					// rootEle.addComment("这是一个dom4j生成的xml文件");
					Element fpxxEle = rootEle.addElement("Fpxx");
					Element zslEle = fpxxEle.addElement("Zsl");
					zslEle.setText(dataList1.size() + "");
					Element fpsjEle = fpxxEle.addElement("Fpsj");
					for (int i = 0; i < dataList1.size(); i++) {
						BillInfo bi = (BillInfo) dataList1.get(i);

						Element fpEle = fpsjEle.addElement("Fp");
						// 单据号
						Element djhEle = fpEle.addElement("Djh");
						djhEle.setText(bi.getBillId());
						// 购方名称
						Element gfmcEle = fpEle.addElement("Gfmc");
						gfmcEle.setText(bi.getCustomerName());
						// 购方税号
						Element gfshEle = fpEle.addElement("Gfsh");
						gfshEle.setText(bi.getCustomerTaxno());
						// 购方银行账号
						Element gfyhzhEle = fpEle.addElement("Gfyhzh");
						gfyhzhEle.setText(bi.getCustomerBankandaccount());
						// 购方购方地址电话
						Element gfdzdhEle = fpEle.addElement("Gfdzdh");
						gfdzdhEle.setText(bi.getCustomerAddressandphone());
						// 备注
						Element bzEle = fpEle.addElement("Bz");
						bzEle.setText(bi.getRemark());
						// 复核人
						Element fhrEle = fpEle.addElement("Fhr");
						fhrEle.setText(bi.getReviewer());
						// 收款人
						Element skrEle = fpEle.addElement("Skr");
						skrEle.setText(bi.getPayee());
						// 根据发票类型初始化生成XML中的VMS_BILL_ITEM_INFO表数据
						List subDataList = initXmlSubDatas(bi.getBillId(),
								subTableId);
						if (subDataList != null && subDataList.size() > 0) {
							for (int j = 0; j < subDataList.size(); j++) {
								BillItemInfo bii = (BillItemInfo) subDataList
										.get(j);
								// bill_item_info中的子节点
								Element spxxEle = fpEle.addElement("Spxx");
								Element sphEle = spxxEle.addElement("Sph");
								// 序号
								Element xhEle = sphEle.addElement("Xh");
								xhEle.setText(j + 1 + "");
								// 商品名称
								Element spmcEle = sphEle.addElement("Spmc");
								spmcEle.setText(bii.getGoodsName());
								// 规格型号
								Element ggxhEle = sphEle.addElement("Ggxh");
								ggxhEle.setText(bii.getSpecandmodel());
								// 计量单位
								Element jldwEle = sphEle.addElement("Jldw");
								jldwEle.setText(bii.getGoodsUnit());
								// 单价
								Element djEle = sphEle.addElement("Dj");
								djEle.setText(bii.getGoodsPrice() == null ? ""
										: bii.getGoodsPrice().toString());
								// 数量
								Element slEle = sphEle.addElement("Sl");
								slEle.setText(bii.getGoodsNo() == null ? ""
										: bii.getGoodsNo().toString());
								// 金额
								Element jeEle = sphEle.addElement("Je");
								jeEle.setText(bii.getAmt() == null ? "" : bii
										.getAmt().toString());
								// 税率
								Element slvEle = sphEle.addElement("Slv");
								slvEle.setText(bii.getTaxRate() == null ? ""
										: bii.getTaxRate().toString());
								// slvEle.setText("0.17");
							}
						}
					}
					// 将document中的内容写入文件中

					int bufferSize = 65000;
					doc.setXMLEncoding("GBK");
					byte[] bytes = doc.asXML().getBytes("GBK");
					ByteArrayInputStream inputstream = new ByteArrayInputStream(
							bytes);
					byte abyte0[] = new byte[bufferSize];

					response.setContentType("application/octet-stream");
					response.setContentLength((int) bytes.length);
					// 增值税专用发票 billType=1 增值税普通发票=4 货物运输业增值税专用发票=5 机动车销售统一发票=6
					response.setHeader("Content-Disposition",
							"attachment;filename=" + curDate + instCode
									+ "spec.xml");
					int sum = 0;
					int k = 0;
					OutputStream output = response.getOutputStream();
					while ((k = inputstream.read(abyte0, 0, bufferSize)) > -1) {
						output.write(abyte0, 0, k);
						sum += k;
					}
					inputstream.close();
					response.getOutputStream().close();
				} else {
					JOptionPane.showMessageDialog(null, "导出发票信息未找到", "提示",
							JOptionPane.ERROR_MESSAGE);
					response.getOutputStream().close();
					return SUCCESS;
				}
			} else {
				// 根据发票类型初始化生成XML中的VMS_BILL_INFO表数据
				List dataList1 = initXmlDatas2("G", tableId); // czl
				if (dataList1 != null && dataList1.size() > 0) {
					// 创建根元素kp
					Element rootEle = doc.addElement("Kp");
					// 添加注释
					// rootEle.addComment("这是一个dom4j生成的xml文件");
					Element fpxxEle = rootEle.addElement("Fpxx");
					Element zslEle = fpxxEle.addElement("Zsl");
					zslEle.setText(dataList1.size() + "");
					Element fpsjEle = fpxxEle.addElement("Fpsj");
					for (int i = 0; i < dataList1.size(); i++) {
						BillInfo bi = (BillInfo) dataList1.get(i);

						Element fpEle = fpsjEle.addElement("Fp");
						// 单据号
						Element djhEle = fpEle.addElement("Djh");
						djhEle.setText(bi.getBillId());
						// 购方名称
						Element gfmcEle = fpEle.addElement("Gfmc");
						gfmcEle.setText(bi.getCustomerName());
						// 购方税号
						Element gfshEle = fpEle.addElement("Gfsh");
						gfshEle.setText(bi.getCustomerTaxno());
						// 购方银行账号
						Element gfyhzhEle = fpEle.addElement("Gfyhzh");
						gfyhzhEle.setText(bi.getCustomerBankandaccount());
						// 购方购方地址电话
						Element gfdzdhEle = fpEle.addElement("Gfdzdh");
						gfdzdhEle.setText(bi.getCustomerAddressandphone());
						// 备注
						Element bzEle = fpEle.addElement("Bz");
						bzEle.setText(bi.getRemark());
						// 复核人
						Element fhrEle = fpEle.addElement("Fhr");
						fhrEle.setText(bi.getReviewer());
						// 收款人
						Element skrEle = fpEle.addElement("Skr");
						skrEle.setText(bi.getPayee());
						// 根据发票类型初始化生成XML中的VMS_BILL_ITEM_INFO表数据
						List subDataList = initXmlSubDatas(bi.getBillId(),
								subTableId);
						if (subDataList != null && subDataList.size() > 0) {
							for (int j = 0; j < subDataList.size(); j++) {
								BillItemInfo bii = (BillItemInfo) subDataList
										.get(j);
								// bill_item_info中的子节点
								Element spxxEle = fpEle.addElement("Spxx");
								Element sphEle = spxxEle.addElement("Sph");
								// 序号
								Element xhEle = sphEle.addElement("Xh");
								xhEle.setText(j + 1 + "");
								// 商品名称
								Element spmcEle = sphEle.addElement("Spmc");
								spmcEle.setText(bii.getGoodsName());
								// 规格型号
								Element ggxhEle = sphEle.addElement("Ggxh");
								ggxhEle.setText(bii.getSpecandmodel());
								// 计量单位
								Element jldwEle = sphEle.addElement("Jldw");
								jldwEle.setText(bii.getGoodsUnit());
								// 单价
								Element djEle = sphEle.addElement("Dj");
								djEle.setText(bii.getGoodsPrice() == null ? ""
										: bii.getGoodsPrice().toString());
								// 数量
								Element slEle = sphEle.addElement("Sl");
								slEle.setText(bii.getGoodsNo() == null ? ""
										: bii.getGoodsNo().toString());
								// 金额
								Element jeEle = sphEle.addElement("Je");
								jeEle.setText(bii.getAmt() == null ? "" : bii
										.getAmt().toString());
								// 税率
								Element slvEle = sphEle.addElement("Slv");
								slvEle.setText(bii.getTaxRate() == null ? ""
										: bii.getTaxRate().toString());
								// slvEle.setText("0.17");
							}
						}
					}
					// 将document中的内容写入文件中

					int bufferSize = 65000;
					doc.setXMLEncoding("GBK");
					byte[] bytes = doc.asXML().getBytes("GBK");
					ByteArrayInputStream inputstream = new ByteArrayInputStream(
							bytes);
					byte abyte0[] = new byte[bufferSize];

					response.setContentType("application/octet-stream");
					response.setContentLength((int) bytes.length);
					// 增值税专用发票 billType=1 增值税普通发票=4 货物运输业增值税专用发票=5 机动车销售统一发票=6
					response.setHeader("Content-Disposition",
							"attachment;filename=" + curDate + instCode
									+ "comm.xml");
					int sum = 0;
					int k = 0;
					OutputStream output = response.getOutputStream();
					while ((k = inputstream.read(abyte0, 0, bufferSize)) > -1) {
						output.write(abyte0, 0, k);
						sum += k;
					}
					inputstream.close();
					response.getOutputStream().close();
				} else {
					JOptionPane.showMessageDialog(null, "导出发票信息未找到", "提示",
							JOptionPane.ERROR_MESSAGE);
					response.getOutputStream().close();
					return SUCCESS;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return SUCCESS;
	}

	/**
	 * 根据发票类型初始化生成XML主表节点数据
	 * 
	 * @param billType
	 * @return
	 */
	// czl
	private List initXmlDatas1(String customer_taxno, String tableId) {
		// 增值税专用发票 billType=1 增值税普通发票=4
		List dataList = billInfoService.findTaxpayerTypeDatas(customer_taxno,
				tableId);
		return dataList;
	}

	// czl
	private List initXmlDatas2(String customer_taxno, String tableId) {
		// 增值税专用发票 billType=1 增值税普通发票=4
		List dataList = billInfoService.findTaxpayerTypeDatas2(customer_taxno,
				tableId);
		return dataList;
	}

	private List initXmlSubDatas(String billId, String subTableId) {
		// 增值税专用发票 billType=1 增值税普通发票=4
		List dataList = billInfoService.findBillSubDatas(billId, subTableId);
		return dataList;
	}

	private void writeFile(String filePath, String fileName, Document document)
			throws Exception {
		// document到文件
		DocXmlUtil
				.fromDocumentToFile(document, fileName, CharacterEncoding.GBK);
	}

	public static String callServiceAxis(String clientNo, String taxMachineIP,
			String sysInvNo, String invoiceList, String invoiceSplit,
			String invoiceConsolidate, String invoiceData) throws Exception {
		try {
			// 参考 http://blog.csdn.net/lxqluo/article/details/6968599
			// String urlname =
			// "http://192.168.194.23:9080/Logon/services/Logon?wsdl";
			// urlname = "http://192.168.194.23:9080/Logon/services/Logon";
			String urlname = "http://120.0.0.1/ws/Service.asmx";
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTimeout(new Integer(150000));
			call.setOperation("CallService");
			// call.setTargetEndpointAddress(urlname);
			call.setTargetEndpointAddress(new java.net.URL(urlname));
			call.setOperationName(new QName("http://tempuri.org/",
					"CallService"));
			// 该方法需要的参数
			call.addParameter(new QName("http://tempuri.org/", "CLIENTNO"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "TaxMachineIP"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "SysInvNo"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "InvoiceList"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "InvoiceSplit"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/",
					"InvoiceConsolidate"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "InvoiceData"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			// 方法的返回值类型
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/CallService");
			Object[] fn01 = { clientNo, taxMachineIP, sysInvNo, invoiceList,
					invoiceSplit, invoiceConsolidate, invoiceData };
			String result = (String) call.invoke(fn01);
			return result;
		} catch (Exception e) {
			// java.io.InterruptedIOException: Read timed out
			e.printStackTrace();
		}
		return null;
	}

	public static Map receiveXml(String feedbackXml) {
		Map map = new HashMap();
		if (feedbackXml.indexOf("<SysInvNo>") > 0
				&& feedbackXml.indexOf("</SysInvNo>") > 0) {
			String str = feedbackXml
					.substring(feedbackXml.indexOf("<SysInvNo>")
							+ "<SysInvNo>".length(), feedbackXml
							.indexOf("</SysInvNo>"));
			map.put("SysInvNo", str);
		}
		if (feedbackXml.indexOf("<InvCode>") > 0
				&& feedbackXml.indexOf("</InvCode>") > 0) {
			String str = feedbackXml.substring(feedbackXml.indexOf("<InvCode>")
					+ "<InvCode>".length(), feedbackXml.indexOf("</InvCode>"));
			map.put("InvCode", str);
		}
		if (feedbackXml.indexOf("<InvNo>") > 0
				&& feedbackXml.indexOf("</InvNo>") > 0) {
			String str = feedbackXml.substring(feedbackXml.indexOf("<InvNo>")
					+ "<InvNo>".length(), feedbackXml.indexOf("</InvNo>"));
			map.put("InvNo", str);
		}
		if (feedbackXml.indexOf("<InvDate>") > 0
				&& feedbackXml.indexOf("</InvDate>") > 0) {
			String str = feedbackXml.substring(feedbackXml.indexOf("<InvDate>")
					+ "<InvDate>".length(), feedbackXml.indexOf("</InvDate>"));
			map.put("InvDate", str);
		}
		if (feedbackXml.indexOf("<CancelDate>") > 0
				&& feedbackXml.indexOf("</CancelDate>") > 0) {
			String str = feedbackXml.substring(feedbackXml
					.indexOf("<CancelDate>")
					+ "<CancelDate>".length(), feedbackXml
					.indexOf("</CancelDate>"));
			map.put("CancelDate", str);
		}
		if (feedbackXml.indexOf("<OperateFlag>") > 0
				&& feedbackXml.indexOf("</OperateFlag>") > 0) {
			String str = feedbackXml.substring(feedbackXml
					.indexOf("<OperateFlag>")
					+ "<OperateFlag>".length(), feedbackXml
					.indexOf("</OperateFlag>"));
			map.put("OperateFlag", str);
		}
		if (feedbackXml.indexOf("<PrintFlag>") > 0
				&& feedbackXml.indexOf("</PrintFlag>") > 0) {
			String str = feedbackXml.substring(feedbackXml
					.indexOf("<PrintFlag>")
					+ "<PrintFlag>".length(), feedbackXml
					.indexOf("</PrintFlag>"));
			map.put("PrintFlag", str);
		}
		if (feedbackXml.indexOf("<returnmsg>") > 0
				&& feedbackXml.indexOf("</returnmsg>") > 0) {
			String str = feedbackXml.substring(feedbackXml
					.indexOf("<returnmsg>")
					+ "<returnmsg>".length(), feedbackXml
					.indexOf("</returnmsg>"));
			map.put("returnmsg", str);
		}
		return map;
	}

	public TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public String redReceiptSpecialApply() {
		String billId = request.getParameter("billId");
		applyInfo = billInfoService.findByBillId(billId);
		return SUCCESS;
	}

	// 专票保存
	public String redReceiptSpecialSave() {
		Map map = new HashMap();
		map.put(" searchCondition", "t.BILL_NO ='" + specialTicket.getBillNo()
				+ "' and t.BILL_CODE = '" + specialTicket.getBillCode() + "'");
		List list = billInfoService.findSpecialTicketById(map);
		if (null == list || list.size() < 1) {
			specialTicket.setBuySellInd("1");
			specialTicket.setFapiaoType("0");
			specialTicket.setLevel2Option("0");
			billInfoService.saveSpecialTicket(specialTicket);
		} else {
			SpecialTicket temp = (SpecialTicket) list.get(0);
			temp.setLevel1Option(specialTicket.getLevel1Option());
			temp.setLevel2Option(specialTicket.getLevel2Option());
			billInfoService.updateSpecialTicket(temp);
		}
		String commit = request.getParameter("commit");
		if (null != commit && commit.equals("commit")) {
			BillInfo bill = billInfoService.findBillInfo1(specialTicket
					.getBillId());
			if (bill == null) {
				request.setAttribute("message", "数据错误");
				return ERROR;
			}
			bill.setOperateStatus(bill.getDataStatus());
			bill.setDataStatus("16");
			bill.setCancelInitiator(getCurrentUser().getId());
			billInfoService.saveBillInfo1(bill, true);
			this.message = "红冲成功";
			this.request.setAttribute("message", this.message);
		}
		applyInfo = null;
		return SUCCESS;
	}

	public String redReceiptReleaseTrans() {

		String result = request.getParameter("result");
		String billId = request.getParameter("billId");
		String transId;
		BillInfo bill = null;
		bill = billInfoService.findBillInfo1(billId);
		if (bill == null) {
			request.setAttribute("message", "数据错误");
			return ERROR;
		}
//		bill.setDataStatus(result);
//		billInfoService.saveBillInfo1(bill, true);
//		Map dataMap = new HashMap();
//		dataMap.put("oriBillCode", bill.getOriBillCode());
//		dataMap.put("oriBillNo", bill.getOriBillNo());
//		List list = billInfoService.findReleaseTrans(dataMap);
//		for (int i = 0; i < list.size(); i++) {
//			RedReceiptTransInfo rrti = (RedReceiptTransInfo) list.get(i);
//			Map map = transInfoService.findIncomeById(rrti.getTransId());
//			transInfoService.updateTransDatastatus(rrti.getTransId(), "1");
//		}
//		bill = billInfoService.findBillInfo1(billId);
//		if (bill == null) {
//			request.setAttribute("message", "数据错误");
//			return ERROR;
//		}
		String notic = request.getParameter("notic");
		bill.setNoticeNo(notic);
		bill.setDataStatus(result);
		billInfoService.saveBillInfo1(bill, true);
		billInfo = bill;
		return SUCCESS;
	}

	public String ticketEdit() {

		return SUCCESS;
	}

	public String listBillItem1() {
		try {
			String billId = request.getParameter("billId");
			String isHandiwork = request.getParameter("isHandiwork");
			if (StringUtil.isNotEmpty(billId)) {
				List billItemList = billInfoService
						.findBillItemInfoList1(new BillItemInfo(billId, null));
				this.request.setAttribute("billItemList", billItemList);
				this.request.setAttribute("billId", billId);
			}
			this.request.setAttribute("isHandiwork", isHandiwork);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-listBillItem", e);
			return ERROR;
		}
		return SUCCESS;
	}

	public void exportApplyInfo() {

		StringBuffer fileName = new StringBuffer("申请表打印");
		fileName.append(".doc");
		Map dataMap = new HashMap();
		try {
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type",
					"application/vnd.ms-word;charset=UTF-8");
			response.setHeader("Content-Disposition", name);
			response.setCharacterEncoding("UTF-8");
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			configuration.setClassForTemplateLoading(this.getClass(),
					"/com/cjit/vms/trans/util");
			Template t = null;
			try {
				// test.ftl为要装载的模板
				t = configuration.getTemplate("applyTemplate.ftl");
				t.setEncoding("utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Writer out = response.getWriter();
			BillItemInfo billItemInfo = new BillItemInfo();
			billItemInfo.setBillId(applyInfo.getBillId());
			List list = billInfoService.findBillItemInfoList1(billItemInfo);
			dataMap.put("list", list);
			dataMap.put("applyInfo", applyInfo);
			t.process(dataMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拼接打印发票需要传进税控盘中的参数
	 */

	/**
	 * 拼接打印发票需要传进税控盘中的参数
	 * 
	 * @throws Exception
	 */
	public void showLock() throws Exception {

		String result = "";
		String lock = getInstLock();
		if (lock != null) {
			result = lock;
			printWriterResult(result);
			return;
		}
		String taxDiskNo = request.getParameter("taxDiskNo");
		String fapiaoType = request.getParameter("fapiaoType");

		// 获取注册码
		String registeredInfo = billIssueService.findRegisteredInfo(taxDiskNo);
		if (registeredInfo == null) {
			result = "registeredInfoError";
			printWriterResult(result);
			return;
		}

		// 查询税控盘口令和证书口令
		TaxDiskInfo taxDiskInfo = billIssueService
				.findTaxDiskInfoByTaxDiskNo(taxDiskNo);
		if (taxDiskInfo == null) {
			result = "taxPwdError";
			printWriterResult(result);
			return;
		}

		result = registeredInfo + "|" + taxDiskInfo.getTaxDiskPsw() + "|"
				+ fapiaoType;
		printWriterResult(result);

	}

	/**
	 * @return lock 为锁定状态
	 */
	private String getInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		boolean lock = ThreadLock.getLockState(instCode);
		if (lock) {
			return "lock";
		}
		return null;
	}

	private void releaseInstLock() {
		String instCode = this.getCurrentUser().getOrgId();
		ThreadLock.releaseLock(instCode);
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
		releaseInstLock();
	}

	/**
	 * 后台根据前台页面选中的billId组装传进OCX的字符串 注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码|
	 * 发票ID^发票代码^发票号码为循环域
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showOCXstring() throws Exception {
		try {
			String faPiaoType = (String) request.getParameter("faPiaoType");
			int printLimitValue = Integer.parseInt(request
					.getParameter("printLimitValue"));
			String billIds = (String) request.getParameter("billIds");
			String[] selectedBillIds = billIds.split(",");
			// StringBuffer sbOuter = new StringBuffer();
			StringBuffer sbInner = new StringBuffer();
			StringBuffer lianxu = new StringBuffer("发票号码");
			StringBuffer lingyong = new StringBuffer("发票号码");
			StringBuffer billPrintId = new StringBuffer();
			int j = 0;
			boolean falg=false;
			boolean printFalg=false;
			//增加领用逻辑
			if (selectedBillIds != null && selectedBillIds.length > 0) {
				List bills = billInfoService.findBillInfoByIDFaPiaoType(
						selectedBillIds, faPiaoType);

				if (bills.size() == 1) {
					BillInfo billIn = (BillInfo) bills.get(0);
					List list=paperInvoiceTrackService.findReREByBillCodeAndBillNo(billIn.getBillCode(), billIn.getBillNo());
					if(list.size()==0){
						lingyong.append(billIn.getBillNo());
						falg=true;
						
					}else{
					// ：注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码| 红色部分为循环域
					sbInner.append('0').append("^");
					sbInner.append(billIn.getBillId()).append("^").append(
							billIn.getBillCode()).append("^").append(
							billIn.getBillNo());
					lianxu.append(billIn.getBillNo()).append(",");
					billPrintId.append(billIn.getBillId());
					j++;
					printFalg=true;
					}

				} else if (bills.size() > 1) {
					
					BillInfo billIn = (BillInfo) bills.get(0);
					List list=paperInvoiceTrackService.findReREByBillCodeAndBillNo(billIn.getBillCode(), billIn.getBillNo());
					if(list.size()==0){
						lingyong.append(billIn.getBillNo());
						falg=true;
						}else{
						sbInner.append('0').append("^");
						sbInner.append(billIn.getBillId()).append("^").append(
								billIn.getBillCode()).append("^").append(
								billIn.getBillNo()).append("|");
						billPrintId.append(billIn.getBillId()).append(",");
						lianxu.append(billIn.getBillNo()).append(",");
						j++;
					
					for (int i = 0; i < bills.size() - 1; i++) {
						BillInfo bi = (BillInfo) bills.get(i);
						BillInfo bill = (BillInfo) bills.get(i + 1);
						BigDecimal temId = new BigDecimal(Integer.valueOf(
								bill.getBillNo()).intValue());
						BigDecimal billNo = new BigDecimal(Integer.valueOf(
								bi.getBillNo()).intValue());
						if(list.size()==0){
							lingyong.append(billIn.getBillNo());
							falg=true;
							break;
							}else{
							if (i < bills.size() - 1
									&& temId.subtract(billNo).intValue() == 1&& bi.getBillCode().equals(bill.getBillCode())) { //增加发票代码的条件
								// 注册码|发票类型(0:专,1:普)|发票数量|发票ID^发票代码^发票号码|
								// 发票ID^发票代码^发票号码为循环域
								sbInner.append('0').append("^");
								sbInner.append(bill.getBillId()).append("^")
										.append(bill.getBillCode()).append("^")
										.append(bill.getBillNo()).append("|");
								lianxu.append(temId).append(",");
								billPrintId.append(bill.getBillId()).append(",");
								j++;
								printFalg=true;
							} else {
								break;
							}
						}
					}
					}
				}
				if (printLimitValue < j) {
					String result = "printValueError";
					printWriterResult(result);
					return null;
				}
				lianxu.append("发票号码是连续的,请确认是否打印?");

			}
			if(falg){
				lingyong.append("未领用不能打印");
				lianxu.append(lingyong);
			}
			String result="";
			if(printFalg){
				result ="1"+"+"+sbInner.toString() + "+" + lianxu.toString() + "+"
					+ String.valueOf(j) + "+" + billPrintId;
			}else{
				result="0"+"+"+lingyong;
			}
			// 将结果写出
			this.response.setContentType("application/json;charset=UTF-8");
			this.response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("ajax-拼接传给税控盘的字符串出错");
		} finally {

		}
		return null;
	}

	public String timeAjax() throws IOException {
		this.response.setContentType("application/json;charset=UTF-8");
		this.response.getWriter().print("true");
		return null;
	}

	/**
	 * 根据税控端传过来的值修改票据对应的交易信息状态 发票号码,税控盘结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updatePrintResult() throws Exception {
		// resultOCX="1,1,2,0,3,1,4,0,5,1,6,0,7,1,8,0,9,1,10,0";
		// 0 |B2015112700000001043 ^ 1110098079^73746046 ^ 用户取消了打印操作|
		//打印结果（1：成功，0：失败）^发票ID^发票代码^发票号码^返回信息（”成功”或者具体错误）|
		
		String ocxString=request.getParameter("resultOCX"); 
		int num=Integer.parseInt(request.getParameter("num")); 
		String[] dataString=ocxString.split("\\|");
		for(int i=0;i<num;i++){
			String[] dataInfo=dataString[i].split("\\^");
			billInfoService.updateBillByBillId(dataInfo[1], "8");
			if(num<dataString.length&&i==num){
				billInfoService.updateBillByBillId(dataInfo[1], "9");
			}
			if(i==num){
				break;
			}
			updateTransInfo(dataInfo[1]);
		}
		
		return null;
	}


	/*
	 * String billId=""; if(results.length>0){ for(int
	 * i=1;i<results.length;i+=2){ if(results[i].equals("1")){
	 * billId=results[i-1]; billInfoService.updateBillByBillId(billId, "8");
	 * }else{ billInfoService.updateBillByBillId(billId, "9"); } } }
	 */

	/*
	 * }
	 * 
	 * String result="success"; // 将结果写出 }
	 */

	/**
	 * <p>
	 * 方法名称: setResultMessages|描述:设置session中存储的处理信息结果
	 * </p>
	 * 
	 * @param resultMessages
	 *            处理信息结果
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public void setResultMessages(String resultMessages) {

		log.info(request.getHeader("user-agent"));
		log.info(request.getHeader(request.getLocale().toString()));
		resultMessages = chr2Unicode(resultMessages);
		log.info(resultMessages);
		try {
			this.RESULT_MESSAGE = java.net.URLEncoder.encode(resultMessages,
					"utf-8");
			request.setAttribute("RESULT_MESSAGE", RESULT_MESSAGE);
			request.setAttribute("resultMessages", resultMessages);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	/**
	 * 中文转unicode字符(英文环境)
	 * 
	 * @param str
	 * @return
	 */
	public String chr2Unicode(String str) {
		String[] a = { "", "000", "00", "0", "" };
		String result = "";
		if (StringUtils.isNotEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				int chr = (char) str.charAt(i);
				String s = Integer.toHexString(chr);
				result += "\\u" + a[s.length()] + s;
			}
		}
		return result;
	}

	private String customerName;
	private String customerTaxno;
	private String isHandiwork;
	private String issueType;
	private String applyDate;

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getPrintLimitValue() {
		return printLimitValue;
	}

	public void setPrintLimitValue(int printLimitValue) {
		this.printLimitValue = printLimitValue;
	}

	public ParamConfigVmssService getParamConfigVmssService() {
		return paramConfigVmssService;
	}

	public void setParamConfigVmssService(
			ParamConfigVmssService paramConfigVmssService) {
		this.paramConfigVmssService = paramConfigVmssService;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List getAmtList() {
		return amtList;
	}

	public void setAmtList(List amtList) {
		this.amtList = amtList;
	}

	public String getFaPiaoType() {
		return faPiaoType;
	}

	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}

	public Map getMapVatType() {
		return mapVatType;
	}

	public void setMapVatType(Map mapVatType) {
		this.mapVatType = mapVatType;
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String rESULT_MESSAGE) {
		RESULT_MESSAGE = rESULT_MESSAGE;
	}

	public BillIssueService getBillIssueService() {
		return billIssueService;
	}

	public void setBillIssueService(BillIssueService billIssueService) {
		this.billIssueService = billIssueService;
	}

	public String getUpdFlg() {
		return updFlg;
	}

	public void setUpdFlg(String updFlg) {
		this.updFlg = updFlg;
	}

	public Map getMapGoodsList() {
		return mapGoodsList;
	}

	public void setMapGoodsList(Map mapGoodsList) {
		this.mapGoodsList = mapGoodsList;
	}

	public Map getMapTaxList() {
		return mapTaxList;
	}

	public void setMapTaxList(Map mapTaxList) {
		this.mapTaxList = mapTaxList;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public BillInfo getBill() {
		return bill;
	}

	public void setBill(BillInfo bill) {
		this.bill = bill;
	}

	public void updateTransInfo(String billId) {
		BillInfo bill = billIssueService.findBillInfoById(billId);
		if ("3".equals(bill.getIssueType())) {
			// 票据为拆分而来，当交易对应的所有票据状态为已开具更改交易状态
			BillInfo issueBill = new BillInfo();
			issueBill.setBillId(billId);
			List transList = billIssueService.findTransByBillId(billId);
			if (transList != null && transList.size() == 1) {
				TransInfo trans = (TransInfo) transList.get(0);
				if (trans.getBalance().compareTo(new BigDecimal(0)) == 0) {
					issueBill = new BillInfo();
					issueBill.setTransId(trans.getTransId());
					List billFromOneTransList = billIssueService
							.findBillInfoList(issueBill);
					boolean flag = true;
					for (int j = 0; j < billFromOneTransList.size(); j++) {
						BillInfo bill1 = (BillInfo) billFromOneTransList.get(j);
						if (!"8".equals(bill1.getDataStatus())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						billIssueService.updateTransInfoStatus("99", billId);
					}
				}
			}

		} else {
			billIssueService.updateTransInfoStatus("99", billId);
			// printWriterResult("开具成功！");
		}
	}

	public String toUpdateRemark() {
		try {
			String billId = request.getParameter("billId");
			billInfo = billInfoService.findBillById(billId);
			this.fromFlag = "";
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-toUpdateRemark", e);
		}
		return ERROR;
	}

	public void updateRemark() throws Exception {
		String result = "";
		try {
			String billId = request.getParameter("billId");
			String remark = request.getParameter("remark");
			billInfoService.updateBillRemarkById(remark, billId);
			result = SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillInfoAction-updateRemark", e);
			result = e.getMessage();
		}
		printWriterResult(result);
	}

	private void setWritableSheet(WritableSheet ws, Map o, int column)
			throws WriteException {
		Label cell1 = new Label(0, column, (String) o.get("customerName"),
				JXLTool.getContentFormat());
		Label cell2 = new Label(1, column, (String) o.get("customerTaxno"),
				JXLTool.getContentFormat());
		Label cell3 = new Label(2, column, (String) o.get("amtSum").toString(),
				JXLTool.getContentFormat());
		Label cell4 = new Label(3, column, (String) o.get("taxAmtSum")
				.toString(), JXLTool.getContentFormat());
		Label cell5 = new Label(4, column, (String) o.get("sumAmt").toString(),
				JXLTool.getContentFormat());
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
	}

	private PaperInvoiceService paperInvoiceService;
	private PaperInvoiceTrackService paperInvoiceTrackService;
	
	public PaperInvoiceService getPaperInvoiceService() {
		return paperInvoiceService;
	}

	public void setPaperInvoiceService(PaperInvoiceService paperInvoiceService) {
		this.paperInvoiceService = paperInvoiceService;
	}

	public PaperInvoiceTrackService getPaperInvoiceTrackService() {
		return paperInvoiceTrackService;
	}

	public void setPaperInvoiceTrackService(
			PaperInvoiceTrackService paperInvoiceTrackService) {
		this.paperInvoiceTrackService = paperInvoiceTrackService;
	}

	public String getGoodsListJson() {
		return goodsListJson;
	}

	public void setGoodsListJson(String goodsListJson) {
		this.goodsListJson = goodsListJson;
	}
	
	/**
	 * 航信发票打印
	 * @return
	 * @throws Exception 
	 */
	public String printBillAisino() throws Exception {
		try {
			String billIds = (String) request.getParameter("billIds");
			if (StrFunc.isNull(billIds))
				throw new Exception("选择的发票ID不能为空！");
			String[] selectedBillIds = billIds.split(",");
			List bills = billInfoService.findBillInfoByIDFaPiaoType(
					selectedBillIds, faPiaoType);
			BillPrintAisinoService util = new BillPrintAisinoService();
//			String sendStr = util.getPrintXmlInfo(bills, ip, port);
			String sendStr = util.getPrintXmlInfo(bills);
			System.out.println(sendStr);

			this.response.setContentType("application/json;charset=UTF-8");
			String result = HxServiceFactory.createHxInvoiceService()
					.printInvoice(sendStr.toString());
			//TODO
			result = "<service><err count=\"1\"><refp><RETCODE>-1</RETCODE><RETMSG>控制台异常信息!</RETMSG><FPZL>1</FPZL><FPHM>000811101</FPHM><FPDM>31001124111</FPDM></refp></err></service>";
			String checkresult = util.checkXMLResult(result, billInfoService);
			// 将结果写出
			this.response.getWriter().print(checkresult);
		} finally {
			releaseInstLock();
		}
		return null;
	}
	
}
