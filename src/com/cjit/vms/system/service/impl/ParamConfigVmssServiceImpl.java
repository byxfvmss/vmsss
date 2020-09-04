package com.cjit.vms.system.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.system.model.UBaseConfig;
import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.system.service.ParamConfigVmssService;
import com.cjit.vms.taxdisk.single.model.BillCancel;
import com.cjit.vms.trans.util.DataUtil;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:03:08
 * @描述: [ThemeService]主题风格功能服务类
 */
public class ParamConfigVmssServiceImpl extends GenericServiceImpl implements
		ParamConfigVmssService {

	public List findSystemId() {
		Map map = new HashMap();
		List tempList = find("findSystemId", map);
		List list = new LinkedList();
		for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
			String systemId = (String) iterator.next();
			if (!list.contains(systemId)) {
				list.add(systemId);
			}
		}
		return list;
	}

	public Map getParamsMap(List configs) throws Exception {
		Map map = new LinkedHashMap();
		try {
			for (int i = 0; i < configs.size(); i++) {
				StringBuffer sb = new StringBuffer();
				String systemId = (String) configs.get(i);
				sb.append("<table class='editblock' width=100% border=0 id='"
						+ systemId + "'>");

				UBaseConfig ubsc = getConfig(systemId);
				// String itemCname=ubc.getItemCname();
				Map map1 = new HashMap();
				map1.put("systemId", systemId);
				List childParams = find("findByTop", map1);
				sb.append("<tr>");
				/*sb
						.append("<td align='right' valign='middle' height='35' style='padding-right:17px;'><input type='button' class='tbl_query_button' onMouseMove='this.className='tbl_query_button_on'' onMouseOut='this.className='tbl_query_button'' onclick='findOutSubmit()' value='保存' /></td>  </tr><tr><td>");*/
				sb.append("</tr><tr><td>");
				sb.append("<table width=100% border=0 id='" + "'>");
				Iterator ite = childParams.iterator();
				while (ite.hasNext()) {
					UBaseSysParamVmss childParam = (UBaseSysParamVmss) ite
							.next();
					String value = childParam.getSelectedValue();
					String paramId = String.valueOf(childParam.getParamId());
					String itemKey = String.valueOf(childParam.getItemKey());
					if ("00802".equals(systemId)) {
						if ("WAY_OF_MAKE_OUT_AN_INVOICE".equals(itemKey)
								|| "KIND_OF_MAKE_OUT_AN_INVOICE"
										.equals(itemKey)) {
							// continue;
						}
					}
					String itemCname = childParam.getItemCname();
					sb
							.append("<tr><td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb.append(itemCname);
					sb.append("</td><td width=100%>");
					sb.append(this.getInputHtml(value, paramId, itemKey));
					sb.append("</td>");
					sb.append("</tr>");
				}
				sb.append("</table></td></tr>");
				// }
				sb.append("</table>");
				map.put(ubsc.getSystemId() + "#" + ubsc.getSystemCname(), sb
						.toString());// modify by wangxin 20091110 修改为取系统中文简称
				// }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return map;
	}

	/**
	 * @param systemId
	 * @return
	 */
	public UBaseConfig getConfig(String systemId) {
		Map map = new HashMap();
		map.put("systemId", systemId);
		List list1 = find("finduBaseconfigBysytemId", map);
		UBaseConfig ubase = new UBaseConfig();
		if (list1.size() > 0) {
			ubase = (UBaseConfig) list1.get(0);
		}
		return ubase;
	}

	private String getInputHtml(String value, String paramId, String itemKey) {
		if (value == null) {
			value = " ";
		}
		StringBuffer sbInput = new StringBuffer();
		if ("TAXPARAMETERS".equalsIgnoreCase(itemKey)) {
			// 为“税控参数”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >").append("<option value='0'");
			if ((value != null ? value.trim() : value).equals("0")) {
				sbInput.append("selected");
			}
			sbInput.append(">百旺税控盘</option><option value='1'");
			if ((value != null ? value.trim() : value).equals("1")) {
				sbInput.append("selected");
			}
			sbInput.append(">百旺服务器 </option><option value='2'");
			if ((value != null ? value.trim() : value).equals("2")) {
				sbInput.append("selected");
			}
			sbInput.append(">航信单机版</option><option value='3'");
			if ((value != null ? value.trim() : value).equals("3")) {
				sbInput.append("selected");
			}
			sbInput.append(">航信服务器版</option>");
			sbInput.append("</select>");
		} else if ("WAY_OF_MAKE_OUT_AN_INVOICE".equalsIgnoreCase(itemKey)) {
			// 为“开票方式”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >");
			sbInput.append("<option value='").append(DataUtil.W_S).append("' ");
			if (DataUtil.W_S.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >单笔</option>");
			sbInput.append("<option value='").append(DataUtil.W_M).append("' ");
			if (DataUtil.W_M.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >合并</option>");
		} else if ("KIND_OF_MAKE_OUT_AN_INVOICE".equalsIgnoreCase(itemKey)) {
			// 为“开票种类”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >");
			sbInput.append("<option value='").append(DataUtil.K_A).append("' ");
			if (DataUtil.K_A.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >全部</option>");
			sbInput.append("<option value='").append(DataUtil.K_G).append("' ");
			if (DataUtil.K_G.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >普票</option>");
			sbInput.append("<option value='").append(DataUtil.K_S).append("' ");
			if (DataUtil.K_S.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >专票</option>");
		} else if ("IS_AUTO_INVOICE".equalsIgnoreCase(itemKey)) {
			// 为“自动开票”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >");
			sbInput.append("<option value='").append(DataUtil.YES).append("' ");
			if (DataUtil.YES.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >是</option>");
			sbInput.append("<option value='").append(DataUtil.NO).append("' ");
			if (DataUtil.NO.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >否</option>");
		} else if ("BILL_STATUS".equalsIgnoreCase(itemKey)) {
			// 为“票据状态”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >");
			// 提交待审核
			sbInput.append("<option value='").append(DataUtil.BILL_STATUS_2)
					.append("' ");
			if (DataUtil.BILL_STATUS_2.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.BILL_STATUS_2_CH).append(
					"</option>");
			// 无需审核
			sbInput.append("<option value='").append(DataUtil.BILL_STATUS_4)
					.append("' ");
			if (DataUtil.BILL_STATUS_4.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.BILL_STATUS_4_CH).append(
					"</option>");
			// 开具
			sbInput.append("<option value='").append(DataUtil.BILL_STATUS_5)
					.append("' ");
			if (DataUtil.BILL_STATUS_5.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.BILL_STATUS_5_CH).append(
					"</option>");
		} else if ("SEPARATOR".equalsIgnoreCase(itemKey)) {
			sbInput
			.append(
					"<input type='text'  class='tbl_query_text' value='")
			.append(value).append("'  name='param_" )
			.append(paramId).append("' >" );
			/*	// 为“分录流水分隔符”配置项单独处理
			sbInput
					.append(
							"<select style='text-algin:right; width:135px' name='param_")
					.append(paramId).append("' >");
			// 分隔符|
			sbInput.append("<option value='").append(DataUtil.SEPARATOR_1)
					.append("' ");
			if (DataUtil.SEPARATOR_1.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.SEPARATOR_1).append(
					"</option>");
			// 分隔符||
			sbInput.append("<option value='").append(DataUtil.SEPARATOR_2)
					.append("' ");
			if (DataUtil.SEPARATOR_2.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.SEPARATOR_2).append(
					"</option>");
			// 分隔符/
			sbInput.append("<option value='").append(DataUtil.SEPARATOR_3)
					.append("' ");
			if (DataUtil.SEPARATOR_3.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.SEPARATOR_3).append(
					"</option>");
			// 分隔符//
			sbInput.append("<option value='").append(DataUtil.SEPARATOR_4)
					.append("' ");
			if (DataUtil.SEPARATOR_4.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.SEPARATOR_4).append(
					"</option>");
			// 分隔符*
			sbInput.append("<option value='").append(DataUtil.SEPARATOR_5)
					.append("' ");
			if (DataUtil.SEPARATOR_5.equals(value.trim())) {
				sbInput.append("selected");
			}
			sbInput.append(" >").append(DataUtil.SEPARATOR_5).append(
					"</option>");*/
		} else {
			sbInput
					.append(
							" <input style='text-algin:right' class='tbl_query_text' name='param_")
					.append(paramId)
					.append("' value='")
					.append((value != null ? value.trim() : value))
					.append("' id='param_")
					.append(paramId)
					.append("' style='width:100px'")
					.append(
							" onblur='onBlurs(this.value,this.id,this.name);' ><span id = 'p_")
					.append(paramId).append("'></span> ");
		}
		return sbInput.toString();
	}

	public List findParamBytop(String systemId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * 方法名称: saveParamConfig|描述: 保存参数设置
	 * </p>
	 * 
	 * @param paramList
	 * @throws Exception
	 */
	public void saveParamConfig(List paramList) {
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			Map map = new HashMap();
			UBaseSysParamVmss param = (UBaseSysParamVmss) iterator.next();
			map.put("param", param);
			this.save("updateParam", map);
		}
	}

	public void updateVmssParam(UBaseSysParamVmss param) {
		Map map = new HashMap();
		map.put("param", param);
		this.save("updateBaseVmssParamByKey", map);
	}

	public String findvaluebyName(String itemCname) {
		Map map = new HashMap();
		map.put("itemCname", itemCname);
		List list = find("findvaluebyName", map);
		UBaseSysParamVmss uBaseSysParamVmss = new UBaseSysParamVmss();
		if (list != null && list.size() == 1) {
			uBaseSysParamVmss = (UBaseSysParamVmss) list.get(0);
		}
		return uBaseSysParamVmss.getSelectedValue();
	}

	public List findBaseVmssParamList(UBaseSysParamVmss uBaseSysParamVmss) {
		Map map = new HashMap();
		map.put("uBaseSysParamVmss", uBaseSysParamVmss);
		List list = find("findBaseVmssParamList", map);
		return list;
	}

	public UBaseSysParamVmss getUBaseSysParamVmssByKey(String itemKey) {
		UBaseSysParamVmss uBaseSysParamVmss = new UBaseSysParamVmss();
		uBaseSysParamVmss.setItemKey(itemKey);
		List list = this.findBaseVmssParamList(uBaseSysParamVmss);
		if (list != null && list.size() == 1) {
			return (UBaseSysParamVmss) list.get(0);
		}
		return null;
	}

	@Override
	public UBaseSysParamVmss getUBaseSysParamVmssByName(String ParaName) {
		Map map = new HashMap();
		map.put("ParaName", ParaName);
		List list = find("getUBaseSysParamVmssByName", map);
		UBaseSysParamVmss u = null;
		if (list.size() == 1) {
			u = (UBaseSysParamVmss) list.get(0);
		}
		return u;
	}

	@Override
	public void updateUBaseSysParamVmssByParamId(String pValue, String paramId) {
		Map map = new HashMap();
		map.put("pvalue", pValue);
		map.put("paramId", paramId);
		update("updateUBaseSysParamVmssByParamId", map);
	}

	@Override
	public BillCancel getBillCancelByDiskNo(String DiskNo) {
		Map map = new HashMap();
		map.put("DiskNo", DiskNo);
		List list =	find("getBillCancelByDiskNo", map);
		BillCancel billCancel = null;
		if(list.size()==1){
			billCancel=(BillCancel)list.get(0);
		}
		return billCancel;
	}

}
