package com.cjit.vms.trans.action.config;

import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.config.TransTypeService;

public class TransTypeAction extends DataDealAction {
	
	//交易类型设置页面的查询条件对象
	VerificationInfo transTypeCondition = new VerificationInfo();
	
	//交易关联页面的查询条件对象
	VerificationInfo relCondition = new VerificationInfo();

	private TransTypeService transTypeService;
	private List rightList;
	private List leftList;
	private VerificationInfo transGoodsCondition = new VerificationInfo();
	private String transTypeIds;

	/***
	 * 交易类型
	 */
	TransTypeInfo transTypeInfoPram;//
	TransTypeInfo transTypeInfo;//修改form
	List transTypeInfoList;
	String checkedlineNo[];// 画面选择的ids
	boolean createFlag;
	
	public String selectTransTypeList() {
		transTypeService.selectTransType(transTypeInfoPram, paginationList);
		return SUCCESS;
	}
	
	/**
	 * 交易类型设置
	 * @return
	 */
	public String listTransType(){
		//transTypeCondition
		List transTypeInfo = transTypeService.listTransType(transTypeCondition, paginationList);
		return SUCCESS;
	}
	
	/**
	 * 交易类型关联商品
	 * @return
	 */
	public String relTrans2Goods(){
		String instId = this.request.getParameter("instId");
		String instName = this.request.getParameter("instName");
		if(null == instId||"".equals(instId)){
			instId = this.getCurrentUser().getOrgId();
			instName = instId + " - " + this.getCurrentUser().getOrgFullName();
		}
		String taxNo = this.organizationService.findTaxNoByInstCode(instId).getTaxperNumber();
		relCondition.setInstCode(instId);
		relCondition.setTaxNo(taxNo);
		relCondition.setInstName(instName);
		transTypeIds = this.request.getParameter("ids");
		String str[] = transTypeIds.split(",");
		relCondition.setTransTypeList(str);
		leftList = transTypeService.listLeftGoods(relCondition);
		rightList = transTypeService.listRightGoods(relCondition);
		return SUCCESS;
	}

	public String selectPickTransTypeList() {
		// 查询时屏蔽itemCode参数 然后重设回去带到画面作为保存时参数
		String itemCode = transTypeInfoPram.getItemCode();
		transTypeInfoPram.setItemCode("");
		transTypeService.selectTransType(transTypeInfoPram, true,
				paginationList);
		transTypeInfoPram.setItemCode(itemCode);
		return SUCCESS;
	}

	/***
	 * 科目详细画面 勾选交易保存按钮
	 * @return
	 */
	public String updateTansTypeItemCode() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			transTypeService.updateTransTypeItemCode(checkedlineNo,
					transTypeInfoPram.getItemCode());
		}
		return SUCCESS;
	}

	/***
	 * 科目详细画面 交易一览删除按钮
	 * @return
	 */
	public String removeTansTypeItemCode() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			transTypeService.updateTransTypeItemCode(checkedlineNo, "");
		}
		return SUCCESS;
	}

	/***
	 * 
	 * @return
	 */
	
	public String editTransType(){
		createFlag = true;
		if(null!=transTypeCondition){
			if (StringUtil.isNotEmpty(transTypeCondition.getTransTypeId())) {
				List dbList =  transTypeService.listTransType(transTypeCondition, null);
				if (null!=dbList&&dbList.size()>0) {
					transTypeCondition = (VerificationInfo) dbList.get(0);
					createFlag = false;
				}
				
			}
			
		}
		return SUCCESS;
	}
	
	public String saveTransType(){
		if (createFlag) {
			VerificationInfo info = new VerificationInfo();
			info.setTransTypeId(transTypeCondition.getTransTypeId());
			List dbList =  transTypeService.listTransType(info, null);
			if (null!=dbList&&dbList.size()>0) {
				setResultMessages("当前编号已存在");
				return ERROR;
			}
			transTypeService.insertTransType(transTypeCondition);
		}else{
			transTypeService.updateTransType(transTypeCondition);
		}
		return SUCCESS;
	}
	
	public String removeTransType(){
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			
			for (int i = 0; i < checkedlineNo.length; i++) {
				VerificationInfo ty = new VerificationInfo();
				ty.setTransTypeId(checkedlineNo[i]);
				transTypeService.removeTransType(ty);
			}
			
		}
		
		return SUCCESS;
	}
	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public TransTypeInfo getTransTypeInfoPram() {
		return transTypeInfoPram;
	}

	public void setTransTypeInfoPram(TransTypeInfo transTypeInfoPram) {
		this.transTypeInfoPram = transTypeInfoPram;
	}

	public List getTransTypeInfoList() {
		return transTypeInfoList;
	}

	public void setTransTypeInfoList(List transTypeInfoList) {
		this.transTypeInfoList = transTypeInfoList;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}


	public VerificationInfo getTransTypeCondition() {
		return transTypeCondition;
	}

	public void setTransTypeCondition(VerificationInfo transTypeCondition) {
		this.transTypeCondition = transTypeCondition;
	}

	public List getRightList() {
		return rightList;
	}

	public void setRightList(List rightList) {
		this.rightList = rightList;
	}

	public List getLeftList() {
		return leftList;
	}

	public void setLeftList(List leftList) {
		this.leftList = leftList;
	}

	public VerificationInfo getTransGoodsCondition() {
		return transGoodsCondition;
	}

	public void setTransGoodsCondition(VerificationInfo transGoodsCondition) {
		this.transGoodsCondition = transGoodsCondition;
	}

	public VerificationInfo getRelCondition() {
		return relCondition;
	}

	public void setRelCondition(VerificationInfo relCondition) {
		this.relCondition = relCondition;
	}

	public TransTypeInfo getTransTypeInfo() {
		return transTypeInfo;
	}

	public void setTransTypeInfo(TransTypeInfo transTypeInfo) {
		this.transTypeInfo = transTypeInfo;
	}

	public String getTransTypeIds() {
		return transTypeIds;
	}

	public void setTransTypeIds(String transTypeIds) {
		this.transTypeIds = transTypeIds;
	}

	public boolean isCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}
}
