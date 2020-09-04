package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cjit.common.constant.Constants;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.service.DiskRegInfoService;
import com.cjit.vms.trans.util.JXLTool;

/**
 * 税控盘基本信息管理Action类
 * 
 */
public class DiskRegInfoAction extends DataDealAction {

	public String diskRegInfoList() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		try {
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
			}

			InstInfo in = new InstInfo();
			in.setInstId(this.getInstId());
			in.setUserId(this.getCurrentUser().getId());
			in.setTanNo(this.getTaxpayerNo());
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			in.setLstAuthInstIds(lstAuthInstId);
			List insts = diskRegInfoService.getInstInfoList(in, paginationList);
			this.setAuthInstList(insts);
			
			List instTaxno = diskRegInfoService.getInstInfoTaxNoList(in,paginationList);
			this.setTaxperLists(instTaxno);
			
			DiskRegInfo info = new DiskRegInfo();
			info.setInstId(request.getParameter("instId"));
			info.setTaxpayerNo(request.getParameter("taxpayerNo"));
			diskRegInfoService.getTaxDiskInfoList(info, paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-diskRegInfoList", e);
		}
		return ERROR;
	}

	public void findInfoTaxNo()
            throws ServletException, IOException {
		
		try {
			
			InstInfo in = new InstInfo();
			in.setInstId(request.getParameter("authInst"));
			in.setUserId(this.getCurrentUser().getId());
			List instTaxno = diskRegInfoService.getInstInfoTaxNoList(in,paginationList);
			String result = "";
			for(int i=0;i<instTaxno.size();i++){
				InstInfo inst = (InstInfo) instTaxno.get(i);
				if("".equals(result)){
					result += inst.getTanNo();
				}else{
					result += ","+inst.getTanNo();
				}
			}
			response.setHeader("Content-Type","text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: addDiskRegInfo|描述:税控盘注册信息添加
	 * </p>
	 */
	public String addDiskRegInfo() {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-addDiskRegInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * 方法名称: getTaxperListByAuthInst|描述:机构联动其纳税人
	 * </p>
	 */
	public void getTaxperListByAuthInst() throws IOException {
		// 获取当前用户
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// 获取参数【所选择机构ID】
		String authInst = request.getParameter("authInst");
		String result = "";
		// 获取当前用户所属所有机构列表
		List orgsList = currentUser.getOrgs();
		if (orgsList != null && orgsList.size() > 0) {
			for (int i = 0; i <= orgsList.size() - 1; i++) {
				// 依次获取各机构
				Organization org = (Organization) orgsList.get(i);
				// 当前机构为页面选中机构，并且，其属性[纳税人识别号]存在
				if (org.getTaxperNumber() != null
						&& authInst.equals(org.getId())) {
					// 将属性[纳税人识别号]返回页面
					result = org.getTaxperNumber();
					response.setHeader("Content-Type",
							"text/xml; charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print(result);
					out.close();
				}
			}
		}
	}
	/**
	 * <p>
	 * 方法名称: getTaxperNameByTaxperNo|描述:根据纳税人识别号获取纳税人名称
	 * </p>
	 */
	public void getTaxperNameByTaxperNo() throws IOException {
		String taxperName = "";
		// 获取参数【所选择机构ID】
		String taxperNo = request.getParameter("taxperNo");
		Organization info = new Organization();
		info.setTaxperNumber("='" + taxperNo + "'");
		
		List list = vmsCommonService.getAjaxTaxperNameList(info);
		if (null != list && list.size()>0){
			taxperName = ((Organization)list.get(0)).getTaxperName();
		}
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(taxperName);
		out.close();
	}
	/**
	 * <p>
	 * 方法名称: editDiskRegInfo|描述:税控盘注册信息编辑
	 * </p>
	 */
	public String editDiskRegInfo() {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			String taxDiskNo = request.getParameter("taxDiskNo");
			String taxpayerNo = request.getParameter("taxpayerNo");
			List diskRegInfo = (List)diskRegInfoService.getDiskRegInfoDetail(taxDiskNo,
					taxpayerNo);
			this.diskRegInfo = (DiskRegInfo)diskRegInfo.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-editDiskRegInfo", e);
			return ERROR;
		}
		this.setOperType("edit");
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: saveDiskRegInfo|描述:税控盘注册信息入库
	 * </p>
	 */
	public String saveDiskRegInfo() {
		try {
			String operType = request.getParameter("operType");
			DiskRegInfo info = new DiskRegInfo();
			info.setTaxDiskNo(request.getParameter("taxDiskNo"));
			info.setTaxpayerNo(request.getParameter("taxpayerNo"));
			info.setRegisteredInfo(request.getParameter("registeredInfo"));
			
			int result = diskRegInfoService.saveDiskRegInfo(operType, info);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-saveDiskRegInfo", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: CountDiskRegInfo|描述:检查税票盘相同的是否存在
	 * </p>
	 */
	public void CountDiskRegInfo() {
		String taxDiskNo = request.getParameter("taxDiskNo");
		String taxpayerNo = request.getParameter("taxpayerNo");
		Long findNum = diskRegInfoService.CountDiskRegInfo(taxDiskNo,
				taxpayerNo);
		try {
			response.getWriter().write(findNum + "");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-CountDiskRegInfo", e);
		}
	}

	/**
	 * @Action 删除税控盘注册信息管理
	 * 
	 */
	public String deleteDiskRegInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		try {
			diskRegInfoService.deleteDiskRegInfo(selectDiskRegs);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-deleteDiskRegInfo", e);
		}
		return ERROR;
	}

	/**
	 * @Action 导出税控盘注册信息
	 * 
	 */
	public void exportDiskRegInfo() {
		try {

			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			DiskRegInfo info = new DiskRegInfo();
			info.setInstId(request.getParameter("instId"));
			info.setTaxpayerNo(request.getParameter("taxpayerNo"));
			info.setLstAuthInstId(lstAuthInstId);
			List diskInfoList = diskRegInfoService.getTaxDiskInfoList(info);

			StringBuffer fileName = new StringBuffer("税控盘注册信息表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, diskInfoList);
			os.flush();
			os.close();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("DiskRegInfoAction-exportDiskRegInfo", e);
		}
	}

	/**
	 * @writeToExcel 写文件
	 * 
	 */
	private void writeToExcel(OutputStream os, List content) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("税控盘注册信息", 0);
		Label header0 = new Label(i++, 0, "", JXLTool.getHeader());
		Label header1 = new Label(i++, 0, "税控盘号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "纳税人识别号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "注册码信息", JXLTool.getHeader());
		ws.addCell(header0);
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		for (int j = 0; j < 3; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			DiskRegInfo diskRegInfo = (DiskRegInfo) content.get(c);
			int column = count++;
			setWritableSheet(ws, diskRegInfo, column);
		}
		wb.write();
		wb.close();
	}

	/**
	 * @setWritableSheet 文件格式定义
	 * 
	 */
	private void setWritableSheet(WritableSheet ws, DiskRegInfo diskRegInfo,
			int column) throws Exception {
		int i = 0;
		Label cell0 = new Label(i++, column, Integer.toString(column),
				JXLTool.getContentFormat());
		Label cell1 = new Label(i++, column, diskRegInfo.getTaxDiskNo(),
				JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, diskRegInfo.getTaxpayerNo(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, diskRegInfo.getRegisteredInfo(),
				JXLTool.getContentFormat());
		ws.addCell(cell0);
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
	}

	// 页面值传递变量声明
	private String instId;// 机构
	private String taxDiskNo;// 税控盘号
	private String taxpayerNo;// 纳税人识别号
	/** 操作类型 add,edit */
	private String operType;
	private String selectDiskRegs[];

	public String[] getSelectDiskRegs() {
		return selectDiskRegs;
	}

	public void setSelectDiskRegs(String[] selectDiskRegs) {
		this.selectDiskRegs = selectDiskRegs;
	}

	private DiskRegInfo diskRegInfo;// 编辑,新增时对应的主体
	/* service 声明 */
	private DiskRegInfoService diskRegInfoService;

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getTaxpayerNo() {
		return taxpayerNo;
	}

	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}

	public DiskRegInfo getDiskRegInfo() {
		return diskRegInfo;
	}

	public void setDiskRegInfo(DiskRegInfo diskRegInfo) {
		this.diskRegInfo = diskRegInfo;
	}

	public DiskRegInfoService getDiskRegInfoService() {
		return diskRegInfoService;
	}

	public void setDiskRegInfoService(DiskRegInfoService diskRegInfoService) {
		this.diskRegInfoService = diskRegInfoService;
	}
}
