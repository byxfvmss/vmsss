/**
 * UserInterface
 */
package com.cjit.gjsz.interfacemanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huboA
 */
public class UserInterface{

	private int id;
	private String name;
	private int startLine;
	private String fileType;
	private int primaryKey;
	private String separator;
	private String tableType;
	public static final List types = new ArrayList();
	public static final List separators = new ArrayList();
	static{
		types.add("txt");
		types.add("xls");
	}
	static{
		separators.add(",");
		separators.add("|");
	}

	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public int getStartLine(){
		return startLine;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setStartLine(int startLine){
		this.startLine = startLine;
	}

	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public int getPrimaryKey(){
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey){
		this.primaryKey = primaryKey;
	}

	public String getSeparator(){
		return separator;
	}

	public void setSeparator(String separator){
		this.separator = separator;
	}

	public String getTableType(){
		return tableType;
	}

	public void setTableType(String tableType){
		this.tableType = tableType;
	}
}
