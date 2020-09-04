package com.cjit.vms.metlife.service.Impl;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.TransDataInfo;
import com.cjit.vms.metlife.service.ImpDataMetlifeService;
import com.cjit.vms.trans.model.TransInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Abel on 2016/4/7.
 */
public class ImpDataMetlifeServiceImpl extends GenericServiceImpl implements ImpDataMetlifeService{

    private Map map = new HashMap();
    @Override
    public List<TransDataInfo> findTransDataInfoList(TransDataInfo dataInfo, PaginationList paginationList) {
        map.put("dataInfo",dataInfo);
        if(paginationList == null){
            return find("findTransDataInfoList",map);
        }
        return find("findTransDataInfoList",map,paginationList);
    }


    @Override
    public void saveTransBatchInfoList(TransDataInfo dataInfo) {
        map.put("dataInfo",dataInfo);
        this.save("saveTransBatchInfoList",map);
        map.clear();
    }

    @Override
    public void saveTransDataInfoList(List list) {
        this.insertBatch("saveTransDataInfoList",list);
    }

    @Override
    public List<TransDataInfo> findTransDataInfoListByBatchId(TransDataInfo dataInfo, PaginationList paginationList) {
        map.put("dataInfo",dataInfo);
        if(paginationList == null){
            return find("findTransDataInfoListByBatchId",map);
        }
        return find("findTransDataInfoListByBatchId",map,paginationList);
    }

    public List findTransInfo(TransInfo transInfo) {
        map.put("transInfo", transInfo);
        return find("findTransInfo",map);
    }

    @Override
    public void updateTransDataInfoList(List<TransDataInfo> list) {
        updateBatch("updateTransDataInfoList",list);
    }

    @Override
    public void updateTransBatchInfo(TransDataInfo dataInfo) {
        map.put("dataInfo",dataInfo);
        update("updateTransBatchInfo",map);
    }

    @Override
    public void updateTransDataInfoMessage(List<TransDataInfo> list) {
        updateBatch("updateTransDataInfoMessage",list);
    }

    @Override
    public void updateTransDataInfo(TransDataInfo dataInfo) {
        map.put("dataInfo",dataInfo);
        update("updateTransDataInfo",map);
    }

    @Override
    public List findBaseInst(String instCode) {
        map.put("instCode",instCode);
        return find("findBaseInst",map);
    }

    @Override
    public void deleteTransDataInfoByBatchId(String[] batchId,String flag) {
        map.put("batchId",batchId);
        map.put("flag",flag);
        delete("deleteTransDataInfoByBatchId",map);
    }

    @Override
    public void deleteTransBatchInfoByBatchId(String[] batchId) {
        map.put("batchId",batchId);
        delete("deleteTransBatchInfoByBatchId",map);
    }

    @Override
    public void updateTransBatchInfo(List<TransDataInfo> list) {
        updateBatch("updateTransBatchInfoList",list);
    }

    @Override
    public void saveTransDataToTransInfo(TransDataInfo dataInfo) {
        map.put("dataInfo",dataInfo);
        save("saveTransDataToTransInfo",map);
    }

    @Override
    public void findTransDataTransIdByBatchId(String batchId) {
        map.put("batchId",batchId);
        List<TransDataInfo> list =  find("findTransDataTransIdByBatchId",map);
        saveTransTmtoTransInfo(list);
        saveTransTmFroSaleAccountDetailsD1(list);
    }

    @Override
    public void saveTransTmtoTransInfo(List<TransDataInfo> list) {
        insertBatch("saveTransTmtoTrabsInfo",list);
    }

    @Override
    public void saveTransTmFroSaleAccountDetailsD1(List<TransDataInfo> list) {
        insertBatch("saveTransTmFroSaleAccountDetailsD1",list);
        saveTransTmFroSaleAccountDetailsC1(list);
    }

    @Override
    public void saveTransTmFroSaleAccountDetailsC1(List<TransDataInfo> list) {
        insertBatch("saveTransTmFroSaleAccountDetailsC1",list);
    }
}
