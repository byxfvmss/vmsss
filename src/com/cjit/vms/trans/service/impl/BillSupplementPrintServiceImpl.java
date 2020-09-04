package com.cjit.vms.trans.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.service.BillSupplementPrintService;
import com.cjit.vms.trans.util.DataUtil;

public class BillSupplementPrintServiceImpl extends GenericServiceImpl implements BillSupplementPrintService {

	public List findBillSupplementPrintInfoList(BillCancelInfo billCancelInfo,
			String id, PaginationList paginationList) {
		Map map = new HashMap();

		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("dataStatus", DataUtil.BILL_STATUS_8);
		map.put("billCancelInfo", billCancelInfo);
		return find("findBillSupplementPrintInfoList", map, paginationList);
	}

	public List findBillSupplementPrintQuery(BillCancelInfo billCancelInfo) {
		Map map = new HashMap();
		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("dataStatus", billCancelInfo.getDataStatus());
		map.put("billCancelInfo", billCancelInfo);
		return find("findBillSupplementPrintQuery", map);
	}

}
