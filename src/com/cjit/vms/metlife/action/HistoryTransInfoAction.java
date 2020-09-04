package com.cjit.vms.metlife.action;

import cjit.crms.util.json.JsonUtil;

import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.vms.metlife.model.HistoryTransInfo;
import com.cjit.vms.metlife.service.HistoryTransInfoService;
import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.DataUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

/**
 * Created by Abel-西阳 on 2016/2/19.
 */
public class HistoryTransInfoAction extends DataDealAction {

	private HistoryTransInfoService historyTransInfoService;
	private HistoryTransInfo historyInfo;
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private VmsCommonService vmsCommonService;
	private String busiNessIds;
	private TransInfoService transInfoService;
	private String RESULT_MESSAGE;
	public String historyTransInfoList() throws ParseException {
		try {

			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");
			historyTransInfoService.historyTransInfoList(historyInfo,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public String savehTransInfoToTransInfo() {
		String[] p = {};
		try {
			p = this.busiNessIds.split(",");
			historyTransInfoService.hTransInfoToTransInfo(p);
//			for (int i = 0; i < p.length; i++) {
//				transInfoService.saveSaleAccountDetailsD(p[i],null);
//				transInfoService.saveSaleAccountDetailsC(p[i],DataUtil.ACCOUNT_CODE_C1_8260000010);
//			}
			this.setRESULT_MESSAGE("移送成功!");
			return SUCCESS;
		} catch (Exception e) {
//			if(this.busiNessIds.length()>0){
//				p = this.busiNessIds.split(",");
//				historyTransInfoService.deleteHtransInfoList(p);
//			}
			e.printStackTrace();
			this.setRESULT_MESSAGE("移送失败 ,请稍后重试!");
			return ERROR;
		}
	}

	public HistoryTransInfoService getHistoryTransInfoService() {
		return historyTransInfoService;
	}

	public void setHistoryTransInfoService(
			HistoryTransInfoService historyTransInfoService) {
		this.historyTransInfoService = historyTransInfoService;
	}

	public HistoryTransInfo getHistoryInfo() {
		return historyInfo;
	}

	public void setHistoryInfo(HistoryTransInfo historyInfo) {
		this.historyInfo = historyInfo;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public String getBusiNessIds() {
		return busiNessIds;
	}

	public void setBusiNessIds(String busiNessIds) {
		if (busiNessIds != null) {
			this.busiNessIds = busiNessIds;
		}
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		if (transInfoService != null) {
			this.transInfoService = transInfoService;
		}
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE != null ? RESULT_MESSAGE : "";
	}

	public void setRESULT_MESSAGE(String rESULT_MESSAGE) {
		if (rESULT_MESSAGE != null) {
			RESULT_MESSAGE = rESULT_MESSAGE;
		}
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}
}
