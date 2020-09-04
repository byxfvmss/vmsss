package com.cjit.vms.trans.model;

/**
 * XML标签类
 * 
 * @author
 */
public class XmlColumnInfo {
	private String bill_Type; // 发票类型 1-增值税专用发票、2-增值税普通发票、3-货物运输业增值税专用发票、4-机动车销售统一发票
	private String column_Id; // XML标签列
	private String column_Name; // XML标签列名称
	private String column_Table_Column;// XML标签列对应实体表的数据列
	private String column_Table_Name;// XML标签列对应实体表的数据列中文名称
	private String data_Type;// XML标签列对应实体表的数据列数据类型
	private String table_Id; // XML标签列对应实体表的数据列所在表
	private String order1; // 顺序
	private String is_Sub; // 1、是子节点 0、不是子节点
	private String parent_Label; // 子节点在哪个发票类型下
	private String remark; // 备注
	public String getBill_Type() {
		return bill_Type;
	}
	public void setBill_Type(String bill_Type) {
		this.bill_Type = bill_Type;
	}
	public String getColumn_Id() {
		return column_Id;
	}
	public void setColumn_Id(String column_Id) {
		this.column_Id = column_Id;
	}
	public String getColumn_Name() {
		return column_Name;
	}
	public void setColumn_Name(String column_Name) {
		this.column_Name = column_Name;
	}
	public String getColumn_Table_Column() {
		return column_Table_Column;
	}
	public void setColumn_Table_Column(String column_Table_Column) {
		this.column_Table_Column = column_Table_Column;
	}
	public String getColumn_Table_Name() {
		return column_Table_Name;
	}
	public void setColumn_Table_Name(String column_Table_Name) {
		this.column_Table_Name = column_Table_Name;
	}
	public String getData_Type() {
		return data_Type;
	}
	public void setData_Type(String data_Type) {
		this.data_Type = data_Type;
	}
	public String getTable_Id() {
		return table_Id;
	}
	public void setTable_Id(String table_Id) {
		this.table_Id = table_Id;
	}
	public String getOrder1() {
		return order1;
	}
	public void setOrder1(String order1) {
		this.order1 = order1;
	}
	public String getIs_Sub() {
		return is_Sub;
	}
	public void setIs_Sub(String is_Sub) {
		this.is_Sub = is_Sub;
	}
	public String getParent_Label() {
		return parent_Label;
	}
	public void setParent_Label(String parent_Label) {
		this.parent_Label = parent_Label;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	 
}
