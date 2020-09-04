package com.cjit.vms.trans.util.taxDisk;

/**
 * @author tom
 *
 */
public class TaxDiskUtil {
	/*注册码信息导入  	Import_registration_code_information
	税控盘信息查询  	tax_disk_information_query

	监控数据查询    	Mon_data_query
	税种税目信息查询	Tax_taxable_items_information_query
	购票信息查询		buy_ticket_information_query
	发票开具		bill_issue
	发票作废		bill_Canccel
	发票查询		bill_query
	发票打印		bill_print
	页边距设置		page_Mar_set*/
/*	*//****
	*注册码信息导入 
	***/

	public static final String comment_Import_registration_code_information="注册码信息导入 ";
	/****
	*税控盘信息查询 
	***/

	public static final String comment_tax_disk_information_query="税控盘信息查询 ";
	/****
	*监控数据查询
	***/

	public static final String comment_Mon_data_query="监控数据查询";
	/****
	*税种税目信息查询
	***/

	public static final String comment_Tax_taxable_items_information_query="税种税目信息查询";
	/****
	*购票信息查询
	***/

	public static final String comment_buy_ticket_information_query="购票信息查询";
	/****
	*发票开具
	***/

	public static final String comment_bill_issue="发票开具";
	/****
	*发票作废
	***/

	public static final String comment_bill_Canccel="发票作废";
	/****
	*发票查询
	***/

	public static final String comment_bill_query="发票查询";
	/****
	*发票打印
	***/

	public static final String comment_bill_print="发票打印";
	/****
	*页边距设置
	***/

	public static final String comment_page_Mar_set="页边距设置";
	/****
	*注册码信息导入 
	***/

	public static final String id_Import_registration_code_information="ZCMDR";
	/****
	*税控盘信息查询 
	***/

	public static final String id_tax_disk_information_query="SKPXXCX";
	/****
	*监控数据查询
	***/

	public static final String id_Mon_data_query="JKSJCX";
	/****
	*税种税目信息查询
	***/

	public static final String id_Tax_taxable_items_information_query="SZSMCX";
	/****
	*购票信息查询
	***/

	public static final String id_buy_ticket_information_query="GPXXCX";
	/****
	*发票开具
	***/

	public static final String id_bill_issue="FPKJ";
	/****
	*发票作废
	***/

	public static final String id_bill_Canccel="FPZF";
	/****
	*发票查询
	***/

	public static final String id_bill_query="FPCX";
	/****
	*发票打印
	***/

	public static final String id_bill_print="FPDY";
	/****
	*页边距设置
	***/

	public static final String id_page_Mar_set="YBJSZ";
	/**
	 * 1	yylxdm	应用类型代码	1	是	1：国税2：地税

	 */
	public static final String Application_type_code_1="1";
	/**
	 * 	
	 * 	yylxdm	应用类型代码	1	是	1：国税2：地税

	 */
	public static final String Application_type_code_2="2";



	
	public static void main(String[] args) {
		String data="注册码信息导入 ,Import_registration_code_information;税控盘信息查询 ,tax_disk_information_query;监控数据查询,Mon_data_query;税种税目信息查询,Tax_taxable_items_information_query;购票信息查询,buy_ticket_information_query;发票开具,bill_issue;发票作废,bill_Canccel;发票查询,bill_query;发票打印,bill_print;页边距设置,page_Mar_set;";
		StringBuffer staticdata=new StringBuffer();
		String[] infors=data.split(";");
		for(int i=0;i<infors.length;i++){
			String[] datas=infors[i].split(",");
			staticdata.append("/****\r\n*").append(datas[0]+"\r\n").append("***/\r\r").append("public static final String id_").append(datas[1]).append("="+"\"").append("\""+";\r\n");
		}
		System.out.println(staticdata);
	}
}
