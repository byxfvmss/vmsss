package com.cjit.gjsz.logic.model;

public class FalReportEntity {

	// 报文属性
	private String actiontype;// 操作类型
	private String actiondesc;// 删除原因
	private String snocode;// 数据自编码
	private String objcode;// 填报机构代码
	private String buocmonth;// 报告期
	private String exdebtcode;// 外债编号
	private String remark;// 备注
	// 系统属性
	private String filetype;// 接口文件类型
	private String instcode;// 机构编号
	private String auditname;// 审核人
	private String auditdate;// 审核日期
	private String importdate;// 录入日期
	private int datastatus;// 数据状态
	private String businessid;// 业务ID
	private String modifyuser;// 维护人
	private String subid;// 子表业务ID
	private String businessno;// 业务编号
	// 辅助属性
	private String tableId;// 表名

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getActiondesc() {
		return actiondesc;
	}

	public void setActiondesc(String actiondesc) {
		this.actiondesc = actiondesc;
	}

	public String getSnocode() {
		return snocode;
	}

	public void setSnocode(String snocode) {
		this.snocode = snocode;
	}

	public String getObjcode() {
		return objcode;
	}

	public void setObjcode(String objcode) {
		this.objcode = objcode;
	}

	public String getBuocmonth() {
		return buocmonth;
	}

	public void setBuocmonth(String buocmonth) {
		this.buocmonth = buocmonth;
	}

	public String getExdebtcode() {
		return exdebtcode;
	}

	public void setExdebtcode(String exdebtcode) {
		this.exdebtcode = exdebtcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

	public String getAuditname() {
		return auditname;
	}

	public void setAuditname(String auditname) {
		this.auditname = auditname;
	}

	public String getAuditdate() {
		return auditdate;
	}

	public void setAuditdate(String auditdate) {
		this.auditdate = auditdate;
	}

	public String getImportdate() {
		return importdate;
	}

	public void setImportdate(String importdate) {
		this.importdate = importdate;
	}

	public int getDatastatus() {
		return datastatus;
	}

	public void setDatastatus(int datastatus) {
		this.datastatus = datastatus;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	public String getModifyuser() {
		return modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}

	public String getBusinessno() {
		return businessno;
	}

	public void setBusinessno(String businessno) {
		this.businessno = businessno;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

}
