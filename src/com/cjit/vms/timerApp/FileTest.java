package com.cjit.vms.timerApp;

//管浩：文件解析——交易表处理
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;
/**
 * 销项交易信息
 * 客户信息
 * 产品详细信息的INS_COD对应销项交易信息表ITEM_CODE
 * @author Administrator
 *
 */


public class FileTest implements BaseTask{
	static WriteErrorMessage wem = new WriteErrorMessage();
	private static File datafileerror;
	private static Object pst;
	
	
	

	public boolean base(String date, String parse1, String parse2,
			String parse3, String parse4, String parse5) throws Exception {
		
		//"C://Users//Administrator//Desktop//进项抵扣//";
		
		String path = parse1;        //文件路径
		String filedate = date;      //执行日期
		String tablename = parse2;   //表名
		String filename="";
		if(filedate.contains("-")){          //得到表名中的日期
			filename = filedate.replace("-", "");
			filename=filename.substring(0,8);
		}
		
		
		
		//构造完整文件名
		filename = "VMS"+filename+tablename;   //文件名称  不带扩展名
		String filenameok = filename+"OK";
		String filenameerror = filename+"ERROR";
		File datafile = new File(path+filename+".txt");
		File datafileok= new File(path+filenameok+".txt");
		File datafileerror= new File(path+filenameerror+".txt");
		
		
		FileTest.datafileerror=datafileerror;
		
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datafile)));
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datafileerror)));
		
		System.out.println(path);
		
		System.out.println(datafile.exists()&&datafileok.exists());
		
		if(datafile.exists()&&datafileok.exists()){
			
			System.out.println("判断文件"+new Date());
			
			handleFile(datafile);
				
				
		}else{
			wem.WriteError(datafileerror, "数据源文件未传输完成");
		}
		return true;
	}

	
	
	
	//对文件进行处理的方法
	//读取文件信息	(按行读数据  不能有回车)
	public static void handleFile(File file) {
		
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader(file));
			
			
		} catch (FileNotFoundException e1) {
			wem.WriteError(datafileerror, "数据源文件未找到");
			e1.printStackTrace();
		}
		
		
		String line = null;
		
			try {
				
				while((line=reader.readLine())!=null){
					//对文件中的内容读取之后进行处理
					System.out.println(line);
					handleLine(line);
					
				}
				reader.close();
			} catch (IOException e) {
				wem.WriteError(datafileerror, "数据源文件读取失败");
				e.printStackTrace();
			}
	}
	
	
	
	/**对读取到的内容
	 * @param line
	 */
	public static void handleLine(String line){
		
		String strs[] = line.split("%\\|");
		
		for(String str:strs){
			
			//对内容进行切割   切割之后变成一条条的数据
			handleLine2(str);
		}
	}
	
	
	/**对单条数据进行处理的方法
	 * @param line
	 */
	public static void handleLine2(String line){
		
		line = line.substring(2);
		
		String[] strs = line.split("\\|");
		
		try {
			handleDb(strs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	} 
	
	
	/**对数据库操作的总类
	 * @param strs
	 * @throws Exception
	 */
	public static void handleDb(String[] strs) throws Exception{	
		//对主数据库操作的类
		//handleTest(strs);
		//对用户表操作的类
		//handToCustomer(strs);
		
		
		//对交易表操作的类
		handToDeal(strs);
		/*handToCustomer(strs);*/
	}
	/**
	 * TRANS_ID 交易ID
	 * CHERNUM  保单号
	 * REPNUM  旧保单号
	 * TTMPRCNO  投保单号
	 * CUSTOMER_ID  客户ID
	 * TRANS_CURR 交易币种
	 * AMT_CCY	交易原币金额    NUMBER(16.2)
	 * TRANS_DATE	交易时间
	 * FaPiao_Type 发票类型
	 * FEETYP  费用类型
	 * BILLFREQ  缴费频率
	 * POLYEAR  保单年度    NUMBER
	 * PLANLONGDESC  主险名称
	 * TRANS_TYPE  交易类型   INSTCODE
	 * VAT_RATE_CODE  增值税种类
	 * 
	 * AMT_CNY  金额_人民币(价税合并)  AMT_CNY+TAX_AMT_CNY  NUMBER(16.2)
	 * TAX_AMT_CNY  税额_人民币  NUMBER(16.2)
	 * INCOME_CNY  收入_人民币    AMT_CNY  NUMBER(16.2)
	 * 
	 * AMT_CNY  金额
	 * TAX_AMT_CNY  税额
	 */
	private static void handToDeal(String[] strs) {
		
		Connection conn = null;
		
		String sql = "insert into VMS_TRANS_INFO (TRANS_ID,CHERNUM,REPNUM,TTMPRCNO," +
				"CUSTOMER_ID,TRANS_CURR,AMT_CCY,TRANS_DATE,FAPIAO_TYPE,FEETYP,BILLFREQ,POLYEAR,PLANLONGDESC," +
				"TRANS_TYPE,VAT_RATE_CODE,AMT_CNY,TAX_AMT_CNY,INCOME_CNY) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement pst = null;
		
		try {
			conn=DBUtil.getConnection();
			pst=conn.prepareStatement(sql);

			
		//数据库中顺序写死了     当插入数据时根据配置的顺序来插入到数据库中
		pst.setString(1, strs[getKeyIndex("TRANS_ID")]);//1
		pst.setString(2, strs[getKeyIndex("CHERNUM")]);//2
		pst.setString(3, strs[getKeyIndex("REPNUM")]);//3
		pst.setString(4, strs[getKeyIndex("TTMPRCNO")]);//4
		pst.setString(5, strs[getKeyIndex("CUSTOMER_ID")]);//5
		pst.setString(6, strs[getKeyIndex("TRANS_CURR")]);//12
		pst.setDouble(7, Double.parseDouble(strs[getKeyIndex("AMT_CCY")]));//13
		pst.setString(8, strs[getKeyIndex("TRANS_DATE")]);//15
		pst.setString(9, strs[getKeyIndex("FaPiao_Type")]);//17
		pst.setString(10, strs[getKeyIndex("FEETYP")]);//18
		pst.setString(11, strs[getKeyIndex("BILLFREQ")]);//19
		pst.setInt(12, Integer.parseInt(strs[getKeyIndex("POLYEAR")]));//20
		pst.setString(13, strs[getKeyIndex("PLANLONGDESC")]);//21
		//TRANS_TYPE对应配置文件中的INSTCODE
		pst.setString(14, strs[getKeyIndex("INSTCODE")]);//22
		pst.setString(15, strs[getKeyIndex("VAT_RATE_CODE")]);//27
		pst.setDouble(16, Double.parseDouble(strs[getKeyIndex("AMT_CNY")])+Double.parseDouble(strs[getKeyIndex("TAX_AMT_CNY")]));
		pst.setDouble(17, Double.parseDouble(strs[getKeyIndex("TAX_AMT_CNY")]));//26
		pst.setDouble(18, Double.parseDouble(strs[getKeyIndex("AMT_CNY")]));//25
		pst.execute();
		
		} catch (Exception e) {
			wem.WriteError(datafileerror, e.toString()+"数据源文件数据存储失败/n"+"预存储表名：VMS_TRANS_INFO/n"+pst.toString());
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, pst);
		}
	}
	
	
	/**
	 * @param strs  要进行切割的字符串
	 * @param userName 数据库连接的用户名
	 * @param url   
	 * @param userPwd
	 * @throws SQLException 
	 */
	public static void handToCustomer(String[] strs) throws Exception{
		
		
		/*CUSTOMER_ID      客户号 
		CUSTOMER_CNAME   客户纳税人中文名称
		CUSTOMER_TAXNO   客户纳税人识别号
		CUSTOMER_ADDRESS  客户地址
		TAXPAYER_TYPE     客户纳税人类别
		CUSTOMER_PHONE    客户电话
		CUSTOMER_CBANK    客户开户银行中文名称
		CUSTOMER_ACCOUNT  客户开户账号*/
		

		String sql = "insert into vms_customer_info(CUSTOMER_ID,CUSTOMER_CNAME,CUSTOMER_TAXNO,"
				+ "CUSTOMER_ADDRESS,TAXPAYER_TYPE,CUSTOMER_PHONE,CUSTOMER_CBANK,"
				+ "CUSTOMER_ACCOUNT values(?,?,?,?,?,?,?,?)";
		
		Connection connection = DBUtil.getConnection();
		
		PreparedStatement pst = connection.prepareStatement(sql);
		
		
		try {
					
			
			pst.setInt(1, Integer.parseInt(strs[getKeyIndex("CUSTOMER_ID")]));//5
			
			pst.setString(2, strs[getKeyIndex("CUSTOMER_CNAME")]);//6
			
			pst.setString(3, strs[getKeyIndex("CUSTOMER_TAXNO")]);//7
			
			pst.setString(4, strs[getKeyIndex("CUSTOMER_ADDRESS")]);//8
			
			pst.setString(5, strs[getKeyIndex("CUSTOMER_PHONE")]);//9
			
			pst.setString(6, strs[getKeyIndex("CUSTOMER_CBANK")]);//10
		
			pst.setString(7, strs[getKeyIndex("CUSTOMER_ACCOUNT")]);//11
			
			pst.setString(8, strs[getKeyIndex("TRANS_CURR")]);//12
			
			pst.execute();
					
			
		}catch (Exception e) {
			wem.WriteError(datafileerror, e.toString()+"数据源文件数据存储失败/n"+"预存储表名：vms_customer_info/n"+pst.toString());
			e.printStackTrace();
		}finally{
			DBUtil.close(connection, pst);
		}
		
		
	}
	
	/**这个表示对主表进行插入数据的方法
	 * @param strs
	 * @throws Exception
	 */
//	public static void handleTest(String[] strs) throws Exception{
//
//		Connection connection = DBUtil.getConnection();
//		
//		String sql = "insert into vms_input_test values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		
//		PreparedStatement pst = connection.prepareStatement(sql);
//		
//		//数据库中顺序写死了     当插入数据时根据配置的顺序来插入到数据库中
//		pst.setString(1, strs[getKeyIndex("TRANS_ID")]);
//		
//		pst.setString(2, strs[getKeyIndex("CHERNUM")]);
//		
//		pst.setString(3, strs[getKeyIndex("REPNUM")]);
//		
//		pst.setString(4, strs[getKeyIndex("TTMPRCNO")]);
//		
//		pst.setInt(5, Integer.parseInt(strs[getKeyIndex("CUSTOMER_ID")]));
//		
//		pst.setString(6, strs[getKeyIndex("CUSTOMER_CNAME")]);
//		
//		pst.setString(7, strs[getKeyIndex("CUSTOMER_TAXNO")]);
//		
//		pst.setString(8, strs[getKeyIndex("CUSTOMER_ADDRESS")]);
//		
//		pst.setString(9, strs[getKeyIndex("CUSTOMER_PHONE")]);
//		
//		pst.setString(10, strs[getKeyIndex("CUSTOMER_CBANK")]);
//		
//		pst.setString(11, strs[getKeyIndex("CUSTOMER_ACCOUNT")]);
//		
//		pst.setString(12, strs[getKeyIndex("ORIGCURR")]);
//		
//		pst.setInt(13, Integer.parseInt(strs[getKeyIndex("ORIGAMT")]));
//		
//		pst.execute();
//
//		DBUtil.close(connection, pst);
//	}
	
	public static Integer getKeyIndex(String str) throws IOException{
		InputStream inputStrem = new BufferedInputStream(new FileInputStream("db.properties"));
		
		Properties properties = new Properties();
		
		properties.load(inputStrem);
		
		System.out.println(str);
		System.out.println(properties.getProperty(str));
		
		return (Integer.parseInt(properties.getProperty(str))-1);
	}
	
	
}
