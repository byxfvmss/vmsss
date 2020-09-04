/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

/**
 * @author yulubin
 */
public class SaveDataInnerAction extends SaveDataAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5936866222684494967L;

	/**
	 * 普通内嵌表（除了国家投资代码表以外的其它内嵌表）保存数据的入口
	 * @return
	 */
	public String saveDataInner(){
		return saveData(infoTypeCodeInner, tableIdInner);
	}
}
