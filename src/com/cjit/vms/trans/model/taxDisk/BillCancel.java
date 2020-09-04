package com.cjit.vms.trans.model.taxDisk;

import org.hibernate.dialect.IngresDialect;
import org.jdom.Document;
import org.jdom.Element;

import cjit.crms.util.json.JsonUtil;

import com.cjit.vms.taxServer.model.parseXMl.BaseReturnXml;
import com.cjit.vms.taxServer.model.parseXMl.BillCancelReturnCancel;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.taxDisk.parseXml.BillCancelReturnXMl;
import com.cjit.vms.trans.model.taxDisk.parseXml.BillPrintReturnXML;
import com.cjit.vms.trans.util.taxDisk.TaxDiskUtil;

public class BillCancel extends BaseDiskModel {
	
	/**
	*纳税人识别号  是否必须：是

	*/
	private static final String tax_no_ch="nsrsbh";
	/**
	*税控盘编号  是否必须：是

	*/
	private static final String tax_disk_no_ch="skpbh";
	/**
	*税控盘口令  是否必须：是

	*/
	private static final String tax_disk_pwd_ch="skpkl";
	/**
	*税务数字证书密码  是否必须：是

	*/
	private static final String tax_cert_pwd_ch="keypwd";
	/**
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	/***
	*作废类型  是否必须：是
	0：空白发票作废 1：已开发票作废
	*/
	private static final String cancel_type_ch="zflx";
	/**
	*发票代码  是否必须：否
	空白发票作废为空 长度根据发票类型不同
	*/
	private static final String bill_code_ch="fpdm";
	/**
	*发票号码  是否必须：否
	空白发票作废为空
	*/
	private static final String bill_no_ch="fphm";
	/**
	*合计金额  是否必须：否
	空白发票作废为空，单位：元（两位小数）
	*/
	private static final String sum_amt_ch="hjje";
	/**
	*作废人  是否必须：是
	作废人姓名
	*/
	private static final String cancel_people_ch="zfr";
	/**
	*签名参数  是否必须：否

	*/
	private static final String sign_param_ch="qmcs";
	private static final String filename="发票作废.xml";



	

	
	/**
	*纳税人识别号 是否必须：是

	*/
	private String taxNo;
	/**
	*税控盘编号 是否必须：是

	*/
	private String taxDiskNo;
	/**
	*税控盘口令 是否必须：是

	*/
	private String taxDiskPwd;
	/**
	*税务数字证书密码 是否必须：是

	*/
	private String taxCertPwd;
	/**
	*发票类型代码 是否必须：是

	*/
	private String faPiaoType;
	/**
	*作废类型 是否必须：是
	0：空白发票作废 1：已开发票作废
	*/
	private String cancelType;
	/**
	*发票代码 是否必须：否
	*空白发票作废为空 长度根据发票类型不同
	*/
	private String billCode;
	/**
	*发票号码 是否必须：否
	空白发票作废为空
	*/
	private String billNo;
	/**
	*合计金额 是否必须：否
	空白发票作废为空，单位：元（两位小数）
	*/
	private String sumAmt;
	/**
	*作废人 是否必须：是
	*作废人姓名
	*/
	private String cancelPeople;
	/**
	*签名参数 是否必须：否
	*
	*/
	private String signParam;
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}
	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
	}
	public String getTaxCertPwd() {
		return taxCertPwd;
	}
	public void setTaxCertPwd(String taxCertPwd) {
		this.taxCertPwd = taxCertPwd;
	}
	public String getFaPiaoType() {
		return faPiaoType;
	}
	public void setFaPiaoType(String faPiaoType) {
		this.faPiaoType = faPiaoType;
	}
	public String getCancelType() {
		return cancelType;
	}
	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getCancelPeople() {
		return cancelPeople;
	}
	public void setCancelPeople(String cancelPeople) {
		this.cancelPeople = cancelPeople;
	}
	public String getSignParam() {
		return signParam;
	}
	public void setSignParam(String signParam) {
		this.signParam = signParam;
	}
	
	public String createBillCancelXMl() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		Element input=createInputElement();
		addChildElementText(input, tax_no_ch, taxNo);
		addChildElementText(input, tax_disk_no_ch, taxDiskNo);
		addChildElementText(input, tax_disk_pwd_ch,taxDiskPwd);
		addChildElementText(input, tax_cert_pwd_ch, taxCertPwd);
		addChildElementText(input, fapiao_type_ch, faPiaoType);
		addChildElementText(input, cancel_type_ch, cancelType);
		addChildElementText(input, bill_code_ch, billCode);
		addChildElementText(input, bill_no_ch, billNo);
		addChildElementText(input, sum_amt_ch, sumAmt);
		addChildElementText(input, cancel_people_ch, cancelPeople);
		addChildElementText(input,sign_param_ch, signParam);
		elements.addContent(input);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,filename);
		System.out.println(outString);
		return outString;
		
	}
	

public BillCancel(String taxNo, String taxDiskNo, String taxDiskPwd,
			String taxCertPwd, String faPiaoType, String cancelType,
			String billCode, String billNo, String sumAmt, String cancelPeople,
			String signParam) {
		super();
		this.taxNo = taxNo;
		this.taxDiskNo = taxDiskNo;
		this.taxDiskPwd = taxDiskPwd;
		this.taxCertPwd = taxCertPwd;
		this.faPiaoType = faPiaoType;
		this.cancelType = cancelType;
		this.billCode = billCode;
		this.billNo = billNo;
		this.sumAmt = sumAmt;
		this.cancelPeople = cancelPeople;
		this.signParam = signParam;
	}

public BillCancel() {
	super();
	// TODO Auto-generated constructor stub
}




public static final String out_put_ch="output";

/*
*作废日期  是否必须：是
作废成功税控盘返回，格式YYYYMMDD
*/
private static final String cancel_date_ch="zfrq";
/*
*返回代码  是否必须：是
00000000成功，其它失败
*/
private static final String return_code_ch="returncode";
/*
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";


}
