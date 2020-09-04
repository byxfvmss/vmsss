package com.cjit.vms.home;

import javax.servlet.http.HttpServletRequest;

import com.cjit.gjsz.common.homenote.xml.HomeDataDO;


/**
* <p>版权所有:(C)2003-2010 </p>
* @作者: yuanshihong
* @日期: 2009-10-12 下午05:52:38
* @描述: [IBuildHomeData]综合报表首页数据构建接口
*/
public interface IBuildHomeData{
	
	/**
	* <p>方法名称: buildHomeData|描述: 构建首页显示数据</p>
	* @param request
	* @return
	*/
	HomeDataDO buildHomeData(HttpServletRequest request);
}
