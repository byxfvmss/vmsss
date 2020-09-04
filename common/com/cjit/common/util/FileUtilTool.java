package com.cjit.common.util;

import java.io.File;

import javax.servlet.ServletContext;

/**
 * 文件处理工具类
 * 
 * @since Jun 13, 2008
 */
public class FileUtilTool{

	/**
	 * 取得文件相对路径
	 * @param basePath 基路径
	 * @param basePath 文件绝对路径
	 * @return String 文件相对路径
	 * 
	 * @since Jun 16, 2008
	 */
	public static String getRealPath(String basePath, String rePath){
		return rePath.substring(basePath.length());
	}

	/**
	 * 取得文件后缀
	 * @param fileName 文件名
	 * @return String 后缀名
	 * 
	 * @since Jun 16, 2008
	 */
	public static String getExtension(String fileName){
		if(fileName != null){
			int i = fileName.lastIndexOf('.');
			if(i > 0 && i < fileName.length() - 1){
				return fileName.substring(i + 1).toLowerCase();
			}
		}
		return "";
	}

	/**
	 * 取得文件前缀
	 * @param fileName 文件名
	 * @return String 文件前缀
	 * 
	 * @since Jun 16, 2008
	 */
	public static String getPrefix(String fileName){
		if(fileName != null){
			int i = fileName.lastIndexOf('.');
			if(i > 0 && i < fileName.length() - 1){
				return fileName.substring(0, i);
			}
		}
		return "";
	}

	/**
	 * 删除上下文环境中的一个文件
	 * @param fileName 文件名
	 * @param application 系统上下文环境
	 * 
	 * @since Jun 16, 2008
	 */
	public static void deleteFile(ServletContext application, String filePath){
		if(StringUtil.isNotBlank(filePath)){
			String physicalFilePath = application.getRealPath(filePath);
			if(StringUtil.isNotBlank(physicalFilePath)){
				File file = new File(physicalFilePath);
				file.delete();
			}
		}
	}
}
