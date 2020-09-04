package cjit.logger;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LogDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private long logId;
	private String userId;
	private String userEname;
	private String userCname;
	private String instId;
	private String instCname;
	private String menuId;
	private String menuName;
	private String ip;
	private String browse;
	private String logType;
	private Date execTime;
	private String description;
	private String status;
	private String systemId;
	private List instIds;
	private Date endExecTime;
	private Date beginExecTime;
	private String uId;

	public String getUId(){
		return uId == null ? "" : uId.trim();
	}

	public void setUId(String id){
		uId = id;
	}

	public LogDO(String userId, String instId){
		this.userId = userId;
		this.instId = instId;
	}

	public LogDO(String menuId, String menuName, String logType,
			String description, String status){
		this.menuId = menuId;
		this.menuName = menuName;
		this.logType = logType;
		this.description = description;
		this.status = status;
	}

	public LogDO(){
	}

	public long getLogId(){
		return this.logId;
	}

	public void setLogId(long logId){
		this.logId = logId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserEname(){
		return this.userEname;
	}

	public void setUserEname(String userEname){
		this.userEname = userEname;
	}

	public String getUserCname(){
		return this.userCname;
	}

	public void setUserCname(String userCname){
		this.userCname = userCname;
	}

	public String getInstId(){
		return this.instId;
	}

	public void setInstId(String instId){
		this.instId = instId;
	}

	public String getInstCname(){
		return this.instCname;
	}

	public void setInstCname(String instCname){
		this.instCname = instCname;
	}

	public String getMenuId(){
		return this.menuId;
	}

	public void setMenuId(String menuId){
		this.menuId = menuId;
	}

	public String getMenuName(){
		return this.menuName;
	}

	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	public String getIp(){
		return this.ip;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getBrowse(){
		return this.browse;
	}

	public void setBrowse(String browse){
		this.browse = browse;
	}

	public String getLogType(){
		return this.logType;
	}

	public void setLogType(String logType){
		this.logType = logType;
	}

	public Date getExecTime(){
		return this.execTime;
	}

	public void setExecTime(Date execTime){
		this.execTime = execTime;
	}

	public String getDescription(){
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription4Html(){
		return this.description.replaceAll("\r", "<br>");
	}

	public String getDescriptionShort(){
		return this.description.substring(0, 17) + "...";
	}

	public String getStatus(){
		return this.status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public List getInstIds(){
		return this.instIds;
	}

	public void setInstIds(List instIds){
		this.instIds = instIds;
	}

	public String getSystemId(){
		return this.systemId;
	}

	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	public Date getEndExecTime(){
		return this.endExecTime;
	}

	public void setEndExecTime(Date endExecTime){
		this.endExecTime = endExecTime;
	}

	public Date getBeginExecTime(){
		return this.beginExecTime;
	}

	public void setBeginExecTime(Date beginExecTime){
		this.beginExecTime = beginExecTime;
	}

	public String getStrInstIds(){
		if((this.instIds != null) && (this.instIds.size() > 0)){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < this.instIds.size(); ++i)
				sb.append('\'').append(this.instIds.get(i).toString()).append(
						"',");
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('[').append("userId=").append(getUserId()).append(
				",userEname=").append(getUserEname()).append(",userCname=")
				.append(getUserCname()).append(",instId=").append(getInstId())
				.append(",instCname=").append(getInstCname()).append(",ip=")
				.append(getIp()).append(",\"BROWSE\"=").append(getBrowse())
				.append(",menuId=").append(getMenuId()).append(",menuName=")
				.append(getMenuName()).append(",logType=").append(getLogType())
				.append(",execTime").append(getExecTime()).append(
						",description=").append(getDescription()).append(
						",status=").append(getStatus()).append(']');
		return sb.toString();
	}
}