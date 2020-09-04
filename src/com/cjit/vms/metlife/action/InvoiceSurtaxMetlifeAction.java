package com.cjit.vms.metlife.action;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.vms.input.model.InputInvoiceNew;
import com.cjit.vms.metlife.model.AccountingEntriesInfo;
import com.cjit.vms.metlife.service.InvoiceSurtaxMetlifeService;
import com.cjit.vms.system.model.LogEmp;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.InputInvoiceInfo;
import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.service.TaxDiskInfoService;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.util.CheckUtil;
import com.cjit.vms.trans.util.DataUtil;

/** 
 *  createTime:2016.3
 * 	author:沈磊
 *	content:会计分录  metlife
*/

public class InvoiceSurtaxMetlifeAction extends DataDealAction{
	private InputInvoiceInfo inputInvoiceInfo= new InputInvoiceInfo();
	private InvoiceSurtaxMetlifeService invoiceSurtaxMetlifeService;
	private TaxDiskInfoService taxDiskInfoService;
	private VmsCommonService vmsCommonService;
	private String instId;
	private List lstAuthInstId = new ArrayList();
	private List authInstList = new ArrayList();
	private UserInterfaceConfigService userInterfaceConfigService;
	
	private InputInvoiceNew inputInvoiceNew;
	
	private String selectedIds ;
	//查询&初始化页面
		public String listInvoiceInSurtax(){
			if (!sessionInit(true)) {
				request.setAttribute("msg", "用户失效");
				return ERROR;
			}
			try {
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					inputInvoiceInfo= new InputInvoiceInfo();
					inputInvoiceInfo.setDatastatus("15");
					inputInvoiceInfo.setSubjectType("0");
				}
				this.getAuthInstList(lstAuthInstId);
				inputInvoiceInfo.setInstcode(instId);
				inputInvoiceInfo.setLstAuthInstId(lstAuthInstId);
				List paperInfoList=invoiceSurtaxMetlifeService.findInvoiceInSurtaxList(inputInvoiceInfo, paginationList);	
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
		}
		//转出
		public String rollOutInvoiceInSurtax(){ 
			try{
			String expenseDocNum=request.getParameter("expenseDocnuma");
			String billno=request.getParameter("code");
			System.out.println(inputInvoiceInfo.getSubjectType());
			String[] expenseDocNumArray=expenseDocNum.split(",");
			String[] billnoArray=billno.split(",");
			List listbillinfo=new ArrayList();
		    System.out.println(this.getCurrentUser().getCustomId());
			//重复报销单号
		    List<String> list = new ArrayList<String>();
		    for (int i=0; i<expenseDocNumArray.length; i++) {
			if(!list.contains(expenseDocNumArray[i])) {//如果数组 list 不包含当前项，则增加该项到数组中
				list.add(expenseDocNumArray[i]);
				}
		    } 
		    String[] newExpenseDocNum =  list.toArray(new String[1]); 
		    for (String element:newExpenseDocNum ) {  
		        System.out.print(element + " ");  
		    }

			//采购
			if(inputInvoiceInfo.getSubjectType().equalsIgnoreCase("0")){
			for(int i=0;i<newExpenseDocNum.length;i++){
				inputInvoiceInfo.setExpenseDocnum(newExpenseDocNum[i]);
				List<AccountingEntriesInfo> infolist=invoiceSurtaxMetlifeService.findListInvoice(inputInvoiceInfo);
				//更新会计分录原有状态
				invoiceSurtaxMetlifeService.updateInvoiceInfo(infolist);
				//新增会计分录数据
				Map map=new HashMap();
				map.put("expenseDocnum",newExpenseDocNum[i].toString());
				map.put("instId", this.getCurrentUser().getCustomId());
				listbillinfo.add(map);
			} 
//新增会计分录数据
			invoiceSurtaxMetlifeService.insertInvoceInfo(listbillinfo);
//更新发票表
			invoiceSurtaxMetlifeService.updatetransInfo(listbillinfo);
			}
			//佣金
			else if(inputInvoiceInfo.getSubjectType().equalsIgnoreCase("3")){
				for(int i=0;i<newExpenseDocNum.length;i++){
					Map map=new HashMap();
					map.put("expenseDocnum",newExpenseDocNum[i]);
					map.put("instId",  this.getCurrentUser().getCustomId());
					listbillinfo.add(map);
				}
				invoiceSurtaxMetlifeService.insertcommission(listbillinfo);
			}
			return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
		}
//财务月页面初始化
		public String financeMonth(){
			if (!sessionInit(true)) {
				request.setAttribute("msg", "用户失效");
				return ERROR;
			}
			try {
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					inputInvoiceInfo= new InputInvoiceInfo();
				}
				
				invoiceSurtaxMetlifeService.findFinanceMonth(inputInvoiceInfo,paginationList);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
			return SUCCESS;
		}

		//财务月导入	
		//导入功能
				public String uploadFinanceMonth(){
					MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
					File[] files = mRequest.getFiles("theFile");
					if (files != null && files.length > 0) {
						try {
							if (!sessionInit(false))
								throw new Exception("初始化缓存数据失败!");
							doImportFile(files[0]);
							//this.setResultMessages("上传文件完成!");
							files = null;
							return SUCCESS;
						} catch (Exception e) {
							log.error(e);
							e.printStackTrace();
							this.setResultMessages("上传文件失败:" + e.getMessage());
							return ERROR;
						}
					} else {
						this.setResultMessages("上传文件失败!");
						return ERROR;
					}
				}
				private String doImportFile(File file) throws Exception {
					try{
					List<Dictionary> headList = userInterfaceConfigService.getDictionarys1(
							"VMSS_FINANCE_MONTH", "", "");
					Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
					Map<String, String> mapBusi=new HashMap<String,String>();
					if (hs != null) {
						String[][] sheet = (String[][]) hs.get("0");
						// 获取表头列表
						String[] heads = sheet[0];
						List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
						String result="";
						String resultId="";
						Map<String,Boolean> m=new  HashMap<String, Boolean>();
						String startDate=StringUtil.getCurrentDate();
						for (int i = 1; i < sheet.length; i++) {
							String[] row = sheet[i];
							Map<String,String> map= new HashMap<String, String>();
							map=CheckUtil.CreatMap(heads, headList, row);
							result=checkformat(map, i, sheet, result);
							dataList.add(map);
							}	
						invoiceSurtaxMetlifeService.insertFinanceMonth(dataList);
					}
					} catch (Exception e) {
						log.error(e);
						e.printStackTrace();
						this.setResultMessages("上传文件失败:" + e.getMessage());
						return ERROR;
					}
					return SUCCESS;
					
				}
				public String checkformat(Map<String,String> map,int i,String[][]sheet,String result){
					List<String> list=new ArrayList<String>();
					list.add(map.get("accountPeriod"));
					list.add(map.get("accountPeriodStrart"));
					list.add(map.get("accountPeriodEnd"));
					Map<String, Boolean> mapCheck=new HashMap<String, Boolean>();
					mapCheck.put("checkNull",CheckUtil.checkNotNull(list));
					mapCheck.put("checkDate", CheckUtil.checkDate(map.get("accountPeriodStrart")));
					//mapCheck.put("checkDate", CheckUtil.checkDate(map.get("transDate")));
					result=CheckUtil.checkData(mapCheck, i, result, sheet.length);
					return result;
					
				}
			public String delFinanceMonth(){
				try{
					String accountPeriod=request.getParameter("vtorId");
					String[] accountPeriodArray=accountPeriod.split(",");
					List cancelFinanceMonth=new ArrayList();
					for(int i=1;i<accountPeriodArray.length;i++){
						Map map = new HashMap();
						map.put("accountPeriod",accountPeriodArray[i]);
						cancelFinanceMonth.add(map);
					}
					invoiceSurtaxMetlifeService.cancelFinanceMonth(cancelFinanceMonth);
					
					} catch (Exception e) {
						e.printStackTrace();
						log.error("PaperInvoiceAction-listPageInvoice", e);
						return ERROR;
					}
					return SUCCESS;
				
			}	
					
//		转出比例界面
		public String transferOutRatioInfo(){
			if (!sessionInit(true)) {
				request.setAttribute("msg", "用户失效");
				return ERROR;
			}
			try {
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					inputInvoiceInfo= new InputInvoiceInfo();
				}
				InstInfo in = new InstInfo();
			    in.setUserId(this.getCurrentUser().getId());
				List lstAuthInstId = new ArrayList(); 
			    this.getAuthInstList(lstAuthInstId);
			    in.setLstAuthInstIds(lstAuthInstId);
			    authInstList = taxDiskInfoService.getInstInfoList(in);
				List tansferOutRatio=invoiceSurtaxMetlifeService.findTansferOutRatio(inputInvoiceInfo,paginationList);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
			return SUCCESS;
		}
		
		public String updatetransferOutRatio(){
			try{
			List<InputInvoiceInfo> transferOutRatio=invoiceSurtaxMetlifeService.updatetransferOutRatio();
			if(transferOutRatio.size()==0){
				this.setResultMessages("会计月不存在!");
				return ERROR;
				
			}
			invoiceSurtaxMetlifeService.updatetransferOutRatio1(transferOutRatio);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
			return SUCCESS;
		}

		public InputInvoiceInfo getInputInvoiceInfo() {
			return inputInvoiceInfo;
		}
		public void setInputInvoiceInfo(InputInvoiceInfo inputInvoiceInfo) {
			this.inputInvoiceInfo = inputInvoiceInfo;
		}
		public InvoiceSurtaxMetlifeService getInvoiceSurtaxMetlifeService() {
			return invoiceSurtaxMetlifeService;
		}
		public void setInvoiceSurtaxMetlifeService(
				InvoiceSurtaxMetlifeService invoiceSurtaxMetlifeService) {
			this.invoiceSurtaxMetlifeService = invoiceSurtaxMetlifeService;
		}
		public TaxDiskInfoService getTaxDiskInfoService() {
			return taxDiskInfoService;
		}
		public void setTaxDiskInfoService(TaxDiskInfoService taxDiskInfoService) {
			this.taxDiskInfoService = taxDiskInfoService;
		}
		public VmsCommonService getVmsCommonService() {
			return vmsCommonService;
		}
		public void setVmsCommonService(VmsCommonService vmsCommonService) {
			this.vmsCommonService = vmsCommonService;
		}
		public String getInstId() {
			return instId;
		}
		public void setInstId(String instId) {
			this.instId = instId;
		}
		public List getLstAuthInstId() {
			return lstAuthInstId;
		}
		public void setLstAuthInstId(List lstAuthInstId) {
			this.lstAuthInstId = lstAuthInstId;
		}
		public List getAuthInstList() {
			return authInstList;
		}
		public void setAuthInstList(List authInstList) {
			this.authInstList = authInstList;
		}
		public UserInterfaceConfigService getUserInterfaceConfigService() {
			return userInterfaceConfigService;
		}
		public void setUserInterfaceConfigService(
				UserInterfaceConfigService userInterfaceConfigService) {
			this.userInterfaceConfigService = userInterfaceConfigService;
		}
		
		
		
		public String getSelectedIds() {
			return selectedIds;
		}
		public void setSelectedIds(String selectedIds) {
			this.selectedIds = selectedIds;
		}
		/**李松加
		 * @return
		 */
		public String listInvoiceInSurtaxNew(){
			if (!sessionInit(true)) {
				request.setAttribute("msg", "用户失效");
				return ERROR;
			}
			try {
				
				if ("menu".equalsIgnoreCase(fromFlag)) {
					fromFlag = null;
					inputInvoiceInfo= new InputInvoiceInfo();
					inputInvoiceInfo.setDatastatus("15");
					inputInvoiceInfo.setSubjectType("0");
				}
				this.getAuthInstList(lstAuthInstId);
				inputInvoiceInfo.setInstcode(instId);
				inputInvoiceInfo.setLstAuthInstId(lstAuthInstId);
				inputInvoiceNew = new InputInvoiceNew();
				if(request.getParameter("billCode")!=null&&request.getAttribute("billCode")!=""){
					inputInvoiceNew.setBillCode(request.getParameter("billCode"));
				}
				if(request.getParameter("billNo")!=null&&request.getAttribute("billNo")!=""){
					inputInvoiceNew.setBillNo(request.getParameter("inputInvoiceInfo.billNo"));
				}
				if(request.getParameter("transferStatus")!=null&&request.getAttribute("transferStatus")!=""){
					inputInvoiceNew.setTransferStatus(request.getParameter("transferStatus"));
				}
				
				List paperInfoList=
						invoiceSurtaxMetlifeService.findInvoiceInSurtaxListNew(inputInvoiceNew, paginationList);	
				System.out.println(paperInfoList);
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("PaperInvoiceAction-listPageInvoice", e);
				return ERROR;
			}
		}
		
		/**李松加
		 * @return
		 */
		public String rollOutInvoiceInSurtaxNew(){ 
			//传进参数billNo  和    transforAmt
			String Ids[] = selectedIds.split(",");
			List idList = new ArrayList<String>();
			List insertList = new ArrayList<InputInvoiceNew>();
			for(String id :Ids){
				InputInvoiceNew inputInvoiceNew = invoiceSurtaxMetlifeService.findInputTransByIdCodeNoAndNo(id);
				insertList.add(inputInvoiceNew);
				idList.add(id);
			}
		
			Map idMap = new HashMap<String, List<String>>();
			idMap.put("idList", idList);
			invoiceSurtaxMetlifeService.updatetransInfoNew(idMap);
			//往表中插入数据的时候先查询出数据
			idMap.put("insertList",insertList);
			invoiceSurtaxMetlifeService.saveInputInvoice(idMap);
			
			
			return SUCCESS;
		}
		
}
