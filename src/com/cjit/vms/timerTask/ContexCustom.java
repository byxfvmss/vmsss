package com.cjit.vms.timerTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;



import com.cjit.gjsz.cache.CacheabledMap;


/**
 * ddd
 * @author Administrator
 *
 */

public class ContexCustom extends HttpServlet implements ServletContextListener{
	
	//提示信息
	private  Logger logger = Logger.getLogger(this.getClass().getName()+".java");
	
	
	public void init(){
		
		System.out.println("计时器启动!!");
		autoTask();
	}
	
	public void ContexCustom(ServletContextEvent servletcontextevent){
		CacheabledMap.WEBAPP_PATH = servletcontextevent.getServletContext()
				.getRealPath("");
		System.out.println("1——————————————————————————"+CacheabledMap.WEBAPP_PATH );
	}
	
	public void contextDestroyed(ServletContextEvent servletcontextevent){
	}
	
	
	
	

	public void contextInitialized(ServletContextEvent arg0) {
		
		try {
			
			autoTask();
			
		} catch (Exception e) {
		
			logger.info("监控启动中......");
			
			e.printStackTrace();
		}
		
	}
	
	
	public static void autoTask(){
		
		TaskService taskService = new TaskService();
		
		Connection connection = null;
		
		ResultSet rs = null;
		
		PreparedStatement pst = null;
		
		String sql = "select * from timerSwitch where flag = 0";
		
		try {
			
			connection = DBUtil.getConnection();
			
			pst = connection.prepareStatement(sql);
			
			rs = pst.executeQuery();
			
			System.out.println("开始读取数据");
			if(null!=rs){
			while(rs.next()){
				System.out.println("第"+rs.getRow()+"条");
				String taskId = rs.getString(1);
				
				String taskName = rs.getString(2);
				
				String taskTime = rs.getString(3);
				
				String type = rs.getString(4);
				
				String para1 = rs.getString(5);
				
				String para2 = rs.getString(6);
				
				String para3 = rs.getString(7);
				
				String para4 = rs.getString(8);
				
				String para5 = rs.getString(9);
				System.out.println("任务id"+taskId+"任务名称"+taskId+"任务时间");
		        taskService.submitData(taskId, taskName, taskTime, type, para1, para2, para3, para4,para5);
				
			}
			}else{
				System.out.println("没有可执行任务");
			}
		} catch (SQLException e) {
			
			//logger.warn("提交数据失败");
			
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBUtil.close(connection, pst);
			
		}
		
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("计时器启动!!");
		autoTask();
	}
	
}
