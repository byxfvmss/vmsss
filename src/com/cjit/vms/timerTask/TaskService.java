package com.cjit.vms.timerTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import org.apache.log4j.Logger;

public class TaskService extends Timer {
	
	private Logger logger = Logger.getLogger(this.getClass().getName()+".java");
	
	public SimpleDateFormat tSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static Calendar c = Calendar.getInstance();
	
	private long mPeriod=1000*60*60*24;
	
	public TaskService(){
	}
	
	public static Long getOneDayMillis(){
		c.setTime(new Date());
		Long l1=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH,-1);
		Long l2=c.getTimeInMillis();
		return l1-l2;
	}
	
	public void submitData(String taskId,String taskName,String taskTime,String type,String para1,
			String para2,String para3,String para4,String para5) {
		
		System.out.println("进入submitDate方法");
		
		TaskCustom taskCustom = new TaskCustom(taskId, taskName, taskTime,type,para1,
				para2,para3,para4,para5);

		
		Date myDate = null;
		
		try {
			//日期转换
			myDate = tSimpleDateFormat.parse(taskTime);
			
		} catch (ParseException e) {
			
			logger.warn("TaskService。submitData  日期转换异常");
			
			e.printStackTrace();
		}
		//判断执行时间是否过时并处理
		long tCurrentTime = new Date().getTime();//当前时间(毫秒)
		long runtime = 1000*60*60*24;
		long period1 = 1000 * 60 * 10;
		long delay1 = 1 * 1000;
			if(tCurrentTime > myDate.getTime()){//当前时间在计划时间之后
				System.out.println("过时任务计划执行时间"+getStrDate(myDate));
				long tNextDayProjectedTime = ((tCurrentTime - myDate.getTime())/this.mPeriod + 1)*this.mPeriod + myDate.getTime();
				myDate = new Date(tNextDayProjectedTime);
				System.out.println("当前时间"+getStrDate(new Date(tCurrentTime)));
				System.out.println("过时任务延迟后执行时间"+getStrDate(myDate));
					this.schedule(taskCustom,delay1,period1);
			}else{
				System.out.println("正常任务计划执行时间"+getStrDate(myDate));
					this.schedule(taskCustom,myDate,runtime);			
			}
	}
	
	public String getStrDate(Date date){
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	}
//	public static void main(String[] args) throws Exception {
//		Timer timer = new Timer();
//		String dateString = "2016-06-02 17:27:33";
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		timer.scheduleAtFixedRate(new TimerTask() {
//			@Override
//			public void run() {
//			System.out.println("******");
//				
//			}
//		}, simpleDateFormat.parse(dateString), 10000);
//	}
}
