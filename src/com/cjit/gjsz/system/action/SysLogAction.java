package com.cjit.gjsz.system.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cjit.crms.util.StringUtil;
import cjit.logger.LogDO;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

public class SysLogAction extends BaseListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogManagerService logManagerService;
	private List dataList;
	// JSP页面的参
	private String userCname;
	private String menuName;
	private String execDate;
	private String status;
	private String pageFlag;
	private String message;
	private String tableStr;// excel导出使用

	public String getTableStr() {
		return tableStr;
	}

	public void setTableStr(String tableStr) {
		this.tableStr = tableStr;
	}

	public String list() {
		try {
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// Map parms = new HashMap();
			// parms.put("userId", currentUser.getId());
			// parms.put("userCname", this.getUserCname());
			// parms.put("menuName", this.getMenuName());
			// parms.put("status", this.getStatus());
			if ("listPage".equalsIgnoreCase(this.pageFlag)) {
				LogDO log = new LogDO();
				log.setUserId(this.getUserCname());
				log.setUserCname(this.getUserCname());
				log.setMenuName("%" + this.getMenuName() + "%");
				if (StringUtil.isNotEmpty(this.getExecDate())) {
					if (this.getExecDate().length() == 10) {
						// yyyy-MM-dd
						log.setExecTime(DateUtils.stringToDate(this
								.getExecDate(), DateUtils.ORA_DATES_FORMAT));
					} else if (this.getExecDate().length() == 8) {
						// yyyyMMdd
						log.setExecTime(DateUtils.stringToDate(this
								.getExecDate(), DateUtils.ORA_DATE_FORMAT));
					}
				}
				log.setStatus(this.getStatus());
				log.setUId(currentUser.getId());
				dataList = logManagerService.selectByFormWithPaging(log,
						paginationList);
				if (dataList == null)
					dataList = new ArrayList();
				this.request.setAttribute("status", this.getStatus());
			} else {
				dataList = new ArrayList();
			}
			if (paginationList != null) {
				int nCurPage = paginationList.getCurrentPage();
				this.curPage = nCurPage;
			}
			this.request.setAttribute("curPage", String.valueOf(this.curPage));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete() {
		try {
			String[] delIds = new String[getIds().size()];
			for (int i = 0; i < getIds().size(); i++) {
				delIds[i] = (String) getIds().get(i);
			}
			logManagerService.deleteByPrimarys(delIds);
			this.message = "删除成功";
			if (paginationList != null && this.curPage > 1) {
				paginationList.setCurrentPage(this.curPage);
			}
			list();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 导出到Excel
	 * 
	 * @return String
	 * @throws Exception
	 * @throws IOException
	 */
	public String exportToExcel() throws Exception, IOException {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// Map parms = new HashMap();
		// parms.put("userCname", this.getUserCname());
		// parms.put("menuName", this.getMenuName());
		// parms.put("status", this.getStatus());
		LogDO log = new LogDO();
		log.setUserId(this.getUserCname());
		log.setUserCname(this.getUserCname());
		log.setMenuName(this.getMenuName());
		log.setExecTime(DateUtils.stringToDate(this.getExecDate(),
				DateUtils.ORA_DATES_FORMAT));
		log.setStatus(this.getStatus());
		log.setUId(currentUser.getId());
		PaginationList paginationList = new PaginationList();
		paginationList.setPageSize(10000);
		paginationList.setCurrentPage(1);
		List dataList = logManagerService.selectByFormWithPaging(log,
				paginationList);
		OutputStream out = response.getOutputStream();
		String fileName = "操作日志导出";
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + ".xls");
		HSSFWorkbook wb = null;
		HSSFSheet sheetMain = null;// 主表页
		try {
			wb = new HSSFWorkbook();
			// 构造第一个页签中报文主表信息 Begin
			sheetMain = wb.createSheet();
			// 创建主表页Excel第一行
			HSSFRow row = sheetMain.createRow(0);
			// 设置Cell格式：粗体字＋居中
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 创建Cell
			HSSFCell cell0 = row.createCell((short) 0);
			cell0.setCellStyle(cellStyle);
			cell0.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell0.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell0.setCellValue(new HSSFRichTextString("序号"));
			sheetMain.setColumnWidth((short) 0, (short) 1800);
			// 创建Cell
			HSSFCell cell1 = row.createCell((short) 1);
			cell1.setCellStyle(cellStyle);
			cell1.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell1.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell1.setCellValue(new HSSFRichTextString("用户名"));
			sheetMain.setColumnWidth((short) 1, (short) 2500);
			// 创建Cell
			HSSFCell cell2 = row.createCell((short) 2);
			cell2.setCellStyle(cellStyle);
			cell2.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell2.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell2.setCellValue(new HSSFRichTextString("机构名称"));
			sheetMain.setColumnWidth((short) 2, (short) 4200);
			// 创建Cell
			HSSFCell cell3 = row.createCell((short) 3);
			cell3.setCellStyle(cellStyle);
			cell3.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell3.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell3.setCellValue(new HSSFRichTextString("菜单名"));
			sheetMain.setColumnWidth((short) 3, (short) 5000);
			// 创建Cell
			HSSFCell cell4 = row.createCell((short) (4));
			cell4.setCellStyle(cellStyle);
			cell4.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell4.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell4.setCellValue(new HSSFRichTextString("操作时间"));
			sheetMain.setColumnWidth((short) 4, (short) 5000);
			// 创建Cell
			HSSFCell cell5 = row.createCell((short) (5));
			cell5.setCellStyle(cellStyle);
			cell5.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell5.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell5.setCellValue(new HSSFRichTextString("说明"));
			sheetMain.setColumnWidth((short) 5, (short) 13700);
			// 创建Cell
			HSSFCell cell6 = row.createCell((short) (6));
			cell6.setCellStyle(cellStyle);
			cell6.setCellType(HSSFCell.CELL_TYPE_STRING); // String类型数据
			cell6.setEncoding(HSSFCell.ENCODING_UTF_16); // 中文字符处理
			cell6.setCellValue(new HSSFRichTextString("操作状态"));
			sheetMain.setColumnWidth((short) 6, (short) 2300);
			// 单元格样式
			HSSFCellStyle cellStyleTemp = wb.createCellStyle();
			// 设置Cell格式：对齐方式
			cellStyleTemp.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			// cellStyleTemp.setVerticalAlignment(HSSFCellStyle.ALIGN_GENERAL);
			cellStyleTemp.setWrapText(true);
			// 从第二行开始创建所有导出报文主表信息
			if ((dataList != null) && (dataList.size() > 0)) {
				for (int i = 0; i < dataList.size(); i++) {
					row = sheetMain.createRow(i + 1);
					row.setHeight((short) 750);
					LogDO logDO = (LogDO) dataList.get(i);
					// 序号
					HSSFCell cell_0 = row.createCell((short) 0);
					// 中文字符处理
					cell_0.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_0.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_0.setCellValue(new HSSFRichTextString(String.valueOf(
							i + 1).toString()));
					// 用户名
					HSSFCell cell_1 = row.createCell((short) 1);
					// 中文字符处理
					cell_1.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_1.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_1.setCellValue(new HSSFRichTextString(logDO
							.getUserCname()));
					// 机构名称
					HSSFCell cell_2 = row.createCell((short) 2);
					// 中文字符处理
					cell_2.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_2.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_2.setCellValue(new HSSFRichTextString(logDO
							.getInstCname()));
					// 菜单名
					HSSFCell cell_3 = row.createCell((short) 3);
					// 中文字符处理
					cell_3.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_3.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_3.setCellValue(new HSSFRichTextString(logDO
							.getMenuName()));
					// 操作时间
					HSSFCell cell_4 = row.createCell((short) 4);
					// 中文字符处理
					cell_4.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_4.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_4.setCellValue(new HSSFRichTextString(DateUtils
							.toString(logDO.getExecTime(),
									"yyyy-MM-dd HH:mm:ss")));
					// 说明
					HSSFCell cell_5 = row.createCell((short) 5);
					// 中文字符处理
					cell_5.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					HSSFCellStyle cs = wb.createCellStyle();
					cs.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
					cs.setWrapText(true);
					cell_5.setCellStyle(cs);
					// 设置列值
					cell_5.setCellValue(new HSSFRichTextString(logDO
							.getDescription()));
					// 操作状态
					HSSFCell cell_6 = row.createCell((short) 6);
					// 中文字符处理
					cell_6.setEncoding(HSSFCell.ENCODING_UTF_16);
					// 对齐方式
					cell_6.setCellStyle(cellStyleTemp);
					// 设置列值
					cell_6.setCellValue(new HSSFRichTextString(logDO
							.getStatus().equals("1") ? "成功" : "失败"));
				}
			}
			// 构造第一个页签中报文主表信息 End
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			this.log.error("exportToExcel-Exception");
			// e.printStackTrace();
			// throw new Exception("写文件输出流时出现错误....");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				this.log.error("exportToExcel-IOException");
				// e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	/**
	 * 导出到Excel(网页格式)废弃
	 * 
	 * @return
	 */
	public String exportToExcel_() {
		Map parms = new HashMap();
		parms.put("userCname", this.getUserCname());
		parms.put("menuName", this.getMenuName());
		parms.put("status", this.getStatus());
		PaginationList paginationList = new PaginationList();
		paginationList.setPageSize(10000);
		paginationList.setCurrentPage(1);
		List dataList = logManagerService.selectByFormWithPaging(parms,
				paginationList);
		StringBuffer sb = new StringBuffer();
		if (dataList != null && dataList.size() > 0) {
			sb.append("<table border='1'>");
			sb.append("<tr>");
			sb.append("<td>序号</td>");
			sb.append("<td>用户名</td>");
			sb.append("<td>机构名称</td>");
			sb.append("<td>菜单名</td>");
			sb.append("<td>操作时间</td>");
			sb.append("<td>说明</td>");
			sb.append("<td>操作状态</td>");
			sb.append("</tr>");
			for (int i = 0; i < dataList.size(); i++) {
				LogDO logDO = (LogDO) dataList.get(i);
				sb.append("<tr>");
				sb.append("<td>").append(i + 1).append("</td>");
				sb.append("<td>").append(logDO.getUserCname()).append("</td>");
				sb.append("<td>").append(logDO.getInstCname()).append("</td>");
				sb.append("<td>").append(logDO.getMenuName()).append("</td>");
				sb.append("<td>&nbsp;").append(
						DateUtils.toString(logDO.getExecTime(),
								DateUtils.ORA_DATE_TIMES_FORMAT)).append(
						"&nbsp;</td>");
				sb.append("<td>").append(logDO.getDescription())
						.append("</td>");
				sb.append("<td>").append(
						logDO.getStatus().equals("1") ? "成功" : "失败").append(
						"</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
		}
		tableStr = sb.toString();
		return SUCCESS;
	}

	public String getUserCname() {
		return userCname;
	}

	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getExecDate() {
		return execDate;
	}

	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}
}
