/**
 * 
 */
package com.cjit.gjsz.datadeal.model;

import java.util.Date;
import java.util.List;

import com.cjit.common.util.DateUtils;

/**
 * @author yulubin
 */
public class RptData {

	// 非物理字段属性
	private String jcTableId;
	private String relaTableId;
	private String tableId;
	private String sbHxStatus;
	private boolean isHaveSendCommit = false;
	private String notDataStatus; // 被排除的数据状态
	private String notBusinessId; // 被排除的业务ID
	private String busiDataType;
	private String infoType;
	private String fileType;
	private String busiName;
	private String rptNoColumnId;
	private String account;// 外汇账户账号
	private String currenceCode;// 账户币种
	private String dealDate;// 发生日期
	private String branchCode;
	private String branchName;
	private String byeRptNoColumnId;
	private String byeRptNo;
	private String isHandiwork;// 是否手工录入
	private String fileTypeDesc;
	private String buocMonthFrom;// 报告期
	private String buocMonthTo;// 报告期

	public String getNotBusinessId() {
		return notBusinessId;
	}

	public void setNotBusinessId(String notBusinessId) {
		this.notBusinessId = notBusinessId;
	}

	public String getNotDataStatus() {
		return notDataStatus;
	}

	public void setNotDataStatus(String notDataStatus) {
		this.notDataStatus = notDataStatus;
	}

	public boolean getIsHaveSendCommit() {
		return isHaveSendCommit;
	}

	public void setIsHaveSendCommit(boolean isHaveSendCommit) {
		this.isHaveSendCommit = isHaveSendCommit;
	}

	// 物理字段属性
	private String businessId;
	private String subId;
	private String instCode;
	private String dataStatus;
	private String rptNo;
	private String actionType;
	private String actionDesc;
	private String cusType;
	private String auditName;
	private String auditDate;
	private String importDate;
	private String batchNo;
	private String isRef;
	private String isCommit;
	private String businessNo;
	// 条件属性
	private String columns;
	private String relaDataStatus;
	private String dataStatusCondition;
	private String updateSql;
	private String modifyUser;// 报表修改人
	private String columnId;
	private String updateCondition;// 修改条件属性
	private String[] instCodes;
	private String cFileType;// 外债变动、余额信息对应签约信息文件类型
	private String cBusinessId;// 外债变动、余额信息对应签约信息业务ID
	private String cDebtorCode;// 外债变动、余额信息对应签约信息债务人代码
	private String cContractAmount;// 外债变动、余额信息对应签约信息签约金额
	private String cValueDate;// 外债变动、余额信息对应签约信息起息日
	private String cDebtorName; // CA/DA/EA签约信息债务人中文名称
	private String cCredConAmount; // DA/EA签约信息贷款签约金额
	private String cTeamId;// 对应签约信息业务团队
	private String cTeamIdName;// 对应签约信息业务团队名称
	private String searchLowerOrg;// 是否查询下级机构
	private String userId;// 当前用户ID
	private String fileTypeFor;
	private String joinTable;// 查询时需关联的表
	private String configRptNo; // 机构所配置的申报号码
	private String errorDesc;// 反馈错误原因
	private String linkBussType;// 是否关联业务类型

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	// 排序字段
	private String orderColumn;
	// 排序方式
	private String orderDirection;
	// 综合查询动态条件属性
	private String searchCondition;
	// 其它
	private String instName;
	private String financeHiden;
	private String custcode;
	private String companyBusinessId; // 基础信息对应单位基本信息表业务ID
	private String verifyResult;
	private boolean canNext = true; // 是否可点击下一步按钮
	// 数据状态补充描述
	private String dataStatusDesc = "";
	private String reasionInfo;
	private String c1;
	private String c2;
	private String c3;
	private String c4;
	private String c5;
	private String c6;
	private String c7;
	private String c8;
	private String c9;
	private String c10;
	private String c11;
	private String c12;
	private String c13;
	private String c14;
	private String c15;
	private String c16;
	private String c17;
	private String c18;
	private String c19;
	private String c20;
	private String c21;
	private String c22;
	private String c23;
	private String c24;
	private String c25;
	private String c26;
	private String c27;
	private String c28;
	private String c29;
	private String c30;
	private String c31;
	private String c32;
	private String c33;
	private String c34;
	private String c35;
	private String c36;
	private String c37;
	private String c38;
	private String c39;
	private String c40;
	private String c41;
	private String c42;
	private String c43;
	private String c44;
	private String c45;
	private String c46;
	private String c47;
	private String c48;
	private String c49;
	private String c50;
	private String c51;
	private String c52;
	private String c53;
	private String c54;
	private String c55;
	private String c56;
	private String c57;
	private String c58;
	private String c59;
	private String c60;
	private String c61;
	private String c62;
	private String c63;
	private String c64;
	private String c65;
	private String c66;
	private String c67;
	private String c68;
	private String c69;
	private String c70;
	// 
	private String rptMethod;
	//
	private String leftJoin;
	//
	private boolean canEdit = true;
	// DFHL:增加开始和结束日期开始
	private String beginDate;// 审核起止时间
	private String endDate;
	private int countRow;

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

