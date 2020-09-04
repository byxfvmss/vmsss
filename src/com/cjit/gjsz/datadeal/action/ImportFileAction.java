package com.cjit.gjsz.datadeal.action;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.ExcelHelper;
import com.cjit.common.util.ExcelUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.datadeal.util.DataValidater;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.RptTemplateService;

public class ImportFileAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private final int batchSize = 200; // oracle10默认最大游标数是300，超过这个数就会抛出异常：超出最大游标数
	private List errorList;
	private RptTemplateService rptTemplateService;
	private OrganizationService organizationService;

	public String importPage() {
		return SUCCESS;
	}

	public String importExcel() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				FileInputStream is = new FileInputStream(files[0]);
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				String tableId = workbook.getSheetName(0);
				if (tableId.indexOf(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL) > -1)
					this.setErrorList(doImportFile(workbook));
				else {
					this.setErrorList(doImportFileSafe(workbook));
				}
				this.message = "上传文件完成!";
			} catch (Exception e) {
				this.message = "上传文件失败:" + e.getMessage();
				e.printStackTrace();
				log.error(e);
				return SUCCESS;
			}
		}
		return SUCCESS;
	}

	private List doImportFile(HSSFWorkbook workbook) throws Exception {
		List errorList = new ArrayList();
		final List insertList = new ArrayList();
		final List updateList = new ArrayList();
		final List insertLogList = new ArrayList();
		final List updateLogList = new ArrayList();
		boolean isInsert = false;
		List specialColumnList = new ArrayList();
		// 第一页标示了报表唯一id
		String tableUniqueId = workbook.getSheetName(0);
		RptData rptData = new RptData();
		rptData.setTableId(tableUniqueId
				.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0]);
		rptData.setFileType(tableUniqueId
				.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1]);
		RptTableInfo tableInfo = dataDealService.findRptTableInfoById(rptData
				.getTableId(), rptData.getFileType());
		if (tableInfo == null || StringUtil.isEmpty(tableInfo.getTableName())) {
			errorList.add("数据导入失败,原因:不能正确识别的标签页名称[" + tableUniqueId + "]");
			return errorList;
		}
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(rptData.getTableId(),
						null, "1", rptData.getFileType()));
		List instIdList = getInstIdList(this.authInstList);
		// 用到的参数
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
		if (dictionaryMap == null)
			throw new Exception("字典项不能为空,请刷新缓存!");
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// 获得结构
		// 获得第一个sheet页
		HSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet != null) {
			int nRowCount = sheet.getLastRowNum() + 1;
			// 第一行，获得插入结构
			HSSFRow row = sheet.getRow(0);
			int nColumnCount = row.getLastCellNum() - 1;// 不包含最后一个单元格
			// 校验列是否正确
			// 1.获得行列顺序,从第四个单元格起
			List columnList = getInsertOrder(rptColumnList, row);
			// 2.获得插入列语句
			StringBuffer insertColumn = null;
			StringBuffer insertLogColumns = null;
			insertLogColumns = new StringBuffer(
					"logtype,businessid,tableid,datastatus,filetype,updatetime,userid,");
			insertColumn = new StringBuffer(
					"businessid,instcode,datastatus,filetype,importdate,modifyuser,");
			for (Iterator it = columnList.iterator(); it.hasNext();) {
				RptColumnInfo column = (RptColumnInfo) it.next();
				if (isSpecialColumn(column, rptData.getFileType()))
					continue;// 最后处理
				insertColumn.append(column.getColumnId()).append(",");
				if (StringUtils.isNotEmpty(column.getLogColumnId()))
					insertLogColumns.append(column.getLogColumnId())
							.append(",");
			}
			// 特殊数据插入
			RptColumnInfo specialColumn = getColumn(columnList, "ACTIONTYPE",
					false);
			if (specialColumn != null) {
				specialColumnList.add(specialColumn);
				insertColumn.append(specialColumn.getColumnId()).append(",");
				if (StringUtils.isNotEmpty(specialColumn.getLogColumnId()))
					insertLogColumns.append(specialColumn.getLogColumnId())
							.append(",");
			}
			// specialColumn=
			// getColumn(columnList,DataUtil.getRptNoColumnIdByFileType(rptData.getFileType()),false);
			String strColumn = StringUtils.removeEnd(insertColumn.toString(),
					",");
			String strLogColumn = StringUtils.removeEnd(insertLogColumns
					.toString(), ",");
			// 第二行开始获得数据
			for (int j = 1; j < nRowCount; j++) {
				row = sheet.getRow(j);
				StringBuffer insertValues = new StringBuffer();
				StringBuffer updateValues = new StringBuffer();
				StringBuffer insertLogValues = new StringBuffer();
				boolean isOK = true;
				// 前四个
				// order 1 businessid
				HSSFCell cell = row.getCell((short) 0);
				String value = ExcelUtil.getStringCellValue(cell, null);
				if (StringUtils.isEmpty(value)) {// 插入方法-insert
					isInsert = true;
					value = createBusinessId();
					insertValues.append("'").append(value).append("',");
					insertLogValues.append("'insert','").append(value).append(
							"','").append(rptData.getTableId()).append("',");
				} else {
					String t = dataDealService.findRptDataStatus(rptData
							.getTableId(), value);
					if (StringUtils.isEmpty(t)) {
						errorList.add(combinationInfo(j + 1, 1,
								"该业务ID对应的数据未找到,不做更新!"));
						continue;
					}
					boolean canUpdate = false;
					switch (Integer.parseInt(t)) {
					case DataUtil.SHYTG_STATUS_NUM:
						errorList.add(combinationInfo(j + 1, 1,
								"该业务主键对应的数据状态为[" + DataUtil.SHYTG_STATUS_CH
										+ "],不做更新!"));
						break;
					case DataUtil.YSC_STATUS_NUM:
						errorList.add(combinationInfo(j + 1, 1,
								"该业务主键对应的数据状态为[" + DataUtil.YSC_STATUS_CH
										+ "],不做更新!"));
						break;
					case DataUtil.YBS_STATUS_NUM:
						errorList.add(combinationInfo(j + 1, 1,
								"该业务主键对应的数据状态为[" + DataUtil.YBS_STATUS_CH
										+ "],不做更新!"));
						break;
					case DataUtil.LOCKED_STATUS_NUM:
						errorList.add(combinationInfo(j + 1, 1,
								"该业务主键对应的数据状态为[" + DataUtil.LOCKED_STATUS_CH
										+ "],不做更新!"));
						break;
					default:
						canUpdate = true;
					}
					if (!canUpdate)
						continue;
					else {
						isInsert = false;
						rptData.setBusinessId(value);
						insertLogValues.append("'update','").append(value)
								.append("','").append(rptData.getTableId())
								.append("',");
					}
				}
				// order 1 instcode
				cell = row.getCell((short) 1);
				value = ExcelUtil.getStringCellValue(cell, null);
				if (StringUtils.isEmpty(value)) {
					errorList.add(combinationInfo(j + 1, 2, "机构ID不能为空!"));
					continue;
				} else {
					if (!instIdList.contains(value)) {
						errorList.add(combinationInfo(j + 1, 2,
								"当前用户没有权限导入该机构[" + value + "]的数据!"));
						continue;
					}
				}
				insertValues.append("'").append(value).append("',");
				updateValues.append("instcode='").append(value).append("',");
				// order 2 datastatus
				insertValues.append(DataUtil.WJY_STATUS_NUM).append(",");
				updateValues.append("datastatus=").append(
						DataUtil.WJY_STATUS_NUM).append(",");
				insertLogValues.append(DataUtil.WJY_STATUS_NUM).append(",");
				// order 3 filetype
				insertValues.append("'").append(rptData.getFileType()).append(
						"',");
				updateValues.append("filetype ='")
						.append(rptData.getFileType()).append("',");
				insertLogValues.append("'").append(rptData.getFileType())
						.append("',");
				// order 4 date
				Date date = new Date();
				if ("oracle".equalsIgnoreCase(this.getDbType())) {
					String strDate = DateUtils.toString(date,
							DateUtils.ORA_DATES_FORMAT);
					insertValues.append("to_date('" + strDate
							+ "','yyyy-mm-dd'),");
					// 更新操作时，不对importdate字段进行修改
					// updateValues.append("importdate=to_date('" + strDate +
					// "','yyyy-mm-dd'),");
				} else if ("db2".equalsIgnoreCase(this.getDbType())) {
					String strDate = DateUtils.toString(date,
							DateUtils.ORA_DATE_TIMES_FORMAT);
					insertValues.append("timestamp('" + strDate + "'),");
					// 更新操作时，不对importdate字段进行修改
					// updateValues.append("importdate=timestamp('" + strDate +
					// "'),");
				}
				insertLogValues.append("'").append(
						DateUtils.serverCurrentTimeStamp()).append("',");
				// order 5 user
				insertValues.append("'").append(currentUser.getId()).append(
						"',");
				updateValues.append("modifyuser='").append(currentUser.getId())
						.append("',");
				insertLogValues.append("'").append(currentUser.getId()).append(
						"',");
				RptColumnInfo column = null;
				// 后面内容
				for (int k = 4; k < nColumnCount; k++) {
					column = (RptColumnInfo) columnList.get(k - 4);
					if (isSpecialColumn(column, rptData.getFileType()))
						continue;// 特殊数据 不处理
					cell = row.getCell((short) k);
					if (column.getDataType().startsWith("n")) {
						NumberFormat format = new DecimalFormat("0");
						if (column.getDataType().endsWith(",2")) {
							format = new DecimalFormat("0.00");
						} else if (column.getDataType().endsWith(",8")) {
							format = new DecimalFormat("0.00000000");
						} else if (column.getDataType().endsWith(",9")) {
							format = new DecimalFormat("0.000000000");
						} else if (column.getDataType().endsWith(",10")) {
							format = new DecimalFormat("0.0000000000");
						} else if (column.getDataType().endsWith(",11")) {
							format = new DecimalFormat("0.00000000000");
						}
						value = ExcelUtil.getStringCellValue(cell, format);
					} else {
						value = ExcelUtil.getStringCellValue(cell, null);
					}
					// 过滤字典项，如果字典项为空，则跳过比对，仅验证该空格是否可以为空
					if ("3".equals(column.getTagType())
							&& !StringUtils.isEmpty(value)) {
						if (dictionaryMap != null) {
							Map tableMap = (HashMap) dictionaryMap.get(rptData
									.getTableId());
							if (tableMap != null) {
								List codeDictionaryList = (ArrayList) tableMap
										.get(column.getDictionaryTypeId());
								if (codeDictionaryList != null) {
									value = getKeyFromDict(codeDictionaryList,
											value);
									if (StringUtils.isEmpty(value)) {
										isOK = false;
										errorList.add(combinationInfo(j + 1,
												k + 1, "未找到对应的字典项数据!"));
										break;
									}
								}
							}
						}
					}
					String info = validatePass(column, value);
					if (!StringUtils.isEmpty(info)) {
						isOK = false;
						errorList.add(combinationInfo(j + 1, k + 1, info));
						break;
					}
					// 拼接sql
					if ("n".equalsIgnoreCase(column.getDataType().substring(0,
							1))) {
						if (StringUtils.isEmpty(value)) {
							insertValues.append("null,");
							updateValues.append(column.getColumnId()).append(
									"=null,");
						} else {
							value = value.replaceAll(",", "");
							insertValues.append(value).append(",");
							updateValues.append(column.getColumnId()).append(
									"=").append(value).append(",");
						}
					} else {
						if (StringUtils.isEmpty(value)) {
							insertValues.append("null,");
							updateValues.append(column.getColumnId()).append(
									"=null,");
						} else {
							insertValues.append("'").append(
									value.replaceAll("'", "''")).append("',");
							updateValues.append(column.getColumnId()).append(
									"='").append(value.replaceAll("'", "''"))
									.append("',");
						}
					}
					if (StringUtils.isNotEmpty(column.getLogColumnId())) {
						if (value != null) {
							insertLogValues.append("'").append(
									value.replaceAll("'", "''")).append("',");
						} else {
							insertLogValues.append("null,");
						}
					}
				}// for end 单条记录
				// 特殊数据插入
				for (Iterator it = specialColumnList.iterator(); it.hasNext();) {
					column = (RptColumnInfo) it.next();
					if ("ACTIONTYPE".equals(column.getColumnId())) {// 新数据,强制插入新建
						if (isInsert) {
							insertValues.append("'A',");
							if (StringUtils.isNotEmpty(column.getLogColumnId()))
								insertLogValues.append("'A',");
						} else {
							if (StringUtils.isNotEmpty(column.getLogColumnId()))
								insertLogValues.append("null,");
						}
					}
				}
				// 执行更新
				if (isOK) {
					if (isInsert) {
						if (StringUtil
								.isNotEmpty(this.importExcelInsertIsHandiworkValue)) {
							if (strColumn.toUpperCase().indexOf("IS_HANDIWORK") < 0) {
								strColumn += ",IS_HANDIWORK";
							}
							insertValues.append("'").append(
									this.importExcelInsertIsHandiworkValue)
									.append("',");
						}
						Map param = new HashMap();
						param.put("tableId", rptData.getTableId());
						param.put("insertColumns", strColumn);
						param.put("insertValues", StringUtils.removeEnd(
								insertValues.toString(), ","));
						insertList.add(param);
						Map paramLog = new HashMap();
						paramLog.put("tableId", "t_rpt_log_info");
						paramLog.put("insertColumns", strLogColumn);
						paramLog.put("insertValues", StringUtils.removeEnd(
								insertLogValues.toString(), ","));
						insertLogList.add(paramLog);
						if (insertList.size() > batchSize) {
							dataDealService.saveRptDataBatch(insertList);
							dataDealService.saveRptDataBatch(insertLogList);
							insertList.clear();
							insertLogList.clear();
						}
					} else {
						Map param = new HashMap();
						param.put("rptData", new RptData(rptData.getTableId(),
								StringUtils.removeEnd(updateValues.toString(),
										","), rptData.getBusinessId(), null,
								null, true));
						updateList.add(param);
						Map paramLog = new HashMap();
						paramLog.put("tableId", "t_rpt_log_info");
						paramLog.put("insertColumns", strLogColumn);
						paramLog.put("insertValues", StringUtils.removeEnd(
								insertLogValues.toString(), ","));
						updateLogList.add(paramLog);
						if (updateList.size() > batchSize) {
							dataDealService.updateRptDataBatch(updateList);
							dataDealService.saveRptDataBatch(updateLogList);
							updateList.clear();
							updateLogList.clear();
						}
					}
				}
			}// end for row
			if (insertList.size() != 0) {
				dataDealService.saveRptDataBatch(insertList);
				dataDealService.saveRptDataBatch(insertLogList);
				insertList.clear();
				insertLogList.clear();
			}
			if (updateList.size() != 0) {
				dataDealService.updateRptDataBatch(updateList);
				dataDealService.saveRptDataBatch(updateLogList);
				updateList.clear();
				updateLogList.clear();
			}
		}// end if sheet
		// 添加报文修改记录
		// dataDealService.saveRptData("t_rpt_log_info", insertLogColumns
		// .toString(), insertLogValues.toString());
		// if(!validateData(rptColumnList, rptData, true))
		return errorList;
	}

	private List doImportFileSafe(HSSFWorkbook workbook) throws Exception {
		List errorList = new ArrayList();
		final List insertList = new ArrayList();
		final List insertLogList = new ArrayList();
		List instIdList = getInstIdList(this.authInstList);
		for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
			String saveTableId = workbook.getSheetName(sheetIndex);
			RptTableInfo tableInfo = dataDealService
					.findRptTableInfoBySaveTableId(saveTableId);
			if (tableInfo == null) {
				errorList.add("不能正确识别的标签页名称,第" + String.valueOf(sheetIndex)
						+ "页名称[" + saveTableId + "],该页数据不执行导入!");
				continue;
			}
			RptData rptData = new RptData();
			rptData.setTableId(tableInfo.getTableId());
			rptData.setFileType(tableInfo.getFileType());
			List rptColumnList = dataDealService
					.findRptColumnInfo(new RptColumnInfo(
							tableInfo.getTableId(), null, "1", null));
			// 用到的参数
			Map dictionaryMap = (Map) SystemCache
					.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
			if (dictionaryMap == null)
				throw new Exception("字典项不能为空,请刷新缓存!");
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			// 获得sheet页
			HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			if (sheet != null) {
				log.info("获得插入列sql语句");
				StringBuffer insertColumn = null;
				StringBuffer insertLogColumns = null;
				insertLogColumns = new StringBuffer(
						"logtype,businessid,tableid,datastatus,filetype,updatetime,userid,");
				insertColumn = new StringBuffer(
						"businessid,instcode,datastatus,filetype,importdate,modifyuser,");
				for (Iterator it = rptColumnList.iterator(); it.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) it.next();
					insertColumn.append(column.getColumnId()).append(",");
					if (StringUtils.isNotEmpty(column.getLogColumnId()))
						insertLogColumns.append(column.getLogColumnId())
								.append(",");
				}
				String strColumn = StringUtils.removeEnd(insertColumn
						.toString(), ",");
				log.info(strColumn);
				String strLogColumn = StringUtils.removeEnd(insertLogColumns
						.toString(), ",");
				log.info(strLogColumn);
				log.info("获取开户银行代码,托管行代码,托管人代码:");
				HSSFRow row = sheet.getRow(1);
				HSSFCell cell = row.getCell(0);
				String value = ExcelUtil.getStringCellValue(cell, null);
				value = StringUtils.replace(value, "：", ":");
				if (value.split(":").length <= 1) {
					errorList.add("工作页[" + saveTableId
							+ "]中第2行第1个单元格的数据内容不能为空,详情参见模板!");
					continue;
				}
				value = value.split(":")[1];
				rptData.setRptNo(value.trim());
				String instcode = organizationService.findInstCode(rptData
						.getRptNo());
				if (StringUtils.isEmpty(instcode)) {
					errorList
							.add("工作页["
									+ saveTableId
									+ "]中第2行第1个单元格中数据异常，填报的数据未能找到对应的机构,请先在机构对照管理中进行配置!");
					continue;
				} else if (!instIdList.contains(instcode)) {
					errorList.add("工作页[" + saveTableId
							+ "]中第2行第1个单元格数据异常,当前用户没有权限导入该申报号["
							+ rptData.getRptNo() + "]对应机构的数据!");
					continue;
				}
				rptData.setInstCode(instcode);
				log.info("获取数据");
				int beginRow = rptTemplateService.getBeginDataRow(saveTableId);
				for (int j = beginRow; j < sheet.getLastRowNum() + 1; j++) {
					// 判断不在需要获得数据的列
					if (ExcelHelper.isMerged(j, (short) 0, sheet))
						break;
					row = sheet.getRow(j);
					StringBuffer insertValues = new StringBuffer();
					StringBuffer insertLogValues = new StringBuffer();
					boolean isOK = true;
					// businessid
					value = createBusinessId();
					insertValues.append("'").append(value).append("',");
					insertLogValues.append("'insert','").append(value).append(
							"','").append(rptData.getTableId()).append("',");
					// instcode
					insertValues.append("'").append(rptData.getInstCode())
							.append("',");
					// datastatus
					insertValues.append(DataUtil.WJY_STATUS_NUM).append(",");
					insertLogValues.append(DataUtil.WJY_STATUS_NUM).append(",");
					// filetype
					insertValues.append("'").append(rptData.getFileType())
							.append("',");
					insertLogValues.append("'").append(rptData.getFileType())
							.append("',");
					// date
					Date date = new Date();
					if ("oracle".equalsIgnoreCase(this.getDbType())) {
						String strDate = DateUtils.toString(date,
								DateUtils.ORA_DATES_FORMAT);
						insertValues.append("to_date('" + strDate
								+ "','yyyy-mm-dd'),");
					} else if ("db2".equalsIgnoreCase(this.getDbType())) {
						String strDate = DateUtils.toString(date,
								DateUtils.ORA_DATE_TIMES_FORMAT);
						insertValues.append("timestamp('" + strDate + "'),");
					}
					insertLogValues.append("'").append(
							DateUtils.serverCurrentTimeStamp()).append("',");
					// order 5 user
					insertValues.append("'").append(currentUser.getId())
							.append("',");
					insertLogValues.append("'").append(currentUser.getId())
							.append("',");
					short nCellData = 0;
					for (Iterator it = rptColumnList.iterator(); it.hasNext();) {
						RptColumnInfo column = (RptColumnInfo) it.next();
						if (column.getColumnId().equals("OPEN_BANK_ID")
								|| column.getColumnId().equals(
										"CUSTODIAN_BANK_ID")
								|| column.getColumnId().equals("CUSTODIAN_ID")) {
							insertValues.append("'").append(rptData.getRptNo())
									.append("',");
							if (StringUtils.isNotEmpty(column.getLogColumnId()))
								insertLogValues.append("'").append(
										rptData.getRptNo()).append("',");
							continue;
						}
						// 检查该列是否需要读取值
						while (rptTemplateService.isBlankColumn(saveTableId,
								nCellData)) {
							nCellData++;
						}
						cell = row.getCell(nCellData);
						value = ExcelUtil.getStringCellValue(cell, null);
						// 过滤字典项，如果字典项为空，则跳过比对，仅验证该空格是否可以为空
						if ("3".equals(column.getTagType())
								&& !StringUtils.isEmpty(value)) {
							if (dictionaryMap != null) {
								Map tableMap = (HashMap) dictionaryMap
										.get(rptData.getTableId());
								if (tableMap != null) {
									List codeDictionaryList = (ArrayList) tableMap
											.get(column.getDictionaryTypeId());
									if (codeDictionaryList != null) {
										value = getKeyFromDict(
												codeDictionaryList, value);
										if (StringUtils.isEmpty(value)) {
											isOK = false;
											errorList.add(combinationInfo(
													saveTableId, j + 1,
													nCellData + 1,
													"未找到对应的字典项数据!"));
											break;
										}
									}
								}
							}
						}
						String info = validatePass(column, value);
						if (!StringUtils.isEmpty(info)) {
							isOK = false;
							errorList.add(combinationInfo(saveTableId, j + 1,
									nCellData + 1, info));
							break;
						}
						// 拼接sql
						if ("n".equalsIgnoreCase(column.getDataType()
								.substring(0, 1))) {
							if (StringUtils.isEmpty(value)) {
								insertValues.append("null,");
							} else {
								value = value.replaceAll(",", "");
								insertValues.append(value).append(",");
							}
						} else {
							if (StringUtils.isEmpty(value)) {
								insertValues.append("null,");
							} else {
								insertValues.append("'").append(
										value.replaceAll("'", "''")).append(
										"',");
							}
						}
						if (StringUtils.isNotEmpty(column.getLogColumnId()))
							insertLogValues.append("'").append(
									value.replaceAll("'", "''")).append("',");
						nCellData++;
						// 外管局的模板真个性，币种后面隐藏一个币种名称列
						if ("CURRENCY".equals(column.getDictionaryTypeId())) {
							nCellData++;
						}
					}// for end 单条记录
					// 执行更新
					if (isOK) {
						Map param = new HashMap();
						param.put("tableId", rptData.getTableId());
						param.put("insertColumns", strColumn);
						param.put("insertValues", StringUtils.removeEnd(
								insertValues.toString(), ","));
						insertList.add(param);
						Map paramLog = new HashMap();
						paramLog.put("tableId", "t_rpt_log_info");
						paramLog.put("insertColumns", strLogColumn);
						paramLog.put("insertValues", StringUtils.removeEnd(
								insertLogValues.toString(), ","));
						insertLogList.add(paramLog);
						if (insertList.size() > batchSize) {
							dataDealService.saveRptDataBatch(insertList);
							dataDealService.saveRptDataBatch(insertLogList);
							insertList.clear();
							insertLogList.clear();
						}
					}
				}// end for row
				if (insertList.size() != 0) {
					dataDealService.saveRptDataBatch(insertList);
					dataDealService.saveRptDataBatch(insertLogList);
					insertList.clear();
					insertLogList.clear();
				}
			}// end if sheet
		}// end for sheet
		return errorList;
	}

	private String validatePass(RptColumnInfo column, String value) {
		try {
			String dataType = column.getDataType();
			if (dataType != null && dataType.toLowerCase().startsWith("n")) {
				if (value != null && value.startsWith(".")) {
					value = "0" + value;
				}
				if (value != null && value.indexOf(",") > 0) {
					value = value.replaceAll(",", "");
				}
			}
			// 数据属性校验结果，并设置结果
			StringBuffer info = new StringBuffer();
			if (!DataValidater.validateDataType(value, dataType, info, true,
					column))
				return info.toString();
			else
				return "";
		} catch (Exception e) {
			log.error(e);
		}
		return "校验数据时出现异常!";
	}

	private String combinationInfo(int row, int column, String errorInfo) {
		return new StringBuffer("第").append(row).append("行记录未能正确导入,在单元格:[第")
				.append(row).append("行,第").append(column).append("列]处出现错误,原因:")
				.append(errorInfo).toString();
	}

	private String combinationInfo(String sheetName, int row, int column,
			String errorInfo) {
		return new StringBuffer("在工作页[").append(sheetName).append("]中第")
				.append(row).append("行记录未能正确导入,在单元格:[第").append(row).append(
						"行,第").append(column).append("列]处出现错误,原因:").append(
						errorInfo).toString();
	}

	private String getKeyFromDict(List codeDictionaryList, String key) {
		for (Iterator it = codeDictionaryList.iterator(); it.hasNext();) {
			Dictionary dict = (Dictionary) it.next();
			if (dict.getValueStandardNum().equals(key)) {
				return dict.getValueStandardNum();
			}
		}
		return "";
	}

	private List getInstIdList(List authInstList) {
		List result = new ArrayList();
		for (Iterator it = authInstList.iterator(); it.hasNext();) {
			Organization org = (Organization) it.next();
			result.add(org.getId());
		}
		return result;
	}

	private List getInsertOrder(List rptColumnList, HSSFRow row)
			throws Exception {
		List orderList = new ArrayList();
		for (int k = 4; k < row.getLastCellNum() - 1; k++) {
			HSSFCell cell = row.getCell((short) k);
			String value = cell.getRichStringCellValue().getString();
			RptColumnInfo column = getColumn(rptColumnList, value, true);
			if (column != null)
				orderList.add(column);
			else
				throw new Exception("未找到列[" + value + "]信息");
		}
		return orderList;
	}

	private RptColumnInfo getColumn(List rptColumnList, String value,
			boolean isName) {
		boolean isFind = false;
		RptColumnInfo column = null;
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			column = (RptColumnInfo) i.next();
			if ((isName && column.getColumnName().trim().equals(value))
					|| (!isName && column.getColumnId().trim().equals(value))) {
				isFind = true;
				break;
			}
		}
		if (isFind) {
			return column;
		} else
			return null;
	}

	private boolean isSpecialColumn(RptColumnInfo column, String fileType) {
		return column.getColumnId().equals("ACTIONTYPE")
				|| (!column.getColumnId().equals("BRANCHCODE") && column
						.getColumnId().equals(
								DataUtil.getRptNoColumnIdByFileType(fileType)));
	}

	public List getErrorList() {
		return errorList;
	}

	public void setErrorList(List errorList) {
		this.errorList = errorList;
	}

	public RptTemplateService getRptTemplateService() {
		return rptTemplateService;
	}

	public void setRptTemplateService(RptTemplateService rptTemplateService) {
		this.rptTemplateService = rptTemplateService;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
