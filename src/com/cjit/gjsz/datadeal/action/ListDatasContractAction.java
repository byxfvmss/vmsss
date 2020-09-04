package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.List;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.dataserch.model.QueryCondition;
import com.cjit.gjsz.system.model.User;

public class ListDatasContractAction extends ListDatasAction{

	private static final long serialVersionUID = 1L;
	private String businessNo = null;

	/**
	 * 外部表数据列表显示入口
	 * @return
	 */
	public String listDatas(){
		if(!sessionInit(true)){
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		return listDatas(infoType, tableId);
	}

	public String listDatas(String infoType, String tableId){
		try{
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if(currentUser != null){
				userId = currentUser.getId();
			}
			dataStatusList = new ArrayList();
			SelectTag s6 = new SelectTag(String
					.valueOf(DataUtil.SHYTG_STATUS_NUM),
					DataUtil.SHYTG_STATUS_CH);
			dataStatusList.add(s6);
			SelectTag s7 = new SelectTag(String
					.valueOf(DataUtil.YSC_STATUS_NUM), DataUtil.YSC_STATUS_CH);
			dataStatusList.add(s7);
			SelectTag s8 = new SelectTag(String
					.valueOf(DataUtil.YBS_STATUS_NUM), DataUtil.YBS_STATUS_CH);
			dataStatusList.add(s8);
			SelectTag s9 = new SelectTag(String
					.valueOf(DataUtil.LOCKED_STATUS_NUM),
					DataUtil.LOCKED_STATUS_CH);
			dataStatusList.add(s9);
			//
			String fileTypeFor = (String) this.request
					.getParameter("fileTypeFor");
			// 从页面获取 数据采集范围和业务类型
			this.busiDataType = (String) this.request
					.getParameter("busiDataType");
			// 根据信息类型和是否显示获取报表信息
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(this.busiDataType,
							"1", "1"));
			List tableInfoList = dataDealService.findRptTableInfo(
					new RptTableInfo(this.getInfoTypeName(this.infoType,
							busiDataInfoList), "1", "1"), userId);
			rptTableList.clear();
			for(int i = 0; i < tableInfoList.size(); i++){
				RptTableInfo tableInfo = (RptTableInfo) tableInfoList.get(i);
				if(tableInfo.getTableName().indexOf("签约") >= 0){
					if("A".equals(this.infoType)){
						if("AR".equals(fileTypeFor)){
							if(!"AL".equals(tableInfo.getFileType())
									&& !"AM".equals(tableInfo.getFileType())
									&& !"AN".equals(tableInfo.getFileType())
									&& !"AP".equals(tableInfo.getFileType())){
								rptTableList.add(tableInfo);
							}
						}else if("AS".equals(fileTypeFor)){
							if("AL".equals(tableInfo.getFileType())
									|| "AM".equals(tableInfo.getFileType())
									|| "AN".equals(tableInfo.getFileType())
									|| "AP".equals(tableInfo.getFileType())){
								rptTableList.add(tableInfo);
							}
						}
					}else{
						rptTableList.add(tableInfo);
					}
				}
			}
			this.rptColumnList = this.getListTitle(infoType);
			addCommonColumnListQuery();
			String columns = getColumnsSql(this.rptColumnList);
			// 查询是否提交为0,即未提交的信息 or 是否提交为空,即关联查询出、并未编辑保存的申报、核销
			// if(request.getParameter("checkfalg") != null
			// && request.getSession().getAttribute("paginationList") != null){
			// paginationList = (PaginationList) request.getSession()
			// .getAttribute("paginationList");
			// queryCondition = (QueryCondition) request.getSession()
			// .getAttribute("queryCondition");
			// }
			if(DataUtil.isJCDWSBHX(infoTypeCode)){
				if(orderColumn == null && orderDirection == null){
					orderColumn = (String) request.getSession().getAttribute(
							"orderColumn");
					orderDirection = (String) request.getSession()
							.getAttribute("orderDirection");
				}else{
					request.getSession().setAttribute("orderColumn",
							orderColumn);
					request.getSession().setAttribute("orderDirection",
							orderDirection);
				}
				orderColumnSub = null;
				orderDirectionSub = null;
			}
			String searchCondition = createSqlCondition(queryCondition, userId);
			RptData rptData = new RptData();
			rptData.setFileType(fileType);
			rptData.setInfoType(infoType);
			rptData.setColumns(columns);
			rptData.setOrderColumn(orderColumn);
			rptData.setOrderDirection(orderDirection);
			if(StringUtil.isNotEmpty(fileType)
					&& !"all".equalsIgnoreCase(fileType)
					&& DataUtil.getTableNameByFileType(fileType).indexOf("签约") > 0){
				searchCondition += " and t.fileType = '" + fileType + "' ";
			}
			if(StringUtil.isNotEmpty(dataStatus)){
				searchCondition += " and t.datastatus = " + dataStatus + " ";
			}
			if(StringUtil.isNotEmpty(businessNo)){
				searchCondition += " and t.businessNo like '%" + businessNo
						+ "%' ";
			}
			if(StringUtil.isNotEmpty(userId)){
				searchCondition += " and t.fileType in (select distinct fileType from t_rela_tables where objId in (select v.role_id from v_role_user v where v.user_id = '"
						+ userId + "'))";
			}
			// 外债信息
			if("A".equals(infoType)){
				rptData.setFileTypeFor(fileTypeFor);
				if("AR".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select EXDEBTCODE
					// from T_CFA_A_EXDEBT where fileType = 'AR' and datastatus
					// < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and EXDEBTCODE = t.EXDEBTCODE) or t.EXDEBTCODE is
					// null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_A_EXDEBT where fileType = 'AR' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and EXDEBTCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
					searchCondition += " and t.FILETYPE <> 'AL' and t.FILETYPE <> 'AM' and t.FILETYPE <> 'AN' and t.FILETYPE <> 'AP' and t.FILETYPE <> 'AR' and t.FILETYPE <> 'AS' ";
				}else if("AS".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select EXDEBTCODE
					// from T_CFA_A_EXDEBT where fileType = 'AS' and datastatus
					// <"
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and EXDEBTCODE = t.EXDEBTCODE) or t.EXDEBTCODE is
					// null)";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_A_EXDEBT where fileType = 'AS' and datastatus <"
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and EXDEBTCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null)";
					searchCondition += " and (t.FILETYPE = 'AL' or t.FILETYPE = 'AM' or t.FILETYPE = 'AN' or t.FILETYPE = 'AP') ";
				}
			}else if("B".equals(infoType)){
				if("BB".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select EXGUARANCODE
					// from T_CFA_B_EXGUARAN where fileType = 'BB' and
					// datastatus < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and EXGUARANCODE = t.EXGUARANCODE) or t.EXGUARANCODE
					// is null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_B_EXGUARAN where fileType = 'BB' and datastatus <"
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and EXGUARANCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null)";
				}else if("BC".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select EXGUARANCODE
					// from T_CFA_B_EXGUARAN where fileType = 'BC' and
					// datastatus < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and EXGUARANCODE = t.EXGUARANCODE) or t.EXGUARANCODE
					// is null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_B_EXGUARAN where fileType = 'BC' and datastatus <"
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and EXGUARANCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null)";
				}
			}else if("C".equals(infoType)){
				if("CB".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select DOFOEXLOCODE
					// from T_CFA_C_DOFOEXLO where fileType = 'CB' and
					// datastatus < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and DOFOEXLOCODE = t.DOFOEXLOCODE) or t.DOFOEXLOCODE
					// is null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_C_DOFOEXLO where fileType = 'CB' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and DOFOEXLOCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
				}
			}else if("D".equals(infoType)){
				if("DB".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select LOUNEXGUCODE
					// from T_CFA_D_LOUNEXGU where fileType = 'DB' and
					// datastatus < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and LOUNEXGUCODE = t.LOUNEXGUCODE) or t.LOUNEXGUCODE
					// is null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_D_LOUNEXGU where fileType = 'DB' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and LOUNEXGUCODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
				}
			}else if("E".equals(infoType)){
				if("EB".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select EXPLRMBLONO
					// from T_CFA_E_EXPLRMBLO where fileType = 'EB' and
					// datastatus < "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and EXPLRMBLONO = t.EXPLRMBLONO) or t.EXPLRMBLONO is
					// null) ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_E_EXPLRMBLO where fileType = 'EB' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and EXPLRMBLONO is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
				}
			}else if("F".equals(infoType)){
				if("FB".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select STRDECODE
					// from T_CFA_F_STRDE where fileType = 'FB' and datastatus <
					// "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and STRDECODE = t.STRDECODE) or t.STRDECODE is null)
					// ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_F_STRDE where fileType = 'FB' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and STRDECODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
				}else if("FC".equals(fileTypeFor)){
					// searchCondition += " and (not exists (select STRDECODE
					// from T_CFA_F_STRDE where fileType = 'FC' and datastatus <
					// "
					// + DataUtil.YTJDSH_STATUS_NUM
					// + " and datastatus > "
					// + DataUtil.DELETE_STATUS_NUM
					// + " and STRDECODE = t.STRDECODE) or t.STRDECODE is null)
					// ";
					searchCondition += " and (not exists (select BUSINESSNO from T_CFA_F_STRDE where fileType = 'FC' and datastatus < "
							+ DataUtil.YTJDSH_STATUS_NUM
							+ " and datastatus > "
							+ DataUtil.DELETE_STATUS_NUM
							+ " and STRDECODE is null and BUSINESSNO = t.BUSINESSNO) or t.BUSINESSNO is null) ";
				}
			}
			rptData.setSearchCondition(searchCondition);
			dataDealService.findRptCfaContract(rptData, paginationList);
			// 将部分字段值用字典表中的对应文字替换
			this.setSelectTagValue(paginationList.getRecordList(), tableId,
					rptColumnList);
			this.request.getSession().setAttribute("paginationList",
					paginationList);
			// 在wl8.1下放入request
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("businessNo", this.businessNo);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("paginationList", this.paginationList);
			this.request.setAttribute("busiDataType", this.busiDataType);
			this.request.setAttribute("infoType", this.infoType);
			this.request.setAttribute("fileType", this.fileType);
			this.request.setAttribute("fileTypeFor", fileTypeFor);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error("listDatas", e);
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("businessNo", this.businessNo);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			request.getSession().setAttribute("orderColumnSub",
					this.orderColumnSub);
			request.getSession().setAttribute("orderDirectionSub",
					this.orderDirectionSub);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		}
	}

	private String createSqlCondition(QueryCondition qc, String userId){
		String returnStr = "";
		StringBuffer sb = new StringBuffer();
		// 增加所有选项
		if(StringUtil.isEmpty(instCode)){
			// 所有
			if((authInstList != null) && (authInstList.size() > 0)){
				sb
						.append(
								" and t.instCode in (select fk_orgId from t_user_org where fk_userId = '")
						.append(userId).append("') ");
			}else{
				// 没有有权限的机构
				// sb.append(" and 1 = 0");
				return " and 1 = 0 ";
			}
		}else{
			sb.append(" and t.instCode = '").append(instCode).append("' ");
		}
		if(qc != null){
			if(!StringUtil.isEmpty(qc.getValueFirst())){
				// 先判断该列是否是字典项
				if(qc.getColumnIdFirst().startsWith("common.")){
					sb.append(getCommonSql(qc.getColumnIdFirst(), qc
							.getOpFirst(), qc.getValueFirst()));
				}else{
					if(!CONTRACTTYPE.equals(qc.getColumnIdFirst())){
						if(checkDic(qc.getColumnIdFirst())){
							sb.append(getDicSql(qc.getColumnIdFirst(), qc
									.getOpFirst(), qc.getValueFirst()));
						}else{
							sb.append(getNoDicSql(qc.getColumnIdFirst(), qc
									.getOpFirst(), qc.getValueFirst()));
						}
					}else{
						sb.append(getContractTypeSQL((String) operatorsMap
								.get(new Integer(qc.getOpFirst())), qc
								.getValueFirst()));
					}
				}
			}
		}
		if(sb.length() > 3){
			returnStr = sb.toString().replaceFirst("and", "");
		}
		return returnStr;
	}

	private List getListTitle(String infoType){
		List rptColumnList = new ArrayList();
		rptColumnList.add(new RptColumnInfo("ACTIONTYPE", "操作类型", "actionType",
				"ACTIONTYPE", "3"));
		if("A".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("EXDEBTCODE", "外债编号", "rptNo",
					null, null));
			rptColumnList.add(new RptColumnInfo("DEBTORCODE", "债务人代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("DEBTYPE", "债务类型", "c2",
					"DEBTYPE", "3"));
			rptColumnList.add(new RptColumnInfo("CONTRACTCURR", "签约币种", "c3",
					"CURRENCY", "3"));
		}else if("B".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("EXGUARANCODE", "对外担保编号",
					"rptNo", null, null));
			rptColumnList.add(new RptColumnInfo("GUARANTORCODE", "担保人代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("GUARANTYPE", "担保类型", "c2",
					"GUARANTYPE", "3"));
			rptColumnList.add(new RptColumnInfo("GUARANCURR", "保函币种", "c3",
					"CURRENCY", "3"));
		}else if("C".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("DOFOEXLOCODE", "国内外汇贷款编号",
					"rptNo", null, null));
			rptColumnList.add(new RptColumnInfo("CREDITORCODE", "债权人代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("DOFOEXLOTYPE", "国内外汇贷款类型",
					"c2", "DOFOEXLOTYPE", "3"));
			rptColumnList.add(new RptColumnInfo("CURRENCE", "贷款币种", "c3",
					"CURRENCY", "3"));
		}else if("D".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("LOUNEXGUCODE", "外保内贷编号",
					"rptNo", null, null));
			rptColumnList.add(new RptColumnInfo("CREDITORCODE", "债权人代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("CREDCURRCODE", "贷款币种", "c2",
					"CURRENCY", "3"));
			rptColumnList.add(new RptColumnInfo("CREDCONAMOUNT", "贷款签约金额",
					"c3", null, null));
		}else if("E".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("EXPLRMBLONO", "外汇质押人民币贷款编号",
					"rptNo", null, null));
			rptColumnList.add(new RptColumnInfo("CREDITORCODE", "债权人代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("CREDCONCURR", "贷款签约币种", "c2",
					"CURRENCY", "3"));
			rptColumnList.add(new RptColumnInfo("CREDCONAMOUNT", "贷款签约金额",
					"c3", null, null));
		}else if("F".equals(infoType)){
			rptColumnList.add(new RptColumnInfo("STRDECODE", "人民币结构性存款编号",
					"rptNo", null, null));
			rptColumnList.add(new RptColumnInfo("CLIENTCODE", "客户代码", "c1",
					null, null));
			rptColumnList.add(new RptColumnInfo("CONTRACT", "合同号", "c2", null,
					null));
			rptColumnList.add(new RptColumnInfo("CONTRACTAMOUNT", "签约金额", "c3",
					null, null));
		}
		rptColumnList.add(new RptColumnInfo("BUSINESSNO", "业务编号", "businessNo",
				null, null));
		return rptColumnList;
	}

	private String getColumnsSql(List rptColumnList){
		StringBuffer sbSql = new StringBuffer();
		if(rptColumnList != null){
			for(int i = 0; i < rptColumnList.size(); i++){
				RptColumnInfo columnInfo = (RptColumnInfo) rptColumnList.get(i);
				sbSql.append(", t.").append(columnInfo.getColumnId()).append(
						" as ").append(columnInfo.getAliasColumnId());
			}
		}
		return sbSql.toString();
	}

	private void addCommonColumnListQuery(){
		rptColumnListQuery = new ArrayList();
		String selectFileType = "";
		if(this.tableId == null && this.fileType == null
				&& this.rptTableList == null){
			return;
		}else{
			if("all".equals(this.fileType) && this.rptTableList != null
					&& this.rptTableList.size() > 0){
				RptTableInfo tableInfo = (RptTableInfo) rptTableList.get(0);
				selectFileType = tableInfo.getFileType();
			}else{
				selectFileType = this.fileType;
			}
		}
		List listColumn = dataDealService.findRptColumnInfo(new RptColumnInfo(
				tableId, null, "1", selectFileType));
		for(int i = 0; i < listColumn.size(); i++){
			rptColumnListQuery.add(listColumn.get(i));
		}
	}

	public String getBusinessNo(){
		return businessNo;
	}

	public void setBusinessNo(String businessNo){
		this.businessNo = businessNo;
	}
}
