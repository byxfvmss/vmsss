package cjit.crms.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.trans.service.billPreOpen.BillPreOpenService;

public class SpringTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext app= new ClassPathXmlApplicationContext(new String[]{
				"classpath*:config/spring/spring-base.xml",                    
				"classpath*:config/spring/spring-dao.xml",                     
				"classpath*:config/spring/spring-service.xml",                 
				"classpath*:config/spring/spring-action.xml",                  
				"classpath*:config/spring/spring-datadeal.xml",                
				"classpath*:config/spring/spring-filem.xml",                   
				"classpath*:config/spring/spring-system.xml",                  
				"classpath*:config/spring/spring-vms.xml",                     
				"classpath*:config/spring/spring-vms-aisino.xml",              
				"classpath*:config/spring/spring-vms-createBill.xml",          
				"classpath*:config/spring/spring-vms-input.xml  ",             
				"classpath*:config/spring/spring-vms-job.xml",                 
				"classpath*:config/spring/spring-vms-metlife.xml",             
				"classpath*:config/spring/spring-vms-billinterface.xml",       
				"classpath*:config/spring/spring-vms-billTransRelate.xml"  
		});
		BillCancelDiskAssitService serveice = (BillCancelDiskAssitService) app.getBean("billCancelDiskAssitService");
		serveice.openBillCancelTrans("B2016043000000007404");
	}

}
