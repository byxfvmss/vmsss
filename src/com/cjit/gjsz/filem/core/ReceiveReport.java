package com.cjit.gjsz.filem.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.util.DataUtil;

public class ReceiveReport {

	private int id;
	private String errorType = "";
	private String dataNumber = "";
	private String dataType = "";
	private String tableId = "";
	private String tableName = "";
	private String infoType = "";
	private String receiveDate = "";
	private String errorMemo = "";
	private String hasReject = "";
	private Integer totalFiles = new Integer(0);
	private String rptSendFileName = "";
	// 数据错误信息
	private String dataErrorInfo = "";
	// DFHL 反馈报文机构过滤 start
	private String userId;
	private String instCode;
	private String packName;
	private String packType;
	// 
	private String[] dataNumbers = null;
	// 
	private String dataStatus;
	//
	private String notInfoType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// DFHL 反馈报文机构过滤 end
	public static final List INFO_TYPES = new ArrayList();
	public static final List HASREJECT_FLAGS = new ArrayList();
	public static final String DATA_TYPE_REPNO = "申报号";
	public static final String DATA_TYPE_ORGNO = "机构号";
	static {
		INFO_TYPES.add("控制反馈信息");
		// INFO_TYPES.add("外债");
		// INFO_TYPES.add("对外担保");
		// INFO_TYPES.add("国内外汇贷款");
		// INFO_TYPES.add("境外担保项下境内贷款");
		// INFO_TYPES.add("外汇质押人民币贷款");
		// INFO_TYPES.add("商业银行人民币结构性存款");

		INFO_TYPES.add("Z.填报单位基本信息");
		INFO_TYPES.add("A.直接投资（10%及以上股权）");
		INFO_TYPES.add("B.证券投资（10%以下股权和债务证券）");
		INFO_TYPES.add("C.金融衍生产品及雇员认股权");
		INFO_TYPES.add("D.存贷款、应收应付款及非公司制机构股权等其他投资");
		INFO_TYPES.add("E.货物、服务、薪资及债务减免等其他各类往来");
		INFO_TYPES.add("F.与进出口票据、单证有关业务");
		INFO_TYPES.add("G.涉外银行卡相关统计");
		INFO_TYPES.add("H.涉外托管业务");
		INFO_TYPES.add("I.涉外保险业务");
		INFO_TYPES.add("X.补充报表：银行进出口贸易融资余额");

		Map mHasReject0 = new HashMap();
		mHasReject0.put("value", "0");
		mHasReject0.put("name", "未打回");
		Map mHasReject1 = new HashMap();
		mHasReject1.put("value", "1");
		mHasReject1.put("name", "已打回");
		Map mHasReject2 = new HashMap();
		mHasReject2.put("value", "");
		mHasReject2.put("name", "全部");
		HASREJECT_FLAGS.add(mHasReject0);
		HASREJECT_FLAGS.add(mHasReject1);
		HASREJECT_FLAGS.add(mHasReject2);
	}

	public void refreshInfoTypes(String interfaceVer) {
		if (StringUtil.isNotEmpty(interfaceVer)) {
			if (INFO_TYPES.contains("核销信息")) {
				INFO_TYPES.remove("核销信息");
				INFO_TYPES.remove("单位基本信息");
				INFO_TYPES.remove("控制反馈信息");
				INFO_TYPES.add("管理信息");
				INFO_TYPES.add("单位基本信息");
				INFO_TYPES.add("控制反馈信息");
			}
		} else {
			if (INFO_TYPES.contains("管理信息")) {
				INFO_TYPES.remove("管理信息");
				INFO_TYPES.remove("单位基本信息");
				INFO_TYPES.remove("控制反馈信息");
				INFO_TYPES.add("核销信息");
				INFO_TYPES.add("单位基本信息");
				INFO_TYPES.add("控制反馈信息");
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getDataNumber() {
		return dataNumber;
	}

	public void setDataNumber(String dataNumber) {
		this.dataNumber = dataNumber;
	}

	public String getTableId() {
		if (StringUtil.isEmpty(tableId) && StringUtil.isNotEmpty(infoType)) {
			if ("外债".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_A_EXDEBT";
			} else if ("对外担保".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_B_EXGUARAN";
			} else if ("国内外汇贷款".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_C_DOFOEXLO";
			} else if ("境外担保项下境内贷款".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_D_LOUNEXGU";
			} else if ("外汇质押人民币贷款".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_E_EXPLRMBLO";
			} else if ("商业银行人民币结构性存款".equalsIgnoreCase(infoType.trim())) {
				tableId = "T_CFA_F_STRDE";
			}
		}
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getErrorMemo() {
		return errorMemo;
	}

	public void setErrorMemo(String errorMemo) {
		this.errorMemo = errorMemo;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getHasReject() {
		return hasReject;
	}

	public void setHasReject(String hasReject) {
		this.hasReject = hasReject;
	}

	public Integer getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(Integer totalFiles) {
		this.totalFiles = totalFiles;
	}

	public String getRptSendFileName() {
		return rptSendFileName;
	}

	public void setRptSendFileName(String rptSendFileName) {
		this.rptSendFileName = rptSendFileName;
	}

	public String getDataErrorInfo() {
		return dataErrorInfo;
	}

	public void setDataErrorInfo(String dataErrorInfo) {
		this.dataErrorInfo = dataErrorInfo;
	}

	public String[] getDataNumbers() {
		return dataNumbers;
	}

	public void setDataNumbers(String[] dataNumbers) {
		this.dataNumbers = dataNumbers;
	}

	public String getInstCode() {
		return instCode;
	}

	public void setInstCode(String instCode) {
		this.instCode = instCode;
	}

	public String getNotInfoType() {
		return notInfoType;
	}

	public void setNotInfoType(String notInfoType) {
		this.notInfoType = notInfoType;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getStatFileDesc() {
		if (StringUtil.isNotEmpty(packType)
				&& DataUtil.PACKTYPE_ERRORFILES.equals(packType)) {
			return "<font color=\"orange\" title='所有记录恢复为审核通过'>收到ErrorFiles反馈</font>";
		}
		if (totalFiles != null) {
			if (totalFiles.intValue() > 0) {
				return "<font color=\"red\">收到错误反馈</font>";
			} else {
				return "<font color=\"green\">收到正确反馈</font>";
			}
		}
		return "<font color=\"blue\">未收到反馈</font>";
	}
}
