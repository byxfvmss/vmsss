package com.cjit.gjsz.common.homenote.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.util.FileCopyUtils;

import cjit.fmss.dataexchange.ExchangeManager;
import cjit.fmss.vms.Version;

import com.cjit.common.constant.Constants;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.common.homenote.service.HomeDataService;
import com.cjit.gjsz.common.homenote.xml.HomeDataCellDO;
import com.cjit.gjsz.common.homenote.xml.HomeDataDO;
import com.cjit.gjsz.common.homenote.xml.HomeDataItemDO;
import com.cjit.gjsz.common.homenote.xml.HomeDataXmlSerial;
import com.cjit.gjsz.datadeal.action.DataDealAction;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.StatusCountInfo;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.storage.InvoiceAlertListInfo;
import com.cjit.vms.trans.util.DataUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

public class HomeDataXmlAction extends DataDealAction {

	private static final long serialVersionUID = -1L;
	private String maxRowNum;
	private String type;
	private HomeDataService homeDataService;
	private SqlMapClient sqlMapClient = null;

	public String getMaxRowNum() {
		return maxRowNum;
	}

	public void setMaxRowNum(String maxRowNum) {
		this.maxRowNum = maxRowNum;
	}

	public void setHomeDataService(HomeDataService homeDataService) {
		this.homeDataService = homeDataService;
	}

