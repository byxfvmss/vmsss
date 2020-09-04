package com.cjit.vms.taxServer.util;

public class TaxSelvetUtil {
	/**
	 * 参数管理 编号
	 */
	public static final String id_param = "20001";
	/**
	 * 税控钥匙编号
	 */
	public static final String id_key = "20002";
	/**
	 * 发票开具编号
	 */
	public static final String id_Issue = "10008";
	/**
	 * 发票作废
	 */
	public static final String id_Cancel = "10009";
	/**
	 * 发票查询
	 */
	public static final String id_Query = "10010";
	/**
	 * 发票领购信息查询
	 */
	public static final String id_buy_query = "10005";
	/**
	 *  发票打印 
	 */
	public static final String id_print = "20004";
	/**
	 * 打印边距设置
	 */
	public static final String id_print_mgr = "20003";
	/**
	 * 查询当前票号
	 */
	public static final String id_current_bill = "10004";
	
	/**
	 * 参数设置
	 */
	public static final String comment_paramSet = "参数设置";
	/**
	 * 税控钥匙信息查询 描述
	 */
	public static final String comment_servlet_key = "税控钥匙信息查询";
	/**
	 * 发票开具
	 */
	public static final String comment_bill_issue = "发票开具";
	/**
	 * 发票作废
	 */
	public static final String comment_bill_Cancel = "发票作废";
	/**
	 * 发票查询
	 */
	public static final String comment_bill_query = "发票查询";
	/**
	 * 发票领购信息查询
	 */
	public static final String comment_bill_buy = "发票领购信息查询";
	/**
	 * 发票打印
	 */
	public static final String comment_bill_print = "发票打印";
	/**
	 * 页边距设置
	 */
	public static final String comment_page_mgr = "页边距设置";
	/**
	 * 查询当前未开票号
	 */
	public static final String comment_current_bill = "查询当前未开票号";
	/**
	 * 服务器IP地址
	 */
	public static final String selvet_Ip_address = "123.124.177.178";
	/**
	 * 服务器端口号
	 */
	public static final String servlet_port = "10009";
	/**
	 * 税控钥匙口令
	 */
	public static final String Tax_key_pwd = "88888888";
	
	/**
	 * 北京百望税控钥匙安全码
	 */
	public static final String Tax_aqm = "7b71d5afa5470531bafa26a783dfa7dbb1e08f224ff8ad347b8485e46ef6b3c5f4090fce4410951eaf8e7dec3936885eb638b5a644c3de8df1c1ff7af8596d0ca3723c1b0e37e5417a575bf1b8deaf7923b2a6872200facc253b97375ef586a74c107163a7dbc638ade84e0385d92902eeec2b5e248c2e22d3f62828d7b89ce2e6e7eb0dbe3e48b9cdb59a173e744f8afcfdbfecbc8a23107b5f22ca9b7d7a2c1878f7dbae87a10c9f55278144be0e4cc60eb2d490d9f406a2c5459f1a7ae329d76a8f40601c9cdde5a2717510b23f270b09e271c4c3dba0dbe7e1535df95607cb1b0b207519c850d6fe1838985f4d85f431f62c89ed0fba6e71ec41dd2445db";
	/**
	 * 应用类型代码
	 */
	public static final String apply_type_ch = "1";
	/**
	 * 开票终端标示
	 */
	public static final String bill_TerminalFlag_ch = "7020";
	/**
	 * 空白作废
	 */
	public static final String bill_enmpty_cancel_Typech = "0";
	/**
	 * 已开具作废
	 */
	public static final String bill_cancel_Typech = "1";
	/**
	 * 返回代码
	 */
	public static final String return_code_ch = "returncode";
	/**
	 * 返回信息
	 */
	public static final String return_Msg_ch = "returnmsg";
	/**
	 * 返回数据
	 */
	public static final String return_data_ch = "returndata";
	/**
	 * 清单标志
	 */
	public static final String Detailed_Falg_0 = "0";
	/**清单标志
	 * 
	 */
	public static final String Detailed_Falg_ = "";
	/**
	 * 发票请求流水号
	 */
	public static final String Fapiao_Request_No_ = "";
	/**信息表编号
	 * 
	 */
	public static final String information_table_number_ = "";
	
	/**
	 * 开具类型
	 */
	public static final String Issue_Bill_Type_0 = "0";
	/**
	 * 开具类型
	 */
	public static final String Issue_Bill_Type_1 = "1";
	/**
	 * 签名参数
	 */
	public static final String Signature_param_= "";
	/**
	 * 特殊票种标识
	 */
	public static final String Special_Falg_00= "00";
	/**
	 * 特殊票种标识
	 */
	public static final String Special_Falg_01= "01";
	/**
	 * 特殊票种标识
	 */
	public static final String Special_Falg_02= "02";
	/**
	 * 发票行性质
	 */
	public static final String Bill_row_Property_0= "0";
	/**
	 * 发票行性质
	 */
	public static final String Bill_row_Property_1= "1";
	/**
	 * 发票行性质
	 */
	public static final String Bill_row_Property_2= "2";
	/**
	 * 商品税目
	 */
	public static final String Product_Tax_= "";
	/**含税标志
	 * 
	 */
	public static final String Tax_logo_0= "0";
	/**
	 * 含税标志
	 */
	public static final String Tax_logo_1= "1";
	/**
	 * 发票打印
	 */
	public static final String Print_Type_0= "0";
	/**
	 * 清单打印
	 */
	public static final String Print_Type_1= "1";
	/**
	 * 为每次弹出打印机确定选项
	 */
	public static final String print_Way_0= "0";
	/**
	 * 为弹出一次后，不在弹出打印机确定选项
	 */
	public static final String print_Way_1= "1";
	/**
	 * 税控参数 -税控服务器
	 */
	public static final String tax_Server_ch= "2";
	/**
	 * 税控参数哦-税控盘
	 */
	public static final String tax_disk_ch= "1";

	
}
