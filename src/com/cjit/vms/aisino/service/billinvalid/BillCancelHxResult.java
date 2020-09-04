package com.cjit.vms.aisino.service.billinvalid;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cjit.vms.aisino.util.XmlFunc;

/**
 * 发票作废的调用航信的WebService返回的结果
 * 
 * @author weishuang
 * 
 */
public class BillCancelHxResult {
	enum ReturnCode {
		SUCCESS("0"), CONSOLE_EXCEPTION("-1"), CONNECTION_CONSOLE_ERROR("-2"), CONSOLE_IP_PORT_ERROR(
				"-3");
		private String returnCode;

		private ReturnCode(String returnCode) {
			this.returnCode = returnCode;
		}

		@Override
		public String toString() {
			return this.returnCode;
		}
	}

	/**
	 * 作废失败的个数
	 */
	private int errorCount = 0;

	/**
	 * 作废成功的发票个数
	 */
	private int successCount = 0;

	/**
	 * 失败的发票类型
	 */
	private List<String> failureFplx;
	/**
	 * 作废失败的发票号码
	 */
	private List<String> failureFphm;

	/**
	 * 作废失败的发票代码
	 */
	private List<String> failureFpdm;

	/**
	 * 作废失败的返回值
	 */
	private List<String> failureRetCode;

	/**
	 * 作废失败的消息返回值
	 */
	private List<String> failureRetMsg;

	/**
	 * 作废成功的发票种类
	 */
	private List<String> successFpzls;

	/**
	 * 作废成功的发票号码
	 */
	private List<String> successFphms;

	/**
	 * 作废成功的发票代码
	 */
	private List<String> successFpdms;

	/**
	 * 作废成功的的票据ID
	 */
	private String[] successBillIds;

	/**
	 * 是否是OK的
	 * 
	 * @return
	 */
	public boolean isResultOk() {
		return this.errorCount == 0;
	}

	private void addFailure(String failureRetCode, String failureRetMsg,
			String failureFplx, String failureFpdm, String failureFphm) {
		this.errorCount++;
		if (this.failureFpdm == null) {
			this.failureFplx = new ArrayList<String>();
			this.failureFpdm = new ArrayList<String>();
			this.failureFphm = new ArrayList<String>();
			this.failureRetCode = new ArrayList<String>();
			this.failureRetMsg = new ArrayList<String>();
		}
		this.failureFplx.add(failureFplx);
		this.failureFpdm.add(failureFpdm);
		this.failureFphm.add(failureFphm);
		this.failureRetCode.add(failureRetCode);
		this.failureRetMsg.add(failureRetMsg);

	}

	/**
	 * 返回作废成功的发票数目
	 * 
	 * @return
	 */
	public int getSuccessCount() {
		return this.successCount;
	}

	/**
	 * 返回作废成功的票据ID
	 * 
	 * @return
	 */
	public String[] getSuccessBillIds() {
		return this.successBillIds;
	}

	/**
	 * 添加一个作废成功的发票,作废成功的发票可能需要回写到系统里
	 * 
	 * @param index
	 * @param failureFplx
	 * @param failureFpdm
	 * @param failureFphm
	 */
	private void addSuccess(String successFpzl, String successFpdm,
			String successFphm) {
		this.successCount++;
		if (this.successFpzls == null) {
			this.successFpdms = new ArrayList<String>();
			this.successFpzls = new ArrayList<String>();
			this.successFphms = new ArrayList<String>();
		}
		this.successFpdms.add(successFpdm);
		this.successFpzls.add(successFpzl);
		this.successFphms.add(successFphm);
	}

	/**
	 * 如果有作废失败的，就将作废失败的信息以字符串返回
	 * 
	 * @return
	 */
	public String getExceptionStr() {
		if (this.errorCount == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("作废失败").append(this.errorCount).append("张发票,")
				.append("详情如下：").append("\r\n").append("[");
		for (int i = 0; i < this.errorCount; i++) {
			sb.append("发票种类：").append(this.failureFplx.get(i)).append(",发票代码：")
					.append(this.failureFpdm.get(i)).append(",发票号码：")
					.append(this.failureFphm.get(i)).append(",返回代码：")
					.append(this.failureRetCode.get(i)).append(",返回消息：")
					.append(this.failureRetMsg.get(i));
			if (i != this.errorCount - 1) {
				sb.append("；\r\n");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @param srcFpzl源发票种类参数
	 * @param srcFphms源发票号码参数
	 * @param srcFpdms源发票代码参数
	 *            <pre>
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <service>
	 * 	<err count="2">
	 * 		<refp>
	 * 			<RETCODE></RETCODE>
	 * 			<RETMSG></RETMSG>
	 * 			<FPZL></FPZL>
	 * 			<FPHM></FPHM>
	 * 			<FPDM></FPDM>
	 * 		</refp>
	 * 		<refp>...</refp>
	 * 	</err>
	 * </service>
	 * </pre>
	 * @param responseXml
	 * @return
	 * @throws Exception
	 */
	public static BillCancelHxResult createResult(String[] billIds,
			String[] srcFpzls, String[] srcFphms, String[] srcFpdms,
			String responseXml) throws Exception {
		BillCancelHxResult result = new BillCancelHxResult();
		Document doc = XmlFunc.getDocument(responseXml);
		Element root = doc.getDocumentElement();
		NodeList errs = root.getElementsByTagName("err");
		if (errs == null || errs.getLength() == 0) {
			Element err = (Element) errs.item(0);
			NodeList nl = err.getElementsByTagName("refp");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0, l = nl.getLength(); i < l; i++) {
					Element ref = (Element) nl.item(i);
					String retCode = XmlFunc.getNodeValue(ref, "RETCODE", "");
					String retMsg = XmlFunc.getNodeValue(ref, "RETMSG", "");
					String fplx = XmlFunc.getNodeValue(ref, "FPZL", "");
					String fpdm = XmlFunc.getNodeValue(ref, "FPDM", "");
					String fphm = XmlFunc.getNodeValue(ref, "FPHM", "");
					result.addFailure(retCode, retMsg, fplx, fpdm, fphm);
					if (ReturnCode.SUCCESS.equals(retCode)) {
						result.addSuccess(fplx, fpdm, fphm);
					} else {
						result.addFailure(retCode, retMsg, fplx, fpdm, fphm);
					}
				}
			}
		}
		if (result.successCount > 0) {
			result.successBillIds = new String[result.successCount];
			int index = -1;

			for (int i = 0; i < srcFpzls.length; i++) {
				boolean find = false;
				for (int j = 0; j < result.errorCount; j++) {
					if (srcFpzls[i].equals(result.failureFplx.get(j))
							&& srcFpdms[i].equals(result.failureFpdm.get(j))
							&& srcFphms[i].equals(result.failureFphm.get(j))) {
						find = true;
						break;
					}
				}
				if (!find) {
					++index;
					result.successBillIds[index] = billIds[i];
				}
			}
		}
		doc = null;
		return result;
	}
}