	// DFHL:增加开始和结束日期结束
	// 子表信息 Begin
	// 开户信息 t_company_openinfo
	private List listCompanyOpenInfo = null;
	// 投资国别代码 t_invcountrycode_info
	private List listInvcountrycodeInfo = null;
	// 报关单信息 t_customs_decl
	private List listCustomDeclare = null;
	// 出口收汇核销单号码 t_export_info
	private List listExportInfo = null;

	public List getListCompanyOpenInfo() {
		return listCompanyOpenInfo;
	}

	public void setListCompanyOpenInfo(List listCompanyOpenInfo) {
		this.listCompanyOpenInfo = listCompanyOpenInfo;
	}

	public List getListInvcountrycodeInfo() {
		return listInvcountrycodeInfo;
	}

	public void setListInvcountrycodeInfo(List listInvcountrycodeInfo) {
		this.listInvcountrycodeInfo = listInvcountrycodeInfo;
	}

	public List getListCustomDeclare() {
		return listCustomDeclare;
	}

	public void setListCustomDeclare(List listCustomDeclare) {
		this.listCustomDeclare = listCustomDeclare;
	}

	public List getListExportInfo() {
		return listExportInfo;
	}

	public void setListExportInfo(List listExportInfo) {
		this.listExportInfo = listExportInfo;
	}

	// 子表信息 End
	// XT增加对于交易日期的查询范围
	private String tradeDateFrom = "";
	private String tradeDateTo = "";

	public String getTradeDateFrom() {
		return tradeDateFrom == null ? "" : tradeDateFrom.replaceAll("-", "");
	}

	public void setTradeDateFrom(String tradeDateFrom) {
		this.tradeDateFrom = tradeDateFrom;
	}

	public String getTradeDateTo() {
		return tradeDateTo == null ? "" : tradeDateTo.replaceAll("-", "");
	}

	public void setTradeDateTo(String tradeDateTo) {
		this.tradeDateTo = tradeDateTo;
	}

	// XT增加对于交易日期的查询范围
	public RptData() {
	}

	/**
	 * findRptData时用此方法快速构造
	 * 
	 * @param tableId
	 * @param columns
	 * @param instCode
	 * @param dataStatusCondition
	 * @param businessId
	 */
	public RptData(String tableId, String columns, String instCode,
			String dataStatusCondition, String businessId, String orderColumn,
			String orderDirection) {
		this.tableId = tableId;
		this.columns = columns;
		this.instCode = instCode;
		this.dataStatusCondition = dataStatusCondition;
		this.businessId = businessId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
	}

	/**
	 * findRptData时用此方法快速构造 zhaoqian 增加一个综合查询的条件，其它同上
	 * 
	 * @param tableId
	 * @param columns
	 * @param instCode
	 * @param dataStatusCondition
	 * @param businessId
	 */
	public RptData(String tableId, String columns, String instCode,
			String dataStatusCondition, String businessId, String orderColumn,
			String orderDirection, String searchCondition) {
		this.tableId = tableId;
		this.columns = columns;
		this.instCode = instCode;
		this.dataStatusCondition = dataStatusCondition;
		this.businessId = businessId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
		this.searchCondition = searchCondition;
	}

