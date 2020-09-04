package com.cjit.vms.metlife.action;

import com.cjit.vms.metlife.service.InvoicePrintingService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 * Created by Abel on 2016/4/12.
 */
public class InvoicePrintingAction extends DataDealAction{

    private BillInfo billInfo;
    private String billId;
    private InvoicePrintingService invoicePrintingService;


    public String InvoicePrintingBillInfo(){
        try{

            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            return ERROR;
        }
    }

    public BillInfo getBillInfo() {
        return billInfo;
    }

    public void setBillInfo(BillInfo billInfo) {
        this.billInfo = billInfo;
    }

    public InvoicePrintingService getInvoicePrintingService() {
        return invoicePrintingService;
    }

    public void setInvoicePrintingService(InvoicePrintingService invoicePrintingService) {
        this.invoicePrintingService = invoicePrintingService;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }
}
