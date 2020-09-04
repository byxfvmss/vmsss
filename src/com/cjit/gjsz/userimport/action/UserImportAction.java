package com.cjit.gjsz.userimport.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.userimport.model.ImportBats;
import com.cjit.gjsz.userimport.model.ImportFiles;
import com.cjit.gjsz.userimport.service.UserImportService;

public class UserImportAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1389075465633129080L;
	private UserImportService userImportService;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(UserImportAction.class);
	private List listFiles = new LinkedList();
	private String orgId;
	private String errorMsg = "";
	private String infoMsg = "";
	private File f0;
	private File f1;

	public File getF0(){
		return f0;
	}

	public void setF0(File f0){
		this.f0 = f0;
	}

	public File getF1(){
		return f1;
	}

	public void setF1(File f1){
		this.f1 = f1;
	}

	public String getErrorMsg(){
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public String list(){
		try{
			request.setAttribute("listFiles", listFiles);
			return SUCCESS;
		}catch (Exception e){
			logger.warn("", e);
		}
		return ERROR;
	}

	public String onOrgChange(){
		try{
			listFiles = userImportService.getFilesByOrgId(orgId);
			request.setAttribute("listFiles", listFiles);
			return SUCCESS;
		}catch (Exception e){
			logger.warn("", e);
		}
		return ERROR;
	}

	public String saveAndRun(){
		try{
			listFiles = userImportService.getFilesByOrgId(orgId);
			request.setAttribute("listFiles", listFiles);
			for(int i = 0; i < listFiles.size(); i++){
				Method m = BeanUtils.findMethod(this.getClass(), "getF" + i,
						null);
				File f = (File) m.invoke(this, null);
				ImportFiles ifs = (ImportFiles) listFiles.get(i);
				File nf = new File(ifs.getIFilePath());
				nf.deleteOnExit();
				try{
					FileUtils.copyFile(f, nf);
				}catch (Exception ex){
					// 允许空文件上传
					errorMsg += "注意：文件" + ifs.getIFileName() + "可能为空;";
				}
			}
			List list = userImportService.getBatsByOrgId(orgId);
			for(Iterator i = list.iterator(); i.hasNext();){
				ImportBats ib = (ImportBats) i.next();
				runBat(ib.getBFilePath());
			}
			infoMsg = "导入完成.";
		}catch (Exception e){
			logger.warn("", e);
			errorMsg = e.getMessage();
		}
		return SUCCESS;
	}

	private void runBat(String filePath) throws Exception{
		try{
			String ls_1;
			Process p = Runtime.getRuntime().exec(filePath);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			while((ls_1 = bufferedReader.readLine()) != null){
				System.out.println(ls_1);
			}
			if(bufferedReader != null){
				bufferedReader.close();
			}
		}catch (Exception e){
			logger.warn("执行bat失败", e);
			throw new Exception("执行bat失败:" + e.getMessage());
		}
	}

	public void setUserImportService(UserImportService userImportService){
		this.userImportService = userImportService;
	}

	public List getListFiles(){
		return listFiles;
	}

	public String getInfoMsg(){
		return infoMsg;
	}
}
