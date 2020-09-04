package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

/**
 * （返回报文）公共参数
 * @author john
 *
 */
public class CommonResp {

	private String retcode;//返回码
	private String retmsg;//返回信息
	private String info;//局端返回信息1
	private String responseMsg;//局端返回信息2
	
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
}