	/**
	 * findRelaRptData时用此方法快速构造
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatusCondition
	 * @param businessId
	 */
	public RptData(String relaTableId, String tableId, String columns,
			String instCode, String relaDataStatus, String dataStatusCondition,
			String businessId, String orderColumn, String orderDirection) {
		this.relaTableId = relaTableId;
		this.tableId = tableId;
		this.columns = columns;
		this.instCode = instCode;
		this.relaDataStatus = relaDataStatus;
		this.dataStatusCondition = dataStatusCondition;
		this.businessId = businessId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
	}

	/**
	 * findRelaRptDataNew时用此方法快速构造
	 * 
	 * @param jcTableId
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatusCondition
	 * @param businessId
	 * @param orderColumn
	 * @param orderDirection
	 * @param flag
	 */
	public RptData(String jcTableId, String relaTableId, String tableId,
			String columns, String instCode, String relaDataStatus,
			String dataStatusCondition, String businessId, String orderColumn,
			String orderDirection, boolean flag) {
		this.jcTableId = jcTableId;
		this.relaTableId = relaTableId;
		this.tableId = tableId;
		this.columns = columns;
		this.instCode = instCode;
		this.relaDataStatus = relaDataStatus;
		this.dataStatusCondition = dataStatusCondition;
		this.businessId = businessId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
	}

	/**
	 * findRelaRptData时用此方法快速构造(综合查询)
	 * 
	 * @param relaTableId
	 * @param tableId
	 * @param columns
	 * @param instCode
	 * @param relaDataStatus
	 * @param dataStatusCondition
	 * @param businessId
	 */
	public RptData(String relaTableId, String tableId, String columns,
			String instCode, String relaDataStatus, String dataStatusCondition,
			String businessId, String orderColumn, String orderDirection,
			String searchCondition) {
		this.relaTableId = relaTableId;
		this.tableId = tableId;
		this.columns = columns;
		this.instCode = instCode;
		this.relaDataStatus = relaDataStatus;
		this.dataStatusCondition = dataStatusCondition;
		this.businessId = businessId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
		this.searchCondition = searchCondition;
	}

	/**
	 * findInnerRptData时调用此方法快速构造
	 * 
	 * @param tableId
	 * @param columns
	 * @param businessId
	 * @param subId
	 */
	public RptData(String tableId, String columns, String businessId,
			String subId, String orderColumn, String orderDirection) {
		this.tableId = tableId;
		this.columns = columns;
		this.businessId = businessId;
		this.subId = subId;
		this.orderColumn = orderColumn;
		this.orderDirection = orderDirection;
	}

	/**
	 * update时用此方法快速构造
	 * 
	 * @param tableId
	 * @param updateSql
	 * @param businessIdOrInstCode
	 * @param subId
	 * @param dataStatus
	 * @param updateFlag
	 */
	public RptData(String tableId, String updateSql,
			String businessIdOrInstCode, String subId, String dataStatus,
			boolean businessIdOrInstCodeFlag) {
		this.tableId = tableId;
		this.updateSql = updateSql;
		if (businessIdOrInstCodeFlag) {
			this.businessId = businessIdOrInstCode;
		} else {
			this.instCode = businessIdOrInstCode;
		}
		this.subId = subId;
		this.dataStatus = dataStatus;
	}

	public RptData(String tableId, String updateSql, String businessId,
			String subId) {
		this.tableId = tableId;
		this.updateSql = updateSql;
		this.businessId = businessId;
		this.subId = subId;
	}

	public RptData(String tableId, String updateSql,
			String businessIdOrInstCode, String subId, String dataStatus,
			boolean businessIdOrInstCodeFlag, String reasionInfo) {
		this.tableId = tableId;
		this.updateSql = updateSql;
		if (businessIdOrInstCodeFlag) {
			this.businessId = businessIdOrInstCode;
		} else {
			this.instCode = businessIdOrInstCode;
		}
		this.subId = subId;
		this.dataStatus = dataStatus;
		this.reasionInfo = reasionInfo;
	}

