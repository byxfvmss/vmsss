/**
 * 
 */
package com.cjit.gjsz.common.homenote.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.common.homenote.model.PubHomeCell;
import com.cjit.gjsz.common.homenote.service.HomeDataService;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransInfo;

/**
 * @author sunzhan
 */
public class HomeNoteServiceImpl extends GenericServiceImpl implements
		HomeDataService {

	public List getHomeDic(String dicValue) {
		Map map = new HashMap();
		map.put("dicValue", dicValue);
		return find("findPubHomeDic", map);
	}

	public Long sumTableCount(String searchCondition, String userId,
			String interfaceVer) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("searchCondition", searchCondition);
		map.put("interfaceVer", interfaceVer);
		return getRowCount("getCfaSelfDataCnt", map);
	}

	public Long sumFeedbackCount(String searchCondition, String userId,
			String interfaceVer) throws SQLException {
		Map map = new HashMap();
		map.put("searchCondition", searchCondition);
		map.put("userId", userId);
		map.put("interfaceVer", interfaceVer);
		return getRowCount("sumFeedbackCount", map);
	}

	public List searchHomeCell(String tableId, String dicTypeName,
			String userId, String top, String interfaceVer) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("dicTypeName", dicTypeName);
		map.put("userId", userId);
		map.put("top", top);
		map.put("interfaceVer", interfaceVer);
		return find("findCfaSelfDataDetail", map);
	}

	public List getFalDatasCnt(String userId, String searchCondition)
			throws SQLException {
		Map map = new HashMap();
		map.put("searchCondition", searchCondition);
		map.put("userId", userId);
		return find("getFalDatasCnt", map);
	}

	public List getHomeCell(String userId, String date, String top) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("date", date);
		map.put("top", new Integer(top));
		return find("getHomeCell", map);
	}

	public void insertHomeCell(PubHomeCell pubHomeCell) {
		Map map = new HashMap();
		map.put("pubHomeCell", pubHomeCell);
		save("insertHomeCell", map);
	}

	// vms------------------------------

	public List getTransCount(TransInfo transInfo, List orgIdList) {
		Map map = new HashMap();
		map.put("auth_inst_ids", orgIdList);
		map.put("transInfo", transInfo);
		return find("getTransCount", map);
	}

	public List getBillCount(BillInfo billInfo, List orgIdList) {
		Map map = new HashMap();
		map.put("auth_inst_ids", orgIdList);
		map.put("billInfo", billInfo);
		return find("getBillCount", map);
	}

	public List getIuputInvoiceCount(List orgIdList) {
		Map map = new HashMap();
		map.put("auth_inst_ids", orgIdList);
		return find("getIuputInvoiceCount", map);
	}

	public List getInvoicePaperAlert(List orgIdList) {
		Map map = new HashMap();
		map.put("auth_inst_ids", orgIdList);
		return find("getInvoicePaperAlert", map);
	}

}
