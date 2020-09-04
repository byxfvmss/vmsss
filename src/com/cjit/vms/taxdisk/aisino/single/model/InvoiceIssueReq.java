package com.cjit.vms.taxdisk.aisino.single.model;

import java.util.ArrayList;
import java.util.List;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;

/**
 * 发票开具（请求报文）
 * @author john
 *
 */
public class InvoiceIssueReq {

	private Integer invoiceType;//0专票 2普票
	private String customerName;//购方名称
	private String customerTaxNr;//购方税号
	private String customerAddressTel;//购方地址电话
	private String customerBankAccountNr;//购方银行账户
	private String memo;//备注
	private String payee;//付款人
	private String checker;//审核人
	private String issuer;//开票人
	private String sellerBankAccountNr;//销方银行账户
	private String sellerAddressTel;//销方地址电话
	private boolean listed;//是否是清单
	private String documentNr;//销售单号
	private Integer CheckEWM;// 1标识校验，0标识开票
	private List<ContainerType> invoiceItems;//发票明细为容器类型
	private double tax;//税额
	private double quantity;//数量
	
	public InvoiceIssueReq(){
		
	}
	
	public InvoiceIssueReq(BillInfo billInfo,TaxDiskInfo taxDiskInfo){
			this.setInvoiceType(billInfo.getFapiaoType());
			this.setCustomerName(billInfo.getCustomerName());
			this.setCustomerTaxNr(billInfo.getCustomerTaxno());
			this.setCustomerAddressTel(billInfo.getCustomerAddressandphone());
			this.setCustomerBankAccountNr(billInfo.getCustomerBankandaccount());
			this.setMemo(billInfo.getRemark());
			this.setPayee(billInfo.getPayee());
			//this.setChecker(null);
			this.setIssuer(billInfo.getDrawer());
			//this.setSellerBankAccountNr(null);
			//this.setSellerAddressTel(null);
			//this.setListed(false);
			//this.setDocumentNr(null);
			//this.setCheckEWM(null);
			
			List<BillItemInfo> billItems = billInfo.getList();
			if(billItems != null){
				List<ContainerType> invoiceItems = new ArrayList<ContainerType>();
				for(BillItemInfo billItem : billItems){
					ContainerType ct = new ContainerType();
					ct.setProductName(billItem.getGoodsName());
					ct.setIncludeTax(billItem.getTaxFlag());
					ct.setTaxRate(billItem.getTaxRate());
					ct.setValue(billItem.getAmt());
					ct.setPrice(billItem.getGoodsPrice());
					ct.setProductUnit(billItem.getGoodsUnit());
					ct.setProductSpec(billItem.getSpecandmodel());
					invoiceItems.add(ct);
				}
				this.setInvoiceItems(invoiceItems);
			}else{
				//this.setInvoiceItems(null);
			}
			
			//this.setTax(null);
			//this.setQuantity(null);

	}
	
	public Integer getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		try {
			this.invoiceType = Integer.parseInt(invoiceType);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerTaxNr() {
		return customerTaxNr;
	}
	public void setCustomerTaxNr(String customerTaxNr) {
		this.customerTaxNr = customerTaxNr;
	}
	public String getCustomerAddressTel() {
		return customerAddressTel;
	}
	public void setCustomerAddressTel(String customerAddressTel) {
		this.customerAddressTel = customerAddressTel;
	}
	public String getCustomerBankAccountNr() {
		return customerBankAccountNr;
	}
	public void setCustomerBankAccountNr(String customerBankAccountNr) {
		this.customerBankAccountNr = customerBankAccountNr;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getSellerBankAccountNr() {
		return sellerBankAccountNr;
	}
	public void setSellerBankAccountNr(String sellerBankAccountNr) {
		this.sellerBankAccountNr = sellerBankAccountNr;
	}
	public String getSellerAddressTel() {
		return sellerAddressTel;
	}
	public void setSellerAddressTel(String sellerAddressTel) {
		this.sellerAddressTel = sellerAddressTel;
	}
	public boolean isListed() {
		return listed;
	}
	public void setListed(boolean listed) {
		this.listed = listed;
	}
	public String getDocumentNr() {
		return documentNr;
	}
	public void setDocumentNr(String documentNr) {
		this.documentNr = documentNr;
	}
	public Integer getCheckEWM() {
		return CheckEWM;
	}
	public void setCheckEWM(Integer checkEWM) {
		CheckEWM = checkEWM;
	}
	public List<ContainerType> getInvoiceItems() {
		return invoiceItems;
	}
	public void setInvoiceItems(List<ContainerType> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(String tax) {
		try {
			this.tax = Double.parseDouble(tax);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		try {
			this.quantity = Double.parseDouble(quantity);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
