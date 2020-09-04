package com.cjit.vms.trans.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;

import com.cjit.common.util.StringUtil;
import com.cjit.vms.trans.model.GoodsConfig;
import com.cjit.vms.trans.model.TransVerification;
import com.cjit.vms.trans.service.GoodsConfigService;
import com.cjit.vms.trans.service.TransVerificationService;

public class GoodsConfigAction extends DataDealAction {

	GoodsConfigService goodsConfigService;
	TransVerificationService transVerificationService;
	GoodsConfig goodsConfig;// 修改form对像
	String goodsNo;
	String goodsName;
	String checkedlineNo[];
	List transVerificationList = new ArrayList();// 右则基本数据
	List transVerificationSelList = new ArrayList();// 左则基本数据

	List taxTypeList;// 纳税人类型

	String leftIds[];// left提交数据
	String rightIds[];// 左则提交数据

	boolean showOnly = false;

	public String listGoodsConfig() {
		Map map = new HashMap();
		map.put("goodsNo", goodsNo);
		map.put("goodsName", goodsName);
		goodsConfigService.selectlistGoodsConfig(map, paginationList);
		return SUCCESS;
	}

	public String editGoodsConfig() {
		if (null != goodsNo) {
			Map map = new HashMap();
			map.put("goodsNo", goodsNo);
			List goods = goodsConfigService.selectlistGoodsConfig(map, null);
			if (null != goods && goods.size() > 0) {
				Map parameters1 = new HashMap();
				parameters1.put("goodsNo", goodsNo);
				transVerificationList = transVerificationService
						.selectTransVerification(parameters1, null);
				Map parameters2 = new HashMap();
				parameters2.put("emptyGoodsNo", "true");
				transVerificationSelList = transVerificationService
						.selectTransVerification(parameters2, null);
				goodsConfig = (GoodsConfig) goods.get(0);
			}
		} else {
			Map parameters2 = new HashMap();
			parameters2.put("emptyGoodsNo", "true");
			transVerificationSelList = transVerificationService
					.selectTransVerification(parameters2, null);
		}
		return SUCCESS;
	}

	public String saveGoodsConfig() {
		Map map = new HashMap();
		//同种纳税主体商品名不可重复
		boolean isExsist = checkIsExsist(goodsConfig.getGoodsNo(),goodsConfig.getGoodsName(),
				goodsConfig.getTaxType());
		if (isExsist) {
			this.setResultMessages("当前纳税人类型已存在同名商品,请修改商品名");
			Map parameters2 = new HashMap();
			parameters2.put("emptyGoodsNo", "true");
			transVerificationSelList = transVerificationService
					.selectTransVerification(parameters2, null);
			
			return ERROR;
		}
		if (StringUtil.isNotEmpty(goodsConfig.getGoodsNo())) {
			
			// 删除现有关联
			Map parameters = new HashMap();
			parameters.put("goodsNoSel", goodsConfig.getGoodsNo());
			transVerificationService.updateTransVerificationGoodsNo(parameters);

			// 更新商品
			map.put("goodsNo", goodsConfig.getGoodsNo());
			map.put("goodsName", goodsConfig.getGoodsName());
			map.put("taxType", goodsConfig.getTaxType());
			goodsConfigService.updateGoodsConfig(map);

			// 右侧商品关联信息更新
			if (rightIds.length > 0) {
				Map rightMap = new HashMap();
				rightMap.put("ids", rightIds);
				rightMap.put("goodsNo", goodsConfig.getGoodsNo());
				rightMap.put("goodsName", goodsConfig.getGoodsName());
				rightMap.put("taxType", goodsConfig.getTaxType());
				transVerificationService
						.updateTransVerificationGoodsNo(rightMap);
			}

			/*
			 * //左侧清空商品信息 if (leftIds.length>0) { Map leftMap = new HashMap();
			 * leftMap.put("ids", leftIds); leftMap.put("goodsNo", "");
			 * leftMap.put("goodsName", ""); leftMap.put("taxType",
			 * goodsConfig.getTaxType());
			 * transVerificationService.updateTransVerificationGoodsNo(leftMap);
			 * }
			 */

		} else {
			// 取得id 并插入
			int goodsNo = goodsConfigService.selectlistGoodsConfigSeq();
			map.put("idAuto", goodsNo);
			map.put("goodsName", goodsConfig.getGoodsName());
			map.put("taxType", goodsConfig.getTaxType());
			goodsConfigService.insertGoodsConfig(map);
			// 右则交易选择的商品号更新
			if (rightIds.length > 0) {
				Map rightMap = new HashMap();
				rightMap.put("ids", rightIds);
				rightMap.put("goodsNo", goodsNo);
				rightMap.put("goodsName", goodsConfig.getGoodsName());
				rightMap.put("taxType", goodsConfig.getTaxType());
				transVerificationService
						.updateTransVerificationGoodsNo(rightMap);
			}

		}

		return SUCCESS;
	}

