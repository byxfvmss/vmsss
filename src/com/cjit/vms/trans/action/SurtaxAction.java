package com.cjit.vms.trans.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.vms.trans.service.SurtaxService;
import com.cjit.vms.trans.util.JXLTool;

public class SurtaxAction extends DataDealAction {

	SurtaxService surtaxService;

	private List datailList;
	private Map mapSurtaxAmtType;
	// 画面值
	private String taxPerNumber;// 纳税人识别号
	private String taxperName; // 纳税人名称
	private String surtaxType;// 附加税税种
	private String surtaxRate;// 附加税税率
	private String applyPeriod;// 申请周期
	private int[] checkedlineNo;

	public String listSurtax() {
		mapSurtaxAmtType = this.vmsCommonService
				.findCodeDictionary("SURTAX_AMT_TYPE");

		if (null == applyPeriod) {
			applyPeriod = getBeforMonth();
		}

		Map map = new HashMap();
		map.put("taxPerNumber", taxPerNumber);
		map.put("taxperName", taxperName);
		map.put("surtaxType", surtaxType);
		map.put("surtaxRate", surtaxRate);
		map.put("applyPeriod", applyPeriod);

		List authInstList = new ArrayList();
		getAuthInstList(authInstList);
		map.put("auth_inst_ids", authInstList);

		surtaxService.selectSurtaxAMT(map, paginationList);
		countSurtax(paginationList.getRecordList());
		return SUCCESS;
	}

	public String exportSurtaxList() throws Exception {

		Map map = new HashMap();

		map.put("taxPerNumber", taxPerNumber);
		map.put("taxperName", taxperName);
		map.put("surtaxType", surtaxType);
		map.put("surtaxRate", surtaxRate);
		map.put("applyPeriod", applyPeriod);

		List authInstList = new ArrayList();
		getAuthInstList(authInstList);
		map.put("auth_inst_ids", authInstList);

		List list = surtaxService.selectSurtaxAMT(map, null);
		countSurtax(list);
		// 画面勾选 导出勾选的 不勾选导出全部
		List exportList = new ArrayList();
		if (null != checkedlineNo && checkedlineNo.length > 0) {
			for (int i = 0; i < checkedlineNo.length; i++) {
				Object bean = null;
				try {
					bean = list.get(checkedlineNo[i]);
				} catch (IndexOutOfBoundsException e) {
					continue;
				}
				exportList.add(bean);
			}
		} else {
			exportList = list;
		}

		StringBuffer fileName = new StringBuffer("附加税");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		String dateStr = sf.format(new Date());
		fileName = fileName.append(dateStr);
		fileName.append(".xls");
		String name = "attachment;filename="
				+ URLEncoder.encode(fileName.toString(), "UTF-8").toString();
		response.setHeader("Content-type", "application/vnd.ms-excel");
		response.setHeader("Content-Disposition", name);
		OutputStream os = response.getOutputStream();
		writeToExcel(os, exportList);
		os.flush();
		os.close();

		return null;

	}

	/***
	 * 获取上月 yyyy--MM
	 * 
	 * @return
	 */
	private String getBeforMonth() {
		//
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);

		return sf.format(c.getTime());
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		int i = 0;

		ws = wb.createSheet("附加税", 0);

