package com.cjit.gjsz.common.homenote.xml;


public class HomeDataCellDO{

	private String name;

	private String value;

	private String target;

	private String key;

	private String url;

	private String sysId;

	private String menuId;

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

	public HomeDataCellDO(){
	};

	/**
	 * @param name
	 * @param value
	 * @param target
	 * @param key
	 * @param url
	 */
	public HomeDataCellDO(String name, String value, String target, String key,
			String url, String sysId, String menuId){
		this.name = name;
		this.value = value;
		this.target = target;
		this.key = key;
		this.url = url;
		this.sysId = sysId;
		this.menuId = menuId;
	};

	public HomeDataCellDO(String name, String value){
		this.name = name;
		this.value = value;
	}

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

	public String getTarget(){
		return this.target;
	}

	public void setTarget(String target){
		this.target = target;
	}

	public String getKey(){
		return this.key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getUrl(){
		return this.url;
	}

	public void setUrl(String url){
		this.url = url;
	}

}
