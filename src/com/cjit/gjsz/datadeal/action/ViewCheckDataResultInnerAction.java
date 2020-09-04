/**
 * 
 */
package com.cjit.gjsz.datadeal.action;


/**
 * @author yulubin
 */
public class ViewCheckDataResultInnerAction extends DataDealAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7453859702121006895L;

	// private String viewlog="";
	public String viewCheckDataResultInner(){
		this.request.setAttribute("subId", this.request.getAttribute("subId"));
		this.request.setAttribute("cris" + subId, this
				.getFieldFromSession("cris" + subId));
		return SUCCESS;
	}
}
