/**
 * 
 */
package com.cjit.gjsz.datadeal.model;

/**
 * @author yulubin
 */
public class CodeDictionary{

	private String id;
	private String codeType;
	private String codeValueBank;
	private String codeValueStandardLetter;
	private String codeValueStandardNum;
	private String codeName;
	private String codeTypeDesc;
	private String codeSym;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getCodeType(){
		return codeType;
	}

	public void setCodeType(String codeType){
		this.codeType = codeType;
	}

	public String getCodeValueBank(){
		return codeValueBank;
	}

	public void setCodeValueBank(String codeValueBank){
		this.codeValueBank = codeValueBank;
	}

	public String getCodeValueStandardLetter(){
		return codeValueStandardLetter;
	}

	public void setCodeValueStandardLetter(String codeValueStandardLetter){
		this.codeValueStandardLetter = codeValueStandardLetter;
	}

	public String getCodeValueStandardNum(){
		return codeValueStandardNum;
	}

	public void setCodeValueStandardNum(String codeValueStandardNum){
		this.codeValueStandardNum = codeValueStandardNum;
	}

	public String getCodeName(){
		return codeName;
	}

	public void setCodeName(String codeName){
		this.codeName = codeName;
	}

	public String getCodeTypeDesc(){
		return codeTypeDesc;
	}

	public void setCodeTypeDesc(String codeTypeDesc){
		this.codeTypeDesc = codeTypeDesc;
	}

	public String getCodeSym(){
		return codeSym;
	}

	public void setCodeSym(String codeSym){
		this.codeSym = codeSym;
	}
}
