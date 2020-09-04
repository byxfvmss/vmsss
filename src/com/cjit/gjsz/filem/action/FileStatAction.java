package com.cjit.gjsz.filem.action;

import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.system.model.User;

/**
 * 
 * @作者: lihaiboA
 * @日期: Aug 26, 2013 10:59:54 AM
 * @描述: [FileStatAction] 报文管理－报文反馈查询 功能操作类
 */
public class FileStatAction extends BaseListAction{

	private static final long serialVersionUID = -201308231528L;
	private ReceiveReportService receiveReportService;
	private OrgConfigeService orgconfigeservice;
	private String rptTitle;// 主报告行
	private String rptDate;// 报文生成日期
	private List rptTitleList;// 受权机构列表

	public String list(){
		try{
			User user = this.getCurrentUser();
			String userid = user.getId();
			setRptTitleList(orgconfigeservice.findRptTitles(userid, null));
			String whereCondition = "";
			if(StringUtil.isNotEmpty(rptTitle)){
				whereCondition += " where substr(s.packName, 6, 12) = '"
						+ rptTitle + "' ";
			}else if(StringUtil.isNotEmpty(userid)){
				whereCondition += " where exists (select 1 from t_org_config where rpttitle = substr(s.packName, 6, 12) "
						+ " and exists (select 1 from t_user_org where fk_orgid = org_id and fk_userid = '"
						+ userid + "')) ";
			}
			if(StringUtil.isNotEmpty(rptDate)){
				if("".equals(whereCondition)){
					whereCondition += " where substr(s.packName, 18, 6) = '"
							+ rptDate + "' ";
				}else{
					whereCondition += " and substr(s.packName, 18, 6) = '"
							+ rptDate + "' ";
				}
			}
			List fileStatList = receiveReportService.getFileReceiveStat(
					whereCondition, paginationList);
			this.request.setAttribute("rptTitle", this.rptTitle);
			this.request.setAttribute("rptDate", this.rptDate);
			this.request.setAttribute("fileStatList", fileStatList);
			this.request.setAttribute("rptTitleList", this.getRptTitleList());
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error("FileStatAction-list", e);
			return ERROR;
		}
	}

	public void setReceiveReportService(
			ReceiveReportService receiveReportService){
		this.receiveReportService = receiveReportService;
	}

	public void setOrgconfigeservice(OrgConfigeService orgconfigeservice){
		this.orgconfigeservice = orgconfigeservice;
	}

	public String getRptTitle(){
		return this.rptTitle;
	}

	public void setRptTitle(String rptTitle){
		this.rptTitle = rptTitle;
	}

	public String getRptDate(){
		return this.rptDate;
	}

	public void setRptDate(String rptDate){
		this.rptDate = rptDate;
	}

	public List getRptTitleList(){
		return rptTitleList;
	}

	public void setRptTitleList(List rptTitleList){
		this.rptTitleList = rptTitleList;
	}
}
