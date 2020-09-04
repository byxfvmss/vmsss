package com.cjit.vms.taxdisk.aisino.single.model.parseJson;

/**
 * 获取库存信息（返回报文）
 * @author john
 *
 */
public class GetInvInfoResp extends CommonResp{

	private String InfoNumber;//发票号码
	private String InfoTypeCode;//发票代码
	private Integer InvStock;//库存
	private String TaxClock;//(格式yyyy-mm-dd hh:nn:ss )//金税设备日期
	private String TaxCode;//税号
	private Integer UploadMode;//0手动上传 1自动上传
	private Integer MachineNo;//机器号或分机号
	private Integer IsRepReached;//是否已到抄税期 1已到
	private Integer IsLockReached;//是否已到锁死期 1 已到
	private Integer InvMcType;//0打开的是防伪开票；1打开的是开票服务器
	private Integer Kpfwqh;//开票服务器号
	private String CheckCode;//机器码
	private String CorpName;//企业名称
	
	public String getInfoNumber() {
		return InfoNumber;
	}
	public void setInfoNumber(String infoNumber) {
		InfoNumber = infoNumber;
	}
	public String getInfoTypeCode() {
		return InfoTypeCode;
	}
	public void setInfoTypeCode(String infoTypeCode) {
		InfoTypeCode = infoTypeCode;
	}
	public Integer getInvStock() {
		return InvStock;
	}
	public void setInvStock(Integer invStock) {
		InvStock = invStock;
	}
	public String getTaxClock() {
		return TaxClock;
	}
	public void setTaxClock(String taxClock) {
		TaxClock = taxClock;
	}
	public String getTaxCode() {
		return TaxCode;
	}
	public void setTaxCode(String taxCode) {
		TaxCode = taxCode;
	}
	public Integer getUploadMode() {
		return UploadMode;
	}
	public void setUploadMode(Integer uploadMode) {
		UploadMode = uploadMode;
	}
	public Integer getMachineNo() {
		return MachineNo;
	}
	public void setMachineNo(Integer machineNo) {
		MachineNo = machineNo;
	}
	public Integer getIsRepReached() {
		return IsRepReached;
	}
	public void setIsRepReached(Integer isRepReached) {
		IsRepReached = isRepReached;
	}
	public Integer getIsLockReached() {
		return IsLockReached;
	}
	public void setIsLockReached(Integer isLockReached) {
		IsLockReached = isLockReached;
	}
	public Integer getInvMcType() {
		return InvMcType;
	}
	public void setInvMcType(Integer invMcType) {
		InvMcType = invMcType;
	}
	public Integer getKpfwqh() {
		return Kpfwqh;
	}
	public void setKpfwqh(Integer kpfwqh) {
		Kpfwqh = kpfwqh;
	}
	public String getCheckCode() {
		return CheckCode;
	}
	public void setCheckCode(String checkCode) {
		CheckCode = checkCode;
	}
	public String getCorpName() {
		return CorpName;
	}
	public void setCorpName(String corpName) {
		CorpName = corpName;
	}
}
