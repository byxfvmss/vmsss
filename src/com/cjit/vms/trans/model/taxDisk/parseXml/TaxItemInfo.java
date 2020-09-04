package com.cjit.vms.trans.model.taxDisk.parseXml;

import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class TaxItemInfo extends BaseDiskModel{
public static void main(String[] args) {
	String data="tax_item_index_no_ch    , taxItemIndexNo             ,szsmsyh	     ,税种税目索引号	,是, ;"+
	"tax_item_code           , taxItemCode                ,szsmdm	     ,税种税目代码   ,是, ;"+
	"rate_ch                 , rate                       ,sl	         ,税率	        ,是,	4位小数表示，例如：11%应该为0.11 ;"+
	"tax_flag                , taxFlag                    ,hsbz	       ,含税标志	    ,是,	0：不含税1：含税 ;"+
	"tax_name_ch             , taxName                    ,szmc	       ,税种名称	    ,是, ;"+
	"item_name_ch            , itemName                   ,smmc	       ,税目名称	    ,是, ;";
	new TaxItemInfo().createEntityUtil(data);
	
}

public TaxItemInfo(Element e) {
	super();
	this.taxItemIndexNo =e.getChildText(tax_item_index_no_ch) ;
	this.taxItemCode = e.getChildText(tax_item_code);
	this.rate = e.getChildText(rate_ch);
	this.taxFlag =e.getChildText(tax_flag) ;
	this.taxName =e.getChildText(tax_name_ch) ;
	this.itemName =e.getChildText(item_name_ch) ;
}

public TaxItemInfo() {
	super();
	// TODO Auto-generated constructor stub
}

/**
*税种税目索引号  是否必须：是

*/
private static final String tax_item_index_no_ch="szsmsyh";
/**
*税种税目代码  是否必须：是

*/
private static final String tax_item_code="szsmdm";
/**
*税率  是否必须：是
4位小数表示，例如：11%应该为0.11
*/
private static final String rate_ch="sl";
/**
*含税标志  是否必须：是
0：不含税1：含税
*/
private static final String tax_flag="hsbz";
/**
*税种名称  是否必须：是

*/
private static final String tax_name_ch="szmc";
/**
*税目名称  是否必须：是

*/
private static final String item_name_ch="smmc";

/**
*税种税目索引号 是否必须：是

*/
private String taxItemIndexNo;
/**
*税种税目代码 是否必须：是

*/
private String taxItemCode;
/**
*税率 是否必须：是
4位小数表示，例如：11%应该为0.11
*/
private String rate;
/**
*含税标志 是否必须：是
0：不含税1：含税
*/
private String taxFlag;
/**
*税种名称 是否必须：是

*/
private String taxName;
/**
*税目名称 是否必须：是

*/
private String itemName;
public String getTaxItemIndexNo() {
	return taxItemIndexNo;
}
public void setTaxItemIndexNo(String taxItemIndexNo) {
	this.taxItemIndexNo = taxItemIndexNo;
}
public String getTaxItemCode() {
	return taxItemCode;
}
public void setTaxItemCode(String taxItemCode) {
	this.taxItemCode = taxItemCode;
}
public String getRate() {
	return rate;
}
public void setRate(String rate) {
	this.rate = rate;
}
public String getTaxFlag() {
	return taxFlag;
}
public void setTaxFlag(String taxFlag) {
	this.taxFlag = taxFlag;
}
public String getTaxName() {
	return taxName;
}
public void setTaxName(String taxName) {
	this.taxName = taxName;
}
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}


}
