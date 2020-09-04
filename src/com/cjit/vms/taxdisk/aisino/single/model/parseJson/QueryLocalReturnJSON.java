package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: QueryLocalReturnJSON 
 * @Description: 查询本地库
 * @author Napoléon 
 * @date 2016-4-13
 */
public class QueryLocalReturnJSON extends CommonResp{
	
	/**
	 * 查询结果
	 */
	private int RecNo;
	/**
	 * 查询结果容器
	 */
	private List FP=new ArrayList();
	/**
	 * 发票种类
	 */
	private String FPZL; 
	/**
	 * 发票代码
	 */
	private String FPDM;
	/**
	 * 发票号码
	 */
	private String FPHM; 
	/**
	 * 开票机号
	 */
	private String KPJH; 
	/**
	 * 销售单号
	 */
	private String XSDH; 
	/**
	 * 购方名称
	 */
	private String GFMC; 
	/**
	 * 购方税号
	 */
	private String GFSH; 
	/**
	 *购方地址电话
	 */
	private String GFDZDH;
	/**
	 * 购方银行账号
	 */
	private String GFYHZH; 
	/**
	 * 销方名称
	 */
	private String XFMC; 
	/**
	 * 销方税号
	 */
	private String XFSH; 
	/**
	 *销方地址电话
	 */
	private String XFDZDH;
	/**
	 * 销方银行账号
	 */
	private String XFYHZH; 
	/**
	 * 开票日期
	 */
	private String KPRQ; 
	/**
	 * 合计金额
	 */
	private String HJJE;
	/**
	 * 税率
	 */
	private String SLV;
	/**
	 * 合计税额
	 */
	private String HJSE;
	/**
	 * 作废标志
	 */
	private String ZFBZ; 
	/**
	 * 报送标志
	 */
	private String BSBZ;
	public int getRecNo() {
		return RecNo;
	}
	public void setRecNo(int recNo) {
		RecNo = recNo;
	}
	public List getFP() {
		return FP;
	}
	public void setFP(List fP) {
		FP = fP;
	}
	public String getFPZL() {
		return FPZL;
	}
	public void setFPZL(String fPZL) {
		FPZL = fPZL;
	}
	public String getFPDM() {
		return FPDM;
	}
	public void setFPDM(String fPDM) {
		FPDM = fPDM;
	}
	public String getFPHM() {
		return FPHM;
	}
	public void setFPHM(String fPHM) {
		FPHM = fPHM;
	}
	public String getKPJH() {
		return KPJH;
	}
	public void setKPJH(String kPJH) {
		KPJH = kPJH;
	}
	public String getXSDH() {
		return XSDH;
	}
	public void setXSDH(String xSDH) {
		XSDH = xSDH;
	}
	public String getGFMC() {
		return GFMC;
	}
	public void setGFMC(String gFMC) {
		GFMC = gFMC;
	}
	public String getGFSH() {
		return GFSH;
	}
	public void setGFSH(String gFSH) {
		GFSH = gFSH;
	}
	public String getGFDZDH() {
		return GFDZDH;
	}
	public void setGFDZDH(String gFDZDH) {
		GFDZDH = gFDZDH;
	}
	public String getGFYHZH() {
		return GFYHZH;
	}
	public void setGFYHZH(String gFYHZH) {
		GFYHZH = gFYHZH;
	}
	public String getXFMC() {
		return XFMC;
	}
	public void setXFMC(String xFMC) {
		XFMC = xFMC;
	}
	public String getXFSH() {
		return XFSH;
	}
	public void setXFSH(String xFSH) {
		XFSH = xFSH;
	}
	public String getXFDZDH() {
		return XFDZDH;
	}
	public void setXFDZDH(String xFDZDH) {
		XFDZDH = xFDZDH;
	}
	public String getXFYHZH() {
		return XFYHZH;
	}
	public void setXFYHZH(String xFYHZH) {
		XFYHZH = xFYHZH;
	}
	public String getKPRQ() {
		return KPRQ;
	}
	public void setKPRQ(String kPRQ) {
		KPRQ = kPRQ;
	}
	public String getHJJE() {
		return HJJE;
	}
	public void setHJJE(String hJJE) {
		HJJE = hJJE;
	}
	public String getSLV() {
		return SLV;
	}
	public void setSLV(String sLV) {
		SLV = sLV;
	}
	public String getHJSE() {
		return HJSE;
	}
	public void setHJSE(String hJSE) {
		HJSE = hJSE;
	}
	public String getZFBZ() {
		return ZFBZ;
	}
	public void setZFBZ(String zFBZ) {
		ZFBZ = zFBZ;
	}
	public String getBSBZ() {
		return BSBZ;
	}
	public void setBSBZ(String bSBZ) {
		BSBZ = bSBZ;
	} 	
	
}
