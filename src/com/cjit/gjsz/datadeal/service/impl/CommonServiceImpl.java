package com.cjit.gjsz.datadeal.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.cjit.gjsz.datadeal.model.SelectTag;
import com.cjit.gjsz.datadeal.service.CommonService;
import com.cjit.gjsz.datadeal.util.DataUtil;

/**
 * @author yulubin
 */
public class CommonServiceImpl implements CommonService{

	// DFHL:增加是否显示打回状态 审核通过\校验通过开始
	private String isLowerStatusContant5 = "0";

	public void setIsLowerStatusContant5(String isLowerStatusContant5){
		this.isLowerStatusContant5 = isLowerStatusContant5;
	}

	private String isLowerStatusContant3 = "0";

	public void setIsLowerStatusContant3(String isLowerStatusContant3){
		this.isLowerStatusContant3 = isLowerStatusContant3;
	}

	// DFHL:增加是否显示打回状态 审核通过\校验通过结束
	public List getDataStatusListForPageListDatas(){
		List l = new ArrayList();
		SelectTag s = new SelectTag("", "所有状态");
		l.add(s);
		// SelectTag s1 = new SelectTag("1", "未校验");
		SelectTag s1 = new SelectTag(String.valueOf(DataUtil.WJY_STATUS_NUM),
				DataUtil.WJY_STATUS_CH);
		l.add(s1);
		// SelectTag s2 = new SelectTag("2", "校验未通过");
		SelectTag s2 = new SelectTag(String.valueOf(DataUtil.JYWTG_STATUS_NUM),
				DataUtil.JYWTG_STATUS_CH);
		l.add(s2);
		// SelectTag s3 = new SelectTag("3", "校验已通过");
		SelectTag s3 = new SelectTag(String.valueOf(DataUtil.JYYTG_STATUS_NUM),
				DataUtil.JYYTG_STATUS_CH);
		l.add(s3);
		// SelectTag s4 = new SelectTag("4", "审核未通过");
		SelectTag s5 = new SelectTag(String.valueOf(DataUtil.SHWTG_STATUS_NUM),
				DataUtil.SHWTG_STATUS_CH);
		l.add(s5);
		// 逻辑删除
		// SelectTag s0 = new SelectTag(
		// String.valueOf(DataUtil.DELETE_STATUS_NUM),
		// DataUtil.DELETE_STATUS_CH);
		// l.add(s0);
		return l;
	}

	// 数据审核页面 审核记录类型下拉框
	public List getDataStatusListForPageListDatasAudit(){
		List l = new ArrayList();
		SelectTag s0 = new SelectTag("0", "删除待审核");
		l.add(s0);
		SelectTag s4 = new SelectTag("4", "上报待审核");
		l.add(s4);
		return l;
	}

	// 数据打回页面 数据状态下拉框
	public List getDataStatusListForPageListDatasLowerStatus(){
		List l = new ArrayList();
		SelectTag s = new SelectTag("", "所有状态");
		l.add(s);
		// 审核通过
		SelectTag s5 = new SelectTag(String.valueOf(DataUtil.SHYTG_STATUS_NUM),
				DataUtil.SHYTG_STATUS_CH);
		l.add(s5);
		// 已报送
		SelectTag s6 = new SelectTag(String.valueOf(DataUtil.YBS_STATUS_NUM),
				DataUtil.YBS_STATUS_CH);
		l.add(s6);
		// 已生成
		SelectTag s7 = new SelectTag(String.valueOf(DataUtil.YSC_STATUS_NUM),
				DataUtil.YSC_STATUS_CH);
		l.add(s7);
		// 逻辑删除
		SelectTag s0 = new SelectTag(
				String.valueOf(DataUtil.DELETE_STATUS_NUM),
				DataUtil.DELETE_STATUS_CH);
		l.add(s0);
		// 已锁定
		SelectTag s9 = new SelectTag(
				String.valueOf(DataUtil.LOCKED_STATUS_NUM),
				DataUtil.LOCKED_STATUS_CH);
		l.add(s9);
		return l;
	}

	public List getResetDataStatusListForPageListDatasAudit(){
		List l = new ArrayList();
		// SelectTag s6 = new SelectTag("6", "审核通过");
		SelectTag s6 = new SelectTag(String.valueOf(DataUtil.SHYTG_STATUS_NUM),
				DataUtil.SHYTG_STATUS_CH);
		l.add(s6);
		// SelectTag s5 = new SelectTag("5", "审核不通过");
		SelectTag s5 = new SelectTag(String.valueOf(DataUtil.SHWTG_STATUS_NUM),
				DataUtil.SHWTG_STATUS_CH);
		l.add(s5);
		return l;
	}

