package com.cjit.bop.xml2txt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FtpHelp{

	static Logger logger = Logger.getLogger(FtpHelp.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private String ftpServer = "";
	private int ftpPort = 21;
	private String ftpUID = "";
	private String ftpPWD = "";
	private FTPClient ftp;

	public FtpHelp(String ftpIp, int ftpPort, String ftpUserName,
			String ftpPassword){
		this.ftpServer = ftpIp;
		this.ftpPort = ftpPort;
		this.ftpUID = ftpUserName;
		this.ftpPWD = ftpPassword;
	}

	/**
	 * @param SourcePath ftp服务器文件目录
	 * @return 服务器上准备好的文件列表List<String>
	 * @throws IOException
	 * @throws Throwable
	 */
	public List getOKFileNameList(String SourcePath) throws Throwable,
			IOException{
		if(!checkOKFILE(SourcePath)){
			return new ArrayList();
		}
		List fs = new ArrayList();
		FTPFile[] ftpfiles = ftp.listFiles();
		if(ftpfiles != null){
			for(int i = 0; i < ftpfiles.length; i++){
				if(ftpfiles[i].isFile()
						&& !ftpfiles[i].getName().toUpperCase().equals("OK")
						&& !ftpfiles[i].getName().toUpperCase().equals(
								FtpFileImport.DOWNLOADING_FILENAME)){
					logger.debug("数据文件:"
							+ ftpfiles[i].getName()
							+ "  size:"
							+ ftpfiles[i].getSize()
							+ " last modifiy:"
							+ dateFormat.format(ftpfiles[i].getTimestamp()
									.getTime()));
					fs.add(ftpfiles[i].getName());
				}
			}
		}
		if(fs.size() == 0){
			logger.info("服务器上没有数据文件");
		}
		return fs;
	}

	public boolean upLoadDownLoadingFile(String sourcePath, String filename){
		logger
				.info("开始上上传正在下载标志符：[" + FtpFileImport.DOWNLOADING_FILENAME
						+ "]");
		InputStream input = null;
		try{
			if(!login()){
				return false;
			}
			boolean bdir = ftp.changeWorkingDirectory(sourcePath);
			if(!bdir){
				logger.error("工作目录:" + sourcePath + "不存在");
				return false;
			}
			input = new ByteArrayInputStream(" ".getBytes("utf-8"));
			ftp.storeFile(filename, input);
			return true;
		}catch (Exception e){
			logger.error("文件下载标志符设置失败", e);
			e.printStackTrace();
		}finally{
			try{
				if(input != null){
					input.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @param SourcePath 服务器目录
	 * @param filename 文件名
	 * @return 本地文件觉得路径
	 */
	public File downLoadFile(String SourcePath, String filename){
		logger.info("开始下载文件：" + filename);
		try{
			if(!login()){
				return null;
			}
			boolean bdir = ftp.changeWorkingDirectory(SourcePath);
			if(!bdir){
				logger.error("工作目录:" + SourcePath + "不存在");
				return null;
			}
			OutputStream outputStream = null;
			String tmpFileName = System.getProperty("java.io.tmpdir").endsWith(
					File.separator) ? System.getProperty("java.io.tmpdir")
					+ filename : System.getProperty("java.io.tmpdir")
					+ File.separator + filename;
			File file = null;
			FTPFile[] ftpfiles = ftp.listFiles();
			if(ftpfiles != null){
				for(int i = 0; i < ftpfiles.length; i++){
					if(ftpfiles[i].isFile()
							&& ftpfiles[i].getName().equals(filename)){// 存在OK文件，表示目前服务器上文件都完整
						try{
							file = new java.io.File(tmpFileName);
							outputStream = new FileOutputStream(file);
							ftp.retrieveFile(filename, outputStream);
							outputStream.flush();
							outputStream.close();
							logger
									.info("下载文件成功：" + filename + "--->>>"
											+ file.getPath() + " size:"
											+ file.length());
							return file;
						}catch (Exception e){
							logger.error("文件下载错误", e);
							System.err.println(e);
						}finally{
							try{
								if(outputStream != null){
									outputStream.close();
								}
							}catch (IOException e){
								logger.error("文件下载错误", e);
								e.printStackTrace();
							}
						}
					}
				}
			}
		}catch (Exception e){
			logger.error("文件下载失败", e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param SourcePath OK文件存在的目录
	 * @return 该目录下是否存在OK文件
	 * @throws Exception
	 */
	public boolean checkOKFILE(String SourcePath) throws Exception{
		if(!login()){
			return false;
		}
		boolean bdir = ftp.changeWorkingDirectory(SourcePath);
		if(!bdir){
			logger.error("工作目录:" + SourcePath + "不存在");
			return false;
		}
		logger.debug("成功切换到:[" + SourcePath + "]目录");
		logger.info("开始检查接口目录中是否存在OK文件");
		try{
			FTPFile[] ftpfiles = ftp.listFiles();
			logger.info("开始检查接口目录中是否存在OK文件-ftp.listFiles()");
			boolean haveOK = false;
			if(ftpfiles != null){
				for(int i = 0; i < ftpfiles.length; i++){
					if(ftpfiles[i].isFile()){
						if(ftpfiles[i].getName().toUpperCase().equals("OK")){// 存在OK文件，表示目前服务器上文件都完整
							haveOK = true;
							break;
						}
					}
				}
			}
			if(haveOK){
				logger.info("服务器上文件准备完成,系统可以进行文件抓取");
				return true;
			}else{
				logger.info("服务器上文件文件正在准备或者不存在接口文件,接口目录中不存在[OK]文件");
				return false;
			}
		}catch (Exception e){
			logger.info("FtpHelp-checkOKFILE", e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 如果已经成功登陆返回true 如果没有登录，则登录,成功返回true,失败返回false
	 * @return 登录服务器状态
	 */
	public boolean login(){
		if(ftp != null && ftp.isConnected()){
			return true;
		}
		try{
			ftp = new FTPClient();
			logger.info("开始登录服务:" + ftpServer + ":" + ftpPort);
			ftp.setDefaultPort(ftpPort);
			ftp.connect(ftpServer);
			logger.info("设置FTP.BINARY_FILE_TYPE");
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			logger.info("准备进入本地被动模式");
			ftp.enterLocalPassiveMode();
			logger.info("已进入本地被动模式");
			boolean blogin = ftp.login(ftpUID, ftpPWD);
			if(!blogin){
				// System.out.print(ftp.getReplyString());
				logger.error(ftp.getReplyString());
				if(!FTPReply.isPositiveCompletion(ftp.getReplyCode())){
					logger.error("FTP server 拒绝连接.");
				}
				logger.error("用户" + ftpUID + "登录失败");
				ftp.disconnect();
				ftp = null;
				return false;
			}
			logger.info("成功登录FTP服务器");
		}catch (Exception e1){
			logger.error("系统错误", e1);
			ftp = null;
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	public void logout(){
		try{
			if(ftp != null){
				ftp.logout();
				ftp.disconnect();
				ftp = null;
				logger.info("ftp断开连接成功");
			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("ftp断开连接错误", e);
		}
	}

	/**
	 * @param fileName 服务器上待删除文件名
	 * @throws Exception
	 */
	public boolean delFile(String SourcePath, String fileName) throws Exception{
		if(!login()){
			return false;
		}
		boolean bdir = ftp.changeWorkingDirectory(SourcePath);
		if(!bdir){
			logger.error("工作目录:" + SourcePath + "不存在");
			return false;
		}
		boolean isok = ftp.deleteFile(fileName);
		if(isok){
			logger.info("删除服务器[" + SourcePath + "]下文件[" + fileName + "]成功");
		}else{
			logger.info("删除服务器[" + SourcePath + "]下文件[" + fileName + "]失败");
		}
		return isok;
	}

	/**
	 * 转码[ISO-8859-1 -> GBK] 不同的平台需要不同的转码
	 * @param obj
	 * @return
	 */
	// private String iso8859togbk(Object obj){
	// try{
	// if(obj == null){
	// return "";
	// }else{
	// return new String(obj.toString().getBytes("iso-8859-1"),
	// "GB18030");
	// }
	// }catch (Exception e){
	// return "";
	// }
	// }
	public static void main(String[] args){
		try{
			PropertyConfigurator
					.configure("E:\\workspace\\ylcs\\common\\log4j.properties");
			FtpHelp fh = new FtpHelp("10.168.168.10", 21, "anonymous",
					"anonymous");
			List names = fh.getOKFileNameList("/ylcsdata/");
			for(Iterator iter = names.iterator(); iter.hasNext();){
				String n = (String) iter.next();
				if(fh.downLoadFile("/ylcsdata/", n) != null){
					fh.delFile("/ylcsdata/", n);
				}
			}
			fh.logout();
		}catch (Exception e){
			e.printStackTrace();
		}catch (Throwable e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
