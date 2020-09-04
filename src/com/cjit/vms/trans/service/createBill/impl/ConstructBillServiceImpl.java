package com.cjit.vms.trans.service.createBill.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.config.VerificationInfo;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.GoodsCollectionInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.TransInfoService;
import com.cjit.vms.trans.service.createBill.ConstructBillService;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

/***
 * 封装BillsTaxNoContext
 * 
 * @author yang
 *
 */
public class ConstructBillServiceImpl extends GenericServiceImpl implements
		ConstructBillService {

	// 用于生成ID的属性
	private int busFlag = 1;
	private String timeStamp = "";

	TransInfoService transInfoService;

	public BillContext addTransToContext(BillsTaxNoContext billsTaxNoContext,
			TransInfo transInfo) {

		// 取得票据分组基本信息
		BigDecimal balance = transInfo.getBalance();
//		BigDecimal billMaxAmt = getBillMaxAmt(transInfo.getTaxNo(),
//				transInfo.getFapiaoType());
		
		BigDecimal billMaxAmt = getBillMaxAmt(transInfo);

		// 如果交易未开票金额等于票据最大限额起新票， 否则要在现有票据查找适合的票据
		if (balance.compareTo(billMaxAmt) == 0) {
			// 生成新票据
			billsTaxNoContext.addBillContext(createBillContext(transInfo));

		} else {
			// 根据税号 客户号 发票类型取得 票据List
			List<BillContext> billContextList = billsTaxNoContext
					.getBillContextList(transInfo);

			// 没有相关票据 创建新的
			if (billContextList.size() == 0) {
				billsTaxNoContext.addBillContext(createBillContext(transInfo));
			} else {
				// 循环票据查找对应票据及商品
				BillContext billContextTar = null;
				BillGoodsInfo billGoodsInfoTar = null;
				for (int i = 0; i < billContextList.size(); i++) {
					BillContext billContextObj = billContextList.get(i);
					if (isMoreThanBillMaxAmt(billContextObj, transInfo)) {
						continue;
					}

					boolean isFullGoodsSize = isFullGoodsSize(billContextObj);
					BillGoodsInfo billGoodsInfo = findGoodsInfo(billContextObj,
							transInfo);

					// 如果没有找到商品且商品最大数量等于8 跳过当前票据
					if (null == billGoodsInfo && isFullGoodsSize) {
						continue;
					}
					billContextTar = billContextObj;
					billGoodsInfoTar = billGoodsInfo;
					break;

				}

				if (null == billContextTar) {
					billsTaxNoContext
							.addBillContext(createBillContext(transInfo));
				} else {
					if (null == billGoodsInfoTar) {
						billGoodsInfoTar = createBillGoodsInfo(transInfo);
						billContextTar.addGoodsInfo(billGoodsInfoTar);
						billContextTar.writeBackBillAmt(transInfo);
					} else {
						BillTransInfo billTransInfo = new BillTransInfo(
								transInfo);
						billGoodsInfoTar.addBillTransInfo(billTransInfo);
						billContextTar.writeBackBillAmt(transInfo);
					}
				}

			}

		}

		return null;

	}

	public BillGoodsInfo findGoodsInfo(BillContext billContext,
			TransInfo transInfo) {

		// 校验新交易进入当前票据后的最后结果 成功后加入现有billsTaxNoContext
		String goodsId = transInfo.getVerificationInfo().getGoodsId();
		String goodsRate = transInfo.getTaxRate().toString();

		String goodsKey = goodsId + goodsRate;
		List<BillGoodsInfo> goodsList = billContext
				.getSameGoodsInfoList(goodsKey);

		BillGoodsInfo goodsInfoResult = null;
		// 商品循环
		for (int j = 0; j < goodsList.size(); j++) {
			BillGoodsInfo goodsInfo = goodsList.get(j);
			boolean isMoreThanGoodsMaxReteDiff = isMoreThanGoodsMaxReteDiff(
					goodsInfo, transInfo);
			// 如果大于最大误差 跳过当前商品
			if (isMoreThanGoodsMaxReteDiff) {
				continue;
			}
			goodsInfoResult = goodsInfo;
			break;
		}
		return goodsInfoResult;
	}

	/***
	 * 创建新的票据上下文
	 */
	public BillContext addTransToBillContext(
			BillsTaxNoContext billsTaxNoContext, TransInfo transInfo) {

		return null;
	}

	@Override
	public BillsTaxNoContext constructBill(List transInfoList) {

		// 票据封装对象
		BillsTaxNoContext billsTaxNoContext = new BillsTaxNoContext();
		// 循环原始交易
		for (int i = 0; i < transInfoList.size(); i++) {
			// 取得原始交易
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			// 拆分超票据限额交易
			List<TransInfo> spilitTransList = spilitTrans(transInfo);
			// 循环拆分后的交易 生成票据
			for (int j = 0; j < spilitTransList.size(); j++) {
				TransInfo spilitTrans = (TransInfo) spilitTransList.get(j);

				addTransToContext(billsTaxNoContext, spilitTrans);
			}

		}

		return billsTaxNoContext;
	}

	/***
	 * 创建新的票据上下文
	 * 
	 */

	public BillContext createBillContext(TransInfo transInfo) {

		// String billStatus = DataUtil.BILL_STATUS_1;// 状态-待提交
		// String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		// String isSueType = DataUtil.ISSUE_TYPE_1; // 发票开具类型1-单笔

		String billId = createBillId("B");
		String billItemId = createBusinessId("BI");

		// BillContext billContext = new BillContext(billId, billItemId,
		// billStatus, isHandiwork, isSueType, transInfo);

		BillGoodsInfo billGoodsInfo = new BillGoodsInfo(billId, billItemId,
				transInfo);
		BillContext billContext = new BillContext(billId, billGoodsInfo,
				transInfo);
		return billContext;
	};

	public BillGoodsInfo createBillGoodsInfo(TransInfo transInfo) {
		String billItemId = createBusinessId("BI");
		BillGoodsInfo goodsInfo = new BillGoodsInfo(billItemId, transInfo);
		return goodsInfo;
	}

	protected String createBillId(String tabFlag) {
		String temp = DateUtils.serverCurrentDetailDate();
		String sequence = transInfoService.getBillIdSequence();
		return tabFlag + temp + sequence;
	}

	/**
	 * 生成业务主键（在插入数据时需要，由时间戳生成）
	 * 
	 * @return String
	 */
	protected String createBusinessId(String tabFlag) {
		if (tabFlag == null) {
			tabFlag = "";
		}
		String temp = DateUtils.serverCurrentTimeStamp();
		long random = Math.round(Math.random() * 110104 + 100000);
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return tabFlag + timeStamp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		} else {
			return tabFlag + temp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		}
	}

	/***
	 * 校验当前商品是否等于最大商品数量
	 * 
	 * @param billContext
	 * @param transInfo
	 * @return
	 */
	public boolean isFullGoodsSize(BillContext billContext) {

		// // 轧差过大须要占用一个商品
		// int needGoodsIndex = 0;
		//
		// // 超过最大误差要点一个商品
		// if (isMoreThanGoodsMaxReteDiff(billContext, transInfo)) {
		// needGoodsIndex++;
		// }
		//
		// // 得到票据商品数量（现有加上当前交易）
		// int billContextGoodsInfoSize = billContext.getGoodsInfoSize()
		// + needGoodsIndex;
		//
		// // 当前交易商品不存在且票据商品列表已满
		//
		// if ((null == goodsInfo) && billContextGoodsInfoSize > 8) {
		// return null;
		// } else {
		//
		// }
		int billContextGoodsInfoSize = billContext.getGoodsInfoSize();
		if (billContextGoodsInfoSize == 8) {
			return true;
		}
		return false;
	}

