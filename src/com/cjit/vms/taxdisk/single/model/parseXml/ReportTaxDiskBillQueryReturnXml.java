package com.cjit.vms.taxdisk.single.model.parseXml;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class ReportTaxDiskBillQueryReturnXml extends BaseDiskModel {
	
	/**
	 * 发票类型代码 是否必须：否
	 */
	private String fapiaoType;
	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;
	private List<BillBatch> BillList;

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public List<BillBatch> getBillList() {
		return BillList;
	}

	public void setBillList(List<BillBatch> billList) {
		BillList = billList;
	}

	public ReportTaxDiskBillQueryReturnXml(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);
		Element billfield = output.getChild(bill_field_info);
		List list = billfield.getChildren(group_ch);
		List billList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			BillBatch b = new BillBatch((Element) list.get(i));
			billList.add(b);
		}
		if (billList.size() > 0) {
			this.BillList = billList;
		}
		this.fapiaoType = output.getChildText(fapiao_type_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}

	/**
	 * 发票类型代码 是否必须：是
	 */
	private static final String fapiao_type_ch = "fplxdm";
	private static final String bill_field_info = "fpdxx";
	private static final String group_ch = "group";
	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private static final String return_code_ch = "returncode";
	/**
	 * 返回信息 是否必须：是
	 */
	private static final String return_msg_ch = "returnmsg";

}
