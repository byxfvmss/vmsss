package com.cjit.gjsz.logic;

import java.math.BigDecimal;
import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.logic.model.SearchModel;

public interface SearchService {

	public List search(SearchModel searchModel, PaginationList paginationList);

	public List search(SearchModel searchModel);

	public long getCount(SearchModel searchModel);

	public Object getDataVerifyModel(String tableId, String businessid);

	public Object getDataVerifyModel(String tableId, String rptNo,
			String buinsessNo);

	public List getChildren(String tableId, String businessid);

	public List getCfaChildren(String tableId, String businessid);

	public List getFalChildren(String tableId, String businessid);

	public long getDataVerifyModelSize(String tableId, String businessid);

	public long getVerifySize(String sql);

	public String getKey(String dataKey);

	public String getBackupNum(String dataKey);

	public List getCompanyInfos(String businessId, String custCode,
			String instCode);

	public BigDecimal getFalSumBigDecimal(SearchModel searchModel);
}
