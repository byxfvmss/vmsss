package com.cjit.vms.taxdisk.aisino.single.model.parseJson;


/**
 * ClassName: GoldFacilityReturnJSON 
 * @Description: 金税设备查询
 * @author Napoléon 
 * @date 2016-4-13
 */
public class GoldFacilityReturnJSON extends CommonResp {
	
	
	
	private String retcode;//返回码
	private String retmsg;//返回信息
	private String responseMsg; //返回金税设备状态
	private String TaxCode;//税号
	private String MachineNo;//开票服务器号
	private String CorpName;//企业名称
	private String CheckCode;//设备编号
	private int InvMcType;//0打开的是防伪开票；1打开的是开票服务器
	
	
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getTaxCode() {
		return TaxCode;
	}
	public void setTaxCode(String taxCode) {
		TaxCode = taxCode;
	}
	public String getMachineNo() {
		return MachineNo;
	}
	public void setMachineNo(String machineNo) {
		MachineNo = machineNo;
	}
	public String getCorpName() {
		return CorpName;
	}
	public void setCorpName(String corpName) {
		CorpName = corpName;
	}
	public String getCheckCode() {
		return CheckCode;
	}
	public void setCheckCode(String checkCode) {
		CheckCode = checkCode;
	}
	public int getInvMcType() {
		return InvMcType;
	}
	public void setInvMcType(int invMcType) {
		InvMcType = invMcType;
	}
	
	
}



