package com.cjit.vms.trans.service.impl.billInvalid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.service.billInvalid.BillCancelService;
import com.cjit.vms.trans.util.DataUtil;

public class BillCancelServiceImpl extends GenericServiceImpl implements BillCancelService{

	public List findBillCancelInfoList(BillCancelInfo billCancelInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billCancelInfo", billCancelInfo);
		List l = new ArrayList();
		l.add(DataUtil.BILL_STATUS_5);
		l.add(DataUtil.BILL_STATUS_6);
		l.add(DataUtil.BILL_STATUS_8);
		l.add(DataUtil.BILL_STATUS_9);
		l.add(DataUtil.BILL_STATUS_10);
		l.add(DataUtil.BILL_STATUS_11);
		l.add(DataUtil.BILL_STATUS_19);
		map.put("dataStatus", l);
		map.put("NdataStatus", DataUtil.BILL_STATUS_12);
		return find("findBillCancelInfoList", map, paginationList);
	}

	public void updateBillCancelOperateStatus(Map params) {
		this.delete("updateBillCancelOperateStatus", params);		
	}
	
	public void updateBillCanceldataStatus(Map params) {
		this.delete("updateBillCancelStatus", params);		
	}

	public List findBillCancelAuditingList(BillCancelInfo billCancelInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		List l = new ArrayList();
		l.add(DataUtil.BILL_STATUS_13);
		map.put("dataStatus", l);
		map.put("auth_inst_ids", lstTmp);
		map.put("billCancelInfo", billCancelInfo);
		map.put("cancelAuditor", userID);
		return find("findBillCancelInfoList", map, paginationList);
	}

	public List findBillCancelList(BillCancelInfo billCancelInfo,
			String userID, PaginationList paginationList) {
		Map map = new HashMap();
		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		List l = new ArrayList();
		l.add(DataUtil.BILL_STATUS_14);
		l.add(DataUtil.BILL_STATUS_15);
		map.put("dataStatus", l);
		map.put("billCancelInfo", billCancelInfo);
		return find("findBillCancelInfoList", map, paginationList);
	}

	public List findBillCancelInfoList(BillCancelInfo billCancelInfo) { 
		Map map = new HashMap();
		map.put("billCancelInfo", billCancelInfo);
		return find("findBillCancelInfo", map);
	}
	
	public BillCancelInfo findBillCancelInfo(String billId) {
		BillCancelInfo billCancelInfo = new BillCancelInfo();
		billCancelInfo.setBillId(billId);
		List list = this.findBillCancelInfoList(billCancelInfo);
		if (list != null && list.size() == 1) {
			return (BillCancelInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findBillCancelInfoQuery(BillCancelInfo billCancelInfo, String userID, String reqExportSource) {
		Map map = new HashMap();
		if(reqExportSource.equals("billCancelApply")){
			List l = new ArrayList();
			l.add(DataUtil.BILL_STATUS_5);
			l.add(DataUtil.BILL_STATUS_6);
			l.add(DataUtil.BILL_STATUS_8);
			l.add(DataUtil.BILL_STATUS_9);
			l.add(DataUtil.BILL_STATUS_19);
			map.put("dataStatus", l);
			map.put("NdataStatus", DataUtil.BILL_STATUS_12);
		}else if(reqExportSource.equals("billCancelAuditing")){
			List l = new ArrayList();
			l.add(DataUtil.BILL_STATUS_13);
			map.put("dataStatus", l);
			map.put("cancelAuditor", userID);
		}else if(reqExportSource.equals("billCancel")){
			List l = new ArrayList();
			l.add(DataUtil.BILL_STATUS_14);
			l.add(DataUtil.BILL_STATUS_15);
			map.put("dataStatus", l);
		}else{
			List l = new ArrayList();
			l.add(billCancelInfo.getDataStatus());
			map.put("dataStatus", l);
		}
		List instIds=billCancelInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("billCancelInfo", billCancelInfo);
		return find("findBillCancelInfoQuery", map);
	}

	public String getRegisteredInfo(String taxDiskNo) {
		Map map = new HashMap();
		DiskRegInfo diskRegInfo = new DiskRegInfo();
		diskRegInfo.setTaxDiskNo(taxDiskNo);
		map.put("diskRegInfo", diskRegInfo);
		List list = find("getRegisteredInfo", map);
		if (list != null && list.size() == 1) {
			diskRegInfo = (DiskRegInfo) list.get(0);
			return diskRegInfo.getRegisteredInfo();
		}
		return null;
	}

	public TaxDiskInfo getTaxDiskInfo(String taxDiskNo) {
		Map map = new HashMap();
		TaxDiskInfo taxDiskInfo = new TaxDiskInfo();
		taxDiskInfo.setTaxDiskNo(taxDiskNo);
		map.put("taxDiskInfo", taxDiskInfo);
		List list = find("getTaxDiskInfo", map);
		if (list != null && list.size() == 1) {
			return (TaxDiskInfo) list.get(0);
		}
		return null;
	}

}
