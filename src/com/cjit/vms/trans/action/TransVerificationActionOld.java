package com.cjit.vms.trans.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
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
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import cjit.crms.util.DictionaryCodeType;
import cjit.crms.util.ExcelUtil;

import com.cjit.common.util.DictionaryUtil;
import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.interfacemanager.service.UserInterfaceConfigService;
import com.cjit.vms.trans.model.TransVerification;
import com.cjit.vms.trans.service.TransVerificationService;
import com.cjit.vms.trans.util.JXLTool;

public class TransVerificationActionOld extends DataDealAction {

	TransVerificationService transVerificationService;
	private List taxRateList;
	private List taxTypeList;
	private List inlandFlagList;
	private String[] checkedlineNo;// 多选id

	private TransVerification transVerification = new TransVerification();
	private TransVerification form = new TransVerification();
	private String id;

	public String listTransVerification() {
		// userInterfaceConfigService.getDictionarys(codeType, codeSym)
		// Map parameters = new HashMap();
		BeanMap parameters = new BeanMap(form);
		transVerificationService.selectTransVerification(parameters,
				paginationList);
		return SUCCESS;
	}

	public String saveTransVerification() {
		String formId = transVerification.getId();
		// BeanMap obj = new BeanMap(transVerification);
		Map obj = new HashMap();
		obj.put("obj", transVerification);
		
		if (StringUtils.isEmpty(formId)) {
			Map parameters = new HashMap();
			parameters.put("verificationType", transVerification.getVerificationType());
			parameters.put("taxType", transVerification.getTaxType());
			List dblist = transVerificationService.selectTransVerification(parameters, null);
			if (null!=dblist&&dblist.size()>0) {
				setResultMessages("交易类型已存在");
				return ERROR;
			}
			transVerificationService.insertTransVerification(obj);
			setResultMessages("新增成功");
		} else {
			transVerificationService.updateTransVerification(obj);
			setResultMessages("修改成功");
		}
		
		
		return SUCCESS;
	}

