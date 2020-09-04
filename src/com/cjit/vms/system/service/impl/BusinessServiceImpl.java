package com.cjit.vms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.system.model.Business;
import com.cjit.vms.system.service.BusinessService;

public class BusinessServiceImpl extends GenericServiceImpl implements
		BusinessService {

	public List findBusinessList(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusiness", map);
	}

	public Business findBusiness(Business business) {
		List list = this.findBusinessList(business);
		if (list != null && list.size() == 1) {
			return (Business) list.get(0);
		} else {
			return null;
		}
	}

	public List findBusinessList(Business business, String userID,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusinessList", map, paginationList);
	}
	

	public List findBusinessMgtList(Business business,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusinessMgtList", map, paginationList);
	}

	public String loadInstAndUsrXmlEx(String instId, int level, User user,
			String reInit) {
		if ("Y".equals(reInit)) {// 当有参数reInit,根据页面输入框[机构ID],重新加载机构树
			return this.initTree(instId, level, user, reInit); // 第一次加载时，获取上级机构的清单-显示两层
		} else {
			if (StringUtils.isNotBlank(instId)) {
				// 异步加载该机构下包含的子机构
				return LoadSubInstTree(instId, true);
			} else {
				return this.initTree(instId, level, user, reInit); // 第一次加载时，获取上级机构的清单-显示两层
			}
		}
	}
	
	private String initTree(String instId, int level, User user,
			String reInit) {
		StringBuffer sb = new StringBuffer();
		Business inst = new Business();
		// 第一次加载时，获取上级机构的清单-显示两层
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<Response><Data><Tree>");
		List listInst = null;
		if ("Y".equals(reInit) && StringUtils.isNotEmpty(instId)) {
			listInst = this.getRootInsts(instId);
		} else {
			listInst = this.getRootInsts(user);
		}
		// 新增一个list，存放子机构的机构id(唯一)
		List instInstTemp1 = new ArrayList();
		for (int i = 0; i < listInst.size(); i++) {
			inst = (Business) listInst.get(i);
			// 避免在创建树的同时二次将子机构放进去
			if (instId == null || instId.equals("")) {
				if (instInstTemp1.contains(inst.getBusinessCode())) {
					continue;
				}
			}
			sb.append("<TreeNode name='");
			sb.append(inst.getBusinessCName()).append("[").append(
					inst.getBusinessCode()).append("]");
			sb.append("' id='");
			sb.append(inst.getBusinessCode());
			sb.append("' class='");
			sb.append(inst.getTaxRate());
			sb.append("' levelType='1' ");
			// FIXME: 此地方判断是否有子结点，需要优化
			List listInstTmp = this.getAllInstByParentInst(inst
					.getBusinessCode());
			if (listInstTmp != null && listInstTmp.size() > 0) {
				sb.append(" _hasChild='1' ");
				sb.append(" _opened='true' ");
				for (int j = 0; j < listInstTmp.size(); j++) {
					Business uinst = (Business) listInstTmp.get(j);
					// 存放子机构的机构id(唯一)
					instInstTemp1.add(uinst.getBusinessCode());
				}
			}
			sb.append(" _canSelect='1' ");
			sb.append("> ");
			if (listInstTmp != null && listInstTmp.size() > 0) {
				if (level == 2) {
					if (instId == null || instId.equals("")) {
						sb
								.append(LoadSubInstTree(inst.getBusinessCode(),
										false));
					}
				}
			}
			sb.append("</TreeNode>");
		}
		sb.append("</Tree></Data></Response>");
		return sb.toString();
	}
	
	public List getRootInsts(String instId) {
//		StringBuffer sb = new StringBuffer();
//		List busis = null;
//		sb.append("SELECT busi FROM Business busi where busi.businessCode like ? and busi.isUse=1 ");
//		sb.append(" order by busi.businessLayer,busi.orderNum,busi.businessParentCode desc,busi.businessCode");
//		busis = find(sb.toString(), "%" + instId + "%");
//		return busis;
		
		Map map = new HashMap();
		map.put("instId", instId);
		return find("findRootInstsByInstId", map);
	}
	
	/**
	 * <p>
	 * 方法名称: getRootInsts|描述: 返回所有根机构数据
	 * </p>
	 * 
	 * @param user
	 *            当前用户,控制分级授权
	 * @return 获取所有根机构列表
	 * 
	 * 
	 */
	public List getRootInsts(User user) {
//		StringBuffer sb = new StringBuffer();
//		List insts = null;
//		sb.append("SELECT busi FROM BusinessDO busi where 1=1 ");
//		// 控制分级授权
//		sb
//				.append(" and exists (select 1 from BusinessDO a where a.businessCode=busi.businessCode and  busi.businessParentCode is null and busi.isUse=1)");
//		sb.append(" order by busi.businessLayer,busi.businessParentCode desc");
//		if (user != null) {
//			insts = find(sb.toString());
//		} else {
//			insts = find(sb.toString());
//		}
//		return insts;
		
		Map map = new HashMap();
		map.put("user", user);
		return find("findRootInsts", map);
	}
	
	/**
	 * <p>
	 * 方法名称: LoadSubInstTree|描述:异步获取子机构列表
	 * </p>
	 * 
	 * @param instId
	 *            机构编号
	 * @param isHaveRoot
	 *            是否包含根节点
	 * @return 机构列表生成的XML文件
	 */
	private String LoadSubInstTree(String instId, boolean isHaveRoot) {
		StringBuffer sb = new StringBuffer();
		Business inst = new Business();
		if (StringUtils.isNotBlank(instId)) {
			// 异步加载该机构下包含的子机构
			List listInst = this.getAllInstByParentInst(instId);
			if (listInst != null) {
				// 如果是异步获取需要增加<data>节点，否则无法加载
				if (isHaveRoot)
					sb.append("<data>");
				// 新增一个list，存放子机构的机构id(唯一)
				List instInstTemp1 = new ArrayList();
				for (int i = 0; i < listInst.size(); i++) {
					inst = (Business) listInst.get(i);
					// 避免在创建树的同时二次将子机构放进去
					if (instInstTemp1.contains(inst.getBusinessCode())) {
						continue;
					}
					// FIXME: 此地方判断是否有子结点，需要优化
					List listInstTmp = this.getAllInstByParentInst(inst
							.getBusinessCode());
					sb.append("<TreeNode name='");
					sb.append(inst.getBusinessCName()).append("[").append(
							inst.getBusinessCode()).append("]");
					sb.append("' id='");
					sb.append(inst.getBusinessCode());
					sb.append("' class='");
					sb.append(inst.getTaxRate());
					sb.append("' levelType='2' ");
					
					if (listInstTmp != null && listInstTmp.size() > 0) {
						sb.append(" _hasChild='1' ");
						sb.append(" _opened='false' ");
						for (int j = 0; j < listInstTmp.size(); j++) {
							Business uinst = (Business) listInstTmp.get(j);
							// 存放子机构的机构id(唯一)
							instInstTemp1.add(uinst.getBusinessCode());
						}
					}
					sb.append(" _canSelect='1' ");
					sb.append("> ");
					sb.append("</TreeNode>");
				}
				if (isHaveRoot)
					sb.append("</data>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * <p>
	 * 方法名称: getAllInstByParentInst|描述:返回此机构的所属下级机构
	 * </p>
	 * 
	 * @param parentId
	 *            上级机构编号
	 * @return 机构列表
	 */
	public List getAllInstByParentInst(String parentId) {
		Map map = new HashMap();
		map.put("parentId", parentId);
		return find("findAllInstByParentInst", map);
	}

	public String loadInstAndUsrXmlEx(String instId, User user, String reInit) {
		return loadInstAndUsrXmlEx(instId, 2, user, reInit);
	}

	public void saveInst(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		this.save("saveInst", map);
	}
	
	

	public void saveMgt(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		this.save("saveMgt", map);
	}

	public void updateBusiness(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		this.save("updateBusiness", map);
	}
	
	

	public void updateBusinessMgt(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		this.save("updateBusinessMgt", map);
	}

	public Business getInstByInstId(String businessCode) {
		if (businessCode == null) {
			return null;
		}
		Business business = new Business();
		business.setBusinessCode(businessCode);
		List list = this.findBusinessCode(business);
		if (list != null && list.size() == 1) {
			return (Business) list.get(0);
		} else {
			return null;
		}
	}
	
	
	public Business getMgtById(String taxRateId) {
		if (taxRateId == null) {
			return null;
		}
		Business business = new Business();
		business.setTaxRateId(taxRateId);
		List list = this.findBusinessMgtList(business, new PaginationList());
		if (list != null && list.size() == 1) {
			return (Business) list.get(0);
		} else {
			return null;
		}
	}

	public List findBusinessCode(Business business) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusinessCode", map);
	}

	/**
	 * 重新交易设置路径
	 */
	public void updateBusinessRela1(Business business) {
		if(StringUtils.isNotEmpty(business.getBusinessParentCode())){
			Business parentBusi = (Business) getBusinessListByCodeObject(business.getBusinessParentCode());
			Map map = new HashMap();
			map.put("parentBusi", parentBusi);
			map.put("business", business);
			map.put("isHead", "false");
			if(parentBusi != null){
				map.put("businessPath", parentBusi.getBusinessPath() + business.getBusinessCode() + "\\");
				map.put("businessLayer",Integer.valueOf(parentBusi.getBusinessLayer().intValue()+1));
				this.save("updatePathAndLayer", map);
			}else{
				this.save("updatePath", map);
			}
		}else{
			Map map = new HashMap();
			map.put("business", business);
			map.put("isHead", "true");
			this.save("updatePathOne", map);
		}
		
	}
	
	public Object getBusinessListByCodeObject(String businessCode) {
		if (businessCode == null) {
			return null;
		}
		Map map = new HashMap();
		map.put("businessCode", businessCode);
		List insts = this.find("getBusinessListByCode", map);
		if (insts != null && insts.size() != 0) {
			return insts.get(0);
		}
		return null;
	}

	public Business getBusinessListByCode(String businessCode) {
		if (businessCode == null) {
			return null;
		}
		Business business = new Business();
		business.setBusinessCode(businessCode);
		List insts = this.findBusinessCode(business);
		if (insts != null && insts.size() != 0) {
			return (Business)insts.get(0);
		}
		return null;
	}

	public void updateBusinessIsUse(Business business) {
		Map map = new HashMap();
		map.put("isUse", "0");
		map.put("business", business);
		this.save("updateBusinessIsUse", map);		
	}
	
	

	public void deleteMgt(String taxRateId) {
		Map map = new HashMap();
		map.put("taxRateId", taxRateId);
		this.save("deleteMgt", map);	
	}

	public List findBusiAndBusiSup(Business business, String id,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusiAndBusiSup", map, paginationList);
	}

	public List findBusinessList(Business business,
			PaginationList paginationList) {
		Map map = new HashMap();
		map.put("business", business);
		return find("findBusiness", map,paginationList);
		
	}
}