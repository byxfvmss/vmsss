/**
 * 
 */
package com.cjit.gjsz.filem.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.InnerTableSign;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.filem.util.ZipUtil;
import com.cjit.gjsz.interfacemanager.model.KeyInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.validate.action.PreValidate;
import com.icss.cfa.bank.dataread.facade.DataFileReadFacade;
import com.opensymphony.util.BeanUtils;

/**
 * @author gaole
 */
public class FileGenPreAction extends BaseListAction{

	private static final String CONTROL_FILE = "controlFile";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4613147642773991074L;
	private static final String TOKEN_LOCK_FILE_NAME = "Token.lock";
	private static final String SEND_FILE_SUFFIX = ".XML";
	private static final String SYSTEM_CODE_CFA = "FAL";
	private static final String CONTROL_FILE_TYPE_CODE = "TT";
	private static final String APPTYPE_CFA = "FAL";
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
	private String importDate;
	private String checkPass;
	private String checkRefuse;
	protected String message = "";
	// ---add by gaole ---//
	private String previewFilePath;
	private List validateFileList = new ArrayList();// ---要校验的文件LIST
	private String validateFeedBack;// --FeedBack文件夹

	public String getMessage(){
		return message;
	}

	public List getValidateFileList(){
		return validateFileList;
	}

	public String getValidateFeedBack(){
		return validateFeedBack;
	}

	public void setValidateFeedBack(String validateFeedBack){
		this.validateFeedBack = validateFeedBack;
	}

	public void setValidateFileList(List validateFileList){
		this.validateFileList = validateFileList;
	}

	/**
	 * 判断是否满足导出
	 * @param sendFileMap
	 * @return
	 */
	private String getNeedList(Map sendFileMap){
		boolean flag = true;
		List base = getNeedList(sendFileMap, "base");
		String businessids = getBusinessIds(base);
		List declare = getNeedList(sendFileMap, "declare");
		List finance = getNeedList(sendFileMap, "finance");
		// 对于申报情况
		Map declareMap = innerVerfiy(declare, TableIdRela.getZjsyMap());
		flag = this.isOk(declareMap, businessids);
		if(flag == false){
			return "declare";
		}
		// 对于核销情况
		Map financeMap = innerVerfiy(finance, TableIdRela.getJcsyMap());
		flag = this.isOk(financeMap, businessids);
		if(flag == false){
			return "finance";
		}
		return null;
	}

	/**
	 * 分别得到三级
	 * @param sendFileMap 页面提交的Map
	 * @param type 对照的三种类型
	 * @return
	 */
	private List getNeedList(Map sendFileMap, String type){
		String base = "t_base_income,t_base_remit,t_base_payment,t_base_export,t_base_dom_remit,t_base_dom_pay,t_company_info,t_base_settlement,t_base_purchase";
		String declare = "t_decl_income,t_decl_remit,t_decl_payment";
		String finance = "t_fini_export,t_fini_remit,t_fini_payment,t_fini_dom_remit,t_fini_dom_pay,t_fini_dom_export,t_fini_settlement,t_fini_purchase";
		List list = new ArrayList();
		for(Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			String tableId = (String) entry.getKey();
			Object[] value = (Object[]) entry.getValue();
			if(value[4] == null || ((Integer) value[4]).intValue() == 0){
				continue;
			}
			if(StringUtil.equalsIgnoreCase("base", type)){
				if(StringUtil.contains(base, tableId)){
					list.add(entry);
				}
			}else if(StringUtil.equalsIgnoreCase("declare", type)){
				if(StringUtil.contains(declare, tableId)){
					list.add(entry);
				}
			}else if(StringUtil.equalsIgnoreCase("finance", type)){
				if(StringUtil.contains(finance, tableId)){
					list.add(entry);
				}
			}
		}
		return list;
	}

	/**
	 * 将没有通过第一次验证的记录的业务主键取出来，和当前列表中的业务主键相比较
	 * @param list
	 * @param map
	 * @return
	 */
	private Map innerVerfiy(List list, Map map){
		Map finalMap = new LinkedHashMap();
		for(Iterator node = list.iterator(); node.hasNext();){
			Map.Entry entry = (Map.Entry) node.next();
			String fileType = (String) entry.getKey();
			String tableId = DataUtil.getTableIdByFileType(fileType); // 子表表名
			Object[] value = (Object[]) entry.getValue();
			String sbTableId = (String) map.get(tableId); // 直接上级
			// 取需要生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			// 校验不通的记录放在这里，准备第二次校验
			List rptDatas = null;
			List verifyDatas = new ArrayList();
			// 根据tableId和机构取数据(按businessIds过滤后的数据)
			if(!CollectionUtil.isEmpty(businessIds)){
				rptDatas = this.getRptDatas(tableId, businessIds, tableId);
				if(CollectionUtil.isNotEmpty(rptDatas)){
					for(int i = 0; i < rptDatas.size(); i++){
						RptData rptData = (RptData) rptDatas.get(i); // 得到子信息
						String bussinessid = rptData.getBusinessId(); // 得到主键，去找相应的主表记录
						List ids = new ArrayList();
						ids.add(bussinessid);
						if(StringUtil.isEmpty(rptData.getRptNo())){
							// 判断父亲是否已经生成过的，如果生成过了，可以通过
							List bases = this.getRptDatas(sbTableId, ids, null);
							RptData rptDataBase = (RptData) bases.get(0);
							if(StringUtil.isEmpty(rptDataBase.getRptNo())){
								verifyDatas.add(rptData.getBusinessId());
							}
						}
					}
				}
			}
			finalMap.put(sbTableId, verifyDatas);
		}
		return finalMap;
	}

