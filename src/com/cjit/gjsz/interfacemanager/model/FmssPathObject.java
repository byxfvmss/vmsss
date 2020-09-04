package com.cjit.gjsz.interfacemanager.model;

public class FmssPathObject{

	private String menu_order_num;
	private String menu_img_src;
	private String link_target;
	private String lick_site_url;
	private String unit_login_url;

	public String getMenu_order_num(){
		return menu_order_num;
	}

	public void setMenu_order_num(String menu_order_num){
		this.menu_order_num = menu_order_num;
	}

	public String getMenu_img_src(){
		return menu_img_src;
	}

	public void setMenu_img_src(String menu_img_src){
		this.menu_img_src = menu_img_src;
	}

	public String getLink_target(){
		return link_target;
	}

	public void setLink_target(String link_target){
		this.link_target = link_target;
	}

	public String getLick_site_url(){
		return lick_site_url;
	}

	public void setLick_site_url(String lick_site_url){
		this.lick_site_url = lick_site_url;
	}

	public String getUnit_login_url(){
		return unit_login_url;
	}

	public void setUnit_login_url(String unit_login_url){
		this.unit_login_url = unit_login_url;
	}
}
