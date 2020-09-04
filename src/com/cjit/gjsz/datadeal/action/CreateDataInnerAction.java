/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

/**
 * @author yulubin
 */
public class CreateDataInnerAction extends CreateDataAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6241302010495491593L;

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）的新增数据入口
	 * @return
	 */
	public String createDataInner(){
		if(!sessionInit(false)){
			return SUCCESS;
		}
		this.fileType = null;
		return createData(infoTypeCodeInner, tableIdInner);
	}
}
