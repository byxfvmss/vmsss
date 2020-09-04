package com.cjit.vms.metlife.action;

/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:单证管理  metlife
*/
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.vms.metlife.model.DocumentManageInfo;
import com.cjit.vms.metlife.service.DocumentManageService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;

import com.cjit.vms.trans.model.InstInfo;


import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.JXLTool;
public class DocumentManageAction  extends DataDealAction{
	private DocumentManageService documentManageService;
	private DocumentManageInfo documentManageInfo=new DocumentManageInfo();
	private List DocumentManageinfolist;
	private String mod;
	private Map chanNelList;
	private Map typeList;
	private Map statusList;
	private TaxDiskInfoService taxDiskInfoService;
	private List authInstList = new ArrayList();
	private Map baseUserList;
	private VmsCommonService vmsCommonService;
	
		//序列号规则生成页面初始化&查询
		public String createKeyCode(){
			try{
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				documentManageInfo=new DocumentManageInfo();
				documentManageInfo.setType("1");
			}
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
		    chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
		    typeList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_TYPE");
			DocumentManageinfolist=documentManageService.findDocumentManagelist(documentManageInfo,paginationList);
			return SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
		}
		//新增 规则生成页面初始化
		public String createCode(){
			try{
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			typeList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_TYPE");
			return SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
		}
		//修改规则生成页面初始化
		public String editKeyCode(){
			try{
			
			String ruleId=request.getParameter("ruleId");
			System.out.println(ruleId);
			if(ruleId.equalsIgnoreCase("")){
				return ERROR;
			}
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
			chanNelList = this.vmsCommonService.findCodeDictionary("SIGN_CHANNEL_TYPE");
			documentManageInfo=	(DocumentManageInfo)documentManageService.findByRuleId(ruleId).get(0);
			return SUCCESS;
			
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
		}
		//保存 修改 规则方法
		public String saveKeyCode(){
			try{
				
			
			System.out.println(documentManageInfo.getRuleId());
			String id=documentManageInfo.getRuleId();
			
			if(id!=null){
				System.out.println("into update keycode");
				String yearY="";
				System.out.println(documentManageInfo.getYearYn());
				if(documentManageInfo.getYearYn().equalsIgnoreCase("1")){
					SimpleDateFormat df = new SimpleDateFormat("yy");//设置日期格式
					yearY=df.format(new Date());
					System.out.println(yearY);// new Date()为获取当前系统时间
				}
				int leng= Integer.parseInt(documentManageInfo.getLength());
				String len="";
				String lenx="";
				String maxnum="9";
				for(int i=0;i<leng-1 ;i++){
					 len+="0";
					 lenx+="*";
					 maxnum+="9";
				}
				String rul=documentManageInfo.getInstCode().toString()+yearY+documentManageInfo.getChannelCode().toString()+documentManageInfo.getSpeCode().toString()+documentManageInfo.getSufCode().toString()+lenx+"*";   
				String curnum=documentManageInfo.getInstCode().toString()+yearY+documentManageInfo.getChannelCode().toString()+documentManageInfo.getSpeCode().toString()+documentManageInfo.getSufCode().toString()+len+"1";   
				documentManageInfo.setRule(rul);
				documentManageInfo.setCurNum(curnum);
				documentManageInfo.setMaxNum(maxnum);
				documentManageService.updateKeyCode(documentManageInfo);
			}else{
				System.out.println("into checksave");
				List lists=documentManageService.checkCode(documentManageInfo);
				if(lists.size()>0){
					this.setResultMessages("数据已存在");
					return ERROR;
				}
				String yearY="";
				System.out.println(documentManageInfo.getYearYn());
				if(documentManageInfo.getYearYn().equalsIgnoreCase("1")){
					SimpleDateFormat df = new SimpleDateFormat("yy");//设置日期格式
					yearY=df.format(new Date());
					System.out.println(yearY);// new Date()为获取当前系统时间
				}
				int leng= Integer.parseInt(documentManageInfo.getLength());
				String len="";
				String lenx="";
				String maxnum="9";
				for(int i=0;i<leng-1 ;i++){
					 len+="0";
					 lenx+="*";
					 maxnum+="9";
				}
				String rul=documentManageInfo.getInstCode().toString()+yearY+documentManageInfo.getChannelCode().toString()+documentManageInfo.getSpeCode().toString()+documentManageInfo.getSufCode().toString()+lenx+"*";   
				String curnum=documentManageInfo.getInstCode().toString()+yearY+documentManageInfo.getChannelCode().toString()+documentManageInfo.getSpeCode().toString()+documentManageInfo.getSufCode().toString()+len+"1";   
				documentManageInfo.setRule(rul);
				documentManageInfo.setCurNum(curnum);
				documentManageInfo.setMaxNum(maxnum);
				documentManageService.saveCode(documentManageInfo);
			}
			return SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
		}
		//删除规则方法
		public void  cancelKeyCode(){
			PrintWriter out = null;
		try{
			String ruleid=request.getParameter("rule");
			documentManageService.deleteKeyCode(ruleid);
			out = response.getWriter();

			out.print("Y");
			
		}catch(Exception e){
			out.print("N");
			e.printStackTrace();
		}
		}
		//投保单号页面初始化&查询
		public String manageInsureCode(){
			try{
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				documentManageInfo=new DocumentManageInfo();
				documentManageInfo.setType("1");
				documentManageInfo.setStatus("0");
			}
	
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			typeList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_TYPE");
			statusList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_STATUS");
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
		    DocumentManageinfolist=documentManageService.findManageInsureCodeList(documentManageInfo,paginationList);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		}
		//序列号生成界面
		public String createNum(){
			try{
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					documentManageInfo=new DocumentManageInfo();
				}
				System.out.println(request.getParameter("curNumBegin"));
				documentManageInfo.setBank(request.getParameter("bank"));
				documentManageInfo.setChannel(request.getParameter("channel"));
				documentManageInfo.setType(request.getParameter("type"));
				documentManageInfo.setInstId(request.getParameter("instId"));
				documentManageInfo.setCountNum(request.getParameter("countNum"));
				documentManageInfo.setCurNumBegin(request.getParameter("curNumBegin"));
				documentManageInfo.setCurNumEnd(request.getParameter("curNumEnd"));
				typeList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_TYPE");
				statusList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_STATUS");
				InstInfo in = new InstInfo();
			    in.setUserId(this.getCurrentUser().getId());
				List lstAuthInstId = new ArrayList(); 
			    this.getAuthInstList(lstAuthInstId);
			    in.setLstAuthInstIds(lstAuthInstId);
			    authInstList = taxDiskInfoService.getInstInfoList(in);
			    chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
				return SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
		}
		//生成序列方法
		public String createCodeNum(){
			try{
				List lists=documentManageService.checkCode(documentManageInfo);
				if(lists.size()==0){
					this.setResultMessages("数据不存在");
					return ERROR;
				}
				documentManageInfo=new DocumentManageInfo();
				documentManageInfo=(DocumentManageInfo) lists.get(0);
				
				System.out.println(documentManageInfo.getCurNum()+"@"+documentManageInfo.getLength()+"@"+documentManageInfo.getMaxNum()+"@"+request.getParameter("countNum"));
				int Numlen=Integer.parseInt(documentManageInfo.getLength().toString());
				String checkNum=documentManageInfo.getCurNum().toString();
				String temp=checkNum.substring(0,checkNum.length()-Numlen);
				System.out.println("temp"+temp);
				String countnum=request.getParameter("countNum");
				int count=Integer.parseInt(countnum);
				checkNum=checkNum.substring(checkNum.length()-Numlen, checkNum.length());
				int numNim=Integer.parseInt(checkNum);
				int numNimi=Integer.parseInt("1"+checkNum);
				int numMax=Integer.parseInt(documentManageInfo.getMaxNum().toString());
				documentManageInfo.setCountNum(countnum);
				System.out.println(temp+(numNim+count));
				if((numNim+count)>numMax){
					this.setResultMessages("已超过流水号极限！当前只能生成"+(numMax-numNim)+"张单证。");
					return ERROR;
				}
				String xx=""+(numNimi+1);
				String tempsx=temp+xx.substring(1, xx.length());
				documentManageInfo.setCurNumBegin(tempsx);
				String temps="";
				List documentManageInfoList=new ArrayList();
				for(int i=1;i<count;i++){
					String x=""+(numNimi+i);
					temps=temp+x.substring(1, x.length());
					Map map = new HashMap();
					map.put("curnumarray",temps);
					map.put("type", documentManageInfo.getType());
					map.put("instId", documentManageInfo.getInstId());
					map.put("bank", documentManageInfo.getBank());
					map.put("channel", documentManageInfo.getChannel());
					documentManageInfoList.add(map);
				}
				System.out.println("end");
				String x=""+(numNimi+count);
				temps+=temp+x.substring(1, x.length());
				String upCurNum=temp+x.substring(1, x.length());
//				String[] curnumarray=temps.split(",");
//				System.out.println(temps);
//				documentManageInfo.setCurNumArray(curnumarray);
//				documentManageService.saveCodeNum(documentManageInfo,curnumarray);
				documentManageService.saveCodeNum1(documentManageInfoList);
				documentManageInfo.setCurNum(upCurNum);
				documentManageService.updatecurNum(documentManageInfo);
				documentManageInfo.setCurNumEnd(temp+x);
				return SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				this.setResultMessages("操作失败");
				return ERROR;
			}
		}
		//入库方法
		public String changeStatus(){
		try{
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			String usernam="";
			System.out.println(sta);
			if(sta=="2"){
				usernam=this.getCurrentUser().getId();
			}
			documentManageService.updateKeyCode(numId,sta,usernam, "");
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			typeList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_TYPE");
			statusList=this.vmsCommonService.findCodeDictionary("VMSS_DOC_STATUS");
			InstInfo in = new InstInfo();
		    in.setUserId(this.getCurrentUser().getId());
			List lstAuthInstId = new ArrayList(); 
		    this.getAuthInstList(lstAuthInstId);
		    in.setLstAuthInstIds(lstAuthInstId);
		    authInstList = taxDiskInfoService.getInstInfoList(in);
			DocumentManageinfolist=documentManageService.findManageInsureCodeList(documentManageInfo,paginationList);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//领用弹窗初始化&查询
		public String takecode(){
		try{
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				documentManageInfo=new DocumentManageInfo();
			}
			
			String numId=request.getParameter("num");
			String sta=request.getParameter("status");
			System.out.println(numId+"@@"+sta);
			documentManageService.finDocRecInfo(documentManageInfo,paginationList);
			documentManageInfo.setNum(numId);
			documentManageInfo.setVdriStatus(sta);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//保存领用人
		public String changeStatusx(){
		try{
			fromFlag="menu";
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			String vdName=request.getParameter("vdName");
			String usernam="";
			documentManageService.updateKeyCode(numId,sta,usernam,vdName);
			documentManageInfo.setStatus("1");
			documentManageInfo.setType("1");
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//调拨弹窗初始化&查询
		public String transTo(){
			try{
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					documentManageInfo=new DocumentManageInfo();
				}
				String numId=request.getParameter("num");
				String sta=request.getParameter("status");
				String orderNum=request.getParameter("orderNum");
				System.out.println(orderNum);
				documentManageService.finDocRecInfo(documentManageInfo,paginationList);
				documentManageInfo.setNum(numId);
				documentManageInfo.setVdriStatus(sta);
				documentManageInfo.setVdiOrder(orderNum);
				return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		
		//保存调拨
		public String changeStatusxx(){
		try{
			fromFlag="menu";
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			String vdName=request.getParameter("vdName");
			String orderNum=request.getParameter("orderNum");
			System.out.println(orderNum);
			String[] orderNumArray=orderNum.split(",");
			String[] numIdArray=numId.split(",");
			
			for(int i=0;i<orderNumArray.length;i++){
				if(orderNumArray[i]==""||numIdArray[i]==null){
					return ERROR;
				}
			System.out.println(orderNumArray[i]+"@"+numIdArray[i]);
			documentManageService.insertTransTo(numIdArray[i],orderNumArray[i]);
			documentManageService.updateTransTo(sta,numIdArray[i],orderNumArray[i],vdName);
			}
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//分发弹窗初始化&查询
		public String distribute(){
		try{
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			documentManageInfo.setStatus(sta);
			documentManageInfo.setNum(numId);
			baseUserList=new HashMap();
			List listCD=documentManageService.findBaseuser();
			if(listCD!=null&&listCD.size()>0){
				for(int i=0;i<listCD.size();i++){
					DocumentManageInfo cd=(DocumentManageInfo) listCD.get(i);
					baseUserList.put(cd.getBaseUserId(), cd.getBaseUserName().equals("null")?"":cd.getBaseUserName());
				}
			}
			
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//保存分发人
		public String distributeTo(){
		try{
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			System.out.println(documentManageInfo.getDisId());
			documentManageInfo.setStatus(sta);
			documentManageInfo.setNum(numId);
			documentManageService.distributeTo(documentManageInfo);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		//遗失作废方法
		public String changeStatusy(){
		try{
			System.out.println("into changeStatusy");
			String sta=request.getParameter("status");
			String numId=request.getParameter("num");
			String orderNum=request.getParameter("orderNum");
			String[] orderNumArray=orderNum.split(",");
			String[] numIdArray=numId.split(",");
			
			for(int i=0;i<orderNumArray.length;i++){
				if(orderNumArray[i]==""||numIdArray[i]==null){
					return ERROR;
				}
			System.out.println(orderNumArray[i]+"@"+numIdArray[i]);
			documentManageService.updateCancel(sta,orderNumArray[i],numIdArray[i]);

			}
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			this.setResultMessages("操作失败");
			return ERROR;
		}
		}
		
		//保单关联管理界面初始化&查询
		public String insureRelationManage(){
		try{
			if ("menu".equalsIgnoreCase(fromFlag)) {
				fromFlag = null;
				documentManageInfo=new DocumentManageInfo();
			}
			documentManageService.findDocMapInfo(documentManageInfo,paginationList);
			
			chanNelList = this.vmsCommonService.findCodeDictionary("SIGN_CHANNEL_TYPE");
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		}
		//保单关联管理界面导出方法
		public void insureRelaToExcel(){
			try{
				List insureRelaToExcel=documentManageService.findDocMapInfo(documentManageInfo,paginationList);
				StringBuffer fileName = null;
				fileName = new StringBuffer("保单关联管理信息");
				
				fileName.append(".xls");
				String name = "attachment;filename="
						+ URLEncoder.encode(fileName.toString(), "UTF-8")
								.toString();
				response.setHeader("Content-type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", name);
				OutputStream os = response.getOutputStream();
				writeToExcelYS(os, insureRelaToExcel);
				os.flush();
				os.close();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		public void writeToExcelYS(OutputStream os, List insureRelaToExcel) throws WriteException, IOException{
			WritableWorkbook wb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			ws = wb.createSheet("保单关联管理信息", 0);
			Label header1 = new Label(0, 0, "序号", JXLTool.getHeader());
			Label header2 = new Label(1, 0, "基准日", JXLTool.getHeader());
			Label header3 = new Label(2, 0, "投保单号", JXLTool.getHeader());
			Label header4 = new Label(3, 0, "保险单证号", JXLTool.getHeader());
			Label header5 = new Label(4, 0, "保单号", JXLTool.getHeader());
			Label header6 = new Label(5, 0, "投保单号渠道", JXLTool.getHeader());
			Label header7 = new Label(6, 0, "保险单证号渠道", JXLTool.getHeader());
			Label header8 = new Label(7, 0, "投保单号初始状态", JXLTool.getHeader());
			Label header9 = new Label(8, 0, "保险单证号初始状态", JXLTool.getHeader());
			Label header10 = new Label(9, 0, "报错", JXLTool.getHeader());
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
			for (int i = 0; i < 10; i++) {
				ws.setColumnView(i, 15);
			}
			int count = 1;

			for (int i = 0; i < insureRelaToExcel.size(); i++) {
				DocumentManageInfo o = (DocumentManageInfo) insureRelaToExcel.get(i);
				int column = count++;

				setWritableSheet(ws, o, column);
			}
			wb.write();
			wb.close();
		}
		
		private void setWritableSheet(WritableSheet ws, DocumentManageInfo documentManageInfo, int column)throws WriteException{
			int i = 0;
			
			Label cell1 = new Label(i++, column,Integer.toString(column),JXLTool.getContentFormat());
			
			Label cell2 = new Label(i++, column,documentManageInfo.getVdmiDate(),JXLTool.getContentFormat());
			
			Label cell3 = new Label(i++, column,documentManageInfo.getAppNum(),JXLTool.getContentFormat());
			
			Label cell4 = new Label(i++, column,documentManageInfo.getFormNum(),JXLTool.getContentFormat());
			
			Label cell5 = new Label(i++, column, documentManageInfo.getPolicyNum(), JXLTool.getContentFormat());
			
			Label cell6 = new Label(i++, column,documentManageInfo.getStatus(), JXLTool.getContentFormat());
			
			Label cell7 = new Label(i++, column,documentManageInfo.getChannel(), JXLTool.getContentFormat());
			
			Label cell8 = new Label(i++, column,documentManageInfo.getTepStatus(), JXLTool.getContentFormat());
			
			Label cell9 = new Label(i++, column,documentManageInfo.getTepchannel(), JXLTool.getContentFormat());
			
			Label cell10 = new Label(i++, column,documentManageInfo.getVdmiError(), JXLTool.getContentFormat());
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
			
		}
		public DocumentManageService getDocumentManageService() {
			return documentManageService;
		}

		public void setDocumentManageService(DocumentManageService documentManageService) {
			this.documentManageService = documentManageService;
		}

		public DocumentManageInfo getDocumentManageInfo() {
			return documentManageInfo;
		}

		public void setDocumentManageInfo(DocumentManageInfo documentManageInfo) {
			this.documentManageInfo = documentManageInfo;
		}
		public List getDocumentManageinfolist() {
			return DocumentManageinfolist;
		}
		public void setDocumentManageinfolist(List documentManageinfolist) {
			DocumentManageinfolist = documentManageinfolist;
		}
		public String getMod() {
			return mod;
		}
		public void setMod(String mod) {
			this.mod = mod;
		}
			
		public Map getChanNelList() {
			return chanNelList;
		}
		public void setChanNelList(Map chanNelList) {
			this.chanNelList = chanNelList;
		}
		public VmsCommonService getVmsCommonService() {
			return vmsCommonService;
		}
		public void setVmsCommonService(VmsCommonService vmsCommonService) {
			this.vmsCommonService = vmsCommonService;
		}
		public TaxDiskInfoService getTaxDiskInfoService() {
			return taxDiskInfoService;
		}
		public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
			this.taxDiskInfoService = taxDiskInfoService;
		}
		public List getAuthInstList() {
			return authInstList;
		}
		public void setAuthInstList(List authInstList) {
			this.authInstList = authInstList;
		}
		public Map getTypeList() {
			return typeList;
		}
		public void setTypeList(Map typeList) {
			this.typeList = typeList;
		}
		public Map getStatusList() {
			return statusList;
		}
		public void setStatusList(Map statusList) {
			this.statusList = statusList;
		}
		public Map getBaseUserList() {
			return baseUserList;
		}
		public void setBaseUserList(Map baseUserList) {
			this.baseUserList = baseUserList;
		}
		
		
}