	// DFHL:增加根据配置判断是否打回到 审核通过
	public List getResetDataStatusListForPageListLowerStatus(){
		List l = new ArrayList();
		// SelectTag s1 = new SelectTag("1", "未校验");
		SelectTag s1 = new SelectTag(String.valueOf(DataUtil.WJY_STATUS_NUM),
				DataUtil.WJY_STATUS_CH);
		l.add(s1);
		// DFHL:增加审核通过/校验通过选项开始
		if(isLowerStatusContant3 != null && "1".equals(isLowerStatusContant3)){
			// SelectTag s3 = new SelectTag("3", "校验通过");
			SelectTag s3 = new SelectTag(String
					.valueOf(DataUtil.JYYTG_STATUS_NUM),
					DataUtil.JYYTG_STATUS_CH);
			l.add(s3);
		}
		if(isLowerStatusContant5 != null && "1".equals(isLowerStatusContant5)){
			// SelectTag s5 = new SelectTag("5", "审核通过");
			SelectTag s5 = new SelectTag(String
					.valueOf(DataUtil.SHYTG_STATUS_NUM),
					DataUtil.SHYTG_STATUS_CH);
			l.add(s5);
		}
		// DFHL:增加审核通过/校验通过选项结束
		return l;
	}

	public List getAllStatus(){
		List l = new ArrayList();
		SelectTag s = new SelectTag("", "所有状态");
		l.add(s);
		// 未校验
		SelectTag s1 = new SelectTag(String.valueOf(DataUtil.WJY_STATUS_NUM),
				DataUtil.WJY_STATUS_CH);
		l.add(s1);
		// 校验未通过
		SelectTag s2 = new SelectTag(String.valueOf(DataUtil.JYWTG_STATUS_NUM),
				DataUtil.JYWTG_STATUS_CH);
		l.add(s2);
		// 校验已通过
		SelectTag s3 = new SelectTag(String.valueOf(DataUtil.JYYTG_STATUS_NUM),
				DataUtil.JYYTG_STATUS_CH);
		l.add(s3);
		// 已提交待审核
		SelectTag s4 = new SelectTag(
				String.valueOf(DataUtil.YTJDSH_STATUS_NUM),
				DataUtil.YTJDSH_STATUS_CH);
		l.add(s4);
		// 审核不通过
		SelectTag s5 = new SelectTag(String.valueOf(DataUtil.SHWTG_STATUS_NUM),
				DataUtil.SHWTG_STATUS_CH);
		l.add(s5);
		// 审核通过
		SelectTag s6 = new SelectTag(String.valueOf(DataUtil.SHYTG_STATUS_NUM),
				DataUtil.SHYTG_STATUS_CH);
		l.add(s6);
		// 已报送
		SelectTag s7 = new SelectTag(String.valueOf(DataUtil.YBS_STATUS_NUM),
				DataUtil.YBS_STATUS_CH);
		l.add(s7);
		// 已生成
		SelectTag s8 = new SelectTag(String.valueOf(DataUtil.YSC_STATUS_NUM),
				DataUtil.YSC_STATUS_CH);
		l.add(s8);
		// 已锁定
		SelectTag s9 = new SelectTag(
				String.valueOf(DataUtil.LOCKED_STATUS_NUM),
				DataUtil.LOCKED_STATUS_CH);
		l.add(s9);
		// 逻辑删除
		SelectTag s0 = new SelectTag(
				String.valueOf(DataUtil.DELETE_STATUS_NUM),
				DataUtil.DELETE_STATUS_CH);
		l.add(s0);
		return l;
	}

	public List getStobStatus(){
		List l = new ArrayList();
		SelectTag s = new SelectTag("", "所有状态");
		l.add(s);
		SelectTag s6 = new SelectTag("6", "已审核");
		l.add(s6);
		return l;
	}

	public String getStatusNameByStatusId(String statusId){
		List allStatus = getAllStatus();
		int size = allStatus.size();
		for(int i = 0; i < size; i++){
			SelectTag s = (SelectTag) allStatus.get(i);
			if(s.getValue().equals(statusId)){
				return s.getText();
			}
		}
		return "";
	}
}