package com.cjit.gjsz.datadeal.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.common.constant.ScopeConstants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;

//import java.util.Iterator;
//import java.util.List;
//import com.opensymphony.util.BeanUtils;
//import com.cjit.common.util.StringUtil;
//import com.cjit.gjsz.datadeal.model.RptColumnInfo;
//import com.cjit.gjsz.datadeal.model.RptData;
public class LoadInfoAction extends DataDealAction {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * 方法名称: loadCustNm|描述: 基础信息编辑时，根据组织机构号码，从单位基本信息表中查询对应名称，回填基础信息中相应字段
	 * </p>
	 * 
	 * @return String
	 */
	public String loadCustNm() {
		try {
			this.initConfigParameters();
			// if("yes".equals(this.autoInputCustName)){
			// String custCode = this.request.getParameter("custCode");
			// String instCode = this.request.getParameter("instCode");
			// String custName = "";
			// List rptColumnList = dataDealService
			// .findRptColumnInfo(new RptColumnInfo("t_company_info",
			// null, "1", this.fileType));
			// int cFlag = 0;
			// StringBuffer columns = new StringBuffer();
			// String custNameAlisa = "";
			// for(Iterator i = rptColumnList.iterator(); i.hasNext();){
			// RptColumnInfo column = (RptColumnInfo) i.next();
			// // 根据别名获取属性值 赋别名c1,c2,c3
			// column.setAliasColumnId("c" + (++cFlag));
			// columns.append("t.").append(column.getColumnId()).append(
			// " as ").append(column.getAliasColumnId()).append(
			// ",");
			// if("CUSTNAME".equals(column.getColumnId())){
			// custNameAlisa = column.getAliasColumnId();
			// }
			// }
			// while(cFlag < largestColumnNum){
			// columns.append("'' as c").append(++cFlag).append(",");
			// }
			// // 根据物理表名、组织结构代码获取数据
			// String orderColumn = " datastatus ";
			// String orderDirection = " desc ";
			// RptData rdSelect = new RptData("t_company_info", columns
			// .toString().substring(0,
			// columns.toString().length() - 1), null, null,
			// null, orderColumn, orderDirection);
			// rdSelect.setCustcode(custCode);
			// List rptDataList = dataDealService.findRptData(rdSelect);
			// if(rptDataList != null && rptDataList.size() > 0
			// && StringUtil.isNotEmpty(custNameAlisa)){
			// // 循环根据组织机构代码查询出的单位基本信息，匹配instCode
			// for(int i = 0; i < rptDataList.size(); i++){
			// rdSelect = (RptData) rptDataList.get(i);
			// if(instCode.equals(rdSelect.getInstCode())){
			// custName = BeanUtils.getValue(rdSelect,
			// custNameAlisa).toString();
			// }
			// }
			// // 若无相同instCode的单位基本信息，则使用任意查询出的单位基本信息给名称赋值
			// if(StringUtil.isEmpty(custName)){
			// custName = BeanUtils.getValue(rdSelect, custNameAlisa)
			// .toString();
			// }
			// }
			// this.response.setContentType("text/html; charset=UTF-8");
			// log.info("loadCustNm : " + custName);
			// this.response.getWriter().print(custName);
			// this.response.getWriter().close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LoadInfoAction-loadCustNm", e);
			return ERROR;
		}
		return null;
	}

	public String loadTableInfo() {
		try {
			String tableCode = this.request.getParameter("tableCode");
			String tableName = null;
			Map dictionaryMap = (Map) SystemCache
					.getSessionCache(ScopeConstants.SESSION_DICTIONARY_MAP_BY_TYPE);
			if (dictionaryMap != null) {
				Map tableMap = (HashMap) dictionaryMap.get("T_FAL_Z02");
				List codeDictionaryList = null;
				if (tableMap != null) {
					codeDictionaryList = (ArrayList) tableMap
							.get(DataVerify.FAL_TABLE_INFO);
					if (CollectionUtil.isNotEmpty(codeDictionaryList)) {
						for (Iterator c = codeDictionaryList.iterator(); c
								.hasNext();) {
							Dictionary cd = (Dictionary) c.next();
							if (cd.getValueBank().equals(tableCode)) {
								tableName = cd.getName();
							}
						}
					}
				}
			}
			this.response.setContentType("text/html; charset=UTF-8");
			log.info("tableName : " + tableName);
			this.response.getWriter().print(tableName);
			this.response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LoadInfoAction-loadTableInfo", e);
			return ERROR;
		}
		return null;
	}
}
