/**
 * 
 */
package com.cjit.gjsz.common.homenote.model;

import java.sql.Date;

/**
 * @author sunzhan
 */
public class PubHomeCell {

	private String cellType;
	private String cellTitle;
	private String cellTarget;
	private String cellUrl;
	private String cellKeyid;
	private Date cellDate;
	private String cellDesc;
	private String cellUserId;
	private String dataTime;

	private String cellDateStr;

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public String getCellTitle() {
		return cellTitle;
	}

	public void setCellTitle(String cellTitle) {
		this.cellTitle = cellTitle;
	}

	public String getCellTarget() {
		return cellTarget;
	}

	public void setCellTarget(String cellTarget) {
		this.cellTarget = cellTarget;
	}

	public String getCellUrl() {
		return cellUrl;
	}

	public void setCellUrl(String cellUrl) {
		this.cellUrl = cellUrl;
	}

	public String getCellKeyid() {
		return cellKeyid;
	}

	public void setCellKeyid(String cellKeyid) {
		this.cellKeyid = cellKeyid;
	}

	public Date getCellDate() {
		return cellDate;
	}

	public void setCellDate(Date cellDate) {
		this.cellDate = cellDate;
	}

	public String getCellDesc() {
		return cellDesc;
	}

	public void setCellDesc(String cellDesc) {
		this.cellDesc = cellDesc;
	}

	public String getCellUserId() {
		return cellUserId;
	}

	public void setCellUserId(String cellUserId) {
		this.cellUserId = cellUserId;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public String getCellDateStr() {
		return cellDateStr;
	}

	public void setCellDateStr(String cellDateStr) {
		this.cellDateStr = cellDateStr;
	}

}
