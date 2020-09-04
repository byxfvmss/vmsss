package com.cjit.vms.metlife.action;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cjit.vms.metlife.model.AccountingEntriesInfo;
import com.cjit.vms.metlife.service.AccountingEntriesService;
import com.cjit.vms.system.service.AccTitleService;
import com.cjit.vms.system.service.DLTJobLogService;
import com.cjit.vms.trans.action.DataDealAction;

public class AccTitleMakeMetLifeAction extends DataDealAction {
	private AccTitleService accTitleService ;
	private DLTJobLogService dLTJobLogService;
	private AccountingEntriesService accountingEntriesService;
	private AccountingEntriesInfo accountingEntriesInfo=new AccountingEntriesInfo();
	
	public AccTitleMakeMetLifeAction(){
		this.init();
	}
	
	private void init(){
		log.info("会计分录生成开始！metlife");
		System.out.println("会计分录生成开始！");
	}
	
	public String execute(){
		System.out.println("会计分录生成开始！execute start");
		this.accountingToSun();
		return SUCCESS;
	}
//ODS推送	
	public String executeInput(){
		System.out.println("会计分录生成开始! executeInput");
		accountingEntriesService.inputODS();
		return SUCCESS;
	}
	public String executeChangeFlag(){
//		是否收回发票标识
		System.out.println("会计分录生成开始！executeChangeFlag start");
		accountingEntriesService.updateChangeFlag();
		return SUCCESS;
	}
	//进项sun xml导出
	public String accountingToSun(){
		try{
		String XMLPATH=accountingEntriesService.findXmlPath();
		if(XMLPATH==null){
			log.info("xml 地址错误");
			XMLPATH="C:\\";
			
		}
		List list=accountingEntriesService.findAccountingToSun();
		String xxml=createSunXml(list);
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		FileWriter fw=new FileWriter(XMLPATH);
        fw.write(xxml);
        fw.flush();
		}catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
			return SUCCESS;
     
	}
	
