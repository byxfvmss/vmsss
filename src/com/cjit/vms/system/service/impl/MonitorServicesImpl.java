/**
 * 
 */
package com.cjit.vms.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.BeanMap;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Monitor;
import com.cjit.vms.system.model.MonitorInput;
import com.cjit.vms.system.service.MonitorService;

/**
 * @author tom
 *
 */
public class MonitorServicesImpl extends GenericServiceImpl implements MonitorService {

    
    public List findMonitorList(MonitorInput monitor,PaginationList paginationList,String parma) {
	 Map map=new HashMap();
		List instIds=monitor.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
	 List list=new ArrayList();
	map.put("monitor", monitor);
	map.put("parma", parma);//findMonitorInputTran//findMonitorInputVend//findMonitorInputInst
								//instCode transType vendorTaxno
	if(parma.equals("instCode")){
		 list =find("findMonitorInputInst",map, paginationList);
	}
	if(parma.equals("transType")){
		list =find("findMonitorInputTran",map, paginationList);
	}
	
	if(parma.equals("vendorTaxno")){
		list= find("findMonitorInputVend",map, paginationList);
	}
	List list1=find("findMonitorInputsum",map);
	List listBill=find("findMonitorInputsumBill",map);
	MonitorInput mon=(MonitorInput) list1.get(0);
	MonitorInput monBill=(MonitorInput) listBill.get(0);
	return 	getInputList(list,mon,monBill);
    }

