/**
 * UserInterface Service
 */
package com.cjit.gjsz.interfacemanager.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.UUID;
import com.cjit.gjsz.datadeal.util.DataValidater;
import com.cjit.gjsz.interfacemanager.model.BasePrimaryKey;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.model.ImportModel;
import com.cjit.gjsz.interfacemanager.model.LoadData;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.interfacemanager.model.TableRelation;
import com.cjit.gjsz.interfacemanager.service.ImportDataService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;

/**
 * @author huboA
 */
public class ImportDataServiceImpl extends GenericServiceImpl implements
		ImportDataService{

	private UserInterfaceConfigService userInterfaceConfigService;
	// 使用jdbc进行sql处理
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List getColumnInfos(String tableId){
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setTableId(tableId);
		return userInterfaceConfigService.getColumnInfosByInsert(columnInfo);
	}

	public List getDictionarys(String tableId){
		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setTableId(tableId);
		return userInterfaceConfigService.getAllDictionarys(columnInfo);
	}

	public List getAllDictionarys(){
		return userInterfaceConfigService.getAllDictionarysByCache();
	}

	public String saveImportDate(ImportModel importModel){
		StringBuffer sb = new StringBuffer();
		// 验证每组数据业务主键的合法性,如果有误,直接返回错误
		if(verifyBaseBussniessKey(importModel, sb)){
			return sb.toString();
		}
		// 验证每组数据业务主键的合法性,如果有误,直接返回错误
		if(verifyBussniessKey(importModel, sb)){
			return sb.toString();
		}
		// 验证每组子表主键的合法性,如果有误,直接返回错误
		// if (verifySubKey(importModel, sb)) {
		// return sb.toString();
		// }
		// 验证每组数据的合法性,如果有误,直接返回错误
		if(verifyDate(importModel, sb)){
			return sb.toString();
		}
		// 导入数据
		dataImport(importModel);
		// 保存LoadData
		saveLoadData(importModel);
		// 正常的返回null
		return null;
	}

	private void saveLoadData(ImportModel importModel){
		// 先删除 保存LoadData
		LoadData loadData = new LoadData();
		loadData.setLoadDate(new Date());
		loadData.setStatus(1);
		loadData.setOrgId(importModel.orgId);
		loadData.setUserInterfaceId(importModel.txtId);
		String start = DateUtils.toString(new Date(),
				DateUtils.ORA_DATES_FORMAT);
		Date startDate = DateUtils.stringToDate(start + " 00:00:00.000",
				DateUtils.ORA_DATE_TIMES_FORMAT);
		Date endDate = DateUtils.getNextDay(startDate);
		loadData.setStartDate(startDate);
		loadData.setEndDate(endDate);
		userInterfaceConfigService.deleteLoadData(loadData);
		// 保存LoadData
		userInterfaceConfigService.saveLoadData(loadData);
	}

	/**
	 * 验证主表业务主键
	 * @param importModel
	 * @param sb
	 * @return
	 */
	private boolean verifyBaseBussniessKey(ImportModel importModel,
			StringBuffer sb){
		boolean isError = false; // 是否已经存在请主表的业务主键,如果有误返回true
		String[] tables = {"t_base_income", "t_base_remit", "t_base_payment",
				"t_base_export", "t_base_dom_remit", "t_base_dom_pay",
				"t_company_info"};
		for(int i = 0; i < tables.length; i++){
			if(StringUtil.equalsIgnoreCase(tables[i], importModel.tableId)){
				return false;
			}
		}
		importModel.isImportBase = false;
		// 验证每组数据的合法性
		importModel.reportIds = new String[importModel.records.size()];
		for(int j = 0; j < importModel.records.size(); j++){
			LinkedMap map = (LinkedMap) importModel.records.get(j);
			try{
				List baseTables = userInterfaceConfigService
						.findTableRelation(importModel.tableId);
				String businessKey = (String) map
						.get((importModel.businessId - 1) + "");
				String reportId = null;
				if(CollectionUtil.isNotEmpty(baseTables)){
					for(int k = 0; k < baseTables.size(); k++){
						TableRelation tableRelation = (TableRelation) baseTables
								.get(k);
						String parentId = tableRelation.getTableId();
						if(StringUtil.isNotEmpty(parentId)){
							// 以前导入生成申报申请号时,用tableRelation.getTableColumn(),现在写死用BUSINESSID
							BasePrimaryKey basePrimaryKey = userInterfaceConfigService
									.getBaseRecord(parentId, "BUSINESSID",
											businessKey);
							if(basePrimaryKey == null){
								if(baseTables.size() <= 1){
									isError = true;
									break;
								}else{
									continue;
								}
							}
							if(tableRelation.getSubRelation() == 1){
								int status = basePrimaryKey.getDataStatus();
								if(status > 2){ // N种状态
									String parent = "";
									if(CollectionUtil
											.isNotEmpty(importModel.allTables)){
										for(int i = 0; i < importModel.allTables
												.size(); i++){
											TableInfo tableInfo = (TableInfo) importModel.allTables
													.get(i);
											if(StringUtil.equalsIgnoreCase(
													tableInfo.getTableId(),
													parentId)){
												parent = tableInfo
														.getTableName();
												break;
											}
										}
										if(StringUtil.isEmpty(parent)){
											parent = parentId;
										}
									}
									sb.append("<li>导入文件 ["
											+ importModel.tableName + "] 的第 ["
											+ (j + 1) + "] 行的父表 [" + parent
											+ "] 记录已经审核了，不允许导入。</li>\n");
									isError = true;
								}
							}
							reportId = basePrimaryKey.getReportId();
							if(StringUtil.isNotEmpty(reportId)){
								break;
							}
						}
					}
					if(StringUtil.isEmpty(reportId)){
						logBaseBusinessKeyError(sb, importModel, j, businessKey);
						isError = true;
					}else{
						importModel.reportIds[j] = reportId;
					}
				}
			}catch (Exception ex){
				ex.printStackTrace();
				sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 "
						+ (j + 1) + " 行的业务主键有误</li>\n");
				isError = true;
			}
		}
		return isError;
	}

	/**
	 * 验证主子表主键
	 * @param importModel
	 * @param sb
	 * @return
	 */
	public boolean verifySubKey(ImportModel importModel, StringBuffer sb){
		boolean isError = false; // 是否已经存在请主表的业务主键,如果有误返回true
		Map tableMap = new HashMap();
		tableMap.put("t_customs_decl", "CUSTOMN");
		tableMap.put("t_export_info", "REFNO");
		tableMap.put("t_company_openinfo", "BRANCHCODE");
		tableMap.put("t_invcountrycode_info", "INVCOUNTRYCODE");
		boolean ok = true;
		String subKey = null;
		for(Iterator i = tableMap.entrySet().iterator(); i.hasNext();){
			Map.Entry entry = (Map.Entry) i.next();
			ok = true;
			if(StringUtil.equalsIgnoreCase((String) entry.getKey(),
					importModel.tableId)){
				ok = false;
				subKey = (String) entry.getValue();
				break;
			}
		}
		if(ok){
			return false;
		}
		String[] columns = importModel.columnInfoIds;
		int baseKey = importModel.businessId - 1;
		int position = 0;
		for(int c = 0; c < columns.length; c++){
			if(c == baseKey){
				continue;
			}
			if(StringUtil.equalsIgnoreCase(columns[c], subKey)){
				position = c;
				break;
			}
		}
		for(int j = 0; j < importModel.records.size(); j++){
			LinkedMap map = (LinkedMap) importModel.records.get(j);
			try{
				String businessKey = (String) map
						.get((importModel.businessId - 1) + "");
				String _subKey = (String) map.get(position + "");
				boolean has = userInterfaceConfigService.isHasSubKey(
						importModel.tableId,
						importModel.columnInfoIds[position], _subKey);
				if(has){
					sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 ["
							+ (j + 1) + "] 行的 [" + _subKey
							+ "] 已经在数据库中存在了。</li>\n");
					isError = true;
				}
				// 对t_company_openinfo需要判断customid
				if(StringUtil.equalsIgnoreCase("t_company_openinfo",
						importModel.tableId)){
					has = userInterfaceConfigService.isHasSubKey("t_org",
							"customid", _subKey);
					if(has == false){
						sb.append("<li>导入文件 [" + importModel.tableName
								+ "] 的第 [" + (j + 1) + "] 行的 [" + _subKey
								+ "] 不在银行版金融机构基本情况表中。</li>\n");
						isError = true;
					}
				}
				// 对t_invcountrycode_info需要判断总数
				if(StringUtil.equalsIgnoreCase("t_invcountrycode_info",
						importModel.tableId)){
					has = userInterfaceConfigService.isHasSubKey(
							"t_code_dictionary", "code_value_bank", _subKey);
					if(has == false){
						sb.append("<li>导入文件 [" + importModel.tableName
								+ "] 的第 [" + (j + 1) + "] 行的 [" + _subKey
								+ "] 不在系统字典表的国别/地区代码表中。</li>\n");
						isError = true;
					}
					Long size = userInterfaceConfigService.subKeySize(
							"t_invcountrycode_info", "BUSINESSID", businessKey);
					if(size != null && size.longValue() > 5){
						sb.append("<li>导入文件 [" + importModel.tableName
								+ "] 的第 [" + (j + 1) + "] 行的 [" + _subKey
								+ "] 超过最大限制。投资国别代码个数必须小于等于5。</li>\n");
						isError = true;
					}
				}
			}catch (Exception ex){
				ex.printStackTrace();
				sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 "
						+ (j + 1) + " 行的导入有误。</li>\n");
				isError = true;
			}
		}
		return isError;
	}

	/**
	 * 验证业务主键
	 * @param importModel
	 * @param sb
	 * @return
	 */
	private boolean verifyBussniessKey(ImportModel importModel, StringBuffer sb){
		boolean isError = false; // 是否发生错误,有错误发生的话,就不再插入数据,但将错误信息返回
		// 验证每组数据的合法性
		for(int j = 0; j < importModel.records.size(); j++){
			LinkedMap map = (LinkedMap) importModel.records.get(j);
			try{
				String businessKey = (String) map
						.get((importModel.businessId - 1) + "");
				boolean isOk = this.userInterfaceConfigService
						.isHasBusinessKey(importModel.tableId, businessKey);
				if(isOk == true){
					logBusinessKeyError(sb, importModel, j, businessKey);
					isError = true;
				}
			}catch (Exception ex){
				ex.printStackTrace();
				sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 "
						+ (j + 1) + " 行的第 [" + importModel.businessId
						+ "] 列业务主键有误</li>\n");
			}
		}
		return isError;
	}

	/**
	 * 数据预检验
	 * @param importModel
	 * @param sb
	 * @return
	 */
	private boolean verifyDate(ImportModel importModel, StringBuffer sb){
		boolean isError = false; // 是否发生错误,有错误发生的话,就不再插入数据,但将错误信息返回
		List systemDictionarys = importModel.systemDictionarys;
		// 验证每组数据的合法性
		for(int j = 0; j < importModel.records.size(); j++){
			LinkedMap map = (LinkedMap) importModel.records.get(j);
			for(int i = 0; i < importModel.nums.length; i++){
				String key = "";
				try{
					String tmp = (String) map.get((importModel.nums[i] - 1)
							+ "");
					key = tmp != null ? tmp.trim() : "";
					StringBuffer errorStr = new StringBuffer();
					if(DataValidater.validateDataType(key,
							importModel.types[i], errorStr, false,
							importModel.column)){
						if(StringUtil.contains(importModel.types[i], "s")){
							if(StringUtil
									.isNotEmpty(importModel.dictionarys[i])
									&& StringUtil.isNotEmpty(key)){
								String str = this.findKey(systemDictionarys,
										importModel.dictionarys[i], key);
								if(str == null){
									isError = true;
									logDictionaryError(sb, importModel, j, i,
											key);
								}
							}
							key = StringUtil.replace(key, "'", "’");
							key = StringUtil.replace(key, "‘", "’");
							map.put((importModel.nums[i] - 1) + "", key);
						}else if(StringUtil.contains(importModel.types[i], "n")){
							if(StringUtil
									.isNotEmpty(importModel.dictionarys[i])
									&& StringUtil.isNotEmpty(key)){
								String num = this.findKey(systemDictionarys,
										importModel.dictionarys[i], key);
								if(num == null){
									isError = true;
									logDictionaryError(sb, importModel, j, i,
											key);
								}
								if(!StringUtil.equalsIgnoreCase("n,0,13,8",
										importModel.types[i].trim())){
									double val = Double.parseDouble(key);
									long finalVal = (long) (val + 0.5);
									map.put((importModel.nums[i] - 1) + "",
											finalVal + "");
								}
							}
						}else if(StringUtil.contains(importModel.types[i], "d")){
							// try {
							// DateUtils.StringToDate(key,
							// DateUtils.ORA_DATE_FORMAT);
							// } catch (Exception ex) {
							// ex.printStackTrace();
							// isError = true;
							// logDateError(sb, importModel, j, i, key);
							// }
						}
					}else{
						isError = true;
						logError(sb, errorStr, importModel, j, i, key);
					}
				}catch (Exception ex){
					ex.printStackTrace();
					logError(sb, new StringBuffer(" 类型有误"), importModel, j, i,
							key);
				}
			}
		}
		return isError;
	}

	private String innerImport(LinkedMap map, ImportModel importModel, int j,
			Object[] objs){
		List systemDictionarys = importModel.systemDictionarys;
		String type = "";
		for(int i = 0; i < importModel.nums.length; i++){
			String tmp = (String) map.get((importModel.nums[i] - 1) + "");
			String key = tmp != null ? tmp.trim() : "";
			if(StringUtil.contains(importModel.types[i], "s")){
				if(StringUtil.isNotEmpty(importModel.dictionarys[i])){
					objs[i] = this.findKey(systemDictionarys,
							importModel.dictionarys[i], key);
				}else{
					objs[i] = key;
				}
			}else if(StringUtil.contains(importModel.types[i], "n")){
				if(StringUtil.isNotEmpty(importModel.dictionarys[i])){
					String num = this.findKey(systemDictionarys,
							importModel.dictionarys[i], key);
					objs[i] = Double.valueOf(num);
				}else{
					if(StringUtil.isNotEmpty(key)){
						objs[i] = Double.valueOf(key);
					}else{
						objs[i] = null;
					}
				}
			}else if(StringUtil.contains(importModel.types[i], "d")){
				if(StringUtil.isNotEmpty(key)){
					Date date = DateUtils.stringToDate(key,
							DateUtils.ORA_DATE_FORMAT);
					objs[i] = DateUtils.toString(date,
							DateUtils.ORA_DATE_FORMAT);
				}else{
					objs[i] = null;
				}
			}
			// 这一块验证合法性。
			if(StringUtil.equalsIgnoreCase(importModel.columnInfoIds[i],
					"CUSTYPE")){
				type = getCustomType(systemDictionarys,
						importModel.dictionarys[i], key);
			}
		}
		return type;
	}

	/**
	 * 数据导入
	 * @param importModel
	 */
	public void dataImport(ImportModel importModel){
		for(int j = 0; j < importModel.records.size(); j++){
			LinkedMap map = (LinkedMap) importModel.records.get(j);
			String bussId = (String) map.get((importModel.businessId - 1) + "");
			if(!StringUtil.equalsIgnoreCase(importModel.tableId,
					"t_company_info")){
				Object[] objs = null;
				if(importModel.importType == 0){
					objs = new Object[importModel.nums.length + 4];
				}else{
					objs = new Object[importModel.nums.length + 2];
				}
				String type = innerImport(map, importModel, j, objs);
				// 如果导入主表,则生成申报号码
				if(importModel.isImportBase == true){
					// KeyInfo keyInfo = new KeyInfo(importModel.orgId,
					// importModel.currentDate, importModel.tableId, type,
					// null);
					// String key =
					// this.userInterfaceConfigService.createAutokey(keyInfo);
					// objs[importModel.nums.length] = null; // 暂时注释，不生成申报号码key;
					// // RPTNO:申报编号
				}else{
					// RPTNO:申报编号 或 机构编号
					objs[importModel.nums.length] = bussId.trim(); // BUSINESSID:业务主键
					objs[importModel.nums.length + 1] = UUID.randomUUID()
							.toString();
				}
				if(importModel.importType == 0){
					objs[importModel.nums.length] = importModel.orgId; // INSTCODE:机构号码
					objs[importModel.nums.length + 1] = new Integer(1); // DATASTATUS:审核状态
					objs[importModel.nums.length + 2] = bussId.trim(); // BUSINESSID:业务主键
					objs[importModel.nums.length + 3] = new Date(); // IMPORTDATE:导入日期
				}
				jdbcTemplate.update(importModel.sql, objs);
			}else if(StringUtil.equalsIgnoreCase(importModel.tableId,
					"t_company_info")){
				Object[] objs = new Object[importModel.nums.length + 4];
				innerImport(map, importModel, j, objs);
				objs[importModel.nums.length] = importModel.orgId; // INSTCODE:机构号码
				objs[importModel.nums.length + 1] = new Integer(1); // DATASTATUS:审核状态
				objs[importModel.nums.length + 2] = bussId.trim(); // BUSINESSID:业务主键
				objs[importModel.nums.length + 3] = new Date(); // IMPORTDATE:导入日期
				jdbcTemplate.update(importModel.sql, objs);
			}
		}
	}

	/**
	 * 取得数据是对公,还是对私的位置
	 * @param columnInfos
	 * @return
	 */
	public String getCustomType(List systemDictionarys, String dic, String key){
		return findKey(systemDictionarys, dic, key);
	}

	private String findKey(List systemDictionarys, String dataKey, String key){
		if(CollectionUtil.isNotEmpty(systemDictionarys)){
			for(int i = 0; i < systemDictionarys.size(); i++){
				Dictionary dictionary = (Dictionary) systemDictionarys.get(i);
				if(StringUtil.equalsIgnoreCase(dictionary.getType(), dataKey)){ // 如果找到币种
					if(StringUtil.equalsIgnoreCase(key, dictionary
							.getValueBank())){ // 如果ValueBlank为空,默认不需要转换
						return dictionary.getValueStandardNum(); // 将行内代码码值转成标准代码值
					}
				}
			}
			return null;
		}
		throw new RuntimeException("字典表不能为空");
	}

	public void saveIntoDatabase(ImportModel importModel, List objs){
		if(CollectionUtil.isNotEmpty(objs)){
			for(int i = 0; i < objs.size(); i++){
				Object[] parameters = (Object[]) objs.get(i);
				jdbcTemplate.update(importModel.sql, parameters);
			}
		}
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	private void logBaseBusinessKeyError(StringBuffer sb,
			ImportModel importModel, int j, String key){
		sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 ["
				+ (importModel.startLine + j) + "] 行的第 ["
				+ importModel.businessId + "] 列业务主键 [" + key
				+ "] 还不存在相应的父表记录,请核实数据</li>\n");
	}

	private void logBusinessKeyError(StringBuffer sb, ImportModel importModel,
			int j, String key){
		sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 ["
				+ (importModel.startLine + j) + "] 行的第 ["
				+ importModel.businessId + "] 列业务主键 [" + key
				+ "] 已经在,请核实数据</li>\n");
	}

	private void logDictionaryError(StringBuffer sb, ImportModel importModel,
			int j, int i, String key){
		sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 ["
				+ (importModel.startLine + j) + "] 行第 [" + importModel.nums[i]
				+ "] 列数据 [" + key + "] 在字典表 [" + importModel.dictionarys[i]
				+ "-" + importModel.columnInfoNames[i] + "] 无标准对应项</li>\n");
	}

	private void logError(StringBuffer sb, StringBuffer errorStr,
			ImportModel importModel, int j, int i, String key){
		sb.append("<li>导入文件 [" + importModel.tableName + "] 的第 ["
				+ (importModel.startLine + j) + "] 行第 [" + importModel.nums[i]
				+ "] 列数据 [" + key + "]" + errorStr.toString() + "</li>\n");
	}

	static class ColumnHolder{

		int index;
		int length;
		String[] indexs;

		ColumnHolder(){
		}

		ColumnHolder(int index, int length){
			this.index = index;
			this.length = length;
		}

		public int getIndex(){
			return index;
		}

		public void setIndex(int index){
			this.index = index;
		}

		public int getLength(){
			return length;
		}

		public void setLength(int length){
			this.length = length;
		}

		public String[] getIndexs(){
			return indexs;
		}

		public void setIndexs(String[] indexs){
			this.indexs = indexs;
		}
	}
}
