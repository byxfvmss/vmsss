/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.util.Iterator;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptTableInfo;
import com.cjit.gjsz.system.model.User;

/**
 * @author lihaiboA
 */
public class ListBasicAction extends DataDealAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	/**
	 * 基础信息首页面
	 * 
	 * @return
	 */
	public String listBasic() {
		basicInfoList.clear();
		if (!sessionInit(true)) {
			return SUCCESS;
		}
		try {
			// 获取当前用户信息
			User currentUser = (User) this.getFieldFromSession(Constants.USER);
			String userId = "";
			if (currentUser != null) {
				userId = currentUser.getId();
			}
			//
			busiDataInfoList = dataDealService
					.findRptBusiDataInfo(new RptBusiDataInfo(this.busiDataType,
							"1", "1"));
			// 
			rptTableList = dataDealService.findRptTableInfo(new RptTableInfo(
					this.getInfoTypeName(this.infoType, busiDataInfoList), "1",
					"1"), userId);
			String tableId = null;
			String fileType = null;
			// 给报告期字段覆初始值，默认系统当前月
			if (StringUtil.isEmpty(this.buocMonth)) {
				this.buocMonth = DateUtils.getPreMonth();
			}
			// 循环报表列表，得到每张报表的描述信息
			List rptDataStatusList = null;
			if ("1".equals(this.busiDataType)) {
				rptDataStatusList = dataDealService
						.findRptDataStatusCountByInfoTypeAndInstCode(infoType,
								instCode, searchLowerOrg, userId, linkBussType,
								buocMonth);
			}
			for (Iterator i = rptTableList.iterator(); i.hasNext();) {
				RptTableInfo rptTable = (RptTableInfo) i.next();
				tableId = rptTable.getTableId();
				fileType = rptTable.getFileType();
				List listDesc = getListDesc(tableId, instCode, searchLowerOrg,
						fileType, "1,2,3,5", userId, rptDataStatusList,
						linkBussType);
				rptTable.setListDescription(listDesc);
				basicInfoList.add(rptTable);
			}
			buocMonthList = initBuocMonthSelectList();
			this.request.setAttribute("buocMonthList", buocMonthList);
			this.request.setAttribute("basicInfoList", basicInfoList);
			this.request.setAttribute("configSearchAllOrg",
					this.configSearchAllOrg);
			this.request.setAttribute("configForbidAdd", this.configForbidAdd);
			this.request.setAttribute("searchLowerOrg", this.searchLowerOrg);
			this.request.setAttribute("buocMonth", this.buocMonth);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	/*
	 * private String getDesc(String tableId, String instCode, String typedesc,
	 * String fileType){ long recordNum =
	 * dataDealService.findRptDataCountByTableIdAndInstCode( tableId, instCode,
	 * fileType).longValue(); StringBuffer description = new
	 * StringBuffer("").append(typedesc)
	 * .append(DataUtil.DCL_INFO).append(recordNum).append("条;"); return
	 * description.toString(); }
	 * 
	 * private String getDescTitle(String tb, String inst, String typedesc,
	 * String fileType){ StringBuffer desTitle = new StringBuffer(""); List lst =
	 * dataDealService.findRptDataStatusCountByTableIdAndInstCode( tb, inst,
	 * fileType); if(lst.size() > 0){ desTitle.append("
	 * ").append(typedesc).append(": "); } for(Iterator i = lst.iterator();
	 * i.hasNext();){ RptStatusCountInfo m = (RptStatusCountInfo) i.next();
	 * switch(m.getDataStatus()){ // 1 case DataUtil.WJY_STATUS_NUM:
	 * desTitle.append(DataUtil.WJY_STATUS_CH)
	 * .append(m.getCount()).append("条").append(";"); break; // 2 case
	 * DataUtil.JYWTG_STATUS_NUM:
	 * desTitle.append(DataUtil.JYWTG_STATUS_CH).append(
	 * m.getCount()).append("条").append(";"); break; // 3 case
	 * DataUtil.JYYTG_STATUS_NUM:
	 * desTitle.append(DataUtil.JYYTG_STATUS_CH).append(
	 * m.getCount()).append("条").append(";"); break; // 4 case
	 * DataUtil.SHWTG_STATUS_NUM:
	 * desTitle.append(DataUtil.SHWTG_STATUS_CH).append(
	 * m.getCount()).append("条").append(";"); break; // 5 case
	 * DataUtil.SHYTG_STATUS_NUM:
	 * desTitle.append(DataUtil.SHYTG_STATUS_CH).append(
	 * m.getCount()).append("条").append(";"); break; // 6 case
	 * DataUtil.YBS_STATUS_NUM: desTitle.append(DataUtil.YBS_STATUS_CH)
	 * .append(m.getCount()).append("条").append(";"); break; // 7 case
	 * DataUtil.YSC_STATUS_NUM: desTitle.append(DataUtil.YSC_STATUS_CH)
	 * .append(m.getCount()).append("条").append(";"); break; default: break; } }
	 * if(!"".equals(desTitle.toString())){ return desTitle.toString() + "\r\n"; }
	 * return ""; }
	 */
}
