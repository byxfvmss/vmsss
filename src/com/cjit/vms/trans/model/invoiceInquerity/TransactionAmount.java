/**
 * @Description:
 */
package com.cjit.vms.trans.model.invoiceInquerity;

import java.math.BigDecimal;

/**
 * @ClassName: InvoiceInquerityMoney
 * @date:2019-11-25
 * @Description:
 */
public class TransactionAmount {
	
	private BigDecimal transactionAmount = new BigDecimal("0"); // 金额
	private String applyTime;	//申请时间
	private String productType; //应税服务名称
	private String polMoth;//月度
	
	
	public String getPolMoth() {
		return polMoth;
	}
	public void setPolMoth(String polMoth) {
		this.polMoth = polMoth;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
	


}
