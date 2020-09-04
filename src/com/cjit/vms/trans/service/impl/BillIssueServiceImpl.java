package com.cjit.vms.trans.service.impl;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.gjsz.system.service.LogManagerService;
import com.cjit.vms.metlife.model.TtmPrcnoMatch;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.model.DiskRegInfo;
import com.cjit.vms.trans.model.TaxDiskInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.model.storage.PaperInvoiceUseDetail;
import com.cjit.vms.trans.service.BillIssueService;
import com.cjit.ws.jdkClient.VatServiceClient;
import com.cjit.ws.utils.Axis2Client;

public class BillIssueServiceImpl extends GenericServiceImpl implements
		BillIssueService {
	public List findBillInfoList(BillInfo billInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = billInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = billInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			billInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		map.put("billInfo", billInfo);
		return find("findBillTrack", map, paginationList);
	}

	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		String dataStatus = billInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			billInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		map.put("billInfo", billInfo);

		return find("findBillTrack", map);
	}

	public BillInfo findBillInfoById(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);
		List list = find("findBillInfo", map);
		if ((list != null) && (list.size() == 1)) {
			return ((BillInfo) list.get(0));
		}
		return null;
	}

	public void updatebillInfoIssueResult(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		update("updatebillInfoIssueResult", map);
	}

	public void updateBillInfoStatus(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billId", billInfo.getBillId());
		map.put("dataStatus", billInfo.getDataStatus());

		update("updateBillDataStatus", map);
	}

	public void deleteBillInfoById(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteBillInfo", map);
	}

	public List findBillItemByBillId(String billId) {
		Map map = new HashMap();
		BillItemInfo billItem = new BillItemInfo();
		billItem.setBillId(billId);
		map.put("billItem", billItem);

		return find("findBillItemInfo", map);
	}

	public void deleteBillItemInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteBillItemInfo", map);
	}

	public List findTransByBillId(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransByBillId", map);
	}

	public List findTransInfo(String billId) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransInfoById", map);
	}

	public List findTransByBillId(String billId, PaginationList paginationList) {
		Map map = new HashMap();
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);
		map.put("billInfo", billInfo);

		return find("findTransByBillId", map, paginationList);
	}

	public void deleteTransBillInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);

		delete("deleteTransBillInfo", map);
	}

	public void updateTransInfoStatus(String dataStatus, String billId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("billId", billId);

		update("updateTransInfoStatus", map);
	}

	public List findInvalidPaperInvoice(String dataStatus, String fapiaoType) {
		Map map = new HashMap();
		PaperInvoiceUseDetail paperInvoiceUseDetail = new PaperInvoiceUseDetail();
		paperInvoiceUseDetail.setInvoiceStatus(dataStatus);

		map.put("paperInvoiceUseDetail", paperInvoiceUseDetail);
		return find("findInvalidPaperInvoice", map);
	}

	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType,
			String instId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("fapiaoType", fapiaoType);
		map.put("instId", instId);

		return getRowCount("findInvalidEmptyPaperInvoiceCount", map);
	}

	public void updatePaperInvoiceStatus(PaperInvoiceUseDetail invalidInvoice) {
		Map map = new HashMap();
		map.put("paperInvoiceUseDetail", invalidInvoice);
		update("updatePaperInvoiceStatus", map);
	}

	public String findRegisteredInfo(String taxDiskNo) {
		Map map = new HashMap();
		DiskRegInfo diskRegInfo = new DiskRegInfo();
		diskRegInfo.setTaxDiskNo(taxDiskNo);
		map.put("diskRegInfo", diskRegInfo);
		List list = find("findRegisteredInfo", map);
		if ((list != null) && (list.size() == 1)) {
			diskRegInfo = (DiskRegInfo) list.get(0);
			return diskRegInfo.getRegisteredInfo();
		}
		return null;
	}

	public TaxDiskInfo findTaxDiskInfoByTaxDiskNo(String taxDiskNo) {
		Map map = new HashMap();
		TaxDiskInfo taxDiskInfo = new TaxDiskInfo();
		taxDiskInfo.setTaxDiskNo(taxDiskNo);
		map.put("taxDiskInfo", taxDiskInfo);
		List list = find("findTaxDiskInfoByTaxDiskNo", map);
		if ((list != null) && (list.size() == 1)) {
			return ((TaxDiskInfo) list.get(0));
		}
		return null;
	}

	public PaperInvoiceUseDetail getPaperInvoiceUseDetail(String invoiceStatus,
			String invoiceCode, String invoiceNo) {
		Map map = new HashMap();
		map.put("dataStatus", invoiceStatus);
		map.put("invoiceCode", invoiceCode);
		map.put("invoiceNo", invoiceNo);
		List list = find("getpaperInvoiceUseDetailBycode", map);
		PaperInvoiceUseDetail paperInvoiceUseDetail = null;
		if ((list.size() != 0) && (list.size() == 1)) {
			paperInvoiceUseDetail = (PaperInvoiceUseDetail) list.get(0);
		}
		return paperInvoiceUseDetail;
	}

	public void saveMatchInfoList(List<TtmPrcnoMatch> matchSaveList) {
		insertBatch("saveMatchInfoList", matchSaveList);
	}

	public List findMatchInfoList(List matchFindList, boolean flag) {
		Map map = new HashMap();
		map.put("matchList", matchFindList);
		if (flag) {
			map.put("datastatus", "'3','4','7'");
		}
		return find("findMatchInfoList", map);
	}

	public void updateMatchInfoList(List<TtmPrcnoMatch> matchUpdateList) {
		updateRptDataBatch("updateMatchInfoList", matchUpdateList);
	}

	public List findAllMatchInfoList(TtmPrcnoMatch match) {
		Map map = new HashMap();
		map.put("match", match);
		return find("findAllMatchInfoList", map);
	}

	public String invoiceIssueAccessCore(LogManagerService logManagerService,
			HttpServletRequest request, User user, List transList,
			BillInfo billInfo,Log log) {
		try {
			System.out.println("进入BillIssueServiceImpl--invoiceIssueAccessCore");

			String message = "";
			InputStream is = super.getClass().getResourceAsStream("/config/config.properties");
			Properties prop = new Properties();
			prop.load(is);

			String interfaceParam = "";
			try {
				interfaceParam = (String) prop.get("LIS.interface.applyInvoice");
				System.out.println("正在获取配置文件");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String[] interfaceParams = interfaceParam.split("\\|");

			if (transList.size() == 0) {
				message = "没有查到交易信息";
				return "1|" + message;
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			DecimalFormat decimalFormat = new DecimalFormat("000");
			String serialno = dateFormat.format(new Date())
					+ decimalFormat.format(new Random().nextInt(1000));

			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			Element requestElement = document.addElement("REQUEST");
			Element headDoc = requestElement.addElement("HEAD");
			headDoc.addElement("REQUESTTYPE").addText("0003");
			headDoc.addElement("REQSERIALNO").addText(serialno);
			headDoc.addElement("FLOWINTIME").addText(DateUtils.getCurrentDateTime());

			Element dataDoc = requestElement.addElement("DATA");
			Element billListDoc = dataDoc.addElement("BILLLIST");
			for (Iterator it = transList.iterator(); it.hasNext();) {
				TransInfo trans = (TransInfo) it.next();
				Element billDoc = billListDoc.addElement("BILL");
				System.out.println("BUSINESS_ID"+trans.getTransId().substring(0,30));
				billDoc.addElement("BUSINESS_ID").addText(trans.getTransId().substring(0,30));
				billDoc.addElement("TAX_STATUS").addText("1");
			}
			String requestXml = document.asXML();
			logManagerService.writeLog(request, user, serialno, "发票开具回写核心接口", "请求报文", requestXml, "1");
			if(XmlUtil.validateXML(requestXml)){
				log.info("发票开具回写核心接口:"+requestXml);
				System.out.println("发票开具回写请求核心接口:"+requestXml);
				String responseXml = "";
//				if("LSP".equals(billInfo.getDsouRce())){	//个险
//					/*VatServicePortType_VatServicePort_Client client = new VatServicePortType_VatServicePort_Client();
//					responseXml = client.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);*/
//					
//					//个险专属的方法，里面有些如命名空间或者调用方法个险若有修改，则需检查生成的客户端所有java类都需要修改
//					VatServiceClient vatServiceClient = new VatServiceClient();
//					responseXml = vatServiceClient.invoke(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
//				}else if("LIS".equals(billInfo.getDsouRce())){	//团险
//					Axis2Client axis2Client = new Axis2Client();
//					responseXml = axis2Client.invokeRPCClient(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
					Axis2Client axis2Client = new Axis2Client();
					System.out.println("调用回写接口");
					responseXml = axis2Client.invokeRPCClient(interfaceParams[0], requestXml, interfaceParams[1], interfaceParams[2]);
					System.out.println("回写响应报文"+responseXml);
//				}
				logManagerService.writeLog(request, user, serialno, "发票开具响应核心接口", "响应报文", responseXml, "1");
				log.info("发票开具响应核心接口："+responseXml);
//				Document rootDocument = DocumentHelper.parseText(responseXml);
//				Node responseNode = rootDocument.selectSingleNode("RESPONSE");

//				Node resultNode = responseNode.selectSingleNode("RESULT");
//				String resultType = resultNode.selectSingleNode("RESULTTYPE").getStringValue();
//				if (!"0".equals(resultType)) {
//					message = resultNode.selectSingleNode("ERRORINFO").getStringValue();
//					return "1|" + message;
//				}
			}else{
				return "1|回写核心接口请求报文格式不正确";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("BillIssueServiceImpl-invoiceIssueAccessCore", e);
			System.out.println("回写核心接口请求失败！");
		}
		return "0|成功";
	}
	
	
	public static void main(String[] args) {
		BillIssueServiceImpl a = new BillIssueServiceImpl();
		}
}