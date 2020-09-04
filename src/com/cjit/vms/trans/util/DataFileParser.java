package com.cjit.vms.trans.util;

import java.util.List;

/**
 * <p>数据文件解析器</p>
 * @author Albert Li
 * @date 2010-8-21 下午04:26:01
 * @version 1.0
 * <p>修改记录</p>
 * Albert Li    新建类    2010-8-21 下午04:26:01
 */
public interface DataFileParser {

	/**
	 * <p>是否有下一行</p>
	 * @author  Albert Li
	 * @date 2010-8-21 下午04:26:44
	 * @param 
	 */
	public boolean hasNextLine();
	/**
	 * <p>获取数据列表</p>
	 * @author  Albert Li
	 * @date 2010-8-21 下午04:27:43
	 * @param 
	 */
	public List next();
}
