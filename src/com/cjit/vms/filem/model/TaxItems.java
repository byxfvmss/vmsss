package com.cjit.vms.filem.model;

/**
 * 税种税目信息查询 id="SZSMCX"
 * 
 * @author Administrator
 */
public class TaxItems {

	private String yylxdm;// 应用类型代码
	private String nsrsbh;// 纳税人识别号
	private String skpbh;// 税控盘编号
	private String skpkl;// 税控盘口令
	private String fplxdm;// 发票类型代码

	private String szsmsyh;// 税种税目索引号
	private String szsmdm;// 税种税目代码
	private String sl;// 税率
	private String hsbz;// 含税标志
	private String szmc;// 税种名称
	private String smmc;// 税目名称
	private String returncode;// 返回代码
	private String returnmsg;// 返回信息

	public String getYylxdm() {
		return yylxdm;
	}

	public void setYylxdm(String yylxdm) {
		this.yylxdm = yylxdm;
	}

	public String getNsrsbh() {
		return nsrsbh;
	}

	public void setNsrsbh(String nsrsbh) {
		this.nsrsbh = nsrsbh;
	}

	public String getSkpbh() {
		return skpbh;
	}

	public void setSkpbh(String skpbh) {
		this.skpbh = skpbh;
	}

	public String getSkpkl() {
		return skpkl;
	}

	public void setSkpkl(String skpkl) {
		this.skpkl = skpkl;
	}

	public String getFplxdm() {
		return fplxdm;
	}

	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
	}

	public String getSzsmsyh() {
		return szsmsyh;
	}

	public void setSzsmsyh(String szsmsyh) {
		this.szsmsyh = szsmsyh;
	}

	public String getSzsmdm() {
		return szsmdm;
	}

	public void setSzsmdm(String szsmdm) {
		this.szsmdm = szsmdm;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getHsbz() {
		return hsbz;
	}

	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}

	public String getSzmc() {
		return szmc;
	}

	public void setSzmc(String szmc) {
		this.szmc = szmc;
	}

	public String getSmmc() {
		return smmc;
	}

	public void setSmmc(String smmc) {
		this.smmc = smmc;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getReturnmsg() {
		return returnmsg;
	}

	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}

}
