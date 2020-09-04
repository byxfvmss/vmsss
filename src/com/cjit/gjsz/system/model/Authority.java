/**
 * 权限PO
 */
package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @author huboA
 */
public class Authority implements Serializable{

	private static final long serialVersionUID = -1195032677719224364L;
	private int id;
	private String name;
	private int parentId;
	private String urls;
	private int status = 1; // 0表示不启用 1表示启用

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public String getUrls(){
		return urls;
	}

	public void setUrls(String urls){
		this.urls = urls;
	}

	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getParentId(){
		return parentId;
	}

	public void setParentId(int parentId){
		this.parentId = parentId;
	}
}
