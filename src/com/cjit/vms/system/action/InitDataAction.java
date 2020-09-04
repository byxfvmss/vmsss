package com.cjit.vms.system.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.InitRunningLog;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.util.DataUtil;

public class InitDataAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private String initPath;// config.properties文件中配置的跑入数据文件存储路经
	private String initTime;// 初始化执行开始时间yyyyMMddHHmmssSSS
	private List initLogList;// 初始化操作日志
	private static final String TOKEN_LOCK_FILE_NAME = "Token.lock";
	public static final int BATCH_SIZE_MAX = 300; // 批量执行，最大执行条数
	private static final String SYSTEM_AUTO_INIT = "system auto init";// 系统自动执行加载
	private String dataDate;//

	public InitDataAction() {
		init();
	}

	public synchronized boolean init() {
		// 初始化相关参数
		System.out
				.println("\n\n\n\n InitDataAction init -------------------------- \n\n\n\n");
		return true;
	}

	/**
	 * 定时执行初始化数据跑入
	 */
	public String execute() {
		System.out
				.println("\n\n\n\n InitDataAction execute -------------------------- \n\n\n\n");
		this.initRunData(SYSTEM_AUTO_INIT);
		return "";
	}

	private String initRunData(String userId) {
		// 获取初始化执行开始时间yyyyMMddHHmmssSSS
		initTime = DateUtils.serverCurrentDate(DateUtils.TIMESTAMP_FORMAT);
		// 操作信息日志
		List initLogList = new ArrayList();
		// add by wang
		int insertedSize = 0;
		boolean createBankDataLock = false;
		if (userId == null) {
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			userId = currentUser.getId();
		}
		String instCode = "";
		String dataDate = DateUtils
				.serverCurrentDate(DateUtils.ORA_DATE_FORMAT);
		initLogList.add(new InitRunningLog(userId, initTime, DateUtils
				.serverCurrentDateTime(), "", dataDate, "开始初始化数据", ""));
		try {
			// 初始化文件存储路经
			File fileDir = new File(this.initPath);
			if (fileDir.exists()) {
				// 初始化文件集合
				File[] files = fileDir.listFiles();
				if (files != null && files.length > 0) {
					boolean isCtl = false;// 控制文件是否存在
					boolean isTxt = false;// 数据文件是否存在
					boolean isEnd = false;// 结束文件是否存在
					boolean isLock = false;// 锁文件是否存在
					Map ctlFileMap = new HashMap();
					Map txtFileMap = new HashMap();
					for (int m = 0; m < files.length; m++) {
						File file = files[m];
						String fileName = file.getName();
						if (fileName.toUpperCase().endsWith("CTL")) {
							isCtl = true;
							String[] fileNameInfos = fileName.split("\\.");
							ctlFileMap.put(fileNameInfos[0], fileName);
						} else if (fileName.toUpperCase().endsWith("TXT")) {
							isTxt = true;
							String[] fileNameInfos = fileName.split("\\.");
							txtFileMap.put(fileNameInfos[0], fileName);
						} else if (fileName.toUpperCase().endsWith("END")) {
							isEnd = true;
							String[] fileNameInfos = fileName.split("\\.");
							if (fileNameInfos != null
									&& fileNameInfos.length == 6) {
								instCode = fileNameInfos[2];
							}
						} else if (fileName.toUpperCase().endsWith("LOCK")) {
							isLock = true;
							break;
						}
					}
					if (ctlFileMap != null && txtFileMap != null
							&& ctlFileMap.size() != txtFileMap.size()) {
						Iterator iteCtl = ctlFileMap.keySet().iterator();
						while (iteCtl.hasNext()) {
							String key = (String) iteCtl.next(); // key
							if (!txtFileMap.containsKey(key)) {
								initLogList.add(new InitRunningLog(userId,
										initTime, DateUtils
												.serverCurrentDateTime(), "",
										dataDate, "表单" + key
												+ "对应数据文件不存在，暂不执行数据跑入", ""));
								log.error("BATCH-VMSB0003-E miss txt file for table "
										+ key);
								return "";
							}
						}
						Iterator iteTxt = txtFileMap.keySet().iterator();
						while (iteTxt.hasNext()) {
							String key = (String) iteTxt.next(); // key
							if (!ctlFileMap.containsKey(key)) {
								initLogList.add(new InitRunningLog(userId,
										initTime, DateUtils
												.serverCurrentDateTime(), "",
										dataDate, "表单" + key
												+ "对应控制文件不存在，暂不执行数据跑入", ""));
								log.error("BATCH-VMS0002-E miss clt file for table "
										+ key);
								return "";
							}
						}
					}
					if (!isCtl) {
						// 不存在控制文件，暂不执行数据跑入
						initLogList.add(new InitRunningLog(userId, initTime,
								DateUtils.serverCurrentDateTime(), "",
								dataDate, "不存在控制文件，暂不执行数据跑入", ""));
						log.error("BATCH-VMS0002-E no init clt file");
						return "";
					} else if (!isTxt) {
						// 不存在数据文件，暂不执行数据跑入
						initLogList.add(new InitRunningLog(userId, initTime,
								DateUtils.serverCurrentDateTime(), "",
								dataDate, "不存在数据文件，暂不执行数据跑入", ""));
						log.error("BATCH-VMS0003-E no init txt file");
						return "";
					} else if (!isEnd) {
						// 不存在结束文件，暂不执行数据跑入
						initLogList.add(new InitRunningLog(userId, initTime,
								DateUtils.serverCurrentDateTime(), "",
								dataDate, "不存在结束文件，暂不执行数据跑入", ""));
						log.error("BATCH-VMS0004-E no init end file");
						return "";
					} else if (isLock) {
						// 存在锁文件，数据跑入正在执行，不继续执行新跑入操作
						initLogList.add(new InitRunningLog(userId, initTime,
								DateUtils.serverCurrentDateTime(), "",
								dataDate,
								"存在锁文件，数据跑入正在执行(文件访问权限不存在)，不继续执行新跑入操作", ""));
						log.error("BATCH-VMS0006-E exists init lock file");
						return "";
					} else {
						// 可进行数据文件跑入
						// 先生成锁文件
						createBankDataLock = this.makeLockFile(this.initPath,
								TOKEN_LOCK_FILE_NAME);
						for (int m = 0; m < files.length; m++) {
							File file = files[m];
							String fileName = file.getName();
							if (fileName.toUpperCase().endsWith("CTL")) {
								// 当前读取的是控制文件
							} else if (fileName.toUpperCase().endsWith("TXT")) {
								// 当前读取的是数据文件
								StringBuffer sbInsertColumns = new StringBuffer();
								long seqNumber = 1;
								boolean needDeleteCustDatas = true;// 是否需要删除已有客户信息
								String tableId = "";// 操作业务表名称
								if (fileName.toUpperCase().startsWith(
										"BIVMSOUTVAT")) {
									// 销项税交易明细(insert)
									tableId = "vms_trans_info";
									this.deleteImportData(userId, dataDate,
											tableId);
									sbInsertColumns
											.append("TRANS_ID,TRANS_DATE,TRANS_TYPE,CUSTOMER_CODE,CUSTOMER_OFFICE_CODE,")
											.append("GOODS_NAME,SPECANDMODEL,GOODS_UNIT,GOODS_NO,GOODS_PRICE,")
											.append("TAX_FLAG,AMT,TAX_RATE,TAX_AMT,IS_REVERSE,")
											.append("BANK_CODE,DEPART_CODE,REMARK,INPUT_DATE,INPUT_TIME,")
											.append("TRAN_OFFICE,TRAN_KIND,TRAN_NO,TRAN_SUB_NO,ORG_CURR_CODE,")
											.append("ORG_GL_CODE,ORG_GL_SUB_CODE,ORG_ACC_NO,AMT_CCY,BOOKING_CCY,")
											.append("GST_EXCHANGE_RATE,GST_C_VALUE_DATE,GST_C_BOOKING_DATE,GST_TAX_CODE,GST_C_FLAG,")
											.append("GST_PAID_FLAG,INCOME,BALANCE,")
											.append("INSTCODE,IMPORTDATE,DATASTATUS,SOURCE,TRANS_ID_MUFG");
								} else if (fileName.toUpperCase().startsWith(
										"BIVMSINVAT")) {
									// 进项税交易明细(insert)
									tableId = "vms_input_trans_info";
									this.deleteImportData(userId, dataDate,
											tableId);
									sbInsertColumns
											.append("TRANS_ID,TRANS_DATE,TRANS_TYPE,CUSTOMER_CODE,CUSTOMER_OFFICE_CODE,")
											.append("GOODS_NAME,SPECANDMODEL,GOODS_UNIT,GOODS_NO,GOODS_PRICE,")
											.append("TAX_FLAG,AMT,TAX_RATE,TAX_AMT,IS_REVERSE,")
											.append("BANK_CODE,DEPART_CODE,REMARK,INPUT_DATE,INPUT_TIME,")
											.append("TRAN_OFFICE,TRAN_KIND,TRAN_NO,TRAN_SUB_NO,ORG_CURR_CODE,")
											.append("ORG_GL_CODE,ORG_GL_SUB_CODE,ORG_ACC_NO,AMT_CCY,BOOKING_CCY,")
											.append("GST_EXCHANGE_RATE,GST_C_VALUE_DATE,GST_C_BOOKING_DATE,GST_TAX_CODE,GST_COLLECTION_FLAG,")
											.append("GST_PAID_FLAG,INVOICE_CODE,INVOICE_NO,ID,INCOME,BALANCE,")
											.append("INSTCODE,IMPORTDATE,DATASTATUS,SOURCE,DEDUCTED_FLAG");
								} else if (fileName.toUpperCase().startsWith(
										"BIVMSCUST")) {
									// 客户信息(delete insert)
									tableId = "vms_customer_tb";
									sbInsertColumns
											.append("CUSTOMER_ID,")
											.append("CUSTOMER_CODE,CUSTOMER_OFFICE,CUSTOMER_CNAME,CUSTOMER_TAXNO,TAXPAYER_TYPE,")
											.append("VAT_INVOICE,CUSTOMER_ADDRESS,CUSTOMER_PHONE,CUSTOMER_ACCOUNT,CUSTOMER_CBANK");
								}
								String encoding = "UTF-8";
								InputStreamReader read = new InputStreamReader(
										new FileInputStream(file), encoding);// 考虑到编码格式
								BufferedReader bufferedReader = new BufferedReader(
										read);
								String lineTxt = null;
								List sqlList = new ArrayList();
								int nRowNumber = 1;
								while ((lineTxt = bufferedReader.readLine()) != null) {
									// System.out.println(lineTxt);
									// modify by wang 20150827
									// String[] ss = lineTxt.split("\\,");
									
									String[] ss = dataLineFormat(lineTxt);
									if (ss != null) {
										// modify by wang 20150827
										// 循环ss 将所有列的"去掉
										for (int i = 0; i < ss.length; i++) {
//											if (ss[i].contains("\"")) {

												ss[i] = ss[i].toString()
														.replace("\'", "\'\'")
														.trim();
												// if
												// (fileName.toUpperCase().startsWith("BIVMSCUST"))
												// {
												// ss[3]="1";
												// ss[4]="O";
												// ss[5]="1";
												// }
												// if(fileName.toUpperCase().startsWith("BIVMSOUTVAT")){
												// ss[2]="0737";
												// }
//											}
										}

										// 处理截取后的首尾字段
										// if (ss[0].equals("\"")) {
										// ss[0] = "";
										// } else if (ss[0].startsWith("\"")) {
										// ss[0] = ss[0].substring(1, ss[0]
										// .length());
										// }
										// if (ss[ss.length - 1].equals("\"")) {
										// ss[ss.length - 1] = "";
										// } else if (ss[ss.length - 1].trim()
										// .endsWith("\"")) {
										// ss[ss.length - 1] = ss[ss.length - 1]
										// .replaceAll("\"", "")
										// .trim();
										// }
										
										if (fileName.toUpperCase().startsWith(
												"BIVMSOUTVAT")) {
											// 销项税交易明细(insert)
											if (ss.length != 36) {
												// 解析导入文件包含字段数与接口不符
												initLogList
														.add(new InitRunningLog(
																userId,
																initTime,
																DateUtils
																		.serverCurrentDateTime(),
																"",
																dataDate,
																"表单BIVVMSOUTVAT数据文件第"
																		+ nRowNumber
																		+ "行项目不符",
																"BIVVMSOUTVAT"));
												log.error("BATCH-VMS0008-E txt file column inconformity ");
											}
											StringBuffer sbSQL = new StringBuffer();
											sbSQL.append(
													" insert into vms_trans_info (")
													.append(sbInsertColumns
															.toString())
													.append(") values (");
//											.append("TRANS_ID,TRANS_DATE,TRANS_TYPE,CUSTOMER_CODE,CUSTOMER_OFFICE_CODE,")
//											.append("GOODS_NAME,SPECANDMODEL,GOODS_UNIT,GOODS_NO,GOODS_PRICE,")
//											.append("TAX_FLAG,AMT,TAX_RATE,TAX_AMT,IS_REVERSE,")
//											.append("BANK_CODE,DEPART_CODE,REMARK,INPUT_DATE,INPUT_TIME,")
//											.append("TRAN_OFFICE,TRAN_KIND,TRAN_NO,TRAN_SUB_NO,ORG_CURR_CODE,")
//											.append("ORG_GL_CODE,ORG_GL_SUB_CODE,ORG_ACC_NO,AMT_CCY,BOOKING_CCY,")
//											.append("GST_EXCHANGE_RATE,GST_C_VALUE_DATE,GST_C_BOOKING_DATE,GST_TAX_CODE,GST_C_FLAG,")
//											.append("GST_PAID_FLAG,INCOME,BALANCE,")
//											.append("INSTCODE,IMPORTDATE,DATASTATUS,SOURCE,TRANS_ID_MUFG");
											String transId = ss[0]
													+ NumberUtils
															.format(seqNumber++,
																	NumberUtils.DEFAULT_SEQNUMBER);
											transId = this
													.createBusinessId("T");// 交易ID改为由系统时间戳+随机数
											sbSQL.append("'")
													.append(transId)
													.append("','")
													.append(DateUtils.toString(
															DateUtils
																	.stringToDate(
																			ss[1],
																			DateUtils.ORA_DATE_FORMAT),
															DateUtils.ORA_DATES_FORMAT))
													.append("','")
													.append(ss[2])
													.append("','")
													.append(ss[3])
													.append("','")
													.append(ss[4]).append("',");
											sbSQL.append("'").append(ss[5])
													.append("','")
													.append(ss[6])
													.append("','")
													.append(ss[7])
													.append("','")
													.append(ss[8])
													.append("','")
													.append(ss[9]).append("',");
											sbSQL.append("'").append(ss[10])
													.append("','")
													.append(ss[11])// 金额 AMT
													.append("',")
													.append(ss[12]).append("/100")
													.append(",'")
													.append(ss[13])
													.append("','")
													.append(ss[14])
													.append("',");
											sbSQL.append("'").append(ss[15])
													.append("','")
													.append(ss[16])
													.append("','")
													.append(ss[17])
													.append("','")
													.append(ss[18])
													.append("','")
													.append(ss[19])
													.append("',");
											sbSQL.append("'").append(ss[20])
													.append("','")
													.append(ss[21])
													.append("','")
													.append(ss[22])
													.append("','")
													.append(ss[23])
													.append("','")
													.append(ss[24])
													.append("',");
											sbSQL.append("'").append(ss[25])
													.append("','")
													.append(ss[26])
													.append("','")
													.append(ss[27])
													.append("','")
													.append(ss[28])
													.append("','")
													.append(ss[29])
													.append("','");
											sbSQL.append(ss[30]).append("','")
													.append(ss[31])
													.append("','")
													.append(ss[32])
													.append("','")
													.append(ss[33])
													.append("','")
													.append(ss[34])
													.append("',");
											sbSQL.append("'").append(ss[35])
													.append("','")
													.append(ss[11])// income
													.append("','");
											sbSQL.append(ss[11])// balance
													.append("','")
													.append(ss[4])
													.append("','")
													.append(dataDate)
													.append("','")
													.append(DataUtil.TRANS_STATUS_1)
													.append("','")
													.append(fileName)
													.append("','")
													.append(ss[0]).append("')");
											System.out
													.println(sbSQL.toString());
//											.append("GST_PAID_FLAG,INCOME,BALANCE,")
//											.append("INSTCODE,IMPORTDATE,DATASTATUS,SOURCE,TRANS_ID_MUFG");
											sqlList.add(sbSQL.toString());
										} else if (fileName.toUpperCase()
												.startsWith("BIVMSINVAT")) {
											// 进项税交易明细(insert)
											if (ss.length != 38) {
												// 解析导入文件包含字段数与接口不符
												initLogList
														.add(new InitRunningLog(
																userId,
																initTime,
																DateUtils
																		.serverCurrentDateTime(),
																"",
																dataDate,
																"表单BIVVMSINVAT数据文件第"
																		+ nRowNumber
																		+ "行项目不符",
																"BIVVMSINVAT"));
												log.error("BATCH-VMS0008-E txt file column inconformity ");
											}
											StringBuffer sbSQL = new StringBuffer();
											sbSQL.append(
													" insert into vms_input_trans_info (")
													.append(sbInsertColumns
															.toString())
													.append(") values (");
											// TRANS_ID
											// TRANS_DATE
											// TRANS_TYPE
											// CUSTOMER_CODE
											// CUSTOMER_OFFICE_CODE
											sbSQL.append("'").append(ss[0])
													.append("','")
													.append(ss[1])
													.append("','")
													.append(ss[2])
													.append("','")
													.append(ss[3])
													.append("','")
													.append(ss[4]).append("',");
											// GOODS_NAME
											// SPECANDMODEL
											// GOODS_UNIT
											// GOODS_NO
											// GOODS_PRICE
											sbSQL.append("'").append(ss[5])
													.append("','")
													.append(ss[6])
													.append("','")
													.append(ss[7])
													.append("','")
													.append(ss[8])
													.append("','")
													.append(ss[9]).append("',");
											// TAX_FLAG
											// AMT
											// TAX_RATE
											// TAX_AMT
											// IS_REVERSE
											sbSQL.append("'").append(ss[10])
													.append("','")
													.append(ss[11])
													.append("','")
													.append(ss[12])
													.append("','")
													.append(ss[13])
													.append("','")
													.append(ss[14])
													.append("',");
											// BANK_CODE
											// DEPART_CODE
											// REMARK
											// INPUT_DATE
											// INPUT_TIME
											sbSQL.append("'").append(ss[15])
													.append("','")
													.append(ss[16])
													.append("','")
													.append(ss[17])
													.append("','")
													.append(ss[18])
													.append("','")
													.append(ss[19])
													.append("',");
											// TRAN_OFFICE
											// TRAN_KIND
											// TRAN_NO
											// TRAN_SUB_NO
											// ORG_CURR_CODE
											sbSQL.append("'").append(ss[20])
													.append("','")
													.append(ss[21])
													.append("','")
													.append(ss[22])
													.append("','")
													.append(ss[23])
													.append("','")
													.append(ss[24])
													.append("',");
											// ORG_GL_CODE
											// ORG_GL_SUB_CODE
											// ORG_ACC_NO
											// AMT_CCY
											// BOOKING_CCY
											sbSQL.append("'").append(ss[25])
													.append("','")
													.append(ss[26])
													.append("','")
													.append(ss[27])
													.append("','")
													.append(ss[28])
													.append("','")
													.append(ss[29])
													.append("','");
											// GST_EXCHANGE_RATE
											// GST_C_VALUE_DATE
											// GST_C_BOOKING_DATE
											// GST_TAX_CODE
											// GST_COLLECTION_FLAG
											sbSQL.append("").append(ss[30])
													.append("','")
													.append(ss[31])
													.append("','")
													.append(ss[32])
													.append("','")
													.append(ss[33])
													.append("','")
													.append(ss[34])
													.append("',");
											// GST_PAID_FLAG
											// INVOICE_CODE
											// INVOICE_NO
											// CONTRACT_NO
											// DOCUMENT_NO
											sbSQL.append("'")
													.append(ss[35])
													.append("','")
													.append(ss[36])
													.append("','")
													.append(ss[37])
													// .append(
													// "','").append(
													// ss[38]).append(
													// "','").append(
													// ss[39])
													.append("','")
													.append(this
															.createBusinessId("T")).append("','")
															.append(ss[11]).append("','")
															.append(ss[11]).append("','")
															.append(instCode).append("','")
															.append(dataDate).append("','")
															.append(DataUtil.TRANS_STATUS_1).append("','")
															.append(fileName).append("','")
															.append("N").append("')");
											sqlList.add(sbSQL.toString());
											
											
//											.append(ID,INCOME(11),BALANCE,")
//											.append("INSTCODE,IMPORTDATE,DATASTATUS,SOURCE,TRANS_ID_MUFG,DEDUCTED_FLAG((N))");
											
										} else if (fileName.toUpperCase()
												.startsWith("BIVMSCUST")) {
											// 客户信息(delete insert)
											if (ss.length != 10) {
												// 解析导入文件包含字段数与接口不符
												initLogList
														.add(new InitRunningLog(
																userId,
																initTime,
																DateUtils
																		.serverCurrentDateTime(),
																"",
																dataDate,
																"表单BIVVMSCUST数据文件第"
																		+ nRowNumber
																		+ "行项目不符",
																"BIVVMSCUST"));
												log.error("BATCH-VMS0008-E txt file column inconformity ");
											}
											if (needDeleteCustDatas) {
												// 首先删除已有客户信息
												this.customerService
														.deleteCustomer(null,
																null);
												needDeleteCustDatas = false;
												initLogList
														.add(new InitRunningLog(
																userId,
																initTime,
																DateUtils
																		.serverCurrentDateTime(),
																instCode,
																dataDate,
																"删除已有客户信息",
																"BIVVMSCUST"));
											}
											StringBuffer sbSQL = new StringBuffer();
											sbSQL.append(
													" insert into vms_customer_tb (")
													.append(sbInsertColumns
															.toString())
													.append(") values (");
											sbSQL.append("'")
													.append(createBusinessId("CT"))
													.append("','")
													.append(ss[0])
													.append("','")
													.append(ss[1])
													.append("','")
													.append(ss[2])
													//   regexp_replace('鲁税潍字370705695434664号','[^0-9]'),
													.append("',")
													.append(" case when length(")
													.append("regexp_replace('").append(ss[3]).append("','[^0-9][^X]')")
													.append(") > 15 then ")
													.append(" substr(")
													.append("regexp_replace('").append(ss[3]).append("','[^0-9][^X]')")
													.append(",'1','15')")
													.append(" else")
													.append(" rpad(")
													.append("regexp_replace('").append(ss[3]).append("','[^0-9][^X]')")
													.append(",'15','0') end")
													//.append(ss[3])
													.append(",'")
													//.append(ss[4])
													.append("O")   // 因为加载的数据文件中没有TAXPAYER_TYPE 所以给一个默认值 只能开普票
													.append("','")
													.append(ss[5])
													.append("','")
													.append(ss[6])
													.append("','")
													.append(ss[7])
													.append("','")
													.append(ss[8])
													.append("','")
													.append(ss[9]).append("')");
//											.append("CUSTOMER_ID,")
//											.append("CUSTOMER_CODE,CUSTOMER_OFFICE,CUSTOMER_CNAME,CUSTOMER_TAXNO,TAXPAYER_TYPE,")
//											.append("VAT_INVOICE,CUSTOMER_ADDRESS,CUSTOMER_PHONE,CUSTOMER_ACCOUNT,CUSTOMER_CBANK");
											sqlList.add(sbSQL.toString());
										}
										
										if (CollectionUtil.isNotEmpty(sqlList)
												&& sqlList.size() >= BATCH_SIZE_MAX) {
											// 当已读取导入数据文件行数达到批量插入设置最大记录数时，进行批量插入
											String[] sqls = (String[]) sqlList
													.toArray(new String[sqlList
															.size()]);
											//modify by wang
											int[] ints = null;
											//捕获组异常：300条中有错误，一条一条插入
											try {
												ints = systemDataService
														.insertDataBatch(sqls);
											} catch (Exception e) {
												// TODO: handle exception
												//300条中有异常 逐条插入数据
												//插入总数
												int inseredCountInTry = sqls.length;
												//逐条插入 并捕获异常生成log
												for (int i = 0; i < sqls.length; i++) {

													try {
														String[] lineSql = new String[] {sqls[i]} ;
														systemDataService.insertDataBatch(lineSql);
													} catch (Exception e2) {
														// TODO: handle exception
														//生成log插入总数减一
														e2.printStackTrace();
														log.error("initData", e);
														InitRunningLog runLog = new InitRunningLog(userId, initTime,
																DateUtils.serverCurrentDateTime(), instCode, dataDate,
																"数据初始化过程执行发生异常：" + e2.getMessage(), "");
														initLogList.add(runLog);
														//插入总数减一
														inseredCountInTry--;
													}
													
												}
												//生成正常插入log
												if (inseredCountInTry >0) {
													insertedSize = insertedSize
															+ inseredCountInTry;
													initLogList
															.add(new InitRunningLog(
																	userId,
																	initTime,
																	DateUtils
																			.serverCurrentDateTime(),
																	instCode,
																	dataDate,
																	tableId
																			+ "表中插入"
																			+ inseredCountInTry
																			+ "笔记录",
																	tableId));
												}
												
											}
											//300条正常时的log
											if (ints != null && ints.length > 0) {
												insertedSize = insertedSize
														+ ints.length;
												initLogList
														.add(new InitRunningLog(
																userId,
																initTime,
																DateUtils
																		.serverCurrentDateTime(),
																instCode,
																dataDate,
																tableId
																		+ "表中插入"
																		+ ints.length
																		+ "笔记录",
																tableId));
											}
											
											//清楚sql列表
											sqlList.clear();
										}
									}
									nRowNumber++;
								}
								
								//sql不足300条是插入操作
								if (sqlList.size() > 0) {
									String[] sqls = (String[]) sqlList
											.toArray(new String[sqlList.size()]);
									int[] ints = null;
									try {
										ints = systemDataService
												.insertDataBatch(sqls);
									} catch (Exception e) {
										// TODO: handle exception
										//300条中有异常 逐条插入数据
										int catchInseredCount = sqls.length;
										for (int i = 0; i < sqls.length; i++) {
											try {
												String[] lineSql = new String[] {sqls[i]} ;
												systemDataService.insertDataBatch(lineSql);
											} catch (Exception e2) {
												// TODO: handle exception
												e2.printStackTrace();
												log.error("initData", e2);
												InitRunningLog runLog = new InitRunningLog(userId, initTime,
														DateUtils.serverCurrentDateTime(), instCode, dataDate,
														"数据初始化过程执行发生异常：" + e2.getMessage(), "");
												initLogList.add(runLog);
												catchInseredCount--;
											}
											
										}
										if (catchInseredCount >0) {
											insertedSize = insertedSize
													+ catchInseredCount;
											initLogList
													.add(new InitRunningLog(
															userId,
															initTime,
															DateUtils
																	.serverCurrentDateTime(),
															instCode,
															dataDate,
															tableId
																	+ "表中插入"
																	+ catchInseredCount
																	+ "笔记录",
															tableId));
										}
										
									}
									
									if (ints != null && ints.length > 0) {
										initLogList
												.add(new InitRunningLog(
														userId,
														initTime,
														DateUtils
																.serverCurrentDateTime(),
														instCode, dataDate,
														"插入" + ints.length
																+ "笔记录",
														tableId));
									}
									sqlList.clear();
								}
								read.close();
//								if (fileName.toUpperCase().startsWith(
//										"BIVMSOUTVAT")) {
//									// 当导入数据为销项税交易信息时，需对价、税进行如下计算
//									String updateTableId = "vms_trans_info";
//									StringBuffer sbUpdateCondition = null;
//									StringBuffer sbUpdateColumns = null;
//									// 1、若金额不含税，交易金额=收入=balance，价税合计=收入+税额；
//									sbUpdateColumns = new StringBuffer();
//									sbUpdateColumns.append(" income = amt ");
//									sbUpdateCondition = new StringBuffer();
//									sbUpdateCondition
//											.append(" source = '")
//											.append(fileName)
//											.append("' and (tax_flag = '' or tax_flag is null or tax_flag = '0') and is_reverse = 'N' ");
//									this.systemDataService.updateFreeData(
//											updateTableId,
//											sbUpdateColumns.toString(),
//											sbUpdateCondition.toString());
//									// 2、若金额含税，做价税分离计算，税额=(交易金额*税率)/(1+税率)，收入=交易金额-税额=balance，价税合计=交易金额
//									sbUpdateColumns = new StringBuffer();
//									sbUpdateColumns
//											.append(" tax_amt = (amt * tax_rate)/(1 + tax_rate) ");
//									sbUpdateCondition = new StringBuffer();
//									sbUpdateCondition
//											.append(" source = '")
//											.append(fileName)
//											.append("' and tax_flag = '1' and is_reverse = 'N' ");
//									this.systemDataService.updateFreeData(
//											updateTableId,
//											sbUpdateColumns.toString(),
//											sbUpdateCondition.toString());
//									sbUpdateColumns = new StringBuffer();
//									sbUpdateColumns
//											.append(" income = amt - tax_amt, balance = amt - tax_amt ");
//									this.systemDataService.updateFreeData(
//											updateTableId,
//											sbUpdateColumns.toString(),
//											sbUpdateCondition.toString());
//								}
							}
						}
					}
				} else {
					// 初始化路经中无数据文件
					initLogList.add(new InitRunningLog(userId, initTime,
							DateUtils.serverCurrentDateTime(), "", dataDate,
							"初始化路经中无数据文件", ""));
				}
			} else {
				// 初始化路经不存在
				initLogList
						.add(new InitRunningLog(userId, initTime, DateUtils
								.serverCurrentDateTime(), "", dataDate,
								"初始化路经不存在", ""));
				log.error("BATCH-VMS-0001-E no init path");
			}
			// 记录初始化完成日志
			Thread.sleep(1);// 等待1毫秒
			initLogList
					.add(new InitRunningLog(userId, initTime, DateUtils
							.serverCurrentDateTime(), instCode, dataDate,
							"初始化数据结束", ""));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("initData", e);
			InitRunningLog runLog = new InitRunningLog(userId, initTime,
					DateUtils.serverCurrentDateTime(), instCode, dataDate,
					"数据初始化过程执行发生异常：" + e.getMessage(), "");
			initLogList.add(runLog);
			return ERROR;
		} finally {

			if (CollectionUtil.isNotEmpty(initLogList)) {
				for (Iterator i = initLogList.iterator(); i.hasNext();) {
					InitRunningLog runLog = (InitRunningLog) i.next();
					this.systemDataService.saveInitLog(runLog);
				}
			}
			if (createBankDataLock) {
				// 删除锁文件
				this.deleteLockFile(this.initPath, TOKEN_LOCK_FILE_NAME);
			}
		}
		return "";
	}
	
	public void autoBill(){
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String userId = currentUser.getId();
		initLogList
		.add(new InitRunningLog(userId, initTime, DateUtils
				.serverCurrentDateTime(), instCode, dataDate,
				"自动开票过程开始", ""));
		// 数据成功导入后，执行自动开票过程
		initLogList
		.add(new InitRunningLog(userId, initTime, DateUtils
				.serverCurrentDateTime(), instCode, dataDate,
				"自动开票过程结束", ""));
		
	}
	

	/***
	 * add by wang 20150827
	 * 
	 * @param dataLineStr
	 * @return
	 */
	public String[] dataLineFormat(String dataLineStr) {
		dataLineStr = dataLineStr.trim();
		ArrayList dataLineList = new ArrayList();

		while (dataLineStr.length() > 0) {
			// 以双引号开始 为字符串，以",结尾
			if (dataLineStr.startsWith("\"")) {
				int endindex = dataLineStr.indexOf("\",");
				String subStringVal = "";
				// 找不到结束符时 代表到了行尾
				if (endindex != -1) {
					subStringVal = dataLineStr.substring(1, endindex);
					dataLineStr = dataLineStr.substring(endindex + 2,
							dataLineStr.length());
				} else {
					subStringVal = dataLineStr.substring(1,
							dataLineStr.length() - 1);
					dataLineStr = "";
				}

				dataLineList.add(subStringVal.trim());
			} else {
				// 不以双引号开始 为数字，以,结尾
				int endindex = dataLineStr.indexOf(",");

				String subStringVal = "";
				// 找不到结束符时 代表到了行尾
				if (endindex != -1) {
					subStringVal = dataLineStr.substring(0, endindex);
					dataLineStr = dataLineStr.substring(endindex + 1,
							dataLineStr.length());
				} else {
					subStringVal = dataLineStr.substring(0,
							dataLineStr.length());
					dataLineStr = "";
				}
				dataLineList.add(subStringVal.trim());
			}

		}

		String[] ss = (String[]) dataLineList.toArray(new String[0]);
		return ss;
	}

	/**
	 * 生成锁文件，并返回生成结果；若锁文件已经存在，直接返回false
	 * 
	 * @param filePath
	 *            文件路径
	 * @param lockFileName
	 *            锁文件名
	 * @return boolean
	 * @throws IOException
	 */
	private boolean makeLockFile(String filePath, String lockFileName)
			throws IOException {
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		if (lockFile.exists()) {
			return false;
		} else {
			return (lockFile.createNewFile());
		}
	}

	/**
	 * 删除锁文件
	 * 
	 * @param filePath
	 * @param lockFileName
	 * @return boolean
	 */
	private boolean deleteLockFile(String filePath, String lockFileName) {
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		return lockFile.delete();
	}

	/**
	 * 手工加载时，先删除当日未处理过的跑入数据
	 * 
	 * @param userId
	 * @param dataDate
	 * @param tableId
	 * @return boolean
	 */
	private boolean deleteImportData(String userId, String dataDate,
			String tableId) {
		if (!SYSTEM_AUTO_INIT.equals(userId)) {
			// 当前加载为用户手工加载时，需删除数据库中已加载的数据
			if ("vms_trans_info".equalsIgnoreCase(tableId)) {
				TransInfo transInfo = new TransInfo();
				transInfo.setDataStatus(DataUtil.TRANS_STATUS_1);
//				transInfo.setImportDate(dataDate);
				this.transInfoService.deleteTransInfo(transInfo);
			} else if ("vms_input_trans_info".equalsIgnoreCase(tableId)) {
				this.inputVatInfoService.deleteInputTransInfo(dataDate,
						DataUtil.TRANS_STATUS_1);
			}
		}
		return false;
	}

	/**
	 * 进入手工数据加载界面
	 * 
	 * @return String
	 */
	public String initDataList() {
		try {
			dataDate = (String) request.getParameter("dataDate");
			InitRunningLog runLog = new InitRunningLog();
			if (StringUtil.isNotEmpty(dataDate)) {
				runLog.setDataDate(dataDate.replaceAll("-", ""));
			} else {
				dataDate = DateUtils
						.serverCurrentDate(DateUtils.ORA_DATES_FORMAT);
				runLog.setDataDate(DateUtils
						.serverCurrentDate(DateUtils.ORA_DATE_FORMAT));
			}
			initLogList = this.systemDataService.findInitLogList(runLog);
			this.request.setAttribute("initLogList", initLogList);
			this.request.setAttribute("dataDate", dataDate);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 手工重新加载数据
	 * 
	 * @return String
	 */
	public String initData() {
		// 获取当前用户信息
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		String userId = "";
		if (currentUser != null) {
			userId = currentUser.getId();
		}
		try {
			this.initRunData(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	public String getInitPath() {
		return initPath;
	}

	public void setInitPath(String initPath) {
		this.initPath = initPath;
		//this.initPath = "D:\\INIT\\MUFG_INIT\\VMS";
	}

	public String getInitTime() {
		return initTime;
	}

	public void setInitTime(String initTime) {
		this.initTime = initTime;
	}

	public List getInitLogList() {
		return initLogList;
	}

	public void setInitLogList(List initLogList) {
		this.initLogList = initLogList;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
}
