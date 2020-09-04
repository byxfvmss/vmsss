package com.cjit.vms.taxdisk.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.vms.taxdisk.service.impl.AssionDiskBillInterfaceServiceImpl;
import com.cjit.vms.taxdisk.service.impl.BWDiskBillInterfaceServiceImpl;

public class Message {


	public static final String no_reg_info="该税控盘注册码信息找不到或找到多个 请维护相关的注册码信息";
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
	public static final String system_exception_no_pwd="系统异常 密码错误";
	public static final String system_exception_data="系统异常!数据库错误";
	public static final String no_disk_info="找不到对应的税控盘信息";
	/**
	 * "系统异常 ！创建xml 文件失败
	 */
	public static final String system_exception_Xml_error="系统异常,创建xml字符串失败";
	public static final String system_exception_disk_Xml_error="系统异常,创建税控盘xml字符串失败";
	public static final String system_exception_bill_cancel_Xml_error="系统异常,创建发票作废xml字符串失败";
	
	public static final String update_bill_cancel_result_error="更改票据状态失败";
	public static final String update_bill_print_result_error="更改打印结果失败";
	public static final String system_exception_tax_Xml_error="系统异常,创建税种税目xml字符串失败";
	public static final String system_exception_buy_bill_Xml_error="系统异常,创建购票信息查询xml字符串失败";
	public static final String system_exception_bill_issue_Xml_error="系统异常,创建发票开具 xml字符串失败";
	public static final String system_exception_bill_print_Xml_error="系统异常,创建打印Xml字符串失败";
	public static final String system_exception_bill_cur_bill_no_Xml_error="系统异常,创建当前发票号码Xml字符串失败";
	public static final String system_exception_bill_param_Xml_error="系统异常,创建参数设置Xml字符串失败";
	public static final String system_exception_tax_key_Xml_error="系统异常,创建税控钥匙Xml字符串失败";
	
	/**
	 * 系统异常！创建Json文件失败
	 */
	public static final String system_exception_bill_cancel_Json_error="系统异常,创建发票作废Json字符串失败";
	public static final String system_exception_bill_print_Json_error="系统异常,创建打印Json字符串失败";
	
	public static final String system_exception_update_print_error="系统异常,更改打印结果失败";
	public static final String tax_disk_mon_info_save_erroe="税控盘监控信息保存失败";
	public static final String parse_disk_mon_info_erroe="解析税控盘监控信息失败";
	public static final String parse_tax_Item_info_query_erroe="解析税种税目信息失败";
	public static final String parse_bill_Issue_info_query_erroe="解析开具信息失败";
	public static final String parse_print_Xml_error="解析打印结果失败";
	public static final String parse_bill_cancel_Xml_error="解析作废结果失败";
	public static final String parse_bill_cancel_Xcur_bill_no_xml_error="解析当前票号结果失败"; 
	public static final String parse_bill_param_set_xml_error="解析参数设置结果失败"; 
	public static final String parse_bill_tax_key_xml_error="解析税控钥匙信息查询结果失败"; 
	public static final String stock_no_ch="该税控盘的此发票类型库存不足";
	public static final String single_print_limit_error="超过单次打印限定值不能打印";
	public static final String system_exception_update_bill_issue_datastauas_error="系统异常,发票开具更改状态失败";
	public static final String system_exception_update_bill_cancel_datastauas_error="系统异常,发票作废更改状态失败";
	public static final String tax_disk_info_save_success="税控盘保存成功";
	public static final String tax_disk_mon_info_save_success="税控盘监控信息保存成功";
	public static final String tax_Item_save_success="税目信息保存成功";
	public static final String save_tax_Item_save_error="税目信息保存失败";
	public static final String save_mon_info_save_error="监控信息保存失败";
	public static final String save_tax_disk_info_save_error="税控盘信息保存失败";
	public static final String pwd_no="密码 为空";
	
	
	
	
//	public static final String bill_issue_error="开具失败";
	private static Map<String, String> systemMessageMap = new HashMap<String, String>();
	static {
		systemMessageMap.put("", "");
	}
	private static Map<String, String> assionMessageMap = new HashMap<String, String>();
	static {
		assionMessageMap.put("", "");
	}
	private static Map<String, String> bwMessageMap = new HashMap<String, String>();
	static {
		bwMessageMap.put("", "");
	}

	public static String getMessage(String code) {
		return getMessage(null, code);
	}

	public static String getMessage(String interfaceType, String code) {
		String message = "";
		if (StringUtils.isEmpty(interfaceType)) {
			message = systemMessageMap.get(code);
		} else if (AssionDiskBillInterfaceServiceImpl.INTERFACE_TYPE.equals(interfaceType)) {
			message = assionMessageMap.get(code);
		} else if (BWDiskBillInterfaceServiceImpl.INTERFACE_TYPE.equals(interfaceType)) {
			message = bwMessageMap.get(code);
		}
		return message;
	}

}
