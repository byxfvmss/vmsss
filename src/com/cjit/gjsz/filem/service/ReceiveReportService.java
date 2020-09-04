package com.cjit.gjsz.filem.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.filem.core.ReceiveReport;
import com.cjit.gjsz.interfacemanager.model.TableInfo;

public interface ReceiveReportService extends GenericService{

	/**
	 * 保存反馈错误信息
	 * @param receiveReport
	 */
	public void saveReceiveReport(ReceiveReport receiveReport);

	/**
	 * 保存反馈错误信息记录
	 * @param receiveReport
	 */
	public void saveReceivePack(String packName, String fileName,
			String packType);

	/**
	 * 查询反馈错误信息记录
	 * @param receiveReport
	 */
	public boolean isReceivePackExists(String packName, String packType);

	/**
	 * 更新反馈错误信息,不允许再打回
	 * @param receiveReport
	 */
	public void updateReceiveReport(ReceiveReport receiveReport);

	public void deleteReceiveReports(List ids);

	/**
	 * 根据类型取得TableInfo信息 t_rpt_table_info
	 * @param fileType
	 * @return
	 */
	public TableInfo getTableInfoByFileType(String fileType);

	/**
	 * 保存导入的xml文件
	 * @param file
	 * @param fileName
	 * @return
	 */
	public String saveImportXmlFile(File file, String fileName,
			StringBuffer sb, Map map);

	/**
	 * 分页取出所有错误信息
	 * @param receiveReport
	 * @param paginationList
	 * @return
	 */
	public List getReceiveReports(ReceiveReport receiveReport,
			PaginationList paginationList);

	/**
	 * @param receiveReport
	 * @return
	 */
	public ReceiveReport getReceiveReport(ReceiveReport receiveReport);

	/**
	 * 分页取出所有错误信息
	 * @param receiveReport
	 * @param paginationList
	 * @return
	 */
	public List getReceiveReports2(ReceiveReport receiveReport,
			PaginationList paginationList);

	/**
	 * <p> 方法名称: parseFeedbackReport|描述:
	 * 根据反馈接收文件中的信息,修改T_RPT_SEND_COMMIT报文报送记录表中相应记录<br> 控制类反馈接收文件：<br>
	 * 1、修改报文报送记录表中对应报文包的所有记录为已收到反馈<br> 2、并查询所有报文记录，修改其datastatus状态为6-已报送<br>
	 * 报文类反馈接收文件：<br> 1、 </p>
	 * @param map.packName 反馈报文包名
	 * @param map.TOTALFILES 反馈报文中发生错误文件数
	 * @param map.errFileNames 反馈报文中发生错误文件名称
	 * @param changeDataStatus 对于错误反馈文件，要修改的目标数据状态
	 * @param configLowerStatusLinkage 是否联动打回下游报文
	 * @author lihaiboA
	 */
	public void parseFeedbackReport(Map map, String changeDataStatus,
			String configLowerStatusLinkage);

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
			String serverTime, List rptColumnList, List subTableIds);

	/**
	 * <p>方法名称: getFileReceiveStat|描述: 统计报文是否收到反馈以及反馈是否有误</p>
	 * @param whereConfition
	 * @param paginationList
	 * @return List
	 */
	public List getFileReceiveStat(String whereConfition,
			PaginationList paginationList);

	/**
	 * <p>方法名称: hasDuplicateReceiveReport|描述:
	 * 判断反馈文件是否曾接收过（原为private方法，现升为public 20130902）</p>
	 * @param receiveReport.rptSendFileName
	 * @return boolean
	 */
	public boolean hasDuplicateReceiveReport(ReceiveReport receiveReport);
}
