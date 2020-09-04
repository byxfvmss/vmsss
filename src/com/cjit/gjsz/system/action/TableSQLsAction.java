package com.cjit.gjsz.system.action;

import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.system.model.TableSQL;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.BussSystemService;

/**
 * 
 * @author Larry
 */
public class TableSQLsAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private BussSystemService bussSystemService;
	private List tableSQLList = null;
	private TableSQL tableSQL = new TableSQL();
	private String tableId = null;
	private String message = "";
	private String isSave;

	public String search() {
		try {
			User user = this.getCurrentUser();
			String userId = user.getId();
			//
			tableSQLList = bussSystemService.findTableSQLList(tableSQL, userId);
			//
			List tableInfoList = dataDealService.findRptTableInfo(
					new RptTableInfo(), userId);
			this.request.setAttribute("tableSQLList", tableSQLList);
			this.request.setAttribute("tableInfoList", tableInfoList);
			this.request.setAttribute("tableSQL", tableSQL);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String edit() {
		if ("1".equals(isSave)) {
			message = "保存成功！";
			this.request.setAttribute("message", message);
		}
		tableSQLList = bussSystemService.findTableSQLList(tableSQL, null);
		if (tableSQLList != null) {
			tableSQL = (TableSQL) tableSQLList.get(0);
			this.request.setAttribute("tableSQL", tableSQL);
		}
		return SUCCESS;
	}

	public String save() {
		try {
			if (StringUtil.isNotEmpty(this.tableSQL.getTableId())) {
				if (this.tableSQL.getInitSQL().trim().endsWith(";")
						|| this.tableSQL.getTraceSQL().trim().endsWith(";")) {
					String message = "保存失败，脚本不能使用分号‘;’结尾";
					this.request.setAttribute("message", message);
					return ERROR;
				}
				List tableSQLList = bussSystemService.findTableSQLList(
						this.tableSQL, null);
				if (tableSQLList != null) {
					// 修改
					TableSQL tableSQL = (TableSQL) tableSQLList.get(0);
					tableSQL.setInitSQL(this.tableSQL.getInitSQL());
					tableSQL.setTraceSQL(this.tableSQL.getTraceSQL());
					if ("on".equals(this.tableSQL.getIsSingleSummary())) {
						tableSQL.setIsSingleSummary("1");
					} else {
						tableSQL.setIsSingleSummary("0");
					}
					tableSQL.setSummaryColumns(this.tableSQL
							.getSummaryColumns());
					bussSystemService.saveTableSQL(tableSQL, true);
				} else {
					// 添加
					TableSQL tableSQL = new TableSQL();
					tableSQL.setTableId(this.tableSQL.getTableId());
					tableSQL.setFileType(this.tableSQL.getFileType());
					tableSQL.setInitSQL(this.tableSQL.getInitSQL());
					tableSQL.setTraceSQL(this.tableSQL.getTraceSQL());
					if ("on".equals(this.tableSQL.getIsSingleSummary())) {
						tableSQL.setIsSingleSummary("1");
					} else {
						tableSQL.setIsSingleSummary("0");
					}
					tableSQL.setSummaryColumns(this.tableSQL
							.getSummaryColumns());
					bussSystemService.saveTableSQL(tableSQL, false);
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TableSQLsAction-save", e);
		}
		return ERROR;
	}

	public BussSystemService getBussSystemService() {
		return bussSystemService;
	}

	public void setBussSystemService(BussSystemService bussSystemService) {
		this.bussSystemService = bussSystemService;
	}

	public List getTableSQLList() {
		return tableSQLList;
	}

	public void setTableSQLList(List tableSQLList) {
		this.tableSQLList = tableSQLList;
	}

	public TableSQL getTableSQL() {
		return tableSQL;
	}

	public void setTableSQL(TableSQL tableSQL) {
		this.tableSQL = tableSQL;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

}
