/**
 * 机构PO
 */
package com.cjit.gjsz.system.model;

import java.io.Serializable;

/**
 * @author huboA
 */
public class Organization implements Serializable {

	private static final long serialVersionUID = -1195032677719224364L;
	private String id;
	private String parentId;//爸爸节点
	private String name;
	private String shortName;
	private String customId;
	private String error = "";
	private String news = "yes";
	private String rpttitle = "";
	private String showName = "";
	private String address; // 机构地址
	private String tel; // 电话
	private String taxperName; // 纳税人名称
	private String taxperNumber; // 纳税人识别号，即税务登记号
	private String account; // 账号
	private String taxAddress;// 纳税人地址
	private String taxTel;// 纳税人电话
	private String taxBank;// 纳税人开户行
	private String taxType;
	private String instPath;//路径
	
	
	public String getInstPath() {
		return instPath;
	}

	public void setInstPath(String instPath) {
		this.instPath = instPath;
	}

	public Organization() {
	}

	public Organization(String instCode) {
		this.id = instCode;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRpttitle() {
		return rpttitle;
	}

	public void setRpttitle(String rpttitle) {
		this.rpttitle = rpttitle;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTaxperName() {
		return taxperName;
	}

	public void setTaxperName(String taxperName) {
		this.taxperName = taxperName;
	}

	public String getTaxperNumber() {
		return taxperNumber;
	}

	public void setTaxperNumber(String taxperNumber) {
		this.taxperNumber = taxperNumber;
	}

	public String getAccount() {
		return account;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaxAddress() {
		return taxAddress;
	}

	public void setTaxAddress(String taxAddress) {
		this.taxAddress = taxAddress;
	}

	public String getTaxTel() {
		return taxTel;
	}

	public void setTaxTel(String taxTel) {
		this.taxTel = taxTel;
	}

	public String getTaxBank() {
		return taxBank;
	}

	public void setTaxBank(String taxBank) {
		this.taxBank = taxBank;
	}
}
