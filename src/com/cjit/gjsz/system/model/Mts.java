package com.cjit.gjsz.system.model;

import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.datadeal.util.EncrypDES;

public class Mts {
	private String rptTitle;//主报告行
	private String ip ;
	private String port ;
	private String link ="ftp";
	private String userName;
	private String passWord ;
	private String sendPath;
	private String feedBackPath ;
	private String errorFilesPath;
	private String historySendPath;
	private String runTime ;
	private String enabled ="no";
	private String errorFilesEnabled="no";
	private String historySendEnabled="no";
	private String identity ;//密钥路径
	private String passphrase;//sftp密钥校验密码
	private String model="PASV" ;
	
	public Mts(String rptTitle) {
		this.setRptTitle(rptTitle);
	}

	public Mts() {
	}

	public String getSourcePath(String packType)
	{
		if(packType.equals(DataUtil.PACKTYPE_FEEDBACK)) return getFeedBackPath();
		if(packType.equals(DataUtil.PACKTYPE_ERRORFILES)) return getErrorFilesPath();
		if(packType.equals(DataUtil.PACKTYPE_HISTORYSEND)) return getHistorySendPath();
		return null;
	}
	public boolean isEnabled(String packType)
	{
		if(packType.equals(DataUtil.PACKTYPE_FEEDBACK)) return true;
		if(packType.equals(DataUtil.PACKTYPE_ERRORFILES)) return getErrorFilesEnabled().equals("yes");
		if(packType.equals(DataUtil.PACKTYPE_HISTORYSEND)) return getHistorySendEnabled().equals("yes");
		return false;
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return EncrypDES.getInstance().Decryptor(passWord);
	}
	public String getPassWordEssence() {
		return passWord;
	}
	public void setPassWord(String passWord){
		this.passWord = EncrypDES.getInstance().Encrytor(passWord);
	}
	public void setPassWordEssence(String passWord){
		this.passWord = passWord;
	}
	
	public String getSendPath() {
		return sendPath;
	}

	public void setSendPath(String sendPath) {
		this.sendPath = sendPath;
	}

	public String getFeedBackPath() {
		return feedBackPath;
	}

	public void setFeedBackPath(String feedBackPath) {
		this.feedBackPath = feedBackPath;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getRptTitle() {
		return rptTitle;
	}

	public void setRptTitle(String rptTitle) {
		this.rptTitle = rptTitle;
	}

	public String getErrorFilesPath() {
		return errorFilesPath;
	}

	public void setErrorFilesPath(String errorFilesPath) {
		this.errorFilesPath = errorFilesPath;
	}

	public String getHistorySendPath() {
		return historySendPath;
	}

	public void setHistorySendPath(String historySendPath) {
		this.historySendPath = historySendPath;
	}

	public String getErrorFilesEnabled() {
		return errorFilesEnabled;
	}

	public void setErrorFilesEnabled(String errorFilesEnabled) {
		this.errorFilesEnabled = errorFilesEnabled;
	}

	public String getHistorySendEnabled() {
		return historySendEnabled;
	}

	public void setHistorySendEnabled(String historySendEnabled) {
		this.historySendEnabled = historySendEnabled;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public String getPassphrase(){
		return EncrypDES.getInstance().Decryptor(passphrase);
	}
	public String getPassphraseEssence() {
		return passphrase;
	}
	public void setPassphrase(String passphrase){
		this.passphrase = EncrypDES.getInstance().Encrytor(passphrase);
	}
	public void setPassphraseEssence(String passphrase) {
		this.passphrase = passphrase;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
