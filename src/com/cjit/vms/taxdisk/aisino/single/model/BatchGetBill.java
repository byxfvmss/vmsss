package com.cjit.vms.taxdisk.aisino.single.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BatchGetBill 
 * @Description: 批量获取发票信息
 * @author Napoléon 
 * @date 2016-4-13
 */
public class BatchGetBill {
	
	 
	private List fplist=new ArrayList();; 
	/**
	 * 发票种类
	 */
	private int fpzl; 
	/**
	 * 发票代码 10位
	 */
	private String fpdm; 
	/**
	 * 发票号码8位
	 */
	private String fphm; 
	/**
	 * 销售单据编号
	 */
	private String xsdjbh;
	public List getFplist() {
		return fplist;
	}
	public void setFplist(List fplist) {
		this.fplist = fplist;
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
	public String getXsdjbh() {
		return xsdjbh;
	}
	public void setXsdjbh(String xsdjbh) {
		this.xsdjbh = xsdjbh;
	}
	public BatchGetBill(List fplist, int fpzl, String fpdm, String fphm,
			String xsdjbh) {
		super();
		this.fplist = fplist;
		this.fpzl = fpzl;
		this.fpdm = fpdm;
		this.fphm = fphm;
		this.xsdjbh = xsdjbh;
	} 
	
	
	
}
