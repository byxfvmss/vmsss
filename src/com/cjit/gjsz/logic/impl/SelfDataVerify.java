package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.cache.CacheManager;
import com.cjit.gjsz.cache.CacheabledMap;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.Fal_E01Entity;
import com.cjit.gjsz.logic.model.Fal_X01Entity;
import com.cjit.gjsz.logic.model.SearchModel;
import com.opensymphony.util.BeanUtils;

public abstract class SelfDataVerify implements DataVerify {

	protected List dictionarys;
	protected List verifylList;
	protected String interfaceVer;
	protected SearchService service;
	protected CacheabledMap cache;
	protected Map configMap;
	private String checkBusiNo;// 是否校验业务编号字段不能为空
	private String checkBranchCode;// 是否在自身业务主单据中校验12位金融机构标识码为本行系统中代码
	private String limitBranchCode;// 是否在自身业务主单据中限制12位金融机构标识码为本记录所属机构的代码

	public SelfDataVerify() {
	}

	public SelfDataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster) {
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.interfaceVer = interfaceVer;
		if ("yes".equals(isCluster)) {
			UserInterfaceConfigService userInterfaceConfigService = (UserInterfaceConfigService) SpringContextUtil
					.getBean("userInterfaceConfigService");
			configMap = userInterfaceConfigService.initConfigParameters();
		} else {
			cache = (CacheabledMap) CacheManager.getCacheObject("paramCache");
			if (cache != null) {
				configMap = (Map) cache.get("configMap");
			}
		}
		if (configMap != null) {
			checkBusiNo = (String) configMap
					.get("config.checkBusinessNo.cannotNull");
			checkBranchCode = (String) configMap
					.get("config.check.self.branchCode");
			limitBranchCode = (String) configMap
					.get("config.limit.self.branchCode");
		}
	}

	/**
	 * 校验两个日期，前一日期必须早于或等于后一日期
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean
	 */
	public boolean verifyTwoDates(String date1, String date2) {
		if (StringUtil.isNotEmpty(date1) && StringUtil.isNotEmpty(date2)) {
			Date dDate1 = DateUtils.stringToDate(date1,
					DateUtils.ORA_DATE_FORMAT);
			Date dDate2 = DateUtils.stringToDate(date2,
					DateUtils.ORA_DATE_FORMAT);
			if (dDate1.getTime() <= dDate2.getTime()) {
				return true;
			}
		}
		return false;
	}

	public boolean verifyCustcode(String custcode, String custype) {
		// DFHL: 组织机构代码校验 start
		if (StringUtil.equalsIgnoreCase(custype, "C")) {
			if (StringUtil.isEmpty(custcode)) {
				return false;
			}
			if (!verifyCustcode(custcode)) {
				return false;
			}
			return true;
		} else {
			return StringUtil.isEmpty(custcode);
		}
	}

	/**
	 * 组织机构代码 必输， 技监局代码，代码必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。
	 * 
	 * @param custcode
	 *            组织机构代码
	 * @return
	 */
	public static boolean verifyCustcode(String custcode) {
		if (StringUtil.isEmpty(custcode) || custcode.trim().length() != 9) {
			return false;
		}
		if (StringUtil.equals(custcode, "000000000")) {
			return false;
		}
		int[] arr = { 3, 7, 9, 10, 5, 8, 4, 2 };
		String str = custcode.substring(0, custcode.length() - 1);
		String str2 = String.valueOf(custcode.charAt(custcode.length() - 1));
		int sum = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int b = 0;
			if (ch >= '0' && ch <= '9') {
				b = ch - 48;
			} else if (ch >= 'A' && ch <= 'Z') {
				b = ch - 55;
			} else {
				return false;
			}
			sum = sum + (b * arr[i]);
		}
		int val = 11 - (sum % 11);
		if (val == 11) {
			if (str2.equals("0")) {
				return true;
			}
			return false;
		} else if (val == 10) {
			if (str2.equals("X")) {
				return true;
			}
			return false;
		} else {
			char c = str2.charAt(0);
			if (c >= 'A' && c <= 'Z') {
				int d1 = c - 55;
				if (d1 == val) {
					return true;
				}
				return false;
			} else {
				if (str2.equals(val + "")) {
					return true;
				}
				return false;
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: verifyDictionaryValue|描述: 字典项校验
	 * </p>
	 * 
	 * @param codeType
	 *            字典项code_type
	 * @param key
	 *            数据值
	 * @return boolean
	 */
	public boolean verifyDictionaryValue(String codeType, String key) {
		return findKey(dictionarys, codeType, key, null);
	}

	public boolean verifyDictionaryValue(String codeType, String key,
			String tableId) {
		return findKey(dictionarys, codeType, key, tableId);
	}

	/**
	 * 得到值
	 * 
	 * @param dataKey
	 * @param key
	 * @return
	 */
	public String getKey(String dataKey, String key) {
		return getKey(dictionarys, key, dataKey);
	}

	/**
	 * 根据字典记录集查找某个字典项是否存在
	 * 
	 * @param dictionarys
	 * @param dataKey
	 * @param key
	 * @return
	 */
	protected boolean findKey(List dictionarys, String dataKey, String key,
			String tableId) {
		if (key == null) {
			return true;
		}
		if (CollectionUtil.isNotEmpty(dictionarys)) {
			if (!COUNTRY.equals(dataKey)) {
				for (int i = 0; i < dictionarys.size(); i++) {
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if (StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)) {
						if (StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardLetter())) { // 如果ValueBlank为空,默认不需要转换
							if (StringUtil.isEmpty(tableId)) {
								return true; // 将行内代码码值转成标准代码值
							} else {
								if (dictionary.getTableId().toUpperCase()
										.indexOf(tableId.toUpperCase()) >= 0) {
									return true; // 将行内代码码值转成标准代码值
								}
							}
						}
					}
				}
			} else {
				for (int i = 0; i < dictionarys.size(); i++) {
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if (StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)) { // 如果找到国家地区
						if (StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardNum())
								|| StringUtil.equalsIgnoreCase(key, dictionary
										.getValueStandardLetter())) { // 如果ValueBlank为空,默认不需要转换
							return true; // 将行内代码码值转成标准代码值
						}
					}
				}
			}
			return false;
		}
		throw new RuntimeException("字典表不能为空");
	}

	protected String getKey(List dictionarys, String dataKey, String key) {
		if (CollectionUtil.isNotEmpty(dictionarys)) {
			if (!COUNTRY.equals(dataKey)) {
				for (int i = 0; i < dictionarys.size(); i++) {
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if (StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)) {
						if (StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardLetter())) { // 如果ValueBlank为空,默认不需要转换
							return dictionary.getName();
						}
					}
				}
			} else {
				for (int i = 0; i < dictionarys.size(); i++) {
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if (StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)) {
						if (StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardNum())
								|| StringUtil.equalsIgnoreCase(key, dictionary
										.getValueStandardLetter())) { // 如果ValueBlank为空,默认不需要转换
							return dictionary.getName();
						}
					}
				}
			}
			return key;
		}
		throw new RuntimeException("字典表不能为空");
	}

	/**
	 * 判断签约信息可否报送删除操作 查询有无下游已报送的信息，若存在则不可报删除
	 * 
	 * @param tableId
	 * @param fileType
	 *            签约信息文件类型
	 * @param rptNo
	 * @return boolean
	 */
	protected boolean verifyCannotDelete(String tableId, String fileType,
			String rptNo) {
		String rptNoColumnId = DataUtil.getRptNoColumnIdByFileType(fileType);
		StringBuffer sbSearchCondition = new StringBuffer();
		sbSearchCondition
				.append(" ")
				.append(rptNoColumnId)
				.append(" = '")
				.append(rptNo)
				.append("' and fileType <> '")
				.append(fileType)
				.append(
						"' and (businessid in (select s.businessid from t_rpt_send_commit s, ")
				.append(tableId).append(" c ").append(
						" where s.businessid = c.businessid and s.tableid = '")
				.append(tableId).append(
						"' and s.is_receive = '1' and c.datastatus <> ")
				.append(DataUtil.YBS_STATUS_NUM).append(
						" and (c.actiontype <> '").append(ACTIONTYPE_D).append(
						"' or c.actiontype is null)) or (datastatus = ")
				.append(DataUtil.YBS_STATUS_NUM).append(" and actionType <> '")
				.append(ACTIONTYPE_D).append("')) ");
		SearchModel searchModel = new SearchModel();
		searchModel.setTableId(tableId);
		searchModel.setSearchCondition(sbSearchCondition.toString());
		List list = service.search(searchModel);
		if (list != null && list.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean checkBusinessNoisNull(String businessNo) {
		if ("yes".equalsIgnoreCase(this.checkBusiNo) && isNull(businessNo)) {
			return true;
		}
		return false;
	}

	protected boolean checkBusinessNoRepeat(String businessNo, String tableId,
			String fileType, String businessId) {
		if (StringUtil.isNotEmpty(businessNo) && StringUtil.isNotEmpty(tableId)
				&& StringUtil.isNotEmpty(fileType)) {
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId(tableId);
			StringBuffer searchCondition = new StringBuffer();
			searchCondition.append(" businessNo = '").append(businessNo)
					.append("' ");
			if (!fileType.startsWith("A")) {
				if (fileType.endsWith("A") || fileType.equalsIgnoreCase("FD")) {
					if (fileType.equalsIgnoreCase("FA")
							|| fileType.equalsIgnoreCase("FD")) {
						searchCondition
								.append(" and (fileType = 'FA' or fileType = 'FD') ");
					} else {
						searchCondition.append(" and fileType = '").append(
								fileType).append("' ");
					}
				} else {
					if (fileType.startsWith("B")) {
						searchCondition.append(" and fileType = 'BA' ");
					} else if (fileType.startsWith("C")) {
						searchCondition.append(" and fileType = 'CA' ");
					} else if (fileType.startsWith("D")) {
						searchCondition.append(" and fileType = 'DA' ");
					} else if (fileType.startsWith("E")) {
						searchCondition.append(" and fileType = 'EA' ");
					} else if (fileType.startsWith("F")) {
						searchCondition.append(" and fileType = 'FA' ");
					}
				}
			} else {
				searchCondition
						.append(" and fileType <> 'AR' and fileType <> 'AS' ");
			}
			if (StringUtil.isNotEmpty(businessId)) {
				searchCondition.append(" and businessId <> '").append(
						businessId).append("' ");
			}
			searchModel.setSearchCondition(searchCondition.toString());
			if (service == null) {
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
			}
			List list = service.search(searchModel);
			if (list != null && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected boolean limitBranchCode(String branchCode, String fileType,
			String businessId, String instCode) {
		if ("yes".equalsIgnoreCase(this.limitBranchCode)
				&& StringUtil.isNotEmpty(fileType)
				&& (StringUtil.isNotEmpty(businessId) || StringUtil
						.isNotEmpty(instCode))) {
			String tableId = DataUtil.getTableIdByFileType(fileType);
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition.append(" rptNo = '").append(branchCode).append(
					"' ");
			if (StringUtil.isNotEmpty(businessId)) {
				sbSearchCondition.append(
						" and org_id in (select instcode from ")
						.append(tableId).append(" where businessId = '")
						.append(businessId).append("') ");
			} else if (StringUtil.isNotEmpty(instCode)) {
				sbSearchCondition.append(" and org_id = '").append(instCode)
						.append("' ");
			}
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId("T_ORG_CONFIG");
			searchModel.setSearchCondition(sbSearchCondition.toString());
			long count = service.getCount(searchModel);
			if (count > 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	protected boolean checkBranchCode(String branchCode) {
		if ("yes".equalsIgnoreCase(this.checkBranchCode)) {
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition.append(" rptNo = '").append(branchCode).append(
					"' ");
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId("T_ORG_CONFIG");
			searchModel.setSearchCondition(sbSearchCondition.toString());
			long count = service.getCount(searchModel);
			if (count > 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询前期的变动信息列表
	 * 
	 * @param fileType
	 *            文件类型
	 * @param changeDateColumnId
	 *            变动日期对应数据库表字段名称
	 * @param changeDate
	 *            变动日期值
	 * @param changeNoColumnId
	 *            变动编号对应数据库表字段名称
	 * @param changeNo
	 *            变动编号值
	 * @param rptNo
	 *            申报号码
	 * @param businessNo
	 *            业务编号
	 * @param businessId
	 *            业务ID
	 * @param orderBy
	 *            排序条件
	 * @return List
	 */
	protected List findChangeInfoList(String fileType, String changeNoColumnId,
			String changeNo, String rptNo, String businessNo,
			String businessId, String orderBy) {
		if (StringUtil.isNotEmpty(fileType)) {
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId(DataUtil.getTableIdByFileType(fileType));
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition
					.append(" fileType = '")
					.append(fileType)
					.append("' and datastatus <> ")
					.append(DataUtil.DELETE_STATUS_NUM)
					.append(
							" and (actionType <> 'D' or (actionType = 'D' and datastatus <> ")
					.append(DataUtil.YBS_STATUS_NUM).append("))");
			if (StringUtil.isNotEmpty(businessId)) {
				sbSearchCondition.append(" and businessId <> '").append(
						businessId).append("' ");
			}
			if (StringUtil.isNotEmpty(businessNo)) {
				sbSearchCondition.append(" and businessNo = '").append(
						businessNo).append("' ");
			} else if (StringUtil.isNotEmpty(rptNo)) {
				sbSearchCondition.append(" and ").append(
						DataUtil.getRptNoColumnIdByFileType(fileType)).append(
						" = '").append(rptNo).append("' ");
			}
			if (StringUtil.isNotEmpty(changeNo)
					&& StringUtil.isNotEmpty(changeNoColumnId)) {
				sbSearchCondition.append(" and ").append(changeNoColumnId)
						.append(" <= '").append(changeNo).append("' ");
			}
			searchModel.setSearchCondition(sbSearchCondition.toString());
			if (StringUtil.isNotEmpty(orderBy)) {
				searchModel.setOrderBy(orderBy);
			}
			List list = service.search(searchModel);
			return list;
		}
		return null;
	}

	/**
	 * <p>
	 * 方法名称: checkByeRptNoRepeat|描述: 检验是否有重复的变动编号
	 * </p>
	 * 
	 * @param fileType
	 * @param byeRptNo
	 * @param businessNo
	 * @return boolean
	 */
	protected boolean checkByeRptNoRepeat(String fileType, String byeRptNo,
			String businessNo) {
		if (configMap != null) {
			String checkByeRptNoRepeat = (String) configMap
					.get("config.check.byeRptNo.repeat");
			if (!"yes".equalsIgnoreCase(checkByeRptNoRepeat)) {
				return false;
			}
		}
		if (StringUtil.isNotEmpty(fileType) && StringUtil.isNotEmpty(byeRptNo)
				&& StringUtil.isNotEmpty(businessNo)) {
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId(DataUtil.getTableIdByFileType(fileType));
			String byeRptNoColumnId = DataUtil
					.getByeRptNoColumnIdByFileType(fileType);
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition
					.append(" fileType = '")
					.append(fileType)
					.append("' and businessNo = '")
					.append(businessNo)
					.append("' and ")
					.append(byeRptNoColumnId)
					.append(" = '")
					.append(byeRptNo)
					.append("' and datastatus <> ")
					.append(DataUtil.DELETE_STATUS_NUM)
					.append(
							" and (actionType <> 'D' or (actionType = 'D' and datastatus <> ")
					.append(DataUtil.YBS_STATUS_NUM).append("))");
			searchModel.setSearchCondition(sbSearchCondition.toString());
			long count = service.getCount(searchModel);
			if (count > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * 方法名称: checkFormerByeRptNo|描述: 检验是否存在未审核通过的前期变动信息 From HSBC
	 * </p>
	 * 
	 * @param fileType
	 * @param byeRptNo
	 * @param businessNo
	 * @return boolean
	 */
	protected boolean checkFormerByeRptNo(String fileType, String byeRptNo,
			String businessNo) {
		if (StringUtil.isNotEmpty(fileType) && StringUtil.isNotEmpty(byeRptNo)
				&& StringUtil.isNotEmpty(businessNo)) {
			SearchModel searchModel = new SearchModel();
			searchModel.setTableId(DataUtil.getTableIdByFileType(fileType));
			String byeRptNoColumnId = DataUtil
					.getByeRptNoColumnIdByFileType(fileType);
			StringBuffer sbSearchCondition = new StringBuffer();
			sbSearchCondition.append(" fileType = '").append(fileType).append(
					"' and businessNo = '").append(businessNo).append("' and ")
					.append(byeRptNoColumnId).append(" < '").append(byeRptNo)
					.append("' and datastatus <> ").append(
							DataUtil.DELETE_STATUS_NUM).append(
							" and datastatus < ").append(
							DataUtil.SHYTG_STATUS_NUM).append(" ");
			searchModel.setSearchCondition(sbSearchCondition.toString());
			long count = service.getCount(searchModel);
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证变动日期必需晚于先前录入的变动日期
	 * 
	 * @param changeInfoList
	 * @return boolean
	 */
	protected boolean verifyChangeDate(List changeInfoList,
			String changeDateColumnId, String changeDate) {
		if (changeInfoList != null && changeInfoList.size() > 0
				&& StringUtil.isNotEmpty(changeDateColumnId)) {
			for (int i = 0; i < changeInfoList.size(); i++) {
				Object obj = changeInfoList.get(i);
				String date = BeanUtils.getValue(obj,
						changeDateColumnId.toLowerCase()).toString();
				if (date != null && changeDate != null) {
					Date dDate1 = DateUtils.stringToDate(date,
							DateUtils.ORA_DATE_FORMAT);
					Date dDate2 = DateUtils.stringToDate(changeDate,
							DateUtils.ORA_DATE_FORMAT);
					if (dDate1.getTime() > dDate2.getTime()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据主体类型代码验证是否为境外金融机构
	 * 
	 * @param mainBodyType
	 * @param limitBank
	 *            是否限制为银行类金融机构
	 * @return boolean
	 */
	protected boolean verifyOverseaFinanceOrg(String mainBodyType,
			boolean limitBank) {
		if ("20001200".equals(mainBodyType) || "20001300".equals(mainBodyType)
				|| "20001401".equals(mainBodyType)
				|| "20001402".equals(mainBodyType)
				|| "20001403".equals(mainBodyType)
				|| "20001501".equals(mainBodyType)
				|| "20001502".equals(mainBodyType)) {
			// 判断是否限制为银行类境外金融机构
			if (limitBank) {
				// 20001401-境外-银行类存款机构-境外银行
				// 20001402-境外-银行类存款机构-中资银行海外分支及附属机构
				// 20001403-境外-银行类存款机构-境内银行离岸部
				if ("20001401".equals(mainBodyType)
						|| "20001402".equals(mainBodyType)
						|| "20001403".equals(mainBodyType)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断空值
	 */
	public boolean isNull(Object value) {
		if (value == null || value.toString().trim().equals(""))
			return true;
		else
			return false;
	}

	public static String verifyRptNo(String rptNo, String fileType) {
		if (rptNo.length() != 28) {
			return "不符合编写规范，长度应为28位";
		} else if (StringUtil.isNotEmpty(fileType)
				&& "AR,AS,BB,BC,CB,DB,EB,FB,FC".indexOf(fileType) < 0) {
			if (!rptNo.startsWith(DataUtil
					.getSelfBussTypeCodeByFileType(fileType))) {
				if (fileType.startsWith("A")) {
					return "与当前表单债务类型不相符";
				} else {
					return "与当前表单业务类型不相符";
				}
			}
		}
		return null;
	}

	public static String verifyFalSnoCode(String rptNo, String fileType) {
		if (rptNo.length() != 30) {
			return "不符合编写规范，长度应为28位";
		}
		return null;
	}

	protected String checkE01(Fal_E01Entity e01) {
		StringBuffer sbCondition = new StringBuffer();
		sbCondition.append(" buocMonth = '").append(e01.getBuocmonth()).append(
				"' and instCode = '").append(e01.getInstcode()).append(
				"' and businessId <> '").append(e01.getBusinessid()).append(
				"' and E0102 = '").append(e01.getE0102()).append(
				"' and dataStatus > ").append(DataUtil.DELETE_STATUS_NUM)
				.append(" and ((actionType = '").append(ACTIONTYPE_D).append(
						"' and dataStatus <> ").append(DataUtil.YBS_STATUS_NUM)
				.append(") or actionType <> '").append(ACTIONTYPE_D).append(
						"') ");
		SearchModel searchModel = new SearchModel();
		searchModel.setTableId("T_FAL_E01");
		searchModel.setSearchCondition(sbCondition.toString());
		if (service == null) {
			service = (SearchService) SpringContextUtil
					.getBean("searchService");
		}
		List e01List = service.search(searchModel);
		Map mapE01 = new HashMap();
		if (CollectionUtil.isNotEmpty(e01List)) {
			for (Iterator i = e01List.iterator(); i.hasNext();) {
				Fal_E01Entity e = (Fal_E01Entity) i.next();
				if (e.getE0103() != null) {
					mapE01.put(e.getE0101(), e.getE0103());
				} else {
					mapE01.put(e.getE0101(), new BigDecimal(0.0));
				}
			}
		}
		// 在国别相同的情况下，项目的金额必须满足：
		// 1100≥1101；
		// 1200=1201+1202+1203+1204+1205+1206+1207+1208+1209+1210+1211+1212+1214；
		// 1600=1601+1602；
		// 1700≥1701；
		// 2100≥2101；
		// 2200=2201+2202+2203+2204+2205+2206+2207+2208+2209+2210+2211+2212+2213+2214；
		// 2500≥2501；
		// 2600=2601+2602；
		// 2700≥2701；
		// 1000=1100+1200+1600+1700；
		// 2000=2100+2200+2300+2400+2500+2600+2700；
		// 3000=3001+3002。
		if ("1100".equals(e01.getE0101())) {
			// ∣1100∣＜∣1101∣
			BigDecimal b1101 = mapE01.get("1101") == null ? null
					: ((BigDecimal) mapE01.get("1101"));
			if (b1101 != null
					&& e01.getE0103().abs().compareTo(b1101.abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "|1100|<|1101|REMARK";
			}
			// 1000=1100+1200+1600+1700
			BigDecimal b1000 = mapE01.get("1000") == null ? null
					: ((BigDecimal) mapE01.get("1000"));
			if (b1000 == null) {
				return "1000=1100+1200+1600+1700";
			}
		} else if ("1101".equals(e01.getE0101())) {
			// ∣1100∣＜∣1101∣
			BigDecimal b1100 = mapE01.get("1100") == null ? null
					: ((BigDecimal) mapE01.get("1100"));
			if (b1100 != null
					&& b1100.abs().compareTo(e01.getE0103().abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "|1100|<|1101|REMARK";
			}
		} else if ("1200".equals(e01.getE0101())) {
			// 1200=1201+1202+1203+1204+1205+1206+1207+1208+1209+1210+1211+1212+1214
			BigDecimal b1201 = (BigDecimal) (mapE01.get("1201") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1201"));
			BigDecimal b1202 = (BigDecimal) (mapE01.get("1202") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1202"));
			BigDecimal b1203 = (BigDecimal) (mapE01.get("1203") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1203"));
			BigDecimal b1204 = (BigDecimal) (mapE01.get("1204") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1204"));
			BigDecimal b1205 = (BigDecimal) (mapE01.get("1205") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1205"));
			BigDecimal b1206 = (BigDecimal) (mapE01.get("1206") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1206"));
			BigDecimal b1207 = (BigDecimal) (mapE01.get("1207") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1207"));
			BigDecimal b1208 = (BigDecimal) (mapE01.get("1208") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1208"));
			BigDecimal b1209 = (BigDecimal) (mapE01.get("1209") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1209"));
			BigDecimal b1210 = (BigDecimal) (mapE01.get("1210") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1210"));
			BigDecimal b1211 = (BigDecimal) (mapE01.get("1211") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1211"));
			BigDecimal b1212 = (BigDecimal) (mapE01.get("1212") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1212"));
			BigDecimal b1214 = (BigDecimal) (mapE01.get("1214") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1214"));
			BigDecimal bSum = b1201.add(b1202).add(b1203).add(b1204).add(b1205)
					.add(b1206).add(b1207).add(b1208).add(b1209).add(b1210)
					.add(b1211).add(b1212).add(b1214);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "1200=1201+1202+1203+1204+1205+1206+1207+1208+1209+1210+1211+1212+1214";
			}
			// 1000=1100+1200+1600+1700
			BigDecimal b1000 = mapE01.get("1000") == null ? null
					: ((BigDecimal) mapE01.get("1000"));
			if (b1000 == null) {
				return "1000=1100+1200+1600+1700";
			}
		} else if (!"1200".equals(e01.getE0101())
				&& e01.getE0101().startsWith("12")) {
			// 1200=1201+1202+1203+1204+1205+1206+1207+1208+1209+1210+1211+1212+1214
			BigDecimal b1200 = mapE01.get("1200") == null ? null
					: ((BigDecimal) mapE01.get("1200"));
			if (b1200 == null) {
				return "1200=1201+1202+1203+1204+1205+1206+1207+1208+1209+1210+1211+1212+1214";
			}
		} else if ("1600".equals(e01.getE0101())) {
			// 1600=1601+1602
			BigDecimal b1601 = (BigDecimal) (mapE01.get("1601") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1601"));
			BigDecimal b1602 = (BigDecimal) (mapE01.get("1602") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1602"));
			BigDecimal bSum = b1601.add(b1602);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "1600=1601+1602";
			}
			// 1000=1100+1200+1600+1700
			BigDecimal b1000 = mapE01.get("1000") == null ? null
					: ((BigDecimal) mapE01.get("1000"));
			if (b1000 == null) {
				return "1000=1100+1200+1600+1700";
			}
		} else if (!"1600".equals(e01.getE0101())
				&& e01.getE0101().startsWith("16")) {
			// 1600=1601+1602
			BigDecimal b1600 = mapE01.get("1600") == null ? null
					: ((BigDecimal) mapE01.get("1600"));
			if (b1600 == null) {
				return "1600=1601+1602";
			}
		} else if ("1700".equals(e01.getE0101())) {
			// ∣1700∣＜∣1701∣
			BigDecimal b1701 = mapE01.get("1701") == null ? null
					: ((BigDecimal) mapE01.get("1701"));
			if (b1701 != null
					&& b1701.abs().compareTo(e01.getE0103().abs()) > 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣1700∣＜∣1701∣REMARK";
			}
			// 1000=1100+1200+1600+1700
			BigDecimal b1000 = mapE01.get("1000") == null ? null
					: ((BigDecimal) mapE01.get("1000"));
			if (b1000 == null) {
				return "1000=1100+1200+1600+1700";
			}
		} else if ("1701".equals(e01.getE0101())) {
			// ∣1700∣＜∣1701∣
			BigDecimal b1700 = mapE01.get("1700") == null ? null
					: ((BigDecimal) mapE01.get("1700"));
			if (b1700 != null
					&& b1700.abs().compareTo(e01.getE0103().abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣1700∣＜∣1701∣REMARK";
			}
		} else if ("2000".equals(e01.getE0101())) {
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2100 = (BigDecimal) (mapE01.get("2100") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2100"));
			BigDecimal b2200 = (BigDecimal) (mapE01.get("2200") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2200"));
			BigDecimal b2300 = (BigDecimal) (mapE01.get("2300") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2300"));
			BigDecimal b2400 = (BigDecimal) (mapE01.get("2400") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2400"));
			BigDecimal b2500 = (BigDecimal) (mapE01.get("2500") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2500"));
			BigDecimal b2600 = (BigDecimal) (mapE01.get("2600") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2600"));
			BigDecimal b2700 = (BigDecimal) (mapE01.get("2700") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2700"));
			BigDecimal bSum = b2100.add(b2200).add(b2300).add(b2400).add(b2500)
					.add(b2600).add(b2700);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if ("2100".equals(e01.getE0101())) {
			// ∣2100∣＜∣2101∣
			BigDecimal b2101 = mapE01.get("2101") == null ? null
					: ((BigDecimal) mapE01.get("2101"));
			if (b2101 != null
					&& e01.getE0103().abs().compareTo(b2101.abs()) > 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2100∣＜∣2101∣REMARK";
			}
		} else if ("2101".equals(e01.getE0101())) {
			// ∣2100∣＜∣2101∣
			BigDecimal b2100 = mapE01.get("2100") == null ? null
					: ((BigDecimal) mapE01.get("2100"));
			if (b2100 != null
					&& b2100.abs().compareTo(e01.getE0103().abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2100∣＜∣2101∣REMARK";
			}
		} else if ("2200".equals(e01.getE0101())) {
			// 2200=2201+2202+2203+2204+2205+2206+2207+2208+2209+2210+2211+2212+2213+2214
			BigDecimal b2201 = (BigDecimal) (mapE01.get("2201") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2201"));
			BigDecimal b2202 = (BigDecimal) (mapE01.get("2202") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2202"));
			BigDecimal b2203 = (BigDecimal) (mapE01.get("2203") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2203"));
			BigDecimal b2204 = (BigDecimal) (mapE01.get("2204") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2204"));
			BigDecimal b2205 = (BigDecimal) (mapE01.get("2205") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2205"));
			BigDecimal b2206 = (BigDecimal) (mapE01.get("2206") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2206"));
			BigDecimal b2207 = (BigDecimal) (mapE01.get("2207") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2207"));
			BigDecimal b2208 = (BigDecimal) (mapE01.get("2208") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2208"));
			BigDecimal b2209 = (BigDecimal) (mapE01.get("2209") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2209"));
			BigDecimal b2210 = (BigDecimal) (mapE01.get("2210") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2210"));
			BigDecimal b2211 = (BigDecimal) (mapE01.get("2211") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2211"));
			BigDecimal b2212 = (BigDecimal) (mapE01.get("2212") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2212"));
			BigDecimal b2213 = (BigDecimal) (mapE01.get("2213") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2213"));
			BigDecimal b2214 = (BigDecimal) (mapE01.get("2214") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2214"));
			BigDecimal bSum = b2201.add(b2202).add(b2203).add(b2204).add(b2205)
					.add(b2206).add(b2207).add(b2208).add(b2209).add(b2210)
					.add(b2211).add(b2212).add(b2213).add(b2214);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "2200=2201+2202+2203+2204+2205+2206+2207+2208+2209+2210+2211+2212+2213+2214";
			}
		} else if (!"2200".equals(e01.getE0101())
				&& e01.getE0101().startsWith("22")) {
			// 2200=2201+2202+2203+2204+2205+2206+2207+2208+2209+2210+2211+2212+2213+2214
			BigDecimal b2200 = mapE01.get("2200") == null ? null
					: ((BigDecimal) mapE01.get("2200"));
			if (b2200 == null) {
				return "2200=2201+2202+2203+2204+2205+2206+2207+2208+2209+2210+2211+2212+2213+2214";
			}
		} else if ("2500".equals(e01.getE0101())) {
			// ∣2500∣＜∣2501∣
			BigDecimal b2501 = mapE01.get("2501") == null ? null
					: ((BigDecimal) mapE01.get("2501"));
			if (b2501 != null
					&& e01.getE0103().abs().compareTo(b2501.abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2500∣＜∣2501∣REMARK";
			}
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2000 = mapE01.get("2000") == null ? null
					: ((BigDecimal) mapE01.get("2000"));
			if (b2000 == null) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if ("2501".equals(e01.getE0101())) {
			// ∣2500∣＜∣2501∣
			BigDecimal b2500 = mapE01.get("2500") == null ? null
					: ((BigDecimal) mapE01.get("2500"));
			if (b2500 == null || b2500.compareTo(e01.getE0103()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2500∣＜∣2501∣REMARK";
			}
		} else if ("2600".equals(e01.getE0101())) {
			// 2600=2601+2602
			BigDecimal b2601 = (BigDecimal) (mapE01.get("2601") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2601"));
			BigDecimal b2602 = (BigDecimal) (mapE01.get("2602") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2602"));
			BigDecimal bSum = b2601.add(b2602);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "2600=2601+2602";
			}
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2000 = mapE01.get("2000") == null ? null
					: ((BigDecimal) mapE01.get("2000"));
			if (b2000 == null) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if (!"2600".equals(e01.getE0101())
				&& e01.getE0101().startsWith("26")) {
			// 2600=2601+2602
			BigDecimal b2600 = mapE01.get("2600") == null ? null
					: ((BigDecimal) mapE01.get("2600"));
			if (b2600 == null) {
				return "2600=2601+2602";
			}
		} else if ("2700".equals(e01.getE0101())) {
			// ∣2700∣＜∣2701∣
			BigDecimal b2701 = mapE01.get("2701") == null ? null
					: ((BigDecimal) mapE01.get("2701"));
			if (b2701 != null
					&& e01.getE0103().abs().compareTo(b2701.abs()) > 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2700∣＜∣2701∣REMARK";
			}
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2000 = mapE01.get("2000") == null ? null
					: ((BigDecimal) mapE01.get("2000"));
			if (b2000 == null) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if ("2701".equals(e01.getE0101())) {
			// ∣2700∣＜∣2701∣
			BigDecimal b2700 = mapE01.get("2700") == null ? null
					: ((BigDecimal) mapE01.get("2700"));
			if (b2700 != null
					&& b2700.abs().compareTo(e01.getE0103().abs()) < 0
					&& StringUtil.isEmpty(e01.getRemark())) {
				return "∣2700∣＜∣2701∣REMARK";
			}
		} else if ("1000".equals(e01.getE0101())) {
			// 1000=1100+1200+1600+1700
			BigDecimal b1100 = (BigDecimal) (mapE01.get("1100") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1100"));
			BigDecimal b1200 = (BigDecimal) (mapE01.get("1200") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1200"));
			BigDecimal b1600 = (BigDecimal) (mapE01.get("1600") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1600"));
			BigDecimal b1700 = (BigDecimal) (mapE01.get("1700") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("1700"));
			BigDecimal bSum = b1100.add(b1200).add(b1600).add(b1700);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "1000=1100+1200+1600+1700";
			}
		} else if ("1300".equals(e01.getE0101())
				|| "1400".equals(e01.getE0101())) {
			// 1000=1100+1200+1600+1700
			BigDecimal b1000 = mapE01.get("1000") == null ? null
					: ((BigDecimal) mapE01.get("1000"));
			if (b1000 == null) {
				return "1000=1100+1200+1600+1700";
			}
		} else if ("2000".equals(e01.getE0101())) {
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2100 = (BigDecimal) (mapE01.get("2100") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2100"));
			BigDecimal b2200 = (BigDecimal) (mapE01.get("2200") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2200"));
			BigDecimal b2300 = (BigDecimal) (mapE01.get("2300") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2300"));
			BigDecimal b2400 = (BigDecimal) (mapE01.get("2400") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2400"));
			BigDecimal b2500 = (BigDecimal) (mapE01.get("2500") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2500"));
			BigDecimal b2600 = (BigDecimal) (mapE01.get("2600") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2600"));
			BigDecimal b2700 = (BigDecimal) (mapE01.get("2700") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("2700"));
			BigDecimal bSum = b2100.add(b2200).add(b2300).add(b2400).add(b2500)
					.add(b2600).add(b2700);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if ("2300".equals(e01.getE0101())
				|| "2400".equals(e01.getE0101())) {
			// 2000=2100+2200+2300+2400+2500+2600+2700
			BigDecimal b2000 = mapE01.get("2000") == null ? null
					: ((BigDecimal) mapE01.get("2000"));
			if (b2000 == null) {
				return "2000=2100+2200+2300+2400+2500+2600+2700";
			}
		} else if ("3000".equals(e01.getE0101())) {
			// 3000=3001+3002
			BigDecimal b3001 = (BigDecimal) (mapE01.get("3001") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("3001"));
			BigDecimal b3002 = (BigDecimal) (mapE01.get("3002") == null ? new BigDecimal(
					"0.0")
					: mapE01.get("3002"));
			BigDecimal bSum = b3001.add(b3002);
			if (bSum.compareTo(e01.getE0103()) != 0) {
				return "3000=3001+3002";
			}
		} else if ("3001".equals(e01.getE0101())
				|| "3002".equals(e01.getE0101())) {
			// 3000=3001+3002
			BigDecimal b3000 = mapE01.get("3000") == null ? null
					: ((BigDecimal) mapE01.get("3000"));
			if (b3000 == null) {
				return "3000=3001+3002";
			}
		}
		return null;
	}

	protected String checkX01(Fal_X01Entity x01) {
		StringBuffer sbCondition = new StringBuffer();
		sbCondition.append(" buocMonth = '").append(x01.getBuocmonth()).append(
				"' and instCode = '").append(x01.getInstcode()).append(
				"' and businessId <> '").append(x01.getBusinessid()).append(
				"' and dataStatus > ").append(DataUtil.DELETE_STATUS_NUM)
				.append(" and ((actionType = '").append(ACTIONTYPE_D).append(
						"' and dataStatus <> ").append(DataUtil.YBS_STATUS_NUM)
				.append(") or actionType <> '").append(ACTIONTYPE_D).append(
						"') ");
		SearchModel searchModel = new SearchModel();
		searchModel.setTableId("T_FAL_X01");
		searchModel.setSearchCondition(sbCondition.toString());
		if (service == null) {
			service = (SearchService) SpringContextUtil
					.getBean("searchService");
		}
		List x01List = service.search(searchModel);
		Map mapX01 = new HashMap();
		if (CollectionUtil.isNotEmpty(x01List)) {
			for (Iterator i = x01List.iterator(); i.hasNext();) {
				Fal_X01Entity x = (Fal_X01Entity) i.next();
				if (x.getX0102() != null) {
					mapX01.put(x.getX0101(), x.getX0102());
				} else {
					mapX01.put(x.getX0101(), new BigDecimal(0.0));
				}
			}
		}
		// 项目的金额≥0,且必须满足
		// 1000=1100+1200+1300
		// 1000≥1001
		// 1100≥1101
		// 1110≥1111
		// 1200≥1201
		// 1300≥1301
		// 2000=2100+2200
		// 2000≥2001
		// 2100≥2101
		// 2200≥2201
		if ("1000".equals(x01.getX0101())) {
			// 1000=1100+1200+1300
			BigDecimal b1100 = (BigDecimal) (mapX01.get("1100") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("1100"));
			BigDecimal b1200 = (BigDecimal) (mapX01.get("1200") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("1200"));
			BigDecimal b1300 = (BigDecimal) (mapX01.get("1300") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("1300"));
			BigDecimal bSum = b1100.add(b1200).add(b1300);
			if (bSum.compareTo(x01.getX0102()) != 0) {
				return "1000=1100+1200+1300";
			}
			// 1000≥1001
			BigDecimal b1001 = (BigDecimal) (mapX01.get("1001") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("1001"));
			if (b1001.compareTo(x01.getX0102()) > 0) {
				return "1000≥1001";
			}
		} else if ("1100".equals(x01.getX0101())
				|| "1200".equals(x01.getX0101())
				|| "1300".equals(x01.getX0101())) {
			// 1000=1100+1200+1300
			BigDecimal b1000 = mapX01.get("1000") == null ? null
					: ((BigDecimal) mapX01.get("1000"));
			if (b1000 == null || b1000.compareTo(x01.getX0102()) < 0) {
				return "1000=1100+1200+1300";
			}
			if ("1100".equals(x01.getX0101())) {
				// 1100≥1101
				BigDecimal b1101 = (BigDecimal) (mapX01.get("1101") == null ? new BigDecimal(
						"0.0")
						: mapX01.get("1101"));
				if (b1101.compareTo(x01.getX0102()) > 0) {
					return "1100≥1101";
				}
			} else if ("1200".equals(x01.getX0101())) {
				// 1200≥1201
				BigDecimal b1201 = (BigDecimal) (mapX01.get("1201") == null ? new BigDecimal(
						"0.0")
						: mapX01.get("1201"));
				if (b1201.compareTo(x01.getX0102()) > 0) {
					return "1200≥1201";
				}
			} else if ("1300".equals(x01.getX0101())) {
				// 1300≥1301
				BigDecimal b1301 = (BigDecimal) (mapX01.get("1301") == null ? new BigDecimal(
						"0.0")
						: mapX01.get("1301"));
				if (b1301.compareTo(x01.getX0102()) > 0) {
					return "1300≥1301";
				}
			}
		} else if ("1001".equals(x01.getX0101())) {
			// 1000≥1001
			BigDecimal b1000 = mapX01.get("1000") == null ? null
					: ((BigDecimal) mapX01.get("1000"));
			if (b1000 == null || b1000.compareTo(x01.getX0102()) < 0) {
				return "1000≥1001";
			}
		} else if ("1101".equals(x01.getX0101())) {
			// 1100≥1101
			BigDecimal b1100 = mapX01.get("1100") == null ? null
					: ((BigDecimal) mapX01.get("1100"));
			if (b1100 == null || b1100.compareTo(x01.getX0102()) < 0) {
				return "1100≥1101";
			}
		} else if ("1110".equals(x01.getX0101())) {
			// 1110≥1111
			BigDecimal b1111 = (BigDecimal) (mapX01.get("1111") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("1111"));
			if (b1111.compareTo(x01.getX0102()) > 0) {
				return "1110≥1111";
			}
		} else if ("1111".equals(x01.getX0101())) {
			// 1110≥1111
			BigDecimal b1110 = mapX01.get("1110") == null ? null
					: ((BigDecimal) mapX01.get("1110"));
			if (b1110 == null || b1110.compareTo(x01.getX0102()) < 0) {
				return "1110≥1111";
			}
		} else if ("1201".equals(x01.getX0101())) {
			// 1200≥1201
			BigDecimal b1200 = mapX01.get("1200") == null ? null
					: ((BigDecimal) mapX01.get("1200"));
			if (b1200 == null || b1200.compareTo(x01.getX0102()) < 0) {
				return "1200≥1201";
			}
		} else if ("1301".equals(x01.getX0101())) {
			// 1300≥1301
			BigDecimal b1300 = mapX01.get("1300") == null ? null
					: ((BigDecimal) mapX01.get("1300"));
			if (b1300 == null || b1300.compareTo(x01.getX0102()) < 0) {
				return "1300≥1301";
			}
		} else if ("2000".equals(x01.getX0101())) {
			// 2000=2100+2200
			BigDecimal b2100 = (BigDecimal) (mapX01.get("2100") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("2100"));
			BigDecimal b2200 = (BigDecimal) (mapX01.get("2200") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("2200"));
			BigDecimal bSum = b2100.add(b2200);
			if (bSum.compareTo(x01.getX0102()) != 0) {
				return "2000=2100+2200";
			}
			// 2000≥2001
			BigDecimal b2001 = (BigDecimal) (mapX01.get("2001") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("2001"));
			if (b2001.compareTo(x01.getX0102()) > 0) {
				return "2000≥2001";
			}
		} else if ("2100".equals(x01.getX0101())
				|| "2200".equals(x01.getX0101())) {
			// 2000=2100+2200
			BigDecimal b2000 = (BigDecimal) (mapX01.get("2000") == null ? new BigDecimal(
					"0.0")
					: mapX01.get("2000"));
			if (b2000.compareTo(x01.getX0102()) < 0) {
				return "2000=2100+2200";
			}
			if ("2100".equals(x01.getX0101())) {
				// 2100≥2101
				BigDecimal b2101 = (BigDecimal) (mapX01.get("2101") == null ? new BigDecimal(
						"0.0")
						: mapX01.get("2101"));
				if (b2101.compareTo(x01.getX0102()) > 0) {
					return "2100≥2101";
				}
			} else if ("2200".equals(x01.getX0101())) {
				// 2200≥2201
				BigDecimal b2201 = (BigDecimal) (mapX01.get("2201") == null ? new BigDecimal(
						"0.0")
						: mapX01.get("2201"));
				if (b2201.compareTo(x01.getX0102()) > 0) {
					return "2200≥2201";
				}
			}
		} else if ("2001".equals(x01.getX0101())) {
			// 2000≥2001
			BigDecimal b2000 = mapX01.get("2000") == null ? null
					: ((BigDecimal) mapX01.get("2000"));
			if (b2000 == null || b2000.compareTo(x01.getX0102()) < 0) {
				return "2000≥2001";
			}
		} else if ("2101".equals(x01.getX0101())) {
			// 2100≥2101
			BigDecimal b2100 = mapX01.get("2100") == null ? null
					: ((BigDecimal) mapX01.get("2100"));
			if (b2100 == null || b2100.compareTo(x01.getX0102()) < 0) {
				return "2100≥2101";
			}
		} else if ("2201".equals(x01.getX0101())) {
			// 2200≥2201
			BigDecimal b2200 = mapX01.get("2200") == null ? null
					: ((BigDecimal) mapX01.get("2200"));
			if (b2200 == null || b2200.compareTo(x01.getX0102()) < 0) {
				return "2200≥2201";
			}
		}
		return null;
	}

	public void setDictionarys(List dictionarys) {
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList) {
		this.verifylList = verifylList;
	}

	public void setInterfaceVer(String interfaceVer) {
		this.interfaceVer = interfaceVer;
	}

	public SearchService getService() {
		return service;
	}

	public void setService(SearchService service) {
		this.service = service;
	}
}
