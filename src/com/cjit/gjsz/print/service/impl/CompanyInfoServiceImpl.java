/**
 * 
 */
package com.cjit.gjsz.print.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.model.CompanyInfo;
import com.cjit.gjsz.logic.model.CompanyOpenInfo;
import com.cjit.gjsz.logic.model.InvcountrycodeInfo;
import com.cjit.gjsz.print.service.BasePrintService;

/**
 * @author yulubin 单位基本信息表 涉及到的表操作： 基础信息 t_company_info 投资国别
 *         t_invcountrycode_info (取前五个) 开户信息 t_company_openinfo (取第一个)
 */
public class CompanyInfoServiceImpl extends GenericServiceImpl implements
		BasePrintService{

	private SearchService searchService;
	private final static String CHILD1 = "t_invcountrycode_info";
	private final static String CHILD2 = "t_company_openinfo";

	public Map generator(String businessid, String tableId, String interfaceVer){
		CompanyInfo companyInfo = null;
		List children1 = null;
		List children2 = null;
		Map map = new HashMap();
		companyInfo = (CompanyInfo) searchService.getDataVerifyModel(tableId,
				businessid);
		if(companyInfo != null){
			children1 = searchService.getChildren(CHILD1, businessid);
			children2 = searchService.getChildren(CHILD2, businessid);
			setCompanyInfo(map, companyInfo);
			setInvcountrycodeInfo(map, children1);
			setCompanyOpenInfo(map, children2);
		}
		return map;
	}

	private void setCompanyInfo(Map map, CompanyInfo companyInfo){
		// 审核时间
		map.put("日期", companyInfo.getAuditdate() == null ? ""
				: DateUtils.toString(companyInfo.getAuditdate(),
						DateUtils.ORA_DATE_FORMAT));
		map.put("组织机构代码", companyInfo.getCustcode());
		map.put("组织机构名称", StringUtil.cleanString(companyInfo.getCustname()));
		// map.put("住所/营业场所",
		// StringUtil.cleanString(companyInfo.getAreacode()));
		map.put("住所/营业场所", StringUtil
				.cleanString(companyInfo.getAreacodename()));
		map.put("单位地址", StringUtil.cleanString(companyInfo.getCustaddr()));
		map.put("邮政编码", StringUtil.cleanString(companyInfo.getPostcode()));
		map
				.put("行业属性代码", StringUtil.cleanString(companyInfo
						.getIndustrycode()));
		map.put("经济类型代码", StringUtil.cleanString(companyInfo.getAttrcode()));
		String code = StringUtil.cleanString(companyInfo.getCountrycode());
		String countryName[] = this.searchService.getKey(code).split(" ");
		map.put("常驻国家名称", StringUtil.cleanString(countryName[0]));
		map.put("常驻国家代码", this.searchService.getBackupNum(code));
		map.put("是否特殊经济区内企业", StringUtil
				.cleanString(companyInfo.getIstaxfree()));
		map.put("特殊经济区内企业类型", StringUtil.cleanString(companyInfo
				.getTaxfreecode()));
		map.put("联系用eMail", StringUtil.cleanString(companyInfo.getEmail()));
		map.put("申报方式", StringUtil.cleanString(companyInfo.getRptmethod()));
		map.put("交易用eMail", StringUtil.cleanString(companyInfo.getEcexaddr()));
		map.put("备注", StringUtil.cleanString(companyInfo.getRemarks()));
	}

	private void setInvcountrycodeInfo(Map map, List children){
		if(CollectionUtil.isNotEmpty(children)){
			for(int i = 0; i < children.size(); i++){
				InvcountrycodeInfo invcountrycodeInfo = (InvcountrycodeInfo) children
						.get(i);
				String code = StringUtil.cleanString(invcountrycodeInfo
						.getInvcountrycode());
				String countryName[] = this.searchService.getKey(code).split(
						" ");
				map.put("投资者国别地区" + (i + 1), StringUtil
						.cleanString(countryName[0]));
				map.put("投资者国别代码" + (i + 1), this.searchService
						.getBackupNum(code));
			}
		}
	}

	private void setCompanyOpenInfo(Map map, List children){
		if(CollectionUtil.isNotEmpty(children)){
			String branchCode = "  ";
			String contact = "  ";
			String tel = "  ";
			String fax = "  ";
			for(int i = 0; i < children.size(); i++){
				CompanyOpenInfo companyOpenInfo = (CompanyOpenInfo) children
						.get(i);
				if(companyOpenInfo != null){
					if(children.size() > 1){
						branchCode += (i + 1) + "、"
								+ companyOpenInfo.getBranchcode() + "； ";
						contact += (i + 1) + "、" + companyOpenInfo.getContact()
								+ "； ";
						tel += (i + 1) + "、" + companyOpenInfo.getTel() + "； ";
						fax += (i + 1) + "、" + companyOpenInfo.getFax() + "； ";
					}else{
						branchCode += companyOpenInfo.getBranchcode() + "； ";
						contact += companyOpenInfo.getContact() + "； ";
						tel += companyOpenInfo.getTel() + "； ";
						fax += companyOpenInfo.getFax() + "； ";
					}
				}
			}
			contact = contact.replaceAll("null", "无");
			tel = tel.replaceAll("null", "无");
			fax = fax.replaceAll("null", "无");
			map
					.put("金融机构标识码", branchCode.substring(0,
							branchCode.length() - 2));
			map.put("联系人", contact.substring(0, contact.length() - 2));
			map.put("联系电话", tel.substring(0, tel.length() - 2));
			map.put("传真", fax.substring(0, fax.length() - 2));
		}
	}

	public void setSearchService(SearchService searchService){
		this.searchService = searchService;
	}
}
