package com.cjit.vms.metlife.service.Impl;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:会计分录  metlife
*/
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.service.AccountingEntriesService;
import com.cjit.vms.metlife.model.AccountingEntriesInfo;
import com.cjit.vms.system.model.UBaseSysParamVmss;

public class AccountingEntriesServiceImpl extends GenericServiceImpl implements AccountingEntriesService {
	 
	@Override
	//会计分录初始化&查询
	public List findAccountingEntriesInfo(AccountingEntriesInfo accountingEntriesInfo,PaginationList paginationList) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findAccountingEntriesInfo", map, paginationList);
	}
	@Override
	public List findAccountingEntriesReports(
			AccountingEntriesInfo accountingEntriesInfo) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findAccountingEntriesReports",map);
	}

	public List findAccountingEntriesReports1(
			AccountingEntriesInfo accountingEntriesInfo) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findAccountingEntriesReports1",map);
	}
	
	
	@Override
	public void insertInputInaccdet(List<Map<String, String>> dataList) {
	
		this.insertBatch("insertInputInaccdet", dataList);
	}

	@Override
	//销项
	public List findAccountingEntriesInfoSale(
			AccountingEntriesInfo accountingEntriesInfo,
			PaginationList paginationList) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findAccountingEntriesInfoSale", map, paginationList);
	}

	
	
	@Override
	public List findtoExcelAccountingEntriesInfo(
			AccountingEntriesInfo accountingEntriesInfo) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findtoExcelAccountingEntriesInfo", map);
	}

	@Override
	public List findAccountingEntriesInfoSale1(
			AccountingEntriesInfo accountingEntriesInfo) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		return find("findAccountingEntriesInfoSale1", map);
	}

	@Override
	public List findaccountingPeriod() {
		return find("findaccountingPeriod", null);
	}

	@Override
	public void findsaleAccountingEntriesInfo(
			AccountingEntriesInfo accountingEntriesInfo) {
		Map map=new HashMap();
		map.put("accountingEntriesInfo", accountingEntriesInfo);
		this.save("findsaleAccountingEntriesInfo", map);
		this.update("updateSaleAccountingEntriesInfo", map);
	}

	@Override
//	会计分录导入及数据处理
	public void operationAccountingEntrise() {
		try{
		Map map=new HashMap();
//		 -- 接口临时表 - 接口落地档  报销单号、会计科目、渠道类型、产品代码、项目代码、成本中心代码、分公司代码
		SimpleDateFormat date=new SimpleDateFormat("yyyyMMddHHmmss");
		String da=date.format(new Date());;
		map.put("batchnos", da);
		this.save("transToInaccdet_T", map);
		this.delete("deleteInaccdet", map);
		this.save("transToInaccdet_ALL", map);
		this.delete("deleteInaccdet_T",map);
		this.save("transToIncomeDetailInfo", map);
//		添加发票表
//		 --提取字段 插入发票临时表 如果SUBJECT_TYPE=固定资产 或者 视同销售 状态为已转出
		this.save("transToInvoiceInfo_t", map);
//		临时表插入发票表
		this.save("transToInvoiceInfo", map);
//		删除临时表
		this.delete("deleteIncoiceInfo_t", map);
//		添加销项会计分录
		this.save("transToSaleAccountDetails", map);
		}catch(Exception e){
			e.fillInStackTrace();
			
		}

	}

	@Override
	public List findAccountingToSun() {
		Map map=new HashMap();
		return find("findAccountingToSun",map);
	}

	@Override
	public void updateChangeFlag() {
		Map map=new HashMap();
		this.update("updateChangeFlag",map);
	}
	//ODS推送
	@Override
	public void inputODS() {
		// TODO Auto-generated method stub
	this.getJdbcTemplate().execute("call INPUT_EXP_CUS_REC()");
	}
	@Override
	public String findXmlPath() {
		Map map=new HashMap();
		UBaseSysParamVmss  uBaseSysParamVmss=new UBaseSysParamVmss();
		List path=find("findXmlPath",map);
		uBaseSysParamVmss=(UBaseSysParamVmss)path.get(0);
		
		return uBaseSysParamVmss.getXmlPath();
	}
	
	
	
}
