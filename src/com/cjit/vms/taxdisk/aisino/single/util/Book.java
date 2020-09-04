package com.cjit.vms.taxdisk.aisino.single.util;

import javax.xml.bind.annotation.XmlElement;

/**
 * 测试用
 * @author john
 *
 */
public class Book {

	private String name;
	private Double price;

	public Book() {
		super();
	}
	public Book(String name, Double price) {
		super();
		this.name = name;
		this.price = price;
	}
	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