	/**
	 * 判断是否允许生成
	 * @param declareMap
	 * @param businessids
	 * @return
	 */
	public boolean isOk(Map declareMap, String businessids){
		if(!declareMap.isEmpty()){
			for(Iterator node = declareMap.entrySet().iterator(); node
					.hasNext();){
				Map.Entry entry = (Map.Entry) node.next();
				List ids = (List) entry.getValue();
				if(CollectionUtil.isNotEmpty(ids)){
					for(int i = 0; i < ids.size(); i++){
						if(!StringUtil.contains(businessids, (String) ids
								.get(i))){
							// TODO：申报对应的基础信息没有生成或不在此生成列表中
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 得到Base的业务主键
	 * @param list
	 * @return
	 */
	public String getBusinessIds(List list){
		StringBuffer sb = new StringBuffer();
		for(Iterator i = list.iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			Object[] value = (Object[]) entry.getValue();
			// 取需要生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			if(CollectionUtil.isNotEmpty(businessIds)){
				sb.append(CollectionUtil.getNumberSQLIds(businessIds) + ",");
			}
		}
		return sb.toString();
	}

	private boolean makeSendFile(Map sendFileMap, Map tableColumnMap,
			int serialNo, String fileMakerCustomId, String orgRptTitle,
			String curDate, User user, String filePath, StringBuffer sbMsg,
			String fileShortPath, String systemCode){
		try{
			List lt1 = new ArrayList();
			List lt2 = new ArrayList();
			Organization currentUserOrg = new Organization();
			currentUserOrg.setId(user.getOrgId());
			// 数据文件名列表集，生成控制文件内容时用
			List dataFileNames = new ArrayList();
			// 是否生成数据报文(若本批次无任何可生成报文的记录，则为false)
			boolean haveMakeFile = false;
			// 循环sendFileMap，生成数据文件
			for(Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();){
				Map.Entry entry = (Map.Entry) i.next();
				String fileType = (String) entry.getKey();
				String tableId = DataUtil.getTableIdByFileType(fileType);
				// 取tableId对应的信息
				// 取报表物理表名tableId
				// 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存
				List columns = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableId, null,
								"1", fileType));
				int cFlag = 0;
				for(Iterator j = columns.iterator(); j.hasNext();){
					RptColumnInfo column = (RptColumnInfo) j.next();
					column.setAliasColumnId("c" + (++cFlag));
				}
				tableColumnMap.put(tableId, columns);
				// 取tableId对应的信息
				Object[] value = (Object[]) entry.getValue();
				// 得到数据记录的个数
				Integer recordNums = (Integer) value[4];
				// 记录个数为0则不生成
				if(recordNums == null || new Integer(0).equals(recordNums)){
					continue;
				}
				// 取机构集
				List instCodes = (ArrayList) value[1];
				// 取需要生成的记录的业务主键集
				List businessIds = (ArrayList) value[2];
				// 得到数据文件名
				String dataFileName = (String) value[3];
				List rptDatas = new ArrayList();
				// 根据tableId和机构取数据(按businessIds过滤后的数据)
				if(!CollectionUtil.isEmpty(businessIds)){
					rptDatas = this.getRptDatas(tableId, businessIds, fileType);
				}else if(CollectionUtil.isNotEmpty(instCodes)){
					rptDatas = this.getRptDatasByInstCodes(tableId, instCodes,
							fileType);
					if(CollectionUtil.isNotEmpty(rptDatas)){
						businessIds = new ArrayList();
						for(Iterator r = rptDatas.iterator(); r.hasNext();){
							RptData rd = (RptData) r.next();
							businessIds.add(rd.getBusinessId());
						}
						value[2] = businessIds;
					}
				}
				if(CollectionUtil.isNotEmpty(rptDatas)){
					serialNo = dataDealService.getSerialNo(fileType, user
							.getOrgId(), orgRptTitle, curDate);
					dataFileName = getDataFileName(fileType, curDate,
							currentUserOrg, getSerialNoString(serialNo),
							orgRptTitle);
					dataFileNames.add(dataFileName);
				}
				// 生成数据文件
				// document
				// mantis0017567 5000条分一个文件
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
				for(Iterator k = rptDatas.iterator(); k.hasNext();){
					if(index <= currentSize){
						try{
							addRecord(tableId, fileType, curDate, records,
									REC_ELEMENT, columns, (RptData) k.next(),
									lt1, lt2, user.getId());
						}catch (Exception ex){
							ex.printStackTrace();
							sendFileMakeStatus = "机构对照申报号配置不正确，暂时不能生成报送文件！";
							this.request.setAttribute("sendFileMakeStatus",
									sendFileMakeStatus);
							basic();
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
							this.request.setAttribute("importDate",
									this.importDate);
							this.request.setAttribute("checkPass",
									this.checkPass);
							this.request.setAttribute("checkRefuse",
									this.checkRefuse);
							logManagerService.writeLog(request, user, "0004",
									"报文管理.报文生成", "报文生成", sendFileMakeStatus,
									"0");
							return false;
						}
						index++;
					}else{
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
				if(lt1.size() > 0 || lt2.size() > 0){
					List cannotCreateList = new ArrayList();
					cannotCreateList.addAll(lt1);
					cannotCreateList.addAll(lt2);
					mpCannotCreate.put(tableId, cannotCreateList);
					RptTableInfo info = new RptTableInfo();
					info.setTableId(tableId);
					List rtiList = dataDealService.findRptTableInfo(info, "");
					if(rtiList != null && rtiList.size() > 0){
						RptTableInfo rti = (RptTableInfo) rtiList.get(0);
						sbMsg.append("选中报文[").append(rti.getTableName())
								.append("]");
						if(lt1.size() > 0){
							sbMsg.append("行数超出日生成报文最大限制，有").append(lt1.size())
									.append("行未能生成报文！");
						}
						if(lt2.size() > 0){
							sbMsg.append("所属机构对应申报号码配置错误，有").append(lt2.size())
									.append("行未能生成报文！");
						}
						sbMsg.append("\\n");
					}
				}
				// 写数据文件
				writeFile(filePath, dataFileName, docRoot);
				// 把sendFileMap中的数据文件名改成“目录+文件名”的形式
				value[3] = fileShortPath + File.separator + dataFileName;
				haveMakeFile = true;
			}
			if(!haveMakeFile){
				sbMsg.append("no data");
				return true;
			}
			// 写控制文件
			if(SYSTEM_CODE_CFA.equals(systemCode)){
				writeFile(filePath, controlFileName, getControlFileContent(
						dataFileNames, systemCode));
				// 把控制文件名改成“目录+文件名”的形式
				controlFileName = fileShortPath + File.separator
						+ controlFileName;
			}
			String appFilePath = this.servletContext
					.getRealPath("/data/sendfile");
			if("yes".equals(this.getFileGenDirZip())){
				// 生成zip压缩文件(包含所在BOPT...文件夹)
				// ---modify by gaole
				// zipFileDir(sendFilePath, fileShortPath);
				zipFileDir(previewFilePath, fileShortPath);
			}else{
				// 生成zip压缩文件(仅包含XML文件)
				zipFile(filePath, fileShortPath);
			}
			// 把存放报送文件的文件夹filePath整体拷贝到data\sendfile目录下
			this.copyFile(filePath, appFilePath);
			// 删除锁文件
			this.deleteLockFile(filePath, TOKEN_LOCK_FILE_NAME);
			this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			// 更新数据状态为7
			// resetRptDataStatus(sendFileMap, mpCannotCreate, fileShortPath);
		}catch (Exception e){
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0004", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	private String zipFile(String zipScrPath, String fileName){
		File file = new File(zipScrPath);
		if(file.exists()){
			ZipUtil zu = new ZipUtil();
			String zipFileName = zipScrPath + ".zip";
			zu.zip(zipScrPath, zipFileName);
			String zipFileNameDesc = zipScrPath + File.separator + fileName
					+ ".zip";
			File srcFile = new File(zipFileName);
			try{
				FileUtil.copyFile(srcFile, new File(zipFileNameDesc));
			}catch (IOException e){
				e.printStackTrace();
			}finally{
				FileUtil.deleteContents(srcFile);
			}
			return zipFileNameDesc;
		}
		throw new RuntimeException("报文生成路径配置错误");
	}

	private String zipFileDir(String zipScrPath, String fileName){
		File file = new File(zipScrPath);
		if(file.exists()){
			ZipUtil zu = new ZipUtil();
			String zipFileName = zipScrPath + File.separator + fileName
					+ ".zip";
			zu.zipDir(zipScrPath, fileName, zipFileName);
			String zipFileNameDesc = zipScrPath + File.separator + fileName
					+ File.separator + fileName + ".zip";
			File srcFile = new File(zipFileName);
			try{
				FileUtil.copyFile(srcFile, new File(zipFileNameDesc));
			}catch (IOException e){
				e.printStackTrace();
			}finally{
				FileUtil.deleteContents(srcFile);
			}
			return zipFileNameDesc;
		}
		throw new RuntimeException("报文生成路径配置错误");
	}

	private boolean judgeCannotMakeFile(Map sendFileMap1,
			String sendFileMapAttName, User user){
		try{
			String strNeed = getNeedList(sendFileMap1);
			if(StringUtil.equals(strNeed, "declare")){
				sendFileMakeStatus = "申报信息对应的基础信息还没有生成！";
				this.request.setAttribute("sendFileMakeStatus",
						sendFileMakeStatus);
				if(ScopeConstants.SEND_FILE_MAP.equals(sendFileMapAttName)){
					basic();
				}else if(ScopeConstants.SEND_FILE_MAP2
						.equals(sendFileMapAttName)){
					basic2();
				}
				sendFileDownLoadList = new ArrayList();
				recordDescList = null;
				this.addFieldToSession(sendFileMapAttName, null);
				// 在wl8.1下放入request
				this.request.setAttribute("businessIds", this.businessIds);
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("recordDescList", this.recordDescList);
				this.request.setAttribute("sendFileDownLoadList",
						this.sendFileDownLoadList);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("importDate", this.importDate);
				this.request.setAttribute("checkPass", this.checkPass);
				this.request.setAttribute("checkRefuse", this.checkRefuse);
				logManagerService.writeLog(request, user, "0004", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}else if(StringUtil.equals(strNeed, "finance")){
				sendFileMakeStatus = "核销信息对应的基础信息还没有生成！";
				this.request.setAttribute("sendFileMakeStatus",
						sendFileMakeStatus);
				if(ScopeConstants.SEND_FILE_MAP.equals(sendFileMapAttName)){
					basic();
				}else if(ScopeConstants.SEND_FILE_MAP2
						.equals(sendFileMapAttName)){
					basic2();
				}
				sendFileDownLoadList = new ArrayList();
				recordDescList = null;
				this.addFieldToSession(sendFileMapAttName, null);
				// 在wl8.1下放入request
				this.request.setAttribute("businessIds", this.businessIds);
				this.request.setAttribute("recordList", this.recordList);
				this.request
						.setAttribute("recordDescList", this.recordDescList);
				this.request.setAttribute("sendFileDownLoadList",
						this.sendFileDownLoadList);
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("importDate", this.importDate);
				this.request.setAttribute("checkPass", this.checkPass);
				this.request.setAttribute("checkRefuse", this.checkRefuse);
				logManagerService.writeLog(request, user, "0004", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0004", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	/**
	 * 根据报文生成路径来判断是否可生成报文
	 * @param filePath
	 * @param user
	 * @return boolean 可生成true 不可生成false
	 */
	private boolean judgeCannotMakeFile(String filePath, User user){
		try{
			String appFilePath = this.servletContext
					.getRealPath("/data/sendfile");
			// filePath ~ D:\BANKDATA\SEND\BOPT22200000000010110401
			// appFilePath ~
			// D:\Projects\crms\2012203D\01delib\D\02\CFA\CFA\WebRoot\data\sendfile
			if(filePath == null || StringUtil.isEmpty(appFilePath)){
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
				this.request.setAttribute("importDate", this.importDate);
				this.request.setAttribute("checkPass", this.checkPass);
				this.request.setAttribute("checkRefuse", this.checkRefuse);
				logManagerService.writeLog(request, user, "0004", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}
			boolean createBankDataLock = this.makeLockFile(filePath,
					TOKEN_LOCK_FILE_NAME);
			// 在报送文件路径下生成锁文件
			if(!createBankDataLock
					|| !this.makeLockFile(appFilePath, TOKEN_LOCK_FILE_NAME)){
				if(createBankDataLock){
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
				this.request.setAttribute("importDate", this.importDate);
				this.request.setAttribute("checkPass", this.checkPass);
				this.request.setAttribute("checkRefuse", this.checkRefuse);
				logManagerService.writeLog(request, user, "0004", "报文管理.报文生成",
						"报文生成", sendFileMakeStatus, "0");
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0004", "报文文件生成失败!", "0");
			return false;
		}
		return true;
	}

	private void basic() throws Exception{
		init();
		if(checkInstCodesSameRptTitle()){
			makeRecordDescList();
			getRptDatas();
		}else{
			this.sendFileMakeStatus = "请选取主报告行号相同的机构来生成报文！";
		}
	}

	private void basic2() throws Exception{
		init();
		if(checkInstCodesSameRptTitle()){
			makeRecordDescList2();
		}else{
			this.sendFileMakeStatus = "请选取主报告行号相同的机构来生成报文！";
		}
	}

	private void init() throws Exception{
		// 将参数配置项记录信息放在SESSION中
		this.initConfigParameters();
		// 受权机构
		getAuthInstList(authInstList);
		if(CollectionUtil.isEmpty(authInstList)){
			return;
		}else{
			if(authInstList != null && this.orgConfigList != null){
				for(int i = 0; i < authInstList.size(); i++){
					Organization org = (Organization) authInstList.get(i);
					for(int j = 0; j < orgConfigList.size(); j++){
						com.cjit.gjsz.filem.model.t_org_config orgConfig = (com.cjit.gjsz.filem.model.t_org_config) orgConfigList
								.get(j);
						if(org.getId().equals(orgConfig.getOrg_Id())){
							org.setRpttitle(orgConfig.getRptTitle());
						}
					}
				}
			}
		}
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String userId = "";
		if(currentUser != null){
			userId = currentUser.getId();
		}
		if("Z".equals(infoType)){
			RptTableInfo rti = new RptTableInfo();
			rti.setOrderBys("1,2,3,4,5,6");
			rti.setIsShow("1");
			rptTableList = dataDealService.findRptTableInfo(rti, userId);
		}else{
			List busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo("1", "1", "1"));
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
		}
		// 把报表列信息和查询SQL放在SESSION中
		if(this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP) == null){
			Map[] map = dataDealService
					.initRptColumnSqlMapNew(largestColumnNum);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP, map[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP, map[1]);
		}
		// 将字典信息放在SESSION中
		// if(this.getFieldFromSession(ScopeConstants.SESSION_DICTIONARY_MAP) ==
		// null){
		// Map[] map = userInterfaceConfigService.initDictionaryMap();
		// this.addFieldToSession(ScopeConstants.SESSION_DICTIONARY_MAP,
		// map[0]);
		// this.addFieldToSession(
		// ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE, map[1]);
		// }
		// 是否根据主报告行号汇总生成报文
		this.request.setAttribute("configFileGenRptTitle",
				this.configFileGenRptTitle);
		if("yes".equals(this.configFileGenRptTitle)){
			// 需要根据主报告行号汇总生成报文
			if(authInstList != null){
				for(int i = 0; i < authInstList.size(); i++){
					Organization org = (Organization) authInstList.get(i);
					String showName = org.getName()
							+ " 【主报告行号："
							+ (org.getRpttitle() == null ? "未配置" : org
									.getRpttitle().trim()) + "】";
					org.setShowName(showName);
				}
			}
		}
		// 资本项目接口版本
		this.request.setAttribute("interfaceVer", this.interfaceVer);
	}

	/**
	 * <p> 方法名称: initConfigParameters|描述: 将参数配置项记录信息放在SESSION中 </p>
	 * @throws Exception
	 */
	private void initConfigParameters() throws Exception{
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject("paramCache");
		if(cache != null){
			configMap = (Map) cache.get("configMap");
			orgConfigList = (List) cache.get("orgConfigList");
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
		}else{
			configMap = (HashMap) this
					.getFieldFromSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP);
			if(configMap == null){
				configMap = userInterfaceConfigService.initConfigParameters();
				this.addFieldToSession(
						ScopeConstants.SESSION_CONFIG_PARAMETER_MAP, configMap);
			}
		}
		this.setConfigParametersBaseList(configMap);
		this.request.setAttribute("interfaceVer", this.interfaceVer);
	}

	/**
	 * 报文生成（按记录）－添加记录
	 * @throws Exception
	 */
	private void getRptDatas() throws Exception{
		// (fwy)
		List newRecordlist = null;
		StringBuffer sb = new StringBuffer();
		// 根据表名查找列信息，用于打印报表表头
		if(fileType == null){
			rptColumnList = new ArrayList();
		}else{
			String tableId = DataUtil.getTableIdByFileType(fileType);
			// 拼查询列
			String columns = this.getColumnsSql(tableId, fileType);
			// 获取符合生成条件的列表数据
			if(instCodes != null){
				Map orgMap = new HashMap();
				List insts = new ArrayList();
				getAuthInstList(insts);
				for(int i = 0; i < this.instCodes.length; i++){
					for(int j = 0; j < insts.size(); j++){
						Organization org = (Organization) insts.get(j);
						if(org.getId().equals(this.instCodes[i])){
							orgMap.put(this.instCodes[i], org.getName());
							break;
						}
					}
				}
				String dataStatusCondition = null;
				// 基础
				// if("1".equals(infoTypeCode) || "5".equals(infoTypeCode)){
				// 报文生成的可选记录中，只显示状态为5的
				// dataStatusCondition = " t.datastatus = 5 ";
				String searchCondition = null;
				dataStatusCondition = " t.datastatus = "
						+ DataUtil.SHYTG_STATUS_NUM + " and t.fileType = '"
						+ fileType + "' ";
				for(int i = 0; i < instCodes.length; i++){
					newRecordlist = dataDealService.findRptData(new RptData(
							tableId, columns, instCodes[i],
							dataStatusCondition, null, null, null,
							searchCondition));
					// 关联数据信息需要过滤
					if(configMap != null && !configMap.isEmpty()){
						String relatedFileType = (String) configMap
								.get("config.related.filetype");
						if(this.fileType != null
								&& relatedFileType.indexOf(this.fileType) > -1){
							removeRecordRelated(newRecordlist, tableId,
									this.fileType);
						}
					}
					if(CollectionUtil.isNotEmpty(newRecordlist))
						recordList.addAll(newRecordlist);
				}
				if(CollectionUtil.isNotEmpty(newRecordlist)){
					// 看看有哪些还不符合条件的
					dataStatusCondition = " (t.datastatus < "
							+ DataUtil.SHYTG_STATUS_NUM
							+ " and t.datastatus <> "
							+ DataUtil.DELETE_STATUS_NUM
							+ ") and t.fileType = '" + fileType + "' ";
					List list = new ArrayList();
					for(int i = 0; i < instCodes.length; i++){
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
	 * @param newRecordlist
	 * @param fileType
	 * @param tableId
	 */
	private void removeRecordRelated(List newRecordlist, String tableId,
			String fileType){
		List removeData = new ArrayList();
		for(Iterator it = newRecordlist.iterator(); it.hasNext();){
			RptData rptData = (RptData) it.next();
			// 1.查询签约数据
			RptData data = dataDealService.findRptDataByRelatedBusinessId(
					tableId, fileType, rptData.getBusinessId());
			List sendList = dataDealService.findRptSendCommit(null, data
					.getBusinessId(), null, null, -1);
			if(sendList.size() == 0)
				removeData.add(rptData);
		}
		newRecordlist.removeAll(removeData);
	}

	private void setValue0(String instName, List list0, StringBuffer sb){
		if(CollectionUtil.isNotEmpty(list0)){
			if(sb.length() == 0){
				sb.append("注意：\\n");
			}else{
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
					.append("]的上级报文中有[").append(list0.size()).append(
							"]条记录还未生成过报文。");
		}
	}

	private void setValue(String instName, List list, StringBuffer sb){
		if(CollectionUtil.isNotEmpty(list)){
			if(sb.length() == 0){
				sb.append("注意：\\n");
			}else{
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

	private void setValueForm0(String instName, List list0, StringBuffer sb,
			String tableId){
		if(CollectionUtil.isNotEmpty(list0)){
			if(sb.length() == 0){
				sb.append("注意：\\n");
			}else{
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
					.append("]的上级报文中有[").append(list0.size()).append(
							"]条记录还未生成过报文。");
		}
	}

	private void setValueForm(String instName, List list, StringBuffer sb,
			String tableId, String fileType){
		if(CollectionUtil.isNotEmpty(list)){
			if(sb.length() == 0){
				sb.append("注意：\\n");
			}else{
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
					.append("]中有[" + list.size()).append("]条记录还未审核通过。");
		}
	}

	private void makeRecordDescList(){
		recordDescList.clear();
		Map sendFileMap = (Map) this
				.getFieldFromSession(ScopeConstants.SEND_FILE_MAP);
		if(sendFileMap == null){
			return;
		}
		String fileType = null;
		String tableId = null;
		String instCode = null;
		for(Iterator i = sendFileMap.keySet().iterator(); i.hasNext();){
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
			for(Iterator j = instCodes.iterator(); j.hasNext();){
				instCode = (String) j.next();
				Organization org = new Organization();
				org.setId(instCode);
				instNames.append(
						organizationService.getOrganization(org).getName())
						.append(",");
			}
			// RptTableInfo t = new RptTableInfo();
			// t.setTableId(tableId);
			// t = (RptTableInfo) ((ArrayList)
			// dataDealService.findRptTableInfo(t)).get(0);
			RptTableInfo t = dataDealService.findRptTableInfoById(tableId,
					fileType);
			int nNoSend = 0;// 因未能生成申报号而无法发送的报文条数
			if(this.mpCannotCreate != null){
				List lt = (List) this.mpCannotCreate.get(tableId);
				if(lt != null){
					nNoSend = lt.size();
				}
			}
			t.setDescription("包含以下机构的数据:"
					+ instNames.substring(0, instNames.length() - 1) + "；共"
					+ (businessIds.size() - nNoSend) + "条数据信息");
			// if (value[4] != null && !new Integer(0).equals(value[4])) {
			// t.setIsShow(null);
			// } else {
			// t.setIsShow(null);
			// }
			t.setIsShow(null);
			recordDescList.add(t);
		}
	}

	private void makeRecordDescList2(){
		recordDescList.clear();
		Map sendFileMap2 = (Map) this
				.getFieldFromSession(ScopeConstants.SEND_FILE_MAP2);
		if(sendFileMap2 == null){
			return;
		}
		String fileType = null;
		String tableId = null;
		String instCode = null;
		for(Iterator i = sendFileMap2.keySet().iterator(); i.hasNext();){
			// 取报表物理表名tableId
			fileType = (String) i.next();
			tableId = DataUtil.getTableIdByFileType(fileType);
			// 取tableId对应的信息
			Object[] value = (Object[]) sendFileMap2.get(fileType);
			// 取机构集
			List instCodes = (ArrayList) value[1];
			// 取需要生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			// 循环机构
			StringBuffer instNames = new StringBuffer("");
			// List rptDatas = new ArrayList();
			for(Iterator j = instCodes.iterator(); j.hasNext();){
				instCode = (String) j.next();
				Organization org = new Organization();
				org.setId(instCode);
				instNames.append(
						organizationService.getOrganization(org).getName())
						.append(",");
			}
			// RptTableInfo t = new RptTableInfo();
			// t.setTableId(tableId);
			// t = (RptTableInfo) ((ArrayList)
			// dataDealService.findRptTableInfo(t)).get(0);
			RptTableInfo t = dataDealService.findRptTableInfoById(tableId,
					fileType);
			t.setDescription("包含以下机构的数据:"
					+ instNames.substring(0, instNames.length() - 1) + "；共"
					+ businessIds.size() + "条数据信息");
			// if (value[4] != null && !new Integer(0).equals(value[4])) {
			// t.setIsShow(null);
			// } else {
			// t.setIsShow(null);
			// }
			t.setIsShow(null);
			recordDescList.add(t);
		}
	}

	/**
	 * 将生成报文的记录的数据状态更新为7已生成 同时写入相应的批次号 向T_RPT_SEND_COMMIT报文报送记录表中添加相应记录
	 * @param sendFileMap
	 * @param noSendDataMap
	 */
	private void resetRptDataStatus(Map sendFileMap, Map noSendDataMap,
			String packName){
		String fileType = null;
		String tableId = null;
		String businessId = null;
		// 循环sendFileMap，生成数据文件名，将之添加到sendFileMap中和dataFileNames中
		for(Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();){
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
			if(noSendDataMap != null){
				noSendBusinessIdList = (List) noSendDataMap.get(tableId);
			}
			if(packfilename != null){
				if(packfilename.indexOf("\\") > -1){
					packfilename = StringUtil.replace(packfilename, "\\", "/");
				}
				String[] packfilenames = null;
				if(packfilename.indexOf("/") > -1){
					packfilenames = packfilename.split("/");
					if(packfilenames != null && packfilenames.length == 2){
						packname = packfilenames[0];
						filename = packfilenames[1];
					}
				}
			}
			// 取已生成的记录的业务主键集
			List businessIds = (ArrayList) value[2];
			// 循环业务主键，重置状态
			for(Iterator j = businessIds.iterator(); j.hasNext();){
				businessId = (String) j.next();
				if(noSendBusinessIdList != null
						&& noSendBusinessIdList.size() > 0){
					if(noSendBusinessIdList.contains(businessId)){
						continue;
					}
				}
				StringBuffer sbUpdateSQL = new StringBuffer();
				// 记录状态:7-已生成---modify by gaole --不更新状态
				// sbUpdateSQL.append(" datastatus = ").append(
				// DataUtil.SHYTG_STATUS_NUM).append(" ");
				// dataDealService.updateRptData(new RptData(tableId,
				// sbUpdateSQL
				// .toString(), businessId, null, null, true));
				// 添加报文报送记录//---不插入记录---modify by gaole
				// dataDealService.insertRptSendCommit(tableId, businessId,
				// packname, filename, 0);
				// 如果当前生成报文的记录为"国内外汇贷款（含外债转贷款）信息"
				// 则根据businessno字段到"境外担保项下境内贷款"表中查询有无对应信息
				// 存在的话更新其DOFOEXLOCODE（国内外汇贷款编号）为当前生成的DOFOEXLOCODE值
				// ---不更新记录---modify by gaole
				// if(DataUtil.CFA_SELF_TABLE_C.equalsIgnoreCase(tableId)){
				// dataDealService.autoUpdateDofoecloCode(tableId, businessId);
				// }
			}
		}
		// if(businessId == null && StringUtil.isNotEmpty(packName)){
		// dataDealService.insertRptSendCommit("controlFile", "-", packName,
		// "", 0);
		// }
	}

	// /**
	// * 将生成报文的记录的数据状态更新为6已生成
	// *
	// * @param sendFileMap
	// */
	// private void resetRptDataStatus2(Map sendFileMap2) {
	// String tableId = null;
	// String instCode = null;
	// for (Iterator i = sendFileMap2.keySet().iterator(); i.hasNext();) {
	// // 取报表物理表名tableId
	// tableId = (String) i.next();
	//
	// Object[] value = (Object[]) sendFileMap2.get(tableId);
	// // 取已生成的记录的业务主键集
	// List instCodes = (ArrayList) value[1];
	// // 循环业务主键，重置状态
	// for (Iterator j = instCodes.iterator(); j.hasNext();) {
	// instCode = (String) j.next();
	// dataDealService.updateRptData(new RptData(tableId, " datastatus = '6' ",
	// instCode,
	// null, null, false));
	// }
	// }
	// }
	/**
	 * 生成控制文件名
	 * @param instCode
	 * @param curDate
	 * @param serialNo
	 * @param systemCode
	 * @return systemCode
	 */
	private String getControlFileName(String curDate,
			Organization currentUserOrg, String serialNo, String rptTitle){
		// 根据用户所在的机构号，去映射表中寻找对应的申报号
		if(StringUtil.isEmpty(rptTitle)){
			String orgId = currentUserOrg.getId();
			rptTitle = organizationService.findRptTitle(orgId);
		}
		if(rptTitle == null || "null".equals(rptTitle)
				|| rptTitle.length() != 12){
			log.error("FileGenAction-getControlFileName-主报告行号未配置正确！");
			return null;
		}
		return SYSTEM_CODE_CFA + CONTROL_FILE_TYPE_CODE + rptTitle + curDate
				+ serialNo + SEND_FILE_SUFFIX;
	}

	/**
	 * 生成数据文件名
	 * @param fileType
	 * @param instCode
	 * @param curDate
	 * @param serialNo
	 * @param systemCode
	 * @return systemCode
	 */
	private String getDataFileName(String fileType, String curDate,
			Organization currentUserOrg, String serialNo, String rptTitle){
		// 根据用户所在的机构号，去映射表中寻找对应的申报号
		if(StringUtil.isEmpty(rptTitle)){
			String orgId = currentUserOrg.getId();
			rptTitle = organizationService.findRptTitle(orgId);
		}
		if(rptTitle == null || rptTitle.length() != 12){
			log.error("FileGenAction-getControlFileName-主报告行号未配置正确！");
			return null;
		}
		// 根据tableId查找对应的类型代码（一位字母）
		return SYSTEM_CODE_CFA + fileType + rptTitle + curDate + serialNo
				+ SEND_FILE_SUFFIX;
	}

	/**
	 * 获取一张报表中某个特定机构的全部数据，并将不包含在所选业务主键集中的数据清除
	 * @param tableId 报表ID
	 * @param businessIds 业务主键集
	 * @return List
	 */
	private List getRptDatas(String tableId, List businessIds, String fileType){
		List rptList = new ArrayList();
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
						fileType));
		StringBuffer columns = new StringBuffer();
		int cFlag = 0;
		for(Iterator i = rptColumnList.iterator(); i.hasNext();){
			RptColumnInfo column = (RptColumnInfo) i.next();
			String columnId = column.getColumnId();
			// 取别名
			column.setAliasColumnId("c" + (++cFlag));
			// 拼查询SQL
			if("table".equals(column.getDataType())){
				columns.append(" '' as ").append(column.getAliasColumnId())
						.append(",");
			}else{
				columns.append("t.").append(columnId).append(" as ").append(
						column.getAliasColumnId()).append(",");
			}
		}
		while(cFlag < largestColumnNum){
			columns.append("'' as c").append(++cFlag).append(",");
		}
		String strColumns = columns.toString().substring(0,
				columns.toString().length() - 1);
		// 判断传入生成报文的业务ID个数
		if(businessIds != null){
			if(businessIds.size() <= 500){
				// 当业务ID数小于等于500时，直接查询对应数据list
				rptList = dataDealService.findRptDataByTableIdAndBusinessIds(
						tableId, businessIds, strColumns);
			}else if(businessIds.size() > 500){
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int i = 0;
				int nn = businessIds.size() / 500;
				for(; i < nn; i++){
					List tempIds = businessIds.subList(i * 500, (i + 1) * 500);
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndBusinessIds(tableId,
									tempIds, strColumns);
					if(tempRptDatas != null && tempRptDatas.size() > 0){
						rptList.addAll(tempRptDatas);
					}
				}
				List tempIds = businessIds.subList(i * 500, businessIds.size());
				if(tempIds != null && tempIds.size() > 0){
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndBusinessIds(tableId,
									tempIds, strColumns);
					if(tempRptDatas != null && tempRptDatas.size() > 0){
						rptList.addAll(tempRptDatas);
					}
				}
			}
		}
		return rptList;
	}

	private List getRptDatasByInstCodes(String tableId, List instCodes,
			String fileType){
		List rptList = new ArrayList();
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableId, null, "1",
						fileType));
		StringBuffer columns = new StringBuffer();
		int cFlag = 0;
		for(Iterator i = rptColumnList.iterator(); i.hasNext();){
			RptColumnInfo column = (RptColumnInfo) i.next();
			String columnId = column.getColumnId();
			// 取别名
			column.setAliasColumnId("c" + (++cFlag));
			// 拼查询SQL
			if("table".equals(column.getDataType())){
				columns.append(" '' as ").append(column.getAliasColumnId())
						.append(",");
			}else{
				if("BUSINESSNO".equalsIgnoreCase(columnId)){
					columns.append("t.").append(columnId).append(
							" as businessNo,");
				}else{
					columns.append("t.").append(columnId).append(" as ")
							.append(column.getAliasColumnId()).append(",");
				}
			}
		}
		while(cFlag < largestColumnNum){
			columns.append("'' as c").append(++cFlag).append(",");
		}
		String strColumns = columns.toString().substring(0,
				columns.toString().length() - 1);
		String searchCondition = null;
		if(fileType != null && tableId.indexOf(fileType) > -1){
			// 当前添加的单据不是签约信息时，增加限制条件，要求对应签约信息曾经报送过
			String rptNoColumnId = DataUtil
					.getRptNoColumnIdByFileType(fileType);
			searchCondition = " exists (select businessid from "
					+ tableId
					+ " tc where tc.fileType <> '"
					+ fileType
					+ "' and (t."
					+ rptNoColumnId
					+ " = tc."
					+ rptNoColumnId
					+ " or t.businessNo = tc.businessNo) "
					+ " and (tc.businessId in (select businessId from t_rpt_send_commit where tableID = '"
					+ tableId + "') ";
			if(!"yes".equalsIgnoreCase(this.configFileGenChooseTables)){
				searchCondition += " or tc.datastatus = "
						+ DataUtil.SHYTG_STATUS_NUM + ")) ";
			}else{
				searchCondition += "))";
			}
		}
		String orderBy = null;
		if(StringUtil.isNotEmpty(DataUtil
				.getByeRptNoColumnIdByFileType(fileType))){
			orderBy = "t." + DataUtil.getByeRptNoColumnIdByFileType(fileType)
					+ " asc ";
		}
		// 判断传入生成报文的业务ID个数
		if(instCodes != null){
			if(instCodes.size() <= 500){
				// 当业务ID数小于等于500时，直接查询对应数据list
				rptList = dataDealService.findRptDataByTableIdAndInstCodes(
						tableId, fileType, instCodes, String
								.valueOf(DataUtil.SHYTG_STATUS_NUM),
						strColumns, searchCondition, orderBy);
			}else if(instCodes.size() > 500){
				// 当业务ID数大于500时，分段查询对应数据list，最终合成返回list
				int i = 0;
				int nn = instCodes.size() / 500;
				for(; i < nn; i++){
					List tempInstCodes = instCodes.subList(i * 500,
							(i + 1) * 500);
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndInstCodes(tableId,
									fileType, tempInstCodes,
									String.valueOf(DataUtil.SHYTG_STATUS_NUM),
									strColumns, searchCondition, orderBy);
					if(tempRptDatas != null && tempRptDatas.size() > 0){
						rptList.addAll(tempRptDatas);
					}
				}
				List tempInstCodes = instCodes.subList(i * 500, instCodes
						.size());
				if(tempInstCodes != null && tempInstCodes.size() > 0){
					List tempRptDatas = dataDealService
							.findRptDataByTableIdAndInstCodes(tableId,
									fileType, tempInstCodes,
									String.valueOf(DataUtil.SHYTG_STATUS_NUM),
									strColumns, searchCondition, orderBy);
					if(tempRptDatas != null && tempRptDatas.size() > 0){
						rptList.addAll(tempRptDatas);
					}
				}
			}
		}
		return rptList;
	}

	/**
	 * 根据报表数据生成报文中代表该数据的记录元素
	 * @param tableId 报表ID
	 * @param fileType 文件类型
	 * @param curDate 当前日期
	 * @param records
	 * @param columns 报表所有列信息
	 * @param rptData 代表报表数据的BEAN
	 * @param lt1 无法生成申报号的单据个数(超出日生成报文限制)
	 * @param lt2 无法生成申报号的单据个数(单据所属机构对应申报号码或机构ID配置错误)
	 * @param userId
	 * @return
	 */
	private Element addRecord(String tableId, String fileType, String curDate,
			Element records, String subElementName, List columns,
			RptData rptData, List lt1, List lt2, String userId)
			throws Exception{
		// rec为代表本条数据的XML元素节点
		Element rec = records.addElement(subElementName);
		try{
			String actionType = rptData.getActionType();
			String instCode = rptData.getInstCode();
			String businessId = rptData.getBusinessId();
			String businessNo = null;
			// 第一次循环数据列，获取数据银行核心业务编号的值
			for(Iterator i = columns.iterator(); i.hasNext();){
				RptColumnInfo column = (RptColumnInfo) i.next();
				System.out.println("---" + column.getColumnId().trim());
				if(DataUtil.CFA_SELF_TABLE_D.equalsIgnoreCase(tableId)
						&& "DOFOEXLOCODE".equalsIgnoreCase(column.getColumnId()
								.trim())){
					String dofoexloCode = (String) BeanUtils.getValue(rptData,
							column.getAliasColumnId());
					if(StringUtil.isEmpty(dofoexloCode)){
						dofoexloCode = dataDealService.autoUpdateDofoecloCode(
								tableId, businessId);
						if(StringUtil.isNotEmpty(dofoexloCode)){
							BeanUtils.setValue(rptData, column
									.getAliasColumnId(), dofoexloCode);
						}
					}
				}
				if("BUSINESSNO".equalsIgnoreCase(column.getColumnId().trim())){
					businessNo = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
				}
			}
			// 第二次循环数据列，获取各列数据，生成XML节点
			for(Iterator i = columns.iterator(); i.hasNext();){
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 字段为内嵌表
				if("4".equals(column.getTagType())){
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
				// 如果是基础报表（取不到直接上游报表），列名为申报号码，并且操作类型为A或空（新增），则临时生成申报号码，并将申报号码存到库中
				// 实际上，因为基础信息的报文肯定是最先生成的，当其生成完毕后，肯定也已经生成了申报号码，并随之在库中将申报号码存到了对应的申报/
				// 核销报表中，
				// 所以申报/核销报表在生成报文时肯定已经有了申报号码，不需要再生成
				// || "A".equalsIgnoreCase(actionType) modify by wangxin 删除该判断
				// if ((TableIdRela.getZjsyMap().get(tableId) == null)
				// && ("RPTNO".equalsIgnoreCase(column.getColumnId()))) {
				if(("T_CFA_A_EXDEBT".equals(tableId) && "EXDEBTCODE"
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
								.equalsIgnoreCase(column.getColumnId()))){
					String rptCfaNoColumnId = column.getColumnId();
					String rptCfaNo = (String) BeanUtils.getValue(rptData,
							column.getAliasColumnId());
					// 申报号码为空，需生成申报号
					if(StringUtil.isEmpty(rptCfaNo)){
						// 获取信息的交易日期
						String tradeDate = null;
						for(Iterator iterator = columns.iterator(); iterator
								.hasNext();){
							RptColumnInfo column1 = (RptColumnInfo) iterator
									.next();
							// FIXME: 需确认各种报文对应的交易日期字段
							if("TRADEDATE".equals(column1.getColumnId())){
								tradeDate = (String) BeanUtils.getValue(
										rptData, column1.getAliasColumnId());
								break;
							}
						}
						if(tradeDate != null){
							String tempTradeDate = tradeDate.substring(0, 4)
									+ "-" + tradeDate.substring(4, 6) + "-"
									+ tradeDate.substring(6, 8);
							Date tradedate = null;
							try{
								tradedate = DateUtils.stringToDate(
										tempTradeDate,
										DateUtils.ORA_DATES_FORMAT);
							}catch (Exception e1){
								e1.printStackTrace();
							}
							try{
								// 生成申报号码
								data = userInterfaceConfigService
										.createAutokey(new KeyInfo(
												instCode,
												tradedate,
												tableId,
												DataUtil
														.getSelfBussTypeCodeByFileType(fileType),
												rptCfaNoColumnId, actionType));
								if(data == null){
									// 超出日生成报文限制，未能生成申报号码
									lt1.add(businessId);
									records.remove(rec);
									return null;
								}else if("ErrorOrgId".equals(data)){
									// 单据所属机构对应申报号码或机构ID配置错误
									lt2.add(businessId);
									records.remove(rec);
									return null;
								}
							}catch (Exception e){
								log.error("FileGenAciton-addRecord", e);
								e.printStackTrace();
								throw e;
							}
						}
						if(StringUtil.isEmpty(data)
								&& TableIdRela.getZjxyMap().get(fileType) == null
								&& StringUtil.isNotEmpty(businessNo)){
							// 根据业务编号查询上游报文的申报号码
							data = dataDealService.findRptNoByBusinessNo(
									rptCfaNoColumnId, tableId, businessNo);
						}
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								data);
						// 更新签约信息报表的申报号码字段rptno的值（根据businessId取记录进行更新）
						dataDealService.updateRptData(new RptData(tableId, " "
								+ rptCfaNoColumnId + " = '" + data + "' ",
								businessId, null, null, true));
					}
					// 生成报文时已存在申报号
					else{
						data = rptCfaNo;
					}
					// 查询数据库中是否存在对应下游报表，存在的话则更新对应申报号码字段
					if(StringUtil.isNotEmpty(businessNo)){
						RptData upRptData = new RptData(tableId, " "
								+ rptCfaNoColumnId + " = '" + data + "' ",
								null, null, null, true);
						StringBuffer sbUpdateCondition = new StringBuffer();
						sbUpdateCondition.append(" BUSINESSNO = '").append(
								businessNo).append("' and (").append(
								rptCfaNoColumnId).append(" is null or ")
								.append(rptCfaNoColumnId).append(" = '') ");
						upRptData.setUpdateCondition(sbUpdateCondition
								.toString());
						dataDealService.updateRptData(upRptData);
					}
				}
				// 否则根据别名直接找到数据
				else{
					data = (String) BeanUtils.getValue(rptData, column
							.getAliasColumnId());
					// 当数据为带有小数位的数值类型，且data起始字符为小数点时，在其前面加个0
					if(column.getDataType().startsWith("n")){
						if(data != null && !"".equals(data)){
							if(data.startsWith(".")){
								data = "0" + data;
							}else if(data.startsWith("-.")){
								data = data.replaceAll("-.", "-0.");
							}
							String[] nDataTypes = column.getDataType().split(
									",");
							if(nDataTypes != null && nDataTypes.length == 4){
								if(data.indexOf(".") < 0){
									data += ".";
									for(int n = 0; n < Integer.valueOf(
											nDataTypes[3]).intValue(); n++){
										data += "0";
									}
								}else{
									String[] datas = new String[2];
									datas[0] = data.substring(0, data
											.indexOf("."));
									datas[1] = data.substring(
											data.indexOf(".") + 1, data
													.length());
									for(int n = datas[1].length(); n < Integer
											.valueOf(nDataTypes[3]).intValue(); n++){
										datas[1] += "0";
									}
									data = datas[0] + "." + datas[1];
								}
							}
						}
					}
				}
				// 去除多余字段，该字段不需要生成在报文中
				if("TRADEDATE".equals(column.getColumnId())
						|| "BATCHNO".equals(column.getColumnId())
						|| "BUSINESSNO".equals(column.getColumnId())){
					continue;
				}
				// 给rec添加子元素，元素名为列名
				Element e = rec.addElement(column.getColumnId());
				e.setText(data == null ? "" : DataUtil.replaceForXml(data));
			}
			if(StringUtil.isNotEmpty(tableId) && rptData.getTableId() == null){
				rptData.setTableId(tableId);
			}
			if(StringUtil.isNotEmpty(fileType) && rptData.getFileType() == null){
				rptData.setFileType(fileType);
			}
			autoDealRptService.saveRptLogInfoData(rptData, "send", userId,
					DateUtils.serverCurrentTimeStamp(), columns, null);
		}catch (Exception e){
			log.error("FileGenAction-addRecord", e);
			e.printStackTrace();
			throw e;
		}
		return rec;
	}

	private void addInnerRecords(String tableId, String businessId,
			Element innerRoot, String innerSubName, String userId){
		// 查找tableId对应的列信息，给列设置别名，并将之放到MAP中缓存
		// User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// Organization currentUserOrg = new Organization();
		// currentUserOrg.setId(currentUser.getOrgId());
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableId, null, "1", "SUB"));
		int cFlag = 0;
		StringBuffer columns = new StringBuffer();
		for(Iterator i = rptColumnList.iterator(); i.hasNext();){
			RptColumnInfo column = (RptColumnInfo) i.next();
			String columnId = column.getColumnId();
			// 取别名
			column.setAliasColumnId("c" + (++cFlag));
			// 拼查询SQL
			columns.append("t.").append(columnId).append(" as ").append(
					column.getAliasColumnId()).append(",");
		}
		while(cFlag < largestColumnNum){
			columns.append("'' as c").append(++cFlag).append(",");
		}
		List rptDatas = dataDealService.findInnerRptData(new RptData(tableId,
				columns.toString()
						.substring(0, columns.toString().length() - 1),
				businessId, null, null, null));
		if(StringUtil.equalsIgnoreCase(innerSubName, "REFNO")){
			// 循环报表数据
			if(rptDatas.size() > 0){
				for(Iterator k = rptDatas.iterator(); k.hasNext();){
					RptData rptData = (RptData) k.next();
					Element elem = innerRoot.addElement(innerSubName);
					String val = rptData.getC1(); // c1代表 REFNO
					elem.setText(DataUtil.replaceForXml(val));
				}
			}else{
				// 子表信息为空时，在报文中添加空标签
				Element elem = innerRoot.addElement(innerSubName);
				elem.setText("");
			}
		}else if(StringUtil.equalsIgnoreCase(innerSubName, "PROJECTS")){
			// 循环报表数据
			for(Iterator k = rptDatas.iterator(); k.hasNext();){
				RptData rptData = (RptData) k.next();
				Element elem = innerRoot.addElement("PROJECTNAME");
				String val = rptData.getC1();
				elem.setText(DataUtil.replaceForXml(val));
			}
		}else{
			// 循环报表数据
			if(rptDatas.size() > 0){
				for(Iterator k = rptDatas.iterator(); k.hasNext();){
					try{
						addRecord(tableId, null, DateUtils.serverCurrentDate(),
								innerRoot, innerSubName, rptColumnList,
								(RptData) k.next(), new ArrayList(),
								new ArrayList(), userId);
					}catch (Exception ex){
						ex.printStackTrace();
					}
				}
			}else{
				try{
					addRecord(tableId, null, DateUtils.serverCurrentDate(),
							innerRoot, innerSubName, rptColumnList,
							new RptData(), new ArrayList(), new ArrayList(),
							userId);
				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	private Document getDataFileContent(String currentFile, int totalRecords,
			String systemCode){
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalRecordsElement = msgElement
				.addElement(TOTALRECORDS_ELEMENT);
		Element recordsElement = msgElement.addElement(RECORDS_ELEMENT);
		if(SYSTEM_CODE_CFA.equals(systemCode)){
			appTypeElement.setText(APPTYPE_CFA);
			currentFileElement.setText(APPTYPE_CFA + currentFile);
		}
		inoutElement.setText(IN);
		totalRecordsElement.setText(String.valueOf(totalRecords));
		return root;
	}

	private Document getControlFileContent(List dataFileNames, String systemCode){
		Document root = DocumentHelper.createDocument();
		Element msgElement = root.addElement(MSG_ELEMENT);
		Element appTypeElement = msgElement.addElement(APPTYPE_ELEMENT);
		Element currentFileElement = msgElement.addElement(CURRENTFILE_ELEMENT);
		Element inoutElement = msgElement.addElement(INOUT_ELEMENT);
		Element totalFilesElement = msgElement.addElement(TOTALFILES_ELEMENT);
		Element filesElement = msgElement.addElement(FILES_ELEMENT);
		if(SYSTEM_CODE_CFA.equals(systemCode)){
			appTypeElement.setText(APPTYPE_CFA);
			currentFileElement.setText(APPTYPE_CFA + CONTROL_FILE_TYPE_CODE);
		}
		inoutElement.setText(IN);
		totalFilesElement.setText(String.valueOf(dataFileNames.size()));
		for(int i = 0; i < dataFileNames.size(); i++){
			Element fileNameElement = filesElement.addElement(FILENAME_ELEMENT);
			fileNameElement.setText((String) dataFileNames.get(i));
		}
		return root;
	}

	/**
	 * 获取当前用户拥有权限的机构集
	 * @param authInstList
	 */
	private void getAuthInstList(List authInstList){
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		authInstList.addAll(currentUser.getOrgs());
	}

	/**
	 * 根据报表ID取rptColumnList,以及所有字段的查询SQL。从SESSION缓存中取
	 */
	private String getColumnsSql(String tableId, String fileType){
		String suffixKey = "";
		if(StringUtil.isNotEmpty(fileType) && tableId.indexOf("_SUB_") < 0){
			suffixKey = "#" + fileType;
		}
		Map rptColumnListMap = (Map) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP);
		if(rptColumnListMap != null){
			rptColumnList = (ArrayList) rptColumnListMap.get(tableId
					+ suffixKey);
		}
		Map rptColumnSqlMap = (Map) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP);
		if(rptColumnSqlMap == null){
			return "";
		}else{
			return (String) rptColumnSqlMap.get(tableId + suffixKey);
		}
	}

	/**
	 * 生成报文存储路径，若成功则返回路径字符串，否则返回null
	 * @param dirName
	 * @return
	 */
	private String makeDir(String dirPath, String dirName){
		String dirFullPath = dirPath + File.separator + dirName;
		File dir = new File(dirFullPath);
		// 文件夹已存在则直接删除所有子文件
		if(dir.exists()){
			File[] files = dir.listFiles();
			for(int i = 0; i < files.length; i++){
				files[i].delete();
			}
			try{
				dir.delete();
			}catch (Exception e){
				return dirFullPath;
			}
		}
		// 不存在则创建文件夹
		if(dir.mkdirs()){
			return dirFullPath;
		}
		return null;
	}

	private void deleteDir(String dirPath, String dirName){
		String dirFullPath = dirPath + File.separator + dirName;
		File dir = new File(dirFullPath);
		if(dir.exists()){
			File[] files = dir.listFiles();
			for(int i = 0; i < files.length; i++){
				files[i].delete();
			}
			try{
				dir.delete();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成锁文件，并返回生成结果；若锁文件已经存在，直接返回false
	 * @param filePath 文件路径
	 * @param lockFileName 锁文件名
	 * @return
	 * @throws IOException
	 */
	private boolean makeLockFile(String filePath, String lockFileName)
			throws IOException{
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		if(lockFile.exists()){
			return false;
		}else{
			return (lockFile.createNewFile());
		}
	}

	/**
	 * 删除锁文件
	 * @param filePath
	 * @param lockFileName
	 * @return
	 */
	private boolean deleteLockFile(String filePath, String lockFileName){
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		return lockFile.delete();
	}

	/**
	 * 写文件
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @param document DOCUMENT对象
	 * @throws Exception
	 */
	private void writeFile(String filePath, String fileName, Document document)
			throws Exception{
		String file = filePath + File.separator + fileName;
		// document到文件
		DocXmlUtil
				.fromDocumentToFile(document, file, CharacterEncoding.GB18030);
	}

	/**
	 * 将srcDir文件夹整体拷贝到desDir文件夹中
	 * @param srcDir
	 * @param desDir
	 * @throws Exception
	 */
	private void copyFile(String srcDir, String desDir) throws Exception{
		File src = new File(srcDir);
		File des = new File(desDir);
		File subDir = null;
		// 若src是目录，则先在des下建一个同名子目录,若已存在同名子目录，则先将子目录删除（先删除子目录下的所有文件，再删除子目录），再建子目录
		if(src.isDirectory()){
			String srcDirName = src.getName();
			String subDirFullName = this.makeDir(des.getAbsolutePath(),
					srcDirName);
			if(subDirFullName == null){
				throw new IOException();
			}
			subDir = new File(subDirFullName);
		}
		// 循环src下的所有文件，将每个文件都拷贝到subDir中(只拷贝文件，不拷贝下级目录)
		File[] files = src.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(!file.isDirectory()
					&& !TOKEN_LOCK_FILE_NAME.equalsIgnoreCase(file.getName())){
				FileUtils.copyFileToDirectory(file, subDir);
			}
		}
	}

	/**
	 * 根据>=0&&<100的数字得到表示该数字的两位字符串
	 * @param serialNo 数字
	 * @return
	 */
	private String getSerialNoString(int serialNo){
		if(0 <= serialNo && serialNo < 10){
			return "0" + serialNo;
		}else if(10 <= serialNo && serialNo < 100){
			return "" + serialNo;
		}else{
			return "";
		}
	}

	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public List getAuthInstList(){
		return authInstList;
	}

	public void setAuthInstList(List authInstList){
		this.authInstList = authInstList;
	}

	public List getRptTableList(){
		return rptTableList;
	}

	public void setRptTableList(List rptTableList){
		this.rptTableList = rptTableList;
	}

	public List getRptColumnList(){
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList){
		this.rptColumnList = rptColumnList;
	}

	public List getColumnIdList(){
		return columnIdList;
	}

	public void setColumnIdList(List columnIdList){
		this.columnIdList = columnIdList;
	}

	public int getLargestColumnNum(){
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum){
		this.largestColumnNum = largestColumnNum;
	}

	public RptData getRptData(){
		return rptData;
	}

	public void setRptData(RptData rptData){
		this.rptData = rptData;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String[] getInstCodes(){
		return instCodes;
	}

	public void setInstCodes(String[] instCodes){
		this.instCodes = instCodes;
	}

	public String getInfoTypeCode(){
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode){
		this.infoTypeCode = infoTypeCode;
	}

	public String[] getBusinessIds(){
		return businessIds;
	}

	public void setBusinessIds(String[] businessIds){
		this.businessIds = businessIds;
	}

	public String[] getSendFileTableIds(){
		return sendFileTableIds;
	}

	public void setSendFileTableIds(String[] sendFileTableIds){
		this.sendFileTableIds = sendFileTableIds;
	}

	public void setRecordDescList(List recordDescList){
		this.recordDescList = recordDescList;
	}

	public List getRecordDescList(){
		return recordDescList;
	}

	public String getControlFileName(){
		return controlFileName;
	}

	public void setControlFileName(String controlFileName){
		this.controlFileName = controlFileName;
	}

	public String getSendFilePath(){
		return sendFilePath;
	}

	public void setSendFilePath(String sendFilePath){
		this.sendFilePath = sendFilePath;
	}

	public String getSendFileMakeStatus(){
		return sendFileMakeStatus;
	}

	public void setSendFileMakeStatus(String sendFileMakeStatus){
		this.sendFileMakeStatus = sendFileMakeStatus;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public List getSendFileDownLoadList(){
		return sendFileDownLoadList;
	}

	public void setSendFileDownLoadList(List sendFileDownLoadList){
		this.sendFileDownLoadList = sendFileDownLoadList;
	}

	// public String[] getTableIds(){
	// return tableIds;
	// }
	// public void setTableIds(String[] tableIds){
	// this.tableIds = tableIds;
	// }
	public String[] getFileTypes(){
		return fileTypes;
	}

	public void setFileTypes(String[] fileTypes){
		this.fileTypes = fileTypes;
	}

	public OrganizationService getOrganizationService(){
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService){
		this.organizationService = organizationService;
	}

	public String getServerType(){
		return serverType;
	}

	public void setServerType(String serverType){
		this.serverType = serverType;
	}

	public List getRecordList(){
		return recordList;
	}

	public void setRecordList(List recordList){
		this.recordList = recordList;
	}

	public String getNotClearSendFileMap(){
		return notClearSendFileMap;
	}

	public void setNotClearSendFileMap(String notClearSendFileMap){
		this.notClearSendFileMap = notClearSendFileMap;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}

	public String getFileGenDirZip(){
		if(configMap != null){
			String fileGenDirZip = (String) configMap
					.get("config.fileGen.dirZip");
			if(StringUtils.isNotEmpty(fileGenDirZip)){
				this.fileGenDirZip = fileGenDirZip;
			}
		}
		return fileGenDirZip;
	}

	/**
	 * <p>方法名称: checkInstCodesSameRptTitle|描述: 校验选中生成报文的机构，是否有相同主报告行号</p>
	 * @return boolean
	 */
	private boolean checkInstCodesSameRptTitle(){
		boolean sameRptTitle = true;
		if(this.instCodes != null){
			String baseRptTitle = organizationService
					.findRptTitle(instCodes[0]);
			for(int i = 1; i < instCodes.length; i++){
				String rptTitle = organizationService
						.findRptTitle(instCodes[i]);
				if(!baseRptTitle.equals(rptTitle)){
					return false;
				}
			}
		}
		return sameRptTitle;
	}

	public void setFileGenDirZip(String fileGenDirZip){
		this.fileGenDirZip = fileGenDirZip;
	}

	public String getImportDate(){
		return importDate;
	}

	public void setImportDate(String importDate){
		this.importDate = importDate;
	}

	public String getCheckPass(){
		return checkPass;
	}

	public void setCheckPass(String checkPass){
		this.checkPass = checkPass;
	}

	public String getCheckRefuse(){
		return checkRefuse;
	}

	public void setCheckRefuse(String checkRefuse){
		this.checkRefuse = checkRefuse;
	}

	public String[] getSendFileTypes(){
		return sendFileTypes;
	}

	public void setSendFileTypes(String[] sendFileTypes){
		this.sendFileTypes = sendFileTypes;
	}

	public AutoDealRptService getAutoDealRptService(){
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService){
		this.autoDealRptService = autoDealRptService;
	}

	// ---以下都是高乐为了预校验而增加的---//
	public String getPreviewFilePath(){
		return previewFilePath;
	}

	public void setPreviewFilePath(String previewFilePath){
		this.previewFilePath = previewFilePath;
	}

	/**
	 * 预校验生成报文(按表单)
	 * @author gaole
	 * @return
	 */
	public String makeFile4Preview1(){
		return makeFileCommon4Preview(ScopeConstants.SEND_FILE_MAP);
	}

	public String makeFile4Preview2(){
		return makeFileCommon4Preview(ScopeConstants.SEND_FILE_MAP2);
	}

	/**
	 * 预校验报文生成方法，拷贝makeFileCommon方法而来
	 * @author gaole
	 * @param sendFileMapAttName
	 * @return
	 */
	private String makeFileCommon4Preview(String sendFileMapAttName){
		boolean createSendFileTokenLock = false;
		String appFilePath = this.servletContext.getRealPath("/data/sendfile");
		try{
			this.initConfigParameters();
			// 是否根据主报告行号汇总生成报文
			String configRptTitle = "";
			// 机构对照管理出配置主报告行号
			String orgRptTitle = null;
			if(this.configMap != null){
				configRptTitle = (String) this.configMap
						.get("config.fileGen.rpttitle");
			}
			// 当前系统日期yyyyMMdd格式
			String curDate = DateUtils.serverCurrentDate();
			// 当前用户所属机构号（12位）
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			Organization currentUserOrg = new Organization();
			currentUserOrg.setId(currentUser.getOrgId());
			// 当前用户所属机构对照申报号码
			String fileMakerCustomId = (organizationService
					.getOrganization(currentUserOrg)).getCustomId();
			if(!"yes".equalsIgnoreCase(configRptTitle)
					&& (fileMakerCustomId == null || fileMakerCustomId.length() != 12)){
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
				this.request.setAttribute("importDate", this.importDate);
				this.request.setAttribute("checkPass", this.checkPass);
				this.request.setAttribute("checkRefuse", this.checkRefuse);
				logManagerService.writeLog(request, currentUser, "0004",
						"报文管理.报文生成", "报文生成", sendFileMakeStatus, "0");
				return SUCCESS;
			}
			// SESSION中取fileMakeMap
			Map sendFileMap1 = (Map) this
					.getFieldFromSession(sendFileMapAttName);
			request.getSession().setAttribute(sendFileMapAttName, sendFileMap1);
			// 报送文件完整路径
			String fileShortPath = null;
			StringBuffer sbMsg = new StringBuffer();
			User user = this.getCurrentUser();
			if(!judgeCannotMakeFile(sendFileMap1, sendFileMapAttName, user)){
				// 当"申报信息对应的基础信息还没有生成！"或"核销信息对应的基础信息还没有生成！"
				// 返回生成页面，不进行报文生成操作
				return SUCCESS;
			}
			Map sendFileMap = new TreeMap();
			sendFileMap.putAll(sendFileMap1);
			// 用于存储报表对应的列信息的MAP:tableId->rptColumnInfoList
			Map tableColumnMap = new LinkedHashMap();
			// 循环fileMakeMap的key:tableId
			String tableId = null;
			String fileType = null;
			// String instCode = null;
			int serialNo = 0;
			// 以下两个map需实例化为TreeMap,以便依TableId排序,达到基础、申报、核销顺序处理的效果
			Map sendDataCfaMap = new TreeMap();
			if(sendFileMap != null){
				// 循环sendFileMap，生成数据文件名，将之添加到sendFileMap中和dataFileNames中
				for(Iterator i = sendFileMap.entrySet().iterator(); i.hasNext();){
					Map.Entry entry = (Map.Entry) i.next();
					fileType = (String) entry.getKey();
					// 取tableId对应的信息
					Object[] value = (Object[]) entry.getValue();
					if("yes".equalsIgnoreCase(configRptTitle)
							&& orgRptTitle == null && value[1] != null
							&& this.orgConfigList != null){
						List instCodes = (List) value[1];
						String instCode0 = (String) instCodes.get(0);
						for(int c = 0; c < orgConfigList.size(); c++){
							com.cjit.gjsz.filem.model.t_org_config orgConfig = (com.cjit.gjsz.filem.model.t_org_config) orgConfigList
									.get(c);
							if(instCode0
									.equalsIgnoreCase(orgConfig.getOrg_Id())){
								orgRptTitle = orgConfig.getRptTitle();
							}
						}
					}else{
					}
					// 得到数据文件名----modify by gaole--修改文件名,默认为00
					// String dataFileName = getDataFileName(tableId, curDate,
					// currentUserOrg, getSerialNoString(serialNo),
					// orgRptTitle);
					String dataFileName = getDataFileName(tableId, curDate,
							currentUserOrg, "00", orgRptTitle);
					// DFHL:解决序号生成问题结束
					value[3] = dataFileName;
					sendDataCfaMap.put(fileType, value);
				}
				// ///////////////////////////////////////////////////////////////////////////
				// 控制文件名
				// DFHL:解决序号生成问题开始
				// serialNo = dataDealService.getSerialNo(CONTROL_FILE,
				// fileMakerCustomId, orgRptTitle, curDate);
				// serialNo = dataDealService.getSerialNo(CONTROL_FILE,
				// currentUser.getOrgId(), orgRptTitle, curDate);
				serialNo = 0;// ---add by gaole
				controlFileName = getControlFileName(curDate, currentUserOrg,
						getSerialNoString(serialNo), orgRptTitle);
				// DFHL:解决序号生成问题结束
				// 报送文件完整路径
				fileShortPath = controlFileName.substring(0, controlFileName
						.length()
						- SEND_FILE_SUFFIX.length());
				// ---modify by gaole
				// String filePath = this.makeDir(sendFilePath, fileShortPath);
				String filePath = this.makeDir(previewFilePath, fileShortPath);
				if(!judgeCannotMakeFile(filePath, user)){
					// 当"报文生成路径不存在"或"令牌文件已存在"
					// 返回生成页面，不进行报文生成操作
					return SUCCESS;
				}else{
					createSendFileTokenLock = true;
				}
				// ----modify by gaole
				boolean makeSuccess = this.makeSendFile(sendDataCfaMap,
						tableColumnMap, serialNo, fileMakerCustomId,
						orgRptTitle, curDate, user, filePath, sbMsg,
						fileShortPath, SYSTEM_CODE_CFA);
				if(!makeSuccess){
					if(createSendFileTokenLock){
						this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
					}
					return SUCCESS;
				}
				this.addFieldToRequest("totalZip", fileShortPath
						+ File.separator + fileShortPath + ".zip");
				this.addFieldToRequest("controlFileName", controlFileName);
				// ///////////////////////////////////////////////////////////////////////////
			}
			if(ScopeConstants.SEND_FILE_MAP.equals(sendFileMapAttName)){
				basic();
			}else if(ScopeConstants.SEND_FILE_MAP2.equals(sendFileMapAttName)){
				basic2();
			}
			sendFileDownLoadList = recordDescList;
			recordDescList = null;
			// this.addFieldToSession(sendFileMapAttName, null);
			this.addFieldToRequest("totalZip", fileShortPath + File.separator
					+ fileShortPath + ".zip");
			sendFileMakeStatus = "生成报送文件成功！";
			String logStatus = "1";
			if(sbMsg.toString().length() > 0){
				if(!"no data".equalsIgnoreCase(sbMsg.toString())){
					sendFileMakeStatus += "\\n其中：\\n" + sbMsg.toString();
				}else{
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
			this.request.setAttribute("importDate", this.importDate);
			this.request.setAttribute("checkPass", this.checkPass);
			this.request.setAttribute("checkRefuse", this.checkRefuse);
			// /return SUCCESS;
			if("1".equals(logStatus)){
				// sendPack = fileShortPath;
				// this.request.setAttribute("sendPack", sendPack);
				// return "upfile";
				List sendList = new ArrayList();
				sendList.add(fileShortPath);
				String result = autoDealRptService.uploadReport4Pre(sendList,
						null, this.previewFilePath);
				if(result.equals("lock"))
					this.request.setAttribute("message", "控制文件存在,无法上传文件!");
				else if(result.equals("error"))
					this.request.setAttribute("message", "文件上传出现异常!");
				else if(result.equals("ok"))
					sendFileMakeStatus = sendFileMakeStatus.replaceAll(
							"生成报送文件成功", "生成报送文件并上传成功");
			}else{
				this.addFieldToRequest("totalZip", "");
				this.addFieldToRequest("controlFileName", "");
				// --modify by gaole
				// this.deleteDir(sendFilePath, fileShortPath);
				this.deleteDir(previewFilePath, fileShortPath);
			}
			this.request.setAttribute("sendFileMakeStatus", sendFileMakeStatus);
			logManagerService.writeLog(request, user, "0020", "报文管理.报文生成",
					"报文生成", sendFileMakeStatus, logStatus);
			if(createSendFileTokenLock){
				this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			}
			// ----拷贝的部分结束了
			// ---调用校验、获取校验结果部分开始----//
			DataFileReadFacade read = new DataFileReadFacade();
			read.readDataFile();
			// ---后台线程启动，开始检查，并取数据；
			PreValidate obj = new PreValidate();
			obj.setValidateFilePath(previewFilePath);
			obj.setValidateFeedBack(validateFeedBack);
			List fileList = this.getValidateFileList();
			obj.setValidateFileList(fileList);
			List list = null;
			try{
				while(list == null){
					Thread.currentThread().sleep(2000);
					list = obj.getErrorList();
				}
			}catch (InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("errorList", list);
			// ----//
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error("FileGenAction-makeFileCommon", e);
			logManagerService.writeLog(request, this.getCurrentUser(), "报文生成",
					"报文管理.报文生成", "0004", "报文文件生成失败!", "0");
			if(createSendFileTokenLock){
				this.deleteLockFile(appFilePath, TOKEN_LOCK_FILE_NAME);
			}
			return ERROR;
		}
	}
}