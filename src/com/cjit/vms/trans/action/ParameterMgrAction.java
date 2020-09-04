package com.cjit.vms.trans.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import jxl.write.Number;
import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.GoodsInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.VmsTaxInfo;
import com.cjit.vms.trans.service.ParameterMgrService;
import com.cjit.vms.trans.util.DataFileParser;
import com.cjit.vms.trans.util.DataFileParserUtil;

/**
 * VMS参数管理Action类
 * 
 * @author lee
 */
public class ParameterMgrAction extends DataDealAction {

	private String taxId; // 税目编号
	private String taxno; // 纳税人识别号
	private String fapiaoType; // 税类
	private String taxRate; // 税率
	/* service 声明 */
	private ParameterMgrService parameterMgrService;
	private File attachmentTaxItem;
	private String attachmentTaxItemFileName;
	private String RESULT_MESSAGE;
	private String updFlg; // 新增修改flag。 0：新增，1：修改
	private String selectedIds;

	private String[] selectGoodsNos;
	private String[] selectTaxNos;
	private String[] selectGoodsNames;
	private String[] selectTransTypes;

	private String taxnoBak; // 纳税人识别号
	private String goodsName;// 商品名称： vms_goods_info.goods_name
	private String goodsNameBak;// 商品名称： vms_goods_info.goods_name
	private String goodsNo;// 发票商品编号：vms_goods_info.goods_no
	private String transType;// 交易类型：vms_goods_info_item.trans_type
	private String transTypeBak;// 交易类型：vms_goods_info_item.trans_type
	private String transName;// 交易名称：VMS_BUSINESS_TB.BUSINESS_CNAME transName
	// 交易类型列表
	private List businessList = new ArrayList();
	private List checkBusinessList = new ArrayList();
	private VmsTaxInfo info = new VmsTaxInfo();
	private GoodsInfo info1=new GoodsInfo();
	/**
	 * 税目管理 列表画面初期化/检索
	 * 
	 * @return
	 * @author lee
	 */
	public String listTaxItemInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		VmsTaxInfo info = new VmsTaxInfo();
		//用户USER_ID 仅用于传参
		User u = this.getCurrentUser();
		info.setUser_id(u.getId());
		// 纳税人识别号
		if(this.getTaxno()!=null&&!"".equals(this.getTaxno())){
			info.setTaxno("like '%"+this.getTaxno()+"%'");
		}
		List lstAuthInstId = new ArrayList();
		this.getAuthInstList(lstAuthInstId);
		info.setLstAuthInstId(lstAuthInstId);
		// 税类
		info.setFapiaoType(this.getFapiaoType());
		// 税目编号
		if(this.getTaxId()!=null&&!"".equals(this.getTaxId())){
			info.setTaxId("like '%"+this.getTaxId()+"%'");
		}

		parameterMgrService.getListTaxItemInfo(info, paginationList);

