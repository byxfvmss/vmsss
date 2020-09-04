/**
 * 
 */
package com.cjit.vms.system.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;

import com.cjit.vms.system.model.Monitor;
import com.cjit.vms.system.model.MonitorInput;


/**
 * @author tom
 *
 */
public interface MonitorService {
    
    /**
     * @param monitor 交易类
     * @return 交易 与打票列表 进项监控
     */
    public List findMonitorList(MonitorInput moitor,PaginationList paginationList,String parma);
    /**
     * @param moitor
     * @param paginationList 
     * @return 销向监控
     */
   public List findBuyMonitorList(Monitor moitor,PaginationList paginationList,String parma);
   
   /**
 * @param moitor
 * @param paginationList
 * @param parma
 * @return应交税金
 */
public List findMonitoTaxrList(MonitorInput moitor,PaginationList paginationList);
public List findInputInvoiceMonitorList(MonitorInput monitorInput,
		PaginationList paginationList, String parma);

    
}
