package com.cjit.vms.trans.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.cjit.common.util.CollectionUtil;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.gjsz.common.homenote.model.PubHomeCell;
import com.cjit.gjsz.common.homenote.service.HomeDataService;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.util.DataUtil;

public class CheckAccountAction extends DataDealAction {

	private static final long serialVersionUID = 1L;
	private HomeDataService homeDataService;

	public CheckAccountAction() {
		init();
	}

	public synchronized boolean init() {
		// 初始化相关参数
		System.out
				.println("\n\n\n\n CheckAccountAction init -------------------------- \n\n\n\n");
		return true;
	}

	/**
	 * 执行对账操作
	 * 
	 * @return String
	 */
	public String execute() {
		System.out.println("\n\n\n\n CheckAccountAction execute -------------------------- \n\n\n\n");
		try {
			String date = DateUtils.toString(new Date(),
					DateUtils.ORA_DATE_FORMAT);
			StringBuffer sbMsg1 = new StringBuffer();
			StringBuffer sbMsg2 = new StringBuffer();
			// 查询交易临时表 统计当日导入交易记录 剔除冲账记录
			TransInfo trans = new TransInfo();
//			trans.setImportDate(date);
			trans.setSearchFlag(DataUtil.CHECK);
			List transTempList = this.transInfoService
					.findTransInfoTempList(trans);
			if (CollectionUtil.isEmpty(transTempList)) {
				return SUCCESS;
			}
			sbMsg1.append("导入交易记录").append(transTempList.size()).append("笔；");
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			trans.setLstAuthInstId(lstAuthInstId);
			// 查询交易信息表 统计当日交易处理状况
			List transList = this.transInfoService.findTransInfoList(trans);
			if (CollectionUtil.isEmpty(transList)) {
				sbMsg1.append("校验通过0笔");
			} else {
				// 计算导入交易总金额
				BigDecimal dbSumImpAmt = new BigDecimal(0.0);
				for (Iterator t = transTempList.iterator(); t.hasNext();) {
					trans = (TransInfo) t.next();
					BigDecimal dbAmt = trans.getAmt();
					if (dbAmt != null) {
						dbSumImpAmt = dbSumImpAmt.add(dbAmt);
					}
				}
				sbMsg2.append("导入交易总金额").append(
						NumberUtils.format(dbSumImpAmt, "", 2)).append("；");
			}
			int nCount1 = 0;
			int nCount2 = 0;
			int nCount3 = 0;
			int nCount99 = 0;
			BigDecimal bdAmt = new BigDecimal(0.0);// 完成开票总金额
			BigDecimal bdIncome = new BigDecimal(0.0);// 完成开票总收入
			BigDecimal bdTaxAmt = new BigDecimal(0.0);// 完成开票总税额
			BigDecimal bdShortOver = new BigDecimal(0.0);// 完成开票总尾差
			for (Iterator t = transList.iterator(); t.hasNext();) {
				trans = (TransInfo) t.next();
				if (trans != null) {
					if (DataUtil.TRANS_STATUS_1.equals(trans.getDataStatus())) {
						nCount1++;
					} else if (DataUtil.TRANS_STATUS_2.equals(trans
							.getDataStatus())) {
						nCount2++;
					} else if (DataUtil.TRANS_STATUS_3.equals(trans
							.getDataStatus())) {
						nCount3++;
					} else if (DataUtil.TRANS_STATUS_99.equals(trans
							.getDataStatus())) {
						nCount99++;
						if (trans.getAmt() != null) {
							bdAmt = bdAmt.add(trans.getAmt());
						}
						if (trans.getIncome() != null) {
							bdIncome = bdIncome.add(trans.getIncome());
						}
						if (trans.getTaxAmt() != null) {
							bdTaxAmt = bdTaxAmt.add(trans.getTaxAmt());
						}
						if (trans.getShortAndOver() != null) {
							bdShortOver = bdShortOver.add(trans
									.getShortAndOver());
						}
					}
				}
			}
			if (nCount1 > 0) {
				sbMsg1.append("未处理开票").append(nCount1).append("笔；");
			}
			if (nCount2 > 0) {
				sbMsg1.append("开票编辑锁定中").append(nCount2).append("笔；");
			}
			if (nCount3 > 0) {
				sbMsg1.append("开票中").append(nCount3).append("笔；");
			}
			if (nCount99 > 0) {
				sbMsg1.append("开票完成").append(nCount99).append("笔；");
				sbMsg2.append("完成开票总金额").append(
						NumberUtils.format(bdAmt, "", 2)).append("；");
				sbMsg2.append("完成开票总收入").append(
						NumberUtils.format(bdIncome, "", 2)).append("；");
				sbMsg2.append("完成开票总税额").append(
						NumberUtils.format(bdTaxAmt, "", 2)).append("；");
				sbMsg2.append("完成开票总尾差").append(
						NumberUtils.format(bdShortOver, "", 2)).append("；");
			}
			// 将统计结果计入
			// 记录交易笔数统计对账信息
			PubHomeCell pubHomeCell = new PubHomeCell();
			pubHomeCell.setCellType("detail");
			pubHomeCell.setCellTitle("交易对账");
			pubHomeCell.setCellDateStr(date);
			pubHomeCell.setDataTime(DateUtils.toString(new Date(),
					DateUtils.TIMESTAMP_FORMAT));
			pubHomeCell.setCellDesc(sbMsg1.toString());
			pubHomeCell.setCellUserId("system");
			this.homeDataService.insertHomeCell(pubHomeCell);
			// 记录交易金额统计对账信息
			pubHomeCell = new PubHomeCell();
			pubHomeCell.setCellType("detail");
			pubHomeCell.setCellTitle("交易对账");
			pubHomeCell.setCellDateStr(date);
			pubHomeCell.setDataTime(DateUtils.toString(new Date(),
					DateUtils.TIMESTAMP_FORMAT));
			pubHomeCell.setCellDesc(sbMsg2.toString());
			pubHomeCell.setCellUserId("system");
			this.homeDataService.insertHomeCell(pubHomeCell);
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(),
					"0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("CheckAccountAction-execute", e);
		}
		System.out
				.println("\n\n\n\n CheckAccountAction execute -------------------------- \n\n\n\n");
		return SUCCESS;
	}

	public HomeDataService getHomeDataService() {
		return homeDataService;
	}

	public void setHomeDataService(HomeDataService homeDataService) {
		this.homeDataService = homeDataService;
	}
}
