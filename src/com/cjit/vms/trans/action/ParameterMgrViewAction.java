package com.cjit.vms.trans.action;

import java.util.ArrayList;
import java.util.List;

import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.service.ParameterMgrService;

public class ParameterMgrViewAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private String taxno; // 纳税人识别号
	private String goodsName;// 商品名称： vms_goods_info.goods_name
	private String goodsNameBak;// 商品名称： vms_goods_info.goods_name
	private String goodsNo;// 发票商品编号：vms_goods_info.goods_no

	private String updFlg; // 新增修改flag。 0：新增，1：修改
	// 交易类型列表
	private List checkBusinessList = new ArrayList();

	/* service 声明 */
	private ParameterMgrService parameterMgrService;

	/**
	 * 商品信息 查看
	 * 
	 * @return
	 * @author lyman
	 */
	public String goodsInfoView() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		try {
			List InstId = new ArrayList();
			this.getAuthInstList(InstId);
			this.setAuthInstList(InstId);
			// this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParameterMgrAction-goodsInfoAddOrUpdInit", e);
			return ERROR;
		}

		GoodsInfo info = new GoodsInfo();
		info.setGoodsNo(getGoodsNo());
		info.setTaxNo(getTaxno());
		List lst = parameterMgrService.getGoodsInfoPK(info);
		if (lst != null && lst.size() == 1) {
			setGoodsName(((GoodsInfo) lst.get(0)).getGoodsName());
			setGoodsNameBak(((GoodsInfo) lst.get(0)).getGoodsName());
			parameterMgrService.getListGoodsBusi(info, paginationList);
		}
		return SUCCESS;
	}

    public String getBusiList(){
			GoodsInfo goods = new GoodsInfo();
			goods.setGoodsNo(goodsNo);
			goods.setTaxNo(taxno);
			checkBusinessList = this.parameterMgrService.getListGoodsBusi(goods, paginationList);
			setGoodsName(getGoodsName());
			setTaxno(getTaxno());
			setUpdFlg(getUpdFlg());
			return SUCCESS;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNameBak() {
		return goodsNameBak;
	}

	public void setGoodsNameBak(String goodsNameBak) {
		this.goodsNameBak = goodsNameBak;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getUpdFlg() {
		return updFlg;
	}

	public void setUpdFlg(String updFlg) {
		this.updFlg = updFlg;
	}

	public List getCheckBusinessList() {
		return checkBusinessList;
	}

	public void setCheckBusinessList(List checkBusinessList) {
		this.checkBusinessList = checkBusinessList;
	}

	public ParameterMgrService getParameterMgrService() {
		return parameterMgrService;
	}

	public void setParameterMgrService(ParameterMgrService parameterMgrService) {
		this.parameterMgrService = parameterMgrService;
	}

}
