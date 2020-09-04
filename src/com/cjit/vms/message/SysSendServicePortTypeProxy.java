package com.cjit.vms.message;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

public class SysSendServicePortTypeProxy
  implements SysSendServicePortType
{
  private String _endpoint = null;
  private SysSendServicePortType sysSendServicePortType = null;
  
  public SysSendServicePortTypeProxy()
  {
    _initSysSendServicePortTypeProxy();
  }
  
  public SysSendServicePortTypeProxy(String endpoint)
  {
    this._endpoint = endpoint;
    _initSysSendServicePortTypeProxy();
  }
  
  private void _initSysSendServicePortTypeProxy()
  {
    try
    {
      this.sysSendServicePortType = new SysSendServiceLocator().getSysSendServiceHttpSoap11Endpoint();
      if (this.sysSendServicePortType != null) {
        if (this._endpoint != null) {
          ((Stub)this.sysSendServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
        } else {
          this._endpoint = ((String)((Stub)this.sysSendServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address"));
        }
      }
    }
    catch (ServiceException localServiceException) {}
  }
  
  public String getEndpoint()
  {
    return this._endpoint;
  }
  
  public void setEndpoint(String endpoint)
  {
    this._endpoint = endpoint;
    if (this.sysSendServicePortType != null) {
      ((Stub)this.sysSendServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", this._endpoint);
    }
  }
  
  public SysSendServicePortType getSysSendServicePortType()
  {
    if (this.sysSendServicePortType == null) {
      _initSysSendServicePortTypeProxy();
    }
    return this.sysSendServicePortType;
  }
  
  public String send(String xmlStream)
    throws RemoteException
  {
    if (this.sysSendServicePortType == null) {
      _initSysSendServicePortTypeProxy();
    }
    return this.sysSendServicePortType.send(xmlStream);
  }
  
  public String query(String xml)
    throws RemoteException
  {
    if (this.sysSendServicePortType == null) {
      _initSysSendServicePortTypeProxy();
    }
    return this.sysSendServicePortType.query(xml);
  }
  
  public String subscriber(String inputxml)
    throws RemoteException
  {
    if (this.sysSendServicePortType == null) {
      _initSysSendServicePortTypeProxy();
    }
    return this.sysSendServicePortType.subscriber(inputxml);
  }
}