	/**
	 * <p>
	 * 方法名称: homeNoteData|描述: FMSS首页统计信息
	 * </p>
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String homeNoteData() throws Exception {
		if ("getVersion".equalsIgnoreCase(request.getParameter("type"))) {
			// 得到版本信息
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(Version.getVersion());
		} else {
			// FMSS首页需要处理的任务统计
			try {
				// 将参数配置项记录信息放在SESSION中
				this.initConfigParameters();
				// 事例
				String systemId = (String) this.request
						.getParameter("systemId");
				HomeDataXmlSerial serial = new HomeDataXmlSerial(systemId);
				HomeDataDO homeDataDO = new HomeDataDO();
				// 构造全局数据.
				User user = super.getCurrentUser();
				homeDataDO.setResultCode("vmss");
				homeDataDO.setResultUserId(user.getId());
				// 构造标签数据
				if (maxRowNum == null || maxRowNum == "") {
					maxRowNum = "5";
				}
				// 记录基础、申报、核销信息的"未校验笔数"、"未审核笔数"供页面显示
				List items4Label = new ArrayList();
				// 构造明细标题
				List cells4Thead = new ArrayList();
				// 构造明细行信息
				List rows4Tbody = new ArrayList();
				// 查询PUB_HOME_DIC表中所定义需在FMSS首页统计的信息项
				/*
				 * List homeDics = this.homeDataService.getHomeDic(null); if
				 * (homeDics != null && homeDics.size() > 0) { for (int i = 0; i <
				 * homeDics.size(); i++) { PubHomeDic pubHomeDic = (PubHomeDic)
				 * homeDics.get(i); if
				 * (StringUtil.isEmpty(pubHomeDic.getDicType()) ||
				 * StringUtil.isEmpty(pubHomeDic.getDicValue())) { continue; }
				 * if ("detail".equalsIgnoreCase(pubHomeDic.getDicType())) { //
				 * 以列表形式分机构显示待处理表单数 HomeDataCellDO cell = new HomeDataCellDO();
				 * cell.setName("填报机构"); cells4Thead.add(cell); cell = new
				 * HomeDataCellDO(); cell.setName("单据类型");
				 * cells4Thead.add(cell); cell = new HomeDataCellDO();
				 * cell.setName("详细信息"); cells4Thead.add(cell); // 明细中需统计的单据状态类型
				 * String[] dicValues = pubHomeDic.getDicValue() .split("-"); //
				 * 查询当前用户授权机构 User currentUser = (User) this
				 * .getFieldFromSession(Constants.USER); List authInstList =
				 * currentUser.getOrgs(); for (int j = 0; j <
				 * authInstList.size(); j++) { Organization org = (Organization)
				 * authInstList .get(j); String searchCondition = " and instCode = '" +
				 * org.getId() + "' and datastatus in ("; for (int k = 0; k <
				 * dicValues.length; k++) { if
				 * ("check".equalsIgnoreCase(dicValues[k])) { // 待校验
				 * searchCondition += DataUtil.WJY_STATUS_NUM + ","; } else if
				 * ("commit" .equalsIgnoreCase(dicValues[k])) { // 待提交
				 * searchCondition += DataUtil.JYYTG_STATUS_NUM + ","; } else if
				 * ("audit" .equalsIgnoreCase(dicValues[k])) { // 待审核
				 * searchCondition += DataUtil.YTJDSH_STATUS_NUM + ","; if
				 * ("yes" .equalsIgnoreCase(this.configDeleteNeedAudit)) {
				 * searchCondition += "-" + DataUtil.WJY_STATUS_NUM + ",-" +
				 * DataUtil.JYWTG_STATUS_NUM + ",-" + DataUtil.JYYTG_STATUS_NUM +
				 * ",-" + DataUtil.SHWTG_STATUS_NUM + ","; } } else if ("edit"
				 * .equalsIgnoreCase(dicValues[k])) { // 待处理 searchCondition +=
				 * DataUtil.JYWTG_STATUS_NUM + "," + DataUtil.SHWTG_STATUS_NUM +
				 * ","; } } searchCondition = searchCondition.substring(0,
				 * searchCondition.length() - 1) + ")"; // 查询信息个数 List list =
				 * this.homeDataService .getFalDatasCnt(user.getId(),
				 * searchCondition); if (CollectionUtil.isNotEmpty(list)) {
				 * String fileType = ""; String tableName = ""; int nCheck = 0;
				 * int nCommit = 0; int nAudit = 0; int nEdit = 0; for (Iterator
				 * r = list.iterator(); r .hasNext();) { RptData rd = (RptData)
				 * r.next(); if (!fileType.equalsIgnoreCase(rd .getFileType())) {
				 * if (!"".equals(fileType)) { StringBuffer sbMessage = new
				 * StringBuffer(); if (nCheck > 0) { sbMessage.append("待校验")
				 * .append(nCheck) .append("笔 "); } if (nCommit > 0) {
				 * sbMessage.append("待提交") .append(nCommit) .append("笔 "); } if
				 * (nAudit > 0) { sbMessage.append("待审核") .append(nAudit)
				 * .append("笔 "); } if (nEdit > 0) { sbMessage.append("待处理")
				 * .append(nEdit) .append("笔 "); } // 构造列数据 List cols4Tbody =
				 * new ArrayList(); // 填报机构 cell = new HomeDataCellDO();
				 * cell.setValue(org.getName()); cols4Tbody.add(cell); // 单据类型
				 * cell = new HomeDataCellDO(); cell.setValue(tableName);
				 * cols4Tbody.add(cell); // 详细信息 cell = new HomeDataCellDO();
				 * cell.setValue(sbMessage .toString().trim());
				 * cols4Tbody.add(cell); rows4Tbody.add(cols4Tbody); } //
				 * 开始新单据赋值 fileType = rd.getFileType(); tableName = DataUtil
				 * .getTableNameByFileType(rd .getFileType()); nCheck = 0;
				 * nCommit = 0; nAudit = 0; nEdit = 0; } if (String.valueOf(
				 * DataUtil.WJY_STATUS_NUM) .equals(rd.getDataStatus())) { //
				 * 待校验 nCheck = rd.getCountRow(); } else if (String.valueOf(
				 * DataUtil.JYYTG_STATUS_NUM) .equals(rd.getDataStatus())) { //
				 * 待提交 nCommit = rd.getCountRow(); } else if (String.valueOf(
				 * DataUtil.YTJDSH_STATUS_NUM) .equals(rd.getDataStatus()) ||
				 * Integer.valueOf( rd.getDataStatus()) .intValue() < 0) { //
				 * 待审核 nAudit = rd.getCountRow(); } else if (String.valueOf(
				 * DataUtil.JYWTG_STATUS_NUM) .equals(rd.getDataStatus()) ||
				 * String .valueOf( DataUtil.SHWTG_STATUS_NUM) .equals( rd
				 * .getDataStatus())) { // 待处理 nEdit = rd.getCountRow(); } } if
				 * (!"".equals(fileType)) { StringBuffer sbMessage = new
				 * StringBuffer(); if (nCheck > 0) {
				 * sbMessage.append("待校验").append( nCheck).append("笔 "); } if
				 * (nCommit > 0) { sbMessage.append("待提交").append(
				 * nCommit).append("笔 "); } if (nAudit > 0) {
				 * sbMessage.append("待审核").append( nAudit).append("笔 "); } if
				 * (nEdit > 0) { sbMessage.append("待处理").append(
				 * nEdit).append("笔 "); } // 构造列数据 List cols4Tbody = new
				 * ArrayList(); // 填报机构 cell = new HomeDataCellDO();
				 * cell.setValue(org.getName()); cols4Tbody.add(cell); // 单据类型
				 * cell = new HomeDataCellDO(); cell.setValue(tableName);
				 * cols4Tbody.add(cell); // 详细信息 cell = new HomeDataCellDO();
				 * cell.setValue(sbMessage.toString() .trim());
				 * cols4Tbody.add(cell); rows4Tbody.add(cols4Tbody); } } } }
				 * else { String searchCondition = ""; if
				 * ("check".equalsIgnoreCase(pubHomeDic .getDicType())) { // 待校验
				 * searchCondition = " and DATASTATUS = " +
				 * DataUtil.WJY_STATUS_NUM; } else if
				 * ("commit".equalsIgnoreCase(pubHomeDic .getDicType())) { //
				 * 待提交 searchCondition = " and DATASTATUS = " +
				 * DataUtil.JYYTG_STATUS_NUM; } else if
				 * ("audit".equalsIgnoreCase(pubHomeDic .getDicType())) { // 待审核
				 * searchCondition = " and (DATASTATUS = " +
				 * DataUtil.YTJDSH_STATUS_NUM + " or DATASTATUS < 0)"; } else if
				 * ("edit".equalsIgnoreCase(pubHomeDic .getDicType())) { // 待处理
				 * searchCondition = " and DATASTATUS in (" +
				 * DataUtil.JYWTG_STATUS_NUM + "," + DataUtil.SHWTG_STATUS_NUM +
				 * ")"; } // 查询信息个数 List list =
				 * this.homeDataService.getFalDatasCnt( user.getId(),
				 * searchCondition); if (CollectionUtil.isNotEmpty(list)) { for
				 * (Iterator r = list.iterator(); r.hasNext();) { RptData rd =
				 * (RptData) r.next(); HomeDataItemDO item = new
				 * HomeDataItemDO(); item.setName(pubHomeDic.getDicTypeName() +
				 * DataUtil .getTableNameByFileType(rd .getFileType()) + "笔数");
				 * item.setValue(String.valueOf(rd .getCountRow()));
				 * item.setUrl(pubHomeDic.getDicListTarget());
				 * item.setMenuId(pubHomeDic.getDicMenuId());
				 * items4Label.add(item); } } } } }
				 */
				items4Label = new ArrayList();
				cells4Thead = new ArrayList();
				rows4Tbody = new ArrayList();
				// 获取当前用户有权限处理的机构list
				User currentUser = (User) this
						.getFieldFromSession(Constants.USER);
				List orgIdList = new ArrayList();
				if (currentUser != null
						&& CollectionUtil.isNotEmpty(currentUser.getOrgs())) {
					List userOrgList = currentUser.getOrgs();
					for (int i = 0; i < userOrgList.size(); i++) {
						Organization org = (Organization) userOrgList.get(i);
						orgIdList.add(org.getId());
					}
				}
				if ("00802".equals(systemId)) {
					// 00802 OUTVAT VMS销项税管理
					// 查询提示信息
					items4Label = this.getHomeDataItemListForOutVat(orgIdList);
					// 查询发票预警信息列表
					List invoiceAlertList = this.homeDataService
							.getInvoicePaperAlert(orgIdList);
					if (CollectionUtil.isNotEmpty(invoiceAlertList)) {
						boolean isAlert = false;
						for (Iterator b = invoiceAlertList.iterator(); b
								.hasNext();) {
							InvoiceAlertListInfo iai = (InvoiceAlertListInfo) b
									.next();
							BigDecimal alertNum = StringUtil.isEmpty(iai
									.getAlertNum()) ? new BigDecimal(0)
									: new BigDecimal(iai.getAlertNum());
							BigDecimal stockNum = StringUtil.isEmpty(iai
									.getUnusedInvoiceNum()) ? new BigDecimal(0)
									: new BigDecimal(iai.getUnusedInvoiceNum());
							if (alertNum.compareTo(stockNum) >= 0) {
								isAlert = true;
								HomeDataCellDO cell = new HomeDataCellDO();
								List cols4Tbody = new ArrayList();
								cell = new HomeDataCellDO();
								cell.setValue(iai.getInstName());
								cols4Tbody.add(cell);
								cell = new HomeDataCellDO();
								if (DataUtil.VAT_TYPE_0.equals(iai
										.getInvoiceType())) {
									cell.setValue("增值税专用发票");
								} else if (DataUtil.VAT_TYPE_1.equals(iai
										.getInvoiceType())) {
									cell.setValue("增值税普通发票");
								}
								cols4Tbody.add(cell);
								cell = new HomeDataCellDO();
								cell.setValue(String.valueOf(alertNum));
								cols4Tbody.add(cell);
								cell = new HomeDataCellDO();
								cell.setValue(String.valueOf(stockNum));
								cols4Tbody.add(cell);
								rows4Tbody.add(cols4Tbody);
							}
						}
						if (isAlert) {
							HomeDataCellDO cell = new HomeDataCellDO();
							cell.setName("机构");
							cells4Thead.add(cell);
							cell = new HomeDataCellDO();
							cell.setName("票据类型");
							cells4Thead.add(cell);
							cell = new HomeDataCellDO();
							cell.setName("预警值");
							cells4Thead.add(cell);
							cell = new HomeDataCellDO();
							cell.setName("实际值");
							cells4Thead.add(cell);
						}
					}
				} else if ("00803".equals(systemId)) {
					// 00803 INVAT VMS进项税管理
					items4Label = this
							.getHomeDataItemListForInputVat(orgIdList);
				} else if ("00804".equals(systemId)) {
					// 00804 PARAM VMS参数管理
				} else if ("00805".equals(systemId)) {
					// 00805 DATALOAD VMS数据加载
				} else if ("00806".equals(systemId)) {
					// 00806 SYSTEM VMS系统管理
				} else if ("00807".equals(systemId)) {
					// 00807 MONITOR VMS税项监控
					HomeDataCellDO cell = new HomeDataCellDO();
					cell.setName("CNAME1");
					cells4Thead.add(cell);
					cell = new HomeDataCellDO();
					cell.setName("CNAME2");
					cells4Thead.add(cell);
					cell = new HomeDataCellDO();
					cell.setName("CNAME3");
					cells4Thead.add(cell);

					// 构造列数据
					List cols4Tbody = new ArrayList();
					cell = new HomeDataCellDO();
					cell.setValue("D11");
					cols4Tbody.add(cell);
					cell = new HomeDataCellDO();
					cell.setValue("D12");
					cols4Tbody.add(cell);
					cell = new HomeDataCellDO();
					cell.setValue("D13");
					cols4Tbody.add(cell);
					rows4Tbody.add(cols4Tbody);

					cols4Tbody = new ArrayList();
					cell = new HomeDataCellDO();
					cell.setValue("D21");
					cols4Tbody.add(cell);
					cell = new HomeDataCellDO();
					cell.setValue("D22");
					cols4Tbody.add(cell);
					cell = new HomeDataCellDO();
					cell.setValue("D23");
					cols4Tbody.add(cell);
					rows4Tbody.add(cols4Tbody);

				} else if ("00808".equals(systemId)) {
					// 00808 FJS VMS附加税管理
				} else if ("00810".equals(systemId)) {
					// 00810 SKP VMS税控盘管理
				}
				homeDataDO.setLabel(items4Label);
				homeDataDO.setThead(cells4Thead);
				homeDataDO.setTbody(rows4Tbody);
				InputStream is = null;
				XMLWriter xmlWriter = null;
				try {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					Document doc = serial.buildDocument(homeDataDO);
					OutputFormat xmlFormat = new OutputFormat();
					xmlFormat.setEncoding("UTF-8");
					xmlWriter = new XMLWriter(os, xmlFormat);
					xmlWriter.write(doc);
					is = new ByteArrayInputStream(os.toByteArray());
					response.addHeader("Content-Disposition",
							"attachment;filename=homeData.xml");
					response.setContentType("text/plain");
					FileCopyUtils.copy(is, response.getOutputStream());
				} catch (IOException ioe) {
					ioe.printStackTrace();
					log.error("HomeDataXmlAction-homeNoteData", ioe);
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error("HomeDataXmlAction-homeNoteData", ex);
				} finally {
					try {
						if (xmlWriter != null) {
							xmlWriter.close();
						}
						if (is != null) {
							is.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.error("HomeDataXmlAction-homeNoteData-finally", e);
					}
				}
			} catch (Exception exx) {
				exx.printStackTrace();
				log.error("HomeDataXmlAction-homeNoteData", exx);
			}
		}
		return null;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRes() {
		log.info("收到fmss请求");
		String reqType = request.getParameter("type");
		log.info("Type = " + reqType);
		if ("sql".equalsIgnoreCase(reqType)) {
			String sql = request.getParameter("sql");
			log.info("Sql = " + sql);
			System.out.println("Sql = " + sql);
			ExchangeManager ex = ExchangeManager.getInstance(this
					.getSqlMapClient().getDataSource());
			response.setContentType("text/xml;charset=gbk");
			// 新修改 返回流对象由 OutputStream 为 Writer,解决setContentType在部分特殊环境
			// 下不能生效的问题.
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(response.getOutputStream(),
						"gbk");
				ex.getSqlResultData(sql, writer);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				response.setCharacterEncoding("GBK");
				response
						.getOutputStream()
						.print(
								"<center><h1 ><font color='red'>对不起，你无权访问资源！</font></h1></center>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 销项税统计
	 * 
	 * @param orgIdList
	 *            当前用户授权机构ID
	 * @return List
	 */
	private List getHomeDataItemListForOutVat(List orgIdList) {
		List list = new ArrayList();
		// 查询交易相关信息
		TransInfo transInfo = new TransInfo();
		transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
		List transCntList = this.homeDataService.getTransCount(transInfo,
				orgIdList);
		if (CollectionUtil.isNotEmpty(transCntList)) {
			for (Iterator b = transCntList.iterator(); b.hasNext();) {
				StatusCountInfo sci = (StatusCountInfo) b.next();
				if (DataUtil.TRANS_STATUS_1.equals(sci.getDataStatus())) {
					// 未开票
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.TRANS_STATUS_1_CH + "交易笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.TRANS_STATUS_2.equals(sci.getDataStatus())) {
					// 开票编辑锁定中
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.TRANS_STATUS_2_CH + "交易笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.TRANS_STATUS_7.equals(sci.getDataStatus())) {
					// 冲抵待锁定
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.TRANS_STATUS_7_CH + "交易笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				}
			}
		}
		// 查询票据相关信息
		List billCntList = this.homeDataService.getBillCount(null, orgIdList);
		if (CollectionUtil.isNotEmpty(billCntList)) {
			for (Iterator b = billCntList.iterator(); b.hasNext();) {
				StatusCountInfo sci = (StatusCountInfo) b.next();
				if (DataUtil.BILL_STATUS_1.equals(sci.getDataStatus())) {
					// 编辑待提交
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_1_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_2.equals(sci.getDataStatus())) {
					// 提交待审核
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_2_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_7.equals(sci.getDataStatus())) {
					// 开具失败
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_7_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_9.equals(sci.getDataStatus())) {
					// 打印失败
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_9_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_13.equals(sci.getDataStatus())) {
					// 作废待审核
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_13_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_16.equals(sci.getDataStatus())) {
					// 红冲待审核
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_16_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.BILL_STATUS_20.equals(sci.getDataStatus())) {
					// 红冲待审核
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.BILL_STATUS_20_CH + "票据笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				}
			}
		}

		return list;
	}

	/**
	 * 进项税统计
	 * 
	 * @param orgIdList
	 *            当前用户授权机构ID
	 * @return List
	 */
	private List getHomeDataItemListForInputVat(List orgIdList) {
		List list = new ArrayList();
		// 查询进项发票相关信息
		List billCntList = this.homeDataService.getIuputInvoiceCount(orgIdList);
		if (CollectionUtil.isNotEmpty(billCntList)) {
			for (Iterator b = billCntList.iterator(); b.hasNext();) {
				StatusCountInfo sci = (StatusCountInfo) b.next();
				if (DataUtil.INPUT_INVOICE_STATUS_4.equals(sci.getDataStatus())) {
					// 首次认证未通过
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.INPUT_INVOICE_4_CH + "进项发票笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.INPUT_INVOICE_STATUS_6.equals(sci
						.getDataStatus())) {
					// 再次认证未通过
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.INPUT_INVOICE_6_CH + "进项发票笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.INPUT_INVOICE_STATUS_9.equals(sci
						.getDataStatus())) {
					// 退回
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.INPUT_INVOICE_9_CH + "进项发票笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				} else if (DataUtil.INPUT_INVOICE_STATUS_12.equals(sci
						.getDataStatus())) {
					// 红冲待审核
					HomeDataItemDO item = new HomeDataItemDO();
					item.setName(DataUtil.INPUT_INVOICE_12_CH + "进项发票笔数");
					item.setValue(String.valueOf(sci.getCount()));
					list.add(item);
				}
			}
		}
		return list;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
}