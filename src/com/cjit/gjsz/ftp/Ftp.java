package com.cjit.gjsz.ftp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 
 * @作者: lihaiboA
 * @日期: Dec 10, 2013 4:57:48 PM
 * @描述: [Ftp]FTP工具类
 */
public class Ftp{

	/**
	 * <p>方法名称: isPathExists|描述: 验证FTP路径是否存在</p>
	 * @param ftpclient
	 * @param ftpPath
	 * @return boolean
	 */
	public static boolean isPathExists(FTPClient ftpclient, String ftpPath)
			throws Exception{
		boolean pathExists = false;
		if(ftpclient != null && StringUtils.isNotEmpty(ftpPath)){
			if(ftpPath.startsWith("/")){
				ftpPath = ftpPath.substring(1);
			}
			if(ftpPath.indexOf("/") > 0){
				String ftpPathPath = ftpPath.substring(0, ftpPath
						.lastIndexOf("/"));
				String ftpPathName = ftpPath.substring(
						ftpPath.lastIndexOf("/") + 1, ftpPath.length());
				FTPFile[] ftpFiles = ftpclient.listFiles(ftpPathPath);
				for(int i = 0; i < ftpFiles.length; i++){
					if(ftpFiles[i].isDirectory()
							&& ftpFiles[i].getName().equalsIgnoreCase(
									ftpPathName)){
						pathExists = true;
						break;
					}
				}
			}else{
				FTPFile[] ftpFiles = ftpclient.listFiles();
				for(int i = 0; i < ftpFiles.length; i++){
					if(ftpFiles[i].isDirectory()
							&& ftpFiles[i].getName().equalsIgnoreCase(ftpPath)){
						pathExists = true;
						break;
					}
				}
			}
		}
		return pathExists;
	}
}
