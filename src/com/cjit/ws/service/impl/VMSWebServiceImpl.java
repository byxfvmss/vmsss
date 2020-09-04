package com.cjit.ws.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.input.model.CommonCode;
import com.cjit.ws.dao.VmsCustomerInfoDao;
import com.cjit.ws.dao.VmsTransInfoDao;
import com.cjit.ws.dao.VmsTransTypeDao;
import com.cjit.ws.entity.VmsCustomerInfo;
import com.cjit.ws.entity.VmsTransInfo;
import com.cjit.ws.entity.VmsTransType;

public class VMSWebServiceImpl extends GenericServiceImpl{

	private VmsTransTypeDao vmsTransTypeDao;
	private VmsCustomerInfoDao vmsCustomerInfoDao;
	private VmsTransInfoDao vmsTransInfoDao;

	public String transService(String xml){
		Document document=null;
		Document doc=null;
		Element HeaderResp=null;
		String RequestType=null;
		String UUID=null;
		String SendTime=null;
		String result=null;//²åÈëÊý¾Ý¿â·µ»ØµÄ½á¹ûÐÅÏ¢
		String invtypValue=null;
		try {
			document = DocumentHelper.parseText(xml);
			//System.out.println(document.asXML()+"-----");
			Element Package=document.getRootElement();
			Element Header=Package.element("Header");
			Element Request=Package.element("Request");
			Element RequestTypeElement=Header.element("RequestType");
			Element UUIDElement=Header.element("UUID");
			Element SendTimeElement=Header.element("SendTime");

			RequestType=RequestTypeElement.getText();
			UUID=UUIDElement.getText();
			SendTime=SendTimeElement.getText();
			
			ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"com/cjit/ws/service/config/applicationcontext.xml"});
			
			
			vmsTransTypeDao=(VmsTransTypeDao) ac.getBean("VmsTransTypeBean");
			vmsCustomerInfoDao=(VmsCustomerInfoDao) ac.getBean("VmsCustomerInfoBean");
			vmsTransInfoDao=(VmsTransInfoDao) ac.getBean("VmsTransInfoBean");
			
