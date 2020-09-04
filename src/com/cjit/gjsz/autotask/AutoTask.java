package com.cjit.gjsz.autotask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cjit.crms.util.date.AppDate;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.SystemCache;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.AutoDealRptService;
import com.cjit.gjsz.system.model.Mts;

public class AutoTask implements InitializingBean, DisposableBean{

	private Timer timer = new Timer();// 自动报文处理
	private Timer timerCheck = new Timer();// 自动数据校验
	private AutoDealRptService autoDealRptService;
	private CacheManager cacheManager;
	private SystemCache systemCache;
	private String configIsCluster;
	private Logger logger = Logger.getLogger(AutoTask.class);

	// 自动校验业务逻辑
	private class AutoRunCheck extends TimerTask{

		final String isCluster;

		public AutoRunCheck(String configIsCluster){
			isCluster = configIsCluster;
		}

		public void run(){
			Date now = new Date();
			try{
				AppDate ad = new AppDate(now);// 执行时立刻赋值
				logger.info("检查自动校验时间点:" + DateUtils.toString(now, "HH:mm"));
				// 自动校验业务逻辑
				String runTime = "";// 参数配置界面配置的自动校验时间
				if(!"yes".equalsIgnoreCase(isCluster)){
					Map configMap = getSystemCache().getConfigMap();
					if(configMap == null)
						return;
					runTime = (String) configMap
							.get("config.autoCheck.runtime");
				}else{
					runTime = autoDealRptService.findAutoCheckRunTime();
				}
				if(StringUtils.isBlank(runTime))
					return;
				// 判断当前时间是否是配置的校验执行时间 以及 所配当前时间的校验是否已开始执行(针对集群)
				if(autoDealRptService.isOnSchedule(runTime, ad)
						&& autoDealRptService.isGetResource(now,
								DataUtil.TASK_AUTOCHECK, "-")){
					logger.info(DateUtils.toString(now, "HH:mm")
							+ "获得资源开始执行自动校验功能!");
					autoDealRptService.updateResource(now,
							DataUtil.TASK_AUTOCHECK, null, "自动校验", null, null);
					autoDealRptService.autoCheckData();
					autoDealRptService.updateResource(now,
							DataUtil.TASK_AUTOCHECK, new Date(), "线程结束",
							"自动校验[完成]", null);
					logger.info(DateUtils.toString(now, "HH:mm")
							+ "自动校验任务调度结束!");
				}
			}catch (Exception e){
				logger.error(e);
				autoDealRptService.updateResource(now,
						DataUtil.TASK_AUTOCREATEANDRECEIVYE, new Date(),
						"线程结束", "线程异常结束[" + e.getMessage() + "]", null);
			}
		}
	}

	// 自动生成文件和反馈接受业务逻辑
	private class AutoRun extends TimerTask{

		final String isCluster;

		public AutoRun(String configIsCluster){
			isCluster = configIsCluster;
		}

