package com.cjit.gjsz.filem.action;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.ImportRptService;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.interfacemanager.model.UploadModel;
import com.cjit.gjsz.logic.model.CompanyOpenInfo;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.InvcountrycodeInfo;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

/**
 * 报文导入
 * @作者 李海波A
 * @日期 Oct 25, 2010
 */
public class ImportRptAction extends DataDealAction{

	private static final long serialVersionUID = -1L;
	private UploadModel uploadModel;
	private DataDealService dataDealService;
	private ImportRptService importRptService;
	private LogManagerService logManagerService;
	private OrgConfigeService orgConfigeService;
	private String message;

	/**
	 * <p> 方法名称: toImportPage|描述: 进入报文导入初始页面 </p>
	 * @return String
	 */
	public String toImportPage(){
		String description = "进入报文导入初始页面";
		try{
			return SUCCESS;
		}catch (Exception e){
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0004.0004", "报文管理.导入报文", "导入报文", description, "0");
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: importXML|描述: 导入报文数据 </p>
	 * @return String
	 */
	public String importRptXml(){
		String description = "导入报文数据";
		User currentUser = this.getCurrentUser();
		StringBuffer sbMessage = new StringBuffer();
		try{
			if(uploadModel != null){
				// 导入文件名
				String strFileName = uploadModel.getFileFileName();
				// 导入文件
				File file = uploadModel.getFile();
				if(strFileName != null && file != null){
					// 得到文件名中第四个字母，以判断是哪种报文
					char ch = strFileName.charAt(3);
					if("T".equals(String.valueOf(ch))){
						sbMessage.append("控制类报文不能导入！\n");
						logManagerService.writeLog(request, currentUser,
								"0004.0004", "报文管理.导入报文", "导入报文", description,
								"0");
						this.message = sbMessage.toString();
						this.request.setAttribute("message", this.message);
						return SUCCESS;
					}
					// 得到对应的数据表名
					String tableId = DataUtil.getTableIdByFileType(String
							.valueOf(ch));
					// 得到表信息类型代码
					String infoTypeCode = DataUtil
							.getInfoTypeCodeByTableId(tableId);
					// 查询数据表对应列信息
					List rptColumnList = dataDealService
							.findRptColumnInfo(new RptColumnInfo(tableId, null,
									"1", this.fileType));
					if(rptColumnList != null){
						List list = importRptService.resolveImportXmlFile(file,
								rptColumnList, tableId, sbMessage);
						if(list.size() > 0){
							for(int r = 0; r < list.size(); r++){
								RptData rptData = (RptData) list.get(r);
								// 申报号
								String rptNo = rptData.getRptNo();
								boolean bExist = false;
								// U类型文件导入不需要验证申报号,验证组织机构号。
								if("U".equals(String.valueOf(ch))){
									String custCode = rptData.getCustcode();
									bExist = dataDealService
											.judgeCustCodeRepeat(tableId,
													custCode);
									if(bExist){
										sbMessage.append("组织机构号[").append(
												rptData.getCustcode()).append(
												"]已存在于系统中！");
										logManagerService.writeLog(request,
												currentUser, "0004.0004",
												"报文管理.导入报文", "导入报文",
												description, "0");
										this.message = sbMessage.toString();
										this.request.setAttribute("message",
												this.message);
										return SUCCESS;
									}
								}else{
									bExist = dataDealService
											.judgeRptNoRepeat(new RptData(
													tableId, rptNo));
								}
								if(!bExist){
									// 交易日期
									String tradeDate = null;
									// 根据申报号，解析其机构代码
									String instCode = this
											.getInstCodeFromRptNo(rptNo);
									// 判断是否解析出机构代码
									if(instCode == null
											&& !"U".equals(String.valueOf(ch))){
										sbMessage.append("未找到申报号[").append(
												rptNo)
												.append("]对应机构代码，不能导入！\n");
										logManagerService.writeLog(request,
												currentUser, "0004.0004",
												"报文管理.导入报文", "导入报文",
												description, "0");
										this.message = sbMessage.toString();
										this.request.setAttribute("message",
												this.message);
										return SUCCESS;
									}
									// 执行插入数据库
									StringBuffer insertColumns = new StringBuffer(
											"instcode,businessid,datastatus,");
									StringBuffer insertValues = null;
									StringBuffer insertLogColumns = new StringBuffer();
									StringBuffer insertLogValues = new StringBuffer();
									String createdBusinessId = null;
									// 判断报文信息类型
									if(DataUtil.isJCDW(infoTypeCode)){
										// 信息类型为"基础信息"/"单位基本信息"
										createdBusinessId = createBusinessId();
									}else if(DataUtil.isSBHX(infoTypeCode)){
										// 信息类型为"申报信息"/"核销信息"
										String tableJcId = (String) TableIdRela
												.getJcsyMap().get(tableId);
										String tableZjId = (String) TableIdRela
												.getZjsyMap().get(tableId);
										// 根据申报号查询对应基础信息报文数据
										if(tableJcId != null
												&& !"".equals(tableJcId)){
											RptData rptDataReduce = new RptData();
											rptDataReduce.setTableId(tableJcId);
											rptDataReduce.setRptNo(rptNo);
											List listRptData = dataDealService
													.findRptDataReduce(rptDataReduce);
											if(listRptData != null
													&& listRptData.size() == 1){
												rptDataReduce = (RptData) listRptData
														.get(0);
												// 获取基础信息业务ID
												createdBusinessId = rptDataReduce
														.getBusinessId();
											}else{
												sbMessage
														.append("申报号[")
														.append(rptNo)
														.append(
																"]对应基础信息在系统中不存在！\n");
											}
										}
										// 根据申报号查询对应申报信息报文数据
										if(tableZjId != null
												&& !"".equals(tableZjId)
												&& !tableZjId.equals(tableJcId)){
											RptData rptDataReduce = new RptData();
											rptDataReduce.setTableId(tableZjId);
											rptDataReduce.setRptNo(rptNo);
											List listRptData = dataDealService
													.findRptDataReduce(rptDataReduce);
											if(listRptData == null
													|| listRptData.size() == 0){
												sbMessage
														.append("申报号[")
														.append(rptNo)
														.append(
																"]对应申报信息在系统中不存在！\n");
											}
										}
									}
									if("1".equals(infoTypeCode)
											&& !"".equals(rptNo)){
										// 当导入报文为基础信息时，通过申报号解析交易日期
										tradeDate = rptNo.substring(12, 18);
									}
									if(sbMessage.toString().length() == 0){
										// 插入业务数据SQL
										insertValues = new StringBuffer("'")
												.append(instCode)
												.append("','")
												.append(createdBusinessId)
												.append(
														"',"
																+ DataUtil.WJY_STATUS_NUM
																+ ",");
										// 插入日志SQL
										insertLogColumns
												.append("logtype,tableid,userid,updatetime,businessid");
										insertLogValues
												.append("'import','")
												.append(tableId)
												.append("','")
												.append(currentUser.getId())
												.append("','")
												.append(
														DateUtils
																.serverCurrentTimeStamp())
												.append("','").append(
														createdBusinessId)
												.append("'");
										// 字段按order赋别名c1,c2,c3...,并根据字段物理名和别名拼查询SQL
										int cFlag = 0;
										for(Iterator i = rptColumnList
												.iterator(); i.hasNext();){
											RptColumnInfo column = (RptColumnInfo) i
													.next();
											// 赋别名c1,c2,c3
											column.setAliasColumnId("c"
													+ (++cFlag));
											// 赋值交易日期
											if("TRADEDATE".equals(column
													.getColumnId()
													.toUpperCase())
													&& tradeDate != null){
												BeanUtils
														.setProperty(
																rptData,
																column
																		.getAliasColumnId(),
																"20"
																		+ tradeDate);
											}
											// 根据别名获取属性值
											String columnValue = (String) BeanUtils
													.getProperty(
															rptData,
															column
																	.getAliasColumnId());
											// 拼插入语句，字段值为空时不添加到插入语句
											if(columnValue == null
													|| "".equals(columnValue
															.trim())){
												continue;
											}
											insertColumns.append(
													column.getColumnId())
													.append(",");
											if("n".equalsIgnoreCase(column
													.getDataType().substring(0,
															1))){
												insertValues.append(
														columnValue.replaceAll(
																"'", "''"))
														.append(",");
											}else{
												insertValues
														.append("'")
														.append(
																columnValue
																		.replaceAll(
																				"'",
																				"''"))
														.append("'")
														.append(",");
											}
											insertLogColumns
													.append(",")
													.append(
															column
																	.getLogColumnId());
											insertLogValues
													.append(",'")
													.append(
															columnValue
																	.replaceAll(
																			"'",
																			"''"))
													.append("'");
										}
										if(DataUtil.isJCDWSBHX(infoTypeCode)){
											Date date = new Date();
											String strDate = "";
											insertColumns.append("IMPORTDATE,");
											if("oracle".equalsIgnoreCase(this
													.getDbType())){
												strDate = DateUtils
														.toString(
																date,
																DateUtils.ORA_DATES_FORMAT);
												insertValues.append("to_date('"
														+ strDate
														+ "','yyyy-mm-dd'),");
											}else if("db2"
													.equalsIgnoreCase(this
															.getDbType())){
												strDate = DateUtils
														.toString(
																date,
																DateUtils.ORA_DATE_TIMES_FORMAT);
												insertValues
														.append("TIMESTAMP('"
																+ strDate
																+ "'),");
											}
										}
										// 执行保存操作
										dataDealService
												.saveRptData(
														tableId,
														insertColumns
																.toString()
																.substring(
																		0,
																		insertColumns
																				.length() - 1),
														insertValues
																.toString()
																.substring(
																		0,
																		insertValues
																				.length() - 1));
										// 添加报文修改记录
										dataDealService.saveRptData(
												"t_rpt_log_info",
												insertLogColumns.toString(),
												insertLogValues.toString());
										// 导入子表数据
										this.importSubjectRpt(tableId, rptData,
												createdBusinessId);
									}
								}else{
									sbMessage.append("申报号[").append(rptNo)
											.append("]已存在于系统中！");
								}
							}
						}
					}
				}
			}
			logManagerService.writeLog(request, currentUser, "0004.0004",
					"报文管理.导入报文", "导入报文", description, "1");
			this.message = sbMessage.toString();
			this.request.setAttribute("message", this.message);
			return SUCCESS;
		}catch (Exception e){
			logManagerService.writeLog(request, currentUser, "0004.0004",
					"报文管理.导入报文", "导入报文", description, "0");
			e.printStackTrace();
			this.message = sbMessage.toString();
			this.request.setAttribute("message", this.message);
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: importSubjectRpt|描述: 导入报文子表数据 </p>
	 * @param tableId 主表表名
	 * @param rptData 导入报文信息
	 * @param businessId 主表业务ID
	 */
	private void importSubjectRpt(String tableId, RptData rptData,
			String businessId){
		if(tableId != null && !"".equals(tableId) && rptData != null
				&& businessId != null && !"".equals(businessId)){
			StringBuffer insertColumns = null;
			StringBuffer insertValues = null;
			String serverTime = DateUtils.serverCurrentTimeStamp();
			// 根据主表表名，判断导入何种子表
			if("t_fini_export".equals(tableId)
					|| "t_fini_dom_export".equals(tableId)){
				// 核销信息 出口收汇核销专用联（境外收入） / 出口收汇核销专用联（境内收入）
				// 出口收汇核销单号码 t_export_info
				List listExportInfo = rptData.getListExportInfo();
				if(listExportInfo != null && listExportInfo.size() > 0){
					for(int i = 0; i < listExportInfo.size(); i++){
						ExportInfo exportInfo = (ExportInfo) listExportInfo
								.get(i);
						if(exportInfo != null){
							String subId = this.createSubId(serverTime, i);
							insertColumns = new StringBuffer();
							insertValues = new StringBuffer();
							insertColumns.append(" REFNO, BUSINESSID, SUBID ");
							insertValues.append(" '").append(
									exportInfo.getRefno()).append("','")
									.append(businessId).append("','").append(
											subId).append("' ");
							// 执行保存操作
							dataDealService.saveRptData("T_EXPORT_INFO",
									insertColumns.toString(), insertValues
											.toString());
						}
					}
				}
			}else if("t_fini_remit".equals(tableId)
					|| "t_fini_dom_remit".equals(tableId)){
				// 核销信息 境外汇款申请书 / 境内汇款申请书
				// 报关单信息 t_customs_decl
				List listCustomDeclare = rptData.getListCustomDeclare();
				if(listCustomDeclare != null && listCustomDeclare.size() > 0){
					for(int i = 0; i < listCustomDeclare.size(); i++){
						CustomDeclare custDecl = (CustomDeclare) listCustomDeclare
								.get(i);
						if(custDecl != null){
							String subId = this.createSubId(serverTime, i);
							insertColumns = new StringBuffer();
							insertValues = new StringBuffer();
							insertColumns
									.append(" CUSTOMN, CUSTCCY, CUSTAMT, OFFAMT, BUSINESSID, SUBID ");
							insertValues.append(" '").append(
									custDecl.getCustomn()).append("','")
									.append(custDecl.getCustccy()).append("',")
									.append(custDecl.getCustamt()).append(",")
									.append(custDecl.getOffamt()).append(",'")
									.append(businessId).append("','").append(
											subId).append("' ");
							// 执行保存操作
							dataDealService.saveRptData("T_CUSTOMS_DECL",
									insertColumns.toString(), insertValues
											.toString());
						}
					}
				}
			}else if("t_company_info".equals(tableId)){
				// 单位基本信息
				// 投资国别代码 t_invcountrycode_info
				List listInvcountrycodeInfo = rptData
						.getListInvcountrycodeInfo();
				if(listInvcountrycodeInfo != null
						&& listInvcountrycodeInfo.size() > 0){
					for(int i = 0; i < listInvcountrycodeInfo.size(); i++){
						InvcountrycodeInfo invCountry = (InvcountrycodeInfo) listInvcountrycodeInfo
								.get(i);
						if(invCountry != null){
							String subId = this.createSubId(serverTime, i);
							insertColumns = new StringBuffer();
							insertValues = new StringBuffer();
							insertColumns
									.append(" INVCOUNTRYCODE, BUSINESSID, SUBID ");
							insertValues.append(" '").append(
									invCountry.getInvcountrycode()).append(
									"','").append(businessId).append("','")
									.append(subId).append("' ");
							// 执行保存操作
							dataDealService.saveRptData(
									"T_INVCOUNTRYCODE_INFO", insertColumns
											.toString(), insertValues
											.toString());
						}
					}
				}
				// 开户信息 t_company_openinfo
				List listCompanyOpenInfo = rptData.getListCompanyOpenInfo();
				if(listCompanyOpenInfo != null
						&& listCompanyOpenInfo.size() > 0){
					for(int i = 0; i < listCompanyOpenInfo.size(); i++){
						CompanyOpenInfo openInfo = (CompanyOpenInfo) listCompanyOpenInfo
								.get(i);
						if(openInfo != null){
							String subId = this.createSubId(serverTime, i);
							insertColumns = new StringBuffer();
							insertValues = new StringBuffer();
							insertColumns
									.append(" BRANCHCODE, CONTACT, TEL, FAX, BUSINESSID, SUBID ");
							insertValues.append(" '").append(
									openInfo.getBranchcode()).append("','")
									.append(openInfo.getContact())
									.append("','").append(openInfo.getTel())
									.append("','").append(openInfo.getFax())
									.append("','").append(businessId).append(
											"','").append(subId).append("' ");
							// 执行保存操作
							dataDealService.saveRptData("T_COMPANY_OPENINFO",
									insertColumns.toString(), insertValues
											.toString());
						}
					}
				}
			}
		}
	}

	/**
	 * <p> 方法名称: getInstCodeFromRptNo|描述: 根据申报号，解析其机构代码 </p>
	 * @param rptNo 申报号码
	 * @return String 机构代码
	 */
	private String getInstCodeFromRptNo(String rptNo){
		String instCode = null;
		if(rptNo != null && rptNo.length() == 22){
			String strRptNo = rptNo.substring(0, 12);
			List orgConfigList = orgConfigeService.findOrgConfigs(null,
					strRptNo);
			if(orgConfigList != null && orgConfigList.size() > 0){
				com.cjit.gjsz.filem.model.t_org_config orgConfig = (com.cjit.gjsz.filem.model.t_org_config) orgConfigList
						.get(0);
				if(orgConfig != null){
					instCode = orgConfig.getOrg_Id();
				}
			}
		}else{
			User user = this.getCurrentUser();
			List orgList = user.getOrgs();
			if(orgList != null){
				Organization org = (Organization) orgList.get(0);
				if(org != null){
					instCode = org.getId();
				}
			}
		}
		return instCode;
	}

	public UploadModel getUploadModel(){
		return uploadModel;
	}

	public void setUploadModel(UploadModel uploadModel){
		this.uploadModel = uploadModel;
	}

	public void setDataDealService(DataDealService dataDealService){
		this.dataDealService = dataDealService;
	}

	public void setImportRptService(ImportRptService importRptService){
		this.importRptService = importRptService;
	}

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}

	public void setOrgConfigeService(OrgConfigeService orgConfigeService){
		this.orgConfigeService = orgConfigeService;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}
}