/**
 * SysSendServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cjit.vms.message;

import com.cjit.vms.message.impl.SysSendServiceSoap11BindingStub;

public class SysSendServiceLocator extends org.apache.axis.client.Service implements SysSendService {

    public SysSendServiceLocator() {
    	
    }


    public SysSendServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SysSendServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SysSendServiceHttpSoap11Endpoint
    private java.lang.String SysSendServiceHttpSoap11Endpoint_address = "http://10.1.248.60:7001/sms/services/SysSendService.SysSendServiceHttpSoap11Endpoint/";

    public java.lang.String getSysSendServiceHttpSoap11EndpointAddress() {
        return SysSendServiceHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SysSendServiceHttpSoap11EndpointWSDDServiceName = "SysSendServiceHttpSoap11Endpoint";

    public java.lang.String getSysSendServiceHttpSoap11EndpointWSDDServiceName() {
        return SysSendServiceHttpSoap11EndpointWSDDServiceName;
    }

    public void setSysSendServiceHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
        SysSendServiceHttpSoap11EndpointWSDDServiceName = name;
    }

    public SysSendServicePortType getSysSendServiceHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SysSendServiceHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSysSendServiceHttpSoap11Endpoint(endpoint);
    }

    public SysSendServicePortType getSysSendServiceHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
        	SysSendServiceSoap11BindingStub _stub = new SysSendServiceSoap11BindingStub(portAddress, this);
            _stub.setPortName(getSysSendServiceHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSysSendServiceHttpSoap11EndpointEndpointAddress(java.lang.String address) {
        SysSendServiceHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SysSendServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
            	SysSendServiceSoap11BindingStub _stub = new SysSendServiceSoap11BindingStub(new java.net.URL(SysSendServiceHttpSoap11Endpoint_address), this);
                _stub.setPortName(getSysSendServiceHttpSoap11EndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SysSendServiceHttpSoap11Endpoint".equals(inputPortName)) {
            return getSysSendServiceHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.sms.sinosoft.com", "SysSendService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.sms.sinosoft.com", "SysSendServiceHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SysSendServiceHttpSoap11Endpoint".equals(portName)) {
            setSysSendServiceHttpSoap11EndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
