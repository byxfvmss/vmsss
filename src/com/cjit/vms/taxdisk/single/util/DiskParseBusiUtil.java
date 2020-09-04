package com.cjit.vms.taxdisk.single.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.trans.model.TaxDiskMonitorInfo;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.MonitorInfoQueryReturnXml;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxItemInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.TaxItemInfoQueryReturnXml;

public class DiskParseBusiUtil {
	
	/**
	 * @param mon
	 * @param instCode
	 * @param diskNo
	 * @return 将接口 接入对应的 业务实体类中  税控盘监控数据查询 
	 */
	public static TaxDiskMonitorInfo getTaxDiskMonitorInfo(MonitorInfoQueryReturnXml mon,String instCode,String diskNo){
		TaxDiskMonitorInfo taxDiskMonitorInfo=new TaxDiskMonitorInfo();
		taxDiskMonitorInfo.setBillEndDateS(mon.getIssueCutOffTime());
		taxDiskMonitorInfo.setBillLimitAmtNS(new BigDecimal(mon.getOffLineBillLimitNegTotalAmt()));
		taxDiskMonitorInfo.setBillLimitAmtPS(new BigDecimal(mon.getOffLineBillLimitPosTotalAmt()));
		taxDiskMonitorInfo.setBillLimitAmtS(new BigDecimal(mon.getSingleBillLimitAmt()));
		taxDiskMonitorInfo.setDataRepEndDateS(mon.getDataReportEndData());
		taxDiskMonitorInfo.setDataRepStrDateS(mon.getDataReportBeginData());
		taxDiskMonitorInfo.setFapiaoType(mon.getFapiaoType());
		taxDiskMonitorInfo.setInstId(instCode);
		taxDiskMonitorInfo.setLimitFunctionS(mon.getOffLineLimitFlag());
		taxDiskMonitorInfo.setnBilDayS(mon.getIssueNegBillLimitDayNum());
		taxDiskMonitorInfo.setnBillFlgS(mon.getNegBillFlag());
		taxDiskMonitorInfo.setNewReportDateS(mon.getNewestReportDate());
		taxDiskMonitorInfo.setOffLineAmtNS(new BigDecimal(mon.getOffLineBillLimitNegTotalAmt()));
		taxDiskMonitorInfo.setOffLineAmtPS(new BigDecimal(mon.getOffLineBillLimitPosTotalAmt()));
		taxDiskMonitorInfo.setOffLineBillS(new BigDecimal(mon.getOffLineBillLimitPieceNum()));
		taxDiskMonitorInfo.setOffLineDayS(new BigDecimal(mon.getIssueNegBillLimitDayNum()));
		taxDiskMonitorInfo.setOffLineOtsS(mon.getOffLineBillExtendInfo());
		taxDiskMonitorInfo.setResidualCapacityS(new BigDecimal(mon.getSurCap()));
		taxDiskMonitorInfo.setTaxDiskNo(diskNo);
		taxDiskMonitorInfo.setUploadDeadlineS(mon.getUpCutOffDate());
		return taxDiskMonitorInfo;
	}
	public static List<VmsTaxInfo> getVmsTaxInfo(TaxItemInfoQueryReturnXml taxItemInfo,String taxNo){
		List<VmsTaxInfo> list=new ArrayList<VmsTaxInfo>();
		if(taxItemInfo.getTaxList().size()>0){
			/*for (int i=0;i<taxItemInfo.getTaxList().size();i++){
				VmsTaxInfo  taxInfo=new VmsTaxInfo();
				TaxItemInfo taxItem=taxItemInfo.getTaxList().get(i);
				taxInfo.setFapiaoType(taxItemInfo.getFapiaoType()=="004"?"0":"1");
				taxInfo.setTaxFlag(taxItem.getTaxFlag());
				taxInfo.setTaxId(taxItem.getTaxItemIndexNo());
				taxInfo.setTaxItemCode(taxItem.getTaxItemCode());
				taxInfo.setTaxItemName(taxItem.getItemName());
				taxInfo.setTaxName(taxItem.getTaxName());
				taxInfo.setTaxno(taxNo);
				taxInfo.setTaxRate(taxItem.getRate());
				list.add(taxInfo);
			}*/
		}
		return list;
		
	}
}
