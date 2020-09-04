/**
 * 
 */
package com.cjit.gjsz.datadeal.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptLogInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.system.model.SysData;

/**
 * @author yulubin
 */
public interface DataDealService extends GenericService {

	public Long findRptInfoByBusi(RptData rptData);

	/**
	 * t_rpt_table_info:分页查询
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 */
	public List findRptTableInfo(RptTableInfo info,
			PaginationList paginationList);

	/**
	 * t_rpt_table_info:普通查询
	 * 
	 * @param info
	 * @return
	 */
	public List findRptTableInfo(RptTableInfo info, String userId);

	/**
	 * 根据数据表物理名称查询数据表信息
	 * 
	 * @param tableId
	 * @param fileType
	 * @return RptTableInfo
	 */
	public RptTableInfo findRptTableInfoById(String tableId, String fileType);

	public RptTableInfo findRptTableInfoBySaveTableId(String saveTableId);

	public String getRptCheckInfo(RptData rptData);

	public List findLowerStatusLogReasion(RptData rptData);

	/**
	 * 根据报表物理表名和机构号查询记录数
	 * 
	 * @param tableId
	 * @param instCode
	 * @param fileType
	 * @return
	 */
	public Long findRptDataCountByTableIdAndInstCode(String tableId,
			String instCode, String fileType);

	/**
	 * 根据报表物理表名和机构号查询记录数
	 * 
	 * @param rptData
	 * @return
	 */
	public Long findRptDataCountByTableIdAndInstCode(RptData rptData);

	/**
	 * 根据报表物理表名和机构号查询各个状态记录数
	 * 
	 * @param tableId
	 * @param instCode
	 * @param searchLowerOrg
	 *            是否查询下游机构记录
	 * @param fileType
	 * @param userId
	 * @param linkBussType
	 *            是否关联业务类型
	 * @return
	 */
	public List findRptDataStatusCountByTableIdAndInstCode(String tableId,
			String instCode, String searchLowerOrg, String fileType,
			String userId, String linkBussType);

	/**
	 * 根据报表物理表名和机构号查询各个状态记录数
	 * 
	 * @param rptData
	 * @return
	 */
	public List findRptDataStatusCountByTableIdAndInstCode(RptData rptData);

	public List findRptDataStatusCountByInfoTypeAndInstCode(String infoType,
			String instCode, String searchLowerOrg, String userId,
			String linkBussType, String buocMonth);

	/**
	 * 分页查询：根据报表物理表名，查询列，以及其它条件查询物理数据
	 * 
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param dataStatusCondition
	 * @param businessNo
	 * @param paginationList
	 * @return
	 */
	public List findRptData(RptData rptData, PaginationList paginationList);

	// 同上，不分页
	public List findRptData(RptData rptData);

	public Long findRptDataCount(RptData rptData);

	public List findSysData(SysData sysData, PaginationList paginationList);

	public List findSysData(SysData sysData);

	/**
	 * 按照表名和业务主键集查询数据
	 * 
	 * @param tableId
	 * @param businessIds
	 * @param columns
	 * @return
	 */
	public List findRptDataByTableIdAndBusinessIds(String tableId,
			List businessIds, String columns);

	public List findRptDataByTableIdAndInstCodes(String tableId,
			String fileType, List instCodes, String datastatus, String columns,
			String searchCondition, String orderBy);

	/**
	 * 分页查询：查询内嵌表物理数据
	 * 
	 * @param tableId
	 * @param columns
	 * @param businessId
	 * @param paginationList
	 * @return
	 */
	public List findInnerRptData(RptData rptData, PaginationList paginationList);

	// 同上，不分页
	public List findInnerRptData(RptData rptData);

	/**
	 * 分页查询： 根据关联物理报表（直接上游报表）表名，报表物理表名，查询列，以及其它条件查询物理数据（数据中包含关联报表数据和报表数据）
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatus
	 * @param businessNo
	 * @param paginationList
	 * @return
	 */
	public List findRelaRptData(RptData rptData, PaginationList paginationList);

