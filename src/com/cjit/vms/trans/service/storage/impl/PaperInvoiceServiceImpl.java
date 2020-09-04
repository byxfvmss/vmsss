package com.cjit.vms.trans.service.storage.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.timerApp.hexinInf;
import com.cjit.vms.trans.model.PaperInvoiceStock;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceListInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbDetail;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.model.storage.PaperInvoiceStockDetail;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.storage.PaperInvoiceService;
import com.cjit.vms.trans.util.SqlUtil;
import java.sql.SQLException;
import org.apache.struts2.views.jsp.ui.DivTag;
import org.hibernate.id.UUIDHexGenerator;

import sun.nio.cs.HistoricallyNamedCharset;

import cjit.crms.util.StringUtil;
import cjit.crms.util.json.JsonUtil;

import com.cjit.vms.trans.util.DataUtil;

public class PaperInvoiceServiceImpl  extends GenericServiceImpl  implements PaperInvoiceService {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public List findPaperInvoiceStoreByStoreIds(String[] storeIds) {
		Map map = new HashMap();
		map.put("paper_invoice_stock_ids", SqlUtil.arr2String(storeIds, ",")); 
		return this.find("findPaperInvoiceStoreByStoreIds", map); 
	}
	/**
	 * @param length 长度
	 * @param number  
	 * @return
	 */
	private String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
  }

	public int savePaperInvoiceDistribute(List lstDistribute) {
		int succ_item=0;
		try{
			Map numBeginCache=new HashMap();//用来维护一本发票的未分发起始NO key=store_id+"_"+invoice_code
			Map numDistributeCache = new HashMap();//用来维护本次批量分发的条目 key=store_id+"_"+invoice_code
			Map storeUnDistributeCache = new HashMap();//用来维护库存表中未分发的发票数 key=store_id
			Map storeCurrDistributeCache = new HashMap();
			
			for(int i=0;i<lstDistribute.size();i++){
				PaperInvoiceDistribute distribute=(PaperInvoiceDistribute) lstDistribute.get(i);
				String store_id=distribute.getPaperInvoiceStockId();
				if(!storeUnDistributeCache.containsKey(store_id)){
//					continue;
//				}else{
					Map params=new HashMap();
					params.put("paper_invoice_stock_id",store_id);
					Long unDistributeNum=(Long)this.findForObject("findPaperInvoiceStoreUnDistributeNumByStoreId", params);
					storeUnDistributeCache.put(store_id, unDistributeNum);
				}
				int f=0;
				String key=distribute.getPaperInvoiceStockId()+"_"+distribute.getInvoiceCode();
				if(!numBeginCache.containsKey(key)){//第一次进来的时候从数据库中获取未分发起始No
					Map params=new HashMap();
					params.put("paper_invoice_stock_id", distribute.getPaperInvoiceStockId());
					params.put("invoice_code", distribute.getInvoiceCode());
					PaperInvoiceStockDetail old_detail=(PaperInvoiceStockDetail) this.findForObject("findPaperInvoiceStoreByStoreIdAndInvoiceCode", params);
					int beginNo=Integer.parseInt(old_detail.getInvoiceBeginNo())+Integer.parseInt(old_detail.getHasDistributeNum());
					f=old_detail.getInvoiceBeginNo().length();
					numBeginCache.put(key, new Integer(beginNo));
				}
				
				if(!numDistributeCache.containsKey(key)){
					numDistributeCache.put(key, new Integer(distribute.getDistributeNum()));
				}else{
					int hasDistributeNum=((Integer)numDistributeCache.get(key)).intValue()+Integer.parseInt(distribute.getDistributeNum());
					numDistributeCache.put(key, new Integer(hasDistributeNum));
				}
				
				/*给分发表插入记录*/
				int d_begin_no=((Integer)numBeginCache.get(key)).intValue();
				int d_end_no=d_begin_no+Integer.parseInt(distribute.getDistributeNum());
	//			distribute.setPaperInvoiceDistributeId(this.getDao().getSeqNextNo("paper_invoice_distribute_seq")+"");//获取分发ID
				distribute.setInvoiceBeginNo(lpad(f,d_begin_no)+"");
				distribute.setInvoiceEndNo(lpad(f,d_end_no-1)+"");
				// 统计新增数量
				// 
				long cancelNum= findYBancelInvoiceNum(d_begin_no, d_end_no, distribute.getInvoiceCode());
				long printnum=findYPrintInvoiceNum(d_begin_no, d_end_no, distribute.getInvoiceCode());
				distribute.setBalanceNum(Long.toString(d_end_no-d_begin_no-printnum-cancelNum));
				
				Map map = new HashMap();
				map.put("distribute", distribute);
				int result= this.save("savePaperInvoiceDistribute", map);
				distribute.setPaperInvoiceDistributeId(map.get("id")+"");
				if(result<1){
					return -1;
				}else{
					/*给使用明细表插入数据
					for(int dIdx=d_begin_no;dIdx<d_end_no;dIdx++){
						Map mapDetail = new HashMap();
						PaperInvoiceUseDetail detail=new PaperInvoiceUseDetail();
	//					detail.setPaperInvoiceId(this.getDao().getSeqNextNo("PAPER_INVOICE_SEQ")+"");
						detail.setPaperInvoiceStockId(distribute.getPaperInvoiceStockId());
						detail.setPaperInvoiceDistributeId(distribute.getPaperInvoiceDistributeId());
						detail.setInvoiceCode(distribute.getInvoiceCode());
						detail.setInvoiceNo(dIdx+"");
						detail.setReceiveStatus("0");
						detail.setInvoiceStatus("0");
						mapDetail.put("detail", detail);
						int d_result=this.save("savePaperInvoiceUseDetail", mapDetail);
						if(d_result<1){
							return -1;
						}
					}*/
					numBeginCache.put(key, new Integer(d_end_no));
				}
				succ_item=succ_item+result;
			}
			
			
			/*更新库存明细表中的已分发数量*/
			Set keys=numDistributeCache.keySet();
			Iterator ite=keys.iterator();
			while(ite.hasNext()){
				String invoice_key=(String)ite.next();
				Integer curr_distribute_num=(Integer)numDistributeCache.get(invoice_key);
				String[] key_arr=StringUtils.split(invoice_key, "_");
				Map upd = new HashMap();
				upd.put("curr_distribute_num", curr_distribute_num);
				upd.put("paper_invoice_stock_id", key_arr[0]);
				upd.put("invoice_code", key_arr[1]);
				this.update("updatePaperInvoiceStoreHasDistributeNum", upd);
				
				if(storeCurrDistributeCache.containsKey(key_arr[0])){
					Long tempNum=(Long)storeCurrDistributeCache.get(key_arr[0]);
					storeCurrDistributeCache.put(key_arr[0], new Long(tempNum.longValue()+curr_distribute_num.intValue()));
				}else{
					storeCurrDistributeCache.put(key_arr[0], new Long(curr_distribute_num.intValue()));
				}
			}
			
			/*更新纸质发票库存表分发状态*/
			
			Set currKeySet=storeCurrDistributeCache.keySet();
			Iterator iteCurr=currKeySet.iterator();
			while(iteCurr.hasNext()){
				String distribute_flag="0";//0:否 1：是 2：部分分发
				String pi_store_id=(String)iteCurr.next();
				Long curr_num=(Long) storeCurrDistributeCache.get(pi_store_id);
				Long un_d_num=(Long) storeUnDistributeCache.get(pi_store_id);
				if(curr_num.longValue() < un_d_num.longValue()){
					distribute_flag="2";
				}
				if(curr_num.longValue() >= un_d_num.longValue()){//其实只会等于
					distribute_flag="1";
				}
				HashMap udfMap = new HashMap();
				udfMap.put("distribute_flag",distribute_flag);
				udfMap.put("paper_invoice_stock_id", pi_store_id);
				this.update("updatePaperInvoiceStoreDistributeFlag", udfMap);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return succ_item;
	}







	/**
	 * 纸质发票一览初期化表示
	 * 
	 * @param PaperInfo
	 * @param paginationList
	 * @return
	 * @author cylenve
	 */
	public List getPaperInvoiceListInfo(PaperInvoiceListInfo PaperInfo, PaginationList paginationList) {
		Map map = new HashMap();
		
		// 机构ID	
		map.put("instId", PaperInfo.getInstId());
		// 领购日期	
		if (PaperInfo.getReceiveInvoiceTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceTime());
			map.put("receiveInvoiceTime", receiveDate + " 00:00:00");
		}
		if (PaperInfo.getReceiveInvoiceEndTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceEndTime());
			map.put("receiveInvoiceEndTime", receiveDate+ " 23:59:59");
		}
		// 领购人员
		map.put("userId", PaperInfo.getUserId());
		// 发票代码
		map.put("invoiceCode", PaperInfo.getInvoiceCode());
		// 发票类型
		map.put("invoiceType", PaperInfo.getInvoiceType());
		map.put("taxpayerNo", PaperInfo.getTaxpayerNo());
		List instIds=PaperInfo.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		if (paginationList == null){
			return find("getPaperInvoiceListInfo", map);
		}
		return findLargeData("getPaperInvoiceListInfo", map, paginationList);
	}
	
	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet2 【发票使用情况】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet2(PaperInvoiceListInfo PaperInfo) {
		Map map = new HashMap();
		
		// 机构ID	
		map.put("instId", PaperInfo.getInstId());
		if (PaperInfo.getReceiveInvoiceTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceTime());
			map.put("receiveInvoiceTime", receiveDate + " 00:00:00");
		}
		if (PaperInfo.getReceiveInvoiceEndTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceEndTime());
			map.put("receiveInvoiceEndTime", receiveDate + " 23:59:59");
		}
		// 领购人员
		map.put("userId", PaperInfo.getUserId());
		// 发票类型
		map.put("invoiceType", PaperInfo.getInvoiceType());
		
		return find("exportPaperInvoiceUserInfoSheet2", map);
	}
	
	/**
	 * 纸质发票管理一览用 excel帐票出力 sheet3【发票领用与归还统计】
	 * 
	 * @param PaperInfo
	 * @return
	 * @author cylenve
	 */
	public List exportPaperInvoiceUserInfoSheet3(PaperInvoiceListInfo PaperInfo) {
		Map map = new HashMap();
		
		// 机构ID	
		map.put("instId", PaperInfo.getInstId());
		// 领购日期	
		if (PaperInfo.getReceiveInvoiceTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceTime());
			map.put("receiveInvoiceTime", receiveDate + " 00:00:00");
		}
		if (PaperInfo.getReceiveInvoiceEndTime() != null){
			String receiveDate = DATE_FORMAT.format(PaperInfo.getReceiveInvoiceEndTime());
			map.put("receiveInvoiceEndTime", receiveDate + " 23:59:59");
		}
		// 领购人员
		map.put("userId", PaperInfo.getUserId());
		// 发票类型
		map.put("invoiceType", PaperInfo.getInvoiceType());
		
		return find("exportPaperInvoiceUserInfoSheet3", map);
	}
	
	
	
	/**
	 * 纸质发票使用明细件数的取得
	 * 
	 * @param invoiceCode 发票代码
	 * @param invoiceNo 发票号码
	 * @return
	 * @author cylenve
	 */
	public Long getPaperInvoiceUseDetailCnt(String invoiceCode, String invoiceNo) {
		Map map = new HashMap();
		// 发票代码
		map.put("invoiceCode", invoiceCode);
		// 发票号码
		map.put("invoiceNo", invoiceNo);
		return getRowCount("getPaperInvoiceUseDetailCnt", map);
	}
	
	/**
	 * 空白作废发票信息的更新
	 * 
	 * @param invoiceCode 发票代码
	 * @param invoiceNo 发票号码
	 * @param invalidReason 作废原因
	 * @return
	 * @author cylenve
	 */
	public void updateInvalidPaperInvoiceUseDetail(String invoiceCode, String invoiceNo, String invalidReason) {
		Map map = new HashMap();
		
		// 发票代码
		map.put("invoiceCode", invoiceCode);
		// 发票号码
		map.put("invoiceNo", invoiceNo);
		// 作废原因
		map.put("invalidReason", invalidReason);
		
		update("updateInvalidPaperInvoiceUseDetail", map);
	}

	public int savePaperInvoiceStock(String operType,List lstStock,String receiveInvoiceTime) {
		int result_flag = 0;
		try{
		PaperInvoiceStock stock = (PaperInvoiceStock)lstStock.get(0);
		Map stockMap = new HashMap();
		stockMap.put("stock", stock); 
		if(operType.equals("edit")){
			this.update("updatePaperInvoiceStork", stockMap);
			this.delete("deletePaperInvoiceStorkDetail", stockMap);
			this.delete("deletePaperInvoiceUseDetail", stockMap);
		}else{
			int add_result=this.save("savePaperInvoiceStock", stockMap);
			if(add_result<1){
				return -1;
			}
		}
		
		PaperInvoiceStockDetail stockDetail = new PaperInvoiceStockDetail();
		Map stockDetailMap = new HashMap();
		for(int i=1;i<=lstStock.size()-1;i++){
			stockDetail = (PaperInvoiceStockDetail)lstStock.get(i);			
			if(operType.equals("edit")){
				stockDetail.setPaperInvoiceStockId(stock.getPaperInvoiceStockId());
			}else{
				stockDetail.setPaperInvoiceStockId(stockMap.get("id")+"");
			}
			stockDetailMap.put("stockDetail", stockDetail); 
			int stockdetail_result=this.save("savePaperInvoiceStockDetail", stockDetailMap);
			if(stockdetail_result<1){
				return -1;
			}
		    int beginNo=Integer.parseInt(stockDetail.getInvoiceBeginNo());
		    int endNo=Integer.parseInt(stockDetail.getInvoiceEndNo());	    
			//添加发票使用详情
			for(int dIdx=beginNo;dIdx<=endNo;dIdx++){
				Map mapDetail = new HashMap();
				PaperInvoiceUseDetail detail=new PaperInvoiceUseDetail();
				detail.setPaperInvoiceStockId(stockDetail.getPaperInvoiceStockId());
				detail.setInvoiceCode(stockDetail.getInvoiceCode());
				detail.setInvoiceNo(dIdx+"");
				detail.setReceiveStatus("0");
				detail.setInvoiceStatus("0");
				detail.setReceiveInstId(stock.getInstId());
				detail.setReceiveUserId(stock.getCreateUserId());
				detail.setInvoiceType(stock.getInvoiceType());	
				detail.setReceiveInvoiceTime(receiveInvoiceTime);	
				mapDetail.put("detail", detail);
				int d_result=this.save("savePaperInvoiceUseDetail", mapDetail);
				if(d_result<1){
					return -1;
				}
			}
		}
		}catch(Exception e){
			throw new RuntimeException(e);
		}		
		return result_flag;
	}
 
	public Long CountPaperInvoiceCode(String stockId, String invoiceCode) {
		//int result=0;
		Map map = new HashMap();
		map.put("invoiceCode", invoiceCode);
		map.put("stockId", stockId);
		return this.getRowCount("countPaperInvoiceCode", map);
	}

	
	public PaperInvoiceStock getPaperInvoiceStock(String invoiceStockId) {
		Map map = new HashMap();
		map.put("invoiceStockId", invoiceStockId);
		return (PaperInvoiceStock) this.findForObject("findPaperInvoiceStock", map);
	}
	
	public List getPaperInvoiceStockDetail(String invoiceStockId) {
		Map map = new HashMap();
		map.put("invoiceStockId", invoiceStockId);
		return this.find("findPaperInvoiceStockDetail", map);
	}
	
	@Override
	public long findYBancelInvoiceNum(int beginNo, int endNo,String billCode) {
		Map map=new HashMap();
		map.put("beginNo", beginNo);
		map.put("endNo",endNo );
		map.put("billCode", billCode);
		
		return getRowCount("findYBancelInvoiceNum",map);
	}

	@Override
	public long findYPrintInvoiceNum(int beginNo, int endNo,String billCode) {
		Map map=new HashMap();
		map.put("beginNo", beginNo);
		map.put("endNo",endNo );
		map.put("billCode", billCode);
		return getRowCount("findYPrintInvoiceNum",map);
	}


	
	@Override
	public void  deletePaperInvoiceStock(String paperInvoiceStockId,String invoiceCode ) {
		int result_flag = 0;
		Map paperInvoice = new HashMap();
		paperInvoice.put("paperInvoiceStockId", paperInvoiceStockId);
		paperInvoice.put("invoiceCode", invoiceCode);	
		Map map = new HashMap();
		map.put("paperInvoiceStockId", paperInvoiceStockId);
	    int  i	=getRowCount("findPaperInvoiceStockDetailCount",map).intValue();
	    if(i==1) {
	    	delete("deletePaperInvoiceStork",map);  
	    	delete("deleteAllInvoiceUseDetail",map);	    	
	    }
		delete("deleteLocalInvoiceStorkDetail", paperInvoice);    
		delete("deleteInvoiceUseDetailByCode", paperInvoice);
	}
	@Override
	public String getStockPaperInvoiceStatus(String invoiceCode, String invoiceNo) {
		Map map=new HashMap();
		map.put("invoiceCode", invoiceCode);
		map.put("invoiceNo", invoiceNo);
		List listResult = new ArrayList();	
		String stock="";
		//查询发票信息
		List<PaperInvoiceUseDetail> invoiceUseDetail =find("getStockPaperInvoiceStatus",map);
		 if(invoiceUseDetail.size()==0) {
		    	listResult.add(stock);
			 return JsonUtil.toJsonString(listResult);
		 }
		 listResult.add(invoiceUseDetail.get(0).getPaperInvoiceStockId());	
		 listResult.add(invoiceUseDetail.get(0).getInvoiceStatus());		
		return JsonUtil.toJsonString(listResult);
	}
	@Override
	public void updateStockPaperInvoiceNum(String invoiceCode, String invoiceNo) {
				
	}

	@Override
	public  List<String>  iFInvoiceExists(String invoiceCode, String invoiceBeginNo, String invoiceEndNo) {
	  Map<String,String> map=new HashMap(); 
	  map.put("invoiceCode", invoiceCode);
	  map.put("invoiceBeginNo", invoiceBeginNo);
	  map.put("invoiceEndNo",invoiceEndNo);
	  List listResult = new ArrayList();		 
	  return  this.find("ifInvoiceExists", map);
	}

	@Override
	public List<PaperInvoiceStockDetail> invoiceExistsByIdAndCode(String invoiceCode, String paperInvoiceStockId) {
		Map <String,String>map =new HashMap();
		map.put("invoiceCode", invoiceCode);
		map.put("paperInvoiceStockId", paperInvoiceStockId);	
		return (List<PaperInvoiceStockDetail>) this.find("invoiceExistsByIdAndCode", map);
	}

	@Override
	public PaperInvoiceUseDetail invoiceNoIfExists(String invoiceCode, String invoiceNo) {
		Map map=new HashMap();
		map.put("invoiceCode", invoiceCode);
		map.put("invoiceNo", invoiceNo);
		List<String> listResult = new ArrayList();	
		String stock="";
		//查询发票信息
		List<PaperInvoiceUseDetail> invoiceUseDetail =(List<PaperInvoiceUseDetail>) find("getStockPaperInvoiceStatus",map);	
		if(invoiceUseDetail.size()==0) {
			return null;
		}
		return invoiceUseDetail.get(0);
	}
	
}
