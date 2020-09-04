package com.cjit.vms.trans.service.storage.server;

import java.text.ParseException;
import java.util.List;

public interface InvoiceManagementService {
	public  List CreatePaperAutoInvoiceList(String data,String taxNo,String keyNo,String instId,String fapiaoType) throws ParseException;
}