	// 同上 不分页
	public List findRelaRptData(RptData rptData);

	/**
	 * 分页查询：根据关联物理报表（直接上游报表）表名，报表物理表名，查询列，以及其它条件查询物理数据（数据中包含关联报表数据和报表数据）
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param startDate
	 * @param endDate
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatus
	 * @param businessNo
	 * @param paginationList
	 * @return
	 */
	public List findRelaRptDataNew(RptData rptData,
			PaginationList paginationList);

	// 同上 不分页
	public List findRelaRptDataNew(RptData rptData);

	public List findRptDataX(String columns, String startDate, String endDate,
			String instCode, String tableId, String dataStatus,
			String otherCondition, PaginationList paginationList);

	public List findRptData2(String columns, String startDate, String endDate,
			String instCode, String tableId, String dataStatus,
			String otherCondition, PaginationList paginationList);

	public int saveRptData(String tableId, String insertColumns,
			String insertValues);

	public int saveSysData(SysData sysData);

	public int updateSysData(SysData sysData);

	public void saveRptDataBatch(List data);

	public int updateRptData(RptData rptData);

	public void updateRptDataBatch(List data);

	public int updateRptData2(RptData rptData, String busiType);

	public String getRefuseCheckInfo(RptData rptData, String busiType);

	public int updateRptDataForLowerStatus(RptData rptData);

	public List findCodeDictionaryList(String codeType, String codeSym);

	public List findShownColumnNameList(String tableId);

	public List findRptColumnInfo(RptColumnInfo rptColumnInfo);

	public Map[] initRptColumnSqlMap(int largestColumnNum);

	public Map[] initRptColumnSqlMap(int largestColumnNum, String isShow);

	public Map[] initRptColumnSqlMapNew(int largestColumnNum);

	public void deleteRptData(RptData rptData);

	// DFHL:
	public int getSerialNo(String tableId, String CustomId, String rptTitle,
			String curDate);

	// DFHL:
	// 判断某条记录是否曾经上报过 且接收过反馈（is_receive = '1'）
	public boolean isRptHasSendCommit(String tableId, String businessId);

	// 判断某条记录是否曾经生成过报文（在t_rpt_send_commit表中有对应记录）
	public boolean isRptFileGen(String tableId, String businessId,
			String fileName);

	// 记录上报记录
	public void insertRptSendCommit(String tableId, String businessId,
			String packName, String fileName, int isReceive);

	// 批量记录上报记录
	public void insertRptSendCommitBatch(List sendMapList);

	// 修改报文报送记录
	public void updateRptSendCommitIsReceive(String packName,
			String[] fileNames, String tableId, String businessId, int isReceive);

	// 修改报文报送记录
	public void updateRptSendCommitPackFile(String tableId, String businessId,
			String packName, String fileName, int isReceive);

	/**
	 * <p>
	 * 方法名称: findRptSendCommit|描述: 查询报文报送记录
	 * </p>
	 * 
	 * @param tableId
	 *            表ID
	 * @param businessId
	 *            业务ID
	 * @param packName
	 *            包名
	 * @param fileName
	 *            文件名
	 * @param isReceive
	 *            是否已收到正确的反馈
	 * @return
	 */
	public List findRptSendCommit(String tableId, String businessId,
			String packName, String fileName, int isReceive);

	/**
	 * <p>
	 * 方法名称: findRptSendCommitSuccess|描述: 根据反馈T文件，查询对应包中所有正常保送的tableId和fileName
	 * </p>
	 * 
	 * @param packName
	 * @param fileNames
	 * @return List<RptSendCommit>
	 */
	public List findRptSendCommitSuccess(String packName, String[] fileNames);

