package com.cjit.vms.metlife.service;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.HistoryTransInfo;

import java.util.List;

/**
 * Created by Abel-西阳 on 2016/2/19.
 */
public interface HistoryTransInfoService {

	
	/**
	 * 销项历史数据页面初始化查询
	 * @param historyTransInfo
	 * @param paginationList
	 * @return
	 */
    List historyTransInfoList(HistoryTransInfo historyTransInfo, PaginationList paginationList);
    
    /**
     * 移送合格数据到VMS_TRANS_INFO
     * @param busiNessIds
     * @return
     */
    void hTransInfoToTransInfo(String[] busiNessIds);
    
    
    /**
     * 移送失败删除 VMS_H_TRANS_INFO 所对应 busiNessId - busiNessIds 的数据
     * @param busiNessIds
     * @return
     */
    void updateHtransInfoList(String[] busiNessId,String status);
    
    /**
     * 移送成功删除 VMS_H_TRANS_INFO 所对应 busiNessId - busiNessIds 的数据
     * @param busiNessIds
     * @return
     */
    void deleteHtransInfoList(String[] busiNessId);
    
    

}
