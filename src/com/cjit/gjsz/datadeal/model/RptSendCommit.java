/**
 * 
 */
package com.cjit.gjsz.datadeal.model;

/**
 * @author wangxin
 */
public class RptSendCommit{

	// 物理字段属性
	private String businessId;
	private String tableId;
	private String packName;
	private String fileName;
	private String isReceive;
	private String isSendMts;

	public String getBusinessId(){
		return businessId;
	}

	public void setBusinessId(String businessId){
		this.businessId = businessId;
	}

	public String getTableId(){
		return tableId;
	}

	public void setTableId(String tableId){
		this.tableId = tableId;
	}

	public String getPackName(){
		return packName;
	}

	public void setPackName(String packName){
		this.packName = packName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getIsReceive(){
		return isReceive;
	}

	public void setIsReceive(String isReceive){
		this.isReceive = isReceive;
	}

	public RptSendCommit(){
	}

	public RptSendCommit(String tableId, String businessId, String rptNo){
		this.tableId = tableId;
		this.businessId = businessId;
	}

	public RptSendCommit(String tableId, String businessId, String packName,
			String fileName, String isReceive){
		this.tableId = tableId;
		this.businessId = businessId;
		this.packName = packName;
		this.fileName = fileName;
		this.isReceive = isReceive;
	}

	public String getIsSendMts() {
		return isSendMts;
	}

	public void setIsSendMts(String isSendMts) {
		this.isSendMts = isSendMts;
	}
}
