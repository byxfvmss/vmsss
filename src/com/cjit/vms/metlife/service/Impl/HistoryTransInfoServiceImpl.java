package com.cjit.vms.metlife.service.Impl;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.HistoryTransInfo;
import com.cjit.vms.metlife.service.HistoryTransInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Abel-西阳 on 2016/2/19.
 */
public class HistoryTransInfoServiceImpl
        extends GenericServiceImpl
            implements HistoryTransInfoService{

    @Override
    public List historyTransInfoList(HistoryTransInfo historyTransInfo, PaginationList paginationList) {
        Map map = new HashMap();
        map.put("historyTransInfo",historyTransInfo);
        return this.find("historyTransInfoList",map,paginationList);
    }

    public void hTransInfoToTransInfo(String[] busiNessIds) {
        Map map = new HashMap();
        map.put("busiNessIds",busiNessIds);
        this.save("hTransInfoToTransInfo",map);
        this.updateHtransInfoList(busiNessIds,"1");
    }

	@Override
	public void updateHtransInfoList(String[] busiNessId,String status) {
		Map map = new HashMap();
		map.put("busiNessIds",busiNessId);
		map.put("status",status);
		this.update("updateHtransInfoList", map);;
	}

	@Override
	public void deleteHtransInfoList(String[] busiNessId) {
		Map map = new HashMap();
		map.put("busiNessIds",busiNessId);
		this.delete("deleteHtransInfoList", map);
		this.updateHtransInfoList(busiNessId, "0");
	}
	
	
}
