package com.cjit.vms.taxdisk.aisino.single.model;

/**
 * 金税设备查询(请求报文)
 * @author john
 *
 */
public class GoldFacilityReq {

	private String SIDParam;
	

	public GoldFacilityReq() {
		super();
	}

	public GoldFacilityReq(String id,String data) {
		super();
		StringBuilder sIDParam = new StringBuilder();
		sIDParam.append("<?xmlversion=\"1.0\"encoding=\"GBK\"?>");
		sIDParam.append("<FPXT_COM_INPUT>");
		sIDParam.append("<ID>"+id+"</ID>");
		sIDParam.append("<DATA>"+data+"</DATA>");
		sIDParam.append("</FPXT_COM_INPUT>");
		this.SIDParam = sIDParam.toString();
	}

	public String getSIDParam() {
		return SIDParam;
	}

	public void setSIDParam(String sIDParam) {
		SIDParam = sIDParam;
	}
	
	


}
