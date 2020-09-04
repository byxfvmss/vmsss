package com.cjit.vms.input.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.ExcelUtil;

import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.vms.input.model.SpecialBillWithhold;
import com.cjit.vms.input.service.SpecialBillService;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.util.JXLTool;

public class SpecialBillImportAction extends DataDealAction {

	private SpecialBillService specialBillService;
	private String billNo;
	private String writeData;
	private String subjectName;
	private String payData;
	private String taxInstChn;
	private String taxNo;
	private String belongDataS;// 税款所属时间-开始日期
	private String belongDataE;// 税款所属时间-结束日期
	private String dataStatus;
	private String[] checkedlineNo;// 多选id

	private List billClassList;// 画面票据类型下拉框
	private List billStatusList;
	private String billType;// 票据类型用于初始action画面跳转

	private String editBillNo;// 画面编辑、查看选择的号码
	private SpecialBillWithhold withholdBill;// 编辑查看画面实例
	private String isEdit;// 画面可否编辑控制 true 可编辑

	private Map getSearchMap() {
		Map parameters = new HashMap();
		parameters.put("billNo", billNo);
		parameters.put("writeData", writeData);
		parameters.put("subjectName", subjectName);
		parameters.put("payData", payData);
		parameters.put("taxInstChn", taxInstChn);
		parameters.put("taxNo", taxNo);
		parameters.put("belongDataS", belongDataS);
		parameters.put("belongDataE", belongDataE);
		parameters.put("dataStatus", "1");
		return parameters;
	}

	public String listSpecialBill() {

		if (null == billType) {
			return "init";
		}
		return SUCCESS;

	}

	public String listSpecialBill01() {

		Map parameters = getSearchMap();

		specialBillService
				.selectSpecialBillWithhold(parameters, paginationList);

		return SUCCESS;
	}

	public String importSpecialBill01() {
		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
		File[] files = mRequest.getFiles("theFile");
		if (files != null && files.length > 0) {
			try {
				if (!sessionInit(false))
					throw new Exception("初始化缓存数据失败!");
				doImportFile(files[0]);
				this.setResultMessages("上传文件完成!");
				files = null;
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				this.setResultMessages("上传文件失败:" + e.getMessage());
				return ERROR;
			}
		} else {
			this.setResultMessages("上传文件失败!");
			return ERROR;
		}
	}

	public String confrimSpecialBill01() {
		Map parameters = new HashMap();
		parameters.put("billNos", checkedlineNo);
		parameters.put("dataStatus", "2");
		if (checkedlineNo.length > 0) {
			specialBillService.updateSpecialBillWithholdStatus(parameters);
		}

		return SUCCESS;
	}

	public String exportSpecialBill01() throws Exception {
		Map parameters = getSearchMap();
		parameters.put("billNos", checkedlineNo);
		List dataList = specialBillService.selectSpecialBillWithhold(
				parameters, null);

		StringBuffer fileName = new StringBuffer("代扣代缴票据");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateStr = sf.format(new Date());
		fileName = fileName.append(dateStr);
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, dataList);
		os.flush();
		os.close();

