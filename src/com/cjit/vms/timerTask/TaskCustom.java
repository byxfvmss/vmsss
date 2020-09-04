package com.cjit.vms.timerTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import com.cjit.vms.timerApp.SynchInvoiceInfo;
import com.cjit.vms.timerApp.getAccBalance;
import com.cjit.vms.timerApp.hexinInf;

public class TaskCustom extends TimerTask{
	private String taskId;
	//类名	Qualified Name
	private String taskName;
	//任务执行时间
	private String taskTime;
	//T-1 T-2	 推迟或提前执行任务模式
	private String type;
	//任务参数1
	private String para1;
	//任务参数2
	private String para2;
	
	private String para3;
	
	private String para4;
	
	private String para5;
	
	public TaskCustom(){}
	
	public TaskCustom(String taskId){
		
		this.taskId = taskId; 
		
	}
	public TaskCustom(String taskId,String taskName,String taskTime,
			String type,String para1,String para2,String para3,String para4,String para5){
			
			this.taskId = taskId; 
			
			this.taskName = taskName;
			
			this.taskTime = taskTime;
			
			this.type=type;
			
			this.para1 = para1;
			
			this.para2 = para2;
			
			this.para3 = para3;
			 
			this.para4 = para4;
			
			this.para5 = para5;
		}
	@Override
	public void run()
	  {
	    try
	    {
	      Calendar calendar = Calendar.getInstance();
	      
	      calendar.setTime(new Date());
	      
	      String dateString = this.taskTime;
	      if (this.type != null)
	      {
	        String days = this.type.substring(2);
	        if (this.type.contains("-")) {
	          calendar.add(5, -Integer.parseInt(days));
	        } else if (this.type.contains("+")) {
	          calendar.add(5, Integer.parseInt(days));
	        }
	        Date date = calendar.getTime();
	        
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        
	        dateString = simpleDateFormat.format(date);
	      }
	      Class<?> myclass = Class.forName(this.taskName);
	      
	      BaseTask task = (BaseTask)myclass.newInstance();
	      System.out.println(dateString);
	      task.base(dateString, this.para1, this.para2, this.para3, this.para4, this.para5);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }

}
