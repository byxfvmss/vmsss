package com.cjit.vms.taxdisk.aisino.single.model;

import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;

/**
 * 发票打印（请求报文）
 * @author john
 *
 */
public class InvoicePrintReq {

	private Integer InfoKind;//发票种类
	private String InfoNumber;//发票号码
	private String InfoTypeCode;//发票代码
	private Integer ShowPrintDlg;//是否显示打印边框0不显示 1显示
	private Integer PrintKind;//是否打印清单 0打印发票1打印清单
	
	public InvoicePrintReq(){}
	
	public InvoicePrintReq(BillInfo billInfo,TaxDiskInfo taxDiskInfo){
		this.setInfoKind(billInfo.getFapiaoType());
		this.setInfoNumber(billInfo.getBillNo());
		this.setInfoTypeCode(billInfo.getBillCode());
		//this.setShowPrintDlg(null);
		//this.setPrintKind(null);
	}
	
	public Integer getInfoKind() {
		return InfoKind;
	}
	public void setInfoKind(String infoKind) {
		try {
			InfoKind = Integer.parseInt(infoKind);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getInfoNumber() {
		return InfoNumber;
	}
	public void setInfoNumber(String infoNumber) {
		InfoNumber = infoNumber;
	}
	public String getInfoTypeCode() {
		return InfoTypeCode;
	}
	public void setInfoTypeCode(String infoTypeCode) {
		InfoTypeCode = infoTypeCode;
	}
	public Integer getShowPrintDlg() {
		return ShowPrintDlg;
	}
	public void setShowPrintDlg(Integer showPrintDlg) {
		ShowPrintDlg = showPrintDlg;
	}
	public Integer getPrintKind() {
		return PrintKind;
	}
	public void setPrintKind(Integer printKind) {
		PrintKind = printKind;
	}
}
