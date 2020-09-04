package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

import java.util.List;

/**
 * 查询领用存（返回报文）
 * @author john
 *
 */
public class QuerAndReceInfoResp extends CommonResp{
	
	private List<Record> records;

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

}
