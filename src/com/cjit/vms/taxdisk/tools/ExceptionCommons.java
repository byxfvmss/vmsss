package com.cjit.vms.taxdisk.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionCommons {

	public static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}