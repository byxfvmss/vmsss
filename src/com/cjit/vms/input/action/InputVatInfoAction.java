package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.ExcelUtil;
import com.cjit.vms.input.model.InputVatInfo;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillItemInfo;

public class InputVatInfoAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private List inputVatInfoList;
	private List billTypeList;// 发票种类信息
	private List authenticationFlagList;// 认证结果信息
	private InputVatInfo inputVatInfo = new InputVatInfo();
	private String[] selectInVatIds;
	// 交易起止日期
	private String valueBeginDate;
	private String valueEndDate;
	// 记账起止日期
	private String bookingBeginDate;
	private String bookingEndDate;
	// 供应商信息
	private String suppName;
	private String suppAccount;
	// 发票种类
	private String billType;
	// 记账科目
	private String bookingCourse;
	// 机构
	private String instCode;

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	/**
	 * 菜单点击进入进项税管理界面
	 * 
	 * @return String
	 */
	public String listInputVat() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			// User currentUser = this.getCurrentUser();
			if ("menu".equalsIgnoreCase(fromFlag)) {
				inputVatInfo = new InputVatInfo();
				this.setValueBeginDate(null);
				this.setValueEndDate(null);
				this.setBookingBeginDate(null);
				this.setBookingEndDate(null);
				this.request.getSession().removeAttribute("valueBeginDate");
				this.request.getSession().removeAttribute("valueEndDate");
				this.request.getSession().removeAttribute("bookingBeginDate");
				this.request.getSession().removeAttribute("bookingEndDate");
				fromFlag = null;
			}
			billTypeList = this.userInterfaceConfigService.getDictionarys(
					"BILL_TYPE", "");
			InputVatInfo inputVat = new InputVatInfo();
			inputVat.setValueBeginDate(valueBeginDate);
			inputVat.setValueEndDate(valueEndDate);
			inputVat.setBookingBeginDate(bookingBeginDate);
			inputVat.setBookingEndDate(bookingEndDate);
			inputVat.setSuppName(suppName);
			inputVat.setBillType(billType);
			inputVat.setInstCode(instCode);
			inputVat.setBookingCourse(bookingCourse);
			inputVatInfoList = inputVatInfoService.findInputVatInfoList(
					inputVat, paginationList);
			this.request.setAttribute("billTypeList", billTypeList);
			this.request.setAttribute("inputVatInfoList", inputVatInfoList);
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0110", "进项税管理", "查询", "查询进项税信息列表", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0110", "进项税管理", "查询", "查询进项税信息列表", "0");
			log.error("InputVatInfoAction-listInputVat", e);
		}
		return ERROR;
	}

	/**
	 * 进入进项税添加界面
	 * 
	 * @return String
	 */
	public String createInputVat() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			inputVatInfo = new InputVatInfo();
			billTypeList = this.userInterfaceConfigService.getDictionarys(
					"BILL_TYPE", "");
			this.request.setAttribute("billTypeList", billTypeList);
			authenticationFlagList = this.userInterfaceConfigService
					.getDictionarys("AUTHENTICATION_FLAG", "");
			this.request.setAttribute("authenticationFlagList",
					authenticationFlagList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputVatInfoAction-createInputVat", e);
		}
		return ERROR;
	}

	/**
	 * 进入进项税编辑界面
	 * 
	 * @return String
	 */
	public String editInputVat() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			String inVatId = request.getParameter("inVatId");
			inputVatInfo = inputVatInfoService.findInputVatInfo(inVatId);
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(inVatId);
			List billItemList = billInfoService.findBillItemInfoList(billItem);
			this.request.setAttribute("billItemList", billItemList);
			billTypeList = this.userInterfaceConfigService.getDictionarys(
					"BILL_TYPE", "");
			this.request.setAttribute("billTypeList", billTypeList);
			authenticationFlagList = this.userInterfaceConfigService
					.getDictionarys("AUTHENTICATION_FLAG", "");
			this.request.setAttribute("authenticationFlagList",
					authenticationFlagList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputVatInfoAction-editInputVat", e);
		}
		return ERROR;
	}

	/**
	 * 保存进项税信息
	 * 
	 * @return String
	 */
	public String saveInputVat() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			if (StringUtil.IsEmptyStr(inputVatInfo.getInVatId())) {
				inputVatInfo.setInVatId(createBusinessId("IV"));
				inputVatInfoService.saveInputVatInfo(inputVatInfo, false);
			} else {
				inputVatInfoService.saveInputVatInfo(inputVatInfo, true);
			}
			BillItemInfo billItem = new BillItemInfo();
			billItem.setBillId(inputVatInfo.getInVatId());
			List billItemList = billInfoService.findBillItemInfoList(billItem);
			this.request.setAttribute("billItemList", billItemList);
			billTypeList = this.userInterfaceConfigService.getDictionarys(
					"BILL_TYPE", "");
			this.request.setAttribute("billTypeList", billTypeList);
			authenticationFlagList = this.userInterfaceConfigService
					.getDictionarys("AUTHENTICATION_FLAG", "");
			this.request.setAttribute("authenticationFlagList",
					authenticationFlagList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("InputVatInfoAction-saveInputVatInfo", e);
		}
		return ERROR;
	}

	/**
	 * 删除进项税单据
	 * 
	 * @return String
	 */
	public String deleteInputVat() {
		String inVatId = "";
		try {
			if (this.selectInVatIds != null && this.selectInVatIds.length > 0) {
				// 循环删除选中的进项税
				for (int i = 0; i < this.selectInVatIds.length; i++) {
					inVatId = this.selectInVatIds[i];
					inputVatInfoService.deleteInputVatInfo(inVatId, null, null);
					logManagerService.writeLog(request, this.getCurrentUser(),
							"0001.0010", "进项税管理", "删除单据", "对进项税单据ID为" + inVatId
									+ "的票据执行删除操作", "1");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "进项税管理", "删除单据", "对进项税单据ID为" + inVatId
							+ "的票据执行删除操作", "0");
			log.error("InputVatInfoAction-deleteInputVat", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 导入进项税数据文件
	 * 
	 * @return
	 */
	public String importInputVat() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportFile(files[0]);
				request.setAttribute("message", "上传文件完成!");
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				request.setAttribute("message", "上传文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			request.setAttribute("message", "上传文件失败!");
			return ERROR;
		}
	}

	private List doImportFile(File file) throws Exception {
		List errorList = new ArrayList();
		try {
			Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
			if (hs != null) {
				String[][] cellValues = (String[][]) hs.get("0");
				for (int i = 0; i < cellValues.length; i++) {
					String[] colValues = cellValues[i];
					System.out.println(colValues[0] + "," + colValues[1] + ","
							+ colValues[2]);
				}
			}
		} catch (Exception e) {
			// 出现异常
			e.printStackTrace();
		}
		return errorList;
	}

	public List getInputVatInfoList() {
		return inputVatInfoList;
	}

	public void setInputVatInfoList(List inputVatInfoList) {
		this.inputVatInfoList = inputVatInfoList;
	}

	public List getBillTypeList() {
		return billTypeList;
	}

	public void setBillTypeList(List billTypeList) {
		this.billTypeList = billTypeList;
	}

	public List getAuthenticationFlagList() {
		return authenticationFlagList;
	}

	public void setAuthenticationFlagList(List authenticationFlagList) {
		this.authenticationFlagList = authenticationFlagList;
	}

	public InputVatInfo getInputVatInfo() {
		return inputVatInfo;
	}

	public void setInputVatInfo(InputVatInfo inputVatInfo) {
		this.inputVatInfo = inputVatInfo;
	}

	public String[] getSelectInVatIds() {
		return selectInVatIds;
	}

	public void setSelectInVatIds(String[] selectInVatIds) {
		this.selectInVatIds = selectInVatIds;
	}

	public String getValueBeginDate() {
		if (valueBeginDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("valueBeginDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				valueBeginDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("valueBeginDate", valueBeginDate);
		}
		return valueBeginDate;
	}

	public void setValueBeginDate(String valueBeginDate) {
		this.valueBeginDate = valueBeginDate;
	}

	public String getValueEndDate() {
		if (valueEndDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("valueEndDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				valueEndDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("valueEndDate", valueEndDate);
		}
		return valueEndDate;
	}

	public void setValueEndDate(String valueEndDate) {
		this.valueEndDate = valueEndDate;
	}

	public String getBookingBeginDate() {
		if (bookingBeginDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("bookingBeginDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				bookingBeginDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("bookingBeginDate",
					bookingBeginDate);
		}
		return bookingBeginDate;
	}

	public void setBookingBeginDate(String bookingBeginDate) {
		this.bookingBeginDate = bookingBeginDate;
	}

	public String getBookingEndDate() {
		if (bookingEndDate == null) {
			// 从session中获取查询起止日期
			String sessionEndDate = (String) this.request.getSession()
					.getAttribute("bookingEndDate");
			if (sessionEndDate != null && !"".equals(sessionEndDate)) {
				bookingEndDate = sessionEndDate;
			}
		} else {
			request.getSession().setAttribute("bookingEndDate", bookingEndDate);
		}
		return bookingEndDate;
	}

	public void setBookingEndDate(String bookingEndDate) {
		this.bookingEndDate = bookingEndDate;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppAccount() {
		return suppAccount;
	}

	public void setSuppAccount(String suppAccount) {
		this.suppAccount = suppAccount;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBookingCourse() {
		return bookingCourse;
	}

	public void setBookingCourse(String bookingCourse) {
		this.bookingCourse = bookingCourse;
	}

	public String scan() {
		if (1 == 1) {
			return SUCCESS;
		}
		try {
			// Runtime.getRuntime().exec(
			// new String[] { "C:\\WINDOWS\\system32\\notepad.exe",
			// "note.txt" });
			// Runtime.getRuntime().exec(
			// new String[] { "C:\\WINDOWS\\system32\\notepad.exe" });
			Runtime.getRuntime().exec(
					new String[] { "C:\\vat\\bin\\jizrzP.exe" });
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		return SUCCESS;
	}

}
