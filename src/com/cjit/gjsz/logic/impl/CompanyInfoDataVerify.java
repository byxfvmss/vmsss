/**
 * 单位基本情况表 t_company_info
 */
package com.cjit.gjsz.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.CompanyInfo;
import com.cjit.gjsz.logic.model.CompanyOpenInfo;
import com.cjit.gjsz.logic.model.InvcountrycodeInfo;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class CompanyInfoDataVerify implements DataVerify{

	public static final String ACTIONTYPE_VERIFY = "A,C,D";
	public static final String PAYATTR_VERIFY = "F,T,O";
	public static final String ISTAXFREE_VERIFY = "Y,N";
	public final String childTableId1 = "t_company_openinfo";
	public final String childTableId2 = "t_invcountrycode_info";
	protected List dictionarys;
	protected List verifylList;

	// private VerifyConfig verifyConfig;
	public CompanyInfoDataVerify(){
	}

	public CompanyInfoDataVerify(List dictionarys, List verifylList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
	}

	public CompanyInfoDataVerify(List dictionarys, List verifylList,
			List companyList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.companyList = companyList;
	}

	private List companyList;

	public List getCompanyList(){
		return companyList;
	}

	public void setCompanyList(List companyList){
		this.companyList = companyList;
	}

	/**
	 * 组织机构代码 必输， 技监局代码，代码必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。
	 * @param custcode 组织机构代码
	 * @return
	 */
	/*
	 * public boolean verifyCustcode(String custcode) { //20100604 mxz for test
	 * if (true) return true; if (StringUtil.isEmpty(custcode) ||
	 * custcode.trim().length() != 9) { return false; } if
	 * (StringUtil.equals(custcode, "000000000")) { return false; } int[] arr = {
	 * 3, 7, 9, 10, 5, 8, 4, 2 }; String str = custcode.substring(0,
	 * custcode.length() - 1); String str2 =
	 * String.valueOf(custcode.charAt(custcode.length() - 1)); int sum = 0; for
	 * (int i = 0; i < str.length(); i++) { char ch = str.charAt(i); int b = 0;
	 * if (ch >= '0' && ch <= '9') { b = ch - 48; } else if (ch >= 'A' && ch <=
	 * 'Z') { b = ch - 55; } else { return false; } sum = sum + (b * arr[i]); }
	 * int val = 11 - (sum % 11); if (val == 11) { if (str2.equals("0")) {
	 * return true; } return false; } else if (val == 10) { if
	 * (str2.equals("X")) { return true; } return false; } else { char c =
	 * str2.charAt(0); if (c >= 'A' && c <= 'Z') { int d1 = c - 55; if (d1 ==
	 * val) { return true; } return false; } else { if (str2.equals(val + "")) {
	 * return true; } return false; } } // if (val < 10) { // if
	 * (str2.equals(val + "")) { // return true; // } // return false; // } //
	 * else { // if (str2.equals("X")) { // return true; // } // return false; // } }
	 */
	/**
	 * 组织机构名称 必输， 当常驻国为中国时必须输中文。
	 * @param custname 组织机构名称
	 * @param countrycode 常驻国家代码
	 * @return
	 */
	public boolean verifyCustname(String custname, String countrycode){
		if(StringUtil.isEmpty(custname)){
			return false;
		}
		if(StringUtil.equals(custname, "000000000")){
			return false;
		}
		if(StringUtil.equalsIgnoreCase("CHN", countrycode)){
			return StringUtil.isChiness(custname);
		}
		return true;
	}

	/**
	 * 住所/营业场所 必输， 必须是行政区划表中存在的记录，但不能选100000。
	 * @param areacode 住所/营业场所
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyAreacode(String areacode, String type){
		if(StringUtil.isEmpty(areacode)){
			return false;
		}
		if(StringUtil.equalsIgnoreCase(areacode, "100000")){
			return false;
		}
		return findKey(dictionarys, type, areacode);
	}

	/**
	 * 行业属性代码 必输， 必须是行业属性代码表中存在的最细分类的记录。
	 * @param industrycode 行业属性代码
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyIndustrycode(String industrycode, String type){
		return verifyAreacode(industrycode, type);
	}

	/**
	 * 行业属性代码 必输， 必须是经济类型代码表中存在的最细分类的记录。
	 * @param attrcode 行业属性代码
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyAttrcode(String attrcode, String type){
		String temp = "100、140、150、170、200、300";
		if(StringUtil.contains(temp, attrcode)){
			return false;
		}
		return verifyAreacode(attrcode, type);
	}

	/**
	 * 常驻国家代码 字母代码，必须是国别/地区代码表中存在的记录。 <br> 如果100、200、300项下的任何一项，则常驻国家地区应为中国CHN。
	 * <br> 如果经济类型选择400，则常驻国家地区为外国或者港澳台。
	 * @param countrycode 常驻国家代码
	 * @param attrcode 经济类型代码
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyCountrycode(String countrycode, String attrcode,
			String type){
		int value = Integer.parseInt(attrcode);
		if(value < 400){
			if(StringUtil.equals(countrycode, "CHN")){
				return true;
			}else{
				return false;
			}
		}
		if(value == 400){
			if(StringUtil.equals(countrycode, "CHN")){
				return false;
			}else{
				// return true;
				return findKey(dictionarys, type, countrycode);
			}
		}
		// return verifyAreacode(countrycode, type);
		return findKey(dictionarys, type, countrycode);
	}

	/**
	 * 是否特殊经济区内企业
	 * @param istaxfree 是否特殊经济区内企业
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyIstaxfree(String istaxfree, String type){
		if(StringUtil.contains(type, istaxfree)){
			return true;
		}
		return false;
	}

	/**
	 * 特殊经济区内企业类型 ISTAXFREE=Y时必输， 必须是特殊经济区类型代码表中存在的记录； ISTAXFREE=N时不输。
	 * @param taxfreecode 特殊经济区内企业类型
	 * @param istaxfree 是否特殊经济区内企业
	 * @return
	 */
	public boolean verifyTaxfreecode(String taxfreecode, String istaxfree){
		if(StringUtil.isNotEmpty(istaxfree)
				&& StringUtil.equalsIgnoreCase(istaxfree, "Y")){
			if(StringUtil.isNotEmpty(taxfreecode)){
				return true;
			}else{
				return false;
			}
		}
		if(StringUtil.equalsIgnoreCase(istaxfree, "N")
				&& StringUtil.isEmpty(taxfreecode)){
			return true;
		}
		return false;
	}

	// TODO: 编号004 已实现，特别复杂
	/**
	 * 字母代码，必须是国别/地区代码表中存在的记录； 外方投资者国别不能选择中国。
	 * 如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空；
	 * 如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）； 如果经济类型选择400，外方投资者国别必须为空。
	 * 投资国别代码个数<=5，同一笔单位基本情况表下投资国别代码不能重复 INVCOUNTRYCODE
	 * @param invcountrycode 外方投资者国别数组
	 * @param type 国别
	 * @param istaxfree 是否特殊经济区内企业
	 * @param attrcode 经济类型代码
	 * @param countrycode 常驻国家代码
	 * @param StringBuffer sb
	 */
	public boolean verifyInvcountrycode(String[] invcountrycode, String type,
			String istaxfree, String attrcode, String countrycode,
			StringBuffer sb){
		String str0 = "CHN";
		String str1 = "MAC,HKG,TWN";
		// DFHL：无论什么情况都不能选择中国
		if(invcountrycode != null){
			for(int i = 0; i < invcountrycode.length; i++){
				if(invcountrycode[i] != null){
					if(StringUtil.contains(str0, invcountrycode[i].trim())){
						sb.append("NOT CHINA");
						return false;
					}
				}else{
					sb.append("INVCOUNTRYCODE");
					return false;
				}
			}
		}
		if(invcountrycode != null && invcountrycode.length > 5){
			sb.append("MORE FIVE");
			return false;
		}
		// DFHL:修改校验关系，原校验关系实现好像存在问题
		// if (invcountrycode == null || invcountrycode.length == 0) {
		// return true;
		// }
		Integer code = Integer.valueOf(attrcode);
		// 如果经济类型为100项下 (不会出现)
		if(code.intValue() < 100){
			if(invcountrycode != null && invcountrycode.length > 0){
				// 内资企业不可有投资国别项
				sb.append("NOT INVCOUNTRY");
				return false;
			}
			return true;
		}
		// 如果经济类型为100项下，则外方投资者国别必需为空。
		if(code.intValue() >= 100 && code.intValue() < 200){
			if(invcountrycode != null && invcountrycode.length > 0){
				// 内资企业不可有投资国别项
				sb.append("NOT INVCOUNTRY");
				return false;
			}
			return true;
		}
		// 如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空；
		if(code.intValue() > 200 && code.intValue() < 300){
			if(invcountrycode == null || invcountrycode.length == 0){
				sb.append("INVCOUNTRYCODE");
				return false;
			}
			for(int i = 0; i < invcountrycode.length; i++){
				if(StringUtil.contains(str1, invcountrycode[i].trim())){
					return true;
				}
			}
			sb.append("200-300");
			return false;
		}
		// 如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）；
		if(code.intValue() > 300 && code.intValue() < 400){
			int flag = 0;
			if(invcountrycode == null || invcountrycode.length == 0){
				sb.append("INVCOUNTRYCODE");
				return false;
			}
			for(int i = 0; i < invcountrycode.length; i++){
				if(StringUtil.contains(str1, invcountrycode[i].trim())
						|| StringUtil.contains(str0, invcountrycode[i].trim())){
					flag++;
				}
			}
			if(flag == invcountrycode.length){
				sb.append("300-400");
				return false;
			}
			return true;
		}
		// 如果经济类型选择400，外方投资者国别必须为空。 投资国别代码个数<=5
		if(code.intValue() == 400){
			// /int flag = 0;
			if(invcountrycode == null || invcountrycode.length == 0){
			}else{
				sb.append("MORE 400");
				return false;
			}
		}
		return true;
	}

	/**
	 * 外汇局联系用eMail 选输， 用于与外汇局之间的日常办公联系用的eMail地址
	 * @param taxfreecode
	 * @param type
	 * @param istaxfree
	 * @return
	 */
	public boolean verifyEmail(String taxfreecode, String type, String istaxfree){
		return true;
	}

	/** ************************************************************************************* */
	/**
	 * 根据字典记录集查找某个字典项是否存在
	 * @param dictionarys
	 * @param dataKey
	 * @param key
	 * @return
	 */
	protected boolean findKey(List dictionarys, String dataKey, String key){
		if(CollectionUtil.isNotEmpty(dictionarys)){
			for(int i = 0; i < dictionarys.size(); i++){
				Dictionary dictionary = (Dictionary) dictionarys.get(i);
				if(StringUtil.equalsIgnoreCase(dictionary.getType(), dataKey)){ // 如果找到币种
					if(StringUtil.equalsIgnoreCase(key, dictionary
							.getValueStandardLetter())){ // 如果ValueBlank为空,默认不需要转换
						return true; // 将行内代码码值转成标准代码值
					}
				}
			}
			return false;
		}
		throw new RuntimeException("字典表不能为空");
	}

	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList){
		this.verifylList = verifylList;
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				CompanyInfo companyInfo = (CompanyInfo) verifylList.get(i);
				SearchService service = (SearchService) SpringContextUtil
						.getBean("searchService");
				List invsetChildren = service.getChildren(childTableId2,
						companyInfo.getBusinessid());
				// long size = service.getDataVerifyModelSize(childTableId2,
				// companyInfo
				// .getBusinessid());
				// boolean has = size > 0 ? true : false;
				List invcountrycodeInfos = new ArrayList();
				if(CollectionUtil.isNotEmpty(invsetChildren)){
					for(int j = 0; j < invsetChildren.size(); j++){
						InvcountrycodeInfo invcountrycodeInfo = (InvcountrycodeInfo) invsetChildren
								.get(j);
						invcountrycodeInfos.add(invcountrycodeInfo
								.getInvcountrycode());
					}
				}
				// /String[] finalString = (String[])
				// invcountrycodeInfos.toArray(new String[0]);
				if(this.companyList != null && this.companyList.size() > 0){
					map.put("CUSTCODE", "[组织机构代码] 已存在，请重新输入。\n");
				}
				if(!BaseDataVerify.verifyCustcode(companyInfo.getCustcode())){
					map.put("CUSTCODE",
							"[组织机构代码] 必须符合技监局的机构代码编制规则，但去掉特殊代码000000000.\n");
				}
				if(!verifyCustname(companyInfo.getCustname(), companyInfo
						.getCountrycode())){
					map.put("CUSTNAME", "[组织机构名称] 不能为空，当常驻国为中国时必须输中文。\n");
				}
				if(!verifyAreacode(companyInfo.getAreacode(), DISTRICTCO)){
					map.put("AREACODE",
							"[住所/营业场所] 不能为空，必须是行政区划表中存在的记录，但不能选100000。\n");
				}
				if(!verifyIndustrycode(companyInfo.getIndustrycode(), INDUSTRY)){
					map.put("INDUSTRYCODE",
							"[行业属性代码] 不能为空，必须是行业属性代码表中存在的最细分类的记录。\n");
				}
				if(!verifyAttrcode(companyInfo.getAttrcode(), ECONOMICTY)){
					map.put("ATTRCODE", "[经济类型代码]不能为空，必须是经济类型代码表中存在的最细分类的记录。"
							+ "100、140、150、170、200、300由于不是最细分类，因此为不可选项；\n");
				}
				if(!this.verifyCountrycode(companyInfo.getCountrycode(),
						companyInfo.getAttrcode(), COUNTRY)){
					map
							.put(
									"COUNTRYCODE",
									"[常驻国家代码]不能为空，必须是 [国别/地区代码] 表中存在的记录。"
											+ "如果100、200、300项下的任何一项，则常驻国家地区应为中国CHN。如果经济类型选择400，则常驻国家地区为外国或者港澳台。\n");
				}
				if(!verifyIstaxfree(companyInfo.getIstaxfree(),
						ISTAXFREE_VERIFY)){
					map.put("ISTAXFREE", "[是否特殊经济区内企业]不能为空。\n");
				}
				if(!verifyTaxfreecode(companyInfo.getTaxfreecode(), companyInfo
						.getIstaxfree())){
					map
							.put("TAXFREECODE",
									"[是否特殊经济区内企业] 等于 [是] 时必输，必须是特殊经济区类型代码表中存在的记录；[是否特殊经济区内企业] 等于 [否] 时不输。\n");
				}
				List list = new ArrayList();
				// 单位基本信息 的子类验证
				List children = service.getChildren(childTableId2, companyInfo
						.getBusinessid());
				String[] finalString = null;
				if(CollectionUtil.isNotEmpty(children)){
					finalString = new String[children.size()];
					InvcountrycodeInfoDataVerify invcountrycodeInfoDataVerify = null;
					for(int j = 0; j < children.size(); j++){
						InvcountrycodeInfo invcountrycodeInfo = (InvcountrycodeInfo) children
								.get(j);
						List tmp = new ArrayList();
						tmp.add(invcountrycodeInfo);
						invcountrycodeInfoDataVerify = new InvcountrycodeInfoDataVerify(
								dictionarys, tmp);
						VerifyModel vm = invcountrycodeInfoDataVerify.execute();
						if(vm.getFatcher() != null
								&& !vm.getFatcher().isEmpty()){
							vm.getFatcher().put(SUBID,
									invcountrycodeInfo.getSubid());
							list.add(vm.getFatcher());
						}
						finalString[j] = invcountrycodeInfo.getInvcountrycode();
					}
					if(CollectionUtil.isNotEmpty(list)){
						verifyModel.setChildren(list);
					}
				}
				// 投资国别 子表信息校验
				StringBuffer sb = new StringBuffer();
				if(!verifyInvcountrycode(finalString, COUNTRY, companyInfo
						.getIstaxfree(), companyInfo.getAttrcode(), companyInfo
						.getCountrycode(), sb)){
					String message = "";
					if("NOT CHINA".equals(sb.toString())){
						message = "不能选择中国。\n";
					}else if("MORE FIVE".equals(sb.toString())){
						message = "同一个单位下的 [投资国别代码] 不能超过5个。必须是国别/地区代码表中存在的记录。\n";
					}else if("NOT INVCOUNTRY".equals(sb.toString())){
						message = "如果经济类型为100项下，外方投资者国别必须为空。\n";
					}else if("200-300".equals(sb.toString())){
						message = "如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空。\n";
					}else if("300-400".equals(sb.toString())){
						message = "如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）。\n";
					}else if("MORE 400".equals(sb.toString())){
						message = "如果经济类型选择400，外方投资者国别必须为空。\n";
					}else{
						message = "投资国别不能选择中国；如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空；如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）；如果经济类型选择400，外方投资者国别必须为空。\n";
					}
					sb = new StringBuffer().append("INVCOUNTRYCODE");
					map.put(sb.toString(), message);
				}
				// 单位基本信息 的子类验证
				if(companyInfo.getBusinessid() != null){
					DataDealService dataGenService = (DataDealService) SpringContextUtil
							.getBean("dataDealService");
					Map map1 = new HashMap();
					map1.put("ids", "'" + companyInfo.getBusinessid() + "'"); //
					List ls = dataGenService.find("getCompanyInfos", map1);
					if(ls != null && ls.size() > 0){
						children = service.getChildren(childTableId1,
								companyInfo.getBusinessid());
						if(CollectionUtil.isNotEmpty(children)){
							CompanyOpenInfoDataVerify companyOpenInfoDataVerify = null;
							for(int j = 0; j < children.size(); j++){
								CompanyOpenInfo companyOpenInfo = (CompanyOpenInfo) children
										.get(j);
								List tmp = new ArrayList();
								tmp.add(companyOpenInfo);
								companyOpenInfoDataVerify = new CompanyOpenInfoDataVerify(
										dictionarys, tmp);
								VerifyModel vm = companyOpenInfoDataVerify
										.execute();
								if(vm.getFatcher() != null
										&& !vm.getFatcher().isEmpty()){
									vm.getFatcher().put(SUBID,
											companyOpenInfo.getSubid());
									list.add(vm.getFatcher());
								}
							}
							if(CollectionUtil.isNotEmpty(list)){
								verifyModel.setChildren(list);
							}
						}else{
							map.put("OPENINFO", "单位下的 [开户信息] 个数必须大于0；\n");
						}
					}
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}