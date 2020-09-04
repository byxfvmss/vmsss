package com.cjit.vms.taxdisk.servlet.model;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.servlet.model.parseXml.BillIssueReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;

/**
 * @author tom 开票机号 默认为税控钥匙编号
 * 
 */
public class BillIssue extends BaseModel {
	/**
	 * 开票终端标识
	 */
	private static final String bill_terminal_flag = "kpzdbs";
	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch = "fplxdm";

	/**
	 * 发票请求流水号
	 */
	private static final String fapiao_request_number_ch = "fpqqlsh";
	/**
	 * 开票类型
	 */
	private static final String issue_bill_tppe_ch = "kplx";
	/**
	 * 特殊票种标识
	 */
	private static final String special_falg_ch = "tspz";
	/**
	 * 销货单位识别号
	 */
	private static final String sale_taxno_ch = "xhdwsbh";
	/**
	 * 销货单位名称
	 */
	private static final String sale_name_ch = "xhdwmc";
	/**
	 * 销货单位地址电话
	 */
	private static final String sale_address_phone_ch = "xhdwdzdh";
	/**
	 * 销货单位银行帐号
	 */
	private static final String sale_bank_account_ch = "xhdwyhzh";
	/**
	 * 购货单位识别号
	 */
	private static final String customer_taxno_ch = "ghdwsbh";
	/**
	 * 购货单位名称 // *
	 */
	private static final String customer_name_ch = "ghdwmc";
	/**
	 * 购货单位地址电话
	 * 
	 */
	private static final String customer_address_phone_ch = "ghdwdzdh";
	/**
	 * 购货单位银行帐号
	 */
	private static final String customer_bank_account_ch = "ghdwyhzh";
	/**
	 * 清单标志
	 */
	private static final String detailed_falg_ch = "qdbz";
	private static final String group_ch = "group";
	/**
	 * fyxm
	 * 
	 */
	private static final String kind_number_ch = "fyxm";
	/**
	 * 发票行性质
	 */
	private static final String bill_row_property_ch = "fphxz";
	/**
	 * 商品名称
	 */
	private static final String product_name_ch = "spmc";
	/**
	 * 商品税目
	 */
	private static final String product_tax_ch = "spsm";
	/**
	 * 规格型号
	 * 
	 */
	private static final String specification_ch = "ggxh";
	/**
	 * 单位
	 * 
	 */
	private static final String unit_ch = "dw";
	/**
	 * 商品数量
	 * 
	 */
	private static final String product_number_ch = "spsl";
	/**
	 * 单价
	 */
	private static final String price_ch = "dj";
	/**
	 * 金额
	 */
	private static final String amt_ch = "je";
	/**
	 * 税率
	 */
	private static final String rate_ch = "sl";
	/**
	 * 税额
	 */
	private static final String tax_amt_ch = "se";
	/**
	 * 含税标志
	 * 
	 */
	private static final String Tax_logo_ch = "hsbz";
	/**
	 * 合计金额
	 */
	private static final String amt_sum_ch = "hjje";
	/**
	 * 合计税额
	 * 
	 */
	private static final String tax_amt_sum_ch = "hjse";
	/**
	 * 价税合计
	 * 
	 */
	private static final String sum_amt_ch = "jshj";
	/**
	 * 备注
	 * 
	 */
	private static final String remark_ch = "bz";
	/**
	 * 收款人
	 * 
	 */
	private static final String payee_ch = "skr";
	/**
	 * 复核人
	 */
	private static final String reviewer_ch = "fhr";
	/**
	 * 开票人
	 */
	private static final String drawer_ch = "kpr";
	/**
	 * 信息表编号
	 */
	private static final String Information_table_number_ch = "tzdbh";
	/**
	 * 原发票代码
	 */
	private static final String original_invoice_code_ch = "yfpdm";
	/**
	 * 原发票号码
	 */
	private static final String original_invoice_no_ch = "yfphm";
	/**
	 * 签名参数
	 * 
	 */
	private static final String signature_param_ch = "qmcs";
	private static final String count_ch = "count";
	private static final String xh_ch = "xh";
	private static final String billIssueFileXml = "发票开具.xml";
	/**
	 * 开票终端标识
	 */
	private String billTerminalFlag;
	/**
	 * 发票类型代码
	 */
	private String fapiaoType;
	/**
	 * 发票请求流水号
	 */
	private String fapiaoRequestNo;
	/**
	 * 开票类型
	 */
	private String issueBillTppe;
	/**
	 * 特殊票种标识
	 */
	private String specialFalg;
	/**
	 * 销货单位识别号
	 */
	private String taxno;
	/**
	 * 销货单位名称
	 */
	private String name;
	/**
	 * 销货单位地址电话
	 */
	private String addressPhone;
	/**
	 * 销货单位银行帐号
	 */
	private String bankAccount;
	/**
	 * 购货单位识别号
	 */
	private String customerTaxno;
	/**
	 * 购货单位名称
	 * 
	 */
	private String customerName;
	/**
	 * 购货单位地址电话
	 * 
	 */
	private String customerAddressphone;
	/**
	 * 购货单位银行帐号
	 */
	private String customerBankaccount;
	/**
	 * 清单标志
	 */
	private String detailedFalg;
	/**
	 * 商品详情
	 */
	private List listProduct;
	/**
	 * 合计金额
	 */
	private String amtSum;
	/**
	 * 合计税额
	 * 
	 */
	private String taxAmtSum;
	/**
	 * 价税合计
	 * 
	 */
	private String sumAmt;
	/**
	 * 备注
	 * 
	 */
	private String remark;
	/**
	 * 收款人
	 * 
	 */
	private String payee;
	/**
	 * 复核人
	 */
	private String reviewer;
	/**
	 * 开票人
	 */
	private String drawer;
	/**
	 * 信息表编号
	 */
	private String Informationtablenumber;
	/**
	 * 原发票代码
	 */
	private String oriBillCode;
	/**
	 * 原发票号码
	 */
	private String oriBillNo;
	/**
	 * 签名参数
	 * 
	 */
	private String signatureParam;

