/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.ExcelUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.model.UploadModel;
import com.cjit.gjsz.system.model.User;

/**
 * @author yulubin
 */
public class ListDatasInnerAction extends ListDatasAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 764989856483393339L;
	private UploadModel uploadModel;

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）的数据列表显示入口
	 * @return
	 */
	public String listDatasInner(){
		if(!sessionInit(true)){
			return SUCCESS;
		}
		String backFlag = this.request.getParameter("backFlag");
		if("1".equals(backFlag)){
			// 取消掉子表排序缓存信息
			request.getSession().setAttribute("orderColumnSub", null);
			request.getSession().setAttribute("orderDirectionSub", null);
		}
		this.request.setAttribute("tableIdInner", tableIdInner);
		this.request.setAttribute("subCanModify", this.request
				.getParameter("subCanModify"));
		this.fileType = null;
		return listDatas(infoTypeCodeInner, tableIdInner);
	}

	/**
	 * <p> 方法名称: importExcel|描述: 导入“报关单信息”或“出口收汇核销单号码” </p>
	 * @return String
	 */
	public String importExcel(){
		String description = "导入数据";
		User user = this.getCurrentUser();
		try{
			// DFHL:增加时间过滤开始
			String strFileName = uploadModel.getFileFileName();
			String errormsg = null;
			Hashtable hs = null;
			if(strFileName.toLowerCase().endsWith(".xls")){
				hs = ExcelUtil.parseExcel(null, uploadModel.getFile(), 1);
			}else if(strFileName.toLowerCase().endsWith(".xlsx")){
				int maxCell = 0;
				if("t_customs_decl".equals(tableIdInner)){
					maxCell = 4;
				}else if("t_export_info".equals(tableIdInner)){
					maxCell = 1;
				}
				hs = ExcelUtil.parseExcel2007(null, uploadModel.getFile(), 1,
						maxCell);
			}
			if(hs != null){
				errormsg = this.importData(tableIdInner, businessId, hs);
			}
			listDatasInner();
			description = description + "[" + strFileName + "]";
			String status = "1";// 成功状态
			if(StringUtil.isEmpty(errormsg)){
				this.addActionMessage("导入数据完成。");
			}else{
				status = "0";
				this.request.setAttribute("importError", errormsg);
			}
			logManagerService.writeLog(request, user, "0001.0004", "数据录入.核销信息",
					"导入数据", description, status);
			return SUCCESS;
		}catch (Exception e){
			logManagerService.writeLog(request, user, "0001.0004", "数据录入.核销信息",
					"导入数据", description, "0");
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: importData|描述: 将解析的Excel数据导入的数据库中 </p>
	 * @param tableIdInner
	 * @param businessId
	 * @param hs
	 * @return String
	 */
	private String importData(String tableIdInner, String businessId,
			Hashtable hs){
		StringBuffer errorMessage = new StringBuffer();
		StringBuffer insertColumns = null;
		StringBuffer insertValues = null;
		List subIdList = new ArrayList();
		String[][] cellValues = (String[][]) hs.get("0");
		if(cellValues != null){
			String serverTime = DateUtils.serverCurrentTimeStamp();
			for(int i = 1; i < cellValues.length; i++){
				insertColumns = new StringBuffer();
				insertValues = new StringBuffer();
				String subId = this.createSubId(serverTime, i);
				subIdList.add(subId);
				String[] row = cellValues[i];
				if("t_customs_decl".equals(tableIdInner)){
					if(row == null || row.length < 4){
						errorMessage.append("导入文件第").append(i).append("行格式不对！");
					}else{
						// 报关单信息
						insertColumns
								.append("CUSTOMN,CUSTCCY,CUSTAMT,OFFAMT,BUSINESSID,SUBID");
						// 报关单号 s,18,18
						if(row[0] != null){
							// 临时允许报关单号9位和18位共存
							if(row[0].length() == 18 || row[0].length() == 9){
								insertValues.append("'").append(row[0]).append(
										"',");
							}else{
								errorMessage.append("导入文件第").append(i).append(
										"行[报关单号:").append(row[0]).append(
										"]长度不正确！");
							}
						}else{
							insertValues.append("'',");
						}
						// 报关单币种 s,1,3
						if(row[1] != null){
							if(row[1].length() == 3){
								insertValues.append("'").append(row[1]).append(
										"',");
							}else{
								errorMessage.append("导入文件第").append(i).append(
										"行[报关单币种:").append(row[1]).append(
										"]长度不正确！");
							}
						}else{
							insertValues.append("'',");
						}
						// 报关金额 n,0,22,0
						if(row[2] != null && !"".equals(row[2])
								&& row[1].length() <= 22){
							try{
								Long CustAmt = Long.valueOf(row[2]);
								if(CustAmt != null){
									long lCustAmt = CustAmt.longValue();
									insertValues.append(lCustAmt).append(",");
								}else{
									errorMessage.append("导入文件第").append(i)
											.append("行[报关金额:").append(row[2])
											.append("]格式不正确！");
								}
							}catch (Exception ex){
								errorMessage.append("导入文件第").append(i).append(
										"行[报关金额:").append(row[2]).append(
										"]格式不正确！");
							}
						}else if(row[2] == null || "".equals(row[2])){
							insertValues.append("null,");
						}else{
							errorMessage.append("导入文件第").append(i).append(
									"行[报关金额:").append(row[2]).append("]格式不正确！");
						}
						// 本次核注金额 n,0,22,0
						if(row[3] != null && !"".equals(row[3])
								&& row[1].length() <= 22){
							try{
								Long OffAmt = Long.valueOf(row[3]);
								if(OffAmt != null){
									long lOffAmt = OffAmt.longValue();
									insertValues.append(lOffAmt).append(",");
								}else{
									errorMessage.append("导入文件第").append(i)
											.append("行[本次核注金额:").append(row[3])
											.append("]格式不正确！");
								}
							}catch (Exception ex){
								errorMessage.append("导入文件第").append(i).append(
										"行[本次核注金额:").append(row[3]).append(
										"]格式不正确！");
							}
						}else if(row[3] == null || "".equals(row[3])){
							insertValues.append("null,");
						}
					}
				}else if("t_export_info".equals(tableIdInner)){
					if(row == null || row.length > 2){
						errorMessage.append("导入文件第").append(i).append("行格式不对！");
					}else{
						// 出口收汇核销单号码 s,9,9
						insertColumns.append("REFNO,BUSINESSID,SUBID");
						if(row[0] != null && row[0].length() <= 9){
							insertValues.append("'").append(row[0])
									.append("',");
						}else{
							errorMessage.append("导入文件第").append(i).append(
									"行[出口收汇核销单号码:").append(row[0]).append(
									"]长度不正确！");
						}
					}
				}
				if(errorMessage.toString().length() == 0){
					// BUSINESSID,SUBID
					insertValues.append("'").append(businessId).append("', '")
							.append(subId).append("' ");
					// 执行保存操作
					dataDealService.saveRptData(tableIdInner, insertColumns
							.toString(), insertValues.toString());
					if(subId != null){
						this.addFieldToSession(ScopeConstants.CURRENT_SUB_ID,
								subId);
					}
				}else{
					if(subIdList != null && subIdList.size() > 0){
						for(int s = 0; s < subIdList.size(); s++){
							String delSubId = (String) subIdList.get(s);
							RptData rd = new RptData();
							rd.setTableId(tableIdInner);
							rd.setBusinessId(businessId);
							rd.setSubId(delSubId);
							dataDealService.deleteRptData(rd);
						}
					}
					return errorMessage.toString();
				}
			}
			// 更新其对应的主表状态为1：未校验
			RptData rptData = new RptData();
			rptData.setTableId((String) this
					.getFieldFromSession(ScopeConstants.CURRENT_TABLE_ID));
			rptData.setBusinessId(businessId);
			rptData.setUpdateSql(" datastatus = " + DataUtil.WJY_STATUS_NUM
					+ " ");
			dataDealService.updateRptData(rptData);
		}
		return "";
	}

	public UploadModel getUploadModel(){
		return uploadModel;
	}

	public void setUploadModel(UploadModel uploadModel){
		this.uploadModel = uploadModel;
	}
}
