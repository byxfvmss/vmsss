package com.cjit.gjsz.interfacemanager.model;

/**
 * 
 * @作者: sunzhan
 * @日期: 2009-7-10 下午02:00:49
 * @描述: [UBaseSysParamDO]请在此简要描述类的功能
 */
public class UBaseSysParamDO{

	/** 参数id */
	private Integer praram_id;
	/** 子系统id */
	private String system_Id;
	/** 参数类别 */
	private String type;
	/** 参数类别说明 */
	private String type_desc;
	/** 参数项英文 */
	private String item_ename;
	/** 参数项中文 */
	private String item_cname;
	/** 参数项值 */
	private String selected_value;
	/** 参数项值列表 */
	private String value_list;
	/** 是否可修改 */
	private String is_Mofify;
	/** 是否可见 */
	private String is_Visible;

	public Integer getPraram_id(){
		return praram_id;
	}

	public void setPraram_id(Integer praram_id){
		this.praram_id = praram_id;
	}

	public String getSystem_Id(){
		return system_Id;
	}

	public void setSystem_Id(String system_Id){
		this.system_Id = system_Id;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType_desc(){
		return type_desc;
	}

	public void setType_desc(String type_desc){
		this.type_desc = type_desc;
	}

	public String getItem_ename(){
		return item_ename;
	}

	public void setItem_ename(String item_ename){
		this.item_ename = item_ename;
	}

	public String getItem_cname(){
		return item_cname;
	}

	public void setItem_cname(String item_cname){
		this.item_cname = item_cname;
	}

	public String getSelected_value(){
		return selected_value;
	}

	public void setSelected_value(String selected_value){
		this.selected_value = selected_value;
	}

	public String getValue_list(){
		return value_list;
	}

	public void setValue_list(String value_list){
		this.value_list = value_list;
	}

	public String getIs_Mofify(){
		return is_Mofify;
	}

	public void setIs_Mofify(String is_Mofify){
		this.is_Mofify = is_Mofify;
	}

	public String getIs_Visible(){
		return is_Visible;
	}

	public void setIs_Visible(String is_Visible){
		this.is_Visible = is_Visible;
	}
}
