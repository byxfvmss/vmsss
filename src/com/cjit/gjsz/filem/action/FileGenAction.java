/**
 * 
 */
package com.cjit.gjsz.filem.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CharacterEncoding;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.DocXmlUtil;
import com.cjit.common.util.ParameterBundle;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptStatusCountInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.InnerTableSign;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.filem.util.ZipUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.impl.SelfDataVerify;
import com.cjit.gjsz.logic.model.Fal_Z02Entity;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.opensymphony.util.BeanUtils;

/**
 * @author yulubin
 */
public class FileGenAction extends BaseListAction {

	private static final String CONTROL_FILE = "controlFile";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4613147642773991074L;
	private static final String TOKEN_LOCK_FILE_NAME = "Token.lock";
	private static final String SEND_FILE_SUFFIX = ".XML";
	private static final String SYSTEM_CODE_FAL = "FAL";
	private static final String CONTROL_FILE_TYPE_CODE = "TT";
	private static final String APPTYPE_FAL = "FAL";
	private static final String IN = "IN";
	// private static final String OUT = "OUT";
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
	private static final String MAP_TABLE_DESC = "MAP_TABLE_DESC_";
	private DataDealService dataDealService;
	private OrganizationService organizationService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private LogManagerService logManagerService;
	private AutoDealRptService autoDealRptService;
	private List authInstList = new ArrayList();
	private List rptTableList;
	// 报表的列信息集
	private List rptColumnList = new ArrayList();
	// 报表字段个数，用于页面灵活打印
	private List columnIdList;
	private int largestColumnNum;
	private RptData rptData = new RptData();
	private String tableId;
	// private String[] tableIds;
	private String[] fileTypes;
	private String[] instCodes;
	private String infoTypeCode;
	private String[] businessIds;
	private String[] sendFileTableIds;
	private String[] sendFileTypes;
	private List recordDescList = new ArrayList();
	private List sendFileDownLoadList = new ArrayList();
	private Map mpCannotCreate = new HashMap();
	private List recordList = new ArrayList();
	private String controlFileName;
	private String sendFilePath;
	private String sendFileMakeStatus;
	private String notClearSendFileMap;
	private String serverType;
	// 生成报文时是否连同所在文件夹一起打入zip包 yes/no
	private String fileGenDirZip;
	protected String message = "";
	// 业务信息集
	private List busiDataInfoList = new ArrayList();

	public String getMessage() {
		return message;
	}

