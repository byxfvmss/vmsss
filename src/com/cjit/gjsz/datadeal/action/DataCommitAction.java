package com.cjit.gjsz.datadeal.action;

import com.cjit.common.constant.Constants;
import com.cjit.common.constant.ScopeConstants;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.system.model.User;

/**
 * @作者: lihaiboA
 * @描述: [DataCommitAction] 数据提交业务类
 */
public class DataCommitAction extends DataDealAction{

	private static final long serialVersionUID = 1442857142857172857L;

	/**
	 * 数据提交
	 * @return String
	 */
	public String dataCommit(){
		if(!sessionInit(true)){
			return SUCCESS;
		}
		try{
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			if(businessIds == null){
				String[] ids = (String[]) session
						.get(ScopeConstants.SESSION_CHECK_BUSINESSIDS);
				if(ids != null){
					businessIds = ids;
					session.remove(ScopeConstants.SESSION_CHECK_BUSINESSIDS);
				}
			}
			if(businessIds != null){
				RptTableInfo _rptTableInfo = dataDealService
						.findRptTableInfoById(tableId, fileType);
				String menuName = "数据录入";
				int i = 0;
				try{
					String updateSql = " datastatus = "
							+ DataUtil.YTJDSH_STATUS_NUM + ", modifyUser = '"
							+ currentUser.getId() + "' ";
					if("yes".equalsIgnoreCase(this.configOverleapAudit)){
						updateSql = " datastatus = "
								+ DataUtil.SHYTG_STATUS_NUM
								+ ", modifyUser = '" + currentUser.getId()
								+ "' ";
					}
					for(; i < businessIds.length; i++){
						dataDealService.updateRptData(new RptData(_rptTableInfo
								.getTableId(), updateSql, businessIds[i], null,
								"3", true));
						logManagerService.writeLog(request, currentUser,
								"0001", menuName, "提交", "针对[机构："
										+ this.instCode + "，单据："
										+ _rptTableInfo.getTableName()
										+ "，业务号：" + businessIds[i] + "]执行提交操作",
								"1");
					}
				}catch (Exception e){
					logManagerService.writeLog(request, currentUser, "0001",
							menuName, "提交", "针对[机构：" + this.instCode + "，单据："
									+ _rptTableInfo.getTableName() + "，业务号："
									+ businessIds[i] + "]执行提交操作", "0");
					e.printStackTrace();
					log.error("DataCommitAction-dataCommit", e);
				}
			}
			this.request.setAttribute("tableId", this.tableId);
			this.request.setAttribute("infoTypeCode", this.infoTypeCode);
			this.request.setAttribute("rptColumnList", this.rptColumnList);
			this.request.setAttribute("orderColumn", this.orderColumn);
			this.request.setAttribute("orderDirection", this.orderDirection);
			this.request.setAttribute("beginDate", this.beginDate);
			this.request.setAttribute("endDate", this.endDate);
			this.request.setAttribute("dataStatus", this.dataStatus);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			this.request.setAttribute("paginationList", this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error("DataCommitAction-dataCommit", e);
			return ERROR;
		}
	}
}