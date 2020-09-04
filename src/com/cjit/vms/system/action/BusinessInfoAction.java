package com.cjit.vms.system.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.cjit.vms.system.model.BusinessInfo;
import com.cjit.vms.system.service.BusinessInfoService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

public class BusinessInfoAction extends DataDealAction {
	private File attachment;
	private String attachmentFileName;
	private String attachmentFileType;

	private BusinessInfoService businessInfoService;
	private BusinessInfo business;
	private String message;
	private String[] selectBusinessIds;

	// private String businessId;

	public String listBusinessInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		businessInfoService.findBusinessInfoList(business, paginationList);
		return SUCCESS;
	}

	public String initBusinessInfo() {
		if (null != business && null != business.getBusinessId()) {
			List businessList = businessInfoService.findBusinessInfoList(
					business, null);
			if (businessList.size() == 0) {
				this.message = "交易已经被删除";
				return ERROR;
			}
			business = (BusinessInfo) businessList.get(0);
		}
		return SUCCESS;
	}

	public String editBusinessInfo() {
		if (null != business && null != business.getBusinessId()
				&& !"".equals(business.getBusinessId())) {

			businessInfoService.updateBusinessInfo(business);
			this.message = "修改交易成功";
			return SUCCESS;

		} else {
			BusinessInfo b = new BusinessInfo();
			b.setBusinessCode(business.getBusinessCode());
			List list = businessInfoService.findBusinessInfo(b);
			if (list.size() > 0) {
				this.message = "交易码已经存在";
				return ERROR;
			}
			businessInfoService.insertBusinessInfo(business);
			this.message = "添加交易成功";
			return SUCCESS;
		}

	}

	public String delBusinessInfo() {

		for (int i = 0; i < selectBusinessIds.length; i++) {
			if (selectBusinessIds[i] != null
					&& !"".equals(selectBusinessIds[i]))
				businessInfoService.deleteBusinessInfo(selectBusinessIds[i]);
		}
		setResultMessages("删除成功");
		return SUCCESS;
	}

	public String impBisinessinfo() {
		ServletContext sc = ServletActionContext.getServletContext();
		if (attachment != null) {

			// 出现异常返回失败
			int i = 1;
			try {
				// 若文件已存在，删除原文件
				String dir = sc.getRealPath("/WEB-INF");
				File saveFile = new File(new File(dir), attachmentFileName);
				if (saveFile.exists()) {
					saveFile.delete();
					saveFile = new File(new File(dir), attachmentFileName);
				}

				attachment.renameTo(saveFile);

				DataFileParser dataFileParserUtil = DataFileParserUtil
						.createDataFileParser(saveFile);

				if (dataFileParserUtil == null) {
					setResultMessages("请选择[.xls]后缀的文件.");
					return ERROR;
				}

				List temp = new ArrayList();

				String businessCode = "";
				String businessCName = "";
				String businessNote = "";

				while (dataFileParserUtil.hasNextLine()) {
					temp = dataFileParserUtil.next();
					businessCode = null == temp.get(1) ? "" : temp.get(1)
							.toString();
					businessCName = null == temp.get(2) ? "" : temp.get(2)
							.toString();
					businessNote = null == temp.get(3) ? "" : temp.get(3)
							.toString();
					if (!"".equals(businessCode) && !"".equals(businessCName)) {
						BusinessInfo info = new BusinessInfo();
						info.setBusinessCode(businessCode);
						info.setBusinessCName(businessCName);
						info.setBusinessNote(businessNote);

						// 商品信息的取得
						List lsInfo = businessInfoService.findBusinessInfoList(
								info, null);
						if (lsInfo != null && lsInfo.size() > 0) {
							BusinessInfo dbInfo = (BusinessInfo) lsInfo.get(0);
							info.setBusinessId(dbInfo.getBusinessId());
							businessInfoService.updateBusinessInfo(info);
						} else {
							businessInfoService.insertBusinessInfo(info);
						}

					} else {
						log.error("交易码、交易名不能为空.");
						System.out.println("交易码、交易名不能为空.");
					}
					i++;
				}
				setResultMessages("上传成功.");
				return SUCCESS;
			} catch (Exception e) {
				setResultMessages("上传文件发生异常.Line:" + i );
				e.printStackTrace();
				return ERROR;
			}

		} else {
			setResultMessages("请选择上传文件.");
			return ERROR;
		}

	}

	public BusinessInfoService getBusinessInfoService() {
		return businessInfoService;
	}

	public void setBusinessInfoService(BusinessInfoService businessInfoService) {
		this.businessInfoService = businessInfoService;
	}

	public BusinessInfo getBusiness() {
		return business;
	}

	public void setBusiness(BusinessInfo business) {
		this.business = business;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getSelectBusinessIds() {
		return selectBusinessIds;
	}

	public void setSelectBusinessIds(String[] selectBusinessIds) {
		this.selectBusinessIds = selectBusinessIds;
	}

	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	public String getAttachmentFileType() {
		return attachmentFileType;
	}

	public void setAttachmentFileType(String attachmentFileType) {
		this.attachmentFileType = attachmentFileType;
	}

}
