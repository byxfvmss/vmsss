package com.cjit.gjsz.interfacemanager.model;

import java.io.Serializable;

public class ThemeObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String theme_id;
	public String theme_Path;
	public String is_used;

	public String getIs_used(){
		return is_used;
	}

	public void setIs_used(String is_used){
		this.is_used = is_used;
	}

	public String getTheme_id(){
		return theme_id;
	}

	public void setTheme_id(String theme_id){
		this.theme_id = theme_id;
	}

	public String getTheme_Path(){
		return theme_Path;
	}

	public void setTheme_Path(String theme_Path){
		this.theme_Path = theme_Path;
	}
}
