package com.cjit.vms.aisino.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.service.BillInfoService;

/**
 * 发票打印服务类
 * @title BillPrintService.java  
 * @author kangy  
 * @version V1.0   
 * @since 2016-3-23
 */
public class BillPrintAisinoService {
	/**
	 * 反馈信息码-控制台异常信息
	 */
	public static final int ERR_CONSOLE_EXCEPTION = -1;
	/**
	 * 反馈信息码-连接控制台异常
	 */
	public static final int ERR_CONSOLE_CONNECTION = -2;
	/**
	 * 反馈信息码-控制台ip或端口号为空
	 */
	public static final int ERR_CONSOLE_IP_PORT = -3;
	/**
	 * 反馈信息码-操作成功
	 */
	public static final int RESULT_SUCCESS = 0;
	/**
	 * 反馈打印成功信息
	 */
	public static final String SUCCESS_MSG = "打印成功！";

	/**
	 * 用Map来存储bills，发票代码作为可以，以便校验时能够获取到发票的ID
	 */
	private Map<String, String> billMap = new HashMap<String, String>();
	/**
	 * 拼接打印发票接口，接口格式：
	 * <?xml version="1.0" encoding="UTF-8"?>
		<service>
			<sid>2</sid>
			<ip></ip>
			<port></port>
			<data count="2">
				<record>
					<FPZL>发票种类</FPZL>
					<FPHM>发票代码</FPHM>
					<FPDM>发票号码</FPDM>
				</record>
				<record>...</record>
			</data>
		</service>
	 * @param bills
	 * @return
	 * @throws Exception 
	 */
	public String getPrintXmlInfo(List<BillInfo> bills) {
		//创建根节点
		Element elroot = DocumentHelper.createElement("service");
		Document doc = DocumentHelper.createDocument(elroot);
		doc.setXMLEncoding("UTF-8");
		//创建sid节点
		Element elsid = elroot.addElement("sid");
		elsid.setText("2");
		Element elip = elroot.addElement("ip");
		elip.setText("ip");
		elroot.addElement("port");
		Element eldata = elroot.addElement("data");
		BillInfo billIn = null;
		Element elrecord = null;
		Element elFPZL = null;
		Element elFPHM = null;
		Element elFPDM = null;
		int size = bills.size();
		for (int i = 0; i < size; i++) {
			billIn = (BillInfo) bills.get(i);
			elrecord = eldata.addElement("record");
			//添加节点发票种类
			elFPZL = elrecord.addElement("FPZL");
			elFPZL.setText(billIn.getFapiaoType());
			//添加节点发票号码
			elFPHM = elrecord.addElement("FPHM");
			elFPHM.setText(billIn.getBillNo());
			//添加节点发票代码
			elFPDM = elrecord.addElement("FPDM");
			elFPDM.setText(billIn.getBillCode());
			//在这里把发票信息存储到Map中
			billMap.put(billIn.getBillCode(), billIn.getBillId());
		}
		eldata.addAttribute("count", String.valueOf(size));
		return doc.asXML();
	}
	
	/**
	 * 验证反馈xml接口,并将打印成功的发票信息更新到数据库中，接口格式：
	 * <?xml version="1.0" encoding="UTF-8"?>
		<service>
			<err count="2">
				<refp>
					<RETCODE></RETCODE>
					<RETMSG></RETMSG>
					<FPZL></FPZL>
					<FPHM></FPHM>
					<FPDM></FPDM>
				</refp>
				<refp>...</refp>
			</err>
		</service>
	 * 
	 * @param xml
	 * @return 
	 * @throws Exception
	 */
	public String checkXMLResult(String xml, BillInfoService billInfoService) throws Exception{
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
//		if(StrFunc.compareStr(root.getName(), "service"))
//			throw new Exception("非法的反馈格式:错误的根节点名称" + root.getName() + "\r\n" + xml);
		Element errEle = root.element("err");
		List<Element> errors = errEle.elements("refp");
		int size = errors.size();
		//err节点
		Element el = null;
		//错误代码
		String errCode = null;
		//错误信息
		String errMsg = null;
		//发票种类
		String type = null;
		//发票号码
		String no = null;
		//发票代码
		String code = null;
		List<BillInfo> billlist = new ArrayList<BillInfo>();
		BillInfo billinfo = null;
		StringBuffer errorinfo = new StringBuffer();
		int errorcount = 0;
		for (int i = 0; errors != null && i < size; i++) {
			billinfo = new BillInfo();
			el = errors.get(i);
			errCode = el.elementText("RETCODE");
			errMsg = el.elementText("RETMSG");
			type = el.elementText("FPZL");
			no =el.elementText("FPHM");
			code = el.elementText("FPDM");
			if(Integer.parseInt(errCode) != RESULT_SUCCESS){
				errorcount ++;
				errorinfo.append("发票打印失败！发票代码：").append(errCode)
				.append("	发票号码").append(no).append("	错误码：").append(errCode)
				.append("	失败原因：")
				.append(errMsg).append("/r/n");
				billMap.remove(code);
			}
			billinfo.setBillCode(code);
			billinfo.setBillNo(no);
			billinfo.setFapiaoType(type);
			billlist.add(billinfo);
		}
		for (String key : billMap.keySet()) {
			billInfoService.updateBillByBillId(key, "8");
		}
		return errorcount > 0 ? errorinfo.toString() : SUCCESS_MSG;
	}
	
	public static void main(String[] args) {
//		BillInfo billinfo = new BillInfo();
//		billinfo.setBillNo("12354567890-");
//		billinfo.setBillCode("-098765467890-");
//		billinfo.setFapiaoType("123");
//		List list = new ArrayList();
//		list.add(billinfo);
//		BillInfoAction b = new BillInfoAction();
//		System.out.println(b.getPrintXmlInfo(list));
	}
}
