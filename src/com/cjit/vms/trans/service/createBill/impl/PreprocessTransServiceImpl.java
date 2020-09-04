package com.cjit.vms.trans.service.createBill.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.type.SpecialOneToOneType;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.CreateBillService;

/**
 * 构造票据信息类
 * @author Dylan
 *
 */
public class PreprocessTransServiceImpl {
	
	public static final String SPECIALINVOICE = "0"; //专票
	public static final String NORMALINVOICE = "1"; //普票
	public static final String SPECIALOVER = "specialOver";
	public static final String SPECIAL =  "special";
	public static final String NORMALOVER = "normalOver";
	public static final String NORMAL =  "normal";
	private List transInfoList;
	private BigDecimal normalLimit;//专票限额
	private BigDecimal specialLimit;//普票限额
	private Map transListMap = new HashMap();
	List specialList = new ArrayList();//专票list
	List normalList = new ArrayList();//普票list
	List specialOverList = new ArrayList();//超限额专票list
	List normalOverList = new ArrayList();//超限额普票list
	CreateBillService createBillService;
	
	/**
	 * 构造函数,传入交易List，获取开票限额，发票类型分组
	 * @param transInfoList
	 */
	public PreprocessTransServiceImpl(){
		this.transInfoList = transInfoList;
		//从缓存中提取当前专票，普票限额
		normalLimit = new BigDecimal("50000");
		specialLimit = new BigDecimal("50000");
		
	}
	
	/**
	 * step1.对交易进行初步分组
	 */
	public void spilitTransList(){
		//按照专票普票对交易分组，超限额交易移入大额交易list中
		for(int i = 0; i < transInfoList.size(); i++){
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			//交易初始化序号为1
			transInfo.setTransIdx("1");
			String type = transInfo.getFapiaoType();
			BigDecimal balance = transInfo.getBalance();
			if(SPECIALINVOICE.equals(type)){
				if(balance.compareTo(normalLimit) == 1){
					transInfo.setTransClass(NORMALOVER);
					specialOverList.add(transInfo);
				}else{
					transInfo.setTransClass(NORMAL);
					specialList.add(transInfo);
				}
			}else if(NORMALINVOICE.equals(type)){
				if(balance.compareTo(normalLimit) == 1){
					transInfo.setTransClass(SPECIALOVER);
					normalOverList.add(transInfo);
				}else{
					transInfo.setTransClass(SPECIAL);
					normalList.add(transInfo);
				}
			}
		}
		transListMap.put("specialList", specialList);
		transListMap.put("normalList", normalList);
	}

	/**
	 * step2.大额交易自动拆分
	 */
	public void overTransSplit(){
		for(int i = 0; i < specialOverList.size(); i++){
			TransInfo transInfo = (TransInfo) specialOverList.get(i);
			BigDecimal balance =transInfo.getBalance();
			List list = new ArrayList();
			//计算需要拆分的笔数
			BigDecimal maxIdx = balance.divide(specialLimit, 0, BigDecimal.ROUND_UP);
			for(int j = 1; i < maxIdx.intValue(); i++){
				TransInfo splitTransInfo = transInfo;
				splitTransInfo.setTransIdx(String.valueOf(j));
				splitTransInfo.setBalance(specialLimit);
				list.add(splitTransInfo);
			}
			//最后一笔计算剩余金额
			transInfo.setTransIdx(String.valueOf(maxIdx));
			BigDecimal splitedCount = maxIdx.subtract(BigDecimal.ONE);
			BigDecimal lastBalance = balance.subtract(specialLimit.multiply(splitedCount));
			transInfo.setBalance(lastBalance);
			transInfo.setTransClass(SPECIAL);
			//拆分后放入交易list
			list.add(transInfo);
			specialList.addAll(list);
		}
		for(int i = 0; i < normalOverList.size(); i++){
			TransInfo transInfo = (TransInfo) normalOverList.get(i);
			BigDecimal balance =transInfo.getBalance();
			List list = new ArrayList();
			//计算需要拆分的笔数
			BigDecimal maxIdx = balance.divide(normalLimit, 0, BigDecimal.ROUND_UP);
			for(int j = 1; i < maxIdx.intValue(); i++){
				TransInfo splitTransInfo = transInfo;
				splitTransInfo.setTransIdx(String.valueOf(j));
				splitTransInfo.setBalance(normalLimit);
				list.add(splitTransInfo);
			}
			//最后一笔计算剩余金额
			transInfo.setTransIdx(String.valueOf(maxIdx));
			BigDecimal splitedCount = maxIdx.subtract(BigDecimal.ONE);
			BigDecimal lastBalance = balance.subtract(normalLimit.multiply(splitedCount));
			transInfo.setBalance(lastBalance);
			transInfo.setTransClass(NORMAL);
			//拆分后放入交易list
			list.add(transInfo);
			normalList.addAll(list);
		}
		//释放内存
		specialOverList = null;
		normalOverList = null;
	}
	
	/**
	 * step3.持久化当前处理的对象list
	 * 
	 */
	public void saveTransProcessing(){
		createBillService.saveTransProcessing(specialList);
		createBillService.saveTransProcessing(normalList);
	}
	
	
	/**
	 * step4.按照商品对交易分组
	 */
	public void byGoodsType(){
		Map goodsMap = new HashMap();
		//普票
		for(int i = 0; i < normalList.size(); i++){
			TransInfo transInfo = (TransInfo) normalList.get(i);
			String goodsId = transInfo.getGoodsId();
			goodsMap.put(goodsId, goodsId);
		}
		//专票
		
		//创建商品组
		List goodsCollList = Arrays.asList(goodsMap.keySet().toArray());
		
	}
	public List getTransInfoList() {
		return transInfoList;
	}
	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public BigDecimal getNormalLimit() {
		return normalLimit;
	}

	public void setNormalLimit(BigDecimal normalLimit) {
		this.normalLimit = normalLimit;
	}

	public BigDecimal getSpecialLimit() {
		return specialLimit;
	}

	public void setSpecialLimit(BigDecimal specialLimit) {
		this.specialLimit = specialLimit;
	}


	public Map getTransListMap() {
		return transListMap;
	}


	public void setTransListMap(Map transListMap) {
		this.transListMap = transListMap;
	}

	public CreateBillService getCreateBillService() {
		return createBillService;
	}

	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}

	public List getSpecialList() {
		return specialList;
	}

	public void setSpecialList(List specialList) {
		this.specialList = specialList;
	}

	public List getNormalList() {
		return normalList;
	}

	public void setNormalList(List normalList) {
		this.normalList = normalList;
	}

	public List getSpecialOverList() {
		return specialOverList;
	}

	public void setSpecialOverList(List specialOverList) {
		this.specialOverList = specialOverList;
	}

	public List getNormalOverList() {
		return normalOverList;
	}

	public void setNormalOverList(List normalOverList) {
		this.normalOverList = normalOverList;
	}
}
