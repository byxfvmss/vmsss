package com.cjit.vms.trans.action.storage.server;


import java.util.ArrayList;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.taxServer.action.BaseTaxSelverAction;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.storage.disk.PageTaxInvoiceService;
import com.cjit.vms.trans.service.storage.server.InvoiceManagementService;
import com.icss.pangu.util.expression.base.ParseException;

/**
 * @author tom
 * 发票管理
 */
public class InvoiceManagementServlerAction extends BaseTaxSelverAction {
	private PageTaxInvoiceService pageTaxInvoiceService;
	private InvoiceManagementService   invoiceManagementService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PaperAutoInvoice	paperAutoInvoice=new PaperAutoInvoice();
	private static final String fapiaoType_0="0";
	private static final String fapiaoType_1="1";
	
	//|成功|当前未开发票代码|当前未开发票号码|总剩余份数|发票代码^起始号码^终止号码^发票份数^剩余份数^领购日期^领购人员
	public void savePaperAutoInvoiceSelvet() throws Exception{
		String TaxKeyString=request.getParameter("TaxKeyString");
		String generalBillString=request.getParameter("generalBillString");
		String specialBillString=request.getParameter("specialBillString");
		String[] infos=TaxKeyString.split("\\|");
		String taxNo=infos[1];
		String keyNo=infos[2];
		String instId=pageTaxInvoiceService.getInstIdbyTaxNo(taxNo);
		if(StringUtil.IsEmptyStr(instId)){
			printWriterResult("纳税人识别号不存在请维护机构表数据库信息");
			return;
		}
		List listgeneralBill=invoiceManagementService.CreatePaperAutoInvoiceList(generalBillString,taxNo, keyNo,instId,fapiaoType_1);
		List listspecialBill=invoiceManagementService.CreatePaperAutoInvoiceList(specialBillString,taxNo, keyNo,instId,fapiaoType_0);
		listgeneralBill.addAll(listspecialBill);
		if(listgeneralBill!=null){
			for(int i=0;i<listgeneralBill.size();i++){
				PaperAutoInvoice paperAutoInvoice=(PaperAutoInvoice) listgeneralBill.get(i);
				List listr=pageTaxInvoiceService.findPaperAutoInvoiceDetial(paperAutoInvoice.getInvoiceType(),paperAutoInvoice.getTaxDiskNo(), paperAutoInvoice.getTaxpayerNo(), 
						paperAutoInvoice.getInvoiceCode(), paperAutoInvoice.getInvoiceBeginNo(),  paginationList);
				if(listr.size()==1){
					PaperAutoInvoice pa=(PaperAutoInvoice)listr.get(0);
					paperAutoInvoice.setAutoInvoiceId(pa.getAutoInvoiceId());
					pageTaxInvoiceService.updatepaperAutoInvoicebybusId(paperAutoInvoice);
					pageTaxInvoiceService.copyAutoInvoiceTostock();
					pageTaxInvoiceService.copyAutoInvoiceToStockDetail();
					pageTaxInvoiceService.updateSynStockDetial();
					
				}else{
					pageTaxInvoiceService.savePaperAutoInvoice(paperAutoInvoice);
					pageTaxInvoiceService.copyAutoInvoiceTostock();
					pageTaxInvoiceService.copyAutoInvoiceToStockDetail();
					pageTaxInvoiceService.updateSynStockDetial();
				}
			}
		}
		printWriterResult("保存成功");
	}
	public List CreatePaperAutoInvoiceList(String data,String taxNo,String keyNo,String instId,String fapiaoType) throws ParseException, java.text.ParseException{
		//1|1100111650|70330128|872|
		//1100111650^70330000^70330999^872^1000^20151014^11
	PaperAutoInvoice paperAutoInvoice=null;
		List list=null;
		
		if(StringUtil.isNotEmpty(data)){
			String[] infos=data.split("\\|");
			list=new ArrayList();
		for(int i=4;i<infos.length;i++)	{
			String datas=infos[i];
			paperAutoInvoice=new PaperAutoInvoice();
			paperAutoInvoice.setAutoInvoiceId(createBusinessId("P"));
			paperAutoInvoice.setTaxpayerNo(taxNo);
			paperAutoInvoice.setInstId(instId);
			paperAutoInvoice.setTaxDiskNo(keyNo);
			paperAutoInvoice.setInvoiceType(fapiaoType);
			paperAutoInvoice.setCurrentInvoiceCode(infos[1]);
			paperAutoInvoice.setCurrentInvoiceNo(infos[2]);
			//发票代码^起始号码^终止号码^发票份数^剩余份数^领购日期^领购人员
			//0			1		2			3		4		5		6
			if(StringUtil.isNotEmpty(datas)){
				String[] fapiaos=datas.split("\\^");
				paperAutoInvoice.setReceiveInvoiceTime(fapiaos[5]);
				paperAutoInvoice.setInvoiceCode(fapiaos[0]);
				paperAutoInvoice.setInvoiceBeginNo(fapiaos[1]);
				paperAutoInvoice.setInvoiceEndNo(fapiaos[2]);
				paperAutoInvoice.setInvoiceNum(fapiaos[3]);
				paperAutoInvoice.setSurplusNum(fapiaos[4]);
				paperAutoInvoice.setUserId(fapiaos[6]);
			}
			list.add(paperAutoInvoice);
			
			}
		}
		return list;
	}
	
	public PageTaxInvoiceService getPageTaxInvoiceService() {
		return pageTaxInvoiceService;
	}
	public void setPageTaxInvoiceService(PageTaxInvoiceService pageTaxInvoiceService) {
		this.pageTaxInvoiceService = pageTaxInvoiceService;
	}
	public PaperAutoInvoice getPaperAutoInvoice() {
		return paperAutoInvoice;
	}
	public void setPaperAutoInvoice(PaperAutoInvoice paperAutoInvoice) {
		this.paperAutoInvoice = paperAutoInvoice;
	}
	public InvoiceManagementService getInvoiceManagementService() {
		return invoiceManagementService;
	}
	public void setInvoiceManagementService(
			InvoiceManagementService invoiceManagementService) {
		this.invoiceManagementService = invoiceManagementService;
	}
	
}
