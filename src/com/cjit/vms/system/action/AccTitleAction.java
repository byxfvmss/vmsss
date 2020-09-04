package com.cjit.vms.system.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import net.sf.json.JSONArray;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.JXLTool;
import com.cjit.vms.system.model.AccTitle;
import com.cjit.vms.system.service.AccTitleService;
import com.cjit.vms.trans.action.DataDealAction;

public class AccTitleAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private AccTitle accTitle = new AccTitle();

	private AccTitleService accTitleService;

	public AccTitle getAccTitle() {
		return accTitle;
	}

	public void setAccTitle(AccTitle accTitle) {
		this.accTitle = accTitle;
	}

	public AccTitleService getAccTitleService() {
		return accTitleService;
	}

	public void setAccTitleService(AccTitleService accTitleService) {
		this.accTitleService = accTitleService;
	}

	public String listAccTitle() {
		try {

			accTitleService.findAccTitleList(accTitle, paginationList);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			return ERROR;
		}
		return SUCCESS;
	}

	public String toSavePage() {
		return SUCCESS;
	}

	/**
	 * @title saveAccTitle
	 * @description 保存科目
	 * @author dev4
	 * @return
	 */
	public String saveAccTitle() {
		try {
			accTitle.setAccTitleCode(this.request.getParameter("accTitleCode"));
			List list = accTitleService.findAccTitleList(accTitle,
					paginationList);
			if (list != null && list.size() > 1) {
				setRESULT_MESSAGE("科目编号已存在");
				return SUCCESS;
			}
			String parentAccTitleCode = this.request.getParameter("parentAccTitleCode");
			
			if(parentAccTitleCode!=null&&!"".equals(parentAccTitleCode.trim())){
				
				AccTitle parentAccTitle = accTitleService.getAccTitleByCode(parentAccTitleCode);
				
				parentAccTitleCode = parentAccTitle.getParentAccTitleCode();
				
				if(parentAccTitleCode==null)
					
					parentAccTitleCode = "";
				
				parentAccTitleCode += ","+parentAccTitle.getAccTitleCode();
				
			}else{
				parentAccTitleCode = "";
			}
			
			
			accTitle.setAccTitleName(this.request.getParameter("accTitleName"));
			accTitle.setParentAccTitleCode(parentAccTitleCode);
			accTitleService.saveAccTitle(accTitle);
			setRESULT_MESSAGE("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			setRESULT_MESSAGE("保存失败");
			// return ERROR;
		}
		return SUCCESS;
	}

	private String findParentCodeStrByCode(String  accTitleCode){
		
		AccTitle parentAccTitle = accTitleService.getAccTitleByCode(accTitleCode);
		
		return "";
	}
	
	
	public String toUpdatePage() {
		accTitle = accTitleService.getAccTitleById(this.request
				.getParameter("accTitleId"));
		return SUCCESS;
	}
	
	/**
	 * @title toUpdatePage_tree
	 * @description 科目树修改
	 * @author dev4
	 * @return
	 */
	public String toUpdatePage_tree() {
		accTitle = accTitleService.getAccTitleByCode(accTitle.getAccTitleCode());
		return SUCCESS;
	}

	public String updateAccTitle() {
		try {
			accTitle.setAccTitleName(this.request.getParameter("accTitleName"));
			accTitle.setAccTitleId(this.request.getParameter("accTitleId"));
			accTitleService.updateAccTitle(accTitle);
			setRESULT_MESSAGE("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			setRESULT_MESSAGE("修改失败");
		}
		return SUCCESS;
	}

	public String deleteAccTitle() throws Exception {
		try {
			String accTitleId = request.getParameter("accTitleIds");
			if (accTitleId != null) {
				String[] accTitleIds = accTitleId.split(",");
				if (accTitleIds != null)
					for (int i = 0; i < accTitleIds.length; i++) {
						accTitleService.deleteAccTitle(accTitleIds[i]);
					}
			}
			printWriterResult(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			printWriterResult("删除失败");
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * @title deleteAccTitle_tree
	 * @description 科目树删除
	 * @author dev4
	 * @return
	 * @throws Exception
	 */
	public String deleteAccTitle_tree() {
		
		
		String accTitleCode = accTitle.getAccTitleCode();
		
		accTitleService.deleteAccTitleByCode(accTitleCode);
		
		return SUCCESS;
	}

	public void exportAccTitle() throws Exception {
		List resultList = accTitleService.findAccTitleList(accTitle, null);

		StringBuffer fileName = new StringBuffer("科目信息列表");
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, resultList);
		os.flush();
		os.close();

	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("科目信息列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "科目编码", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "科目名称", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);

		for (int j = 0; j < 3; j++) {
			ws.setColumnView(j, 18);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			AccTitle accTitle = (AccTitle) content.get(c);
			int column = count++;
			setWritableSheet(ws, accTitle, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, AccTitle accTitle,
			int column) throws WriteException {
		int i = 0;

		Label cell1 = new Label(i++, column, String.valueOf(column));
		Label cell2 = new Label(i++, column, accTitle.getAccTitleCode(),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, accTitle.getAccTitleName(),
				JXLTool.getContentFormat());

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
	}

	public String importAccTitle() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("attachmentAccTitle");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				String result = doImportFile(files[0]);
				files = null;
				return result;
			} catch (Exception e) {
				log.error(e);
				setResultMessages("上传文件失败" + e.getMessage());
				// response.sendRedirect(URLEncoder.encode(url,"GBK"));//
				return ERROR;
			}
		} else {
			setResultMessages("上传文件失败");
			return ERROR;
		}
	}

	private String doImportFile(File file) throws Exception {
		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		if (hs != null) {
			// 获取excel第一个sheet页
			String[][] sheet = (String[][]) hs.get("0");
			// 从第二行开始获取每行数据
			StringBuffer taxNoSB = new StringBuffer();
			StringBuffer nullRow = new StringBuffer("");
			String accTitleCode = "";
			for (int i = 1; i < sheet.length; i++) {
				String[] row = sheet[i];
				accTitleCode = "";
				accTitleCode = null == row[0] ? "" : row[0];
				if (!"".equals(accTitleCode)) {
					accTitle.setAccTitleCode(accTitleCode);
					List list = accTitleService
							.findAccTitleList(accTitle, null);
					if (list == null || list.size() == 0) {
						accTitle.setAccTitleName(null == row[1] ? "" : row[1]
								.toString());
						accTitleService.saveAccTitle(accTitle);
					} else {
						taxNoSB.append(accTitleCode + ",");
						setResultMessages("此科目已存在,不可重复添加。");
					}
				} else {
					nullRow.append(i + ",");
				}
			}
			if (0 == taxNoSB.length() && 0 == nullRow.length()) {
				setResultMessages("导入成功.");
				return SUCCESS;
			} else if (0 < taxNoSB.length() && 0 == nullRow.length()) {
				setResultMessages("科目编号为：" + taxNoSB.toString()
						+ "的科目已存在，不可重复添加，其他已保存成功！");
				return ERROR;
			} else if (0 == taxNoSB.length() && 0 < nullRow.length()) {
				setResultMessages("第" + nullRow.toString()
						+ "行科目编号为空，未导入，其他已保存成功！");
				return ERROR;
			} else {
				setResultMessages("科目编号为：" + taxNoSB.toString()
						+ "的科目已存在，不可重复添加；" + "第" + nullRow.toString()
						+ "行科目编号为空，未导入，其他已保存成功！");
				return ERROR;
			}
		} else {
			setResultMessages("导入文件为空，请选择导入文件。");
			return ERROR;
		}
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();

	}
	
	
	/***
	 * 科目树头部跳转
	 * 
	 * @return
	 */
	public String frameHeadAccTitle() {
		return SUCCESS;

	}
	
	
	/***
	 * 科目树主体跳转
	 * 
	 * @return
	 */
	public String frameAccTitle() {
		return SUCCESS;

	}
	
	/***
	 * 科目树左凭证类型树
	 * 
	 * @return
	 */
	public String treeAccTitle() {
			
		return SUCCESS;

	}
	
	
	/***
	 *  科目树左凭证类型树初始化
	 * @return
	 */
	public void selectItemTree() {

		List dbList = accTitleService.findAccTitleList(accTitle, paginationList);
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(dbList).toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
