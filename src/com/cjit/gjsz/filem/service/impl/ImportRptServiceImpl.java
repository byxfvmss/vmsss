package com.cjit.gjsz.filem.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.XmlUtil;
import com.cjit.gjsz.datadeal.model.RptColumnInfo;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.filem.service.ImportRptService;
import com.cjit.gjsz.logic.model.CompanyOpenInfo;
import com.cjit.gjsz.logic.model.CustomDeclare;
import com.cjit.gjsz.logic.model.ExportInfo;
import com.cjit.gjsz.logic.model.InvcountrycodeInfo;
import com.opensymphony.util.BeanUtils;

public class ImportRptServiceImpl extends GenericServiceImpl implements
		ImportRptService{

	public static final String RECORDS = "RECORDS";
	public static final String REC = "REC";

	/**
	 * 解析导入的xml文件
	 * @param file 导入报文
	 * @param rptColumnList 列信息
	 * @param tableId 数据表名
	 * @param sb 反馈信息记录
	 * @return List
	 */
	public List resolveImportXmlFile(File file, List rptColumnList,
			String tableId, StringBuffer sb){
		List list = null;
		try{
			// 解析导入XML文件
			Document doc = XmlUtil.parse(file);
			Element root = doc.getRootElement();
			if(root != null){
				Element errRecords = root.element(RECORDS);
				if(errRecords != null){
					List recList = errRecords.elements(REC);
					if(CollectionUtil.isNotEmpty(recList)){
						list = new ArrayList();
						for(int i = 0; i < recList.size(); i++){
							Element rec = (Element) recList.get(i);
							if(rec != null){
								RptData rptData = new RptData();
								int cFlag = 0;
								for(Iterator c = rptColumnList.iterator(); c
										.hasNext();){
									RptColumnInfo columnInfo = (RptColumnInfo) c
											.next();
									// 赋别名c1,c2,c3
									columnInfo
											.setAliasColumnId("c" + (++cFlag));
									if("TRADEDATE".equals(columnInfo
											.getColumnId())
											|| "BATCHNO".equals(columnInfo
													.getColumnId())){
										continue;
									}
									// System.out.println("=="
									// + columnInfo.getColumnId() + "=="
									// + columnInfo.getAliasColumnId()
									// + "==" + columnInfo.getDataType());
									if(!"table"
											.equals(columnInfo.getDataType())){
										BeanUtils.setValue(rptData, columnInfo
												.getAliasColumnId(), rec
												.element(
														columnInfo
																.getColumnId())
												.getTextTrim());
									}else{
										// 判断报文属性，分别解析其子表信息
										this.resolveSubjectElement(tableId,
												rec, rptData);
									}
									if("RPTNO".equals(columnInfo.getColumnId())){
										BeanUtils.setValue(rptData, "rptNo",
												rec.element("RPTNO")
														.getTextTrim());
									}
									if("CUSTCODE".equals(columnInfo
											.getColumnId())){
										BeanUtils.setValue(rptData, "custcode",
												rec.element("CUSTCODE")
														.getTextTrim());
									}
								}
								list.add(rptData);
							}
						}
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return list;
	}

	/**
	 * <p> 方法名称: resolveSubjectElement|描述: 判断报文属性，分别解析其子表信息 </p>
	 * @param tableId 报文主表表名
	 * @param rec 导入报文节点元素
	 * @param rptData 报文主表信息
	 */
	private void resolveSubjectElement(String tableId, Element rec,
			RptData rptData){
		if("t_fini_export".equals(tableId)
				|| "t_fini_dom_export".equals(tableId)){
			// 核销信息 出口收汇核销专用联（境外收入）/ 出口收汇核销专用联（境内收入）
			// 出口收汇核销单号码 t_export_info
			Element recRefnos = rec.element("REFNOS");
			if(recRefnos != null){
				List refnoList = recRefnos.elements("REFNO");
				if(refnoList != null && refnoList.size() > 0){
					List listExportInfo = new ArrayList();
					for(int i = 0; i < refnoList.size(); i++){
						Element recSubject = (Element) refnoList.get(i);
						if(recSubject != null){
							ExportInfo exportInfo = new ExportInfo();
							String refno = recSubject.getTextTrim();
							exportInfo.setRefno(refno);
							listExportInfo.add(exportInfo);
						}
					}
					rptData.setListExportInfo(listExportInfo);
				}
			}
		}else if("t_fini_remit".equals(tableId)
				|| "t_fini_dom_remit".equals(tableId)){
			// 核销信息 境外汇款申请书 / 境内汇款申请书
			// 报关单信息 t_customs_decl
			Element rexCustomsDecl = rec.element("CUSTOMS");
			if(rexCustomsDecl != null){
				List custDeclList = rexCustomsDecl.elements("CUSTOM");
				if(custDeclList != null && custDeclList.size() > 0){
					List listCustomDeclare = new ArrayList();
					for(int i = 0; i < custDeclList.size(); i++){
						Element recCustom = (Element) custDeclList.get(i);
						if(recCustom != null){
							CustomDeclare custDecl = new CustomDeclare();
							String customn = recCustom.element("CUSTOMN")
									.getTextTrim();
							String custccy = recCustom.element("CUSTCCY")
									.getTextTrim();
							String custamt = recCustom.element("CUSTAMT")
									.getTextTrim();
							String offamt = recCustom.element("OFFAMT")
									.getTextTrim();
							custDecl.setCustomn(customn);
							custDecl.setCustccy(custccy);
							if(custamt != null && !"".equals(custamt)){
								BigInteger amt = BigInteger.valueOf(Long
										.valueOf(custamt).longValue());
								custDecl.setCustamt(amt);
							}
							if(offamt != null && !"".equals(offamt)){
								BigInteger amt = BigInteger.valueOf(Long
										.valueOf(offamt).longValue());
								custDecl.setOffamt(amt);
							}
							listCustomDeclare.add(custDecl);
						}
					}
					rptData.setListCustomDeclare(listCustomDeclare);
				}
			}
		}else if("t_company_info".equals(tableId)){
			// 单位基本信息
			// 投资国别代码 t_invcountrycode_info
			Element recInvCountry = rec.element("INVCOUNTRY");
			if(recInvCountry != null){
				List invList = recInvCountry.elements("INVCOUNTRY");
				if(invList != null && invList.size() > 0){
					List listInvcountrycodeInfo = new ArrayList();
					for(int i = 0; i < invList.size(); i++){
						Element recSubject = (Element) invList.get(i);
						if(recSubject != null){
							InvcountrycodeInfo invCountry = new InvcountrycodeInfo();
							String invcountrycode = recSubject.element(
									"INVCOUNTRYCODE").getTextTrim();
							invCountry.setInvcountrycode(invcountrycode);
							listInvcountrycodeInfo.add(invCountry);
						}
					}
					rptData.setListInvcountrycodeInfo(listInvcountrycodeInfo);
				}
			}
			// 开户信息 t_company_openinfo
			Element recIpenIndo = rec.element("BANKINFOS");
			if(recIpenIndo != null){
				List openList = recIpenIndo.elements("BANKINFO");
				if(openList != null && openList.size() > 0){
					List listCompanyOpenInfo = new ArrayList();
					for(int i = 0; i < openList.size(); i++){
						Element recSubject = (Element) openList.get(i);
						if(recSubject != null){
							CompanyOpenInfo openInfo = new CompanyOpenInfo();
							String branchcode = recSubject
									.element("BRANCHCODE").getTextTrim();
							String contact = recSubject.element("CONTACT")
									.getTextTrim();
							String tel = recSubject.element("TEL")
									.getTextTrim();
							String fax = recSubject.element("FAX")
									.getTextTrim();
							openInfo.setBranchcode(branchcode);
							openInfo.setContact(contact);
							openInfo.setTel(tel);
							openInfo.setFax(fax);
							listCompanyOpenInfo.add(openInfo);
						}
					}
					rptData.setListCompanyOpenInfo(listCompanyOpenInfo);
				}
			}
		}
	}
}