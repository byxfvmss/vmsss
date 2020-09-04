package com.cjit.vms.trans.service.storage.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.id.UUIDHexGenerator;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbDetail;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.service.storage.PaperInvoiceTrackService;

public class PaperInvoiceTrackServiceImpl extends  GenericServiceImpl implements PaperInvoiceTrackService {
	public List findPaperInvoiceDistributeByInstIds(PaperInvoiceDistribute invoiceDistribute,PaginationList paginationList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("invoiceDistribute", invoiceDistribute);
		List instIds=invoiceDistribute.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		return this.find("findPaperInvoiceDistributeByInstIds", map,paginationList); 
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

	public PaperInvoiceDistribute findPaperInvoiceDistributeByDistributeId(
			String distribute_id) {
		Map map = new HashMap();
		map.put("paper_invoice_distribute_id", distribute_id);
		return (PaperInvoiceDistribute) this.findForObject("findPaperInvoiceDistributeByDistributeId", map);
	}

	public int savePaperInvoiceRbHistory(PaperInvoiceRbHistory history) {
		try{
		List<PaperInvoiceRbHistory> list=new ArrayList<PaperInvoiceRbHistory>();
		String paper_invoice_distribute_id=history.getPaperInvoiceDistributeId();
		List<PaperInvoiceRbDetail> listDetil=new ArrayList<PaperInvoiceRbDetail>();
		List<PaperInvoiceRbDetail> listDetil1=new ArrayList<PaperInvoiceRbDetail>();
		PaperInvoiceDistribute paperInvoiceDistribute=findPaperInvoiceDistributeByDistributeId(paper_invoice_distribute_id);
		int beginNo=Integer.parseInt(paperInvoiceDistribute.getInvoiceBeginNo());
		int endNo =Integer.parseInt(paperInvoiceDistribute.getInvoiceEndNo());
		int curr_receive_num =Integer.parseInt(history.getReceive_num());
		int hasReceiveNum=Integer.parseInt(paperInvoiceDistribute.getHasReceiveNum());
		int l=paperInvoiceDistribute.getInvoiceBeginNo().length();
		int j=0;
		int startMiddle=0;
		int endMiddle=0;
		for(int i=beginNo;i<endNo+1;i++){
			List list1=findReREByBillCodeAndBillNo(paperInvoiceDistribute.getInvoiceCode(), Integer.toString(i));
			//详情表的拼接
			if(list1.size()==0&&startMiddle==0){
				startMiddle=i;
				PaperInvoiceRbDetail rbDetail = new PaperInvoiceRbDetail();
				rbDetail.setInvoiceCode(paperInvoiceDistribute.getInvoiceCode());
				rbDetail.setInvoiceNo(lpad(l, i));
				listDetil.add(rbDetail);
				//j++;
			//	continue;
			}
			if(list1.size()==0&&startMiddle!=0){
				endMiddle=i;
				if(startMiddle!=endMiddle){
				PaperInvoiceRbDetail rbDetail = new PaperInvoiceRbDetail();
				rbDetail.setInvoiceCode(paperInvoiceDistribute.getInvoiceCode());
				rbDetail.setInvoiceNo(lpad(l, i));
				listDetil.add(rbDetail);
				}
				//j++;
			}
			if(list1.size()==0){
				j++;
			}
			// 领用履历表的拼接
			if(list1.size()==1&&startMiddle!=0&&endNo!=0||j==Integer.parseInt(history.getReceive_num()) ){
				PaperInvoiceRbHistory pa=new PaperInvoiceRbHistory();
				String id=(String) new UUIDHexGenerator().generate(null, null);
				pa.setBalanceNum(Integer.toString(endMiddle-startMiddle+1));
				pa.setCreateInstId(history.getCreateInstId());
				pa.setCreateTime(history.getCreateTime());
				pa.setCreateUserId(history.getCreateUserId());
				pa.setInvoiceBeginNo(lpad(l,startMiddle));
				pa.setInvoiceCode(paperInvoiceDistribute.getInvoiceCode());
				pa.setInvoiceEndNo(lpad(l,endMiddle));
				pa.setOperatorFlag("0");
				pa.setPaperInvoiceDistributeId(paper_invoice_distribute_id);
				pa.setPaperInvoiceStockId(paperInvoiceDistribute.getPaperInvoiceStockId());
				pa.setReceive_num(Integer.toString(endNo-startMiddle+1));
				pa.setReceiveInstId(history.getReceiveInstId());
				pa.setReceiveUserId(history.getReceiveUserId());
				pa.setPaperInvoiceRbHistoryId(id);
				pa.setListDetil(listDetil);
				/*(#paperInvoiceRbHistoryId#,
				  #paperInvoiceStockId#,
				  #paperInvoiceDistributeId#,
				  #invoiceCode#,
			        #invoiceBeginNo#,
			         #invoiceEndNo#,
			        #receiveInstId#,
			         #receiveUserId#,
			        #operatorFlag#,
			        to_date(#createTime#,'yyyy-mm-dd
			        hh24:mi:ss'),
			        #createUserId#,#createInstId#,#balanceNum#)*/
				for(int m=0;m<listDetil.size();m++){
					PaperInvoiceRbDetail rbDetail = new PaperInvoiceRbDetail();
					rbDetail=listDetil.get(m);
				//	rbDetail.setInvoiceCode();
					rbDetail.setPaperInvoiceRbHistoryId(id);
					listDetil1.add(rbDetail);
				}
				
				list.add(pa);
				listDetil=new ArrayList<PaperInvoiceRbDetail>();
				startMiddle=0;endMiddle=0;
			}
			if(j==Integer.parseInt(history.getReceive_num())){
				break;
			}
			//beginNo=
			//pa.set
		}
		//int invoiceBeginNo=initBeginNo+hasReceiveNum;
		int Receive_num=Integer.parseInt(history.getReceive_num());
		//int invoiceEndNo=invoiceBeginNo+curr_receive_num-1;
		history.setBalanceNum(history.getReceive_num());
		/*history.setInvoiceBeginNo(invoiceBeginNo+"");
		history.setInvoiceEndNo(invoiceEndNo+"");
		history.setInvoiceCode(paperInvoiceDistribute.getInvoiceCode());
		history.setPaperInvoiceStockId(paperInvoiceDistribute.getPaperInvoiceStockId());
		
		Map map = new HashMap();
		map.put("history", history)*/; 
		saveReHisAndCurReInfo(list,listDetil1); 
			/*this.save("savePaperInvoiceRbHistory", map);
		String paper_invoice_rb_history_id=map.get("id")+""; savePaperInvoiceRbDetail*/
		
		
			Map uptMap = new HashMap();
			uptMap.put("curr_receive_num",curr_receive_num+"");
			uptMap.put("paper_invoice_distribute_id", history.getPaperInvoiceDistributeId());
			this.update("updatePaperInvoiceDistributeHasReceiveNum", uptMap);
			
/*			Map uptUserMap=new HashMap();
			uptUserMap.put("receiveInstId", history.getReceiveInstId());
			uptUserMap.put("receiveUserId", history.getReceiveUserId());
			uptUserMap.put("invoiceCode", history.getInvoiceCode());
			uptUserMap.put("invoiceBeginNo", history.getInvoiceBeginNo());
			uptUserMap.put("invoiceEndNo", history.getInvoiceEndNo());
			this.update("updatePaperInvoiceUseDetailReceiveInfo", uptUserMap);*/
			}
		catch(Exception e){
			e.printStackTrace();
		}
		return 1;
	}

	public void saveReHisAndCurReInfo(List<PaperInvoiceRbHistory> list,List<PaperInvoiceRbDetail> list1) throws SQLException{
		
		insertBatch("savePaperInvoiceRbHistory",list);
		insertBatch("savePaperInvoiceRbCurUse",list);
		insertBatch("savePaperInvoiceRbDetail", list1);
		
			
		}
	@Override
	public List findReREByBillCodeAndBillNo(String billCode, String billNo) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		return find("findReREByBillCodeAndBillNo",map);
	}
	public List findPaperInvoiceRbHistoryList(String paperInvoiceDistributeId,PaginationList paginationList) {
		Map map = new HashMap();
		map.put("paperInvoiceDistributeId", paperInvoiceDistributeId);
		return this.find("findPaperInvoiceRbHistoryList", map,paginationList);
	}
	public PaperInvoiceDistribute findPaperInvoiceDistributeByDistribute(
			String receiveUserId,String paperInvoiceDistributeId) {
		Map map = new HashMap();
		
		map.put("paperInvoiceDistributeId", paperInvoiceDistributeId);
		map.put("receiveUserId", receiveUserId);
		return (PaperInvoiceDistribute) this.findForObject("findStockHistoryBackInit", map);
	}
	public List  findReceiveUserByInvoiceRange(String invoiceCode,
			String invoiceBeginNo, String invoiceEndNo) {
		Map map = new HashMap();
		map.put("invoice_code", invoiceCode);
		map.put("invoice_begin_no", invoiceBeginNo);
		map.put("invoice_end_no", invoiceEndNo);
		return this.find("findReceiveUserByInvoiceRange", map);
	}
	public Long findMayBackNumByInvoiceRange(String invoiceCode,
			String invoiceBeginNo, String invoiceEndNo) {
		Map map = new HashMap();
		map.put("invoice_code", invoiceCode);
		map.put("invoice_begin_no", invoiceBeginNo);
		map.put("invoice_end_no", invoiceEndNo);
		return this.getRowCount("findMayBackNumByInvoiceRange", map);
	}
	public void savePaperInvoiceBack(PaperInvoiceRbHistory paperInvoiceRbHistory) {
		try{
		/*int curr_receive_num=Integer.parseInt(paperInvoiceRbHistory.getInvoiceEndNo())-Integer.parseInt(paperInvoiceRbHistory.getInvoiceBeginNo())+1;
		Map uptMap = new HashMap();
		uptMap.put("curr_receive_num","-"+curr_receive_num);
		uptMap.put("paper_invoice_distribute_id", paperInvoiceRbHistory.getPaperInvoiceDistributeId());
		//更改领用张数
		this.update("updatePaperInvoiceDistributeHasReceiveNum", uptMap);
		uptMap.put("paperInvoiceRbHistoryId", paperInvoiceRbHistory.getPaperInvoiceRbHistoryId());
		//this.update("updateStockhistoryBackBanaceNum",uptMap);
		
		List lstRbHistory=findStockHistoryBackInitbyId(paperInvoiceRbHistory.getPaperInvoiceRbHistoryId());
		PaperInvoiceRbHistory old=(PaperInvoiceRbHistory) lstRbHistory.get(0);
		//
		Map rbMap = new HashMap();
		paperInvoiceRbHistory.setInvoiceNum(curr_receive_num+"");
		paperInvoiceRbHistory.setReceiveInstId(old.getReceiveInstId());
		paperInvoiceRbHistory.setReceiveUserId(old.getReceiveUserId());
		rbMap.put("history", paperInvoiceRbHistory); 
		this.save("savePaperInvoiceRbHistory", rbMap);*/
		
		
		int j=0;
		//退换数量
		int returnNum=Integer.parseInt(paperInvoiceRbHistory.getReturnNum());
		//更改 当前表
		int startMiddle=0;
		int endMiddle=0;
		String billCode=paperInvoiceRbHistory.getInvoiceCode();
		List<PaperInvoiceRbHistory> list=new ArrayList<PaperInvoiceRbHistory>();
		List<PaperInvoiceRbHistory> listCurUpdate=new ArrayList<PaperInvoiceRbHistory>();
		List<PaperInvoiceRbHistory> listCurDelete=new ArrayList<PaperInvoiceRbHistory>();
		List<PaperInvoiceRbDetail> listDetail=new ArrayList<PaperInvoiceRbDetail>();
		List<PaperInvoiceRbDetail> listDetail1=new ArrayList<PaperInvoiceRbDetail>();
		List<PaperInvoiceRbHistory> listCur=findYUseInvoiceByUser(paperInvoiceRbHistory.getReceiveUserId());
		PaperInvoiceRbHistory  curPaper=null;
		PaperInvoiceRbHistory  p1=listCur.get(0);
		int l=paperInvoiceRbHistory.getInvoiceEndNo().length();
		for(int i=Integer.parseInt(paperInvoiceRbHistory.getInvoiceEndNo());i>=Integer.parseInt(paperInvoiceRbHistory.getInvoiceBeginNo());i--){
			String billNo=findYEmptyAndPrintBillNo(billCode,lpad(l,i),paperInvoiceRbHistory.getReceiveUserId());
			if(billNo!=null && endMiddle==0){
				endMiddle=i;
				PaperInvoiceRbDetail pa=new PaperInvoiceRbDetail();
				pa.setInvoiceCode(billCode);
				pa.setInvoiceNo(lpad(l,i));
				listDetail.add(pa);
			}
			if(billNo!=null){
				startMiddle=i;
				if(endMiddle!=startMiddle){
				PaperInvoiceRbDetail pa=new PaperInvoiceRbDetail();
				pa.setInvoiceCode(billCode);
				pa.setInvoiceNo(lpad(l,i));
				listDetail.add(pa);
				}
			}
			if(billNo!=null){
				j++;
			}
			if((billNo==null && endMiddle!=0&&startMiddle!=0)||j==returnNum){
				String id=(String) new UUIDHexGenerator().generate(null, null);
				PaperInvoiceRbHistory pa=new PaperInvoiceRbHistory();
				pa.setBalanceNum(Integer.toString(startMiddle-endMiddle));//返回的数量
				pa.setCreateInstId(paperInvoiceRbHistory.getCreateInstId());
				pa.setCreateTime(paperInvoiceRbHistory.getCreateTime());
				pa.setCreateUserId(paperInvoiceRbHistory.getCreateUserId());
				pa.setInstId(paperInvoiceRbHistory.getInstId());
				pa.setInvoiceBeginNo(lpad(l,startMiddle));
				pa.setInvoiceCode(billCode);
				pa.setInvoiceEndNo(lpad(l,endMiddle));
				for(int m=0;m<listDetail.size();m++){
					PaperInvoiceRbDetail pd=new PaperInvoiceRbDetail();
					pd=listDetail.get(m);
					pd.setPaperInvoiceRbHistoryId(id);
					listDetail1.add(pd);
				}
				pa.setOperatorFlag("1");
				pa.setPaperInvoiceDistributeId(paperInvoiceRbHistory.getPaperInvoiceDistributeId());
				pa.setPaperInvoiceRbHistoryId(id);
				pa.setPaperInvoiceStockId(paperInvoiceRbHistory.getPaperInvoiceStockId());
				pa.setReceiveInstId(paperInvoiceRbHistory.getReceiveInstId());
				pa.setReceiveUserId(paperInvoiceRbHistory.getReceiveUserId());
				list.add(pa);
				listDetail=new ArrayList<PaperInvoiceRbDetail>();
				endMiddle=0;
				startMiddle=0;
				
			}
			//更改当前领用表
			for(int c=0;c<listCur.size();c++){
				PaperInvoiceRbHistory p=new PaperInvoiceRbHistory();
				p=listCur.get(c);
				if(Integer.parseInt(p.getInvoiceBeginNo())<=i&&i<=Integer.parseInt(p.getInvoiceEndNo())){
					if(curPaper==null){
						curPaper=new PaperInvoiceRbHistory();
					// 余票不为零时 
					if(Integer.parseInt(p.getBalanceNum())>0){
						//当 退还时  余票减1 结束号码 为当前号码减1
						if(billNo!=null){
								curPaper.setInvoiceBeginNo(p.getInvoiceBeginNo());
								curPaper.setInvoiceEndNo(lpad(l,i-1));
								curPaper.setInvoiceCode(p.getInvoiceCode());
								curPaper.setBalanceNum(Integer.toString(Integer.parseInt(p.getBalanceNum())-1));
								 if(Integer.parseInt(curPaper.getBalanceNum())==0){
									 String cid=(String) new UUIDHexGenerator().generate(null, null);
									 curPaper.setPaperInvoiceRbHistoryId(cid);
									 curPaper.setBalanceNum("0");//返回的数量
									 curPaper.setCreateInstId(paperInvoiceRbHistory.getCreateInstId());
									 curPaper.setCreateTime(paperInvoiceRbHistory.getCreateTime());
									 curPaper.setCreateUserId(paperInvoiceRbHistory.getCreateUserId());
									 curPaper.setInstId(paperInvoiceRbHistory.getInstId());
									 curPaper.setInvoiceEndNo(Integer.toString(i));
									 curPaper.setInvoiceCode(billCode);
									 curPaper.setOperatorFlag("2");
									 curPaper.setPaperInvoiceDistributeId(paperInvoiceRbHistory.getPaperInvoiceDistributeId());
									 curPaper.setPaperInvoiceRbHistoryId(cid);
									 curPaper.setPaperInvoiceStockId(paperInvoiceRbHistory.getPaperInvoiceStockId());
									 curPaper.setReceiveInstId(paperInvoiceRbHistory.getReceiveInstId());
									 curPaper.setReceiveUserId(paperInvoiceRbHistory.getReceiveUserId());
									// list.add(curPaper); 已用完的添加
									 listCurDelete.add(curPaper);
									 curPaper=null;
								}
							}
						
							// 遇到不符合要求的 不做更改
							else{
								
							}
							//当 退票数够时
							if(returnNum==j){
								if(curPaper!=null){
								listCurUpdate.add(curPaper);
								}
							}
							//余票为零是
						}else{
							String cid=(String) new UUIDHexGenerator().generate(null, null);
							p.setPaperInvoiceRbHistoryId(cid);
							p.setBalanceNum("0");//返回的数量
							p.setCreateInstId(paperInvoiceRbHistory.getCreateInstId());
							p.setCreateTime(paperInvoiceRbHistory.getCreateTime());
							p.setCreateUserId(paperInvoiceRbHistory.getCreateUserId());
							p.setInstId(paperInvoiceRbHistory.getInstId());
							p.setInvoiceCode(billCode);
							p.setOperatorFlag("2");
							p.setPaperInvoiceDistributeId(paperInvoiceRbHistory.getPaperInvoiceDistributeId());
							p.setPaperInvoiceRbHistoryId(cid);
							p.setPaperInvoiceStockId(paperInvoiceRbHistory.getPaperInvoiceStockId());
							p.setReceiveInstId(paperInvoiceRbHistory.getReceiveInstId());
							p.setReceiveUserId(paperInvoiceRbHistory.getReceiveUserId());
							//list.add(p);//已用完的添加
							listCurDelete.add(p);
							curPaper=null;
						}
					// 第二次进入
					}else{
						//余票不为零时
						if(Integer.parseInt(curPaper.getBalanceNum())>0){
							//当 退还时  余票减1 结束号码 为当前号码减1
							if(billNo!=null){
									curPaper.setInvoiceEndNo(lpad(l,i-1));
									curPaper.setBalanceNum(Integer.toString(Integer.parseInt(curPaper.getBalanceNum())-1));
								}
							
								// 遇到不符合要求的 不做更改
								else{
									
								}
								//当 退票数够时
								if(returnNum==j){
									if(curPaper!=null){
									listCurUpdate.add(curPaper);
									}
								}
								//余票为零是
							}else if(Integer.parseInt(curPaper.getBalanceNum())==0){
								String cid=(String) new UUIDHexGenerator().generate(null, null);
								curPaper.setPaperInvoiceRbHistoryId(cid);
								p.setPaperInvoiceRbHistoryId(cid);
								p.setBalanceNum("0");//返回的数量
								p.setCreateInstId(paperInvoiceRbHistory.getCreateInstId());
								p.setCreateTime(paperInvoiceRbHistory.getCreateTime());
								p.setCreateUserId(paperInvoiceRbHistory.getCreateUserId());
								p.setInstId(paperInvoiceRbHistory.getInstId());
								p.setInvoiceEndNo(Integer.toString(i));

								p.setInvoiceCode(billCode);
								p.setOperatorFlag("2");
								p.setPaperInvoiceDistributeId(paperInvoiceRbHistory.getPaperInvoiceDistributeId());
								p.setPaperInvoiceRbHistoryId(cid);
								p.setPaperInvoiceStockId(paperInvoiceRbHistory.getPaperInvoiceStockId());
								p.setReceiveInstId(paperInvoiceRbHistory.getReceiveInstId());
								p.setReceiveUserId(paperInvoiceRbHistory.getReceiveUserId());
								
								//list.add(p);//已用完的添加
								listCurDelete.add(p);
								curPaper=null;
							}
					}
					
				}
				
			}
			

			if(j==returnNum){
				break;
			}
		}
		saveCurAndUpdaeUse(list, listDetail1, listCurUpdate, listCurDelete);
		//更改分发的数量
		Map uptMap = new HashMap();
		uptMap.put("curr_receive_num","-"+paperInvoiceRbHistory.getReturnNum());
		uptMap.put("paper_invoice_distribute_id", paperInvoiceRbHistory.getPaperInvoiceDistributeId());
		//更改领用张数
		this.update("updatePaperInvoiceDistributeHasReceiveNum", uptMap);
		
		//更改 当前履历表
		//更改详情表
		
		/*Map map = new HashMap();
		map.put("invoiceCode", paperInvoiceRbHistory.getInvoiceCode());
		map.put("invoiceBeginNo", paperInvoiceRbHistory.getInvoiceBeginNo());
		map.put("invoiceEndNo", paperInvoiceRbHistory.getInvoiceEndNo());
		this.delete("deletePaperInvoiceRbDetail", map);
		*/
	
		
		/*Map uptUserMap=new HashMap();
		uptUserMap.put("receiveInstId",null);
		uptUserMap.put("receiveUserId", null);
		uptUserMap.put("invoiceCode", paperInvoiceRbHistory.getInvoiceCode());
		uptUserMap.put("invoiceBeginNo", paperInvoiceRbHistory.getInvoiceBeginNo());
		uptUserMap.put("invoiceEndNo", paperInvoiceRbHistory.getInvoiceEndNo());
		this.update("updatePaperInvoiceUseDetailReceiveInfo", uptUserMap);*/
		}catch (Exception e) {
			e.printStackTrace();
}
		
	}
	// 批量删除 详 请表 --  履历表的添加 -- 当前表的更改! --当前表的删除-》履历表 中
	public void saveCurAndUpdaeUse(List<PaperInvoiceRbHistory> list,List<PaperInvoiceRbDetail> listDetial,
			List<PaperInvoiceRbHistory> ListCurUpdate,List<PaperInvoiceRbHistory> listCurDelete){
		// 履历表的添加
		if(list.size()>0&& list!=null){
			insertBatch("savePaperInvoiceRbHistory",list);
		}
		//详情表的删除
		if(listDetial.size()>0&&listDetial!=null){
			this.deleteBatch("deleteBatchInvoiceReDetial",listDetial);
		}
		//当前表的更改
		if(ListCurUpdate.size()>0 && ListCurUpdate!=null){
			this.updateRptDataBatch("updateBatchInvoiceReCur", ListCurUpdate);
		}
		//当前表的删除
		if(listCurDelete.size()>0 && listCurDelete!=null){
			this.deleteBatch("deleteBatchInvoiceReCur", listCurDelete);
			
		}
		
	}
	@Override
	public List findYUseInvoiceByUser(String receiveUserId) {
		Map map=new HashMap();
		map.put("receiveUserId", receiveUserId);
		
		return find("findYUseInvoiceByUser", map);
	}
	@Override
	public String findYEmptyAndPrintBillNo(String billCode, String billNo,String receiveUserId) {
		Map map=new HashMap();
		map.put("billCode", billCode);
		map.put("billNo", billNo);
		map.put("receiveUserId", receiveUserId);
		List list=find("findYEmptyAndPrintBillNo", map);
		if(list.size()==1){
			return (String)list.get(0);
		}
		return null;
	}

}
