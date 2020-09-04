package com.cjit.vms.timerApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;
import com.cjit.vms.timerTask.dbpro;
import com.cjit.vms.timerTask.dbpro1;

public class HexinInfT extends TimerTask implements BaseTask {
	private Logger logger = Logger.getLogger(HexinInfT.class);
	private dbpro dbpro = new dbpro();
	public static HexinInfT hexinInf;
	private Connection connection;

	private Timer timer;
	private String date, path;
	private File datafileok;
	private boolean is;

	public boolean base(String date, String parse1, String parse2, String parse3, String parse4, String parse5)
			throws Exception {

		this.path = parse1;

		Calendar c = Calendar.getInstance();

		c.setTime(new Date());

		c.add(Calendar.DATE, Integer.parseInt(parse3));

		// Date date0 = c.getTime();
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//
		// String date1 = sdf.format(date0);

		this.date = date;

		timer = new Timer();
		long delay1 = 1 * 1000;
		long period1 = 1000 * 60 * 10;
		timer.schedule(HexinInfT.hexinInf, delay1, period1);

		return true;
	}

	@Override
	public void run() {

		String filenameok = "VMS" + date + "OK";
		this.datafileok = new File(path + filenameok + ".txt");

		logger.info("所需要的OK表为：" + datafileok);

		is = true;

		if (is) {

			if (datafileok.exists()) {

				try {

					is = false;

					String sql1 = "delete from VMS_CUSTOMER_INFO";

					connection = DBUtil.getConnection();
					connection.setAutoCommit(false);
					PreparedStatement pst1 = connection.prepareStatement(sql1);

					pst1.executeUpdate();
					connection.commit();
					pst1.close();

					// logger.info("开始解析JGKH... ...");
					// handleToCustomer("JGKH");
					// logger.info("解析表JGKH完成！");
					//
					// logger.info("开始解析GRKH... ...");
					// handleToCustomer("GRKH");
					// logger.info("解析表GRKH完成！");

					logger.info("开始解析GRKH... ...");
					handleToTrans("JY");
					logger.info("解析表JY完成！");

					creatFile();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				logger.info("ok表不存在！！");
			}

		} else {
			timer.cancel();
			logger.info("跳出往表中插入数据，准备跳出轮询... ...");
		}

	}

	private void handleToCustomer(String tablename) {

		File fileTxt = new File(path + "VMS" + date + tablename + ".txt");

		BufferedReader reader = null;
		String line = null;

		int count = 1;

		if (fileTxt.exists()) {

			try {

				InputStreamReader fReader;

				fReader = new InputStreamReader(new FileInputStream(fileTxt), "UTF-8");

				reader = new BufferedReader(fReader);

				StringBuilder sql = new StringBuilder();
				sql.append("insert into VMS_CUSTOMER_INFO(CUSTOMER_ID,CUSTOMER_CNAME,CUSTOMER_TAXNO,CUSTOMER_ADDRESS,");
				sql.append("TAXPAYER_TYPE,CUSTOMER_PHONE,CUSTOMER_CBANK,CUSTOMER_ACCOUNT,");
				sql.append("CUSTOMER_TYPE,FAPIAO_TYPE，DATA_SOURCE,CUSTOMER_FAPIAO_FLAG,CUSTOMER_NATIONALITY) values (?,?,?,?,?,?,?,?,?,'1','1',?,?)");
				PreparedStatement pst = connection.prepareStatement(sql.toString());

				if (tablename.equals("GRKH")) {
					tablename = "I";
				} else {

					if (tablename.equals("JGKH")) {
						tablename = "C";

					} else {
						tablename = "0";

					}
				}

				while ((line = reader.readLine()) != null) { // 对文件中的内容读取之后进行处理

					String strs[] = line.split("\n");

					PreparedStatement pp = null;

					for (int i = 0; i < strs.length; i++) {

						String[] strs1 = line.split("\\|%\\|\\|%\\|", -1);

						if (tablename.equals("GRKH")) {
							strs1[dbpro1.TAXPAYER_TYPE] = "O";
						}

						pp = customer(strs1, tablename, count, pst);
						count++;

					}

					pp.executeBatch();

				}

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			logger.info(fileTxt + "不存在！！");
		}

	}

	private PreparedStatement customer(String[] strs1, String tablename, int count, PreparedStatement pst) {

		try {

			if (strs1[0] != null && !strs1[0].equals("")) {

				pst.setString(1, strs1[0]);

				pst.setString(2, strs1[1]);

				pst.setString(3, strs1[2]);// CUSTOMER_TAXNO

				pst.setString(4, strs1[3]);// CUSTOMER_ADDRESS

				pst.setString(5, strs1[4]);// TAXPAYER_TYPE

				pst.setString(6, strs1[5]);// CUSTOMER_PHONE

				pst.setString(7, strs1[6]);// CUSTOMER_CBANK

				pst.setString(8, strs1[7]);// CUSTOMER_ACCOUNT

				pst.setString(9, tablename);

				pst.setString(10, "M");

				pst.setString(11, "CHN");

				pst.addBatch();

			}

			if (count % 10000 == 0) {

				pst.executeBatch();

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return pst;

	}

	private void handleToTrans(String tablename) {

		File fileTxt = new File(path + "VMS" + date + tablename + ".txt");

		BufferedReader reader = null;
		String line = null;

		int count = 1;

		if (fileTxt.exists()) {

			try {

				InputStreamReader fReader = new InputStreamReader(new FileInputStream(fileTxt), "UTF-8");
				reader = new BufferedReader(fReader);
				StringBuilder sql =new StringBuilder();
				sql.append("insert into VMS_TRANS_INFO (");
				sql.append("TRANS_ID,TTMPRCNO,CHERNUM,REPNUM,CUSTOMER_ID,TRANS_CURR,AMT_CCY,AMT_CNY,TRANS_DATE,");
				sql.append("FAPIAO_TYPE,TRANS_TYPE,BILLFREQ,POLYEAR,INSTCODE,ZNTCODE05,PLANLONGDESC,");
				sql.append("INCOME_CNY,INCOME_CCY,TAX_AMT_CNY,TAX_AMT_CCY," + "VAT_RATE_CODE,TAX_RATE,");
				sql.append("DATASTATUS,IS_REVERSE,TRANS_FAPIAO_FLAG,TRANS_FLAG,BALANCE,TAX_CNY_BALANCE) ");
				sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				PreparedStatement pst = connection.prepareStatement(sql.toString());

				while ((line = reader.readLine()) != null) { 
					// 对文件中的内容读取之后进行处理

					String strs[] = line.split("\n");


					for (int i = 0; i < strs.length; i++) {

						String[] strs1 = line.split("\\|%\\|\\|%\\|", -1);

						trans(strs1, count, pst);

						count++;

					}


				}
				pst.executeBatch();
				connection.commit();
				fReader.close();
				reader.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			finally {
				connection = null;
			}

		} else {
			logger.info(fileTxt + "不存在！！");
		}

	}

	private PreparedStatement trans(String[] strs1, int count, PreparedStatement pst) {

		try {
			String inst = strs1[dbpro.INSTCODE];
			String temp1 = inst;
			StringBuilder sql1=new StringBuilder();
			sql1.append("select temp as temp from common_code where code='");
			sql1.append(inst);
			sql1.append("' and type='inst_id'");
			PreparedStatement pst1 = connection.prepareStatement(sql1.toString());
			ResultSet rs = pst1.executeQuery();
			if (rs.next()) {
				temp1 = rs.getString("temp");
			} else {
				temp1 = inst;
			}
			pst1.close();

			Double code_name = null;
			String vat = strs1[dbpro.VAT_RATE_CODE];
			StringBuilder sql2=new StringBuilder();
			sql2.append("select code_name as code_name from t_code_dictionary where ");
			sql2.append("code_type='VAT_RATE_TYPE_NEW' and code_value_standard_letter='");
			sql2.append(vat);
			sql2.append("'");
			PreparedStatement pst2 = connection.prepareStatement(sql2.toString());
			ResultSet rs2 = pst2.executeQuery();

			if (rs2.next()) {
				String cn = rs2.getString("code_name");
				code_name = Double.parseDouble(cn);
			} else {
				logger.info("t_code_dictionary表中没有" + vat + "对应的税率！！！");
			}
			pst2.close();

			pst.setString(1, strs1[dbpro.TRANS_ID]);
			pst.setString(2, strs1[dbpro.TTMPRCNO]);
			pst.setString(3, strs1[dbpro.CHERNUM]);
			pst.setString(4, strs1[dbpro.REPNUM]);
			pst.setString(5, strs1[dbpro.CUSTOMER_ID]);
			pst.setString(6, strs1[dbpro.TRANS_CURR]);
			pst.setString(7, strs1[dbpro.AMT_CCY]);
			pst.setString(8, strs1[dbpro.AMT_CNY]);
			pst.setString(9, strs1[dbpro.TRANS_DATE]);

			pst.setString(10, strs1[dbpro.FAPIAO_TYPE]);
			pst.setString(11, strs1[dbpro.TRANS_TYPE]);
			pst.setString(12, strs1[dbpro.BILLFREQ]);
			/* pst.setInt(13, Integer.parseInt(strs[dbpro.POLYEAR])); */

			if (strs1[dbpro.POLYEAR].equals("")) {
				pst.setNull(13, Types.INTEGER);
			} else {
				pst.setInt(13, Integer.parseInt(strs1[dbpro.POLYEAR]));
			}

			pst.setString(14, temp1);
			pst.setString(15, strs1[dbpro.ZNTCODE05]);
			pst.setString(16, strs1[dbpro.PLANLONGDESC]);

			pst.setDouble(17, Double.parseDouble(strs1[dbpro.INCOME_CNY]));
			pst.setDouble(18, Double.parseDouble(strs1[dbpro.INCOME_CNY]));

			pst.setDouble(19, Double.parseDouble(strs1[dbpro.TAX_AMT_CNY]));
			pst.setDouble(20, Double.parseDouble(strs1[dbpro.TAX_AMT_CNY]));

			pst.setString(21, strs1[dbpro.VAT_RATE_CODE]);
			pst.setDouble(22, code_name);

			pst.setString(23, "1");
			pst.setString(24, "N");
			pst.setString(25, "M");
			pst.setString(26, "2");

			pst.setDouble(27, Double.parseDouble(strs1[dbpro.AMT_CNY]));
			pst.setDouble(28, Double.parseDouble(strs1[dbpro.TAX_AMT_CNY]));

			pst.addBatch();

			if (count % 1000 == 0) {

				pst.executeBatch();
				connection.commit();
				pst.clearBatch();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return pst;

	}

	private void creatFile() throws ParseException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String ss = sdf.format(new Date());

		String okfile = path + "VMS" + ss + ".OK";

		File ff = new File(okfile);

		logger.info("计划生成的.OK文件为：" + ff);

		if (!ff.exists()) {

			ff.createNewFile();

			logger.info(".OK文件生成成功！");

		} else {
			logger.info(".OK文件存在！！！");
		}

	}

	public static void main(String[] args) {
		HexinInfT.hexinInf = new HexinInfT();
		try {
			hexinInf.base("20160705", "G:\\data\\", "0", "0", "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
