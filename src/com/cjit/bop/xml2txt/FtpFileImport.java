package com.cjit.bop.xml2txt;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.interfacemanager.service.impl.FmssDataServiceImpl;

public class FtpFileImport{

	private String ftphost;
	private String ftpport;
	private String ftpusername;
	private String ftppassword;
	private String ftpdir;
	private String backupPath;
	private String savePath;
	private String errPath;
	private String txtPath;
	private String logpath;
	private String[] fileTyps = new String[] {"BOPA", "BOPB", "BOPC", "BOPD",
			"BOPE", "BOPF", "BOPG", "BOPH", "BOPK", "BOPM", "BOPM_REFNOS",
			"BOPN", "BOPN_CUSTOMS", "BOPP", "BOPQ", "BOPQ_CUSTOMS", "BOPR",
			"BOPR_REFNOS", "BOPS", "BOPU", "BOPU_BANKINFOS", "BOPU_INVCOUNTRY",
			"JSHD", "JSHE", "JSHF", "JSHG", "JSHU", "JSHU_BANKINFOS",
			"JSHU_INVCOUNTRY"};
	public static String LOGFILENAME = "XML2TXT.HTML";
	public static String DOWNLOADING_FILENAME = "DOWNLOADING";
	private boolean hasInit = false;
	private int serverPort = 21;
	public static String defaultBackupPath = "";
	private static Object lock = new Object();
	private static boolean fleg = false;
	private boolean isAuto; // 是否为自动转换

	public boolean isAuto(){
		return isAuto;
	}

	public void setAuto(boolean isAuto){
		this.isAuto = isAuto;
	}

	public FtpFileImport(){
		init();
	}

