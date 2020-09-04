package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

import java.util.List;

/**
 * 查询发票信息（返回报文）
 * @author john
 *
 */
public class CheckInvoInfoResp extends CommonResp{

	private List<Record> records;
	private String KPR;//开票人
	private String FHR;//复核人
	private String SKR;//收款人
	private Integer BSZT;//保送状态
	private Integer ZFBZ;//作废标志
	private Integer XFBZ;//修复标志
	private Integer DYBZ;//打印标志
	private String BillNumber;//单据号
	private List<Detail> Details;
	
	public List<Record> getRecords() {
		return records;
	}
	public void setRecords(List<Record> records) {
		this.records = records;
	}
	public String getKPR() {
		return KPR;
	}
	public void setKPR(String kPR) {
		KPR = kPR;
	}
	public String getFHR() {
		return FHR;
	}
	public void setFHR(String fHR) {
		FHR = fHR;
	}
	public String getSKR() {
		return SKR;
	}
	public void setSKR(String sKR) {
		SKR = sKR;
	}
	public Integer getBSZT() {
		return BSZT;
	}
	public void setBSZT(Integer bSZT) {
		BSZT = bSZT;
	}
	public Integer getZFBZ() {
		return ZFBZ;
	}
	public void setZFBZ(Integer zFBZ) {
		ZFBZ = zFBZ;
	}
	public Integer getXFBZ() {
		return XFBZ;
	}
	public void setXFBZ(Integer xFBZ) {
		XFBZ = xFBZ;
	}
	public Integer getDYBZ() {
		return DYBZ;
	}
	public void setDYBZ(Integer dYBZ) {
		DYBZ = dYBZ;
	}
	public String getBillNumber() {
		return BillNumber;
	}
	public void setBillNumber(String billNumber) {
		BillNumber = billNumber;
	}
	public List<Detail> getDetails() {
		return Details;
	}
	public void setDetails(List<Detail> details) {
		Details = details;
	}
	
	
}
