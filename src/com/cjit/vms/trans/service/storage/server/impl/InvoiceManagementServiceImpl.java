package com.cjit.vms.trans.service.storage.server.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cjit.crms.util.StringUtil;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.vms.trans.model.storage.PaperAutoInvoice;
import com.cjit.vms.trans.service.storage.server.InvoiceManagementService;

public class InvoiceManagementServiceImpl extends GenericServiceImpl implements InvoiceManagementService {

	public List CreatePaperAutoInvoiceList(String data, String taxNo,String keyNo, String instId, String fapiaoType) throws ParseException {
		//	|成功|当前未开发票代码|当前未开发票号码|总剩余份数|
		//		0	1					2			3		
			//发票代码^起始号码^终止号码^发票份数^剩余份数^领购日期^领购人员
				//0		1		2			3		4		5		6
		PaperAutoInvoice paperAutoInvoice=null;
			List list=null;
			
			if(StringUtil.isNotEmpty(data)){
				String[] infos=data.split("\\|");
				list=new ArrayList();
			for(int i=4;i<infos.length;i++)	{
				String datas=infos[i];
				paperAutoInvoice=new PaperAutoInvoice();
				paperAutoInvoice.setAutoInvoiceId(createBusinessId("P"));
				paperAutoInvoice.setTaxpayerNo(taxNo);
				paperAutoInvoice.setInstId(instId);
				paperAutoInvoice.setTaxDiskNo(keyNo);
				paperAutoInvoice.setInvoiceType(fapiaoType);
				paperAutoInvoice.setCurrentInvoiceCode(infos[1]);
				paperAutoInvoice.setCurrentInvoiceNo(infos[2]);
				//发票代码^起始号码^终止号码^发票份数^剩余份数^领购日期^领购人员
				//0			1		2			3		4		5		6
				if(StringUtil.isNotEmpty(datas)){
					String[] fapiaos=datas.split("\\^");
					paperAutoInvoice.setReceiveInvoiceTime(fapiaos[5]);
					paperAutoInvoice.setInvoiceCode(fapiaos[0]);
					paperAutoInvoice.setInvoiceBeginNo(fapiaos[1]);
					paperAutoInvoice.setInvoiceEndNo(fapiaos[2]);
					paperAutoInvoice.setInvoiceNum(fapiaos[3]);
					paperAutoInvoice.setSurplusNum(fapiaos[4]);
					paperAutoInvoice.setUserId(fapiaos[6]);
				}
				list.add(paperAutoInvoice);
				
				}
			}
			return list;
		
	}
	/**
	 * 生成业务主键（在插入数据时需要，由时间戳生成）
	 * 
	 * @return String
	 */
	protected String createBusinessId(String tabFlag) {
		if (tabFlag == null) {
			tabFlag = "";
		}
		String temp = DateUtils.serverCurrentTimeStamp();
		long random = Math.round(Math.random() * 110104 + 100000);
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return tabFlag + timeStamp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		} else {
			return tabFlag + temp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		}
	}
	private static String timeStamp = "";
	private static int busFlag = 1;

}
