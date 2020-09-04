package com.cjit.vms.timerApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.record.formula.functions.Replace;

import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;


/*import com.cjit.vms.listener.StrTool;*/


public class getAccBalance_beifen_2019  implements BaseTask{
	
	private String date;

	public void getAccBalance(){
		
	}
	  public static void main(String[] args)
			    throws Exception
			  {
		     getAccBalance_beifen_2019 hexinInf = new getAccBalance_beifen_2019();
			    hexinInf.base("2019-10-10", "D:\\00zzs\\", "", "0", "", "");
			  }

	
	public boolean base(String date,String parse1,String parse2,String parse3,String parse4,String parse5) throws Exception {
		
		
		try{
			
		 System.out.println("张静静的进程");	
	    
         Connection   connection = DBUtil.getConnection();
         
         
         this.date=date;
			
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
 		
 		Date d = sdf.parse(date);
 		
 		
 	
 		Calendar c=Calendar.getInstance();
 		
 		c.setTime(d);
 		
 		Date ddd=c.getTime();
 		
 		
 		
 		c.add(Calendar.MONTH, -1);
 		
 		
 		
         //String tString=sdf.format(c.getTime()).replace("-", "");
 		String tString="201910";
             
 	    System.out.println("tSting="+tString);	
         
         
         
         
         
         String sql0="delete from VMS_ACC_BALANCE_INFO where YEAR_MONTH='"+tString+"'";
			
         PreparedStatement	pst = connection.prepareStatement(sql0);
			
		 ResultSet rs = pst.executeQuery();
		
		 System.out.println("数据删除成功！");	
		String sql="insert into VMS_ACC_BALANCE_INFO("
				+ "DIRECTION_ID,DIRECTION_NAME,YEAR_MONTH,OPERATE_DATE,DEBIT_SOURCE,CREDIT_SOURCE,DEBIT_DESC,CREDIT_DESC,"
				+ "BALANCE_SOURCE,BALANCE_DESC,DATA_SOURCE,VALITE_DATE)"
				+ "select "
				+ "DIRECTIONIDX,DIRECTIONIDXNAME,YEARMONTH,to_char(sysdate,'yyyy-mm-dd'),DEBITSOURCE,CREDITSOURCE,DEBITDEST,CREDITDEST,BALANCESOURCE,"
				+ "BALANCEDEST,'accditailbaltrace',to_char(sysdate,'yyyy-mm-dd')"
				+ "from ACCDETAILBALTRACE@acclink where YEARMONTH='"+tString+"'";	
		
		 PreparedStatement	pst1 = connection.prepareStatement(sql);
			
		 ResultSet rs1 = pst1.executeQuery();
		
		 System.out.println("数据加载成功！");
		
	
	 /*   rs0.close();  
	    pstmt0.close(); */
	  /*  
	    rs.close();  
	    pstmt.close(); */
	    
		}catch(SQLException e){
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	    
}


	

	
	
}