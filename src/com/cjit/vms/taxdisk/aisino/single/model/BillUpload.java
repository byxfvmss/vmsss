package com.cjit.vms.taxdisk.aisino.single.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.cjit.vms.taxdisk.servlet.model.BaseModel;
import com.cjit.vms.trans.service.impl.VmsCommonServiceImpl;

/**
 * ClassName: BillUpload 
 * @Description: 发票上传
 * @author Napoléon 
 * @date 2016-4-13
 */
public class BillUpload {
	
	/**
	 * 上传的发票代码
	 */
	private String fpdm; 
	/**
	 *上传的发票号码
	 */
	private String fphm;
	
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	
	public BillUpload(String fpdm, String fphm) {
		super();
		this.fpdm = fpdm;
		this.fphm = fphm;
	}
	public BillUpload() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
