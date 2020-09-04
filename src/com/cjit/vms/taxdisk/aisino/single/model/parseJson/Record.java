package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

public class Record {

	private String TypeCode;//发票代码
	private String InvNo;//发票号码
	private String InvDate;//开具日期yyyy-mm-dd hh:ss:nn
	private String BuyerName;//购方名称
	private String BuyTaxCode;//购方税号
	private String BuyerAddress;//购方地址电话
	private String BuyerAccounts;//购方银行账户
	private String Seller;//销方名称
	private String SellerTaxCode;//销方税号
	private String SellerAddress;//销方地址电话
	private String SellerAccounts;//销方银行账户
	private Integer InfoKind;//发票种类
	private Integer ListFlag;//清单标志
	private String WareName;//主要商品名称
	private double Amount;//商品金额
	private Integer TaxRate;//税率；
	private double Tax;//税额
	private String Memo;//备注
	private String MemoText;//作废标志
	
	private String StartNo;//起始号码
	private Integer InvNumbers;//发票张数
	private String SegAttr;//区段性质
	private Integer MachineNo;//分机号
	private Integer Month;//所属月份
	
	public String getTypeCode() {
		return TypeCode;
	}
	public void setTypeCode(String typeCode) {
		TypeCode = typeCode;
	}
	public String getInvNo() {
		return InvNo;
	}
	public void setInvNo(String invNo) {
		InvNo = invNo;
	}
	public String getInvDate() {
		return InvDate;
	}
	public void setInvDate(String invDate) {
		InvDate = invDate;
	}
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getBuyTaxCode() {
		return BuyTaxCode;
	}
	public void setBuyTaxCode(String buyTaxCode) {
		BuyTaxCode = buyTaxCode;
	}
	public String getBuyerAddress() {
		return BuyerAddress;
	}
	public void setBuyerAddress(String buyerAddress) {
		BuyerAddress = buyerAddress;
	}
	public String getBuyerAccounts() {
		return BuyerAccounts;
	}
	public void setBuyerAccounts(String buyerAccounts) {
		BuyerAccounts = buyerAccounts;
	}
	public String getSeller() {
		return Seller;
	}
	public void setSeller(String seller) {
		Seller = seller;
	}
	public String getSellerTaxCode() {
		return SellerTaxCode;
	}
	public void setSellerTaxCode(String sellerTaxCode) {
		SellerTaxCode = sellerTaxCode;
	}
	public String getSellerAddress() {
		return SellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		SellerAddress = sellerAddress;
	}
	public String getSellerAccounts() {
		return SellerAccounts;
	}
	public void setSellerAccounts(String sellerAccounts) {
		SellerAccounts = sellerAccounts;
	}
	public Integer getInfoKind() {
		return InfoKind;
	}
	public void setInfoKind(Integer infoKind) {
		InfoKind = infoKind;
	}
	public Integer getListFlag() {
		return ListFlag;
	}
	public void setListFlag(Integer listFlag) {
		ListFlag = listFlag;
	}
	public String getWareName() {
		return WareName;
	}
	public void setWareName(String wareName) {
		WareName = wareName;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public Integer getTaxRate() {
		return TaxRate;
	}
	public void setTaxRate(Integer taxRate) {
		TaxRate = taxRate;
	}
	public double getTax() {
		return Tax;
	}
	public void setTax(double tax) {
		Tax = tax;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public String getMemoText() {
		return MemoText;
	}
	public void setMemoText(String memoText) {
		MemoText = memoText;
	}
	public String getStartNo() {
		return StartNo;
	}
	public void setStartNo(String startNo) {
		StartNo = startNo;
	}
	public Integer getInvNumbers() {
		return InvNumbers;
	}
	public void setInvNumbers(Integer invNumbers) {
		InvNumbers = invNumbers;
	}
	public String getSegAttr() {
		return SegAttr;
	}
	public void setSegAttr(String segAttr) {
		SegAttr = segAttr;
	}
	public Integer getMachineNo() {
		return MachineNo;
	}
	public void setMachineNo(Integer machineNo) {
		MachineNo = machineNo;
	}
	public Integer getMonth() {
		return Month;
	}
	public void setMonth(Integer month) {
		Month = month;
	}

}
