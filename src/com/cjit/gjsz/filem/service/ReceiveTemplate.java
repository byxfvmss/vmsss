package com.cjit.gjsz.filem.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.ftp.Ftp;
import com.cjit.gjsz.ftp.Sftp;
import com.cjit.gjsz.system.model.Mts;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public abstract class ReceiveTemplate {

	protected transient Log log = LogFactory.getLog(this.getClass());
	protected ReceiveReportService receiveReportService;
	protected DataDealService dataDealService;
	protected String localFolder;// 接收到的文件夹名,需要子类赋值
	protected String packType;// 接收文件类型,需要子类赋值

	protected abstract String doRecieve(List downFolder);// 实现接受功能,需要子类实现

	public abstract String getFunctionName();// 返回该功能的中文名称，由子类实现

	public ReceiveTemplate(ReceiveReportService service1) {
		this.receiveReportService = service1;
	}

	public ReceiveTemplate(ReceiveReportService service1,
			DataDealService service2) {
		this.receiveReportService = service1;
		this.dataDealService = service2;
	}

	protected String sftpRecieve(Mts mts, String filePath) {
		List downFolder = new ArrayList();
		boolean isLock = false;
		ChannelSftp csftp = null;
		try {
			if (!StringUtils.isEmpty(mts.getIdentity())) {
				log.info("尝试使用密钥连接SFTP服务器");
				csftp = Sftp.connectByCert(mts.getIdentity(), mts
						.getPassphrase(), mts.getUserName(), mts.getIp(),
						Integer.parseInt(mts.getPort()));
			} else {
				log.info("尝试使用用户名密码连接SFTP服务器");
				csftp = Sftp.connect(mts.getUserName(), mts.getPassWord(), mts
						.getIp(), Integer.parseInt(mts.getPort()));
			}
			List filelist = csftp.ls(mts.getSourcePath(packType));
			// 检查是否含有锁文件
			for (int i = 0; i < filelist.size(); i++) {
				LsEntry le = (LsEntry) filelist.get(i);
				if (le.getFilename().equals("Token.lock")) {
					isLock = true;
					return "lock";
				}
			}
			log.info("向服务器写入锁文件");
			csftp.put(
					new FileInputStream(File.createTempFile("Token", "lock")),
					mts.getSourcePath(packType) + "/Token.lock");
			// 下载全部文件
			for (int i = 0; i < filelist.size(); i++) {
				LsEntry le = (LsEntry) filelist.get(i);
				if (sftpIsRecieve(le)) {
					log.info("下载文件夹:" + le.getFilename());
					String dstPath = filePath + File.separator
							+ le.getFilename() + File.separator;
					downloadSFTPByFolder(dstPath, mts.getSourcePath(packType)
							+ "/" + le.getFilename(), csftp);
					downFolder.add(dstPath);
				}
			}
			log.info("执行解析文件操作");
			if (CollectionUtil.isEmpty(downFolder)) {
				return "noFolder";
			}
			doRecieve(downFolder);
			return "ok";
		} catch (com.jcraft.jsch.JSchException ce) {
			log.error("SFTP连接出现异常", ce);
			return "connectException";
		} catch (Exception e) {
			log.error("SFTP接收出现异常", e);
			return "error";
		} finally {
			try {
				if (!isLock)
					csftp.rm(mts.getSourcePath(packType) + "/Token.lock");
				if (csftp != null)
					csftp.quit();
			} catch (Exception e) {
			}
		}
	}

	public boolean sftpIsRecieve(LsEntry le) {
		return le.getAttrs().isDir()
				&& !le.getFilename().equals(".")
				&& !le.getFilename().equals("..")
				&& !receiveReportService.isReceivePackExists(le.getFilename(),
						packType);
	}

	protected String ftpRecieve(Mts mts, String filePath) {
		List downFolder = new ArrayList();
		boolean isLock = false;
		FTPClient ftpclient = new FTPClient();
		try {
			log.info("尝试连接FTP服务器");
			ftpclient.connect(mts.getIp(), Integer.parseInt(mts.getPort()));
			ftpclient.login(mts.getUserName(), mts.getPassWord());
			if (mts.getModel().equals("PASV"))
				ftpclient.enterLocalPassiveMode();
			// FTP根目录下的文件和文件夹列表
			// 是否存在SendPath路径
			boolean pathExists = Ftp.isPathExists(ftpclient, mts
					.getSourcePath(packType));
			if (!pathExists) {
				isLock = true;// 无需执行删除锁文件操作
				return "pathNotExists";// 配置路径不存在
			}
			FTPFile[] ftpFileList = ftpclient.listFiles(mts
					.getSourcePath(packType));
			// 检查锁文件
			for (int i = 0; i < ftpFileList.length; i++) {
				if (ftpFileList[i].getName().equals("Token.lock")) {
					isLock = true;
					return "lock";
				}
			}
			log.info("向服务器写入锁文件");
			ftpclient.storeFile(mts.getSourcePath(packType) + "/Token.lock",
					new FileInputStream(File.createTempFile("Token", "lock")));
			// 下载全部文件
			for (int i = 0; i < ftpFileList.length; i++) {
				if (ftpIsReceive(ftpFileList[i])) {
					log.info("下载文件夹:" + ftpFileList[i].getName());
					String dstPath = filePath + File.separator
							+ ftpFileList[i].getName() + File.separator;
					downloadFTPByFolder(dstPath, mts.getSourcePath(packType)
							+ File.separator + ftpFileList[i].getName(),
							ftpclient);
					downFolder.add(dstPath);
				}
			}
			log.info("执行解析文件操作");
			if (CollectionUtil.isEmpty(downFolder)) {
				return "noFolder";
			}
			doRecieve(downFolder);
			return "ok";
		} catch (java.net.ConnectException ce) {
			log.error("FTP连接出现异常", ce);
			return "connectException";
		} catch (Exception e) {
			log.error("FTP接收出现异常", e);
			return "error";
		} finally {
			try {
				if (!isLock)
					ftpclient.deleteFile(mts.getSourcePath(packType)
							+ "/Token.lock");
				ftpclient.quit();
			} catch (Exception e) {
			}
		}
	}

	protected boolean ftpIsReceive(FTPFile ftpFile) {
		return ftpFile.isDirectory()
				&& !ftpFile.getName().equals(".")
				&& !ftpFile.getName().equals("..")
				&& !receiveReportService.isReceivePackExists(ftpFile.getName(),
						packType);
	}

	/**
	 * 接受文件入口函数
	 * 
	 * @param mts
	 * @return ok:完成 error:异常 lock:已锁定 disabled:未启用
	 */
	public String recieve(Mts mts) {
		String filePath = null;
		try {
			CacheabledMap cache = (CacheabledMap) CacheManager
					.getCacheObject("paramCache");
			if (cache.get("realPath") != null) {
				filePath = StringUtils.removeEnd(cache.get("realPath")
						.toString(), File.separator)
						+ File.separator
						+ "data"
						+ File.separator
						+ localFolder;
			} else if (CacheabledMap.WEBAPP_PATH != null) {
				filePath = StringUtils.removeEnd(CacheabledMap.WEBAPP_PATH,
						File.separator)
						+ File.separator
						+ "data"
						+ File.separator
						+ localFolder;
			}
			if (filePath == null) {
				return "realPathIsNull";
			}
			if (mts.isEnabled(packType)) {
				log.info(mts.getRptTitle() + getFunctionName()
						+ "已启用，开始执行接受操作!");
				String info = "error";
				if (mts.getLink().equals("ftp")) {
					info = ftpRecieve(mts, filePath);
				} else if (mts.getLink().equals("sftp")) {
					info = sftpRecieve(mts, filePath);
				}
				log.info(mts.getRptTitle() + getFunctionName() + "接收完成!");
				return info;
			} else {
				log.info(mts.getRptTitle() + getFunctionName() + "未启用");
				return "disabled";
			}
		} catch (Exception e) {
			log.error("接受mts文件失败", e);
			return "error";
		}
	}

	protected void downloadFTPByFolder(String destination, String source,
			FTPClient ftpclient) {
		try {
			FTPFile[] files = ftpclient.listFiles(source);
			// 建立文件夹
			File dst = new File(destination);
			removeFolder(dst);
			dst.mkdirs();
			// 循环下载
			for (int i = 0; i < files.length; i++) {
				if (!files[i].getName().equals(".")
						&& !files[i].getName().equals("..")) {
					if (files[i].isDirectory()) {
						downloadFTPByFolder(destination + File.separator
								+ files[i].getName(), source + File.separator
								+ files[i].getName(), ftpclient);
					} else if (files[i].isFile()) {
						File localFile = new File(destination + File.separator
								+ files[i].getName());
						OutputStream is = new FileOutputStream(localFile);
						ftpclient.retrieveFile(source + File.separator
								+ files[i].getName(), is);
					}
				}
			}
		} catch (Exception e) {
			log.error("从FTP下载文件出错", e);
		}
	}

	protected void downloadSFTPByFolder(String destination, String source,
			ChannelSftp sftp) {
		try {
			List files = sftp.ls(source);
			// 建立文件夹
			File dst = new File(destination);
			removeFolder(dst);
			dst.mkdirs();
			// 循环下载
			for (int i = 0; i < files.size(); i++) {
				ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) files.get(i);
				if (!entry.getFilename().equals(".")
						&& !entry.getFilename().equals("..")) {
					if (entry.getAttrs().isDir()) {
						downloadSFTPByFolder(destination + "/"
								+ entry.getFilename(), source + "/"
								+ entry.getFilename(), sftp);
					} else if (!entry.getAttrs().isLink()) {
						sftp
								.get(source + "/" + entry.getFilename(),
										new FileOutputStream(new File(
												destination + File.separator
														+ entry.getFilename())));
					}
				}
			}
		} catch (Exception e) {
			log.error("从SFTP下载文件出错", e);
		}
	}

	protected void removeFolder(File folder) {
		if (!folder.exists())
			return;
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				removeFolder(files[i]);
			else if (files[i].isFile())
				files[i].delete();
			else if (files[i].isHidden())
				continue;
		}
		folder.delete();
	}
}