		return SUCCESS;
	}

	/**
	 * 商品信息 列表画面初期化/检索
	 * 
	 * @return
	 * @author lee
	 */
	public String listGoodsInfo() {
		setResultMessages(null);
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		GoodsInfo info = new GoodsInfo();
		
		if (getTaxno() != null && !"".equals(getTaxno())) {
			info.setTaxNo(" in ('" + getTaxno() + "')");
		} else {
			List taxList = getTaxperList();
			String arrId = "";
			for (int i = 0; i <= taxList.size() - 1; i++) {
				Organization user = (Organization) taxList.get(i);
				arrId = arrId + "'" + user.getTaxperNumber() + "',";
			}
			if (arrId != null && !"".equals(arrId)) {
				arrId = arrId.substring(0, arrId.length() - 1);
				info.setTaxNo(" in (" + arrId + ")");
			} else {
				info.setTaxNo(" in ('')");
			}
		}

		// 发票商品编号
		if (getGoodsNo() != null && !"".equals(getGoodsNo())) {
			info.setGoodsNo(" like '%" + this.getGoodsNo() + "%'");
		}

		// 商品名称
		if (getGoodsName() != null && !"".equals(getGoodsName())) {
			info.setGoodsName(" like '%" + this.getGoodsName() + "%'");
		}
		parameterMgrService.getListGoodsInfo(info, paginationList);
		return SUCCESS;
	}

	/**
	 * 税目管理 列表画面初期化/检索
	 * 
	 * @return
	 * @author lee
	 */
	public String taxItemInfoAddOrUpdInit() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		try {
			List instId = new ArrayList();
			this.getAuthInstList(instId);
//			this.setAuthInstList(instId);
			// this.setOperType("add");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ParameterMgrAction-taxItemInfoAddOrUpdInit", e);
			return ERROR;
		}

		if ("1".equals(this.getUpdFlg())) {
			VmsTaxInfo info = new VmsTaxInfo();
			// 纳税人识别号
			 if(getTaxno()!=null&&!"".equals(getTaxno())){
				 info.setTaxno("= '"+getTaxno()+"'"); 
			 }
			// 税目编号
			if(!"".equals(getTaxId())&&getTaxId()!=null){
				info.setTaxId(getTaxId());
			}
			List instId = new ArrayList();
			this.getAuthInstList(instId);
			info.setLstAuthInstId(instId);
			//用户ID
			info.setUser_id(this.getCurrentUser().getId());
			// 税目情报的取得
			List lst = parameterMgrService.getListTaxItemInfo(info, null);
			if (lst != null && lst.size() == 1) {
				VmsTaxInfo out = (VmsTaxInfo) lst.get(0);
				this.setTaxno(out.getTaxno());
				this.setTaxnoBak(out.getTaxno());
				this.setFapiaoType(out.getFapiaoType());
				this.setTaxId(out.getTaxId());
				this.setTaxRate(out.getTaxRate());
			}
		}
		return SUCCESS;
	}

	/**
	 * 商品信息 列表画面初期化/检索
	 * 
	 * @return
	 * @author lee
	 */
	public String getBusitoList(){
		//openWindows('goodsInfoView.action?updFlg=1&taxno=123456789012340&goodsNo=11&transType=')
		
			GoodsInfo goods = new GoodsInfo();
			goods.setGoodsNo(goodsNo);
			goods.setTaxNo(taxno);
			checkBusinessList = this.parameterMgrService.getListGoodstoBusi(goods.getTaxNo(), goods.getGoodsNo(),transName, transType, paginationList);
		setGoodsName(getGoodsName());
		setTaxno(getTaxno());
		setUpdFlg(getUpdFlg());
		return SUCCESS;
		
	}
	public String goodsInfoAddOrUpdInit() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}

		this.RESULT_MESSAGE = null;
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
//		businessList = this.businessService.findBusinessList(new Business());

		// 新增修改flag。 0：新增，1：修改
		if ("1".equals(this.getUpdFlg())) {
//			businessList = this.businessService.findBusinessList(new Business());
			
			GoodsInfo info = new GoodsInfo();
			if(!"".equals(getGoodsNo())&&getGoodsNo()!=null){
				info.setGoodsNo(getGoodsNo());				
			}
			if(!"".equals(getTaxno())&&getTaxno()!=null){
				info.setTaxNo(getTaxno());
			}
			checkBusinessList = this.parameterMgrService.getListGoodsBusi(info, paginationList);
//			for (int i = businessList.size()-1; i >= 0; i--) {
//				if(checkBusinessList.contains(businessList.get(i))){
//					businessList.remove(i);
//				}
//			}
			List lst = parameterMgrService.getGoodsInfoPK(info);
			if (lst != null && lst.size() == 1) {
				setGoodsName(((GoodsInfo) lst.get(0)).getGoodsName());
				setGoodsNameBak(((GoodsInfo) lst.get(0)).getGoodsName());
			}
			// try{
			// this.setGoodsName(new
			// String(getGoodsName().getBytes(CharacterEncoding.ISO_8859_1),
			// CharacterEncoding.UTF8));
			// this.setGoodsNameBak(new
			// String(getGoodsName().getBytes(CharacterEncoding.ISO_8859_1),
			// CharacterEncoding.UTF8));
			// } catch (Exception e) {
			// this.setGoodsName("");
			// this.setGoodsNameBak("");
			// }
			// GoodsInfo info = new GoodsInfo();
			// // 纳税人识别号
			// info.setTaxNo(this.getTaxno());
			// // 发票商品编号
			// info.setGoodsNo(this.getGoodsNo());
			// // 商品名称
			// info.setGoodsName(this.getGoodsName());
			// // 交易类型
			// info.setTransType(this.getTransType());
			// // 税目情报的取得
			// List lst= parameterMgrService.getGoodsInfoPK(info);
			// if (lst != null && lst.size() == 1){
			// GoodsInfo out = (GoodsInfo)lst.get(0);
			// this.setTaxno(out.getTaxNo());
			// this.setFapiaoType(out.getFapiaoType());
			// this.setTaxId(out.getTaxId());
			// this.setTaxRate(out.getTaxRate());
			// }
		}
		return SUCCESS;
	}

	/**
	 * 税目管理 保存/更新
	 * 
	 * @return
	 * @author lee
	 */
	public String addOrUpdTaxItemInfo() {
		List instId = new ArrayList();
		this.getAuthInstList(instId);
		info.setLstAuthInstId(instId);
		VmsTaxInfo info = new VmsTaxInfo();
		// 纳税人识别号
		info.setTaxno(this.getTaxno());
		// 税目编号
		info.setTaxId(this.getTaxId());
		// 税类
		info.setFapiaoType(this.getFapiaoType());
		// 税率
		info.setTaxRate(this.getTaxRate());
		//判断新增和修改时的数据是否重复
		VmsTaxInfo tf = new VmsTaxInfo();
		tf.setTaxno(this.getTaxno());
		tf.setFapiaoType(this.getFapiaoType());
		tf.setTaxRate(this.getTaxRate());
		
		List line = parameterMgrService.findTaxItemInfoRepeatList(tf);
		
		VmsTaxInfo tfs = new VmsTaxInfo();
		tfs.setTaxno(this.getTaxno());
		tfs.setTaxId(this.getTaxId());
		
		List lines = parameterMgrService.findTaxItemInfoRepeatList(tfs);
		
		// 0：新增，1：修改
		if ("0".equals(getUpdFlg())) {
			if(line.size()>0){
				this.setResultMessages ("该纳税人识别号以及税类下,相同的税类信息已经存在!");
				return ERROR;
				
			}else if(lines.size()>0){
				this.setResultMessages ("该纳税人识别号下,相同的税目编号信息!");
				return ERROR;
			}
			
			parameterMgrService.saveTaxItemInfo(info);
			this.RESULT_MESSAGE = "保存成功";
			System.out.println("新增.taxno=" + taxno + "/fapiaoType="
					+ fapiaoType + "/taxId=" + taxId + "/taxRate=" + taxRate);
		} else {
			if(line.size()>0){
				this.setResultMessages ("该纳税人识别号以及税类下,相同的税类信息已经存在!");
				return ERROR;
			}
			parameterMgrService.updTaxItemInfo(info);
			this.RESULT_MESSAGE = "修改成功";
		}

		return SUCCESS;
	}

	/**
	 * 商品信息 保存/更新
	 * 
	 * @return
	 * @author lee
	 */
	public String addOrUpdGoodsInfo() {
		GoodsInfo info = new GoodsInfo();
		// 纳税人识别号
		info.setTaxNo(this.getTaxno());
		// 发票商品编号
		info.setGoodsNo(this.getGoodsNo().trim());
		// 商品名称
		info.setGoodsName(this.getGoodsName().trim());
        
		String selectTransTypes = (String)request.getParameter("selectTransTypes");
		GoodsInfo inf = new GoodsInfo();
		inf.setTaxNo(this.getTaxno());
		List line = parameterMgrService.getGoodLines(inf);
		
		// 0：新增，1：修改
		if ("0".equals(getUpdFlg())) {
			if(line.size()>0&&!selectTransTypes.equals("")){
				this.RESULT_MESSAGE = "该纳税人识别号下,交易类型不可重复登录!";
				return ERROR;
			}
			parameterMgrService.saveGoodsInfo(info);
			if(selectTransTypes!=null&& selectTransTypes!=""){
				String[] transTypeIds=selectTransTypes.split(",");
				for (int i = 0; i < transTypeIds.length; i++) {
					// 交易类型
					parameterMgrService.saveGoodsItemInfo(info);
				}
			}
			this.RESULT_MESSAGE = "新增成功";
		} else {
			// 纳税人识别号
			info.setTaxNo(this.getTaxnoBak());
			// 商品名称
			info.setGoodsName(this.getGoodsNameBak().trim());
			// 交易类型
			parameterMgrService.delGoodsInfoItem(info);

			String[] selectTransTypesSRC = this.request.getParameterValues("selectTransTypesSRC");
			if(selectTransTypes!=null){
				String[] transTypeIds=selectTransTypes.split(",");
				for (int i = 0; i < transTypeIds.length; i++) {
					if(transTypeIds[i]!=null&&!"".equals(transTypeIds[i])){
						parameterMgrService.saveGoodsItemInfo(info);
					}
				}
			}
			if(selectTransTypesSRC!=null){
				for (int i = 0; i < selectTransTypesSRC.length; i++) {
					if(selectTransTypesSRC[i]!=null&&!"".equals(selectTransTypesSRC[i])){
						parameterMgrService.saveGoodsItemInfo(info);
					}
				}
			}
//			// 纳税人识别号
//			info.setTaxNo(this.getTaxno());
//			// 商品名称
//			info.setGoodsName(this.getGoodsName().trim());
//			// 交易类型
//			info.setTransType(this.getTransType());
//			parameterMgrService.saveGoodsInfo(info);
			
			this.RESULT_MESSAGE = "修改成功";
			
		}

		return SUCCESS;
	}

	// /**
	// * @Action
	// *
	// * 税目管理 税率存在性check
	// *
	// * @author lee
	// * @return
	// */
	// public void chkVmsTaxInfoTaxRate() throws IOException{
	// int cnt = 0;
	//
	// VmsTaxInfo info = new VmsTaxInfo();
	// // 0：新增，1：修改
	// if("1".equals(getUpdFlg())){
	// // 税目ID
	// info.setTaxId(" <> '" + this.getTaxId() + "'");
	// }
	// // 纳税人识别号
	// info.setTaxno(" = '" + this.getTaxno() + "'");
	// // 税类
	// info.setFapiaoType(this.getFapiaoType());
	// // 税率
	// // info.setTaxRate(this.getTaxRate());
	// // 税目情报的取得
	// List lstRate = parameterMgrService.getListTaxItemInfo(info, null);
	// if (lstRate != null && lstRate.size() > 0){
	// cnt = lstRate.size();
	// }
	// System.out.println("税目管理  税率存在性check.taxno=" + getTaxno() +
	// "/fapiaoType=" + getFapiaoType() + "/taxRate=" + getTaxRate() + "/cnt=" +
	// cnt);
	//
	// response.setHeader("Content-Type", "text/xml; charset=utf-8");
	// PrintWriter out = response.getWriter();
	// out.print(String.valueOf(cnt));
	// out.close();
	// }

	/**
	 * @Action
	 * 
	 *         税目管理 税目ID存在性check
	 * 
	 * @author lee
	 * @return
	 */
	public void chkVmsTaxInfoTaxId() throws IOException {
		int cnt = 0;
		VmsTaxInfo info = new VmsTaxInfo();
		List instId = new ArrayList();
		this.getAuthInstList(instId);
		info.setLstAuthInstId(instId);
		// 税目ID
		info.setTaxId(this.getTaxId());
		// 纳税人识别号
		
		if(this.getTaxno()!=null&&!"".equals(this.getTaxno())){
			info.setTaxno("like '%"+this.getTaxno()+"%'");
		}

		
//		info.setTaxno(this.getTaxno());
		// 税类
		info.setFapiaoType(this.getFapiaoType());
		//用户ＩＤ
		info.setUser_id(this.getCurrentUser().getId());
		// 税目情报的取得
		List lst = parameterMgrService.getListTaxItemInfo(info, null);
		if (lst != null && lst.size() > 0) {
			cnt = lst.size();
		}
		System.out.println("税目管理  税目ID存在性check.taxId=" + getTaxId() + "/cnt="
				+ cnt);
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(String.valueOf(cnt));
		out.close();
	}

	/**
	 * @Action
	 * 
	 *         商品信息 商品存在性check
	 * 
	 * @author lee
	 * @return
	 */
	public void chkVmsGoodsInfo() throws IOException {
		int cnt = 0;
		GoodsInfo info = new GoodsInfo();
		// 纳税人识别号
		info.setTaxNo(this.getTaxno());
		// 发票商品编号
		info.setGoodsNo(this.getGoodsNo());
//		// 商品名称
//		info.setGoodsName(this.getGoodsName().trim());

		// 新增修改flag。 0：新增，1：修改
		if ("0".equals(getUpdFlg())) {
			// 商品情报的取得
			List lst = parameterMgrService.getGoodsInfoPK(info);
			if (lst != null && lst.size() > 0) {
				cnt = lst.size();
			}
		} else if ("1".equals(getUpdFlg())) {
			if (!this.getTaxno().equals(this.getTaxnoBak())
					|| !this.getGoodsName().trim()
							.equals(this.getGoodsNameBak())) {
				// 商品情报的取得
				List lst = parameterMgrService.getGoodsInfoPK(info);
				if (lst != null && lst.size() > 0) {
					cnt = lst.size();
				}
			}
		}

		System.out.println("商品信息  商品存在性check.GoodsNo=" + getGoodsNo() + "/cnt="
				+ cnt);
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(String.valueOf(cnt));
		out.close();
	}

	/**
	 * @Action
	 * 
	 *         商品信息 商品明细存在性check
	 * 
	 * @author lee
	 * @return
	 */
	public void chkVmsGoodsItemInfo() throws IOException {
		int cnt = 0;
		GoodsInfo info = new GoodsInfo();
		// 纳税人识别号
		info.setTaxNo(this.getTaxno());
		// 发票商品编号
		info.setGoodsNo(this.getGoodsNo());

		// 新增修改flag。 0：新增，1：修改
		if ("0".equals(getUpdFlg())) {
			// 商品情报的取得
			List lst = parameterMgrService.getGoodsItemInfoPK(info);
			if (lst != null && lst.size() > 0) {
				cnt = lst.size();
			}
		} else if ("1".equals(getUpdFlg())) {
			if (!this.getTransType().equals(this.getTransTypeBak())) {
				// 商品情报的取得
				List lst = parameterMgrService.getGoodsItemInfoPK(info);
				if (lst != null && lst.size() > 0) {
					cnt = lst.size();
				}
			}
		}
		System.out.println("商品信息  商品明细表的交易类型存在性check.TransType="
				+ getTransType() + "/cnt=" + cnt);
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(String.valueOf(cnt));
		out.close();
	}

	/**
	 * 附加税税种维护 列表画面 删除
	 * 
	 * @return
	 * @author lee
	 */
	public String delTaxItemInfo() {
		if (this.selectedIds != null && !"".equals(this.selectedIds)) {
			String[] ids = this.selectedIds.split(",");
			for (int i = 0; i <= ids.length - 1; i++) {
				parameterMgrService.delTaxItemInfo(ids[i]);
			}
		}
		return SUCCESS;
	}

	/**
	 * 商品信息 列表画面 删除
	 * 
	 * @return
	 * @author lee
	 */
	public String delGoodsInfo() {
		if (this.selectGoodsNos != null && selectGoodsNos.length > 0) {
			for (int i = 0; i <= selectGoodsNos.length - 1; i++) {
				System.out.println("selectGoodsNos[" + i + "]="
						+ selectGoodsNos[i]);

				GoodsInfo info = new GoodsInfo();
				info.setGoodsNo(selectGoodsNos[i]);
				info.setTaxNo(selectTaxNos[i]);
				info.setGoodsName(selectGoodsNames[i]);
				// 删除
				parameterMgrService.delGoodsInfo(info);
			}
		}
		this.RESULT_MESSAGE="删除成功";
		setResultMessages("删除成功");
		return SUCCESS;
	}

	/**
	 * 税目管理 列表画面 导出
	 * 
	 * @return
	 * @author lee
	 */
	public void taxItemInfoToExcel() throws Exception {
		try {
			StringBuffer fileName = new StringBuffer("税目信息列表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	/**
	 * 商品信息 列表画面 导出
	 * 
	 * @return
	 * @author lee
	 */
	public void goodsInfoToExcel() throws Exception {
		try {
			StringBuffer fileName = new StringBuffer("商品信息列表");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcelGoods(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	/**
	 * <p>
	 * 方法名称: importTaxItemInfo|描述: 导入税目
	 * </p>
	 * 
	 * @return KEY
	 */
	public String importTaxItemInfo() {
		ServletContext sc = ServletActionContext.getServletContext();
		System.out.println("入力文件：" + attachmentTaxItem);
		if (attachmentTaxItem != null) {
			// 若文件已存在，删除原文件
			String dir = sc.getRealPath("/WEB-INF");
			File saveFile = new File(new File(dir), attachmentTaxItemFileName);
			if (saveFile.exists()) {
				saveFile.delete();
				saveFile = new File(new File(dir), attachmentTaxItemFileName);
			}

			attachmentTaxItem.renameTo(saveFile);

			DataFileParser dataFileParserUtil = DataFileParserUtil
					.createDataFileParser(saveFile);
			if (dataFileParserUtil == null) {
				setResultMessages("请选择[.xls]后缀的文件.");
				return ERROR;
			}

			List temp = new ArrayList();
			int i = 0;
			String taxno = ""; // 纳税人识别号
			String fapiaoType = ""; // 税类
			String taxId = ""; // 税目种类
			String taxRate = ""; // 税率
			while (dataFileParserUtil.hasNextLine()) {
				temp = dataFileParserUtil.next();
				taxno = null == temp.get(1) ? "" : temp.get(1).toString();
				fapiaoType = null == temp.get(2) ? "" : temp.get(2).toString();
				String tax_Id = null == temp.get(3) ? "" : temp.get(3).toString();
				taxId = tax_Id.trim();
				taxRate = null == temp.get(4) ? "" : temp.get(4).toString();
				if (!"".equals(taxno) && !"".equals(fapiaoType)
						&& !"".equals(taxId) && !"".equals(taxRate)) {

					VmsTaxInfo info = new VmsTaxInfo();
					// 税类
					if ("增值税专用发票".equals(fapiaoType)) {
						info.setFapiaoType("0");
					} else if ("增值税普通发票".equals(fapiaoType)) {
						info.setFapiaoType("1");
					}
					// 纳税人识别号
					info.setTaxno(taxno);
					// 税率
					info.setTaxRate(taxRate);
					// 税目情报的取得
					List lstRate = parameterMgrService.findTaxItemInfoRepeatList(info);

					info = new VmsTaxInfo();
					// 纳税人识别号
					info.setTaxno(taxno);
					// 税目编号
					info.setTaxId(taxId);
					// 税目情报的取得
					List lst = parameterMgrService.findTaxItemInfoRepeatList(info);

					// 税类
					if ("增值税专用发票".equals(fapiaoType)) {
						info.setFapiaoType("0");
					} else if ("增值税普通发票".equals(fapiaoType)) {
						info.setFapiaoType("1");
					}
					// 税目编号
					info.setTaxId(taxId);
					// 税率
					info.setTaxRate(taxRate);

					if (lst.size() == 0) {
						if (lstRate != null && lstRate.size() > 0) {
							// 如果同一税类存在相同税率是不允许新增
							System.out.println("第" + (i + 1)
									+ "条数据,同一税类存在相同税率.taxno=" + taxno
									+ "/fapiaoType=" + fapiaoType + "/taxId="
									+ taxId + "/taxRate=" + taxRate);
							continue;
						}

						parameterMgrService.saveTaxItemInfo(info);
						System.out.println("导入第" + (i + 1) + "条数据.taxno="
								+ taxno + "/fapiaoType=" + fapiaoType
								+ "/taxId=" + taxId + "/taxRate=" + taxRate);
					} else {
						if (lstRate != null && lstRate.size() > 1) {
							// 如果同一税类存在相同税率是不允许新增
							System.out.println("第" + (i + 1)
									+ "条数据,同一税类存在相同税率.taxno=" + taxno
									+ "/fapiaoType=" + fapiaoType + "/taxId="
									+ taxId + "/taxRate=" + taxRate);
							continue;
						}
						parameterMgrService.updTaxItemInfo(info);
						System.out.println("修改第" + (i + 1) + "条数据.taxno="
								+ taxno + "/fapiaoType=" + fapiaoType
								+ "/taxId=" + taxId + "/taxRate=" + taxRate);
					}
				} else {
					System.out.println("税目ID，纳税人识别号，发票类型，税率不能为空.");
				}
				i++;
			}
			setResultMessages("上传成功.");
			return SUCCESS;
		} else {
			setResultMessages("请选择上传文件.");
			return ERROR;
		}
	}

	/**
	 * <p>
	 * 方法名称: importGoodsInfo|描述: 导入商品信息
	 * </p>
	 * 
	 * @return KEY
	 */
	public String importGoodsInfo() {
		ServletContext sc = ServletActionContext.getServletContext();
		System.out.println("入力文件：" + attachmentTaxItem);
		if (attachmentTaxItem != null) {
			// 若文件已存在，删除原文件
			String dir = sc.getRealPath("/WEB-INF");
			File saveFile = new File(new File(dir), attachmentTaxItemFileName);
			if (saveFile.exists()) {
				saveFile.delete();
				saveFile = new File(new File(dir), attachmentTaxItemFileName);
			}

			attachmentTaxItem.renameTo(saveFile);

			DataFileParser dataFileParserUtil = DataFileParserUtil
					.createDataFileParser(saveFile);
			if (dataFileParserUtil == null) {
				setResultMessages("请选择[.xls]后缀的文件.");
				return ERROR;
			}

			List temp = new ArrayList();
			int i = 0;
			String goodsName = ""; // 商品名称
			String goodsNo = ""; // 发票商品编号
			String taxno = ""; // 纳税人识别号
			String transType = ""; // 交易类型
			while (dataFileParserUtil.hasNextLine()) {
				temp = dataFileParserUtil.next();
				goodsName = null == temp.get(1) ? "" : temp.get(1).toString();
				goodsNo = null == temp.get(2) ? "" : temp.get(2).toString();
				taxno = null == temp.get(3) ? "" : temp.get(3).toString();
				transType = null == temp.get(4) ? "" : temp.get(4).toString();
				if (!"".equals(goodsName) && !"".equals(goodsNo)
						&& !"".equals(taxno) && !"".equals(transType)) {

					GoodsInfo info = new GoodsInfo();
					info.setGoodsName(goodsName);
					info.setGoodsNo(goodsNo);
					info.setTaxNo(taxno);

					// 商品信息的取得
					List lstGoods = parameterMgrService.getGoodsInfoPK(info);
					if (lstGoods != null && lstGoods.size() > 0) {
						System.out.println("第" + (i + 1)
								+ "条数据,商品信息表已经存在.goodsName=" + goodsName
								+ "/goodsNo=" + goodsNo + "/taxno=" + taxno);
					} else {
						parameterMgrService.saveGoodsInfo(info);
					}

					// 商品明细情报的取得
					List lstGoodsItem = parameterMgrService
							.getGoodsItemInfoPK(info);
					if (lstGoodsItem != null && lstGoodsItem.size() > 0) {
						System.out.println("第" + (i + 1)
								+ "条数据,商品明细表已经存在.goodsNo=" + goodsNo
								+ "/transType=" + transType);
					} else {
						parameterMgrService.saveGoodsItemInfo(info);
					}
				} else {
					System.out.println("商品名称，发票商品编号，纳税人识别号，交易类型不能为空.");
				}
				i++;
			}
			setResultMessages("上传成功.");
			return SUCCESS;
		} else {
			setResultMessages("请选择上传文件.");
			return ERROR;
		}
	}
	public List setwriteWidth(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 info=null;
		 String invoiceType = "";
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			info=(VmsTaxInfo)list.get(i);
			if ("0".equals(info.getFapiaoType())) {
				invoiceType = "增值税专用发票";
			} else if ("1".equals(info.getFapiaoType())) {
				invoiceType = "增值税普通发票";
			}
			rowlist.add(Integer.toString(i+1));
			rowlist.add(info.getTaxno());
			rowlist.add(invoiceType);
			rowlist.add(info.getTaxId());
			rowlist.add(info.getTaxRate());
			sheetList.add(rowlist);
		}
		return sheetList;
	}
	/**
	 * @Action
	 * 
	 *         税目管理 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void writeToExcel(OutputStream os) throws IOException,
		RowsExceededException, WriteException, Exception {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("税目管理", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "纳税人识别号", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "税类", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "税目种类", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "税率", JXLTool.getHeader()); 
		ws.addCell(header1);  
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		 info = new VmsTaxInfo();
		// 纳税人识别号
		 if(!"".equals(this.getTaxno())&&this.getTaxno()!=null){
			 info.setTaxno("like  '%"+this.getTaxno()+"%'");
		 }
		//用户ID
		info.setUser_id(this.getCurrentUser().getId());

		// 税类
		info.setFapiaoType(this.getFapiaoType());

		// 税目编号
		if(!"".equals(this.getTaxId())&&this.getTaxId()!=null){
			info.setTaxId("like '%"+this.getTaxId()+"%'");
		}
		
		// 一览数据检索
		List taxLists = parameterMgrService.getListTaxItemInfo(info, null);
		
		JXLTool.setAutoWidth(ws, setwriteWidth(taxLists));
		int count = 1;
		for (int i = 0; i < taxLists.size(); i++) {
			VmsTaxInfo info1 = (VmsTaxInfo) taxLists.get(i);
			
			int column = count++;
			setWritableSheet(ws, info1, column);
		}
		wb.write();
		wb.close();
	}
	public List setwriteWidth1(List list) throws JXLException{
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 info1=null;
		 
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			info1=(GoodsInfo)list.get(i);
			rowlist.add(Integer.toString(i+1));
			rowlist.add(info1.getGoodsName());
			rowlist.add(info1.getGoodsNo());
			rowlist.add(info1.getTaxNo());
			sheetList.add(rowlist);

		}
		return sheetList;
	}
	/**
	 * @Action
	 * 
	 *         商品信息 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void writeToExcelGoods(OutputStream os) throws IOException,
			RowsExceededException, WriteException, Exception {

		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("商品信息", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "商品名称", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "发票商品编号", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "纳税人识别号", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "交易编码", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		info1 = new GoodsInfo();
		for(int i = 0; i < 5; i++){
			ws.setColumnView(i, 19);
		}
//		if (getTaxno() != null && !"".equals(getTaxno())) {
//			info1.setTaxNo(" in ('" + getTaxno() + "')");
//		} else {
//			List taxList = getTaxperList();
//			String arrId = "";
//			for (int i = 0; i <= taxList.size() - 1; i++) {
//				Organization user = (Organization) taxList.get(i);
//				arrId = arrId + "'" + user.getTaxperNumber() + "',";
//			}
//			if (arrId != null && !"".equals(arrId)) {
//				arrId = arrId.substring(0, arrId.length() - 1);
//				info1.setTaxNo(" in (" + arrId + ")");
//			} else {
//				info1.setTaxNo(" in ('')");
//			}
//		}
//		
//		// 发票商品编号
//		if (getGoodsNo() != null && !"".equals(getGoodsNo())) {
//			info1.setGoodsNo(" like '%" + this.getGoodsNo() + "%'");
//		}
//
//		// 商品名称
//		if (getGoodsName() != null && !"".equals(getGoodsName())) {
//			info1.setGoodsName(" like '%" + this.getGoodsName() + "%'");
//		}
//
//		List goodsList = parameterMgrService.getListGoodsInfo(info1, null);
//		JXLTool.setAutoWidth(ws, setwriteWidth1(goodsList));
//		int count = 1;
//		List list=null;
//		for (int i = 0; i < goodsList.size(); i++) {
//			GoodsInfo inf = (GoodsInfo) goodsList.get(i);
//			list=new ArrayList();
//			list= this.parameterMgrService.getListBusiness(inf);
//			if(list!=null){
//				for(int j=0;j<list.size();j++){
//					GoodsInfo in =new GoodsInfo();
//						in=(GoodsInfo) list.get(j);
//					int column = count++;
//					
//					setWritableSheetGoods(ws, inf,in, column);
//				}
//			}
//			// }
//		}
		List content = parameterMgrService.getGoodInfoToExcel(info1);
		GoodsInfo map = new GoodsInfo();
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			int column = count++;
			map = (GoodsInfo) content.get(c);
			writeSheet(ws, map, column);
		}
		wb.write();
		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	/**
	 * @Action
	 * 
	 *         税目管理 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void setWritableSheet(WritableSheet ws, VmsTaxInfo info, int column)
			throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column),
				JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell2 = new Label(1, column, info.getTaxno(),
				JXLTool.getContentFormat());
		// 税类
		String invoiceType = "";
		if ("0".equals(info.getFapiaoType())) {
			invoiceType = "增值税专用发票";
		} else if ("1".equals(info.getFapiaoType())) {
			invoiceType = "增值税普通发票";
		}
		Label cell3 = new Label(2, column, invoiceType,
				JXLTool.getContentFormat());
		// 税目编号
		Label cell4 = new Label(3, column, info.getTaxId(),
				JXLTool.getContentFormat());
		if(Double.parseDouble(info.getTaxRate())==0){
			info.setTaxRate("0.00");
		}
		// 税率
		Label cell5 = new Label(4, column, info.getTaxRate(),
				JXLTool.getContentFormatNumberFloat());
		//税率数字格式导出
		Number number5  =   new  Number( 4,  column ,Double.parseDouble(info.getTaxRate()),JXLTool.getContentFormatNumberFloat());  
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(number5); 
	}

	/**
	 * @Action
	 * 
	 *         商品信息 导出
	 * 
	 * @author lee
	 * @return
	 */
	private void setWritableSheetGoods(WritableSheet ws, GoodsInfo info,GoodsInfo in,
			int column) throws WriteException {
		
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column),
				JXLTool.getContentFormat());
		// 商品名称
		Label cell2 = new Label(1, column, info.getGoodsName(),
				JXLTool.getContentFormat());
		// 发票商品编号
		Label cell3 = new Label(2, column, info.getGoodsNo(),
				JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell4 = new Label(3, column, info.getTaxNo(),
				JXLTool.getContentFormat());

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
	}

	private void writeSheet(WritableSheet ws, GoodsInfo map, int column) throws WriteException {
		// 序号
		Label cell1 = new Label(0, column, String.valueOf(column),
				JXLTool.getContentFormat());
		// 商品名称
		Label cell2 = new Label(1, column, (String) map.getGoodsName(),
				JXLTool.getContentFormat());
		// 发票商品编号
		Label cell3 = new Label(2, column, (String) map.getGoodsNo(),
				JXLTool.getContentFormat());
		// 纳税人识别号
		Label cell4 = new Label(3, column, (String) map.getTaxNo(),
				JXLTool.getContentFormat());

		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
	}
	
	/**
	 * <p>
	 * 方法名称: setResultMessages|描述:设置session中存储的处理信息结果
	 * </p>
	 * 
	 * @param resultMessages
	 *            处理信息结果
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public void setResultMessages(String resultMessages) {

		log.info(request.getHeader("user-agent"));
		log.info(request.getHeader(request.getLocale().toString()));
		resultMessages = chr2Unicode(resultMessages);
		log.info(resultMessages);
		try {
			this.RESULT_MESSAGE = java.net.URLEncoder.encode(resultMessages,
					"utf-8");
			request.setAttribute("RESULT_MESSAGE", RESULT_MESSAGE);
			request.setAttribute("resultMessages", resultMessages);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

	/**
	 * 中文转unicode字符(英文环境)
	 * 
	 * @param str
	 * @return
	 */
	public String chr2Unicode(String str) {
		String[] a = { "", "000", "00", "0", "" };
		String result = "";
		if (StringUtils.isNotEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				int chr = (char) str.charAt(i);
				String s = Integer.toHexString(chr);
				result += "\\u" + a[s.length()] + s;
			}
		}
		return result;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getTaxno() {
		return taxno;
	}

	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public ParameterMgrService getParameterMgrService() {
		return parameterMgrService;
	}

	public File getAttachmentTaxItem() {
		return attachmentTaxItem;
	}
	
	public VmsTaxInfo getInfo() {
		return info;
	}

	public void setInfo(VmsTaxInfo info) {
		this.info = info;
	}

	public GoodsInfo getInfo1() {
		return info1;
	}

	public void setInfo1(GoodsInfo info1) {
		this.info1 = info1;
	}

	public String getAttachmentTaxItemFileName() {
		return attachmentTaxItemFileName;
	}

	public void setAttachmentTaxItem(File attachmentTaxItem) {
		this.attachmentTaxItem = attachmentTaxItem;
	}

	public void setAttachmentTaxItemFileName(String attachmentTaxItemFileName) {
		this.attachmentTaxItemFileName = attachmentTaxItemFileName;
	}

	public void setParameterMgrService(ParameterMgrService parameterMgrService) {
		this.parameterMgrService = parameterMgrService;
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String result_message) {
		RESULT_MESSAGE = result_message;
	}

	public String getUpdFlg() {
		return updFlg;
	}

	public void setUpdFlg(String updFlg) {
		this.updFlg = updFlg;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public List getBusinessList() {
		return businessList;
	}

	public void setBusinessList(List businessList) {
		this.businessList = businessList;
	}

	public String[] getSelectTaxNos() {
		return selectTaxNos;
	}

	public void setSelectTaxNos(String[] selectTaxNos) {
		this.selectTaxNos = selectTaxNos;
	}

	public String[] getSelectGoodsNames() {
		return selectGoodsNames;
	}

	public void setSelectGoodsNames(String[] selectGoodsNames) {
		this.selectGoodsNames = selectGoodsNames;
	}

	public String[] getSelectTransTypes() {
		return selectTransTypes;
	}

	public void setSelectTransTypes(String[] selectTransTypes) {
		this.selectTransTypes = selectTransTypes;
	}

	public String[] getSelectGoodsNos() {
		return selectGoodsNos;
	}

	public void setSelectGoodsNos(String[] selectGoodsNos) {
		this.selectGoodsNos = selectGoodsNos;
	}

	public String getTaxnoBak() {
		return taxnoBak;
	}

	public void setTaxnoBak(String taxnoBak) {
		this.taxnoBak = taxnoBak;
	}

	public String getGoodsNameBak() {
		return goodsNameBak;
	}

	public void setGoodsNameBak(String goodsNameBak) {
		this.goodsNameBak = goodsNameBak;
	}

	public String getTransTypeBak() {
		return transTypeBak;
	}

	public void setTransTypeBak(String transTypeBak) {
		this.transTypeBak = transTypeBak;
	}

	public List getCheckBusinessList() {
		return checkBusinessList;
	}

	public void setCheckBusinessList(List checkBusinessList) {
		this.checkBusinessList = checkBusinessList;
	}
	
	
}
