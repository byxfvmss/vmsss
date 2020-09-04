package com.cjit.vms.timerApp;

import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class getAccBalance implements BaseTask {
	private String date;

	public void getAccBalance() {
	}

	public boolean base(String date, String parse1, String parse2, String parse3, String parse4, String parse5)
			throws Exception {
		try {
			System.out.println("总账接口进程");

			Connection connection = DBUtil.getConnection();

			Calendar c = Calendar.getInstance();

			c.setTime(new Date());

			c.add(2, -1);

			Date date0 = c.getTime();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

			String tString = sdf.format(date0);

			String sql0 = "delete from VMS_ACC_BALANCE_INFO where YEAR_MONTH='" + tString + "'";

			PreparedStatement pst = connection.prepareStatement(sql0);

			ResultSet rs = pst.executeQuery();

			System.out.println(tString + "月的数据删除成功！");

			pst.close();
			rs.close();

			String sql = "insert into VMS_ACC_BALANCE_INFO(DIRECTION_ID,DIRECTION_NAME,YEAR_MONTH,OPERATE_DATE,DEBIT_SOURCE,CREDIT_SOURCE,DEBIT_DESC,CREDIT_DESC,BALANCE_SOURCE,BALANCE_DESC,DATA_SOURCE,VALITE_DATE)select DIRECTIONIDX,DIRECTIONIDXNAME,YEARMONTH,to_char(sysdate,'yyyy-mm-dd'),DEBITSOURCE,CREDITSOURCE,DEBITDEST,CREDITDEST,BALANCESOURCE,BALANCEDEST,'accditailbaltrace',to_char(sysdate,'yyyy-mm-dd')from ACCDETAILBALTRACE@acclink where YEARMONTH='"
					+

					tString + "'";

			PreparedStatement pst1 = connection.prepareStatement(sql);

			ResultSet rs1 = pst1.executeQuery();

			System.out.println("数据加载成功！");

			rs1.close();
			pst1.close();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}
}