	/**
	 * 报文生成初始页面 菜单【报文管理－报文生成（按记录）】
	 * 
	 * @return
	 */
	public String genFile() {
		try {
			this.initConfigParameters();
			if (request.getParameter("notClearSendFileMap") == null) {
				this.addFieldToSession(ScopeConstants.SEND_FILE_MAP,
						new LinkedHashMap());
			}
			// 切换下拉列表时，添加下面的两个函数
			init();
			makeRecordDescList();
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("interfaceVer", this.interfaceVer);
			if (this.request.getParameter("fromUpFile") != null) {
				if (this.request.getParameter("fromUpFile").equals("1"))
					this.request.setAttribute("message", "文件上传成功!");
				else if (this.request.getParameter("fromUpFile").equals("2"))
					this.request.setAttribute("message", "控制文件存在,无法上传文件!");
				else
					this.request.setAttribute("message", "文件上传出现异常!");
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-genFile", e);
			return ERROR;
		}
	}

	/**
	 * 报文生成2初始页面 菜单【报文管理－报文生成（按表单）】
	 * 
	 * @return
	 */
	public String genFile2() {
		try {
			this.initConfigParameters();
			if (request.getParameter("notClearSendFileMap") == null) {
				this.addFieldToSession(ScopeConstants.SEND_FILE_MAP2,
						new LinkedHashMap());
			}
			// 清空详细信息描述列表内容
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			if (currentUser != null) {
				this.addFieldToSession(MAP_TABLE_DESC + currentUser.getId(),
						null);
			}
			basic2();
			// 设置默认全选所有机构
			if (authInstList != null && instCodes == null) {
				instCodes = new String[authInstList.size()];
				for (int i = 0; i < authInstList.size(); i++) {
					instCodes[i] = ((Organization) authInstList.get(i)).getId();
				}
			}
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("interfaceVer", this.interfaceVer);
			this.request.setAttribute("infoType", this.infoType);
			if (this.request.getParameter("fromUpFile") != null) {
				if (this.request.getParameter("fromUpFile").equals("1"))
					this.request.setAttribute("message", "文件上传成功!");
				else if (this.request.getParameter("fromUpFile").equals("2"))
					this.request.setAttribute("message", "控制文件存在,无法上传文件!");
				else
					this.request.setAttribute("message", "文件上传出现异常!");
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-genFile2", e);
			return ERROR;
		}
	}

	// 读附件
	public void downloadAttachment() {
		// 读到流中
		try {
			sendFilePath = StringUtil.replace(sendFilePath, "//", "/");
			sendFilePath = StringUtil.replace(sendFilePath, "\\", "/");
			controlFileName = StringUtil.replace(controlFileName, "//", "/");
			controlFileName = StringUtil.replace(controlFileName, "\\", "/");
			response.reset();
			String fileName = controlFileName.substring(controlFileName
					.indexOf("/") + 1, controlFileName.length());
			if (fileName != null && fileName.toLowerCase().endsWith("xml")) {
				response.setContentType("application/xml");
			} else {
				response.setContentType("application/zip");
			}
			response.addHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
			while ((size = bis.read(buf)) != -1) {
				out.write(buf, 0, size);
			}
			out.close();
			bis.close();
			is.close();
		} catch (FileNotFoundException e) {
			log.error("FileGenAction-downloadAttachment", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("FileGenAction-downloadAttachment", e);
			e.printStackTrace();
		}
	}

	/**
	 * 添加记录（按记录）
	 * 
	 * @return String
	 */
	public String getDatas() {
		try {
			this.addFieldToRequest("selectAllCheckBox", "1");
			basic();
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("tableId", this.tableId);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-getDatas", e);
			return ERROR;
		}
	}

	/**
	 * 添加报文记录(按记录)
	 * 
	 * @return String
	 */
	public String addRecords() {
		try {
			Map sendFileMap = (Map) this
					.getFieldFromSession(ScopeConstants.SEND_FILE_MAP);
			if (sendFileMap == null) {
				sendFileMap = new LinkedHashMap();
				this.addFieldToSession(ScopeConstants.SEND_FILE_MAP,
						sendFileMap);
			}
			// 要往sendFileMap中添加的内容
			Object[] value = new Object[5];
			String selectFileType = this.request.getParameter("selectFileType");
			sendFileMap.put(selectFileType, value);
			// infoTypeCode
			value[0] = infoTypeCode;
			// instCodes
			List instCodes = new ArrayList();
			for (int i = 0; i < this.instCodes.length; i++) {
				instCodes.add(this.instCodes[i]);
			}
			value[1] = instCodes;
			// businessIds
			List businessIds = new ArrayList();
			if (this.businessIds != null) {
				for (int i = 0; i < this.businessIds.length; i++) {
					businessIds.add(this.businessIds[i]);
				}
			}
			value[2] = businessIds;
			value[4] = new Integer(businessIds.size());
			basic();
			// this.instCodes=null;
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("selectFileType", "");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-addRecords", e);
			return ERROR;
		}
	}

	/**
	 * 添加报文记录2
	 * 
	 * @return
	 */
	public String addRecords2() {
		try {
			StringBuffer sb = new StringBuffer();
			Map sendFileMap2 = (Map) this
					.getFieldFromSession(ScopeConstants.SEND_FILE_MAP2);
			if (sendFileMap2 == null) {
				sendFileMap2 = new LinkedHashMap();
				this.addFieldToSession(ScopeConstants.SEND_FILE_MAP2,
						sendFileMap2);
			}
			this.initConfigParameters();
			String relatedFileType = "";// 关联数据信息需要过滤
			String configRptTitle = "";// 是否根据主报告行号汇总生成报文
			String fileGenWithZ02 = "";// 是否严格依据Z02配置生成报文 yes/no 默认yes
			String configVeriftTables = "";// 是否在报文生成处依问答四期进行跨表跨期校验
			if (configMap != null && !configMap.isEmpty()) {
				relatedFileType = (String) configMap
						.get("config.related.filetype");
				configRptTitle = (String) configMap
						.get("config.fileGen.rpttitle");
				fileGenWithZ02 = (String) configMap
						.get("config.fileGen.with.Z02");
				configVeriftTables = (String) configMap
						.get("config.fileGen.verify.tables");
			}
			if (!this.checkInstCodesSameRptTitle(configRptTitle)) {
				basic2();
				this.sendFileMakeStatus = "请选取主报告行号相同的机构来生成报文！";
				return SUCCESS;
			}
			if ("yes".equals(configVeriftTables)) {
				List msgList = this.autoDealRptService.verifyTables(
						this.instCodes, buocMonth);
				if (msgList != null && msgList.size() > 0) {
					basic2();
					if (this.sendFileMakeStatus == null) {
						this.sendFileMakeStatus = "";
					}
					for (Iterator it = msgList.iterator(); it.hasNext();) {
						String msg = (String) it.next();
						this.sendFileMakeStatus += msg + "\\n";
					}
					return SUCCESS;
				}
			}
			boolean addAllRpt = false;
			if ("Z".equals(infoType)) {
				addAllRpt = true;
			}
			Map orgMap = new HashMap();
			if (this.instCodes != null && this.instCodes.length > 0) {
				List insts = new ArrayList();
				getAuthInstList(insts);
				for (int i = 0; i < this.instCodes.length; i++) {
					for (int j = 0; j < insts.size(); j++) {
						Organization org = (Organization) insts.get(j);
						if (org.getId().equals(this.instCodes[i])) {
							orgMap.put(this.instCodes[i], org.getName());
							break;
						}
					}
				}
			}
			boolean sameBatchCA = false;
			for (int i = 0; i < fileTypes.length; i++) {
				if (fileTypes[i] != null && fileTypes[i].equalsIgnoreCase("CA")) {
					// sameBatchCA = true;
				}
			}
			// 依机构和日期，从Z02表单查询所配置当期需报送的表单号
			List z02List = this.autoDealRptService.findRptTableCodeFromZ02(
					null, this.buocMonth, this.instCodes);
			Map z02tableCodeMap = null;// 定义指定报告期需报送的表号
			if (CollectionUtils.isNotEmpty(z02List)) {
				Map dictionaryMap = (Map) SystemCache
						.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
				if (dictionaryMap != null) {
					Map tableMap = (HashMap) dictionaryMap.get("T_FAL_Z02");
					if (tableMap != null) {
						List tableCodeList = (ArrayList) tableMap
								.get(DataVerify.FAL_TABLE_INFO);
						List missingCodeList = new ArrayList();
						for (Iterator t = tableCodeList.iterator(); t.hasNext();) {
							Dictionary cd = (Dictionary) t.next();
							boolean existsTableCode = false;
							for (Iterator z = z02List.iterator(); z.hasNext();) {
								Fal_Z02Entity z02 = (Fal_Z02Entity) z.next();
								if (cd.getValueStandardNum().equalsIgnoreCase(
										z02.getTablecode())
										&& (z02.getDatastatus() == DataUtil.SHYTG_STATUS_NUM
												|| z02.getDatastatus() == DataUtil.YSC_STATUS_NUM
												|| z02.getDatastatus() == DataUtil.YBS_STATUS_NUM || z02
												.getDatastatus() == DataUtil.LOCKED_STATUS_NUM)) {
									existsTableCode = true;
								}
							}
							if (!existsTableCode) {
								if ("yes".equalsIgnoreCase(fileGenWithZ02)) {
									missingCodeList.add(cd.getValueBank());
								}
							}
						}
						if (missingCodeList.size() > 0) {
							basic2();
							StringBuffer sbMessage = new StringBuffer();
							sbMessage.append("当期在Z02业务概览表中缺少对[");
							for (Iterator z = missingCodeList.iterator(); z
									.hasNext();) {
								sbMessage.append((String) z.next()).append(",");
							}
							this.sendFileMakeStatus = sbMessage.toString()
									.substring(0,
											sbMessage.toString().length() - 1)
									+ "]的业务描述无法生成报文！";
							return SUCCESS;
						}
					}
				}
				z02tableCodeMap = new HashMap();
				for (Iterator z = z02List.iterator(); z.hasNext();) {
					Fal_Z02Entity z02 = (Fal_Z02Entity) z.next();
					if (z02 != null
							&& StringUtil.isNotEmpty(z02.getTablecode())
							&& "1".equals(z02.getZ0201())
							&& (z02.getDatastatus() == DataUtil.SHYTG_STATUS_NUM
									|| z02.getDatastatus() == DataUtil.YSC_STATUS_NUM
									|| z02.getDatastatus() == DataUtil.YBS_STATUS_NUM || z02
									.getDatastatus() == DataUtil.LOCKED_STATUS_NUM)) {
						z02tableCodeMap.put(z02.getTablecode(), z02);
					}
				}
			}
			if (z02tableCodeMap == null || z02tableCodeMap.size() == 0) {
				if ("yes".equalsIgnoreCase(fileGenWithZ02)) {
					basic2();
					this.sendFileMakeStatus = "当期填报单位基本信息Z02中未配置发生业务表单！";
					this.request.setAttribute("sendFileMakeStatus",
							this.sendFileMakeStatus);
					return SUCCESS;
				}
			}
			for (int i = 0; i < fileTypes.length; i++) {
				if (fileTypes[i] == null) {
					continue;
				} else {
					String tableCode = DataUtil.getRptCodeByFileType(
							fileTypes[i], 3);
					if (z02tableCodeMap == null
							|| !z02tableCodeMap.containsKey(tableCode)) {
						if ("yes".equalsIgnoreCase(fileGenWithZ02)) {
							continue;
						}
					}
				}
				// 要往sendFileMap中添加的内容
				String tableSelectId = DataUtil
						.getTableIdByFileType(fileTypes[i]);
				Object[] value = new Object[5];
				sendFileMap2.put(fileTypes[i], value);
				infoTypeCode = DataUtil.getInfoTypeCodeByTableId(tableSelectId);
				// infoTypeCode
				value[0] = infoTypeCode;
				// instCodes
				List instCodes = new ArrayList();
				for (int j = 0; j < this.instCodes.length; j++) {
					instCodes.add(this.instCodes[j]);
				}
				value[1] = instCodes;
				List businessIds = new ArrayList();
				// 拼查询列
				String columns = this
						.getColumnsSql(tableSelectId, fileTypes[i]);
				// 获取符合生成条件的列表数据
				if (instCodes != null) {
					// List recordList = new ArrayList();
					long nFileCount = 0; // 可生成报文记录数
					long nNoPass = 0; // 审核未通过记录数
					long nNoAudit = 0; // 未审核记录数
					long nNoGen = 0; // 审核通过但未生成记录数
					// 依当前文件类型filetype，分别创建适用于签约、变动、余额等的查询条件
					String searchCondition = this
							.getAddFileGenSearchConditonSQL(tableSelectId,
									fileTypes[i], relatedFileType, sameBatchCA,
									this.buocMonth);
					// 报文生成的可选记录中，只显示状态为6的
					String dataStatusCondition = " t.datastatus = "
							+ DataUtil.SHYTG_STATUS_NUM + " and t.fileType = '"
							+ fileTypes[i] + "' ";
					String[] instCodess = new String[instCodes.size()];
					instCodes.toArray(instCodess);
					RptData rd = new RptData();
					rd.setTableId(tableSelectId);
					rd.setColumns(columns);
					rd.setInstCodes(instCodess);
					rd.setDataStatusCondition(dataStatusCondition);
					rd.setSearchCondition(searchCondition);
					nFileCount = dataDealService.findRptDataCount(rd)
							.longValue();
					// 看看有哪些还不符合条件的
					rd.setDataStatusCondition(null);
					searchCondition = this
							.getCannotAddFileGenSearchConditonSQL(
									tableSelectId, fileTypes[i],
									relatedFileType, sameBatchCA,
									this.buocMonth);
					rd.setSearchCondition(searchCondition);
					List listCount = dataDealService
							.findRptCountGroupbyStatus(rd);
					if (listCount != null && listCount.size() > 0) {
						for (int c = 0; c < listCount.size(); c++) {
							RptData cRd = (RptData) listCount.get(c);
							if (String.valueOf(DataUtil.SHWTG_STATUS_NUM)
									.equals(cRd.getDataStatus())) {
								nNoAudit += cRd.getCountRow();
							} else if (String
									.valueOf(DataUtil.JYYTG_STATUS_NUM).equals(
											cRd.getDataStatus())
									|| String
											.valueOf(DataUtil.JYWTG_STATUS_NUM)
											.equals(cRd.getDataStatus())
									|| String.valueOf(DataUtil.WJY_STATUS_NUM)
											.equals(cRd.getDataStatus())) {
								nNoPass += cRd.getCountRow();
							} else if (String
									.valueOf(DataUtil.SHYTG_STATUS_NUM).equals(
											cRd.getDataStatus())) {
								nNoGen += cRd.getCountRow();
							}
						}
					}
					setValueForm(nNoPass, nNoAudit, nNoGen, sb, tableSelectId,
							fileTypes[i], relatedFileType);
					value[4] = new Integer(String.valueOf(nFileCount));
				}
				value[2] = businessIds;
				// value[4] = new Integer(businessIds.size());
			}
			if (addAllRpt) {
				this.infoTypeCode = "250";
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			}
			basic2();
			if (sb.length() > 0) {
				message = sb.toString();
				this.request.setAttribute("message", message);
			}
			// this.instCodes=null;
			// 在wl8.1下放入request
			this.request.setAttribute("infoType", this.infoType);
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-addRecords2", e);
			return ERROR;
		}
	}

	/**
	 * 删除报文记录
	 * 
	 * @return
	 */
	public String deleteRecords() {
		try {
			Map sendFileMap = (Map) this
					.getFieldFromSession(ScopeConstants.SEND_FILE_MAP);
			if (sendFileMap != null) {
				for (int i = 0; i < sendFileTypes.length; i++) {
					sendFileMap.remove(sendFileTypes[i]);
				}
			}
			basic();
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-deleteRecords", e);
			return ERROR;
		}
	}

	/**
	 * 删除报文记录
	 * 
	 * @return
	 */
	public String deleteRecords2() {
		try {
			Map sendFileMap = (Map) this
					.getFieldFromSession(ScopeConstants.SEND_FILE_MAP2);
			if (sendFileMap != null) {
				for (int i = 0; i < sendFileTypes.length; i++) {
					sendFileMap.remove(sendFileTypes[i]);
				}
			}
			basic2();
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-deleteRecords2", e);
			return ERROR;
		}
	}

	/**
	 * 得到Base的业务主键
	 * 
	 * @param list
	 * @return
	 */
	public String getBusinessIds(List list) {
		StringBuffer sb = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			Object[] value = (Object[]) entry.getValue();
			// 取需要生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			if (CollectionUtil.isNotEmpty(businessIds)) {
				sb.append(CollectionUtil.getNumberSQLIds(businessIds) + ",");
			}
		}
		return sb.toString();
	}

	/**
	 * 生成报文通用方法
	 * 
	 * @param sendFileMapAttName
	 * @return
	 */
	private String makeFileCommon(String sendFileMapAttName) {
		boolean createSendFileTokenLock = false;
		String appFilePath = this.servletContext.getRealPath("/data/sendfile");
		try {
			this.initConfigParameters();
			// 关联数据信息需要过滤
			String relatedFileType = "";
			// 是否根据主报告行号汇总生成报文
			String configRptTitle = "";
			// 机构对照管理出配置主报告行号
			String orgRptTitle = null;
			// 生成报文后是否自动上传MTS目录 yes/no 默认yes
			String fileGenAutoSendMTS = "";
			if (configMap != null && !configMap.isEmpty()) {
				relatedFileType = (String) configMap
						.get("config.related.filetype");
				configRptTitle = (String) this.configMap
						.get("config.fileGen.rpttitle");
				fileGenAutoSendMTS = (String) configMap
						.get("config.fileGen.auto.sendMTS");
			}
			// 当前系统日期yyMMdd格式
			String curDate = DateUtils.serverCurrentDate();
			// 当前用户所属机构号（12位）
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			Organization currentUserOrg = new Organization();
			currentUserOrg.setId(currentUser.getOrgId());
			// 当前用户所属机构对照申报号码
			String fileMakerCustomId = (organizationService
					.getOrganization(currentUserOrg)).getCustomId();
			if (!"yes".equalsIgnoreCase(configRptTitle)
					&& (fileMakerCustomId == null || fileMakerCustomId.length() != 12)) {
				sendFileMakeStatus = "当前用户所属机构对照申报号配置不正确，暂时不能生成报送文件！";
				this.request.setAttribute("sendFileMakeStatus",
						sendFileMakeStatus);
				basic();
				sendFileDownLoadList = null;
				// 在wl8.1下放入request
				this.request.setAttribute("businessIds", this.businessIds);
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("recordDescList", this.recordDescList);
				this.request.setAttribute("sendFileDownLoadList",
						this.sendFileDownLoadList);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				logManagerService.writeLog(request, currentUser, "0020",
						"报文管理.报文生成", "报文生成", sendFileMakeStatus, "0");
				return SUCCESS;
			}
			// 清空详细信息描述列表内容
			this.addFieldToSession(MAP_TABLE_DESC + currentUser.getId(), null);
			// SESSION中取fileMakeMap
			Map sendFileMap1 = (Map) this
					.getFieldFromSession(sendFileMapAttName);
			if (sendFileMap1 == null) {
				sendFileMap1 = (Map) request.getSession().getAttribute(
						sendFileMapAttName);
			}
			// 报送文件完整路径
			String fileShortPath = null;
			StringBuffer sbMsg = new StringBuffer();
			User user = this.getCurrentUser();
			Map sendFileMap = new TreeMap();
			sendFileMap.putAll(sendFileMap1);
			// 用于存储报表对应的列信息的MAP:tableId->rptColumnInfoList
			Map tableColumnMap = new LinkedHashMap();
			// 循环fileMakeMap的key:tableId
			// String tableId = null;
			String fileType = null;
			// String instCode = null;
			int serialNo = 0;
			// 以下两个map需实例化为TreeMap,以便依TableId排序,达到基础、申报、核销顺序处理的效果
			Map sendDataCfaMap = new TreeMap();
			// 是否同批生成CA单据
			boolean sameBatchCA = false;
			if (sendFileMap != null) {
				// 循环sendFileMap，生成数据文件名，将之添加到sendFileMap中和dataFileNames中
				for (Iterator i = sendFileMap.entrySet().iterator(); i
						.hasNext();) {
					Map.Entry entry = (Map.Entry) i.next();
					fileType = (String) entry.getKey();
					// if (fileType != null && fileType.equalsIgnoreCase("CA"))
					// {
					// sameBatchCA = true;
					// }
					// 取tableId对应的信息
					Object[] value = (Object[]) entry.getValue();
					if ("yes".equalsIgnoreCase(configRptTitle)
							&& orgRptTitle == null && value[1] != null
							&& this.orgConfigList != null) {
						List instCodes = (List) value[1];
						String instCode0 = (String) instCodes.get(0);
						for (int c = 0; c < orgConfigList.size(); c++) {
							com.cjit.gjsz.filem.model.t_org_config orgConfig = (com.cjit.gjsz.filem.model.t_org_config) orgConfigList
									.get(c);
							if (instCode0.equalsIgnoreCase(orgConfig
									.getOrg_Id())) {
								orgRptTitle = orgConfig.getRptTitle();
							}
						}
					} else {
					}
					// 得到数据文件名
					String dataFileName = getDataFileName(fileType, curDate,
							currentUserOrg, getSerialNoString(serialNo),
							orgRptTitle);
					value[3] = dataFileName;
					sendDataCfaMap.put(fileType, value);
				}
				// 控制文件名:解决序号生成问题开始
				serialNo = dataDealService.getSerialNo(CONTROL_FILE,
						currentUser.getOrgId(), orgRptTitle, curDate);
				controlFileName = getControlFileName(curDate, currentUserOrg,
						getSerialNoString(serialNo), orgRptTitle);
				if (controlFileName == null) {
					// 主报告行号未配置正确
					sendFileMakeStatus = "所选机构主报告行号未配置正确";
					this.request.setAttribute("sendFileMakeStatus",
							sendFileMakeStatus);
					basic();
					sendFileDownLoadList = null;
					// 在wl8.1下放入request
					this.request.setAttribute("businessIds", this.businessIds);
					this.request.setAttribute("recordList", this.recordList);
					this.request.setAttribute("recordDescList",
							this.recordDescList);
					this.request.setAttribute("sendFileDownLoadList",
							this.sendFileDownLoadList);
					this.request.setAttribute("rptColumnList",
							this.rptColumnList);
					logManagerService.writeLog(request, user, "0020",
							"报文管理.报文生成", "报文生成", sendFileMakeStatus, "0");
					return SUCCESS;
				}
				// 报送文件完整路径
				fileShortPath = controlFileName.substring(0, controlFileName
						.length()
						- SEND_FILE_SUFFIX.length());
				String filePath = this.makeDir(sendFilePath, fileShortPath);
				if (!judgeCannotMakeFile(filePath, user)) {
					// 当"报文生成路径不存在"或"令牌文件已存在"
					// 返回生成页面，不进行报文生成操作
					return SUCCESS;
				} else {
					createSendFileTokenLock = true;
				}
				boolean makeSuccess = this.makeSendFile(sendDataCfaMap,
						tableColumnMap, serialNo, fileMakerCustomId,
						orgRptTitle, curDate, user, filePath, sbMsg,
						fileShortPath, SYSTEM_CODE_FAL, relatedFileType,
						sameBatchCA);
				if (!makeSuccess) {
					log.error("makeFileCommon：报文生成出现异常！处理开始");
					// 报文生成失败时，判断本程序是否生成了锁文件
					if (createSendFileTokenLock) {
						// 将本程序生成的锁文件删除
						log.error("makeFileCommon：报文生成出现异常，删除锁文件！");
						this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
						this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
					}
					log.error("makeFileCommon：报文生成出现异常，删除已创建的报文xml文件！");
					this.deleteDir(sendFilePath, fileShortPath);
					// 将对应记录的状态从99还原到6
					log.error("makeFileCommon：报文生成出现异常，回滚选中记录状态为审核通过！");
					this.rollbackRptDataStatus(sendDataCfaMap, relatedFileType,
							sameBatchCA);
					log.error("makeFileCommon：报文生成出现异常，刷新页面信息！");
					if (ScopeConstants.SEND_FILE_MAP.equals(sendFileMapAttName)) {
						basic();
					} else if (ScopeConstants.SEND_FILE_MAP2
							.equals(sendFileMapAttName)) {
						basic2();
					}
					log.error("makeFileCommon：报文生成出现异常！处理结束");
					return SUCCESS;
				}
				this.addFieldToRequest("totalZip", fileShortPath
						+ File.separator + fileShortPath + ".zip");
				this.addFieldToRequest("controlFileName", controlFileName);
			}
			if (ScopeConstants.SEND_FILE_MAP.equals(sendFileMapAttName)) {
				basic();
			} else if (ScopeConstants.SEND_FILE_MAP2.equals(sendFileMapAttName)) {
				basic2();
			}
			sendFileDownLoadList = recordDescList;
			recordDescList = null;
			this.addFieldToSession(sendFileMapAttName, null);
			this.addFieldToRequest("totalZip", fileShortPath + File.separator
					+ fileShortPath + ".zip");
			sendFileMakeStatus = "生成报送文件成功！";
			String logStatus = "1";
			if (sbMsg.toString().length() > 0) {
				if (!"no data".equalsIgnoreCase(sbMsg.toString())) {
					sendFileMakeStatus += "\\n其中：\\n" + sbMsg.toString();
				} else {
					logStatus = "0";
					sendFileMakeStatus = "本批次无可生成报文的记录，请重新选择报文和机构！";
				}
			}
			// 在wl8.1下放入request
			this.request.setAttribute("businessIds", this.businessIds);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("recordDescList", this.recordDescList);
			this.request.setAttribute("sendFileDownLoadList",
					this.sendFileDownLoadList);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			if ("1".equals(logStatus)) {
				// 判断是否在生成报文后直接执行上传MTS操作
				if ("yes".equalsIgnoreCase(fileGenAutoSendMTS)) {
					List sendList = new ArrayList();
					sendList.add(fileShortPath);
					String result = autoDealRptService.uploadReport(sendList,
							null, null);
					if (result.equals("lock")) {
						this.request.setAttribute("message", "控制文件存在,无法上传文件!");
					} else if (result.equals("error")) {
						this.request.setAttribute("message", "文件上传出现异常!");
					} else if (result.equals("ok")) {
						sendFileMakeStatus = sendFileMakeStatus.replaceAll(
								"生成报送文件成功", "生成报送文件并上传成功");
					} else if ("realPathIsNull".equals(result)) {
						// 在20130628版中为error
						this.request.setAttribute("message", "上传出现异常,未找到系统路径");
					} else if ("noFileUpload".equals(result)) {
						// 在20130628版中为error
						this.request.setAttribute("message", "无文件可供上传");
					} else if ("mtsException".equals(result)) {
						// 在20130628版中为error
						this.request.setAttribute("message", "传输过程中出现异常");
					} else if ("mtsNoConfigurate".equals(result)) {
						// 在20130628版中为error
						this.request.setAttribute("message", "存在主报告行未配置MTS参数");
					} else if (result.equals("notAll")) {
						this.request.setAttribute("message", "部分报文上传成功,未全部上传");
					}
				}
			} else {
				this.addFieldToRequest("totalZip", "");
				this.addFieldToRequest("controlFileName", "");
				this.deleteDir(sendFilePath, fileShortPath);
			}
			this.request.setAttribute("sendFileMakeStatus", sendFileMakeStatus);
			logManagerService.writeLog(request, user, "0020", "报文管理.报文生成",
					"报文生成", sendFileMakeStatus, logStatus);
			if (createSendFileTokenLock) {
				this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0020", "报文文件生成失败!", "0");
			if (createSendFileTokenLock) {
				this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			}
			return ERROR;
		}
	}

	private boolean makeSendFile(Map sendFileMap, Map tableColumnMap,
			int serialNo, String fileMakerCustomId, String orgRptTitle,
			String curDate, User user, String filePath, StringBuffer sbMsg,
			String fileShortPath, String systemCode, String relatedFileType,
			boolean sameBatchCA) {
		try {
			List lt1 = new ArrayList();
			List lt2 = new ArrayList();
			Organization currentUserOrg = new Organization();
			currentUserOrg.setId(user.getOrgId());
			// 数据文件名列表集，生成控制文件内容时用
			List dataFileNames = new ArrayList();
			// 是否生成数据报文(若本批次无任何可生成报文的记录，则为false)
			boolean haveMakeFile = false;
			// BusinessId和FileName对照
			Map sendBussIdFileName = new LinkedHashMap();
			// 循环sendFileMap，生成数据文件
			for (Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				String fileType = (String) entry.getKey();
				String tableId = DataUtil.getTableIdByFileType(fileType);
				Map mapInstFileRptNo = new HashMap();// 用于存储当前单据机构最新申报号码
				// 取tableId对应的信息
				// 取报表物理表名tableId
				// 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存
				List columns = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", fileType));
				int cFlag = 0;
				for (Iterator j = columns.iterator(); j.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) j.next();
					column.setAliasColumnId("c" + (++cFlag));
				}
				tableColumnMap.put(tableId, columns);
				// 取tableId对应的信息
				Object[] value = (Object[]) entry.getValue();
				// 得到数据记录的个数
				Integer recordNums = (Integer) value[4];
				// FAL 允许0报送，屏蔽代码 BEGIN 放开如下被屏蔽代码（2014-09-02）
				// 记录个数为0则不生成
				if (recordNums == null || new Integer(0).equals(recordNums)) {
					continue;
				}
				// FAL 允许0报送，屏蔽代码 END 放开如上被屏蔽代码（2014-09-02）
				// 取机构集
				List instCodes = (ArrayList) value[1];
				// 取需要生成的记录的业务主键集
				List businessIds = (ArrayList) value[2];
				// 得到数据文件名
				String dataFileName = (String) value[3];
				List rptDatas = new ArrayList();
				// 根据tableId和机构取数据(按businessIds过滤后的数据)
				if (!CollectionUtil.isEmpty(businessIds)) {
					rptDatas = this.getRptDatas(tableId, businessIds, fileType,
							columns);
					List dataList = new ArrayList();
					for (int j = 0; j < businessIds.size(); j++) {
						RptData rd = new RptData();
						rd.setBusinessId((String) businessIds.get(j));
						rd.setDataStatus(String
								.valueOf(DataUtil.DSC_STATUS_NUM));
						dataList.add(rd);
						if (dataList.size() >= DataUtil.BATCH_SIZE_MAX) {
							log.info("生成报文时，根据ID执行批量更新记录状态为99");
							dataDealService.updateRptDatastatus(tableId,
									dataList);
							dataList.clear();
						}
					}
					if (dataList.size() != 0) {
						log.info("生成报文时，根据ID执行批量更新记录状态为99");
						dataDealService.updateRptDatastatus(tableId, dataList);
						dataList.clear();
					}
				} else if (CollectionUtil.isNotEmpty(instCodes)) {
					rptDatas = this.getRptDatasByInstCodes(tableId, instCodes,
							fileType, relatedFileType, columns, sameBatchCA);
					if (CollectionUtil.isNotEmpty(rptDatas)) {
						businessIds = new ArrayList();
						for (Iterator r = rptDatas.iterator(); r.hasNext();) {
							RptData rd = (RptData) r.next();
							businessIds.add(rd.getBusinessId());
						}
						value[2] = businessIds;
					}
					// 将记录状态置为99待生成
					this.updateRptDataStatusByInstCodes(tableId, instCodes,
							fileType, relatedFileType, DataUtil.DSC_STATUS_NUM,
							sameBatchCA);
				}
				if (CollectionUtil.isNotEmpty(rptDatas)) {
					serialNo = dataDealService.getSerialNo(fileType, user
							.getOrgId(), orgRptTitle, curDate);
					dataFileName = getDataFileName(fileType, curDate,
							currentUserOrg, getSerialNoString(serialNo),
							orgRptTitle);
					dataFileNames.add(dataFileName);
				}
				// 生成数据文件
				// 从config.properties文件中即时读取配置参数，默认5000条分一个文件
				int recordSize = Integer.parseInt(ParameterBundle.getProperty(
						"report.file.size", "5000"));
				int currentSize = rptDatas.size() > recordSize ? recordSize
						: rptDatas.size();
				Document docRoot = getDataFileContent(dataFileName.substring(3,
						5), currentSize, systemCode);
				Element records = docRoot.getRootElement().element(
						RECORDS_ELEMENT);
				// 根据tableId从tableColumnMap中获取报表对应的columns
				columns = (ArrayList) tableColumnMap.get(tableId);
				// 循环报表数据
				int index = 1, times = 0;
				for (Iterator k = rptDatas.iterator(); k.hasNext();) {
					if (index <= currentSize) {
						try {
							RptData addRd = (RptData) k.next();
							if (addRd != null
									&& StringUtil.isNotEmpty(addRd
											.getBusinessId())
									&& StringUtil.isNotEmpty(addRd
											.getConfigRptNo())
									&& addRd.getConfigRptNo().length() == 12) {
								sendBussIdFileName.put(addRd.getBusinessId(),
										dataFileName);
								addRecord(tableId, fileType, curDate, records,
										REC_ELEMENT, columns, addRd, lt1, lt2,
										user.getId(), mapInstFileRptNo);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							sendFileMakeStatus = "出现异常，暂时不能生成报送文件！";
							log.error("FileGenAction-makeSendFile"
									+ sendFileMakeStatus, ex);
							this.request.setAttribute("sendFileMakeStatus",
									sendFileMakeStatus);
							sendFileDownLoadList = null;
							// 在wl8.1下放入request
							this.request.setAttribute("businessIds",
									this.businessIds);
							this.request.setAttribute("recordList",
									this.recordList);
							this.request.setAttribute("recordDescList",
									this.recordDescList);
							this.request.setAttribute("sendFileDownLoadList",
									this.sendFileDownLoadList);
							this.request.setAttribute("rptColumnList",
									this.rptColumnList);
							logManagerService.writeLog(request, user, "0020",
									"报文管理.报文生成", "报文生成", sendFileMakeStatus,
									"0");
							return false;
						}
						index++;
					} else {
						// 写数据文件
						writeFile(filePath, dataFileName, docRoot);
						times++;
						currentSize = (rptDatas.size() - (currentSize * times)) > recordSize ? recordSize
								: (rptDatas.size() - (currentSize * times));
						// 重新赋值
						serialNo = dataDealService.getSerialNo(fileType, user
								.getOrgId(), orgRptTitle, curDate);
						dataFileName = getDataFileName(fileType, curDate,
								currentUserOrg, getSerialNoString(serialNo),
								orgRptTitle);
						dataFileNames.add(dataFileName);
						docRoot = getDataFileContent(dataFileName.substring(3,
								5), currentSize, systemCode);
						records = docRoot.getRootElement().element(
								RECORDS_ELEMENT);
						index = 1;
					}
				}
				// System.out.println(lt1.size());
				if (lt1.size() > 0 || lt2.size() > 0) {
					List cannotCreateList = new ArrayList();
					cannotCreateList.addAll(lt1);
					cannotCreateList.addAll(lt2);
					mpCannotCreate.put(tableId, cannotCreateList);
					RptTableInfo info = new RptTableInfo();
					info.setTableId(tableId);
					List rtiList = dataDealService.findRptTableInfo(info, "");
					if (rtiList != null && rtiList.size() > 0) {
						RptTableInfo rti = (RptTableInfo) rtiList.get(0);
						sbMsg.append("选中报文[").append(rti.getTableName())
								.append("]");
						if (lt1.size() > 0) {
							sbMsg.append("行数超出日生成报文最大限制，有").append(lt1.size())
									.append("行未能生成报文！");
						}
						if (lt2.size() > 0) {
							sbMsg.append("所属机构对应申报号码配置错误，有").append(lt2.size())
									.append("行未能生成报文！");
						}
						sbMsg.append("\\n");
					}
				}
				// 写数据文件 // filePath =
				// D:/BANKDATA/SEND/FAL\\FALTT11010400000015032502
				// dataFileName = FALDA11010400000015032501.XML
				writeFile(filePath, dataFileName, docRoot);
				// 把sendFileMap中的数据文件名改成“目录+文件名”的形式
				value[3] = fileShortPath + File.separator + dataFileName;
				haveMakeFile = true;
			}
			if (!haveMakeFile) {
				sbMsg.append("no data");
				return true;
			}
			// 写控制文件
			if (SYSTEM_CODE_FAL.equals(systemCode)) {
				writeFile(filePath, controlFileName, getControlFileContent(
						dataFileNames, systemCode));
				// 把控制文件名改成“目录+文件名”的形式
				controlFileName = fileShortPath + File.separator
						+ controlFileName;
			}
			String appFilePath = this.servletContext
					.getRealPath("/data/sendfile");
			if ("yes".equals(this.getFileGenDirZip())) {
				// 生成zip压缩文件(包含所在BOPT...文件夹)
				zipFileDir(sendFilePath, fileShortPath);
			} else {
				// 生成zip压缩文件(仅包含XML文件)
				zipFile(filePath, fileShortPath);
			}
			// 把存放报送文件的文件夹filePath整体拷贝到data\sendfile目录下
			this.copyFile(filePath, appFilePath);
			// 删除锁文件
			this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
			this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			// 更新数据状态为7
			resetRptDataStatus(sendFileMap, mpCannotCreate, sendBussIdFileName,
					fileShortPath, relatedFileType, sameBatchCA);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-makeSendFile", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0020", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	/**
	 * 回滚不能生成报文记录状态为审核通过
	 * 
	 * @param sendFileMap
	 * @param relatedFileType
	 * @param sameBatchCA
	 * @return boolean
	 */
	private boolean rollbackRptDataStatus(Map sendFileMap,
			String relatedFileType, boolean sameBatchCA) {
		try {
			// 循环sendFileMap，生成数据文件
			for (Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				String fileType = (String) entry.getKey();
				String tableId = DataUtil.getTableIdByFileType(fileType);
				// 取tableId对应的信息
				Object[] value = (Object[]) entry.getValue();
				// 得到数据记录的个数
				Integer recordNums = (Integer) value[4];
				// 记录个数为0则不生成
				if (recordNums == null || new Integer(0).equals(recordNums)) {
					continue;
				}
				// 取机构集
				List instCodes = (ArrayList) value[1];
				// 取需要生成的记录的业务主键集
				List businessIds = (ArrayList) value[2];
				// 根据tableId和机构取数据(按businessIds过滤后的数据)
				if (CollectionUtil.isNotEmpty(businessIds)) {
					for (int j = 0; j < businessIds.size(); j++) {
						dataDealService.updateRptData(new RptData(tableId,
								" dataStatus = " + DataUtil.SHYTG_STATUS_NUM,
								(String) businessIds.get(j), null, null, true));
					}
				} else if (CollectionUtil.isNotEmpty(instCodes)) {
					// 将记录状态置为6审核通过
					this.updateRptDataStatusByInstCodes(tableId, instCodes,
							fileType, relatedFileType,
							DataUtil.SHYTG_STATUS_NUM, sameBatchCA);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-rollbackRptDataStatus", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0020", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	private String zipFile(String zipScrPath, String fileName) {
		File file = new File(zipScrPath);// D:/BANKDATA/SEND/FAL\FALTT11010400000015032501
		if (file.exists()) {
			ZipUtil zu = new ZipUtil();
			String zipFileName = zipScrPath + ".zip";// D:/BANKDATA/SEND/FAL\FALTT11010400000015032501.zip
			zu.zip(zipScrPath, zipFileName);
			String zipFileNameDesc = zipScrPath + File.separator + fileName
					+ ".zip";// D:/BANKDATA/SEND/FAL\FALTT11010400000015032501\FALTT11010400000015032501.zip
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

	private String zipFileDir(String zipScrPath, String fileName) {
		File file = new File(zipScrPath);
		if (file.exists()) {
			ZipUtil zu = new ZipUtil();
			String zipFileName = zipScrPath + File.separator + fileName
					+ ".zip";
			zu.zipDir(zipScrPath, fileName, zipFileName);
			String zipFileNameDesc = zipScrPath + File.separator + fileName
					+ File.separator + fileName + ".zip";
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

	/**
	 * 根据报文生成路径来判断是否可生成报文
	 * 
	 * @param filePath
	 * @param user
	 * @return boolean 可生成true 不可生成false
	 */
	private boolean judgeCannotMakeFile(String filePath, User user) {
		try {
			String appFilePath = this.servletContext
					.getRealPath("/data/sendfile");
			if (filePath == null || StringUtil.isEmpty(appFilePath)) {
				sendFileMakeStatus = "报文生成路径不存在，生成失败！";
				this.request.setAttribute("sendFileMakeStatus",
						sendFileMakeStatus);
				basic();
				sendFileDownLoadList = null;
				// 在wl8.1下放入request
				this.request.setAttribute("businessIds", this.businessIds);
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("recordDescList", this.recordDescList);
				this.request.setAttribute("sendFileDownLoadList",
						this.sendFileDownLoadList);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				logManagerService.writeLog(request, user, "0020", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}
			boolean createBankDataLock = this.makeLockFile(filePath,
					TOKEN_LOCK_FILE_NAME);
			// 在报送文件路径下生成锁文件
			if (!createBankDataLock
					|| !this.makeLockFile(appFilePath, TOKEN_LOCK_FILE_NAME)) {
				if (createBankDataLock) {
					this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
				}
				sendFileMakeStatus = "令牌文件已存在，暂时不能生成报送文件！";
				this.request.setAttribute("sendFileMakeStatus",
						sendFileMakeStatus);
				basic();
				sendFileDownLoadList = null;
				// 在wl8.1下放入request
				this.request.setAttribute("businessIds", this.businessIds);
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("recordDescList", this.recordDescList);
				this.request.setAttribute("sendFileDownLoadList",
						this.sendFileDownLoadList);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				logManagerService.writeLog(request, user, "0020", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0020", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	/**
	 * 生成报文(按记录)
	 * 
	 * @return
	 */
	public String makeFile() {
		return makeFileCommon(ScopeConstants.SEND_FILE_MAP);
	}

	/**
	 * 生成报文(按表单)
	 * 
	 * @return
	 */
	public String makeFile2() {
		return makeFileCommon(ScopeConstants.SEND_FILE_MAP2);
	}

	private void basic() throws Exception {
		init();
		String configRptTitle = "";// 是否根据主报告行号汇总生成报文
		if (configMap != null && !configMap.isEmpty()) {
			configRptTitle = (String) configMap.get("config.fileGen.rpttitle");
		}
		if (checkInstCodesSameRptTitle(configRptTitle)) {
			makeRecordDescList();
			getRptDatas();
		} else {
			this.sendFileMakeStatus = "请选取主报告行号相同的机构来生成报文！";
		}
	}

	private void basic2() throws Exception {
		init();
		String configRptTitle = "";// 是否根据主报告行号汇总生成报文
		if (configMap != null && !configMap.isEmpty()) {
			configRptTitle = (String) configMap.get("config.fileGen.rpttitle");
		}
		if (checkInstCodesSameRptTitle(configRptTitle)) {
			makeRecordDescList2();
		} else {
			this.sendFileMakeStatus = "请选取主报告行号相同的机构来生成报文！";
		}
	}

	private void init() throws Exception {
		// 将参数配置项记录信息放在SESSION中
		this.initConfigParameters();
		// 受权机构
		getAuthInstList(authInstList);
		if (CollectionUtil.isEmpty(authInstList)) {
			return;
		} else {
			if (authInstList != null && this.orgConfigList != null) {
				for (int i = 0; i < authInstList.size(); i++) {
					Organization org = (Organization) authInstList.get(i);
					for (int j = 0; j < orgConfigList.size(); j++) {
						com.cjit.gjsz.filem.model.t_org_config orgConfig = (com.cjit.gjsz.filem.model.t_org_config) orgConfigList
								.get(j);
						if (org.getId().equals(orgConfig.getOrg_Id())) {
							org.setRpttitle(orgConfig.getRptTitle());
						}
					}
				}
			}
		}
		// 报告期
		if (StringUtil.isEmpty(this.buocMonth)) {
			this.buocMonth = DateUtils.getPreMonth();
		}
		buocMonthList = initBuocMonthSelectList();
		this.request.setAttribute("buocMonthList", buocMonthList);
		this.request.setAttribute("buocMonth", this.buocMonth);
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String userId = "";
		if (currentUser != null) {
			userId = currentUser.getId();
		}
		String notFileType = "";
		// 判断是否配置了仅在月底生成报文的表单，以及当日是否为该月最后一天
		if (StringUtil.isNotEmpty(this.configFileGenOnlyEndOfMonth)
				&& !"yes"
						.equalsIgnoreCase(this.configFileGemMonthManualEveryday)) {
			boolean canCreateMonthReport = true;
			if (!"yes".equals(this.configFileGemMonthFiveDays)) {
				int day = DateUtils.getDay(new Date());
				// 判断当日是否为该月第一天
				if (day != 1) {
					// 不是该月第一天，则不可生成月报报文
					canCreateMonthReport = false;
				}
			} else {
				String yyyyMMdd = DateUtils
						.serverCurrentDate(DateUtils.ORA_DATE_FORMAT);
				boolean isTop5WorkingDay = this.autoDealRptService
						.belongToTopFiveWorkingDay(yyyyMMdd);
				// 判断当日是否早于该月前5个工作日
				if (!isTop5WorkingDay) {
					// 当日晚于该月第五个工作日，不可生成月报报文
					canCreateMonthReport = false;
				}
			}
			if (!canCreateMonthReport) {
				String[] notFileTypes = this.configFileGenOnlyEndOfMonth
						.split(",");
				if (notFileTypes != null && notFileTypes.length > 0) {
					for (int i = 0; i < notFileTypes.length; i++) {
						notFileType += "'" + notFileTypes[i] + "',";
					}
					if (notFileType.length() > 0 && notFileType.endsWith(",")) {
						notFileType = notFileType.substring(0, notFileType
								.length() - 1);
					}
				}
			}
		}
		if ("ALL".equals(infoType)) {
			// RptTableInfo rti = new RptTableInfo();
			// rti.setOrderBys("1,2,3,4,5,6");
			// rti.setIsShow("1");
			busiDataInfoList = dataDealService
					.findRptBusiDataInfoWithAll(new RptBusiDataInfo("1", "1",
							"1"));
			rptTableList = new ArrayList();
			for (int i = 0; i < busiDataInfoList.size(); i++) {
				RptBusiDataInfo busiData = (RptBusiDataInfo) busiDataInfoList
						.get(i);
				RptTableInfo rti = new RptTableInfo(busiData.getBusiInfoName(),
						"1", "1");
				if (StringUtil.isNotEmpty(notFileType)) {
					rti.setNotFileType(notFileType);
				}
				rptTableList.addAll(dataDealService.findRptTableInfo(rti,
						userId));
			}
			// rptTableList = dataDealService.findRptTableInfo(rti, userId);
		} else {
			this.busiDataInfoList = dataDealService
					.findRptBusiDataInfoWithAll(new RptBusiDataInfo("1", "1",
							"1"));
			RptTableInfo rti = new RptTableInfo(this.getInfoTypeName(
					this.infoType, getBusiDataInfoList()), "1", "1");
			if (StringUtil.isNotEmpty(notFileType)) {
				rti.setNotFileType(notFileType);
			}
			rptTableList = dataDealService.findRptTableInfo(rti, userId);
		}
		// 把报表列信息和查询SQL放在SESSION中
		if (this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP) == null) {
			Map[] map = dataDealService
					.initRptColumnSqlMapNew(largestColumnNum);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP, map[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP, map[1]);
		}
		// 是否根据主报告行号汇总生成报文
		this.request.setAttribute("configFileGenRptTitle",
				this.configFileGenRptTitle);
		if ("yes".equals(this.configFileGenRptTitle)) {
			// 需要根据主报告行号汇总生成报文
			if (authInstList != null) {
				for (int i = 0; i < authInstList.size(); i++) {
					Organization org = (Organization) authInstList.get(i);
					String showName = org.getName()
							+ " 【主报告行号："
							+ (org.getRpttitle() == null ? "未配置" : org
									.getRpttitle().trim()) + "】";
					org.setShowName(showName);
				}
			}
		}
		// 设置默认全选所有单据
		if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)) {
			if (rptTableList != null) {
				fileTypes = new String[rptTableList.size()];
				for (int i = 0; i < rptTableList.size(); i++) {
					fileTypes[i] = ((RptTableInfo) rptTableList.get(i))
							.getFileType();
				}
			}
		}
		// 构造单据详细信息
		if (rptTableList != null) {
			// Map mapTableDesc = (HashMap) this
			// .getFieldFromSession(MAP_TABLE_DESC + userId);
			// if (mapTableDesc != null) {
			// for (Iterator i = rptTableList.iterator(); i.hasNext();) {
			// RptTableInfo rptTable = (RptTableInfo) i.next();
			// rptTable.setListDescription((ArrayList) mapTableDesc
			// .get(rptTable.getFileType()));
			// }
			// } else {
			// mapTableDesc = new HashMap();
			// List rptDataStatusList = null;
			// if ("Z".equals(infoType)
			// && CollectionUtils.isNotEmpty(busiDataInfoList)) {
			// rptDataStatusList = new ArrayList();
			// for (int i = 0; i < busiDataInfoList.size(); i++) {
			// RptBusiDataInfo busiData = (RptBusiDataInfo) busiDataInfoList
			// .get(i);
			// if (!"Z".equals(busiData.getBusiInfoID())) {
			// rptDataStatusList
			// .addAll(dataDealService
			// .findRptDataStatusCountByTableIdAndInstCode(
			// DataUtil
			// .getTableIdByFileType(busiData
			// .getBusiInfoID()),
			// null, null, null, userId,
			// this.linkBussType));
			// }
			// }
			// } else {
			// rptDataStatusList = dataDealService
			// .findRptDataStatusCountByTableIdAndInstCode(
			// DataUtil
			// .getTableIdByFileType(this.infoType),
			// null, null, null, userId, this.linkBussType);
			// }
			// for (Iterator i = rptTableList.iterator(); i.hasNext();) {
			// RptTableInfo rptTable = (RptTableInfo) i.next();
			// List listDesc = getListDesc(rptTable.getTableId(), null,
			// null, rptTable.getFileType(), "1,2,3,4,5,6",
			// userId, rptDataStatusList);
			// rptTable.setListDescription(listDesc);
			// mapTableDesc.put(rptTable.getFileType(), listDesc);
			// }
			// this.addFieldToSession(MAP_TABLE_DESC + userId, mapTableDesc);
			// }
		}
		this.request.setAttribute("interfaceVer", this.interfaceVer);
		this.request.setAttribute("busiDataInfoList", this
				.getBusiDataInfoList());
	}

	/**
	 * <p>
	 * 方法名称: initConfigParameters|描述: 将参数配置项记录信息放在SESSION中
	 * </p>
	 * 
	 * @throws Exception
	 */
	private void initConfigParameters() throws Exception {
		if ("yes".equalsIgnoreCase(this.configIsCluster)) {
			configMap = userInterfaceConfigService.initConfigParameters();
			orgConfigList = autoDealRptService.findAllOrgConfigList("", "", "");
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
		} else {
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache != null) {
				configMap = (Map) cache.get("configMap");
				orgConfigList = (List) cache.get("orgConfigList");
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			} else {
				configMap = (HashMap) this
						.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
				if (configMap == null) {
					configMap = userInterfaceConfigService
							.initConfigParameters();
					this.addFieldToSession(
							ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
							configMap);
				}
				if (CollectionUtils.isEmpty(orgConfigList)) {
					orgConfigList = autoDealRptService.findAllOrgConfigList("",
							"", "");
				}
			}
		}
		this.setConfigParametersBaseList(configMap);
		this.request.setAttribute("interfaceVer", this.interfaceVer);
		// 是否在报文生成页面能够手工选择单据 yes/no 默认no
		this.request.setAttribute("configFileGenChooseTables",
				this.configFileGenChooseTables);
		// 是否在报文生成页面提供预校验功能 yes/no 默认
		this.request.setAttribute("configFileGenPreVerify",
				this.configFileGenPreVerify);
	}

	/**
	 * 报文生成（按记录）－添加记录
	 * 
	 * @throws Exception
	 */
	private void getRptDatas() throws Exception {
		List newRecordlist = null;
		StringBuffer sb = new StringBuffer();
		// 根据表名查找列信息，用于打印报表表头
		if (fileType == null) {
			rptColumnList = new ArrayList();
		} else {
			this.request.setAttribute("selectFileType", fileType);
			String tableId = DataUtil.getTableIdByFileType(fileType);
			// 拼查询列
			String columns = this.getColumnsSql(tableId, fileType);
			// 获取符合生成条件的列表数据
			if (instCodes != null) {
				Map orgMap = new HashMap();
				List insts = new ArrayList();
				getAuthInstList(insts);
				for (int i = 0; i < this.instCodes.length; i++) {
					for (int j = 0; j < insts.size(); j++) {
						Organization org = (Organization) insts.get(j);
						if (org.getId().equals(this.instCodes[i])) {
							orgMap.put(this.instCodes[i], org.getName());
							break;
						}
					}
				}
				String dataStatusCondition = null;
				// 报文生成的可选记录中，只显示状态为审核通过的
				String searchCondition = null;
				dataStatusCondition = " t.datastatus = "
						+ DataUtil.SHYTG_STATUS_NUM + " and t.fileType = '"
						+ fileType + "' ";
				for (int i = 0; i < instCodes.length; i++) {
					newRecordlist = dataDealService.findRptData(new RptData(
							tableId, columns, instCodes[i],
							dataStatusCondition, null, null, null,
							searchCondition));
					// 关联数据信息需要过滤
					if (configMap != null && !configMap.isEmpty()) {
						String relatedFileType = (String) configMap
								.get("config.related.filetype");
						if (this.fileType != null
								&& relatedFileType.indexOf(this.fileType) > -1) {
							removeRecordRelated(newRecordlist, tableId,
									this.fileType);
						}
					}
					if (CollectionUtil.isNotEmpty(newRecordlist))
						recordList.addAll(newRecordlist);
				}
				if (CollectionUtil.isNotEmpty(newRecordlist)) {
					// 看看有哪些还不符合条件的
					dataStatusCondition = " (t.datastatus < "
							+ DataUtil.SHYTG_STATUS_NUM
							+ " and t.datastatus <> "
							+ DataUtil.DELETE_STATUS_NUM
							+ ") and t.fileType = '" + fileType + "' ";
					List list = new ArrayList();
					for (int i = 0; i < instCodes.length; i++) {
						list = dataDealService.findRptData(new RptData(tableId,
								columns, instCodes[i], dataStatusCondition,
								null, null, null, searchCondition));
						String orgName = (String) orgMap.get(instCodes[i]);
						setValue(orgName, list, sb);
					}
				}
			}
		}
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
			if (data != null) {
				if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)
						&& String.valueOf(DataUtil.SHYTG_STATUS_NUM).equals(
								data.getDataStatus())) {
					continue;
				} else {
					// 当可以在报文生成页面选择单据时
					List sendList = dataDealService.findRptSendCommit(null,
							data.getBusinessId(), null, null, -1);
					if (sendList.size() == 0) {
						removeData.add(rptData);
					}
				}
			} else {
				removeData.add(rptData);
			}
		}
		newRecordlist.removeAll(removeData);
	}

	private void setValue(String instName, List list, StringBuffer sb) {
		if (CollectionUtil.isNotEmpty(list)) {
			if (sb.length() == 0) {
				sb.append("注意：\\n");
			} else {
				sb.append("\\n");
			}
			// RptTableInfo rpt = new RptTableInfo();
			// rpt.setTableId(tableId);
			// List table = dataDealService.findRptTableInfo(rpt);
			// rpt = (RptTableInfo) table.get(0);
			RptTableInfo rpt = dataDealService.findRptTableInfoById(tableId,
					fileType);
			sb.append("机构[").append(instName).append("]所选[").append(
					rpt.getInfoType()).append("-").append(rpt.getTableName())
					.append("]中有[").append(list.size()).append("]条记录还未审核通过。");
			// sb.append("注意： [" + rpt.getTableName() + "] 中有 [" + list.size()
			// + "] 条记录还未审核通过。");
			message = sb.toString();
		}
	}

	private void setValueForm(long nNoPass, long nNoAudit, long nNoGen,
			StringBuffer sb, String tableId, String fileType,
			String relatedFileType) {
		// if (nNoPass > 0 || nNoAudit > 0 || nNoGen > 0) {
		if (nNoPass > 0 || nNoAudit > 0) {
			if (sb.length() == 0) {
				sb.append("注意：\\n");
			} else {
				sb.append("\\n");
			}
			RptTableInfo rpt = dataDealService.findRptTableInfoById(tableId,
					fileType);
			sb.append("所选[").append(rpt.getInfoType()).append("-").append(
					rpt.getTableName()).append("]中");
			if (nNoPass > 0 || nNoAudit > 0) {
				sb.append("有[").append((nNoPass + nNoAudit)).append(
						"]条记录还未审核通过 ");
			}
			if (nNoGen > 0) {
				if (relatedFileType.indexOf(fileType) < 0) {
					// 签约信息
					// sb.append("有[").append(nNoGen)
					// .append("]条记录存在未成功报送删除的下游单据 ");
				} else {
					// 非签约信息
				}
			}
		}
	}

	private void makeRecordDescList() {
		recordDescList.clear();
		Map sendFileMap = (Map) this
				.getFieldFromSession(ScopeConstants.SEND_FILE_MAP);
		if (sendFileMap == null) {
			return;
		}
		String fileType = null;
		String tableId = null;
		String instCode = null;
		for (Iterator i = sendFileMap.keySet().iterator(); i.hasNext();) {
			// 取报表物理表名tableId
			fileType = (String) i.next();
			tableId = DataUtil.getTableIdByFileType(fileType);
			// 取tableId对应的信息
			Object[] value = (Object[]) sendFileMap.get(fileType);
			// 取机构集
			List instCodes = (ArrayList) value[1];
			// 取需要生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			// 循环机构
			StringBuffer instNames = new StringBuffer("");
			// List rptDatas = new ArrayList();
			for (Iterator j = instCodes.iterator(); j.hasNext();) {
				instCode = (String) j.next();
				Organization org = new Organization();
				org.setId(instCode);
				instNames.append(
						organizationService.getOrganization(org).getName())
						.append(",");
			}
			RptTableInfo t = dataDealService.findRptTableInfoById(tableId,
					fileType);
			int nNoSend = 0;// 因未能生成申报号而无法发送的报文条数
			if (this.mpCannotCreate != null) {
				List lt = (List) this.mpCannotCreate.get(tableId);
				if (lt != null) {
					nNoSend = lt.size();
				}
			}
			t.setDescription("包含以下机构的数据:"
					+ instNames.substring(0, instNames.length() - 1) + "；共"
					+ (businessIds.size() - nNoSend) + "条数据信息");
			t.setIsShow(null);
			recordDescList.add(t);
		}
	}

	private void makeRecordDescList2() {
		recordDescList.clear();
		Map sendFileMap2 = (Map) this
				.getFieldFromSession(ScopeConstants.SEND_FILE_MAP2);
		if (sendFileMap2 == null) {
			return;
		}
		String fileType = null;
		String tableId = null;
		for (Iterator i = sendFileMap2.keySet().iterator(); i.hasNext();) {
			// 取报表物理表名tableId
			fileType = (String) i.next();
			tableId = DataUtil.getTableIdByFileType(fileType);
			// 取tableId对应的信息
			Object[] value = (Object[]) sendFileMap2.get(fileType);
			// 取机构集
			List instCodes = (ArrayList) value[1];
			RptTableInfo t = dataDealService.findRptTableInfoById(tableId,
					fileType);
			t.setDescription("包含所选" + instCodes.size() + "家机构数据共" + value[4]
					+ "条数据信息");
			t.setIsShow(null);
			recordDescList.add(t);
		}
	}

	/**
	 * 将生成报文的记录的数据状态更新为7已生成 同时写入相应的批次号 向T_RPT_SEND_COMMIT报文报送记录表中添加相应记录
	 * 
	 * @param sendFileMap
	 * @param noSendDataMap
	 * @param sendBussIdFileName
	 * @param packName
	 * @param relatedFileType
	 * @param sameBatchCA
	 */
	private void resetRptDataStatus(Map sendFileMap, Map noSendDataMap,
			Map sendBussIdFileName, String packName, String relatedFileType,
			boolean sameBatchCA) {
		String fileType = null;
		String tableId = null;
		String businessId = null;
		// 循环sendFileMap，生成数据文件名，将之添加到sendFileMap中和dataFileNames中
		for (Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			// 取tableId对应的信息
			Object[] value = (Object[]) entry.getValue();
			// 取报表物理表名tableId
			fileType = (String) entry.getKey();
			tableId = DataUtil.getTableIdByFileType(fileType);
			// 取生成报文zip压缩包名称和xml文件名
			String packfilename = (String) value[3];
			String packname = "";// zip压缩包名称
			String filename = "";// xml文件名
			List noSendBusinessIdList = null;
			if (noSendDataMap != null) {
				noSendBusinessIdList = (List) noSendDataMap.get(tableId);
			}
			if (packfilename != null) {
				if (packfilename.indexOf("\\") > -1) {
					packfilename = StringUtil.replace(packfilename, "\\", "/");
				}
				String[] packfilenames = null;
				if (packfilename.indexOf("/") > -1) {
					packfilenames = packfilename.split("/");
					if (packfilenames != null && packfilenames.length == 2) {
						packname = packfilenames[0];
						filename = packfilenames[1];
					}
				}
			}
			boolean batchUpdateStatus = true; // 是否需批量修改状态
			// 取已生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			if (CollectionUtils.isEmpty(businessIds)) {
				List instCodes = (ArrayList) value[1];
				if (CollectionUtils.isNotEmpty(instCodes)) {
					String searchCondition = this
							.getAddFileGenSearchConditonSQL(tableId, fileType,
									relatedFileType, sameBatchCA,
									this.buocMonth);
					if (instCodes.size() <= 500) {
						String[] instCodess = new String[instCodes.size()];
						instCodes.toArray(instCodess);
						// 当业务ID数小于等于500时，直接查询对应数据list
						dataDealService.updateRptDataStatusByInstCodes(tableId,
								" t.datastatus = " + DataUtil.YSC_STATUS_NUM,
								DataUtil.DSC_STATUS_NUM, instCodess,
								searchCondition, null);
					} else if (instCodes.size() > 500) {
						// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
						int c = 0;
						int nn = instCodes.size() / 500;
						for (; c < nn; c++) {
							List tempInstCodes = instCodes.subList(c * 500,
									(c + 1) * 500);
							String[] instCodess = new String[tempInstCodes
									.size()];
							tempInstCodes.toArray(instCodess);
							dataDealService.updateRptDataStatusByInstCodes(
									tableId, " t.datastatus = "
											+ DataUtil.YSC_STATUS_NUM,
									DataUtil.DSC_STATUS_NUM, instCodess,
									searchCondition, null);
						}
						List tempInstCodes = instCodes.subList(c * 500,
								instCodes.size());
						if (tempInstCodes != null && tempInstCodes.size() > 0) {
							String[] instCodess = new String[tempInstCodes
									.size()];
							tempInstCodes.toArray(instCodess);
							dataDealService.updateRptDataStatusByInstCodes(
									tableId, " t.datastatus = "
											+ DataUtil.YSC_STATUS_NUM,
									DataUtil.DSC_STATUS_NUM, instCodess,
									searchCondition, null);
						}
					}
					batchUpdateStatus = false;
				}
			}
			// 循环业务主键，重置状态
			List dataStatusList = new ArrayList();
			List insertSendList = new ArrayList();
			for (Iterator j = businessIds.iterator(); j.hasNext();) {
				businessId = (String) j.next();
				filename = (String) sendBussIdFileName.get(businessId);
				if (noSendBusinessIdList != null
						&& noSendBusinessIdList.size() > 0) {
					if (noSendBusinessIdList.contains(businessId)) {
						continue;
					}
				}
				// 未上报过 记录状态:7-已生成
				if (batchUpdateStatus) {
					dataStatusList
							.add(new RptData(tableId, null, businessId, null,
									String.valueOf(DataUtil.YSC_STATUS_NUM),
									true));
				}
				// 插入send commit信息
				insertSendList.add(createSendMap(tableId, businessId, packname,
						filename, 0));
				if (insertSendList.size() > DataUtil.BATCH_SIZE_MAX) {
					if (batchUpdateStatus) {
						dataDealService.updateRptDatastatus(tableId,
								dataStatusList);
						dataStatusList.clear();
					}
					dataDealService.insertRptSendCommitBatch(insertSendList);
					insertSendList.clear();
				}
				// 如果当前生成报文的记录为"国内外汇贷款（含外债转贷款）信息"
				// 则根据businessno字段到"境外担保项下境内贷款"表中查询有无对应信息
				// 存在的话更新其DOFOEXLOCODE（国内外汇贷款编号）为当前生成的DOFOEXLOCODE值
				if (DataUtil.CFA_SELF_TABLE_C.equalsIgnoreCase(tableId)) {
					dataDealService.autoUpdateDofoecloCode(tableId, businessId);
				}
			}
			if (insertSendList.size() != 0) {
				if (batchUpdateStatus) {
					dataDealService
							.updateRptDatastatus(tableId, dataStatusList);
					dataStatusList.clear();
				}
				dataDealService.insertRptSendCommitBatch(insertSendList);
				insertSendList.clear();
			}
		}
		if (businessId == null && StringUtil.isNotEmpty(packName)) {
			dataDealService.insertRptSendCommit("controlFile", "-", packName,
					"", 0);
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

	/**
	 * 生成控制文件名
	 * 
	 * @param instCode
	 * @param curDate
	 * @param serialNo
	 * @param systemCode
	 * @return systemCode
	 */
	private String getControlFileName(String curDate,
			Organization currentUserOrg, String serialNo, String rptTitle) {
		// 根据用户所在的机构号，去映射表中寻找对应的申报号
		if (StringUtil.isEmpty(rptTitle)) {
			String orgId = currentUserOrg.getId();
			rptTitle = organizationService.findRptTitle(orgId);
		}
		if (rptTitle == null || "null".equals(rptTitle)
				|| rptTitle.length() != 12) {
			log.error("FileGenAction-getControlFileName-主报告行号未配置正确！");
			return null;
		}
		return SYSTEM_CODE_FAL + CONTROL_FILE_TYPE_CODE + rptTitle + curDate
				+ serialNo + SEND_FILE_SUFFIX;
	}

	/**
	 * 生成数据文件名
	 * 
	 * @param fileType
	 * @param instCode
	 * @param curDate
	 * @param serialNo
	 * @param systemCode
	 * @return systemCode
	 */
	private String getDataFileName(String fileType, String curDate,
			Organization currentUserOrg, String serialNo, String rptTitle) {
		// 根据用户所在的机构号，去映射表中寻找对应的申报号
		if (StringUtil.isEmpty(rptTitle)) {
			String orgId = currentUserOrg.getId();
			rptTitle = organizationService.findRptTitle(orgId);
		}
		if (rptTitle == null || rptTitle.length() != 12) {
			log.error("FileGenAction-getControlFileName-主报告行号未配置正确！");
			return null;
		}
		// 根据tableId查找对应的类型代码（一位字母）
		return SYSTEM_CODE_FAL + fileType + rptTitle + curDate + serialNo
				+ SEND_FILE_SUFFIX;
	}

	/**
	 * 获取一张报表中某个特定机构的全部数据，并将不包含在所选业务主键集中的数据清除
	 * 
	 * @param tableId
	 *            报表ID
	 * @param businessIds
	 *            业务主键集
	 * @return List
	 */
	private List getRptDatas(String tableId, List businessIds, String fileType,
			List rptColumnList) {
		List rptList = new ArrayList();
		if (CollectionUtils.isEmpty(rptColumnList)) {
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType));
		}
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
				if ("BUSINESSNO".equalsIgnoreCase(columnId)) {
					columns.append("t.").append(columnId).append(
							" as businessNo,");
				} else {
					columns.append("t.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
			}
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		String strColumns = columns.toString().substring(0,
				columns.toString().length() - 1);
		// 判断传入生成报文的业务ID个数
		if (businessIds != null) {
			if (businessIds.size() <= 500) {
				// 当业务ID数小于等于500时，直接查询对应数据list
				rptList = dataDealService.findRptDataByTableIdAndBusinessIds(
						tableId, businessIds, strColumns);
			} else if (businessIds.size() > 500) {
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int i = 0;
				int nn = businessIds.size() / 500;
				for (; i < nn; i++) {
					List tempIds = businessIds.subList(i * 500, (i + 1) * 500);
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndBusinessIds(tableId,
									tempIds, strColumns);
					if (tempRptDatas != null && tempRptDatas.size() > 0) {
						rptList.addAll(tempRptDatas);
					}
				}
				List tempIds = businessIds.subList(i * 500, businessIds.size());
				if (tempIds != null && tempIds.size() > 0) {
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndBusinessIds(tableId,
									tempIds, strColumns);
					if (tempRptDatas != null && tempRptDatas.size() > 0) {
						rptList.addAll(tempRptDatas);
					}
				}
			}
		}
		return rptList;
	}

	private List getRptDatasByInstCodes(String tableId, List instCodes,
			String fileType, String relatedFileType, List rptColumnList,
			boolean sameBatchCA) {
		List rptList = new ArrayList();
		if (CollectionUtils.isEmpty(rptColumnList)) {
			rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
							fileType));
		}
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
				if ("BUSINESSNO".equalsIgnoreCase(columnId)) {
					columns.append("t.").append(columnId).append(
							" as businessNo,");
				} else {
					columns.append("t.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
			}
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		String strColumns = columns.toString().substring(0,
				columns.toString().length() - 1);
		String searchCondition = this.getAddFileGenSearchConditonSQL(tableId,
				fileType, relatedFileType, sameBatchCA, this.buocMonth);
		String orderBy = null;
		if (StringUtil
				.isNotEmpty(DataUtil.getRptNoColumnIdByFileType(fileType))) {
			orderBy = " t." + DataUtil.getRptNoColumnIdByFileType(fileType)
					+ " asc ";
			if (StringUtil.isNotEmpty(DataUtil
					.getByeRptNoColumnIdByFileType(fileType))) {
				orderBy += ", t."
						+ DataUtil.getByeRptNoColumnIdByFileType(fileType)
						+ " asc ";
			}
		} else {
			orderBy = " t.businessId asc ";
		}
		// 判断传入生成报文的业务ID个数
		if (instCodes != null) {
			if (instCodes.size() <= 500) {
				// 当业务ID数小于等于500时，直接查询对应数据list
				rptList = dataDealService.findRptDataByTableIdAndInstCodes(
						tableId, fileType, instCodes, String
								.valueOf(DataUtil.SHYTG_STATUS_NUM),
						strColumns, searchCondition, orderBy);
			} else if (instCodes.size() > 500) {
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int i = 0;
				int nn = instCodes.size() / 500;
				for (; i < nn; i++) {
					List tempInstCodes = instCodes.subList(i * 500,
							(i + 1) * 500);
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndInstCodes(tableId,
									fileType, tempInstCodes,
									String.valueOf(DataUtil.SHYTG_STATUS_NUM),
									strColumns, searchCondition, orderBy);
					if (tempRptDatas != null && tempRptDatas.size() > 0) {
						rptList.addAll(tempRptDatas);
					}
				}
				List tempInstCodes = instCodes.subList(i * 500, instCodes
						.size());
				if (tempInstCodes != null && tempInstCodes.size() > 0) {
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndInstCodes(tableId,
									fileType, tempInstCodes,
									String.valueOf(DataUtil.SHYTG_STATUS_NUM),
									strColumns, searchCondition, orderBy);
					if (tempRptDatas != null && tempRptDatas.size() > 0) {
						rptList.addAll(tempRptDatas);
					}
				}
			}
		}
		return rptList;
	}

	/**
	 * 将记录状态置为99待生成，或在生成出现异常时将状态置成6审核通过
	 * 
	 * @param tableId
	 * @param instCodes
	 * @param fileType
	 * @param relatedFileType
	 * @param dataStatus
	 *            目标状态
	 */
	private void updateRptDataStatusByInstCodes(String tableId, List instCodes,
			String fileType, String relatedFileType, int dataStatus,
			boolean sameBatchCA) {
		String searchCondition = this.getAddFileGenSearchConditonSQL(tableId,
				fileType, relatedFileType, sameBatchCA, this.buocMonth);
		int whereDataStatus = DataUtil.SHYTG_STATUS_NUM;
		if (dataStatus == DataUtil.SHYTG_STATUS_NUM) {
			whereDataStatus = DataUtil.DSC_STATUS_NUM;
		}
		// 判断传入生成报文的业务ID个数
		if (instCodes != null) {
			if (instCodes.size() <= 500) {
				String[] instCodess = new String[instCodes.size()];
				instCodes.toArray(instCodess);
				// 当业务ID数小于等于500时，直接查询对应数据list
				dataDealService.updateRptDataStatusByInstCodes(tableId,
						" datastatus = " + dataStatus, whereDataStatus,
						instCodess, searchCondition, null);
			} else if (instCodes.size() > 500) {
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int i = 0;
				int nn = instCodes.size() / 500;
				for (; i < nn; i++) {
					List tempInstCodes = instCodes.subList(i * 500,
							(i + 1) * 500);
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + dataStatus, whereDataStatus,
							instCodess, searchCondition, null);
				}
				List tempInstCodes = instCodes.subList(i * 500, instCodes
						.size());
				if (tempInstCodes != null && tempInstCodes.size() > 0) {
					String[] instCodess = new String[tempInstCodes.size()];
					tempInstCodes.toArray(instCodess);
					dataDealService.updateRptDataStatusByInstCodes(tableId,
							" t.datastatus = " + dataStatus, whereDataStatus,
							instCodess, searchCondition, null);
				}
			}
		}
	}

	/**
	 * 根据报表数据生成报文中代表该数据的记录元素
	 * 
	 * @param tableId
	 *            报表ID
	 * @param fileType
	 *            文件类型
	 * @param curDate
	 *            当前日期
	 * @param records
	 * @param columns
	 *            报表所有列信息
	 * @param rptData
	 *            代表报表数据的BEAN
	 * @param lt1
	 *            无法生成申报号的单据个数(超出日生成报文限制)
	 * @param lt2
	 *            无法生成申报号的单据个数(单据所属机构对应申报号码或机构ID配置错误)
	 * @param userId
	 * @return Element
	 */
	private Element addRecord(String tableId, String fileType, String curDate,
			Element records, String subElementName, List columns,
			RptData rptData, List lt1, List lt2, String userId,
			Map mapInstFileRptNo) throws Exception {
		// rec为代表本条数据的XML元素节点
		Element rec = records.addElement(subElementName);
		try {
			String actionType = rptData.getActionType();
			String instCode = rptData.getInstCode();
			String businessId = rptData.getBusinessId();
			String businessNo = rptData.getBusinessNo();
			String objCode = null;
			String buocMonth = null;
			String configRptNo = rptData.getConfigRptNo();// 记录所属机构配置的申报号码
			// 第一次循环数据列，获取数据银行核心业务编号的值
			for (Iterator i = columns.iterator(); i.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) i.next();
				if (DataUtil.CFA_SELF_TABLE_D.equalsIgnoreCase(tableId)
						&& "DOFOEXLOCODE".equalsIgnoreCase(column.getColumnId()
								.trim())) {
					String dofoexloCode = (String) BeanUtils.getValue(rptData,
							column.getAliasColumnId());
					if (StringUtil.isEmpty(dofoexloCode)) {
						dofoexloCode = dataDealService.autoUpdateDofoecloCode(
								tableId, businessId);
						if (StringUtil.isNotEmpty(dofoexloCode)) {
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), dofoexloCode);
						}
					}
				}
				if (StringUtil.isEmpty(businessNo)
						&& "BUSINESSNO".equalsIgnoreCase(column.getColumnId()
								.trim())) {
					businessNo = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
				}
				if (StringUtil.isEmpty(objCode)
						&& "OBJCODE".equalsIgnoreCase(column.getColumnId()
								.trim())) {
					objCode = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
				}
				if (StringUtil.isEmpty(buocMonth)
						&& "BUOCMONTH".equalsIgnoreCase(column.getColumnId()
								.trim())) {
					buocMonth = (String) BeanUtils.getValue(rptData, column
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
							.addElement((String) InnerTableSign
									.getRootSignMap().get(innerTableId));
					addInnerRecords(column.getColumnId(), businessId,
							innerTableElementRoot, (String) InnerTableSign
									.getSubSignMap().get(innerTableId), userId);
					continue;
				}
				String data = null;
				// 判断当前字段是否为主申报号字段
				if ("SNOCODE".equalsIgnoreCase(column.getColumnId())) {
					// 数据自编码
					// 该编码用于标识金融机构报送的每一条数据，按给定规则编码，便于进行增
					// 删改查等操作。建议规则：机构代码（12 位，不足12 位补a）+报表代码（5
					// 位，不足5 位补a）+报送期（6 位）+顺序号（7 位，从0000001 开始增加）。
					// 12+5+6+7
					String rptFalNoColumnId = column.getColumnId();
					String rptFalNo = (String) BeanUtils.getValue(rptData,
							column.getAliasColumnId());
					// 申报号码为空，需生成申报号
					boolean createNewRptNo = false;
					if (StringUtil.isEmpty(rptFalNo)
							|| StringUtil.isNotEmpty(SelfDataVerify
									.verifyFalSnoCode(rptFalNo, fileType))) {
						data = userInterfaceConfigService.createAutokey(
								objCode, buocMonth, fileType);
						createNewRptNo = true;
						// // 获取信息的交易日期
						// String tradeDate = null;
						// for (Iterator iterator = columns.iterator(); iterator
						// .hasNext();) {
						// RptColumnInfo column1 = (RptColumnInfo) iterator
						// .next();
						// // FIXME: 需确认各种报文对应的交易日期字段
						// if ("TRADEDATE".equals(column1.getColumnId())) {
						// tradeDate = (String) BeanUtils.getValue(
						// rptData, column1.getAliasColumnId());
						// break;
						// }
						// }
						// if (tradeDate != null && tradeDate.length() == 8) {
						// String key = DataUtil
						// .getSelfBussTypeCodeByFileType(fileType)
						// + configRptNo + tradeDate;
						// if (mapInstFileRptNo != null
						// && mapInstFileRptNo.containsKey(key)) {
						// String currRptNo = (String) mapInstFileRptNo
						// .get(key);
						// if (StringUtil.isNotEmpty(currRptNo)
						// && currRptNo.length() == 28) {
						// String lastNumber = currRptNo.substring(22,
						// 28);
						// int ints = Integer.valueOf(lastNumber)
						// .intValue();
						// if (ints >= 1 && ints < 999999) {
						// int max = ints + 1;
						// String finalChar = StringNumFormat
						// .getFormatLong(max, "000000");
						// data = key + finalChar;
						// mapInstFileRptNo.put(key, data);
						// createNewRptNo = true;
						// }
						// }
						// } else {
						// String tempTradeDate = tradeDate
						// .substring(0, 4)
						// + "-"
						// + tradeDate.substring(4, 6)
						// + "-"
						// + tradeDate.substring(6, 8);
						// Date tradedate = null;
						// try {
						// tradedate = DateUtils.stringToDate(
						// tempTradeDate,
						// DateUtils.ORA_DATES_FORMAT);
						// } catch (Exception e1) {
						// e1.printStackTrace();
						// }
						// try {
						// // 生成申报号码
						// data = userInterfaceConfigService
						// .createAutokey(new KeyInfo(
						// instCode,
						// tradedate,
						// tableId,
						// DataUtil
						// .getSelfBussTypeCodeByFileType(fileType),
						// rptCfaNoColumnId,
						// actionType));
						// if (data == null) {
						// // 超出日生成报文限制，未能生成申报号码
						// lt1.add(businessId);
						// records.remove(rec);
						// return null;
						// } else if ("ErrorOrgId".equals(data)) {
						// // 单据所属机构对应申报号码或机构ID配置错误
						// lt2.add(businessId);
						// records.remove(rec);
						// return null;
						// }
						// if (mapInstFileRptNo != null) {
						// mapInstFileRptNo.put(key, data);
						// }
						// createNewRptNo = true;
						// } catch (Exception e) {
						// log.error("FileGenAciton-addRecord", e);
						// e.printStackTrace();
						// throw e;
						// }
						// }
						// }
						// if (StringUtil.isEmpty(data)
						// && TableIdRela.getZjxyMap().get(fileType) == null
						// && StringUtil.isNotEmpty(businessNo)) {
						// // 根据业务编号查询上游报文的申报号码
						// data = dataDealService.findRptNoByBusinessNo(
						// rptCfaNoColumnId, tableId, businessNo);
						// this.updateRptCfaNo(tableId, rptCfaNoColumnId,
						// data, businessNo, businessId);
						// }
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								data);
					}
					// 生成报文时已存在申报号
					else {
						data = rptFalNo;
					}
					if (createNewRptNo) {
						this.updateRptCfaNo(tableId, rptFalNoColumnId, data,
								businessNo, businessId);
					}
				}
				// 判断当前字段是否为辅申报号字段（变动编号/履约编号/终止支付编号/付息编号）
				if (("CHANGENO".equalsIgnoreCase(column.getColumnId()) && ("AR"
						.equals(fileType)
						|| "AS".equals(fileType)
						|| "CB".equals(fileType)
						|| "DB".equals(fileType) || "EB".equals(fileType)))
						|| ("COMPLIANCENO".equalsIgnoreCase(column
								.getColumnId()) && "BC".equals(fileType))
						|| ("TERPAYCODE".equalsIgnoreCase(column.getColumnId()) && "FB"
								.equals(fileType))
						|| ("INPAYCODE".equalsIgnoreCase(column.getColumnId()) && "FC"
								.equals(fileType))) {
					data = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
					if (StringUtil.isEmpty(data)
							&& StringUtil.isNotEmpty(businessNo)
							&& StringUtil.isNotEmpty(businessId)) {
						data = dataDealService.findIndexCodeForSelf(tableId,
								fileType, column.getColumnId(), null,
								businessNo);
						dataDealService.updateRptData(new RptData(tableId, " "
								+ column.getColumnId() + " = '" + data + "' ",
								businessId, null, null, true));
					}
				}
				// 否则根据别名直接找到数据
				else {
					data = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
					// 当数据为带有小数位的数值类型，且data起始字符为小数点时，在其前面加个0
					if (column.getDataType().startsWith("n")
							&& !column.getDataType().endsWith(",0")) {
						if (data != null && !"".equals(data)) {
							if (data.startsWith(".")) {
								data = "0" + data;
							} else if (data.startsWith("-.")) {
								data = data.replaceAll("-.", "-0.");
							}
							String[] nDataTypes = column.getDataType().split(
									",");
							if (nDataTypes != null && nDataTypes.length == 4) {
								if (data.indexOf(".") < 0) {
									data += ".";
									for (int n = 0; n < Integer.valueOf(
											nDataTypes[3]).intValue(); n++) {
										data += "0";
									}
								} else {
									String[] datas = new String[2];
									datas[0] = data.substring(0, data
											.indexOf("."));
									datas[1] = data.substring(
											data.indexOf(".") + 1, data
													.length());
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
						|| "BUSINESSNO".equals(column.getColumnId())
						|| !"1".equals(column.getIsReport())) {
					continue;
				}
				// 给rec添加子元素，元素名为列名
				Element e = rec.addElement(column.getColumnId());
				e.setText(data == null ? "" : DataUtil.replaceForXml(data));
			}
			if (StringUtil.isNotEmpty(tableId) && rptData.getTableId() == null) {
				rptData.setTableId(tableId);
			}
			if (StringUtil.isNotEmpty(fileType)
					&& rptData.getFileType() == null) {
				rptData.setFileType(fileType);
			}
			// 手工生成报文时，不再向数据日志表中添加记录 lihaiboa 20130829
			// autoDealRptService.saveRptLogInfoData(rptData, "send", userId,
			// DateUtils.serverCurrentTimeStamp(), columns, null);
		} catch (Exception e) {
			log.error("FileGenAction-addRecord", e);
			e.printStackTrace();
			throw e;
		}
		return rec;
	}

	/**
	 * 在生成签约或下游报文时，若数据库中主申报号码为空，则根据查询到主申报号更新数据库表中对应字段
	 * 
	 * @param rptCfaNoColumnId
	 * @param rptCfaNo
	 * @param businessNo
	 * @param businessId
	 */
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
			Element innerRoot, String innerSubName, String userId) {
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
					elem.setText(DataUtil.replaceForXml(val));
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
				elem.setText(DataUtil.replaceForXml(val));
			}
		} else {
			// 循环报表数据
			if (rptDatas.size() > 0) {
				for (Iterator k = rptDatas.iterator(); k.hasNext();) {
					try {
						addRecord(tableId, null, DateUtils.serverCurrentDate(),
								innerRoot, innerSubName, rptColumnList,
								(RptData) k.next(), new ArrayList(),
								new ArrayList(), userId, null);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				try {
					addRecord(tableId, null, DateUtils.serverCurrentDate(),
							innerRoot, innerSubName, rptColumnList,
							new RptData(), new ArrayList(), new ArrayList(),
							userId, null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private Document getDataFileContent(String currentFile, int totalRecords,
			String systemCode) {
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalRecordsElement = msgElement
				.addElement(TOTALRECORDS_ELEMENT);
		Element recordsElement = msgElement.addElement(RECORDS_ELEMENT);
		if (SYSTEM_CODE_FAL.equals(systemCode)) {
			appTypeElement.setText(APPTYPE_FAL);
			currentFileElement.setText(APPTYPE_FAL + currentFile);
		}
		inoutElement.setText(IN);
		totalRecordsElement.setText(String.valueOf(totalRecords));
		return root;
	}

	private Document getControlFileContent(List dataFileNames, String systemCode) {
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalFilesElement = msgElement.addElement(TOTALFILES_ELEMENT);
		Element filesElement = msgElement.addElement(FILES_ELEMENT);
		if (SYSTEM_CODE_FAL.equals(systemCode)) {
			appTypeElement.setText(APPTYPE_FAL);
			currentFileElement.setText(APPTYPE_FAL + CONTROL_FILE_TYPE_CODE);
		}
		inoutElement.setText(IN);
		totalFilesElement.setText(String.valueOf(dataFileNames.size()));
		for (int i = 0; i < dataFileNames.size(); i++) {
			Element fileNameElement = filesElement.addElement(FILENAME_ELEMENT);
			fileNameElement.setText((String) dataFileNames.get(i));
		}
		return root;
	}

	/**
	 * 获取当前用户拥有权限的机构集
	 * 
	 * @param authInstList
	 */
	private void getAuthInstList(List authInstList) {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		authInstList.addAll(currentUser.getOrgs());
	}

	/**
	 * 根据报表ID取rptColumnList,以及所有字段的查询SQL。从SESSION缓存中取
	 */
	private String getColumnsSql(String tableId, String fileType) {
		String suffixKey = "";
		if (StringUtil.isNotEmpty(fileType) && tableId.indexOf("_SUB_") < 0) {
			suffixKey = "#" + fileType;
		}
		Map rptColumnListMap = (Map) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP);
		if (rptColumnListMap != null) {
			rptColumnList = (ArrayList) rptColumnListMap.get(tableId
					+ suffixKey);
		}
		Map rptColumnSqlMap = (Map) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP);
		if (rptColumnSqlMap == null) {
			return "";
		} else {
			return (String) rptColumnSqlMap.get(tableId + suffixKey);
		}
	}

	/**
	 * 生成报文存储路径，若成功则返回路径字符串，否则返回null
	 * 
	 * @param dirName
	 * @return
	 */
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

	private void deleteDir(String dirPath, String dirName) {
		String dirFullPath = dirPath + File.separator + dirName;
		File dir = new File(dirFullPath);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			try {
				dir.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成锁文件，并返回生成结果；若锁文件已经存在，直接返回false
	 * 
	 * @param filePath
	 *            文件路径
	 * @param lockFileName
	 *            锁文件名
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * 删除锁文件
	 * 
	 * @param filePath
	 * @param lockFileName
	 * @return
	 */
	private boolean deleteLockFile(String filePath, String lockFileName) {
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		return lockFile.delete();
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @param document
	 *            DOCUMENT对象
	 * @throws Exception
	 */
	private void writeFile(String filePath, String fileName, Document document)
			throws Exception {
		String file = filePath + File.separator + fileName;
		// document到文件
		DocXmlUtil
				.fromDocumentToFile(document, file, CharacterEncoding.GB18030);
	}

	/**
	 * 将srcDir文件夹整体拷贝到desDir文件夹中
	 * 
	 * @param srcDir
	 * @param desDir
	 * @throws Exception
	 */
	private void copyFile(String srcDir, String desDir) throws Exception {
		File src = new File(srcDir);
		File des = new File(desDir);
		File subDir = null;
		// 若src是目录，则先在des下建一个同名子目录,若已存在同名子目录，则先将子目录删除（先删除子目录下的所有文件，再删除子目录），再建子目录
		if (src.isDirectory()) {
			String srcDirName = src.getName();
			String subDirFullName = this.makeDir(des.getAbsolutePath(),
					srcDirName);
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

	/**
	 * 根据>=0&&<100的数字得到表示该数字的两位字符串
	 * 
	 * @param serialNo
	 *            数字
	 * @return
	 */
	private String getSerialNoString(int serialNo) {
		if (0 <= serialNo && serialNo < 10) {
			return "0" + serialNo;
		} else if (10 <= serialNo && serialNo < 100) {
			return "" + serialNo;
		} else {
			return "";
		}
	}

	public DataDealService getDataDealService() {
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public List getAuthInstList() {
		return authInstList;
	}

	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}

	public List getRptTableList() {
		return rptTableList;
	}

	public void setRptTableList(List rptTableList) {
		this.rptTableList = rptTableList;
	}

	public List getRptColumnList() {
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList) {
		this.rptColumnList = rptColumnList;
	}

	public List getColumnIdList() {
		return columnIdList;
	}

	public void setColumnIdList(List columnIdList) {
		this.columnIdList = columnIdList;
	}

	public int getLargestColumnNum() {
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum) {
		this.largestColumnNum = largestColumnNum;
	}

	public RptData getRptData() {
		return rptData;
	}

	public void setRptData(RptData rptData) {
		this.rptData = rptData;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String[] getInstCodes() {
		return instCodes;
	}

	public void setInstCodes(String[] instCodes) {
		this.instCodes = instCodes;
	}

	public String getInfoTypeCode() {
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode) {
		this.infoTypeCode = infoTypeCode;
	}

	public String[] getBusinessIds() {
		return businessIds;
	}

	public void setBusinessIds(String[] businessIds) {
		this.businessIds = businessIds;
	}

	public String[] getSendFileTableIds() {
		return sendFileTableIds;
	}

	public void setSendFileTableIds(String[] sendFileTableIds) {
		this.sendFileTableIds = sendFileTableIds;
	}

	public void setRecordDescList(List recordDescList) {
		this.recordDescList = recordDescList;
	}

	public List getRecordDescList() {
		return recordDescList;
	}

	public String getControlFileName() {
		return controlFileName;
	}

	public void setControlFileName(String controlFileName) {
		this.controlFileName = controlFileName;
	}

	public String getSendFilePath() {
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath) {
		this.sendFilePath = sendFilePath;
	}

	public String getSendFileMakeStatus() {
		return sendFileMakeStatus;
	}

	public void setSendFileMakeStatus(String sendFileMakeStatus) {
		this.sendFileMakeStatus = sendFileMakeStatus;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public List getSendFileDownLoadList() {
		return sendFileDownLoadList;
	}

	public void setSendFileDownLoadList(List sendFileDownLoadList) {
		this.sendFileDownLoadList = sendFileDownLoadList;
	}

	public String[] getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(String[] fileTypes) {
		this.fileTypes = fileTypes;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	public String getNotClearSendFileMap() {
		return notClearSendFileMap;
	}

	public void setNotClearSendFileMap(String notClearSendFileMap) {
		this.notClearSendFileMap = notClearSendFileMap;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public String getFileGenDirZip() {
		if (configMap != null) {
			String fileGenDirZip = (String) configMap
					.get("config.fileGen.dirZip");
			if (StringUtils.isNotEmpty(fileGenDirZip)) {
				this.fileGenDirZip = fileGenDirZip;
			}
		}
		return fileGenDirZip;
	}

	/**
	 * <p>
	 * 方法名称: checkInstCodesSameRptTitle|描述: 校验选中生成报文的机构，是否有相同主报告行号
	 * </p>
	 * 
	 * @return boolean
	 */
	private boolean checkInstCodesSameRptTitle(String configRptTitle) {
		if (this.instCodes != null && "yes".equals(configRptTitle)) {
			// 查询与第一家机构的主报告行号相同的所有机构列表
			List list = organizationService
					.findOrgIdsWithSameRptTitle(instCodes[0]);
			Set s = new HashSet();
			for (int i = 0; i < list.size(); i++) {
				s.add(list.get(i));
			}
			Set s2 = new HashSet();
			for (int i = 1; i < instCodes.length; i++) {
				s2.add(instCodes[i]);
			}
			// 判断所选机构集合s2是否完全包含在s中
			if (!s.containsAll(s2)) {
				return false;
			}
		}
		return true;
	}

	protected List getListDesc(String tb, String inst, String searchLowerOrg,
			String fileType, String showDataStatus, String userId,
			List rptDataStatusList) {
		List result = new ArrayList();
		List lst = null;
		if ("AR".equalsIgnoreCase(fileType) || "AS".equalsIgnoreCase(fileType)) {
			lst = dataDealService.findRptDataStatusCountByTableIdAndInstCode(
					tb, inst, searchLowerOrg, fileType, userId,
					this.linkBussType);
		} else {
			lst = new ArrayList();
			for (Iterator i = rptDataStatusList.iterator(); i.hasNext();) {
				RptStatusCountInfo m = (RptStatusCountInfo) i.next();
				if (fileType.equalsIgnoreCase(m.getFileType())) {
					lst.add(m);
				}
			}
		}
		for (Iterator i = lst.iterator(); i.hasNext();) {
			RptStatusCountInfo m = (RptStatusCountInfo) i.next();
			int nDataStatus = m.getDataStatus();
			String sDataStatus = Integer.toString(nDataStatus);
			if (StringUtil.isNotEmpty(showDataStatus)
					&& showDataStatus.indexOf(sDataStatus) >= 0) {
				switch (nDataStatus) {
				// 1 未校验
				case DataUtil.WJY_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.WJY_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 2 校验未通过
				case DataUtil.JYWTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.JYWTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 3 校验已通过
				case DataUtil.JYYTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.JYYTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 4 已提交待审核
				case DataUtil.YTJDSH_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YTJDSH_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 5 审核未通过
				case DataUtil.SHWTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.SHWTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 6 审核已通过
				case DataUtil.SHYTG_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.SHYTG_STATUS_CH).append(
									m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 7 已生成
				case DataUtil.YSC_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YSC_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				// 8 已报送
				case DataUtil.YBS_STATUS_NUM:
					result.add(new SelectTag(sDataStatus, new StringBuffer()
							.append(DataUtil.YBS_STATUS_CH)
							.append(m.getCount()).append("条").append(";")
							.toString()));
					break;
				default:
					break;
				}
			} else {
				continue;
			}
		}
		return result;
	}

	public void setFileGenDirZip(String fileGenDirZip) {
		this.fileGenDirZip = fileGenDirZip;
	}

	public String[] getSendFileTypes() {
		return sendFileTypes;
	}

	public void setSendFileTypes(String[] sendFileTypes) {
		this.sendFileTypes = sendFileTypes;
	}

	public AutoDealRptService getAutoDealRptService() {
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService) {
		this.autoDealRptService = autoDealRptService;
	}

	public List getBusiDataInfoList() {
		return busiDataInfoList;
	}

	public void setBusiDataInfoList(List busiDataInfoList) {
		this.busiDataInfoList = busiDataInfoList;
	}
}