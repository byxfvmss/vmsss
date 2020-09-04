package com.cjit.gjsz.common.homenote.xml;

public class HomeDataItemDO{

	private String name;

	private String value;

	private String url;

	private String sysId;

	private String menuId;

	public HomeDataItemDO(){
	};

	/**
	 * @param name
	 * @param value
	 * @param url
	 */
	public HomeDataItemDO(String name, String value, String url, String sysId,
			String menuId){
		this.name = name;
		this.value = value;
		this.url = url;
		this.sysId = sysId;
		this.menuId = menuId;
	};

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getValue(){
		return this.value;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getUrl(){
		return this.url;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getSysId(){
		return this.sysId;
	}

	public void setSysId(String sysId){
		this.sysId = sysId;
	}

	public String getMenuId(){
		return this.menuId;
	}

	public void setMenuId(String menuId){
		this.menuId = menuId;
	}
}
