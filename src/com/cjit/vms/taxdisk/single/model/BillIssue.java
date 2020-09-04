package com.cjit.vms.taxdisk.single.model;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import cjit.crms.util.json.JsonUtil;

import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.model.parseXml.BillIssueReturnXML;
import com.cjit.vms.taxdisk.single.util.TaxDiskUtil;


public class BillIssue extends BaseDiskModel {
	/**
	*税控盘编号
	*/
	private static final String tax_Disk_No_ch="skpbh";
	/**
	*数字证书密码
	*/
	private static final String data_Cer_Pwd_ch="keypwd";
	/**
	*发票类型代码
	*/
	private static final String fapiao_Type_ch="fplxdm";
	/**
	*开票类型
	*/
	private static final String issue_bill_Type_ch="kplx";
	/**
	*特殊票种标识
	*/
	private static final String special_ticket_flag_ch="tspz";
	/**
	*税控盘口令
	*/
	private static final String tax_Disk_pwd_ch="skpkl";
	/**
	*销货单位识别号
	*/
	private static final String tax_No_Ch="xhdwsbh";
	/**
	*销货单位名称
	*/
	private static final String name_ch="xhdwmc";
	/**
	*销货单位地址电话
	*/
	private static final String address_phone_ch="xhdwdzdh";
	/**
	*销货单位银行帐号
	*/
	private static final String account_ch="xhdwyhzh";
	/**
	*购货单位识别号
	*/
	private static final String customer_taxno_ch="ghdwsbh";
	/**
	*购货单位名称
	*/
	private static final String cunstomer_name_ch="ghdwmc";
	/**
	*购货单位地址电话
	*/
	private static final String customer_address_phone_ch="ghdwdzdh";
	/**
	*购货单位银行帐号
	*/
	private static final String customer_account="ghdwyhzh";
	/**
	*清单信息
	*/
	private static final String cost_item_ch="qdxm";
	/**
	*综合税率
	*/
	private static final String composite_rate_ch="zhsl";
	/**
	*价税金额
	*/
	private static final String sum_Amt_ch="jshj";
	/**
	*合计税额
	*/
	private static final String sum_Tax_Amt_ch="hjse";
	/**
	*合计金额
	*/
	private static final String Amt_sum_ch="hjje";
	/**
	*备注
	*/
	private static final String remark_ch="bz";
	/**
	*收款人
	*/
	private static final String payee_ch="skr";
	/**
	*复核人
	*/
	private static final String review_ch="fhr";
	/**
	*开票人
	*/
	private static final String drawer_ch="kpr";
	/**
	*加密版本号
	*/
	private static final String encrypt_versions_no_ch="jmbbh";
	/**
	*主要商品名称
	*/
	private static final String main_goods_name_ch="zyspmc";
	/**
	*商品税目
	*/
	private static final String good_tax_item_ch="spsm";
	/**
	*清单标志
	*/
	private static final String detail_flag_ch="qdbz";
	/**
	*所属月份
	*/
	private static final String apply_date_ch="ssyf";
	/**
	*开票机号
	*/
	private static final String issue_machine_no_ch="kpjh";
	/**
	*通知单编号
	*/
	private static final String inform_No="tzdbh";
	/**
	*原发票代码
	*/
	private static final String ori_bill_code_ch="yfpdm";
	/**
	*原发票号码
	*/
	private static final String ori_bill_no="yfphm";
	/**
	*签名参数
	*/
	private static final String sign_param_ch="qmcs";
	private static final String group_ch="group";
	private static final String xh_ch="xh";
	private static final String filename="开具.xml";
	

	
	/**
	*税控盘编号
	*/
	private  String taxDiskNo;
	/**
	*数字证书密码
	*/
	private  String dataCertPwd;
	/**
	*发票类型代码
	*/
	private  String fapiaoType;
	/**
	*开票类型
	*/
	private  String IssueBillType;
	/**
	*特殊票种标识
	*/
	private  String specialTicketFlag;
	/**
	*税控盘口令
	*/
	private  String taxDiskPwd;
	/**
	*销货单位识别号
	*/
	private  String taxNo;
	/**
	*销货单位名称
	*/
	private  String name;
	/**
	*销货单位地址电话
	*/
	private  String addressAndPhone;
	/**
	*销货单位银行帐号
	*/
	private  String account;
	/**
	*购货单位识别号
	*/
	private  String customerTaxNo;
	/**
	*购货单位名称
	*/
	private  String customer_name;
	/**
	*购货单位地址电话
	*/
	private  String customerAddressPhone;
	/**
	*购货单位银行帐号
	*/
	private  String customerAccount;
	
