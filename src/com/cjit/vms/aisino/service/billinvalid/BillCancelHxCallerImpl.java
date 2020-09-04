package com.cjit.vms.aisino.service.billinvalid;

import com.cjit.vms.aisino.service.HxServiceFactory;
import com.cjit.vms.aisino.util.WebXmlBase;

/**
 * 发票作废的WebService调用接口
 * 
 * @author weishuang
 * 
 */
public class BillCancelHxCallerImpl implements
		BillCancelHxCaller {

	@Override
	public BillCancelHxResult invalidBills(String ip, String port,
			String[] billIds, String[] fpzl, String[] fphm, String[] fpdm)
			throws Exception {
		String requestText = createRequestXml(ip, port, fpzl, fphm, fpdm);
		String webServiceResult = callWebService(requestText);
		return BillCancelHxResult.createResult(billIds,fpzl, fphm, fpdm,
				webServiceResult);
	}

	/**
	 * 调用航信的WebService接口，进行发票作废的操作
	 * 
	 * @param requestText
	 * @return
	 * @throws Exception
	 */
	private String callWebService(String requestText) throws Exception {
		return HxServiceFactory.createHxInvoiceService().invalidInvoice(requestText);
	}

	/**
	 * 生成一个饭票作废请求的XML
	 * 
	 * <pre>
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <service>
	 * 	<sid>4</sid>
	 * 	<ip></ip>
	 * 	<port></port>
	 * 	<data count="2">
	 * 		<record>
	 * 			<FPZL></FPZL>
	 * 			<FPHM></FPHM>
	 * 			<FPDM></FPDM>
	 * 		</record>
	 * 		<record>...</record>
	 * 	</data>
	 * </service>
	 * </pre>
	 * 
	 * @param ip
	 * @param port
	 * @param fpzl
	 * @param fphm
	 * @param fpdm
	 * @return
	 */
	private String createRequestXml(String ip, String port, String[] fpzl,
			String[] fphm, String[] fpdm) {
		WebXmlBase base = new WebXmlBase("service");
		base.startTag("sid");
		base.setTagValue("4");
		base.endTag();
		base.startTag("ip");
		base.setTagValue(ip);
		base.endTag();
		base.startTag("port");
		base.setTagValue(port);
		base.endTag();
		base.startTagWithNewLine("data");
		base.setAttribute("count",
				String.valueOf(fpzl == null ? 0 : fpzl.length));
		for (int i = 0, l = fpzl == null ? 0 : fpzl.length; i < l; i++) {
			base.startTag("record");
			base.startTagWithNewLine("FPZL");
			base.setTagValue(fpzl[i]);
			base.endTag();
			base.startTagWithNewLine("FPHM");
			base.setTagValue(fphm[i]);
			base.endTag();
			base.startTagWithNewLine("FPDM");
			base.setTagValue(fpdm[i]);
			base.endTag();
		}
		base.endTag();
		base.finish();
		return base.toString();
	}

}
