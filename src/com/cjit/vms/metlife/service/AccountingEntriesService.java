package com.cjit.vms.metlife.service;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:会计分录  metlife
*/
import java.util.List;
import java.util.Map;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.AccountingEntriesInfo;


public interface AccountingEntriesService {

	List findAccountingEntriesInfo(AccountingEntriesInfo accountingEntriesInfo,
			PaginationList paginationList);

	List findAccountingEntriesReports(AccountingEntriesInfo accountingEntriesInfo);

	List findAccountingEntriesInfoSale(
			AccountingEntriesInfo accountingEntriesInfo,
			PaginationList paginationList);

	void insertInputInaccdet(List<Map<String, String>> dataList);

	List findaccountingPeriod();

	void findsaleAccountingEntriesInfo(
			AccountingEntriesInfo accountingEntriesInfo);

	List findAccountingEntriesInfoSale1(
			AccountingEntriesInfo accountingEntriesInfo);

	List findtoExcelAccountingEntriesInfo(
			AccountingEntriesInfo accountingEntriesInfo);

	void operationAccountingEntrise();

	List findAccountingEntriesReports1(
			AccountingEntriesInfo accountingEntriesInfo);

	List findAccountingToSun();

	void updateChangeFlag();

	void inputODS();

	String findXmlPath();



}
