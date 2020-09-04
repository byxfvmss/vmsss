package com.cjit.gjsz.filem.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import cjit.crms.util.date.AppDate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CharacterEncoding;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.DocXmlUtil;
import com.cjit.common.util.ParameterBundle;
import com.cjit.common.util.StringNumFormat;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptSendCommit;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.InnerTableSign;
import com.cjit.gjsz.filem.model.t_org_config;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.filem.service.ReceiveTemplate;
import com.cjit.gjsz.filem.util.ZipUtil;
import com.cjit.gjsz.ftp.Ftp;
import com.cjit.gjsz.ftp.Sftp;
import com.cjit.gjsz.interfacemanager.model.KeyInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyService;
import com.cjit.gjsz.logic.impl.SelfDataVerify;
import com.cjit.gjsz.logic.model.Fal_A01Entity;
import com.cjit.gjsz.logic.model.Fal_A02Entity;
import com.cjit.gjsz.logic.model.Fal_B01Entity;
import com.cjit.gjsz.logic.model.Fal_B02Entity;
import com.cjit.gjsz.logic.model.Fal_B03Entity;
import com.cjit.gjsz.logic.model.Fal_B04Entity;
import com.cjit.gjsz.logic.model.Fal_B05Entity;
import com.cjit.gjsz.logic.model.Fal_B06Entity;
import com.cjit.gjsz.logic.model.Fal_C01Entity;
import com.cjit.gjsz.logic.model.Fal_D01Entity;
import com.cjit.gjsz.logic.model.Fal_D02Entity;
import com.cjit.gjsz.logic.model.Fal_D03Entity;
import com.cjit.gjsz.logic.model.Fal_D04Entity;
import com.cjit.gjsz.logic.model.Fal_D05Entity;
import com.cjit.gjsz.logic.model.Fal_D06Entity;
import com.cjit.gjsz.logic.model.Fal_D07Entity;
import com.cjit.gjsz.logic.model.Fal_D09Entity;
import com.cjit.gjsz.logic.model.Fal_E01Entity;
import com.cjit.gjsz.logic.model.Fal_F01Entity;
import com.cjit.gjsz.logic.model.Fal_G01Entity;
import com.cjit.gjsz.logic.model.Fal_G02Entity;
import com.cjit.gjsz.logic.model.Fal_H01Entity;
import com.cjit.gjsz.logic.model.Fal_H02Entity;
import com.cjit.gjsz.logic.model.Fal_I01Entity;
import com.cjit.gjsz.logic.model.Fal_I02Entity;
import com.cjit.gjsz.logic.model.Fal_I03Entity;
import com.cjit.gjsz.logic.model.Fal_X01Entity;
import com.cjit.gjsz.logic.model.Fal_Z01Entity;
import com.cjit.gjsz.logic.model.Fal_Z02Entity;
import com.cjit.gjsz.logic.model.Fal_Z03Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.Self_A_EXDEBT;
import com.cjit.gjsz.logic.model.Self_B_EXGUARAN;
import com.cjit.gjsz.logic.model.Self_C_DOFOEXLO;
import com.cjit.gjsz.logic.model.Self_D_LOUNEXGU;
import com.cjit.gjsz.logic.model.Self_E_EXPLRMBLO;
import com.cjit.gjsz.logic.model.Self_F_STRDE;
import com.cjit.gjsz.logic.model.VerifyModel;
import com.cjit.gjsz.system.model.Mts;
import com.cjit.gjsz.system.service.LogManagerService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class AutoDealRptServiceImpl extends GenericServiceImpl implements
		AutoDealRptService {

	protected transient Log log = LogFactory.getLog(this.getClass());
	private DataDealService dataDealService; //
	private OrgConfigeService orgconfigeservice; //
	private ReceiveReportService receiveReportService;//
	private UserInterfaceConfigService userInterfaceConfigService;//
	private SearchService searchService; //
	private SystemCache systemCache;
	private int largestColumnNum;
	private String sendFilePath;
	private VerifyService verifyService;
	private LogManagerService logManagerService;
	private static final String TOKEN_LOCK_FILE_NAME = "Token.lock";
	private static final String CONTROL_FILE = "controlFile";
	private static final String SYSTEM_CODE = "FAL";
	private static final String CONTROL_FILE_TYPE_CODE = "TT";
	private static final String SEND_FILE_SUFFIX = ".XML";
	private static final String APPTYPE = "FAL";
	private static final String IN = "IN";
	private static final String MSG_ELEMENT = "MSG";
	private static final String APPTYPE_ELEMENT = "APPTYPE";
	private static final String CURRENTFILE_ELEMENT = "CURRENTFILE";
	private static final String INOUT_ELEMENT = "INOUT";
	private static final String TOTALFILES_ELEMENT = "TOTALFILES";
	private static final String FILES_ELEMENT = "FILES";
	private static final String FILENAME_ELEMENT = "FILENAME";
	private static final String TOTALRECORDS_ELEMENT = "TOTALRECORDS";
	private static final String RECORDS_ELEMENT = "RECORDS";
	private static final String REC_ELEMENT = "REC";

	/**
	 * 暂时废弃 public String autoCreateReport(List rptTitleList) {
	 * log.info("开始执行自动生成报文"); String returnAutoCreate = ""; CacheabledMap cache =
	 * (CacheabledMap) CacheManager .getCacheObject("paramCache"); Map configMap =
	 * systemCache.getConfigMap(); String appFilePath = null; if
	 * (cache.get("realPath") != null) { appFilePath =
	 * StringUtils.removeEnd(cache.get("realPath") .toString(), File.separator) +
	 * File.separator + "data" + File.separator + "sendfile"; } else if
	 * (CacheabledMap.WEBAPP_PATH != null) { appFilePath =
	 * StringUtils.removeEnd(CacheabledMap.WEBAPP_PATH, File.separator) +
	 * File.separator + "data" + File.separator + "sendfile"; } else { return
	 * "realPathIsNull"; } // 查询所有已配置对照关系的机构 List orgConfigList = (List)
	 * orgconfigeservice.findOrgConfigList(null); // 可生成报文的机构 // instList<Object[]<String
	 * rptTitle,List rptNo,List orgId,List // orgNameList>> List instList =
	 * null; if (orgConfigList != null) { instList = new ArrayList(); String
	 * rptTitle = ""; List rptNoList = null; List orgIdList = null; List
	 * orgNameList = null; int i = 0; for (; i < orgConfigList.size(); i++) {
	 * t_org_config oc = (t_org_config) orgConfigList.get(i); String cOrgId =
	 * oc.getOrg_Id(); String cOrgName = oc.getName(); String cRptNo =
	 * oc.getRptNo(); String cRptTitle = oc.getRptTitle(); if
	 * (StringUtil.isEmpty(cRptNo) || StringUtil.isEmpty(cRptTitle) ||
	 * (rptTitleList != null && !rptTitleList .contains(cRptTitle))) { continue; }
	 * if (!rptTitle.equals(cRptTitle)) { if (!"".equals(rptTitle)) { Object[]
	 * objs = new Object[4]; objs[0] = rptTitle; objs[1] = rptNoList; objs[2] =
	 * orgIdList; objs[3] = orgNameList; instList.add(objs); } rptTitle =
	 * cRptTitle; rptNoList = new ArrayList(); orgIdList = new ArrayList();
	 * orgNameList = new ArrayList(); } rptNoList.add(cRptNo);
	 * orgIdList.add(cOrgId); orgNameList.add(cOrgName); } if (i ==
	 * orgConfigList.size()) { Object[] objs = new Object[4]; objs[0] =
	 * rptTitle; objs[1] = rptNoList; objs[2] = orgIdList; objs[3] =
	 * orgNameList; instList.add(objs); } } if (instList != null) { boolean
	 * createSendFileTokenLock = false; try { // 在工程SendFile目录中创建锁文件
	 * createSendFileTokenLock = this.makeLockFile(appFilePath,
	 * TOKEN_LOCK_FILE_NAME); if (!createSendFileTokenLock) { //
	 * 令牌文件已存在，暂时不能生成报送文件！ return "existsTokenLockAppFilePath"; } //
	 * 当前系统日期yyyyMMdd格式 String curDate = DateUtils.serverCurrentDate(); String
	 * notFileType = ""; String relatedFileType = null; String
	 * configFileGenOnlyEndOfMonth = null; String monthFiveDays = null; if
	 * (configMap != null && !configMap.isEmpty()) { relatedFileType = (String)
	 * configMap .get("config.related.filetype"); configFileGenOnlyEndOfMonth =
	 * (String) configMap .get("config.fileGen.onlyEndOfMonth"); monthFiveDays =
	 * (String) configMap .get("config.fileGen.month.fiveDays"); } //
	 * 判断是否配置了仅在月底生成报文的表单(20130731修改月度报文生成逻辑，将原先在月末最后一天生成改为每月第一天生成) if
	 * (StringUtil.isNotEmpty(configFileGenOnlyEndOfMonth)) { boolean
	 * canCreateMonthReport = true; if (!"yes".equals(monthFiveDays)) { int day =
	 * DateUtils.getDay(new Date()); // 判断当日是否为该月第一天 if (day != 1) { //
	 * 不是该月第一天，则不可生成月报报文 canCreateMonthReport = false; } } else { String
	 * yyyyMMdd = DateUtils .serverCurrentDate(DateUtils.ORA_DATE_FORMAT);
	 * boolean isTop5WorkingDay = this .belongToTopFiveWorkingDay(yyyyMMdd); //
	 * 判断当日是否早于该月前5个工作日 if (!isTop5WorkingDay) { // 当日晚于该月第五个工作日，不可生成月报报文
	 * canCreateMonthReport = false; } } if (!canCreateMonthReport) { String[]
	 * notFileTypes = configFileGenOnlyEndOfMonth .split(","); if (notFileTypes !=
	 * null && notFileTypes.length > 0) { for (int i = 0; i <
	 * notFileTypes.length; i++) { notFileType += "'" + notFileTypes[i] + "',"; }
	 * if (notFileType.length() > 0 && notFileType.endsWith(",")) { notFileType =
	 * notFileType.substring(0, notFileType.length() - 1); } } } } for (int j =
	 * 0; j < instList.size(); j++) { Object[] objs = (Object[])
	 * instList.get(j); String rptTitle = (String) objs[0]; List rptNoList =
	 * (List) objs[1]; List orgIdList = (List) objs[2]; List orgNameList =
	 * (List) objs[3]; List canCreatRptDateList = dataDealService
	 * .findUnsettledReport(null, null, notFileType,
	 * String.valueOf(DataUtil.SHYTG_STATUS_NUM), null, null, null, rptTitle);
	 * if (canCreatRptDateList == null || canCreatRptDateList.size() == 0) {
	 * continue; } // List rptTableInfos = dataDealService //
	 * .findRptTableInfo(new RptTableInfo(null, "1", "1")); //
	 * ------------------------------------------------------------------ //
	 * BusinessId和FileName对照 Map sendBussIdFileName = new LinkedHashMap(); //
	 * 用于存储报表对应的列信息的MAP:tableId->rptColumnInfoList Map tableColumnMap = new
	 * LinkedHashMap(); // 数据文件名列表集，生成控制文件内容时用 List dataFileNames = new
	 * ArrayList(); // zip打包文件名称 String zipFileName = null; // 包名 String
	 * packName = null; // 控制文件名 int serialNo =
	 * dataDealService.getSerialNo(CONTROL_FILE, null, rptTitle, curDate);
	 * String controlFileName = getControlFileName(curDate,
	 * getSerialNoString(serialNo), rptTitle); // 报送文件完整路径 String fileShortPath =
	 * controlFileName.substring(0, controlFileName.length() -
	 * SEND_FILE_SUFFIX.length()); String filePath = this.makeDir(sendFilePath,
	 * fileShortPath); if (filePath == null) {//
	 * D:\BANKDATA\SEND\BOPT22200000000010110401 // 报文生成路径不存在，生成失败！ return
	 * "notExistsFilePath"; } // 在报送文件路径下生成锁文件 if (!this.makeLockFile(filePath,
	 * TOKEN_LOCK_FILE_NAME)) { // 令牌文件已存在，暂时不能生成报送文件！ return
	 * "existsTokenLockSendFilePath"; } // 循环rptTableInfos，生成数据文件 for (Iterator
	 * i = canCreatRptDateList.iterator(); i .hasNext();) { RptData rdTableInfo =
	 * (RptData) i.next(); String tableId = rdTableInfo.getTableId(); String
	 * fileType = rdTableInfo.getFileType(); Map mapInstFileRptNo = new
	 * HashMap();// 用于存储当前单据机构最新申报号码 // 取tableId对应的信息 // 取报表物理表名tableId //
	 * 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存 List columns = dataDealService
	 * .findRptColumnInfo(new RptColumnInfo(tableId, null, "1", fileType)); int
	 * cFlag = 0; for (Iterator c = columns.iterator(); c.hasNext();) {
	 * RptColumnInfo column = (RptColumnInfo) c.next();
	 * column.setAliasColumnId("c" + (++cFlag)); } tableColumnMap.put(fileType,
	 * columns); List rptDatas = new ArrayList(); //
	 * 根据tableId和机构取数据(按businessIds过滤后的数据) if
	 * (!CollectionUtil.isEmpty(orgIdList)) { rptDatas =
	 * this.getRptDatas(tableId, fileType, orgIdList, relatedFileType,
	 * rptTitle); // 将记录状态置为99待生成 this.updateRptDataStatusByInstCodes(tableId,
	 * orgIdList, fileType, relatedFileType, rptTitle); } // 得到数据文件名 String
	 * dataFileName = null; if (CollectionUtil.isNotEmpty(rptDatas)) { serialNo =
	 * dataDealService.getSerialNo(fileType, null, rptTitle, curDate);
	 * dataFileName = getDataFileName(fileType, curDate,
	 * getSerialNoString(serialNo), rptTitle); dataFileNames.add(dataFileName); }
	 * else { continue; } // 生成数据文件 int recordSize =
	 * Integer.parseInt(ParameterBundle .getProperty("report.file.size",
	 * "5000")); int currentSize = rptDatas.size() > recordSize ? recordSize :
	 * rptDatas.size(); Document docRoot = getDataFileContent(dataFileName
	 * .substring(3, 5), currentSize); Element records =
	 * docRoot.getRootElement().element( RECORDS_ELEMENT); //
	 * 根据tableId从tableColumnMap中获取报表对应的columns columns = (ArrayList)
	 * tableColumnMap.get(fileType); // 循环报表数据 int index = 1, times = 0; for
	 * (Iterator k = rptDatas.iterator(); k.hasNext();) { if (index <=
	 * currentSize) { try { RptData rd = (RptData) k.next(); String rptNo = "";
	 * String orgName = ""; for (int c = 0; c < orgIdList.size(); c++) { String
	 * orgId = (String) orgIdList .get(c); if
	 * (rd.getInstCode().equalsIgnoreCase( orgId)) { rptNo = (String)
	 * rptNoList.get(c); orgName = (String) orgNameList .get(c);
	 * rd.setBranchCode(rptNo); rd.setBranchName(orgName); } }
	 * sendBussIdFileName.put(rd.getBusinessId(), dataFileName);
	 * addRecord(tableId, fileType, curDate, records, REC_ELEMENT, columns, rd,
	 * mapInstFileRptNo); } catch (Exception ex) { ex.printStackTrace(); //
	 * 机构对照申报号配置不正确，暂时不能生成报送文件！; return "errorRptNo"; } index++; } else { //
	 * 写数据文件 writeFile(filePath, dataFileName, docRoot); times++; currentSize =
	 * (rptDatas.size() - (currentSize * times)) > recordSize ? recordSize :
	 * (rptDatas.size() - (currentSize * times)); // 重新赋值 serialNo =
	 * dataDealService.getSerialNo( fileType, null, rptTitle, curDate);
	 * dataFileName = getDataFileName(fileType, curDate,
	 * getSerialNoString(serialNo), rptTitle); dataFileNames.add(dataFileName);
	 * docRoot = getDataFileContent(dataFileName .substring(3, 4), currentSize);
	 * records = docRoot.getRootElement().element( RECORDS_ELEMENT); index = 1; } } //
	 * 写数据文件 writeFile(filePath, dataFileName, docRoot); //
	 * 把sendFileMap中的数据文件名改成“目录+文件名”的形式 // value[3] = fileShortPath + "/" +
	 * dataFileName; packName = fileShortPath; resetRptDataStatus(tableId,
	 * fileType, rptDatas, packName, sendBussIdFileName, orgIdList,
	 * relatedFileType, rptTitle); } // 写控制文件 writeFile(filePath,
	 * controlFileName, getControlFileContent(dataFileNames)); //
	 * 把控制文件名改成“目录+文件名”的形式 controlFileName = fileShortPath + File.separator +
	 * controlFileName; // 生成zip压缩文件(仅包含XML文件) zipFileName = zipFile(filePath,
	 * fileShortPath); // 把存放报送文件的文件夹filePath整体拷贝到data\sendfile目录下
	 * this.copyFile(filePath, appFilePath); // 删除锁文件
	 * this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME); } returnAutoCreate =
	 * "success"; } catch (Exception ex) { ex.printStackTrace();
	 * log.error("AutoDealRptServiceImpl-autoCreateReport", ex);
	 * returnAutoCreate = "exception"; } finally { if (createSendFileTokenLock) {
	 * this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME); } } } return
	 * returnAutoCreate; }
	 */

	public String autoCreateReport(String rptTitle) {
		log.info("开始执行自动生成报文");
		String returnAutoCreate = "";
		CacheabledMap cache = (CacheabledMap) CacheManager
				.getCacheObject("paramCache");
		Map configMap = systemCache.getConfigMap();
		String appFilePath = null;
		if (cache.get("realPath") != null) {
			appFilePath = StringUtils.removeEnd(cache.get("realPath")
					.toString(), File.separator)
					+ File.separator + "data" + File.separator + "sendfile";
		} else if (CacheabledMap.WEBAPP_PATH != null) {
			appFilePath = StringUtils.removeEnd(CacheabledMap.WEBAPP_PATH,
					File.separator)
					+ File.separator + "data" + File.separator + "sendfile";
		} else {
			return "realPathIsNull";
		}
		// 查询所有已配置对照关系的机构
		List orgConfigList = (List) orgconfigeservice
				.findOrgConfigList(rptTitle);
		// 可生成报文的机构
		// instList<Object[]<String rptTitle,List rptNo,List orgId,List
		// orgNameList>>
		Object[] objs = new Object[4];
		if (orgConfigList != null) {
			List rptNoList = new ArrayList();
			List orgIdList = new ArrayList();
			List orgNameList = new ArrayList();
			for (int i = 0; i < orgConfigList.size(); i++) {
				t_org_config oc = (t_org_config) orgConfigList.get(i);
				String cRptNo = oc.getRptNo();
				if (StringUtil.isEmpty(cRptNo)) {
					continue;
				}
				String cOrgId = oc.getOrg_Id();
				String cOrgName = oc.getName();
				rptNoList.add(cRptNo);
				orgIdList.add(cOrgId);
				orgNameList.add(cOrgName);
			}
			objs[0] = rptTitle;
			objs[1] = rptNoList;
			objs[2] = orgIdList;
			objs[3] = orgNameList;
		}
		if (objs != null) {
			boolean createSendFileTokenLock = false;
			boolean existsDataToCreate = false;// 是否存在可供生成报文的数据
			try {
				// 在工程SendFile目录中创建锁文件
				createSendFileTokenLock = this.makeLockFile(appFilePath,
						TOKEN_LOCK_FILE_NAME);
				if (!createSendFileTokenLock) {
					// 令牌文件已存在，暂时不能生成报送文件！
					return "existsTokenLockAppFilePath";
				}
				// 当前系统日期yyyyMMdd格式
				String curDate = DateUtils.serverCurrentDate();
				String notFileType = "";
				String relatedFileType = null;
				String configFileGenOnlyEndOfMonth = null;
				String monthFiveDays = null;
				if (configMap != null && !configMap.isEmpty()) {
					relatedFileType = (String) configMap
							.get("config.related.filetype");
					configFileGenOnlyEndOfMonth = (String) configMap
							.get("config.fileGen.onlyEndOfMonth");
					monthFiveDays = (String) configMap
							.get("config.fileGen.month.fiveDays");
				}
				// 判断是否配置了仅在月底生成报文的表单(20130731修改月度报文生成逻辑，将原先在月末最后一天生成改为每月第一天生成)
				if (StringUtil.isNotEmpty(configFileGenOnlyEndOfMonth)) {
					boolean canCreateMonthReport = true;
					if (!"yes".equals(monthFiveDays)) {
						int day = DateUtils.getDay(new Date());
						// 判断当日是否为该月第一天
						if (day != 1) {
							// 不是该月第一天，则不可生成月报报文
							canCreateMonthReport = false;
						}
					} else {
						String yyyyMMdd = DateUtils
								.serverCurrentDate(DateUtils.ORA_DATE_FORMAT);
						boolean isTop5WorkingDay = this
								.belongToTopFiveWorkingDay(yyyyMMdd);
						// 判断当日是否早于该月前5个工作日
						if (!isTop5WorkingDay) {
							// 当日晚于该月第五个工作日，不可生成月报报文
							canCreateMonthReport = false;
						}
					}
					if (!canCreateMonthReport) {
						String[] notFileTypes = configFileGenOnlyEndOfMonth
								.split(",");
						if (notFileTypes != null && notFileTypes.length > 0) {
							for (int i = 0; i < notFileTypes.length; i++) {
								notFileType += "'" + notFileTypes[i] + "',";
							}
							if (notFileType.length() > 0
									&& notFileType.endsWith(",")) {
								notFileType = notFileType.substring(0,
										notFileType.length() - 1);
							}
						}
					}
				}
				List rptNoList = (List) objs[1];
				List orgIdList = (List) objs[2];
				List orgNameList = (List) objs[3];
				List canCreatRptDateList = dataDealService.findUnsettledReport(
						null, null, notFileType, String
								.valueOf(DataUtil.SHYTG_STATUS_NUM), null,
						null, null, rptTitle);
				if (canCreatRptDateList == null
						|| canCreatRptDateList.size() == 0) {
					return "noDataCanCreate";
				}
				// List rptTableInfos = dataDealService
				// .findRptTableInfo(new RptTableInfo(null, "1", "1"));
				// ------------------------------------------------------------------
				// BusinessId和FileName对照
				Map sendBussIdFileName = new LinkedHashMap();
				// 用于存储报表对应的列信息的MAP:tableId->rptColumnInfoList
				Map tableColumnMap = new LinkedHashMap();
				// 数据文件名列表集，生成控制文件内容时用
				List dataFileNames = new ArrayList();
				// zip打包文件名称
				String zipFileName = null;
				// 包名
				String packName = null;
				// 控制文件名
				int serialNo = dataDealService.getSerialNo(CONTROL_FILE, null,
						rptTitle, curDate);
				String controlFileName = getControlFileName(curDate,
						getSerialNoString(serialNo), rptTitle);
				// 报送文件完整路径
				String fileShortPath = controlFileName.substring(0,
						controlFileName.length() - SEND_FILE_SUFFIX.length());
				String filePath = this.makeDir(sendFilePath, fileShortPath);
				if (filePath == null) {// D:\BANKDATA\SEND\BOPT22200000000010110401
					// 报文生成路径不存在，生成失败！
					return "notExistsFilePath";
				}
				// 在报送文件路径下生成锁文件
				if (!this.makeLockFile(filePath, TOKEN_LOCK_FILE_NAME)) {
					// 令牌文件已存在，暂时不能生成报送文件！
					return "existsTokenLockSendFilePath";
				}
				// 循环rptTableInfos，生成数据文件
				for (Iterator i = canCreatRptDateList.iterator(); i.hasNext();) {
					RptData rdTableInfo = (RptData) i.next();
					String tableId = rdTableInfo.getTableId();
					String fileType = rdTableInfo.getFileType();
					Map mapInstFileRptNo = new HashMap();// 用于存储当前单据机构最新申报号码
					// 取tableId对应的信息
					// 取报表物理表名tableId
					// 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存
					List columns = dataDealService
							.findRptColumnInfo(new RptColumnInfo(tableId, null,
									"1", fileType));
					int cFlag = 0;
					for (Iterator c = columns.iterator(); c.hasNext();) {
						RptColumnInfo column = (RptColumnInfo) c.next();
						column.setAliasColumnId("c" + (++cFlag));
					}
					tableColumnMap.put(fileType, columns);
					List rptDatas = new ArrayList();
					// 根据tableId和机构取数据(按businessIds过滤后的数据)
					if (!CollectionUtil.isEmpty(orgIdList)) {
						rptDatas = this.getRptDatas(tableId, fileType,
								orgIdList, relatedFileType, rptTitle);
						// 将记录状态置为99待生成
						this.updateRptDataStatusByInstCodes(tableId, orgIdList,
								fileType, relatedFileType, rptTitle);
					}
					// 得到数据文件名
					String dataFileName = null;
					if (CollectionUtil.isNotEmpty(rptDatas)) {
						serialNo = dataDealService.getSerialNo(fileType, null,
								rptTitle, curDate);
						dataFileName = getDataFileName(fileType, curDate,
								getSerialNoString(serialNo), rptTitle);
						dataFileNames.add(dataFileName);
						existsDataToCreate = true;
					} else {
						continue;
					}
					// 生成数据文件
					int recordSize = Integer.parseInt(ParameterBundle
							.getProperty("report.file.size", "5000"));
					int currentSize = rptDatas.size() > recordSize ? recordSize
							: rptDatas.size();
					Document docRoot = getDataFileContent(dataFileName
							.substring(3, 5), currentSize);
					Element records = docRoot.getRootElement().element(
							RECORDS_ELEMENT);
					// 根据tableId从tableColumnMap中获取报表对应的columns
					columns = (ArrayList) tableColumnMap.get(fileType);
					// 循环报表数据
					int index = 1, times = 0;
					for (Iterator k = rptDatas.iterator(); k.hasNext();) {
						if (index <= currentSize) {
							try {
								RptData rd = (RptData) k.next();
								String rptNo = "";
								String orgName = "";
								for (int c = 0; c < orgIdList.size(); c++) {
									String orgId = (String) orgIdList.get(c);
									if (rd.getInstCode()
											.equalsIgnoreCase(orgId)) {
										rptNo = (String) rptNoList.get(c);
										orgName = (String) orgNameList.get(c);
										rd.setBranchCode(rptNo);
										rd.setBranchName(orgName);
									}
								}
								sendBussIdFileName.put(rd.getBusinessId(),
										dataFileName);
								addRecord(tableId, fileType, curDate, records,
										REC_ELEMENT, columns, rd,
										mapInstFileRptNo);
							} catch (Exception ex) {
								ex.printStackTrace();
								// 机构对照申报号配置不正确，暂时不能生成报送文件！;
								return "errorRptNo";
							}
							index++;
						} else {
							// 写数据文件
							writeFile(filePath, dataFileName, docRoot);
							times++;
							currentSize = (rptDatas.size() - (currentSize * times)) > recordSize ? recordSize
									: (rptDatas.size() - (currentSize * times));
							// 重新赋值
							serialNo = dataDealService.getSerialNo(fileType,
									null, rptTitle, curDate);
							dataFileName = getDataFileName(fileType, curDate,
									getSerialNoString(serialNo), rptTitle);
							dataFileNames.add(dataFileName);
							docRoot = getDataFileContent(dataFileName
									.substring(3, 4), currentSize);
							records = docRoot.getRootElement().element(
									RECORDS_ELEMENT);
							index = 1;
						}
					}
					// 写数据文件
					writeFile(filePath, dataFileName, docRoot);
					// 把sendFileMap中的数据文件名改成“目录+文件名”的形式
					// value[3] = fileShortPath + "/" + dataFileName;
					packName = fileShortPath;
					resetRptDataStatus(tableId, fileType, rptDatas, packName,
							sendBussIdFileName, orgIdList, relatedFileType,
							rptTitle);
				}
				if (existsDataToCreate) {
					// 写控制文件
					writeFile(filePath, controlFileName,
							getControlFileContent(dataFileNames));
					// 把控制文件名改成“目录+文件名”的形式
					controlFileName = fileShortPath + File.separator
							+ controlFileName;
					// 生成zip压缩文件(仅包含XML文件)
					zipFileName = zipFile(filePath, fileShortPath);
					// 把存放报送文件的文件夹filePath整体拷贝到data\sendfile目录下
					this.copyFile(filePath, appFilePath);
					// 删除锁文件
					this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
					returnAutoCreate = "success";
				} else {
					// 删除锁文件
					this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
					returnAutoCreate = "noDataCanCreate";
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("AutoDealRptServiceImpl-autoCreateReport", ex);
				returnAutoCreate = "exception";
			} finally {
				if (createSendFileTokenLock) {
					this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
				}
			}
		}
		return returnAutoCreate;
	}

	/**
	 * 过滤有关联关系的子表，如果他们的签约信息没有生成过报文，则这些数据也不能生成报文
	 * 
	 * @param newRecordlist
	 * @param fileType
	 * @param tableId
	 */
	private void removeRecordRelated(List newRecordlist, String tableId,
			String fileType) {
		List removeData = new ArrayList();
		for (Iterator it = newRecordlist.iterator(); it.hasNext();) {
			RptData rptData = (RptData) it.next();
			// 1.查询签约数据
			RptData data = dataDealService.findRptDataByRelatedBusinessId(
					tableId, fileType, rptData.getBusinessId());
			if (data == null) {
				// 查询不到对应签约信息
				removeData.add(rptData);
			} else {
				List sendList = dataDealService.findRptSendCommit(null, data
						.getBusinessId(), null, null, -1);
				// 签约信息未生成过报文
				if (sendList.size() == 0) {
					removeData.add(rptData);
				}
			}
		}
		newRecordlist.removeAll(removeData);
	}

	private List getRptDatas(String tableId, String fileType, List instCodes,
			String relatedFileType, String rptTitle) {
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
						fileType));
		StringBuffer columns = new StringBuffer();
		int cFlag = 0;
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			String columnId = column.getColumnId();
			// 取别名
			column.setAliasColumnId("c" + (++cFlag));
			// 拼查询SQL
			if ("table".equals(column.getDataType())) {
				columns.append(" '' as ").append(column.getAliasColumnId())
						.append(",");
			} else {
				columns.append("t.").append(columnId).append(" as ").append(
						column.getAliasColumnId()).append(",");
			}
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		columns.append("t.instcode").append(",");
		String searchCondition = getAddFileGenSearchConditonSQL(tableId,
				fileType, relatedFileType);
		String orderBy = null;
		if (StringUtil
				.isNotEmpty(DataUtil.getRptNoColumnIdByFileType(fileType))) {
			orderBy = "t." + DataUtil.getRptNoColumnIdByFileType(fileType)
					+ " asc ";
			if (StringUtil.isNotEmpty(DataUtil
					.getByeRptNoColumnIdByFileType(fileType))) {
				orderBy += ", t."
						+ DataUtil.getByeRptNoColumnIdByFileType(fileType)
						+ " asc ";
			}
		}
		List rdList = null;
		if (StringUtil.isNotEmpty(rptTitle)
				&& StringUtil.isNotEmpty(searchCondition)) {
			searchCondition += " and t.instCode in (select org_id from t_org_config where rpttitle = '"
					+ rptTitle + "' and rptNo is not null ) ";
			rdList = dataDealService.findRptDataByTableIdAndInstCodes(tableId,
					fileType, null, String.valueOf(DataUtil.SHYTG_STATUS_NUM),
					columns.toString().substring(0,
							columns.toString().length() - 1), searchCondition,
					orderBy);
		} else {
			rdList = dataDealService.findRptDataByTableIdAndInstCodes(tableId,
					fileType, instCodes, String
							.valueOf(DataUtil.SHYTG_STATUS_NUM), columns
							.toString().substring(0,
									columns.toString().length() - 1),
					searchCondition, orderBy);
		}
		return rdList;
	}

	private void updateRptDataStatusByInstCodes(String tableId, List instCodes,
			String fileType, String relatedFileType, String rptTitle) {
		String searchCondition = this.getAddFileGenSearchConditonSQL(tableId,
				fileType, relatedFileType);
		if (StringUtil.isNotEmpty(rptTitle)) {
			dataDealService.updateRptDataStatusByInstCodes(tableId,
					" t.datastatus = " + DataUtil.DSC_STATUS_NUM,
					DataUtil.SHYTG_STATUS_NUM, null, searchCondition, rptTitle);
		} else if (instCodes != null) {
			// 判断传入生成报文的机构ID个数
			if (instCodes.size() <= 500) {
				String[] instCodess = new String[instCodes.size()];
				instCodes.toArray(instCodess);
				// 当机构ID数小于等于500时，直接更新
				dataDealService.updateRptDataStatusByInstCodes(tableId,
						" t.datastatus = " + DataUtil.DSC_STATUS_NUM,
						DataUtil.SHYTG_STATUS_NUM, instCodess, searchCondition,
						null);
			} else if (instCodes.size() > 500) {
				// 当机构ID数大于500时，分段进行更新
				int i = 0;
				int nn = instCodes.size() / 500;
				for (; i < nn; i++) {
					List tempInstCodes = instCodes.subList(i * 500,
							(i + 1) * 500);
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + DataUtil.DSC_STATUS_NUM,
							DataUtil.SHYTG_STATUS_NUM, instCodess,
							searchCondition, null);
				}
				List tempInstCodes = instCodes.subList(i * 500, instCodes
						.size());
				if (tempInstCodes != null && tempInstCodes.size() > 0) {
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + DataUtil.DSC_STATUS_NUM,
							DataUtil.SHYTG_STATUS_NUM, instCodess,
							searchCondition, null);
				}
			}
		}
	}

	private String getAddFileGenSearchConditonSQL(String tableSelectId,
			String fileType, String relatedFileType) {
		String searchCondition = null;
		if (relatedFileType.indexOf(fileType) > -1) {
			// 当前添加的单据不是签约信息时，增加限制条件，要求对应签约信息曾经报送过
			searchCondition = " exists (select 1 from "
					+ tableSelectId
					+ " tc where tc.fileType <> '"
					+ fileType
					+ "' and t.businessNo = tc.businessNo "
					+ " and (exists (select 1 from t_rpt_send_commit s where s.tableID = '"
					+ tableSelectId
					+ "' and s.businessId = tc.businessId) or tc.datastatus > "
					+ DataUtil.SHYTG_STATUS_NUM + ")) and t.filetype = '"
					+ fileType + "' ";
		} else {
			// 当添加的单据是签约信息时
			// 当签约信息操作类型为删除时，要求不可存在未成功报送删除或逻辑删除的下游单据
			searchCondition = " ((not exists (select 1 from "
					+ tableSelectId
					+ " t2 where t2.businessno = t.businessno and t2.filetype <> '"
					+ fileType
					+ "' and ((t2.datastatus <> "
					+ DataUtil.YBS_STATUS_NUM
					+ " and t2.actiontype = 'D') or ((t2.actiontype <> 'D' or t2.actiontype is null) and t2.datastatus <> "
					+ DataUtil.DELETE_STATUS_NUM
					+ "))) and t.actiontype = 'D') or t.actiontype <> 'D') and t.filetype = '"
					+ fileType + "' ";
			if ("DA".equals(fileType)) {
				// 当前单据为境外担保项下境内贷款时 通过贷款币种是否为人民币，限制国内外汇贷款编号是否已存在
				searchCondition += " and ((t.CREDCURRCODE <> 'CNY' and t.DOFOEXLOCODE is not null) or t.CREDCURRCODE = 'CNY') ";
			}
		}
		return searchCondition;
	}

	private String getControlFileName(String curDate, String serialNo,
			String rptTitle) {
		return SYSTEM_CODE + CONTROL_FILE_TYPE_CODE + rptTitle + curDate
				+ serialNo + SEND_FILE_SUFFIX;
	}

	private String getSerialNoString(int serialNo) {
		if (0 <= serialNo && serialNo < 10) {
			return "0" + serialNo;
		} else if (10 <= serialNo && serialNo < 100) {
			return "" + serialNo;
		} else {
			return "";
		}
	}

	private String makeDir(String dirPath, String dirName) {
		String dirFullPath = dirPath + File.separator + dirName;
		File dir = new File(dirFullPath);
		// 文件夹已存在则直接删除所有子文件
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			try {
				dir.delete();
			} catch (Exception e) {
				return dirFullPath;
			}
		}
		// 不存在则创建文件夹
		if (dir.mkdirs()) {
			return dirFullPath;
		}
		return null;
	}

	private boolean makeLockFile(String filePath, String lockFileName)
			throws IOException {
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		if (lockFile.exists()) {
			return false;
		} else {
			return (lockFile.createNewFile());
		}
	}

	private String getDataFileName(String fileType, String curDate,
			String serialNo, String rptTitle) {
		// 根据tableId查找对应的类型代码（一位字母）
		return SYSTEM_CODE + fileType + rptTitle + curDate + serialNo
				+ SEND_FILE_SUFFIX;
	}

	private Document getDataFileContent(String currentFile, int totalRecords) {
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalRecordsElement = msgElement
				.addElement(TOTALRECORDS_ELEMENT);
		Element recordsElement = msgElement.addElement(RECORDS_ELEMENT);
		appTypeElement.setText(APPTYPE);
		currentFileElement.setText(APPTYPE + currentFile);
		inoutElement.setText(IN);
		totalRecordsElement.setText(String.valueOf(totalRecords));
		return root;
	}

	private Document getControlFileContent(List dataFileNames) {
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalFilesElement = msgElement.addElement(TOTALFILES_ELEMENT);
		Element filesElement = msgElement.addElement(FILES_ELEMENT);
		appTypeElement.setText(APPTYPE);
		currentFileElement.setText(APPTYPE + CONTROL_FILE_TYPE_CODE);
		inoutElement.setText(IN);
		totalFilesElement.setText(String.valueOf(dataFileNames.size()));
		for (int i = 0; i < dataFileNames.size(); i++) {
			Element fileNameElement = filesElement.addElement(FILENAME_ELEMENT);
			fileNameElement.setText((String) dataFileNames.get(i));
		}
		return root;
	}

	private Element addRecord(String tableId, String fileType, String curDate,
			Element records, String subElementName, List columns,
			RptData rptData, Map mapInstFileRptNo) throws Exception {
		// rec为代表本条数据的XML元素节点
		Element rec = records.addElement(subElementName);
		String actionType = rptData.getActionType();
		String instCode = rptData.getInstCode();
		String businessId = rptData.getBusinessId();
		String businessNo = null;
		String configRptNo = rptData.getConfigRptNo();// 记录所属机构配置的申报号码
		// 第一次循环数据列，获取客户类型cusType的值
		for (Iterator i = columns.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if (DataUtil.CFA_SELF_TABLE_D.equalsIgnoreCase(tableId)
					&& "DOFOEXLOCODE".equalsIgnoreCase(column.getColumnId()
							.trim())) {
				String dofoexloCode = BeanUtils.getProperty(rptData, column
						.getAliasColumnId());
				if (StringUtil.isEmpty(dofoexloCode)) {
					dofoexloCode = dataDealService.autoUpdateDofoecloCode(
							tableId, businessId);
					if (StringUtil.isNotEmpty(dofoexloCode)) {
						BeanUtils.setProperty(rptData, column
								.getAliasColumnId(), dofoexloCode);
					}
				}
			}
			if ("BUSINESSNO".equalsIgnoreCase(column.getColumnId().trim())) {
				businessNo = BeanUtils.getProperty(rptData, column
						.getAliasColumnId());
			}
		}
		// 第二次循环数据列，获取各列数据，生成XML节点
		for (Iterator i = columns.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			// 字段为内嵌表
			if ("4".equals(column.getTagType())) {
				String innerTableId = column.getColumnId();
				Element innerTableElementRoot = rec
						.addElement((String) InnerTableSign.getRootSignMap()
								.get(innerTableId));
				addInnerRecords(column.getColumnId(), businessId,
						innerTableElementRoot, (String) InnerTableSign
								.getSubSignMap().get(innerTableId));
				continue;
			}
			String data = null;
			if (("T_CFA_A_EXDEBT".equals(tableId) && "EXDEBTCODE"
					.equalsIgnoreCase(column.getColumnId()))
					|| ("T_CFA_B_EXGUARAN".equals(tableId) && "EXGUARANCODE"
							.equalsIgnoreCase(column.getColumnId()))
					|| ("T_CFA_C_DOFOEXLO".equals(tableId) && "DOFOEXLOCODE"
							.equalsIgnoreCase(column.getColumnId()))
					|| ("T_CFA_D_LOUNEXGU".equals(tableId) && "LOUNEXGUCODE"
							.equalsIgnoreCase(column.getColumnId()))
					|| ("T_CFA_E_EXPLRMBLO".equals(tableId) && "EXPLRMBLONO"
							.equalsIgnoreCase(column.getColumnId()))
					|| ("T_CFA_F_STRDE".equals(tableId) && "STRDECODE"
							.equalsIgnoreCase(column.getColumnId()))) {
				String rptCfaNoColumnId = column.getColumnId();
				String rptCfaNo = BeanUtils.getProperty(rptData, column
						.getAliasColumnId());
				// 申报号码为空，需生成申报号
				boolean createNewRptNo = false;
				if (StringUtil.isEmpty(rptCfaNo)
						|| "null".equals(rptCfaNo)
						|| StringUtil.isNotEmpty(SelfDataVerify.verifyRptNo(
								rptCfaNo, fileType))) {
					// 获取信息的交易日期
					String tradeDate = null;
					for (Iterator iterator = columns.iterator(); iterator
							.hasNext();) {
						RptColumnInfo column1 = (RptColumnInfo) iterator.next();
						// FIXME: 需确认各种报文对应的交易日期字段
						if ("TRADEDATE".equals(column1.getColumnId())) {
							tradeDate = BeanUtils.getProperty(rptData, column1
									.getAliasColumnId());
							break;
						}
					}
					if (tradeDate != null && tradeDate.length() == 8) {
						String key = DataUtil
								.getSelfBussTypeCodeByFileType(fileType)
								+ configRptNo + tradeDate;
						if (mapInstFileRptNo != null
								&& mapInstFileRptNo.containsKey(key)) {
							String currRptNo = (String) mapInstFileRptNo
									.get(key);
							if (StringUtil.isNotEmpty(currRptNo)
									&& currRptNo.length() == 28) {
								String lastNumber = currRptNo.substring(22, 28);
								int ints = Integer.valueOf(lastNumber)
										.intValue();
								if (ints >= 1 && ints < 999999) {
									int max = ints + 1;
									String finalChar = StringNumFormat
											.getFormatLong(max, "000000");
									data = key + finalChar;
									mapInstFileRptNo.put(key, data);
									createNewRptNo = true;
								}
							}
						} else {
							String tempTradeDate = tradeDate.substring(0, 4)
									+ "-" + tradeDate.substring(4, 6) + "-"
									+ tradeDate.substring(6, 8);
							Date tradedate = null;
							try {
								tradedate = DateUtils.stringToDate(
										tempTradeDate,
										DateUtils.ORA_DATES_FORMAT);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							try {
								// 生成申报号码
								data = userInterfaceConfigService
										.createAutokey(new KeyInfo(
												instCode,
												tradedate,
												tableId,
												DataUtil
														.getSelfBussTypeCodeByFileType(fileType),
												rptCfaNoColumnId, actionType));
								if (data == null) {
									// 超出日生成报文限制，未能生成申报号码
									records.remove(rec);
									return null;
								} else if ("ErrorOrgId".equals(data)) {
									// 单据所属机构对应申报号码或机构ID配置错误
									records.remove(rec);
									return null;
								}
								if (mapInstFileRptNo != null) {
									mapInstFileRptNo.put(key, data);
								}
								createNewRptNo = true;
							} catch (Exception e) {
								throw e;
							}
						}
					}
					if (StringUtil.isEmpty(data)
							&& TableIdRela.getZjxyMap().get(fileType) == null
							&& StringUtil.isNotEmpty(businessNo)) {
						// 根据业务编号查询上游报文的申报号码
						data = dataDealService.findRptNoByBusinessNo(
								rptCfaNoColumnId, tableId, businessNo);
						this.updateRptCfaNo(tableId, rptCfaNoColumnId, data,
								businessNo, businessId);
					}
					BeanUtils.setProperty(rptData, column.getAliasColumnId(),
							data);
				}
				// 生成报文时已存在申报号
				else {
					data = rptCfaNo;
				}
				if (createNewRptNo) {
					this.updateRptCfaNo(tableId, rptCfaNoColumnId, data,
							businessNo, businessId);
				}
			}
			// 判断当前字段是否为辅申报号字段（变动编号/履约编号/终止支付编号/付息编号）
			if (("CHANGENO".equalsIgnoreCase(column.getColumnId()) && ("AR"
					.equals(fileType)
					|| "AS".equals(fileType)
					|| "CB".equals(fileType)
					|| "DB".equals(fileType) || "EB".equals(fileType)))
					|| ("COMPLIANCENO".equalsIgnoreCase(column.getColumnId()) && "BC"
							.equals(fileType))
					|| ("TERPAYCODE".equalsIgnoreCase(column.getColumnId()) && "FB"
							.equals(fileType))
					|| ("INPAYCODE".equalsIgnoreCase(column.getColumnId()) && "FC"
							.equals(fileType))) {
				data = (String) BeanUtils.getProperty(rptData, column
						.getAliasColumnId());
				if (StringUtil.isEmpty(data)
						&& StringUtil.isNotEmpty(businessNo)
						&& StringUtil.isNotEmpty(businessId)) {
					data = dataDealService.findIndexCodeForSelf(tableId,
							fileType, column.getColumnId(), null, businessNo);
					dataDealService.updateRptData(new RptData(tableId, " "
							+ column.getColumnId() + " = '" + data + "' ",
							businessId, null, null, true));
				}
			}
			// 否则根据别名直接找到数据
			else {
				data = BeanUtils
						.getProperty(rptData, column.getAliasColumnId());
				// 当数据为带有小数位的数值类型，且data起始字符为小数点时，在其前面加个0
				if (column.getDataType().startsWith("n")) {
					if (data != null && !"".equals(data)) {
						if (data.startsWith(".")) {
							data = "0" + data;
						}
						String[] nDataTypes = column.getDataType().split(",");
						if (nDataTypes != null && nDataTypes.length == 4) {
							if (data.indexOf(".") < 0) {
								data += ".";
								for (int n = 0; n < Integer.valueOf(
										nDataTypes[3]).intValue(); n++) {
									data += "0";
								}
							} else {
								String[] datas = new String[2];
								datas[0] = data.substring(0, data.indexOf("."));
								datas[1] = data.substring(
										data.indexOf(".") + 1, data.length());
								for (int n = datas[1].length(); n < Integer
										.valueOf(nDataTypes[3]).intValue(); n++) {
									datas[1] += "0";
								}
								data = datas[0] + "." + datas[1];
							}
						}
					}
				}
			}
			// 去除多余字段，该字段不需要生成在报文中
			if ("TRADEDATE".equals(column.getColumnId())
					|| "BATCHNO".equals(column.getColumnId())
					|| "BUSINESSNO".equals(column.getColumnId())) {
				continue;
			}
			// 给rec添加子元素，元素名为列名
			Element e = rec.addElement(column.getColumnId());
			e.setText(data == null ? "" : DataUtil.replaceForXml(data));
		}
		return rec;
	}

	private void updateRptCfaNo(String tableId, String rptCfaNoColumnId,
			String rptCfaNo, String businessNo, String businessId) {
		if (StringUtil.isNotEmpty(businessNo)) {
			RptData upRptData = new RptData(tableId, " " + rptCfaNoColumnId
					+ " = '" + rptCfaNo + "' ", null, null, null, true);
			StringBuffer sbUpdateCondition = new StringBuffer();
			sbUpdateCondition.append(" BUSINESSNO = '").append(businessNo)
					.append("' and (").append(rptCfaNoColumnId).append(
							" is null or ").append(rptCfaNoColumnId).append(
							" = '') ");
			upRptData.setUpdateCondition(sbUpdateCondition.toString());
			dataDealService.updateRptData(upRptData);
		} else if (StringUtil.isNotEmpty(businessId)) {
			dataDealService.updateRptData(new RptData(tableId, " "
					+ rptCfaNoColumnId + " = '" + rptCfaNo + "' ", businessId,
					null, null, true));
		}
	}

	private void addInnerRecords(String tableId, String businessId,
			Element innerRoot, String innerSubName) {
		// 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableId, null, "1", "SUB"));
		int cFlag = 0;
		StringBuffer columns = new StringBuffer();
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			String columnId = column.getColumnId();
			// 取别名
			column.setAliasColumnId("c" + (++cFlag));
			// 拼查询SQL
			columns.append("t.").append(columnId).append(" as ").append(
					column.getAliasColumnId()).append(",");
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		List rptDatas = dataDealService.findInnerRptData(new RptData(tableId,
				columns.toString()
						.substring(0, columns.toString().length() - 1),
				businessId, null, null, null));
		if (StringUtil.equalsIgnoreCase(innerSubName, "REFNO")) {
			// 循环报表数据
			if (rptDatas.size() > 0) {
				for (Iterator k = rptDatas.iterator(); k.hasNext();) {
					RptData rptData = (RptData) k.next();
					Element elem = innerRoot.addElement(innerSubName);
					String val = rptData.getC1(); // c1代表 REFNO
					elem.setText(val);
				}
			} else {
				// 子表信息为空时，在报文中添加空标签
				Element elem = innerRoot.addElement(innerSubName);
				elem.setText("");
			}
		} else if (StringUtil.equalsIgnoreCase(innerSubName, "PROJECTS")) {
			// 循环报表数据
			for (Iterator k = rptDatas.iterator(); k.hasNext();) {
				RptData rptData = (RptData) k.next();
				Element elem = innerRoot.addElement("PROJECTNAME");
				String val = rptData.getC1();
				elem.setText(val);
			}
		} else {
			// 循环报表数据
			if (rptDatas.size() > 0) {
				for (Iterator k = rptDatas.iterator(); k.hasNext();) {
					try {
						addRecord(tableId, null, DateUtils.serverCurrentDate(),
								innerRoot, innerSubName, rptColumnList,
								(RptData) k.next(), null);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				try {
					addRecord(tableId, null, DateUtils.serverCurrentDate(),
							innerRoot, innerSubName, rptColumnList,
							new RptData(), null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private String zipFile(String zipScrPath, String fileName) {
		File file = new File(zipScrPath);
		if (file.exists()) {
			ZipUtil zu = new ZipUtil();
			String zipFileName = zipScrPath + ".zip";
			zu.zip(zipScrPath, zipFileName);
			String zipFileNameDesc = zipScrPath + File.separator + fileName
					+ ".zip";
			File srcFile = new File(zipFileName);
			try {
				FileUtil.copyFile(srcFile, new File(zipFileNameDesc));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				FileUtil.deleteContents(srcFile);
			}
			return zipFileNameDesc;
		}
		throw new RuntimeException("报文生成路径配置错误");
	}

	private boolean deleteLockFile(String filePath, String lockFileName) {
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		return lockFile.delete();
	}

	private void writeFile(String filePath, String fileName, Document document)
			throws Exception {
		String file = filePath + File.separator + fileName;
		// document到文件
		DocXmlUtil
				.fromDocumentToFile(document, file, CharacterEncoding.GB18030);
	}

	private void copyFile(String srcDir, String desDir) throws Exception {
		File src = new File(srcDir);
		File des = new File(desDir);
		File subDir = null;
		// 若src是目录，则先在des下建一个同名子目录,若已存在同名子目录，则先将子目录删除（先删除子目录下的所有文件，再删除子目录），再建子目录
		if (src.isDirectory()) {
			String srcDirName = src.getName();
			System.out.println("srcDirName" + srcDirName);
			String subDirFullName = this.makeDir(des.getAbsolutePath(),
					srcDirName, true);
			System.out.println("subDirFullName" + subDirFullName);
			if (subDirFullName == null) {
				throw new IOException();
			}
			subDir = new File(subDirFullName);
		}
		// 循环src下的所有文件，将每个文件都拷贝到subDir中(只拷贝文件，不拷贝下级目录)
		File[] files = src.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (!file.isDirectory()
					&& !TOKEN_LOCK_FILE_NAME.equalsIgnoreCase(file.getName())) {
				FileUtils.copyFileToDirectory(file, subDir);
			}
		}
	}

	private String makeDir(String dirPath, String dirName, boolean deleteFiles) {
		String dirFullPath = dirPath + File.separator + dirName;
		File dir = new File(dirFullPath);
		// 文件夹已存在则直接删除所有子文件
		if (dir.exists()) {
			if (deleteFiles) {
				File[] files = dir.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().toLowerCase().endsWith("txt")) {
						try {
							files[i].delete();
						} catch (SecurityException se) {
							se.printStackTrace();
						}
					}
				}
				try {
					// dir.delete();
				} catch (Exception e) {
					// return dirFullPath;
				}
			}
		} else {
			// 不存在则创建文件夹
			if (dir.mkdir()) {
				return dirFullPath;
			}
		}
		// 服务器目录下没有ABOQ文件夹时，能顺利通过；若存在则应返回路径全称
		// return null;
		return dirFullPath;
	}

	private void resetRptDataStatus(String tableId, String fileType,
			List rptDatas, String packName, Map sendBussIdFileName,
			List orgIdList, String relatedFileType, String rptTitle) {
		String businessId = null;
		String fileName = null;
		boolean batchUpdateStatus = true;
		String searchCondition = this.getAddFileGenSearchConditonSQL(tableId,
				fileType, relatedFileType);
		if (StringUtil.isNotEmpty(rptTitle)) {
			dataDealService.updateRptDataStatusByInstCodes(tableId,
					" t.datastatus = " + DataUtil.YSC_STATUS_NUM,
					DataUtil.DSC_STATUS_NUM, null, searchCondition, rptTitle);
			batchUpdateStatus = false;
		} else if (CollectionUtils.isNotEmpty(orgIdList)) {
			if (orgIdList.size() <= 500) {
				String[] instCodess = new String[orgIdList.size()];
				orgIdList.toArray(instCodess);
				// 当业务ID数小于等于500时，直接查询对应数据list
				dataDealService.updateRptDataStatusByInstCodes(tableId,
						" t.datastatus = " + DataUtil.YSC_STATUS_NUM,
						DataUtil.DSC_STATUS_NUM, instCodess, searchCondition,
						null);
			} else if (orgIdList.size() > 500) {
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int c = 0;
				int nn = orgIdList.size() / 500;
				for (; c < nn; c++) {
					List tempInstCodes = orgIdList.subList(c * 500,
							(c + 1) * 500);
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + DataUtil.YSC_STATUS_NUM,
							DataUtil.DSC_STATUS_NUM, instCodess,
							searchCondition, null);
				}
				List tempInstCodes = orgIdList.subList(c * 500, orgIdList
						.size());
				if (tempInstCodes != null && tempInstCodes.size() > 0) {
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + DataUtil.YSC_STATUS_NUM,
							DataUtil.DSC_STATUS_NUM, instCodess,
							searchCondition, null);
				}
			}
			batchUpdateStatus = false;
		}
		// 循环业务主键，重置状态
		List dataStatusList = new ArrayList();
		List insertSendList = new ArrayList();
		for (Iterator j = rptDatas.iterator(); j.hasNext();) {
			RptData rptData = (RptData) j.next();
			businessId = rptData.getBusinessId();
			fileName = (String) sendBussIdFileName.get(businessId);
			// 未上报过 记录状态:7-已生成
			if (batchUpdateStatus) {
				dataStatusList.add(new RptData(tableId, null, businessId, null,
						String.valueOf(DataUtil.YSC_STATUS_NUM), true));
			}
			// 插入send commit信息
			insertSendList.add(createSendMap(tableId, businessId, packName,
					fileName, 0));
			if (insertSendList.size() > DataUtil.BATCH_SIZE_MAX) {
				if (batchUpdateStatus) {
					dataDealService
							.updateRptDatastatus(tableId, dataStatusList);
					dataStatusList.clear();
				}
				dataDealService.insertRptSendCommitBatch(insertSendList);
				insertSendList.clear();
			}
			/*
			 * StringBuffer sbUpdateSQL = new StringBuffer(); // 未上报过 记录状态:7-已生成
			 * sbUpdateSQL.append(" datastatus = ")
			 * .append(DataUtil.YSC_STATUS_NUM).append(" ");
			 * dataDealService.updateRptData(new RptData(tableId, sbUpdateSQL
			 * .toString(), businessId, null, null, true)); // 添加报文报送记录
			 * dataDealService.insertRptSendCommit(tableId, businessId,
			 * packName, fileName, 0);
			 */
			// 如果当前生成报文的记录为"国内外汇贷款（含外债转贷款）信息"
			// 则根据businessno字段到"境外担保项下境内贷款"表中查询有无对应信息
			// 存在的话更新其DOFOEXLOCODE（国内外汇贷款编号）为当前生成的DOFOEXLOCODE值
			if (DataUtil.CFA_SELF_TABLE_C.equalsIgnoreCase(tableId)) {
				dataDealService.autoUpdateDofoecloCode(tableId, businessId);
			}
			// }
		}
		if (insertSendList.size() != 0) {
			if (batchUpdateStatus) {
				dataDealService.updateRptDatastatus(tableId, dataStatusList);
				dataStatusList.clear();
			}
			dataDealService.insertRptSendCommitBatch(insertSendList);
			insertSendList.clear();
		}
	}

	private Map createSendMap(String tableId2, String businessId,
			String packName, String fileName, int isReceive) {
		Map map = new HashMap();
		map.put("tableId", tableId2);
		map.put("businessId", businessId);
		map.put("packName", packName);
		map.put("fileName", fileName);
		map.put("isReceive", String.valueOf(isReceive));
		return map;
	}

	// ---------------------------------------------------------------------
	public String toReceive(ReceiveTemplate template, List rptTitleList,
			String rptTitle) {
		StringBuffer info = new StringBuffer();
		// 过滤主报告行权限
		Map configMtsMap = systemCache.getConfigMtsMap();
		String preFix = "";// 提示文字前缀
		String postFix = "";// 提示文字后缀
		if (StringUtils.isEmpty(rptTitle)) {
			// 用于反馈界面手工点击MTS接收按钮
			preFix = "";
			postFix = "!</br>";
		} else {
			// 用于后台自动执行MTS接收
			preFix = "[";
			postFix = "]";
		}
		for (Iterator it = configMtsMap.keySet().iterator(); it.hasNext();) {
			String mtsRptTitle = (String) it.next();
			if (StringUtils.isNotEmpty(rptTitle)) {
				if (!rptTitle.equals(mtsRptTitle)) {
					continue;
				}
			}
			if (rptTitleList != null && !rptTitleList.contains(mtsRptTitle)) {
				continue;
			}
			if (StringUtils.isEmpty(rptTitle)) {
				info.append("主报告行[").append(mtsRptTitle).append("]\"").append(
						template.getFunctionName()).append("\"");
			} else {
				info.append(template.getFunctionName());
			}
			Mts mts = (Mts) configMtsMap.get(mtsRptTitle);
			String result = template.recieve(mts);
			if (result.equals("ok")) {
				info.append(preFix).append("执行完成").append(postFix);
			} else if (result.equals("lock")) {
				info.append(preFix).append("对应的服务器存在控制文件,不执行接收文件").append(
						postFix);
			} else if (result.equals("connectException")) {
				info.append(preFix).append("连接异常").append(postFix);
			} else if (result.equals("pathNotExists")) {
				info.append(preFix).append("连接异常,未找到MTS对应路径").append(postFix);
			} else if (result.equals("realPathIsNull")) {
				info.append(preFix).append("出现异常,未找到系统路径").append(postFix);
			} else if (result.equals("error")) {
				info.append(preFix).append("出现异常").append(postFix);
			} else if (result.equals("disabled")) {
				info.append(preFix).append("功能未启用").append(postFix);
			} else if (result.equals("noFolder")) {
				info.append(preFix).append("尚无需要接收的反馈").append(postFix);
			}
		}
		return info.toString();
	}

	public String receiveReport(List rptTitleList, String rptTitle,
			String changeDataStatus, String configLowerStatusLinkage) {
		log.info("开始执行反馈文件接受功能!");
		ReceiveTemplate template = new ReceiveReport(this.receiveReportService,
				changeDataStatus, configLowerStatusLinkage);
		return toReceive(template, rptTitleList, rptTitle);
	}

	public String receiveErrorFiles(List rptTitleList, String rptTitle) {
		log.info("开始执行错误文件接受功能!");
		ReceiveTemplate template = new ReceiveErrorFiles(
				this.receiveReportService, this.dataDealService);
		return toReceive(template, rptTitleList, rptTitle);
	}

	public String receiveHistorySend(List rptTitleList, String rptTitle) {
		log.info("开始执行历史文件接受功能!");
		ReceiveTemplate template = new ReceiveHistorySend(
				this.receiveReportService);
		return toReceive(template, rptTitleList, rptTitle);
	}

	/**
	 * 取得相对于当前连接目录的某个目录下所有文件列表
	 * 
	 * @param path
	 *            ftpClient ftpRootPath
	 * @return List
	 * @throws FtpProtocolException 
	 */
	public List getFileListOfFTP(String path, FtpClient ftpClient) throws FtpProtocolException {
		List list = new ArrayList();
		DataInputStream dis;
		try {
			dis = new DataInputStream(ftpClient.nameList(path));
			String filename = "";
			while ((filename = dis.readLine()) != null) {
				list.add(filename);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private boolean doUpload(Mts mts, List fileList, String filePath)
			throws Exception {
		boolean uploadover = true;
		if (fileList.size() == 0)
			return true;
		// MTS SendFiles 目录中是否已含有锁文件
		boolean isLock = false;
		// 当MTS SendFiles目录中不存在锁文件，由当前程序创建后并执行完流程后，是否完成了清理工作
		boolean clearLock = true;
		ChannelSftp csftp = null;
		FTPClient ftpclient = null;
		if (mts.getLink().equals("sftp")) {
			try {
				if (!StringUtils.isEmpty(mts.getIdentity())) {
					// 使用密钥方式连接
					csftp = Sftp.connectByCert(mts.getIdentity(), mts
							.getPassphrase(), mts.getUserName(), mts.getIp(),
							Integer.parseInt(mts.getPort()));
				} else {
					// 不使用密钥
					csftp = Sftp.connect(mts.getUserName(), mts.getPassWord(),
							mts.getIp(), Integer.parseInt(mts.getPort()));
				}
				List ftpFileList = csftp.ls(mts.getSendPath());
				// 检查是否含有锁文件
				for (int i = 0; i < ftpFileList.size(); i++) {
					LsEntry le = (LsEntry) ftpFileList.get(i);
					if (le.getFilename().equals(TOKEN_LOCK_FILE_NAME)) {
						log.info("MTS SendFiles 目录中含有锁文件，不能上传");
						isLock = true;
						uploadover = false;
					}
				}
				log.info("向服务器写入锁文件");
				csftp.put(new FileInputStream(File.createTempFile("Token",
						"lock")), mts.getSendPath() + "/"
						+ TOKEN_LOCK_FILE_NAME);
				clearLock = false;
				for (int i = 0; i < fileList.size(); i++) {
					RptSendCommit sendDo = (RptSendCommit) fileList.get(i);
					if (this.uploadSFTPByFolder(mts.getSendPath(), filePath
							+ File.separator + sendDo.getPackName(), csftp)) {
						this.dataDealService.lockDatas(sendDo.getPackName());
					} else {
						uploadover = false;
					}
				}
			} catch (Exception e) {
				log.error("SFTP上传文件失败", e);
				e.printStackTrace();
				uploadover = false;
			} finally {
				try {
					if (!isLock) {
						String rmPath = mts.getSendPath() + "/"
								+ TOKEN_LOCK_FILE_NAME;
						csftp.rm(rmPath);
						clearLock = true;// 完成锁文件清理
					}
					if (csftp != null) {
						csftp.exit();
					}
				} catch (Exception e) {
					log.error("SFTP上传文件 删除锁文件 失败", e);
					e.printStackTrace();
				}
			}
		} else {
			ftpclient = new FTPClient();
			try {
				ftpclient.connect(mts.getIp(), Integer.parseInt(mts.getPort()));
				ftpclient.login(mts.getUserName(), mts.getPassWord());
				if (mts.getModel().equals("PASV")) {
					ftpclient.enterLocalPassiveMode();
				}
				// 是否存在SendPath路径
				boolean pathExists = Ftp.isPathExists(ftpclient, mts
						.getSendPath());
				if (!pathExists) {
					// FTP目录下不存在SendPath路径
					uploadover = false;
					isLock = true;
				} else {
					FTPFile[] sendPathFileList = ftpclient.listFiles(mts
							.getSendPath());
					// 检查锁文件
					for (int i = 0; i < sendPathFileList.length; i++) {
						if (sendPathFileList[i].getName().equals(
								TOKEN_LOCK_FILE_NAME)) {
							log.info("MTS SendFiles 目录中含有锁文件，不能上传");
							isLock = true;
							uploadover = false;
						}
					}
					log.info("向服务器写入锁文件");
					ftpclient.storeFile(mts.getSendPath() + "/"
							+ TOKEN_LOCK_FILE_NAME, new FileInputStream(File
							.createTempFile("Token", "lock")));
					clearLock = false;
					// 一次回话传输数据
					for (int i = 0; i < fileList.size(); i++) {
						RptSendCommit sendDo = (RptSendCommit) fileList.get(i);
						if (this.uploadFTPByFolder(mts.getSendPath(), filePath
								+ File.separator + sendDo.getPackName(),
								ftpclient)) {
							this.dataDealService
									.lockDatas(sendDo.getPackName());
						}
					}
				}
			} catch (Exception e) {
				log.error("FTP上传文件失败", e);
				e.printStackTrace();
				uploadover = false;
			} finally {
				try {
					if (!isLock) {
						ftpclient.deleteFile(mts.getSendPath() + "/"
								+ TOKEN_LOCK_FILE_NAME);
						clearLock = true;// 完成锁文件清理
					}
					ftpclient.quit();
				} catch (Exception e) {
					log.error("FTP上传文件 删除锁文件 失败", e);
					e.printStackTrace();
				}
			}
		}
		try {
			if (!clearLock && !isLock) {
				if (csftp != null) {
					String rmPath = mts.getSendPath() + "/"
							+ TOKEN_LOCK_FILE_NAME;
					csftp.rm(rmPath);
					clearLock = true;// 完成锁文件清理
				} else if (ftpclient != null) {
					ftpclient.deleteFile(mts.getSendPath() + "/"
							+ TOKEN_LOCK_FILE_NAME);
					clearLock = true;// 完成锁文件清理
				}
			}
		} catch (Exception e) {
			log.error("上传文件后清理锁文件出现异常", e);
			e.printStackTrace();
		}
		try {
			if (csftp != null) {
				csftp.exit();
			} else if (ftpclient != null) {
				ftpclient.quit();
			}
		} catch (Exception e) {
			log.error("doUpload上传文件后退出FTP/SFTP时出现异常", e);
			e.printStackTrace();
		}
		return uploadover;
	}

	/**
	 * MTS上传
	 * 
	 * @param sendList
	 *            发送文件过滤 List<String可发送报文包名称> 自动生成时此参数为空
	 * @param rptTitleList
	 *            主报告行过滤
	 * @param rptTitle
	 *            指定上传的主报告行
	 * @return error,ok,notAll...
	 */
	public String uploadReport(List sendList, List rptTitleList, String rptTitle) {
		log.info("开始上传mts文件");
		String filePath = null;// 报文存放路径
		String strReturn = "ok";
		try {
			int passNum = 0;
			Map mtsBeanMap = new HashMap();
			Map configMtsMap = systemCache.getConfigMtsMap();// 缓存mts配置列表
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache.get("realPath") != null) {
				filePath = StringUtils.removeEnd(cache.get("realPath")
						.toString(), File.separator)
						+ File.separator + "data" + File.separator + "sendfile";
			} else if (CacheabledMap.WEBAPP_PATH != null) {
				filePath = StringUtils.removeEnd(CacheabledMap.WEBAPP_PATH,
						File.separator)
						+ File.separator + "data" + File.separator + "sendfile";
			}
			if (filePath == null) {
				// 在20130628版中返回为error
				return "realPathIsNull";
			}
			// 获得待发送文件
			File file = new File(filePath);
			File[] subFile = file.listFiles();
			if (subFile != null)
				for (int i = 0; i < subFile.length; i++) {
					if (subFile[i].isDirectory()) {
						if (subFile[i].getName().startsWith(SYSTEM_CODE)
								&& subFile[i].getName().length() == 25) {
							String subPackName = subFile[i].getName();
							if (sendList != null
									&& !sendList.contains(subPackName)) {
								continue;
							}
							// modify by panshaobo 使用文件夹作为唯一标示需要上传的文件
							List list = dataDealService.findRptSendCommit(null,
									null, subPackName, null, -1);
							if (CollectionUtil.isNotEmpty(list)) {
								RptSendCommit sendDo = (RptSendCommit) list
										.get(0);
								if (StringUtils.isEmpty(sendDo.getIsSendMts())
										|| !sendDo.getIsSendMts().equals("1")) {
									String sendRptTitle = sendDo.getPackName()
											.substring(5, 17);
									if (StringUtil.isNotEmpty(rptTitle)) {
										if (!rptTitle.equals(sendRptTitle)) {
											continue;
										}
									}
									if (rptTitleList != null
											&& !rptTitleList
													.contains(sendRptTitle)) {
										continue;
									}
									if (mtsBeanMap.containsKey(sendRptTitle)) {
										((List) mtsBeanMap.get(sendRptTitle))
												.add(sendDo);
									} else {
										List mtsBeanList = new ArrayList();
										mtsBeanList.add(sendDo);
										mtsBeanMap.put(sendRptTitle,
												mtsBeanList);
									}
								}
							}
						}
					} else {
						if (subFile[i].getName().equals("Token.lock")) {
							return "lock";
						}
					}
				}// end for
			// 上锁
			File fileLS = new File(filePath + "\\Token.lock");
			fileLS.createNewFile();
			boolean mtsException = false;// 上传过程中是否出现异常
			boolean mtsNoConfigurate = false;// 是否存在未配置MTS参数的主报告行
			// 循环上传
			for (Iterator it = mtsBeanMap.keySet().iterator(); it.hasNext();) {
				String rptTitleMts = (String) it.next();
				if (!configMtsMap.containsKey(rptTitleMts)) {
					mtsNoConfigurate = true;
					continue;
				}
				if (rptTitle != null && !rptTitleMts.equalsIgnoreCase(rptTitle)) {
					// 指定上传的主报告行与当前主报告行不一致
					continue;
				}
				Mts mts = (Mts) configMtsMap.get(rptTitleMts);
				if (mts == null) {
					mtsNoConfigurate = true;
					continue;
				}
				List fileList = (ArrayList) mtsBeanMap.get(rptTitleMts);
				if (doUpload(mts, fileList, filePath)) {
					passNum += fileList.size();
				} else {
					mtsException = true;
				}
			}
			if (passNum == 0) {
				if (mtsNoConfigurate) {
					strReturn = "mtsNoConfigurate";
				} else if (mtsException) {
					strReturn = "mtsException";
				} else {
					strReturn = "noFileUpload";
				}
			} else if ((sendList != null && passNum == sendList.size())
					|| sendList == null) {
				strReturn = "ok";
			} else {
				strReturn = "notAll";
			}
		} catch (Exception e) {
			log.error("上传mts文件失败", e);
			strReturn = "error";
		} finally {
			try {
				File fileLS = new File(filePath + "\\Token.lock");
				if (fileLS.exists())
					fileLS.delete();
			} catch (Exception e) {
				log.warn("删除锁文件失败", e);
			}
		}
		return strReturn;
	}

	public void removeDirectory(String path, ChannelSftp sftpClient)
			throws IOException, SftpException {
		Vector ftpFileArr = null;
		try {
			ftpFileArr = sftpClient.ls(path);
		} catch (Exception e) {
			ftpFileArr = null;
		}
		if (ftpFileArr == null || ftpFileArr.size() == 0) {
			try {
				sftpClient.rmdir(path);
			} catch (Exception e) {
			}
			return;
		}
		for (int i = 0; i < ftpFileArr.size(); i++) {
			ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) ftpFileArr.get(i);
			if (entry.getAttrs().isDir()) {
				removeDirectory(path + "/" + entry.getFilename(), sftpClient);
			} else if (entry.getAttrs().isLink()) {
			} else {
				sftpClient.rm(path + "/" + entry.getFilename());
			}
		}
		sftpClient.rmdir(path);
	}

	public boolean removeDirectory(String path, FTPClient ftpClient)
			throws IOException {
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return ftpClient.removeDirectory(path);
		}
		for (int i = 0; i < ftpFileArr.length; i++) {
			String name = ftpFileArr[i].getName();
			if (ftpFileArr[i].isDirectory()) {
				if (!ftpFileArr[i].getName().equals(".")
						&& (!ftpFileArr[i].getName().equals(".."))) {
					removeDirectory(path + "/" + name, ftpClient);
				}
			} else if (ftpFileArr[i].isFile()) {
				ftpClient.deleteFile(path + "/" + name);
			} else if (ftpFileArr[i].isSymbolicLink()) {
			} else if (ftpFileArr[i].isUnknown()) {
			}
		}
		return ftpClient.removeDirectory(path);
	}

	public boolean uploadFTPByFolder(String remotefilename,
			String localfilename, FTPClient ftpclient) {
		try {
			// 进入待上传文件夹
			File packageFolder = new File(localfilename);
			String packageName = packageFolder.getName();
			String path = remotefilename + File.separator + packageName;
			// 建立待上传文件夹
			removeDirectory(path, ftpclient);
			ftpclient.mkd(path);
			// 循环上传文件
			File[] files = packageFolder.listFiles();
			if (files != null)
				for (int i = 0; i < files.length; i++)
					if (files[i].isFile()
							&& files[i].getName().toLowerCase()
									.endsWith(".xml")) {
						FileInputStream is = new FileInputStream(files[i]);
						ftpclient.storeFile(path + File.separator
								+ files[i].getName(), is);
					}
			return true;
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}

	/**
	 * <p>
	 * 方法名称: uploadSFTPByFolder|描述: SFTP方式上传报文
	 * </p>
	 * 
	 * @param directory
	 * @param uploadFile
	 * @param sftp
	 * @return boolean
	 */
	public boolean uploadSFTPByFolder(String directory, String uploadFile,
			ChannelSftp sftp) {
		try {
			// 进入待上传文件夹
			File packageFolder = new File(uploadFile);
			String packageName = packageFolder.getName();
			String path = directory + "/" + packageName + "/";
			// 建立待上传文件夹
			removeDirectory(path, sftp);
			sftp.mkdir(path);
			// 循环上传文件
			File[] files = packageFolder.listFiles();
			if (files != null)
				for (int i = 0; i < files.length; i++)
					if (files[i].isFile()
							&& files[i].getName().toLowerCase()
									.endsWith(".xml")) {
						sftp.put(new FileInputStream(files[i]), path + "/"
								+ files[i].getName());
					}
			return true;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			return false;
		}
	}

	public void autoCheckData() {
		log.info("开始执行自动校验");
		if ("yes".equalsIgnoreCase(this.configIsCluster)) {
			try {
				systemCache.registerParams();
			} catch (Exception ex) {
				log.error("自动校验刷新缓存异常", ex);
			}
		}
		// 获得报表数据
		log.info("获取报表结构");
		List rptTableInfo = dataDealService.findRptTableInfo(
				new RptTableInfo(), new String());
		ExecutorService executor = Executors.newFixedThreadPool(8);
		for (Iterator it = rptTableInfo.iterator(); it.hasNext();) {
			RptTableInfo tableInfo = (RptTableInfo) it.next();
			log.info("启动线程，执行" + tableInfo.getTableName() + "表的自动校验!");
			executor.execute(new Thread(new CheckData(tableInfo)));
		}// end for table
		executor.shutdown();// 不调用主线程会阻塞
		try {
			while (!executor.awaitTermination(30, TimeUnit.SECONDS))
				;
		} catch (InterruptedException e) {
			log.error(e);
		}
		log.info("执行自动校验操作完成");
	}

	public class CheckData implements Runnable {

		RptTableInfo tableInfo;

		public CheckData(RptTableInfo tableInfo) {
			this.tableInfo = tableInfo;
		}

		public void run() {
			String interfaceVer = (String) systemCache.getConfigMap().get(
					"config.interface.ver");
			String overleapCommit = (String) systemCache.getConfigMap().get(
					"config.overleap.commit");
			String overleapAudit = (String) systemCache.getConfigMap().get(
					"config.overleap.audit");
			if (tableInfo.getTableId().indexOf("_SUB_") > -1)
				return;// 子表随主表校验，不单独校验
			log.info("获取报表列结构并且组合列信息");
			List rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(
							tableInfo.getTableId(), null, "1", tableInfo
									.getFileType()));
			String searchSql = aliasColumnIdForSearchAndPutTo(rptColumnList);
			if (searchSql.toLowerCase().indexOf("modifyuser") < 0) {
				searchSql += ", modifyuser as modifyUser ";
			}
			if (!"1".equals(tableInfo.getFileType())) {
				searchSql += ", businessno as businessNo ";
			}
			log.info("获取未校验的数据");
			RptData condition = new RptData(tableInfo.getTableId(), searchSql,
					null, "datastatus='1'", null, null, null);
			condition.setFileType(tableInfo.getFileType());
			condition.setSearchCondition(" rownum <= 1000 ");
			List waitCheckData = dataDealService.findRptData(condition);
			while (waitCheckData.size() > 0) {
				// 循环体内基本参数
				List dataList = new ArrayList();
				// List logList=new ArrayList();
				for (Iterator itData = waitCheckData.iterator(); itData
						.hasNext();) {
					Object[] result = checkDataAndModifyStatus((RptData) itData
							.next(), tableInfo, rptColumnList, interfaceVer,
							overleapCommit, overleapAudit);
					if (result != null) {
						dataList.add(result[0]);
						// logList.add(result[1]);
						if (dataList.size() >= DataUtil.BATCH_SIZE_MAX) {
							log.info("执行批量更新");
							dataDealService.updateRptDatastatus(tableInfo
									.getTableId(), dataList);
							// logManagerService.insertBatch(logList);
							dataList.clear();
							// logList.clear();
						}
					}
				}// end for datas
				if (dataList.size() != 0) {
					log.info("执行批量更新");
					dataDealService.updateRptDatastatus(tableInfo.getTableId(),
							dataList);
					// logManagerService.insertBatch(logList);
					dataList.clear();
					// logList.clear();
				}
				waitCheckData = dataDealService.findRptData(condition);
			}
		}
	}

	private Object[] checkDataAndModifyStatus(RptData rptData,
			RptTableInfo rptTableInfo, List rptColumnList, String interfaceVer,
			String overleapCommit, String overleapAudit) {
		try {
			Object[] result = new Object[2];
			log.info("执行输入校验,业务ID:" + rptData.getBusinessId());
			if (dataDealService.validateData(rptColumnList, rptData, false)) {
				log.info("执行逻辑校验");
				String tableId = rptTableInfo.getTableId();
				boolean saveToMap = false; // 使用map方式传值
				Object reportData = null;
				// 根据tableId和数据构造响应的类
				/* FAL */
				if (StringUtil.equals("T_FAL_A01_1", tableId)
						|| StringUtil.equals("T_FAL_A01_2", tableId)) {
					reportData = new Fal_A01Entity();
				} else if (StringUtil.equals("T_FAL_A02_1", tableId)
						|| StringUtil.equals("T_FAL_A02_2", tableId)
						|| StringUtil.equals("T_FAL_A02_3", tableId)) {
					reportData = new Fal_A02Entity();
				} else if (StringUtil.equals("T_FAL_B01", tableId)) {
					reportData = new Fal_B01Entity();
				} else if (StringUtil.equals("T_FAL_B02", tableId)) {
					reportData = new Fal_B02Entity();
				} else if (StringUtil.equals("T_FAL_B03", tableId)) {
					reportData = new Fal_B03Entity();
				} else if (StringUtil.equals("T_FAL_B04", tableId)) {
					reportData = new Fal_B04Entity();
				} else if (StringUtil.equals("T_FAL_B05", tableId)) {
					reportData = new Fal_B05Entity();
				} else if (StringUtil.equals("T_FAL_B06", tableId)) {
					reportData = new Fal_B06Entity();
				} else if (StringUtil.equals("T_FAL_C01", tableId)) {
					reportData = new Fal_C01Entity();
				} else if (StringUtil.equals("T_FAL_D01", tableId)) {
					reportData = new Fal_D01Entity();
				} else if (StringUtil.equals("T_FAL_D02", tableId)) {
					reportData = new Fal_D02Entity();
				} else if (StringUtil.equals("T_FAL_D03", tableId)) {
					reportData = new Fal_D03Entity();
				} else if (StringUtil.equals("T_FAL_D04", tableId)) {
					reportData = new Fal_D04Entity();
				} else if (StringUtil.equals("T_FAL_D05_1", tableId)
						|| StringUtil.equals("T_FAL_D05_2", tableId)) {
					reportData = new Fal_D05Entity();
				} else if (StringUtil.equals("T_FAL_D06_1", tableId)) {
					reportData = new Fal_D06Entity();
				} else if (StringUtil.equals("T_FAL_D07", tableId)) {
					reportData = new Fal_D07Entity();
				} else if (StringUtil.equals("T_FAL_D09", tableId)) {
					reportData = new Fal_D09Entity();
				} else if (StringUtil.equals("T_FAL_E01", tableId)) {
					reportData = new Fal_E01Entity();
				} else if (StringUtil.equals("T_FAL_F01", tableId)) {
					reportData = new Fal_F01Entity();
				} else if (StringUtil.equals("T_FAL_G01", tableId)) {
					reportData = new Fal_G01Entity();
				} else if (StringUtil.equals("T_FAL_G02", tableId)) {
					reportData = new Fal_G02Entity();
				} else if (StringUtil.equals("T_FAL_H01", tableId)) {
					reportData = new Fal_H01Entity();
				} else if (StringUtil.equals("T_FAL_H02", tableId)) {
					reportData = new Fal_H02Entity();
				} else if (StringUtil.equals("T_FAL_I01", tableId)) {
					reportData = new Fal_I01Entity();
				} else if (StringUtil.equals("T_FAL_I02", tableId)) {
					reportData = new Fal_I02Entity();
				} else if (StringUtil.equals("T_FAL_I03", tableId)) {
					reportData = new Fal_I03Entity();
				} else if (StringUtil.equals("T_FAL_X01", tableId)) {
					reportData = new Fal_X01Entity();
				} else if (StringUtil.equals("T_FAL_Z01", tableId)) {
					reportData = new Fal_Z01Entity();
				} else if (StringUtil.equals("T_FAL_Z02", tableId)) {
					reportData = new Fal_Z02Entity();
				} else if (StringUtil.equals("T_FAL_Z03", tableId)) {
					reportData = new Fal_Z03Entity();
				}
				/* 银行自身业务信息 */
				if (StringUtil.equals("T_CFA_A_EXDEBT", tableId)) {
					reportData = new Self_A_EXDEBT();
				} else if (StringUtil.equals("T_CFA_B_EXGUARAN", tableId)) {
					reportData = new Self_B_EXGUARAN();
				} else if (StringUtil.equals("T_CFA_C_DOFOEXLO", tableId)) {
					reportData = new Self_C_DOFOEXLO();
				} else if (StringUtil.equals("T_CFA_D_LOUNEXGU", tableId)) {
					reportData = new Self_D_LOUNEXGU();
				} else if (StringUtil.equals("T_CFA_E_EXPLRMBLO", tableId)) {
					reportData = new Self_E_EXPLRMBLO();
				} else if (StringUtil.equals("T_CFA_F_STRDE", tableId)) {
					reportData = new Self_F_STRDE();
				}
				/* 银行代客业务 */
				else if (StringUtil.equals("T_CFA_QFII_ACCOUNT", tableId)
						|| StringUtil.equals("T_CFA_QFII_ASSETS_DEBT", tableId)
						|| StringUtil.equals("T_CFA_QFII_ASSETS_DEBT_MONTH",
								tableId)
						|| StringUtil.equals("T_CFA_QFII_CHANGES", tableId)
						|| StringUtil.equals("T_CFA_QFII_CHANGES_SPECIAL",
								tableId)
						|| StringUtil.equals("T_CFA_QFII_PROFIT_LOSS", tableId)
						|| StringUtil.equals("T_CFA_QFII_REMIT", tableId)
						|| StringUtil.equals("T_CFA_QDII_ACCOUNT", tableId)
						|| StringUtil.equals("T_CFA_QDII_INVEST", tableId)
						|| StringUtil.equals("T_CFA_QDII_REMIT", tableId)
						|| StringUtil.equals("T_CFA_QDII_TRUSTEE_ACCOUNT",
								tableId)
						|| StringUtil.equals("T_CFA_BESTIR_ACCOUNT_CLOESD",
								tableId)
						|| StringUtil.equals("T_CFA_BESTIR_CHANGES", tableId)
						|| StringUtil
								.equals("T_CFA_RQFII_ASSETS_DEBT", tableId)
						|| StringUtil.equals("T_CFA_RQFII_ASSETS_DEBT_MONTH",
								tableId)
						|| StringUtil.equals("T_CFA_RQFII_CHANGES", tableId)
						|| StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND",
								tableId)
						|| StringUtil.equals("T_CFA_RQFII_INCOME_EXPEND_BUY",
								tableId)
						|| StringUtil
								.equals("T_CFA_RQFII_PROFIT_LOSS", tableId)) {
					saveToMap = true;
				}
				if (saveToMap) {
					Map mapData = new HashMap();
					for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
						RptColumnInfo column = (RptColumnInfo) i.next();
						if ("table".equals(column.getDataType())) {
							continue;
						}
						String value = BeanUtils.getProperty(rptData, column
								.getAliasColumnId());
						mapData.put(column.getColumnId(), value);
					}
					mapData.put("businessid", rptData.getBusinessId());
					reportData = mapData;
				} else {
					for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
						RptColumnInfo column = (RptColumnInfo) i.next();
						if ("table".equals(column.getDataType())) {
							continue;
						}
						if ("".equals(BeanUtils.getProperty(rptData, column
								.getAliasColumnId()))) {
							continue;
						} else {
							if (BeanUtils.getProperty(rptData, column
									.getAliasColumnId()) != null) {
								BeanUtils.setProperty(reportData, (column
										.getColumnId().toLowerCase()),
										BeanUtils.getProperty(rptData, column
												.getAliasColumnId()));
							}
						}
					}
					BeanUtils.setProperty(reportData, "businessid", rptData
							.getBusinessId());
					BeanUtils.setProperty(reportData, "filetype", rptData
							.getFileType());
				}// end save
				// end
				// 根据报文是否曾经报送，校验操作类型
				String verifyActionTypeMessage = null;
				List listSendCommit = dataDealService.findRptSendCommit(
						tableId, rptData.getBusinessId(), null, null, 1);
				if (listSendCommit != null && listSendCommit.size() == 1) {
					RptSendCommit rsc = (RptSendCommit) listSendCommit.get(0);
					if (rsc != null) {
						String actionType = BeanUtils.getProperty(reportData,
								"actiontype");
						// 该报文曾报送并反馈无误,则操作类型不能是A-新建
						if ("A".equals(actionType)) {
							verifyActionTypeMessage = "[操作类型]不能是新建。";
						}
					}
				}
				// 判断是否需要创建变动编号类辅申报号码是否需要创建
				String indexCode = null;
				String indexNoColumn = null;
				if ("AR".equals(rptData.getFileType())
						|| "AS".equals(rptData.getFileType())
						|| "CB".equals(rptData.getFileType())
						|| "DB".equals(rptData.getFileType())
						|| "EB".equals(rptData.getFileType())) {
					// 变动编号
					indexCode = BeanUtils.getProperty(reportData, "changeno");
					indexNoColumn = "changeno";
				} else if ("BC".equals(rptData.getFileType())) {
					// 履约编号
					indexCode = BeanUtils.getProperty(reportData,
							"complianceno");
					indexNoColumn = "complianceno";
				} else if ("FB".equals(rptData.getFileType())) {
					// 终止支付编号
					indexCode = BeanUtils.getProperty(reportData, "terpaycode");
					indexNoColumn = "terpaycode";
				} else if ("FC".equals(rptData.getFileType())) {
					// 付息编号
					indexCode = BeanUtils.getProperty(reportData, "inpaycode");
					indexNoColumn = "inpaycode";
				}
				// 如果所校验变动类单据的变动编号为空，则先为其赋值
				if (StringUtil.isEmpty(indexCode)
						&& StringUtil.isNotEmpty(indexNoColumn)) {
					String businessNo = BeanUtils.getProperty(reportData,
							"businessno");
					if (StringUtil.isNotEmpty(businessNo)) {
						indexCode = dataDealService.findIndexCodeForSelf(
								tableId, rptData.getFileType(), indexNoColumn,
								null, businessNo);
						dataDealService.updateRptData(new RptData(tableId, " "
								+ indexNoColumn + " = '" + indexCode + "' ",
								rptData.getBusinessId(), null, null, true));
					}
				}
				// 以下程序获取逻辑校验结果，更新rptColumnList,并根据校验结果更新数据状态
				// 校验结果
				VerifyModel vm = verifyService.verify(reportData, tableId,
						rptData.getInstCode(), interfaceVer, configIsCluster);
				// 主表校验结果
				Map mainResult = null;
				List innerResults = null;
				if (vm != null) {
					mainResult = vm.getFatcher();
					innerResults = vm.getChildren();
				}
				// 若操作类型不正确，则将校验结果补充进主表校验结果中
				if (verifyActionTypeMessage != null) {
					if (mainResult == null) {
						mainResult = new HashMap();
					}
					mainResult.put("ACTIONTYPE", verifyActionTypeMessage);
				}
				if ((mainResult != null && mainResult.size() != 0)
						|| (innerResults != null && innerResults.size() != 0)) {
					rptData.setDataStatus(String
							.valueOf(DataUtil.JYWTG_STATUS_NUM));
					result[1] = logManagerService.getLog("system", "system",
							"系统", rptData.getInstCode(), null, null, "自动校验",
							null, null, null, new Date(), "针对[机构："
									+ rptData.getInstCode() + "，单据："
									+ rptTableInfo.getTableName() + "，业务号："
									+ rptData.getBusinessId() + "]执行数据逻辑校验操作",
							"0");
				} else {
					if ("yes".equalsIgnoreCase(overleapAudit)) {
						// 忽略审核操作
						rptData.setDataStatus(String
								.valueOf(DataUtil.SHYTG_STATUS_NUM));
						if (StringUtil.isEmpty(rptData.getModifyUser())) {
							rptData.setModifyUser("system");
						}
						if ((TableIdRela.getZjxyMap()
								.get(rptData.getFileType()) != null || "FD"
								.equals(rptData.getFileType()))
								&& StringUtil.isEmpty(rptData.getBusinessNo())) {
							rptData.setBusinessNo(rptData.getBusinessId());
						}
					} else if ("yes".equalsIgnoreCase(overleapCommit)) {
						// 忽略提交操作
						rptData.setDataStatus(String
								.valueOf(DataUtil.YTJDSH_STATUS_NUM));
						if (StringUtil.isEmpty(rptData.getModifyUser())) {
							rptData.setModifyUser("system");
						}
					} else {
						rptData.setDataStatus(String
								.valueOf(DataUtil.JYYTG_STATUS_NUM));
					}
					result[1] = logManagerService.getLog("system", "system",
							"系统", rptData.getInstCode(), null, null, "自动校验",
							null, null, null, new Date(), "针对[机构："
									+ rptData.getInstCode() + "，单据："
									+ rptTableInfo.getTableName() + "，业务号："
									+ rptData.getBusinessId() + "]执行数据逻辑校验操作",
							"1");
				}
			} else {
				rptData
						.setDataStatus(String
								.valueOf(DataUtil.JYWTG_STATUS_NUM));
				result[1] = logManagerService.getLog("system", "system", "系统",
						rptData.getInstCode(), null, null, "自动校验", null, null,
						null, new Date(), "针对[机构：" + rptData.getInstCode()
								+ "，单据：" + rptTableInfo.getTableName()
								+ "，业务号：" + rptData.getBusinessId()
								+ "]执行数据逻辑校验操作", "0");
			}
			result[0] = rptData;
			return result;
		} catch (Exception e) {
			log.error("自动校验异常", e);
			return null;
		}
	}

	/**
	 * 1.拼装别名的查询字段sql用于赋值给rptData 2.将rptColumnList中的column配置给对应的别名
	 * 
	 * @param rptColumnList
	 * @return
	 */
	private String aliasColumnIdForSearchAndPutTo(List rptColumnList) {
		StringBuffer columns = new StringBuffer();
		int cFlag = 0;
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if ("table".equals(column.getDataType())) {
				continue;
			}
			column.setAliasColumnId("c" + (++cFlag));
			columns.append("t.").append(column.getColumnId()).append(" as ")
					.append(column.getAliasColumnId()).append(",");
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		return StringUtils.removeEnd(columns.toString(), ",");
	}

	public boolean isOnSchedule(String runTime, AppDate ad) {
		String[] runTimes = null;
		if (StringUtils.isNotEmpty(runTime)) {
			if (runTime.indexOf(";") >= 0) {
				runTimes = runTime.split(";");
			} else {
				runTimes = new String[] { runTime };
			}
		} else
			return false;
		for (int i = 0; i < runTimes.length; i++) {
			if (StringUtil.isNotEmpty(runTimes[i])) {
				int hourApp = Integer.parseInt(runTimes[i].substring(0,
						runTimes[i].indexOf(":")));
				int minuteApp = Integer.parseInt(runTimes[i]
						.substring(runTimes[i].indexOf(":") + 1));
				int minuteNow = ad.getMinute();
				int hourNow = ad.getHour();
				if (hourApp == hourNow && minuteApp == minuteNow) {
					return true;
				}
			}
		}
		// 没找到对应的时间节点，返回否
		return false;
	}

	public boolean isGetResource(Date now, String taskType, String rptTitle) {
		String task_id = DateUtils.toString(now, "yyyyMMddHHmm");
		Map params = new HashMap();
		params.put("id", task_id);
		params.put("type", taskType);
		params.put("rptTitle", rptTitle);
		try {
			this.save("getResource", params);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void updateResource(Date now, String taskType, Date endDate,
			String step, String taskInfo, String rptTitle) {
		String task_id = DateUtils.toString(now, "yyyyMMddHHmm");
		String task_end = DateUtils.toString(endDate, "yyyyMMddHHmm");
		Map params = new HashMap();
		params.put("id", task_id);
		params.put("type", taskType);
		params.put("end_date", task_end);
		params.put("step", step);
		params.put("info", taskInfo);
		params.put("rptTitle", rptTitle);
		this.update("updateResource", params);
	}

	// ---------------------------------------------------------------------
	public String[] findRptTitles(String userId, String instCode) {
		String[] rptTitles = null;
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("orgId", instCode);
		List list = find("findRptTitles", map);
		if (CollectionUtil.isNotEmpty(list)) {
			rptTitles = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				rptTitles[i] = (String) list.get(i);
			}
		}
		return rptTitles;
	}

	public Object getDataVerifyModel(String tableId, String businessid) {
		Map map = new HashMap();
		map.put("ids", "'" + businessid + "'");
		List list = find((String) SearchModel.SEARCH_MAP.get(tableId), map);
		if (CollectionUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	// ---------------------------------------------------------------------
	public DataDealService getDataDealService() {
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public OrgConfigeService getOrgconfigeservice() {
		return orgconfigeservice;
	}

	public void setOrgconfigeservice(OrgConfigeService orgconfigeservice) {
		this.orgconfigeservice = orgconfigeservice;
	}

	public int getLargestColumnNum() {
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum) {
		this.largestColumnNum = largestColumnNum;
	}

	public String getSendFilePath() {
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath) {
		this.sendFilePath = sendFilePath;
	}

	public ReceiveReportService getReceiveReportService() {
		return receiveReportService;
	}

	public void setReceiveReportService(
			ReceiveReportService receiveReportService) {
		this.receiveReportService = receiveReportService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public SearchService getSearchService() {
		return searchService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public SystemCache getSystemCache() {
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}

	public void saveRptLogInfoData(RptData rd, String logType, String userId,
			String serverTime, List rptColumnList, List subTableIds) {
		receiveReportService.saveRptLogInfoData(rd, logType, userId,
				serverTime, rptColumnList, subTableIds);
	}

	public VerifyService getVerifyService() {
		return verifyService;
	}

	public void setVerifyService(VerifyService verifyService) {
		this.verifyService = verifyService;
	}

	public LogManagerService getLogManagerService() {
		return logManagerService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	/*
	 * 为了预校验的上传处理
	 * 
	 */
	public String uploadReport4Pre(List sendList, List rptTitleList,
			String previewFilePath) {
		log.info("开始上传mts文件");
		String filePath = null;// 报文存放路径
		try {
			// 筛选
			int passNum = 0;
			Map mtsBeanMap = new HashMap();
			Map configMtsMap = systemCache.getConfigMtsMap();// 缓存mts配置列表
			// CacheabledMap cache = (CacheabledMap) CacheManager
			// .getCacheObject("paramCache");
			// if(cache.get("realPath") != null){
			// ---modify by gaole
			// filePath = StringUtils.removeEnd(cache.get("realPath")
			// .toString(), File.separator)
			// + File.separator + "data" + File.separator + "sendfile";
			// --下面是我修改的
			filePath = StringUtils.removeEnd(previewFilePath, File.separator)
					+ File.separator + "data" + File.separator + "sendfile";
			// }
			if (filePath == null)
				return "error";
			// 获得待发送文件
			File file = new File(filePath);
			File[] subFile = file.listFiles();
			if (subFile != null)
				for (int i = 0; i < subFile.length; i++) {
					if (subFile[i].isDirectory()) {
						if (subFile[i].getName().startsWith(SYSTEM_CODE)
								&& subFile[i].getName().length() == 25) {
							String subPackName = subFile[i].getName();
							if (sendList != null
									&& !sendList.contains(subPackName)) {
								continue;
							}
							// modify by panshaobo 使用文件夹作为唯一标示需要上传的文件
							List list = dataDealService.findRptSendCommit(null,
									null, subPackName, null, -1);
							if (CollectionUtil.isNotEmpty(list)) {
								RptSendCommit sendDo = (RptSendCommit) list
										.get(0);
								if (StringUtils.isEmpty(sendDo.getIsSendMts())
										|| !sendDo.getIsSendMts().equals("1")) {
									String rptTitle = sendDo.getPackName()
											.substring(5, 17);
									if (rptTitleList != null
											&& !rptTitleList.contains(rptTitle)) {
										continue;
									}
									if (mtsBeanMap.containsKey(rptTitle)) {
										((List) mtsBeanMap.get(rptTitle))
												.add(sendDo);
									} else {
										List mtsBeanList = new ArrayList();
										mtsBeanList.add(sendDo);
										mtsBeanMap.put(rptTitle, mtsBeanList);
									}
								}
							}
						}
					} else {
						if (subFile[i].getName().equals("Token.lock")) {
							return "lock";
						}
					}
				}// end for
			// 上锁
			File fileLS = new File(filePath + "\\Token.lock");
			fileLS.createNewFile();
			// 循环上传
			for (Iterator it = mtsBeanMap.keySet().iterator(); it.hasNext();) {
				String rptTitle = (String) it.next();
				if (!configMtsMap.containsKey(rptTitle)) {
					continue;
				}
				Mts mts = (Mts) configMtsMap.get(rptTitle);
				List fileList = (ArrayList) mtsBeanMap.get(rptTitle);
				if (doUpload(mts, fileList, filePath)) {
					passNum += fileList.size();
				}
			}
			if (passNum == 0)
				return "error";
			else if (sendList != null && passNum == sendList.size())
				return "ok";
			else
				return "notAll";
		} catch (Exception e) {
			log.error("上传mts文件失败", e);
			return "error";
		} finally {
			try {
				File fileLS = new File(filePath + "\\Token.lock");
				if (fileLS.exists())
					fileLS.delete();
			} catch (Exception e) {
				log.warn("删除锁文件失败", e);
			}
		}
	}

	public String findAutoCheckRunTime() {
		String runTime = null;
		List list = this.find("findAutoCheckRunTime", new HashMap());
		if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
			runTime = (String) list.get(0);
		}
		return runTime;
	}

	public Map initConfigMts() {
		return userInterfaceConfigService.initConfigMts();
	}

	public Map initConfigParameters() {
		return userInterfaceConfigService.initConfigParameters();
	}

	public List findAllOrgConfigList(String orgName, String orgId, String userid) {
		return orgconfigeservice.findAll(orgName, orgId, userid, null);
	}

	public List findRptTitleList() {
		return orgconfigeservice.findRptTitleList();
	}

	public boolean belongToTopFiveWorkingDay(String yyyyMMdd) {
		String day = yyyyMMdd.substring(6, 8);
		if (Integer.valueOf(day).intValue() <= 5) {
			return true;
		}
		String yearMonth = yyyyMMdd.substring(0, 4) + "-"
				+ yyyyMMdd.substring(4, 6);
		Map param = new HashMap();
		param.put("holidayValue", yearMonth);
		// 从FMSS节假日管理中查询该月设置的节假日
		List listHoliday = this.find("findHolidayFromFmss", param);
		if (CollectionUtils.isNotEmpty(listHoliday)) {
			// 该月有节假日
			Date date = DateUtils.stringToDate(yyyyMMdd,
					DateUtils.ORA_DATE_FORMAT);
			// 得到该月的第一天和最后一天
			Date firstDate = DateUtils.getFirstDayOfMonth(date);
			Date lastDate = DateUtils.getLastDayOfMonth(date);
			int nFirst = Integer.valueOf(
					DateUtils.toString(firstDate, DateUtils.ORA_DATE_FORMAT))
					.intValue();
			int nLast = Integer.valueOf(
					DateUtils.toString(lastDate, DateUtils.ORA_DATE_FORMAT))
					.intValue();
			Map mapTopFive = new HashMap();// 该月前5个工作日
			String fifthWorkingDay = null;// 该月第5个工作日yyyyMMdd
			for (int i = nFirst; i <= nLast; i++) {
				String date_yyyyMMdd = String.valueOf(i);
				String date_yyyy_MM_dd = date_yyyyMMdd.substring(0, 4) + "-"
						+ date_yyyyMMdd.substring(4, 6) + "-"
						+ date_yyyyMMdd.substring(6, 8);
				if (listHoliday.contains(date_yyyy_MM_dd)) {
					// 当前日期定义为节假日
				} else {
					mapTopFive.put(date_yyyyMMdd, date_yyyyMMdd);
					if (mapTopFive.size() == 5) {
						fifthWorkingDay = date_yyyyMMdd;
						break;
					}
				}
			}
			if (mapTopFive.containsKey(yyyyMMdd)
					|| Integer.valueOf(yyyyMMdd).intValue() <= Integer.valueOf(
							fifthWorkingDay).intValue()) {
				// 当前日期属于当月前5个工作日或不晚于当月第5个工作日
				return true;
			} else {
				return false;
			}
		} else {
			// 该月没有节假日
			if (Integer.valueOf(day).intValue() > 5) {
				// 凡晚于该月5日的日期，都不属于前5个工作日
				return false;
			}
		}
		return true;
	}

	public List findRptTableCodeFromZ02(String objCode, String buocMonth,
			String[] instCodes) {
		Map map = new HashMap();
		map.put("objCode", objCode);
		map.put("buocMonth", buocMonth);
		if (instCodes != null && instCodes.length > 0) {
			StringBuffer searchInstCodes = new StringBuffer();
			searchInstCodes.append("'").append(instCodes[0]).append("'");
			for (int i = 1; i < instCodes.length; i++) {
				searchInstCodes.append(",'").append(instCodes[i]).append("'");
			}
			map.put("instCodes", searchInstCodes.toString());
		}
		return this.find("findRptTableCodeFromZ02", map);
	}

	public List verifyTables(String[] instCodes, String buocMonth) {
		List msgList = new ArrayList();
		if (instCodes == null || instCodes.length == 0
				|| StringUtil.isEmpty(buocMonth)) {
			msgList.add("所需信息不完整，无法生成报文");
			return msgList;
		}
		for (int i = 0; i < instCodes.length; i++) {
			String instCode = instCodes[i];
			List z01list = null;
			List z02list = null;
			List z03list = null;
			List a0201list = null;
			List a0202list = null;
			List a0203list = null;
			List d01list = null;
			List d02list = null;
			List d03list = null;
			List d04list = null;
			List d0501list = null;
			List d0502list = null;
			List d0601list = null;
			SearchModel searchModel = new SearchModel();
			StringBuffer searchCondition = new StringBuffer().append(
					" instCode = '").append(instCode).append(
					"' and buocMonth = '").append(buocMonth).append(
					"' and datastatus in (").append(DataUtil.SHYTG_STATUS_NUM)
					.append(",").append(DataUtil.YSC_STATUS_NUM).append(",")
					.append(DataUtil.YBS_STATUS_NUM).append(",").append(
							DataUtil.LOCKED_STATUS_NUM).append(
							") and actiontype <> '").append(
							DataVerify.ACTIONTYPE_D).append("' ");
			searchModel.setSearchCondition(searchCondition.toString());
			// 集中获取生成报文中需要校验的表单
			// Z01
			searchModel.setTableId("T_FAL_Z01");
			z01list = this.searchService.search(searchModel);
			// Z02
			searchModel.setTableId("T_FAL_Z02");
			z02list = this.searchService.search(searchModel);
			// Z03
			searchModel.setTableId("T_FAL_Z03");
			z03list = this.searchService.search(searchModel);
			// A02_1
			searchModel.setTableId("T_FAL_A02_1");
			a0201list = this.searchService.search(searchModel);
			// A02_2
			searchModel.setTableId("T_FAL_A02_2");
			a0202list = this.searchService.search(searchModel);
			// A02_3
			searchModel.setTableId("T_FAL_A02_3");
			a0203list = this.searchService.search(searchModel);
			// D01
			searchModel.setTableId("T_FAL_D01");
			d01list = this.searchService.search(searchModel);
			// D02
			searchModel.setTableId("T_FAL_D02");
			d02list = this.searchService.search(searchModel);
			// D05_1
			searchModel.setTableId("T_FAL_D05_1");
			d0501list = this.searchService.search(searchModel);
			// D06_1
			searchModel.setTableId("T_FAL_D06_1");
			d0601list = this.searchService.search(searchModel);
			// 进行校验
			if (CollectionUtils.isNotEmpty(z01list)) {
				for (Iterator it = z01list.iterator(); it.hasNext();) {
					Fal_Z01Entity z01Entity = (Fal_Z01Entity) it.next();
					// 问答四-1.2跨表和跨期核查规则-1
					// 问答四-1.2跨表和跨期核查规则-2
					// 问答四-1.2跨表和跨期核查规则-5
					// 问答四-1.2跨表和跨期核查规则-6
					// 问答四-1.2跨表和跨期核查规则-7
					if (z01Entity != null
							&& ((StringUtil.isNotEmpty(z01Entity.getZ0106()) && (z01Entity
									.getZ0106().startsWith("2%") || z01Entity
									.getZ0106().startsWith("3%"))) || "2"
									.equals(z01Entity.getZ0107()))) {
						if (CollectionUtils.isEmpty(a0201list)) {
							if (StringUtil.isNotEmpty(z01Entity.getZ0106())
									&& (z01Entity.getZ0106().startsWith("2%") || z01Entity
											.getZ0106().startsWith("3%"))) {
								// 问答四-1.2跨表和跨期核查规则-6
								msgList
										.add("当Z0106=“7.6经济类型代码表”200或300下的子项时，A02-1表必须有数据！当前A02-1表为空");
							} else if ("2".equals(z01Entity.getZ0107())) {
								// 问答四-1.2跨表和跨期核查规则-7
								msgList
										.add("当Z0107=2(分支机构)时，A02-1表必须有数据！当前A02-1表为空");
							}
						}
						if (CollectionUtils.isEmpty(a0202list)) {
							if (StringUtil.isNotEmpty(z01Entity.getZ0106())
									&& (z01Entity.getZ0106().startsWith("2%") || z01Entity
											.getZ0106().startsWith("3%"))) {
								// 问答四-1.2跨表和跨期核查规则-6
								msgList
										.add("当Z0106=“7.6经济类型代码表”200或300下的子项时，A02-2表必须有数据！当前A02-2表为空");
							} else if ("2".equals(z01Entity.getZ0107())) {
								// 问答四-1.2跨表和跨期核查规则-7
								msgList
										.add("当Z0107=2(分支机构)时，A02-2表必须有数据！当前A02-2表为空");
							}
						}
						if (CollectionUtils.isEmpty(z03list)
								&& "2".equals(z01Entity.getZ0107())) {
							// 问答四-1.2跨表和跨期核查规则-5
							msgList
									.add("当Z0107=2(分支机构)时，必然为外资机构，必须填写Z03表！当前Z03表为空");
						}
						if (CollectionUtils.isNotEmpty(z02list)) {
							boolean z03is1 = false;
							boolean a0201is1 = false;
							boolean a0202is1 = false;
							for (Iterator itr = z02list.iterator(); itr
									.hasNext();) {
								Fal_Z02Entity z02Entity = (Fal_Z02Entity) itr
										.next();
								if (z02Entity != null) {
									if ("Z03".equals(z02Entity.getTablecode())
											&& "1".equals(z02Entity.getZ0201())) {
										z03is1 = true;
									}
									if ("A02-1"
											.equals(z02Entity.getTablecode())
											&& "1".equals(z02Entity.getZ0201())) {
										a0201is1 = true;
									}
									if ("A02-2"
											.equals(z02Entity.getTablecode())
											&& "1".equals(z02Entity.getZ0201())) {
										a0202is1 = true;
									}
								}
							}
							if (!z03is1) {
								// 问答四-1.2跨表和跨期核查规则-1
								// 问答四-1.2跨表和跨期核查规则-2
								msgList
										.add("当Z0106=“7.6经济类型代码表”200或300下的子项或Z0107=2(分支机构)时，Z02表中的Z03表的Z0201应为“1-是”");
							}
							if (!a0201is1) {
								// 问答四-1.2跨表和跨期核查规则-1
								// 问答四-1.2跨表和跨期核查规则-2
								msgList
										.add("当Z0106=“7.6经济类型代码表”200或300下的子项或Z0107=2(分支机构)时，Z02表中的A02-1表的Z0201应为“1-是”");
							}
							if (!a0202is1) {
								// 问答四-1.2跨表和跨期核查规则-1
								// 问答四-1.2跨表和跨期核查规则-2
								msgList
										.add("当Z0106=“7.6经济类型代码表”200或300下的子项或Z0107=2(分支机构)时，Z02表中的A02-2表的Z0201应为“1-是”");
							}
						}
					}
				}
			}
			if (CollectionUtils.isNotEmpty(d01list)
					&& Integer.valueOf(buocMonth).intValue() > 201409) {
				// 问答四-1.2跨表和跨期核查规则-30
				List currencyList = new ArrayList();
				Map d0107Map = new HashMap();
				Map d0108Map = new HashMap();
				// 循环当期D01数据，整理所有涉及币种和其对应上月末本金余额合计
				for (Iterator it = d01list.iterator(); it.hasNext();) {
					Fal_D01Entity entity = (Fal_D01Entity) it.next();
					if (entity != null
							&& StringUtil.isNotEmpty(entity.getD0106())
							&& (entity.getD0107() != null || entity.getD0108() != null)) {
						if (!currencyList.contains(entity.getD0106())) {
							d0107Map.put(entity.getD0106(), entity.getD0107());
							d0108Map.put(entity.getD0106(), entity.getD0108());
							currencyList.add(entity.getD0106());
						} else {
							BigDecimal d0107 = (BigDecimal) d0107Map.get(entity
									.getD0106());
							d0107 = d0107.add(entity.getD0107());
							d0107Map.put(entity.getD0106(), d0107);
							BigDecimal d0108 = (BigDecimal) d0108Map.get(entity
									.getD0106());
							d0108 = d0108.add(entity.getD0108());
							d0108Map.put(entity.getD0106(), d0108);
						}
					}
				}
				for (Iterator it = currencyList.iterator(); it.hasNext();) {
					String curr = (String) it.next();
					BigDecimal d0107 = d0107Map.get(curr) == null ? new BigDecimal(
							0.0)
							: (BigDecimal) d0107Map.get(curr);
					BigDecimal d0108 = d0108Map.get(curr) == null ? new BigDecimal(
							0.0)
							: (BigDecimal) d0108Map.get(curr);
					SearchModel searchModelPre = new SearchModel();
					StringBuffer searchConditionPre = new StringBuffer()
							.append(" instCode = '").append(instCode).append(
									"' and buocMonth = '").append(
									DateUtils.getPreMonth(buocMonth, "yyyyMM"))
							.append("' and D0106 = '").append(curr).append(
									"' and datastatus in (").append(
									DataUtil.YSC_STATUS_NUM).append(",")
							.append(DataUtil.YBS_STATUS_NUM).append(",")
							.append(DataUtil.LOCKED_STATUS_NUM).append(
									") and ACTIONTYPE <> '").append(
									DataVerify.ACTIONTYPE_D).append("' ");
					searchModelPre.setSearchCondition(searchConditionPre
							.toString());
					searchModelPre.setTableId("T_FAL_D01");
					List preList = this.searchService.search(searchModelPre);
					BigDecimal preD0109 = new BigDecimal(0.00);
					BigDecimal preD0111 = new BigDecimal(0.00);
					if (CollectionUtils.isNotEmpty(preList)) {
						for (Iterator itr = preList.iterator(); itr.hasNext();) {
							Fal_D01Entity d01Entity = (Fal_D01Entity) itr
									.next();
							if (d01Entity != null) {
								if (d01Entity.getD0109() != null) {
									preD0109 = preD0109.add(d01Entity
											.getD0109());
								}
								if (d01Entity.getD0111() != null) {
									preD0111 = preD0111.add(d01Entity
											.getD0111());
								}
							}
						}
					}
					if (preD0109.compareTo(d0107) != 0) {
						msgList.add("D01表单中，币种" + curr
								+ "本期[上月末本金余额]与上一个报告期的[本月末本金余额]不一致");
					}
					if (preD0111.compareTo(d0108) != 0) {
						msgList.add("D01表单中，币种" + curr
								+ "本期[上月末应收利息余额]与上一个报告期的[本月末应收利息余额]不一致");
					}
				}
			}
			if (CollectionUtils.isNotEmpty(d02list)
					&& Integer.valueOf(buocMonth).intValue() > 201409) {
				// 问答四-1.2跨表和跨期核查规则-30
				List currencyList = new ArrayList();
				Map d0208Map = new HashMap();
				Map d0209Map = new HashMap();
				// 循环当期D02数据，整理所有涉及币种和其对应上月末本金余额合计
				for (Iterator it = d02list.iterator(); it.hasNext();) {
					Fal_D02Entity entity = (Fal_D02Entity) it.next();
					if (entity != null
							&& StringUtil.isNotEmpty(entity.getD0207())
							&& (entity.getD0208() != null || entity.getD0209() != null)) {
						if (!currencyList.contains(entity.getD0207())) {
							d0208Map.put(entity.getD0207(), entity.getD0208());
							d0209Map.put(entity.getD0207(), entity.getD0209());
							currencyList.add(entity.getD0207());
						} else {
							BigDecimal d0208 = (BigDecimal) d0208Map.get(entity
									.getD0207());
							d0208 = d0208.add(entity.getD0208());
							d0208Map.put(entity.getD0207(), d0208);
							BigDecimal d0209 = (BigDecimal) d0209Map.get(entity
									.getD0207());
							d0209 = d0209.add(entity.getD0209());
							d0209Map.put(entity.getD0207(), d0209);
						}
					}
				}
				for (Iterator it = currencyList.iterator(); it.hasNext();) {
					String curr = (String) it.next();
					BigDecimal d0208 = d0208Map.get(curr) == null ? new BigDecimal(
							0.0)
							: (BigDecimal) d0208Map.get(curr);
					BigDecimal d0209 = d0209Map.get(curr) == null ? new BigDecimal(
							0.0)
							: (BigDecimal) d0209Map.get(curr);
					SearchModel searchModelPre = new SearchModel();
					StringBuffer searchConditionPre = new StringBuffer()
							.append(" instCode = '").append(instCode).append(
									"' and buocMonth = '").append(
									DateUtils.getPreMonth(buocMonth, "yyyyMM"))
							.append("' and D0207 = '").append(curr).append(
									"' and datastatus in (").append(
									DataUtil.YSC_STATUS_NUM).append(",")
							.append(DataUtil.YBS_STATUS_NUM).append(",")
							.append(DataUtil.LOCKED_STATUS_NUM).append(
									") and ACTIONTYPE <> '").append(
									DataVerify.ACTIONTYPE_D).append("' ");
					searchModelPre.setSearchCondition(searchConditionPre
							.toString());
					searchModelPre.setTableId("T_FAL_D02");
					List preList = this.searchService.search(searchModelPre);
					BigDecimal preD0210 = new BigDecimal(0.00);
					BigDecimal preD0212 = new BigDecimal(0.00);
					if (CollectionUtils.isNotEmpty(preList)) {
						for (Iterator itr = preList.iterator(); itr.hasNext();) {
							Fal_D02Entity entity = (Fal_D02Entity) itr.next();
							if (entity != null) {
								if (entity.getD0210() != null) {
									preD0210 = preD0210.add(entity.getD0210());
								}
								if (entity.getD0212() != null) {
									preD0212 = preD0212.add(entity.getD0212());
								}
							}
						}
					}
					if (preD0210.compareTo(d0208) != 0) {
						msgList.add("D02表单中，币种" + curr
								+ "本期[上月末本金余额]与上一个报告期的[本月末本金余额]不一致");
					}
					if (preD0212.compareTo(d0209) != 0) {
						msgList.add("D02表单中，币种" + curr
								+ "本期[上月末应付利息余额]与上一个报告期的[本月末应收利息余额]不一致");
					}
				}
			}
			if (CollectionUtils.isNotEmpty(d0501list)
					&& Integer.valueOf(buocMonth).intValue() > 201409) {
				// 问答四-1.2跨表和跨期核查规则-30
				BigDecimal sumD0508 = new BigDecimal(0.00); // 计算本报告期的“上月末应付利息余额”（总量）
				for (Iterator it = d0501list.iterator(); it.hasNext();) {
					Fal_D05Entity d05Entity = (Fal_D05Entity) it.next();
					if (d05Entity != null && d05Entity.getD0508() != null) {
						sumD0508 = sumD0508.add(d05Entity.getD0508());
					}
				}
				SearchModel searchModelPre = new SearchModel();
				StringBuffer searchConditionPre = new StringBuffer().append(
						" instCode = '").append(instCode).append(
						"' and buocMonth = '").append(
						DateUtils.getPreMonth(buocMonth, "yyyyMM")).append(
						"' and datastatus in (")
						.append(DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> '").append(
								DataVerify.ACTIONTYPE_D).append("' ");
				searchModelPre
						.setSearchCondition(searchConditionPre.toString());
				searchModelPre.setTableId("T_FAL_D05_1");
				List preD0501list = this.searchService.search(searchModelPre);
				BigDecimal sumD0511 = new BigDecimal(0.00); // 计算上一个报告期的“本月末应付利息余额”（总量）
				for (Iterator it = preD0501list.iterator(); it.hasNext();) {
					Fal_D05Entity d05Entity = (Fal_D05Entity) it.next();
					if (d05Entity != null && d05Entity.getD0511() != null) {
						sumD0511 = sumD0511.add(d05Entity.getD0511());
					}
				}
				if (sumD0508.compareTo(sumD0511) != 0) {
					msgList
							.add("D05-1表单中，上一个报告期的“本月末应付利息余额”（总量）与本报告期的“上月末应付利息余额”（总量）应一致");
				}
			}
			if (CollectionUtils.isNotEmpty(d0601list)
					&& Integer.valueOf(buocMonth).intValue() > 201409) {
				// 问答四-1.2跨表和跨期核查规则-30
				BigDecimal sumD0607 = new BigDecimal(0.00); // 计算本报告期的“上月末应付利息余额”（总量）
				for (Iterator it = d0601list.iterator(); it.hasNext();) {
					Fal_D06Entity d06Entity = (Fal_D06Entity) it.next();
					if (d06Entity != null && d06Entity.getD0607() != null) {
						sumD0607 = sumD0607.add(d06Entity.getD0607());
					}
				}
				SearchModel searchModelPre = new SearchModel();
				StringBuffer searchConditionPre = new StringBuffer().append(
						" instCode = '").append(instCode).append(
						"' and buocMonth = '").append(
						DateUtils.getPreMonth(buocMonth, "yyyyMM")).append(
						"' and datastatus in (")
						.append(DataUtil.YSC_STATUS_NUM).append(",").append(
								DataUtil.YBS_STATUS_NUM).append(",").append(
								DataUtil.LOCKED_STATUS_NUM).append(
								") and ACTIONTYPE <> '").append(
								DataVerify.ACTIONTYPE_D).append("' ");
				searchModelPre
						.setSearchCondition(searchConditionPre.toString());
				searchModelPre.setTableId("T_FAL_D06_1");
				List preD0601list = this.searchService.search(searchModelPre);
				BigDecimal sumD0610 = new BigDecimal(0.00); // 计算上一个报告期的“本月末应付利息余额”（总量）
				for (Iterator it = preD0601list.iterator(); it.hasNext();) {
					Fal_D06Entity d06Entity = (Fal_D06Entity) it.next();
					if (d06Entity != null && d06Entity.getD0610() != null) {
						sumD0610 = sumD0610.add(d06Entity.getD0610());
					}
				}
				if (sumD0607.compareTo(sumD0610) != 0) {
					msgList
							.add("D06-1表单中，上一个报告期的“本月末应付利息余额”（总量）与本报告期的“上月末应付利息余额”（总量）应一致");
				}
			}
		}
		return msgList;
	}
}
