/**
 * 
 */
package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.VerifyService;
import com.cjit.gjsz.logic.model.CompanyOpenInfo;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class CompanyOpenInfoDataVerify implements DataVerify{

	public static final String ACTIONTYPE_VERIFY = "A,C,D";
	public static final String PAYATTR_VERIFY = "F,T,O";
	public static final String ISTAXFREE_VERIFY = "Y,N";
	protected List dictionarys;
	protected List verifylList;

	// private VerifyConfig verifyConfig;
	public CompanyOpenInfoDataVerify(){
	}

	public CompanyOpenInfoDataVerify(List dictionarys, List verifylList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
	}

	/**
	 * 金融机构标识码 必输， 开户银行的金融机构标识码， 必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。
	 * @param branchcode 组织机构代码
	 * @return
	 */
	public boolean verifyBranchcode(String branchcode){
		return StringUtil.isEmpty(branchcode) ? false : true;
	}

	/**
	 * 选输， 该单位与开户银行联系的人员，如果此项输入了，则金融机构标识码必输 单位联系人
	 * @param contact 单位联系人
	 * @return
	 */
	public boolean verifyContact(String contact, String branchcode){
		if(StringUtil.isNotEmpty(contact)){
			return StringUtil.isEmpty(branchcode) ? false : true;
		}
		return false;
	}

	/**
	 * 选输，该单位与开户银行联系的人员电话，如果此项输入了，则金融机构标识码必输 单位联系电话
	 * @param tel 单位联系电话
	 * @return
	 */
	public boolean verifyTel(String tel, String branchcode){
		if(StringUtil.isNotEmpty(tel)){
			return StringUtil.isEmpty(branchcode) ? false : true;
		}
		return false;
	}

	/**
	 * 选输， 该单位与开户银行联系的人员，如果此项输入了，则金融机构标识码必输 单位传真
	 * @param fax 单位传真
	 * @return
	 */
	public boolean verifyFax(String fax, String branchcode){
		if(StringUtil.isNotEmpty(fax)){
			return StringUtil.isEmpty(branchcode) ? false : true;
		}
		return false;
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
		UserInterfaceConfigService service = (UserInterfaceConfigService) SpringContextUtil
				.getBean("userInterfaceConfigService");
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				CompanyOpenInfo companyOpenInfo = (CompanyOpenInfo) verifylList
						.get(i);
				// TODO: 编号005 银行版金融机构基本情况表 是什么?
				if(verifyBranchcode(companyOpenInfo.getBranchcode())){
					// 对t_company_openinfo需要判断customid
					boolean has = service.isHasSubKey("t_org", "customid",
							companyOpenInfo.getBranchcode());
					if(has)
					// 在机构表中有注册
					{
						List subs = service.subList("BRANCHCODE",
								"t_company_openinfo", "BRANCHCODE",
								companyOpenInfo.getBranchcode(), "BUSINESSID",
								companyOpenInfo.getBusinessid());
						if(CollectionUtil.isNotEmpty(subs) && subs.size() > 0){
							String branchcode = (String) subs.get(0);
							/*if (branchcode.trim().length() > 12) {
								map
										.put("BRANCHCODE",
												"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
							}*/
							// 主表保存时校验子表，即子表已保存数据时
							if(companyOpenInfo.getSubid() != null){
								if(!"".equalsIgnoreCase(companyOpenInfo
										.getSubid())){
									VerifyService verifyService = (VerifyService) SpringContextUtil
											.getBean("verifyService");
									List subs1 = verifyService.subList(
											branchcode, companyOpenInfo
													.getBusinessid());
									if(CollectionUtil.isNotEmpty(subs1)
											&& subs1.size() > 0){
										String branchcode1 = (String) subs1
												.get(0);
										if(!branchcode1
												.equalsIgnoreCase(companyOpenInfo
														.getSubid())){
											map
													.put("BRANCHCODE",
															"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
										}
									}
								}else{
									map
											.put("BRANCHCODE",
													"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
								}
							}else{
								map
										.put("BRANCHCODE",
												"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
							}
						}
					}
					// 无注册
					else{
						map
								.put("BRANCHCODE",
										"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
					}
				}// 为空的情况
				else{
					map
							.put("BRANCHCODE",
									"[金融机构标识码] 不允许重复并且不能为空,不能超过12位,必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。\n");
				}
				/*if (!verifyContact(companyOpenInfo.getContact(), companyOpenInfo.getBranchcode())) {
					map.put("CONTACT", "如果此项输入了 [金融机构标识码]，则 [单位联系人] 必输。\n");
				}

				if (!verifyTel(companyOpenInfo.getTel(), companyOpenInfo.getBranchcode())) {
					map.put("TEL", "如果此项输入了 [金融机构标识码]，则 [单位联系电话] 必输。\n");
				}

				if (!verifyFax(companyOpenInfo.getFax(), companyOpenInfo.getBranchcode())) {
					map.put("FAX", "如果此项输入了 [金融机构标识码]，则[单位传真] 必输。\n");
				}*/
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
