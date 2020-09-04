package com.cjit.vms.taxServer.action;


import java.io.PrintWriter;



import com.cjit.vms.trans.action.DataDealAction;

public class BaseTaxSelverAction extends DataDealAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getfapiaoType(String fapiao) {
		if (fapiao.equals("0")) {
			return "004";
		}
		return "007";
	}
	public void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();

	}
}
