package com.cjit.gjsz.logic.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.logic.DataVerify;
import com.cjit.gjsz.logic.SearchService;
import com.cjit.gjsz.logic.VerifyConfig;
import com.cjit.gjsz.logic.model.Self_A_EXDEBT;
import com.cjit.gjsz.logic.model.Self_Sub_CREDITOR;
import com.cjit.gjsz.logic.model.Self_Sub_PROJECT;
import com.cjit.gjsz.logic.model.VerifyModel;

/**
 * @作者: lihaiboA
 * @描述: [T_CFA_A_EXDEBT]银行自身外债－外债信息校验类
 */
public class Self_A_DataVerify extends SelfDataVerify implements DataVerify{

	public Self_A_DataVerify(){
	}

	public Self_A_DataVerify(List dictionarys, List verifylList,
			String interfaceVer, String isCluster){
		super(dictionarys, verifylList, interfaceVer, isCluster);
	}

	public VerifyModel execute(){
		Map map = new HashMap();
		VerifyModel verifyModel = new VerifyModel();
		verifyModel.setFatcher(map);
		if(CollectionUtil.isNotEmpty(verifylList)){
			for(int i = 0; i < verifylList.size(); i++){
				Self_A_EXDEBT exdebt = (Self_A_EXDEBT) verifylList.get(i);
				service = (SearchService) SpringContextUtil
						.getBean("searchService");
				if(!verifyDictionaryValue(ACTIONTYPE, exdebt.getActiontype())){
					String value = getKey(exdebt.getActiontype(), ACTIONTYPE);
					map.put("ACTIONTYPE", "[操作类型] [" + value + "] 无效 ");
				}else if(ACTIONTYPE_D.equalsIgnoreCase(exdebt.getActiontype())){
					if(StringUtil.isEmpty(exdebt.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型为D-删除时，本字段必填 ");
					}
				}else if(!ACTIONTYPE_D.equalsIgnoreCase(exdebt.getActiontype())){
					if(!isNull(exdebt.getActiondesc())){
						map.put("ACTIONDESC", "[删除原因] 操作类型不为D-删除时，删除原因应该为空 ");
					}
				}
				// 签约信息－外债编号
				if(StringUtil.isNotEmpty(exdebt.getExdebtcode())){
					String vErrInfo = verifyRptNo(exdebt.getExdebtcode(),
							exdebt.getFiletype());
					if(StringUtil.isNotEmpty(vErrInfo)){
						map.put("EXDEBTCODE", "[外债编号] " + vErrInfo);
					}
				}
				// 签约信息－债务人代码
				if(exdebt.getDebtorcode() != null){
					if(exdebt.getDebtorcode().length() != 12){
						map.put("DEBTORCODE", "[债务人代码] 应为12位金融机构标识码 ");
					}else{
						String strDistrictCode = exdebt.getDebtorcode()
								.substring(0, 6);
						if(!verifyDictionaryValue(DISTRICTCO, strDistrictCode)){
							map.put("DEBTORCODE", "[债务人代码] 前6位数字地区标识码有误 ");
						}
					}
				}
				// 签约信息－债务类型
				if(exdebt.getDebtype() != null){
					if(!verifyDictionaryValue(DEBTYPE, exdebt.getDebtype())){
						String value = getKey(exdebt.getDebtype(), DEBTYPE);
						map.put("DEBTYPE", "[债务类型] [" + value + "] 无效 ");
					}
				}
				// 签约信息－签约日期、起息日、到期日
				if(exdebt.getContractdate() != null){
					if(!verifyTwoDates(exdebt.getContractdate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("CONTRACTDATE", "[签约日期] 不能晚于当前日期 ");
					}
				}
				if(exdebt.getValuedate() != null
						&& exdebt.getContractdate() != null){
					if(!verifyTwoDates(exdebt.getContractdate(), exdebt
							.getValuedate())){
						map.put("VALUEDATE", "[起息日] 不能早于签约日期 ");
					}
				}
				if(exdebt.getMaturity() != null
						&& exdebt.getValuedate() != null){
					if(!verifyTwoDates(exdebt.getValuedate(), exdebt
							.getMaturity())){
						map.put("MATURITY", "[到期日] 不能早于起息日 ");
					}
				}
				// 签约信息－签约币种
				if(exdebt.getContractcurr() != null){
					if(!verifyDictionaryValue(CURRENCY, exdebt
							.getContractcurr())){
						String value = getKey(exdebt.getContractcurr(),
								CURRENCY);
						map.put("CONTRACTCURR", "[签约币种] [" + value + "] 无效 ");
					}
				}
				// 签约信息－是否浮动利率
				if(!isNull(exdebt.getFloatrate())
						&& !verifyDictionaryValue(YESORNO, exdebt
								.getFloatrate())){
					String value = getKey(exdebt.getFloatrate(), YESORNO);
					map.put("FLOATRATE", "[是否浮动利率] [" + value + "] 无效 ");
				}
				// 签约信息－是否经外汇局特批不需占用指标
				if(exdebt.getSpapreboindex() != null){
					if(!verifyDictionaryValue(YESORNO, exdebt
							.getSpapreboindex())){
						String value = getKey(exdebt.getSpapreboindex(),
								YESORNO);
						map.put("SPAPFEBOINDEX", "[是否经外汇局特批不需占用指标] [" + value
								+ "] 无效 ");
					}
				}
				// 签约信息－签约金额
				if(exdebt.getContractamount() != null
						&& exdebt.getContractamount().compareTo(
								new BigDecimal(0.0)) < 0){
					map.put("CONTRACTAMOUNT", "[签约金额] 应大于等于0 ");
				}
				// 签约信息－年化利率值
				if(exdebt.getAnninrate() != null
						&& exdebt.getAnninrate().compareTo(new BigDecimal(0.0)) < 0){
					map.put("ANNINRATE",
							"[年化利率值] 应大于等于0，按小数填写，如利率为3.21%，则填写0.0321 ");
				}
				if("AA".equals(exdebt.getFiletype())){
					// 双边贷款—签约信息
					// DEBTYPE 债务类型
					if(!"1101".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					// 项目信息
					List subList = service.getCfaChildren(
							"T_CFA_SUB_PROJECT_INFO", exdebt.getBusinessid());
					List list = new ArrayList();
					if(CollectionUtil.isNotEmpty(subList)){
						Self_Sub_ProjectVerify subVerify = null;
						for(int j = 0; j < subList.size(); j++){
							Self_Sub_PROJECT sub = (Self_Sub_PROJECT) subList
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_ProjectVerify(dictionarys,
									tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_PROJECT_INFO");
								list.add(vm.getFatcher());
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							verifyModel.setChildren(list);
						}
					}else{
						map.put("T_CFA_SUB_PROJECT_INFO", "[项目信息] 不能为空 ");
					}
				}else if("AB".equals(exdebt.getFiletype())){
					// 买方信贷—签约信息
					// DEBTYPE 债务类型
					if(!"1103".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					// 项目信息
					List subList = service.getCfaChildren(
							"T_CFA_SUB_PROJECT_INFO", exdebt.getBusinessid());
					List list = new ArrayList();
					if(CollectionUtil.isNotEmpty(subList)){
						Self_Sub_ProjectVerify subVerify = null;
						for(int j = 0; j < subList.size(); j++){
							Self_Sub_PROJECT sub = (Self_Sub_PROJECT) subList
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_ProjectVerify(dictionarys,
									tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_PROJECT_INFO");
								list.add(vm.getFatcher());
							}
						}
						if(CollectionUtil.isNotEmpty(list)){
							verifyModel.setChildren(list);
						}
					}else{
						map.put("T_CFA_SUB_PROJECT_INFO", "[项目信息] 不能为空 ");
					}
				}else if("AC".equals(exdebt.getFiletype())){
					// 境外同业拆借—签约信息
					// DEBTYPE 债务类型
					if(!"1105".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AD".equals(exdebt.getFiletype())){
					// 海外代付—签约信息
					// DEBTYPE 债务类型
					if(!"1106".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AE".equals(exdebt.getFiletype())){
					// 卖出回购—签约信息
					// DEBTYPE 债务类型
					if(!"1107".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					// INPRIAMOUNT 利息本金化金额 数值型，22.2 非必填项，大于等于0。
					if(!isNull(exdebt.getInpriamount())
							&& exdebt.getInpriamount().compareTo(
									new BigDecimal("0.00")) < 0){
						map.put("INPRIAMOUNT", "[利息本金化金额] 可为空否则应大于等于0 ");
					}
				}else if("AF".equals(exdebt.getFiletype())){
					// 远期信用证—签约信息
					// DEBTYPE 债务类型
					if(!"1108".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AG".equals(exdebt.getFiletype())){
					// 银团贷款—签约信息
					// DEBTYPE 债务类型
					if(!"1110".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					List list = new ArrayList();
					// 债权人信息
					List sub1List = service.getCfaChildren(
							"T_CFA_SUB_CREDITOR_INFO", exdebt.getBusinessid());
					// 项目信息
					List sub2List = service.getCfaChildren(
							"T_CFA_SUB_PROJECT_INFO", exdebt.getBusinessid());
					if(CollectionUtil.isNotEmpty(sub1List)){
						Self_Sub_CreditorVerify subVerify = null;
						for(int j = 0; j < sub1List.size(); j++){
							Self_Sub_CREDITOR sub = (Self_Sub_CREDITOR) sub1List
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_CreditorVerify(
									dictionarys, tmp, exdebt);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_CREDITOR_INFO");
								list.add(vm.getFatcher());
							}
						}
					}else{
						map.put("T_CFA_SUB_CREDITOR_INFO", "[债权人信息] 不能为空 ");
					}
					if(CollectionUtil.isNotEmpty(sub2List)){
						Self_Sub_ProjectVerify subVerify = null;
						for(int j = 0; j < sub2List.size(); j++){
							Self_Sub_PROJECT sub = (Self_Sub_PROJECT) sub2List
									.get(j);
							List tmp = new ArrayList();
							tmp.add(sub);
							subVerify = new Self_Sub_ProjectVerify(dictionarys,
									tmp);
							VerifyModel vm = subVerify.execute();
							if(vm.getFatcher() != null
									&& !vm.getFatcher().isEmpty()){
								vm.getFatcher().put(SUBID, sub.getSubid());
								vm.getFatcher().put(INNERTABLEID,
										"T_CFA_SUB_PROJECT_INFO");
								list.add(vm.getFatcher());
							}
						}
					}
					if(CollectionUtil.isNotEmpty(list)){
						verifyModel.setChildren(list);
					}
				}else if("AH".equals(exdebt.getFiletype())){
					// 贵金属拆借—签约信息
					// DEBTYPE 债务类型
					if(!"1117".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AI".equals(exdebt.getFiletype())){
					// 其他贷款—签约信息
					// DEBTYPE 债务类型
					if(!"1199".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AJ".equals(exdebt.getFiletype())){
					// 货币市场工具—签约信息
					// DEBTYPE 债务类型
					if(!"1201".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					if(isNull(exdebt.getIsincode())
							|| !verifyIsinCode(exdebt.getIsincode())){
						map
								.put("ISINCODE",
										"[国际证券识别编码] 不能为空，共12位。第1-2位为字母，用于标识国别；第3-11位为数字和字母，为国内证券识别编码；第12位为数字，为校对码 ");
					}
					// CREDITORTYPE 债权人类型代码
					if(isNull(exdebt.getCreditortype())
							|| !"20001800".equalsIgnoreCase(exdebt
									.getCreditortype())){
						map.put("CREDITORTYPE",
								"[债权人类型代码] 不能为空，应为资本市场所对应的债权人类型代码 ");
					}
				}else if("AK".equals(exdebt.getFiletype())){
					// 债券和票据—签约信息
					// DEBTYPE 债务类型
					if(!"1202".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					if(isNull(exdebt.getIsincode())
							|| !verifyIsinCode(exdebt.getIsincode())){
						map
								.put("ISINCODE",
										"[国际证券识别编码] 不能为空，共12位。第1-2位为字母，用于标识国别；第3-11位为数字和字母，为国内证券识别编码；第12位为数字，为校对码 ");
					}
					// CREDITORTYPE 债权人类型代码
					if(isNull(exdebt.getCreditortype())
							|| !"20001800".equalsIgnoreCase(exdebt
									.getCreditortype())){
						map.put("CREDITORTYPE",
								"[债权人类型代码] 不能为空，应为资本市场所对应的债权人类型代码 ");
					}
				}else if("AL".equals(exdebt.getFiletype())){
					// 境外同业存放—签约信息
					// DEBTYPE 债务类型
					if(!"1301".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AM".equals(exdebt.getFiletype())){
					// 境外联行及附属机构往来—签约信息
					// DEBTYPE 债务类型
					if(!"1302".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AN".equals(exdebt.getFiletype())){
					// 非居民机构存款—签约信息
					// DEBTYPE 债务类型
					if(!"1303".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
					// CREDITORTYPE 债权人类型代码
					if(!"20001699".equalsIgnoreCase(exdebt.getCreditortype())){
						// 应"问答第三期"而添加 应"外债签约数据错误类型.doc"而删除
						// map.put("CREDITORTYPE",
						// "[债权人类型代码] 应在境外主体类型代码表中选择“其他企业”（二级主体代码20001699）");
					}
				}else if("AP".equals(exdebt.getFiletype())){
					// 非居民个人存款—签约信息
					// DEBTYPE 债务类型
					if(!"1304".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}else if("AQ".equals(exdebt.getFiletype())){
					// 其他外债—签约信息
					// DEBTYPE 债务类型
					if(!"9900".equalsIgnoreCase(exdebt.getDebtype())){
						map.put("DEBTYPE", "[债务类型] 外债类型代码与业务类型不一致 ");
					}
				}
				// 若当前校验报文为变动信息或余额信息，则需查询对应上级签约信息
				if("AR".equals(exdebt.getFiletype())
						|| "AS".equals(exdebt.getFiletype())){
					Self_A_EXDEBT contractExdebt = (Self_A_EXDEBT) service
							.getDataVerifyModel("T_CFA_A_EXDEBT", exdebt
									.getExdebtcode(), exdebt.getBusinessno());
					if(contractExdebt == null){
						map.put("EXDEBTCODE", "未发现此外债编号对应的签约信息 ");
						continue;
					}else if(ACTIONTYPE_A.equals(exdebt.getActiontype())
							&& contractExdebt.getDatastatus() < DataUtil.JYYTG_STATUS_NUM){
						map.put("EXDEBTCODE", "对应签约信息尚未校验通过");
						continue;
					}else if(ACTIONTYPE_D
							.equals(contractExdebt.getActiontype())
							&& contractExdebt.getDatastatus() == DataUtil.YBS_STATUS_NUM){
						map.put("EXDEBTCODE", "对应签约信息已报送删除");
						continue;
					}
					List changeInfoList = findChangeInfoList(exdebt
							.getFiletype(), "CHANGENO", exdebt.getChangeno(),
							exdebt.getExdebtcode(), exdebt.getBusinessno(),
							exdebt.getBusinessid(), " order by CHANGENO desc ");
					if(CollectionUtil.isNotEmpty(changeInfoList)
							&& StringUtil.isNotEmpty(exdebt.getChangeno())){
						if(configMap != null){
							String checkByeRptNoRepeat = (String) configMap
									.get("config.check.byeRptNo.repeat");
							if("yes".equalsIgnoreCase(checkByeRptNoRepeat)){
								// 得到查询出结果中变动编号最大的记录
								Self_A_EXDEBT lastExdebt = (Self_A_EXDEBT) changeInfoList
										.get(0);
								if(lastExdebt != null
										&& exdebt.getChangeno().equals(
												lastExdebt.getChangeno())){
									map.put("CHANGENO", "[变动编号] 已经存在 ");
									continue;
								}
							}
						}
						// HSBC
						for(int j = 0; j < changeInfoList.size(); j++){
							Self_A_EXDEBT formerExdebt = (Self_A_EXDEBT) changeInfoList
									.get(j);
							if(formerExdebt != null
									&& formerExdebt.getDatastatus() < DataUtil.SHYTG_STATUS_NUM
									&& formerExdebt.getDatastatus() != DataUtil.DELETE_STATUS_NUM){
								map.put("CHANGENO", "[变动编号] 存在尚未审核通过的往期记录 ");
								continue;
							}
						}
					}
					if("AR".equals(exdebt.getFiletype())){
						// 外债－变动信息校验
						if(exdebt.getActiontype().equals("D")
								&& StringUtil.isEmpty(exdebt.getActiondesc())){
							map
									.put("ACTIONDESC",
											"[删除原因] 当操作类型为D删除时，必需填写删除原因 ");
						}
						if(exdebt.getChamount() == null
								|| exdebt.getChamount().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("CHAMOUNT", "[变动金额] 应大于或等于零 ");
						}
						if(!verifyChangeDate(changeInfoList, "CHDATE", exdebt
								.getChdate())){
							map.put("CHDATE", "[变动日期] 需晚于前期变动日期 ");
						}
						// 校验银行报送数据的第一条变动信息必须为提款
						// (汇发36号文和49号文银行数据接口规范校验内容补充说明.doc)
						if(isNull(exdebt.getChangeno())
								|| "0001".equals(exdebt.getChangeno())
								|| CollectionUtil.isEmpty(changeInfoList)){
							if(!isNull(exdebt.getChangtype())
									&& !exdebt.getChangtype().startsWith("11")){
								map.put("CHANGTYPE", "[变动类型] 第一次变动必须为提款 ");
							}
						}
					}else if("AS".equals(exdebt.getFiletype())){
						// 外债－余额信息校验
						if("1304".equals(contractExdebt.getDebtype())
								&& !"N/A".equals(exdebt.getBuscode())){
							map
									.put("BUSCODE",
											"[银行账号] 当债务类型为“非居民个人存款”时，因非居民个人存款为分币种分国别汇总报送，此处应填写“N/A” ");
						}
						if(exdebt.getAccoamount() == null
								|| exdebt.getAccoamount().compareTo(
										new BigDecimal(0.0)) < 0){
							map.put("ACCOAMOUNT", "[外债余额] 应大于或等于零 ");
						}
					}
					if(StringUtil.isNotEmpty(contractExdebt.getContractdate())){
						if(!verifyTwoDates(contractExdebt.getContractdate(),
								exdebt.getChdate())){
							map.put("CHDATE", "[变动日期] 不能早于签约日期 ["
									+ contractExdebt.getContractdate() + "] ");
						}
					}else{
						if(!verifyTwoDates(contractExdebt.getTradedate(),
								exdebt.getChdate())){
							map.put("CHDATE", "[变动日期] 不能早于签约信息交易日期 ["
									+ contractExdebt.getTradedate() + "] ");
						}
					}
				}else{
					if(ACTIONTYPE_D.equals(exdebt.getActiontype())){
						if(!verifyCannotDelete("T_CFA_A_EXDEBT", exdebt
								.getFiletype(), exdebt.getExdebtcode())){
							map.put("ACTIONTYPE", "[操作类型] 银行已报送该外债编号 ["
									+ exdebt.getExdebtcode()
									+ "] 下的变动或余额信息，本信息不可删除 ");
						}
					}
					if(!"AG".equals(exdebt.getFiletype())){
						// 债权人代码
						if("AN".equals(exdebt.getFiletype())){
							// 该笔外债的债务类型为“非居民机构存款”，应填写外汇局签发的特殊机构代码赋码通知书上的特殊机构代码。
							if("1303".equals(exdebt.getDebtype())
									&& isNull(exdebt.getCreditorcode())){
								// map
								// .put("CREDITORCODE",
								// "[债权人代码]
								// 该笔外债的债务类型为“非居民机构存款”，应填写外汇局签发的特殊机构代码赋码通知书上的特殊机构代码
								// ");
							}
						}else if("AP".equals(exdebt.getFiletype())){
							// 该笔外债的债务类型为“非居民个人存款”，应在部分债权人代码表中选择该国个人对应的代码。
							if("1304".equals(exdebt.getDebtype())
									&& isNull(exdebt.getCreditorcode())){
								// map
								// .put("CREDITORCODE",
								// "[债权人代码]
								// 该笔外债的债务类型为“非居民个人存款”，应在部分债权人代码表中选择该国个人对应的代码
								// ");
							}
							if(!isNull(exdebt.getCreditortype())
									&& !isNull(exdebt.getCreditorcode())){
								String errorMsg = this.verifyCreditorCode(
										exdebt.getDebtype(), exdebt
												.getCreditortype(), exdebt
												.getCreditorcode());
								if(StringUtil.isNotEmpty(errorMsg)){
									map.put("CREDITORCODE", "[债权人代码] "
											+ errorMsg);
								}
							}
						}else if("AQ".equals(exdebt.getFiletype())){
							// 如果债权人为银行，应填写8位或11位的SWIFT CODE；
							// 如果债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”时，应在外汇局编制的部分债权人代码表中选择债权人代码；
							// 否则为空。
							// 如果债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE。
							if(!isNull(exdebt.getCreditortype())){
								if("20001401".equals(exdebt.getCreditortype())
										|| "20001402".equals(exdebt
												.getCreditortype())
										|| "20001403".equals(exdebt
												.getCreditortype())){
									if(isNull(exdebt.getCreditorcode())
											|| (exdebt.getCreditorcode()
													.length() != 8 && exdebt
													.getCreditorcode().length() != 11)){
										map
												.put("CREDITORCODE",
														"[债权人代码] 债权人为银行，应填写8位或11位的SWIFT CODE ");
									}
								}else if("20001100".equals(exdebt
										.getCreditortype())
										|| "20001300".equals(exdebt
												.getCreditortype())
										|| "1201".equals(exdebt.getDebtype())
										|| "1202".equals(exdebt.getDebtype())){
									if(isNull(exdebt.getCreditorcode())){
										map
												.put(
														"CREDITORCODE",
														"[债权人代码] 债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”时，应在外汇局编制的部分债权人代码表中选择债权人代码 ");
									}else{
										String errorMsg = this
												.verifyCreditorCode(
														exdebt.getDebtype(),
														exdebt
																.getCreditortype(),
														exdebt
																.getCreditorcode());
										if(StringUtil.isNotEmpty(errorMsg)){
											map.put("CREDITORCODE", "[债权人代码] "
													+ errorMsg);
										}
									}
								}else if("20001501".equals(exdebt
										.getCreditortype())
										|| "20001502".equals(exdebt
												.getCreditortype())
										|| "20001200".equals(exdebt
												.getCreditortype())){
									if(isNull(exdebt.getCreditorcode())){
										map
												.put("CREDITORCODE",
														"[债权人代码] 债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE ");
									}
								}else{
									if(!isNull(exdebt.getCreditorcode())){
										map.put("CREDITORCODE", "[债权人代码] 应为空 ");
									}
								}
							}
						}else{
							// 如果债权人为银行，应填写8位或11位的SWIFT CODE；
							// 如果债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”或“非居民个人存款”时，应在外汇局编制的部分债权人代码表中选择债权人代码；
							// 否则为空。
							// 如果债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE。
							if(!isNull(exdebt.getCreditortype())){
								if("20001401".equals(exdebt.getCreditortype())
										|| "20001402".equals(exdebt
												.getCreditortype())
										|| "20001403".equals(exdebt
												.getCreditortype())){
									// 如果债权人为银行，应填写8位或11位的SWIFT CODE；
									if(isNull(exdebt.getCreditorcode())
											|| (exdebt.getCreditorcode()
													.length() != 8 && exdebt
													.getCreditorcode().length() != 11)){
										map
												.put("CREDITORCODE",
														"[债权人代码] 债权人为银行，应填写8位或11位的SWIFT CODE ");
									}
								}else if("20001100".equals(exdebt
										.getCreditortype())
										|| "20001300".equals(exdebt
												.getCreditortype())
										|| "1201".equals(exdebt.getDebtype())
										|| "1202".equals(exdebt.getDebtype())
										|| "1304".equals(exdebt.getDebtype())){
									// 如果债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”或“非居民个人存款”时
									if(isNull(exdebt.getCreditorcode())){
										map
												.put(
														"CREDITORCODE",
														"[债权人代码] 债权人类型为“政府”、或“中央银行”时，或者当债务类型为“货币市场工具”或“债券和票据”或“非居民个人存款”时，应在外汇局编制的部分债权人代码表中选择债权人代码 ");
									}else{
										String errorMsg = this
												.verifyCreditorCode(
														exdebt.getDebtype(),
														exdebt
																.getCreditortype(),
														exdebt
																.getCreditorcode());
										if(StringUtil.isNotEmpty(errorMsg)){
											map.put("CREDITORCODE", "[债权人代码] "
													+ errorMsg);
										}
									}
								}else if("20001501".equals(exdebt
										.getCreditortype())
										|| "20001502".equals(exdebt
												.getCreditortype())
										|| "20001200".equals(exdebt
												.getCreditortype())){
									// 如果债权人为“非银行金融机构”或“国际金融组织”
									if(isNull(exdebt.getCreditorcode())){
										map
												.put("CREDITORCODE",
														"[债权人代码] 债权人为“非银行金融机构”或“国际金融组织”，应优先填写SWIFT CODE ");
									}
								}else{
									if(!isNull(exdebt.getCreditorcode())){
										map.put("CREDITORCODE", "[债权人代码] 应为空 ");
									}
								}
							}
						}
						// 债权人中文名称
						if(isNull(exdebt.getCreditorname())
								&& isNull(exdebt.getCreditornamen())){
							map.put("CREDITORNAME",
									"[债权人中文名称] 债权人中文名称和债权人英文名称至少填写一个 ");
						}
						// 债权人类型代码
						if(isNull(exdebt.getCreditortype())){
							map.put("CREDITORTYPE", "[债权人类型代码] 不能为空 ");
						}else if(!verifyDictionaryValue(MAINBODYTYPE, exdebt
								.getCreditortype())){
							String value = getKey(exdebt.getCreditortype(),
									MAINBODYTYPE);
							map.put("CREDITORTYPE", "[债权人类型代码] [" + value
									+ "] 无效 ");
						}
						// 国家（地区）代码
						if(isNull(exdebt.getCrehqcode())){
							map.put("CREHQCODE", "[国家（地区）代码] 不能为空 ");
						}else if(!verifyDictionaryValue(COUNTRY, exdebt
								.getCrehqcode())){
							String value = getKey(exdebt.getCrehqcode(),
									COUNTRY);
							map.put("CREHQCODE", "[国家（地区）代码] [" + value
									+ "] 无效 ");
						}
					}
					if(isNull(exdebt.getTradedate())){
						map.put("TRADEDATE", "[交易日期] 不能为空 ");
					}else if(!verifyTwoDates(exdebt.getTradedate(), DateUtils
							.serverCurrentDate(DateUtils.ORA_DATE_FORMAT))){
						map.put("TRADEDATE", "[交易日期] 不能晚于当前日期 ");
					}
					// 行内校验
					if(limitBranchCode(exdebt.getDebtorcode(), exdebt
							.getFiletype(), exdebt.getBusinessid(), exdebt
							.getInstcode())){
						map.put("DEBTORCODE", "[债务人代码] 与当前记录所属机构对应的申报号码不匹配 ");
					}else if(checkBranchCode(exdebt.getDebtorcode())){
						map.put("DEBTORCODE",
								"[债务人代码] 应为行内金融机构标识码 见机构对照管理处所配置申报号码 ");
					}
				}
				// 业务编号校验
				// 签约不能重复
				if(!"AR".equals(exdebt.getFiletype())
						&& !"AS".equals(exdebt.getFiletype())
						&& checkBusinessNoRepeat(exdebt.getBusinessno(),
								"T_CFA_A_EXDEBT", exdebt.getFiletype(), exdebt
										.getBusinessid())){
					map.put("BUSINESSNO", "[业务编号] 外债签约信息中存在重复业务编号 ");
				}
				// 不能为空
				else if(checkBusinessNoisNull(exdebt.getBusinessno())){
					map.put("BUSINESSNO", "[业务编号] 不能为空 ");
				}
			}
		}
		return verifyModel;
	}

	public void setVerifyConfig(VerifyConfig vc){
		// verifyConfig = vc;
	}

	public boolean verifyIsinCode(String isinCode){
		if(isinCode.length() != 12){
			return false;
		}else{
			// 第1-2位为字母，用于标识国别；第3-11位为数字和字母，为国内证券识别编码；第12位为数字，为校对码。
			String code1 = isinCode.substring(0, 2);
			String code2 = isinCode.substring(2, 11);
			String code3 = isinCode.substring(11, 12);
			if(!StringUtil.isLatter(code1)){
				return false;
			}
			if(!StringUtil.isNumberOrLatter(code2)){
				return false;
			}
			if(!StringUtil.isNumLegal(code3)){
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>方法名称: verifyCreditorCode|描述: 部分债权人代码校验规则</p>
	 * @param debtType 债务类型
	 * @param creditorType 债权人类型代码
	 * @param creditorCode 债权人代码
	 * @return String
	 */
	private String verifyCreditorCode(String debtType, String creditorType,
			String creditorCode){
		String errorMsg = null;
		if(configMap != null){
			String checkCreditorCode = (String) configMap
					.get("config.check.creditorCode");
			if(!"yes".equalsIgnoreCase(checkCreditorCode)){
				return null;
			}
		}
		if("1304".equals(debtType)){
			// Type:各国非居民个人 Code:IDV+国家地区代码
			if(creditorCode.length() != 6
					|| !creditorCode.startsWith("IDV")
					|| !verifyDictionaryValue(COUNTRY, creditorCode.substring(
							3, 6))){
				errorMsg = "债权人代码 格式应为IDV+国家地区代码";
			}
		}else{
			if("20001100".equals(creditorType)){
				// Type:各国政府 Code:GOV+国家地区代码
				if(creditorCode.length() != 6
						|| !creditorCode.startsWith("GOV")
						|| !verifyDictionaryValue(COUNTRY, creditorCode
								.substring(3, 6))){
					errorMsg = "债权人代码 格式应为GOV+国家地区代码";
				}
			}else if("20001300".equals(creditorType)){
				// Type:各国中央银行 Code:CEB+国家地区代码
				if(creditorCode.length() != 6
						|| !creditorCode.startsWith("CEB")
						|| !verifyDictionaryValue(COUNTRY, creditorCode
								.substring(3, 6))){
					errorMsg = "债权人代码 格式应为CEB+国家地区代码";
				}
			}else if("20001800".equals(creditorType)){
				// Type:各国资本市场 Code:CAP+国家地区代码
				if(creditorCode.length() != 6
						|| !creditorCode.startsWith("CAP")
						|| !verifyDictionaryValue(COUNTRY, creditorCode
								.substring(3, 6))){
					errorMsg = "债权人代码 格式应为CAP+国家地区代码";
				}
			}
		}
		return errorMsg;
	}
}
