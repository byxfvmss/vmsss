package com.cjit.vms.taxdisk.aisino.single.model;


/**
 * 获取库存信息（请求报文）
 * @author john
 *
 */
public class GetInvInfoReq{

	private Integer InfoKind;
	
	public GetInvInfoReq(){}

	public GetInvInfoReq(Integer infoKind) {
		super();
		InfoKind = infoKind;
	}

	public Integer getInfoKind() {
		return InfoKind;
	}

	public void setInfoKind(Integer infoKind) {
		InfoKind = infoKind;
	}
}
