package com.cjit.vms.message;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public abstract interface SysSendService
  extends Service
{
  public abstract String getSysSendServiceHttpSoap11EndpointAddress();
  
  public abstract SysSendServicePortType getSysSendServiceHttpSoap11Endpoint()
    throws ServiceException;
  
  public abstract SysSendServicePortType getSysSendServiceHttpSoap11Endpoint(URL paramURL)
    throws ServiceException;
}
