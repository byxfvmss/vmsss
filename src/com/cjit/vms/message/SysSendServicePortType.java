package com.cjit.vms.message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface SysSendServicePortType
  extends Remote
{
  public abstract String send(String paramString)
    throws RemoteException;
  
  public abstract String query(String paramString)
    throws RemoteException;
  
  public abstract String subscriber(String paramString)
    throws RemoteException;
}


/* Location:           D:\manySofts\Desktop\文件比对\生产\vmss\WEB-INF\classes\
 * Qualified Name:     com.cjit.vms.message.SysSendServicePortType
 * JD-Core Version:    0.7.0.1
 */