	public String delGoodsConfigs() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			// 更新交易类型关联
			Map parameters = new HashMap();
			parameters.put("goodsNos", checkedlineNo);
			transVerificationService.updateTransVerificationGoodsNo(parameters);
			// updateTransVerification(checkedlineNo);
			// 删除商品
			Map map = new HashMap();
			map.put("goodsNos", checkedlineNo);
			goodsConfigService.deleteGoodsConfig(map);

		}

		return SUCCESS;
	}

	private boolean checkIsExsist(String goodNo,String goodName, String taxType) {
		Map map = new HashMap();
		map.put("fullGoodsName", goodName);
		map.put("taxType", taxType);
		map.put("checkGoodsNo", goodNo);
		List goods = goodsConfigService.selectlistGoodsConfig(map, null);
		if (null != goods && goods.size() > 0) {
			return true;
		}
		return false;
	}

	/*
	 * private void updateTransVerification(String[] checkedlineNo){
	 * 
	 * for (int i = 0; i < checkedlineNo.length; i++) { //取得商品关联交易类型 Map
	 * parameters1 = new HashMap(); parameters1.put("goodsNo",
	 * checkedlineNo[i]); transVerificationList =
	 * transVerificationService.selectTransVerification(parameters1, null);
	 * String ids[] = new String[transVerificationList.size()]; for (int j = 0;
	 * j < transVerificationList.size(); j++) { TransVerification tv =
	 * (TransVerification) transVerificationList.get(j); ids[j] = tv.getId(); }
	 * //删除与商品的关联 Map delmap = new HashMap(); delmap.put("ids", ids);
	 * delmap.put("goodsNo", ""); delmap.put("goodsName", "");
	 * transVerificationService.updateTransVerificationGoodsNo(delmap); }
	 * 
	 * }
	 */

	public GoodsConfigService getGoodsConfigService() {
		return goodsConfigService;
	}

	public void setGoodsConfigService(GoodsConfigService goodsConfigService) {
		this.goodsConfigService = goodsConfigService;
	}

	public TransVerificationService getTransVerificationService() {
		return transVerificationService;
	}

	public void setTransVerificationService(
			TransVerificationService transVerificationService) {
		this.transVerificationService = transVerificationService;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public GoodsConfig getGoodsConfig() {
		return goodsConfig;
	}

	public void setGoodsConfig(GoodsConfig goodsConfig) {
		this.goodsConfig = goodsConfig;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public List getTransVerificationList() {
		return transVerificationList;
	}

	public void setTransVerificationList(List transVerificationList) {
		this.transVerificationList = transVerificationList;
	}

	public List getTransVerificationSelList() {
		return transVerificationSelList;
	}

	public void setTransVerificationSelList(List transVerificationSelList) {
		this.transVerificationSelList = transVerificationSelList;
	}

	public String[] getLeftIds() {
		return leftIds;
	}

	public void setLeftIds(String[] leftIds) {
		this.leftIds = leftIds;
	}

	public String[] getRightIds() {
		return rightIds;
	}

	public void setRightIds(String[] rightIds) {
		this.rightIds = rightIds;
	}

	public boolean isShowOnly() {
		return showOnly;
	}

	public void setShowOnly(boolean showOnly) {
		this.showOnly = showOnly;
	}

	public List getTaxTypeList() {
		if (null == taxTypeList) {
			taxTypeList = this.createSelectList("TAX_TYPE", null, true);
		}
		return taxTypeList;
	}

	public void setTaxTypeList(List taxTypeList) {
		this.taxTypeList = taxTypeList;
	}

}
