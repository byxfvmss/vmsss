package com.cjit.vms.trans.action.config;

import java.util.List;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.service.OrganizationService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.config.GoodsInfo;
import com.cjit.vms.trans.model.config.GoodsTrans;
import com.cjit.vms.trans.service.config.GoodsInfoService;
import com.cjit.vms.trans.service.config.TransTypeService;

public class GoodsInfoAction extends DataDealAction {
	private GoodsInfoService goodsInfoService;
	private TransTypeService transTypeService;
	private GoodsInfo goodsInfo;
	private boolean createFlag;
	private String[] checkedlineNo;

	private List leftSel;// 未选择过的
	private List rightSel;// 已经选择的
	private String[] selectedTransType;

	public String mainGoodsInfo() {
		return SUCCESS;
	}

	public String mainTreeInst() {
		return SUCCESS;
	}

	public String frameHeadGoodsInfo() {
		return SUCCESS;
	}

	public String editGoodsTransType() {

		leftSel = transTypeService.selectGoodsTransType(null, goodsInfo, true);
		rightSel = transTypeService
				.selectGoodsTransType(null, goodsInfo, false);
		List dbList = goodsInfoService.selectGoodsInfo(goodsInfo, null);
		if (null != dbList && dbList.size() > 0) {
			goodsInfo = (GoodsInfo) dbList.get(0);
		}

		return SUCCESS;
	}

	public String saveGoodsTransType() {
		GoodsTrans goodsTrans = new GoodsTrans();
		goodsTrans.setTaxNo(goodsInfo.getTaxNo());
		goodsTrans.setGoodsId(goodsInfo.getGoodsId());
		transTypeService.deleteVmsGoodsTrans(goodsTrans);

		if (null != selectedTransType) {
			for (int i = 0; i < selectedTransType.length; i++) {
				GoodsTrans goodsTransInsert = new GoodsTrans();
				goodsTransInsert.setTaxNo(goodsInfo.getTaxNo());
				goodsTransInsert.setGoodsId(goodsInfo.getGoodsId());
				goodsTransInsert.setTransTypeId(selectedTransType[i]);
				transTypeService.insertVmsGoodsTrans(goodsTransInsert);
			}
		}

		return SUCCESS;
	}

	/***
	 * 查询商品（注意提供纳税人识别号）
	 * 
	 * @return
	 */
	public String selectGoodsInfoList() {
		if (null != goodsInfo && StringUtil.isNotEmpty(goodsInfo.getTaxNo())) {
			goodsInfoService.selectGoodsInfo(goodsInfo, paginationList);
		}

		return SUCCESS;
	}

	/***
	 * 编辑新建预处理 编辑时查询详细信息
	 * 
	 * @return
	 */
	public String editGoodsInfo() {

		if (null != goodsInfo && StringUtil.isEmpty(goodsInfo.getGoodsId())) {
			createFlag = true;
		} else {
			createFlag = false;
			List dbList = goodsInfoService.selectGoodsInfo(goodsInfo, null);
			if (null != dbList && dbList.size() > 0) {
				goodsInfo = (GoodsInfo) dbList.get(0);
			}
		}
		return SUCCESS;
	}

	/***
	 * 保存修改信息 新建insert 修改update
	 * 
	 * @return
	 */
	public String saveGoodsInfo() {
		if (createFlag) {
			GoodsInfo dbObjPar = new GoodsInfo();
			dbObjPar.setGoodsId(goodsInfo.getGoodsId());
			dbObjPar.setTaxNo(goodsInfo.getTaxNo());
			List dbList = goodsInfoService.selectGoodsInfo(dbObjPar, null);
			if (null!=dbList&&dbList.size()>0) {
				setResultMessages("当前商品编号已存在！");
				return ERROR;
			}
			goodsInfoService.insertGoodsInfo(goodsInfo);
			
		} else {
			goodsInfoService.updateGoodsInfo(goodsInfo);
		}
		return SUCCESS;
	}

	/***
	 * 删除商品信息
	 * 
	 * @return
	 */
	public String removeGoodsInfo() {
		if (null != checkedlineNo && checkedlineNo.length > 0)
			for (int i = 0; i < checkedlineNo.length; i++) {
				GoodsInfo delInfo = new GoodsInfo();
				delInfo.setGoodsId(checkedlineNo[i]);
				delInfo.setTaxNo(goodsInfo.getTaxNo());
				goodsInfoService.deleteGoodsInfo(delInfo);
			}

		return SUCCESS;
	}

	public GoodsInfoService getGoodsInfoService() {
		return goodsInfoService;
	}

	public void setGoodsInfoService(GoodsInfoService goodsInfoService) {
		this.goodsInfoService = goodsInfoService;
	}

	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public boolean isCreateFlag() {
		return createFlag;
	}

	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public String[] getSelectedTransType() {
		return selectedTransType;
	}

	public void setSelectedTransType(String[] selectedTransType) {
		this.selectedTransType = selectedTransType;
	}

	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public List getLeftSel() {
		return leftSel;
	}

	public void setLeftSel(List leftSel) {
		this.leftSel = leftSel;
	}

	public List getRightSel() {
		return rightSel;
	}

	public void setRightSel(List rightSel) {
		this.rightSel = rightSel;
	}
}
