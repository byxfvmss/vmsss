package com.cjit.gjsz.logic.model;

import java.math.BigDecimal;

/**
 * @作者: lihaiboA
 * @日期: Sep 9, 2012 3:40:52 PM
 * @描述: [Self_Sub_CREDITOR]债权人信息
 */
public class Self_Sub_CREDITOR extends SelfSubEntity{

	private String creditorcode; // 债权人代码
	private String creditorname; // 中文名称
	private String creditornamen; // 英文名称
	private BigDecimal creditorca; // 签约金额
	private String creditortype; // 类型代码
	private String crehqcode; // 总部所在国家（地区）代码
	private String opercode; // 经营地所在国家（地区）代码

	public String getCreditorcode(){
		return creditorcode;
	}

	public void setCreditorcode(String creditorcode){
		this.creditorcode = creditorcode;
	}

	public String getCreditorname(){
		return creditorname;
	}

	public void setCreditorname(String creditorname){
		this.creditorname = creditorname;
	}

	public String getCreditornamen(){
		return creditornamen;
	}

	public void setCreditornamen(String creditornamen){
		this.creditornamen = creditornamen;
	}

	public BigDecimal getCreditorca(){
		return creditorca;
	}

	public void setCreditorca(BigDecimal creditorca){
		this.creditorca = creditorca;
	}

	public String getCreditortype(){
		return creditortype;
	}

	public void setCreditortype(String creditortype){
		this.creditortype = creditortype;
	}

	public String getCrehqcode(){
		return crehqcode;
	}

	public void setCrehqcode(String crehqcode){
		this.crehqcode = crehqcode;
	}

	public String getOpercode(){
		return opercode;
	}

	public void setOpercode(String opercode){
		this.opercode = opercode;
	}
}
