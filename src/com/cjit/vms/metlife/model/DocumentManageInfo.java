package com.cjit.vms.metlife.model;
/** 
 *  createTime:2016.2
 * 	author:沈磊
 *	content:单证管理 实体类  metlife
*/
import java.util.List;

import com.cjit.vms.trans.model.InstInfo;
import com.cjit.vms.trans.util.DataUtil;

public class DocumentManageInfo extends InstInfo{
	//VMS_DOC_RULES
	private String  ruleId;//单证规则ID
	private String type;//单证类型
	private String instId;//分支机构Id
	private String bank;//合作机构
	private String channel;//渠道
	private String instCode;//机构代码
	private String channelCode;//聚到代码
	private String yearYn;//是否包含年度
	private String speCode;//特殊项目代码
	private String sufCode;//其他补充代码 
	private String length;//流水号长度
	private String rule;//规则序列
	private String curNum;//当前最大号
	private String maxNum;//流水号极限
	//VMS_DOC_INFO
	private String status;//状态
	private String Num;//单证号
	private String vdiOrder;//编号
	private String disId;//分发人编号
	private String recId;//领用人编号
	private String matchStatus;//初始状态
	private String beginNum;//序列号开始
	private String endNum;//序列号结束
	private String[] curNumArray;
	//VMS_DOC_REC_INFO
	private String vdmiId;//系统序列号
	private String vdmiDate;//基准日
	private String formNum;//保险单证号
	private String policyNum;//保单号
	private String appNum;//投保单号
	private String vdmiError;//报错
	private String tepStatus;//保险单证号状态
	private String tepchannel;//保险单证号渠道
	private String beginDate;
	private String endDate;
	//VMS_DOC_REC_INFO
	private String vdriId;//领用人编号
	private String vdriName;//领用人名称
	private String vdriStatus;//状态
	
	private List lstAuthInstId;
	private String countNum;//生成单证号数量;
	private String curNumBegin;//开始号段
	private String curNumEnd;//结束号段
	
	
	private String baseUserId;
	private String baseUserName;
	private String channelStr;
	
	
	public String getChannelStr() {
		return DataUtil.getChanNel(channel);
	}
	public void setChannelStr(String channelStr) {
		this.channelStr = channelStr;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTepStatus() {
		return tepStatus;
	}
	public void setTepStatus(String tepStatus) {
		this.tepStatus = tepStatus;
	}
	public String getTepchannel() {
		return tepchannel;
	}
	public void setTepchannel(String tepchannel) {
		this.tepchannel = tepchannel;
	}
	public String getBaseUserId() {
		return baseUserId;
	}
	public void setBaseUserId(String baseUserId) {
		this.baseUserId = baseUserId;
	}
	public String getBaseUserName() {
		return baseUserName;
	}
	public void setBaseUserName(String baseUserName) {
		this.baseUserName = baseUserName;
	}
	public String getCurNumBegin() {
		return curNumBegin;
	}
	public void setCurNumBegin(String curNumBegin) {
		this.curNumBegin = curNumBegin;
	}
	public String getCurNumEnd() {
		return curNumEnd;
	}
	public void setCurNumEnd(String curNumEnd) {
		this.curNumEnd = curNumEnd;
	}
	public String[] getCurNumArray() {
		return curNumArray;
	}
	public void setCurNumArray(String[] curNumArray) {
		this.curNumArray = curNumArray;
	}
	public String getCountNum() {
		return countNum;
	}
	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}
	public List getLstAuthInstId() {
		return lstAuthInstId;
	}
	public void setLstAuthInstId(List lstAuthInstId) {
		this.lstAuthInstId = lstAuthInstId;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getBank() {
		return bank;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getInstCode() {
		return instCode;
	}
	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getYearYn() {
		return yearYn;
	}
	public void setYearYn(String yearYn) {
		this.yearYn = yearYn;
	}
	public String getSpeCode() {
		return speCode;
	}
	public void setSpeCode(String speCode) {
		this.speCode = speCode;
	}
	public String getSufCode() {
		return sufCode;
	}
	public void setSufCode(String sufCode) {
		this.sufCode = sufCode;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getCurNum() {
		return curNum;
	}
	public void setCurNum(String curNum) {
		this.curNum = curNum;
	}
	public String getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(String maxNum) {
		this.maxNum = maxNum;
	}
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}


	public String getVdiOrder() {
		return vdiOrder;
	}
	public void setVdiOrder(String vdiOrder) {
		this.vdiOrder = vdiOrder;
	}
	public String getBeginNum() {
		return beginNum;
	}
	public void setBeginNum(String beginNum) {
		this.beginNum = beginNum;
	}
	public String getEndNum() {
		return endNum;
	}
	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}
	public String getDisId() {
		return disId;
	}
	public void setDisId(String disId) {
		this.disId = disId;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		matchStatus = matchStatus;
	}
	public String getVdmiId() {
		return vdmiId;
	}
	public void setVdmiId(String vdmiId) {
		this.vdmiId = vdmiId;
	}
	
	public String getVdmiDate() {
		return vdmiDate;
	}
	public void setVdmiDate(String vdmiDate) {
		this.vdmiDate = vdmiDate;
	}
	public String getFormNum() {
		return formNum;
	}
	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}
	public String getPolicyNum() {
		return policyNum;
	}
	public void setPolicyNum(String policyNum) {
		this.policyNum = policyNum;
	}
	public String getAppNum() {
		return appNum;
	}
	public void setAppNum(String appNum) {
		this.appNum = appNum;
	}
	
	public String getVdmiError() {
		return vdmiError;
	}
	public void setVdmiError(String vdmiError) {
		this.vdmiError = vdmiError;
	}
	public String getVdriId() {
		return vdriId;
	}
	public void setVdriId(String vdriId) {
		this.vdriId = vdriId;
	}
	public String getVdriName() {
		return vdriName;
	}
	public void setVdriName(String vdriName) {
		this.vdriName = vdriName;
	}
	public String getVdriStatus() {
		return vdriStatus;
	}
	public void setVdriStatus(String vdriStatus) {
		this.vdriStatus = vdriStatus;
	}
	
	
	
	
	
}