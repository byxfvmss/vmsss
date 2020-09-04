package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.CollectionUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;

public abstract class AgencyDataVerify implements DataVerify {
	
	protected List dictionarys;
	protected List verifylList;
	protected String interfaceVer;

	public AgencyDataVerify(){
	}

	public AgencyDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.interfaceVer = interfaceVer;
	}
	
	//**********************common method*********************************
	protected Map checkPositiveNumber(Map data,String[] columns) {
		Map info=new HashMap();
		for(int i=0;i<columns.length;i++)
		{
			String value=(String)data.get(columns[i]);
			if(StringUtil.IsEmptyStr(value)) continue;
			
			if(Double.parseDouble(value)<0) info.put(columns[i], "应大于等于0");
		}
		return info;
	}
	protected double getDouble(Map data,String key)
	{
		String value=(String)data.get(key);
		if(StringUtil.IsEmptyStr(value)) return 0.0;
		return new Double(value).doubleValue();
	}
	protected double getDoubleSum(Map data,String[] keyGroup)
	{
		double result=0.0;
		for(int i=0;i<keyGroup.length;i++)
		{
			result+=getDouble(data,keyGroup[i]);
		}
		return result;
	}
	protected boolean isAllEmpty(Map data,String[] keyGroup)
	{
		for(int i=0;i<keyGroup.length;i++)
		{
			if(!StringUtil.IsEmptyStr((String)data.get(keyGroup[i])))
			{
				return false;
			}
		}
		return true;
	}
	protected boolean isAllNotEmpty(Map data,String[] keyGroup)
	{
		for(int i=0;i<keyGroup.length;i++)
		{
			if(StringUtil.IsEmptyStr((String)data.get(keyGroup[i])))
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * <p>方法名称: verifyDictionaryValue|描述: 字典项校验</p>
	 * @param codeType 字典项code_type
	 * @param key 数据值
	 * @return boolean
	 */
	protected boolean verifyDictionaryValue(String codeType, String key){
		return findKey(dictionarys, codeType, key);
	}
	/**
	 * 根据字典记录集查找某个字典项是否存在
	 * @param dictionarys
	 * @param dataKey
	 * @param key
	 * @return
	 */
	private boolean findKey(List dictionarys, String dataKey, String key){
		if(key == null){
			return true;
		}
		if(CollectionUtil.isNotEmpty(dictionarys)){
			if(!COUNTRY.equals(dataKey)){
				for(int i = 0; i < dictionarys.size(); i++){
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if(StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)){ // 如果找到币种
						if(StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardLetter())){ // 如果ValueBlank为空,默认不需要转换
							return true; // 将行内代码码值转成标准代码值
						}
					}
				}
			}else{
				for(int i = 0; i < dictionarys.size(); i++){
					Dictionary dictionary = (Dictionary) dictionarys.get(i);
					if(StringUtil.equalsIgnoreCase(dictionary.getType(),
							dataKey)){ // 如果找到币种
						if(StringUtil.equalsIgnoreCase(key, dictionary
								.getValueStandardNum())
								|| StringUtil.equalsIgnoreCase(key, dictionary
										.getValueStandardLetter())){ // 如果ValueBlank为空,默认不需要转换
							return true; // 将行内代码码值转成标准代码值
						}
					}
				}
			}
			return false;
		}
		throw new RuntimeException("字典表不能为空");
	}
	//**********************get and set***********************************
	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList){
		this.verifylList = verifylList;
	}

	public void setInterfaceVer(String interfaceVer){
		this.interfaceVer = interfaceVer;
	}
	
	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
