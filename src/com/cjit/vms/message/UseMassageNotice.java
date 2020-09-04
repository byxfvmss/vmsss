package com.cjit.vms.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.service.impl.BWServletBillInterfaceServiceImpl;
import com.cjit.vms.timerTask.BaseTask;
import com.cjit.vms.timerTask.DBUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UseMassageNotice{
	
	Logger logger = Logger.getLogger(UseMassageNotice.class);
	
	
	/*public static void main(String[] args){
		UseMassageNotice uu=new UseMassageNotice();
		try {
			uu.callSMSIterface("核心系统批处理短信通知测试！！！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
	/*public boolean base(String date, String parse1, String parse2,
			String parse3, String parse4, String parse5) throws Exception {
		
		System.out.println("短信：核心数据批处理");	
		UseMassageNotice uu=new UseMassageNotice();
		try {
			uu.callSMSIterface("张静静进行短信测试！！！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}*/
	
	
	
	
	
	
	public void callSMSIterface(String sendMsg,String toWho)throws Exception{
		//组织要发送的短信数据XML
	    String sMSData=CreateSMSData(sendMsg,toWho);
		//调用短信接口
	    try{
	    	SysSendServicePortTypeProxy si=new SysSendServicePortTypeProxy();
			si.send(sMSData);
	    }catch(Exception e){
	    	logger.error("核心系统票据信息导入情况短信通知失败",e);
	    	System.out.println("核心系统票据信息导入情况短信通知失败,具体原因是："+e.getMessage());
	    	e.printStackTrace();
	    	throw new RuntimeException("核心系统票据信息导入情况短信通知失败,具体原因是："+e.getMessage());
	    }
	}
	
	private String CreateSMSData(String sendMsg,String toWho){
 
		  String smsDataStr = ""
			  +"<?xml version=\"1.0\" encoding=\"gb2312\"?>"
			  +"<Messages>"
			  +"	<PublicInfo>"
			  +"		<SystemCode>ZZS</SystemCode>"
			  +"		<ServiceCode>ZengZS001</ServiceCode>"
			  +"		<User>"+encodeMD5("ZengZS").toUpperCase()+"</User>"
			  +"		<Password>"+encodeMD5("1q2w3e4R").toUpperCase()+"</Password>"
		      +"    </PublicInfo>"
		      +"    <Message>"
		      +"    	<MessageId>000000001</MessageId>"
		      +"	    <MobileNums>";
		      List pl = getSQLPhoneNum(toWho);
			  for(int i=0;i<pl.size();i++)
			  {
			  smsDataStr
				     +="<MobileNum>"+pl.get(i)+"</MobileNum>";
			  }
		      smsDataStr
		      +="    	</MobileNums>"
		      +"    	<MessTopic>核心系统票据信息导入情况短信通知</MessTopic>"
		      +"		<SendData>"+sendMsg+"</SendData>"
		      +"		<DataType>1</DataType>"
		      +"		<SendWay>0</SendWay>"
		      +"		<FixedDate />"
		      +"		<FixedTime />"
		      +"		<UnitCode>86</UnitCode>"
		      +"		<DealOrder>0</DealOrder>"
		      +"		<AnswerMatch>0</AnswerMatch>"
		      +"	</Message>"
			      +"</Messages>"
			  ;
		      
		  logger.info("推送短信接口信息："+smsDataStr);
		  System.out.println("推送短信接口信息："+smsDataStr);
		  return smsDataStr;
  		  
    }
	
	private List getSQLPhoneNum(String toWho){

		Connection conn;
		List list = new ArrayList<String>();
		
		try {
			
			String sql1 = "select c.code as code from  common_code c where c.type='"+toWho+"'";
			conn = DBUtil.getConnection();
			PreparedStatement pst1 = conn.prepareStatement(sql1);
			ResultSet rs=pst1.executeQuery();
			while(rs.next()){
				list.add(rs.getString("code"));
			}
			pst1.close();
			conn.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
		
	}
	
	
	
	
	public String encodeMD5(String plainText) {  
        String md5 = new String();  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(plainText.getBytes());  
            byte b[] = md.digest();  
  
            int i;  
  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
  
            md5 = buf.toString();  
  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return md5;  
    }

	

	
	
	
	
	

}
