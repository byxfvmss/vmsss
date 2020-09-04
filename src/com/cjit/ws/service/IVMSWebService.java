package com.cjit.ws.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;




@WebService(serviceName="IVmsWebService",targetNamespace="http://ws.vms.cjit.com") 
public interface IVMSWebService { 
    @WebResult(name="out") 
    public String excute(@WebParam(name = "xmlStr",targetNamespace="http://ws.vms.cjit.com") String xmlStr) throws Exception;
    
    
    public String importVatInfo(String xmlStr) throws Exception;


//	public List<TransListInfo> findTransListByNo(String reqSerialno);
//
//
//	public void saveTransList(TransListInfo transList);
//
//
//	public void saveInsuranceKindInfo(InsuranceKindInfo kind);
//
//
//	public void updateTransListStatus(String reqSerialno, String dealStatus);
    
    
}
