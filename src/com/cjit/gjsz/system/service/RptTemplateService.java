package com.cjit.gjsz.system.service;

import java.util.List;
import java.util.Map;

import com.cjit.common.service.GenericService;

public interface RptTemplateService extends GenericService
{
	public Map findRptTemplate(String template_id,String safe_table_id);
	
	public List findRptTemplate();

	public void saveRptTemplate(String template_id,String template_name,byte[] template_content);
	
	public int getBeginDataRow(String saveTableId);
	
	public boolean isBlankColumn(String saveTableId,int column);

}