	/**
	 * 查询报文修改记录(可有翻页)
	 * 
	 * @param rptLogInfo.tableId
	 * @param paginationList
	 * @return List<RptLogInfo>
	 * @author lihaiboA
	 */
	public List findRptLogInfo(RptLogInfo rptLogInfo,
			PaginationList paginationList);

	public Long findRptLogInfoCount(RptLogInfo rptLogInfo);

	/**
	 * 查询报文修改记录
	 * 
	 * @param rptData.tableId
	 * @param rptData.columns
	 * @param rptData.businessId
	 * @param rptData.subId
	 * @return List<RptLogInfo>
	 * @author lihaiboA
	 */
	public List findRptDataToLogInfo(RptData rptData);

	/**
	 * 新增报文修改记录
	 * 
	 * @param rptLogInfo
	 * @return int
	 * @author lihaiboA
	 */
	public int insertRptLogInfo(RptLogInfo rptLogInfo);

	/**
	 * 判断申报号是否已存在
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return boolean
	 * @author lihaiboA
	 */
	public boolean judgeRptNoRepeat(RptData rptData);

	/**
	 * 查询仅包含业务ID、数据状态和申报号的数据
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return List
	 * @author lihaiboA
	 */
	public List findRptDataReduce(RptData rptData);

	/**
	 * 查询仅包含业务ID、数据状态和申报号的数据(单位基本信息)
	 * 
	 * @param rptData.tableId
	 * @param rptData.rptNo
	 * @param rptData.businessId
	 * @return List
	 * @author lihaiboA
	 */
	public List findRptDataReduceCompany(RptData rptData);

	/**
	 * 根据单位基本信息查询仅包含业务ID、数据状态和申报号的基础信息数据
	 * 
	 * @param rptData.tableId
	 * @param rptData.custCode
	 * @param rptData.instCode
	 * @param rptData.companyBusinessId
	 * @return List
	 * @author lihaiboA
	 */
	public List findBaseRptDataReduceByCompany(RptData rptData);

	/**
	 * 判断组织机构代码是否已存在
	 * 
	 * @param tableId
	 * @param custCode
	 * @return boolean
	 * @author tongxiaoming
	 */
	public boolean judgeCustCodeRepeat(String tableId, String custCode);

	/**
	 * 查询报文关键字报送记录
	 * 
	 * @param tableId
	 * @param businessId
	 * @return List
	 */
	public List findRptKeywordSendlog(String tableId, String businessId);

	/**
	 * 删除报文关键字变更记录
	 * 
	 * @param tableId
	 * @param businessId
	 */
	public void deleteRptKeywordChange(String tableId, String businessId);

	/**
	 * 查询报文关键字变更记录个数
	 * 
	 * @param tableId
	 * @param businessId
	 * @return long
	 */
	public long findRptKeywordChangeCount(String tableId, String businessId);

	/**
	 * 删除报送记录
	 * 
	 * @param tableId
	 * @param businessId
	 */
	public void deleteRptSendCommit(String tableId, String businessId);

	/**
	 * 修改数据表信息
	 * 
	 * @param busiTableId
	 * @param updateSql
	 * @return int
	 */
	public int updateRptTableInfo(String busiTableId, String updateSql);

	/**
	 * 修改数据列信息
	 * 
	 * @param tableId
	 * @param columnId
	 * @param updateSql
	 * @return int
	 */
	public int updateRptColumnInfo(String tableId, String columnId,
			String updateSql);

	// ////////////////////////////////////////////////////
	public List findRptBusiDataInfo(RptBusiDataInfo rptBusiDataInfo);

	public List findRptBusiDataInfoWithAll(RptBusiDataInfo rptBusiDataInfo);

	public List findRptCfaContract(RptData rptData,
			PaginationList paginationList);

	public String findMaxIndexCode(RptData rptData);

	/**
	 * 通过报表id和filetype建立报表唯一标示
	 * 
	 * @param tableId
	 * @param fileType
	 * @return
	 */
	public String createTablueUniqueId(String tableId, String fileType);

