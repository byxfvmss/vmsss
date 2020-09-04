package com.cjit.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:全局翻页类
 * 
 * @version
 * @since 2008-6-30 下午10:17:17
 */
public class PaginationList{

	private List recordList = new ArrayList(); // 当前分页包含的数据纪录列表
	private long recordCount = 0; // 总纪录数
	private int pageSize = 20; // 每页默认步长
	private int currentPage = 1; // 当前页码
	private String showCount = "true";

	public String getPageSizeStr(){
		return (new Integer(pageSize)).toString();
	}

	public PaginationList(){
	}

	public int getCurrentPage(){
                if(currentPage==0){
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	public String getCurrentPageStr(){
		return Integer.toString(currentPage);
	}

	public int getPageSize(){
		return pageSize;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public long getRecordCount(){
		return recordCount;
	}

	public void setRecordCount(long recordCount){
		this.recordCount = recordCount;
	}

	public String getRecordCountStr(){
		return Long.toString(recordCount);
	}

	public List getRecordList(){
		return recordList;
	}

	public void setRecordList(List recordList){
		this.recordList = recordList;
	}

	/*
	 * 根据当前页和每页步长获取当前页开始的纪录序号
	 */
	public int getPageStart(){
		return (this.currentPage - 1) * this.pageSize + 1;
	}

	public String getShowCount() {
		return showCount;
	}

	public void setShowCount(String showCount) {
		this.showCount = showCount;
	}
}
