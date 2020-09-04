package com.cjit.gjsz.logic.model;

public class SelfEntity{

	private String actiontype;// 操作类型
	private String actiondesc;// 删除原因
	private String remark;// 备注
	private String filetype;// 接口文件类型
	private String instcode;// 机构编号
	private String auditname;// 审核人
	private String auditdate;// 审核日期
	private String importdate;// 录入日期
	private int datastatus;// 数据状态
	private String businessid;// 业务ID
	private String modifyuser;// 维护人
	private String businessno;// (银行核心)业务编号
	private String tradedate;// 交易日期

	public String getActiontype(){
		return actiontype;
	}

	public void setActiontype(String actiontype){
		this.actiontype = actiontype;
	}

	public String getActiondesc(){
		return actiondesc;
	}

	public void setActiondesc(String actiondesc){
		this.actiondesc = actiondesc;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getFiletype(){
		return filetype;
	}

	public void setFiletype(String filetype){
		this.filetype = filetype;
	}

	public String getInstcode(){
		return instcode;
	}

	public void setInstcode(String instcode){
		this.instcode = instcode;
	}

	public String getAuditname(){
		return auditname;
	}

	public void setAuditname(String auditname){
		this.auditname = auditname;
	}

	public String getAuditdate(){
		return auditdate;
	}

	public void setAuditdate(String auditdate){
		this.auditdate = auditdate;
	}

	public String getImportdate(){
		return importdate;
	}

	public void setImportdate(String importdate){
		this.importdate = importdate;
	}

	public int getDatastatus(){
		return datastatus;
	}

	public void setDatastatus(int datastatus){
		this.datastatus = datastatus;
	}

	public String getBusinessid(){
		return businessid;
	}

	public void setBusinessid(String businessid){
		this.businessid = businessid;
	}

	public String getModifyuser(){
		return modifyuser;
	}

	public void setModifyuser(String modifyuser){
		this.modifyuser = modifyuser;
	}

	public String getBusinessno(){
		return businessno;
	}

	public void setBusinessno(String businessno){
		this.businessno = businessno;
	}

	public String getTradedate(){
		return tradedate;
	}

	public void setTradedate(String tradedate){
		this.tradedate = tradedate;
	}
}
