/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

/**
 * @author yulubin
 */
public class EditDataInnerAction extends EditDataAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7171899007904298653L;

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）的编辑数据入口
	 * @return
	 */
	public String editDataInner(){
		if(!sessionInit(true)){
			return SUCCESS;
		}
		return editData(infoTypeCodeInner, tableIdInner);
	}

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）的查看数据入口
	 * @return
	 */
	public String viewDataInner(){
		if(!sessionInit(true)){
			return SUCCESS;
		}
		return viewData(infoTypeCodeInner, tableIdInner);
	}
}