	/**
	 * 自动更新DOFOEXLOCODE（国内外汇贷款编号）
	 * 
	 * @param tableId
	 * @param businessId
	 * @return String
	 */
	public String autoUpdateDofoecloCode(String tableId, String businessId);

	/**
	 * 锁定已生成报文记录 同时标记该xml已经上传至MTS服务目录
	 * 
	 * @param sendFileName
	 *            报送xml文件全名
	 */
	public void lockDatas(String sendFileName);

	/**
	 * 判断某报文xml文件是否已上传至MTS服务目录(is_sendmts = 1)
	 * 
	 * @param packName
	 * @param fileName
	 * @return boolean
	 */
	public boolean isFileSendMts(String packName, String fileName);

	/**
	 * <p>
	 * 方法名称: findUnsettledReport|描述: 查询待处理的信息
	 * </p>
	 * 
	 * @param tableId
	 * @param fileType
	 * @param notFileType
	 * @param dataStatus
	 * @param instCode
	 * @param modifyUser
	 * @param noModifyUser
	 * @param rptTitle
	 * @return List
	 */
	public List findUnsettledReport(String tableId, String fileType,
			String notFileType, String dataStatus, String instCode,
			String modifyUser, String noModifyUser, String rptTitle);

	public RptData findRptDataByRptNoAndBusinessNo(String tableId,
			String fileType, String rptNo, String businessNo);

	public RptData findRptDataByRelatedBusinessId(String tableId,
			String fileType, String businessId);

	public String findRptDataStatus(String tableId, String businessId);

	public String findRptNoByBusinessNo(String rptCfaNoColumnId,
			String tableId, String businessNo);

	public String findBusinessNoByBusinessId(String tableId, String fileType,
			String businessId);

	public boolean validateData(List rptColumnList, RptData rptData,
			boolean isSkipBlanks) throws Exception;

	public List findPackNameListByInstCode(List orgIdList);

	public List findPackNameListByUserId(String userId);

	public ReceiveReport findReceiveReportByDataNumber(String dataNo);

	public void updateRptDatastatus(String tableId, final List rptDataList);

	public void batchLowerStatusLinkage(String tableId, final List rptDataList);

	public List findRptCountGroupbyStatus(RptData rptData);

	public void updateRptDataStatusByInstCodes(String tableId,
			String updateSql, int dataStatus, String[] instCodes,
			String whereCondition, String rptTitle);

	/**
	 * 获取：AR-变动编号/AS-变动编号/BC-履约编号/CB-变动编号/DB-变动编号/EB-变动编号/FB-终止支付编号/FC-付息编号
	 * 
	 * @param tableId
	 * @param fileType
	 * @param columnId
	 * @param rptNo
	 * @param businessNo
	 * @return String
	 */
	public String findIndexCodeForSelf(String tableId, String fileType,
			String columnId, String rptNo, String businessNo);

	/**
	 * 根据包名，获取此报文包所包括的业务表名称
	 * 
	 * @param packName
	 * @return List
	 */
	public List findTableListFromSendCommit(String packName);

	/**
	 * 删除审核不通过、打回原因
	 * 
	 * @param rptData
	 * @param busiType
	 */
	public void deleteRefuseReasion(RptData rptData, String busiType,
			String deleteCondition);

	/**
	 * 依据报送记录，添加打回原因（为解析ErrorFiles目录信息所用）
	 * 
	 * @param reasion
	 * @param busiType
	 * @param packName
	 */
	public void insertRefuseReasionFromSendCommit(String reasion,
			String busiType, String packName);

	public List findBussTypeList(String userId);

	public List findBussTypeList(String code, String name);

	public void saveBussType(String code, String name);

	public void updateBussType(String code, String name, String enabled);

	public void modifyRefuseReasion(String busiType, final List rptDataList);

	public void updateRptSendCommitIsReceiveBatch(final List rptDataList,
			String isReceive, String fileName);
}
