/**
 * Dictionary
 */
package com.cjit.gjsz.interfacemanager.model;

/**
 * @author huboA
 * @table t_code_dictionary
 */
public class Dictionary{

	private int id;
	private String type;
	private String valueBank;
	private String valueStandardLetter;
	private String valueStandardNum;
	private String name;
	private String typeName;
	private String tableId;
	private String typeId;
	private String backupNum;

	public String getBackupNum(){
		return backupNum;
	}

	public void setBackupNum(String backupNum){
		this.backupNum = backupNum;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getType(){
		return type;
	}

	public String getValueBank(){
		return valueBank;
	}

	public String getValueStandardLetter(){
		return valueStandardLetter;
	}

	public String getValueStandardNum(){
		return valueStandardNum;
	}

	public String getName(){
		return name;
	}

	public void setType(String type){
		this.type = type;
	}

	public void setValueBank(String valueBank){
		this.valueBank = valueBank;
	}

	public void setValueStandardLetter(String valueStandardLetter){
		this.valueStandardLetter = valueStandardLetter;
	}

	public void setValueStandardNum(String valueStandardNum){
		this.valueStandardNum = valueStandardNum;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getTypeName(){
		return typeName;
	}

	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public String getTypeId(){
		return typeId;
	}

	public void setTypeId(String typeId){
		this.typeId = typeId;
	}
}
