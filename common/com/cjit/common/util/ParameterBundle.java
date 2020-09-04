package com.cjit.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ParameterBundle{

	private static transient Logger logger = Logger
			.getLogger(ParameterBundle.class);
	static String bundleName = "config/config.properties";
	static Properties jdbc = new Properties();
	static{
		// initProperties();
	}

	private static void initProperties(){
		InputStream is = null;
		try{
			is = ParameterBundle.class.getClassLoader().getResource(bundleName)
					.openConnection().getInputStream();
			if(is == null){
				throw new RuntimeException("Properties not found:" + bundleName);
			}
			jdbc.load(is);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("load properites error", e);
		}finally{
			try{
				if(is != null){
					is.close();
				}
			}catch (IOException e){
				logger.warn("Could not close InputStream", e);
			}
		}
	}

	public static String getProperty(String name){
		String property = null;
		try{
			property = jdbc.getProperty(name);
		}catch (Exception e){
			logger.error("could not found properties:" + name, e);
		}
		if(property != null){
			return property;
		}else{
			return "";
		}
	}

	public static String getProperty(String name, String def){
		String property = null;
		try{
			jdbc = new Properties();
			initProperties();
			property = jdbc.getProperty(name, def);
		}catch (Exception e){
			logger.error("could not found properties:" + name, e);
		}
		if(property != null){
			return property;
		}else{
			return def;
		}
	}

	public static void main(String[] a){
		initProperties();
	}
}
