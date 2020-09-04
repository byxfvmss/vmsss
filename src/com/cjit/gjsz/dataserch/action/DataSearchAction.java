package com.cjit.gjsz.dataserch.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.DictionaryUtil;
import com.cjit.common.util.ExcelHelper;
import com.cjit.common.util.ExcelUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.service.CommonService;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.gjsz.system.service.RptTemplateService;
import com.opensymphony.util.BeanUtils;

public class DataSearchAction extends BaseListAction {

	private static final long serialVersionUID = -2853191635963894791L;
	private DataDealService dataDealService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private OrganizationService organizationService;
	private CommonService commonService;
	private RptTemplateService rptTemplateService;
	private List recordList = new ArrayList();
	private List authInstList = new ArrayList(); // 受权机构列表
	private List rptTableList = new ArrayList(); // 表名list
	private List rptColumnList = new ArrayList(); // 列名list
	private List rptColumnListQuery = new ArrayList();
	private List busiDataInfoList = new ArrayList(); // 业务信息集

	public List getRptColumnListQuery() {
		return rptColumnListQuery;
	}

	public void setRptColumnListQuery(List rptColumnListQuery) {
		this.rptColumnListQuery = rptColumnListQuery;
	}

	private String infoTypeCode; // 基础信息 申报信息 核销信息
	private String tableSelectId; // 选中的table
	private String tableUniqueSelectId;
	private String instCode; // 机构code
	private String orderColumn;// 排序字段
	private String orderDirection;// 排序方式
	private String orderColumnSub;// 排序字段 子表
	private String orderDirectionSub;// 排序方式 子表
	private QueryCondition queryCondition; // 查询条件
	private String queryConditionResult; // 查询条件的结果
	private int canExcel = 0; // 控制导出的button 1显示 0 disable
	// 业务主键
	private String businessId;
	private int largestColumnNum;// 最长行
	private String dataStatus = String.valueOf(DataUtil.SHYTG_STATUS_NUM);// 数据状态
	private List dataStatusList;// 数据状态的List
	private String beginDate;// 审核起止时间
	private String endDate;
	private String tableStr;// excel导出使用
	public static final int TYPE_MORE = 1; // 大于
	public static final int TYPE_LESS = 2; // 小于
	public static final int TYPE_EQUAL = 3; // 等于
	public static final int TYPE_LIKE = 4; // like
	public static final int TYPE_MORE_EQUAL = 5;// 大于等于
	public static final int TYPE_LESS_EQUAL = 6;// 小于等于
	private static Map operatorsMap;
	static {
		operatorsMap = new LinkedMap();
		operatorsMap.put(new Integer(TYPE_LIKE), "like");
		operatorsMap.put(new Integer(TYPE_EQUAL), "=");
		operatorsMap.put(new Integer(TYPE_MORE), ">");
		operatorsMap.put(new Integer(TYPE_LESS), "<");
		operatorsMap.put(new Integer(TYPE_MORE_EQUAL), ">=");
		operatorsMap.put(new Integer(TYPE_LESS_EQUAL), "<=");
	}