	/**
	 * @param billInfo
	 *            票据信息
	 * @param list
	 *            费用项目
	 * @param issueBillType
	 *            开具类型
	 * @param informationtablenumber
	 *            通知单编号
	 */
	public BillIssue(BillInfo billInfo, List<Product> list,
			String issueBillType, String informationtablenumber, VmsTaxKeyInfo taxKeyInfo) {
		super();
		this.billTerminalFlag = taxKeyInfo.getBilTerminalFlag();
		this.fapiaoType = billInfo.getFapiaoType().equals("0")?"004":"007";
		this.fapiaoRequestNo = billInfo.getBillId();
		this.issueBillTppe = issueBillType;
		this.specialFalg = TaxSelvetUtil.Special_Falg_00;
		this.taxno = billInfo.getTaxno();
		this.name = billInfo.getName();
		this.addressPhone = billInfo.getAddressandphone();
		this.bankAccount = billInfo.getBankandaccount();
		this.customerTaxno = billInfo.getCustomerTaxno();
		this.customerName = billInfo.getCustomerName();
		this.customerAddressphone = billInfo.getCustomerAddressandphone();
		this.customerBankaccount = billInfo.getCustomerBankandaccount();
		this.detailedFalg = TaxSelvetUtil.Detailed_Falg_0;
		this.listProduct = list;
		this.amtSum = billInfo.getAmtSum();
		this.taxAmtSum = billInfo.getTaxAmtSum();
		this.sumAmt = billInfo.getSumAmt();
		this.remark = billInfo.getRemark();
		this.payee = billInfo.getPayee();
		this.reviewer = billInfo.getReviewer();
		this.drawer = billInfo.getDrawer();
		Informationtablenumber = informationtablenumber;
		this.oriBillCode = billInfo.getOriBillCode();
		this.oriBillNo = billInfo.getOriBillNo();
		this.signatureParam = "";
		this.Id=TaxSelvetUtil.id_Issue;
		this.comment=TaxSelvetUtil.comment_bill_issue;
		this.applyTypeCode=TaxSelvetUtil.apply_type_ch;
	}

	public BillIssue() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return 发票开具的xml 文件 xml 字符串
	 * @throws Exception
	 */
	public String createBillIssueXml() throws Exception {
		Element root = CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements = CreateBodyElement();
		AddElement(elements);
		root.addContent(elements);
		String outString = CreateDocumentFormt(Doc, path_ch, billIssueFileXml);
		return outString;
	}

