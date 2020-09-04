package com.cjit.vms.taxdisk.tools;

public class TaxDict {//税务字典
	
	/*引入js参数*/
	private String code_name;
	/*引入object参数*/
	private String code_type_desc;
	
	public String getCode_name() {
		return code_name==null?"":code_name;
	}
	public void setCode_name(String code_name) {
		this.code_name = code_name;
	}
	public String getCode_type_desc() {
		return code_type_desc==null?"":code_type_desc;
	}
	public void setCode_type_desc(String code_type_desc) {
		this.code_type_desc = code_type_desc;
	}

}
