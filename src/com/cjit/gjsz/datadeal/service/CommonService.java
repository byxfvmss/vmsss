/**
 * 
 */
package com.cjit.gjsz.datadeal.service;

import java.util.List;

/**
 * @author yulubin
 */
public interface CommonService{

	public List getDataStatusListForPageListDatas();

	public List getDataStatusListForPageListDatasAudit();

	public List getDataStatusListForPageListDatasLowerStatus();

	public List getResetDataStatusListForPageListDatasAudit();

	public List getResetDataStatusListForPageListLowerStatus();

	public List getAllStatus();

	public List getStobStatus();

	public String getStatusNameByStatusId(String statusId);
}
