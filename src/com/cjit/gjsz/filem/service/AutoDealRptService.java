package com.cjit.gjsz.filem.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cjit.crms.util.date.AppDate;

import com.cjit.gjsz.datadeal.model.RptData;

public interface AutoDealRptService {

	/**
	 * 自动生成报文
	 * 
	 * @param rptTitleList
	 *            主报告行 public String autoCreateReport(List rptTitleList);
	 */

	/**
	 * 自动生成报文
	 * 
	 * @param rptTitle
	 *            主报告行
	 */
	public String autoCreateReport(String rptTitle);

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
	public String uploadReport(List sendList, List rptTitleList, String rptTitle);

	/**
	 * 接收反馈文件
	 * 
	 * @param rptTitleList
	 *            主报告行权限,null则不过滤
	 * @return
	 */
	public String receiveReport(List rptTitleList, String rptTitle,
			String changeDataStatus, String configLowerStatusLinkage);

	/**
	 * 接收错误文件
	 * 
	 * @param rptTitleList
	 *            主报告行权限,null则不过滤
	 * @return
	 */
	public String receiveErrorFiles(List rptTitleList, String rptTitle);

	/**
	 * 接收历史文件
	 * 
	 * @param rptTitleList
	 *            主报告行权限,null则不过滤
	 * @return
	 */
	public String receiveHistorySend(List rptTitleList, String rptTitle);

	public String[] findRptTitles(String userId, String instCode);

	public Object getDataVerifyModel(String tableId, String businessid);

	/**
	 * <p>
	 * 方法名称: saveRptLogInfoData|描述: 调用ReceiveReportService的同名方法
	 * </p>
	 * 
	 * @param rd
	 *            RptData对象
	 * @param logType
	 *            日志类型
	 * @param userId
	 *            当前用户ID
	 * @param serverTime
	 *            当前系统时间
	 * @param rptColumnList
	 *            报文列信息
	 * @param subTableIds
	 *            报文子表ID列表
	 */
	public void saveRptLogInfoData(RptData rd, String logType, String userId,
			String serverTime, List rptColumnList, List subTableIds);

	/**
	 * 自动校验，仅校验状态为“未校验”的数据，子表随主表校验
	 */
	public void autoCheckData();

	public boolean isGetResource(Date ad, String taskType, String rptTitle);

	public void updateResource(Date now, String taskType, Date endDate,
			String step, String taskInfo, String rptTitle);

	public boolean isOnSchedule(String runTime, AppDate ad);

	public String uploadReport4Pre(List sendList, List rptTitleList,
			String previewFilePath);

	public String findAutoCheckRunTime();

	public Map initConfigMts();

	public Map initConfigParameters();

	public List findAllOrgConfigList(String orgName, String orgId, String userid);

	public List findRptTitleList();

	/**
	 * <p>
	 * 方法名称: belongToTopFiveWorkingDay|描述: 判断日期是否属于当月前5个工作日
	 * </p>
	 * 
	 * @param yyyyMMdd
	 * @return boolean
	 */
	public boolean belongToTopFiveWorkingDay(String yyyyMMdd);

	/**
	 * 依机构和日期，从Z02表单查询所配置当期需报送的表单号
	 * 
	 * @param objCode
	 * @param buocMonth
	 * @param instCodes
	 * @return List
	 */
	public List findRptTableCodeFromZ02(String objCode, String buocMonth,
			String[] instCodes);

	/**
	 * 依问答四，对所要生成报文的记录进行跨表、跨期校验
	 * 
	 * @param instCodes
	 *            选中生成报文的机构集合
	 * @param buocMonth
	 *            报告期
	 * @return
	 */
	public List verifyTables(String[] instCodes, String buocMonth);

}