	public String deleteTransVerification() {
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			Map parameters = new HashMap();
			parameters.put("ids", checkedlineNo);
			transVerificationService.deleteTransVerification(parameters);
		} else {
			setResultMessages("请先选择一条明细");

		}
		return SUCCESS;
	}

	public String editTransVerification() {

		if (!StringUtils.isEmpty(id)) {
			Map parameters = new HashMap();
			parameters.put("id", id);
			List list = transVerificationService.selectTransVerification(
					parameters, null);
			if (list.size() > 0) {
				transVerification = (TransVerification) list.get(0);
			} else {
				setResultMessages("选择的明细已不存在");
				return ERROR;
			}

		}
		return SUCCESS;
	}

	public String importTransVerification() throws Exception {

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

	public String exportTransVerification() throws Exception {
		Map parameters = new HashMap();
		parameters = BeanUtils.describe(form);
		parameters.put("ids", checkedlineNo);
		List dataList = transVerificationService.selectTransVerification(
				parameters, null);

		StringBuffer fileName = new StringBuffer("交易认定");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
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

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;

		ws = wb.createSheet("交易认定", 0);

		/*ws.addCell(new Label(i++, 0, "序号", JXLTool.getHeader()));*/
		ws.addCell(new Label(i++, 0, "交易认定类型", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "交易认定名称", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "商品编号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "商品名称", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "交易发生地", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "纳税人类型", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "税率", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "备注", JXLTool.getHeader()));

		for (int j = 0; j <= i; j++) {
			ws.setColumnView(j, 18);
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

//		Label cell1 = new Label(i++, column, String.valueOf(column),
//				JXLTool.getContentFormat());

		Label cell2 = new Label(i++, column, getProperty(bean,
				"verificationType"), JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, getProperty(bean,
				"verificationName"), JXLTool.getContentFormat());
		
		Label cell7 = new Label(i++, column, getProperty(bean, "goodsNo"),
				JXLTool.getContentFormat());
		Label cell8 = new Label(i++, column, getProperty(bean, "goodsName"),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column,
				getProperty(bean, "inlandFlagName"), JXLTool.getContentFormat());

		Label cell9 = new Label(i++, column, getProperty(bean, "taxTypeName"),
				JXLTool.getContentFormat());
		Label cell5 = new Label(i++, column, getProperty(bean, "taxRateName"),
				JXLTool.getContentFormat());
		Label cell6 = new Label(i++, column, getProperty(bean, "ramark"),
				JXLTool.getContentFormat());

//		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);
		ws.addCell(cell9);

	}

	private void doImportFile(File file) throws Exception {

		Hashtable hs = ExcelUtil.parseExcel(null, file, 1);
		String[][] sheet = (String[][]) hs.get("0");
		List dataList = new ArrayList();
		
		/*中文名数据库对应信息加载*/
		// 交易发生地校验map
		Map inlandMap = new HashMap();
		for (int i = 0; i < getInlandFlagList().size(); i++) {
			SelectTag s = (SelectTag) inlandFlagList.get(i);
			inlandMap.put(s.getText(), s.getValue());
		}
		// 税率
		Map taxRateMap = new HashMap();
		for (int i = 0; i < getTaxRateList().size(); i++) {
			SelectTag s = (SelectTag) taxRateList.get(i);
			taxRateMap.put(s.getText(),s.getValue() );
		}
		Map taxTypeMap = new HashMap();
		for (int i = 0; i < getTaxTypeList().size(); i++) {
			SelectTag s = (SelectTag) taxTypeList.get(i);
			taxTypeMap.put(s.getText(), s.getValue());
		}
		
		/*循环校验*/
		for (int i = 1; i < sheet.length; i++) {
			Map bean = new HashMap();
			String[] row = sheet[i];

			int k = 0;
			try {

				bean.put("verificationType", row[k++]);
				bean.put("verificationName", row[k++]);
				bean.put("goodsNo", row[k++]);
				bean.put("goodsName", row[k++]);
				bean.put("inlandFlag", row[k++]);
				bean.put("taxType", row[k++]);
				bean.put("taxRate", row[k++]);
				bean.put("ramark", row[k++]);
			} catch (Exception e) {
				throw new Exception("第" + i + "行字段列数不匹配！");
			}

			// 字段空校验 备注已外必须输入
			String[] keys = (String[]) bean.keySet().toArray(new String[0]);
			for (int j = 0; j < keys.length; j++) {
				if ("ramark".equals(keys[j])) {
					continue;
				}
				if (null == bean.get(keys[j])) {
					throw new Exception("第" + i + "行除备注外都不可为空，请检查数据！");
				} else {

					if (StringUtils.isEmpty((String) bean.get(keys[j]))) {
						throw new Exception("第" + i + "行除备注外都不可为空，请检查数据！");
					}
				}
			}
			// 交易发生地合法校验、中文转Y、N
			if (null == inlandMap.get(bean.get("inlandFlag"))) {
				throw new Exception("第" + i + "行交易发生地未维护，请检查数据！");
			} else {
				bean.put("inlandFlag", inlandMap.get(bean.get("inlandFlag")));
			}
			// 税率合法校验、格式化
			if (null == taxRateMap.get(bean.get("taxRate"))) {

				throw new Exception("第" + i + "行税率类型未维护，请检查数据！");
			} else {
				bean.put("taxRate", taxRateMap.get(bean.get("taxRate")));
			}
			//纳税人类别
			if (null == taxTypeMap.get(bean.get("taxType"))) {
				
				throw new Exception("第" + i + "行纳税人类别未维护，请检查数据！");
			} else {
				bean.put("taxType", taxTypeMap.get(bean.get("taxType")));
			}
			
			/*Map search = new HashMap();
			search.put("verificationType", bean.get("verificationType"));
			List dbData = transVerificationService.selectTransVerification(
					search, null);

			if (dbData.size() > 0) {
				throw new Exception("第" + i + "行税票票号数据已存在！");
			}*/
			dataList.add(bean);

		}
		for (int i = 0; i < dataList.size(); i++) {

			Map map = new HashMap();
			map.put("obj", dataList.get(i));
			transVerificationService.insertTransVerification(map);
		}

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

	public TransVerification getTransVerification() {
		return transVerification;
	}

	public void setTransVerification(TransVerification transVerification) {
		this.transVerification = transVerification;
	}

	public TransVerificationService getTransVerificationService() {
		return transVerificationService;
	}

	public void setTransVerificationService(
			TransVerificationService transVerificationService) {
		this.transVerificationService = transVerificationService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransVerification getForm() {
		return form;
	}

	public void setForm(TransVerification form) {
		this.form = form;
	}

	public List getTaxRateList() {
		if (null == taxRateList) {
			taxRateList = this.createSelectList(DictionaryCodeType.TAX_TYPE, null,
					false);
		}
		return taxRateList;
	}

	public void setTaxRateList(List taxRateList) {
		this.taxRateList = taxRateList;
	}

	public List getInlandFlagList() {
		if (null == inlandFlagList) {
			inlandFlagList = this.createSelectList(DictionaryCodeType.INLAND_FLAG, null, false);
		}
		return inlandFlagList;
	}

	public void setInlandFlagList(List inlandFlagList) {
		this.inlandFlagList = inlandFlagList;
	}

	public String[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(String[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

	public List getTaxTypeList() {
		if (null == taxTypeList) {
			taxTypeList = this.createSelectList("TAX_TYPE", null, false);
		}
		return taxTypeList;
	}

	public void setTaxTypeList(List taxTypeList) {
		this.taxTypeList = taxTypeList;
	}

}
