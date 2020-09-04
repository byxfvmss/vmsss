package com.cjit.vms.taxdisk.single.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class PrintMgrSetReturnXMl extends BaseDiskModel {

	/*
	 * 发票类型代码 是否必须：是
	 */
	private String fapiaoType;
	/*
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private String returnCode;
	/*
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

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

	/*
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private static final String return_code_ch = "returncode";
	/*
	 * 返回信息 是否必须：是
	 */
	/*
	 * 发票类型代码 是否必须：是
	 */
	private static final String fapiao_type_ch = "fplxdm";

	private static final String return_msg_ch = "returnmsg";

	public PrintMgrSetReturnXMl(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);
		this.fapiaoType = output.getChildText(fapiao_type_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}
}
