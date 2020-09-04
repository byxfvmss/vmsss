package com.cjit.gjsz.filem.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import cjit.crms.util.Assert;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.model.RptSendCommit;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.interfacemanager.model.TableInfo;

public class ReceiveReportServiceImpl extends GenericServiceImpl implements
		ReceiveReportService{

	public static final String MSG = "MSG";
	public static final String APPTYPE = "APPTYPE";
	public static final String CURRENTFILE = "CURRENTFILE";
	public static final String INOUT = "INOUT";
	public static final String FORMATERRS = "FORMATERRS";
	public static final String FORMATS = "FORMATS";
	public static final String FORMAT = "FORMAT";
	public static final String TOTALRECORDS = "TOTALRECORDS";
	public static final String SUCRECORDS = "SUCRECORDS";
	public static final String FALRECORDS = "FALRECORDS";
	public static final String ERRRECORDS = "ERRRECORDS";
	public static final String REC = "REC";
	public static final String BUSSNO = "BUSSNO";
	public static final String ERRFIELDS = "ERRFIELDS";
	public static final String ERR = "ERR";
	public static final String ERRFIELD = "ERRFIELD";
	public static final String ERRFIELDCN = "ERRFIELDCN";
	public static final String ERRDESC = "ERRDESC";
	// 控制文件部分
	public static final String TOTALFILES = "TOTALFILES";
	public static final String FILES = "FILES";
	public static final String FILENAME = "FILENAME";
	public static final String FILE_ERROR = "文件错误";
	public static final String DATE_ERROR = "数据错误";
	public static final String CONTROL = "T"; // 单位基本信息
	protected transient Log log = LogFactory.getLog(this.getClass());
	private DataDealService dataDealService;

	/**
	 * 解析文件格式错误
	 * @param root
	 */
	/*
	private void resolveFileError(Element root, ReceiveReport receiveReport){
		Element formats = root.element(FORMATS);
		if(formats != null){
			List formatList = formats.elements(FORMAT);
			if(CollectionUtil.isNotEmpty(formatList)){
				ReceiveReport temp = new ReceiveReport();
				BeanUtils.copyProperties(receiveReport, temp);
				StringBuffer sb = new StringBuffer();
				sb
						.append("<p><div id=\"msg\"><strong>失败的文件详细信息描述如下</strong></div> </p> ");
				sb.append("<ol>");
				sb.append("<ol>");
				for(int i = 0; i < formatList.size(); i++){
					Element format = (Element) formatList.get(i);
					sb.append("<li> " + format.getTextTrim() + " </li>");
				}
				sb.append("</ol>");
				temp.setErrorMemo(sb.toString());
				temp.setErrorType(FILE_ERROR);
				this.saveReceiveReport(temp);
			}
		}
	}
	*/
	/**
	 * 解析失败记录错误
	 * @param root
	 */
	private String resolveMsgError(Element root, ReceiveReport receiveReport,
			Map map){
		Element errRecords = root.element(ERRRECORDS);
		StringBuffer sb = new StringBuffer();
		if(errRecords != null){
			List recList = errRecords.elements(REC);
			if(CollectionUtil.isNotEmpty(recList)){
				List rptNos = new ArrayList();
				ReceiveReport temp = new ReceiveReport();
				BeanUtils.copyProperties(receiveReport, temp);
				for(int i = 0; i < recList.size(); i++){
					Element rec = (Element) recList.get(i);
					setRec(rec, temp);
					if(hasDuplicateReceiveReport(temp)){
						sb.append("业务编号[" + temp.getDataNumber() + "]日期["
								+ temp.getReceiveDate() + "]反馈已经上传</br>");
						this.updateReceiveReportDetail(temp);
					}else{
						rptNos.add(temp.getDataNumber());
						this.saveReceiveReport(temp);
					}
				}
				map.put("errRptBussNos", rptNos);
			}
		}
		return sb.toString();
	}

	private void setRec(Element rec, ReceiveReport receiveReport){
		StringBuffer sb = new StringBuffer();
		Element eBussno = rec.element(BUSSNO);
		String strBussno = eBussno.getTextTrim(); // 业务编号
		receiveReport.setDataNumber(strBussno); // 业务编号
		receiveReport.setErrorType(DATE_ERROR);
		Element errFields = rec.element(ERRFIELDS);
		if(errFields != null){
			List errFieldsList = errFields.elements(ERR);
			if(CollectionUtil.isNotEmpty(errFieldsList)){
				sb
						.append("<p><div id=\"msg\"><strong>失败的记录字段详细信息描述如下</strong></div> </p> ");
				sb.append("<ol>");
				for(int j = 0; j < errFieldsList.size(); j++){
					Element err = (Element) errFieldsList.get(j);
					if(err != null){
						Element errField = err.element(ERRFIELD);
						sb.append("<li>出错字段英文标识 [" + errField.getTextTrim()
								+ "], ");
						Element errFieldCn = err.element(ERRFIELDCN);
						sb.append(" 出错字段中文标识 [" + errFieldCn.getTextTrim()
								+ "], ");
						Element errDesc = err.element(ERRDESC);
						sb.append(" 出错原因 [" + errDesc.getTextTrim()
								+ "] </li> ");
					}
				}
				sb.append("</ol>");
				receiveReport.setErrorMemo(sb.toString());
			}
		}
	}

	/**
	 * 解析失败记录错误
	 * @param root
	 */
	private String resolveControlError(Element root,
			ReceiveReport receiveReport, Map map){
		Element totalFiles = root.element(TOTALFILES);
		if(totalFiles != null){
			String str = totalFiles.getTextTrim();
			receiveReport.setTotalFiles(Integer.valueOf(str));
			map.put("TOTALFILES", str);
		}
		Element fileElemnts = root.element(FILES);
		if(fileElemnts != null){
			List files = fileElemnts.elements(FILENAME);
			if(CollectionUtil.isNotEmpty(files)){
				String[] errFileNames = new String[files.size()];
				StringBuffer sb = new StringBuffer();
				sb
						.append("<p><div id=\"msg\"><strong>失败的文件名称详细信息描述如下</strong></div> </p> ");
				sb.append("<ol> ");
				ReceiveReport temp = new ReceiveReport();
				BeanUtils.copyProperties(receiveReport, temp);
				for(int i = 0; i < files.size(); i++){
					Element fileName = (Element) files.get(i);
					sb.append("<li>出错文件名称为 [" + fileName.getTextTrim()
							+ "] </li> ");
					errFileNames[i] = fileName.getTextTrim();
				}
				sb.append("</ol> ");
				receiveReport.setErrorMemo(sb.toString());
				map.put("errFileNames", errFileNames);
			}
		}
		if(!hasDuplicateReceiveReport(receiveReport)){
			this.saveReceiveReport(receiveReport);
			return "";
		}else{
			map.put("hasDuplicate", "yes");
			return "控制文件已上传";
		}
	}

	public String saveImportXmlFile(File file, String fileName,
			StringBuffer sb, Map map){
		if(StringUtil.isEmpty(fileName)){
			log.error("解析xml文件[" + fileName + "]出错:文件名不能为空");
			return "<div id=\"msg\">文件名不能为空.</div>";
		}
		if(fileName.indexOf(".") < 0){
			log.error("解析xml文件[" + fileName + "]出错:文件名格式不对,不包含文件扩展名");
			return "<div id=\"msg\">文件名格式不对,请认真核查.</div>";
		}
		String name = fileName.substring(0, fileName.indexOf("."));
		if(name.length() != 28){
			log.error("解析xml文件[" + fileName + "]出错:文件名长度不是28位");
			return "<div id=\"msg\">文件名格式不对,请认真核查.</div>";
		}
		String fileType = fileName.substring(3, 5);
		if(StringUtil.contains("TT", fileType)){
			// 控制反馈文件
			map.put("flag", "TT");
			map.put("packName", name);
			sb.append("控制反馈信息");
			return this.saveControlXml(fileName, file, map);
		}else{
			TableInfo tableInfo = getTableInfoByFileType(fileType);
			// 报文反馈文件
			map.put("flag", String.valueOf(fileType));
			map.put("errFileNames", new String[] {name + ".XML"});
			//sb.append(DataUtil.getInfoTypeName("", "", fileType));
			sb.append(fileType);
			return this.saveDataXml(fileType, fileName, tableInfo, file, map);
		}
	}

	private String saveDataXml(String fileType, String fileName,
			TableInfo tableInfo, File file, Map map){
		String date = fileName.substring(17, 23);
		String s = "";
		ReceiveReport receiveReport = new ReceiveReport();
		receiveReport.setInfoType(DataUtil.getInfoTypeName(null, tableInfo
				.getTableId(), fileType));
		// receiveReport.setInfoType("业务数据信息");
		receiveReport.setDataType(ReceiveReport.DATA_TYPE_REPNO);
		receiveReport.setTableId(tableInfo.getTableId());
		receiveReport.setTableName(DataUtil.getTableNameByFileType(fileType));
		receiveReport.setHasReject("0"); // 未打回
		receiveReport.setReceiveDate(date);
		if(fileName.indexOf("ERR") > 0){
			String rptSendFileName = fileName.replaceAll("ERR", "");
			receiveReport.setRptSendFileName(rptSendFileName);
		}
		try{
			Document doc = XmlUtil.parse(file);
			Element root = doc.getRootElement();
			if(!StringUtil.equals(root.getName(), MSG)){
				return "<div id=\"msg\">文件格式有误根目录 [" + root.getName()
						+ "] 不是 [" + MSG + "]</div>";
			}else{
				// resolveFileError(root, receiveReport);
				s = resolveMsgError(root, receiveReport, map);
				if(StringUtils.isNotEmpty(s))
					s = "<div id=\"msg\">" + s + "</div>";
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (DocumentException e){
			e.printStackTrace();
		}
		return s;
	}

	private String saveControlXml(String fileName, File file, Map map){
		String date = fileName.substring(17, 23);
		String orgId = fileName.substring(5, 17);
		ReceiveReport receiveReport = new ReceiveReport();
		receiveReport.setInfoType("控制反馈信息");
		receiveReport.setHasReject("0"); // 未打回
		receiveReport.setReceiveDate(date);
		receiveReport.setDataNumber(orgId);
		receiveReport.setErrorType(DATE_ERROR);
		receiveReport.setRptSendFileName(fileName);
		try{
			Document doc = XmlUtil.parse(file);
			Element root = doc.getRootElement();
			if(!StringUtil.equals(root.getName(), MSG)){
				return "<div id=\"msg\">文件格式有误根目录 [" + root.getName()
						+ "] 不是 [" + MSG + "]</div>";
			}else{
				return resolveControlError(root, receiveReport, map);
			}
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (DocumentException e){
			e.printStackTrace();
		}
		return "";
	}

	public void saveReceiveReport(ReceiveReport receiveReport){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		this.save("saveReceiveReport", map);
	}

	public void updateReceiveReport(ReceiveReport receiveReport){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		this.save("updateReceiveReport", map);
	}

	private void updateHasReject(String hasReject, String dataNo,
			String fileName){
		Map map = new HashMap();
		map.put("hasReject", hasReject);
		map.put("dataNo", dataNo);
		map.put("fileName", fileName);
		this.save("updateHasReject", map);
	}

	private void saveRefuseResion(String tableId, String businessId){
		RptData rd = new RptData();
		rd.setBusinessId(businessId);
		rd.setTableId(tableId);
		rd.setReasionInfo("反馈接收 信息错误");
		Map params = new HashMap();
		params.put("rptData", rd);
		params.put("busiType", "2");
		this.delete("deleteRefuseReasion", params);
		this.save("insertRefuseReasionInfo", params);
	}

	public TableInfo getTableInfoByFileType(String fileType){
		Map map = new HashMap();
		map.put("fileType", fileType);
		List list = this.find("getTableInfoByFileType", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (TableInfo) list.get(0);
		}
		return null;
	}

	public List getReceiveReports(ReceiveReport receiveReport,
			PaginationList paginationList){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		if(receiveReport != null && receiveReport.getDataNumbers() != null
				&& receiveReport.getDataNumbers().length > 0){
			StringBuffer sbSQL = new StringBuffer();
			String[] dataNumbers = receiveReport.getDataNumbers();
			for(int i = 0; i < dataNumbers.length; i++){
				sbSQL.append("'").append(dataNumbers[i]).append("',");
			}
			sbSQL.append("'-'");
			map.put("dataNumbers", sbSQL.toString());
		}
		return find("getReceiveReports", map, paginationList);
	}

	public ReceiveReport getReceiveReport(ReceiveReport receiveReport){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		List list = this.find("getReceiveReport", map);
		if(CollectionUtil.isNotEmpty(list)){
			return (ReceiveReport) list.get(0);
		}
		return null;
	}

	public void deleteReceiveReports(List ids){
		if(CollectionUtil.isNotEmpty(ids)){
			this.delete("deleteReceiveReports", CollectionUtil
					.getNumberSQLIds(ids));
		}
	}

	public List getReceiveReports2(ReceiveReport receiveReport,
			PaginationList paginationList){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		return find("getReceiveReports2", map, paginationList);
	}

	public boolean hasDuplicateReceiveReport(ReceiveReport receiveReport){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		List list = find("hasDuplicateReceiveReport", map);
		return CollectionUtils.isNotEmpty(list) ? true : false;
	}

	private void updateReceiveReportDetail(ReceiveReport receiveReport){
		Map map = new HashMap();
		map.put("receiveReport", receiveReport);
		this.save("updateReceiveReportDetail", map);
	}

	public void saveReceivePack(String packName, String fileName,
			String packType){
		Map map = new HashMap();
		map.put("packName", packName);
		map.put("fileName", fileName);
		map.put("packType", packType);
		this.save("insertRecivevPack", map);
	}

	public boolean isReceivePackExists(String packName, String packType){
		Map map = new HashMap();
		map.put("packName", packName);
		map.put("packType", packType);
		Double t = this.findForDouble("findRecievePack", map);
		if(!t.isNaN() && t.intValue() > 0)
			return true;
		else
			return false;
	}

	private Map columnInfoMap = new HashMap();
	private Map tableInfoMap = new HashMap();
	private int largestColumnNum;

	public void parseFeedbackReport(Map map, String changeDataStatus,
			String configLowerStatusLinkage){
		if(map != null){
			Object objFlag = map.get("flag");
			Object packNameObj = map.get("packName");
			// Object totalfilesObj = map.get("TOTALFILES");
			Object errFileNamesObj = map.get("errFileNames");
			Object errRptBussNoObj = map.get("errRptBussNos");
			String userId = (String) map.get("userId");
			if(StringUtils.isEmpty(userId))
				userId = "system";
			if(objFlag != null){
				StringBuffer sbErrorMsg = new StringBuffer();
				// 获取报送文件名称
				String[] fileNames = null;
				if(errFileNamesObj != null){
					Object[] fileNameObjs = (Object[]) errFileNamesObj;
					if(fileNameObjs != null && fileNameObjs.length > 0){
						fileNames = new String[fileNameObjs.length];
						for(int i = 0; i < fileNameObjs.length; i++){
							fileNames[i] = String.valueOf(fileNameObjs[i])
									.toString();
							if(fileNames[i].indexOf("ERR") > -1){
								fileNames[i] = fileNames[i].replaceAll("ERR",
										"");
							}
						}
					}
				}
				// 获取出错报文对应申报号
				String[] rptBussNos = null;
				if(errRptBussNoObj != null){
					List rptBussNoList = (List) errRptBussNoObj;
					if(rptBussNoList != null && rptBussNoList.size() > 0){
						rptBussNos = new String[rptBussNoList.size()];
						for(int i = 0; i < rptBussNoList.size(); i++){
							rptBussNos[i] = String
									.valueOf(rptBussNoList.get(i)).toString();
						}
					}
				}
				if("TT".equals(String.valueOf(objFlag))){
					// 控制类反馈接收文件
					if(packNameObj != null){
						// 从返回文件包名获得报送文件包名
						String packName = String.valueOf(packNameObj)
								.substring(0, 25);
						// 修改报文报送记录表中对应报文包的所有记录为已收到反馈
						// 若T文件显示包中有报文文件存在错误，则该报文文件所对应记录不做修改
						dataDealService.updateRptSendCommitIsReceive(packName,
								fileNames, null, null, 1);
						// 根据报送包名查找对应所有报文记录
						List successList = dataDealService
								.findRptSendCommitSuccess(packName, fileNames);
						if(CollectionUtils.isNotEmpty(successList)){
							for(int i = 0; i < successList.size(); i++){
								RptSendCommit rptSc = (RptSendCommit) successList
										.get(i);
								if(rptSc != null
										&& StringUtil.isNotEmpty(rptSc
												.getTableId())
										&& StringUtil.isNotEmpty(rptSc
												.getFileName())){
									// 根据TableId和FileName关联更新对应记录状态为已报送
									Map params = new HashMap();
									RptData rd = new RptData();
									String strUpdateSet = " datastatus = "
											+ DataUtil.YBS_STATUS_NUM;
									StringBuffer sbUpdateCondition = new StringBuffer();
									sbUpdateCondition
											.append(
													" businessid in (select s.businessid from t_rpt_send_commit s where s.tableId = '")
											.append(
													rptSc.getTableId()
															.toUpperCase())
											.append("' and s.packName = '")
											.append(packName.toUpperCase())
											.append("' and s.fileName = '")
											.append(rptSc.getFileName())
											.append("') and (datastatus = ")
											.append(DataUtil.YSC_STATUS_NUM)
											.append(" or datastatus = ")
											.append(DataUtil.LOCKED_STATUS_NUM)
											.append(") ");
									rd.setTableId(rptSc.getTableId());
									rd.setUpdateSql(strUpdateSet);
									rd.setUpdateCondition(sbUpdateCondition
											.toString());
									params.put("rptData", rd);
									this.save("updateRptData", params);
									params.clear();
								}
							}
						}
						/*
						List list = dataDealService.findRptSendCommit(null,
								null, packName, null, 1);
						if(list != null && list.size() > 0){
							String errFileNames = "";
							if(fileNames != null && fileNames.length > 0){
								for(int i = 0; i < fileNames.length; i++){
									errFileNames += fileNames[i] + ";";
								}
							}
							for(int i = 0; i < list.size(); i++){
								RptSendCommit rptSc = (RptSendCommit) list
										.get(i);
								if(rptSc != null
										&& !"".equals(rptSc.getTableId())
										&& !"".equals(rptSc.getBusinessId())
										&& StringUtil.isNotEmpty(rptSc
												.getFileName())){
									// 判断报文所在数据文件是否属于在T文件所包含的错误报送文件中
									if(errFileNames.indexOf(rptSc.getFileName()
											.toUpperCase()) < 0){
										// FIXME CFA
										String fileType = rptSc.getFileName()
												.substring(3, 5);
										// 根据TableId和BusinessId查询记录
										List rptDataList = this.findRptDatas(
												rptSc.getTableId(), fileType,
												rptSc.getBusinessId(), null);
										// 判断当前要处理的记录是否还存在于对应单据数据库表中
										if(Assert.isEmpty(rptDataList)
												|| rptDataList.get(0) == null){
											// 当前要处理记录已被删除
											sbErrorMsg
													.append("[")
													.append(
															this
																	.getTableName(rptSc
																			.getTableId()))
													.append("]中业务号[")
													.append(
															rptSc
																	.getBusinessId())
													.append(
															"]的记录不存在，可能已被删除！<br>");
										}else{
											RptData sendRptData = (RptData) rptDataList
													.get(0);
											sendRptData.setTableId(rptSc
													.getTableId());
											// 修改报文中所涉及到的记录状态为6-已报送
											dataDealService
													.updateRptData(new RptData(
															rptSc.getTableId(),
															" datastatus = "
																	+ DataUtil.YBS_STATUS_NUM
																	+ "",
															rptSc
																	.getBusinessId(),
															null, null, true));
											// 在"报文修改记录"表中保存正确的报送记录
											// this.saveRptLogInfo(rptSc.getTableId(),
											// rptSc.getBusinessId());
											// 取消掉在接收到正确反馈后记录success操作日志
											// this.saveRptLogInfo(sendRptData,
											// userId);
										}
									}
								}
							}
						}*/
					}
				}else{
					// 报文类反馈接收文件
					if(fileNames != null && fileNames.length == 1){
						// 根据报送XML文件名查找对应所有报文记录
						List list = dataDealService.findRptSendCommit(null,
								null, null, fileNames[0], -1);
						String fileType = fileNames[0].substring(3, 5);
						String keyRptNoColumnId = DataUtil
								.getRptNoColumnIdByFileType(fileType);
						String byeRptNoColumnId = DataUtil
								.getByeRptNoColumnIdByFileType(fileType);
						String tableId = DataUtil
								.getTableIdByFileType(fileType);
						if(list != null && list.size() > 0){
							List datas = new ArrayList(); // 需更新状态的业务记录
							List linkageData = new ArrayList(); // 需联动更新状态的业务记录
							List errorHasReject = new ArrayList(); // 需修改has_reject字段的反馈记录
							List refuseData = new ArrayList(); // 需记录打回原因的业务记录
							List correctData = new ArrayList(); // 需修改is_receive字段的报送记录
							for(int i = 0; i < list.size(); i++){
								RptSendCommit rptSc = (RptSendCommit) list
										.get(i);
								if(rptSc != null){
									String businessId = rptSc.getBusinessId();
									RptData rd = new RptData();
									rd.setTableId(tableId);
									rd.setBusinessId(businessId);
									rd.setRptNoColumnId(keyRptNoColumnId);
									rd.setByeRptNoColumnId(byeRptNoColumnId);
									List listReduce = null;
									listReduce = dataDealService
											.findRptDataReduce(rd);
									if(listReduce != null
											&& listReduce.size() == 1){
										rd = (RptData) listReduce.get(0);
										rd.setTableId(tableId);
										boolean err = false;
										String errorDataNo = null;
										// 若该报文类反馈文件已经上传过，则此处rptNos为null
										if(rptBussNos != null){
											for(int r = 0; r < rptBussNos.length; r++){
												String errKeyRptNo = DataUtil
														.getKeyRptNoForBussNo(rptBussNos[r]);
												String errByeRptNo = DataUtil
														.getByeRptNoForBussNo(rptBussNos[r]);
												if(errKeyRptNo.equals(rd
														.getRptNo())){
													if(!""
															.equals(byeRptNoColumnId)
															&& !""
																	.equals(errByeRptNo)){
														if(errByeRptNo
																.equals(rd
																		.getByeRptNo())){
															err = true;
															errorDataNo = rptBussNos[r];
															break;
														}
													}else{
														err = true;
														errorDataNo = rptBussNos[r];
														break;
													}
												}
											}
											if(err){
												// 对应报文反馈提示有误
												if(String
														.valueOf(
																DataUtil.WJY_STATUS_NUM)
														.equalsIgnoreCase(
																changeDataStatus)){
													// 更新当前记录状态到1-未校验
													rd
															.setDataStatus(String.valueOf(DataUtil.WJY_STATUS_NUM));
													datas.add(rd);
													/*
													dataDealService
															.updateRptData(new RptData(
																	tableId,
																	" datastatus = "
																			+ DataUtil.WJY_STATUS_NUM
																			+ " ",
																	businessId,
																	null));*/
													// 修改反馈记录表has_reject字段
													errorHasReject
															.add(errorDataNo);
													/*
													this.updateHasReject("1",
															errorDataNo,
															fileNames[0]);
													*/
													// 记录打回原因
													rd
															.setReasionInfo("反馈接收 信息错误");
													refuseData.add(rd);
													/*
													this
															.saveRefuseResion(
																	tableId,
																	businessId);*/
													// 联动将下游报文状态置成1-未校验
													if("yes"
															.equalsIgnoreCase(configLowerStatusLinkage)
															&& TableIdRela
																	.getZjxyMap()
																	.get(
																			fileType) != null){
														RptData rLinkage = new RptData();
														rLinkage
																.setDataStatus(String
																		.valueOf(DataUtil.WJY_STATUS_NUM));
														rLinkage
																.setFileType(fileType);
														rLinkage
																.setBusinessId(rd
																		.getBusinessId());
														linkageData
																.add(rLinkage);
														/*
														RptData rdXY = new RptData(
																tableId,
																" datastatus = "
																		+ DataUtil.WJY_STATUS_NUM
																		+ " ",
																null, null);
														String updateCondition = " filetype <> '"
																+ fileType
																+ "' and exists (select 1 from "
																+ tableId
																+ " c where c.businessno = businessno and businessid = '"
																+ businessId
																+ "') and datastatus <> "
																+ DataUtil.YBS_STATUS_NUM
																+ " and datastatus <> "
																+ DataUtil.LOCKED_STATUS_NUM
																+ " and datastatus <> "
																+ DataUtil.DELETE_STATUS_NUM;
														rdXY
																.setUpdateCondition(updateCondition);
														dataDealService
																.updateRptData(rdXY);*/
													}
												}else if(String
														.valueOf(
																DataUtil.YBS_STATUS_NUM)
														.equalsIgnoreCase(
																changeDataStatus)
														|| StringUtil
																.isBlank(changeDataStatus)){
													// 在"报文修改记录"表中查询该记录是否有过成功报送的记录
													RptLogInfo rptLogInfo = new RptLogInfo();
													rptLogInfo
															.setTableid(tableId);
													rptLogInfo
															.setBusinessid(businessId);
													rptLogInfo
															.setDatastatus(String
																	.valueOf(DataUtil.YBS_STATUS_NUM));
													// 默认按时间倒序查询
													List rptLogList = dataDealService
															.findRptLogInfo(
																	rptLogInfo,
																	null);
													if(rptLogList != null
															&& rptLogList
																	.size() > 0){
														rptLogInfo = (RptLogInfo) rptLogList
																.get(0);
														if(rptLogInfo != null
																&& tableId
																		.equals(rptLogInfo
																				.getTableid())
																&& businessId
																		.equals(rptLogInfo
																				.getBusinessid())){
															// 将记录还原到上次正确报送的数据
															this
																	.renewLastSendRpt(rptLogInfo);
														}
													}else{
														// 未查询到曾经正确报送的记录，解除该记录的锁定
														rd
																.setDataStatus(String.valueOf(DataUtil.YSC_STATUS_NUM));
														datas.add(rd);
														/*
														dataDealService
																.updateRptData(new RptData(
																		tableId,
																		" datastatus = "
																				+ DataUtil.YSC_STATUS_NUM
																				+ " ",
																		businessId,
																		null,
																		null,
																		true));*/
													}
												}
											}else{
												// 对应报文反馈提示无误
												// 修改报文报送记录表中对应报文包的所有记录为已收到反馈
												correctData.add(rd);
												/*
												dataDealService
														.updateRptSendCommitIsReceive(
																null,
																new String[] {fileNames[0]},
																tableId,
																businessId, 1);*/
												// 修改报文中所涉及到的记录状态为6-已报送
												rd
														.setDataStatus(String.valueOf(DataUtil.YBS_STATUS_NUM));
												datas.add(rd);
												/*
												dataDealService
														.updateRptData(new RptData(
																tableId,
																" datastatus = "
																		+ DataUtil.YBS_STATUS_NUM
																		+ " ",
																businessId,
																null, null,
																true));*/
											}
										}
									}else{
										// 当前要处理记录已被删除
										sbErrorMsg.append("[").append(
												this.getTableName(rptSc
														.getTableId())).append(
												"]中业务号[").append(
												rptSc.getBusinessId()).append(
												"]的记录不存在，可能已被删除！");
									}
								}
								if(datas.size() >= DataUtil.BATCH_SIZE_MAX){
									dataDealService.updateRptDatastatus(
											tableId, datas);
									datas.clear();
								}
								if(linkageData.size() >= DataUtil.BATCH_SIZE_MAX){
									dataDealService.batchLowerStatusLinkage(
											tableId, linkageData);
									linkageData.clear();
								}
								if(errorHasReject.size() >= DataUtil.BATCH_SIZE_MAX){
									this.updateHasReject(fileNames[0],
											errorHasReject);
									errorHasReject.clear();
								}
								if(refuseData.size() >= DataUtil.BATCH_SIZE_MAX){
									dataDealService.modifyRefuseReasion("2",
											refuseData);
									refuseData.clear();
								}
								if(correctData.size() >= DataUtil.BATCH_SIZE_MAX){
									dataDealService
											.updateRptSendCommitIsReceiveBatch(
													correctData, "1",
													fileNames[0]);
									correctData.clear();
								}
							}
							if(datas.size() > 0){
								dataDealService.updateRptDatastatus(tableId,
										datas);
								datas.clear();
							}
							if(linkageData.size() > 0){
								dataDealService.batchLowerStatusLinkage(
										tableId, linkageData);
								linkageData.clear();
							}
							if(errorHasReject.size() > 0){
								this.updateHasReject(fileNames[0],
										errorHasReject);
								errorHasReject.clear();
							}
							if(refuseData.size() > 0){
								dataDealService.modifyRefuseReasion("2",
										refuseData);
								refuseData.clear();
							}
							if(correctData.size() > 0){
								dataDealService
										.updateRptSendCommitIsReceiveBatch(
												correctData, "1", fileNames[0]);
								correctData.clear();
							}
						}
					}
				}
				if(sbErrorMsg.toString().length() > 0){
					map.put("receiveErrorMsg", sbErrorMsg.toString());
				}
			}
		}
	}

	/**
	 * <p> 方法名称: findRptDatas|描述: 查询报文信息列表 </p>
	 * @param tableId 表ID
	 * @param businessId 业务ID
	 * @param rptColumnList 报文列信息
	 * @return List
	 */
	private List findRptDatas(String tableId, String fileType,
			String businessId, List rptColumnList){
		List rptDataList = null;
		try{
			if(rptColumnList == null){
				rptColumnList = (List) this.columnInfoMap.get(tableId);
				if(rptColumnList == null){
					rptColumnList = dataDealService
							.findRptColumnInfo(new RptColumnInfo(tableId, null,
									"1", fileType));
					this.columnInfoMap.put(tableId, rptColumnList);
				}
			}
			// 循环列信息，作各种处理
			int cFlag = 0;
			StringBuffer columns = new StringBuffer();
			for(Iterator i = rptColumnList.iterator(); i.hasNext();){
				RptColumnInfo column = (RptColumnInfo) i.next();
				if("table".equals(column.getDataType())){
					continue;
				}
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据字段物理名和别名拼查询SQL
				if("BUSINESSNO".equalsIgnoreCase(column.getColumnId())){
					columns.append("t.").append(column.getColumnId()).append(
							" as businessNo,");
				}else{
					columns.append("t.").append(column.getColumnId()).append(
							" as ").append(column.getAliasColumnId()).append(
							",");
				}
			}
			while(cFlag < largestColumnNum){
				columns.append("'' as c").append(++cFlag).append(",");
			}
			// 查询主表
			rptDataList = dataDealService.findRptData(new RptData(tableId,
					columns.toString().substring(0,
							columns.toString().length() - 1), null, null,
					businessId, null, null));
		}catch (Exception e){
			e.printStackTrace();
		}
		return rptDataList;
	}

	/**
	 * <p> 方法名称: getTableName|描述: 得到表名称 </p>
	 * @param tableId
	 * @return String
	 */
	private String getTableName(String tableId){
		if(this.tableInfoMap.get(tableId) == null){
			List rptTableList = dataDealService.findRptTableInfo(
					new RptTableInfo(null, "1"), "");
			if(Assert.notEmpty(rptTableList)){
				for(Iterator i = rptTableList.iterator(); i.hasNext();){
					RptTableInfo tableInfo = (RptTableInfo) i.next();
					this.tableInfoMap.put(tableInfo.getTableId(), tableInfo);
				}
			}
		}
		RptTableInfo tableInfo = (RptTableInfo) this.tableInfoMap.get(tableId);
		if(tableInfo != null){
			return tableInfo.getTableName();
		}else{
			return "";
		}
	}

	/**
	 * <p> 方法名称: saveRptLogInfo|描述: 保存报送成功的报文记录 </p> <p>
	 * 对于接收到正确反馈报文的记录，将其当前数据保存到报文修改记录表t_rpt_log_info中 </p>
	 * @param tableId
	 * @param businessId
	 */
	private void saveRptLogInfo(RptData sendRptData, String userId){
		try{
			if(sendRptData != null){
				List rptColumnList = (List) this.columnInfoMap.get(sendRptData
						.getTableId());
				if(rptColumnList == null){
					rptColumnList = dataDealService
							.findRptColumnInfo(new RptColumnInfo(sendRptData
									.getTableId(), null, "1", sendRptData
									.getFileType()));
					this.columnInfoMap.put(sendRptData.getTableId(),
							rptColumnList);
				}
				if(!"".equals(sendRptData.getRptNo())){
					sendRptData.setTableId(sendRptData.getTableId());
					// 所含子表ID
					List subTableIds = new ArrayList();
					// 系统时间
					String serverTime = DateUtils.serverCurrentTimeStamp();
					this.saveRptLogInfoData(sendRptData, "success", userId,
							serverTime, rptColumnList, subTableIds);
					// 添加子表记录
					if(subTableIds.size() > 0){
						for(int i = 0; i < subTableIds.size(); i++){
							String subTableId = (String) subTableIds.get(i);
							List subRptColumnList = (List) this.columnInfoMap
									.get(subTableId);
							if(subRptColumnList == null){
								subRptColumnList = dataDealService
										.findRptColumnInfo(new RptColumnInfo(
												subTableId, null, "1", null));
								this.columnInfoMap.put(subTableId,
										subRptColumnList);
							}
							List subRds = this.findRptDatas(subTableId, null,
									sendRptData.getBusinessId(),
									subRptColumnList);
							if(subRds != null && subRds.size() > 0){
								for(int s = 0; s < subRds.size(); s++){
									RptData subRd = (RptData) subRds.get(s);
									if(subRd != null
											&& sendRptData
													.getBusinessId()
													.equals(
															subRd
																	.getBusinessId())){
										subRd.setTableId(subTableId);
										this.saveRptLogInfoData(subRd,
												"success", userId, serverTime,
												subRptColumnList,
												new ArrayList());
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * <p> 方法名称: renewLastSendRpt|描述: 还原上次正确报送的数据 </p>
	 * @param rptLogInfo
	 */
	private void renewLastSendRpt(RptLogInfo rptLogInfo){
		try{
			List rptColumnList = (List) this.columnInfoMap.get(rptLogInfo
					.getTableid());
			if(rptColumnList == null){
				// FIXME CFA
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(rptLogInfo
								.getTableid(), null, "1", rptLogInfo
								.getFiletype()));
				this.columnInfoMap.put(rptLogInfo.getTableid(), rptColumnList);
			}
			// 记录子表ID
			List subTableIds = new ArrayList();
			// 为主表还原上次正确报送的数据
			StringBuffer updateSql = new StringBuffer("");
			for(Iterator i = rptColumnList.iterator(); i.hasNext();){
				RptColumnInfo column = (RptColumnInfo) i.next();
				if(StringUtils.isEmpty(column.getLogColumnId())
						&& !"table".equalsIgnoreCase(column.getDataType())){
					continue;
				}
				if("table".equalsIgnoreCase(column.getDataType())
						&& subTableIds != null){
					subTableIds.add(column.getColumnId());
				}else{
					String columnValue = (String) org.apache.commons.beanutils.BeanUtils
							.getProperty(rptLogInfo, column.getLogColumnId()
									.toLowerCase());
					if(columnValue == null){
						columnValue = "";
					}
					if("n".equalsIgnoreCase(column.getDataType()
							.substring(0, 1))){
						columnValue = columnValue.replaceAll(",", "");
						columnValue = columnValue.replaceAll("'", "''");
						if(columnValue == null || "".equals(columnValue.trim())){
							columnValue = "null";
						}
						updateSql.append(column.getColumnId()).append("=")
								.append(columnValue).append(",");
					}else{
						if("ACTIONTYPE".equals(column.getColumnId())
								&& "".equals(columnValue)){
							columnValue = "A";
						}
						updateSql.append(column.getColumnId()).append("='")
								.append(columnValue.replaceAll("'", "''"))
								.append("',");
					}
				}
			}
			// 还原主表状态:8-已报送
			updateSql.append(" datastatus = " + DataUtil.YBS_STATUS_NUM);
			dataDealService.updateRptData(new RptData(rptLogInfo.getTableid(),
					updateSql.toString(), rptLogInfo.getBusinessid(),
					rptLogInfo.getSubid(), null, true));
			// 判断是否包含子表
			if(subTableIds != null && subTableIds.size() > 0){
				for(int i = 0; i < subTableIds.size(); i++){
					// 子表ID
					String subTableId = (String) subTableIds.get(i);
					// 子表列信息
					List subRptColumnList = (List) this.columnInfoMap
							.get(subTableId);
					if(subRptColumnList == null){
						subRptColumnList = dataDealService
								.findRptColumnInfo(new RptColumnInfo(
										subTableId, null, "1", null));
						this.columnInfoMap.put(subTableId, subRptColumnList);
					}
					// 查询子表前次正确报送信息
					RptLogInfo subRptLogInfo = new RptLogInfo();
					subRptLogInfo.setTableid(subTableId);
					subRptLogInfo.setBusinessid(rptLogInfo.getBusinessid());
					subRptLogInfo.setUpdatetime(rptLogInfo.getUpdatetime());
					// 默认按时间倒序查询
					List rptLogList = dataDealService.findRptLogInfo(
							subRptLogInfo, null);
					if(rptLogList != null && rptLogList.size() > 0){
						// 存在子表上次正确报送记录，删除现有子表信息，以便恢复
						RptData rd = new RptData();
						rd.setTableId(subTableId);
						rd.setBusinessId(rptLogInfo.getBusinessid());
						dataDealService.deleteRptData(rd);
						// 逐笔恢复上次正确报送记录
						for(int s = 0; s < rptLogList.size(); s++){
							subRptLogInfo = (RptLogInfo) rptLogList.get(s);
							if(subRptLogInfo != null
									&& subTableId.equals(subRptLogInfo
											.getTableid())
									&& rptLogInfo.getBusinessid().equals(
											subRptLogInfo.getBusinessid())){
								// 向子表插入上次正确报送时的记录
								StringBuffer insertColumns = new StringBuffer(
										"businessid,subid,");
								StringBuffer insertValues = new StringBuffer(
										"'").append(
										subRptLogInfo.getBusinessid()).append(
										"','").append(subRptLogInfo.getSubid())
										.append("',");
								for(Iterator sr = subRptColumnList.iterator(); sr
										.hasNext();){
									RptColumnInfo column = (RptColumnInfo) sr
											.next();
									String columnValue = (String) org.apache.commons.beanutils.BeanUtils
											.getProperty(subRptLogInfo, column
													.getLogColumnId()
													.toLowerCase());
									if(columnValue == null
											|| "".equals(columnValue.trim())){
										continue;
									}
									if("n".equalsIgnoreCase(column
											.getDataType().substring(0, 1))){
										columnValue = columnValue.replaceAll(
												",", "");
										columnValue = columnValue.replaceAll(
												"'", "''");
										insertColumns.append(
												column.getColumnId()).append(
												",");
										insertValues.append(columnValue)
												.append(",");
									}else if("d".equalsIgnoreCase(column
											.getDataType().substring(0, 1))){
										// TODO 可能区分数据库类型
										if(columnValue != null
												&& columnValue.length() == 8){
											updateSql.append(
													column.getColumnId())
													.append("=to_date('")
													.append(columnValue)
													.append("','yyyyMMdd'),");
										}
									}else{
										columnValue = columnValue.replaceAll(
												"'", "''");
										insertColumns.append(
												column.getColumnId()).append(
												",");
										insertValues.append("'").append(
												columnValue).append("',");
									}
								}
								// 执行保存操作
								dataDealService.saveRptData(subTableId,
										insertColumns.toString().substring(0,
												insertColumns.length() - 1),
										insertValues.toString().substring(0,
												insertValues.length() - 1));
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error("ReceiveReportAction-renewLastSendRpt", e);
		}
	}

	public DataDealService getDataDealService(){
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public int getLargestColumnNum(){
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum){
		this.largestColumnNum = largestColumnNum;
	}

	/**
	 * <p> 方法名称: saveRptLogInfoData|描述: 保存报送成功的报文记录进数据库 </p>
	 * @param rd RptData对象
	 * @param logType 日志类型
	 * @param userId 当前用户ID
	 * @param serverTime 当前系统时间
	 * @param rptColumnList 报文列信息
	 * @param subTableIds 报文子表ID列表
	 */
	public void saveRptLogInfoData(RptData rd, String logType, String userId,
			String serverTime, List rptColumnList, List subTableIds){
		try{
			StringBuffer insertLogColumns = new StringBuffer();
			StringBuffer insertLogValues = new StringBuffer();
			String subId = rd.getSubId();
			if(subId == null){
				subId = "";
			}
			int datastatus = -1;
			if("send".equalsIgnoreCase(logType)){
				datastatus = DataUtil.YSC_STATUS_NUM;
			}else if("success".equalsIgnoreCase(logType)){
				datastatus = DataUtil.YBS_STATUS_NUM;
			}
			insertLogColumns
					.append("LOGTYPE,TABLEID,FILETYPE,USERID,UPDATETIME,BUSINESSNO,BUSINESSID,SUBID,DATASTATUS");
			insertLogValues.append("'").append(logType).append("','").append(
					rd.getTableId()).append("','").append(rd.getFileType())
					.append("','").append(userId).append("','").append(
							serverTime).append("','")
					.append(rd.getBusinessNo()).append("','").append(
							rd.getBusinessId()).append("','").append(subId)
					.append("',").append(datastatus);
			int cFlag = 0;
			for(Iterator i = rptColumnList.iterator(); i.hasNext();){
				RptColumnInfo column = (RptColumnInfo) i.next();
				// 赋别名c1,c2,c3
				column.setAliasColumnId("c" + (++cFlag));
				// 根据别名获取属性值
				String columnValue = (String) org.apache.commons.beanutils.BeanUtils
						.getProperty(rd, column.getAliasColumnId());
				if(columnValue != null){
					if(column.getLogColumnId() != null
							&& !"".equalsIgnoreCase(column.getLogColumnId())){
						insertLogColumns.append(",").append(
								column.getLogColumnId());
						insertLogValues.append(",'").append(
								columnValue.replaceAll("'", "''")).append("'");
					}
				}
				if("table".equalsIgnoreCase(column.getDataType())
						&& subTableIds != null){
					subTableIds.add(column.getColumnId());
				}
			}
			// 添加报文修改记录
			dataDealService.saveRptData("t_rpt_log_info", insertLogColumns
					.toString(), insertLogValues.toString());
		}catch (Exception e){
			e.printStackTrace();
			log.error("ReceiveReportAction-saveRptLogInfoData", e);
		}
	}

	public List getFileReceiveStat(String whereCondition,
			PaginationList paginationList){
		Map map = new HashMap();
		map.put("whereCondition", whereCondition);
		List list = this.find("getFileReceiveStat", map, paginationList);
		return list;
	}

	private void updateHasReject(String fileName, final List dataNoList){
		String sql = "update t_error_feedback set has_reject = '1' "
				+ " where data_no = ? and rpt_send_file_name like '" + fileName
				+ "%' ";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter(){

			public void setValues(PreparedStatement pStatement, int i)
					throws SQLException{
				String dataNo = (String) dataNoList.get(i);
				pStatement.setString(1, dataNo);
			}

			public int getBatchSize(){
				return dataNoList.size();
			}
		});
	}
}
