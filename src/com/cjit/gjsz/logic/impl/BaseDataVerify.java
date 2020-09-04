/**
 * 
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.model.AddRunBank;

/**
 * @author huboA
 */
public abstract class BaseDataVerify implements DataVerify{

	public static final String ACTIONTYPE_VERIFY = "A,C,D,R";
	public static final String CUSTYPE_VERIFY = "C,D,F";
	public static final String METHOD_VERIFY = "L,G,C,T,D,M,O";
	protected List dictionarys;
	protected List verifylList;
	protected String interfaceVer;

	public BaseDataVerify(){
	}

	public BaseDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.interfaceVer = interfaceVer;
	}

	/**
	 * 操作类型 A－新建 C－修改 D－删除 R-申报无误（银行反馈），预留，暂不用。
	 * @param type 操作类型
	 * @param actionType 操作类型字典
	 */
	public boolean verifyActiontype(String type, String actionType){
		if(StringUtil.contains(actionType, type)){
			return true;
		}
		return false;
	}

	/**
	 * 操作类型 A－新建 C－修改 D－删除 R-申报无误（银行反馈），预留，暂不用。
	 * @param type 操作类型
	 * @param rptno 申报申请号
	 */
	public boolean verifyRptno(String type, String rptno){
		// modify by wangxin 20090224 由于需要修改为先生成申报申请号，所以不做申报申请号为空的校验
		return true;
		/*
		if (StringUtil.isEmpty(rptno)) {
			if (StringUtil.equalsIgnoreCase("A", type)) {
				return true;
			}
			return false;
		} else {
			if (!StringUtil.equalsIgnoreCase("A", type)) {
				return true;
			}
			return false;
		}
		*/
	}

	/**
	 * 收款人类型 C－对公用户 D－对私中国居民 F－对私中国非居民 需与申报号码中第19位字母所表明的含义一致。
	 * @param custype 收款人类型
	 * @param custypeType 收款人类型字典
	 * @return
	 */
	public boolean verifyCustype(String custype, String custypeType){
		if(StringUtil.contains(custypeType, custype)){
			return true;
		}
		return false;
	}

	/**
	 * 个人身份证件号码 CUSTYPE<>C时必输
	 * @param idcode 个人身份证件号码
	 * @param custype 收款人类型
	 * @return
	 */
	public boolean verifyIdcode(String idcode, String custype){
		// DFHL: 个人身份证件号码校验 start
		/*		if (!StringUtil.equalsIgnoreCase(custype, "C")) {
					if (StringUtil.isEmpty(idcode)) {
						return false;
					}
				}
				return true;*/
		// modified by yuanshihong 20090612 增加了对收款人类型为C时必须为空的校验
		return !(StringUtil.equalsIgnoreCase(custype, "C") ^ StringUtil
				.isEmpty(idcode));
		// DFHL: 个人身份证件号码校验 end
	}

	/**
	 * 组织机构代码 CUSTYPE=C时必输
	 * @param custcode 个人身份证件号码
	 * @param custype 收款人类型
	 * @return
	 */
	public boolean verifyCustcode(String custcode, String custype){
		// DFHL: 组织机构代码校验 start
		if(StringUtil.equalsIgnoreCase(custype, "C")){
			if(StringUtil.isEmpty(custcode)){
				return false;
			}
			if(!verifyCustcode(custcode)){
				return false;
			}
			return true;
		}else{
			return StringUtil.isEmpty(custcode);
		}
		// modified by yuanshihong 20090612 增加了对收款人类型不为C时必须为空的校验
		// return (StringUtil.equalsIgnoreCase(custype, "C") ^
		// StringUtil.isEmpty(custcode));
		// DFHL: 组织机构代码校验 end
	}

	/**
	 * 收款人名称 必输项
	 * @param custnm
	 * @return
	 */
	public boolean verifyCustnm(String custnm){
		if(StringUtil.isEmpty(custnm)){
			return false;
		}
		return true;
	}

	/**
	 * 付款人名称 必输项
	 * @param oppuser 付款人名称
	 * @return
	 */
	// public boolean verifyOppuser(String oppuser){
	// if(StringUtil.isEmpty(oppuser)){
	// return false;
	// }
	// return true;
	// }
	/**
	 * <p>方法名称: verifyOppuser|描述:收、付款人名称校验</p>
	 * @param oppuser
	 * @return boolean 校验通过返回true,不通过返回false
	 */
	public boolean verifyOppuser(String oppuser, String tableId){
		if(StringUtil.isEmpty(oppuser)){
			return false;
		}
		if("1.2".equals(this.interfaceVer)
				&& ("t_base_income".equals(tableId)
						|| "t_base_remit".equals(tableId) || "t_base_payment"
						.equals(tableId))){
			// 启用国际收支网上申报系统与银行业务系统数据接口规范（1.2版）时执行下拉校验逻辑
			if(oppuser.startsWith("(JW)") || oppuser.startsWith("(JN)")){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}

	/**
	 * 收入款币种 必填项。字母代码,必须在币种代码表里存在
	 * @param txccy 收入款币种
	 * @param code 字典所对应的类型
	 * @return
	 */
	public boolean verifyTxccy(String txccy, String code){
		if(StringUtil.isEmpty(txccy)){
			return false;
		}
		return findKey(dictionarys, code, txccy);
	}

	/**
	 * 收入款金额 必须大于0。 无小数位。
	 * @param txamt 收入款金额
	 * @return
	 */
	public boolean verifyTxamt(BigInteger txamt){
		if(txamt == null){
			return true;
		}
		return txamt.signum() > 0 ? true : false;
	}

	/**
	 * 结汇汇率 当结汇金额大于0时必填，否则不应该填写
	 * @param exrate 结汇汇率
	 * @param lcyamt 结汇金额
	 * @return
	 */
	public boolean verifyExrate(Double exrate, BigInteger lcyamt){
		if(lcyamt == null){
			return true;
		}
		if(lcyamt.signum() > 0){
			return exrate == null ? false : true;
		}
		return true;
	}

	/**
	 * 结汇汇率 结汇汇率必须大于0
	 * @param exrate 结汇汇率
	 * @return
	 */
	public boolean verifyExrate1(Double exrate){
		if(exrate == null){
			return true;
		}
		if(exrate.doubleValue() > 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 结汇金额 选输项，但不能小于0。 若账号不为空则对应金额必须>0；若金额>0，则对应账号不能为空。
	 * @param lcyamt 结汇金额
	 * @param lcyacc 人民币帐号/银行卡号
	 * @return
	 */
	public boolean verifyLcyamt(BigInteger lcyamt, String lcyacc){
		if(lcyamt != null){
			if(lcyamt.signum() < 0){
				return false;
			}
			if(lcyamt.signum() > 0.0 && StringUtil.isNotEmpty(lcyacc)){
				return true;
			}
			if(lcyamt.signum() == 0 && StringUtil.isEmpty(lcyacc)){
				return true;
			}
			return false;
		}
		if(StringUtil.isNotEmpty(lcyacc)){
			return false;
		}
		return true;
	}

	/**
	 * 人民币帐号/银行卡号 购汇金额、购汇汇率、购汇帐号三个或同时空或同时有值。
	 * @param lcyamt 结汇金额
	 * @param exrate 结汇汇率
	 * @param lcyacc 人民币帐号/银行卡号
	 * @return
	 */
	public boolean verifyLcyacc(BigInteger lcyamt, Double exrate, String lcyacc){
		if(exrate != null && lcyamt != null && StringUtil.isNotEmpty(lcyacc)){
			return true;
		}
		if(exrate == null && lcyamt == null && StringUtil.isEmpty(lcyacc)){
			return true;
		}
		return false;
	}

	/**
	 * 现汇金额 选输项，但不能小于0。 若账号不为空则对应金额必须>0；若金额>0，则对应账号不能为空。
	 * @param fcyamt 现汇金额
	 * @param fcyacc 外汇帐号/银行卡号
	 * @return
	 */
	public boolean verifyFcyamt(BigInteger fcyamt, String fcyacc){
		return verifyLcyamt(fcyamt, fcyacc);
	}

	/**
	 * 外汇帐号/银行卡号 如果有现汇金额，则该字段不能为空。 现汇金额、现汇帐号或同时空，或同时有值。
	 * @param fcyamt 现汇金额
	 * @param fcyacc 外汇帐号/银行卡号
	 * @return
	 */
	public boolean verifyFcyacc(BigInteger fcyamt, String fcyacc){
		if(StringUtil.isNotEmpty(fcyacc) && fcyamt != null){
			return true;
		}
		if(StringUtil.isEmpty(fcyacc) && fcyamt == null){
			return true;
		}
		return false;
	}

	/**
	 * 其它金额 选输项，但不能小于0。 若账号不为空则对应金额必须>0；若金额>0，则对应账号不能为空。 结汇金额, 现汇金额, 其它金额至少输入一项。
	 * 结汇金额、现汇金额、其它金额之和不能大于收入款金额。
	 * @param othamt 其它金额
	 * @param othacc 其它帐号/银行卡号
	 * @param lcyamt 结汇金额
	 * @param txamt 收入款金额
	 * @param fcyamt 现汇金额
	 * @return
	 */
	public boolean verifyOthamt(BigInteger othamt, String othacc,
			BigInteger lcyamt, BigInteger fcyamt, BigInteger txamt){
		if(lcyamt == null && fcyamt == null && othamt == null){
			return false;
		}
		if(StringUtil.isEmpty(othacc)){
			return true;
		}
		if(verifyLcyamt(othamt, othacc)){
			return true;
		}
		return false;
	}

	/**
	 * 其它金额 选输项，但不能小于0。 若账号不为空则对应金额必须>0；若金额>0，则对应账号不能为空。 结汇金额, 现汇金额, 其它金额至少输入一项。
	 * 结汇金额、现汇金额、其它金额之和不能大于收入款金额。
	 * @param othamt 其它金额
	 * @param othacc 其它帐号/银行卡号
	 * @param lcyamt 结汇金额
	 * @param txamt 收入款金额
	 * @param fcyamt 现汇金额
	 * @return
	 */
	public boolean verifySum(BigInteger othamt, String othacc,
			BigInteger lcyamt, BigInteger fcyamt, BigInteger txamt){
		lcyamt = (lcyamt == null) ? BigInteger.valueOf(0) : lcyamt;
		fcyamt = (fcyamt == null) ? BigInteger.valueOf(0) : fcyamt;
		othamt = (othamt == null) ? BigInteger.valueOf(0) : othamt;
		txamt = (txamt == null) ? BigInteger.valueOf(0) : txamt;
		if(lcyamt != null && fcyamt != null && othamt != null && txamt != null){
			BigInteger tmp = othamt.add(lcyamt).add(fcyamt);
			if(txamt.compareTo(tmp) >= 0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 其它金额 选输项，但不能小于0。 若账号不为空则对应金额必须>0；若金额>0，则对应账号不能为空。 结汇金额, 现汇金额, 其它金额至少输入一项。
	 * 结汇金额、现汇金额、其它金额之和不能大于收入款金额。
	 * @param othamt 其它金额
	 * @param othacc 其它帐号/银行卡号
	 * @param lcyamt 结汇金额
	 * @param txamt 收入款金额
	 * @param fcyamt 现汇金额
	 * @return
	 */
	public boolean verifySum(BigInteger othamt, String othacc,
			BigInteger lcyamt, BigInteger fcyamt, BigInteger txamt,
			AddRunBank addRunBank){
		int max = 0;
		if(lcyamt != null && lcyamt.signum() > 0){
			addRunBank.setColumnId("lcyamt");
		}else if(fcyamt != null && fcyamt.signum() > 0){
			addRunBank.setColumnId("fcyamt");
		}else if(othamt != null && othamt.signum() > 0){
			addRunBank.setColumnId("othamt");
		}
		if(lcyamt != null){
			max++;
		}
		if(fcyamt != null){
			max++;
		}
		if(othamt != null){
			max++;
		}
		lcyamt = (lcyamt == null) ? BigInteger.valueOf(0) : lcyamt;
		fcyamt = (fcyamt == null) ? BigInteger.valueOf(0) : fcyamt;
		othamt = (othamt == null) ? BigInteger.valueOf(0) : othamt;
		txamt = (txamt == null) ? BigInteger.valueOf(0) : txamt;
		if(lcyamt != null && fcyamt != null && othamt != null && txamt != null){
			BigInteger tmp = othamt.add(lcyamt).add(fcyamt);
			if(txamt.compareTo(tmp) == 0){
				return true;
			}
			// VerifyService verifyService = (VerifyService) SpringContextUtil
			// .getBean("verifyService");
			long value = txamt.subtract(tmp).longValue();
			addRunBank.setValue(new Long(value));
			if((value == 1) && (max == 2)){
				// verifyService.updateAddBank(addRunBank);
				// return true;
				return false;
			}else if((value <= 2) && (max == 3)){
				// verifyService.updateAddBank(addRunBank);
				// return true;
				return false;
			}
		}
		return false;
	}

	/**
	 * 其它帐号/银行卡号 如果有其他金额，则该字段不能为空， 其他金额为0，则该字段不应该填写， 其它金额、其它帐号或同时空，或同时有值。
	 * @param othamt 其它金额
	 * @param othacc 其它帐号/银行卡号
	 * @return
	 */
	public boolean verifyOthacc(BigInteger othamt, String othacc){
		return verifyLcyamt(othamt, othacc);
	}

	/**
	 * 结算方式 L－信用证 G－保函 C－托收 T－电汇 D－票汇 M－信汇 O－其他
	 * @param method 收款人类型
	 * @param verifyString 比较字符串
	 * @return
	 */
	public boolean verifyMethod(String method, String verifyString){
		if(StringUtil.contains(verifyString, method)){
			return true;
		}
		return false;
	}

	/**
	 * 银行业务编号 必输项
	 * @param buscode 银行业务编号
	 * @return
	 */
	public boolean verifyBuscode(String buscode){
		return StringUtil.isNotEmpty(buscode);
	}

	/**
	 * 实际付款币种 必输项。必须在币种代码表里存在
	 * @param actuccy 实际付款币种
	 * @param code 字典对照
	 * @return
	 */
	public boolean verifyActuccy(String actuccy, String code){
		return verifyTxccy(actuccy, code);
	}

	/**
	 * 实际付款金额 必须大于0。 无小数位。
	 * @param actuamt 实际付款金额
	 * @return
	 */
	public boolean verifyActuamt(BigInteger actuamt){
		if(actuamt != null && actuamt.signum() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 国内银行扣费币种 选输项，必须在币种代码表里存在
	 * @param inchargeccy 国内银行扣费币种 收入款币种
	 * @param code 字典所对应的类型
	 * @return
	 */
	public boolean verifyInchargeccy(String inchargeccy, String code){
		if(StringUtil.isEmpty(inchargeccy) && StringUtil.isEmpty(code)){
			return true;
		}
		return verifyTxccy(inchargeccy, code);
	}

	/**
	 * 国内银行扣费金额
	 * @param lnchargeamt 国内银行扣费金额
	 * @param inchargeccy 国内银行扣费币种
	 */
	public boolean verifyInchargeamt(BigInteger lnchargeamt, String inchargeccy){
		if(inchargeccy == null && lnchargeamt == null){
			return true;
		}
		if(lnchargeamt != null && lnchargeamt.longValue() > 0
				&& StringUtil.isNotEmpty(inchargeccy)){
			return true;
		}
		return false;
	}

	/**
	 * 国外银行扣费币种 选输项，必须在币种代码表里存在
	 * @param outchargeccy 国外银行扣费币种
	 * @param code 字典所对应的类型
	 * @return
	 */
	public boolean verifyOutchargeccy(String outchargeccy, String code){
		if(StringUtil.isEmpty(outchargeccy) && StringUtil.isEmpty(code)){
			return true;
		}
		return verifyTxccy(outchargeccy, code);
	}

	/**
	 * 国内银行扣费金额
	 * @param lnchargeamt 国内银行扣费金额
	 * @param inchargeccy 国内银行扣费币种
	 */
	public boolean verifyOutchargeccy(BigInteger outchargeamt,
			String outchargeccy){
		return verifyInchargeamt(outchargeamt, outchargeccy);
	}

	/**
	 * 国外银行扣费金额
	 * @param outchargeamt 国外银行扣费金额
	 * @param outchargeccy 国外银行扣费币种
	 */
	public boolean verifyOutchargeamt(BigInteger outchargeamt,
			String outchargeccy){
		return verifyInchargeamt(outchargeamt, outchargeccy);
	}

	/**
	 * 得到值
	 * @param dataKey
	 * @param key
	 * @return
	 */
	public String getKey(String dataKey, String key){
		return getKey(dictionarys, key, dataKey);
	}

	/** ************************************************************************************* */
	/**
	 * 根据字典记录集查找某个字典项是否存在
	 * @param dictionarys
	 * @param dataKey
	 * @param key
	 * @return
	 */
	protected boolean findKey(List dictionarys, String dataKey, String key){
		if(CollectionUtil.isNotEmpty(dictionarys)){
			for(int i = 0; i < dictionarys.size(); i++){
				Dictionary dictionary = (Dictionary) dictionarys.get(i);
				if(StringUtil.equalsIgnoreCase(dictionary.getType(), dataKey)){ // 如果找到币种
					if(StringUtil.equalsIgnoreCase(key, dictionary
							.getValueStandardLetter())){ // 如果ValueBlank为空,默认不需要转换
						return true; // 将行内代码码值转成标准代码值
					}
				}
			}
			return false;
		}
		throw new RuntimeException("字典表不能为空");
	}

	protected String getKey(List dictionarys, String dataKey, String key){
		if(CollectionUtil.isNotEmpty(dictionarys)){
			for(int i = 0; i < dictionarys.size(); i++){
				Dictionary dictionary = (Dictionary) dictionarys.get(i);
				if(StringUtil.equalsIgnoreCase(dictionary.getType(), dataKey)){ // 如果找到币种
					if(StringUtil.equalsIgnoreCase(key, dictionary
							.getValueStandardLetter())){ // 如果ValueBlank为空,默认不需要转换
						return dictionary.getName();
					}
				}
			}
			return key;
		}
		throw new RuntimeException("字典表不能为空");
	}

	// DFHL:东方汇理增加
	/**
	 * 修改/删除原因 增加在状态为‘修改和删除’时修改和删除原因不能为空。 A－新建 C－修改 D－删除 R-申报无误（银行反馈），预留，暂不用。
	 * @param type 操作类型
	 * @param reasion 修改/删除原因
	 */
	public boolean verifyReasion(String type, String reasion){
		if(StringUtil.equalsIgnoreCase("C", type)
				|| StringUtil.equalsIgnoreCase("D", type)){
			if(reasion != null){
				return "".equals(reasion.trim()) ? false : true;
			}
			return false;
		}
		return true;
	}

	// DFHL:东方汇理增加
	/**
	 * 新增原因 增加在状态为‘新增’时修改和删除原因不能为空。 A－新建 C－修改 D－删除 R-申报无误（银行反馈），预留，暂不用。
	 * @param type 操作类型
	 * @param reasion 修改/删除原因
	 */
	public boolean verifyAReasion(String type, String reasion){
		if(StringUtil.equalsIgnoreCase("A", type) || "".equals(type)){
			if(reasion != null){
				return reasion.trim().equals("") ? true : false;
			}
		}
		return true;
	}

	/**
	 * 系统交易时间 交易日期、申报日期必须小于等于当前系统日期
	 * @param tradeDate
	 * @return
	 */
	public static boolean verifyTradeDate(String tradeDate){
		java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		boolean res = false;
		try{
			res = (new Date()).after(sdf.parse(tradeDate));
		}catch (ParseException e){
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 组织机构代码 必输， 技监局代码，代码必须符合技监局的机构代码编制规则，但去掉特殊代码000000000。
	 * @param custcode 组织机构代码
	 * @return
	 */
	public static boolean verifyCustcode(String custcode){
		// 20100604 mxz for test
		/*if (true)
		return true;*/
		// 从companyInfoDataVerify复制来
		if(StringUtil.isEmpty(custcode) || custcode.trim().length() != 9){
			return false;
		}
		if(StringUtil.equals(custcode, "000000000")){
			return false;
		}
		int[] arr = {3, 7, 9, 10, 5, 8, 4, 2};
		String str = custcode.substring(0, custcode.length() - 1);
		String str2 = String.valueOf(custcode.charAt(custcode.length() - 1));
		int sum = 0;
		for(int i = 0; i < str.length(); i++){
			char ch = str.charAt(i);
			int b = 0;
			if(ch >= '0' && ch <= '9'){
				b = ch - 48;
			}else if(ch >= 'A' && ch <= 'Z'){
				b = ch - 55;
			}else{
				return false;
			}
			sum = sum + (b * arr[i]);
		}
		int val = 11 - (sum % 11);
		if(val == 11){
			if(str2.equals("0")){
				return true;
			}
			return false;
		}else if(val == 10){
			if(str2.equals("X")){
				return true;
			}
			return false;
		}else{
			char c = str2.charAt(0);
			if(c >= 'A' && c <= 'Z'){
				int d1 = c - 55;
				if(d1 == val){
					return true;
				}
				return false;
			}else{
				if(str2.equals(val + "")){
					return true;
				}
				return false;
			}
		}
		/* 原始程序
		
		if (StringUtil.isEmpty(custcode) || custcode.trim().length() != 9) {
			return false;
		}

		if (StringUtil.equals(custcode, "000000000")) {
			return false;
		}

		int[] arr = { 3, 7, 9, 10, 5, 8, 4, 2 };
		String str = custcode.substring(0, custcode.length() - 1);
		String str2 = String.valueOf(custcode.charAt(custcode.length() - 1));
		int sum = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int b = 0;
			if (ch >= '0' && ch <= '9') {
				b = ch - 48;
			} else if (ch >= 'A' && ch <= 'Z') {
				b = ch - 55;
			} else {
				return false;
			}
			sum = sum + (b * arr[i]);
		}

		int val = 11 - (sum % 11);

		if (val == 11) {

			if (str2.equals("0")) {
				return true;
			}
			return false;
		} else if (val == 10) {
			if (str2.equals("X")) {

				return true;
			}
			return false;
		} else {
			char c = str2.charAt(0);
			if (c >= 'A' && c <= 'Z') {
				int d1 = c - 55;
				if (d1 == val) {
					return true;
				}
				return false;
			} else {
				if (str2.equals(val + "")) {
					return true;
				}
				return false;
			}
		}*/
	}

	/**
	 * 结汇，人民币账户开户行校验。有人民币账户时必填
	 * @param lcyacc
	 * @param oppbank
	 * @return boolean 校验无误true 校验有误false
	 */
	public boolean verifyOppBank(String lcyacc, String oppbank){
		if(StringUtil.isNotEmpty(lcyacc) && StringUtil.isEmpty(oppbank)){
			return false;
		}
		return true;
	}

	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList){
		this.verifylList = verifylList;
	}

	public void setInterfaceVer(String interfaceVer){
		this.interfaceVer = interfaceVer;
	}
}
