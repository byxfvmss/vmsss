package com.cjit.gjsz.interfacemanager.service;

import com.cjit.common.service.GenericService;

public interface FmssDataService extends GenericService{

	/**
	 * <p> 方法名称: isHoliday|描述: 查询FMSS节假日表，判断输入日期是否为已定义的节假日 </p>
	 * @param date 待判断的日期 yyyy-MM-dd
	 * @return boolean 是节假日返回true，不是则返回false
	 */
	public boolean isHoliday(String date);
}
