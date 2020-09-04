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
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.InvcountrycodeInfo;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @author huboA
 */
public class InvcountrycodeInfoDataVerify implements DataVerify{

	protected List dictionarys;
	protected List verifylList;

	// private VerifyConfig verifyConfig;
	public InvcountrycodeInfoDataVerify(){
	}

	public InvcountrycodeInfoDataVerify(List dictionarys, List verifylList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
	}

	/**
	 * 金融机构标识码 必输， 开户银行的金融机构标识码， 必须是文件名中金融机构所管辖的且在银行版金融机构基本情况表中存在的记录。
	 * @param invcountrycode 组织机构代码
	 * @return
	 */
	public boolean verifyInvcountrycode(String invcountrycode){
		return StringUtil.isEmpty(invcountrycode) ? false : true;
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
				boolean ok = true;
				InvcountrycodeInfo invcountrycodeInfo = (InvcountrycodeInfo) verifylList
						.get(i);
				// TODO: 编号005 银行版金融机构基本情况表 是什么?
				if(verifyInvcountrycode(invcountrycodeInfo.getInvcountrycode())){
					// 判断是否在字典表吗?
					boolean has = service.isHasSubKey("t_code_dictionary",
							"CODE_VALUE_STANDARD_LETTER", invcountrycodeInfo
									.getInvcountrycode());
					if(has){
						// 添加子表关联到主表的校验20100613 mxz
						// VerifyService vf = (VerifyService) SpringContextUtil
						// .getBean("verifyService");
						// List companys = vf.getDatas("'"
						// + invcountrycodeInfo.getBusinessid() + "'",
						// "getCompanyInfos");
						// if (companys.size()>0){
						// CompanyInfo ci=(CompanyInfo)companys.get(0);
						// if
						// (!this.verifyCountrycode(invcountrycodeInfo.getInvcountrycode(),
						// ci.getAttrcode(), COUNTRY)) {
						// ok=false;
						// }
						// }
						// 判断是否重复吗?
						Long size = service.subKeySize("t_invcountrycode_info",
								"INVCOUNTRYCODE", invcountrycodeInfo
										.getInvcountrycode(), "BUSINESSID",
								invcountrycodeInfo.getBusinessid());
						if(size != null && size.longValue() <= 1){
							size = service.subKeySize("t_invcountrycode_info",
									"BUSINESSID", invcountrycodeInfo
											.getBusinessid());
							// 判断是否超过5个
							if(size != null){
								if(size.longValue() > 5){
									ok = false;
								}
								/*else {
									ok = true;
								}*/
							}else{
								ok = false;
							}
						}else{
							ok = false;
						}
					}else{
						ok = false;
					}
				}else{
					ok = false;
				}
				if(ok == false){
					map
							.put(
									"INVCOUNTRYCODE",
									"[投资国别代码] 不能为空,必须是国别/地区代码表中存在的记录。同一笔单位下的 [投资国别代码] 不能超过5个。\n必须是国别/地区代码表中存在的记录；"
											+ "外方投资者国别不能选择中国。\n如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空；"
											+ "\n如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）；"
											+ "如果经济类型选择400，外方投资者国别必须为空。\n");
				}
			}
		}
		return verifyModel;
	}

	/**
	 * 常驻国家代码 字母代码，必须是国别/地区代码表中存在的记录； 外方投资者国别不能选择中国。
	 * 如果经济类型为200项下，则外方投资者国别中至少有港澳台之一，不能为空；
	 * 如果经济类型选择300项下，则外方投资者国别中不能为空，至少一项为外国（中国大陆及港澳台除外）； 如果经济类型选择400，外方投资者国别必须为空。
	 * 投资国别代码个数<=5，同一笔单位基本情况表下投资国别代码不能重复
	 * @param countrycode 常驻国家代码
	 * @param attrcode 行业属性代码
	 * @param type 字典类型
	 * @return
	 */
	public boolean verifyCountrycode(String countrycode, String attrcode,
			String type){
		int value = Integer.parseInt(attrcode);
		if(StringUtil.equals(countrycode, "CHN")){
			return false;
		}
		if(value >= 200 && value < 300){
			if(StringUtil.equals(countrycode, "MAC")
					|| StringUtil.equals(countrycode, "HKG")
					|| StringUtil.equals(countrycode, "TWN")){
				return true;
			}else{
				return false;
			}
		}
		if(value >= 300 && value < 400){
			if((countrycode != null && !StringUtil.equals(countrycode, ""))
					&& !(StringUtil.equals(countrycode, "MAC")
							|| StringUtil.equals(countrycode, "HKG") || StringUtil
							.equals(countrycode, "TWN"))){
				return true;
			}else{
				return false;
			}
		}
		if(value == 400){
			if(countrycode == null || StringUtil.equals(countrycode, "")){
				return true;
			}else{
				return false;
			}
		}
		return verifyAreacode(countrycode, type);
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

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
