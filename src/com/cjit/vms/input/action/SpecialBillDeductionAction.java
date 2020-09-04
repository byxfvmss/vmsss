package com.cjit.vms.input.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cjit.common.util.JXLTool;
import com.cjit.gjsz.interfacemanager.model.BillClass;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.vms.input.model.SpecialBillWithhold;
import com.cjit.vms.input.service.SpecialBillService;
import com.cjit.vms.trans.action.DataDealAction;

public class SpecialBillDeductionAction extends DataDealAction {
	private UserInterfaceConfigService userInterfaceConfigService;
	private SpecialBillService specialBillService;
	private SpecialBillWithhold specialBillWithhold=new SpecialBillWithhold();
	private BillClass billclass=new BillClass();
	private List specialBillInfoList;
	private String billId;
	private List specialbilltype;
	public String billNo;//税票号码
	public String taxGov;//征收机关
	public String taxNo;//缴款单位-代码(纳税人识别号)
	public String taxInstChn;//缴款单位-全称(代缴人名称)
	public String bankandname;//缴款单位-开户银行
	public String bankandaccount;//缴款单位-账号
	public String subjectId;//预算科目-编码
	public String subjectName;//预算科目-名称
	public String subjectClass;//预算科目-级次
	public String nationalTre;//收款国库
	public String writeData;//填发日期
	public String belongDataS;//税款所属时间-开始日期
	public String belongDataE;//税款所属时间-结束日期
	public String payData;//税款限缴日期
	public String taxAmtSum;//合计税额
	public String dataStatus;//票据状态
	public String remark;//备注
	private String[] billid;
	public String inputSpecialbillDeduction() {
		if ("menu".equalsIgnoreCase(fromFlag)) {
			fromFlag = null;
			specialBillWithhold=new SpecialBillWithhold();
		}
		specialbilltype = userInterfaceConfigService.getBillClassList();
		
		specialBillWithhold=new SpecialBillWithhold();
		specialBillWithhold.setBillNo(this.getBillNo());
		specialBillWithhold.setWriteData(this.getWriteData());
		specialBillWithhold.setPayData(this.getPayData());
		specialBillWithhold.setTaxNo(this.getTaxNo());
		specialBillWithhold.setTaxInstChn(this.getTaxInstChn());
		specialBillWithhold.setDataStatus(this.getDataStatus());
		System.out.println(this.getWriteData()+this.getPayData()+this.getTaxNo()+this.getTaxInstChn()+this.getDataStatus());
		specialBillService.selectSpecialbillDeduction(specialBillWithhold, paginationList);
		
		return SUCCESS;
	}
	public String toEditSpecialBill(){
		String billno=request.getParameter("billid");
		specialBillWithhold=new SpecialBillWithhold();
		specialBillWithhold.setBillNo(billno);
		List specialbilltype=specialBillService.selectSpecialbillDeduction(specialBillWithhold, paginationList);
		specialBillWithhold=(SpecialBillWithhold) specialbilltype.get(0);
		return SUCCESS;
	}
	public String edtieSpecialBill(){
		System.out.println(request.getParameter("billid"));
		Map map=new HashMap();
		map.put("billNo",request.getParameter("billid"));
		map.put("dataStatus",this.getDataStatus());
		specialBillService.updateSpecialbillDeduction(map);
		return SUCCESS;
	}
	public String viewSpecialbillDetail(){
		
		String billno=request.getParameter("billid");
		specialBillWithhold=new SpecialBillWithhold();
		specialBillWithhold.setBillNo(billno);
		List specialbilltype=specialBillService.selectSpecialbillDeduction(specialBillWithhold, paginationList);
		specialBillWithhold=(SpecialBillWithhold) specialbilltype.get(0);
		return SUCCESS;
	}
	public void exportSpecialbillDeduction() throws Exception{
		specialBillWithhold=new SpecialBillWithhold();
		specialBillInfoList=specialBillService.selectSpecialbillDeductionExport(specialBillWithhold);
		System.out.println("specialBillInfoList"+specialBillInfoList.size());
		StringBuffer fileName = new StringBuffer("代扣代缴列表");
		fileName.append(".xls");
		String name = "attachment;filename=" + URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, specialBillInfoList);
		os.flush();
		os.close();
	}
	public void  writeToExcel(OutputStream os, List specialBillInfoList ) throws Exception{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;
		ws = wb.createSheet("代扣代缴列表", 0);
		Label header1 = new Label(i++, 0, "序号", JXLTool.getHeader());
		Label header2 = new Label(i++, 0, "税票票号", JXLTool.getHeader());
		Label header3 = new Label(i++, 0, "缴款单位代码", JXLTool.getHeader());
		Label header4 = new Label(i++, 0, "缴款单位全称", JXLTool.getHeader());
		Label header5 = new Label(i++, 0, "科目编码", JXLTool.getHeader());
		Label header6 = new Label(i++, 0, "科目名称", JXLTool.getHeader());
		Label header7 = new Label(i++, 0, "填发日期", JXLTool.getHeader());
		Label header8 = new Label(i++, 0, "税款开始时间", JXLTool.getHeader());
		Label header9 = new Label(i++, 0, "税款结束时间", JXLTool.getHeader());
		Label header10 = new Label(i++, 0, "税款限缴日期", JXLTool.getHeader());
		Label header11 = new Label(i++, 0, "发票状态", JXLTool.getHeader());
		Label header12 = new Label(i++, 0, "实缴金额", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header7);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header8);
		ws.addCell(header9);
		ws.addCell(header10);
		ws.addCell(header11);
		ws.addCell(header12);
		JXLTool.setAutoWidth(ws, setwriteWidth(specialBillInfoList));
		int count = 1;
		System.out.println(specialBillInfoList.size());
		for(int c = 0; c < specialBillInfoList.size(); c++){
			specialBillWithhold  = (SpecialBillWithhold) specialBillInfoList.get(c);
			int column = count++;
			setWritableSheet(ws, specialBillWithhold, column);
		}
		wb.write();
		wb.close();
	}
	public List setwriteWidth(List list) throws JXLException{
		 String Status = null;
		 List rowlist=null;
		 List sheetList=new ArrayList();
		 specialBillWithhold=null;
		for(int i=0;i<list.size();i++){
			rowlist=new ArrayList();
			specialBillWithhold=(SpecialBillWithhold) list.get(i);
			rowlist.add(Integer.toString(i));
			if(specialBillWithhold.getDataStatus().equalsIgnoreCase("2")){
				Status="已提交";
			}
			if(specialBillWithhold.getDataStatus().equalsIgnoreCase("3")){
				Status="待抵扣";
			}
			if(specialBillWithhold.getDataStatus().equalsIgnoreCase("4")){
				Status="已抵扣";
			}
			if(specialBillWithhold.getDataStatus().equalsIgnoreCase("5")){
				Status="不可抵扣";
			}
			System.out.println(Status);
			rowlist.add(specialBillWithhold.getBillNo());
			rowlist.add(specialBillWithhold.getTaxNo());
			rowlist.add(specialBillWithhold.getTaxInstChn());
			rowlist.add(specialBillWithhold.getSubjectId());
			rowlist.add(specialBillWithhold.getSubjectName());
			rowlist.add(specialBillWithhold.getWriteData());
			rowlist.add(specialBillWithhold.getBelongDataS());
			rowlist.add(specialBillWithhold.getBelongDataE());
			rowlist.add(specialBillWithhold.getPayData());
			rowlist.add(Status);
			rowlist.add(specialBillWithhold.getTaxAmtSum());
			sheetList.add(rowlist);
		}
		return sheetList;
	}
	private void setWritableSheet(WritableSheet ws,SpecialBillWithhold specialBillWithhold, int column)throws WriteException{
		int i = 0;
		String Status="";
		if(specialBillWithhold.getDataStatus().equalsIgnoreCase("2")){
			Status="已提交";
		}
		if(specialBillWithhold.getDataStatus().equalsIgnoreCase("3")){
			Status="待抵扣";
		}
		if(specialBillWithhold.getDataStatus().equalsIgnoreCase("4")){
			Status="已抵扣";
		}
		if(specialBillWithhold.getDataStatus().equalsIgnoreCase("5")){
			Status="不可抵扣";
		}
		Label cell1 = new Label(i++, column, Integer.toString(column),JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, specialBillWithhold.getBillNo(), JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, specialBillWithhold.getTaxNo(), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, specialBillWithhold.getTaxInstChn(), JXLTool.getContentFormatDateFormat());
		Label cell5 = new Label(i++, column, specialBillWithhold.getSubjectId(), JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, specialBillWithhold.getSubjectName(), JXLTool.getContentFormatNumberFloat());
		Label cell7 = new Label(i++, column, specialBillWithhold.getWriteData(), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, specialBillWithhold.getBelongDataS(), JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, specialBillWithhold.getBelongDataE(), JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column, specialBillWithhold.getPayData(), JXLTool.getContentFormat());
		Label cell11= new Label(i++, column, Status, JXLTool.getContentFormatFloat());
		Label cell12= new Label(i++, column, specialBillWithhold.getTaxAmtSum(), JXLTool.getContentFormatDateFormat());
		
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);
		ws.addCell(cell10);
		ws.addCell(cell11);
		ws.addCell(cell12);
	}
	
	
	public String specialbillRollBack(){
		try {
		Map map=new HashMap();
		map.put("billNos", this.getBillid());
		map.put("dataStatus", "1");
			specialBillService.updateSpecialBillWithholdStatus(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SUCCESS;
	}
	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}


	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}


	public SpecialBillWithhold getSpecialBillWithhold() {
		return specialBillWithhold;
	}


	public void setSpecialBillWithhold(SpecialBillWithhold specialBillWithhold) {
		this.specialBillWithhold = specialBillWithhold;
	}


	public BillClass getBillclass() {
		return billclass;
	}


	public void setBillclass(BillClass billclass) {
		this.billclass = billclass;
	}


	


	public String[] getBillid() {
		return billid;
	}
	public void setBillid(String[] billid) {
		this.billid = billid;
	}
	public List getSpecialBillInfoList() {
		return specialBillInfoList;
	}
	public void setSpecialBillInfoList(List specialBillInfoList) {
		this.specialBillInfoList = specialBillInfoList;
	}
	public String getBillId() {
		return billId;
	}


	public void setBillId(String billId) {
		this.billId = billId;
	}


	public List getSpecialbilltype() {
		return specialbilltype;
	}


	public void setSpecialbilltype(List specialbilltype) {
		this.specialbilltype = specialbilltype;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTaxGov() {
		return taxGov;
	}

	public void setTaxGov(String taxGov) {
		this.taxGov = taxGov;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getTaxInstChn() {
		return taxInstChn;
	}

	public void setTaxInstChn(String taxInstChn) {
		this.taxInstChn = taxInstChn;
	}

	public String getBankandname() {
		return bankandname;
	}

	public void setBankandname(String bankandname) {
		this.bankandname = bankandname;
	}

	public String getBankandaccount() {
		return bankandaccount;
	}

	public void setBankandaccount(String bankandaccount) {
		this.bankandaccount = bankandaccount;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}

	public String getNationalTre() {
		return nationalTre;
	}

	public void setNationalTre(String nationalTre) {
		this.nationalTre = nationalTre;
	}

	public String getWriteData() {
		return writeData;
	}

	public void setWriteData(String writeData) {
		this.writeData = writeData;
	}

	public String getBelongDataS() {
		return belongDataS;
	}

	public void setBelongDataS(String belongDataS) {
		this.belongDataS = belongDataS;
	}

	public String getBelongDataE() {
		return belongDataE;
	}

	public void setBelongDataE(String belongDataE) {
		this.belongDataE = belongDataE;
	}

	public String getPayData() {
		return payData;
	}

	public void setPayData(String payData) {
		this.payData = payData;
	}

	public String getTaxAmtSum() {
		return taxAmtSum;
	}

	public void setTaxAmtSum(String taxAmtSum) {
		this.taxAmtSum = taxAmtSum;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SpecialBillService getSpecialBillService() {
		return specialBillService;
	}


	public void setSpecialBillService(SpecialBillService specialBillService) {
		this.specialBillService = specialBillService;
	}
}
