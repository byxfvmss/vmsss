package com.cjit.vms.trans.service;

import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.trans.model.InSurtaxListInfo;
import com.cjit.vms.trans.model.VmsSurtaxInfo;


public interface InSurtaxService {
	public List findInSurtaxList(InSurtaxListInfo info,PaginationList paginationList);
	
	/**
	 * 附加税税种维护 新增/修改画面 保存/更新
	 * 
	 * @param updFlg 新增修改flag。                  0：新增，1：修改
	 * @param info
	 * 
	 * @author lee
	 */
	public void addOrUpdSurtaxTypeInfo(String updFlg, VmsSurtaxInfo info);
	
	/**
	 * 附加税税种维护 列表画面初期化/检索用情报检索
	 * 
	 * @param info
	 * @param paginationList
	 * @return
	 * @author lee
	 */
	public List getListSurtaxTypeInfo(VmsSurtaxInfo info, PaginationList paginationList);
	
	/**
	 * 附加税税率维护表情报检索
	 * 
	 * @param info
	 * @return
	 * @author lee
	 */
	public List getVmsSurtaxInfo(VmsSurtaxInfo info);
	
	/**
	 * 附加税税种维护 列表画面 删除
	 * 
	 * @param taxpayerId
	 * @param surtaxType
	 * @return
	 * @author lee
	 */
	public void delSurtaxTypeInfo(String taxpayerId, String surtaxType);
	
}