		return null;
	}

	public String editSpecialBill01() {
		Map parameters = new HashMap();
		parameters.put("billNo", editBillNo);
		List dbdata = specialBillService.selectSpecialBillWithhold(parameters,
				null);
		if (null != dbdata) {
			withholdBill = (SpecialBillWithhold) dbdata.get(0);
		}
		return SUCCESS;
	}

	public String updateSpecialBill01() {
		Map parameters = new BeanMap(withholdBill);
		specialBillService.updateSpecialBillWithhold(parameters);
		return SUCCESS;
	}

	private void doImportFile(File file) throws Exception {

		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		String[][] sheet = (String[][]) hs.get("0");
		List dataList = new ArrayList();
		for (int i = 1; i < sheet.length; i++) {
			Map bean = new HashMap();
			String[] row = sheet[i];

			int k = 0;
			try {
				bean.put("billNo", row[k++]);
				bean.put("taxGov", row[k++]);
				bean.put("taxNo", row[k++]);
				bean.put("taxInstChn", row[k++]);
				bean.put("bankandname", row[k++]);
				bean.put("bankandaccount", row[k++]);
				bean.put("subjectId", row[k++]);
				bean.put("subjectName", row[k++]);
				bean.put("subjectClass", row[k++]);
				bean.put("nationalTre", row[k++]);
				bean.put("writeData", row[k++]);
				bean.put("belongDataS", row[k++]);
				bean.put("belongDataE", row[k++]);
				bean.put("payData", row[k++]);
				bean.put("taxAmtSum", row[k++]);
				bean.put("dataStatus", 1);
				bean.put("remark", row[k++]);
			} catch (Exception e) {
				throw new Exception("第" + i + "行字段列数不匹配！");
			}
			Map search = new HashMap();
			search.put("billNo", bean.get("billNo"));
			List dbData = specialBillService.selectSpecialBillWithhold(search,
					null);

			if (dbData.size() > 0) {
				throw new Exception("第" + i + "行税票票号数据已存在！");
			}
			dataList.add(bean);

		}
		specialBillService.insertSpecialBillWithhold(dataList);

	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;

		ws = wb.createSheet("代扣代缴通知书", 0);

		ws.addCell(new Label(i++, 0, "序号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "税票票号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "征收机关", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "缴款单位-代码(纳税人识别号)", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "缴款单位-全称(代缴人名称)", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "缴款单位-开户银行", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "缴款单位-账号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "预算科目-编码", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "预算科目-名称", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "预算科目-级次", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "收款国库", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "填发日期", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "税款所属时间-开始日期", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "税款所属时间-结束日期", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "税款限缴日期", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "合计税额", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "备注", JXLTool.getHeader()));

		for (int j = 0; j < i; j++) {
			ws.setColumnView(j, 25);
		}
		int count = 1;
		for (int c = 0; c < content.size(); c++) {
			Object bean = content.get(c);
			int column = count++;
			setWritableSheet(ws, bean, column);
		}
		wb.write();
		wb.close();
	}

	private void setWritableSheet(WritableSheet ws, Object bean, int column)
			throws WriteException {
		int i = 0;
		NumberFormat nf = new NumberFormat("#0.00");

		Label cell1 = new Label(i++, column, String.valueOf(column),
				JXLTool.getContentFormat());

		Label cell2 = new Label(i++, column, getProperty(bean, "billNo"),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, getProperty(bean, "taxGov"),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, getProperty(bean, "taxNo"),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, getProperty(bean, "taxInstChn"),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, getProperty(bean, "bankandname"),
				JXLTool.getContentFormat());
		Label cell7 = new Label(i++, column,
				getProperty(bean, "bankandaccount"), JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, getProperty(bean, "subjectId"),
				JXLTool.getContentFormat());
		Label cell9 = new Label(i++, column, getProperty(bean, "subjectName"),
				JXLTool.getContentFormat());
		Label cell10 = new Label(i++, column,
				getProperty(bean, "subjectClass"), JXLTool.getContentFormat());
		Label cell11 = new Label(i++, column, getProperty(bean, "nationalTre"),
				JXLTool.getContentFormat());
		Label cell12 = new Label(i++, column, getProperty(bean, "writeData"),
				JXLTool.getContentFormat());
		Label cell13 = new Label(i++, column, getProperty(bean, "belongDataS"),
				JXLTool.getContentFormat());
		Label cell14 = new Label(i++, column, getProperty(bean, "belongDataE"),
				JXLTool.getContentFormat());
		Label cell15 = new Label(i++, column, getProperty(bean, "payData"),
				JXLTool.getContentFormat());
		Label cell16 = new Label(i++, column, getProperty(bean, "taxAmtSum"),
				JXLTool.getContentFormat());
		Label cell18 = new Label(i++, column, getProperty(bean, "remark"),
				JXLTool.getContentFormat());

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
		ws.addCell(cell13);
		ws.addCell(cell14);
		ws.addCell(cell15);
		ws.addCell(cell16);
		ws.addCell(cell18);

	}

	private double toDouble(String value) {
		double d = 0;
		if (null != value && !"".equals(value)) {
			try {
				d = Double.parseDouble(value);
			} catch (Exception e) {
				d = 0;
			}

		}
		return d;
	}

	private String getProperty(Object bean, String name) {
		String value = "";
		try {
			value = BeanUtils.getProperty(bean, name);
		} catch (IllegalAccessException e) {

		} catch (InvocationTargetException e) {

		} catch (NoSuchMethodException e) {

		}
		return value;
	}

	// private String getProperty(String[] row,int index){
	//
	// BeanUtils.getIndexedProperty(row, name)
	// return null;
	// }

	public SpecialBillService getSpecialBillService() {
		return specialBillService;
	}

	public void setSpecialBillService(SpecialBillService specialBillService) {
		this.specialBillService = specialBillService;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getWriteData() {
		return writeData;
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

	public void setWriteData(String writeData) {
		this.writeData = writeData;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getPayData() {
		return payData;
	}

	public UserInterfaceConfigService getUserInterfaceConfigService() {
		return userInterfaceConfigService;
	}

	public void setUserInterfaceConfigService(
			UserInterfaceConfigService userInterfaceConfigService) {
		this.userInterfaceConfigService = userInterfaceConfigService;
	}

	public void setPayData(String payData) {
		this.payData = payData;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getTaxInstChn() {
		return taxInstChn;
	}

	public void setTaxInstChn(String taxInstChn) {
		this.taxInstChn = taxInstChn;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public List getBillStatusList() {
		return billStatusList;
	}

	public void setBillStatusList(List billStatusList) {
		this.billStatusList = billStatusList;
	}

	public String getBillType() {
		if (null == billType) {
			if (null != getBillClassList() && getBillClassList().size() > 0) {
				try {
					billType = BeanUtils.getProperty(getBillClassList().get(0),
							"billType");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return billType;
	}

	public void setBillType(String billType) {

		this.billType = billType;
	}

	public List getBillClassList() {
		if (null == billClassList) {
			billClassList = userInterfaceConfigService.getBillClassList();
		}

		return billClassList;
	}

	public void setBillClassList(List billClassList) {

		this.billClassList = billClassList;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public String getEditBillNo() {
		return editBillNo;
	}

	public SpecialBillWithhold getWithholdBill() {
		return withholdBill;
	}

	public void setWithholdBill(SpecialBillWithhold withholdBill) {
		this.withholdBill = withholdBill;
	}

	public void setEditBillNo(String editBillNo) {
		this.editBillNo = editBillNo;
	}

}
