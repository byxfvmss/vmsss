/**
 * 
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;

/**
 * @author huboA
 */
public abstract class DeclareDataVerify implements DataVerify{

	public static final String ACTIONTYPE_VERIFY = "A,C,D";
	public static final String PAYTYPE_VERIFY = "A,R,O";
	public static final String PAYATTR_VERIFY = "M,X,R,C,I,F,T,N,L,S,B,O";
	public static final String PAYATTR_VERIFY_12 = "M,X,E,D,A,R,C,I,N,L,B,O";
	public static final String ISREF_VERIFY = "Y,N";
	protected List dictionarys;
	protected List verifylList;
	protected String interfaceVer;

	public DeclareDataVerify(){
	}

	public DeclareDataVerify(List dictionarys, List verifylList,
			String interfaceVer){
		this.dictionarys = dictionarys;
		this.verifylList = verifylList;
		this.interfaceVer = interfaceVer;
	}

	/**
	 * 操作类型 A－新建 C－修改 D－删除
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
		 * if (StringUtil.isEmpty(rptno)) { if (StringUtil.equalsIgnoreCase("A",
		 * type)) { return true; } return false; }
		 */
		// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 start
		/*
		 * else { if (!StringUtil.equalsIgnoreCase("A", type)) { return true; }
		 * return false; }
		 */
		// return true;
		// DFHL: 去掉否则当时 [申报申请号] 为不为空时, [操作类型] 不允许为 [新建]的校验 end
	}

	/**
	 * 付款人常驻国家/地区代码
	 * @param country
	 * @param type
	 * @return
	 */
	public boolean verifyCountry(String country, String type){
		if(StringUtil.isNotEmpty(country)){
			return this.findKey(dictionarys, type, country);
		}
		return false;
	}

	/**
	 * 收款性质 A－预收货款 R－退款 O-其它
	 * @param paytype 操作类型
	 * @param actionType 操作类型字典
	 */
	public boolean verifyPaytype(String paytype, String actionType){
		if(StringUtil.isEmpty(paytype)){
			return true;
		}
		if(StringUtil.contains(actionType, paytype)){
			return true;
		}
		return false;
	}

	/**
	 * 境内收汇类型 M-深加工结转 X-特殊经济区域 R-汇路引起出口项下跨境收汇 C-出口信用保险理赔 I-出口货物保险理赔 F-福费廷业务
	 * T-无追索权出口保理业务 N-买方信贷 L-转让信用证 S-离岸业务 B-背对背信用证 O-其它出口项下收汇
	 * @param payattr 境内收汇类型
	 * @param actionType 操作类型字典
	 * @return
	 */
	public boolean verifyPayattr(String payattr, String actionType){
		if(StringUtil.contains(actionType, payattr)){
			return true;
		}
		return false;
	}

	/**
	 * 交易编码1 必输 必须在国际收支交易编码表中存在
	 * @param txcode 交易编码1
	 * @param type 操作类型字典
	 * @return
	 */
	public boolean verifyTxcode(String txcode, String type){
		if(StringUtil.isNotEmpty(txcode)){
			return this.findKey(dictionarys, type, txcode);
		}
		return false;
	}

	/**
	 * 相应金额1 必输 且不能小于0
	 * @param tc1amt 相应金额1
	 * @return
	 */
	public boolean verifyTc1amt(BigInteger tc1amt){
		if(tc1amt == null){
			return false;
		}else{
			if(tc1amt.compareTo(BigInteger.ZERO) == -1){
				return false;
			}
		}
		return true;
	}

	/**
	 * 交易附言1 必输
	 * @param txrem 交易附言1
	 * @return
	 */
	public boolean verifyTxrem(String txrem){
		return StringUtil.isNotEmpty(txrem);
	}

	/**
	 * 交易编码2 选输 必须在国际收支交易编码表中存在 不能与交易编码1相同， 没有输入交易编码时，相应金额及交易附言不应该填写。
	 * 有交易金额2或交易附言2时必填。
	 * @param txcode2 交易编码2
	 * @param type 操作类型字典
	 * @param txcode1 交易编码1
	 * @param tc2amt 相应金额2
	 * @param tx2rem 交易附言2
	 * @return
	 */
	public boolean verifyTxcode2(String txcode2, String type, String txcode1,
			BigInteger tc2amt, String tx2rem){
		if(StringUtil.isEmpty(txcode2)){
			if(tc2amt != null || StringUtil.isNotEmpty(tx2rem)){
				return false;
			}
		}
		if(StringUtil.isNotEmpty(txcode2)){
			if(this.findKey(dictionarys, type, txcode2)){
				if(StringUtil.equals(txcode2, txcode1)){
					return false;
				}
			}else{
				return false;
			}
		}
		if(tc2amt != null || StringUtil.isNotEmpty(tx2rem)){
			if(StringUtil.isEmpty(txcode2)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 相应金额2 必输
	 * @param tc1amt 相应金额2
	 * @param txcode2 交易编码2
	 * @param tc1amt 相应金额1
	 * @param txamt 收入款金额
	 * @return
	 */
	/*
	 * public boolean verifyTc2amt(BigInteger tc2amt, String txcode2, Object
	 * tc1amt, Object txamt) { BigInteger tc1amt1=BigInteger.valueOf(0);
	 * BigInteger txamt1=BigInteger.valueOf(0); BigInteger
	 * tc2amt1=BigInteger.valueOf(0); if (tc1amt!=null){
	 * tc1amt1=(BigInteger)tc1amt; } if (txamt!=null){ txamt1=(BigInteger)txamt; }
	 * if (tc2amt!=null){ tc2amt1=(BigInteger)tc2amt; } return
	 * verifyTc2amt(tc2amt1,txcode2,tc1amt1,txamt1); }
	 */
	public boolean verifyTc2amt(BigInteger tc2amt, String txcode2,
			BigInteger tc1amt, BigInteger txamt){
		if(txamt == null)
			txamt = BigInteger.ZERO;
		if(StringUtil.isNotEmpty(txcode2)){
			if(tc2amt != null){
				if(tc2amt.compareTo(BigInteger.ZERO) == -1){
					return false;
				}
				BigInteger tmp = tc2amt.add(tc1amt);
				if(txamt.compareTo(tmp) == 0){
					return true;
				}
			}else{
				return false;
			}
		}else{
			if(txamt.compareTo(tc1amt) == 0){
				return true;
			}
		}
		return false;
	}

	/**
	 * 交易附言2
	 * @param txcode2 交易编码2
	 * @param tx2rem 交易附言2
	 * @return
	 */
	public boolean verifyTx2rem(String tx2rem, String txcode2){
		if(StringUtil.isNotEmpty(txcode2) && StringUtil.isNotEmpty(tx2rem)){
			return true;
		}
		if(StringUtil.isEmpty(txcode2) && StringUtil.isEmpty(tx2rem)){
			return true;
		}
		return false;
	}

	/**
	 * 是否出口核销项下收汇
	 * @param isref 是否出口核销项下收汇
	 * @param isrefType 字典类型
	 * @return
	 */
	public boolean verifyIsref(String isref, String isrefType){
		if(StringUtil.contains(isrefType, isref)){
			return true;
		}
		return false;
	}

	/**
	 * 收汇总金额中用于出口核销的金额 若出口收汇核销单号码不为空，则收汇总金额中用于出口核销的金额必须>0；
	 * 若收汇总金额中用于出口核销的金额>0，则出口收汇核销单号码不能为空。
	 * @param chkamt 收汇总金额中用于出口核销的金额
	 * @param refno 出口收汇核销单号码
	 * @return
	 */
	public boolean verifyChkamt(BigInteger chkamt, boolean has){
		if(has){
			if(chkamt != null && chkamt.signum() > 0){
				return true;
			}
			return false;
		}
		if(chkamt != null && chkamt.signum() > 0){
			return false;
		}
		return true;
	}

	/**
	 * 外债编号 选输项，如果本笔款为外债提款
	 * @param billno 外债编号
	 * @return
	 */
	public boolean verifyBillno(String billno){
		return true;
	}

	/**
	 * 填报人
	 * @param crtuser 填报人
	 * @return
	 */
	public boolean verifyCrtuser(String crtuser){
		if(StringUtil.isNotEmpty(crtuser)){
			return true;
		}
		return false;
	}

	/**
	 * 填报人电话
	 * @param inptelc 填报人电话
	 * @return
	 */
	public boolean verifyInptelc(String inptelc){
		if(StringUtil.isNotEmpty(inptelc)){
			return true;
		}
		return false;
	}

	/**
	 * 申报日期 必填项，按申报主体真实申报日期填写。申报日期>=申报号码中的日期。
	 * @param rptdate 申报日期
	 * @param repno 申报号码
	 * @param tradedate 基础信息交易日期
	 * @return
	 */
	public boolean verifyRptdate(String rptdate, String repno,
			String tradedate, StringBuffer sb){
		if(StringUtil.isNotEmpty(rptdate)){
			Date dtSystem = new Date();
			Date dtRptDate = DateUtils.stringToDate(rptdate,
					DateUtils.ORA_DATE_FORMAT);
			if(dtSystem.getTime() < dtRptDate.getTime()){
				// sb.append(DateUtils.toString(dtSystem,
				// DateUtils.ORA_DATE_FORMAT));
				return false;
			}else if(StringUtil.isNotEmpty(repno)){
				if(repno.trim().length() != 22){
					return false;
				}
				String repnodate = repno.substring(12, 18);
				Date dtRepnoDate = DateUtils.stringToDate(repnodate,
						DateUtils.ORA_DATE_FORMAT_SIMPLE);
				if(dtRptDate.getTime() >= dtRepnoDate.getTime()){
					return true;
				}
				sb.append(repnodate);
				return false;
			}else if(StringUtil.isNotEmpty(rptdate)
					&& StringUtil.isNotEmpty(tradedate)){
				// 在没有申报号的情况下（还没生成），比较基础信息中的交易日期(TRADEDATE)
				Date dtTradeDate = DateUtils.stringToDate(tradedate,
						DateUtils.ORA_DATE_FORMAT);
				if(dtRptDate.getTime() >= dtTradeDate.getTime()){
					return true;
				}
				// sb.append(tradedate);
				return false;
			}
		}
		return false;
	}

	/**
	 * 申报日期 必填项，按申报主体真实申报日期填写。
	 * @param rptdate 报日期申
	 * @return
	 */
	public boolean verifyRptdate(String rptdate){
		if(StringUtil.isNotEmpty(rptdate) && rptdate.length() == 8){
			Date date1 = new Date();
			Date date2 = DateUtils.stringToDate(rptdate,
					DateUtils.ORA_DATE_FORMAT);
			if(date1.getTime() >= date2.getTime()){
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 最迟装运日期 付款方式为预付货款时，必须输入，系统检查日期格式及合法性
	 * @param impdate 最迟装运日期
	 * @param paytype 境外汇款类型
	 * @return
	 */
	public boolean verifyImpdate(String impdate, String paytype){
		// if (StringUtil.isEmpty(paytype)) {
		// return false;
		// }
		if(StringUtil.equals(paytype, "A")){
			if(impdate != null && impdate.trim().length() == 8){
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * 最迟装运日期 付款方式为预付货款时，必须输入，系统检查日期格式及合法性
	 * @param impdate 最迟装运日期
	 * @return
	 */
	public boolean verifyImpdate(String impdate){
		if(StringUtil.isEmpty(impdate)){
			return false;
		}
		if(impdate.length() == 8){
			return true;
		}
		return false;
	}

	/**
	 * 填报人 必输项
	 * @param contrno 合同号
	 * @return
	 */
	public boolean verifyContrno(String contrno){
		return StringUtil.isNotEmpty(contrno) ? true : false;
	}

	/**
	 * 发票号 必输项
	 * @param invoino 发票号
	 * @return
	 */
	public boolean verifyInvoino(String invoino){
		return StringUtil.isNotEmpty(invoino) ? true : false;
	}

	/**
	 * 报关单经营单位代码
	 * @param cusmno 报关单经营单位代码
	 * @param has 是否有报关单
	 * @return
	 */
	public boolean verifyCusmno(String cusmno, boolean has){
		if(has){
			return StringUtil.isNotEmpty(cusmno) ? true : false;
		}
		return true;
	}

	/**
	 * 报关单号 付款方式为货到付款必须输入 当输入报关单，则以下各项报关单信息都必须输入
	 * @param customn
	 * @param has
	 * @return
	 */
	public boolean verifyCustomn(String customn, String custccy, Long custamt,
			Long offamt, String crtuser, String inptelc, String rptdate,
			String type){
		if(StringUtil.equals("P", type)){
			if(StringUtil.isEmpty(customn) || StringUtil.isEmpty(custccy)
					|| custamt == null || offamt == null
					|| StringUtil.isEmpty(crtuser)
					|| StringUtil.isEmpty(inptelc)
					|| StringUtil.isEmpty(rptdate)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 出口收汇核销单号码 本笔收入所对应的所有出口收汇核销单号码，可有多笔，但号码不得重复。
	 * @param refno 出口收汇核销单号码
	 * @return
	 */
	public boolean verifyRefno(String refno){
		return true;
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

	public void setDictionarys(List dictionarys){
		this.dictionarys = dictionarys;
	}

	public void setVerifylList(List verifylList){
		this.verifylList = verifylList;
	}

	// DFHL:东方汇理增加
	/**
	 * 修改/删除原因 增加在状态为‘修改和删除’是修改和删除原因不能为空。 A－新建 C－修改 D－删除 R-申报无误（银行反馈），预留，暂不用。
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
				return "".equalsIgnoreCase(reasion.trim()) ? true : false;
			}
		}
		return true;
	}

	// DFHL:金宏工程外汇局子项与银行业务系统数据接口规范中
	/**
	 * 
	 * 
	 */
	public boolean verifyOrgCodeOfCompany(String orgCode){
		Map map = new HashMap();
		map.put("ids", orgCode);
		// return find("getCompanyOpenInfos", map);
		return true;
	}

	public void setInterfaceVer(String interfaceVer){
		this.interfaceVer = interfaceVer;
	}
}
