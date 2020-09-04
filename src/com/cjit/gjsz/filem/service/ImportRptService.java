package com.cjit.gjsz.filem.service;

import java.io.File;
import java.util.List;

public interface ImportRptService{

	/**
	 * 解析导入的xml文件
	 * @param file 导入报文
	 * @param ch 导入报文标识
	 * @param sb 反馈信息记录
	 * @return List
	 */
	// public List resolveImportXmlFile_(File file, char ch, StringBuffer sb);
	/**
	 * 解析导入的xml文件
	 * @param file 导入报文
	 * @param rptColumnList 列信息
	 * @param tableId 数据表名
	 * @param sb 反馈信息记录
	 * @return List
	 */
	public List resolveImportXmlFile(File file, List rptColumnList,
			String tableId, StringBuffer sb);
}