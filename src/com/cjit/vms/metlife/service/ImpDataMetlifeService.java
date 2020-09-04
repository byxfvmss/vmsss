package com.cjit.vms.metlife.service;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.TransDataInfo;
import com.cjit.vms.trans.model.TransInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Abel on 2016/4/7.
 */
public interface ImpDataMetlifeService {

    List<TransDataInfo> findTransDataInfoList(TransDataInfo dataInfo, PaginationList paginationList);

    void saveTransBatchInfoList(TransDataInfo dataInfo);


    void saveTransDataInfoList(List list);

    List<TransDataInfo> findTransDataInfoListByBatchId(TransDataInfo dataInfo, PaginationList paginationList);

    List findTransInfo(TransInfo transInfo);

    void updateTransDataInfoList(List<TransDataInfo> list);

    void updateTransDataInfoMessage(List<TransDataInfo> list);

    void updateTransBatchInfo(TransDataInfo dataInfo);

    void updateTransBatchInfo(List<TransDataInfo> list);

    void updateTransDataInfo(TransDataInfo dataInfo);

    List findBaseInst(String instCode);

    void deleteTransDataInfoByBatchId(String[] batchId,String flag);

    void deleteTransBatchInfoByBatchId(String[] batchId);

    void saveTransDataToTransInfo(TransDataInfo dataInfo);

    void findTransDataTransIdByBatchId(String batchId);

    void saveTransTmtoTransInfo(List<TransDataInfo> list);

    void saveTransTmFroSaleAccountDetailsD1(List<TransDataInfo> list);

    void saveTransTmFroSaleAccountDetailsC1(List<TransDataInfo> list);
}
