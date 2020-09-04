package com.cjit.vms.trans.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.StringUtil;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.vms.customer.model.SubCustomer;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.system.model.UserColComments;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransBillInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.TransInfoYS;
import com.cjit.vms.trans.model.trans.ConnCust;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.ws.jdkClient.VatServiceClient;
import com.cjit.ws.utils.Axis2Client;

public class TransInfoServiceImpl extends GenericServiceImpl implements
		TransInfoService {
	
	protected LogManagerService logManagerService;
	private VmsCommonService vmsCommonService;

	// ======================= ys ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public List selectTransInfoList(TransInfoYS transInfoYS,
			PaginationList paginationList) {
		return find("selectTransInfo", makeFindTransParamYS(transInfoYS),
				paginationList);
	}

	public List selectTransInfoList(TransInfoYS transInfoYS) {
		return find("selectTransInfo", makeFindTransParamYS(transInfoYS));
	}

	public List selectTransToTrans(TransInfoYS transInfoYS) {
		return find("selectTransToTrans", makeFindTransParamYS(transInfoYS));
	}

	private Map makeFindTransParamYS(TransInfoYS transInfoYS) {
		Map map = new HashMap();
		if (DataUtil.MAKE_INVOICE.equals(transInfoYS.getSearchFlag())) {
			// ��ѯ�ɹ���Ʊ����
			StringBuffer searchCondition = new StringBuffer();
			searchCondition
					.append(" (t.CUSTOMER_ID = '")
					.append(
							"' or t.CUSTOMER_ID is null) and (t.is_reverse = 'N' or t.is_reverse is null) ")
					.append(
							" and (t.REVERSE_TRANS_ID = '' or t.REVERSE_TRANS_ID is null) ");
			map.put("searchCondition", searchCondition.toString());
		} else if (DataUtil.DELETE_BILL.equals(transInfoYS.getSearchFlag())
				&& StringUtil.isNotEmpty(transInfoYS.getBillId())) {
			// Ʊ��ɾ��ʱ����ѯ��Ӧ������Ϣ
			StringBuffer searchColums = new StringBuffer();
			searchColums.append(", tb.TRANS_AMT as transBillAmt ");
			StringBuffer searchTables = new StringBuffer();
			searchTables.append(", vms_trans_bill tb ");
			StringBuffer searchCondition = new StringBuffer();
			searchCondition.append(" tb.bill_Id = '").append(
					transInfoYS.getBillId()).append(
					"' and tb.trans_id = t.trans_id ");
			map.put("searchColums", searchColums.toString());
			map.put("searchTables", searchTables.toString());
			map.put("searchCondition", searchCondition.toString());
		}
		map.put("transInfoYS", transInfoYS);
		return map;
	}

	public TransInfoYS selectTransInfo(String transId) {
		TransInfoYS transInfoYS = new TransInfoYS();
		transInfoYS.setTransBusId(transId);
		List list = this.selectTransInfoList(transInfoYS);
		if (list != null && list.size() == 1) {
			return (TransInfoYS) list.get(0);
		} else {
			return null;
		}
	}

	public TransInfoYS selectTransInfoById(String transId) {
		TransInfoYS transInfoYS = new TransInfoYS();
		transInfoYS.setTransId(transId);
		List list = this.selectTransInfoList(transInfoYS);
		if (list != null && list.size() == 1) {
			return (TransInfoYS) list.get(0);
		} else {
			return null;
		}
	}

	public void updateTransInfoYS(TransInfoYS transInfoYS) {
		Map map = new HashMap();
		map.put("transInfo", transInfoYS);
		this.save("updateTransInfoYS", map);
	}

	// ======================= ys ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	public List findTransInfoList(TransInfo transInfo,
			PaginationList paginationList) {
		if(DataUtil.MAKE_INVOICE.equals(transInfo.getSearchFlag())){
			Map map = new HashMap();
			List instIds=transInfo.getLstAuthInstId();
			List lstTmp=new ArrayList();
			for(int i=0;i<instIds.size();i++){
				Organization org=(Organization)instIds.get(i);
				lstTmp.add(org.getId());
			}
			map.put("auth_inst_ids", lstTmp);
			map.put("transInfo", transInfo);
			return findLargeData("outPutInvoiceApplyList", map, paginationList);
		}else if(DataUtil.DETAIL.equals(transInfo.getSearchFlag())){
				Map map = new HashMap();
				List instIds=transInfo.getLstAuthInstId();
				List lstTmp=new ArrayList();
				for(int i=0;i<instIds.size();i++){
					Organization org=(Organization)instIds.get(i);
					lstTmp.add(org.getId());
				}
				map.put("auth_inst_ids", lstTmp);
				map.put("transInfo", transInfo);
				return find("outPutInvoiceApplyDetail", map, paginationList);
		}else{
			return find("findTransInfo", makeFindTransParam(transInfo),
					paginationList);
		}
	}

	public List findTransInfoList(TransInfo transInfo) {
		Map map = new HashMap();
		List instIds=transInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		map.put("transInfo", transInfo);
		if (DataUtil.AUTO_INVOICE.equals(transInfo.getSearchFlag())) {
			return find("findTransInfoForAuto", makeFindTransParam(transInfo));
		}else{
			return find("findTransInfo", map);
		}
	}

	private Map makeFindTransParam(TransInfo transInfo) {
		Map map = new HashMap();
		if(transInfo.getType() != null && transInfo.getType() == "true"){
			map.put("type",transInfo.getType());
		}
		List instIds=transInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		if (DataUtil.AUTO_INVOICE.equals(transInfo.getSearchFlag())){
			// 查询客户信息表
			StringBuffer searchTables = new StringBuffer();
			searchTables
					.append(" inner join VMS_CUSTOMER_INFO c on t.CUSTOMER_ID = c.customer_id ");
			/*20160113  商品查询表变更
			 * searchTables
					.append(" inner join VMS_GOODS_INFO goods on goods.TAX_NO = t.bank_Code ");
			searchTables
					.append(" inner join VMS_GOODS_ITEM_INFO goods_item on goods_item.GOODS_NO = goods.GOODS_NO ");
			searchTables.append(" and goods_item.TRANS_TYPE = t.TRANS_TYPE ");
			searchTables.append(" and goods_item.TAX_NO = t.bank_Code ");*/
			searchTables.append(" and t.trans_fapiao_flag = c.customer_fapiao_flag	");
			searchTables.append("  inner join vms_trans_type v	");
			searchTables.append(" on v.trans_type_id = t.trans_type	");
			searchTables.append(" inner join vms_goods_trans v1 ");
			searchTables.append(" on v1.trans_type_id =  t.trans_type ");
			searchTables.append(" and v1.tax_no = inst.taxpernumber ");
			searchTables.append(" inner join vms_goods_info v2 ");
			searchTables.append(" on v1.goods_id = v2.goods_id ");

			map.put("searchTables", searchTables.toString());
			// 查询客户信息列
			StringBuffer searchColums = new StringBuffer();
			searchColums.append(", c.customer_cname as customerName,").append(
					" c.customer_taxno as customerTaxno, ").append(
					" c.customer_address as customerAddress,").append(
					" c.customer_phone as customerTel, ").append(
				    " c.taxpayer_type as customerTaxPayerType, ").append(
					" c.fapiao_type as customerFaPiaoType ");
			map.put("searchColums", searchColums.toString());
		}
		map.put("transInfo", transInfo);
		return map;
	}

	public TransInfo findTransInfo(BillInfo info) {
		TransInfo transInfo = new TransInfo();
		transInfo.setSearchFlag(DataUtil.DETAIL);
		transInfo.setTransId(info.getTransId());
		transInfo.setAmt(new BigDecimal(0));
		transInfo.setTaxRate(new BigDecimal(0));
		transInfo.setLstAuthInstId(info.getLstAuthInstId());
		transInfo.setTransCustomerList(info.getCustomerList());
		List list = this.findTransInfoList(transInfo);
		if (list != null && list.size() == 1) {
			return (TransInfo) list.get(0);
		} else {
			return null;
		}
	}

	public void updateTransInfo(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		this.update("updateTransInfo", map);
	}
	
	public void updateTransInfoForCancel(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		this.update("updateTransInfoStatusBalance", map);
	}

	public void updateTransInfoForAuto(String dataStatus, String billId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("billId", billId);
		this.update("updateTransInfoStatusForAuto", map);
	}

	public void updateTransDatastatus(String transId, String datastatus) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("dataStatus", datastatus);
		this.update("updateTransDataStatus", map);
	}

	public void saveTransBill(String transId, String billId, String billItemId,
			BigDecimal amt, BigDecimal taxAmt, BigDecimal income) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("billId", billId);
		map.put("billItemId", billItemId);
		// map.put("transAmt", transAmt);
		// map.put("billStatus", DataUtil.BILL_STATUS_1);
		map.put("amt", amt);
		map.put("taxAmt", taxAmt);
		map.put("income", income);
		this.save("saveTransBill", map);
	}

	public void saveTransBillForAuto(List transBillInfoList) {
		this.insertBatch("saveTransBill", transBillInfoList);
	}

	public void updateTransBillStatus(String transId, String billId,
			String billItemId, String billStatus) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("billId", billId);
		map.put("billItemId", billItemId);
		map.put("billStatus", billStatus);
		this.save("updateTransBillStatus", map);
	}

	public long findTransBillCount(String transId, String billId,
			String billItemId, String searchFlag) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("billId", billId);
		map.put("billItemId", billItemId);
		if (DataUtil.TRANS_BILL_NO_PASS.equals(searchFlag)) {
			String searchCondition = " exists (select 1 from vms_bill_info b where b.bill_id = t.bill_id and b.datastatus in ("
					+ DataUtil.BILL_STATUS_1
					+ ", "
					+ DataUtil.BILL_STATUS_2
					+ ")) ";
			map.put("searchCondition", searchCondition);
		}
		Object obj = this.findForObject("findTransBillCount", map);
		return Long.valueOf(String.valueOf(obj)).longValue();
	}

	public void deleteTransBill(String transId, String billId) {
		Map params = new HashMap();
		params.put("transId", transId);
		params.put("billId", billId);
		this.delete("deleteTransBill", params);
	}

	public List findTransInfoTempList(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		// if (transInfo != null) {
		// StringBuffer searchCondition = new StringBuffer();
		// if (DataUtil.CHECK.equalsIgnoreCase(transInfo.getSearchFlag())) {
		// searchCondition
		// .append(" (is_reverse = '1' or is_reverse is null) ")
		// .append(
		// " and not exists (select 1 from vms_trans_info_temp t1 where
		// t.trans_id = t1.trans_id and t1.is_reverse = '1') ");
		// map.put("searchCondition", searchCondition.toString());
		// }
		// }
		return find("findTransInfoTemp", map);
	}

	public void deleteTransInfo(TransInfo transInfo) {
		Map params = new HashMap();
		params.put("transInfo", transInfo);
		this.delete("deleteTransInfo", params);
	}
	
	public void deleteTransData(String selectTransIds){
		Map params = new HashMap();
		params.put("trans_ids", selectTransIds.split(","));
		this.delete("deleteTransData", params);
	}

	public List findTransInfoMainQuery(TransInfo transInfo,
			PaginationList paginationList) {
		return findLargeData("findTransInfoMainQuery", makeFindTransParam(transInfo),
				paginationList);
	}

	public TransInfo findTransInfoMainQuery(String transId) {
		TransInfo transInfo = new TransInfo();
		transInfo.setTransId(transId);
		List list = this.findTransInfoMainQuery(transInfo);
		if (list != null && list.size() == 1) {
			return (TransInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findTransInfoMainQuery(TransInfo transInfo) {
		return find("findTransInfoMainQuery", makeFindTransParam(transInfo));
	}

	public String findGoodsNameByTransType(String transType) {
		Map map = new HashMap();
		map.put("transType", transType);
		List list = this.find("findGoodsNameByTransType", map);
		if (list != null && list.size() > 0) {
			return list.get(0).toString();
		} else {
			return null;
		}
	}

	public String getBillIdSequence() {
		Map para = new HashMap();
		List list = this.find("getBillIdSequence", para);
		String sequence = (String) list.get(0);
		return sequence;
	}

	public Map findSysParam(String itemKey) {
		Map param = new HashMap();
		param.put("ITEM_KEY", itemKey);
		Map map = new HashMap();
		List list = this.find("select_sys_param_vmss", param);
		if (list != null && list.size() > 0) {
			return (Map) list.get(0);
		}
		return map;
	}

	public void updateTransStatusAndBalance(String transId, BigDecimal balance,
			String dataStatus) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("balance", balance);
		map.put("dataStatus", dataStatus);
		this.save("updateTransStatusAndBalance", map);
	}

	public List findTransInfoForBillCancelList(TransInfo transInfo) {
		return find("findTransInfoForBillCancel", makeFindTransParam(transInfo));
	}

	public void saveTransBill(String transId, String billId, String billItemId,
			BigDecimal transAmt) {


	}

	public Map findIncomeById(String transId) {
		Map param = new HashMap();
		param.put("transId", transId);
		Map map = new HashMap();
		List list = this.find("findIncomeById", param);
		if (list != null && list.size() > 0) {
			return (Map) list.get(0);
		}
		return map;
	}

	public String checkTranSql(String sql) {
		Map map = new HashMap();
		map.put("sql", sql);
		List list;
		try {
			list = this.find("checkSQL", map);
		} catch (Exception e) {
			return "sql语法错误！";
		}
		return "sql语法正确!";
	}

	public List findUserColComments(UserColComments userColComments) {
		Map map = new HashMap();
		map.put("userColComments", userColComments);
		return find("findUserColComments", map);
	}

	/*public List findCustomerByCustomerId(String customerID,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("customerId", customerID);
		return find("findCustomerById", map, paginationList);
	}*/

	public List findCustomerByCustomerId(String customerID,String customerName,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("customerId", customerID);
		map.put("customerName", customerName);
		return find("findCustomerById", map, paginationList);
	}
	
	public List isFindSubCustomerByCustomerId(String customerID) {
		Map map = new HashMap();
		map.put("customerId", customerID);
		return find("selectSubCustomerList", map);
	}
	
	/**
	 * 查询子公司的所有信息
	 */
	public List findSubCustomerByCustomerId(String customerID,PaginationList paginationList){
		Map map = new HashMap();
		map.put("customerId", customerID);
		List list = find("selectSubCustomerList", map, paginationList);
		return list;
	}
	
	public void updateConnectCustomer(ConnCust connCust,int flag){
		List list = new ArrayList();
		String transIds = connCust.getTransIds();
		String[] transId = transIds.split(",");
		String currCustomerId = connCust.getCurrCustomerId();
		for(int i = 0 ;i < transId.length; i++){
			Map map = new HashMap();
			map.put("customerId", currCustomerId);
			map.put("transId", transId[i]);
			list.add(map);
		}
		try{
			if(flag == 0){
				this.updateBatch("updateConnectCustomer", list);
			}else{
				this.updateBatch("updateConnectSubCustomer", list);
				//将子公司表(sub_customer)中数据插入总公司表(vms_customer_info)中
				Map currMap = new HashMap();
				List insertlist = new ArrayList();
				currMap.put("customerId", connCust.getCurrCustomerId());
				SubCustomer currSubCustomer = (SubCustomer) this.find("findSubCustomerById", currMap).get(0);
				insertlist.add(currSubCustomer);
				this.insertBatch("insertConnectCustomer", insertlist);
			}
			//记录历史信息
			connCustHistory(connCust,flag);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 交易信息税额误差表
	public BigDecimal selectTransTaxAmtErr(String transId) {
		Map map = new HashMap();
		map.put("transId", transId);
		List list = this.find("selectTransTaxAmtErr", map);
		if (list != null && list.size() > 0) {
			return new BigDecimal(list.get(0).toString());
		} else {
			return null;
		}
	}
	public BigDecimal selectTransTaxAmt(String transId) {
		Map map = new HashMap();
		map.put("transId", transId);
		List list = this.find("selectTransTaxAmt", map);
		if (list != null && list.size() > 0) {
			return new BigDecimal(list.get(0).toString());
		} else {
			return null;
		}
	}
	public TransBillInfo selectTransBillInfo(String billId, String transId) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("transId", transId);
		List list = this.find("selectTransBillInfo", map);
		if (list != null && list.size() > 0) {
			return (TransBillInfo) list.get(0);
		} else {
			return null;
		}
	}
	public void updateTransTaxAmtErr(String transId, BigDecimal taxErr) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("taxErr", taxErr);
		this.save("updateTransTaxAmtErr", map);
	}
	public void insertTransTaxAmtErr(String transId, BigDecimal taxErr) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("taxErr", taxErr);
		this.update("insertTransTaxAmtErr", map);
	}
	// 交易信息税额误差表

	@Override
	public List findTransTypeList(Map map) {
		return this.find("findTransType", map);
	}

	@Override
	public List findTransCustomerList(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		return this.find("findTransCustomerListByTrans", map);
	}
//	@Override
//	public List findTransCustomerList(BillInfo billInfo) {
//		Map map = new HashMap();
//		map.put("billInfo", billInfo);
//		return this.find("findTransCustomerListByBill", map);
//	}
	
	/**
	 * 记录关联客户历史
	 */
	public void connCustHistory(ConnCust connCust,int flag){
		String transIds = connCust.getTransIds();//交易ID列
		connCust.setTransIds("");
		String orgCustId = connCust.getOrgCustomerId();//旧客户ID
		Customer orgCustomer = new Customer();//旧客户信息
		String currCustId = connCust.getCurrCustomerId();//新客户ID
		Customer currCustomer = new Customer();//新客户信息
		SubCustomer currSubCustomer = new SubCustomer();
		Map orgMap = new HashMap();
		orgMap.put("customerId", orgCustId);
		Map currMap = new HashMap();
		currMap.put("customerId", currCustId);
		orgCustomer = (Customer) this.find("findCustomerById", orgMap).get(0);
		if(flag == 0){
			currCustomer = (Customer) this.find("findCustomerById", currMap).get(0);
			connCust.setCurrCustomerTaxNo(currCustomer.getCustomerTaxno());
			connCust.setCurrCustomerName(currCustomer.getCustomerCName());
		}else{
			currSubCustomer = (SubCustomer) this.find("findSubCustomerById", currMap).get(0);
			connCust.setCurrCustomerTaxNo(currSubCustomer.getSubCustomerTaxno());
			connCust.setCurrCustomerName(currSubCustomer.getSubCustomerName());
		}
		connCust.setOrgCustomerTaxNo(orgCustomer.getCustomerTaxno());
		connCust.setOrgCustomerName(orgCustomer.getCustomerCName());
		
		String[] transId = transIds.split(",");
		List list = new ArrayList();
		for(int i = 0; i < transId.length; i ++){
			ConnCust param = new ConnCust();
			param = connCust;
			param.setTransId(transId[i]);
			list.add(param);
		}
		try{
			this.insertBatch("saveConnCustHis", list);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 @Override
	    public List checkingTransByCherNum(TransInfo transInfo, boolean flag) {
	        Map map = new HashMap();
	        if (flag == true) {
	            transInfo.setPremTerm(null);
	        }
	        map.put("transInfo", transInfo);
	        if(transInfo != null){
	        	map.put("selectTransId", transInfo.getSelectTransIds());
	        }
	        map.put("billFreq", DataUtil.PAYMENT_FREQUENCY_12);
	        map.put("dataStatus", DataUtil.TRANS_STATUS_1);
	        return this.find("checkingTransByCherNum", map);
	    }

	@Override
	public List findTransInfoListByTransId(String[] selectTransId) {
		Map map = new HashMap();
		map.put("selectTransId", selectTransId);
		return this.find("findTransInfoListByTransId",map);
	}

	@Override
	public void saveSaleAccountDetailsD(String transId,String acCountCodeD) {
		Map map = new HashMap();
		map.put("id", transId);
		map.put("acCountCodeD", acCountCodeD);
		this.save("saveSaleAccountDetailsD", map);
	}
	
	@Override
	public void saveSaleAccountDetailsC(String transId,String acCountCodeC) {
		Map map = new HashMap();
		map.put("id", transId);
		map.put("acCountCodeC", acCountCodeC);
		this.save("saveSaleAccountDetailsC", map);
	}

	@Override
	public long findCheckApplyTransInfoCount(Map map) {
		Object obj = this.findForObject("findCheckApplyTransInfoCount", map);
		return Long.valueOf(String.valueOf(obj)).longValue();
	}

	/*
	 * (non-Javadoc)
	 * 返回0-成功，1失败
	 */
	@Override
	public String applyInvoice(HttpServletRequest request,User user,String businessType,String businessCode,String source){
		//如果不存在已开票记录则通过webservice去核心获取开票信息
		String message="";

		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("001", "1");
		typeMap.put("002", "2");
		typeMap.put("003", "3");
		typeMap.put("015", "4");

		Map<String, String> feeTypeMap = new HashMap<String, String>();	//发往核心的费用类型
		feeTypeMap.put("A", "001");
		feeTypeMap.put("B", "002");
		feeTypeMap.put("C", "015");
		feeTypeMap.put("D", "016");
		feeTypeMap.put("E", "019");
		feeTypeMap.put("F", "017");
		feeTypeMap.put("G", "018");
		feeTypeMap.put("H", "020");
		Map<String,String> transTypeMap = new HashMap<String,String>();//交易类型映射
		transTypeMap.put("1-0.0000", "100101");
		transTypeMap.put("1-0.0600", "100102");
		transTypeMap.put("2-0.0600", "100201");
		transTypeMap.put("3-0.0600", "100201");
		transTypeMap.put("4-0.0000", "100102");
		transTypeMap.put("5-0.0600", "100201");

		Map<String, String> billFrequeMap = new HashMap<String, String>();	//发往核心的费用类型
		billFrequeMap.put("A", "12");
		billFrequeMap.put("B", "04");
		billFrequeMap.put("C", "02");
		billFrequeMap.put("D", "01");
		billFrequeMap.put("E", "00");
		billFrequeMap.put("F", "99");

		Map<String, String> taxRateMap = new HashMap<String, String>();	//税率对应关系
		taxRateMap.put("S", "0.06");
		taxRateMap.put("N", "0.03");
		taxRateMap.put("Z", "0");
		taxRateMap.put("P", "0.17");
		taxRateMap.put("F", "0");

		String ifType = typeMap.get(businessType);
		
		try {
			InputStream is = this.getClass().getResourceAsStream(
					"/config/config.properties");
			Properties prop = new Properties();
			prop.load(is);
			
			String interfaceParam = "";
			if("LSP".equals(source)){	//个险
				interfaceParam = (String) prop.get("LSP.interface");
			}else if("LIS".equals(source)){	//团险
				interfaceParam = (String) prop.get("LIS.interface.applyInvoice");
			}else {
				message = "数据来源获取失败";
				return "1|"+message;
			}
			
			String[] interfaceParams = interfaceParam.split("\\|");
			
			List<com.cjit.vms.trans.model.createBill.TransInfo> saveTransInfos = new  ArrayList<com.cjit.vms.trans.model.createBill.TransInfo>();
			List<Customer> saveCustomers = new  ArrayList<Customer>();
			List<SubCustomer> saveSubCustomers = new  ArrayList<SubCustomer>();
			
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			DecimalFormat decimalFormat = new DecimalFormat("000");
			String serialno = dateFormat.format(new Date())
					+ decimalFormat.format(new Random().nextInt(1000));
			
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			Element requestElement = document.addElement("REQUEST");
			Element headDoc = requestElement.addElement("HEAD");
			headDoc.addElement("REQUESTTYPE").addText("0002");	//开票申请交易
			headDoc.addElement("REQSERIALNO").addText(serialno);
			headDoc.addElement("FLOWINTIME").addText(DateUtils.getCurrentDateTime());
			
			Element dataDoc = requestElement.addElement("DATA");
			//dataDoc.addElement("BUSINESS_TYPE").addText(ifType);
			dataDoc.addElement("CONTNO").addText(businessCode);
			//dataDoc.addElement("INST_ID").addText(user.getOrgId());
			String requestXml = document.asXML();
			
			System.out.println("requestXml:"+requestXml);
			
			//校验xml正确性
			if(!XmlUtil.validateXML(requestXml)){
				message = "生成核心请求报文格式校验错误";
				return "1|"+message;
			}
			
			logManagerService.writeLog(request, user,
					serialno, "开票申请接口", "请求报文", requestXml, "1");
			
			/*JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			org.apache.cxf.endpoint.Client client = dcf.createClient(path);
			QName name = new QName("http://service.pub.sinosoft.com","queryVATInfo");
			
			Object[] objects = client.invoke(name, requestXml);
			String responseXml = objects[0].toString();*/
			
			String responseXml = "";
			if("LSP".equals(source)){	//个险
				/*VatServicePortType_VatServicePort_Client client = new VatServicePortType_VatServicePort_Client();
				responseXml = client.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);*/
				
				//个险专属的方法，里面有些如命名空间或 者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
				VatServiceClient vatServiceClient = new VatServiceClient();
				responseXml = vatServiceClient.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
			}else if("LIS".equals(source)){	//团险
				Axis2Client axis2Client = new Axis2Client();
				responseXml = axis2Client.invokeRPCClient(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
			}
			
			/*File file = new File("C:\\Users\\Administrator\\Desktop\\testXml\\aaa.xml");
	        FileReader reader = new FileReader(file);// 获取该文件的输入流  
	        char[] bb = new char[1024];// 用来保存每次读取到的字符  
	        String str = "";// 用来将每次读取到的字符拼接，当然使用StringBuffer类更好  
	        int n;// 每次读取到的字符长度  
	        while ((n = reader.read(bb)) != -1) {  
	            str += new String(bb, 0, n);  
	        }  
	        reader.close();// 关闭输入流，释放连接  
			String responseXml = str;*/

			if(StringUtil.isBlank(responseXml)){
				//失败
				message = "与核心接口调用异常，核心返回报文为空";
				return "1|"+message;
			}
			
			System.out.println("responseXml:"+responseXml);
			
			logManagerService.writeLog(request, user,
					serialno, "开票申请接口", "响应报文", responseXml, "1");
			
			Document rootDocument = DocumentHelper.parseText(responseXml);
			Node responseNode = rootDocument.selectSingleNode("RESPONSE");
			//首先判断是否存在错误信息
			String resultType = responseNode.selectSingleNode("RESULTTYPE").getStringValue();
			if(!"0".equals(resultType)){
				//失败
				message = responseNode.selectSingleNode("ERRORINFO").getStringValue();
				return "1|"+message;
			}
			
			Node headNode = responseNode.selectSingleNode("HEAD");
			
			List<Node> resultNodes = responseNode.selectNodes("RESULTLIST/RESULT");

			String customerId = "";
			String customerIds = "";
			String subCustomerIds = "";
			String checkSameCustomer = "";
			String checkSameSubCustomer = "";
			String repnums = "";
			String chernums = "";
			String checkSameWhereSql = "";
			for (int i = 0; i < resultNodes.size(); i++) {
				Node result = resultNodes.get(i);
				Customer customer = new Customer();
				
				//险种信息
				List<Node> subInsInfos = result.selectNodes("INSLIST/INSINFO");
				if(subInsInfos == null || subInsInfos.size() <= 0){
					message = "接口错误：核心响应报文缺少险种信息节点";
					return "1|"+message;
				}
				
				//先校验客户名称
				//无论个险团险，纳税人名称都不能为空
				if(result.selectSingleNode("CUSTOMER_NAME") == null || "".equals(result.selectSingleNode("CUSTOMER_NAME").getStringValue())){
					message = "接口错误：核心响应报文客户纳税人名称为空";
					return "1|"+message;
				}
				
				//该校验要放到险种信息循环外面，否则，每条险种都会生成一个新的customer号
				if((result.selectSingleNode("CUSTOMER_TAXNO") == null || "".equals(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue()))){
					if(!"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
						//如果是团险并且不是小规模纳税人，则纳税识别号不能为空
						message = "接口错误：团险核心响应报文非小规模纳税人客户纳税人识别号为空";
						return "1|"+message;
					}else{
						//小规模纳税人则系统自动生成主键
						customerId = UUID.randomUUID().toString().replace("-", "");
					}
				}else if((result.selectSingleNode("CUSTOMER_TAXNO") == null || "".equals(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue()))){
					//如果个险渠道，并且纳税人识别号为空，则系统自动生成一个customer表主键
					customerId = UUID.randomUUID().toString().replace("-", "");
				}else {
					//如果不是以上情况，则纳税识别号取返回报文的信息
					customerId = result.selectSingleNode("CUSTOMER_TAXNO").getStringValue();
				}
				
				//获取客户名称，如果客户名称在已存的客户list里存在，则客户id取已存的list中对应的客户id
				String customer_name = result.selectSingleNode("CUSTOMER_NAME").getStringValue();
				for (int z = 0; z < saveCustomers.size(); z++) {
					if(customer_name.equals(saveCustomers.get(z).getCustomerCName())){
						//如果已存列表里存在客户同名的，则取对应list中客户id
						customerId = saveCustomers.get(z).getCustomerID();
					}
				}
				
				for (int j = 0; j < subInsInfos.size(); j++) {
					checkSameWhereSql += "(";
					
					com.cjit.vms.trans.model.createBill.TransInfo mTransInfo = new com.cjit.vms.trans.model.createBill.TransInfo();
					//如果存在一条或多条险种，则将原发票信息拆成多条存储
					Node subInsInfo = subInsInfos.get(j);
					
					//先做格式校验
					if(result.selectSingleNode("CHERNUM") == null || "".equals(result.selectSingleNode("CHERNUM").getStringValue())){
						message = "接口错误：核心响应报文保单号为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("ORIGCURR") == null || "".equals(result.selectSingleNode("ORIGCURR").getStringValue())){
						message = "接口错误：核心响应报文交易币种为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("TRDT") == null || "".equals(result.selectSingleNode("TRDT").getStringValue())){
						message = "接口错误：核心响应报文交易日期为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("INVTYP") == null || "".equals(result.selectSingleNode("INVTYP").getStringValue())){
						message = "接口错误：核心响应报文发票类型为空";
						return "1|"+message;
					}else if(!"0".equals(result.selectSingleNode("INVTYP").getStringValue()) && !"1".equals(result.selectSingleNode("INVTYP").getStringValue())
							 && !"2".equals(result.selectSingleNode("INVTYP").getStringValue())){
						message = "接口错误：核心响应报文发票类型值错误";
						return "1|"+message;
					}
					
					if(result.selectSingleNode("BILLFREQ") == null || "".equals(result.selectSingleNode("BILLFREQ").getStringValue())){
						message = "接口错误：核心响应报文交费频率为空";
						return "1|"+message;
					}else if(StringUtil.isBlank(billFrequeMap.get(result.selectSingleNode("BILLFREQ").getStringValue()))){
						message = "接口错误：核心响应报交费频率值错误";
						return "1|"+message;
					}
					if(result.selectSingleNode("POLYEAR") == null || "".equals(result.selectSingleNode("POLYEAR").getStringValue())){
						message = "接口错误：核心响应报文保单年度为空";
						return "1|"+message;
					}else{
						try {
							Integer.parseInt(result.selectSingleNode("POLYEAR").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文保单年度值类型错误";
							return "1|"+message;
						}
					}
					if(result.selectSingleNode("HISSDTE") == null || "".equals(result.selectSingleNode("HISSDTE").getStringValue())){
						message = "接口错误：核心响应报文承保日期为空";
						return "1|"+message;
					}
					if("LSP".equals(source) && (result.selectSingleNode("LIFCNUM") == null || "".equals(result.selectSingleNode("LIFCNUM").getStringValue()))){
						message = "接口错误：个险核心响应报文被保险人客户号为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("OCCDATE") == null || "".equals(result.selectSingleNode("OCCDATE").getStringValue())){
						message = "接口错误：核心响应报文生效日期为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("PREMTERM") == null || "".equals(result.selectSingleNode("PREMTERM").getStringValue())){
						message = "接口错误：核心响应报文期数为空";
						return "1|"+message;
					}else{
						try {
							Integer.parseInt(result.selectSingleNode("PREMTERM").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文期数值类型错误";
							return "1|"+message;
						}
					}
					if("LIS".equals(source) && (result.selectSingleNode("TAXPAYER_TYPE") == null || "".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue()))){
						message = "接口错误：团险核心响应报文客户纳税人类别为空";
						return "1|"+message;
					}
					
					if(subInsInfo.selectSingleNode("INS_COD") == null || "".equals(subInsInfo.selectSingleNode("INS_COD").getStringValue())){
						message = "接口错误：核心响应报文险种代码为空";
						return "1|"+message;
					}
					if(subInsInfo.selectSingleNode("AMT_CNY") == null || "".equals(subInsInfo.selectSingleNode("AMT_CNY").getStringValue())){
						message = "接口错误：核心响应报文金额_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文金额_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("TAX_AMT_CNY") == null || "".equals(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue())){
						message = "接口错误：核心响应报文税额_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文税额_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("INCOME_CNY") == null || "".equals(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue())){
						message = "接口错误：核心响应报文收入_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文收入_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("TAX_RATE") == null || "".equals(subInsInfo.selectSingleNode("TAX_RATE").getStringValue())){
						message = "接口错误：核心响应报文税率为空";
						return "1|"+message;
					}else if(StringUtil.isBlank(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue()))){
						message = "接口错误：核心响应报交税率值错误";
						return "1|"+message;
					}else {
						try {
							new BigDecimal(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue()));
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文税率值类型错误";
							return "1|"+message;
						}
					}
					
					mTransInfo.setTransId(result.selectSingleNode("BUSINESS_ID").getStringValue()+subInsInfo.selectSingleNode("INS_COD").getStringValue());
					mTransInfo.setCherNum(result.selectSingleNode("CHERNUM").getStringValue());
					chernums += "'"+result.selectSingleNode("CHERNUM").getStringValue()+"',";
					checkSameWhereSql += "CHERNUM = '"+result.selectSingleNode("CHERNUM").getStringValue()+"' and ";
					
					mTransInfo.setRepNum("".equals(result.selectSingleNode("REPNUM").getStringValue())?null:result.selectSingleNode("REPNUM").getStringValue());
					if(StringUtil.isNotBlank(result.selectSingleNode("REPNUM").getStringValue())){
						repnums += "'"+result.selectSingleNode("REPNUM").getStringValue()+"',";
						checkSameWhereSql += "REPNUM = '"+result.selectSingleNode("REPNUM").getStringValue()+"' and ";
					}else {
						checkSameWhereSql += "REPNUM is null and ";
					}
					
					mTransInfo.setTtmpRcno(result.selectSingleNode("TTMPRCNO").getStringValue());
					mTransInfo.setCustomerId(customerId);
					mTransInfo.setTransCurr("CNY".equals(result.selectSingleNode("ORIGCURR").getStringValue())?"":result.selectSingleNode("ORIGCURR").getStringValue());
					mTransInfo.setTransDate(result.selectSingleNode("TRDT").getStringValue());
					//result.selectSingleNode("INVTYP").getStringValue()
					mTransInfo.setFapiaoType("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())?"0":"1");
					//费用类型做数据转换
//					mTransInfo.setFeeTyp(feeTypeMap.get(result.selectSingleNode("FEETYP").getStringValue()));	//feetype是否需要转换
					
					//交费频率做数据转换
					mTransInfo.setBillFreq(billFrequeMap.get(result.selectSingleNode("BILLFREQ").getStringValue()));
					
					mTransInfo.setPolYear(Integer.parseInt(result.selectSingleNode("POLYEAR").getStringValue()));
					mTransInfo.setHissDte(result.selectSingleNode("HISSDTE").getStringValue());
					mTransInfo.setPlanLongDesc(result.selectSingleNode("PLANLONGDESC").getStringValue());
					mTransInfo.setInstFrom(result.selectSingleNode("INSTFROM").getStringValue());
					mTransInfo.setInstTo(result.selectSingleNode("INSTTO").getStringValue());
					mTransInfo.setOccDate(result.selectSingleNode("OCCDATE").getStringValue());
					mTransInfo.setPremTerm(Integer.parseInt(result.selectSingleNode("PREMTERM").getStringValue()));
					
					if(StringUtil.isNotBlank(result.selectSingleNode("PREMTERM").getStringValue())){
						checkSameWhereSql += "PREMTERM = "+result.selectSingleNode("PREMTERM").getStringValue()+"";
					}else {
						checkSameWhereSql += "PREMTERM is null";
					}
					
					//险种信息
					mTransInfo.setAmtCny(new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue()));	//这个值注意下对不对已经保留小数情况
					mTransInfo.setTaxAmtCny(new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue()));
					mTransInfo.setTaxCnyBalance(new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue()));
					mTransInfo.setIncomeCny(new BigDecimal(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue()));
					mTransInfo.setBalance(new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue()));	//未开票金额
					mTransInfo.setVatRateCode(subInsInfo.selectSingleNode("TAX_RATE").getStringValue());
					mTransInfo.setTaxRate(new BigDecimal(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue())));
					/**
					 * 根据feetype 来转换得到transtype
					 * 1-保费收入
					 * 税率是6：100101，0：100102
					 * 2-手续费支出
					 * 100201
					 * 3-利息收入
					 * TODO
					 * 4-保护储金及投资款
					 * 100102
					 * 5-其他业务收入
					 * 100201
					 */
					String feeType = subInsInfo.selectSingleNode("FEETYP").getStringValue();
					String transType =transTypeMap.get(feeType+"-"+mTransInfo.getTaxRate());
					if(feeType == null || "".equals(feeType)){
						message = "接口错误：核心响应报文费用类型为空";
						return "1|"+message;
					}else if(StringUtil.isBlank(transType)){
						message = "接口错误：根据核心响应报文的费用类型获取交易类型失败";
						return "1|"+message;
					}
					mTransInfo.setTransType(transType);
					
					//transinfo表默认值设置
					mTransInfo.setTaxFlag("N");
					mTransInfo.setIsReverse(Double.parseDouble(subInsInfo.selectSingleNode("AMT_CNY").getStringValue())>=0?"N":"Y");	//0存什么
					mTransInfo.setBankCode(user.getOrgId());
					mTransInfo.setInstCode(user.getOrgId());
					mTransInfo.setDatastatus("1");
					mTransInfo.setTransFlag("2");
					mTransInfo.setDsouRce(source);
					mTransInfo.setTransFapiaoFlag("M");	//交易是否打票
					
					saveTransInfos.add(mTransInfo);
					
					checkSameWhereSql += ") or ";
				}
				
				//客户信息
				//先做格式校验
				
				//如果是团险非小规模纳税人，则所有客户信息都不能为空
				if("LIS".equals(source) && !"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
					if(result.selectSingleNode("CUSTOMER_ADDRESSAND") == null || "".equals(result.selectSingleNode("CUSTOMER_ADDRESSAND").getStringValue())){
						message = "接口错误：团险核心响应报文客户地址为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("CUSTOMER_PHONE") == null || "".equals(result.selectSingleNode("CUSTOMER_PHONE").getStringValue())){
						message = "接口错误：团险核心响应报文客户电话为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("CUSTOMER_BANKAND") == null || "".equals(result.selectSingleNode("CUSTOMER_BANKAND").getStringValue())){
						message = "接口错误：团险核心响应报文客户开户行为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("CUSTOMER_ACCOUNT") == null || "".equals(result.selectSingleNode("CUSTOMER_ACCOUNT").getStringValue())){
						message = "接口错误：团险核心响应报文客户开户行账号为空";
						return "1|"+message;
					}
				}
				
				customer.setCustomerCName(result.selectSingleNode("CUSTOMER_NAME").getStringValue());
				customer.setCustomerID(customerId);
				customerIds += "'"+customerId+"',";
				
				String customerName = result.selectSingleNode("CUSTOMER_NAME").getStringValue();
				boolean existFlag = false;
				for (int w = 0; w < saveCustomers.size(); w++) {
					if(customerName.equals(saveCustomers.get(w).getCustomerCName())){
						//如果已存列表里存在客户同名的，则不存储该条数据
						existFlag = true;
					}
				}
				if(!existFlag){
					//如果不存在，则存储
					customer.setCustomerTaxno(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue());
					customer.setTaxPayerType(result.selectSingleNode("TAXPAYER_TYPE").getStringValue());
					customer.setCustomerAddress(result.selectSingleNode("CUSTOMER_ADDRESSAND").getStringValue());
					customer.setCustomerPhone(result.selectSingleNode("CUSTOMER_PHONE").getStringValue());
					customer.setCustomerCBank(result.selectSingleNode("CUSTOMER_BANKAND").getStringValue());
					customer.setCustomerAccount(result.selectSingleNode("CUSTOMER_ACCOUNT").getStringValue());
					customer.setCustomerType("LSP".equals(source)?"I":"C");	//客户类型
					customer.setCustomerFapiaoFlag("M");	//是否打发票字段，默认手工打印
					customer.setFapiaoType("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())?"0":"1");		//默认开票类型
					saveCustomers.add(customer);
				}
				
				//子公司信息，只有团险才有子公司信息，因此个险不需要取
				if("LIS".equals(source)){
					List<Node> subCustomerNodes = result.selectNodes("SUB_CUSTOMER_LIST/SUB_CUSTOMER_INFO");
					for (int j = 0; j < subCustomerNodes.size(); j++) {
						String subCustomerId = "";
						
						Node subCustomerNode = subCustomerNodes.get(j);
						SubCustomer subCustomer = new SubCustomer();
						if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue())){
							message = "接口错误：核心响应报文子公司客户纳税人名称为空";
							return "1|"+message;
						}
						/*if("LIS".equals(source) && !"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
							//如果是团险非小规模纳税人，则所有客户信息都不能为空
							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue())){
								message = "接口错误：团险核心响应报文子公司客户纳税人识别号为空";
								return "1|"+message;
							}
							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND").getStringValue())){
								message = "接口错误：团险核心响应报文子公司客户地址为空";
								return "1|"+message;
							}
							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE").getStringValue())){
								message = "接口错误：团险核心响应报文子公司客户电话为空";
								return "1|"+message;
							}
							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND").getStringValue())){
								message = "接口错误：团险核心响应报文子公司客户开户行为空";
								return "1|"+message;
							}
							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT").getStringValue())){
								message = "接口错误：团险核心响应报文子公司客户开户行账号为空";
								return "1|"+message;
							}
							subCustomerId = subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue();
						}else */if("LIS".equals(source) && (subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue()))){
							//如果这个值是空，则直接生成一个即可
							subCustomerId = UUID.randomUUID().toString().replace("-", "");
						}else {
							subCustomerId = subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue();
						}
						
						//获取客户名称，如果客户名称在已存的子客户list里存在，则客户id取已存的list中对应的客户id
						String subCustomerName = subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue();
						boolean existFlag2 = false;
						for (int x = 0; x < saveSubCustomers.size(); x++) {
							if(subCustomerName.equals(saveSubCustomers.get(x).getSubCustomerName())){
								//如果已存列表里存在客户同名的，则不存储该条数据
								existFlag2 = true;
								subCustomerId = saveSubCustomers.get(x).getSubCustomerId();
							}
						}
						
						subCustomer.setSubCustomerId(subCustomerId);	//主键取什么值
						subCustomerIds += "'"+subCustomerId+"',";
						
						if(!existFlag2){
							//如果不存在相同记录，则存储
							subCustomer.setCustomerId(customerId);
							subCustomer.setSubCustomerName(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue());
							subCustomer.setSubCustomerTaxno(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue());
							subCustomer.setSubCustomerAddressand(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND").getStringValue());
							subCustomer.setSubCustomerPhone(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE").getStringValue());
							subCustomer.setSubCustomerBankand(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND").getStringValue());
							subCustomer.setSubCustomerAccount(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT").getStringValue());
							subCustomer.setTaxpayerType(subCustomerNode.selectSingleNode("TAXPAYER_TYPE").getStringValue());
							subCustomer.setSubCustomerType("LSP".equals(source)?"I":"C");	//客户类型
							subCustomer.setSubCustomerFapiaoFlag("M");	//是否打发票字段，默认手工打印
							saveSubCustomers.add(subCustomer);
						}
					}
				}
			}
			
			if(saveTransInfos == null || saveTransInfos.size() <= 0){
				message = "核心响应报文缺少交易数据节点";
				return "1|"+message;
			}
			
			//判断数据库中是否已经存在记录了，如果存在则删掉，然后插入，发票信息、客户信息、子客户信息都是同样处理，不用查询，直接删除，加上状态
			if("003".equals(businessType) || "015".equals(businessType)){
				//如果是保全或者是定结的类型，先删除数据库数据（未开票的数据），再插入
				String newBusinessType = businessType;
				if("003".equals(businessType)){
					newBusinessType = "016','017','018','019";
				}
				Map delMap1 = new HashMap();
				chernums = chernums.substring(0,chernums.length()-1);
				repnums = repnums.substring(0,repnums.length()-1);
				delMap1.put("chernum",chernums);
				delMap1.put("repnum",repnums);
				delMap1.put("businessType", newBusinessType);
				delMap1.put("source",source);
				delMap1.put("datastatus","1");
				this.delete("deleteTransInfos", delMap1);
			}else if("001".equals(businessType) || "002".equals(businessType)){
				//如果是首期或续期的类型
				checkSameWhereSql = checkSameWhereSql.substring(0,checkSameWhereSql.lastIndexOf(" or "));
				Map checkMap1 = new HashMap();
				checkMap1.put("checkSameWhereSql", checkSameWhereSql);

				List transInfoList = this.find("findTransByWhereSql",checkMap1);
				//循环每条记录，判断状态，如果是未开票的状态，则删除数据库记录，如果不是未开票记录，则需同时把saveTransInfos里的数据remove掉，防止插入的时候报错
				for (int i = 0; i < transInfoList.size(); i++) {
					com.cjit.vms.trans.model.createBill.TransInfo mTransInfo = 
							(com.cjit.vms.trans.model.createBill.TransInfo)transInfoList.get(i);
					if("1".equals(mTransInfo.getDatastatus())){
						Map delMap2 = new HashMap();
						delMap2.put("transid", mTransInfo.getTransId());
						delMap2.put("transtype", mTransInfo.getTransType());

						this.delete("deleteTransInfos2", delMap2);
					}else {
						for (int j = 0; j < saveTransInfos.size(); j++) {
							com.cjit.vms.trans.model.createBill.TransInfo mTransInfo2 = 
									(com.cjit.vms.trans.model.createBill.TransInfo)saveTransInfos.get(j);
							if (mTransInfo.getCherNum().equals(mTransInfo2.getCherNum()) && 
									((mTransInfo.getRepNum() == null && mTransInfo2.getRepNum() == null) || (mTransInfo.getRepNum() != null && mTransInfo.getRepNum().equals(mTransInfo2.getRepNum()))) &&
									(mTransInfo.getPremTerm() != null && mTransInfo.getPremTerm().equals(mTransInfo2.getPremTerm()))) {
								//这三个条件都相同说明是重复的 <>'未开票' 状态的记录，此时应该删除掉saveTransInfos里对应的记录，防止重复插入
								saveTransInfos.remove(j);
							}
						}
					}
				}
			}
			
			
			//客户信息要先删后插
			if(!"".equals(customerIds)){
				customerIds = customerIds.substring(0,customerIds.length()-1);
				Map delMap2 = new HashMap();
				delMap2.put("customerids", customerIds);
				this.delete("deleteCustomers", delMap2);
			}
			if(!"".equals(subCustomerIds)){
				subCustomerIds = subCustomerIds.substring(0,subCustomerIds.length()-1);
				Map delMap3 = new HashMap();
				delMap3.put("customerids", subCustomerIds);
				this.delete("deleteSubCustomers", delMap3);
			}
			
			if(saveTransInfos != null && saveTransInfos.size() > 0){
				//提交数据到数据库
				this.insertBatch("saveTransInfos", saveTransInfos);
				//插入之前要先校验是否存在记录？
				//只有交易信息存在时才存储客户信息
				if(saveCustomers != null && saveCustomers.size() > 0){
					this.insertBatch("saveCustomers", saveCustomers);
				}
				//有可能当前交易没有子客户
				if(saveSubCustomers != null && saveSubCustomers.size() > 0){
					this.insertBatch("saveSubCustomers", saveSubCustomers);
				}
			}
			//有可能
		} catch (Exception e) {
			e.printStackTrace();
			message = "接口调用处理失败";
			return "1|"+message;
		}
		return "0|成功";
	}

	public String applyInvoice(HttpServletRequest request,User user,String businessCode){
		//如果不存在已开票记录则通过webservice去核心获取开票信息
		String message="";

		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("001", "1");
		typeMap.put("002", "2");
		typeMap.put("003", "3");
		typeMap.put("015", "4");

//		Map<String, String> feeTypeMap = new HashMap<String, String>();	//发往核心的费用类型
//		feeTypeMap.put("A", "001");
//		feeTypeMap.put("B", "002");
//		feeTypeMap.put("C", "015");
//		feeTypeMap.put("D", "016");
//		feeTypeMap.put("E", "019");
//		feeTypeMap.put("F", "017");
//		feeTypeMap.put("G", "018");
//		feeTypeMap.put("H", "020");
		
		Map<String,String> transTypeMap = new HashMap<String,String>();//交易类型映射
		transTypeMap.put("1-0.00", "100102");
		transTypeMap.put("1-0.06", "100101");
		transTypeMap.put("2-0.06", "100201");
		transTypeMap.put("3-0.06", "100201");
		transTypeMap.put("4-0.00", "100102");
		transTypeMap.put("5-0.06", "100201");
		
		Map<String, String> feeTypeMap = new HashMap<String, String>();	//发往核心的费用类型
		feeTypeMap.put( "1","A");
		feeTypeMap.put( "2","B");
		feeTypeMap.put( "3","C");
		feeTypeMap.put( "4","D");
		feeTypeMap.put( "5","E");
//		feeTypeMap.put( "017","F");
//		feeTypeMap.put( "018","G");
//		feeTypeMap.put( "020","H");
		

		Map<String, String> billFrequeMap = new HashMap<String, String>();	//发往核心的费用类型
		billFrequeMap.put("A", "12");
		billFrequeMap.put("B", "04");
		billFrequeMap.put("C", "02");
		billFrequeMap.put("D", "01");
		billFrequeMap.put("E", "00");
		billFrequeMap.put("F", "99");

		Map<String, String> taxRateMap = new HashMap<String, String>();	//税率对应关系
		taxRateMap.put("S", "0.06");
		taxRateMap.put("N", "0.03");
		taxRateMap.put("Z", "0");
		taxRateMap.put("P", "0.17");
		taxRateMap.put("F", "0");

//		String ifType = typeMap.get(businessType);
		
		try {
			InputStream is = this.getClass().getResourceAsStream(
					"/config/config.properties");
			Properties prop = new Properties();
			prop.load(is);
			
			String interfaceParam = "";
//			if("LSP".equals(source)){	//个险
//				interfaceParam = (String) prop.get("LSP.interface");
//			}else if("LIS".equals(source)){	//团险
//			}else {
			interfaceParam = (String) prop.get("LIS.interface.applyInvoice");

			if ("".equals(interfaceParam)||null==interfaceParam) {
				message = "数据来源获取失败";
				return "1|"+message;
			}
				
//			}
			
			String[] interfaceParams = interfaceParam.split("\\|");
			
			List<com.cjit.vms.trans.model.createBill.TransInfo> saveTransInfos = new  ArrayList<com.cjit.vms.trans.model.createBill.TransInfo>();
			List<Customer> saveCustomers = new  ArrayList<Customer>();
			List<SubCustomer> saveSubCustomers = new  ArrayList<SubCustomer>();
			
			DateFormat dateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			DecimalFormat decimalFormat = new DecimalFormat("000");
			String serialno = dateFormat.format(new Date())
					+ decimalFormat.format(new Random().nextInt(1000));
			
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			Element requestElement = document.addElement("REQUEST");
			Element headDoc = requestElement.addElement("HEAD");
			headDoc.addElement("REQUESTTYPE").addText("0002");	//开票申请交易
			headDoc.addElement("REQSERIALNO").addText(serialno);
			headDoc.addElement("FLOWINTIME").addText(DateUtils.getCurrentDateTime());
			
			Element dataDoc = requestElement.addElement("DATA");
//			dataDoc.addElement("BUSINESS_TYPE").addText(ifType);
			dataDoc.addElement("CONTNO").addText(businessCode);
//			dataDoc.addElement("INST_ID").addText(user.getOrgId());
			String requestXml = document.asXML();
			
			System.out.println("requestXml:"+requestXml);
			
			//校验xml正确性
			if(!XmlUtil.validateXML(requestXml)){
				message = "生成核心请求报文格式校验错误";
				return "1|"+message;
			}
			
			logManagerService.writeLog(request, user,
					serialno, "开票申请接口", "请求报文", requestXml, "1");
			
			/*JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			org.apache.cxf.endpoint.Client client = dcf.createClient(path);
			QName name = new QName("http://service.pub.sinosoft.com","queryVATInfo");
			
			Object[] objects = client.invoke(name, requestXml);
			String responseXml = objects[0].toString();*/
			
			String responseXml = "";
//			if("LSP".equals(source)){	//个险
				/*VatServicePortType_VatServicePort_Client client = new VatServicePortType_VatServicePort_Client();
				responseXml = client.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);*/
				
				//个险专属的方法，里面有些如命名空间或 者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
//				VatServiceClient vatServiceClient = new VatServiceClient();
//				responseXml = vatServiceClient.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
//			}else if("LIS".equals(source)){	//团险
				Axis2Client axis2Client = new Axis2Client();
				responseXml = axis2Client.invokeRPCClient(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
//				responseXml = axis2Client.invokeRPCClient("http://localhost:8080/lis/services/VATService", requestXml, "http://vat.lis.sinosoft.com", "DoCenter");

//			}
				System.out.println("-------------返回报文---start----------------");
				System.out.println(responseXml);
				System.out.println("-------------返回报文------end-------------");

			

			if(StringUtil.isBlank(responseXml)){
				//失败
				message = "与核心接口调用异常，核心返回报文为空";
				return "1|"+message;
			}
			
			System.out.println("responseXml:"+responseXml);
			
			logManagerService.writeLog(request, user,
					serialno, "开票申请接口", "响应报文", responseXml, "1");
			
			Document rootDocument = DocumentHelper.parseText(responseXml);
			Node responseNode = rootDocument.selectSingleNode("RESPONSE");
			//首先判断是否存在错误信息
			String resultType = responseNode.selectSingleNode("RESULTTYPE").getStringValue();
			if(!"0".equals(resultType)){
				//失败
				message = responseNode.selectSingleNode("ERRORINFO").getStringValue();
				return "1|"+message;
			}
			
			Node headNode = responseNode.selectSingleNode("HEAD");
			
			List<Node> resultNodes = responseNode.selectNodes("RESULTLIST/RESULT");

			String customerId = "";
			String customerIds = "";
			String subCustomerIds = "";
			String checkSameCustomer = "";
			String checkSameSubCustomer = "";
			String repnums = "";
			String chernums = "";
			String checkSameWhereSql = "";
			for (int i = 0; i < resultNodes.size(); i++) {
				Node result = resultNodes.get(i);
				Customer customer = new Customer();
				
				//险种信息
				List<Node> subInsInfos = result.selectNodes("INSLIST/INSINFO");
				if(subInsInfos == null || subInsInfos.size() <= 0){
					message = "接口错误：核心响应报文缺少险种信息节点";
					return "1|"+message;
				}
				
				//先校验客户名称
				//无论个险团险，纳税人名称都不能为空
				if(result.selectSingleNode("CUSTOMER_NAME") == null || "".equals(result.selectSingleNode("CUSTOMER_NAME").getStringValue())){
					message = "接口错误：核心响应报文客户纳税人名称为空";
					return "1|"+message;
				}
				
				//该校验要放到险种信息循环外面，否则，每条险种都会生成一个新的customer号
//				if("LIS".equals(source) && (result.selectSingleNode("CUSTOMER_TAXNO") == null || "".equals(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue()))){
//					if(!"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
//						//如果是团险并且不是小规模纳税人，则纳税识别号不能为空
//						message = "接口错误：团险核心响应报文非小规模纳税人客户纳税人识别号为空";
//						return "1|"+message;
//					}else{
						//小规模纳税人则系统自动生成主键
						customerId = UUID.randomUUID().toString().replace("-", "");
//					}
//				}else if("LSP".equals(source) && (result.selectSingleNode("CUSTOMER_TAXNO") == null || "".equals(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue()))){
//					如果个险渠道，并且纳税人识别号为空，则系统自动生成一个customer表主键
//					customerId = UUID.randomUUID().toString().replace("-", "");
//				}else {
					//如果不是以上情况，则纳税识别号取返回报文的信息
//					customerId = result.selectSingleNode("CUSTOMER_TAXNO").getStringValue();
//				}
				
				//获取客户名称，如果客户名称在已存的客户list里存在，则客户id取已存的list中对应的客户id
				String customer_name = result.selectSingleNode("CUSTOMER_NAME").getStringValue();
				for (int z = 0; z < saveCustomers.size(); z++) {
					if(customer_name.equals(saveCustomers.get(z).getCustomerCName())){
						//如果已存列表里存在客户同名的，则取对应list中客户id
						customerId = saveCustomers.get(z).getCustomerID();
					}
				}
				
				for (int j = 0; j < subInsInfos.size(); j++) {
					checkSameWhereSql = "";
					checkSameWhereSql += "(";
					
					com.cjit.vms.trans.model.createBill.TransInfo mTransInfo = new com.cjit.vms.trans.model.createBill.TransInfo();
					//如果存在一条或多条险种，则将原发票信息拆成多条存储
					Node subInsInfo = subInsInfos.get(j);
					
					//先做格式校验
					if(result.selectSingleNode("CHERNUM") == null || "".equals(result.selectSingleNode("CHERNUM").getStringValue())){
						message = "接口错误：核心响应报文保单号为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("ORIGCURR") == null || "".equals(result.selectSingleNode("ORIGCURR").getStringValue())){
						message = "接口错误：核心响应报文交易币种为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("TRDT") == null || "".equals(result.selectSingleNode("TRDT").getStringValue())){
						message = "接口错误：核心响应报文交易日期为空";
						return "1|"+message;
					}
					if(result.selectSingleNode("INVTYP") == null || "".equals(result.selectSingleNode("INVTYP").getStringValue())){
						message = "接口错误：核心响应报文发票类型为空";
						return "1|"+message;
					}else if(!"0".equals(result.selectSingleNode("INVTYP").getStringValue()) && !"1".equals(result.selectSingleNode("INVTYP").getStringValue())
							 && !"2".equals(result.selectSingleNode("INVTYP").getStringValue())){
						message = "接口错误：核心响应报文发票类型值错误";
						return "1|"+message;
					}
					
					
					if(result.selectSingleNode("BILLFREQ") == null || "".equals(result.selectSingleNode("BILLFREQ").getStringValue())){
						message = "接口错误：核心响应报文交费频率为空";
						return "1|"+message;
					}else if(StringUtil.isBlank(billFrequeMap.get(result.selectSingleNode("BILLFREQ").getStringValue()))){
						message = "接口错误：核心响应报交费频率值错误";
						return "1|"+message;
					}
					if(result.selectSingleNode("POLYEAR") == null || "".equals(result.selectSingleNode("POLYEAR").getStringValue())){
						message = "接口错误：核心响应报文保单年度为空";
						return "1|"+message;
					}else{
						try {
							Integer.parseInt(result.selectSingleNode("POLYEAR").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文保单年度值类型错误";
							return "1|"+message;
						}
					}
					if(result.selectSingleNode("HISSDTE") == null || "".equals(result.selectSingleNode("HISSDTE").getStringValue())){
						message = "接口错误：核心响应报文承保日期为空";
						return "1|"+message;
					}
//					if("LSP".equals(source) && (result.selectSingleNode("LIFCNUM") == null || "".equals(result.selectSingleNode("LIFCNUM").getStringValue()))){
//						message = "接口错误：个险核心响应报文被保险人客户号为空";
//						return "1|"+message;
//					}
					if(result.selectSingleNode("OCCDATE") == null || "".equals(result.selectSingleNode("OCCDATE").getStringValue())){
						message = "接口错误：核心响应报文生效日期为空";
						return "1|"+message;
					}
//					if(result.selectSingleNode("PREMTERM") == null || "".equals(result.selectSingleNode("PREMTERM").getStringValue())){
//						message = "接口错误：核心响应报文期数为空";
//						return "1|"+message;
//					}else{
//						try {
//							Integer.parseInt(result.selectSingleNode("PREMTERM").getStringValue());
//						} catch (NumberFormatException e) {
//							message = "接口错误：核心响应报文期数值类型错误";
//							return "1|"+message;
//						}
//					}
//					if("LIS".equals(source) && (result.selectSingleNode("TAXPAYER_TYPE") == null || "".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue()))){
//						message = "接口错误：团险核心响应报文客户纳税人类别为空";
//						return "1|"+message;
//					}
					if(subInsInfo.selectSingleNode("FEETYP") == null || "".equals(subInsInfo.selectSingleNode("FEETYP").getStringValue())){
						message = "接口错误：核心响应报文费用类型为空";
						return "1|"+message;
					}
					if(subInsInfo.selectSingleNode("TRANSTYPE") == null || "".equals(subInsInfo.selectSingleNode("TRANSTYPE").getStringValue())){
						message = "接口错误：核心响应报文交易类型为空";
						return "1|"+message;
					}
					if(subInsInfo.selectSingleNode("INS_COD") == null || "".equals(subInsInfo.selectSingleNode("INS_COD").getStringValue())){
						message = "接口错误：核心响应报文险种代码为空";
						return "1|"+message;
					}
					if(subInsInfo.selectSingleNode("INS_NAM") == null || "".equals(subInsInfo.selectSingleNode("INS_NAM").getStringValue())){
						message = "接口错误：核心响应报文险种名称为空";
						return "1|"+message;
					}
					if(subInsInfo.selectSingleNode("AMT_CNY") == null || "".equals(subInsInfo.selectSingleNode("AMT_CNY").getStringValue())){
						message = "接口错误：核心响应报文金额_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文金额_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("TAX_AMT_CNY") == null || "".equals(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue())){
						message = "接口错误：核心响应报文税额_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文税额_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("INCOME_CNY") == null || "".equals(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue())){
						message = "接口错误：核心响应报文收入_人民币为空";
						return "1|"+message;
					}else{
						try {
							new BigDecimal(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文收入_人民币值类型错误";
							return "1|"+message;
						}
					}
					if(subInsInfo.selectSingleNode("TAX_RATE") == null || "".equals(subInsInfo.selectSingleNode("TAX_RATE").getStringValue())){
						message = "接口错误：核心响应报文税率为空";
						return "1|"+message;
					}
					//取核心传过来的税率
//					else if(StringUtil.isBlank(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue()))){
//						message = "接口错误：核心响应报交税率值错误";
//						return "1|"+message;
//					}
					else {
						try {
//							new BigDecimal(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue()));
							new BigDecimal(subInsInfo.selectSingleNode("TAX_RATE").getStringValue());
						} catch (NumberFormatException e) {
							message = "接口错误：核心响应报文税率值类型错误";
							return "1|"+message;
						}
					}
					
					mTransInfo.setTransId(subInsInfo.selectSingleNode("BUSINESS_ID").getStringValue()+subInsInfo.selectSingleNode("INS_COD").getStringValue());
//					mTransInfo.setTransId(subInsInfo.selectSingleNode("BUSINESS_ID").getStringValue());

					mTransInfo.setCherNum(result.selectSingleNode("CHERNUM").getStringValue());
					chernums += "'"+result.selectSingleNode("CHERNUM").getStringValue()+"',";
					checkSameWhereSql += "CHERNUM = '"+result.selectSingleNode("CHERNUM").getStringValue()+"' and ";
					
					mTransInfo.setRepNum("".equals(result.selectSingleNode("REPNUM").getStringValue())?null:result.selectSingleNode("REPNUM").getStringValue());
					if(StringUtil.isNotBlank(result.selectSingleNode("REPNUM").getStringValue())){
						repnums += "'"+result.selectSingleNode("REPNUM").getStringValue()+"',";
						checkSameWhereSql += "REPNUM = '"+result.selectSingleNode("REPNUM").getStringValue()+"' and ";
					}else {
						checkSameWhereSql += "REPNUM is null and ";
					}
					
					mTransInfo.setTtmpRcno(result.selectSingleNode("TTMPRCNO").getStringValue());
					mTransInfo.setCustomerId(customerId);
					mTransInfo.setTransCurr("CNY".equals(result.selectSingleNode("ORIGCURR").getStringValue())?"":result.selectSingleNode("ORIGCURR").getStringValue());
					mTransInfo.setTransDate(result.selectSingleNode("TRDT").getStringValue());
					mTransInfo.setFapiaoType("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())?"0":"1");
					
					//费用类型做数据转换
//					mTransInfo.setFeeTyp(feeTypeMap.get(result.selectSingleNode("FEETYP").getStringValue()));	//feetype是否需要转换
					mTransInfo.setFeeTyp(subInsInfo.selectSingleNode("FEETYP").getStringValue());	//feetype是否需要转换

					//交费频率做数据转换
					mTransInfo.setBillFreq(billFrequeMap.get(result.selectSingleNode("BILLFREQ").getStringValue()));
					
					mTransInfo.setPolYear(Integer.parseInt(result.selectSingleNode("POLYEAR").getStringValue()));
					mTransInfo.setHissDte(result.selectSingleNode("HISSDTE").getStringValue());
					//英大这边不用
//					mTransInfo.setPlanLongDesc(result.selectSingleNode("PLANLONGDESC").getStringValue());
					mTransInfo.setInstFrom(result.selectSingleNode("INSTFROM").getStringValue());
					mTransInfo.setInstTo(result.selectSingleNode("INSTTO").getStringValue());
					mTransInfo.setOccDate(result.selectSingleNode("OCCDATE").getStringValue());
					
					
					if(StringUtil.isNotBlank(result.selectSingleNode("PREMTERM").getStringValue())){
					    mTransInfo.setPremTerm(Integer.parseInt(result.selectSingleNode("PREMTERM").getStringValue()));
						checkSameWhereSql += "PREMTERM = "+result.selectSingleNode("PREMTERM").getStringValue()+"";
					}else {
						checkSameWhereSql += "PREMTERM is null";
					}
					
					
//					mTransInfo.setTransType(subInsInfo.selectSingleNode("INS_COD").getStringValue());
					//险种信息ss
					mTransInfo.setGoodsId(subInsInfo.selectSingleNode("INS_NAM").getStringValue());
					mTransInfo.setAmtCny(new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue()));	//这个值注意下对不对已经保留小数情况
					mTransInfo.setTaxAmtCny(new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue()));
					mTransInfo.setTaxCnyBalance(new BigDecimal(subInsInfo.selectSingleNode("TAX_AMT_CNY").getStringValue()));
					mTransInfo.setIncomeCny(new BigDecimal(subInsInfo.selectSingleNode("INCOME_CNY").getStringValue()));
					mTransInfo.setBalance(new BigDecimal(subInsInfo.selectSingleNode("AMT_CNY").getStringValue()));	//未开票金额
					
					
					mTransInfo.setVatRateCode(subInsInfo.selectSingleNode("TAX_RATE").getStringValue());//不知道怎么搞这个了，先写死一个‘S’
					mTransInfo.setVatRateCode("S");
//					mTransInfo.setTaxRate(new BigDecimal(taxRateMap.get(subInsInfo.selectSingleNode("TAX_RATE").getStringValue())));
					mTransInfo.setTaxRate(new BigDecimal(subInsInfo.selectSingleNode("TAX_RATE").getStringValue()));
					/**
					 * 根据transtype 来转换得到transtype
					 * 1-保费收入
					 * 税率是6：100101，0：100102
					 * 2-手续费支出
					 * 100201
					 * 3-利息收入
					 * TODO
					 * 4-保护储金及投资款
					 * 100102
					 * 5-其他业务收入
					 * 100201
					 */
					String feeType = subInsInfo.selectSingleNode("FEETYP").getStringValue();
					String transType =transTypeMap.get(feeType+"-"+mTransInfo.getTaxRate());
					if(feeType == null || "".equals(feeType)){
						message = "接口错误：核心响应报文费用类型为空";
						return "1|"+message;
					}else if(StringUtil.isBlank(transType)){
						message = "接口错误：根据核心响应报文的费用类型获取交易类型失败";
						return "1|"+message;
					}
					mTransInfo.setTransType(transType);
					//transinfo表默认值设置
					mTransInfo.setTaxFlag("N");
					mTransInfo.setIsReverse(Double.parseDouble(subInsInfo.selectSingleNode("AMT_CNY").getStringValue())>=0?"N":"Y");	//0存什么
					mTransInfo.setBankCode(user.getOrgId());
					mTransInfo.setInstCode(user.getOrgId());
					mTransInfo.setDatastatus("1");
					mTransInfo.setTransFlag("2");
//					mTransInfo.setDsouRce(source);
					mTransInfo.setTransFapiaoFlag("M");	//交易是否打票
					mTransInfo.setTaxNo(getTaxNo(user.getOrgId()));
					saveTransInfos.add(mTransInfo);
					
					checkSameWhereSql += ") or ";
				}
				
				//客户信息
				//先做格式校验
				
				//如果是团险非小规模纳税人，则所有客户信息都不能为空
//				if("LIS".equals(source) && !"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
				
//				if ("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())) {
//					
//				}else{
//					if(result.selectSingleNode("CUSTOMER_ADDRESSAND") == null || "".equals(result.selectSingleNode("CUSTOMER_ADDRESSAND").getStringValue())){
//						message = "接口错误：团险核心响应报文客户地址为空";
//						return "1|"+message;
//					}
//					if(result.selectSingleNode("CUSTOMER_PHONE") == null || "".equals(result.selectSingleNode("CUSTOMER_PHONE").getStringValue())){
//						message = "接口错误：团险核心响应报文客户电话为空";
//						return "1|"+message;
//					}
//					if(result.selectSingleNode("CUSTOMER_BANKAND") == null || "".equals(result.selectSingleNode("CUSTOMER_BANKAND").getStringValue())){
//						message = "接口错误：团险核心响应报文客户开户行为空";
//						return "1|"+message;
//					}
//					if(result.selectSingleNode("CUSTOMER_ACCOUNT") == null || "".equals(result.selectSingleNode("CUSTOMER_ACCOUNT").getStringValue())){
//						message = "接口错误：团险核心响应报文客户开户行账号为空";
//						return "1|"+message;
//					}
//				}
					
//				}
				
				customer.setCustomerCName(result.selectSingleNode("CUSTOMER_NAME").getStringValue());
				customer.setCustomerID(customerId);
				customerIds += "'"+customerId+"',";
				
				String customerName = result.selectSingleNode("CUSTOMER_NAME").getStringValue();
				boolean existFlag = false;
				for (int w = 0; w < saveCustomers.size(); w++) {
					if(customerName.equals(saveCustomers.get(w).getCustomerCName())){
						//如果已存列表里存在客户同名的，则不存储该条数据
						existFlag = true;
					}
				}
				if(!existFlag){
					//如果不存在，则存储
					customer.setCustomerTaxno(result.selectSingleNode("CUSTOMER_TAXNO").getStringValue());
					customer.setTaxPayerType(result.selectSingleNode("TAXPAYER_TYPE").getStringValue());
					customer.setCustomerAddress(result.selectSingleNode("CUSTOMER_ADDRESSAND").getStringValue());
					customer.setCustomerPhone(result.selectSingleNode("CUSTOMER_PHONE").getStringValue());
					customer.setCustomerCBank(result.selectSingleNode("CUSTOMER_BANKAND").getStringValue());
					customer.setCustomerAccount(result.selectSingleNode("CUSTOMER_ACCOUNT").getStringValue());
//					customer.setCustomerType("LSP".equals(source)?"I":"C");	//客户类型
					customer.setCustomerFapiaoFlag("M");	//是否打发票字段，默认手工打印
//					customer.setFapiaoType("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())?"0":"1");		//默认开票类型
					//需要改      专票两个条件  1.G   2. 6%
					customer.setFapiaoType("G".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())?"0":"1");		//默认开票类型

					saveCustomers.add(customer);
				}
				
				//子公司信息，只有团险才有子公司信息，因此个险不需要取
//				if("LIS".equals(source)){
//					List<Node> subCustomerNodes = result.selectNodes("SUB_CUSTOMER_LIST/SUB_CUSTOMER_INFO");
//					for (int j = 0; j < subCustomerNodes.size(); j++) {
//						String subCustomerId = "";
//						
//						Node subCustomerNode = subCustomerNodes.get(j);
//						SubCustomer subCustomer = new SubCustomer();
//						if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue())){
//							message = "接口错误：核心响应报文子公司客户纳税人名称为空";
//							return "1|"+message;
//						}
//						/*if("LIS".equals(source) && !"S".equals(result.selectSingleNode("TAXPAYER_TYPE").getStringValue())){
//							//如果是团险非小规模纳税人，则所有客户信息都不能为空
//							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue())){
//								message = "接口错误：团险核心响应报文子公司客户纳税人识别号为空";
//								return "1|"+message;
//							}
//							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND").getStringValue())){
//								message = "接口错误：团险核心响应报文子公司客户地址为空";
//								return "1|"+message;
//							}
//							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE").getStringValue())){
//								message = "接口错误：团险核心响应报文子公司客户电话为空";
//								return "1|"+message;
//							}
//							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND").getStringValue())){
//								message = "接口错误：团险核心响应报文子公司客户开户行为空";
//								return "1|"+message;
//							}
//							if(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT").getStringValue())){
//								message = "接口错误：团险核心响应报文子公司客户开户行账号为空";
//								return "1|"+message;
//							}
//							subCustomerId = subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue();
//						}else */if("LIS".equals(source) && (subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO") == null || "".equals(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue()))){
//							//如果这个值是空，则直接生成一个即可
//							subCustomerId = UUID.randomUUID().toString().replace("-", "");
//						}else {
//							subCustomerId = subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue();
//						}
//						
//						//获取客户名称，如果客户名称在已存的子客户list里存在，则客户id取已存的list中对应的客户id
//						String subCustomerName = subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue();
//						boolean existFlag2 = false;
//						for (int x = 0; x < saveSubCustomers.size(); x++) {
//							if(subCustomerName.equals(saveSubCustomers.get(x).getSubCustomerName())){
//								//如果已存列表里存在客户同名的，则不存储该条数据
//								existFlag2 = true;
//								subCustomerId = saveSubCustomers.get(x).getSubCustomerId();
//							}
//						}
//						
//						subCustomer.setSubCustomerId(subCustomerId);	//主键取什么值
//						subCustomerIds += "'"+subCustomerId+"',";
//						
//						if(!existFlag2){
//							//如果不存在相同记录，则存储
//							subCustomer.setCustomerId(customerId);
//							subCustomer.setSubCustomerName(subCustomerNode.selectSingleNode("SUB_CUSTOMER_NAME").getStringValue());
//							subCustomer.setSubCustomerTaxno(subCustomerNode.selectSingleNode("SUB_CUSTOMER_TAXNO").getStringValue());
//							subCustomer.setSubCustomerAddressand(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ADDRESSAND").getStringValue());
//							subCustomer.setSubCustomerPhone(subCustomerNode.selectSingleNode("SUB_CUSTOMER_PHONE").getStringValue());
//							subCustomer.setSubCustomerBankand(subCustomerNode.selectSingleNode("SUB_CUSTOMER_BANKAND").getStringValue());
//							subCustomer.setSubCustomerAccount(subCustomerNode.selectSingleNode("SUB_CUSTOMER_ACCOUNT").getStringValue());
//							subCustomer.setTaxpayerType(subCustomerNode.selectSingleNode("TAXPAYER_TYPE").getStringValue());
//							subCustomer.setSubCustomerType("LSP".equals(source)?"I":"C");	//客户类型
//							subCustomer.setSubCustomerFapiaoFlag("M");	//是否打发票字段，默认手工打印
//							saveSubCustomers.add(subCustomer);
//						}
//					}
//				}
//			}
			
			if(saveTransInfos == null || saveTransInfos.size() <= 0){
				message = "核心响应报文缺少交易数据节点";
				return "1|"+message;
			}
			
			//判断数据库中是否已经存在记录了，如果存在则删掉，然后插入，发票信息、客户信息、子客户信息都是同样处理，不用查询，直接删除，加上状态
			/*if("003".equals(businessType) || "015".equals(businessType)){
				//如果是保全或者是定结的类型，先删除数据库数据（未开票的数据），再插入
				String newBusinessType = businessType;
				if("003".equals(businessType)){
					newBusinessType = "016','017','018','019";
				}
				Map delMap1 = new HashMap();
				chernums = chernums.substring(0,chernums.length()-1);
				repnums = repnums.substring(0,repnums.length()-1);
				delMap1.put("chernum",chernums);
				delMap1.put("repnum",repnums);
				delMap1.put("businessType", newBusinessType);
//				delMap1.put("source",source);
				delMap1.put("datastatus","1");
				this.delete("deleteTransInfos", delMap1);*/
//			}else if("001".equals(businessType) || "002".equals(businessType)){/*
				//如果是首期或续期的类型
				checkSameWhereSql = checkSameWhereSql.substring(0,checkSameWhereSql.lastIndexOf(" or "));
				Map checkMap1 = new HashMap();
				checkMap1.put("checkSameWhereSql", checkSameWhereSql);

				List transInfoList = this.find("findTransByWhereSql",checkMap1);
				//循环每条记录，判断状态，如果是未开票的状态，则删除数据库记录，如果不是未开票记录，则需同时把saveTransInfos里的数据remove掉，防止插入的时候报错
				for (int j = 0; j < transInfoList.size(); j++) {
					com.cjit.vms.trans.model.createBill.TransInfo mTransInfo = 
							(com.cjit.vms.trans.model.createBill.TransInfo)transInfoList.get(j);
					if("1".equals(mTransInfo.getDatastatus())){
						Map delMap2 = new HashMap();
						delMap2.put("transid", mTransInfo.getTransId());
						delMap2.put("transtype", mTransInfo.getTransType());

						this.delete("deleteTransInfos2", delMap2);
					}else {
						for (int k = 0; k < saveTransInfos.size(); k++) {
							com.cjit.vms.trans.model.createBill.TransInfo mTransInfo2 = 
									(com.cjit.vms.trans.model.createBill.TransInfo)saveTransInfos.get(k);
							if (mTransInfo.getCherNum().equals(mTransInfo2.getCherNum()) && 
									((mTransInfo.getRepNum() == null && mTransInfo2.getRepNum() == null) || (mTransInfo.getRepNum() != null && mTransInfo.getRepNum().equals(mTransInfo2.getRepNum()))) &&
									(mTransInfo.getPremTerm() != null && mTransInfo.getPremTerm().equals(mTransInfo2.getPremTerm()))) {
								//这三个条件都相同说明是重复的 <>'未开票' 状态的记录，此时应该删除掉saveTransInfos里对应的记录，防止重复插入
								saveTransInfos.remove(j);
							}
						}
					}
				}
			}
			
			
			//客户信息要先删后插
			if(!"".equals(customerIds)){
				customerIds = customerIds.substring(0,customerIds.length()-1);
				Map delMap2 = new HashMap();
				delMap2.put("customerids", customerIds);
				this.delete("deleteCustomers", delMap2);
			}
			if(!"".equals(subCustomerIds)){
				subCustomerIds = subCustomerIds.substring(0,subCustomerIds.length()-1);
				Map delMap3 = new HashMap();
				delMap3.put("customerids", subCustomerIds);
				this.delete("deleteSubCustomers", delMap3);
			}
			
			if(saveTransInfos != null && saveTransInfos.size() > 0){
				//提交数据到数据库
				this.insertBatch("saveTransInfos", saveTransInfos);
				//插入之前要先校验是否存在记录？
				//只有交易信息存在时才存储客户信息
				if(saveCustomers != null && saveCustomers.size() > 0){
					this.insertBatch("saveCustomers", saveCustomers);
				}
				//有可能当前交易没有子客户
				if(saveSubCustomers != null && saveSubCustomers.size() > 0){
					this.insertBatch("saveSubCustomers", saveSubCustomers);
				}
			}
			//有可能
		} catch (Exception e) {
			e.printStackTrace();
			message = "接口调用处理失败";
			return "1|"+message;
		}
		return "0|成功";
	}
/**
	 * 根据机构号，获取机构的纳税人识别号
	 * @param orgId
	 * @return
	 */
	private String getTaxNo(String orgId) {
		Map para = new HashMap();
		para.put("instCode", orgId);
		List result = find("findTaxNoByInstCode",para);
		if(result.size()<1) return "";
		Organization org = (Organization) result.get(0);
		return org.getTaxperNumber();
	}
	
	public LogManagerService getLogManagerService() {
		return logManagerService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}
}