	/**
	*综合税率
	*/
	private  String compositeRate;
	/**
	*价稅合计
	*/
	private  String sumAmt;
	/**
	*合计税额
	*/
	private  String sumTaxAmt;
	/**
	*合计金额
	*/
	private  String AmtSum;
	/**
	*备注
	*/
	private  String remark;
	/**
	*收款人
	*/
	private  String payee;
	/**
	*复核人
	*/
	private  String review;
	/**
	*开票人
	*/
	private  String drawer;
	/**
	*加密版本号
	*/
	private  String encryptVersionNo;
	/**
	*主要商品名称
	*/
	private  String mainGoodsName;
	/**
	*商品税目
	*/
	private  String goodsTaxItem;
	/**
	*清单标志
	*/
	private  String detialFlag;
	/**
	*所属月份
	*/
	private  String applyDate;
	/**
	*开票机号
	*/
	private  String issueMachineNo;
	/**
	*通知单编号
	*/
	private  String informNo;
	/**
	*原发票代码
	*/
	private  String oriBillCode;
	/**
	*原发票号码
	*/
	private  String oriBillNo;
	/**
	*签名参数
	*/
	private  String signParam;
	private static final String count_ch="count";
	private  List<Goods>  goodslist;
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getDataCertPwd() {
		return dataCertPwd;
	}
	public void setDataCertPwd(String dataCertPwd) {
		this.dataCertPwd = dataCertPwd;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getIssueBillType() {
		return IssueBillType;
	}
	public void setIssueBillType(String issueBillType) {
		IssueBillType = issueBillType;
	}
	public String getSpecialTicketFlag() {
		return specialTicketFlag;
	}
	public void setSpecialTicketFlag(String specialTicketFlag) {
		this.specialTicketFlag = specialTicketFlag;
	}
	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}
	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddressAndPhone() {
		return addressAndPhone;
	}
	public void setAddressAndPhone(String addressAndPhone) {
		this.addressAndPhone = addressAndPhone;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCustomerTaxNo() {
		return customerTaxNo;
	}
	public void setCustomerTaxNo(String customerTaxNo) {
		this.customerTaxNo = customerTaxNo;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	public String getCustomerAddressPhone() {
		return customerAddressPhone;
	}
	public void setCustomerAddressPhone(String customerAddressPhone) {
		this.customerAddressPhone = customerAddressPhone;
	}
	public String getCustomerAccount() {
		return customerAccount;
	}
	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}
	public String getCompositeRate() {
		return compositeRate;
	}
	public void setCompositeRate(String compositeRate) {
		this.compositeRate = compositeRate;
	}
	public String getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}
	public String getSumTaxAmt() {
		return sumTaxAmt;
	}
	public void setSumTaxAmt(String sumTaxAmt) {
		this.sumTaxAmt = sumTaxAmt;
	}
	public String getAmtSum() {
		return AmtSum;
	}
	public void setAmtSum(String amtSum) {
		AmtSum = amtSum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public String getDrawer() {
		return drawer;
	}
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}
	public String getEncryptVersionNo() {
		return encryptVersionNo;
	}
	public void setEncryptVersionNo(String encryptVersionNo) {
		this.encryptVersionNo = encryptVersionNo;
	}
	public String getMainGoodsName() {
		return mainGoodsName;
	}
	public void setMainGoodsName(String mainGoodsName) {
		this.mainGoodsName = mainGoodsName;
	}
	public String getGoodsTaxItem() {
		return goodsTaxItem;
	}
	public void setGoodsTaxItem(String goodsTaxItem) {
		this.goodsTaxItem = goodsTaxItem;
	}
	public String getDetialFlag() {
		return detialFlag;
	}
	public void setDetialFlag(String detialFlag) {
		this.detialFlag = detialFlag;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getIssueMachineNo() {
		return issueMachineNo;
	}
	public void setIssueMachineNo(String issueMachineNo) {
		this.issueMachineNo = issueMachineNo;
	}
	public String getInformNo() {
		return informNo;
	}
	public void setInformNo(String informNo) {
		this.informNo = informNo;
	}
	public String getOriBillCode() {
		return oriBillCode;
	}
	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}
	public String getOriBillNo() {
		return oriBillNo;
	}
	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}
	public String getSignParam() {
		return signParam;
	}
	public void setSignParam(String signParam) {
		this.signParam = signParam;
	}
	public List<Goods> getGoodslist() {
		return goodslist;
	}
	public void setGoodslist(List<Goods> goodslist) {
		this.goodslist = goodslist;
	}
	

	public String createBillIssueXml() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		Element input=createInputElement();
		String[] label={tax_Disk_No_ch, tax_Disk_pwd_ch, data_Cer_Pwd_ch, fapiao_Type_ch, issue_bill_Type_ch, special_ticket_flag_ch,  tax_No_Ch, name_ch, address_phone_ch, account_ch, customer_taxno_ch, cunstomer_name_ch, customer_address_phone_ch, customer_account};
		String[] value={taxDiskNo,     taxDiskPwd,      dataCertPwd,    fapiaoType,      IssueBillType,      specialTicketFlag,          taxNo,     name,    addressAndPhone,   account,   customerTaxNo,      customer_name,     customerAddressPhone,     customerAccount};
		for(int i=0;i<label.length;i++){
			addChildElementText(input, label[i], value[i]);
		}
		if(goodslist!=null){
			Element goodsElemrnt=CreateElement(cost_item_ch, count_ch, Integer.toString(goodslist.size()));
			for(int i=0;i<goodslist.size();i++){
				Goods goods=goodslist.get(i);
				Element groupElement = CreateElement(group_ch, xh_ch, Integer.toString(i+1));
				goods.createGoodsElement(groupElement);
				goodsElemrnt.addContent(groupElement);
			}
			input.addContent(goodsElemrnt);
		}
		String[] Surlabel={composite_rate_ch, Amt_sum_ch, sum_Tax_Amt_ch, sum_Amt_ch, remark_ch, payee_ch, review_ch, drawer_ch, encrypt_versions_no_ch, main_goods_name_ch, good_tax_item_ch, detail_flag_ch, apply_date_ch, issue_machine_no_ch, inform_No, ori_bill_code_ch, ori_bill_no, sign_param_ch};
		String[] surValue={compositeRate, 	  AmtSum,	 	sumTaxAmt, 		 sumAmt,	remark, 	payee, review, 		drawer, 	encryptVersionNo,	 mainGoodsName, 		goodsTaxItem, 	detialFlag, 	applyDate, 	issueMachineNo, 		informNo, oriBillCode, 		oriBillNo, signParam};
		for(int i=0;i<Surlabel.length;i++){
			addChildElementText(input, Surlabel[i], surValue[i]);
		}
		elements.addContent(input);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,filename);
		System.out.println(outString);
		return outString;
	}

