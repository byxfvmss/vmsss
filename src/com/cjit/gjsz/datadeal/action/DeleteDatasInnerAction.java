/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import org.apache.struts2.ServletActionContext;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class DeleteDatasInnerAction extends ListDatasInnerAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1024968839419277182L;

	/**
	 * 删除子表记录，并将主表记录状态置为1
	 * @return
	 */
	public String deleteDatasInner(){
		String result = null;
		RptTableInfo _rptTableInfo = new RptTableInfo();
		try{
			// 根据报表ID获取报表信息
			// List tables = dataDealService.findRptTableInfo(new RptTableInfo(
			// tableIdInner));
			// _rptTableInfo = (RptTableInfo) tables.get(0);
			_rptTableInfo = dataDealService.findRptTableInfoById(tableIdInner,
					fileType);
			// do delete action
			RptData rptData = new RptData();
			rptData.setTableId(tableIdInner);
			rptData.setSubId(subId);
			this.saveInnerRptDataLog(tableIdInner, businessId, subId, "delete",
					DateUtils.serverCurrentTimeStamp());
			dataDealService.deleteRptData(rptData);
			ServletActionContext.getRequest().setAttribute(
					"delete_datas_result", "删除数据成功！");
			result = this.listDatasInner();
			try{
				// 更新主表状态为1：未校验
				rptData = new RptData();
				rptData.setTableId(tableId);
				rptData.setBusinessId(businessId);
				// rptData.setUpdateSql(" datastatus = 1 ");
				rptData
						.setUpdateSql(" datastatus = "
								+ DataUtil.WJY_STATUS_NUM);
				dataDealService.updateRptData(rptData);
			}catch (Exception ee){
				ee.printStackTrace();
				User user = this.getCurrentUser();
				String menuName = "数据录入";
				logManagerService.writeLog(request, user, "0001", menuName,
						"删除", "针对[机构：" + this.instCode + "，字段科目："
								+ _rptTableInfo.getTableName() + "，业务号："
								+ businessId + "]执行主表状态更新", "0");
				ServletActionContext.getRequest().setAttribute(
						"delete_datas_result", "删除数据成功,更新主表状态失败！");
			}
			User user = this.getCurrentUser();
			String menuName = "数据录入";
			logManagerService.writeLog(request, user, "0001", menuName, "删除",
					"针对[机构：" + this.instCode + "，字段科目："
							+ _rptTableInfo.getTableName() + "，业务号："
							+ businessId + "]执行数据删除操作", "1");
			return result;
		}catch (Exception e){
			e.printStackTrace();
			log.error("DeleteDatasInnerAction-deleteDatasInner", e);
			ServletActionContext.getRequest().setAttribute(
					"delete_datas_result", "删除数据失败！");
			User user = this.getCurrentUser();
			String menuName = "数据录入";
			logManagerService.writeLog(request, user, "0001", menuName, "删除",
					"针对[机构：" + this.instCode + "，字段科目："
							+ _rptTableInfo.getTableName() + "，业务号："
							+ businessId + "]执行数据删除操作", "0");
			result = this.listDatasInner();
			return result;
		}
	}
}