	/**
	 * 初始化综合查询
	 * 
	 * @return
	 */
	public String initSearch() {
		try {
			// 获取配置参数
			initConfigParameters();
			if (this.configMap != null) {
				this.request.setAttribute("interfaceVer",
						(String) this.configMap.get("config.interface.ver"));
				this.request.setAttribute("overleapAudit",
						(String) this.configMap.get("config.overleap.audit"));
			}
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			// 获取有权限的机构
			getAuthInstList(authInstList);
			// 返回基础信息的单据列表
			loadTableNameByType(userId);
			// 状态列表
			dataStatusList = commonService.getAllStatus();
			//
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			// 
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			if (rptTableList != null && rptTableList.size() > 0) {
				if (!containsTable(rptTableList, this.tableUniqueSelectId)) {
					this.setTableUniqueSelectId(((RptTableInfo) rptTableList
							.get(0)).getUniqueTable());
				}
			} else {
				RptTableInfo rti = new RptTableInfo();
				rti.setTableId(DataUtil.getTableIdByFileType(this.infoType));
				rti.setFileType("ZZ");
				rti.setTableName("未授权");
				rptTableList.add(rti);
				this.fileType = "ZZ";
			}
			if (!StringUtils.isEmpty(getTableUniqueSelectId())) {
				this.tableSelectId = this.getTableUniqueSelectId().split(
						DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
				this.fileType = this.getTableUniqueSelectId().split(
						DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
			}
			// load列名列表
			if (rptTableList.size() > 0) {
				RptTableInfo rt = (RptTableInfo) rptTableList.get(0);
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(rt.getTableId(),
								null, "1", this.fileType));
				this.addCommonColumnListQuery();
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 数据查询
	 * 
	 * @return
	 */
	public String dataQuery() {
		try {
			if (!initSession()) {
				// weblogic8.1
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request.setAttribute("recordList", this.recordList);
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				return SUCCESS;
			}
			if (this.configMap != null) {
				this.request.setAttribute("interfaceVer",
						(String) this.configMap.get("config.interface.ver"));
				this.request.setAttribute("overleapAudit",
						(String) this.configMap.get("config.overleap.audit"));
			}
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			// 获取有权限的机构
			getAuthInstList(authInstList);
			// 返回基础信息的单据列表
			loadTableNameByType(userId);
			// 状态列表
			dataStatusList = commonService.getAllStatus();
			//
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			// 
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			if (rptTableList != null && rptTableList.size() > 0) {
				if (!containsTable(rptTableList, this.tableUniqueSelectId)) {
					this.setTableUniqueSelectId(((RptTableInfo) rptTableList
							.get(0)).getUniqueTable());
				}
			} else {
				RptTableInfo rti = new RptTableInfo();
				rti.setTableId(DataUtil.getTableIdByFileType(this.infoType));
				rti.setFileType("ZZ");
				rti.setTableName("未授权");
				rptTableList.add(rti);
				this.fileType = "ZZ";
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				request.getSession().setAttribute("orderColumnSub",
						this.orderColumnSub);
				request.getSession().setAttribute("orderDirectionSub",
						this.orderDirectionSub);
				this.request.setAttribute("recordList", this.recordList);
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				return SUCCESS;
			}
			if (!StringUtils.isEmpty(getTableUniqueSelectId())) {
				this.tableSelectId = this.getTableUniqueSelectId().split(
						DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
				this.fileType = this.getTableUniqueSelectId().split(
						DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
			}
			// load列名列表
			if (rptTableList.size() > 0) {
				rptColumnList = dataDealService
						.findRptColumnInfo(new RptColumnInfo(tableSelectId,
								null, "1", this.fileType));
				this.addCommonColumnListQuery();
			}
			if (!check()) {
				// weblogic8.1
				this.request.setAttribute("rptColumnList", this.rptColumnList);
				this.request.setAttribute("orderColumn", this.orderColumn);
				this.request
						.setAttribute("orderDirection", this.orderDirection);
				this.request.setAttribute("recordList", this.recordList);
				this.request.setAttribute("infoTypeCode", this.infoTypeCode);
				return ERROR;
			}
			this.queryConditionResult = createSqlCondition(queryCondition,
					beginDate, endDate, userId);
			String dbDataStatus = "datastatus";// 数据库中用于记录数据状态的字段名称
			String columns = this.getColumnsSql(this.getTableUniqueSelectId());
			String dataStatusCondition = null;
			if (dataStatus == null || "".equals(dataStatus)) {
				// dataStatusCondition = " t.datastatus in(1,2,3,4,5,6) ";
				dataStatusCondition = " t." + dbDataStatus + " in ("
						+ DataUtil.WJY_STATUS_NUM + ","
						+ DataUtil.JYWTG_STATUS_NUM + ","
						+ DataUtil.JYYTG_STATUS_NUM + ","
						+ DataUtil.YTJDSH_STATUS_NUM + ","
						+ DataUtil.SHWTG_STATUS_NUM + ","
						+ DataUtil.SHYTG_STATUS_NUM + ","
						+ DataUtil.YBS_STATUS_NUM + ","
						+ DataUtil.YSC_STATUS_NUM + ","
						+ DataUtil.LOCKED_STATUS_NUM + ") ";
			} else {// 特定状态
				dataStatusCondition = " t." + dbDataStatus + " in ("
						+ dataStatus + ")";
			}
			/**
			 * 修正分页信息无排序 pagefalg为执行分页查询标识 将orderColumn、orderDirection
			 * 缓存到session中。
			 */
			String pagefalg = (String) request.getParameter("pagefalg");
			if (pagefalg == null) {
				request.getSession().removeAttribute("orderColumn");
				request.getSession().removeAttribute("orderDirection");
			}
			if (DataUtil.isJCDWSBHX(infoTypeCode)) {
				if (orderColumn == null && orderDirection == null) {
					orderColumn = (String) request.getSession().getAttribute(
							"orderColumn");
					orderDirection = (String) request.getSession()
							.getAttribute("orderDirection");
				} else {
					request.getSession().setAttribute("orderColumn",
							orderColumn);
					request.getSession().setAttribute("orderDirection",
							orderDirection);
				}
				if (orderColumn != null
						&& orderColumn.indexOf("datastatus") < 0
						&& columns != null && columns.indexOf(orderColumn) < 0) {
					orderColumn = null;
					orderDirection = null;
					request.getSession().setAttribute("orderColumn", null);
					request.getSession().setAttribute("orderDirection", null);
				}
				orderColumnSub = null;
				orderDirectionSub = null;
			} else {
				if (orderColumnSub == null && orderDirectionSub == null) {
					orderColumnSub = (String) request.getSession()
							.getAttribute("orderColumnSub");
					orderDirectionSub = (String) request.getSession()
							.getAttribute("orderDirectionSub");
				} else {
					request.getSession().setAttribute("orderColumnSub",
							orderColumnSub);
					request.getSession().setAttribute("orderDirectionSub",
							orderDirectionSub);
				}
			}
			RptData tempData = new RptData(tableSelectId, columns, instCode,
					dataStatusCondition, null, orderColumn, orderDirection,
					queryConditionResult);
			if ("on".equals(searchLowerOrg)) {
				tempData.setSearchLowerOrg(searchLowerOrg);
				tempData.setUserId(userId);
			}
			if ("yes".equalsIgnoreCase(super.linkBussType)) {
				tempData.setLinkBussType(super.linkBussType);
				tempData.setUserId(userId);
			}
			tempData.setFileType(this.fileType);
			recordList = dataDealService.findRptData(tempData, paginationList);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(recordList, tableSelectId, rptColumnList);
			if (this.fileType.equals("1")) {
				canExcel = 2;
			} else {
				canExcel = 1;
			}
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			// weblogic8.1
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("recordList", this.recordList);
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			return ERROR;
		}
	}

	public boolean containsTable(List tableList, String uniqueTable) {
		for (Iterator it = tableList.iterator(); it.hasNext();) {
			if (((RptTableInfo) it.next()).getUniqueTable().equals(uniqueTable))
				return true;
		}
		return false;
	}

	/**
	 * 获得查询字符串的对应别名列
	 * 
	 * @param rptColumnList
	 * @return
	 */
	private String getQueryColumn(List rptColumnList) {
		// 组合导出单据查询列SQL语句
		int cFlag = 0;
		StringBuffer columns = new StringBuffer();
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if ("table".equals(column.getDataType())) {
				continue;
			}
			// 赋别名c1,c2,c3
			column.setAliasColumnId("c" + (++cFlag));
			// 根据字段物理名和别名拼查询SQL
			columns.append("t.").append(column.getColumnId()).append(" as ")
					.append(column.getAliasColumnId()).append(",");
		}
		while (cFlag < largestColumnNum) {
			columns.append("'' as c").append(++cFlag).append(",");
		}
		return StringUtils.removeEnd(columns.toString(), ",");
	}

	/**
	 * @param rptData
	 *            need:tableId,fileType
	 * @throws Exception
	 */
	private void insertDataToSheet(HSSFSheet sheet, String saveTableId,
			ExcelHelper exHelp) {
		log.info("开始向[" + saveTableId + "]页中插入数据");
		RptTableInfo tableInfo = dataDealService
				.findRptTableInfoBySaveTableId(saveTableId);
		int beginRow = rptTemplateService.getBeginDataRow(saveTableId);
		// int endColumn=sheet.getRow(beginRow).getLastCellNum();
		this.tableSelectId = tableInfo.getTableId();
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableSelectId, null, "1",
						null));
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
		Map tableMap = null;
		if (dictionaryMap != null)
			tableMap = (HashMap) dictionaryMap.get(tableSelectId);
		log.info("组合查询条件!");
		String columns = getQueryColumn(rptColumnList);
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String queryConditionResult_ = createSqlCondition(queryCondition,
				beginDate, endDate, currentUser.getId());
		String dataStatusCondition_ = null;
		if (StringUtils.isNotEmpty(dataStatus)) {
			dataStatusCondition_ = " t.datastatus  in(" + dataStatus + ")";
		}
		log.info("查询数据");
		List recordList = null;
		RptData tempData = new RptData(tableSelectId, columns, instCode,
				dataStatusCondition_, null, orderColumn, orderDirection,
				queryConditionResult_);
		Long recordCount = dataDealService.findRptDataCount(tempData);
		if (recordCount != null && recordCount.longValue() > 10000) {
			this.request.setAttribute("message", "more10000");
			dataQuery();
			return;
		}
		recordList = dataDealService.findRptData(tempData);
		log.info("写开户银行代码,托管行代码,托管人代码:");
		HSSFRow row = sheet.getRow(1);
		HSSFCell cell = row.getCell(0);
		String value = ExcelUtil.getStringCellValue(cell, null);
		String code = getOrganizationService().findRptNo(instCode);
		value = StringUtils.replace(value, "：", ":");
		value = value.split(":")[0] + ":" + code;// 获取冒号以前的字段名例如:
		// 开户银行代码:110000000101
		cell.setCellValue(new HSSFRichTextString(value));
		if (!saveTableId.equals("STOCKACCM")
				&& !saveTableId.equals("STOCKACCOPENCLOSEM")) {
			log.info("托管行名称,托管人名称");
			row = sheet.getRow(2);
			cell = row.getCell(0);
			value = ExcelUtil.getStringCellValue(cell, null);
			String branch_name = organizationService
					.getOrgNameByInstCode(instCode);
			value = StringUtils.replace(value, "：", ":");
			value = value.split(":")[0] + ":" + branch_name;// 获取冒号以前的字段名例如:
			// 开户银行代码:110000000101
			cell.setCellValue(new HSSFRichTextString(value));
		}
		log.info("填写填报日期");
		row = sheet.getRow(sheet.getLastRowNum());
		cell = row.getCell(0);
		cell.setCellValue(new HSSFRichTextString("填报日期:"
				+ DateUtils.toString(new Date(), "yyyy-MM-dd")));
		if (recordList != null && recordList.size() > 1) {
			log.info("移动尾巴到数据行数以下");
			sheet.shiftRows(beginRow + 1, sheet.getLastRowNum(), recordList
					.size() - 1, true, false);
		}
		log.info("插入数据");
		HSSFCellStyle csData = exHelp.getSafeDataCS(); // 样式：文本型
		HSSFCellStyle csBottemData = exHelp.getSafeBottemDataCS(); // 样式：底下
		// 从第二行开始创建所有导出报文数据信息
		if (recordList != null) {
			for (int i = 0; i < recordList.size(); i++) {
				row = sheet.createRow(i + beginRow);
				row.setHeight((short) 500);
				RptData data = (RptData) recordList.get(i);
				short nCellData = 0;
				for (Iterator it = rptColumnList.iterator(); it.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) it.next();
					// 检查该列是否需要写入值
					while (rptTemplateService.isBlankColumn(saveTableId,
							nCellData)) {
						exHelp.setCell(row, nCellData++, csData, "");
					}
					// 填入实际值
					if ("4".equals(column.getTagType())
							|| isNotExportColumnSafe(column)) {
						continue;
					}
					String aliasColumnId = column.getAliasColumnId();
					String strData = (String) (BeanUtils.getValue(data,
							aliasColumnId) == null ? "" : BeanUtils.getValue(
							data, aliasColumnId));
					exHelp.setCell(row, nCellData++, csData, strData);
					// 模板上面的特殊处理都放在这里
					// 外管局的模板真个性，币种后面隐藏一个币种名称列
					if ("CURRENCY".equals(column.getDictionaryTypeId())) {
						if (tableMap != null) {
							List codeDictionaryList = (ArrayList) tableMap
									.get(column.getDictionaryTypeId());
							if (codeDictionaryList != null) {
								String dicValue = getValueFromDict(
										codeDictionaryList, strData);
								if (!StringUtils.isEmpty(dicValue)) {
									strData = dicValue;
								}
							}
						}
						exHelp.setCell(row, nCellData++, csData, strData);
					}
				}
			}
			// 重绘样式
			if (sheet.getLastRowNum() == row.getRowNum() + 1) {// 如果是最后一行，使用这个样式
				for (int i = 0; i < row.getLastCellNum(); i++) {
					row.getCell(i).setCellStyle(csBottemData);
				}
			}
		}
	}

	private String getValueFromDict(List codeDictionaryList, String key) {
		for (Iterator it = codeDictionaryList.iterator(); it.hasNext();) {
			Dictionary dict = (Dictionary) it.next();
			if (dict.getValueStandardNum().equals(key)) {
				return dict.getName();
			}
		}
		return "";
	}

	public String agencyToExcel() throws Exception, IOException {
		if (this.configMap != null) {
			this.request.setAttribute("interfaceVer", (String) this.configMap
					.get("config.interface.ver"));
		}
		// 获取导出单据列信息
		this.tableSelectId = this.getTableUniqueSelectId().split(
				DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
		this.fileType = this.getTableUniqueSelectId().split(
				DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
		RptTableInfo tableInfo = dataDealService.findRptTableInfoById(
				this.tableSelectId, this.fileType);
		OutputStream out = response.getOutputStream();
		ExcelHelper exHelp = null;
		try {
			String safeTableId = tableInfo.getSafeTableId();
			Map fileMap = rptTemplateService.findRptTemplate(null, safeTableId);
			if (fileMap != null) {
				String fileName = getSafeExcelFileName((String) fileMap
						.get("TEMPLATE_ID"));
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
				response
						.setContentType("application/vnd.ms-excel;charset=UTF-8");
				response.setHeader("Content-disposition",
						"attachment; filename=" + fileName + ".xls");
				log.info("从数据库中获取Excel模板");
				oracle.sql.BLOB blob = (oracle.sql.BLOB) fileMap
						.get("TEMPLATE_CONTENT");
				InputStream is = blob.getBinaryStream();
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				exHelp = new ExcelHelper(workbook);
				for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
					insertDataToSheet(workbook.getSheetAt(sheetNum), workbook
							.getSheetName(sheetNum), exHelp);
				}
				workbook.write(out);
				out.flush();
			}
		} catch (Exception e) {
			throw new Exception("写文件输出流时出现错误....");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	private String getSafeExcelFileName(String templateId) {
		String format = "yyyyMMdd";
		if (templateId.endsWith("Y")) {
			format = "yyyy";
		} else if (templateId.endsWith("M")) {
			format = "yyyyMM";
		} else if (templateId.endsWith("D")) {
			format = "yyyyMMdd";
		}
		String code = getOrganizationService().findRptNo(instCode);
		return DateUtils.toString(new Date(), format) + "-" + code;
	}

	public String exportToExcel() throws Exception, IOException {
		if (this.configMap != null) {
			this.request.setAttribute("interfaceVer", (String) this.configMap
					.get("config.interface.ver"));
		}
		// 获取有权限的机构
		getAuthInstList(authInstList);
		// 获取导出单据列信息
		this.tableSelectId = this.getTableUniqueSelectId().split(
				DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
		this.fileType = this.getTableUniqueSelectId().split(
				DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
		RptTableInfo tableInfo = dataDealService.findRptTableInfoById(
				this.tableSelectId, this.fileType);
		List rptColumnList = dataDealService
				.findRptColumnInfo(new RptColumnInfo(tableSelectId, null, "1",
						this.fileType));
		String columns = getQueryColumn(rptColumnList);
		// 构造查询条件
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String queryConditionResult_ = createSqlCondition(queryCondition,
				beginDate, endDate, currentUser.getId());
		String dataStatusCondition_ = null;
		if (dataStatus == null || "".equals(dataStatus)) {
			// dataStatusCondition = " t.datastatus in(1,2,3,4,5,6) ";
			dataStatusCondition_ = " t.datastatus in ("
					+ DataUtil.WJY_STATUS_NUM + "," + DataUtil.JYWTG_STATUS_NUM
					+ "," + DataUtil.JYYTG_STATUS_NUM + ","
					+ DataUtil.YTJDSH_STATUS_NUM + ","
					+ DataUtil.SHWTG_STATUS_NUM + ","
					+ DataUtil.SHYTG_STATUS_NUM + "," + DataUtil.YBS_STATUS_NUM
					+ "," + DataUtil.LOCKED_STATUS_NUM + ","
					+ DataUtil.YSC_STATUS_NUM + ") ";
		} else {
			// 特定状态
			dataStatusCondition_ = " t.datastatus  in(" + dataStatus + ")";
		}
		String orderColumn = this.request.getParameter("exportOrderColumn");
		String orderDirection = this.request
				.getParameter("exportOrderDirection");
		// 查询数据
		List recordList = null;
		// if(DataUtil.isJCDWSBHX(infoTypeCode)){
		RptData tempData = new RptData(tableSelectId, columns, instCode,
				dataStatusCondition_, null, orderColumn, orderDirection,
				queryConditionResult_);
		if (this.fileType != null && !this.fileType.equals("1")) {
			tempData.setFileType(this.fileType);
		}
		Long recordCount = dataDealService.findRptDataCount(tempData);
		if (recordCount != null && recordCount.longValue() > 10000) {
			this.request.setAttribute("message", "more10000");
			dataQuery();
			return ERROR;
		} else {
			recordList = dataDealService.findRptData(tempData);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValueSimple(recordList);
			OutputStream out = response.getOutputStream();
			String fileName = tableInfo.getTableName();
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName + ".xls");
			HSSFWorkbook wb = null;
			HSSFSheet sheetMain = null;// 主表页
			ExcelHelper exHelp = null;
			try {
				wb = new HSSFWorkbook();
				exHelp = new ExcelHelper(wb);
				// 构造第一个页签中报文主表信息 Begin
				sheetMain = wb.createSheet(tableInfo.getUniqueTable());
				// 样式准备
				HSSFCellStyle csTitle = exHelp.getTitleCS(); // 样式：标题
				HSSFCellStyle csTitleY = exHelp // 样式：标题黄色
						.getTitleCS(HSSFColor.YELLOW.index);
				HSSFCellStyle csTitleB = exHelp // 样式：标题蓝色
						.getTitleCS(HSSFColor.BLUE.index);
				HSSFCellStyle csData = exHelp.getDataCS(); // 样式：文本型
				HSSFDataFormat df = exHelp.getDataFormatText();
				csData.setDataFormat(df.getFormat("@"));// 文本型
				HSSFCellStyle csDataNumeric = exHelp.getDataCS(); // 样式：数值型
				HSSFFont fontRed = exHelp.getFont(HSSFColor.RED.index);
				HSSFFont fontGreen = exHelp.getFont(HSSFColor.GREEN.index);
				HSSFFont fontBlue = exHelp.getFont(HSSFColor.BLUE.index);
				HSSFCellStyle csColorDataRed = exHelp.getDataCS(); // 样式：红色字体-校验用
				csColorDataRed.setFont(fontRed);
				HSSFCellStyle csColorDataGreen = exHelp.getDataCS(); // 样式：绿色字体-校验用
				csColorDataGreen.setFont(fontGreen);
				HSSFCellStyle csColorDataBlue = exHelp.getDataCS(); // 样式：蓝色字体-校验用
				csColorDataBlue.setFont(fontBlue);
				short nCellHeader = 0;
				// 表头 title
				// 创建主表页Excel第一行
				HSSFRow row = sheetMain.createRow(0);
				// 创建Cell-业务ID
				exHelp.setCell(row, nCellHeader, csTitleB, "业务ID");
				sheetMain.setColumnWidth(nCellHeader++, (short) 6000);
				// 创建Cell-机构ID
				exHelp.setCell(row, nCellHeader, csTitleB, "机构ID");
				sheetMain.setColumnWidth(nCellHeader++, (short) 2500);
				// 创建Cell-机构名称
				exHelp.setCell(row, nCellHeader, csTitle, "机构名称");
				sheetMain.setColumnWidth(nCellHeader++, (short) 4000);
				// 创建Cell-数据状态
				exHelp.setCell(row, nCellHeader, csTitle, "数据状态");
				sheetMain.setColumnWidth(nCellHeader++, (short) 4000);
				for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) i.next();
					if ("4".equals(column.getTagType())) {
						continue;
					}
					if (isNotExportColumn(column, this.fileType))
						exHelp.setCell(row, nCellHeader, csTitle, column
								.getColumnName());
					else
						exHelp.setCell(row, nCellHeader, csTitleY, column
								.getColumnName());
					sheetMain.setColumnWidth(nCellHeader++, (short) 4000);
				}
				exHelp.setCell(row, nCellHeader, csTitle, "审核时间");
				sheetMain.setColumnWidth(nCellHeader++, (short) 3000);
				// title end
				// 从第二行开始创建所有导出报文数据信息
				if (recordList != null)
					for (int i = 0; i < recordList.size(); i++) {
						row = sheetMain.createRow(i + 1);
						row.setHeight((short) 550);
						RptData data = (RptData) recordList.get(i);
						short nCellData = 0;
						// 创建Cell-业务ID
						exHelp.setCell(row, nCellData++, csData, data
								.getBusinessId());
						// 创建Cell-机构ID
						exHelp.setCell(row, nCellData++, csData, data
								.getInstCode());
						// 机构名称
						exHelp.setCell(row, nCellData++, csData, data
								.getInstName());
						// 数据状态
						String dataStatus = (String) DataUtil
								.getDataStatusChinese((String) BeanUtils
										.getValue(data, "dataStatus"));
						if (DataUtil.JYWTG_STATUS_CH.equals(dataStatus)
								|| DataUtil.SHWTG_STATUS_CH.equals(dataStatus)) {
							// RED
							exHelp.setCell(row, nCellData++, csColorDataRed,
									dataStatus);
						} else if (DataUtil.JYYTG_STATUS_CH.equals(dataStatus)
								|| DataUtil.SHYTG_STATUS_CH.equals(dataStatus)
								|| DataUtil.YBS_STATUS_CH.equals(dataStatus)
								|| DataUtil.YSC_STATUS_CH.equals(dataStatus)) {
							// GREEN
							exHelp.setCell(row, nCellData++, csColorDataGreen,
									dataStatus);
						} else {
							// BLUE
							exHelp.setCell(row, nCellData++, csColorDataBlue,
									dataStatus);
						}
						// 循环写入列值
						for (int j = 0; j < rptColumnList.size(); j++) {
							RptColumnInfo column = (RptColumnInfo) rptColumnList
									.get(j);
							String aliasColumnId = column.getAliasColumnId();
							if ("4".equals(column.getTagType())) {
								continue;
							}
							String strData = (String) (BeanUtils.getValue(data,
									aliasColumnId) == null ? "" : BeanUtils
									.getValue(data, aliasColumnId));
							if ("n".equalsIgnoreCase(column.getDataType()
									.substring(0, 1))) {
								if (strData.startsWith(".")) {
									strData = "0" + strData;
								} else if (strData.startsWith("-.")) {
									strData = strData.replaceFirst("-.", "-0.");
								}
								exHelp.setCell(row, nCellData++, csDataNumeric,
										strData);
							} else
								exHelp.setCell(row, nCellData++, csData,
										strData);
						}
						// 审核时间
						exHelp.setCell(row, nCellData++, csData, data
								.getAuditDateStr());
					}
				// 在sheet中输出字典项目
				sheetMain = wb.createSheet("Dictionary");
				List dictList = dataDealService.findCodeDictionaryList(null,
						tableInfo.getTableId());
				List dictTypeList = new ArrayList();
				for (Iterator it = dictList.iterator(); it.hasNext();) {
					CodeDictionary dict = (CodeDictionary) it.next();
					if (!dict.getCodeType().equals("CURRENCECODE")
							&& !dictTypeList.contains(dict.getCodeTypeDesc())) {
						dictTypeList.add(dict.getCodeTypeDesc());
					}
				}
				int rowNum = 0;
				row = sheetMain.createRow(rowNum++);
				sheetMain.setColumnWidth((short) 0, (short) 30000);
				exHelp.setCell(row, (short) 0, csTitle, "填报说明(仅填写代码):");
				for (Iterator it = dictTypeList.iterator(); it.hasNext();) {
					// 写标题
					String typeDesc = (String) it.next();
					row = sheetMain.createRow(rowNum++);
					exHelp.setCell(row, (short) 0, csTitleY, typeDesc);
					for (Iterator itt = dictList.iterator(); itt.hasNext();) {
						CodeDictionary dict = (CodeDictionary) itt.next();
						if (dict.getCodeTypeDesc().equals(typeDesc)) {
							row = sheetMain.createRow(rowNum++);
							if (isNotCombineDictionary(dict))
								exHelp.setCell(row, (short) 0, csData, dict
										.getCodeValueStandardNum()
										+ "-" + dict.getCodeName());
							else
								exHelp.setCell(row, (short) 0, csData, dict
										.getCodeName());
						}
					}
				}
				wb.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("写文件输出流时出现错误....");
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return SUCCESS;
	}

	private boolean isNotCombineDictionary(CodeDictionary dict) {
		return dict.getCodeType().equals("CLOSEDTYPE")
				|| dict.getCodeType().equals("ACCOUNTASSETSIN")
				|| dict.getCodeType().equals("QDIIACCOUNTTYPE")
				|| dict.getCodeType().equals("ASSETSREMITREASON")
				|| dict.getCodeType().equals("QFIIACCOUNTTYPE")
				|| dict.getCodeType().equals("ASSETSTYPE")
				|| dict.getCodeType().equals("CLOSEDASSETSDEAL");
	}

	private boolean isNotExportColumn(RptColumnInfo column, String fileType) {
		return column.getColumnId().equals("ACTIONTYPE")
				|| (!column.getColumnId().equals("BRANCHCODE") && column
						.getColumnId().equals(
								DataUtil.getRptNoColumnIdByFileType(fileType)));
	}

	private boolean isNotExportColumnSafe(RptColumnInfo column) {
		// String talbeId=column.getTableId();
		// String columnId=column.getColumnId();
		// if(talbeId.equals("T_CFA_BESTIR_ACCOUNT_CLOESD"))
		// {
		// }
		String columnID = column.getColumnId();
		return columnID.equals("OPEN_BANK_ID")
				|| columnID.equals("CUSTODIAN_BANK_ID")
				|| columnID.equals("CUSTODIAN_ID");
	}

	/**
	 * 获取列类型
	 * 
	 * @param newcolumnInfo
	 * @return
	 */
	public String getColumnType(ColumnInfo newcolumnInfo) {
		String returnType = TYPE_STRING;
		if (newcolumnInfo != null) {
			String type = newcolumnInfo.getDataType();
			if ((type != null) && (type.length() > 0)) {
				String[] tmpType = type.split(",");
				if (tmpType[0].equals("n")) {
					returnType = TYPE_NUM;
				} else if (tmpType[0].equals("s")) {
					returnType = TYPE_STRING;
				} else if (tmpType[0].equals("d")) {
					returnType = TYPE_DATE;
				}
			}
		}
		return returnType;
	}

	public ColumnInfo getColumnInfo(String tableId, String columnId) {
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setColumnId(columnId);
		columnInfo.setTableId(tableId);
		return userInterfaceConfigService.getColumnInfo(columnInfo);
	}

	public String getColumnType(String tableId, String columnId) {
		if ("BUSINESSNO".equals(columnId)) {
			return TYPE_STRING;
		}
		ColumnInfo ci = getColumnInfo(tableId, columnId);
		return getColumnType(ci);
	}

	/**
	 * 验证单个查询条件是否合法
	 * 
	 * @param columnId
	 * @param op
	 * @return
	 */
	private boolean checkOneValue(String columnId, int op, String beginError) {
		boolean flag = true;
		String error = "";
		ColumnInfo ciF = getColumnInfo(this.tableSelectId, columnId);
		String type = getColumnType(ciF);
		if (type.equals(TYPE_NUM)) {
			if (op == TYPE_LIKE) {
				error = error + beginError + "\"" + ciF.getColumnName() + "\""
						+ "为数字型,不可以选择"
						+ operatorsMap.get(new Integer(TYPE_LIKE)) + ";";
				flag = false;
			}
		} else if (type.equals(TYPE_STRING)) {
			// if ((op == TYPE_MORE) || (op == TYPE_LESS) || (op ==
			// TYPE_MORE_EQUAL)
			// || (op == TYPE_LESS_EQUAL)) {
			// error = error+beginError+"\""+ ciF.getColumnName()+"\"" +
			// "为文本型,不可以选择"
			// + operatorsMap.get(new Integer(op)) + ";";
			// flag = false;
			// }
		} else if (type.equals(TYPE_DATE)) {
		}
		if (!(error.equals(""))) {
			this.addActionMessage(error);
		}
		return flag;
	}

	/**
	 * 验证三条查询条件是否合法
	 */
	private boolean check() {
		boolean flag = true;
		boolean flag1 = true;
		boolean flag2 = true;
		boolean flag3 = true;
		if (queryCondition != null) {
			if (!StringUtil.isEmpty(queryCondition.getValueFirst())) {
				if (StringUtil.checkStr(queryCondition.getValueFirst())) {
					this.addActionMessage("查询条件一含有非法字符");
					return false;
				}
				flag1 = checkOneValue(queryCondition.getColumnIdFirst(),
						queryCondition.getOpFirst(), "查询条件一:");
			}
			if (!StringUtil.isEmpty(queryCondition.getValueSecond())) {
				if (StringUtil.checkStr(queryCondition.getValueSecond())) {
					this.addActionMessage("查询条件二含有非法字符");
					return false;
				}
				flag2 = checkOneValue(queryCondition.getColumnIdSecond(),
						queryCondition.getOpSecond(), "查询条件二:");
			}
			if (!StringUtil.isEmpty(queryCondition.getValueThird())) {
				if (StringUtil.checkStr(queryCondition.getValueThird())) {
					this.addActionMessage("查询条件三含有非法字符");
					return false;
				}
				flag3 = checkOneValue(queryCondition.getColumnIdThird(),
						queryCondition.getOpThird(), "查询条件三:");
			}
		}
		flag = flag1 && flag2 && flag3;
		return flag;
	}

	/**
	 * 判断该列是否是字典项 true:表示字典项 false:不是字典项
	 * 
	 * @return
	 */
	private boolean checkDic(String columnId) {
		ColumnInfo columnInfo = getColumnInfo(tableSelectId, columnId);
		if (columnInfo != null && columnInfo.getTagType() != null
				&& columnInfo.getTagType().length() > 0) {
			if (columnInfo.getTagType().equals("3")
					&& !"BUOCMONTH".equals(columnInfo.getColumnId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 非字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return
	 */
	private String getNoDicSql(String columId, int op, String values) {
		StringBuffer sb = new StringBuffer();
		String typeF = getColumnType(tableSelectId, columId);
		if (typeF.equals(TYPE_STRING) || "TRADEDATE".equals(columId)
				|| "RPTDATE".equals(columId) || "IMPDATE".equals(columId)) {
			sb.append(" and t.");
			sb.append(columId + " ");
			sb.append(operatorsMap.get(new Integer(op)));
			if (op == TYPE_LIKE) {
				sb.append(" '%" + values + "%'");
			} else {
				sb.append(" '" + values + "'");
			}
		} else if (typeF.equals(TYPE_DATE)) {
			if (this.getDbType().equalsIgnoreCase("db2")) {
				//
				sb.append(" and SUBSTR(CHAR(t.").append(columId).append(
						"),1,10) ");
				sb.append(operatorsMap.get(new Integer(op)));
				String datetemp = values.substring(0, 4) + "-"
						+ values.substring(4, 6) + "-" + values.substring(6, 8);
				if (op == TYPE_LIKE) {
					sb.append(" '%" + datetemp + "%'");
				} else {
					sb.append(" '" + datetemp + "'");
				}
			} else if (this.getDbType().equalsIgnoreCase("sqlserver")) {
				//
				sb.append(" and convert(varchar(20), t.").append(columId)
						.append(", 23) ");
				sb.append(operatorsMap.get(new Integer(op)));
				if (op == TYPE_LIKE) {
					sb.append(" '%" + values + "%'");
				} else {
					sb.append(" '" + values + "'");
				}
			} else if (this.getDbType().equalsIgnoreCase("oracle")) {
				//
				sb.append(" and to_char(t.").append(columId).append(
						",'yyyymmdd') ");
				sb.append(operatorsMap.get(new Integer(op)));
				if (op == TYPE_LIKE) {
					sb.append(" '%" + values + "%'");
				} else {
					sb.append(" '" + values + "'");
				}
			}
		} else {
			sb.append(" and t.");
			sb.append(columId + " ");
			sb.append(operatorsMap.get(new Integer(op)));
			sb.append(" " + values);
		}
		return sb.toString();
	}

	/**
	 * 字典项的SQL
	 * 
	 * @param columId
	 * @param op
	 * @param values
	 * @return
	 */
	private String getDicSql(String columId, int op, String values) {
		ColumnInfo columnInfo = getColumnInfo(tableSelectId, columId);
		StringBuffer sb = new StringBuffer();
		sb.append(" and t.");
		sb.append(columId + " in (");
		if (!"TEAMID".equalsIgnoreCase(columId)) {
			sb.append(" select code_value_standard_num ").append(
					" from t_code_dictionary ").append(" where code_type = '")
					.append(columnInfo.getDictionaryTypeId()).append(
							"' and code_name ");
		} else {
			sb.append(" select BUSS_TYPE_CODE ").append(
					" from T_RPT_BUSS_TYPE ").append(" where BUSS_TYPE_NAME ");
		}
		sb.append(operatorsMap.get(new Integer(op)));
		if (op == TYPE_LIKE) {
			sb.append(" '%" + values + "%')");
		} else {
			sb.append(" '" + values + "')");
		}
		return sb.toString();
	}

	private String createSqlCondition(QueryCondition qc, String bDate,
			String eDate, String userId) {
		String returnStr = "";
		StringBuffer sb = new StringBuffer();
		// 增加所有选项
		if (StringUtil.isEmpty(instCode)) {
			// 未指定查询机构
			if (authInstList == null || authInstList.size() == 0) {
				// 没有有权限的机构
				return " 1 = 0 ";
			}
		}
		if (qc != null) {
			if (!StringUtil.isEmpty(qc.getValueFirst())) {
				if (!CONTRACTTYPE.equals(qc.getColumnIdFirst())) {
					// 先判断该列是否是字典项
					if (checkDic(qc.getColumnIdFirst())) {
						sb.append(getDicSql(qc.getColumnIdFirst(), qc
								.getOpFirst(), qc.getValueFirst()));
					} else {
						sb.append(getNoDicSql(qc.getColumnIdFirst(), qc
								.getOpFirst(), qc.getValueFirst()));
					}
				} else {
					sb.append(getContractTypeSQL((String) operatorsMap
							.get(new Integer(qc.getOpFirst())), qc
							.getValueFirst()));
				}
			}
			if (!StringUtil.isEmpty(qc.getValueSecond())) {
				if (!CONTRACTTYPE.equals(qc.getColumnIdSecond())) {
					if (checkDic(qc.getColumnIdSecond())) {
						sb.append(getDicSql(qc.getColumnIdSecond(), qc
								.getOpSecond(), qc.getValueSecond()));
					} else {
						sb.append(getNoDicSql(qc.getColumnIdSecond(), qc
								.getOpSecond(), qc.getValueSecond()));
					}
				} else {
					sb.append(getContractTypeSQL((String) operatorsMap
							.get(new Integer(qc.getOpSecond())), qc
							.getValueSecond()));
				}
			}
			if (!StringUtil.isEmpty(qc.getValueThird())) {
				if (!CONTRACTTYPE.equals(qc.getColumnIdThird())) {
					if (checkDic(qc.getColumnIdThird())) {
						sb.append(getDicSql(qc.getColumnIdThird(), qc
								.getOpThird(), qc.getValueThird()));
					} else {
						sb.append(getNoDicSql(qc.getColumnIdThird(), qc
								.getOpThird(), qc.getValueThird()));
					}
				} else {
					sb.append(getContractTypeSQL((String) operatorsMap
							.get(new Integer(qc.getOpThird())), qc
							.getValueThird()));
				}
			}
		}
		if (!StringUtil.isEmpty(bDate)) {
			if ("oracle".equalsIgnoreCase(this.getDbType())) {
				sb.append(" and t.AUDITDATE>=to_date('" + bDate
						+ "','yyyymmdd')");
			} else if ("db2".equalsIgnoreCase(this.getDbType())) {
				String bDateTemp = DateUtils.toString(DateUtils.stringToDate(
						bDate, DateUtils.ORA_DATES_FORMAT),
						DateUtils.ORA_DATE_TIMES_FORMAT);
				sb.append(" and t.AUDITDATE>=timestamp('" + bDateTemp + "')");
			}
		}
		if (!StringUtil.isEmpty(eDate)) {
			Date tmpend = DateUtils.getAfterData(DateUtils.stringToDate(eDate,
					DateUtils.ORA_DATE_FORMAT), 1);
			if ("oracle".equalsIgnoreCase(this.getDbType())) {
				String endStr = DateUtils.toString(tmpend,
						DateUtils.ORA_DATE_FORMAT);
				sb.append(" and t.AUDITDATE<=to_date('" + endStr
						+ "','yyyy-mm-dd')");
			} else if ("db2".equalsIgnoreCase(this.getDbType())) {
				String endStr = DateUtils.toString(tmpend,
						DateUtils.ORA_DATE_TIMES_FORMAT);
				sb.append(" and t.AUDITDATE<=timestamp('" + endStr + "')");
			}
		}
		if ((authInstList != null) && (authInstList.size() > 0)) {
			sb
					.append(
							" and exists (select fk_orgId from t_user_org where fk_userId = '")
					.append(userId).append("' and t.instCode = fk_orgId) ");
		}
		if (sb.length() > 3) {
			returnStr = sb.toString().replaceFirst("and", "");
		}
		System.out.println("SQL:" + returnStr);
		return returnStr;
	}

	/**
	 * 异步获得列名和列ID
	 * 
	 * @return
	 */
	public String asyLoadColumnName() {
		StringBuffer sb = new StringBuffer();
		String infoType = this.request.getParameter("infoType");
		if (StringUtil.isNotEmpty(infoType) && infoType.length() > 0) {
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(null, "1", "1"));
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			String busiDataType = this.getBusiDataType(this.infoType,
					busiDataInfoList);
			if (rptTableList != null && rptTableList.size() > 0) {
				if ("2".equals(busiDataType)) {
					for (int i = 0; i < rptTableList.size(); i++) {
						((RptTableInfo) rptTableList.get(i)).setFileType(String
								.valueOf(i + 1));
						if (((RptTableInfo) rptTableList.get(i)).getFileType()
								.equals(this.fileType)) {
							this.tableSelectId = ((RptTableInfo) rptTableList
									.get(i)).getTableId();
						}
					}
					if (this.tableSelectId == null) {
						this.tableSelectId = ((RptTableInfo) rptTableList
								.get(0)).getTableId();
						this.fileType = "1";
					}
				} else {
					if (this.fileType == null
							|| !this.fileType.startsWith(this.infoType)) {
						this.tableSelectId = ((RptTableInfo) rptTableList
								.get(0)).getTableId();
						this.fileType = ((RptTableInfo) rptTableList.get(0))
								.getFileType();
					} else {
						for (int i = 0; i < rptTableList.size(); i++) {
							if (((RptTableInfo) rptTableList.get(i))
									.getFileType().equals(this.fileType)) {
								this.tableSelectId = ((RptTableInfo) rptTableList
										.get(i)).getTableId();
							}
						}
					}
				}
			}
		} else {
			if (StringUtil.isNotEmpty(this.tableSelectId)
					&& this.tableSelectId.indexOf("#") > 0) {
				fileType = this.tableSelectId.split("#")[1];
				tableSelectId = this.tableSelectId.split("#")[0];
			}
		}
		List columnInfos = dataDealService.findRptColumnInfo(new RptColumnInfo(
				tableSelectId, null, "1", this.fileType));
		for (int i = 0; i < columnInfos.size(); i++) {
			RptColumnInfo tmpColumnInfo = (RptColumnInfo) columnInfos.get(i);
			sb.append(tmpColumnInfo.getColumnId().trim());
			sb.append(",");
			sb.append(tmpColumnInfo.getColumnName().trim());
			sb.append(",");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		String columnNames = sb.toString();
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			System.out.println("columnName:" + columnNames);
			this.response.getWriter().print(columnNames);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// TODO:(fwy)高级查询时，下拉列表不动的解决方法
		// return SUCCESS;
		return null;
	}

	public String asyRelaLoadColumnName() {
		StringBuffer sb = new StringBuffer();
		sb.append("common.jt.CUSTCOD");
		sb.append(",");
		sb.append("组织机构代码");
		sb.append(",");
		sb.append("common.jt.CUSTNM");
		sb.append(",");
		sb.append("汇款人名称");
		sb.append(",");
		sb.append("common.jt.OPPUSER");
		sb.append(",");
		sb.append("收款人名称");
		sb.append(",");
		sb.append("common.jt.BUSCODE");
		sb.append(",");
		sb.append("银行业务编号");
		sb.append(",");
		sb.append("common.----------");
		sb.append(",");
		sb.append("----------");
		sb.append(",");
		String columnNames = sb.toString();
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			System.out.println("columnName:" + columnNames);
			this.response.getWriter().print(columnNames);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// weblogic8.1
		this.request.setAttribute("rptColumnList", this.rptColumnList);
		this.request.setAttribute("orderColumn", this.orderColumn);
		this.request.setAttribute("orderDirection", this.orderDirection);
		this.request.setAttribute("recordList", this.recordList);
		return SUCCESS;
	}

	private boolean initSession() {
		// 把报表列信息和查询SQL放在SESSION中
		if (this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP2) == null) {
			Map[] map = dataDealService.initRptColumnSqlMap(largestColumnNum,
					"1");
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP2, map[0]);
			this.addFieldToSession(
					ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP2, map[1]);
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
		// 将参数配置项记录信息放在SESSION中
		this.initConfigParameters();
		// weblogic8.1
		this.request.setAttribute("rptColumnList", this.rptColumnList);
		this.request.setAttribute("orderColumn", this.orderColumn);
		this.request.setAttribute("orderDirection", this.orderDirection);
		this.request.setAttribute("recordList", this.recordList);
		return true;
	}

	private void initConfigParameters() {
		// 将参数配置项记录信息放在SESSION中
		if ("yes".equalsIgnoreCase(this.configIsCluster)) {
			configMap = userInterfaceConfigService.initConfigParameters();
			this.addFieldToSession(ScopeConstants.SESSION_CONFIG_PARAMETER_MAP,
					configMap);
		} else {
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache != null) {
				configMap = (Map) cache.get("configMap");
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
			}
		}
		this.setConfigParametersBaseList(configMap);
	}

	/**
	 * 根据报表ID取rptColumnList,以及所有字段的查询SQL。从SESSION缓存中取
	 */
	private String getColumnsSql(String tableUniqueId) {
		// String suffixKey = "";
		// if(StringUtil.isNotEmpty(fileType) && tableId.indexOf("_SUB_") < 0){
		// suffixKey = ":" + fileType;
		// }
		Map rptColumnListMap = (HashMap) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_LIST_MAP2);
		if (rptColumnListMap != null) {
			rptColumnList = (ArrayList) rptColumnListMap.get(tableUniqueId);
		}
		Map rptColumnSqlMap = (HashMap) this
				.getFieldFromSession(ScopeConstants.SESSION_RPT_TABLE_COLUMN_SQL_MAP2);
		if (rptColumnSqlMap == null) {
			return "";
		} else {
			return (String) rptColumnSqlMap.get(tableUniqueId);
		}
	}

	/**
	 * 返回表名列表
	 * 
	 * @return
	 */
	private boolean loadTableNameByType(String userId) {
		// 根据信息类型获取is_show为1的报表列表
		RptTableInfo t = new RptTableInfo();
		if ("5".equals(infoTypeCode)) {
			t.setInfoType("单位基本信息");
		} else if ("9".equals(infoTypeCode)) {
			// STOB
			t.setOrderBys(infoTypeCode);
		} else {
			t.setInfoType(DictionaryUtil.getInfoType(infoTypeCode == null ? "1"
					: infoTypeCode, this.interfaceVer));
		}
		t.setIsShow("1");
		rptTableList = dataDealService.findRptTableInfo(t, userId);
		if ("9".equals(infoTypeCode) && rptTableList.size() > 0) {
			Map map = new HashMap();
			for (int i = 0; i < rptTableList.size(); i++) {
				RptTableInfo rti = (RptTableInfo) rptTableList.get(i);
				if (!map.containsKey(rti.getTableName())) {
					map.put(rti.getTableName(), rti);
				}
			}
			rptTableList = new ArrayList(map.values());
		}
		// if (StringUtil.equalsIgnoreCase(infoTypeCode, "3")) {
		// RptTableInfo rtpTable = new RptTableInfo();
		// rtpTable.setTableId("t_fini_export_shenbao_out");
		// rtpTable.setTableName("出口收汇核销专用联信息申报表（境外收入）");
		// rptTableList.add(rtpTable);
		// }
		return true;
	}

	// 获取当前用户拥有权限的机构集
	private void getAuthInstList(List authInstList) {
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		if (authInstList == null) {
			authInstList = new ArrayList();
		}
		authInstList.addAll(currentUser.getOrgs());
	}

	private void setSelectTagValue(List recordList, String tableId,
			List rptColumnList) {
		Map dictionaryMap = (Map) SystemCache
				.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP);
		Map tableMap = null;
		Map publicMap = null;
		if (dictionaryMap != null) {
			tableMap = (HashMap) dictionaryMap.get(tableId);
			publicMap = (HashMap) dictionaryMap.get("PUBLIC");
		}
		List bussTypeList = null;
		List rptDatas = recordList;
		// 循环报表数据集，对每条记录，循环其所有字段，若字段tagType为3（下拉框），则将字段值设置为字典表里对应的中文描述显示
		for (Iterator i = rptDatas.iterator(); i.hasNext();) {
			RptData rptData = (RptData) i.next();
			// 设置机构名称
			for (int j = 0; j < this.authInstList.size(); j++) {
				Organization orga = (Organization) authInstList.get(j);
				if (orga.getId().equals(rptData.getInstCode())) {
					rptData.setInstName(orga.getName());
				}
			}
			if (tableMap != null) {
				for (Iterator j = rptColumnList.iterator(); j.hasNext();) {
					RptColumnInfo column = (RptColumnInfo) j.next();
					if ("3".equals(column.getTagType())) {
						Object codeValue = BeanUtils.getValue(rptData, column
								.getAliasColumnId());
						String codeName = null;
						if ("BUOCMONTH".equalsIgnoreCase(column.getColumnId())) {
							continue;
						} else if ("TEAMID".equalsIgnoreCase(column
								.getColumnId())) {
							if (CollectionUtil.isEmpty(bussTypeList)) {
								User currentUser = (User) this
										.getFieldFromSession(Constants.USER);
								bussTypeList = dataDealService
										.findBussTypeList(currentUser.getId());
							}
							for (Iterator k = bussTypeList.iterator(); k
									.hasNext();) {
								Dictionary dict = (Dictionary) k.next();
								if (dict.getValueStandardNum()
										.equals(codeValue)) {
									codeName = dict.getName();
								}
							}
						} else if ("T_FAL_Z02".equalsIgnoreCase(tableId)
								&& "TABLECODE".equalsIgnoreCase(column
										.getColumnId())) {
							codeName = String.valueOf(codeValue);
						} else {
							codeName = (String) tableMap.get(column
									.getDictionaryTypeId()
									+ "_" + codeValue);
							if (StringUtil.isEmpty(codeName)) {
								codeName = (String) publicMap.get(column
										.getDictionaryTypeId()
										+ "_" + codeValue);
							}
						}
						BeanUtils.setValue(rptData, column.getAliasColumnId(),
								codeName == null ? "" : codeName);
					}
				}
			}
			// 判断单据状态是否为未校验
			if (String.valueOf(DataUtil.WJY_STATUS_NUM).equals(
					rptData.getDataStatus())) {
				RptData rd = new RptData();
				rd.setBusinessId(rptData.getBusinessId());
				rd.setTableId(tableId);
				String lowerStatusReasion = dataDealService.getRefuseCheckInfo(
						rd, "2");
				if (StringUtil.isNotEmpty(lowerStatusReasion)) {
					rptData.setDataStatusDesc("（打回）");
				}
			}
			// 判断单据状态是否为已生成
			else if (String.valueOf(DataUtil.YSC_STATUS_NUM).equals(
					rptData.getDataStatus())
					&& "1".equals(this.busiDataType)
					&& !"1".equals(this.fileType)) {
				String keyRptNoColumnId = DataUtil
						.getRptNoColumnIdByFileType(this.fileType);
				String byeRptNoColumnId = DataUtil
						.getByeRptNoColumnIdByFileType(this.fileType);
				RptData rd = new RptData();
				rd.setTableId(tableId);
				rd.setBusinessId(rptData.getBusinessId());
				rd.setRptNoColumnId(keyRptNoColumnId);
				rd.setByeRptNoColumnId(byeRptNoColumnId);
				List listReduce = null;
				listReduce = dataDealService.findRptDataReduce(rd);
				if (listReduce != null && listReduce.size() == 1) {
					rd = (RptData) listReduce.get(0);
					String keyRptNo = rd.getRptNo();
					String byeRptNo = rd.getByeRptNo();
					String dataNo = keyRptNo;// 反馈信息表业务数据主键
					if (StringUtil.isNotEmpty(byeRptNoColumnId)
							&& StringUtil.isNotEmpty(byeRptNo)) {
						dataNo += "," + byeRptNo;
					}
					if (StringUtil.isNotEmpty(dataNo)) {
						ReceiveReport receiveReport = dataDealService
								.findReceiveReportByDataNumber(dataNo);
						if (receiveReport != null && receiveReport.getId() > 0
								&& "0".equals(receiveReport.getHasReject())) {
							rptData.setDataStatusDesc("（反馈错误）");
						}
					}
				}
			}
			// 查询显示外债变动、余额信息对应的签约信息类型
			if (("AR".equals(rptData.getFileType()) || "AS".equals(rptData
					.getFileType()))) {
				String fileTypeName = null;
				if (StringUtil.isNotEmpty(rptData.getCFileType())) {
					fileTypeName = DataUtil.getTableNameByFileType(rptData
							.getCFileType());
				} else if ((StringUtil.isNotEmpty(rptData.getBusinessNo()) || StringUtil
						.isNotEmpty(rptData.getRptNo()))) {
					RptData rd = dataDealService
							.findRptDataByRptNoAndBusinessNo("T_CFA_A_EXDEBT",
									rptData.getFileType(), rptData.getRptNo(),
									rptData.getBusinessNo());
					if (rd != null && rd.getFileType() != null) {
						fileTypeName = DataUtil.getTableNameByFileType(rd
								.getFileType());
					}
				}
				if (fileTypeName != null && fileTypeName.indexOf("-") > 0) {
					rptData.setFileTypeDesc(fileTypeName.substring(0,
							fileTypeName.indexOf("-")));
				}
			}
		}
	}

	private void setSelectTagValueSimple(List recordList) {
		for (Iterator i = recordList.iterator(); i.hasNext();) {
			RptData rptData = (RptData) i.next();
			// 设置机构名称
			for (int j = 0; j < this.authInstList.size(); j++) {
				Organization orga = (Organization) authInstList.get(j);
				if (orga.getId().equals(rptData.getInstCode())) {
					rptData.setInstName(orga.getName());
				}
			}
		}
	}

	private void addCommonColumnListQuery() {
		rptColumnListQuery.clear();
		if (CollectionUtil.isNotEmpty(rptColumnList)) {
			for (int i = 0; i < rptColumnList.size(); i++) {
				RptColumnInfo rpt = new RptColumnInfo();
				RptColumnInfo rci = (RptColumnInfo) rptColumnList.get(i);
				org.springframework.beans.BeanUtils.copyProperties(rci, rpt);
				rptColumnListQuery.add(rpt);
			}
		}
		if ("AR".equalsIgnoreCase(this.fileType)
				|| "AS".equalsIgnoreCase(this.fileType)) {
			RptColumnInfo rci = new RptColumnInfo();
			rci.setColumnId(CONTRACTTYPE);
			rci.setColumnName("签约类型");
			rptColumnListQuery.add(rci);
		}
		// if(this.busiDataType != null && "1".equals(this.busiDataType)){
		// RptColumnInfo rci = new RptColumnInfo();
		// rci.setColumnId("BUSINESSNO");
		// rci.setColumnName("业务编号");
		// rptColumnListQuery.add(rci);
		// }
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

	public String getInfoTypeCode() {
		return infoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode) {
		this.infoTypeCode = infoTypeCode;
	}

	public DataDealService getDataDealService() {
		return dataDealService;
	}

	public void setDataDealService(DataDealService dataDealService) {
		this.dataDealService = dataDealService;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public String getTableSelectId() {
		return tableSelectId;
	}

	public void setTableSelectId(String tableSelectId) {
		this.tableSelectId = tableSelectId;
	}

	public List getRptColumnList() {
		return rptColumnList;
	}

	public void setRptColumnList(List rptColumnList) {
		this.rptColumnList = rptColumnList;
	}

	public QueryCondition getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(QueryCondition queryCondition) {
		this.queryCondition = queryCondition;
	}

	public Map getOperatorsMap() {
		return operatorsMap;
	}

	public void setOperatorsMap(Map operatorsMap) {
		// DataSearchAction.operatorsMap = operatorsMap;
	}

	public int getLargestColumnNum() {
		return largestColumnNum;
	}

	public void setLargestColumnNum(int largestColumnNum) {
		this.largestColumnNum = largestColumnNum;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getQueryConditionResult() {
		return queryConditionResult;
	}

	public void setQueryConditionResult(String queryConditionResult) {
		this.queryConditionResult = queryConditionResult;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

	public String getTableStr() {
		return tableStr;
	}

	public void setTableStr(String tableStr) {
		this.tableStr = tableStr;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public List getDataStatusList() {
		return dataStatusList;
	}

	public void setDataStatusList(List dataStatusList) {
		this.dataStatusList = dataStatusList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public int getCanExcel() {
		return canExcel;
	}

	public void setCanExcel(int canExcel) {
		this.canExcel = canExcel;
	}

	public String getOrderColumnSub() {
		return orderColumnSub;
	}

	public void setOrderColumnSub(String orderColumnSub) {
		this.orderColumnSub = orderColumnSub;
	}

	public String getOrderDirectionSub() {
		return orderDirectionSub;
	}

	public void setOrderDirectionSub(String orderDirectionSub) {
		this.orderDirectionSub = orderDirectionSub;
	}

	public List getBusiDataInfoList() {
		return busiDataInfoList;
	}

	public void setBusiDataInfoList(List busiDataInfoList) {
		this.busiDataInfoList = busiDataInfoList;
	}

	public String getTableUniqueSelectId() {
		return tableUniqueSelectId;
	}

	public void setTableUniqueSelectId(String tableUniqueSelectId) {
		this.tableUniqueSelectId = tableUniqueSelectId;
	}

	public OrganizationService getOrganizationService() {
		return organizationService;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public RptTemplateService getRptTemplateService() {
		return rptTemplateService;
	}

	public void setRptTemplateService(RptTemplateService rptTemplateService) {
		this.rptTemplateService = rptTemplateService;
	}
}