		public void run(){
			Date now = new Date();
			try{
				AppDate ad = new AppDate(now);// 执行时立刻赋值
				logger
						.info("检查自生成报文和接收时间点:"
								+ DateUtils.toString(now, "HH:mm"));
				// 自动生成文件和反馈接受业务逻辑
				Map configMtsMap = null;// MTS参数配置信息
				if(!"yes".equalsIgnoreCase(isCluster)){
					configMtsMap = getSystemCache().getConfigMtsMap();
				}else{
					configMtsMap = autoDealRptService.initConfigMts();
				}
				if(configMtsMap == null)
					return;
				List rptTitleList = new ArrayList();// 达到执行时间主报告行
				for(Iterator it = configMtsMap.values().iterator(); it
						.hasNext();){
					Mts mts = (Mts) it.next();
					if(mts.getEnabled().equals("yes")
							&& autoDealRptService.isOnSchedule(
									mts.getRunTime(), ad)
							&& autoDealRptService.isGetResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE, mts
											.getRptTitle())){
						rptTitleList.add(mts.getRptTitle());
						logger.info(DateUtils.toString(now, "HH:mm")
								+ "-获得资源开始执行生成报文和接收操作!-" + mts.getRptTitle());
					}
				}
				// 执行调用
				if(rptTitleList != null && rptTitleList.size() > 0){
					if("yes".equalsIgnoreCase(isCluster)){
						// 集群方式，重新从数据库中读取参数配置构造缓存信息
						try{
							systemCache.registerParams();
						}catch (Exception ex){
							logger.error(DateUtils.toString(now, "HH:mm")
									+ "-集群方式重构缓存出现异常!", ex);
							for(Iterator it = rptTitleList.iterator(); it
									.hasNext();){
								String rptTitle = (String) it.next();
								autoDealRptService.updateResource(now,
										DataUtil.TASK_AUTOCREATEANDRECEIVYE,
										null, "线程结束", "集群方式重构缓存出现异常", rptTitle);
							}
							return;
						}
					}
					// 自动生成报文
					for(Iterator it = rptTitleList.iterator(); it.hasNext();){
						String rptTitle = (String) it.next();
						autoDealRptService.updateResource(now,
								DataUtil.TASK_AUTOCREATEANDRECEIVYE, null,
								"生成报文", null, rptTitle);
						try{
							StringBuffer info = new StringBuffer();
							String infoC = autoDealRptService
									.autoCreateReport(rptTitle);
							if("success".equals(infoC)){
								info.append("生成报文[完成]");
							}else if("realPathIsNull".equals(infoC)){
								info.append("生成报文[未找到系统路径]");
							}else if("existsTokenLockAppFilePath".equals(infoC)){
								info.append("生成报文[工程目录存在锁文件]");
							}else if("noDataCanCreate".equals(infoC)){
								info.append("生成报文[无可生成报文的记录]");
							}else if("notExistsFilePath".equals(infoC)){
								info.append("生成报文[报文生成路径不存在]");
							}else if("existsTokenLockSendFilePath"
									.equals(infoC)){
								info.append("生成报文[报送文件目录存在锁文件]");
							}else if("errorRptNo".equals(infoC)){
								info.append("生成报文[机构对照申报号配置不正确]");
							}else if("exception".equals(infoC)){
								info.append("生成报文[出现系统异常]");
							}
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE, null,
									"上传报文", info.toString(), rptTitle);
						}catch (Exception ex){
							logger.error(DateUtils.toString(now, "HH:mm")
									+ "-自动生成报文出现异常!-" + rptTitle, ex);
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE, null,
									"上传报文", "生成报文[出现系统异常]", rptTitle);
						}
					}
					// 自动上传报文
					for(Iterator it = rptTitleList.iterator(); it.hasNext();){
						String rptTitle = (String) it.next();
						try{
							StringBuffer info = new StringBuffer();
							String infoU = autoDealRptService.uploadReport(
									null, null, rptTitle);
							if(infoU.equals("ok")){
								info.append("上传报文[完成]");
							}else if(infoU.endsWith("lock")){
								info.append("上传报文[上传结束,锁文件已存在]");
							}else if(infoU.endsWith("error")){
								info.append("上传报文[上传出现异常]");
							}else if("realPathIsNull".equals(infoU)){
								// 在20130628版中为error
								info.append("上传报文[出现异常,未找到系统路径]");
							}else if("noFileUpload".equals(infoU)){
								// 在20130628版中为error
								info.append("上传报文[无文件可供上传]");
							}else if("mtsException".equals(infoU)){
								// 在20130628版中为error
								info.append("上传报文[传输过程中出现异常]");
							}else if("mtsNoConfigurate".equals(infoU)){
								// 在20130628版中为error
								info.append("上传报文[主报告行未配置MTS参数]");
							}else if(infoU.equals("notAll")){
								info.append("上传报文[部分上传成功,未全部上传]");
							}
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE, null,
									"接收报文", info.toString(), rptTitle);
						}catch (Exception ex){
							logger.error(DateUtils.toString(now, "HH:mm")
									+ "-自动上传报文出现异常!-" + rptTitle, ex);
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE, null,
									"接收报文", "上传报文[出现系统异常]", rptTitle);
						}
					}
					// 自动接收反馈
					Map configMap = null; // 参数配置信息
					if(!"yes".equalsIgnoreCase(isCluster)){
						// 非集群 从缓存读取
						configMap = getSystemCache().getConfigMap();
					}else{
						// 集群 从数据库读取
						configMap = autoDealRptService.initConfigParameters();
					}
					String changeDataStatus = null;// 接收错误反馈修改目标状态
					String configLowerStatusLinkage = null;// 是否联动打回
					if(configMap != null){
						if(configMap
								.get("config.changeDataStatus.forErrFeedBack") != null){
							changeDataStatus = (String) configMap
									.get("config.changeDataStatus.forErrFeedBack");
						}
						if(configMap.get("config.lowerstatus.linkage") != null){
							configLowerStatusLinkage = (String) configMap
									.get("config.lowerstatus.linkage");
						}
					}
					for(Iterator it = rptTitleList.iterator(); it.hasNext();){
						String rptTitle = (String) it.next();
						try{
							StringBuffer info = new StringBuffer();
							String infoR = autoDealRptService.receiveReport(
									null, rptTitle, changeDataStatus,
									configLowerStatusLinkage);
							if(infoR.length() > 200){
								info.append(infoR.substring(0, 200));
							}else{
								info.append(infoR.toString());
							}
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE,
									new Date(), "接收Error/History", info
											.toString(), rptTitle);
						}catch (Exception ex){
							logger.error(DateUtils.toString(now, "HH:mm")
									+ "-自动接收反馈出现异常!-" + rptTitle, ex);
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE,
									new Date(), "接收Error/History",
									"接收报文[出现系统异常]", rptTitle);
						}
					}
					// 自动接收ErrorFiles/HistorySend
					for(Iterator it = rptTitleList.iterator(); it.hasNext();){
						String rptTitle = (String) it.next();
						try{
							StringBuffer info = new StringBuffer();
							String infoE = autoDealRptService
									.receiveErrorFiles(null, rptTitle);
							if(infoE.length() > 200){
								info.append(infoE.substring(0, 200));
							}else{
								info.append(infoE.toString());
							}
							String infoS = autoDealRptService
									.receiveHistorySend(null, rptTitle);
							if(infoS.length() > 200){
								info.append(infoS.substring(0, 200));
							}else{
								info.append(infoS.toString());
							}
							if(info.length() > 400){
								autoDealRptService.updateResource(now,
										DataUtil.TASK_AUTOCREATEANDRECEIVYE,
										new Date(), "线程结束", info.substring(0,
												400), rptTitle);
							}else{
								autoDealRptService.updateResource(now,
										DataUtil.TASK_AUTOCREATEANDRECEIVYE,
										new Date(), "线程结束", info.toString(),
										rptTitle);
							}
						}catch (Exception ex){
							logger.error(DateUtils.toString(now, "HH:mm")
									+ "-自动接收ErrorFiles/HistorySend出现异常!-"
									+ rptTitle, ex);
							autoDealRptService.updateResource(now,
									DataUtil.TASK_AUTOCREATEANDRECEIVYE,
									new Date(), "线程结束",
									"接收ErrorFiles/HistorySend[出现系统异常]",
									rptTitle);
						}
					}
					logger.info(DateUtils.toString(now, "HH:mm")
							+ "-生成报文和接收任务调度结束!");
				}
			}catch (Exception e){
				logger.error(e);
				autoDealRptService.updateResource(now,
						DataUtil.TASK_AUTOCREATEANDRECEIVYE, new Date(),
						"线程结束", "线程异常结束[" + e.getMessage() + "]", null);
			}
		}
	}

	public void afterPropertiesSet() throws Exception{
		// 延时60秒后开始执行,每60秒执行一次
		// VMS-CANCEL
		//timer.schedule(new AutoRun(configIsCluster), 1000 * 60, 1000 * 60);
		//timerCheck.schedule(new AutoRunCheck(configIsCluster), 1000 * 60, 1000 * 60);
	}

	public void destroy() throws Exception{
		timer.cancel();
		timerCheck.cancel();
	}

	public AutoDealRptService getAutoDealRptService(){
		return autoDealRptService;
	}

	public void setAutoDealRptService(AutoDealRptService autoDealRptService){
		this.autoDealRptService = autoDealRptService;
	}

	public CacheManager getCacheManager(){
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	public SystemCache getSystemCache(){
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache){
		this.systemCache = systemCache;
	}

	public String getConfigIsCluster(){
		return configIsCluster;
	}

	public void setConfigIsCluster(String configIsCluster){
		this.configIsCluster = configIsCluster;
	}
}