		ws.addCell(new Label(i++, 0, "序号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "纳税人识别号", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "纳税人名称", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "附加税类型", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "销项附加税税额", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "进项附加税税额", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "应缴附加税税额", JXLTool.getHeader()));
		ws.addCell(new Label(i++, 0, "机构名称", JXLTool.getHeader()));

		for (int j = 0; j < 9; j++) {
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
		double surtaxAMT = toDouble(getProperty(bean, "surtaxAMT"));
		double inSurtaxAMT = toDouble(getProperty(bean, "inSurtaxAMT"));
		double payurtaxAMT = toDouble(getProperty(bean, "payurtaxAMT"));

		Label cell1 = new Label(i++, column, String.valueOf(column),
				JXLTool.getContentFormat());
		Label cell2 = new Label(i++, column, getProperty(bean, "taxpernumber"),
				JXLTool.getContentFormat());
		Label cell3 = new Label(i++, column, getProperty(bean, "taxpername"),
				JXLTool.getContentFormat());
		Label cell4 = new Label(i++, column, getProperty(bean, "surtax_name"),
				JXLTool.getContentFormat());
		
		
		Number cell5 = new Number(i++, column, surtaxAMT,
				JXLTool.getContentFormat(nf));
		Number cell6 = new Number(i++, column, inSurtaxAMT,
				JXLTool.getContentFormat(nf));
		Number cell7 = new Number(i++, column, payurtaxAMT,
				JXLTool.getContentFormat(nf));
		Label cell8 = new Label(i++, column, getProperty(bean, "inst_name"),
				JXLTool.getContentFormat());
		
		
		
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
		ws.addCell(cell8);

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

	/***
	 * 根据附加税类型 动态取得金额字段
	 * @param recordList
	 */
	private void countSurtax(List recordList) {

		// 跟据附加税类型 获取对应金额
		for (int i = 0; i < recordList.size(); i++) {
			Object bean = recordList.get(i);

			String inSurtaxAMT = "";
			String surtaxAMT = "";
			String payurtaxAMT = "";
			// model字段名拼接
			String inSurtaxCellNameStart = "sum_in_surtax";
			String inSurtaxCellNameEnd = "_amt_cny";
			String surtaxCellNameStart = "sum_surtax";
			String surtaxCellNameEnd = "_amt_cny";

			try {

				String surtax_type = BeanUtils.getProperty(bean, "surtax_type");
				String surtax_rate = BeanUtils.getProperty(bean, "surtax_rate");
				// 进项字段名
				String inAmtKey = inSurtaxCellNameStart + surtax_type
						+ inSurtaxCellNameEnd;
				// 销项字段名
				String AmtKey = surtaxCellNameStart + surtax_type
						+ surtaxCellNameEnd;

				inSurtaxAMT = BeanUtils.getProperty(bean, inAmtKey);
				surtaxAMT = BeanUtils.getProperty(bean, AmtKey);
				// 乘税率
				BigDecimal inSurtaxAMTDec = new BigDecimal(inSurtaxAMT);
				BigDecimal surtaxAMTDec = new BigDecimal(surtaxAMT);
				BigDecimal payurtaxAMTDec = new BigDecimal("0");

//				inSurtaxAMTDec = inSurtaxAMTDec.multiply(new BigDecimal(
//						surtax_rate));
//				surtaxAMTDec = surtaxAMTDec
//						.multiply(new BigDecimal(surtax_rate));
				payurtaxAMTDec = surtaxAMTDec.subtract(inSurtaxAMTDec);
				if (payurtaxAMTDec.compareTo(new BigDecimal("0"))<0) {
					payurtaxAMTDec = new BigDecimal("0");
				}

				inSurtaxAMTDec = inSurtaxAMTDec.setScale(2,
						BigDecimal.ROUND_HALF_UP);
				surtaxAMTDec = surtaxAMTDec.setScale(2,
						BigDecimal.ROUND_HALF_UP);
				payurtaxAMTDec = payurtaxAMTDec.setScale(2,
						BigDecimal.ROUND_HALF_UP);

				inSurtaxAMT = inSurtaxAMTDec.toString();
				surtaxAMT = surtaxAMTDec.toString();
				payurtaxAMT = payurtaxAMTDec.toString();

				BeanUtils.setProperty(bean, "inSurtaxAMT", inSurtaxAMT);
				BeanUtils.setProperty(bean, "surtaxAMT", surtaxAMT);
				BeanUtils.setProperty(bean, "payurtaxAMT", payurtaxAMT);

			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}
	};

	public SurtaxService getSurtaxService() {
		return surtaxService;
	}

	public void setSurtaxService(SurtaxService surtaxService) {
		this.surtaxService = surtaxService;
	}

	public List getDatailList() {
		return datailList;
	}

	public void setDatailList(List datailList) {
		this.datailList = datailList;
	}

	public Map getMapSurtaxAmtType() {
		return mapSurtaxAmtType;
	}

	public void setMapSurtaxAmtType(Map mapSurtaxAmtType) {
		this.mapSurtaxAmtType = mapSurtaxAmtType;
	}

	public String getTaxPerNumber() {
		return taxPerNumber;
	}

	public void setTaxPerNumber(String taxPerNumber) {
		this.taxPerNumber = taxPerNumber;
	}

	public String getTaxperName() {
		return taxperName;
	}

	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}

	public String getSurtaxType() {
		return surtaxType;
	}

	public void setSurtaxType(String surtaxType) {
		this.surtaxType = surtaxType;
	}

	public String getSurtaxRate() {
		return surtaxRate;
	}

	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}

	public String getApplyPeriod() {
		return applyPeriod;
	}

	public void setApplyPeriod(String applyPeriod) {
		this.applyPeriod = applyPeriod;
	}

	public int[] getCheckedlineNo() {
		return checkedlineNo;
	}

	public void setCheckedlineNo(int[] checkedlineNo) {
		this.checkedlineNo = checkedlineNo;
	}

}