	/**
	 * @param element
	 *            忘 body里添加元素
	 */
	public void AddElement(Element elements) {
		addChildElementText(elements, bill_terminal_flag, billTerminalFlag);
		addChildElementText(elements, fapiao_type_ch, fapiaoType);
		addChildElementText(elements, fapiao_request_number_ch, "");
		addChildElementText(elements, issue_bill_tppe_ch, issueBillTppe);
		addChildElementText(elements, special_falg_ch, specialFalg);
		addChildElementText(elements, sale_taxno_ch, taxno);
		addChildElementText(elements, sale_name_ch, name);
		addChildElementText(elements, sale_address_phone_ch, addressPhone);
		addChildElementText(elements, sale_bank_account_ch, bankAccount);
		addChildElementText(elements, customer_taxno_ch, customerTaxno);
		addChildElementText(elements, customer_name_ch, customerName);
		addChildElementText(elements, customer_address_phone_ch,
				customerAddressphone);
		addChildElementText(elements, customer_bank_account_ch,
				customerBankaccount);
		addChildElementText(elements, detailed_falg_ch, detailedFalg);
		Element element = CreateElement(kind_number_ch, count_ch,
				Integer.toString(listProduct.size()));
		for (int i = 0; i < listProduct.size(); i++) {
			Product product = (Product) listProduct.get(i);
			Element groupElement = CreateElement(group_ch, xh_ch,
					Integer.toString(i + 1));
			addChildElementText(groupElement, bill_row_property_ch,
					product.getBillroProperty());
			addChildElementText(groupElement, product_name_ch,
					product.getProductName());
			addChildElementText(groupElement, product_tax_ch,
					product.getProductTax());
			addChildElementText(groupElement, specification_ch,
					product.getSpecification());
			addChildElementText(groupElement, unit_ch, product.getUnit());
			addChildElementText(groupElement, product_number_ch,
					product.getProductNumber());
			addChildElementText(groupElement, price_ch, product.getPrice());
			addChildElementText(groupElement, amt_ch, product.getAmt());
			addChildElementText(groupElement, rate_ch, product.getRate());
			addChildElementText(groupElement, tax_amt_ch, product.getTaxamt());
			addChildElementText(groupElement, Tax_logo_ch, product.getTaxlogo());
			element.addContent(groupElement);
		}
		elements.addContent(element);
		addChildElementText(elements, amt_sum_ch, amtSum);
		addChildElementText(elements, tax_amt_sum_ch, taxAmtSum);
		addChildElementText(elements, sum_amt_ch, sumAmt);
		addChildElementText(elements, remark_ch, remark);
		addChildElementText(elements, payee_ch, payee);
		addChildElementText(elements, reviewer_ch, reviewer);
		addChildElementText(elements, drawer_ch, drawer);
		addChildElementText(elements, Information_table_number_ch,
				Informationtablenumber);
		addChildElementText(elements, original_invoice_code_ch, oriBillCode);
		addChildElementText(elements, original_invoice_no_ch, oriBillNo);
		addChildElementText(elements, signature_param_ch, signatureParam);
	}

	public List getListProduct() {
		return listProduct;
	}

	public void setListProduct(List listProduct) {
		this.listProduct = listProduct;
	}

	/**
	 * @param BillIssue
	 * @throws Exception
	 *             输出 开具的xml 文件
	 */
	public void outBillIssueXmlFile(String BillIssue) throws Exception {

		CreateDocumentFormt(StringToDocument(BillIssue), path_out_ch,
				fapiaoType + billIssueFileXml);
	}

	public String getBillTerminalFlag() {
		return billTerminalFlag;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public String getFapiaoRequestNo() {
		return fapiaoRequestNo;
	}

	public String getIssueBillTppe() {
		return issueBillTppe;
	}

	public String getSpecialFalg() {
		return specialFalg;
	}

	public String getTaxno() {
		return taxno;
	}

	public String getName() {
		return name;
	}

	public String getAddressPhone() {
		return addressPhone;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public String getCustomerTaxno() {
		return customerTaxno;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getCustomerAddressphone() {
		return customerAddressphone;
	}

	public String getCustomerBankaccount() {
		return customerBankaccount;
	}

	public String getDetailedFalg() {
		return detailedFalg;
	}

	public String getAmtSum() {
		return amtSum;
	}

	public String getTaxAmtSum() {
		return taxAmtSum;
	}

	public String getSumAmt() {
		return sumAmt;
	}

	public String getRemark() {
		return remark;
	}

	public String getPayee() {
		return payee;
	}

	public String getReviewer() {
		return reviewer;
	}

	public String getDrawer() {
		return drawer;
	}

	public String getInformationtablenumber() {
		return Informationtablenumber;
	}

	public String getOriBillCode() {
		return oriBillCode;
	}

	public String getOriBillNo() {
		return oriBillNo;
	}

	public String getSignatureParam() {
		return signatureParam;
	}

	public void setBillTerminalFlag(String billTerminalFlag) {
		this.billTerminalFlag = billTerminalFlag;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public void setFapiaoRequestNo(String fapiaoRequestNo) {
		this.fapiaoRequestNo = fapiaoRequestNo;
	}

	public void setIssueBillTppe(String issueBillTppe) {
		this.issueBillTppe = issueBillTppe;
	}

	public void setSpecialFalg(String specialFalg) {
		this.specialFalg = specialFalg;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddressPhone(String addressPhone) {
		this.addressPhone = addressPhone;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setCustomerTaxno(String customerTaxno) {
		this.customerTaxno = customerTaxno;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setCustomerAddressphone(String customerAddressphone) {
		this.customerAddressphone = customerAddressphone;
	}

	public void setCustomerBankaccount(String customerBankaccount) {
		this.customerBankaccount = customerBankaccount;
	}

	public void setDetailedFalg(String detailedFalg) {
		this.detailedFalg = detailedFalg;
	}

	public void setAmtSum(String amtSum) {
		this.amtSum = amtSum;
	}

	public void setTaxAmtSum(String taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public void setInformationtablenumber(String informationtablenumber) {
		Informationtablenumber = informationtablenumber;
	}

	public void setOriBillCode(String oriBillCode) {
		this.oriBillCode = oriBillCode;
	}

	public void setOriBillNo(String oriBillNo) {
		this.oriBillNo = oriBillNo;
	}

	public void setSignatureParam(String signatureParam) {
		this.signatureParam = signatureParam;
	}

}
