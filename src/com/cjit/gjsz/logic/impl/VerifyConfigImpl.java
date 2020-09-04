package com.cjit.gjsz.logic.impl;

import java.util.HashMap;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.logic.TxCode;
import com.cjit.gjsz.logic.VerifyConfig;

public class VerifyConfigImpl extends GenericServiceImpl implements
		VerifyConfig{

	private String verifyRegnoByTxCode9;

	/**
	 * 金宏工程外汇局子项与银行业务系统数据接口规范中
	 * @param baseTable 报文TableID
	 * @param businessids 报文业务ID
	 * @author tongxiaoming
	 */
	public boolean verifyCustCodeOfCompany(String baseTable, String businessids){
		Map parameters = new HashMap();
		parameters.put("baseTable", baseTable);
		parameters.put("businessids", businessids);
		Long l = this.getRowCount("verifyCustCode", parameters);
		if(l != null && l.longValue() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 外汇局批件号/备案表号/业务编号 校验<br>同样适用于管理信息－外汇帐户内结汇报文“结汇用途”字段校验
	 * @param regno (外汇局批件号/备案表号/业务编号)/(结汇用途)
	 * @param txcode
	 * @param txcode2
	 * @param txFlag 交易标志（income收入/payout支出）
	 * @return 校验通过true 校验未通过false
	 */
	public boolean verifyRegno(String regno, String txcode, String txcode2,
			String txFlag){
		if(StringUtil.isNotEmpty(txcode)){
			// 资本项目项下交易（涉外收支交易编码以“5”、“6”、“7”、“8”和部分“9”开头
			// “外汇局批件号/备案表号/业务编号”为必输项
			// 其中弄过"9"开头因涉及是否是资本项目项下交易，系统无从判断，暂未处理
			if(txcode.startsWith("5") || txcode.startsWith("6")
					|| txcode.startsWith("7") || txcode.startsWith("8")){
				if(!StringUtil.isNotEmpty(regno)){
					return false;
				}
			}
			if("yes".equals(verifyRegnoByTxCode9)){
				if("income".equals(txFlag)
						&& TxCode.getIncomeTxCode().get(txcode) != null
						&& !StringUtil.isNotEmpty(regno)){
					return false;
				}else if("payout".equals(txFlag)
						&& TxCode.getPayoutTxCode().get(txcode) != null
						&& !StringUtil.isNotEmpty(regno)){
					return false;
				}
			}
		}
		if(StringUtil.isNotEmpty(txcode2)){
			if(txcode2.startsWith("5") || txcode2.startsWith("6")
					|| txcode2.startsWith("7") || txcode2.startsWith("8")){
				if(!StringUtil.isNotEmpty(regno)){
					return false;
				}
			}
			if("yes".equals(verifyRegnoByTxCode9)){
				if("income".equals(txFlag)
						&& TxCode.getIncomeTxCode().get(txcode2) != null
						&& !StringUtil.isNotEmpty(regno)){
					return false;
				}else if("payout".equals(txFlag)
						&& TxCode.getPayoutTxCode().get(txcode2) != null
						&& !StringUtil.isNotEmpty(regno)){
					return false;
				}
			}
		}
		return true;
	}

	public String getVerifyRegnoByTxCode9(){
		return verifyRegnoByTxCode9;
	}

	public void setVerifyRegnoByTxCode9(String verifyRegnoByTxCode9){
		this.verifyRegnoByTxCode9 = verifyRegnoByTxCode9;
	}
}
