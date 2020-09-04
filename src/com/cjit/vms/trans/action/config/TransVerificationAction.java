package com.cjit.vms.trans.action.config;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import cjit.crms.util.DictionaryCodeType;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.config.ItemInfo;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.service.TransVerificationService;
import com.cjit.vms.trans.service.config.ItemInfoService;
import com.cjit.vms.trans.service.config.ItemRateService;
import com.cjit.vms.trans.service.config.TransTypeService;

public class TransVerificationAction extends DataDealAction {

	private ItemInfoService itemInfoService;
	private TransTypeService transTypeService;
	private ItemRateService itemRateService;
	/***
	 * 科目树
	 */
	private ItemInfo itemInfoPram;// 参数传递用
	private ItemInfo parentItemInfoPram;// 父结点
	private ItemInfo itemInfoForm;// 保存修改用
	private List taxRateSelList;// 税率下拉框
	private String taxNumber;// 用户纳税人识别号
	private boolean createFlag;

	public TransVerificationAction() {
		itemInfoPram = new ItemInfo();
		parentItemInfoPram = new ItemInfo();
		itemInfoForm = new ItemInfo();

	}

	/***
	 * 
	 * @return
	 */
	public void selectItemTree() {

		// itemInfoPram.setTaxNo(getTaxNumber());
		List dbList = itemInfoService.selectItemInfo(itemInfoPram, null);
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(dbList).toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String editItemInfo() {
		// String itemCode = getItemInfoPram().getItemCode();
		// 使用parentItemInfoPram传递参数时为追加
		if (null != itemInfoPram.getItemCode()) {
			List itemInfoList = itemInfoService.selectItemInfo(itemInfoPram,
					null);
			ItemInfo itemInfo;
			if (itemInfoList.size() > 0) {
				itemInfo = (ItemInfo) itemInfoList.get(0);
				this.setItemInfoForm(itemInfo);
			}
			createFlag = false;

		} else {
			// parentItemInfoPram.setTaxNo(this.getTaxNumber());
			itemInfoForm.setParentCode(parentItemInfoPram.getItemCode());
			itemInfoForm.setPath(parentItemInfoPram.getPath());
			createFlag = true;
		}
		itemInfoForm.setTaxNo(getTaxNumber());

		return SUCCESS;

	}

	public String saveItemInfo() {
		// String itemCode = itemInfoPram.getItemCode();
		if (createFlag) {

			ItemInfo iteminfo = new ItemInfo();
			iteminfo.setItemCode(itemInfoForm.getItemCode());
			// iteminfo.setTaxNo(itemInfoForm.getTaxNo());
			List dbist = itemInfoService.selectItemInfo(iteminfo, null);
			if (dbist.size() > 0) {
				setResultMessages("科目号已存在");
				return ERROR;
			}
			if (StringUtil.isEmpty(itemInfoForm.getPath())) {
				itemInfoForm.setPath("\\" + itemInfoForm.getItemCode() + "\\");
			} else {
				itemInfoForm.setPath(itemInfoForm.getPath()
						+ itemInfoForm.getItemCode() + "\\");
			}

			itemInfoService.insertItemInfo(itemInfoForm);
		} else {
			itemInfoService.updateItemInfo(itemInfoForm);
		}
		return SUCCESS;

	}

	public String removeItemInfo() {
		// itemInfoForm.setTaxNo(getTaxNumber());
		VerificationInfo verificationInfo = new VerificationInfo();
		verificationInfo.setItemCode(itemInfoForm.getItemCode());
		List dbList = itemRateService.selectItemRateBase(verificationInfo);

		if (null != dbList && dbList.size() > 0) {
			StringBuffer errorTaxNo = new StringBuffer();
			for (int i = 0; i < dbList.size(); i++) {
				VerificationInfo taxNoObj = (VerificationInfo) dbList.get(i);
				errorTaxNo.append("[");
				errorTaxNo.append(taxNoObj.getTaxNo());
				errorTaxNo.append("]");
				errorTaxNo.append("\\n");
				if (i > 10) {
					errorTaxNo.append("...");
					break;
				}
			}
			setResultMessages("当前科目存在机构税率信息，请先删除后重试\\n纳税人识别号：\\n"+errorTaxNo.toString());
			return SUCCESS;
		}
		transTypeService.removeItemCodeByItemCode(itemInfoForm.getItemCode());
		itemInfoService.deleteItemInfo(itemInfoForm);

		return SUCCESS;
	}

	/***
	 * 主体跳转
	 * 
	 * @return
	 */
	public String frameTransVerification() {
		return SUCCESS;

	}

	/***
	 * 头部跳转
	 * 
	 * @return
	 */
	public String frameHeadTransVerification() {
		return SUCCESS;

	}

	/***
	 * 左凭证类型树
	 * 
	 * @return
	 */
	public String treeTransVerification() {
			
		return SUCCESS;

	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public ItemInfoService getItemInfoService() {
		return itemInfoService;
	}

	public void setItemInfoService(ItemInfoService itemInfoService) {
		this.itemInfoService = itemInfoService;
	}

	public ItemInfo getItemInfoPram() {
		if (null == itemInfoPram) {

			itemInfoPram = new ItemInfo();
		}
		return itemInfoPram;
	}

	public void setItemInfoPram(ItemInfo itemInfoPram) {
		this.itemInfoPram = itemInfoPram;
	}

	public ItemInfo getItemInfoForm() {
		if (null == itemInfoForm) {
			itemInfoForm = new ItemInfo();
		}
		return itemInfoForm;
	}

	public void setItemInfoForm(ItemInfo itemInfoForm) {
		this.itemInfoForm = itemInfoForm;
	}

	public List getTaxRateSelList() {
		if (null == taxRateSelList) {
			taxRateSelList = this.createSelectList(
					DictionaryCodeType.TAXRATE_TYPE, null, false);
		}
		return taxRateSelList;
	}

	public void setTaxRateSelList(List taxRateSelList) {
		this.taxRateSelList = taxRateSelList;
	}

	public ItemInfo getParentItemInfoPram() {
		return parentItemInfoPram;
	}

	public void setParentItemInfoPram(ItemInfo parentItemInfoPram) {
		this.parentItemInfoPram = parentItemInfoPram;
	}

	public String getTaxNumber() {
		if (null == taxNumber) {
			User user = (User) this.getFieldFromSession(Constants.USER);
			taxNumber = getUserTaxNumber(user.getOrgId());
		}
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public boolean isCreateFlag() {
		return createFlag;
	}

	public TransTypeService getTransTypeService() {
		return transTypeService;
	}

	public void setTransTypeService(TransTypeService transTypeService) {
		this.transTypeService = transTypeService;
	}

	public void setCreateFlag(boolean createFlag) {
		this.createFlag = createFlag;
	}

	public ItemRateService getItemRateService() {
		return itemRateService;
	}

	public void setItemRateService(ItemRateService itemRateService) {
		this.itemRateService = itemRateService;
	}

}
