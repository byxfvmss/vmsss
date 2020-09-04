package com.cjit.gjsz.logic;

import java.util.List;

import com.cjit.gjsz.logic.model.AddRunBank;
import com.cjit.gjsz.logic.model.VerifyModel;

public interface VerifyService{

	public VerifyModel verify(Object rptData, String tableId, String instCode,
			String interfaceVer, String isCluster);

	// public VerifyModel executeVerify(String tableId, String interfaceVer);
	public VerifyModel verify(String[] businessId, String tableId,
			String interfaceVer, String isCluster);

	public String[] getBusinessIds(String orgId, String tableId);

	public void updateStatus(String businessId, String tableId, String status);

	public void updateAddBank(AddRunBank addRunBank);

	public List getDatas(String businessIds, String sql);

	public List subList(String branchcode, String bussId);

	public boolean checkBusinessNoRepeat(String businessNo, String tableId,
			String fileType, String businessId);
}
