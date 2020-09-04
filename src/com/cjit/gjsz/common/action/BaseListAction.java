package com.cjit.gjsz.common.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.cjit.common.action.BaseAction;
import com.cjit.common.constant.Constants;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.ExcelHelper;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.common.model.ExportSheet;
import com.cjit.gjsz.datadeal.model.RptBusiDataInfo;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.system.model.User;

public abstract class BaseListAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	protected PaginationList paginationList = null; // 分页信息列表对象
	protected PaginationList paginationList1 = null; // 分页信息列表对象
	protected int curPage = 1;
	protected int toCreate = 0;
	protected String dbType = ""; // 数据库类型标志
	protected transient Log log = LogFactory.getLog(this.getClass());
	public static final String TYPE_STRING = "1";// 字符串的
	public static final String TYPE_NUM = "2";// 数字的
	public static final String TYPE_DATE = "3";// Date的
	// 数据采集范围（1：银行自身外债；2：代客业务）
	protected String busiDataType = "";
	// 文件业务类型（外债/对外担保/···/银行月度外汇资产负债信息 or
	// 合格境外机构投资者境内证券投资/···/境内个人参与境外上市公司股权激励计划）
	protected String infoType = "";
	protected Map configMap = null;// 存储数据库中的配置项信息
	protected List orgConfigList = null;
	// 启用国际收支网上申报系统与银行业务系统数据接口规范版本 填写版本号数字(1.1/1.2/...) 默认1.1
	protected String interfaceVer = "";
	// 打回基础信息时，是否将全部有效状态(非0)的下游报文联动打回 yes/no 默认no
	protected String dataslowerstatusBaseAllstatus = "";
	// 是否根据主报告行号汇总生成报文 yes/no 默认no
	protected String configFileGenRptTitle = "";
	// 是否可在数据录入、数据校验、信息审核、数据打回页面查询所有授权机构 yes/no 默认no
	protected String configSearchAllOrg = "";
	// 是否联动打回下游报文数据 yes/no 默认no
	protected String configLowerStatusLinkage = "";
	// 是否禁止手动新增信息 yes/no 默认no
	protected String configForbidAdd = "";
	// 是否禁止手动保存信息 yes/no 默认no
	protected String configForbidSave = "";
	// 是否禁止手动删除信息 yes/no 默认no
	protected String configForbidDelete = "";
	// 是否忽略掉提交操作 yes/no 默认no
	protected String configOverleapCommit = "";
	// 是否忽略掉审核操作 yes/no 默认no
	protected String configOverleapAudit = "";
	// 删除操作是否需要审核 yes/no 默认no
	protected String configDeleteNeedAudit = "";
	// 是否在自身业务主单据中自动填充12位金融机构标识码字段 yes/no 默认no config.self.autoInput.branchCode
	protected String configSelfAutoInputBranchCode = "";
	// 是否在自身业务主单据中校验12位金融机构标识码为本行系统中代码 yes/no 默认no config.check.self.branchCode
	protected String configCheckSelfBranchCode = "";
	// 定义仅在月底生成报文的表单FileType名称，使用,隔开 config.fileGen.onlyEndOfMonth
	protected String configFileGenOnlyEndOfMonth = "";
	// 是否在报文生成页面能够手工选择单据 yes/no 默认no config.fileGen.chooseTables
	protected String configFileGenChooseTables = "";
	// 是否在报文生成页面提供预校验功能 yes/no 默认no config.fileGen.preVerify
	protected String configFileGenPreVerify = "";
	// 对于月报报文，是否启用次月第五个工作日前可自动生成的逻辑 yes/no 默认n config.fileGen.month.fiveDays
	protected String configFileGemMonthFiveDays = "";
	// 是否每日都允许手工生成月报类报文 yes/no 默认no config.fileGen.month.manualEveryday
	protected String configFileGemMonthManualEveryday = "";
	// 当接收到错误反馈时，将对应记录状态置成何值 1/7 默认为7-已生成
	protected String changeDataStatus = "";
	// 定义需要关联签约信息的报表FileType名称，使用,隔开 config.related.filetype
	protected String relatedFileType = "";
	// 客户标识 银行简称或字母缩写
	protected String configCustomerFlag = "";
	// 补录页面增加打印功能开始
	private String isPrint = "0";
	// 报文类型
	protected String fileType = "";
	// 业务团队
	protected String teamId;
	//
	protected String sendPack = "";
	//
	protected String autoShow = "no";
	// 外债变动、余额信息高级查询中的签约类型查询条件
	protected final String CONTRACTTYPE = "ContractType";
	// 是否查询下级机构
	protected String searchLowerOrg;
	// 是否集群 yes/no 默认no config.is.cluster
	protected String configIsCluster = "";
	// 是否关联业务类型
	protected String linkBussType;
	// 报告期
	protected String buocMonth;
	// 报告期
	protected List buocMonthList = new ArrayList();

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	// DFHL:补录页面增加打印功能结束
	protected List ids;

	/**
	 * 取得分页变量
	 * 
	 * @return
	 */
	public BaseListAction() {
		// DFHL:增加翻页返回功能
		paginationList = paginationList == null ? new PaginationList()
				: paginationList;
		paginationList1 = paginationList1 == null ? new PaginationList()
				: paginationList1;
		if (curPage >= 1) {
			paginationList.setCurrentPage(curPage);
		}

	}

	/**
	 * 得到当前用户
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		User user = (User) session.get(Constants.USER);
		if (user == null) {
			throw new RuntimeException("用户尚未登录");
		}
		return user;
	}

	protected void setConfigParametersBaseList(Map map) {
		if (map != null) {
			// ALL
			// 是否启用国际收支网上申报系统与银行业务系统数据接口规范（1.2版） yes/no 默认no
			String interfaceVer = (String) map.get("config.interface.ver");
			if (StringUtils.isNotEmpty(interfaceVer)) {
				this.interfaceVer = interfaceVer;
			}
			// 打回基础信息时，是否将全部有效状态(非0)的下游报文联动打回 yes/no 默认no
			String dataslowerstatusBaseAllstatus = (String) map
					.get("config.dataslowerstatus.base.allstatus");
			if (StringUtils.isNotEmpty(dataslowerstatusBaseAllstatus)) {
				this.dataslowerstatusBaseAllstatus = dataslowerstatusBaseAllstatus;
			}
			// 是否根据主报告行号汇总生成报文 yes/no 默认no
			String configFileGenRptTitle = (String) map
					.get("config.fileGen.rpttitle");
			if (StringUtils.isNotEmpty(configFileGenRptTitle)) {
				this.configFileGenRptTitle = configFileGenRptTitle;
			}
			// 是否可在数据录入、数据校验、信息审核、数据打回页面查询所有授权机构 yes/no 默认no
			String searchAllorg = (String) map.get("config.search.allorg");
			if (StringUtils.isNotEmpty(searchAllorg)) {
				this.configSearchAllOrg = searchAllorg;
			}
			// 是否联动打回下游报文数据 yes/no 默认no
			String configLowerStatusLinkage = (String) map
					.get("config.lowerstatus.linkage");
			if (StringUtils.isNotEmpty(configLowerStatusLinkage)) {
				this.configLowerStatusLinkage = configLowerStatusLinkage;
			}
			// 是否禁止手动新增信息 yes/no 默认no
			String forbidAdd = (String) map.get("config.forbid.add");
			if (StringUtils.isNotEmpty(forbidAdd)) {
				this.configForbidAdd = forbidAdd;
			}
			// 是否禁止手动保存信息 yes/no 默认no
			String forbidSave = (String) map.get("config.forbid.save");
			if (StringUtils.isNotEmpty(forbidSave)) {
				this.configForbidSave = forbidSave;
			}
			// 是否禁止手动删除信息 yes/no 默认no
			String forbidDelete = (String) map.get("config.forbid.delete");
			if (StringUtils.isNotEmpty(forbidDelete)) {
				this.configForbidDelete = forbidDelete;
			}
			// 是否忽略掉提交操作 yes/no 默认no
			String configOverleapCommit = (String) map
					.get("config.overleap.commit");
			if (StringUtils.isNotEmpty(configOverleapCommit)) {
				this.configOverleapCommit = configOverleapCommit;
			}
			// 是否忽略掉审核操作 yes/no 默认no
			String configOverleapAudit = (String) map
					.get("config.overleap.audit");
			if (StringUtils.isNotEmpty(configOverleapAudit)) {
				this.configOverleapAudit = configOverleapAudit;
			}
			// 删除操作是否需要审核 yes/no 默认no
			String configDeleteNeedAudit = (String) map
					.get("config.delete.need.audit");
			if (StringUtils.isNotEmpty(configDeleteNeedAudit)) {
				this.configDeleteNeedAudit = configDeleteNeedAudit;
			}
			// 编辑查看数据时，是否在页面自动展开明细信息 yes/no 默认no
			String tautoShow = (String) map.get("config.autoShow");
			if (StringUtils.isNotEmpty(tautoShow)) {
				this.setAutoShow(tautoShow);
			}
			// 是否在自身业务主单据中自动填充12位金融机构标识码字段 yes/no 默认no
			String configSelfAutoInputBranchCode = (String) map
					.get("config.self.autoInput.branchCode");
			if (StringUtils.isNotEmpty(configSelfAutoInputBranchCode)) {
				this.configSelfAutoInputBranchCode = configSelfAutoInputBranchCode;
			}
			// 是否在自身业务主单据中校验12位金融机构标识码为本行系统中代码 yes/no
			String configCheckSelfBranchCode = (String) map
					.get("config.check.self.branchCode");
			if (StringUtils.isNotEmpty(configCheckSelfBranchCode)) {
				this.configCheckSelfBranchCode = configCheckSelfBranchCode;
			}
			// 定义仅在月底生成报文的表单FileType名称，使用,隔开
			String configFileGenOnlyEndOfMonth = (String) map
					.get("config.fileGen.onlyEndOfMonth");
			if (StringUtils.isNotEmpty(configFileGenOnlyEndOfMonth)) {
				this.configFileGenOnlyEndOfMonth = configFileGenOnlyEndOfMonth;
			}
			// 是否在报文生成页面能够手工选择单据 yes/no 默认no
			String configFileGenChooseTables = (String) map
					.get("config.fileGen.chooseTables");
			if (StringUtils.isNotEmpty(configFileGenChooseTables)) {
				this.configFileGenChooseTables = configFileGenChooseTables;
			}
			// 是否在报文生成页面提供预校验功能 yes/no 默认no
			String configFileGenPreVerify = (String) map
					.get("config.fileGen.preVerify");
			if (StringUtils.isNotEmpty(configFileGenPreVerify)) {
				this.configFileGenPreVerify = configFileGenPreVerify;
			}
			// 对于月报报文，是否启用次月第五个工作日前可自动生成的逻辑
			String configFileGemMonthFiveDays = (String) map
					.get("config.fileGen.month.fiveDays");
			if (StringUtils.isNotEmpty(configFileGemMonthFiveDays)) {
				this.configFileGemMonthFiveDays = configFileGemMonthFiveDays;
			}
			// 是否每日都允许手工生成月报类报文
			String configFileGemMonthManualEveryday = (String) map
					.get("config.fileGen.month.manualEveryday");
			if (StringUtils.isNotEmpty(configFileGemMonthManualEveryday)) {
				this.configFileGemMonthManualEveryday = configFileGemMonthManualEveryday;
			}
			// 是否关联业务类型
			String linkBussType = (String) map.get("config.buss.type.teamid");
			if (StringUtils.isNotEmpty(linkBussType)) {
				this.linkBussType = linkBussType;
			}
			// 当接收到错误反馈时，将对应记录状态置成何值 1/7 默认为7-已生成
			String changeDataStatus = (String) map
					.get("config.changeDataStatus.forErrFeedBack");
			if (StringUtils.isNotEmpty(changeDataStatus)) {
				this.changeDataStatus = changeDataStatus;
			}
			// 定义需要关联签约信息的报表FileType名称，使用,隔开
			String relatedFileType = (String) map
					.get("config.related.filetype");
			if (StringUtils.isNotEmpty(relatedFileType)) {
				this.relatedFileType = relatedFileType;
			}
		}
	}

	public PaginationList getPaginationList() {
		return paginationList;
	}

	public void setPaginationList(PaginationList paginationList) {
		this.paginationList = paginationList;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	public String getDbType() {
		return dbType == null ? "" : dbType.toLowerCase().trim();
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public Map getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map configMap) {
		this.configMap = configMap;
	}

	public String getInterfaceVer() {
		return interfaceVer;
	}

	public void setInterfaceVer(String interfaceVer) {
		this.interfaceVer = interfaceVer;
	}

	public String getDataslowerstatusBaseAllstatus() {
		return dataslowerstatusBaseAllstatus;
	}

	public void setDataslowerstatusBaseAllstatus(
			String dataslowerstatusBaseAllstatus) {
		this.dataslowerstatusBaseAllstatus = dataslowerstatusBaseAllstatus;
	}

	public String getBusiDataType() {
		return busiDataType;
	}

	public void setBusiDataType(String busiDataType) {
		this.busiDataType = busiDataType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	protected String getInfoTypeName(String infoType, List busiDataInfoList) {
		if ("ALL".equals(infoType)) {
			return null;
		}
		if (busiDataInfoList != null && StringUtil.isNotEmpty(infoType)) {
			for (int i = 0; i < busiDataInfoList.size(); i++) {
				RptBusiDataInfo busiData = (RptBusiDataInfo) busiDataInfoList
						.get(i);
				if (infoType.equals(busiData.getBusiInfoID())) {
					return busiData.getBusiInfoName();
				}
			}
		}
		return infoType;
	}

	protected String getBusiDataType(String infoType, List busiDataInfoList) {
		if (StringUtil.isNotEmpty(infoType)) {
			if (busiDataInfoList != null) {
				for (int i = 0; i < busiDataInfoList.size(); i++) {
					RptBusiDataInfo busiData = (RptBusiDataInfo) busiDataInfoList
							.get(i);
					if (infoType.equals(busiData.getBusiInfoID())) {
						return busiData.getBusiDataType();
					}
				}
				return null;
			} else {
				if ("A".equals(infoType) || "B".equals(infoType)
						|| "C".equals(infoType) || "D".equals(infoType)
						|| "E".equals(infoType) || "F".equals(infoType)) {
					return "1";
				} else {
					return "2";
				}
			}
		} else if (busiDataInfoList != null) {
			return ((RptBusiDataInfo) busiDataInfoList.get(0))
					.getBusiDataType();
		} else {
			return null;
		}
	}

	protected String getContractTypeSQL(String op, String values) {
		StringBuffer sb = new StringBuffer();
		sb
				.append(
						" and exists (select tc.businessid from T_CFA_A_EXDEBT tc ")
				.append(" where tc.businessno = t.businessno ")
				.append(
						" and tc.filetype in (select filetype from t_rpt_table_info where filetype like 'A%' and busi_name ");
		if ("like".equals(op)) {
			sb.append(" like '%").append(values).append("%-签约信息' ");
		} else {
			sb.append(op).append(" '").append(values).append("-签约信息' ");
		}
		sb.append(")) ");
		return sb.toString();
	}

	/**
	 * 得到添加、生成报文时，业务记录的查询条件
	 * 
	 * @param tableSelectId
	 *            业务表名称
	 * @param fileType
	 *            当前查询单据文件类型
	 * @param relatedFileType
	 *            定义为下游单据的文件类型
	 * @param sameBatchCA
	 *            同批报文中是否包含CA单据
	 * @return String
	 */
	protected String getAddFileGenSearchConditonSQL(String tableSelectId,
			String fileType, String relatedFileType, boolean sameBatchCA,
			String buocMonth) {
		String searchCondition = null;
		if (StringUtil.isNotEmpty(buocMonth)) {
			searchCondition = " buocMonth = '" + buocMonth + "' ";
			return searchCondition;
		}
		if (relatedFileType.indexOf(fileType) > -1) {
			// 当前添加的单据不是签约信息时，增加限制条件，要求对应签约信息曾经报送过
			// String rptNoColumnId =
			// DataUtil.getRptNoColumnIdByFileType(fileType);
			searchCondition = " exists (select 1 from " + tableSelectId
					+ " tc where tc.fileType <> '" + fileType
					+ "' and t.businessNo = tc.businessNo ";
			if ("yes".equalsIgnoreCase(this.linkBussType)) {
				searchCondition += " and (exists (select 1 from V_USER_BUSS_TYPE_FROM_FMSS v "
						+ " where v.res_detail_value = tc.teamId "
						+ " and v.USER_ID = '"
						+ this.getCurrentUser().getId()
						+ "') or tc.teamId is null) ";
			}
			searchCondition += " and (exists (select 1 from t_rpt_send_commit s where s.tableID = '"
					+ tableSelectId + "' and s.businessId = tc.businessId) ";
			if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)) {
				// 参数配置报文生成页面不能手工选择单据，说明单据是默认全选的
				// 则允许相关签约信息是高于审核通过状态，表示此次一并生成报文
				searchCondition += " or tc.datastatus > "
						+ DataUtil.SHYTG_STATUS_NUM + ")) and t.filetype = '"
						+ fileType + "' ";
			} else {
				searchCondition += ")) and t.filetype = '" + fileType + "' ";
			}
		} else {
			// 当添加的单据是签约信息时
			// 当签约信息操作类型为删除时，要求不可存在未成功报送删除或逻辑删除的下游单据
			searchCondition = " ((not exists (select 1 from "
					+ tableSelectId
					+ " t2 where t2.businessno = t.businessno and t2.filetype <> '"
					+ fileType
					+ "' and ((t2.datastatus <> "
					+ DataUtil.YBS_STATUS_NUM
					+ " and t2.actiontype = 'D') or ((t2.actiontype <> 'D' or t2.actiontype is null) and t2.datastatus <> "
					+ DataUtil.DELETE_STATUS_NUM
					+ "))) and t.actiontype = 'D') or t.actiontype <> 'D') and t.filetype = '"
					+ fileType + "' ";
			if ("DA".equals(fileType)) {
				// 当前单据为境外担保项下境内贷款时 通过贷款币种是否为人民币，限制国内外汇贷款编号是否已存在
				searchCondition += " and ((t.CREDCURRCODE <> 'CNY' and (t.DOFOEXLOCODE is not null or exists (select businessid from t_cfa_c_dofoexlo c where c.businessno = t.businessno and (c.DOFOEXLOCODE is not null ";
				if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)) {
					// 不可手工选择单据时，判断同批报文中是否包含CA单据
					if (sameBatchCA) {
						// 同批报文中包含CA单据，对应国内外汇贷款信息可以是审核通过状态，表示可同批生成报文
						searchCondition += " or c.datastatus >= ";
					} else {
						// 同批报文中不包含CA单据，对应国内外汇贷款信息需高于审核通过状态
						searchCondition += " or c.datastatus > ";
					}
					searchCondition += DataUtil.SHYTG_STATUS_NUM
							+ ")))) or t.CREDCURRCODE = 'CNY') ";
				} else {
					// 可手工选择单据时，要求对应国内外汇贷款信息已上报过（编号已存在）
					searchCondition += ")))) or t.CREDCURRCODE = 'CNY') ";
				}
			}
			if ("yes".equalsIgnoreCase(this.linkBussType)) {
				searchCondition += " and (exists (select 1 from V_USER_BUSS_TYPE_FROM_FMSS v "
						+ " where v.res_detail_value = t.teamId "
						+ " and v.USER_ID = '"
						+ this.getCurrentUser().getId()
						+ "') or t.teamId is null) ";
			}
		}
		// return searchCondition;
		return null;
	}

	protected String getCannotAddFileGenSearchConditonSQL(String tableSelectId,
			String fileType, String relatedFileType, boolean sameBatchCA,
			String buocMonth) {
		String searchCondition = null;
		if (StringUtil.isNotEmpty(buocMonth)) {
			searchCondition = " buocMonth = '" + buocMonth + "' ";
			return searchCondition;
		}
		if (relatedFileType.indexOf(fileType) > -1) {
			// 当前添加的单据不是签约信息时，增加限制条件，要求对应签约信息曾经报送过
			searchCondition = " exists (select 1 from " + tableSelectId
					+ " tc where tc.fileType <> '" + fileType
					+ "' and t.businessNo = tc.businessNo ";
			if ("yes".equalsIgnoreCase(this.linkBussType)) {
				searchCondition += " and (exists (select 1 from V_USER_BUSS_TYPE_FROM_FMSS v "
						+ " where v.res_detail_value = tc.teamId "
						+ " and v.USER_ID = '"
						+ this.getCurrentUser().getId()
						+ "') or tc.teamId is null) ";
			}
			searchCondition += " and (exists (select s.businessId from t_rpt_send_commit s where s.tableID = '"
					+ tableSelectId + "' and s.businessId = tc.businessId) ";
			if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)) {
				// 参数配置报文生成页面不能手工选择单据，说明单据是默认全选的
				// 则允许相关签约信息是审核通过状态，表示此次一并生成报文
				searchCondition += " or tc.datastatus >= "
						+ DataUtil.SHYTG_STATUS_NUM + ")) ";
			} else {
				searchCondition += ")) ";
			}
			searchCondition += " and t.filetype = '" + fileType
					+ "' and t.datastatus < " + DataUtil.SHYTG_STATUS_NUM
					+ " and t.datastatus <> " + DataUtil.DELETE_STATUS_NUM;
		} else {
			// 当添加的单据是签约信息时
			searchCondition = " t.fileType = '"
					+ fileType
					+ "' and "
					+ " ((t.datastatus < "
					+ DataUtil.SHYTG_STATUS_NUM
					+ " and t.datastatus <> "
					+ DataUtil.DELETE_STATUS_NUM
					+ " and ((not exists (select 1 from "
					+ tableSelectId
					+ " t2 where t2.businessno = t.businessno and t2.filetype <> '"
					+ fileType
					+ "' and ((t2.datastatus <> "
					+ DataUtil.YBS_STATUS_NUM
					+ " and t2.actiontype = 'D') or ((t2.actiontype <> 'D' or t2.actiontype is null) and t2.datastatus <> "
					+ DataUtil.DELETE_STATUS_NUM
					+ "))) and t.actiontype = 'D') or t.actiontype <> 'D') ";
			searchCondition += ") or (t.datastatus = "
					+ DataUtil.SHYTG_STATUS_NUM
					+ " and exists (select 1 from "
					+ tableSelectId
					+ " t2 where t2.businessno = t.businessno and t2.filetype <> '"
					+ fileType
					+ "' and ((t2.datastatus <> "
					+ DataUtil.YBS_STATUS_NUM
					+ " and t2.actiontype = 'D') or ((t2.actiontype <> 'D' or t2.actiontype is null) and t2.datastatus <> "
					+ DataUtil.DELETE_STATUS_NUM
					+ "))) and t.actiontype = 'D'))";
			if ("DA".equals(fileType)) {
				// 当前单据为境外担保项下境内贷款时 通过贷款币种是否为人民币，限制国内外汇贷款编号是否已存在
				searchCondition += " and ((t.CREDCURRCODE <> 'CNY' and (t.DOFOEXLOCODE is not null or exists (select businessid from t_cfa_c_dofoexlo c where c.businessno = t.businessno and (c.DOFOEXLOCODE is not null ";
				if (!"yes".equalsIgnoreCase(this.configFileGenChooseTables)) {
					// 不可手工选择单据时，判断同批报文中是否包含CA单据
					if (sameBatchCA) {
						searchCondition += " or c.datastatus >= ";
					} else {
						searchCondition += " or c.datastatus > ";
					}
					searchCondition += DataUtil.SHYTG_STATUS_NUM
							+ ")))) or t.CREDCURRCODE = 'CNY') ";
				} else {
					// 可手工选择单据时，要求对应国内外汇贷款信息已上报过（编号已存在）
					searchCondition += ")))) or t.CREDCURRCODE = 'CNY') ";
				}
			}
			if ("yes".equalsIgnoreCase(this.linkBussType)) {
				searchCondition += " and (exists (select 1 from V_USER_BUSS_TYPE_FROM_FMSS v "
						+ " where v.res_detail_value = t.teamId "
						+ " and v.USER_ID = '"
						+ this.getCurrentUser().getId()
						+ "') or t.teamId is null) ";
			}
		}
		// return searchCondition;
		return null;
	}

	/**
	 * 根据列配置信息，构造查询SQL中所要查的字段，当存在子表时，计入子表ID集
	 * 
	 * @param rptColumnList
	 *            列配置信息
	 * @param columns
	 *            查询SQL
	 * @param subTableIdList
	 *            包含子表ID
	 */
	protected void createSearchSqlByColumnList(List rptColumnList,
			StringBuffer columns, List subTableIdList) {
		int cFlag = 0;
		if (columns == null) {
			columns = new StringBuffer();
		}
		if (subTableIdList == null) {
			subTableIdList = new ArrayList();
		}
		for (Iterator i = rptColumnList.iterator(); i.hasNext();) {
			RptColumnInfo column = (RptColumnInfo) i.next();
			if (column.getDataType().equals("table")) {
				subTableIdList.add(column.getColumnId());
				continue;
			}
			// 赋别名c1,c2,c3
			column.setAliasColumnId("c" + (++cFlag));
			// 根据字段物理名和别名拼查询SQL
			if (column.getTagType().startsWith("n")) {
				// 将数值类字段转换为字符型数值
				if ("oracle".equalsIgnoreCase(this.getDbType())) {
					columns.append("to_char(t.");
				} else if ("db2".equalsIgnoreCase(this.getDbType())) {
					columns.append("char(t.");
				} else if ("sqlserver".equalsIgnoreCase(this.getDbType())) {
					columns.append("convert(varchar(50), t.");
				} else {
					columns.append("(t.");
				}
				columns.append(column.getColumnId()).append(") as ").append(
						column.getAliasColumnId()).append(",");
			} else {
				columns.append("t.").append(column.getColumnId())
						.append(" as ").append(column.getAliasColumnId())
						.append(",");
			}
		}
	}

	/**
	 * 通用EXCEL导出
	 * 
	 * @param titleList
	 *            标题行信息
	 * @param sourceDataList
	 *            数据信息
	 * @param fileName
	 *            导出文件名
	 * @throws Exception
	 * @throws IOException
	 */
	protected void exportToExcel(ExportSheet[] sheets, String fileName)
			throws Exception, IOException {
		OutputStream out = response.getOutputStream();
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="
				+ fileName + ".xls");
		HSSFWorkbook wb = null;
		HSSFSheet sheetMain = null;// 主表页
		ExcelHelper exHelp = null;
		try {
			wb = new HSSFWorkbook();
			exHelp = new ExcelHelper(wb);
			// 样式准备
			HSSFCellStyle csTitleG = exHelp // 样式：标题绿色
					.getTitleCS(HSSFColor.GREEN.index);
			HSSFCellStyle csData = exHelp.getDataCS(); // 样式：文本型
			HSSFDataFormat df = exHelp.getDataFormatText();
			csData.setDataFormat(df.getFormat("@"));// 文本型
			HSSFFont fontRed = exHelp.getFont(HSSFColor.RED.index);
			HSSFFont fontGreen = exHelp.getFont(HSSFColor.GREEN.index);
			HSSFFont fontBlue = exHelp.getFont(HSSFColor.BLUE.index);
			HSSFCellStyle csColorDataRed = exHelp.getDataCS(); // 样式：红色字体-校验用
			csColorDataRed.setFont(fontRed);
			HSSFCellStyle csColorDataGreen = exHelp.getDataCS(); // 样式：绿色字体-校验用
			csColorDataGreen.setFont(fontGreen);
			HSSFCellStyle csColorDataBlue = exHelp.getDataCS(); // 样式：蓝色字体-校验用
			csColorDataBlue.setFont(fontBlue);
			// 逐个创建sheet页
			for (int s = 0; s < sheets.length; s++) {
				ExportSheet sheet = sheets[s];
				List titleList = sheet.getTitleList();
				List sourceDataList = sheet.getSourceDataList();
				// 构造第一个页签中报文主表信息 Begin
				sheetMain = wb.createSheet(sheet.getSheetName());
				short nCellHeader = 0;
				// 表头 title
				// 创建主表页Excel第一行
				HSSFRow row = sheetMain.createRow(0);
				// 创建Cell-序号
				exHelp.setCell(row, nCellHeader, csTitleG, "序号");
				sheetMain.setColumnWidth(nCellHeader++, (short) 2000);
				for (Iterator i = titleList.iterator(); i.hasNext();) {
					// 创建Cell
					String title = (String) i.next();
					exHelp.setCell(row, nCellHeader, csTitleG, title);
					sheetMain.setColumnWidth(nCellHeader++, (short) 4000);
				}
				// title end
				// 从第二行开始创建所有导出报文数据信息
				if (sourceDataList != null) {
					for (int i = 0; i < sourceDataList.size(); i++) {
						row = sheetMain.createRow(i + 1);
						row.setHeight((short) 550);
						ListOrderedMap sourceDateMap = (ListOrderedMap) sourceDataList
								.get(i);
						short nCellData = 0;
						exHelp.setCell(row, nCellData++, csData, String
								.valueOf(i + 1));
						for (int j = 0; j < titleList.size(); j++) {
							String title = (String) titleList.get(j);
							Object obj = sourceDateMap.get(title);
							String value = obj == null ? "" : obj.toString();
							exHelp.setCell(row, nCellData++, csData, value);
						}
					}
				}
			}
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("写文件输出流时出现错误....");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构造"报告期"字段下拉框选项
	 * 
	 * @return List<Dictionary<ValueStandardNum,Name>>
	 */
	protected List initBuocMonthSelectList() {
		List yearMonthList = new ArrayList();
		String currentYearMonth = DateUtils.serverCurrentDate("yyyyMM");
		int year = Integer.valueOf(currentYearMonth.substring(0, 4)).intValue();
		int month = Integer.valueOf(currentYearMonth.substring(4, 6))
				.intValue();
		// 构造当前日期前一年内所有月份记录
		int addMonth = month;
		while (addMonth <= 12) {
			Dictionary tag = new Dictionary();
			if (addMonth < 10) {
				String addYearMonth = String.valueOf(year - 1) + "0"
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			} else {
				String addYearMonth = String.valueOf(year - 1)
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			}
			yearMonthList.add(tag);
			addMonth++;
		}
		// 构造当前日期所在年内所有月份记录
		addMonth = 1;
		while (addMonth <= 12) {
			Dictionary tag = new Dictionary();
			if (addMonth < 10) {
				String addYearMonth = String.valueOf(year) + "0"
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			} else {
				String addYearMonth = String.valueOf(year)
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			}
			yearMonthList.add(tag);
			addMonth++;
		}
		// 构造截至下一年当前月的所有记录
		addMonth = 1;
		while (addMonth <= month) {
			Dictionary tag = new Dictionary();
			if (addMonth < 10) {
				String addYearMonth = String.valueOf(year + 1) + "0"
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			} else {
				String addYearMonth = String.valueOf(year + 1)
						+ String.valueOf(addMonth);
				tag.setValueStandardNum(addYearMonth);
				tag.setName(addYearMonth);
			}
			yearMonthList.add(tag);
			addMonth++;
		}
		return yearMonthList;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getToCreate() {
		return toCreate;
	}

	public void setToCreate(int toCreate) {
		this.toCreate = toCreate;
	}

	protected CacheManager cacheManager;
	protected String tableUniqueSelectId;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public String getConfigFileGenRptTitle() {
		return configFileGenRptTitle;
	}

	public void setConfigFileGenRptTitle(String configFileGenRptTitle) {
		this.configFileGenRptTitle = configFileGenRptTitle;
	}

	public List getOrgConfigList() {
		return orgConfigList;
	}

	public void setOrgConfigList(List orgConfigList) {
		this.orgConfigList = orgConfigList;
	}

	public String getTableUniqueSelectId() {
		return tableUniqueSelectId;
	}

	public void setTableUniqueSelectId(String tableUniqueSelectId) {
		this.tableUniqueSelectId = tableUniqueSelectId;
	}

	public String getSendPack() {
		return sendPack;
	}

	public void setSendPack(String sendPack) {
		this.sendPack = sendPack;
	}

	public String getConfigSearchAllOrg() {
		return configSearchAllOrg;
	}

	public void setConfigSearchAllOrg(String configSearchAllOrg) {
		this.configSearchAllOrg = configSearchAllOrg;
	}

	public String getConfigLowerStatusLinkage() {
		return configLowerStatusLinkage;
	}

	public void setConfigLowerStatusLinkage(String configLowerStatusLinkage) {
		this.configLowerStatusLinkage = configLowerStatusLinkage;
	}

	public String getAutoShow() {
		return autoShow;
	}

	public void setAutoShow(String autoShow) {
		this.autoShow = autoShow;
	}

	public String getConfigForbidAdd() {
		return configForbidAdd;
	}

	public void setConfigForbidAdd(String configForbidAdd) {
		this.configForbidAdd = configForbidAdd;
	}

	public String getConfigForbidSave() {
		return configForbidSave;
	}

	public void setConfigForbidSave(String configForbidSave) {
		this.configForbidSave = configForbidSave;
	}

	public String getConfigForbidDelete() {
		return configForbidDelete;
	}

	public void setConfigForbidDelete(String configForbidDelete) {
		this.configForbidDelete = configForbidDelete;
	}

	public String getConfigOverleapCommit() {
		return configOverleapCommit;
	}

	public void setConfigOverleapCommit(String configOverleapCommit) {
		this.configOverleapCommit = configOverleapCommit;
	}

	public String getConfigOverleapAudit() {
		return configOverleapAudit;
	}

	public void setConfigOverleapAudit(String configOverleapAudit) {
		this.configOverleapAudit = configOverleapAudit;
	}

	public String getConfigDeleteNeedAudit() {
		return configDeleteNeedAudit;
	}

	public void setConfigDeleteNeedAudit(String configDeleteNeedAudit) {
		this.configDeleteNeedAudit = configDeleteNeedAudit;
	}

	public String getSearchLowerOrg() {
		return searchLowerOrg;
	}

	public void setSearchLowerOrg(String searchLowerOrg) {
		this.searchLowerOrg = searchLowerOrg;
	}

	public String getConfigIsCluster() {
		return configIsCluster;
	}

	public void setConfigIsCluster(String configIsCluster) {
		this.configIsCluster = configIsCluster;
	}

	public String getLinkBussType() {
		return linkBussType;
	}

	public void setLinkBussType(String linkBussType) {
		this.linkBussType = linkBussType;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getChangeDataStatus() {
		return changeDataStatus;
	}

	public void setChangeDataStatus(String changeDataStatus) {
		this.changeDataStatus = changeDataStatus;
	}

	public String getRelatedFileType() {
		return relatedFileType;
	}

	public void setRelatedFileType(String relatedFileType) {
		this.relatedFileType = relatedFileType;
	}

	public String getBuocMonth() {
		return buocMonth;
	}

	public void setBuocMonth(String buocMonth) {
		this.buocMonth = buocMonth;
	}

	public List getBuocMonthList() {
		return buocMonthList;
	}

	public void setBuocMonthList(List buocMonthList) {
		this.buocMonthList = buocMonthList;
	}

	public PaginationList getPaginationList1() {
		return paginationList1;
	}

	public void setPaginationList1(PaginationList paginationList1) {
		this.paginationList1 = paginationList1;
	}

	public String getConfigCustomerFlag() {
		return configCustomerFlag;
	}

	public void setConfigCustomerFlag(String configCustomerFlag) {
		this.configCustomerFlag = configCustomerFlag;
	}

}