//	public BigDecimal getBillMaxAmt(String taxNo, String fapiaoType) {
//		return DataUtil.billMaxAmt;
//	}
	
	public BigDecimal getBillMaxAmt(TransInfo transInfo) {
		Map map = new HashMap();
		map.put("transInfo", transInfo);
		List list = (List)find("findMaxAmt", map);
		if (list != null && list.size() != 0){
			return (BigDecimal)list.get(0);
		} else {
			return null;
		}
	}

	public BigDecimal getBillMaxRateDiff() {
		return DataUtil.different;
	}

	public TransInfoService getTransInfoService() {
		return transInfoService;
	}

	/***
	 * 票据信息加上当前交易信息 是否大于票据最大金额
	 * 
	 * @param billContext
	 * @param transInfo
	 * @return
	 */
	public boolean isMoreThanBillMaxAmt(BillContext billContext,
			TransInfo transInfo) {
		// 开票金额
		BigDecimal tansAmt = transInfo.getBalance();
		// 票据金额
		BigDecimal billSumAmt = billContext.getSumAmt();
		// 当前票据加上当前交易开票金额
		BigDecimal transAndBillAmt = tansAmt.add(billSumAmt);

		// 新交易加上当前票据金额是否超限制最大金额
//		if (transAndBillAmt.compareTo(getBillMaxAmt(transInfo.getTaxNo(),
//				transInfo.getFapiaoType())) == 1) {
//			return true;
//		}
		if (transAndBillAmt.compareTo(getBillMaxAmt(transInfo)) == 1) {
	         return true;
        }
		return false;
	}

	public boolean isMoreThanGoodsMaxReteDiff(BillGoodsInfo goodsInfo,
			TransInfo transInfo) {

		 // 开票金额
		 BigDecimal tansAmt = transInfo.getBalance();
		 // 交易税率
		 BigDecimal transTaxRate = transInfo.getTaxRate();
		 // 交易开票税额
		 BigDecimal transTaxCny = transInfo.getTaxCnyBalance();
		 
		 
		 // 商品金额
		 BigDecimal goodsAmt = goodsInfo.getAmt();
		 // 商品金额
		 BigDecimal goodsTaxAmt = goodsInfo.getTaxAmt();
		// 当前票据加上当前交易开票金额
		 BigDecimal transAndGoodsAmt = tansAmt.add(goodsAmt);
				 
		 // 当前商品金额当前交易开票金额
		 BigDecimal transAndBillTaxCny = goodsTaxAmt.add(transTaxCny);
		 
		
		 BigDecimal billItemTaxAmt = DataUtil.calculateTaxAmt(transAndGoodsAmt,
		 transTaxRate, null);
		 // -------------------------- 判断轧差金额
		 BigDecimal diff =
		 (transAndBillTaxCny.subtract(billItemTaxAmt)).abs();
		
		 if (diff.compareTo(DataUtil.different) >= 0) {
		 return true;
		 }
		return false;
	}

	public void setTransInfoService(TransInfoService transInfoService) {
		this.transInfoService = transInfoService;
	}

	public List<TransInfo> spilitTrans(TransInfo transInfo) {
		// 拆分后List
		List<TransInfo> spilitTransList = new ArrayList<TransInfo>();
		// 取得交易未开票金额
		BigDecimal balance = transInfo.getBalance();
		BigDecimal taxCnyBalance = transInfo.getTaxCnyBalance();
		// 发票最大限额
//		BigDecimal billMaxAmt = getBillMaxAmt(transInfo.getTaxNo(),
//				transInfo.getFapiaoType());
		BigDecimal billMaxAmt = getBillMaxAmt(transInfo);
		// 交易未开票金超超出最大限额

		if (balance.compareTo(billMaxAmt) == 1) {
			// 拆分

			while (balance.compareTo(billMaxAmt) == 1) {
				try {
					TransInfo cloneTrans = new TransInfo();
					cloneTrans = (TransInfo) BeanUtils.cloneBean(transInfo);
					BigDecimal taxRate = cloneTrans.getTaxRate();
					BigDecimal OneAddRate = taxRate.add(BigDecimal.ONE);
//					BigDecimal incomeTemp = balance.divide(OneAddRate,10,BigDecimal.ROUND_HALF_UP);
					BigDecimal incomeTemp = billMaxAmt.divide(OneAddRate,10,BigDecimal.ROUND_HALF_UP);
					BigDecimal taxCnyBalanceTemp = incomeTemp.multiply(taxRate);
					BigDecimal cloneTaxCnyBalance = taxCnyBalanceTemp.setScale(2,BigDecimal.ROUND_HALF_UP);
					cloneTrans.setBalance(billMaxAmt);
					cloneTrans.setTaxCnyBalance(cloneTaxCnyBalance);

					balance = balance.subtract(billMaxAmt);
					taxCnyBalance = taxCnyBalance.subtract(cloneTaxCnyBalance);
					spilitTransList.add(cloneTrans);
				} catch (Exception e) {
					//拆分异常  跳过当前交易
					e.printStackTrace();
					return new ArrayList<TransInfo>();
				}
			}
			transInfo.setBalance(balance);
			transInfo.setTaxCnyBalance(taxCnyBalance);
			spilitTransList.add(transInfo);
		} else {
			// 不拆分
			spilitTransList.add(transInfo);
		}

		return spilitTransList;

	}

}
