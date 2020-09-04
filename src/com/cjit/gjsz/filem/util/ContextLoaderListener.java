package com.cjit.gjsz.filem.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cjit.gjsz.cache.CacheabledMap;

public class ContextLoaderListener implements ServletContextListener{

	public void contextInitialized(ServletContextEvent servletcontextevent){
		CacheabledMap.WEBAPP_PATH = servletcontextevent.getServletContext()
				.getRealPath("");
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent){
	}
}