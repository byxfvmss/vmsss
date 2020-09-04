package com.cjit.vms.taxdisk.aisino.single.model;


import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;

/**
 * 发票作废（请求报文）
 * @author john
 *
 */
public class InvoiceVoidReq {

	private Integer InfoKind;//发票类别
	private String InfoNumber;//发票号码
	private String InfoTypeCode;//发票代码
	
	public InvoiceVoidReq(){}
	
	public InvoiceVoidReq(BillInfo billInfo){
		this.setInfoKind(billInfo.getFapiaoType());
		this.setInfoNumber(billInfo.getBillNo());
		this.setInfoTypeCode(billInfo.getBillCode());
	}

	public Integer getInfoKind() {
		return InfoKind;
	}

	public void setInfoKind(String infoKind) {
		try {
			this.InfoKind = Integer.parseInt(infoKind);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getInfoNumber() {
		return InfoNumber;
	}

	public void setInfoNumber(String infoNumber) {
		this.InfoNumber = infoNumber;
	}

	public String getInfoTypeCode() {
		return InfoTypeCode;
	}

	public void setInfoTypeCode(String infoTypeCode) {
		this.InfoTypeCode = infoTypeCode;
	}
	
	
}
