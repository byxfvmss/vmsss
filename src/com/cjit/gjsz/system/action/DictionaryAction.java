/**
 * AuthorityAction
 */
package com.cjit.gjsz.system.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;

/**
 * @author huboA
 */
public class DictionaryAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5503681331878965820L;
	private UserInterfaceConfigService userInterfaceConfigService;
	private LogManagerService logManagerService;
	private ColumnInfo columnInfo = new ColumnInfo();
	private Dictionary dictionary = new Dictionary();
	private List types = new ArrayList();
	private List tables = new ArrayList();
	private List columns = new ArrayList();
	private ByteArrayInputStream is;

	public ByteArrayInputStream getIs(){
		return is;
	}

	public void setIs(ByteArrayInputStream is){
		this.is = is;
	}

	private void init(){
		types = userInterfaceConfigService.getTableInfosByType(null);
		TableInfo info = new TableInfo();
		info.setType(dictionary.getType());
		info.setTableId(dictionary.getTableId());
		if(StringUtil.isNotEmpty(dictionary.getType())){
			info = userInterfaceConfigService.getTableInfosByTypeId(info);
			tables = userInterfaceConfigService.getTableInfos(info);
		}
		if(StringUtil.isNotEmpty(dictionary.getTableId())){
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setTableId(dictionary.getTableId());
			columns = userInterfaceConfigService.getColumnInfos(columnInfo);
		}
	}

	public String list(){
		try{
			init();
			boolean ok = false;
			if(StringUtil.isNotEmpty(dictionary.getTableId())){
				columnInfo.setTableId(dictionary.getTableId());
				ok = true;
			}
			if(StringUtil.isNotEmpty(dictionary.getTypeName())){
				columnInfo.setTypeName(dictionary.getTypeName());
				ok = true;
			}
			if(ok){
				userInterfaceConfigService.getDictionarys(columnInfo,
						paginationList);
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		try{
			dictionary = userInterfaceConfigService.getDictionary(dictionary
					.getId());
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String update(){
		User user = this.getCurrentUser();
		try{
			userInterfaceConfigService.updateDictionary(dictionary);
			logManagerService.writeLog(request, user, "0006.0006", "系统管理.字典维护",
					"更新", "修改字典信息", "1");
			return SUCCESS;
		}catch (Exception e){
			logManagerService.writeLog(request, user, "0006.0006", "系统管理.字典维护",
					"更新", "修改字典信息", "0");
			e.printStackTrace();
		}
		return ERROR;
	}

	public String refreshCache(){
		try{
			userInterfaceConfigService.removeDictionarysByCache();
			is = new ByteArrayInputStream("success".getBytes("UTF-8"));
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			try{
				is = new ByteArrayInputStream("fail".getBytes("UTF-8"));
			}catch (UnsupportedEncodingException e1){
				e1.printStackTrace();
			}
		}
		return SUCCESS;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public ColumnInfo getColumnInfo(){
		return columnInfo;
	}

	public void setColumnInfo(ColumnInfo columnInfo){
		this.columnInfo = columnInfo;
	}

	public Dictionary getDictionary(){
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary){
		this.dictionary = dictionary;
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

	public void setLogManagerService(LogManagerService logManagerService){
		this.logManagerService = logManagerService;
	}
}
