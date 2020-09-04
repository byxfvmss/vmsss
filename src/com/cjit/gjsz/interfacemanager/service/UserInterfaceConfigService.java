/**
 * UserInterface Service
 */
package com.cjit.gjsz.interfacemanager.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.interfacemanager.model.BasePrimaryKey;
import com.cjit.gjsz.interfacemanager.model.BillClass;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.model.KeyInfo;
import com.cjit.gjsz.interfacemanager.model.LoadData;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.system.model.Mts;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.RoleUser;

/**
 * @author huboA
 */
public interface UserInterfaceConfigService extends GenericService{

	/**
	 * 得到TableInfo
	 * @return
	 */
	public List getTableInfos(TableInfo tableInfo);

	/**
	 * 得到TableInfo
	 * @return
	 */
	public TableInfo getTableInfo(String tableId);

	/**
	 * 得到TableInfo
	 * @return
	 */
	public List getTableInfosByType(TableInfo tableInfo);

	/**
	 * 得到TableInfo
	 * @return
	 */
	public TableInfo getTableInfosByTypeId(TableInfo tableInfo);

	/**
	 * 得到所有ColumnInfo
	 * @return
	 */
	public List getColumnInfos(ColumnInfo columnInfo);

	/**
	 * @param columnInfo
	 * @return
	 */
	public List getColumnInfosByInsert(ColumnInfo columnInfo);

	/**
	 * 得到所有ColumnInfo
	 * @return
	 */
	public ColumnInfo getColumnInfo(ColumnInfo columnInfo);

	/**
	 * 得到所有Dictionary
	 * @return
	 */
	public List getDictionarys(ColumnInfo columnInfo);
	
	public List<Dictionary> getDictionarys(String codeType, String codeSym);
	public List<Dictionary> getDictionarys1(String codeType, String codeSym,String backup_num);
	
	/**
	 * 获取其他票据类型
	 * @param billType
	 * @return
	 */
	public BillClass getBillClass(String billType);
	
	/**
	 * 获取其他票据类型列表
	 * @param billType
	 * @return
	 */
	public List getBillClassList();
	
	/**
	 * 根据ID取出字典
	 * @param dictionaryId
	 * @return
	 */
	public Dictionary getDictionary(int dictionaryId);

	/**
	 * 更新字典项
	 * @param dictionary
	 */
	public void updateDictionary(Dictionary dictionary);

	/**
	 * 分页取出某个表单下的所有字典记录
	 * @param columnInfo
	 * @param paginationList
	 * @return
	 */
	public List getDictionarys(ColumnInfo columnInfo,
			PaginationList paginationList);

	/**
	 * 得到所有字典(初始化系统时,加载到内存中去)
	 * @param columnInfo
	 * @return
	 */
	public List getAllDictionarys(ColumnInfo columnInfo);

	public List getAllDictionarysByCache();

	public List getAllDictionarys();

	public List getSwitchCodesByCache();

	public void removeDictionarysByCache();

	/**
	 * 更新 columnInfo
	 * @param columnInfo
	 */
	public void updateColumnInfo(ColumnInfo columnInfo);

	/**
	 * <p>方法名称: createRptNoPrefix|描述: 创建申报号前缀字符</p>
	 * @param orgId
	 * @return String
	 */
	public String createRptNoPrefix(String orgId);

	/**
	 * 创建申报单号
	 * @param keyInfo
	 * @return
	 */
	public String createAutokey(KeyInfo keyInfo);
	
	public String createAutokey(String objCode, String buocMonth, String fileType);

	/**
	 * 根据机构,当前时间等信息生成最后一条流水号
	 * @param org
	 * @param currentDate
	 * @param keyInfo
	 * @return
	 */
	public String getLastFlowNumber(Organization org, String currentDate,
			KeyInfo keyInfo);

	/**
	 * 根据接口文件返回所有的列信息
	 * @param columnInfo
	 * @return
	 */
	public ColumnInfo getColumnInfoByUserInterface(ColumnInfo columnInfo);

	/**
	 * 判断某个表的业务主键是否已经存在
	 * @param tableId
	 * @param BusinessId
	 * @return
	 */
	public boolean isHasBusinessKey(String tableId, String BusinessId);

	/**
	 * 判断某个子表的主键是否已经存在
	 * @param tableId
	 * @param subKey
	 * @param subId
	 * @return
	 */
	public boolean isHasSubKey(String tableId, String subKey, String subId);

	/**
	 * 判断总数
	 * @param tableId
	 * @param subKey
	 * @param subId
	 * @return
	 */
	public Long subKeySize(String tableId, String subKey, String subId);

	/**
	 * 判断总数2
	 * @param tableId
	 * @param subKey
	 * @param subId
	 * @return
	 */
	public Long subKeySize(String tableId, String subKey, String subId,
			String bussKey, String bussId);

	public List subList(String column, String tableId, String subKey,
			String subId, String bussKey, String bussId);

	/**
	 * 根据子表ID返回所有其主表信息
	 * @param tableId
	 * @return
	 */
	public List findTableRelation(String tableId);

	/**
	 * 根据得到主表的业务主键
	 * @param tableId
	 * @return
	 */
	public String getBaseBusinessKey(String tableId, String BusinessId);

	public String getCompanyKey(String tableId, String businessId);

	public BasePrimaryKey getBaseRecord(String tableId, String pk,
			String businessId);

	public List getImportList(LoadData loadData);

	public void saveLoadData(LoadData loadData);

	public void updateLoadData(LoadData loadData);

	public void deleteLoadData(LoadData loadData);

	public LoadData getLoadData(LoadData loadData);

	public List getLoadDatas(LoadData loadData);

	/**
	 * 把ylb的dataDealAction的工具方法挪到这里了 便于公用
	 * @return
	 */
	public Map[] initDictionaryMap();

	public Map[] refreshDictionaryMap();

	/**
	 * <p> 方法名称: initConfigParameters|描述: 初始化数据库中config参数配置信息map </p>
	 * @return Map
	 */
	public Map initConfigParameters();

	public Map initConfigMts();

	public void updateConfigMts(Mts mtsVO);

	public void updateConfig(String string, String autoShow);

	public List findVRoleUser(RoleUser roleUser, String searchUser);

	public List findRelaTablesByRoleId(String roleId);

	public void deleteRelaTables(String objId, String tableId, String objType);

	public void insertRelaTables(String insertColumns, String insertValues);
}
