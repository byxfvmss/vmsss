package com.cjit.gjsz.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.service.RptTemplateService;

public class RptTemplateServiceImpl extends GenericServiceImpl implements RptTemplateService{

	private final String[] fiveBegin=new String[]{"QDIICURRACCM","QDIISTOCKM","QDIIFUNDD","QFIIASSETSM","QFIICNYACCM","QFIICURRACCM","QFIIFUNDD","RQFIICNYACCM","RQFIISTOCKM","RQFIIASSETM"};
	//String[] fourBegin=new String[]{"STOCKACCM","STOCKACCOPENCLOSEM","QDIIACCD","QFIIPROFITY","QFIIASSETSY","QFIIACCD","RQFIIASSETY","RQFIIPROFITY","RQFIIFUNDD"};
	private Map blankColunm=new HashMap();
	
	public RptTemplateServiceImpl()
	{
		blankColunm.put("STOCKACCOPENCLOSEM",new int[]{2});
		blankColunm.put("QDIIFUNDD",new int[]{3});
		blankColunm.put("QFIIPROFITY",new int[]{8,10,12,14,16,18,20,22,24,26,28,30,32,34,36});
		blankColunm.put("QFIIASSETSY",new int[]{8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42});
		blankColunm.put("QFIIASSETSM",new int[]{9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69,71});
		blankColunm.put("QFIICNYACCM",new int[]{10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56});
		blankColunm.put("QFIIFUNDD",new int[]{4});
		blankColunm.put("QFIIACCD",new int[]{4,13,14,15,16,17,18,19,20,23,24,25,26,27,28,29,30});
		blankColunm.put("RQFIIASSETY",new int[]{6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,40,50,52,54,56,58,60,62,64,66});
		blankColunm.put("RQFIIPROFITY",new int[]{6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40});
		blankColunm.put("RQFIICNYACCM",new int[]{9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69,71,73,75});
		blankColunm.put("RQFIISTOCKM",new int[]{9,11,13,15,19,21,25,27,29});
		blankColunm.put("RQFIIASSETM",new int[]{7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69,71,73,75,77,79,81,83,85,87,89,91,93,95,97,99,101,103,105,107,109,111,113});
		blankColunm.put("RQFIIFUNDD",new int[]{6,11,13,15,17,19,20,22});
	}
	
	public Map findRptTemplate(String template_id,String safe_table_id) {
		Map param=new HashMap();
		param.put("template_id", template_id);
		param.put("safe_table_id", safe_table_id);
		List t=this.find("findRptTemplate", param);
		if(t.size()>0) return (Map)t.get(0);
		else return null;
	}

	public List findRptTemplate() {
		return this.find("findRptTemplate", null);
	}
	
	public void saveRptTemplate(String template_id, String template_name,
			byte[] template_content) {
		
		Map param=new HashMap();
		param.put("template_id", template_id);
		param.put("template_name", template_name);
		param.put("template_content", template_content);
		this.update("saveRptTemplate", param);
	}
	
	public int getBeginDataRow(String saveTableId) {
		int begin=4;
		for(int i =0;i<fiveBegin.length;i++){
			if(fiveBegin[i].equals(saveTableId)) begin=5;
		}
		return begin;
	}
	public boolean isBlankColumn(String saveTableId,int column)
	{
		int[] columnList=(int[])blankColunm.get(saveTableId);
		if(columnList!=null)
			for(int i =0;i<columnList.length;i++){
				if(columnList[i]==column) return true;
			}
		return false;
	}
}