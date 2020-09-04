/**
 * 
 */
package com.cjit.gjsz.common.homenote.model;

import java.sql.Date;

/**
 * @author sunzhan
 */
public class PubHomeNote{

	private String noteType;
	private String noteName;
	private String noteNum;
	private String noteUrl;
	private String noteUserId;
	private Date noteTime;
	private String menuId;

	public String getMenuId(){
		return menuId;
	}

	public void setMenuId(String menuId){
		this.menuId = menuId;
	}

	public String getNoteType(){
		return noteType;
	}

	public void setNoteType(String noteType){
		this.noteType = noteType;
	}

	public String getNoteName(){
		return noteName;
	}

	public void setNoteName(String noteName){
		this.noteName = noteName;
	}

	public String getNoteNum(){
		return noteNum;
	}

	public void setNoteNum(String noteNum){
		this.noteNum = noteNum;
	}

	public String getNoteUrl(){
		return noteUrl;
	}

	public void setNoteUrl(String noteUrl){
		this.noteUrl = noteUrl;
	}

	public String getNoteUserId(){
		return noteUserId;
	}

	public void setNoteUserId(String noteUserId){
		this.noteUserId = noteUserId;
	}

	public Date getNoteTime(){
		return noteTime;
	}

	public void setNoteTime(Date noteTime){
		this.noteTime = noteTime;
	}
}
