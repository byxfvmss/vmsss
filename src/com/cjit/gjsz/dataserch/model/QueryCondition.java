package com.cjit.gjsz.dataserch.model;

public class QueryCondition{

	private String columnIdFirst; // 第一个查询列
	private String columnIdSecond; // 第二个查询列
	private String columnIdThird; // 第三个查询列
	private int opFirst; // 第一个操作符
	private int opSecond; // 第二个操作符
	private int opThird; // 第三个操作符
	private String valueFirst; // 第一个查询值
	private String valueSecond; // 第二个查询值
	private String valueThird; // 第三个查询值

	public String getColumnIdFirst(){
		return columnIdFirst;
	}

	public void setColumnIdFirst(String columnIdFirst){
		this.columnIdFirst = columnIdFirst;
	}

	public String getColumnIdSecond(){
		return columnIdSecond;
	}

	public void setColumnIdSecond(String columnIdSecond){
		this.columnIdSecond = columnIdSecond;
	}

	public String getColumnIdThird(){
		return columnIdThird;
	}

	public void setColumnIdThird(String columnIdThird){
		this.columnIdThird = columnIdThird;
	}

	public int getOpFirst(){
		return opFirst;
	}

	public void setOpFirst(int opFirst){
		this.opFirst = opFirst;
	}

	public int getOpSecond(){
		return opSecond;
	}

	public void setOpSecond(int opSecond){
		this.opSecond = opSecond;
	}

	public int getOpThird(){
		return opThird;
	}

	public void setOpThird(int opThird){
		this.opThird = opThird;
	}

	public String getValueFirst(){
		return valueFirst;
	}

	public void setValueFirst(String valueFirst){
		this.valueFirst = valueFirst;
	}

	public String getValueSecond(){
		return valueSecond;
	}

	public void setValueSecond(String valueSecond){
		this.valueSecond = valueSecond;
	}

	public String getValueThird(){
		return valueThird;
	}

	public void setValueThird(String valueThird){
		this.valueThird = valueThird;
	}
}