	public RptData(String tableId, String rptNo) {
		this.tableId = tableId;
		this.rptNo = rptNo;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getC4() {
		return c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}

	public String getC5() {
		return c5;
	}

	public void setC5(String c5) {
		this.c5 = c5;
	}

	public String getC6() {
		return c6;
	}

	public void setC6(String c6) {
		this.c6 = c6;
	}

	public String getC7() {
		return c7;
	}

	public void setC7(String c7) {
		this.c7 = c7;
	}

	public String getC8() {
		return c8;
	}

	public void setC8(String c8) {
		this.c8 = c8;
	}

	public String getC9() {
		return c9;
	}

	public void setC9(String c9) {
		this.c9 = c9;
	}

	public String getC10() {
		return c10;
	}

	public void setC10(String c10) {
		this.c10 = c10;
	}

	public String getC11() {
		return c11;
	}

	public void setC11(String c11) {
		this.c11 = c11;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getC12() {
		return c12;
	}

	public void setC12(String c12) {
		this.c12 = c12;
	}

	public String getC13() {
		return c13;
	}

	public void setC13(String c13) {
		this.c13 = c13;
	}

	public String getC14() {
		return c14;
	}

	public void setC14(String c14) {
		this.c14 = c14;
	}

	public String getC15() {
		return c15;
	}

	public void setC15(String c15) {
		this.c15 = c15;
	}

	public String getC16() {
		return c16;
	}

	public void setC16(String c16) {
		this.c16 = c16;
	}

	public String getC17() {
		return c17;
	}

	public void setC17(String c17) {
		this.c17 = c17;
	}

	public String getC18() {
		return c18;
	}

	public void setC18(String c18) {
		this.c18 = c18;
	}

	public String getC19() {
		return c19;
	}

	public void setC19(String c19) {
		this.c19 = c19;
	}

	public String getC20() {
		return c20;
	}

	public void setC20(String c20) {
		this.c20 = c20;
	}

	public String getC21() {
		return c21;
	}

	public void setC21(String c21) {
		this.c21 = c21;
	}

	public String getC22() {
		return c22;
	}

	public void setC22(String c22) {
		this.c22 = c22;
	}

	public String getC23() {
		return c23;
	}

	public void setC23(String c23) {
		this.c23 = c23;
	}

	public String getC24() {
		return c24;
	}

	public void setC24(String c24) {
		this.c24 = c24;
	}

	public String getC25() {
		return c25;
	}

	public void setC25(String c25) {
		this.c25 = c25;
	}

	public String getC26() {
		return c26;
	}

	public void setC26(String c26) {
		this.c26 = c26;
	}

	public String getC27() {
		return c27;
	}

	public void setC27(String c27) {
		this.c27 = c27;
	}

	public String getC28() {
		return c28;
	}

	public void setC28(String c28) {
		this.c28 = c28;
	}

	public String getC29() {
		return c29;
	}

	public void setC29(String c29) {
		this.c29 = c29;
	}

	public String getC30() {
		return c30;
	}

	public void setC30(String c30) {
		this.c30 = c30;
	}

	public String getRptNo() {
		return rptNo;
	}

	public void setRptNo(String rptNo) {
		this.rptNo = rptNo;
	}

	public String getC31() {
		return c31;
	}

	public void setC31(String c31) {
		this.c31 = c31;
	}

	public String getC32() {
		return c32;
	}

	public void setC32(String c32) {
		this.c32 = c32;
	}

	public String getC33() {
		return c33;
	}

	public void setC33(String c33) {
		this.c33 = c33;
	}

	public String getC34() {
		return c34;
	}

	public void setC34(String c34) {
		this.c34 = c34;
	}

	public String getC35() {
		return c35;
	}

	public void setC35(String c35) {
		this.c35 = c35;
	}

	public String getSbHxStatus() {
		return sbHxStatus;
	}

	public void setSbHxStatus(String sbHxStatus) {
		this.sbHxStatus = sbHxStatus;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	// public void setDataStatus(int dataStatus){
	// this.dataStatus = String.valueOf(dataStatus);
	// }

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}

	public String getRelaTableId() {
		return relaTableId;
	}

	public void setRelaTableId(String relaTableId) {
		this.relaTableId = relaTableId;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getRelaDataStatus() {
		return relaDataStatus;
	}

	public void setRelaDataStatus(String relaDataStatus) {
		this.relaDataStatus = relaDataStatus;
	}

	public String getDataStatusCondition() {
		return dataStatusCondition;
	}

	public void setDataStatusCondition(String dataStatusCondition) {
		this.dataStatusCondition = dataStatusCondition;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditDateStr() {
		if (this.auditDate != null) {
			Date tmpDate = DateUtils.stringToDate(this.auditDate,
					DateUtils.ORA_DATE_TIMES_FORMAT);
			return DateUtils.toString(tmpDate, DateUtils.ORA_DATES_FORMAT);
		}
		return "";
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getJcTableId() {
		return jcTableId;
	}

	public void setJcTableId(String jcTableId) {
		this.jcTableId = jcTableId;
	}

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	public String getFinanceHiden() {
		return financeHiden;
	}

	public void setFinanceHiden(String financeHiden) {
		this.financeHiden = financeHiden;
	}

	public String getCustcode() {
		return custcode;
	}

	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getReasionInfo() {
		return reasionInfo;
	}

	public void setReasionInfo(String reasionInfo) {
		this.reasionInfo = reasionInfo;
	}

	public String getC36() {
		return c36;
	}

	public void setC36(String c36) {
		this.c36 = c36;
	}

	public String getC37() {
		return c37;
	}

	public void setC37(String c37) {
		this.c37 = c37;
	}

	public String getC38() {
		return c38;
	}

	public void setC38(String c38) {
		this.c38 = c38;
	}

	public String getC39() {
		return c39;
	}

	public void setC39(String c39) {
		this.c39 = c39;
	}

	public String getC40() {
		return c40;
	}

	public void setC40(String c40) {
		this.c40 = c40;
	}

	public String getC41() {
		return c41;
	}

	public void setC41(String c41) {
		this.c41 = c41;
	}

	public String getC42() {
		return c42;
	}

	public void setC42(String c42) {
		this.c42 = c42;
	}

	public String getC43() {
		return c43;
	}

	public void setC43(String c43) {
		this.c43 = c43;
	}

	public String getC44() {
		return c44;
	}

	public void setC44(String c44) {
		this.c44 = c44;
	}

	public String getC45() {
		return c45;
	}

	public void setC45(String c45) {
		this.c45 = c45;
	}

	public String getRptMethod() {
		return rptMethod;
	}

	public void setRptMethod(String rptMethod) {
		this.rptMethod = rptMethod;
	}

	public String getLeftJoin() {
		return leftJoin;
	}

	public void setLeftJoin(String leftJoin) {
		this.leftJoin = leftJoin;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public String getDataStatusDesc() {
		return dataStatusDesc;
	}

	public void setDataStatusDesc(String dataStatusDesc) {
		this.dataStatusDesc = dataStatusDesc;
	}

	public String getIsRef() {
		return isRef;
	}

	public void setIsRef(String isRef) {
		this.isRef = isRef;
	}

	public String getCompanyBusinessId() {
		return companyBusinessId;
	}

	public void setCompanyBusinessId(String companyBusinessId) {
		this.companyBusinessId = companyBusinessId;
	}

	public boolean isCanNext() {
		return canNext;
	}

	public void setCanNext(boolean canNext) {
		this.canNext = canNext;
	}

	public String getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(String isCommit) {
		this.isCommit = isCommit;
	}

	public String getC46() {
		return c46;
	}

	public void setC46(String c46) {
		this.c46 = c46;
	}

	public String getC47() {
		return c47;
	}

	public void setC47(String c47) {
		this.c47 = c47;
	}

	public String getC48() {
		return c48;
	}

	public void setC48(String c48) {
		this.c48 = c48;
	}

	public String getBusiDataType() {
		return busiDataType;
	}

	public void setBusiDataType(String busiDataType) {
		this.busiDataType = busiDataType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getBusiName() {
		return busiName;
	}

	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}

	public String getRptNoColumnId() {
		return rptNoColumnId;
	}

	public void setRptNoColumnId(String rptNoColumnId) {
		this.rptNoColumnId = rptNoColumnId;
	}

	public String getByeRptNoColumnId() {
		return byeRptNoColumnId;
	}

	public void setByeRptNoColumnId(String byeRptNoColumnId) {
		this.byeRptNoColumnId = byeRptNoColumnId;
	}

	public String getByeRptNo() {
		return byeRptNo;
	}

	public void setByeRptNo(String byeRptNo) {
		this.byeRptNo = byeRptNo;
	}

	public String getC49() {
		return c49;
	}

	public void setC49(String c49) {
		this.c49 = c49;
	}

	public String getC50() {
		return c50;
	}

	public void setC50(String c50) {
		this.c50 = c50;
	}

	public String getC51() {
		return c51;
	}

	public void setC51(String c51) {
		this.c51 = c51;
	}

	public String getC52() {
		return c52;
	}

	public void setC52(String c52) {
		this.c52 = c52;
	}

	public String getC53() {
		return c53;
	}

	public void setC53(String c53) {
		this.c53 = c53;
	}

	public String getC54() {
		return c54;
	}

	public void setC54(String c54) {
		this.c54 = c54;
	}

	public String getC55() {
		return c55;
	}

	public void setC55(String c55) {
		this.c55 = c55;
	}

	public String getC56() {
		return c56;
	}

	public void setC56(String c56) {
		this.c56 = c56;
	}

	public String getC57() {
		return c57;
	}

	public void setC57(String c57) {
		this.c57 = c57;
	}

	public String getC58() {
		return c58;
	}

	public void setC58(String c58) {
		this.c58 = c58;
	}

	public String getC59() {
		return c59;
	}

	public void setC59(String c59) {
		this.c59 = c59;
	}

	public String getC60() {
		return c60;
	}

	public void setC60(String c60) {
		this.c60 = c60;
	}

	public String getC61() {
		return c61;
	}

	public void setC61(String c61) {
		this.c61 = c61;
	}

	public String getC62() {
		return c62;
	}

	public void setC62(String c62) {
		this.c62 = c62;
	}

	public String getC63() {
		return c63;
	}

	public void setC63(String c63) {
		this.c63 = c63;
	}

	public String getC64() {
		return c64;
	}

	public void setC64(String c64) {
		this.c64 = c64;
	}

	public String getC65() {
		return c65;
	}

	public void setC65(String c65) {
		this.c65 = c65;
	}

	public String getC66() {
		return c66;
	}

	public void setC66(String c66) {
		this.c66 = c66;
	}

	public String getC67() {
		return c67;
	}

	public void setC67(String c67) {
		this.c67 = c67;
	}

	public String getC68() {
		return c68;
	}

	public void setC68(String c68) {
		this.c68 = c68;
	}

	public String getC69() {
		return c69;
	}

	public void setC69(String c69) {
		this.c69 = c69;
	}

	public String getC70() {
		return c70;
	}

	public void setC70(String c70) {
		this.c70 = c70;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getUpdateCondition() {
		return updateCondition;
	}

	public void setUpdateCondition(String updateCondition) {
		this.updateCondition = updateCondition;
	}

	public String getBusinessNo() {
		return businessNo == null ? "" : businessNo.trim();
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getBranchCode() {
		return branchCode == null ? "" : branchCode.trim();
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAccount() {
		return account == null ? "" : account.trim();
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCurrenceCode() {
		return currenceCode == null ? "" : currenceCode.trim();
	}

	public void setCurrenceCode(String currenceCode) {
		this.currenceCode = currenceCode;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getIsHandiwork() {
		return isHandiwork;
	}

	public void setIsHandiwork(String isHandiwork) {
		this.isHandiwork = isHandiwork;
	}

	public String getFileTypeDesc() {
		return fileTypeDesc == null ? "" : fileTypeDesc.trim();
	}

	public void setFileTypeDesc(String fileTypeDesc) {
		this.fileTypeDesc = fileTypeDesc;
	}

	public String[] getInstCodes() {
		return instCodes;
	}

	public void setInstCodes(String[] instCodes) {
		this.instCodes = instCodes;
	}

	public int getCountRow() {
		return countRow;
	}

	public void setCountRow(int countRow) {
		this.countRow = countRow;
	}

	public String getCFileType() {
		return cFileType == null ? "" : cFileType;
	}

	public void setCFileType(String fileType) {
		cFileType = fileType;
	}

	public String getCBusinessId() {
		return cBusinessId;
	}

	public void setCBusinessId(String businessId) {
		cBusinessId = businessId;
	}

	public String getSearchLowerOrg() {
		return searchLowerOrg;
	}

	public void setSearchLowerOrg(String searchLowerOrg) {
		this.searchLowerOrg = searchLowerOrg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileTypeFor() {
		return fileTypeFor;
	}

	public void setFileTypeFor(String fileTypeFor) {
		this.fileTypeFor = fileTypeFor;
	}

	public String getJoinTable() {
		return joinTable;
	}

	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}

	public String getCDebtorCode() {
		return cDebtorCode == null ? "" : cDebtorCode;
	}

	public void setCDebtorCode(String debtorCode) {
		cDebtorCode = debtorCode;
	}

	public String getCContractAmount() {
		return cContractAmount == null ? "" : cContractAmount;
	}

	public void setCContractAmount(String contractAmount) {
		cContractAmount = contractAmount;
	}

	public String getCValueDate() {
		return cValueDate == null ? "" : cValueDate;
	}

	public void setCValueDate(String valueDate) {
		cValueDate = valueDate;
	}

	public String getConfigRptNo() {
		return configRptNo;
	}

	public void setConfigRptNo(String configRptNo) {
		this.configRptNo = configRptNo;
	}

	public String getCDebtorName() {
		return cDebtorName == null ? "" : cDebtorName;
	}

	public void setCDebtorName(String debtorName) {
		cDebtorName = debtorName;
	}

	public String getCCredConAmount() {
		return cCredConAmount == null ? "" : cCredConAmount;
	}

	public void setCCredConAmount(String credConAmount) {
		cCredConAmount = credConAmount;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getLinkBussType() {
		return linkBussType;
	}

	public void setLinkBussType(String linkBussType) {
		this.linkBussType = linkBussType;
	}

	public String getCTeamId() {
		return cTeamId;
	}

	public void setCTeamId(String teamId) {
		cTeamId = teamId;
	}

	public String getCTeamIdName() {
		return cTeamIdName;
	}

	public void setCTeamIdName(String teamIdName) {
		cTeamIdName = teamIdName;
	}

	public String getBuocMonthFrom() {
		return buocMonthFrom;
	}

	public void setBuocMonthFrom(String buocMonthFrom) {
		if (buocMonthFrom != null) {
			if (buocMonthFrom.indexOf("-") > 0) {
				buocMonthFrom = buocMonthFrom.replaceAll("-", "");
			}
			if (buocMonthFrom.length() == 8) {
				buocMonthFrom = buocMonthFrom.substring(0, 6);
			}
		}
		this.buocMonthFrom = buocMonthFrom;
	}

	public String getBuocMonthTo() {
		return buocMonthTo;
	}

	public void setBuocMonthTo(String buocMonthTo) {
		if (buocMonthTo != null) {
			if (buocMonthTo.indexOf("-") > 0) {
				buocMonthTo = buocMonthTo.replaceAll("-", "");
			}
			if (buocMonthTo.length() == 8) {
				buocMonthTo = buocMonthTo.substring(0, 6);
			}
		}
		this.buocMonthTo = buocMonthTo;
	}
}