public BillIssue(String taxDiskNo, String dataCertPwd, String fapiaoType,
			String issueBillType, String specialTicketFlag, String taxDiskPwd,
			String taxNo, String name, String addressAndPhone, String account,
			String customerTaxNo, String customerName,
			String customerAddressPhone, String customerAccount,
			String compositeRate, String sumAmt, String sumTaxAmt,
			String amtSum, String remark, String payee, String review,
			String drawer, String encryptVersionNo, String mainGoodsName,
			String goodsTaxItem, String detialFlag, String applyDate,
			String issueMachineNo, String informNo, String oriBillCode,
			String oriBillNo, String signParam, List<Goods> goodslist) {
		super();
		this.taxDiskNo = taxDiskNo;
		this.dataCertPwd = dataCertPwd;
		this.fapiaoType = fapiaoType;
		this.IssueBillType = issueBillType;
		this.specialTicketFlag = specialTicketFlag;
		this.taxDiskPwd = taxDiskPwd;
		this.taxNo = taxNo;
		this.name = name;
		this.addressAndPhone = addressAndPhone;
		this.account = account;
		this.customerTaxNo = customerTaxNo;
		customer_name = customerName;
		this.customerAddressPhone = customerAddressPhone;
		this.customerAccount = customerAccount;
		this.compositeRate = compositeRate;
		this.sumAmt = sumAmt;
		this.sumTaxAmt = sumTaxAmt;
		this.AmtSum = amtSum;
		this.remark = remark;
		this.payee = payee;
		this.review = review;
		this.drawer = drawer;
		this.encryptVersionNo = encryptVersionNo;
		this.mainGoodsName = mainGoodsName;
		this.goodsTaxItem = goodsTaxItem;
		this.detialFlag = detialFlag;
		this.applyDate = applyDate;
		this.issueMachineNo = issueMachineNo;
		this.informNo = informNo;
		this.oriBillCode = oriBillCode;
		this.oriBillNo = oriBillNo;
		this.signParam = signParam;
		this.goodslist = goodslist;
	}