	public String createSunXml(List list) throws Exception{
		AccountingEntriesInfo accountingEntriesInfo1 = (AccountingEntriesInfo) list.get(0);
		//系统前一天
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		long day=0;
		Date beginD = dft.parse(accountingEntriesInfo1.getAccountPeriodStart());
		Date endD= dft.parse(dft.format(date.getTime()));    
        day=(endD.getTime()-beginD.getTime())/(24*60*60*1000);
        NumberFormat nf = new DecimalFormat("0000");
        String b = nf.format(day);
        //System.out.println("相隔的天数="+day);   
		
		String xml = "";
		StringBuffer  sb= new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		sb.append("<SSC>");
		
		sb.append("<ErrorContext>");
		sb.append("<CompatibilityMode>");
		sb.append("1");
		sb.append("</CompatibilityMode>");
		sb.append("<ErrorOutput>").append("1").append("</ErrorOutput>");
		sb.append("<ErrorThreshold>").append("1").append("</ErrorThreshold>");
		sb.append("</ErrorContext>");
		 
		sb.append("<SunSystemsContext>");
		sb.append("<BusinessUnit>").append("NCA").append("</BusinessUnit>");
		sb.append("<BudgetCode>").append("A").append("</BudgetCode>");
		sb.append("</SunSystemsContext>");
		
		sb.append("<MethodContext>");
		sb.append("<LedgerPostingParameters>");
		sb.append("<AllowBalTran>").append("N").append("</AllowBalTran>");
		sb.append("<AllowOverBudget>").append("N").append("</AllowOverBudget>"); 
		sb.append("<AllowPostToSuspended>").append("N").append("</AllowPostToSuspended>");  
		sb.append("<BalancingOptions>").append("T9").append("</BalancingOptions>");
		//待确定 ILFDP_HO_20111201_0010.format(new Date())
		sb.append("<Description>").append("VSDP_VSHO_").append(dft.format(date.getTime())+"_").append(b).append("</Description>");
		sb.append("<JournalType>").append("ST").append("</JournalType>");  
		sb.append("<LayoutCode>").append("SPACE").append("</LayoutCode>");  
		sb.append("<LoadOnly>").append("N").append("</LoadOnly>");  
		sb.append("<PostProvisional>").append("N").append("</PostProvisional>");  
		sb.append("<PostToHold>").append("Y").append("</PostToHold>"); 
		sb.append("<PostingType>").append("2").append("</PostingType>"); 
		sb.append("<Print>").append("N").append("</Print>"); 
		sb.append("<ReportErrorsOnly>").append("Y").append("</ReportErrorsOnly>"); 
		sb.append("<ReportingAccount>").append("9999999999").append("</ReportingAccount>"); 
		sb.append("<SuppressSubstitutedMessages>").append("Y").append("</SuppressSubstitutedMessages>"); 
		sb.append("<SuspenseAccount>").append("9999999999").append("</SuspenseAccount>"); 
		sb.append("<TransactionAmountAccount>").append("9999999999").append("</TransactionAmountAccount>"); 
		sb.append("</LedgerPostingParameters>");
		sb.append("</MethodContext>");
		
		sb.append("<Payload>"); 
		sb.append("<Ledger>");
		if (list != null && list.size() > 0) {
		for(int i=0;i<list.size();i++){
		AccountingEntriesInfo accountingEntriesInfo = (AccountingEntriesInfo) list.get(i);
			sb.append("<line>");
			sb.append("<AccountCode>").append(accountingEntriesInfo.getAccountCode()).append("</AccountCode>");
			sb.append("<AccountingPeriod>").append(accountingEntriesInfo.getAccountingPeriod()).append("</AccountingPeriod>"); 
			sb.append("<AnalysisCode1>").append(accountingEntriesInfo.getLa1Fund()).append("</AnalysisCode1>"); 
			sb.append("<AnalysisCode2>").append(accountingEntriesInfo.getLa2Channel()).append("</AnalysisCode2>"); 
			sb.append("<AnalysisCode3>").append(accountingEntriesInfo.getLa3Category()).append("</AnalysisCode3>"); 
			sb.append("<AnalysisCode4>").append(" ").append("</AnalysisCode4>"); 
			sb.append("<AnalysisCode5>").append(accountingEntriesInfo.getLa5Plan()).append("</AnalysisCode5>"); 
			sb.append("<AnalysisCode6>").append(accountingEntriesInfo.getLa6District()).append("</AnalysisCode6>"); 
			sb.append("<AnalysisCode7>").append(accountingEntriesInfo.getLa7Unit()).append("</AnalysisCode7>"); 
			sb.append("<AnalysisCode8>").append(" ").append("</AnalysisCode8>"); 
			sb.append("<AnalysisCode9>").append(" ").append("</AnalysisCode9>"); 
			sb.append("<AnalysisCode10>").append(accountingEntriesInfo.getLa10Branch()).append("</AnalysisCode10>"); 
			sb.append("<BaseAmount>").append(accountingEntriesInfo.getBaseAmount()).append("</BaseAmount>"); 
			sb.append("<CurrencyCode>").append(accountingEntriesInfo.getCurrency()).append("</CurrencyCode>"); 
			sb.append("<DebitCredit>").append(accountingEntriesInfo.getDc()).append("</DebitCredit>"); 
			sb.append("<Description>").append("VAT").append("</Description>"); 
			sb.append("<JournalSource>").append(accountingEntriesInfo.getJournalSource()).append("</JournalSource>");
			sb.append("<TransactionAmount>").append(accountingEntriesInfo.getTransactionAmount()).append("</TransactionAmount>"); 
			sb.append("<TransactionDate>").append(accountingEntriesInfo.getTransactionDate()).append("</TransactionDate>");
			sb.append("<TransactionReference>").append("VS"+accountingEntriesInfo.getLa10Branch()+dft.format(date.getTime())+b).append("</TransactionReference>");
			sb.append("</line>");
		}
		}
		sb.append("</Ledger>"); 
		sb.append("</Payload>"); 
		
		sb.append("</SSC>");
		
		xml = sb.toString();
		return xml;
	}
	
	
	
	public AccTitleService getAccTitleService() {
		return accTitleService;
	}
	public void setAccTitleService(AccTitleService accTitleService) {
		this.accTitleService = accTitleService;
	}
	public DLTJobLogService getdLTJobLogService() {
		return dLTJobLogService;
	}
	public void setdLTJobLogService(DLTJobLogService dLTJobLogService) {
		this.dLTJobLogService = dLTJobLogService;
	}

	public AccountingEntriesService getAccountingEntriesService() {
		return accountingEntriesService;
	}

	public void setAccountingEntriesService(
			AccountingEntriesService accountingEntriesService) {
		this.accountingEntriesService = accountingEntriesService;
	}
	
}
