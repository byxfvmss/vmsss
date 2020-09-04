package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BatchGetBillReturnJSON 
 * @Description: 批量获取发票信息
 * @author Napoléon 
 * @date 2016-4-13
 */
public class BatchGetBillReturnJSON extends CommonResp{
	
	
	private List xsdbh=new ArrayList();; 
	/**
	 * 发票种类
	 */
	private int fpzl; 
	/**
	 * 发票代码
	 */
	private String fpdm; 
	/**
	 * 发票号码
	 */
	private String fphm; 
	/**
	 * 销售单据编号
	 */
	private String xsdjbh; 
	/**
	 * 合计不含税金额
	 */
	private double hjbhsje; 
	/**
	 * 合计税额
	 */
	private double hjse; 
	/**
	 * 开票日期(yyyy-mm-dd)
	 */
	private String kprq; 
	/**
	 * 打印标志1已打印 0未打印
	 */
	private String dybz; 
	/**
	 * 发票保送状态0 未保送，1 已报送，2保送失败，
	3 保送中，4 验签失败
	 */
	private String fpbszt; 
	/**
	 *作废标志 0 未作废 1已作废
	 */
	private int zfbz; 
	/**
	 * 发票信息的XML
	 */
	private String infoxml;
	public List getXsdbh() {
		return xsdbh;
	}
	public void setXsdbh(List xsdbh) {
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
	public String getDybz() {
		return dybz;
	}
	public void setDybz(String dybz) {
		this.dybz = dybz;
	}
	public String getFpbszt() {
		return fpbszt;
	}
	public void setFpbszt(String fpbszt) {
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
	
	
	
}
