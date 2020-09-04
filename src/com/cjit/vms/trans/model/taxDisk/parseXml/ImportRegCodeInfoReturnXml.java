package com.cjit.vms.trans.model.taxDisk.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class ImportRegCodeInfoReturnXml extends BaseDiskModel {
	
	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

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

	public ImportRegCodeInfoReturnXml (String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);

		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}

	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private static final String return_code_ch = "returncode";
	/**
	 * 返回信息 是否必须：是
	 */
	private static final String return_msg_ch = "returnmsg";

}
