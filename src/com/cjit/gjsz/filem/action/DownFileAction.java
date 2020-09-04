package com.cjit.gjsz.filem.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.RptSendCommit;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.filem.model.DownFile;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.OrganizationService;

public class DownFileAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String ZIP_TYPE = ".zip";
	private final String XML_TYPE = ".xml";
	private final String SYS_TYPE = "FAL";
	private String sendFilePath = "";
	private List listFiles = new LinkedList();
	private List authInstList = new ArrayList();// 授权机构
	private DataDealService dataDealService;
	private OrganizationService organizationService;
	private String packName;
	private String pageFlag = "";

	public String getSendFilePath(){
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath){
		this.sendFilePath = sendFilePath;
	}

	public void setListFiles(List listFiles){
		this.listFiles = listFiles;
	}

	public List getListFiles(){
		return listFiles;
	}

	public String downFileList(){
		this.pageFlag = request.getParameter("pageFlag");
		if(StringUtil.isEmpty(this.pageFlag)){
			this.packName = null;
		}else{
			this.pageFlag = "";
		}
		return this.downFileList(pageFlag);
	}

	public String downFileList2(){
		this.pageFlag = request.getParameter("pageFlag");
		if(StringUtil.isEmpty(this.pageFlag)){
			this.packName = null;
			this.pageFlag = "2";
		}
		return this.downFileList(pageFlag);
	}

	private String downFileList(String pageFlag){
		this.pageFlag = pageFlag;
		try{
			// 用户受权机构
			// getAuthInstList(authInstList);
			// List orgIdList = new ArrayList();
			// for(Iterator it = authInstList.iterator(); it.hasNext();){
			// orgIdList.add(((Organization) it.next()).getId());
			// }
			// final List packNameList = dataDealService
			// .findPackNameListByInstCode(orgIdList);
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			if(currentUser == null || StringUtil.isEmpty(currentUser.getId())){
				return ERROR + this.getPageFlag();
			}
			final List packNameList = dataDealService
					.findPackNameListByUserId(currentUser.getId());
			if(listFiles != null){
				listFiles.clear();
			}
			String path = this.servletContext.getRealPath("/data/sendfile");
			log.info("DownFileAction-downFileList-path: " + path);
			for(Iterator it = packNameList.iterator(); it.hasNext();){
				RptSendCommit rptSend = (RptSendCommit) it.next();
				// 过滤
				if(!StringUtils.isEmpty(this.packName)
						&& rptSend.getPackName().indexOf(this.packName) == -1){
					continue;
				}
				File m = new File(new StringBuffer(path).append(File.separator)
						.append(rptSend.getPackName()).append(File.separator)
						.append(rptSend.getPackName()).append(ZIP_TYPE)
						.toString());
				if(m.exists()){
					DownFile df = new DownFile();
					df.setFileName(m.getName());
					df.setFilePath(rptSend.getPackName() + File.separator
							+ m.getName());
					df.setFileCreateDate(new Date(m.lastModified()));
					if(rptSend.getIsSendMts() != null
							&& !rptSend.getIsSendMts().equals("0")){
						df.setSendMts("1");
						if("2".equalsIgnoreCase(this.pageFlag)){
							listFiles.add(df);
						}
					}else{
						if(!"2".equalsIgnoreCase(this.pageFlag)){
							listFiles.add(df);
						}
					}
				}
			}
			// 排序,按照时间倒叙
			Collections.sort(listFiles, new Comparator(){

				public int compare(Object o1, Object o2){
					DownFile a = (DownFile) o1;
					DownFile b = (DownFile) o2;
					return b.getFileCreateDate().compareTo(
							a.getFileCreateDate());
				}
			});
			// 在wl8.1下放入request
			this.request.setAttribute("listFiles", this.listFiles);
			this.request.setAttribute("downFileReturn", request
					.getParameter("downFileReturn"));
			return SUCCESS + this.getPageFlag();
		}catch (Exception e){
			e.printStackTrace();
			log.error("DownFileAction-downFileList", e);
			return ERROR + this.getPageFlag();
		}
	}

	/**
	 * <p> 方法名称: downStobFileList|描述: 进入STOB文件下载页面 </p>
	 * @return String
	 */
	public String downStobFileList(){
		try{
			if(listFiles != null){
				listFiles.clear();
			}
			String path = this.servletContext.getRealPath("/data/stobfile");
			File f = new File(path);
			File[] fs = f.listFiles();
			if(fs != null && fs.length > 0){
				for(int j = 0; j < fs.length; j++){
					if(fs[j].isFile()){
						if(fs[j].getName().toUpperCase().startsWith("STOB")
								&& fs[j].getName().toLowerCase().endsWith(
										XML_TYPE)){
							DownFile df = new DownFile();
							df.setFileName(fs[j].getName());
							df
									.setFileCreateDate(new Date(fs[j]
											.lastModified()));
							listFiles.add(df);
						}
					}
				}
			}
			// 在wl8.1下放入request
			this.request.setAttribute("listFiles", this.listFiles);
			this.request.setAttribute("downFileReturn", request
					.getParameter("downFileReturn"));
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error("DownFileAction-downFileList", e);
			return ERROR;
		}
	}

	private String controlFileName;

	public String getControlFileName(){
		return controlFileName;
	}

	public void setControlFileName(String controlFileName){
		this.controlFileName = controlFileName;
	}

	// 读附件
	public void downloadAttachmentEx(){
		// 读到流中
		try{
			sendFilePath = StringUtil.replace(sendFilePath, "//", "/");
			sendFilePath = StringUtil.replace(sendFilePath, "\\", "/");
			controlFileName = StringUtil.replace(controlFileName, "//", "/");
			controlFileName = StringUtil.replace(controlFileName, "\\", "/");
			response.reset();
			response.setContentType("application/zip");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ controlFileName.substring(
							controlFileName.indexOf("/") + 1, controlFileName
									.length()));
			response.getOutputStream().flush();
			String path = this.servletContext.getRealPath("/data/sendfile/"
					+ controlFileName);
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			OutputStream out = this.response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buf = new byte[512];
			int size = 0;
			// 循环取出流中的数据
			while((size = bis.read(buf)) != -1){
				out.write(buf, 0, size);
			}
			out.close();
			bis.close();
			is.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
			log.error("DownFileAction-downloadAttachmentEx", e);
		}catch (IOException e){
			e.printStackTrace();
			log.error("DownFileAction-downloadAttachmentEx", e);
		}
	}

	/**
	 * <p> 方法名称: downloadAttachmentStob|描述: STOB文件下载 </p>
	 */
	public void downloadAttachmentStob(){
		// 读到流中
		try{
			sendFilePath = StringUtil.replace(sendFilePath, "//", "/");
			sendFilePath = StringUtil.replace(sendFilePath, "\\", "/");
			controlFileName = StringUtil.replace(controlFileName, "//", "/");
			controlFileName = StringUtil.replace(controlFileName, "\\", "/");
			response.reset();
			response.setContentType("application/xml");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ controlFileName.substring(
							controlFileName.indexOf("/") + 1, controlFileName
									.length()));
			response.getOutputStream().flush();
			String path = this.servletContext.getRealPath("/data/stobfile/"
					+ controlFileName);
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			OutputStream out = this.response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buf = new byte[512];
			int size = 0;
			// 循环取出流中的数据
			while((size = bis.read(buf)) != -1){
				out.write(buf, 0, size);
			}
			out.close();
			bis.close();
			is.close();
		}catch (FileNotFoundException e){
			e.printStackTrace();
			log.error("DownFileAction-downloadAttachmentStob", e);
		}catch (IOException e){
			e.printStackTrace();
			log.error("DownFileAction-downloadAttachmentStob", e);
		}
	}

	/**
	 * 获取当前用户拥有权限的机构集
	 * @param authInstList
	 */
	private void getAuthInstList(List authInstList){
		if(authInstList == null){
			authInstList = new ArrayList();
		}
		if(authInstList.size() > 0){
			authInstList.clear();
		}
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if(currentUser != null){
			authInstList.addAll(currentUser.getOrgs());
		}
	}

	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public OrganizationService getOrganizationService(){
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}

	public String getPackName(){
		return packName;
	}

	public void setPackName(String packName){
		this.packName = packName;
	}

	public String getPageFlag(){
		return pageFlag == null ? "" : pageFlag.trim();
	}

	public void setPageFlag(String pageFlag){
		this.pageFlag = pageFlag;
	}
}