package com.cjit.vms.timerApp;

import com.cjit.vms.message.UseMassageNotice;
import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;
import com.cjit.vms.timerTask.dbpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class hexinInf extends TimerTask implements BaseTask {
	dbpro dbpro = new dbpro();
	Connection connection;
	public static Timer timer;
	public static String date;
	public static String path;
	public static String parse3;
	public static File datafileVerf;	
	public static boolean success01 = true;
	public static boolean success02 = true;

	public static void main(String[] args) throws Exception {
		hexinInf hexinInf = new hexinInf();
		hexinInf.base("2019-11-20", "D:\\00zzs\\", "", "0", "", "");
	}

	public boolean base(String date, String parse1, String parse2, String parse3, String parse4, String parse5)
			throws Exception {
		this.path = parse1;
		this.parse3 = parse3;

		timer = new Timer();
		long delay1 = 1 * 1000;
		long period1 = 1000 * 60 * 10;
		// schedule(task, delay,period)
		// 等待delay毫秒后首次执行task，每隔period毫秒重复执行task服
		// 等待1秒之后首次执行task，每隔10分钟重复执行
		timer.schedule(new hexinInf(), delay1, period1);

		return true;
	}

	public void run() {
		Calendar c = Calendar.getInstance();

		c.setTime(new Date());

		c.add(Calendar.DATE, Integer.parseInt(parse3));

		Date date0 = c.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date1 = sdf.format(date0);

		date =date1;
         //1.判断数仓系统交易文件是否生成成功 .verf
		datafileVerf = new File(path + "VMS_EDW_TRANSACTION_INFOMATION_JY_" + date + ".verf");		
		String verfFile = path + "VMS_EDW_TRANSACTION_INFOMATION_JY_" + date + ".OK";
		File ok = new File(verfFile);
		
		String lockfile = path + "lock_" +"VMS_EDW_TRANSACTION_INFOMATION_JY_" +date + ".txt";
		File lock = new File(lockfile);

		System.out.println("处理" + date + "的JY数据。。。 。。。");

		if (datafileVerf.exists()) {
			if (!ok.exists()) {
				if (!lock.exists()) {
					try {
						lock.createNewFile();
						this.connection = DBUtil.getConnection();
						this.connection.setAutoCommit(false);
						System.out.println("开始解析JY... ...");
						handleToTrans("JY");
						System.out.println("。。。JY。。。");
						ok.createNewFile();
						System.out.println(".OK文件生成成功！");

						lock.delete();

						 UseMassageNotice umn=new UseMassageNotice();//创建短信发送类的对象
						 umn.callSMSIterface(date+":核心系统数据导入增值税系统批处理完成！","smsPhones");

						this.connection.close();
						System.out.println("关闭连接");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(lock + "   锁文件存在没解除：文件已经解析过了但存在错误没解析成功！！");
				}

			} else {
				System.out.println(date + "的.ok文件已经存在！文件已经成功解析！");
			}

		} else {
			System.out.println(datafileVerf + "文件不存在！！");
		}
	}

	private void handleToTrans(String tablename) {
		File fileTxt = new File(path + "VMS_EDW_TRANSACTION_INFOMATION_JY_" + date + "_001" + ".txt");
		BufferedReader reader = null;
		String line = null;
		int count = 1;
		int count77 = 1;
		System.out.println(fileTxt);
		if (fileTxt.exists()) {
			try {
				InputStreamReader fReader = new InputStreamReader(new FileInputStream(fileTxt), "GBK");
				reader = new BufferedReader(fReader);
				String sql = "insert into VMS_TRANS_INFO(TRANS_ID,TTMPRCNO,CHERNUM,CUSTOMER_ID,TRANS_CURR,AMT_CCY,AMT_CNY,TRANS_DATE,FAPIAO_TYPE,TRANS_TYPE,INSTCODE,ZNTCODE05,PLANLONGDESC,INCOME_CNY,INCOME_CCY,TAX_AMT_CNY,TAX_AMT_CCY,VAT_RATE_CODE,TAX_RATE,DATASTATUS,IS_REVERSE,TRANS_FAPIAO_FLAG,TRANS_FLAG,BALANCE,TAX_CNY_BALANCE,POLYEAR) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst = this.connection.prepareStatement(sql);
				while ((line = reader.readLine()) != null) {
					String[] strs = line.split("\n");
					for (int i = 0; i < strs.length; i++) {
						String[] strs1 = line.split("\001", -1);
						transInsert(strs1, count, pst);
						count++;
						count77++;
					}
				}
				pst.executeBatch();
				this.connection.commit();
				fReader.close();
				reader.close();
				success02 = true;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}

		} else {
			success02 = false;
			System.out.println(fileTxt + "不存在！！");
		}
	}
	private void transInsert(String[] strs1, int count, PreparedStatement pst) {
		try {
			String temp1 = "1000000000";
			String transCurr="CNY";
			Double code_name = null;
		    code_name = Double.valueOf(Double.parseDouble("0.06"));	
		    //1.交易Id 即  合同号 
			pst.setString(1, strs1[2]);
			//2.借据号
			pst.setString(2, strs1[1]);
			//3.合同号
			pst.setString(3, strs1[2]);
			//4.客户号
			pst.setString(4, strs1[3]);
			//5.交易币种 默认CNY
			pst.setString(5, transCurr);
			//6.放款金额（交易原币金额）
			pst.setDouble(6,Double.parseDouble(strs1[5]));
			//7.金额_人民币(价税合计)(目前没有为空)
			pst.setDouble(7, Double.parseDouble(strs1[6])*(1+0.06));
			//8交易时间
			pst.setString(8, strs1[4]);
			//9.发票类型 0-增值税专用发票/1-增值税普通发票
			pst.setString(9, "0");
			//10.交易类型(待定2019)
			pst.setString(10, "B");
			//11.所属机构
			pst.setString(11, temp1);
			//12.产品代码T5 {易日升（003439）}
			pst.setString(12, strs1[7]);
            //13.产品名称
				pst.setNull(13, 4);
			//14.收入_人民币（扣贴息金额）
			pst.setDouble(14, Double.parseDouble(strs1[6]));
			//15交易原币收入()
			pst.setDouble(15, Double.parseDouble(strs1[6]));
			//16.税额_人民币
			pst.setDouble(16,  Double.parseDouble(strs1[6])*(0.06));
			//17.交易原币税额
			pst.setDouble(17, Double.parseDouble(strs1[6])*(0.06));
			//18.增值税种类 S-6%/Z-0%/F-免税';
			pst.setString(18, "S");
			//19 税率
			pst.setDouble(19, code_name.doubleValue());
            //20.状态 1-未开票
			pst.setString(20, "1");
			//21.是否冲账 Y-是/N-否)
			pst.setString(21,"N");
			//22.交易是否打票 A-自动打印/M-手动打印/N-用不打印
			pst.setString(22, "M");
			//23.交易标志 1-权责发生/2-实收实付';)
			pst.setString(23, "2");
			//24.未开票金额（人民币
			pst.setDouble(24,Double.parseDouble(strs1[6])*(1+0.06));
			//25.未开票税额)
			pst.setDouble(25,Double.parseDouble(strs1[6])*(0.06));
			pst.setDouble(26, Double.parseDouble(getlastMonth()));
			pst.addBatch();
			if (count % 1000 == 0) {
				pst.executeBatch();
				this.connection.commit();
				pst.clearBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	private String getlastMonth() {
		
		SimpleDateFormat format  =new SimpleDateFormat("yyyyMM");
		Calendar c =Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date d=c.getTime();
		String mon=format.format(d);
		System.out.print("上个月日期"+mon);			
		return mon;
		  
	}
	private void handleToCustomer(String tablename)

	{
		System.out.println("handleToCustomer");
		File fileTxt = new File(path + "VMS" + date + tablename + ".txt");

		BufferedReader reader = null;
		String line = null;

		int count = 1;

		if (fileTxt.exists()) {
			try {
				InputStreamReader fReader = new InputStreamReader(new FileInputStream(fileTxt), "UTF-8");

				reader = new BufferedReader(fReader);

				String sql = "insert into VMS_CUSTOMER_INFO(CUSTOMER_ID,CUSTOMER_CNAME,CUSTOMER_TAXNO,CUSTOMER_ADDRESS,TAXPAYER_TYPE,CUSTOMER_PHONE,CUSTOMER_CBANK,CUSTOMER_ACCOUNT,CUSTOMER_TYPE,FAPIAO_TYPE,DATA_SOURCE,CUSTOMER_FAPIAO_FLAG,CUSTOMER_NATIONALITY) values (?,?,?,?,?,?,?,?,?,'1','2',?,?)";
				System.out.println("sql新增数据");
				PreparedStatement pst = this.connection.prepareStatement(sql);
				if (tablename.equals("GRKH")) {
					tablename = "I";
				} else if (tablename.equals("JGKH")) {
					tablename = "C";
				} else {
					tablename = "0";
				}

				while ((line = reader.readLine()) != null) {
					String[] strs = line.split("\n");

					for (int i = 0; i < strs.length; i++) {
						String[] strs1 = line.split("\n", -1);

						if (tablename.equals("GRKH")) {
							strs1[4] = "O";
						}

						customer(strs1, tablename, count, pst);
						count++;
					}

				}

				pst.executeBatch();

				this.connection.commit();

				fReader.close();
				reader.close();

				fileTxt.delete();// 2016-12-22

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					this.connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}

		} else {
			System.out.println(fileTxt + "不存在！！");
		}
	}

	private PreparedStatement customer(String[] strs1, String tablename, int count, PreparedStatement pst) {
		try {
			if ((strs1[0] != null) && (!strs1[0].equals(""))) {
				pst.setString(1, strs1[0]);

				pst.setString(2, strs1[1]);

				pst.setString(3, strs1[2]);

				pst.setString(4, strs1[3]);

				pst.setString(5, strs1[4]);

				pst.setString(6, strs1[5]);

				pst.setString(7, strs1[6]);

				pst.setString(8, strs1[7]);

				pst.setString(9, tablename);

				pst.setString(10, "M");

				pst.setString(11, "CHN");

				pst.addBatch();
			}

			if (count % 1000 == 0) {
				pst.executeBatch();
				this.connection.commit();
				pst.clearBatch();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}

		return pst;
	}

	private void transUpdate(String[] strs1, int count77, PreparedStatement pst77) {
		try {
			this.dbpro.getClass();
			pst77.setString(1, strs1[6]);
			this.dbpro.getClass();
			pst77.setString(2, strs1[7]);

			this.dbpro.getClass();
			pst77.setDouble(3, Double.parseDouble(strs1[17]));
			this.dbpro.getClass();
			pst77.setDouble(4, Double.parseDouble(strs1[17]));

			this.dbpro.getClass();
			pst77.setDouble(5, Double.parseDouble(strs1[18]));
			this.dbpro.getClass();
			pst77.setDouble(6, Double.parseDouble(strs1[18]));

			this.dbpro.getClass();
			pst77.setDouble(7, Double.parseDouble(strs1[7]));
			this.dbpro.getClass();
			pst77.setDouble(8, Double.parseDouble(strs1[18]));

			this.dbpro.getClass();
			pst77.setString(9, strs1[8]);
			this.dbpro.getClass();
			pst77.setString(10, strs1[0]);

			this.dbpro.getClass();
			pst77.setString(11, strs1[4]);
			this.dbpro.getClass();
			pst77.setString(12, strs1[1]);
			this.dbpro.getClass();
			pst77.setString(13, strs1[2]);
			pst77.setString(14, "1");
			this.dbpro.getClass();
			pst77.setString(15, strs1[11]);

			pst77.addBatch();

			if (count77 % 1000 == 0) {
				pst77.executeBatch();
				this.connection.commit();
				pst77.clearBatch();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				this.connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	
	
}