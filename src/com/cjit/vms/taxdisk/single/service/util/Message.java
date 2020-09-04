package com.cjit.vms.taxdisk.single.service.util;

public class Message {
	/**
	 * 机构纳税人识别号与税控盘纳税人识别号不一致
	 */
	public static final String tax_no_and_inst_tax_no_not="机构纳税人识别号与税控盘纳税人识别号不一致";
	/**
	 * "机构号纳税人识别号为空 不能税控盘操作";
	 */
	public static final String blank_inst_tax_no="机构号纳税人识别号为空 不能税控盘操作";
	public static final String inst_name_Not_name="机构名称与稅控盘纳税人名称不一致";
	/**
	 * "系统异常"
	 */
	public static final String system_exception="系统异常";
	public static final String system_exception_data="系统异常!数据库错误";
	/**
	 * "系统异常 ！创建xml 文件失败
	 */
	public static final String system_exception_Xml_error="系统异常,创建xml字符串失败";
	public static final String system_exception_bill_cancel_Xml_error="系统异常,创建发票作废xml字符串失败";
	
	public static final String update_bill_cancel_result_error="更改票据状态失败";
	public static final String update_bill_print_result_error="更改打印结果失败";
	public static final String system_exception_tax_Xml_error="系统异常,创建税种税目xml字符串失败";
	public static final String system_exception_buy_bill_Xml_error="系统异常,创建购票信息查询xml字符串失败";
	public static final String system_exception_bill_issue_Xml_error="系统异常,创建发票开具 xml字符串失败";
	public static final String system_exception_bill_print_Xml_error="系统异常,创建打印Xml字符串失败";
	public static final String system_exception_update_print_error="系统异常,更改打印结果失败";
	public static final String tax_disk_info_save_success="税控盘保存成功";
	public static final String tax_disk_mon_info_save_success="税控盘监控信息保存成功";
	public static final String tax_Item_save_success="税目信息保存成功";
	public static final String tax_disk_mon_info_save_erroe="税控盘监控信息保存失败";
	public static final String parse_disk_mon_info_erroe="解析税控盘监控信息失败";
	public static final String parse_tax_Item_info_query_erroe="解析税种税目信息失败";
	public static final String parse_print_Xml_error="解析打印结果失败";
	public static final String stock_no_ch="该税控盘的此发票类型库存不足";
	public static final String single_print_limit_error="超过单次打印限定值不能打印";
	public static final String system_exception_update_bill_issue_datastauas_error="系统异常,发票开具更改状态失败";
	/**
	 * 空白作废
	 */
	public static final String issue_and_blank_cancel_falg_0="0";
	/**
	 * 开具
	 */
	public static final String issue_and_blank_cancel_falg_1="1";
	
	/**
	 *  成功 success  
	 */
	public static final String success="success";
	/**
	 *  失败 error
	 */
	public static final String  error="error";
	
	/**
	 * 返回代码
	 */
	private String returnCode;
	/**
	 * 返回信息
	 */
	private String returnMsg;
	/**
	 * 税控盘号
	 */
	private String taxDiskNo;
	/**
	 * 税控盘密码
	 */
	private String taxDiskpwd;
	/**
	 * 证书密码
	 */
	private String taxcertPwd;
	/**
	 * xml 字符串
	 */
	private String stringXml;
	private String stringXmlTwo;
	/**
	 * 号码区间
	 */
	private String billNoField;
	/**
	 * 发票代码
	 */
	private String billCode;
	/**
	 * 开具 空白作废标记
	 */
	private String issueCancelFalg;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getTaxDiskpwd() {
		return taxDiskpwd;
	}
	public void setTaxDiskpwd(String taxDiskpwd) {
		this.taxDiskpwd = taxDiskpwd;
	}
	public String getTaxcertPwd() {
		return taxcertPwd;
	}
	public void setTaxcertPwd(String taxcertPwd) {
		this.taxcertPwd = taxcertPwd;
	}
	public String getStringXml() {
		return stringXml;
	}
	public void setStringXml(String stringXml) {
		this.stringXml = stringXml;
	}
	public String getStringXmlTwo() {
		return stringXmlTwo;
	}
	public void setStringXmlTwo(String stringXmlTwo) {
		this.stringXmlTwo = stringXmlTwo;
	}
	public String getIssueCancelFalg() {
		return issueCancelFalg;
	}
	public void setIssueCancelFalg(String issueCancelFalg) {
		this.issueCancelFalg = issueCancelFalg;
	}
	public String getBillNoField() {
		return billNoField;
	}
	public void setBillNoField(String billNoField) {
		this.billNoField = billNoField;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
}
