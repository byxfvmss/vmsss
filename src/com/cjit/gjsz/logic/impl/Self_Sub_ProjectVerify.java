package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_Sub_PROJECT;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_SUB_PROJECT_INFO]项目信息表校验类
 */
public class Self_Sub_ProjectVerify extends SelfDataVerify implements
		DataVerify{

	public Self_Sub_ProjectVerify(){
	}

	public Self_Sub_ProjectVerify(List dictionarys, List verifylList){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
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
				Self_Sub_PROJECT sub = (Self_Sub_PROJECT) verifylList.get(i);
				if(StringUtil.isEmpty(sub.getProjectname())){
					map.put("PROJECTNAME", "[项目名称]不能为空 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}
}
