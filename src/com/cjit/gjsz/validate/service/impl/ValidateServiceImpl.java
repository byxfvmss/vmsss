package com.cjit.gjsz.validate.service.impl;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.validate.service.ValidateService;

public class ValidateServiceImpl extends GenericServiceImpl implements
		ValidateService {

	public void saveValidateList(List list) {
		//insertBatch("saveValidate", list);
		
	}

	/*
	 * 翻页用
	 */
	public List findValidate(Map map, PaginationList paginationList) {
		return this.find("findValidate", map, paginationList);
	}

	/*
	 * 校验页面使用
	 */
	public List findValidate(Map map) {
		return this.find("findValidate", map);
	}

	/*
	 * 删除已有校验结果数据
	 */
	public void deleteValidate(Map map) {
		this.delete("deleteValidate", map);
	}

}
