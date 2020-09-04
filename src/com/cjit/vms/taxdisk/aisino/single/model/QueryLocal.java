package com.cjit.vms.taxdisk.aisino.single.model;

/**
 * ClassName: QueryLocal 
 * @Description: 查询本地库
 * @author Napoléon 
 * @date 2016-4-13
 */
public class QueryLocal {
	
	/**
	 * 单据号可以为空
	 */
	private String djh ; 
	/**
	 * 购方名称可以为空
	 */
	private String gfmc ; 
	/**
	 *发票号码可以为空
	 */
	private String fphm ; 
	/**
	 * 发票代码可以为空
	 */
	private String fpdm ; 
	/**
	 *发票种类可以为空
	 */
	private String fpzl ; 
	/**
	 *清单标志可以为空
	 */
	private String qdbz; 
	/**
	 * 作废状态可以为空
	 */
	private String zfzt; 
	/**
	 * 起始时间可以为空
	 */
	private String BeginDate ; 
	/**
	 * 终止时间可以为空
	 */
	private String EndDate;
	public String getDjh() {
		return djh;
	}
	public void setDjh(String djh) {
		this.djh = djh;
	}
	public String getGfmc() {
		return gfmc;
	}
	public void setGfmc(String gfmc) {
		this.gfmc = gfmc;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFpzl() {
		return fpzl;
	}
	public void setFpzl(String fpzl) {
		this.fpzl = fpzl;
	}
	public String getQdbz() {
		return qdbz;
	}
	public void setQdbz(String qdbz) {
		this.qdbz = qdbz;
	}
	public String getZfzt() {
		return zfzt;
	}
	public void setZfzt(String zfzt) {
		this.zfzt = zfzt;
	}
	public String getBeginDate() {
		return BeginDate;
	}
	public void setBeginDate(String beginDate) {
		BeginDate = beginDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public QueryLocal(String djh, String gfmc, String fphm, String fpdm,
			String fpzl, String qdbz, String zfzt, String beginDate,
			String endDate) {
		super();
		this.djh = djh;
		this.gfmc = gfmc;
		this.fphm = fphm;
		this.fpdm = fpdm;
		this.fpzl = fpzl;
		this.qdbz = qdbz;
		this.zfzt = zfzt;
		BeginDate = beginDate;
		EndDate = endDate;
	} 
	
	
}
