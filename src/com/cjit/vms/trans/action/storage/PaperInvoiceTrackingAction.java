package com.cjit.vms.trans.action.storage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cjit.crms.util.json.JsonUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.storage.PaperInvoiceDistribute;
import com.cjit.vms.trans.model.storage.PaperInvoiceRbHistory;
import com.cjit.vms.trans.service.storage.PaperInvoiceTrackService;

/**
 * @author tom
 * 发票跟踪
 */
public class PaperInvoiceTrackingAction extends DataDealAction{
	
	private PaperInvoiceDistribute invoiceDistribute=new PaperInvoiceDistribute();
	/**
	 * @Action
	 * 
	 * 纸质发票分发明细页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public  String listDistrubute(){
		try{
			if ("menu".equalsIgnoreCase(fromFlag)) {
				invoiceDistribute = new PaperInvoiceDistribute();
				fromFlag = null;
			}
			List lstAuthInstId=new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			invoiceDistribute.setLstAuthInstId(lstAuthInstId);
			
			List lstInvoiceDistribute=paperInvoiceTrackService.findPaperInvoiceDistributeByInstIds(invoiceDistribute,paginationList);
			this.request.setAttribute("paperInfoList", lstInvoiceDistribute);
		} catch (Exception e) {
			e.printStackTrace();
//			logManagerService.writeLog(request, this.getCurrentUser(),
//					"0001.0010", "查询开票", "分发纸质开票", "对纸质发票库存ID为(" + billId
//							+ ")的票据进行撤销处理", "0");
			log.error("PaperInvoiceAction-listDistrubute", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * @Action
	 * 
	 * 纸质发票领取页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public  String initReceiveDistribute(){
		try{
			paperInvoiceDistribute=paperInvoiceTrackService.findPaperInvoiceDistributeByDistributeId(paper_invoice_distribute_id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listDistrubute", e);
			return ERROR;
		}
		return SUCCESS;
	}
	/**
	 * @Action
	 * 
	 * 纸质发票领取页面处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String doReceiveDistribute(){
		try{
			String currentTime=StringUtils.substringBefore(DateUtils.serverCurrentDateTime(), ".");
			paperInvoiceRbHistory.setPaperInvoiceDistributeId(paper_invoice_distribute_id);
			paperInvoiceRbHistory.setOperatorFlag("0");
			paperInvoiceRbHistory.setCreateTime(currentTime);
			paperInvoiceRbHistory.setCreateInstId(getCurrentUser().getOrgId());
			paperInvoiceRbHistory.setCreateUserId(getCurrentUser().getId());
			paperInvoiceTrackService.savePaperInvoiceRbHistory(paperInvoiceRbHistory);
			return SUCCESS;
		} catch (Exception e) {
			
			String path = request.getContextPath();
			String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
			try {
				response.sendRedirect(webapp+"/initReceiveDistribute.action?paper_invoice_distribute_id="+paper_invoice_distribute_id+"&retry=true");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return ERROR;
	}
	/**
	 * @Action
	 * 
	 * 纸质发票领取退货履历明细
	 * 
	 * @return
	 */
	public  String listPaperInvoiceRbHistory(){
		try{
			String paper_invoice_distribute_id = request.getParameter("paper_invoice_distribute_id");
			this.setPaperInvoiceDistributeId(paper_invoice_distribute_id);
			List lstInvoiceRbHistory=paperInvoiceTrackService.findPaperInvoiceRbHistoryList(paper_invoice_distribute_id,paginationList);
			this.request.setAttribute("paperInfoList", lstInvoiceRbHistory);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("PaperInvoiceAction-listPaperInvoiceRbHistory", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * @Action
	 * 
	 * 纸质发票退还页面初始化
	 * 
	 * @author jobell
	 * @return
	 */
	public  String initBackDistribute(){
		paperInvoiceDistribute=paperInvoiceTrackService.findPaperInvoiceDistributeByDistribute("",paper_invoice_distribute_id);
		return SUCCESS;
	}
	public void checkNumByReceverName() throws Exception{
		String people=request.getParameter("people");
		paperInvoiceDistributeId=request.getParameter("paperInvoiceDistributeId");
		paperInvoiceDistribute=paperInvoiceTrackService.findPaperInvoiceDistributeByDistribute(people,paperInvoiceDistributeId);
		String json=JsonUtil.toJsonString(paperInvoiceDistribute);
		//System.out.println(json);
		printWriterResult(json);
	}
	public String checkBackReceiver(){
		JSONObject jsonObject=new JSONObject();
		String invoiceCode=paperInvoiceRbHistory.getInvoiceCode();
		String invoiceBeginNo=paperInvoiceRbHistory.getInvoiceBeginNo();
		String invoiceEndNo=paperInvoiceRbHistory.getInvoiceEndNo();
		try{
			List lstUsers=paperInvoiceTrackService.findReceiveUserByInvoiceRange(invoiceCode, invoiceBeginNo, invoiceEndNo);
			Long mayBackNum=paperInvoiceTrackService.findMayBackNumByInvoiceRange(invoiceCode, invoiceBeginNo, invoiceEndNo);
			jsonObject.put("msg","ok");
			jsonObject.put("count",lstUsers.size()+"");
			jsonObject.put("mayBackNum", mayBackNum+"");
			if(lstUsers.size()==1){
				PaperInvoiceRbHistory rb=(PaperInvoiceRbHistory) lstUsers.get(0);
				rb.setReceiveInstId(URLEncoder.encode(rb.getReceiveInstId(),"utf8"));
				rb.setReceiveUserId(URLEncoder.encode(rb.getReceiveUserId(),"utf8"));
				jsonObject.put("data", rb);
			}
		}catch(Exception e){
			jsonObject.put("msg","ng");
		}
		try {
			response.getWriter().write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * @Action
	 * 
	 * 纸质发票退还处理
	 * 
	 * @author jobell
	 * @return
	 */
	public  String doBackDistribute(){
		String paper_invoice_distribute_id=null;
		try{
			String currentTime=StringUtils.substringBefore(DateUtils.serverCurrentDateTime(), ".");
			paper_invoice_distribute_id=paperInvoiceRbHistory.getPaperInvoiceDistributeId();
			paperInvoiceRbHistory.setOperatorFlag("1");
			paperInvoiceRbHistory.setCreateTime(currentTime);
			paperInvoiceRbHistory.setCreateInstId(getCurrentUser().getOrgId());
			paperInvoiceRbHistory.setCreateUserId(getCurrentUser().getId());
			paperInvoiceTrackService.savePaperInvoiceBack(paperInvoiceRbHistory);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			String path = request.getContextPath();
			String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
			try {
				response.sendRedirect(webapp+"/initBackDistribute.action?paper_invoice_distribute_id="+paper_invoice_distribute_id+"&retry=true");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return ERROR;
	}
	
	private PaperInvoiceTrackService paperInvoiceTrackService;
	private PaperInvoiceDistribute paperInvoiceDistribute;
	private PaperInvoiceRbHistory paperInvoiceRbHistory=new PaperInvoiceRbHistory();
	private String paper_invoice_distribute_id;
	
	/** 发票领用退还翻页参数*/
	private String paperInvoiceDistributeId;

	public String getPaperInvoiceDistributeId() {
		return paperInvoiceDistributeId;
	}

	public void setPaperInvoiceDistributeId(String paperInvoiceDistributeId) {
		this.paperInvoiceDistributeId = paperInvoiceDistributeId;
	}
	public PaperInvoiceDistribute getPaperInvoiceDistribute() {
		return paperInvoiceDistribute;
	}
	public void setPaperInvoiceDistribute(
			PaperInvoiceDistribute paperInvoiceDistribute) {
		this.paperInvoiceDistribute = paperInvoiceDistribute;
	}
	public PaperInvoiceDistribute getInvoiceDistribute() {
		return invoiceDistribute;
	}
	public void setInvoiceDistribute(PaperInvoiceDistribute invoiceDistribute) {
		this.invoiceDistribute = invoiceDistribute;
	}
	public PaperInvoiceTrackService getPaperInvoiceTrackService() {
		return paperInvoiceTrackService;
	}
	public void setPaperInvoiceTrackService(
			PaperInvoiceTrackService paperInvoiceTrackService) {
		this.paperInvoiceTrackService = paperInvoiceTrackService;
	}
	public String getPaper_invoice_distribute_id() {
		return paper_invoice_distribute_id;
	}
	public void setPaper_invoice_distribute_id(String paperInvoiceDistributeId) {
		paper_invoice_distribute_id = paperInvoiceDistributeId;
	}

	public PaperInvoiceRbHistory getPaperInvoiceRbHistory() {
		return paperInvoiceRbHistory;
	}

	public void setPaperInvoiceRbHistory(PaperInvoiceRbHistory paperInvoiceRbHistory) {
		this.paperInvoiceRbHistory = paperInvoiceRbHistory;
	}
	 private void printWriterResult(String result) throws Exception {
	        response.setHeader("Content-Type", "text/xml; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        out.print(result);
	        out.close();
	    }
	
 } 