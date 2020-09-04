package com.cjit.gjsz.logic;

import java.util.ArrayList;
import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.model.SearchModel;

public class SearchAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7209809119151463393L;
	private SearchService searchService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private SearchModel searchModel = new SearchModel();
	private List types = new ArrayList();
	private List tables = new ArrayList();
	private List columns = new ArrayList();

	private void init(){
		types = userInterfaceConfigService.getTableInfosByType(null);
		TableInfo info = new TableInfo();
		info.setType(searchModel.getType());
		info.setTableId(searchModel.getTableId());
		if(StringUtil.isNotEmpty(searchModel.getType())){
			info = userInterfaceConfigService.getTableInfosByTypeId(info);
			tables = userInterfaceConfigService.getTableInfos(info);
		}
		if(StringUtil.isNotEmpty(searchModel.getTableId())){
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setTableId(searchModel.getTableId());
			columns = userInterfaceConfigService.getColumnInfos(columnInfo);
		}
	}

	public String search(){
		init();
		if(StringUtil.isNotEmpty(searchModel.getTableId())){
			searchService.search(searchModel, paginationList);
		}
		return SUCCESS;
	}

	public List getTypes(){
		return types;
	}

	public void setTypes(List types){
		this.types = types;
	}

	public List getTables(){
		return tables;
	}

	public void setTables(List tables){
		this.tables = tables;
	}

	public List getColumns(){
		return columns;
	}

	public void setColumns(List columns){
		this.columns = columns;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}

	public SearchModel getSearchModel(){
		return searchModel;
	}

	public void setSearchModel(SearchModel searchModel){
		this.searchModel = searchModel;
	}
}
