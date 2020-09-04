/**
 * UserInterfaceConfigAction
 */
package com.cjit.gjsz.interfacemanager.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.interfacemanager.model.ColumnInfo;
import com.cjit.gjsz.interfacemanager.model.ImportModel;
import com.cjit.gjsz.interfacemanager.model.LoadData;
import com.cjit.gjsz.interfacemanager.model.PageManager;
import com.cjit.gjsz.interfacemanager.model.TableInfo;
import com.cjit.gjsz.interfacemanager.model.UploadModel;
import com.cjit.gjsz.interfacemanager.model.UserInterface;
import com.cjit.gjsz.interfacemanager.service.ImportDataService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceService;
import com.cjit.gjsz.interfacemanager.util.ImportDateUtil;

/**
 * @author huboA
 */
public class UserInterfaceConfigAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5552051394036857578L;
	private UserInterfaceService userInterfaceService;
	private UserInterfaceConfigService userInterfaceConfigService;
	private ImportDataService importDataService;
	private List types = new ArrayList();
	private List tables = new ArrayList();
	private List columns = new ArrayList();
	private List userInterfaces = new ArrayList();
	private List dictionarys = new ArrayList();
	private List loadDatas = new ArrayList();
	private PageManager pageManager = new PageManager();
	private UploadModel uploadModel = new UploadModel();
	private LoadData loadData = new LoadData();

	public LoadData getLoadData(){
		return loadData;
	}

	public void setLoadData(LoadData loadData){
		this.loadData = loadData;
	}

	public List getLoadDatas(){
		return loadDatas;
	}

	public void setImportDataService(ImportDataService importDataService){
		this.importDataService = importDataService;
	}

	public UploadModel getUploadModel(){
		return uploadModel;
	}

	public void setUploadModel(UploadModel uploadModel){
		this.uploadModel = uploadModel;
	}

	private void init(){
		types = userInterfaceConfigService.getTableInfosByType(null);
		TableInfo info = new TableInfo();
		info.setType(pageManager.getType());
		info.setTableId(pageManager.getTableId());
		if(StringUtil.isEmpty(info.getType())
				&& StringUtil.isNotEmpty(info.getTableId())){
			TableInfo tmp = userInterfaceConfigService.getTableInfo(pageManager
					.getTableId());
			info.setType(tmp.getType());
			pageManager.setType(tmp.getType());
		}
		if(StringUtil.isNotEmpty(pageManager.getType())){
			info = userInterfaceConfigService.getTableInfosByTypeId(info);
			tables = userInterfaceConfigService.getTableInfos(info);
		}
		if(StringUtil.isNotEmpty(pageManager.getTableId())){
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setTableId(pageManager.getTableId());
			columns = userInterfaceConfigService.getColumnInfos(columnInfo);
		}
		if(StringUtil.equals(pageManager.getSuccess(), "true")){
			this.addActionMessage("数据导入成功。");
		}
		if(StringUtil.equals(pageManager.getVerify(), "true")){
			this.addActionMessage("数据校验完成。");
		}
	}

	public String viewData(){
		init();
		if(loadData.getLoadDate() == null){
			loadData.setLoadDate(new Date());
		}
		try{
			if(StringUtil.isNotEmpty(loadData.getOrgId())){
				String start = DateUtils.toString(loadData.getLoadDate(),
						DateUtils.ORA_DATES_FORMAT);
				Date startDate = DateUtils.stringToDate(
						start + " 00:00:00.000",
						DateUtils.ORA_DATE_TIMES_FORMAT);
				Date endDate = DateUtils.getNextDay(startDate);
				loadData.setStartDate(startDate);
				loadData.setEndDate(endDate);
				loadDatas = this.userInterfaceConfigService
						.getImportList(loadData);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String upload(){
		init();
		if(pageManager.getCurrentDate() == null){
			pageManager.setCurrentDate(DateUtils.toString(new Date(),
					DateUtils.ORA_DATES_FORMAT));
		}
		return SUCCESS;
	}

	public String importData(){
		try{
			File file = uploadModel.getFile();
			if(file == null){
				return SUCCESS;
			}
			UserInterface userInterface = new UserInterface();
			String fileName = uploadModel.getFileFileName();
			userInterface.setFileType(fileName.split("\\.")[1]);
			userInterface.setName(fileName.split("\\.")[0]);
			userInterface = userInterfaceService
					.getUserInterfaceByFullName(userInterface);
			if(userInterface == null){
				upload();
				this.addActionMessage("该单据还未配置接口,请先在接口管理中进行相应设置");
				return INPUT;
			}
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setTxtId(userInterface.getId());
			// TODO:(FWY)自己添加的东西
			// columnInfo.setTableId(userInterface.getName());;
			ColumnInfo col = userInterfaceConfigService
					.getColumnInfoByUserInterface(columnInfo);
			if(col == null){
				upload();
				this.addActionMessage("该单据还未配置接口对照管理,请先在接口管理中进行相应设置");
				return INPUT;
			}
			String tableId = col.getTableId();
			List list = ImportDateUtil.getDate(file, userInterface);
			TableInfo tableInfo = this.userInterfaceConfigService
					.getTableInfo(tableId);
			List columnInfos = importDataService.getColumnInfos(tableId); // 得到该表下的所有列项
			List systemDictionarys = importDataService.getAllDictionarys(); // 得到系统所有字典项
			ImportModel model = new ImportModel(); // 配置 JavaBean参数,用于数据库导入
			List baseTables = userInterfaceConfigService
					.findTableRelation(tableId);
			List allTables = userInterfaceConfigService.getTableInfos(null);
			String sql = ImportDateUtil.getSQL(columnInfos, tableId,
					baseTables, model); // 生成insertSql语句
			String[] dateTypes = ImportDateUtil.getDateType(columnInfos); // 得到数据类型
			int[] nums = ImportDateUtil.getDateNum(columnInfos); // 得到配置的顺序号,与导入的文本对应起来
			String[] dictionarys = ImportDateUtil.getDictionary(columnInfos); // 得到所有字典项
			String[] columnInfoIds = ImportDateUtil
					.getColumnInfoIds(columnInfos);
			String[] columnInfoNames = ImportDateUtil
					.getColumnInfoNames(columnInfos);
			String[] columnInfoDescriptions = ImportDateUtil
					.getColumnInfoDescriptions(columnInfos);
			model.sql = sql;
			model.records = list;
			model.nums = nums;
			model.types = dateTypes;
			model.dictionarys = dictionarys;
			model.systemDictionarys = systemDictionarys;
			model.columnInfoIds = columnInfoIds;
			model.columnInfoNames = columnInfoNames;
			model.columnInfoDescriptions = columnInfoDescriptions;
			model.orgId = loadData.getOrgId();
			model.tableId = tableId;
			model.tableName = tableInfo.getTableName();
			model.txtId = userInterface.getId();
			model.businessId = userInterface.getPrimaryKey();
			model.startLine = userInterface.getStartLine();
			model.currentDate = loadData.getLoadDate();
			model.allTables = allTables;
			System.out.println("sql " + sql);
			String errorMsg = importDataService.saveImportDate(model); // 开始导入数据库
			System.out.println(errorMsg);
			if(StringUtil.isNotEmpty(errorMsg)){
				viewData();
				this.pageManager.setErrorMessage(errorMsg);
				return INPUT;
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		throw new RuntimeException("导入数据发生异常");
	}

	public String create(){
		try{
			init();
			userInterfaces = userInterfaceService.findUserInterface(null);
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setTableId(pageManager.getTableId());
			dictionarys = userInterfaceConfigService.getDictionarys(columnInfo);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String view(){
		try{
			init();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public String save(){
		try{
			String[] columnIds = pageManager.getColumnIds();
			String[] txtColumnIds = pageManager.getTxtColumnIds();
			String[] dictionaryTypeIds = pageManager.getDictionaryTypeIds();
			String[] txtIds = pageManager.getTxtIds();
			String[] shows = pageManager.getShows();
			String[] orders = pageManager.getOrders();
			String[] modifys = pageManager.getModifys();
			if(columnIds != null && columnIds.length > 0){
				for(int i = 0; i < columnIds.length; i++){
					ColumnInfo columnInfo = new ColumnInfo();
					columnInfo.setTableId(pageManager.getTableId());
					columnInfo.setColumnId(columnIds[i]);
					columnInfo = this.userInterfaceConfigService
							.getColumnInfo(columnInfo);
					// 如果选择了接口文件,则下面的字典配置和接口列配置都需要设置
					if(StringUtil.isNotEmpty(txtIds[i])){
						columnInfo.setTxtId(Integer.parseInt(txtIds[i]));
						columnInfo.setDictionaryTypeId(dictionaryTypeIds[i]);
						columnInfo.setShow(shows[i]);
						columnInfo.setModify(modifys[i]);
						columnInfo.setOrder(Integer.parseInt(orders[i]));
						if(StringUtil.isNotEmpty(txtColumnIds[i])){
							columnInfo.setTxtColumnId(Integer
									.parseInt(txtColumnIds[i]));
						}else{
							columnInfo.setTxtColumnId(0);
						}
					}else{// 否则所有配置全部还原
						columnInfo.setTxtId(0);
						columnInfo.setTxtColumnId(0);
					}
					userInterfaceConfigService.updateColumnInfo(columnInfo);
				}
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	public void setUserInterfaceService(
			UserInterfaceService userInterfaceService){
		this.userInterfaceService = userInterfaceService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService){
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public PageManager getPageManager(){
		return pageManager;
	}

	public void setPageManager(PageManager pageManager){
		this.pageManager = pageManager;
	}

	public List getTypes(){
		return types;
	}

	public List getTables(){
		return tables;
	}

	public void setTypes(List types){
		this.types = types;
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

	public List getUserInterfaces(){
		return userInterfaces;
	}

	public void setUserInterfaces(List userInterfaces){
		this.userInterfaces = userInterfaces;
	}

	public List getDictionarys(){
		return dictionarys;
	}

	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}
}
