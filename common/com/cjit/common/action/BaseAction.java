/**
 * 
 */
package com.cjit.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ognl.OgnlValueStack;

/**
 * 基本Action基类
 * @since Jun 20, 2008
 */
public class BaseAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware, SessionAware, ServletContextAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3212441513097661756L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext servletContext;
	protected Map session;

	public void addFieldToStack(String name, Object v){
		ActionContext context = ActionContext.getContext();
		OgnlValueStack stack = (OgnlValueStack) context.getValueStack();
		Map entity = new HashMap();
		entity.put(name, v);
		stack.push(entity);
	}

	public void addFieldToRequest(String name, Object v){
		request.setAttribute(name, v);
	}

	public void addFieldToSession(String name, Object v){
		session.put(name, v);
	}

	public Object getFieldFromSession(String name){
		return session.get(name);
	}

	public void setServletRequest(HttpServletRequest arg0){
		request = arg0;
	}

	public void setServletResponse(HttpServletResponse arg0){
		response = arg0;
	}

	public void setSession(Map arg0){
		session = arg0;
	}

	public void setServletContext(ServletContext arg0){
		servletContext = arg0;
	}
}
