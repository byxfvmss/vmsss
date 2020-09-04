package com.cjit.gjsz.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @作者: lihaiboA
 * @日期: Apr 28, 2012 1:39:38 PM
 * @描述: [TxCode]资本项目项下交易编码
 *      用于判断资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头，“外汇局批件号/备案表号/业务编号”不能为空
 */
public class TxCode{

	private static Map incomeTxCode = new HashMap();
	private static Map payoutTxCode = new HashMap();
	static{
		// 资本项目项下交易编码（收入）
		incomeTxCode.put("901010", "转入出口押汇");
		incomeTxCode.put("901020", "转入进口押汇");
		incomeTxCode.put("903010", "境内投资性公司投资款");
		incomeTxCode.put("903020", "中方外汇投资款");
		incomeTxCode.put("903090", "其他境内投资收入");
		incomeTxCode.put("904010", "获得国内银行及其他金融机构外汇贷款本金");
		incomeTxCode.put("904020", "获得委托贷款本金");
		incomeTxCode.put("904030", "委托贷款划回");
		incomeTxCode.put("904090", "获得其他贷款");
		incomeTxCode.put("909020", "对外发生或有负债产生的收入");
		incomeTxCode.put("909110", "集团公司成员间结售汇项下收入");
		incomeTxCode.put("909130",
				"实行外汇资金集中运营的境内企业，其资本金和经常项目外汇账户与其外汇委托贷款账户之间的资金划转收入");
		// 资本项目项下交易编码（支出）
		payoutTxCode.put("901010", "偿还出口押汇");
		payoutTxCode.put("901020", "偿还进口押汇");
		payoutTxCode.put("903010", "境内投资性公司投资款");
		payoutTxCode.put("903020", "中方外汇投资款");
		payoutTxCode.put("903090", "其他境内投资支出");
		payoutTxCode.put("904010", "偿还国内银行及其他金融机构外汇贷款本金");
		payoutTxCode.put("904020", "偿还委托贷款本金");
		payoutTxCode.put("904030", "委托贷款划出");
		payoutTxCode.put("904090", "偿还其他贷款");
		payoutTxCode.put("909110", "集团公司成员间结售汇项下支出");
		payoutTxCode.put("909130",
				"实行外汇资金集中运营的境内企业，其资本金和经常项目外汇账户与其外汇委托贷款账户之间的资金划转支出");
	}

	public static Map getIncomeTxCode(){
		return incomeTxCode;
	}

	public static Map getPayoutTxCode(){
		return payoutTxCode;
	}
}
