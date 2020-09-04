package com.cjit.gjsz.system.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.ValidatorUtil;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.filem.service.OrgConfigeService;
import com.cjit.gjsz.ftp.Ftp;
import com.cjit.gjsz.ftp.Sftp;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Mts;
import com.jcraft.jsch.ChannelSftp;

public class ConfigurationMTS extends BaseListAction{

	private Mts mtsVO = new Mts();// 页面使用
	private String message;
	private SystemCache systemCache;
	private UserInterfaceConfigService userInterfaceConfigService;
	private OrgConfigeService orgconfigeservice;
	// private final int TIMEOUT = 3000;//自己使用，用于配置超时时间
	// 受权机构列表
	private List rptTitleList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String configuration(){
		initPage();
		Map configMtsMap = null;
		if("yes".equalsIgnoreCase(this.configIsCluster)){
			configMtsMap = userInterfaceConfigService.initConfigMts();
		}else{
			configMtsMap = systemCache.getConfigMtsMap();
		}
		//if(configMtsMap.containsKey(mtsVO.getRptTitle())){
		//	mtsVO = (Mts) configMtsMap.get(mtsVO.getRptTitle());
		//}else{
		//	mtsVO = new Mts(mtsVO.getRptTitle());
		//}
		request.setAttribute("mtsVO", this.mtsVO);
		return SUCCESS;
	}

	private void initPage(){
		//setRptTitleList(orgconfigeservice.findRptTitleList());
		//request.setAttribute("rptTitleList", this.getRptTitleList());
		// 默认取第一个
		//if(StringUtils.isEmpty(mtsVO.getRptTitle())){
		//	if(this.rptTitleList.size() > 0){
		//		mtsVO.setRptTitle((String) this.rptTitleList.get(0));
		//	}
		//}
	}

	public String testConnection(){
		initPage();
		String errInfo = null;
		// 校验
		if(!validateMTS())
			return SUCCESS;
		if(this.mtsVO.getLink().equals("sftp")){
			ChannelSftp sftp = null;
			try{
				// 测试登录
				if(!StringUtils.isEmpty(mtsVO.getIdentity())){
					File t = new File(mtsVO.getIdentity());
					if(!t.exists())
						throw new Exception("密钥文件未找到!");
				}
				if(!StringUtils.isEmpty(mtsVO.getIdentity())){
					File t = new File(mtsVO.getIdentity());
					sftp = Sftp.connectByCert(t.getAbsolutePath(), mtsVO
							.getPassphrase(), mtsVO.getUserName(), mtsVO
							.getIp(), Integer.parseInt(mtsVO.getPort()));
				}else{
					sftp = Sftp.connect(mtsVO.getUserName(), mtsVO
							.getPassWord(), mtsVO.getIp(), Integer
							.parseInt(mtsVO.getPort()));
				}
				// 校验路径
				if(StringUtils.isNotEmpty(this.mtsVO.getSendPath())){
					try{
						sftp.ls(this.mtsVO.getSendPath());
					}catch (Exception e){
						throw new Exception("Send路径不存在");
					}
				}
				if(StringUtils.isNotEmpty(this.mtsVO.getFeedBackPath())){
					try{
						sftp.ls(this.mtsVO.getFeedBackPath());
					}catch (Exception e){
						throw new Exception("FeedBack路径不存在");
					}
				}
				if(this.mtsVO.getErrorFilesEnabled().equals("yes")
						&& StringUtils.isNotEmpty(this.mtsVO
								.getErrorFilesPath())){
					try{
						sftp.ls(this.mtsVO.getErrorFilesPath());
					}catch (Exception e){
						throw new Exception("ErrorFiles错误文件目录不存在");
					}
				}
				if(this.mtsVO.getHistorySendEnabled().equals("yes")
						&& StringUtils.isNotEmpty(this.mtsVO
								.getHistorySendPath())){
					try{
						sftp.ls(this.mtsVO.getHistorySendPath());
					}catch (Exception e){
						throw new Exception("HistorySend发送历史目录不存在");
					}
				}
			}catch (Exception e){
				errInfo = "SFTP测试连接出现异常!原因:[" + e.getMessage() + "]";
				log.error("SFTP测试连接出现异常!", e);
			}finally{
				if(sftp != null)
					sftp.exit();
			}
		}else{
			FTPClient ftpclient = new FTPClient();
			try{
				String ip = this.mtsVO.getIp();
				int port = Integer.parseInt(this.mtsVO.getPort());
				ftpclient.connect(ip, port);
				if(!ftpclient.login(this.mtsVO.getUserName(), this.mtsVO
						.getPassWord())){
					throw new Exception("用户名密码不正确");
				}
				if(this.mtsVO.getModel().equals("PASV"))
					ftpclient.enterLocalPassiveMode();
				FTPFile[] ftpFiles = ftpclient.listFiles();
				// 校验路径
				if(StringUtils.isNotEmpty(this.mtsVO.getSendPath())){
					boolean pathExists = Ftp.isPathExists(ftpclient, this.mtsVO
							.getSendPath());
					if(!pathExists){
						throw new Exception("SendPath路径不存在");
					}
				}
				if(StringUtils.isNotEmpty(this.mtsVO.getFeedBackPath())){
					boolean pathExists = Ftp.isPathExists(ftpclient, this.mtsVO
							.getFeedBackPath());
					if(!pathExists){
						throw new Exception("FeedBack路径不存在");
					}
				}
				if(this.mtsVO.getErrorFilesEnabled().equals("yes")
						&& StringUtils.isNotEmpty(this.mtsVO
								.getErrorFilesPath())){
					boolean pathExists = Ftp.isPathExists(ftpclient, this.mtsVO
							.getErrorFilesPath());
					if(!pathExists){
						throw new Exception("ErrorFiles错误文件目录不存在");
					}
				}
				if(this.mtsVO.getHistorySendEnabled().equals("yes")
						&& StringUtils.isNotEmpty(this.mtsVO
								.getHistorySendPath())){
					boolean pathExists = Ftp.isPathExists(ftpclient, this.mtsVO
							.getHistorySendPath());
					if(!pathExists){
						throw new Exception("HistorySend发送历史目录不存在");
					}
				}
			}catch (Exception e){
				errInfo = "FTP测试连接出现异常!原因:[" + e.getMessage() + "]";
				log.error("FTP测试连接出现异常!", e);
			}finally{
				try{
					ftpclient.quit();
				}catch (Exception e){
				}
			}
		}
		// 测试
		if(!StringUtils.isBlank(errInfo)){
			this.message = errInfo;
			this.message = this.message.replaceAll("\n", "");
		}else{
			this.message = "测试连接成功!";
		}
		this.request.setAttribute("message", this.message);
		return SUCCESS;
	}

