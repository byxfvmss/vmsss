/**
 * TableInfo
 */
package com.cjit.gjsz.interfacemanager.model;

import java.util.Date;
import java.util.List;

import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.system.model.User;

/**
 * @author huboA
 * @table t_rpt_table_info
 */
public class ImportModel{

	/**
	 * 表列类型
	 */
	public String[] types;
	/**
	 * 表列数量
	 */
	public int[] nums;
	/**
	 * 记录总数
	 */
	public List records;
	/**
	 * 列对应的字典项
	 */
	public String[] dictionarys;
	/**
	 * 拼接成的SQL语句
	 */
	public String sql;
	/**
	 * 全局数据字典的引用
	 */
	public List systemDictionarys;
	/**
	 * 全局编码对照
	 */
	public List switchCodes;
	/**
	 * 所有列的物理名称
	 */
	public String[] columnInfoIds;
	/**
	 * 所有列的中文名称
	 */
	public String[] columnInfoNames;
	/**
	 * 是否需要对照
	 */
	public String[] verifyFromColumns;
	/**
	 * 所有列的描述信息
	 */
	public String[] columnInfoDescriptions;
	/**
	 * 主表的申报ID
	 */
	public String[] reportIds;
	/**
	 * 要插入的表名
	 */
	public String tableId;
	/**
	 * 要插入的中文件表名
	 */
	public String tableName;
	/**
	 * 机构ID
	 */
	public String orgId;
	/**
	 * 业务主键所在位置(整形)
	 */
	public int businessId;
	/**
	 * 当前日期
	 */
	public Date currentDate;
	/**
	 * 是否导入主表,还是子表
	 */
	public boolean isImportBase = true;
	/**
	 * 开始导入行数
	 */
	public int startLine;
	/**
	 * txtid
	 */
	public int txtId;
	/**
	 * 0:表示基础，核销，申报 1:表示嵌套表
	 */
	public int importType;
	public int dataTypeCol;
	public String importDataType;
	/**
	 * 所有表信息
	 */
	public List allTables;
	/**
	 * 当前用户
	 */
	public User user;
	/**
	 * 字段对应列配置信息
	 */
	public RptColumnInfo column;
}