public BillIssue(BillInfo bill,TaxDiskInfo disk) {
	super();
	this.taxDiskNo = disk.getTaxDiskNo(); 
	this.dataCertPwd =disk.getTaxCertPsw();
	this.fapiaoType = bill.getFapiaoType()=="0"?"004":"007";
	this.IssueBillType =TaxDiskUtil.issue_Bill_type_0; 
	this.specialTicketFlag =TaxDiskUtil.special_Ticket_Flag_00;
	this.taxDiskPwd = disk.getTaxDiskPsw();
	this.taxNo = disk.getTaxpayerNo();
	this.name = bill.getName();
	this.addressAndPhone = bill.getAddressandphone(); 
	this.account = bill.getBankandaccount();
	this.customerTaxNo = bill.getCustomerTaxno();
	this.customer_name = bill.getCustomerName();
	this.customerAddressPhone = bill.getCustomerAddressandphone();
	this.customerAccount = bill.getCustomerBankandaccount();
	this.compositeRate = "";
	this.sumAmt =bill.getSumAmt();
	this.sumTaxAmt =bill.getTaxAmtSum();
	this.AmtSum = bill.getAmtSum();
	this.remark = bill.getRemark();
	this.payee = bill.getPayee();
	this.review = bill.getReviewer();
	this.drawer = bill.getDrawer();
	this.encryptVersionNo ="";
	this.mainGoodsName = "";
	this.goodsTaxItem ="";
	this.detialFlag = TaxDiskUtil.detial_Flag_1 ; 
	this.applyDate =bill.getApplyDate();
	this.issueMachineNo =bill.getMachineNo();
	this.informNo = bill.getNoticeNo(); 
	this.oriBillCode =bill.getOriBillCode();
	this.oriBillNo =bill.getOriBillNo();
	this.signParam = TaxDiskUtil.sign_Param_0;
	this.goodslist = goodslist; 
	this.Id=TaxDiskUtil.id_bill_issue;
	this.comment=TaxDiskUtil.comment_bill_issue;
	this.applyTypeCode=TaxDiskUtil.Application_type_code_1;
}

public static void main(String[] args) throws Exception {
	String StringXml="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
	"<business comment=\"发票开具\" id=\"FPKJ\">"+
	"<body yylxdm=\"1\">"+
	"<output>"+
	"<fplxdm>发票类型代码</fplxdm>"+
	"<fpdm>发票代码</fpdm>"+
	"<fphm>发票号码</fphm>"+
	"<kprq>开票日期</kprq>"+
	"<hjje>合计金额</hjje>"+
	"<skm>税控码</skm>"+
	"<returncode>0</returncode>"+
	"<returnmsg>成功</returnmsg>"+
	"</output>"+
	"</body>"+
	"</business>";
	
//	System.out.println(JsonUtil.toJsonString(new BillIssueReturnXML(StringXml)));
	BillIssue bill=new BillIssue();
	System.out.println(bill.createBillIssueXml1());
	


}



public String createBillIssueXml1() throws Exception{
//	Element root =new Element(business_cH).setAttribute(comment_cH, comment).setAttribute(id_cH,Id);;
//	Document Doc = new Document(root);
//	Element elements =new Element(body_cH).setAttribute(Application_type_code,applyTypeCode);
	Element input=new Element(input_ch);
	Document Doc = new Document(input);
//	String[] label={tax_Disk_No_ch, data_Cer_Pwd_ch, fapiao_Type_ch, issue_bill_Type_ch, special_ticket_flag_ch, tax_Disk_pwd_ch, tax_No_Ch, name_ch, address_phone_ch, account_ch, customer_taxno_ch, cunstomer_name_ch, customer_address_phone_ch, customer_account};
//	String[] value={taxDiskNo,     dataCertPwd,    fapiaoType,      IssueBillType,      specialTicketFlag,      taxDiskPwd,      taxNo,     name,    addressAndPhone,   account,   customerTaxNo,      customer_name,     customerAddressPhone,     customerAccount};
	String[] label={tax_Disk_No_ch, data_Cer_Pwd_ch};
	String[] value={taxDiskNo,     dataCertPwd};
	for(int i=0;i<label.length;i++){
		addChildElementText(input, label[i], value[i]);
	}
//	elements.addContent(input);
//	root.addContent(elements);
//	String outString=CreateDocumentFormt(Doc, path_ch,filename);
	XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setEncoding("GBK"));
	String outString = xmlOut.outputString(Doc);
	System.out.println(outString);
	return outString;
}
public BillIssue() {
	super();


}
}
