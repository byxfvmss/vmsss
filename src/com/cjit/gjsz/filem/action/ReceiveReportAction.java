/**
 * 
 */
package com.cjit.gjsz.filem.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.service.CommonService;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.interfacemanager.model.UploadModel;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

/**
 * @author yulubin
 */
public class ReceiveReportAction extends BaseListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3424688200625651275L;
	private UploadModel uploadModel;
	private ReceiveReport receiveReport = new ReceiveReport();
	private ReceiveReportService receiveReportService;
	private DataDealService dataDealService;
	private CommonService commonService;
	private LogManagerService logManagerService;
	private List resetDataStatusList;
	private AutoDealRptService autoDealRptService;
	// 报表数据表最大字段数（该属性的值必须大于等于字段数最多的报表的字段数）
	protected int largestColumnNum;
	static final int BUFFER = 2048;
	private List ids1 = new ArrayList();
	private String reasionInfo = "";
	// 受权机构列表
	protected List authInstList = new ArrayList();
	/*
	 * 列信息（Key:tableId, Value:RptColumnInfoList）
	 */
	private Map columnInfoMap = new HashMap();
	/*
	 * 表信息（Key:tableId, Value:RptTableInfo）
	 */
	private Map tableInfoMap = new HashMap();
	//
	private String pageFrom = "";

	public String getReasionInfo() {
		return reasionInfo;
	}

	public void setReasionInfo(String reasionInfo) {
		this.reasionInfo = reasionInfo;
	}

	public List getIds1() {
		return ids1;
	}

	public void setIds1(List ids1) {
		this.ids1 = ids1;
	}

	public List getResetDataStatusList() {
		return resetDataStatusList;
	}

	public String resolveZip() {
		String description = "接收反馈报文";
		User user = this.getCurrentUser();
		try {
			initConfigParameters();
			// zip包中xml文件集
			List xmlFileList = null;
			// 存放解压后xml反馈文件的文件夹
			String errFilePath = this.servletContext
					.getRealPath("/data/errfile");
			File dir = new File(errFilePath);
			// 文件夹已存在则直接删除所有子文件
			if (dir.exists()) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			// 不存在则创建文件夹
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 解析xml反馈文件是否出错
			boolean bNoError = true;
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			// 接收zip反馈文件
			ZipFile zipfile = new ZipFile(uploadModel.getFile());
			if (zipfile != null) {
				description = description + "[" + uploadModel.getFileFileName()
						+ "]";
				xmlFileList = new ArrayList();
				ZipEntry entry;
				Enumeration e = zipfile.entries();
				while (e.hasMoreElements()) {
					entry = (ZipEntry) e.nextElement();
					if (entry.getName().toLowerCase().endsWith("xml")) {
						// 定义xml文件
						File xmlFile = new File(errFilePath + File.separator
								+ entry.getName());
						is = new BufferedInputStream(zipfile
								.getInputStream(entry));
						int count;
						byte data[] = new byte[BUFFER];
						FileOutputStream fos = new FileOutputStream(xmlFile);
						dest = new BufferedOutputStream(fos, BUFFER);
						while ((count = is.read(data, 0, BUFFER)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();
						dest.close();
						is.close();
						// 将解压的xml文件加入文件集
						xmlFileList.add(xmlFile);
					}
				}
				// 解析xml文件
				if (xmlFileList != null && xmlFileList.size() > 0) {
					StringBuffer sbErrorMsg = new StringBuffer();
					StringBuffer sbDataErrorInfo = new StringBuffer();
					for (int i = 0; i < xmlFileList.size(); i++) {
						File xmlFile = (File) xmlFileList.get(i);
						String errormsg = this.resolveXml(xmlFile);
						if (StringUtil.isNotEmpty(errormsg)) {
							description += "<br>包含文件[" + xmlFile.getName()
									+ "]";
							if (errormsg.indexOf("记录不存在") >= 0) {
								description += " 对应报文<br>" + errormsg + "<br>";
								sbDataErrorInfo.append(description);
							} else {
								if (errormsg.indexOf("格式错误") >= 0) {
									description += "格式错误<br>";
									bNoError = false;
								} else {
									description += "失败<br>";
									bNoError = false;
								}
								sbErrorMsg.append(description);
							}
						}
					}
					if (bNoError) {
						description += "成功";
					}
					receiveReport.setErrorMemo(sbErrorMsg.toString());
					receiveReport.setDataErrorInfo(sbDataErrorInfo.toString());
				} else {
					listReceiveReports2();
					description = "接收ZIP文件有误";
					receiveReport
							.setDataErrorInfo("接收ZIP文件有误！ZIP文件中未发现可识别的xml反馈文件外。");
					logManagerService.writeLog(request, user, "0020.0030",
							"报文管理.反馈接收", "反馈接收", description, "0");
					return ERROR;
				}
				// 查询反馈错误信息
				listReceiveReports2();
			} else {
				listReceiveReports2();
				description = "接收ZIP文件有误";
				logManagerService.writeLog(request, user, "0020.0030",
						"报文管理.反馈接收", "反馈接收", description, "0");
				return ERROR;
			}
			String status = "1";// 成功状态
			if (bNoError) {
				this.addActionMessage("反馈接收完成。");
			} else {
				status = "0";
			}
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"反馈接收", description, status);
			return SUCCESS;
		} catch (java.io.FileNotFoundException e) {
			listReceiveReports2();
			description = "接收ZIP文件有误";
			receiveReport
					.setDataErrorInfo("接收ZIP文件有误！ZIP文件中除xml反馈文件外，可能还含有目录或其它不可识别的文件。");
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"反馈接收", description, "0");
			e.printStackTrace();
			log.error("ReceiveReportAction-resolveZip", e);
			return ERROR;
		} catch (Exception e) {
			listReceiveReports2();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"反馈接收", description, "0");
			e.printStackTrace();
			log.error("ReceiveReportAction-resolveZip", e);
			return ERROR;
		}
	}

	// (fwy)反馈文件的接收ACTION
	public String resolveXml() {
		String description = "接收反馈报文";
		try {
			initConfigParameters();
			String strFileName = null;
			if (uploadModel != null) {
				strFileName = uploadModel.getFileFileName();
			}
			if (strFileName == null || strFileName.equals("")) {
				// 获取当前登录用户的主报告行列表
				User user = this.getCurrentUser();
				String[] m = this.autoDealRptService.findRptTitles(
						user.getId(), null);
				if (m == null)
					this.addActionMessage("用户[" + user.getId()
							+ "]所属机构未配置主报告行!");
				else {
					String info = this.getAutoDealRptService().receiveReport(
							Arrays.asList(m), null, this.changeDataStatus,
							this.configLowerStatusLinkage);
					this.addActionMessage(info);
				}
				// this.getAutoDealRptService().receiveErrorFiles(Arrays.asList(m));
				// this.getAutoDealRptService().receiveHistorySend(Arrays.asList(m));
				listReceiveReports();
			} else {
				StringBuffer sb = new StringBuffer();
				Map map = new HashMap();
				String errormsg = "";
				// 判断文件是否需要接收
				ReceiveReport rr = new ReceiveReport();
				if (strFileName.startsWith("FALTT")) {
					rr.setRptSendFileName(strFileName);
				} else {
					rr.setRptSendFileName(strFileName.replaceAll("ERR", ""));
				}
				if (receiveReportService.hasDuplicateReceiveReport(rr)) {
					errormsg = "<div id=\"msg\">反馈文件" + strFileName
							+ "已接收</div>";
				} else if (strFileName.length() > 17) {
					// 获取对应主报告行
					String rptTitle = strFileName.substring(5, 17);
					List rptTitleList = this.getAutoDealRptService()
							.findRptTitleList();
					if (rptTitleList != null && rptTitle != null
							&& !rptTitleList.contains(rptTitle)) {
						errormsg = "<div id=\"msg\">反馈文件" + strFileName
								+ "对应主报告行" + rptTitle + "不存在于系统中 </div>";
					} else {
						errormsg = receiveReportService.saveImportXmlFile(
								uploadModel.getFile(), strFileName, sb, map);
						String fileType = sb.toString();
						receiveReport.setInfoType(DataUtil.getInfoTypeName(
								null, null, fileType));
						receiveReport.setTableId(DataUtil
								.getTableIdByFileType(fileType));
					}
				}
				receiveReport.setErrorMemo(errormsg);
				if (strFileName != null && strFileName.length() > 23) {
					String substr = strFileName.substring(17, 23);
					if (DateUtils.isValidDate(substr, "yyMMdd"))
						receiveReport.setReceiveDate(DateUtils.toString(
								DateUtils.stringToDate(substr,
										DateUtils.ORA_DATE_FORMAT_SIMPLE),
								DateUtils.ORA_DATES_FORMAT));
				}
				// listReceiveReports();
				description = description + "[" + strFileName + "]";
				User user = this.getCurrentUser();
				String status = "1";// 成功状态
				if (StringUtil.isEmpty(errormsg)) {
					this.addActionMessage("反馈接收完成。");
				} else {
					status = "0";
				}
				logManagerService.writeLog(request, user, "0020.0030",
						"报文管理.反馈接收", "反馈接收", description, status);
				if (map != null
						&& !"yes".equalsIgnoreCase((String) map
								.get("hasDuplicate"))) {
					map.put("userId", user.getId());
					// 根据反馈文件内容，修改对应业务数据表中记录状态和发送记录表中标识
					receiveReportService.parseFeedbackReport(map,
							this.changeDataStatus,
							this.configLowerStatusLinkage);
					if (map.get("receiveErrorMsg") != null) {
						receiveReport.setDataErrorInfo("对应报文<br>"
								+ (String) map.get("receiveErrorMsg"));
					}
				}
				listReceiveReports();
				if (StringUtil.equalsIgnoreCase(receiveReport.getInfoType(),
						"控制反馈信息")) {
					return "success1";
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"反馈接收", description, "0");
			e.printStackTrace();
			log.error("ReceiveReportAction-resolveXml", e);
			return ERROR;
		}
	}

	private String resolveXml(File file) {
		String errormsg = null;
		try {
			StringBuffer sb = new StringBuffer();
			String strFileName = file.getName();
			Map map = new HashMap();
			// 解析反馈xml文件并保存反馈错误信息
			errormsg = receiveReportService.saveImportXmlFile(file,
					strFileName, sb, map);
			if (map != null) {
				User user = this.getCurrentUser();
				map.put("userId", user.getId());
				receiveReportService.parseFeedbackReport(map,
						this.changeDataStatus, this.configLowerStatusLinkage);
				if (map.get("receiveErrorMsg") != null
						&& StringUtil.isEmpty(errormsg)) {
					errormsg = (String) map.get("receiveErrorMsg");
				}
			}
		} catch (Exception e) {
			errormsg = "异常";
			e.printStackTrace();
			log.error("ReceiveReportAction-resolveXml-private", e);
		}
		return errormsg;
	}

	// 进入反馈接收页面、接收xml反馈文件后查询反馈错误信息列表
	public String listReceiveReports() {
		try {
			initInstList();
			// String interfaceVer = initInterfaceVer();
			resetDataStatusList = commonService
					.getResetDataStatusListForPageListLowerStatus();
			if (StringUtil.isNotEmpty(receiveReport.getInfoType())
					|| StringUtil.isNotEmpty(receiveReport.getTableId())
					|| "uprr".equalsIgnoreCase(this.pageFrom)) {
				if (StringUtil.isNotEmpty(receiveReport.getReceiveDate())
						&& receiveReport.getReceiveDate().length() > 6) {
					receiveReport.setReceiveDate(DateUtils.toString(DateUtils
							.stringToDate(receiveReport.getReceiveDate(),
									DateUtils.ORA_DATES_FORMAT),
							DateUtils.ORA_DATE_FORMAT_SIMPLE));
				}
				// 当所查询反馈信息不是"控制反馈信息"时
				if ("uprr".equalsIgnoreCase(this.pageFrom)
						|| receiveReport.getInfoType().indexOf("控制") < 0) {
					// DFHL 过滤用户能看到的机构
					receiveReport.setNotInfoType("控制反馈信息");
					receiveReport.setUserId(this.getCurrentUser().getId());
					receiveReportService.getReceiveReports(receiveReport,
							paginationList);
					// 关联各报文数据表，判断其数据状态，若不等于6或7，则显示为已打回
					if (paginationList.getRecordList() != null) {
						List recordList = paginationList.getRecordList();
						for (int i = 0; i < recordList.size(); i++) {
							ReceiveReport rr = (ReceiveReport) recordList
									.get(i);
							if (rr != null && "0".equals(rr.getHasReject())) {
								if (StringUtil.isEmpty(rr.getDataStatus())) {
									String fileType = rr.getRptSendFileName()
											.substring(3, 5);
									String keyRptNoColumnId = DataUtil
											.getRptNoColumnIdByFileType(fileType);
									String byeRptNoColumnId = DataUtil
											.getByeRptNoColumnIdByFileType(fileType);
									String errKeyRptNo = DataUtil
											.getKeyRptNoForBussNo(rr
													.getDataNumber());
									String errByeRptNo = DataUtil
											.getByeRptNoForBussNo(rr
													.getDataNumber());
									RptData rd = new RptData();
									rd.setTableId(rr.getTableId());
									// rd.setRptNo(rr.getDataNumber());
									if (StringUtil.isNotEmpty(keyRptNoColumnId)
											&& StringUtil
													.isNotEmpty(errKeyRptNo)) {
										rd.setRptNoColumnId(keyRptNoColumnId);
										rd.setRptNo(errKeyRptNo);
									}
									if (StringUtil.isNotEmpty(byeRptNoColumnId)
											&& StringUtil
													.isNotEmpty(errByeRptNo)) {
										rd
												.setByeRptNoColumnId(byeRptNoColumnId);
										rd.setByeRptNo(errByeRptNo);
									}
									List rptList = dataDealService
											.findRptDataReduce(rd);
									if (rptList != null && rptList.size() == 1) {
										rd = (RptData) rptList.get(0);
										if (rd.getDataStatus() != null
												&& !"".equals(rd
														.getDataStatus())) {
											int datastatus = Integer.valueOf(
													rd.getDataStatus())
													.intValue();
											if (datastatus != DataUtil.YBS_STATUS_NUM
													&& datastatus != DataUtil.YSC_STATUS_NUM) {
												rr.setHasReject("1");
											}
										}
									}
								} else if (Integer.valueOf(rr.getDataStatus())
										.intValue() != DataUtil.YSC_STATUS_NUM) {
									rr.setHasReject("1");
								}
							}
						}
					}
				} else {
					String configFileGenRptTitle = "";
					String[] rptTitles = null;
					if ("yes".equalsIgnoreCase(this.configIsCluster)) {
						configMap = autoDealRptService.initConfigParameters();
						orgConfigList = autoDealRptService
								.findAllOrgConfigList(null, null, null);
					} else {
						CacheabledMap cache = (CacheabledMap) CacheManager
								.getCacheObject("paramCache");
						if (cache != null) {
							configMap = (Map) cache.get("configMap");
							orgConfigList = (List) cache.get("orgConfigList");
						}
					}
					if (configMap != null) {
						configFileGenRptTitle = (String) configMap
								.get("config.fileGen.rpttitle");
					}
					if ("yes".equals(configFileGenRptTitle)) {
						// 根据主报告行号汇总生成报文
						rptTitles = autoDealRptService.findRptTitles(this
								.getCurrentUser().getId(), receiveReport
								.getInstCode());
					} else {
						// 根据用户所属机构主报告行号生成报文
						if (StringUtil.isEmpty(receiveReport.getInstCode())) {
							// 页面未选择机构
							rptTitles = autoDealRptService.findRptTitles(this
									.getCurrentUser().getId(), null);
						} else {
							// 页面选择机构
							rptTitles = autoDealRptService.findRptTitles(null,
									receiveReport.getInstCode());
						}
					}
					if (rptTitles == null) {
						rptTitles = new String[] { "NullRptTitle" };
					}
					receiveReport.setDataNumbers(rptTitles);
					// receiveReport.setDataNumber(this.getCurrentUser()
					// .getOrgId());
					receiveReportService.getReceiveReports(receiveReport,
							paginationList);
				}
				if (StringUtil.isNotEmpty(receiveReport.getReceiveDate())) {
					receiveReport.setReceiveDate(DateUtils.toString(DateUtils
							.stringToDate(receiveReport.getReceiveDate(),
									DateUtils.ORA_DATE_FORMAT_SIMPLE),
							DateUtils.ORA_DATES_FORMAT));
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ReceiveReportAction-listReceiveReports", e);
			return ERROR;
		}
	}

	// 进入反馈接收页面、接收xml反馈文件后查询反馈错误信息列表
	public String listReceiveReports2() {
		try {
			initInstList();
			resetDataStatusList = commonService
					.getResetDataStatusListForPageListLowerStatus();
			if (StringUtil.isNotEmpty(receiveReport.getReceiveDate())
					&& receiveReport.getReceiveDate().length() > 6) {
				receiveReport.setReceiveDate(DateUtils.toString(DateUtils
						.stringToDate(receiveReport.getReceiveDate(),
								DateUtils.ORA_DATES_FORMAT),
						DateUtils.ORA_DATE_FORMAT_SIMPLE));
			}
			// DFHL 过滤用户能看到的机构
			receiveReport.setUserId(this.getCurrentUser().getId());
			receiveReport.setDataNumber(this.getCurrentUser().getOrgId());
			receiveReportService.getReceiveReports2(receiveReport,
					paginationList);
			// 关联各报文数据表，判断其数据状态，若不等于6或7，则显示为已打回
			if (paginationList.getRecordList() != null) {
				List recordList = paginationList.getRecordList();
				for (int i = 0; i < recordList.size(); i++) {
					ReceiveReport rr = (ReceiveReport) recordList.get(i);
					if (rr != null && "0".equals(rr.getHasReject())
							&& StringUtil.isNotEmpty(rr.getTableId())) {
						RptData rd = new RptData();
						rd.setTableId(rr.getTableId());
						rd.setRptNo(rr.getDataNumber());
						List rptList = dataDealService.findRptDataReduce(rd);
						if (rptList != null && rptList.size() == 1) {
							rd = (RptData) rptList.get(0);
							if (rd.getDataStatus() != null
									&& !"".equals(rd.getDataStatus())) {
								int datastatus = Integer.valueOf(
										rd.getDataStatus()).intValue();
								if (datastatus != DataUtil.YBS_STATUS_NUM
										&& datastatus != DataUtil.YSC_STATUS_NUM) {
									rr.setHasReject("1");
								}
							}
						}
					}
				}
			}
			if (StringUtil.isNotEmpty(receiveReport.getReceiveDate())) {
				receiveReport.setReceiveDate(DateUtils.toString(DateUtils
						.stringToDate(receiveReport.getReceiveDate(),
								DateUtils.ORA_DATE_FORMAT_SIMPLE),
						DateUtils.ORA_DATES_FORMAT));
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ReceiveReportAction-listReceiveReports2", e);
			return ERROR;
		}
	}

	public String viewReceiveReport() {
		try {
			receiveReport = receiveReportService
					.getReceiveReport(receiveReport);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ReceiveReportAction-viewReceiveReport", e);
			return ERROR;
		}
	}

	/**
	 * 删除反馈文件
	 */
	public String deleteReject() {
		try {
			if (CollectionUtil.isNotEmpty(ids1)) {
				receiveReportService.deleteReceiveReports(ids1);
			}
			listReceiveReports();
			if (StringUtil.equalsIgnoreCase(receiveReport.getInfoType(),
					"控制反馈信息")) {
				return "success1";
			}
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"删除", " 删除反馈报文", "1");
			return SUCCESS;
		} catch (Exception e) {
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"删除", " 删除反馈报文", "0");
			e.printStackTrace();
			log.error("ReceiveReportAction-deleteReject", e);
			return ERROR;
		}
	}

	/**
	 * 执行打回动作
	 */
	public String reject() {
		try {
			// 初始化参数配置
			this.initConfigParameters();
			if (CollectionUtil.isNotEmpty(ids)) {
				// 执行打回操作
				doDataLowerStatus();
			}
			// 刷新列表页面显示
			listReceiveReports();
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"打回", "打回反馈报文", "1");
			return SUCCESS;
		} catch (Exception e) {
			User user = this.getCurrentUser();
			logManagerService.writeLog(request, user, "0020.0030", "报文管理.反馈接收",
					"打回", "打回反馈报文", "0");
			e.printStackTrace();
			log.error("ReceiveReportAction-reject", e);
			return ERROR;
		}
	}

	private void doDataLowerStatus() {
		if (ids != null) {
			boolean isLinkXyRptData = false;// 是否联动打回下游报文数据
			if ("yes".equalsIgnoreCase(this.configLowerStatusLinkage)) {
				isLinkXyRptData = true;
			}
			for (int i = 0; i < ids.size(); i++) {
				String id = (String) ids.get(i);
				ReceiveReport receiveReport = new ReceiveReport();
				receiveReport.setId(Integer.valueOf(id).intValue());
				receiveReport = receiveReportService
						.getReceiveReport(receiveReport);
				String updateSql = " datastatus = "
						+ uploadModel.getResetDataStatus() + " ";
				String updateCondition = "";
				// /String infoTypeCode = DataUtil
				// / .getInfoTypeCodeByTableId(receiveReport.getTableId());
				// 更新报表自身状态
				RptData rptData = new RptData(receiveReport.getTableId(),
						updateSql, null, null, null, true);
				String keyRptNoColumnId = "";
				String byeRptNoColumnId = "";
				String keyRptNo = "";
				String byeRptNo = "";
				if (StringUtil.isNotEmpty(receiveReport.getDataNumber())
						&& StringUtil.isNotEmpty(receiveReport
								.getRptSendFileName())) {
					String fileType = receiveReport.getRptSendFileName()
							.substring(3, 5);
					keyRptNoColumnId = DataUtil
							.getRptNoColumnIdByFileType(fileType);
					byeRptNoColumnId = DataUtil
							.getByeRptNoColumnIdByFileType(fileType);
					if (receiveReport.getDataNumber().indexOf(",") > 0) {
						String[] sDataNumber = receiveReport.getDataNumber()
								.split(",");
						if (sDataNumber != null && sDataNumber.length == 2) {
							updateCondition = " " + keyRptNoColumnId + " = '"
									+ sDataNumber[0] + "' and "
									+ byeRptNoColumnId + " = '"
									+ sDataNumber[1] + "' ";
							keyRptNo = sDataNumber[0];
							byeRptNo = sDataNumber[1];
						}
					} else {
						updateCondition = " " + keyRptNoColumnId + " = '"
								+ receiveReport.getDataNumber() + "' ";
						keyRptNo = receiveReport.getDataNumber();
					}
					rptData.setUpdateCondition(updateCondition);
					rptData.setFileType(fileType);
				}
				// dataDealService.updateRptData(rptData);
				rptData.setReasionInfo(this.reasionInfo);
				if (StringUtil.isEmpty(rptData.getBusinessId())) {
					rptData.setRptNoColumnId(keyRptNoColumnId);
					rptData.setByeRptNoColumnId(byeRptNoColumnId);
					if (StringUtil.isNotEmpty(keyRptNo)) {
						rptData.setRptNo(keyRptNo);
					}
					if (StringUtil.isNotEmpty(byeRptNo)) {
						rptData.setByeRptNo(byeRptNo);
					}
					List rdReduces = null;
					rdReduces = dataDealService.findRptDataReduce(rptData);
					if (rdReduces != null && rdReduces.size() == 1) {
						RptData rdReduce = (RptData) rdReduces.get(0);
						rptData.setBusinessId(rdReduce.getBusinessId());
						rptData.setFileType(rdReduce.getFileType());
					}
				}
				// 打回的同时向t_lowerstatus_log数据打回记录表中添加信息
				dataDealService.updateRptDataForLowerStatus(rptData);
				if (StringUtil.isNotEmpty(receiveReport.getErrorMemo())) {
					rptData.setReasionInfo(receiveReport.getErrorMemo());
					dataDealService.updateRptData2(rptData, "3");
				}
				// 修改反馈信息,标记为已打回
				receiveReport.setHasReject("1");
				receiveReportService.updateReceiveReport(receiveReport);
				// 判断是否联动打回下游报文数据
				if (!isLinkXyRptData) {
					// 配置不执行联动打回，则循环下一条所选单据
					continue;
				}
				// 打回单据的业务ID
				String businessId = rptData.getBusinessId();
				String businessNo = null;
				// 对于存在下游报表的，更新所有直接和间接下游报表状态
				String xyFileType = (String) TableIdRela.getZjxyMap().get(
						rptData.getFileType());
				if (xyFileType != null) {
					businessNo = dataDealService.findBusinessNoByBusinessId(
							receiveReport.getTableId(), rptData.getFileType(),
							businessId);
				} else {
					continue;
				}
				// 为修改语句的条件重新赋值
				updateCondition = " filetype <> '" + rptData.getFileType()
						+ "' ";
				if (businessNo != null) {
					updateCondition += " and businessNo = '" + businessNo
							+ "' ";
				} else {
					continue;
				}
				// 如果是打回成1状态，则所有直接和间接下游报表状态也都得打回成1
				// if ("1".equals(uploadModel.getResetDataStatus())) {
				if (String.valueOf(DataUtil.WJY_STATUS_NUM).equals(
						uploadModel.getResetDataStatus())) {
					StringBuffer sql = new StringBuffer(" datastatus = "
							+ DataUtil.WJY_STATUS_NUM + " ");
					RptData upRptData = new RptData(receiveReport.getTableId(),
							sql.toString(), null, null, null, true);
					upRptData.setUpdateCondition(updateCondition);
					upRptData.setNotDataStatus(String
							.valueOf(DataUtil.YBS_STATUS_NUM)
							+ ","
							+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
							+ "," + String.valueOf(DataUtil.DELETE_STATUS_NUM));
					dataDealService.updateRptData(upRptData);
				}
				// 如果是打回成5状态，则所有状态为7的直接和间接下游报表状态也都得打回成5
				// else if ("5".equals(uploadModel.getResetDataStatus())) {
				else if (String.valueOf(DataUtil.SHYTG_STATUS_NUM).equals(
						uploadModel.getResetDataStatus())) {
					StringBuffer sql = new StringBuffer(" datastatus = "
							+ DataUtil.SHYTG_STATUS_NUM + " ");
					RptData upRptData = new RptData(receiveReport.getTableId(),
							sql.toString(), null, null, null, true);
					updateCondition += " and datastatus in (6,7) ";
					upRptData.setUpdateCondition(updateCondition);
					upRptData.setNotDataStatus(String
							.valueOf(DataUtil.YBS_STATUS_NUM)
							+ ","
							+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
							+ "," + String.valueOf(DataUtil.DELETE_STATUS_NUM));
					dataDealService.updateRptData(upRptData);
				}
				// DFHL:增加打回状态校验通过开始
				// else if ("3".equals(uploadModel.getResetDataStatus())) {
				else if (String.valueOf(DataUtil.JYYTG_STATUS_NUM).equals(
						uploadModel.getResetDataStatus())) {
					StringBuffer sql = new StringBuffer(" datastatus = "
							+ DataUtil.JYYTG_STATUS_NUM + " ");
					RptData upRptData = new RptData(receiveReport.getTableId(),
							sql.toString(), null, null, null, true);
					updateCondition += " and datastatus in (4,5,6,7) ";
					upRptData.setUpdateCondition(updateCondition);
					upRptData.setNotDataStatus(String
							.valueOf(DataUtil.YBS_STATUS_NUM)
							+ ","
							+ String.valueOf(DataUtil.LOCKED_STATUS_NUM)
							+ "," + String.valueOf(DataUtil.DELETE_STATUS_NUM));
					dataDealService.updateRptData(upRptData);
				}
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: saveRptLogInfoData|描述: 保存报送成功的报文记录进数据库
	 * </p>
	 * 
	 * @param rd
	 *            RptData对象
	 * @param userId
	 *            当前用户ID
	 * @param serverTime
	 *            当前系统时间
	 * @param rptColumnList
	 *            报文列信息
	 * @param subTableIds
	 *            报文子表ID列表 private void saveRptLogInfoData(RptData rd, String
	 *            userId, String serverTime, List rptColumnList, List
	 *            subTableIds){ try{ StringBuffer insertLogColumns = new
	 *            StringBuffer(); StringBuffer insertLogValues = new
	 *            StringBuffer(); String subId = rd.getSubId(); if(subId ==
	 *            null){ subId = ""; } insertLogColumns
	 *            .append("LOGTYPE,TABLEID,USERID,UPDATETIME,BUSINESSID,SUBID,DATASTATUS");
	 *            insertLogValues.append("'send','").append(rd.getTableId()).append(
	 *            "','").append(userId).append("','").append(serverTime)
	 *            .append("','").append(rd.getBusinessId()).append("','")
	 *            .append(subId).append("',").append(DataUtil.YBS_STATUS_NUM);
	 *            int cFlag = 0; for(Iterator i = rptColumnList.iterator();
	 *            i.hasNext();){ RptColumnInfo column = (RptColumnInfo)
	 *            i.next(); // 赋别名c1,c2,c3 column.setAliasColumnId("c" +
	 *            (++cFlag)); // 根据别名获取属性值 String columnValue = (String)
	 *            org.apache.commons.beanutils.BeanUtils .getProperty(rd,
	 *            column.getAliasColumnId()); if(columnValue != null){
	 *            if(column.getLogColumnId() != null &&
	 *            !"".equalsIgnoreCase(column.getLogColumnId())){
	 *            insertLogColumns.append(",").append( column.getLogColumnId());
	 *            insertLogValues.append(",'").append(
	 *            columnValue.replaceAll("'", "''")).append("'"); } }
	 *            if("table".equalsIgnoreCase(column.getDataType())){
	 *            subTableIds.add(column.getColumnId()); } } // 添加报文修改记录
	 *            dataDealService.saveRptData("t_rpt_log_info", insertLogColumns
	 *            .toString(), insertLogValues.toString()); }catch (Exception
	 *            e){ e.printStackTrace();
	 *            log.error("ReceiveReportAction-saveRptLogInfoData", e); } }
	 */
	private void initConfigParameters() {
		if ("yes".equalsIgnoreCase(this.configIsCluster)) {
			configMap = autoDealRptService.initConfigParameters();
		} else {
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache != null) {
				configMap = (Map) cache.get("configMap");
			} else {
				configMap = autoDealRptService.initConfigParameters();
			}
		}
		this.setConfigParametersBaseList(configMap);
	}

	// /private String initInterfaceVer(){
	// / this.initConfigParameters();
	// / String interfaceVer = (String) configMap.get("config.interface.ver");
	// / return interfaceVer;
	// /}
	private void initInstList() {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (authInstList == null) {
			authInstList = new ArrayList();
		}
		if (currentUser == null) {
			return;
		} else if (currentUser.getOrgs() != null) {
			authInstList.addAll(currentUser.getOrgs());
		}
		this.request.setAttribute("authInstList", authInstList);
	}

	public UploadModel getUploadModel() {
		return uploadModel;
	}

	public void setUploadModel(UploadModel uploadModel) {
		this.uploadModel = uploadModel;
	}

	public void setReceiveReportService(
			ReceiveReportService receiveReportService) {
		this.receiveReportService = receiveReportService;
	}

	public ReceiveReport getReceiveReport() {
		return receiveReport;
	}

	public void setReceiveReport(ReceiveReport receiveReport) {
		this.receiveReport = receiveReport;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public int getLargestColumnNum() {
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum) {
		this.largestColumnNum = largestColumnNum;
	}

	public Map getColumnInfoMap() {
		return columnInfoMap;
	}

	public void setColumnInfoMap(Map columnInfoMap) {
		this.columnInfoMap = columnInfoMap;
	}

	public Map getTableInfoMap() {
		return tableInfoMap;
	}

	public void setTableInfoMap(Map tableInfoMap) {
		this.tableInfoMap = tableInfoMap;
	}

	public AutoDealRptService getAutoDealRptService() {
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService) {
		this.autoDealRptService = autoDealRptService;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public String getPageFrom() {
		return pageFrom;
	}

	public void setPageFrom(String pageFrom) {
		this.pageFrom = pageFrom;
	}
}
