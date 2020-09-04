package com.cjit.vms.taxdisk.aisino.single.model;

/**
 * ClassName: BillSelect 
 * @Description: 发票查询更新
 * @author Napoléon 
 * @date 2016-4-13
 */
public class BillSelect {
	
	/**
	 * 销售单编号
	 */
	private String xsdbh; 
	/**
	 * 发票种类 0专票，2普票，11货运发票，12机动车发票，51电子发票
	 */
	private int fpzl;
	/**
	 *  发票代码 10位
	 */
	private String fpdm;
	/**
	 *  发票号码8位
	 */
	private String fphm;
	public String getXsdbh() {
		return xsdbh;
	}
	public void setXsdbh(String xsdbh) {
		this.xsdbh = xsdbh;
	}
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
	public BillSelect(String xsdbh, int fpzl, String fpdm, String fphm) {
		super();
		this.xsdbh = xsdbh;
		this.fpzl = fpzl;
		this.fpdm = fpdm;
		this.fphm = fphm;
	}
	public BillSelect() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
