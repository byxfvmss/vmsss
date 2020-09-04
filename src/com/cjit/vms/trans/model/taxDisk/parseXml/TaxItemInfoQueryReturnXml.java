package com.cjit.vms.trans.model.taxDisk.parseXml;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class TaxItemInfoQueryReturnXml extends BaseDiskModel {
	
	/*public static void main(String[] args) {
		String data = "fapiao_type_ch          , fapiaoType                 ,fplxdm	     ,发票类型代码	 ,是, ;"
				+ "return_code_ch          , returnCode                 ,returncode	 ,返回代码	    ,是,	00000000成功，其它失败 ;"
				+ "return_msg_ch           , returnMsg                  ,returnmsg	   ,返回信息	 	  ,是, ;";
		String StringXml = "";
		String className = "TaxItemInfoQueryReturnXml";
		new TaxItemInfoQueryReturnXml().CreatParseXMl(StringXml, data,
				className);

	}*/

	/**
	 * 发票类型代码 是否必须：是
	 */
	private String fapiaoType;
	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;
	private List<TaxItemInfo> taxList;

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

	public List<TaxItemInfo> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxItemInfo> taxList) {
		this.taxList = taxList;
	}

	public TaxItemInfoQueryReturnXml(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);

		this.fapiaoType = output.getChildText(fapiao_type_ch);
		Element taxInfo = output.getChild(tax_Item_info_ch);
		List list = taxInfo.getChildren(group_ch);
		List<TaxItemInfo> taxList = new ArrayList<TaxItemInfo>();
		for (int i = 0; i < list.size(); i++) {
			TaxItemInfo taxIn = new TaxItemInfo((Element) list.get(i));
			taxList.add(taxIn);
		}
		this.taxList = taxList;
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}

	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch = "fplxdm";

	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private static final String return_code_ch = "returncode";

	/**
	 * 返回信息 是否必须：是
	 */
	private static final String return_msg_ch = "returnmsg";
	private static final String tax_Item_info_ch = "szsmxx";
	private static final String group_ch = "group";

}
