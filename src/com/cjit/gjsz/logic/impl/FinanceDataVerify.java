/**
 * 
 */
package com.cjit.gjsz.logic.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.interfacemanager.model.Dictionary;
import com.cjit.gjsz.logic.DataVerify;

/**
 * @author huboA
 */
public abstract class FinanceDataVerify implements DataVerify{

	public static final String ACTIONTYPE_VERIFY = "A,C,D";
	public static final String PAYATTR_VERIFY = "F,T,O";
	public static final String CHKPRTD_VERIFY = "Y,N";
	protected List dictionarys;
	protected List verifylList;
	protected String interfaceVer;

	public FinanceDataVerify(){
	}

	public FinanceDataVerify(List dictionarys, List verifylList,
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
	 * 已出具出口收汇核销专用联
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
	 * 收汇类型 F-本笔款为福费廷项下收汇 T-本笔款为无追索权的出口保理融资项下收汇 O-其它
	 * @param payattr 收汇类型
	 * @param actionType 操作类型字典
	 */
	public boolean verifyPayattr(String payattr, String actionType){
		if(StringUtil.contains(actionType, payattr)){
			return true;
		}
		return false;
	}

	/**
	 * 余款金额 本笔款为无追索权的出口保理融资项下收汇时才可选填
	 * @param osamt 余款金额
	 * @param payattr 收汇类型
	 * @return
	 */
	public boolean verifyOsamt(BigInteger osamt, String payattr){
		if(!StringUtil.equals(payattr, "T") && osamt != null){
			return false;
		}
		return true;
	}

	/**
	 * 出口收汇核销单号码 本笔收入所对应的所有出口收汇核销单号码，可有多笔，但号码不得重复。
	 * @param refno 出口收汇核销单号码
	 * @return
	 */
	public boolean verifyRefno(boolean flag){
		return flag;
	}

	/**
	 * 收汇总金额中用于出口核销的金额 若出口收汇核销单号码不为空，则收汇总金额中用于出口核销的金额必须>0；
	 * 若收汇总金额中用于出口核销的金额>0，则出口收汇核销单号码不能为空。
	 * @param chkamt 收汇总金额中用于出口核销的金额
	 * @param has 是否有出口收汇核销单号码
	 * @return
	 */
	public boolean verifyChkamt(BigInteger chkamt, boolean has){
		if(has && chkamt != null && chkamt.signum() > 0){
			return true;
		}
		if(has == false && chkamt == null){
			return true;
		}
		return false;
	}

	/**
	 * 已出具出口收汇核销专用联 Y－是 N－否
	 * @param chkprtd 已出具出口收汇核销专用联
	 * @param type 数据字典类型
	 * @return
	 */
	public boolean verifyChkprtd(String chkprtd, String type){
		return verifyPayattr(chkprtd, type);
	}

	/**
	 * 填报人 必输项
	 * @param crtuser 填报人
	 * @return
	 */
	public boolean verifyCrtuser(String crtuser){
		return StringUtil.isNotEmpty(crtuser) ? true : false;
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
	 * @param repnodate 申报号码中的日期
	 * @return
	 */
	public boolean verifyRptdate(String rptdate, String repnodate){
		if(StringUtil.isEmpty(rptdate)){
			return false;
		}else{
			// 单据申报日期
			Date date1 = DateUtils.stringToDate(rptdate,
					DateUtils.ORA_DATE_FORMAT);
			if(StringUtil.isNotEmpty(repnodate)){
				// 申报号码中的日期
				Date date2 = DateUtils.stringToDate(repnodate,
						DateUtils.ORA_DATE_FORMAT_SIMPLE);
				if(date1.getTime() >= date2.getTime()){
					// 必须晚于或等于申报号码中的日期
					return true;
				}else{
					return false;
				}
			}else{
				// 当前系统日期
				Date date2 = new Date();
				if(date1.getTime() <= date2.getTime()){
					// 必须不晚于当前系统日期
					return true;
				}else{
					return false;
				}
			}
		}
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
	 * 最迟装运日期 付款方式为预付货款时，必须输入，系统检查日期格式及合法性 20091126 修改检测impdate 为空变为 检测paytype
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
	 * 提运单号 必输项
	 * @param billno 发票号
	 * @return
	 */
	public boolean verifyBillno(String billno){
		return StringUtil.isNotEmpty(billno) ? true : false;
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
	public boolean verifyCustomn(String customn, String custccy,
			BigInteger custamt, BigInteger offamt, String type){
		if(StringUtil.equals("P", type)){
			if(StringUtil.isEmpty(customn) || StringUtil.isEmpty(custccy)
					|| custamt == null || offamt == null){
				return false;
			}
		}
		return true;
	}

	/**
	 * 报关单币种 校验币种是否存在
	 * @param custccy 报关单币种
	 * @param code
	 * @return
	 */
	public boolean verifyCustccy(String custccy, String code){
		if(StringUtil.isEmpty(custccy)){
			return true;
		}
		return findKey(dictionarys, code, custccy);
	}

	/**
	 * 报关金额 必须大于0 若币种不为空则对应金额必须>0；若金额>0，则对应币种不能为空。
	 * @param custamt 报关金额
	 * @param custccy 报关单币种
	 * @return
	 */
	public boolean verifyCustamt(BigInteger custamt, String custccy){
		if(custamt == null || custamt.signum() <= 0){
			return false;
		}
		if(custamt.signum() > 0 && StringUtil.isEmpty(custccy)){
			return false;
		}
		return true;
	}

	/**
	 * 本次核注金额 系统校验本次核注金额应小于等于报关金额
	 * @param offamt 本次核注金额
	 * @param custamt 报关金额
	 * @return
	 */
	public boolean verifyOffamt(BigInteger offamt, BigInteger custamt){
		if(custamt != null && offamt != null){
			BigInteger tmp = offamt.subtract(custamt);
			if(tmp.signum() <= 0){
				return true;
			}
			return false;
		}
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
				return "".equals(reasion.trim()) ? true : false;
			}
		}
		return true;
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
	 * 结汇详细用途 校验
	 * @param usedetail 结汇详细用途
	 * @param usetype 结汇用途
	 * @return 校验通过true 校验未通过false
	 */
	public boolean verifyUseDetail(String usedetail, String usetype){
		if("005".equals(usetype) || "006".equals(usetype)
				|| "099".equals(usetype)){
			if(StringUtil.isEmpty(usedetail)){
				// 如果结汇用途选择“005”、“006”或“099”，则应填列详细用途
				return false;
			}
		}
		return true;
	}

	public void setInterfaceVer(String interfaceVer){
		this.interfaceVer = interfaceVer;
	}
}
