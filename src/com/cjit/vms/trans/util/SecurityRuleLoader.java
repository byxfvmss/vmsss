package com.cjit.vms.trans.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import com.cjit.vms.trans.util.SecurityRuleLoader;

public class SecurityRuleLoader {
	
	private static final Log log = LogFactory.getLog(SecurityRuleLoader.class);
	
	static String bundleName = "/config/passwordcheck.xml";
	
	static List rules = new ArrayList();
	
	static {
		initProperties();
	}
	
	private static void initProperties() {
		InputStream in = null;
		try {
			in = SecurityRuleLoader.class.getClassLoader().getResource(bundleName).openConnection().getInputStream();
			if (in == null) {
				throw new RuntimeException("Properties not found:" + bundleName);
			}
			String s = IOUtils.toString(in, "gbk");
			XStream xs = new XStream(new DomDriver());
			rules = (List) xs.fromXML(s);
		}
		catch (Exception e) {
			log.error("load xml error:passwordcheck1.xml", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static boolean isValid() {
		return rules.size() > 0;
	}

	public static List getRules() {
		return rules;
	}
}

class SecurityRule {
	private String name;
	private String value;
	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
