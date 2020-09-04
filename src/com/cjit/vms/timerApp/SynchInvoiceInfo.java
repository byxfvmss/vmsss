package com.cjit.vms.timerApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;

// 金旭：修改表功能；


public class SynchInvoiceInfo implements BaseTask{
	public boolean base(String date,String parse1,String parse2,String parse3,String parse4,String parse5) throws Exception {
		Connection conn = DBUtil.getConnection();
		String sql = "select * from vms_input_invoice_new_temp v where v.flag = '1'";
		PreparedStatement pStatement = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = pStatement.executeQuery();
		resultSet.last();
		//获取查询到的数据的条数
		int count = resultSet.getRow();
		if(count <= 0){
			return true;
		}
		//将光标移动到此resultSet的开头，即第一行
		resultSet.beforeFirst();
		while (resultSet.next()) {
			//发票代码
			String billCode=resultSet.getString("BILL_CODE");
			//发票号码
			String billNo=resultSet.getString("BILL_NO");
			//认证状态
			String datastatus=resultSet.getString("DATASTATUS");
			//用途
			String porpuseCode=resultSet.getString("PORPUSE_CODE");
			String tUpdateTemp="";
			//用途为应税并通过认证
			if (ConstantUtil.PORPUSE_CODE_TAXABLE.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				tUpdateTemp="UPDATE VMS_INPUT_INVOICE_NEW_TEMP A "
						+ " SET A.FLAG=?,A.DATASTATUS=?,A.TRANSFER_STATUS=?,A.DEDUC_AMT=A.AMT,A.DEDUC_DATE=TO_CHAR(SYSDATE,'YYYY-MM-DD') "
						+ " WHERE A.BILL_CODE=? AND A.BILL_NO=? ";
				pStatement=conn.prepareStatement(tUpdateTemp);
				pStatement.setString(1, ConstantUtil.FLAG_CHANGE);
				pStatement.setString(2, ConstantUtil.DATASTATUS_DEDUCTION);
				pStatement.setString(3, ConstantUtil.TRANSFER_STATUS_NO);
				pStatement.setString(4, billCode);
				pStatement.setString(5, billNo);
				pStatement.executeUpdate();
			}
			//用途为进项转出并通过认证
			if (ConstantUtil.PORPUSE_CODE_INPUT_OUT.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				tUpdateTemp="UPDATE VMS_INPUT_INVOICE_NEW_TEMP A "
						+ "SET A.FLAG=?,A.DATASTATUS=?,A.TRANSFER_STATUS=?,A.TRANSFER_AMT=A.AMT "
						+ "WHERE A.BILL_CODE=? AND A.BILL_NO=?";
				pStatement=conn.prepareStatement(tUpdateTemp);
				pStatement.setString(1, ConstantUtil.FLAG_CHANGE);
				pStatement.setString(2, ConstantUtil.DATASTATUS_ROLL_OUT);
				pStatement.setString(3, ConstantUtil.TRANSFER_STATUS_YES);
				pStatement.setString(4, billCode);
				pStatement.setString(5, billNo);
				pStatement.executeUpdate();
			}
			String tUpdateSql="UPDATE VMS_INPUT_INVOICE_NEW A "
					+ "SET A.INST_ID=?, "//1    //修改为INDUSTRY_TYPE
					+ "A.INST_NAME=?, "//2
					+ "A.PORPUSE_CODE=?,"//3
					+ "A.PORPUSE=?,"//4
					+ "A.VENDOR_NAME=?,"//5
					+ "A.TAX_NO=?,"//6
					+ "A.TAX_RATE=?,"//7
					+ "A.AMT_TAX_SUM=?,"//8
					+ "A.DATA_STATUS=?,"//9
					+ "A.DIRECTION_ID=?,"//10
					+ "A.DIRECTION_NAME=?,"//11
					+ "A.TRANSFER_AMT=?,"//12
					+ "A.TRANSFER_STATUS=?,"//13
					+ "A.DEDUC_AMT=?,"//14
					+ "A.DEDUC_DATE=TO_CHAR(SYSDATE,'YYYY-MM-DD'),"
					+ "A.BILL_TYPE=?,"//15
					+ "A.DATA_SOURCE=? "//16
					+ "WHERE A.BILL_CODE=? AND A.BILL_NO=?";//17
			pStatement=conn.prepareStatement(tUpdateSql);
			pStatement.setString(1, resultSet.getString("INST_ID"));
			pStatement.setString(2, resultSet.getString("INST_NAME"));
			pStatement.setString(3, resultSet.getString("PORPUSE_CODE"));
			pStatement.setString(4, resultSet.getString("PORPUSE"));
			pStatement.setString(5, resultSet.getString("VENDOR_NAME"));
			pStatement.setString(6, resultSet.getString("TAX_NO"));
			pStatement.setFloat(7, resultSet.getFloat("TAX_RATE"));
			pStatement.setFloat(8, resultSet.getFloat("AMT_TAX_SUM"));
			if (ConstantUtil.PORPUSE_CODE_TAXABLE.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				pStatement.setString(9, ConstantUtil.DATASTATUS_DEDUCTION);
				pStatement.setString(13,ConstantUtil.TRANSFER_STATUS_NO);
			}
			if (ConstantUtil.PORPUSE_CODE_INPUT_OUT.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				pStatement.setString(9, ConstantUtil.DATASTATUS_ROLL_OUT);
				pStatement.setString(13,ConstantUtil.TRANSFER_STATUS_YES);
			}
			pStatement.setString(10, resultSet.getString("DIRECTION_ID"));
			pStatement.setString(11, resultSet.getString("DIRECTION_NAME"));
			pStatement.setFloat(12, resultSet.getFloat("TRANSFER_AMT"));
			pStatement.setFloat(14, resultSet.getFloat("DEDUC_AMT"));
			pStatement.setString(15, resultSet.getString("BILL_TYPE"));
			pStatement.setString(16, resultSet.getString("DATA_SOURCE"));
			pStatement.setString(17, resultSet.getString("BILL_CODE"));
			pStatement.setString(18, resultSet.getString("BILL_NO"));
			pStatement.executeUpdate();
			
			String accInsertSql="INSERT INTO VMS_ACC_INVOICE_INFO A VALUES(ACC.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)";
			pStatement=conn.prepareStatement(accInsertSql);
			pStatement.setString(1, resultSet.getString("PORPUSE_CODE"));
			pStatement.setString(2, resultSet.getString("INST_ID"));
			pStatement.setString(3, resultSet.getString("INST_NAME"));
			pStatement.setString(4, resultSet.getString("BILL_CODE"));
			pStatement.setString(5, resultSet.getString("BILL_NO"));
			pStatement.setFloat(6, resultSet.getFloat("AMT"));
			pStatement.setFloat(7, resultSet.getFloat("TAX_RATE"));
			pStatement.setFloat(8, resultSet.getFloat("TAX_AMT"));
			pStatement.setFloat(9, resultSet.getFloat("AMT_TAX_SUM"));
			if (ConstantUtil.PORPUSE_CODE_TAXABLE.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				pStatement.setString(10, ConstantUtil.DATASTATUS_DEDUCTION);
				pStatement.setNull(12, Types.FLOAT);
				pStatement.setFloat(13, resultSet.getFloat("AMT"));
			}
			if (ConstantUtil.PORPUSE_CODE_INPUT_OUT.equals(porpuseCode)&&ConstantUtil.DATASTATUS_YES.equals(datastatus)) {
				pStatement.setString(10, ConstantUtil.DATASTATUS_ROLL_OUT);
				pStatement.setFloat(12, resultSet.getFloat("AMT"));
				pStatement.setNull(13, Types.FLOAT);
			}
			pStatement.setString(11, resultSet.getString("DIRECTION_ID"));
			
			pStatement.setString(14, null);
			pStatement.executeUpdate();
			
			System.out.println("另一程序执行完毕");
		}

		try {
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
		
	}
	public static void main(String[] args) {
		SynchInvoiceInfo ss=new SynchInvoiceInfo();
		try {
			ss.base("", "", "", "", "", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
}