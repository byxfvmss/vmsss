
package com.cjit.ws.jdkClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cjit.ws.jdkClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VatService_QNAME = new QName("http://service.esb.soa.pub.ebao.com/", "VatService");
    private final static QName _VatServiceResponse_QNAME = new QName("http://service.esb.soa.pub.ebao.com/", "VatServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cjit.ws.jdkClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VatServiceResponse }
     * 
     */
    public VatServiceResponse createVatServiceResponse() {
        return new VatServiceResponse();
    }

    /**
     * Create an instance of {@link VatService }
     * 
     */
    public VatService createVatService() {
        return new VatService();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VatService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.esb.soa.pub.ebao.com/", name = "VatService")
    public JAXBElement<VatService> createVatService(VatService value) {
        return new JAXBElement<VatService>(_VatService_QNAME, VatService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VatServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.esb.soa.pub.ebao.com/", name = "VatServiceResponse")
    public JAXBElement<VatServiceResponse> createVatServiceResponse(VatServiceResponse value) {
        return new JAXBElement<VatServiceResponse>(_VatServiceResponse_QNAME, VatServiceResponse.class, null, value);
    }

}
