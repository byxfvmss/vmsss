/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.Iterator;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import cjit.crms.util.Assert;
import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.gjsz.datadeal.core.TableIdRela;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class DeleteDatasAction extends ListDatasAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2962989968563248703L;

	/**
	 * 删除记录，如果有内嵌表，将内嵌表的所有属于这个主表的记录也都删除
	 * @return
	 */
	public String deleteDatas(){
		this.initConfigParameters();
		String result = null;
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// 根据报表ID获取报表信息
		// List tables = dataDealService
		// .findRptTableInfo(new RptTableInfo(tableId));
		// RptTableInfo _rptTableInfo = (RptTableInfo) tables.get(0);
		// 使用tables
		if(this.tableUniqueSelectId != null){
			this.tableId = tableUniqueSelectId
					.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[0];
			this.fileType = tableUniqueSelectId
					.split(DataUtil.TABLE_UNIQUE_SPLIT_SYMBOL)[1];
		}
		RptTableInfo _rptTableInfo = dataDealService.findRptTableInfoById(
				tableId, fileType);
		String menuName = "数据录入." + _rptTableInfo.getInfoType();
		int i = 0;
		try{
			// do delete action
			RptData rptData = new RptData();
			boolean successDelete = true;
			for(; i < businessIds.length; i++){
				// 判断是否为空数据
				RptData rptDataTemp = new RptData();
				rptDataTemp.setTableId(_rptTableInfo.getTableId());
				rptDataTemp.setBusinessId(businessIds[i]);
				List rptList = dataDealService.findRptDataReduce(rptDataTemp);
				if(rptList == null || rptList.size() == 0){
					this.deleteEmptyData(_rptTableInfo.getTableId(),
							businessIds[i]);
				}else{
					// 判断是否曾成功报送过
					if(this.judgeSendCommit(tableId, businessIds[i])){
						logManagerService.writeLog(request, currentUser,
								"0001", menuName, "删除", "针对[机构："
										+ this.instCode + "，单据："
										+ _rptTableInfo.getTableName()
										+ "，业务号：" + businessIds[i]
										+ "]执行数据删除操作", "0");
						successDelete = false;
						this.request.setAttribute("delete_datas_result",
								"exist_send_commit_cannot_delete");
						continue;
					}
					rptData.setTableId(_rptTableInfo.getTableId());
					rptData.setBusinessId(businessIds[i]);
					// 判断删除操作是否需要进行审核
					if("yes".equalsIgnoreCase(this.configDeleteNeedAudit)){
						// 需要
						rptData
								.setUpdateSql(" datastatus = 0 - datastatus, modifyUser = '"
										+ currentUser.getId() + "' ");
						dataDealService.updateRptData(rptData);
					}else{
						// 不需要
						saveDeleteLog(_rptTableInfo.getTableId(), _rptTableInfo
								.getFileType(), businessIds[i], null, true);
						// dataDealService.deleteRptData(rptData);
						String updateSql = " datastatus = "
								+ DataUtil.DELETE_STATUS_NUM
								+ ", modifyUser = '" + currentUser.getId()
								+ "' ";
						dataDealService.updateRptData(new RptData(tableId,
								updateSql, businessIds[i], null, null, true));
						// 删除直接下级报表相同businessId的记录
						String xyFileType = (String) TableIdRela.getZjxyMap()
								.get(_rptTableInfo.getFileType());
						if(xyFileType != null){
							deleteXyRptData(_rptTableInfo.getTableId(),
									_rptTableInfo.getFileType(),
									businessIds[i], xyFileType, updateSql);
						}
					}
				}
				logManagerService.writeLog(request, currentUser, "0001",
						menuName, "删除", "针对[机构：" + this.instCode + "，单据："
								+ _rptTableInfo.getTableName() + "，业务号："
								+ businessIds[i] + "]执行数据删除操作", "1");
			}
			if(successDelete){
				ServletActionContext.getRequest().setAttribute(
						"delete_datas_result", "删除数据成功！");
			}
			result = this.listDatas();
			return result;
		}catch (Exception e){
			logManagerService.writeLog(request, currentUser, "0001", menuName,
					"删除", "针对[机构：" + this.instCode + "，单据："
							+ _rptTableInfo.getTableName() + "，业务号："
							+ businessIds[i] + "]执行数据删除操作", "0");
			e.printStackTrace();
			log.error("DeleteDataAction-deleteDatas", e);
			ServletActionContext.getRequest().setAttribute(
					"delete_datas_result", "删除数据失败！");
			result = this.listDatas();
			return result;
		}
	}

	/**
	 * 删除主表下子表的数据记录
	 * @param tableId 主表ID
	 * @param businessId 业务主键
	 */
	private void deleteInnerDatas(String tableId, String businessId){
		try{
			List columns = dataDealService.findRptColumnInfo(new RptColumnInfo(
					tableId, null, "1", this.fileType));
			for(Iterator j = columns.iterator(); j.hasNext();){
				RptColumnInfo column = (RptColumnInfo) j.next();
				if("4".equals(column.getTagType())){
					String innerTableId = column.getColumnId();
					saveDeleteLog(innerTableId, "", businessId, null, false);
					RptData innerRptData = new RptData();
					innerRptData.setTableId(innerTableId);
					innerRptData.setBusinessId(businessId);
					dataDealService.deleteRptData(innerRptData);
				}
			}
		}catch (Exception ex){
			log.error("DeleteDatasAction-deleteInnerDatas:" + tableId + "-"
					+ "-" + businessId, ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 删除记录，如果有内嵌表，将内嵌表的所有属于这个主表的记录也都删除
	 * @return
	 */
	public String deleteBaseInfos(){
		String result = null;
		User currentUser = (User) this.getFieldFromSession(Constants.USER);
		// 根据报表ID获取报表信息
		// List tables = dataDealService
		// .findRptTableInfo(new RptTableInfo(tableId));
		// RptTableInfo _rptTableInfo = (RptTableInfo) tables.get(0);
		RptTableInfo _rptTableInfo = dataDealService.findRptTableInfoById(
				tableId, fileType);
		String menuName = "数据录入." + _rptTableInfo.getInfoType();
		int i = 0;
		try{
			// do delete action
			RptData rptData = new RptData();
			for(; i < businessIds.length; i++){
				// 然后删除主表记录
				rptData.setTableId(tableId);
				rptData.setBusinessId(businessIds[i]);
				List rds = dataDealService.findRptDataReduce(rptData);
				if(rds != null && rds.size() == 1){
					RptData rd = (RptData) rds.get(0);
					if(rd.getRptNo() != null && !"".equals(rd.getRptNo())){
						continue;
					}
				}
				saveDeleteLog(tableId, _rptTableInfo.getFileType(),
						businessIds[i], null, true);
				dataDealService.deleteRptData(rptData);
				// 删除直接下级报表相同businessId的记录
				String zjxyFileType = (String) TableIdRela.getZjxyMap().get(
						_rptTableInfo.getFileType());
				if(zjxyFileType != null){
					// 先删除子表记录
					deleteInnerDatas(tableId, businessIds[i]);
					// 然后删除主表记录
					rptData.setTableId(tableId);
					saveDeleteLog(tableId, zjxyFileType, businessIds[i], null,
							true);
					dataDealService.deleteRptData(rptData);
				}
				logManagerService.writeLog(request, currentUser, "0001",
						menuName, "删除", "针对[机构：" + this.instCode + "，单据："
								+ _rptTableInfo.getTableName() + "，业务号："
								+ businessIds[i] + "]执行数据删除操作", "1");
			}
			ServletActionContext.getRequest().setAttribute(
					"delete_datas_result", "删除数据成功！");
			result = this.listDatas();
			return result;
		}catch (Exception e){
			logManagerService.writeLog(request, currentUser, "0001", menuName,
					"删除", "针对[机构：" + this.instCode + "，单据："
							+ _rptTableInfo.getTableName() + "，业务号："
							+ businessIds[i] + "]执行数据删除操作", "0");
			e.printStackTrace();
			log.error("DeleteDataAction-deleteBaseInfos", e);
			ServletActionContext.getRequest().setAttribute(
					"delete_datas_result", "删除数据失败！");
			result = this.listDatas();
			return result;
		}
	}

	/**
	 * <p> 方法名称: deleteEmptyData|描述: 删除空记录（在数据库中插入一条对应机构、业务ID、状态为删除的记录） </p>
	 * @param tableId
	 * @param businessId
	 */
	private void deleteEmptyData(String tableId, String businessId){
		StringBuffer insertColumns = new StringBuffer(
				"instcode,businessid,datastatus");
		StringBuffer insertValues = new StringBuffer("'").append(instCode)
				.append("','").append(businessId).append("',").append(
						DataUtil.DELETE_STATUS_NUM);
		dataDealService.saveRptData(tableId, insertColumns.toString(),
				insertValues.toString());
	}

	/**
	 * <p> 方法名称: judgeSendCommit|描述: 判断是否曾成功报送过 </p>
	 * @param tableId
	 * @param businessId
	 * @return boolean
	 * @throws Exception
	 */
	private boolean judgeSendCommit(String tableId, String businessId)
			throws Exception{
		if(StringUtil.isNotEmpty(tableId) && StringUtil.isNotEmpty(businessId)){
			List list = dataDealService.findRptSendCommit(tableId, businessId,
					null, null, 1);
			if(Assert.notEmpty(list)){
				return true;
			}
		}
		return false;
	}
}