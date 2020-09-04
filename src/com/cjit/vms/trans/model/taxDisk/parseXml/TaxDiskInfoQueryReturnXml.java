package com.cjit.vms.trans.model.taxDisk.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;

public class TaxDiskInfoQueryReturnXml extends BaseDiskModel {

	/**
	 * 税控盘编号 是否必须：是
	 */
	private String taxDiskNo;
	/**
	 * 税控盘口令 是否必须：是
	 */
	private String taxDiskPwd;
	/**
	 * 纳税人识别号 是否必须：是
	 */
	private String taxNo;
	/**
	 * 纳税人名称 是否必须：是
	 */
	private String name;
	/**
	 * 税务机关代码 是否必须：是 9位或者11位
	 */
	private String taxOrganCode;
	/**
	 * 税务机关名称 是否必须：是
	 */
	private String taxOrganName;
	/**
	 * 发票类型代码 是否必须：是 存在的所有发票类型代码（三位一组，无分割）
	 */
	private String fapiaoType;
	/**
	 * 当前时钟 是否必须：是 YYYYMMDDHHMMSS
	 */
	private String curTime;
	/**
	 * 启用时间 是否必须：是 YYYYMMDDHHMMSS
	 */
	private String enableTime;
	/**
	 * 版本号 是否必须：是
	 */
	private String visNo;
	/**
	 * 开票机号 是否必须：是 最大65535
	 */
	private String issueNo;
	/**
	 * 企业类型，即特殊企业标识 是否必须：是 “00”不是特殊企业 “01”是特殊企业
	 */
	private String companyType;
	/**
	 * 保留信息 是否必须：是 农产品销售收购类型+铁路企业类型+纳税人性质
	 */
	private String keepInfo;
	/**
	 * 其它扩展信息 是否必须：是
	 */
	private String otherExtendInfo;
	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private String returnCode;
	/**
	 * 返回信息 是否必须：是
	 */
	private String returnMsg;

	public String getTaxDiskNo() {
		return taxDiskNo;
	}

	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}

	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}

	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
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

	public String getFapiaoType() {
		return fapiaoType;
	}

	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
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

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getKeepInfo() {
		return keepInfo;
	}

	public void setKeepInfo(String keepInfo) {
		this.keepInfo = keepInfo;
	}

	public String getOtherExtendInfo() {
		return otherExtendInfo;
	}

	public void setOtherExtendInfo(String otherExtendInfo) {
		this.otherExtendInfo = otherExtendInfo;
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

	public TaxDiskInfoQueryReturnXml(String StringXml) throws Exception {
		Document doc = StringToDocument(StringXml);
		Element body = getBodyElement(doc);
		Element output = body.getChild(out_put_ch);

		this.taxDiskNo = output.getChildText(tax_disk_no_ch);
		this.taxDiskPwd = output.getChildText(tax_disk_pwd_ch);
		this.taxNo = output.getChildText(tax_no_ch);
		this.name = output.getChildText(name_ch);
		this.taxOrganCode = output.getChildText(tax_organ_code_ch);
		this.taxOrganName = output.getChildText(tax_organ_name_ch);
		this.fapiaoType = output.getChildText(fapiao_type_ch);
		this.curTime = output.getChildText(cur_time_ch);
		this.enableTime = output.getChildText(enable_time_ch);
		this.visNo = output.getChildText(vis_no_ch);
		this.issueNo = output.getChildText(issue_no_ch);
		this.companyType = output.getChildText(company_type_ch);
		this.keepInfo = output.getChildText(keep_info_ch);
		this.otherExtendInfo = output.getChildText(other_extend_info_ch);
		this.returnCode = output.getChildText(return_code_ch);
		this.returnMsg = output.getChildText(return_msg_ch);
	}

	/**
	 * 税控盘口令&nbsp;&nbsp; n
	 */
	private static final String tax_disk_pwd_ch = "skpkl";

	/**
	 * 税控盘编号 是否必须：是
	 */
	private static final String tax_disk_no_ch = "skpbh";

	/**
	 * 纳税人识别号 是否必须：是
	 */
	private static final String tax_no_ch = "nsrsbh";
	/**
	 * 纳税人名称 是否必须：是
	 */
	private static final String name_ch = "nsrmc";
	/**
	 * 税务机关代码 是否必须：是 9位或者11位
	 */
	private static final String tax_organ_code_ch = "swjgdm";
	/**
	 * 税务机关名称 是否必须：是
	 */
	private static final String tax_organ_name_ch = "swjgmc";
	/**
	 * 发票类型代码 是否必须：是 存在的所有发票类型代码（三位一组，无分割）
	 */
	private static final String fapiao_type_ch = "fplxdm";
	/**
	 * 当前时钟 是否必须：是 YYYYMMDDHHMMSS
	 */
	private static final String cur_time_ch = "dqsz";
	/**
	 * 启用时间 是否必须：是 YYYYMMDDHHMMSS
	 */
	private static final String enable_time_ch = "qysj";
	/**
	 * 版本号 是否必须：是
	 */
	private static final String vis_no_ch = "bbh";
	/**
	 * 开票机号 是否必须：是 最大65535
	 */
	private static final String issue_no_ch = "kpjh";
	/**
	 * 企业类型，即特殊企业标识 是否必须：是 “00”不是特殊企业 “01”是特殊企业
	 */
	private static final String company_type_ch = "qylx";
	/**
	 * 保留信息 是否必须：是 农产品销售收购类型+铁路企业类型+纳税人性质
	 */
	private static final String keep_info_ch = "blxx";

	/**
	 * 其它扩展信息 是否必须：是
	 */
	private static final String other_extend_info_ch = "qtkzxx";
	/**
	 * 返回代码 是否必须：是 00000000成功，其它失败
	 */
	private static final String return_code_ch = "returncode";
	/**
	 * 返回信息 是否必须：是
	 */
	private static final String return_msg_ch = "returnmsg";

}
