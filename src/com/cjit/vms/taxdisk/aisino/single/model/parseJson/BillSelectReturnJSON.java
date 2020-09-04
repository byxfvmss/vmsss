package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ClassName: BillSelectReturnJSON 
 * @Description: 发票查询更新
 * @author Napoléon 
 * @date 2016-4-13
 */
public class BillSelectReturnJSON extends CommonResp {
	
	/**
	 * 发票种类
	 */
	private int fpzl; 
	/**
	 * 发票代码
	 */
	private String fpdm;
	/**
	 *  发票号码
	 */
	private String fphm;
	/**
	 *  销售单据编号
	 */
	private String xsdjbh;
	/**
	 *  合计不含税金额
	 */
	private double hjbhsje;
	/**
	 *  合计税额
	 */
	private double hjse;
	/**
	 *  开票日期(yyyy-mm-dd)
	 */
	private String kprq;
	/**
	 *  打印标志1已打印 0未打印
	 */
	private int dybz;
	/**
	 *  发票保送状态 0 未报送，1 已报送，2报送失败，3 报送中，4 验签失败
	 */
	private int fpbszt;
	/**
	 *  作废标志 0 未作废 1已作废
	 */
	private int zfbz;
	/**
	 *  发票信息的XML
	 */
	private String infoxml;
	public int getFpzl() {
		return fpzl;
	}
	public void setFpzl(int fpzl) {
		this.fpzl = fpzl;
	}
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	public String getXsdjbh() {
		return xsdjbh;
	}
	public void setXsdjbh(String xsdjbh) {
		this.xsdjbh = xsdjbh;
	}
	public double getHjbhsje() {
		return hjbhsje;
	}
	public void setHjbhsje(double hjbhsje) {
		this.hjbhsje = hjbhsje;
	}
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public int getDybz() {
		return dybz;
	}
	public void setDybz(int dybz) {
		this.dybz = dybz;
	}
	public int getFpbszt() {
		return fpbszt;
	}
	public void setFpbszt(int fpbszt) {
		this.fpbszt = fpbszt;
	}
	public int getZfbz() {
		return zfbz;
	}
	public void setZfbz(int zfbz) {
		this.zfbz = zfbz;
	}
	public String getInfoxml() {
		return infoxml;
	}
	public void setInfoxml(String infoxml) {
		this.infoxml = infoxml;
	}
	
	@Override
	public String toString() {
		return "billSelectReturnXMl [fpzl=" + fpzl + ", fpdm=" + fpdm
				+ ", fphm=" + fphm + ", xsdjbh=" + xsdjbh + ", hjbhsje="
				+ hjbhsje + ", hjse=" + hjse + ", kprq=" + kprq + ", dybz="
				+ dybz + ", fpbszt=" + fpbszt + ", zfbz=" + zfbz + ", infoxml="
				+ infoxml + "]";
	}
	public static void main (String args[]){
		String sJson
		 ="{\"retCode\":\"0000\",\"retMsg\":\"提交成功\",\"fpzl\":\"1328\",\"fpdm\":\"1328\",\"fphm\":\"1328\",\"xsdjbh\":\"1328\",\"hjbhsje\":\"1328\",\"hjse\":\"1328\",\"kprq\":\"1328\",\"dybz\":\"1328\",\"fpbszt\":\"1328\",\"zfbz\":\"1328\",\"infoxml\":\"1328\"}";
		JSONObject jO = JSONObject.parseObject(sJson);
		BillSelectReturnJSON j=JSON.toJavaObject(jO, BillSelectReturnJSON.class);
        System.out.println(j.toString());
	}
	
}