	public String save(){
		initPage();
		// 校验
		if(!validateMTS())
			return SUCCESS;
		// 强制转换
		this.mtsVO.setFeedBackPath(formatPath(this.mtsVO.getFeedBackPath()));
		this.mtsVO.setSendPath(formatPath(this.mtsVO.getSendPath()));
		this.mtsVO
				.setErrorFilesPath(formatPath(this.mtsVO.getErrorFilesPath()));
		this.mtsVO.setHistorySendPath(formatPath(this.mtsVO
				.getHistorySendPath()));
		// 强制写no
		this.mtsVO.setEnabled(this.mtsVO.getEnabled().equals("yes") ? "yes"
				: "no");
		this.mtsVO.setErrorFilesEnabled(this.mtsVO.getErrorFilesEnabled()
				.equals("yes") ? "yes" : "no");
		this.mtsVO.setHistorySendEnabled(this.mtsVO.getHistorySendEnabled()
				.equals("yes") ? "yes" : "no");
		// 保存
		getUserInterfaceConfigService().updateConfigMts(this.mtsVO);
		try{
			systemCache.registerParams();
			this.message = "保存成功！";
			this.request.setAttribute("message", this.message);
		}catch (IOException e){
			log.error(e);
		}
		return SUCCESS;
	}

	private String formatPath(String path){
		String repath = StringUtils.replace(path, "\\", "/");
		while(repath.endsWith("/")){
			repath = StringUtils.removeEnd(repath, "/");
		}
		return repath;
	}

	public boolean validateMTS(){
		if(!ValidatorUtil.isValidIP(this.mtsVO.getIp())){
			this.message = "输入的ip地址不符合规范，形式如:xxx.xxx.xxx.xxx";
			return false;
		}
		if(!ValidatorUtil.isValidIpPort(this.mtsVO.getPort())){
			this.message = "端口号范围应在0-65535之间";
			return false;
		}
		if(StringUtil.isNotEmpty(this.mtsVO.getRunTime())){
			this.mtsVO.setRunTime(StringUtils.replace(this.mtsVO.getRunTime(),
					"；", ";"));
			this.mtsVO.setRunTime(StringUtils.replace(this.mtsVO.getRunTime(),
					"：", ":"));
			if(this.mtsVO.getRunTime().indexOf(";") >= 0){
				String[] runTimes = this.mtsVO.getRunTime().split(";");
				if(runTimes != null && runTimes.length > 0){
					for(int i = 0; i < runTimes.length; i++){
						if(StringUtil.isNotBlank(runTimes[i])){
							if(!DateUtils.isValidDate(runTimes[i], "HH:mm")){
								this.message = "执行时间配置不符合规范，形式如 HH:mm;HH:mm;HH:mm ";
								return false;
							}else{
								int nHour = Integer.valueOf(
										(runTimes[i].split(":"))[0]).intValue();
								int nMinute = Integer.valueOf(
										(runTimes[i].split(":"))[1]).intValue();
								if(nHour < 0 || nHour > 23 || nMinute < 0
										|| nMinute > 59){
									this.message = "执行时间配置不符合规范，" + runTimes[i]
											+ " 为无效时间";
									return false;
								}
							}
						}
					}
				}
			}else{
				if(!DateUtils.isValidDate(this.mtsVO.getRunTime(), "HH:mm")){
					this.message = "执行时间配置不符合规范，形式如 HH:mm;HH:mm;HH:mm ";
					return false;
				}else{
					int nHour = Integer.valueOf(
							(this.mtsVO.getRunTime().split(":"))[0]).intValue();
					int nMinute = Integer.valueOf(
							(this.mtsVO.getRunTime().split(":"))[1]).intValue();
					if(nHour < 0 || nHour > 23 || nMinute < 0 || nMinute > 59){
						this.message = "执行时间配置不符合规范，" + this.mtsVO.getRunTime()
								+ " 为无效时间";
						return false;
					}
				}
			}
		}
		return true;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public SystemCache getSystemCache(){
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache){
		this.systemCache = systemCache;
	}

	public Mts getMtsVO(){
		return mtsVO;
	}

	public void setMtsVO(Mts mtsVO){
		this.mtsVO = mtsVO;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService(){
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public OrgConfigeService getOrgconfigeservice(){
		return orgconfigeservice;
	}

	public void setOrgconfigeservice(OrgConfigeService orgconfigeservice){
		this.orgconfigeservice = orgconfigeservice;
	}

	public List getRptTitleList(){
		return rptTitleList;
	}

	public void setRptTitleList(List rptTitleList){
		this.rptTitleList = rptTitleList;
	}
}