    public List findBuyMonitorList(Monitor monitor,PaginationList paginationList,String parma) {
		List instIds=monitor.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		List customerIds=new ArrayList();
		Map map=new HashMap();
		map.put("auth_inst_ids", lstTmp);
		map.put("customerIds", getcustomerIds(monitor));
		map.put("monitor", monitor);
		
    	List TransMonList=find("findTransListbaseball",map);
    	map.put("customerTaxNos", getCustomerTaxno(monitor));
    	List BillMonList=find("findBillInfoInbatch",map);
   	 List list=new ArrayList();
   	map.put("parma", parma);
   	if(parma.equals("instId")){
   		list=getcombineList(TransMonList, BillMonList,paginationList);
   	}
   	if(parma.equals("customerName")){
   		List listcustTran=find("findSumtranbyCustomer", map);
   		List listcustBill=find("findBillInfoInbatchBycustomer", map);
   		
   		
   		list =getcombineList(listcustTran,listcustBill, paginationList);
   	}
   	
   
   	list.add(getMonitor(map));
 
   	
   	return list;  
	}//TaxAmtSum;//合计稅额  AmtSum  balance balanceTax
    public List getcombineList(List list ,List list1,PaginationList paginationList){
    	List monList=new ArrayList();
    	int m=1;
    	for(int i=0;i<list.size();i++){
    		Map map=new BeanMap(list.get(i));
    		Map map1=new BeanMap(list1.get(i));
    		map1.put("taxAmtSum",map.get("taxAmtSum"));
    		map1.put("amtSum", map.get("amtSum"));
    		map1.put("balance", map.get("balance"));
    		map1.put("balanceTax", map.get("balanceTax"));
    		Iterator<String> it=map1.keySet().iterator();  
    		int j=0;
    		while(it.hasNext()){    
    		     String key;    
    		     String value;    
    		     key=it.next().toString();    
    		     value= map1.get(key)==null?"": map1.get(key).toString();    
    		     if(StringUtil.isNotEmpty(value)){
    		    	j++;
    		     }
    		    
    		}
    		if(j>1){
    			Monitor monitor=(Monitor)list1.get(i);
    			Monitor  monitor1=(Monitor)list.get(i);
    			monitor.setAmtSum(monitor1.getAmtSum());
    			monitor.setTaxAmtSum(monitor1.getTaxAmtSum());
    			monitor.setBalance(monitor1.getBalance());
    			monitor.setBalanceTax(monitor1.getBalanceTax());
    			monitor.setNumber(Integer.toString(m));
    			m++;
    			monList.add(monitor);
    			
    		}
    	}
    	return monList;
    }
    public List getcustomerIds(Monitor monitor){
    	Map map=new HashMap();
    	map.put("customerName", monitor.getCustomerName());
    	return find("findcustomerIdInCustomersInfo",map);
    	
    }
    public List getCustomerTaxno(Monitor monitor){
    	Map map=new HashMap();
    	map.put("customerName", monitor.getCustomerName());
    	return find("findcustomerTaxnobyCustomerId",map);
    }
    public List findMonitoTaxrList(MonitorInput moitor,PaginationList paginationList)
    {
    	 Map map=new HashMap();
    	 List list=new ArrayList();
 		List instIds=moitor.getLstAuthInstId();
 		List lstTmp=new ArrayList();
 		for(int i=0;i<instIds.size();i++){
 			Organization org=(Organization)instIds.get(i);
 			lstTmp.add(org.getId());
 		}
 		map.put("auth_inst_ids", lstTmp);
    	map.put("monitor", moitor);
    	list=find("findMonitorPayTax",map,paginationList);
    	return list;
    	
    }
    public Monitor getMonitor(Map map){
    	List list=find("findsumMonTran",map);
    	Monitor monitor=null;
    	Monitor monitor1=null;
    	if(list.size()==1){
    		monitor1=(Monitor)list.get(0);
    		
    	}
    	list=find("findSumMonBill", map);
    	if(list.size()==1){
    		 monitor=(Monitor)list.get(0);
    		
    	}
    	if(monitor1!=null){
    		monitor.setAmtSum(monitor1.getAmtSum());
			monitor.setTaxAmtSum(monitor1.getTaxAmtSum());
			monitor.setBalance(monitor1.getBalance());
			monitor.setBalanceTax(monitor1.getBalanceTax());
			monitor.setCustomerName("-");
			monitor.setInstName("-");
			monitor.setNumber("总计");
    	}
    	return monitor;
    }
    /**
     * @param list
     * @return 数据处理 销向
     */
    public List getList( List  list, Monitor mon){
    	List listM=new ArrayList();
    	list.add(mon);
    	if(list !=null){
	    	for(int i=0;i<list.size();i++){
	    		Monitor o =(Monitor) list.get(i);
	    		if(o!=null){
	    			if(i<list.size()-1){
	    			o.setNumber(Integer.toString(i+1));
	    			}
	    			else{
	    				o.setNumber("总计");
	    			}
		    		if(o.getInstId()==null){
		    			o.setInstId("－");
		    		}
		    		if(o.getInstName()==null){
		    			o.setInstName("－");
		    		}
		    		if(o.getTransName()==null){
		    			o.setTransName ("－");
		    		}
		    		if(o.getCustomerName()==null){
		    			o.setCustomerName("－");
		    		}
		    		
		    		listM.add(o);
		    	}
	    	}
    	}
    	return listM;
    }
    public List getInputList( List  list, MonitorInput mon,MonitorInput monBill){
    	List listM=new ArrayList();
    	if(mon!=null){
    		monBill.setAmtCny(mon.getAmtCny());
    		monBill.setTaxAmtCny(mon.getTaxAmtCny());
    		monBill.setIncomeCny(mon.getIncomeCny());
    	}
    	list.add(monBill);
    	if(list !=null){
    		for(int i=0;i<list.size();i++){
    			MonitorInput o=(MonitorInput) list.get(i);
	    		if(o!=null){
	    			if(i<list.size()-1){
	    				o.setNumber(Integer.toString(i+1));
	    			}
    			else{
	    				o.setNumber("总计");
	    			}
	    			if(o.getInstName()==null){
	    				o.setInstName("-");
	    			}
	    			if(o.getTransName()==null){
	    				o.setTransName("-");
	    			}
	    			if(o.getVendorName()==null){
	    				o.setVendorName("-");
	    			}
	    			if(o.getAmtCny()==null){
	    				o.setAmtCny(new BigDecimal(0));
	    			}
	    			if(o.getIncomeCny()==null){
	    				o.setIncomeCny(new BigDecimal(0));
	    			}
	    			if(o.getTaxAmtCny()==null){
	    				o.setTaxAmtCny(new BigDecimal(0));
	    			}
	    			if(o.getAmtSum()==null){
	    				o.setAmtSum(new BigDecimal(0));
	    			}
	    			if(o.getTaxAmtSum()==null){
	    				o.setTaxAmtSum(new BigDecimal(0));
	    			}
	    			if(o.getSumAmt()==null){
	    				o.setSumAmt(new BigDecimal(0));
	    			}
	    			if(o.getVatOutAmt()==null){
	    				o.setVatOutAmt(new BigDecimal(0));
	    			}
	    			listM.add(o);
	    		}
	    	}
	    	
    	}
    	return listM;
    }
   
    
    public List getpayTaxList( List  list){
    	List listM=new ArrayList();
    	if(list !=null){
    		for(int i=0;i<list.size();i++){
    			MonitorInput o=(MonitorInput) list.get(i);
	    		if(o!=null){
//	    			if(i<list.size()-1){
//	    			o.setNumber(Integer.toString(i));
//	    			}
//	    			else{
//	    				o.setNumber("总计");
//	    			}
//	    			if(o.getSelltax()==null){
//	    				o.setSelltax(new BigDecimal(0));
//	    				}
	    			if(o.getInstName()==null){
	    				o.setInstName("-");
	    			}
//	    			if(o.getInputtax()==null){
//	    				o.setInputtax(new BigDecimal(0));
//	    			}
	    			if(o.getVatOutAmt()==null){
	    				o.setVatOutAmt(new BigDecimal(0));
	    			}
//	    			if(o.getPaytax()==null){
//	    				o.setPaytax(new BigDecimal(0));
//	    			}
	    			listM.add(o);
	    		}
	    	}
    	}
    	return listM;
    }

	public List findInputInvoiceMonitorList(MonitorInput monitor,
			PaginationList paginationList, String parma) {
		Map map=new HashMap();
		map.put("monitor", monitor);
		List instIds=monitor.getLstAuthInstId();
		List lstTmp=new ArrayList();
		for(int i=0;i<instIds.size();i++){
			Organization org=(Organization)instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		if ("instCode".equals(parma)) {
			return find("findInputInvoiceMonitorByInst",map, paginationList);
		} else if ("vendorTaxno".equals(parma)){
			map.put("param", "vendor_taxno");
			return find("findInputInvoiceMonitorByVendor",map, paginationList);
		}
		return null;
	}
}
