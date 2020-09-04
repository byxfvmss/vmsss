package com.cjit.commons.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 类说明:访问控制
 * @author E-mail:
 * @version
 * @since 2008-6-30 下午10:32:14
 */
public class ExceptionInterceptor implements Interceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -398682384754120821L;
	private static Logger log = Logger.getLogger(ExceptionInterceptor.class);
	public static final String EXCEPTION = "exception";

	public String intercept(ActionInvocation invocation) throws Exception{
		try{
			return invocation.invoke();
		}catch (Exception e){
			String message = "异常错误Action: " + invocation.getAction();
			log.error(message, e);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			invocation.getInvocationContext().put("exceptionMessage",
					e.getMessage());
			invocation.getInvocationContext().put("exceptionDetail",
					sw.toString());
			return EXCEPTION;
		}
	}

	public void destroy(){
	}

	public void init(){
	}
}