	public synchronized boolean init(){
		if(!hasInit){
			try{
				InputStream inputStream = this.getClass().getClassLoader()
						.getResourceAsStream("config/jobDefine.properties");
				Properties p = new Properties();
				try{
					p.load(inputStream);
				}catch (IOException e1){
					e1.printStackTrace();
				}
				this.ftphost = p.getProperty("ftp.host");
				this.ftpport = p.getProperty("ftp.port");
				this.ftpusername = p.getProperty("ftp.username");
				this.ftppassword = p.getProperty("ftp.password");
				this.ftpdir = p.getProperty("ftp.dir");
				this.backupPath = p.getProperty("syctran.backuppath");
				this.errPath = p.getProperty("syctran.errpath");
				this.txtPath = p.getProperty("syctran.txtpath");
				this.logpath = p.getProperty("syctran.logpath");
				if(this.getFtpport() == null || this.getFtpport() == ""){
					logger.warn("配置文件中ftp.port值没有设置,在系统将使用默认值'21'");
					logger
							.warn("配置文件中ftp.port值表示数据ftp服务器端口,该属性默认在jobDefine.properties中配置,");
					ftpport = "21";
				}else{
					if(!Pattern.matches("^\\d{1,5}$", ftpport.trim())){
						logger.warn("配置文件中ftp.port值配置错误,应该为数字,系统将使用默认值'21'");
						ftpport = "21";
					}
				}
				serverPort = Integer.parseInt(ftpport);
				if(this.getFtpusername() == null || this.getFtpusername() == ""){
					logger.warn("配置文件中ftp.username值没有设置,在系统将用匿名用户登录FTP服务器");
					logger
							.warn("配置文件中ftp.username值表示据ftp服务器的用户名,该属性默认在jobDefine.properties中配置,");
					this.ftpusername = "anonymous";
					this.ftppassword = "anonymous";
				}
				if(!backupPath.endsWith(File.separator))
					backupPath += File.separator;
				logger.debug("backupPath:[" + backupPath + "]");
				if(!(new File(backupPath)).exists()
						|| (new File(backupPath)).isFile()){
					logger.error("配置文件中jobDefine.properties中配置的backuppath：["
							+ backupPath + "]不存在,或者不是目录");
					return false;
				}
				if(!txtPath.endsWith(File.separator))
					txtPath += File.separator;
				logger.debug("txtPath:[" + txtPath + "]");
				if(!(new File(txtPath)).exists()
						|| (new File(txtPath)).isFile()){
					logger.error("配置文件中jobDefine.properties中配置的txtPath：["
							+ txtPath + "]不存在,或者不是目录");
					return false;
				}
				if(!errPath.endsWith(File.separator))
					errPath += File.separator;
				logger.debug("errPath:[" + errPath + "]");
				if(!(new File(errPath)).exists()
						|| (new File(errPath)).isFile()){
					logger.error("配置文件中jobDefine.properties中配置的errPath：["
							+ errPath + "]不存在,或者不是目录");
					return false;
				}
				if(!logpath.endsWith(File.separator)){
					logpath += File.separator;
				}
				logger.debug("logpath:[" + logpath + "]");
				if(!(new File(logpath)).exists()
						|| (new File(logpath)).isFile()){
					logger.error("配置文件中jobDefine.logpath：[" + logpath
							+ "]不存在,或者不是目录");
					return false;
				}
				// 设置日志路径
				Enumeration en = logger.getAllAppenders();
				logger.addAppender(new FileAppender());
				FileAppender fa = null;
				while(en.hasMoreElements()){
					Object o = en.nextElement();
					if(o instanceof FileAppender){
						fa = (FileAppender) o;
						break;
					}
				}
				if(fa != null){
					fa.setFile(logpath + LOGFILENAME);
					fa.activateOptions();
				}
				hasInit = true;
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public String getFtpdir(){
		return ftpdir;
	}

	public void setFtpdir(String ftpdir){
		this.ftpdir = ftpdir;
	}

	public String getFtphost(){
		return ftphost;
	}

	public void setFtphost(String ftphost){
		this.ftphost = ftphost;
	}

	public String getFtppassword(){
		return ftppassword;
	}

	public void setFtppassword(String ftppassword){
		this.ftppassword = ftppassword;
	}

	public String getFtpport(){
		return ftpport;
	}

	public void setFtpport(String ftpport){
		this.ftpport = ftpport;
	}

	public String getFtpusername(){
		return ftpusername;
	}

	public void setFtpusername(String ftpusername){
		this.ftpusername = ftpusername;
	}

	private static Logger logger = Logger.getLogger(FtpFileImport.class);

	/**
	 * 手动执行
	 */
	public void executeByHand(){
		if(!init()){
			logger.error("系统初始化失败");
			fleg = true;
			return;
		}
		logger.info("手动执行");
		this.setAuto(false);
		execute();
	}

	/**
	 * 自动执行
	 */
	public void execute(){
		if(!init()){
			logger.error("系统初始化失败");
			fleg = true;
			return;
		}
		logger.info("----------------------");
		if(fleg){
			logger.error("当前有任务正在执行,请等待上一个任务完成再提交");
			logger.info("----------------------");
			return;
		}
		synchronized(lock){
			if(fleg){
				logger.error("当前有任务正在执行,请等待上一个任务完成再提交");
				logger.info("----------------------");
				return;
			}else{
				fleg = true;
			}
		}
		// while (true) {
		// if (fleg) {
		// try {
		// logger.error("当前有任务正在执行,请等待上一个任务完成再提交");
		// Thread.sleep(120000);
		//
		// } catch (InterruptedException e1) {
		// logger.error("线程出现错误", e1);
		// e1.printStackTrace();
		// }
		// } else {
		// synchronized (lock) {
		// if (fleg) {
		// continue;
		// } else {
		// fleg = true;
		// break;
		// }
		// }
		// }
		// }
		try{
			// 操作为自动转换时，判断当前日期是否为FMSS中定义的节假日
			if(this.isAuto()){
				FmssDataServiceImpl fmssDataService = (FmssDataServiceImpl) SpringContextUtil
						.getApplicationContext().getBean("fmssDataService");
				String currentDate = DateUtils
						.serverCurrentDate(DateUtils.ORA_DATES_FORMAT);
				boolean isHoliday = fmssDataService.isHoliday(currentDate);
				if(isHoliday){
					logger.error("当前日期为节假日,无需进行转换");
					logger.info("----------------------");
					return;
				}
			}
			logger.info("同步转换任务开始");
			// importDateMap.clear();
			FtpHelp fh = new FtpHelp(this.getFtphost(), serverPort,
					ftpusername, ftppassword);
			List names = null;
			try{
				names = fh.getOKFileNameList(this.getFtpdir());
			}catch (IOException e){
				logger.error("获取已经准备好的接口文件列表错误", e);
				e.printStackTrace();
				fh.logout();
				logger.info("数据同步任务结束");
				fleg = false;
				return;
			}catch (Throwable e){
				logger.error("获取已经准备好的接口文件列表错误", e);
				e.printStackTrace();
				fh.logout();
				logger.info("数据同步任务结束");
				fleg = false;
				return;
			}
			// modify by lihaiboa 2011-3-16 BEGIN
			// if(!fh.upLoadDownLoadingFile(
			// this.getFtpdir(),this.DOWNLOADING_FILENAME)){
			// fh.logout();
			// logger.info("数据同步任务结束");
			// fleg = false;
			// return;
			// }
			if(names != null && names.size() > 0){
				// 至少有一个T文件和一个数据文件
				boolean oflag = false;// 是否数据文件
				boolean tflag = false;// 是否存在T文件
				// boolean isOnlyT = false;// 仅有T文件
				for(Iterator iterator = names.iterator(); iterator.hasNext();){
					String nameFlag = ((String) iterator.next())
							.substring(3, 4);
					logger.debug("文件类型标识" + nameFlag);
					if("T".equalsIgnoreCase(nameFlag)){
						tflag = true;
					}else{
						oflag = true;
					}
				}
				// if(!(tflag && oflag)){
				if(!tflag && !oflag){
					// 既没有T文件也没有数据文件
					return;
				}
				// if(tflag && !oflag){
				// 仅有T文件
				// isOnlyT = true;
				// }
				if(!fh.upLoadDownLoadingFile(this.getFtpdir(),
						DOWNLOADING_FILENAME)){
					fh.logout();
					logger.info("数据同步任务结束");
					fleg = false;
					return;
				}
				// modify by lihaiboa 2011-3-16 END
				// 清空TXT
				File txtDir = new File(this.txtPath);
				File[] f = txtDir.listFiles(new FileFilter(){

					public boolean accept(File pathname){
						return (pathname.isFile() && (Pattern.matches(
								"^BOP[A-Z]\\S*\\.TXT$", pathname.getName()) || Pattern
								.matches("^JSH[A-Z]\\S*\\.TXT$", pathname
										.getName())));
					}
				});
				for(int i = 0; i < f.length; i++){
					String n = f[i].getPath();
					// if (!isOnlyT) {// 当仅有T文件时，不删除原有文件
					f[i].delete();
					logger.debug("删除历史接口文件[" + n + "]");
					// }
				}
				File fok = new File(this.txtPath + "OK");
				if(fok.exists()){
					fok.delete();
				}
				for(Iterator iter = names.iterator(); iter.hasNext();){
					String n = (String) iter.next();
					File file = fh.downLoadFile(this.getFtpdir(), n);
					if(file == null){
						continue;
					}
					boolean isOk = loadDataFromFile(file);
					if(!isOk){
						logger.info("拷贝数据文件到错误目录:" + errPath);
						try{
							FileUtils.copyFile(file, new File(errPath
									+ file.getName()));
							FileUtils.forceDelete(file);
						}catch (Exception e){
							logger.error("拷贝数据文件到备份目录:[" + errPath + "]错误", e);
							continue;
						}
					}else{
						logger.info("拷贝数据文件到备份目录:" + backupPath);
						try{
							FileUtils.copyFile(file, new File(backupPath
									+ file.getName()));
							FileUtils.forceDelete(file);
						}catch (Exception e){
							logger.error("拷贝数据文件到备份目录:[" + backupPath + "]错误",
									e);
							continue;
						}
					}
					// 删除接口文件
					logger.info("删除接口文件:[ " + n + "]");
					try{
						fh.delFile(getFtpdir(), n);
					}catch (Exception e){
						logger.error("删除接口文件:[ " + n + "]出错", e);
						e.printStackTrace();
					}
				}
				try{
					logger.info("删除接口OK文件出错");
					fh.delFile(getFtpdir(), "ok");
					logger.info("删除接口文件正在下载标志[" + DOWNLOADING_FILENAME + "]");
					fh.delFile(getFtpdir(), DOWNLOADING_FILENAME);
				}catch (Exception e){
					logger.error("删除文件出错", e);
					e.printStackTrace();
				}
				try{
					for(int i = 0; i < fileTyps.length; i++){
						File ffff = new File(this.getTxtPath() + File.separator
								+ fileTyps[i] + ".TXT");
						if(!ffff.exists()){
							FileUtils.writeStringToFile(ffff, "");
						}
						ffff = null;
					}
				}catch (IOException e){
					logger.error("往[" + this.getTxtPath() + "]目录写入空接口文件失败", e);
				}
				try{
					FileUtils.writeStringToFile(new File(this.getTxtPath()
							+ File.separator + "OK"), "");
					logger.info("往[" + this.getTxtPath() + "]目录写入OK文件成功");
				}catch (IOException e){
					logger.error("往[" + this.getTxtPath() + "]目录写入OK文件失败", e);
				}
			}
			fh.logout();
			logger.info("数据同步转换任务结束");
			fleg = false;
		}catch (Exception ex){
			logger.error(ex);
		}finally{
			fleg = false;
		}
	}

	private boolean loadDataFromFile(File file){
		FileInputStream in = null;
		Reader inreader = null;
		SAXReader reader;
		Document document;
		String import_date = "";
		// String org_code = "";
		String fname = file.getName();
		String fileType = "";// 报文类型标识（报文名称前4字母）
		if(fname.length() < 24){
			logger.error("报文文件名不符合规范[" + fname + "]");
			return false;
		}else{
			import_date = "20" + fname.substring(16, 22);
			// org_code = fname.substring(4, 16);
			fileType = fname.substring(0, 4).toUpperCase();
		}
		try{
			in = new FileInputStream(file);
			inreader = new InputStreamReader(in, "gb18030");
			reader = new SAXReader();
			document = reader.read(inreader);
			Node xmlType = document.selectSingleNode("/MSG/CURRENTFILE");
			String xmlTypeName = xmlType.getText();
			List records = document.selectNodes("/MSG/RECORDS/REC");
			List mainTable = new ArrayList();
			Map m = new HashMap();
			for(Iterator iterator = records.iterator(); iterator.hasNext();){
				Element rec = (Element) iterator.next();
				String names = "";
				String values = "";
				for(Iterator it = rec.elementIterator(); it.hasNext();){
					Element subEl = (Element) it.next();
					if("BOPU".equals(fileType) || "JSHU".equals(fileType)){
						if("INVCOUNTRY".equals(subEl.getName().toUpperCase())
								|| "BANKINFOS".equals(subEl.getName()
										.toUpperCase())){
							// 子表标签 不处理
						}else if("REMARKS"
								.equals(subEl.getName().toUpperCase())){
							// 备注字段标签
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText() : "|"
									+ StringUtils.replace(subEl.getText()
											.trim(), "|", "\\|");
							values += "||";
						}else{
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText() : "|"
									+ StringUtils.replace(subEl.getText()
											.trim(), "|", "\\|");
						}
					}else if("BOPM".equals(fileType) || "BOPR".equals(fileType)){
						if("REFNOS".equals(subEl.getName().toUpperCase())){
							// 子表标签 不处理
						}else if("CHKAMT".equals(subEl.getName().toUpperCase())){
							// 收汇总金额中用于出口核销的金额 字段标签
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText()
									: "||"
											+ StringUtils.replace(subEl
													.getText().trim(), "|",
													"\\|");
						}else{
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText() : "|"
									+ StringUtils.replace(subEl.getText()
											.trim(), "|", "\\|");
						}
					}else if("BOPN".equals(fileType) || "BOPQ".equals(fileType)){
						if("CUSTOMS".equals(subEl.getName().toUpperCase())){
							// 子表标签 不处理
						}else if("CRTUSER"
								.equals(subEl.getName().toUpperCase())){
							// 填报人 字段标签
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText()
									: "||"
											+ StringUtils.replace(subEl
													.getText().trim(), "|",
													"\\|");
						}else{
							names += names.equals("") ? subEl.getName() : "|"
									+ subEl.getName();
							values += values.equals("") ? subEl.getText() : "|"
									+ StringUtils.replace(subEl.getText()
											.trim(), "|", "\\|");
						}
					}else{
						names += names.equals("") ? subEl.getName() : "|"
								+ subEl.getName();
						values += values.equals("") ? subEl.getText() : "|"
								+ StringUtils.replace(subEl.getText().trim(),
										"|", "\\|");
					}
					// 含子表
					if(!subEl.isTextOnly()){
						List subValeuList = new ArrayList();
						for(Iterator iterator2 = subEl.elementIterator(); iterator2
								.hasNext();){
							String subnames = "";
							String subvalues = "";
							Element subSubEl = (Element) iterator2.next();
							if(subSubEl.isTextOnly()){
								subnames += subnames.equals("") ? subSubEl
										.getName() : "|" + subSubEl.getName();
								subvalues += subvalues.equals("") ? subSubEl
										.getText() : "|"
										+ StringUtils.replace(subSubEl
												.getText().trim(), "|", "\\|");
							}else{
								for(Iterator subit = subSubEl.elementIterator(); subit
										.hasNext();){
									Element subSubSubEl = (Element) subit
											.next();
									subnames += subnames.equals("") ? subSubSubEl
											.getName()
											: "|" + subSubSubEl.getName();
									subvalues += subvalues.equals("") ? subSubSubEl
											.getText()
											: "|"
													+ StringUtils.replace(
															subSubSubEl
																	.getText()
																	.trim(),
															"|", "\\|");
								}
							}
							String bsid = "";
							Node n = rec.selectSingleNode("RPTNO");
							Node n2 = rec.selectSingleNode("CUSTCODE");
							if(n != null){
								bsid = n.getText().trim();
							}else{
								bsid = n2.getText().trim();
							}
							String subid = getUniqueString();
							subnames += subnames.equals("") ? "BUSINESSID"
									: "|BUSINESSID";
							subvalues += subvalues.equals("") ? bsid : "|"
									+ StringUtils.replace(bsid.trim(), "|",
											"\\|");
							subnames += subnames.equals("") ? "SUBID"
									: "|SUBID";
							subvalues += subvalues.equals("") ? subid : "|"
									+ StringUtils.replace(subid.trim(), "|",
											"\\|");
							logger.debug(subnames);
							logger.debug(subvalues);
							subValeuList.add(subvalues);
						}
						if(m.containsKey(subEl.getName())){
							List preList = (List) m.get(subEl.getName());
							preList.addAll(subValeuList);
						}else{
							m.put(subEl.getName(), subValeuList);
						}
					}
				}
				names += names.equals("") ? "IMPORT_DATE" : "|IMPOR_TDATE";
				// names+=names.equals("")?"ORG_CODE":"|ORG_CODE";
				values += values.equals("") ? "" : "|" + import_date;
				// values+=values.equals("")?"":"|"+org_code;
				logger.debug(names);
				logger.debug(values);
				mainTable.add(values);
			}
			// 开始写接口文件
			//
			// File datePath = new File(this.getTxtPath() + File.separator
			// + import_date);
			// if (!datePath.exists())
			// FileUtils.forceMkdir(datePath);
			FileUtils.writeLines(new File(this.getTxtPath() + File.separator
					+ xmlTypeName + ".TXT"), "GB18030", mainTable);
			for(Iterator iterator = m.keySet().iterator(); iterator.hasNext();){
				String subname = (String) iterator.next();
				List lines = (List) m.get(subname);
				FileUtils.writeLines(
						new File(this.getTxtPath() + File.separator
								+ xmlTypeName + "_" + subname + ".TXT"),
						"GB18030", lines);
			}
			logger.info("文件[" + fname + "]处理完成");
			// if (!importDateMap.containsKey(import_date)) {
			// importDateMap.put(import_date, import_date);
			// }
			return true;
		}catch (DocumentException e){
			e.printStackTrace();
			logger.error("数据格文件解析错误", e);
			return false;
		}catch (Exception e){
			e.printStackTrace();
			logger.error("数据格文件解析错误", e);
			return false;
		}finally{
			try{
				if(inreader != null){
					inreader.close();
					inreader = null;
				}
				if(in != null){
					in.close();
					in = null;
				}
			}catch (IOException e){
				logger.error("数据格文件解析错误", e);
				e.printStackTrace();
				return false;
			}
		}
	}

	public static void main(String[] args){
		// FtpFileImport ff = new FtpFileImport();
		// ff.execute();
		// System.out.println(ff.getLogFileName());
		// File f=new File(ff.getLogFileName());
		// System.out.println(f.getPath()+" "+f.length());
		File txtDir = new File("E:\\bop\\TXT_DIR");
		File[] f = txtDir.listFiles(new FileFilter(){

			public boolean accept(File pathname){
				return (pathname.isFile() && Pattern.matches(
						"^BOP[A-Z]\\S*\\.TXT$", pathname.getName()));
			}
		});
		for(int i = 0; i < f.length; i++){
			System.out.println(f[i].getName());
		}
	}

	private static int generateCount = 0;

	public static synchronized String getUniqueString(){
		if(generateCount > 99999)
			generateCount = 0;
		String uniqueNumber = Long.toString(System.currentTimeMillis())
				+ Integer.toString(generateCount);
		generateCount++;
		return uniqueNumber;
	}

	public String getBackupPath(){
		return backupPath;
	}

	public void setBackupPath(String backupPath){
		this.backupPath = backupPath.trim();
	}

	public String getSavePath(){
		return savePath;
	}

	public void setSavePath(String savePath){
		this.savePath = savePath;
	}

	public String getErrPath(){
		return errPath;
	}

	public void setErrPath(String errPath){
		this.errPath = errPath;
	}

	public String getTxtPath(){
		return txtPath;
	}

	public void setTxtPath(String txtPath){
		this.txtPath = txtPath;
	}

	public static String getLogFileName(){
		Enumeration en = logger.getAllAppenders();
		FileAppender fa = null;
		while(en.hasMoreElements()){
			Object o = en.nextElement();
			if(o instanceof FileAppender){
				fa = (FileAppender) o;
				break;
			}
		}
		if(fa != null)
			return fa.getFile();
		else
			return "";
	}

	public String getLogpath(){
		return logpath;
	}

	public void setLogpath(String logpath){
		this.logpath = logpath;
	}
}