/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

/**
 * @author yulubin
 */
public class UpdateDataInnerAction extends UpdateDataAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1411096680080318970L;

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）更新数据的入口
	 * @return
	 */
	public String updateDataInner(){
		return updateData(infoTypeCodeInner, tableIdInner);
	}
}
