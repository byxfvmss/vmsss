package com.cjit.vms.taxdisk.single.model.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.taxdisk.single.model.BaseDiskModel;

public class ReportTaxDiskQueryReturnXml extends BaseDiskModel {

	/**
	 * 报税盘编号 是否必须：
	 */
	private String reportTaxDisk;
	/**
	 * 纳税人识别号 是否必须：
	 */
	private String taxNo;
	/**
	 * 纳税人名称 是否必须：
	 */
	private String name;
	/**
	 * 税务机关代码 是否必须：
	 */
	private String taxOrganCode;
	/**
	 * 税务机关名称 是否必须：
	 */
	private String taxOrganName;
	/**
	 * 当前时钟 是否必须：
	 */
	private String curTime;
	/**
	 * 启用时间 是否必须：
	 */
	private String enableTime;
	/**
	 * 分发标志 是否必须：
	 */
	private String distributeFlag;
	/**
	 * 版本号 是否必须：
	 */
	private String visNo;
	/**
	 * 开票机号 是否必须：
	 */
	private String issueNo;
	/**
	 * 保留信息 是否必须：
	 */
	private String keepInfo;
	/**
	 * 企业类型 是否必须：
	 */
	private String companyType;
	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

	public String getReportTaxDisk() {
		return reportTaxDisk;
	}

	public void setReportTaxDisk(String reportTaxDisk) {
		this.reportTaxDisk = reportTaxDisk;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaxOrganCode() {
		return taxOrganCode;
	}

	public void setTaxOrganCode(String taxOrganCode) {
		this.taxOrganCode = taxOrganCode;
	}

	public String getTaxOrganName() {
		return taxOrganName;
	}

	public void setTaxOrganName(String taxOrganName) {
		this.taxOrganName = taxOrganName;
	}

	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	public String getEnableTime() {
		return enableTime;
	}

	public void setEnableTime(String enableTime) {
		this.enableTime = enableTime;
	}

	public String getDistributeFlag() {
		return distributeFlag;
	}

	public void setDistributeFlag(String distributeFlag) {
		this.distributeFlag = distributeFlag;
	}

	public String getVisNo() {
		return visNo;
	}

	public void setVisNo(String visNo) {
		this.visNo = visNo;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getKeepInfo() {
		return keepInfo;
	}

	public void setKeepInfo(String keepInfo) {
		this.keepInfo = keepInfo;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
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

	public ReportTaxDiskQueryReturnXml(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);

		this.reportTaxDisk = output.getChildText(report_tax_disk_ch);
		this.taxNo = output.getChildText(tax_no_ch);
		this.name = output.getChildText(name_ch);
		this.taxOrganCode = output.getChildText(tax_organ_code_ch);
		this.taxOrganName = output.getChildText(tax_organ_name_ch);
		this.curTime = output.getChildText(cur_time_ch);
		this.enableTime = output.getChildText(enable_time_ch);
		this.distributeFlag = output.getChildText(distribute_flag_ch);
		this.visNo = output.getChildText(vis_no_ch);
		this.issueNo = output.getChildText(issue_no_ch);
		this.keepInfo = output.getChildText(keep_info_ch);
		this.companyType = output.getChildText(company_type_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}

	/**
	 * 报税盘编号 是否必须：
	 */
	private static final String report_tax_disk_ch = "bspbh";
	/**
	 * 纳税人识别号 是否必须：
	 */
	private static final String tax_no_ch = "nsrsbh";
	/**
	 * 纳税人名称 是否必须：
	 */
	private static final String name_ch = "nsrmc";
	/**
	 * 税务机关代码 是否必须：
	 */
	private static final String tax_organ_code_ch = "swjgdm";
	/**
	 * 税务机关名称 是否必须：
	 */
	private static final String tax_organ_name_ch = "swjgmc";
	/**
	 * 当前时钟 是否必须：
	 */
	private static final String cur_time_ch = "dqsz";

	/**
	 * 启用时间 是否必须：
	 */
	private static final String enable_time_ch = "qysj";
	/**
	 * 分发标志 是否必须：
	 */
	private static final String distribute_flag_ch = "ffbz";
	/**
	 * 版本号 是否必须：
	 */
	private static final String vis_no_ch = "bbh";
	/**
	 * 开票机号 是否必须：
	 */
	private static final String issue_no_ch = "kpjh";
	/**
	 * 保留信息 是否必须：
	 */
	private static final String keep_info_ch = "blxx";
	/**
	 * 企业类型 是否必须：
	 */
	private static final String company_type_ch = "qylx";
	/**
	 * 返回代码 是否必须：是 0成功，其它失败
	 */
	private static final String return_code_ch = "returncode";
	/**
	 * 返回信息 是否必须：是
	 */
	private static final String return_msg_ch = "returnmsg";
}
