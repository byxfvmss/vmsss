package com.cjit.vms.metlife.action;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:收费凭证  metlife
*/
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cjit.vms.metlife.model.ChargesVoucherInfo;
import com.cjit.vms.metlife.service.ChargesManageService;
import com.cjit.vms.trans.action.DataDealAction;

import com.cjit.vms.trans.model.InstInfo;

import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.util.JXLTool;

public class ChargesManageAction extends DataDealAction{
	private ChargesManageService chargesManageService;
	private ChargesVoucherInfo chargesVoucherInfo=new ChargesVoucherInfo();
	private List authInstList = new ArrayList();
	private TaxDiskInfoService taxDiskInfoService;
	protected Map chanNelList;
	
	//收费凭证初始化&查询
	public String chargesVoucherDetial(){
		try{
			if("menu".equalsIgnoreCase(fromFlag)){
				fromFlag=null;
				chargesVoucherInfo=new ChargesVoucherInfo();
			}
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
		    chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		    chargesManageService.findChargesVoucher(chargesVoucherInfo,paginationList);
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
			
		return SUCCESS;
	}
	//收费凭证导出
	public void chargesVoucherToExcel(){
	try{	
		List chargesVoucherList=chargesManageService.findChargesVoucher(chargesVoucherInfo,paginationList);
		StringBuffer fileName = null;
		fileName = new StringBuffer("收费凭证");
		
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8")
						.toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, chargesVoucherList);
		os.flush();
		os.close();
	}catch(Exception e){
		e.printStackTrace();
		
	}
	}
	public void writeToExcel(OutputStream os, List chargesVoucherList) throws Exception{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("收费凭证信息", 0);
		Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "邮编", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "收费地址", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "投保人姓名", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "被保人姓名", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "分支机构", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "渠道", JXLTool.getHeader());
		Label header8 = new Label(7, 0, "保单号", JXLTool.getHeader());
		Label header9 = new Label(9, 0, "投保单号", JXLTool.getHeader());
		Label header10 = new Label(8, 0, "旧保单号", JXLTool.getHeader());
		Label header11 = new Label(10, 0, "主险名称", JXLTool.getHeader());
		Label header12 = new Label(11, 0, "交费频率", JXLTool.getHeader());
		Label header13 = new Label(12, 0, "收费项目", JXLTool.getHeader());
		Label header14 = new Label(13, 0, "交费起始日期", JXLTool.getHeader());
		Label header15 = new Label(14, 0, "交费终止日期", JXLTool.getHeader());
		Label header16 = new Label(15, 0, "金额", JXLTool.getHeader());
		Label header17 = new Label(16, 0, "本期保费到帐日", JXLTool.getHeader());
		Label header18 = new Label(17, 0, "生成日期", JXLTool.getHeader());
		Label header19 = new Label(18, 0, "TM项目名称", JXLTool.getHeader());
		Label header20 = new Label(19, 0, "是否为信用卡", JXLTool.getHeader());
		Label header21 = new Label(20, 0, "续期扣款银行", JXLTool.getHeader());
		Label header22 = new Label(21, 0, "后续处理类型", JXLTool.getHeader());
		Label header23 = new Label(22, 0, "后续处理的具体内容", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
		ws.addCell(header13);
		ws.addCell(header14);
		ws.addCell(header15);
		ws.addCell(header16);
		ws.addCell(header17);
		ws.addCell(header18);
		ws.addCell(header19);
		ws.addCell(header20);
		ws.addCell(header21);
		ws.addCell(header22);
		ws.addCell(header23);
		for (int i = 1; i < 23; i++) {
			ws.setColumnView(i, 15);
		}
		int count = 1;

		for (int i = 0; i < chargesVoucherList.size(); i++) {
			ChargesVoucherInfo chargesVoucherInfo = (ChargesVoucherInfo) chargesVoucherList.get(i);
			int column = count++;

			setWritableSheet(ws, chargesVoucherInfo, column);
		}
		wb.write();
		wb.close();
	}
	public void setWritableSheet(WritableSheet ws, ChargesVoucherInfo chargesVoucherInfo, int column) throws Exception{
		int i = 0;
		String ccard="";
		if(chargesVoucherInfo.getCcardYn().equalsIgnoreCase("1")){
			ccard="是";
		}else{
			ccard="否";
		}
		
		Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
		
		Label cell2 = new Label(i++, column,chargesVoucherInfo.getZipCode(),JXLTool.getContentFormat());
		
		Label cell3 = new Label(i++, column,chargesVoucherInfo.getFeeAdd(),JXLTool.getContentFormat());
		
		Label cell4 = new Label(i++, column,chargesVoucherInfo.getCownNum(),JXLTool.getContentFormat());
		
		Label cell5 = new Label(i++, column, chargesVoucherInfo.getLifCnum(), JXLTool.getContentFormat());
		
		Label cell6 = new Label(i++, column,chargesVoucherInfo.getInstId(), JXLTool.getContentFormat());
		
		Label cell7 = new Label(i++, column,chargesVoucherInfo.getChannel(), JXLTool.getContentFormat());
		
		Label cell8 = new Label(i++, column,chargesVoucherInfo.getCherNum(), JXLTool.getContentFormat());
		
		Label cell9 = new Label(i++, column,chargesVoucherInfo.getTtmprcno(), JXLTool.getContentFormat());
		
		Label cell10 = new Label(i++, column,chargesVoucherInfo.getRepNum(), JXLTool.getContentFormat());
		
		Label cell11 = new Label(i++, column,chargesVoucherInfo.getPlanLongDesc(), JXLTool.getContentFormat());
		
		Label cell12 = new Label(i++, column,chargesVoucherInfo.getBillFreq(), JXLTool.getContentFormat());
		
		Label cell13 = new Label(i++, column,chargesVoucherInfo.getFeeTyp(), JXLTool.getContentFormat());
		
		Label cell14 = new Label(i++, column,chargesVoucherInfo.getInstFrom(), JXLTool.getContentFormat());
		
		Label cell15 = new Label(i++, column,chargesVoucherInfo.getInstTo(), JXLTool.getContentFormat());
		
		Label cell16 = new Label(i++, column,chargesVoucherInfo.getAcctAmt().toPlainString(), JXLTool.getContentFormat());
		
		Label cell17= new Label(i++, column,chargesVoucherInfo.getTrdt(), JXLTool.getContentFormat());
		
		Label cell18 = new Label(i++, column,chargesVoucherInfo.getCreateDate(), JXLTool.getContentFormat());

		Label cell19 = new Label(i++, column,chargesVoucherInfo.getLongDesc(), JXLTool.getContentFormat());
		
		Label cell20 = new Label(i++, column,ccard, JXLTool.getContentFormat());
		
		Label cell21 = new Label(i++, column,chargesVoucherInfo.getLoanBank(), JXLTool.getContentFormat());
		
		Label cell22 = new Label(i++, column,chargesVoucherInfo.getSign(), JXLTool.getContentFormat());
		
		Label cell23 = new Label(i++, column,chargesVoucherInfo.getReMarks(), JXLTool.getContentFormat());
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
		ws.addCell(cell10);
		ws.addCell(cell11);
		ws.addCell(cell12);
		ws.addCell(cell13);
		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell17);
		ws.addCell(cell18);
		ws.addCell(cell19);
		ws.addCell(cell20);
		ws.addCell(cell21);
		ws.addCell(cell22);
		ws.addCell(cell23);
	}
	public ChargesManageService getChargesManageService() {
		return chargesManageService;
	}
	public void setChargesManageService(ChargesManageService chargesManageService) {
		this.chargesManageService = chargesManageService;
	}
	public List getAuthInstList() {
		return authInstList;
	}
	public void setAuthInstList(List authInstList) {
		this.authInstList = authInstList;
	}
	public TaxDiskInfoService getTaxDiskInfoService() {
		return taxDiskInfoService;
	}
	public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
		this.taxDiskInfoService = taxDiskInfoService;
	}
	public ChargesVoucherInfo getChargesVoucherInfo() {
		return chargesVoucherInfo;
	}
	public void setChargesVoucherInfo(ChargesVoucherInfo chargesVoucherInfo) {
		this.chargesVoucherInfo = chargesVoucherInfo;
	}
	public Map getChanNelList() {
		return chanNelList;
	}
	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}
	
	
}
