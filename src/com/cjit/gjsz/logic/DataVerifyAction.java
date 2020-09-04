package com.cjit.gjsz.logic;

import java.util.Iterator;
import java.util.Map;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.model.SearchModel;
import com.cjit.gjsz.logic.model.VerifyModel;

public class DataVerifyAction extends BaseListAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6122843350525007512L;
	private VerifyService verifyService;
	private String orgId;

	public String batchVerify(){
		try{
			Map map = SearchModel.SEARCH_MAP;
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry) it.next();
				String tableId = (String) entry.getKey();
				System.out.println("==========================当时校验表名" + tableId
						+ "======================================");
				String[] businessIds = verifyService.getBusinessIds(orgId,
						tableId);
				if(businessIds != null && businessIds.length > 0){
					for(int i = 0; i < businessIds.length; i++){
						String[] temp = {businessIds[i]};
						VerifyModel flag = verifyService.verify(temp, tableId,
								interfaceVer, configIsCluster);
						if(flag != null){
							// 校验未通过
							// verifyService.updateStatus(businessIds[i],
							// tableId, "2");
							verifyService.updateStatus(businessIds[i], tableId,
									String.valueOf(DataUtil.JYWTG_STATUS_NUM));
						}else{
							// 校验通过
							// verifyService.updateStatus(businessIds[i],
							// tableId, "3");
							verifyService.updateStatus(businessIds[i], tableId,
									String.valueOf(DataUtil.JYYTG_STATUS_NUM));
						}
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public void setVerifyService(VerifyService verifyService){
		this.verifyService = verifyService;
	}

	public String getOrgId(){
		return orgId;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}
}
