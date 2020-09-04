package com.cjit.vms.metlife.service;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:收费凭证  metlife
*/
import java.util.List;

import com.cjit.common.util.PaginationList;
import com.cjit.vms.metlife.model.ChargesVoucherInfo;

public interface ChargesManageService {

	List findChargesVoucher(ChargesVoucherInfo chargesVoucherInfo, PaginationList paginationList);

}