			Element bussListElement=Request.element("BUSSLIST");
			List<Element> bussInfoList=bussListElement.elements();
			
//			VmsBussInfo vmsBussInfo=new VmsBussInfo();
//			List<BussInfo> bussList=new ArrayList<BussInfo>();
			for(Element bussInfoElement:bussInfoList){
				String businessId=bussInfoElement.elementText("BUSINESS_ID");
				String instId=bussInfoElement.elementText("INST_ID");
				String qdFlag=bussInfoElement.elementText("QD_FLAG");
				String chernum=bussInfoElement.elementText("CHERNUM");
				String repnum=bussInfoElement.elementText("REPNUM");
				String ttmprcno=bussInfoElement.elementText("TTMPRCNO");
				String customerName=bussInfoElement.elementText("CUSTOMER_NAME");
				String customerTaxno=bussInfoElement.elementText("CUSTOMER_TAXNO");
				String customerAddressand=bussInfoElement.elementText("CUSTOMER_ADDRESSAND");
				String taxpayerType=bussInfoElement.elementText("TAXPAYER_TYPE");
				String customerPhone=bussInfoElement.elementText("CUSTOMER_PHONE");
				String customerAccount=bussInfoElement.elementText("CUSTOMER_ACCOUNT");
				String trdt=bussInfoElement.elementText("TRDT");
				List<String> errors=new ArrayList<String>();
				String customerBankand=bussInfoElement.elementText("CUSTOMER_BANKAND");
				String origcurr=bussInfoElement.elementText("ORIGCURR");
				String origamt=bussInfoElement.elementText("ORIGAMT");
				String acctamt=bussInfoElement.elementText("ACCTAMT");
				String invtyp=bussInfoElement.elementText("INVTYP");
				String bustyp=bussInfoElement.elementText("BUSTYP");
				String billfreq=bussInfoElement.elementText("BILLFREQ");
				String polyear=bussInfoElement.elementText("POLYEAR");
				String hissdte=bussInfoElement.elementText("HISSDTE");
				String planlongdesc=bussInfoElement.elementText("PLANLONGDESC");
				String instfrom=bussInfoElement.elementText("INSTFROM");
				String instto=bussInfoElement.elementText("INSTTO");
				String occdate=bussInfoElement.elementText("OCCDATE");
				String premterm=bussInfoElement.elementText("PREMTERM");

				invtypValue=invtyp;
				
				if("G".equals(qdFlag)&&(customerName==null||"".equals(customerName)||customerTaxno==null||"".equals(customerTaxno)
						||customerAddressand==null||"".equals(customerAddressand)||customerPhone==null||"".equals(customerPhone)
						||customerBankand==null||"".equals(customerBankand)||customerAccount==null||"".equals(customerAccount))){
					doc = DocumentHelper.createDocument();
					doc.setXMLEncoding("GBK");
					Element PackageResp = doc.addElement("Package");
					HeaderResp = PackageResp.addElement("Header");
					HeaderResp.addElement("RequestType").addText(RequestType);
					HeaderResp.addElement("UUID").addText(UUID);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					HeaderResp.addElement("SendTime").addText(df.format(new Date()));
					
					System.out.println("**"+customerAccount);
					System.out.println("**"+customerAddressand);
					System.out.println("**"+customerBankand);
					System.out.println("**"+customerName);
					System.out.println("**"+customerPhone);
					System.out.println("**"+customerTaxno);
					
					HeaderResp.addElement("ResponseCode").addText("N");
					Element ErrorList=HeaderResp.addElement("ErrorList");
					Element ERROR=ErrorList.addElement("ERROR");
					ERROR.addElement("ERROR_ID").addText("error");
					ERROR.addElement("ERRMSG").addText("¿Í»§ÐÅÏ¢²»ÍêÕû£¬ÇëÍêÉÆ£¡");
					return doc.asXML();
				}
				
				System.out.println(customerTaxno+"---"+customerAddressand);
				
			if("0".equals(invtyp)&&(customerName==null||"".equals(customerName)||(customerTaxno.length()!=15&&customerTaxno.length()!=18)
						||customerAddressand==null||"".equals(customerAddressand)||customerPhone==null||"".equals(customerPhone)
						||customerBankand==null||"".equals(customerBankand)||customerAccount==null||"".equals(customerAccount))){
						
					doc = DocumentHelper.createDocument();
					doc.setXMLEncoding("GBK");
					Element PackageResp = doc.addElement("Package");
					HeaderResp = PackageResp.addElement("Header");
					HeaderResp.addElement("RequestType").addText(RequestType);
					HeaderResp.addElement("UUID").addText(UUID);
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					HeaderResp.addElement("SendTime").addText(df.format(new Date()));
					
					System.out.println("**"+customerAccount);
					System.out.println("**"+customerAddressand);
					System.out.println("**"+customerBankand);
					System.out.println("**"+customerName);
					System.out.println("**"+customerPhone);
					System.out.println("**"+customerTaxno);
					System.out.println(customerTaxno.length());
					
					HeaderResp.addElement("ResponseCode").addText("N");
					Element ErrorList=HeaderResp.addElement("ErrorList");
					Element ERROR=ErrorList.addElement("ERROR");
					ERROR.addElement("ERROR_ID").addText("error");
					ERROR.addElement("ERRMSG").addText("¿Í»§ÐÅÏ¢²»ÍêÕû»ò·¢Æ±ÀàÐÍÓÐÎó£¡");
					return doc.asXML();
							
				}
				
				Map map = new HashMap();				
				map.put("instId",instId);				
				instId = vmsTransInfoDao.find(map);
		        
				VmsTransInfo vmsTransInfo=new VmsTransInfo();
				
				vmsTransInfo.setNARRITIVE3(UUID);
				vmsTransInfo.setBusinessId(businessId);
				vmsTransInfo.setInstId(instId);
				vmsTransInfo.setQdFlag(qdFlag);
				vmsTransInfo.setChernum(chernum);
				vmsTransInfo.setRepnum(repnum);
				vmsTransInfo.setTtmprcno(ttmprcno);
				vmsTransInfo.setOrigcurr(origcurr);
				vmsTransInfo.setOrigamt(Double.parseDouble(origamt));
				vmsTransInfo.setAcctamt(Double.parseDouble(acctamt));
				vmsTransInfo.setTrdt(trdt);
				vmsTransInfo.setInvtyp(invtyp);
				System.out.println(invtyp);
				vmsTransInfo.setBustyp(bustyp);
				vmsTransInfo.setBillfreq(billfreq);
				if(polyear!=null&&!"".equals(polyear)){
					vmsTransInfo.setPolyear(Integer.parseInt(polyear));
				}
				vmsTransInfo.setHissdte(hissdte);
				vmsTransInfo.setPlanlongdesc(planlongdesc);
				vmsTransInfo.setInstfrom(instfrom);
				vmsTransInfo.setInstto(instto);
				vmsTransInfo.setOccdate(occdate);
				if(premterm!=null&&!"".equals(premterm)){
					vmsTransInfo.setPremterm(Integer.parseInt(premterm));
				}
				
				VmsCustomerInfo vmsCustomerInfo=new VmsCustomerInfo();
				vmsCustomerInfo.setCustomerName(customerName);
				vmsCustomerInfo.setCustomerTaxno(customerTaxno);
				vmsCustomerInfo.setCustomerAddressand(customerAddressand);
				vmsCustomerInfo.setTaxpayerType(taxpayerType);
				vmsCustomerInfo.setCustomerPhone(customerPhone);
				vmsCustomerInfo.setCustomerBankand(customerBankand);
				vmsCustomerInfo.setCustomerAccount(customerAccount);
				vmsCustomerInfo.setInvtyp(invtyp);
				vmsCustomerInfo.setBusinessId(businessId);
				
				Element covListElement=bussInfoElement.element("COVLIST");
				List<Element> covInfoList=covListElement.elements();
				int i = 1;
//				List<CovInfo> covList=new ArrayList<CovInfo>();
				for(Element covInfoElement:covInfoList){
					String transType = covInfoElement.elementText("TRANSTYPE");
					String insCod=covInfoElement.elementText("INS_COD");
					String insNam=covInfoElement.elementText("INS_NAM");
					String feetyp=covInfoElement.elementText("FEETYP");
					String amtCny=covInfoElement.elementText("AMT_CNY");
					String taxAmtCny=covInfoElement.elementText("TAX_AMT_CNY");
					String incomeCny=covInfoElement.elementText("INCOME_CNY");
					String taxRate=covInfoElement.elementText("TAX_RATE");
					
					if("Z".equals(taxRate)||"F".equals(taxRate)){
						invtypValue="1";
					}
					double taxRateValue = 0;
					if("S".equals(taxRate)){
						taxRateValue=0.06;
					}else if("N".equals(taxRate)){
						taxRateValue=0.03;
					}else if("Z".equals(taxRate)){
						taxRateValue=0;
					}else if("P".equals(taxRate)){
						taxRateValue=0.17;
					}else if("F".equals(taxRate)){
						taxRateValue=0;
					}else{
						throw new Exception();
					}
					i++;
					vmsTransInfo.setTransId(UUID+i);
					vmsTransInfo.setInvtyp(invtypValue);
					vmsTransInfo.setFeetyp(feetyp);
					vmsTransInfo.setAmtCny(Double.parseDouble(amtCny));
					vmsTransInfo.setTaxAmtCny(Double.parseDouble(taxAmtCny));
					vmsTransInfo.setIncomeCny(Double.parseDouble(incomeCny));
					vmsTransInfo.setTaxRate(taxRateValue);
					vmsTransInfo.setVatRateCode(taxRate);
					vmsTransInfo.setTransType(transType);
					vmsTransInfo.setInsCod(insCod);
					vmsTransInfo.setInsNam(insNam);
					
					result=vmsTransInfoDao.insert(vmsTransInfo);
					
//					VmsTransType vmsTransType=new VmsTransType();
//					vmsTransType.setInsCod(transType);
//					vmsTransType.setInsNam(insNam);
//					
//					System.out.println();
//					System.out.println("** "+insCod);
//					System.out.println("** "+insNam);
					
//					result=vmsTransTypeDao.insert(vmsTransType);
				}
				vmsCustomerInfo.setInvtyp(invtypValue);
				result=vmsCustomerInfoDao.insert(vmsCustomerInfo);
			}
			
			doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element PackageResp = doc.addElement("Package");
			HeaderResp = PackageResp.addElement("Header");
			HeaderResp.addElement("RequestType").addText(RequestType);
			HeaderResp.addElement("UUID").addText(UUID);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HeaderResp.addElement("SendTime").addText(df.format(new Date()));
			
			if("Y".equals(result)){
				HeaderResp.addElement("ResponseCode").addText("Y");
			}else{
				HeaderResp.addElement("ResponseCode").addText("N");
				Element ErrorList=HeaderResp.addElement("ErrorList");
				Element ERROR=ErrorList.addElement("ERROR");
				ERROR.addElement("ERROR_ID").addText(String.valueOf("error"));
				ERROR.addElement("ERRMSG").addText(result);
			}
			System.out.println("*result="+result);
		} catch (Exception e) {
			doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("GBK");
			Element PackageResp = doc.addElement("Package");
			HeaderResp = PackageResp.addElement("Header");
			HeaderResp.addElement("RequestType").addText(RequestType);
			HeaderResp.addElement("UUID").addText(UUID);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HeaderResp.addElement("SendTime").addText(df.format(new Date()));
			
			HeaderResp.addElement("ResponseCode").addText("N");
			Element ErrorList=HeaderResp.addElement("ErrorList");
			Element ERROR=ErrorList.addElement("ERROR");
			ERROR.addElement("ERROR_ID").addText("error");
			ERROR.addElement("ERRMSG").addText("ÇëÇó±¨ÎÄ²»·ûºÏ±ê×¼£¬Çë¼ì²éºóÖØÊÔ£¡");
			e.printStackTrace();
			return doc.asXML();
		}
		
		return doc.asXML();
	}
	
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		Document document;
		String documentStr = null;
		try {
			document = reader.read(new File("E:/004295820363452f91235e022073abe9InvoicePrintR.xml"));
			documentStr = document.asXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		new VMSWebServiceImpl().transService(documentStr);
	}
}
