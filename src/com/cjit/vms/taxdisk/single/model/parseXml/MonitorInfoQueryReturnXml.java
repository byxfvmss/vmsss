package com.cjit.vms.taxdisk.single.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class MonitorInfoQueryReturnXml extends BaseDiskModel {
	
	/**
	 * 发票类型代码 是否必须：是
	 */
	private String fapiaoType;
	/**
	 * 开票截止时间 是否必须：是 YYYYMMDD
	 */
	private String issueCutOffTime;
	/**
	 * 数据报送起始日期 是否必须：是 YYYYMMDD
	 */
	private String dataReportBeginData;
	/**
	 * 数据报送终止日期 是否必须：是 YYYYMMDD
	 */
	private String dataReportEndData;
	/**
	 * 单张发票开票金额限额 是否必须：是 单位元，两位小数
	 */
	private String singleBillLimitAmt;
	/**
	 * 正数发票累计金额限 是否必须：是 单位元，两位小数
	 */
	private String posiBillTotalLimitAmt;
	/**
	 * 负数发票累计金额限 是否必须：是 单位元，两位小数
	 */
	private String negBillTotalLimitAmt;
	/**
	 * 负数发票标志 是否必须：是 开负数发票原票必须在盘内标志（0：不需要；1：需要）
	 */
	private String negBillFlag;
	/**
	 * 开具负数发票限定天数 是否必须：是 整数
	 */
	private String issueNegBillLimitDayNum;
	/**
	 * 最新报税日期 是否必须：是 YYYYMMDD
	 */
	private String newestReportDate;
	/**
	 * 剩余容量 是否必须：是 以字节为单位
	 */
	private String surCap;
	/**
	 * 上传截止日期 是否必须：是 0～31
	 */
	private String upCutOffDate;
	/**
	 * 离线限定功能标识 是否必须：是 二进制状态位
	 */
	private String offLineLimitFlag;
	/**
	 * 离线开票限定时长 是否必须：是 单位：小时
	 */
	private String offLineBillLimitHours;
	/**
	 * 离线开票限定张数 是否必须：是 整数
	 */
	private String offLineBillLimitPieceNum;
	/**
	 * 离线开票限定正数累计金额 是否必须：是 两位小数
	 */
	private String offLineBillLimitPosTotalAmt;
	/**
	 * 离线开票限定负数累计金额 是否必须：是 两位小数
	 */
	private String offLineBillLimitNegTotalAmt;
	/**
	 * 离线开票扩展信息 是否必须：是
	 */
	private String offLineBillExtendInfo;
	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}

	public String getIssueCutOffTime() {
		return issueCutOffTime;
	}

	public void setIssueCutOffTime(String issueCutOffTime) {
		this.issueCutOffTime = issueCutOffTime;
	}

	public String getDataReportBeginData() {
		return dataReportBeginData;
	}

	public void setDataReportBeginData(String dataReportBeginData) {
		this.dataReportBeginData = dataReportBeginData;
	}

	public String getDataReportEndData() {
		return dataReportEndData;
	}

	public void setDataReportEndData(String dataReportEndData) {
		this.dataReportEndData = dataReportEndData;
	}

	public String getSingleBillLimitAmt() {
		return singleBillLimitAmt;
	}

	public void setSingleBillLimitAmt(String singleBillLimitAmt) {
		this.singleBillLimitAmt = singleBillLimitAmt;
	}

	public String getPosiBillTotalLimitAmt() {
		return posiBillTotalLimitAmt;
	}

	public void setPosiBillTotalLimitAmt(String posiBillTotalLimitAmt) {
		this.posiBillTotalLimitAmt = posiBillTotalLimitAmt;
	}

	public String getNegBillTotalLimitAmt() {
		return negBillTotalLimitAmt;
	}

	public void setNegBillTotalLimitAmt(String negBillTotalLimitAmt) {
		this.negBillTotalLimitAmt = negBillTotalLimitAmt;
	}

	public String getNegBillFlag() {
		return negBillFlag;
	}

	public void setNegBillFlag(String negBillFlag) {
		this.negBillFlag = negBillFlag;
	}

	public String getIssueNegBillLimitDayNum() {
		return issueNegBillLimitDayNum;
	}

	public void setIssueNegBillLimitDayNum(String issueNegBillLimitDayNum) {
		this.issueNegBillLimitDayNum = issueNegBillLimitDayNum;
	}

	public String getNewestReportDate() {
		return newestReportDate;
	}

	public void setNewestReportDate(String newestReportDate) {
		this.newestReportDate = newestReportDate;
	}

	public String getSurCap() {
		return surCap;
	}

	public void setSurCap(String surCap) {
		this.surCap = surCap;
	}

	public String getUpCutOffDate() {
		return upCutOffDate;
	}

	public void setUpCutOffDate(String upCutOffDate) {
		this.upCutOffDate = upCutOffDate;
	}

	public String getOffLineLimitFlag() {
		return offLineLimitFlag;
	}

	public void setOffLineLimitFlag(String offLineLimitFlag) {
		this.offLineLimitFlag = offLineLimitFlag;
	}

	public String getOffLineBillLimitHours() {
		return offLineBillLimitHours;
	}

	public void setOffLineBillLimitHours(String offLineBillLimitHours) {
		this.offLineBillLimitHours = offLineBillLimitHours;
	}

	public String getOffLineBillLimitPieceNum() {
		return offLineBillLimitPieceNum;
	}

	public void setOffLineBillLimitPieceNum(String offLineBillLimitPieceNum) {
		this.offLineBillLimitPieceNum = offLineBillLimitPieceNum;
	}

	public String getOffLineBillLimitPosTotalAmt() {
		return offLineBillLimitPosTotalAmt;
	}

	public void setOffLineBillLimitPosTotalAmt(
			String offLineBillLimitPosTotalAmt) {
		this.offLineBillLimitPosTotalAmt = offLineBillLimitPosTotalAmt;
	}

	public String getOffLineBillLimitNegTotalAmt() {
		return offLineBillLimitNegTotalAmt;
	}

	public void setOffLineBillLimitNegTotalAmt(
			String offLineBillLimitNegTotalAmt) {
		this.offLineBillLimitNegTotalAmt = offLineBillLimitNegTotalAmt;
	}

	public String getOffLineBillExtendInfo() {
		return offLineBillExtendInfo;
	}

	public void setOffLineBillExtendInfo(String offLineBillExtendInfo) {
		this.offLineBillExtendInfo = offLineBillExtendInfo;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public MonitorInfoQueryReturnXml(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);

		this.fapiaoType = output.getChildText(fapiao_type_ch);
		this.issueCutOffTime = output.getChildText(issue_cut_off_time_ch);
		this.dataReportBeginData = output
				.getChildText(data_report_begin_data_ch);
		this.dataReportEndData = output.getChildText(data_report_end_data_ch);
		this.singleBillLimitAmt = output.getChildText(single_bill_limit_amt_ch);
		this.posiBillTotalLimitAmt = output
				.getChildText(posi_bill_total_limit_amt_ch);
		this.negBillTotalLimitAmt = output
				.getChildText(neg_bill_total_limit_amt_ch);
		this.negBillFlag = output.getChildText(neg_bill_flag_ch);
		this.issueNegBillLimitDayNum = output
				.getChildText(issue_neg_bill_limit_day_num_ch);
		this.newestReportDate = output.getChildText(newest_report_date_ch);
		this.surCap = output.getChildText(sur_cap_ch);
		this.upCutOffDate = output.getChildText(up_cut_off_date_ch);
		this.offLineLimitFlag = output.getChildText(off_line_limit_flag_ch);
		this.offLineBillLimitHours = output
				.getChildText(off_line_bill_limit_hours);
		this.offLineBillLimitPieceNum = output
				.getChildText(off_line_bill_limit_piece_num_ch);
		this.offLineBillLimitPosTotalAmt = output
				.getChildText(off_line_bill_limit_pos_total_amt_ch);
		this.offLineBillLimitNegTotalAmt = output
				.getChildText(off_line_bill_limit_neg_total_amt_ch);
		this.offLineBillExtendInfo = output
				.getChildText(off_line_bill_extend_info_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}
	
	/**
	 * 发票类型代码
	 */
	private static final String fapiao_type_ch = "fplxdm";
	/**
	*开票截止时间  是否必须：是
	YYYYMMDD
	*/
	private static final String issue_cut_off_time_ch="kpjzsj";
	/**
	*数据报送起始日期  是否必须：是
	YYYYMMDD
	*/
	private static final String data_report_begin_data_ch="bsqsrq";
	/**
	 * 
	 * 
	*数据报送终止日期  是否必须：是
	YYYYMMDD
	*/
	private static final String data_report_end_data_ch="bszzrq";
	/**
	*单张发票开票金额限额  是否必须：是
	单位元，两位小数
	*/
	private static final String single_bill_limit_amt_ch="dzkpxe";
	/**
	*正数发票累计金额限  是否必须：是
	单位元，两位小数
	*/
	private static final String posi_bill_total_limit_amt_ch="zsljxe";
	/**
	*负数发票累计金额限  是否必须：是
	单位元，两位小数
	*/
	private static final String neg_bill_total_limit_amt_ch="fsljxe";
	/**
	*负数发票标志  是否必须：是
	开负数发票原票必须在盘内标志（0：不需要；1：需要）
	*/
	private static final String neg_bill_flag_ch="fsfpbz";
	/**
	*开具负数发票限定天数  是否必须：是
	整数
	*/
	private static final String issue_neg_bill_limit_day_num_ch="fsfpts";
	/**
	*最新报税日期  是否必须：是
	YYYYMMDD
	*/
	private static final String newest_report_date_ch="zxbsrq";
	/**
	*剩余容量  是否必须：是
	以字节为单位
	*/
	private static final String sur_cap_ch="syrl";
	/**
	*上传截止日期  是否必须：是
	0～31
	*/
	private static final String up_cut_off_date_ch="scjzrq";
	/**
	*离线限定功能标识  是否必须：是
	二进制状态位
	*/
	private static final String off_line_limit_flag_ch="xdgnbs";
	/**
	*离线开票限定时长  是否必须：是
	单位：小时
	*/
	private static final String off_line_bill_limit_hours="lxkpsc";
	/**
	*离线开票限定张数  是否必须：是
	整数
	*/
	private static final String off_line_bill_limit_piece_num_ch="lxkpzs";
	/**
	*离线开票限定正数累计金额  是否必须：是
	两位小数
	*/
	private static final String off_line_bill_limit_pos_total_amt_ch="lxzsljje";
	/**
	*离线开票限定负数累计金额  是否必须：是
	两位小数
	*/
	private static final String off_line_bill_limit_neg_total_amt_ch="lxfsljje";
	/**
	*离线开票扩展信息  是否必须：是

	*/
	private static final String off_line_bill_extend_info_ch="lxkzxx";
	/**
	*返回代码  是否必须：是
	00000000成功，其它失败
	*/
	private static final String return_code_ch="returncode";
	/**
	*返回信息  是否必须：是

	*/
	private static final String return_msg_ch="returnmsg";
}
