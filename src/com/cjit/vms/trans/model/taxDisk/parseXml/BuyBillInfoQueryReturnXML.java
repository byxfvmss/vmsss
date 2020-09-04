package com.cjit.vms.trans.model.taxDisk.parseXml;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class BuyBillInfoQueryReturnXML extends BaseDiskModel {


	/**
	 * 发票类型代码 是否必须：是
	 */
	private String fapiaoType;
	/**
	 * 发票领购原始报文 是否必须：否 发票段信息原始报文数据
	 */
	private String billRecePurOriMsg;
	/**
	 * 当前发票代码 是否必须：是
	 */
	private String curBillCode;
	/**
	 * 当前发票号码 是否必须：是
	 */
	private String curBillNo;
	/**
	 * 总剩余份数 是否必须：是 整数
	 */
	private String sumSurNum;
	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getBillRecePurOriMsg() {
		return billRecePurOriMsg;
	}

	public void setBillRecePurOriMsg(String billRecePurOriMsg) {
		this.billRecePurOriMsg = billRecePurOriMsg;
	}

	public String getCurBillCode() {
		return curBillCode;
	}

	public void setCurBillCode(String curBillCode) {
		this.curBillCode = curBillCode;
	}

	public String getCurBillNo() {
		return curBillNo;
	}

	public void setCurBillNo(String curBillNo) {
		this.curBillNo = curBillNo;
	}

	public String getSumSurNum() {
		return sumSurNum;
	}

	public void setSumSurNum(String sumSurNum) {
		this.sumSurNum = sumSurNum;
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

	private List billList;

	public List getBillList() {
		return billList;
	}

	public void setBillList(List billList) {
		this.billList = billList;
	}

	public BuyBillInfoQueryReturnXML(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);
		Element taxInfo = output.getChild(bill_field_ch);
		
		List list = taxInfo.getChildren(group_ch);
		List<BuyBillBatch> buyList = new ArrayList<BuyBillBatch>();
		for (int i = 0; i < list.size(); i++) {
			BuyBillBatch bill = new BuyBillBatch((Element) list.get(i));
			buyList.add(bill);
		}
		if (list.size() > 0) {
			this.billList=buyList;
		}

		this.fapiaoType = output.getChildText(fapiao_type_ch);
		this.billRecePurOriMsg = output.getChildText(bill_rece_pur_ori_msg_ch);
		this.curBillCode = output.getChildText(cur_bill_code_ch);
		this.curBillNo = output.getChildText(cur_bill_no_ch);
		this.sumSurNum = output.getChildText(sum_sur_num_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}
	
	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch = "fplxdm";
	/**
	*发票领购原始报文  是否必须：否
	发票段信息原始报文数据
	*/
	private static final String bill_rece_pur_ori_msg_ch="fplgbw";
	/**
	*当前发票代码  是否必须：是

	*/
	private static final String cur_bill_code_ch="dqfpdm";
	/**
	*当前发票号码  是否必须：是

	*/
	private static final String cur_bill_no_ch="dqfphm";
	/**
	*总剩余份数  是否必须：是
	整数
	*/
	private static final String sum_sur_num_ch="zsyfs";
	/**
	*返回代码  是否必须：是
	00000000成功，其它失败
	*/
	private static final String return_code_ch="returncode";
	/**
	*返回信息  是否必须：是

	*/
	private static final String return_msg_ch="returnmsg";
	private static final String bill_field_ch="fpdxx";
	private static final String group_ch="group";
}
