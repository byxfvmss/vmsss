package com.cjit.vms.trans.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.util.DataUtil;


public class BillQueryByTax extends DataDealAction {

	private static final long serialVersionUID = 1L;

	private BillInfo billInfo;
	private List<BillInfo> billList;
	
	public String billQueryByTax() {
		List<BillInfo> bList = (ArrayList<BillInfo>)this.request.getSession().getAttribute("billInfoListForTax");
		this.request.getSession().removeAttribute("billInfoListForTax");
		this.request.setAttribute("billList", bList);
		return SUCCESS;
	}
	
	public void billQueryByServer() throws Exception {
		String queryType = request.getParameter("queryType");
		String fapiaoType = request.getParameter("fapiaoType");
		String billBeginDate = request.getParameter("billBeginDate");
		String billEndDate = request.getParameter("billEndDate");
		String billCode = request.getParameter("billCode");
		String billBeginNo = request.getParameter("billBeginNo");
		String billEndNo = request.getParameter("billEndNo");
		
		String condition = "";
		if ("1".equals(queryType)) {
			billBeginDate = DateUtils.toString(DateUtils.stringToDate(billBeginDate, "yyyy-MM-dd"), "yyyyMMdd");
			billEndDate = DateUtils.toString(DateUtils.stringToDate(billEndDate, "yyyy-MM-dd"), "yyyyMMdd");
			condition = billBeginDate + billEndDate;
		} else {
			condition = billCode + billBeginNo + billEndNo;
		}
		
		Document doc = createBillQueryDoc(queryType, fapiaoType, condition);
		String result = createBillQueryXML (doc);
		System.out.println("票据查询发送数据：\n" + result);
		printWriterResult(result);
	}
	
	private String createBillQueryXML(Document doc) {
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
		String result = xmlOut.outputString(doc);
		return result;
	}

	private Document createBillQueryDoc(String queryType, String fapiaoType, String condition) {
		Element root = new Element("business");
		root.setAttribute("id", "10010");
		root.setAttribute("comment", "发票查询");
		root.addContent(new Element("body").setAttribute("yylxdm", "1"));
		root.addContent(new Element("kpzdbs").setText(TaxSelvetUtil.bill_TerminalFlag_ch));
		root.addContent(new Element("fplxdm").setText("0".equals(fapiaoType)? "004" : "007"));
		root.addContent(new Element("cxfs").setText(queryType));
		root.addContent(new Element("cxtj").setText(condition));
		Document doc = new Document(root);
		
		return doc;
	}

	public String parseBillQueryServerReturnXml() throws Exception {
		String ret = request.getParameter("ret");
		ret = readTxt();
		String fapiaoType = request.getParameter("fapiaoType");
		StringReader read = new StringReader(ret);
		InputSource source = new InputSource(read);//利用字节流创建新的输入源
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(source);
		Element root = doc.getRootElement();
		Element body = root.getChild("body");
		String returncode =body.getChildText("returncode");
		if ("0".equals(returncode)) {
			Element returndata = body.getChild("returndata");
			Element kpxx = returndata.getChild("kpxx");
			String count = kpxx.getAttributeValue("count");
			int len = Integer.parseInt(count);
			billList = new ArrayList<BillInfo>();
			for (int i=0; i<len; i++){
				Element group = kpxx.getChild("group");
				billInfo = new BillInfo();
				billInfo.setBillDate(DateUtils.toString(DateUtils.stringToDate(group.getChildText("kprq"),"yyyyMMddHHmmss"),"yyyy-MM-dd HH:mm:ss"));
				billInfo.setBillCode(group.getChildText("fpdm"));
				billInfo.setBillNo(group.getChildText("fphm"));
				billInfo.setCustomerName(group.getChildText("xhdwmc"));
				billInfo.setCustomerTaxno(group.getChildText("xhdwsbh"));
				billInfo.setAmtSumStr(group.getChildText("hjje"));
				billInfo.setTaxAmtSumStr(group.getChildText("hjse"));
				billInfo.setSumAmtStr(group.getChildText("jshj"));
				billInfo.setFapiaoType(DataUtil.getFapiaoTypeCH(fapiaoType));
				billInfo.setDataStatus(group.getChildText("fpzt"));
				billList.add(billInfo);
			}
			this.request.getSession().setAttribute("billInfoListForTax", billList);
		}
		printWriterResult(returncode);
		return "server";
	}

	private void printWriterResult(String result) throws Exception {
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}
	
	private String readTxt() {
		StringBuffer sb = new StringBuffer();
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			File file = new File(request.getRealPath("/") + "/WEB-INF/vms/taxServer/票据查询服务器返回.txt");
			System.out.println(file.getAbsolutePath());
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			byte[] b = new byte[50];
			int len;
			while ((len=bis.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
		
	}
	
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	public List<BillInfo> getBillList() {
		return billList;
	}
	public void setBillList(List<BillInfo> billList) {
		this.billList = billList;
	}
	
	
	

}
