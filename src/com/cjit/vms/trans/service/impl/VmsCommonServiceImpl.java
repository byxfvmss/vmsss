package com.cjit.vms.trans.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.kxml2.kdom.Document;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.datadeal.model.CodeDictionary;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.trans.model.BillItemInfo;
import com.cjit.vms.trans.service.VmsCommonService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class VmsCommonServiceImpl extends GenericServiceImpl implements
		VmsCommonService {

	public Map findCodeDictionary(String codeType) {
		Map retMap = new HashMap();
		Map param = new HashMap();
		param.put("codeType", codeType);
		List listCD= this.find("findCodeDictionaryList", param);
		if(listCD!=null&&listCD.size()>0){
			for(int i=0;i<listCD.size();i++){
				CodeDictionary cd=(CodeDictionary)listCD.get(i);
				retMap.put(cd.getCodeValueStandardNum(), cd.getCodeName().equals("null")?"":cd.getCodeName());
			}
		}
		return retMap;
	}
	///template/bill/outBill/
	/**
	 * 给空白发票添加信息
	 * 
	 * @param Map
	 * @return String
	 */
	public String createMark(Map map) {
		String vatType = map.get("vatType")==null?"0":map.get("vatType") + "";
		String billCode = map.get("billCode")==null?"":map.get("billCode") + "";
		String billNo = map.get("billNo")==null?"":map.get("billNo") + "";
		String billDate = map.get("billDate")==null?"":map.get("billDate") + "";
		String customerName = map.get("customerName")==null?"":map.get("customerName") + "";
		String customerTaxno = map.get("customerTaxno")==null?"":map.get("customerTaxno") + "";
		String customerAddressandphone = map.get("customerAddressandphone")==null?"":map.get("customerAddressandphone") + "";
		String customerBankandaccount = map.get("customerBankandaccount")==null?"":map.get("customerBankandaccount") + "";
		String billPasswd = map.get("billPasswd")==null?"":map.get("billPasswd") + "";
		List billItemList = (List) map.get("billItemList");//货物list
		BillItemInfo billItemInfo = new BillItemInfo();
		String cancelName = map.get("cancelName")==null?"":map.get("cancelName") + "";
		String cancelTaxno = map.get("cancelTaxno")==null?"":map.get("cancelTaxno") + "";
		String cancelAddressandphone = map.get("cancelAddressandphone")==null?"":map.get("cancelAddressandphone") + "";
		String cancelBankandaccount = map.get("cancelBankandaccount")==null?"":map.get("cancelBankandaccount") + "";
		String payeeName = map.get("payeeName")==null?"":map.get("payeeName") + "";
		String reviewerName = map.get("reviewerName")==null?"":map.get("reviewerName") + "";
		String drawerName = map.get("drawerName")==null?"":map.get("drawerName") + "";
		String remark = map.get("remark")==null?"":map.get("remark") + "";
		
		//String classPath = this.getClass().getClassLoader().getResource("").getPath();
		 //java.net.URLDecoder.decode(getClassPathFile(this.getClass()).getAbsolutePath(),"UTF-8");
		String classPath=getClassPath(this.getClass());
		//System.out.println(classPath);
		String rooPath = classPath.replaceAll("WEB-INF/classes/","");
		int j=rooPath.indexOf("WEB-INF");
		String rootPath=rooPath.substring(0,j);
		System.out.println(""+rootPath);
		String filePath = "";
		if("0".equals(vatType)){
			filePath = rootPath+"/template/bill/zzszy_invoice.png";
		}else{
			filePath = rootPath+"/template/bill/zzspy_invoice.png";
		}
		System.out.println("模板路径"+filePath);
		// 字体颜色
		Color blackColor = Color.black;
		Color blueColor = Color.blue;
		String fontType = "宋体";
		String fontType2 = "";
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bimage.createGraphics();
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		AttributedString ats = new AttributedString("");
		AttributedString ats1 = new AttributedString("");
		AttributedString ats2 = new AttributedString("");
		Font f = new Font(fontType, Font.PLAIN, 30);
		AttributedCharacterIterator iter = ats.getIterator();
		AttributedCharacterIterator iter1 = ats1.getIterator();
		AttributedCharacterIterator iter2 = ats2.getIterator();
		if(!"".equals(billCode)){
			// 左上角发票代码
			ats = new AttributedString(billCode);
			f = new Font(fontType, Font.PLAIN, 30);
			ats.addAttribute(TextAttribute.FONT, f, 0, billCode.length());
			iter = ats.getIterator();
			g.setColor(blackColor);
			g.drawString(iter, 300, 80);
			// 右上角发票代码
			f = new Font(fontType, Font.PLAIN, 16);
			ats.addAttribute(TextAttribute.FONT, f, 0, billCode.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1280, 55);
		}
		
		if(!"".equals(billNo)){
			// 右上角发票号码(大)
			ats = new AttributedString(billNo);
			f = new Font(fontType, Font.BOLD, 40);
			ats.addAttribute(TextAttribute.FONT, f, 0, billNo.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1080, 63);
			// 右上角发票号码（小）
			ats = new AttributedString(billNo);
			f = new Font(fontType, Font.PLAIN, 16);
			ats.addAttribute(TextAttribute.FONT, f, 0, billNo.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1270, 80);
		}
		// 开票日期
		if(!"".equals(billDate)){
			if(billDate.length()>10)
				billDate = billDate.substring(0,10);
			ats = new AttributedString(billDate);
			f = new Font(fontType, Font.PLAIN, 20);
			ats.addAttribute(TextAttribute.FONT, f, 0, billDate.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1200, 137);
		}
		// 名称
		if(!"".equals(cancelName)){
			ats = new AttributedString(cancelName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, cancelName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 197);
		}
		// 纳税人识别号
		if(!"".equals(cancelTaxno)){
			ats = new AttributedString(cancelTaxno);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, cancelTaxno.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 285, 233);
		}
		// 地址、电话
		if(!"".equals(cancelAddressandphone)){
			ats = new AttributedString(cancelAddressandphone);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					cancelAddressandphone.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 269);
		}
		// 开户行及账号
		if(!"".equals(cancelBankandaccount)){
			ats = new AttributedString(cancelBankandaccount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					cancelBankandaccount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 305);
		}
		// 密码区
		if(!"".equals(billPasswd)){
			List pwdList = stringToList(billPasswd,27);
			for (int i = 0; i < pwdList.size(); i++) {
				String tmp = (String) pwdList.get(i);
				ats = new AttributedString(tmp);
				f = new Font(fontType, Font.PLAIN, 22);
				ats.addAttribute(TextAttribute.FONT, f, 0, tmp.length());
				iter = ats.getIterator();
				g.setColor(blueColor);
				int h = 200 + i * 35;
				g.drawString(iter, 900, h);
			}
		}
		//货物列表
		if(billItemList!=null && billItemList.size()>0){
			BigDecimal amtSum = new BigDecimal(0);
			BigDecimal taxAmtSum = new BigDecimal(0);
			for(int h=0;h<billItemList.size();h++){
				billItemInfo = (BillItemInfo) billItemList.get(h);
				// 货物名称
				String goodsName = billItemInfo.getGoodsName();
				if(!"".equals(goodsName)){
					ats = new AttributedString(goodsName);
					f = new Font(fontType, Font.PLAIN, 17);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsName.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 55, 375+h*25);
				}
				// 规格
				String specandmodel = billItemInfo.getSpecandmodel();
				if(!"".equals(specandmodel)){
					ats = new AttributedString(specandmodel);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, specandmodel.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 410, 375+h*25);
				}
				// 单位
				String goodsUnit = billItemInfo.getGoodsUnit();
				if(!"".equals(goodsUnit)){
					ats = new AttributedString(goodsUnit);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsUnit.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 585, 375+h*25);
				}
				// 数量
				String goodsNo = billItemInfo.getGoodsNo()+"";
				if(!"".equals(goodsNo)){
					ats = new AttributedString(goodsNo);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsNo.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 700 - (goodsNo.length() * 10), 375+h*25);
				}
				// 单价
				String goodsPrice = billItemInfo.getGoodsPrice()+"";
				if(!"".equals(goodsPrice)){
					ats = new AttributedString(goodsPrice);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsPrice.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 875 - (goodsPrice.length() * 10), 375+h*25);
				}
				// 金额
				String amt = billItemInfo.getAmt()+"";
				if(!"".equals(amt)){
					ats = new AttributedString(amt);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, amt.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 1010 - (amt.length() * 10), 375+h*25);
					amtSum = amtSum.add(billItemInfo.getAmt());
				}
				// 税率
				String taxRate = billItemInfo.getTaxRate().multiply(new BigDecimal(100))+"%";
				if(!"".equals(taxRate)){
					ats = new AttributedString(taxRate);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, taxRate.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
//					g.drawString(iter, 1200 - (taxRate.length() * 10), 375+h*25);
					g.drawString(iter, 1155, 375+h*25);
				}
				// 税额
				String taxAmt = billItemInfo.getTaxAmt()+"";
				if(!"".equals(taxAmt)){
					ats = new AttributedString(taxAmt);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, taxAmt.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
//					g.drawString(iter, 1250 - (taxAmt.length() * 10), 375+h*25);
					g.drawString(iter, 1240 , 375+h*25);
				}
				taxAmtSum = taxAmtSum.add(billItemInfo.getTaxAmt());
			}
			// 金额合计
			String amtSumTotal = "¥"+amtSum+"";
			ats = new AttributedString(amtSumTotal);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, amtSumTotal.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 960, 610);
			// 税额合计
			String taxAmtSumTotal = "¥"+taxAmtSum+"";
			ats = new AttributedString(taxAmtSumTotal);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, taxAmtSumTotal.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1240, 610);
			// 合计大写
			String tq = "⊗";
			ats = new AttributedString(tq);
			f = new Font(fontType2, Font.PLAIN, 32);
			ats.addAttribute(TextAttribute.FONT, f, 0, tq.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 430, 661);
			BigDecimal numberOfMoney = amtSum.add(taxAmtSum);
			String hjdx = number2CNMontrayUnit(numberOfMoney);
			ats = new AttributedString(hjdx);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, hjdx.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 465, 660);
			// 合计小写
			String amount = "¥"+numberOfMoney+"";
			ats = new AttributedString(amount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, amount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1150, 655);
		}
		//销方
		// 名称
		if(!"".equals(customerName)){
			ats = new AttributedString(customerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, customerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 709);
		}
		// 纳税人识别号
		if(!"".equals(customerTaxno)){
			ats = new AttributedString(customerTaxno);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, customerTaxno.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 285, 743);
		}
		// 地址、电话
		if(!"".equals(customerAddressandphone)){
			ats = new AttributedString(customerAddressandphone);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					customerAddressandphone.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 773);
		}
		// 开户行及账号
		if(!"".equals(customerBankandaccount)){
			ats = new AttributedString(customerBankandaccount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					customerBankandaccount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 806);
		}
		//收款人
		if(!"".equals(payeeName)){
			ats = new AttributedString(payeeName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, payeeName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 190, 847);
		}
		//复核
		if(!"".equals(reviewerName)){
			ats = new AttributedString(reviewerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, reviewerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 545, 847);
		}
		//开票人
		if(!"".equals(drawerName)){
			ats = new AttributedString(drawerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, drawerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 870, 847);
		}
		//备注
		if (!"".equals(remark)) {
			String Array[] = remark.split("】");
			/*
			 * System.out.println(Array[0]); System.out.println(Array[1]);
			 * System.out.println(Array[2]);
			 */
			String conmmon = "";
//			String tb = Array[0] + "】";
//			String bd = Array[1] + "】";
//			String xz = Array[2] + "】";
			for(int i = 0; i < Array.length ; i++){
				conmmon = Array[i] + "】";
				ats = new AttributedString(conmmon);
				f = new Font(fontType, Font.PLAIN, 20);
				ats.addAttribute(TextAttribute.FONT, f, 0, conmmon.length());
				iter = ats.getIterator();
				g.setColor(blueColor);
				g.drawString(iter, 875, 710 + i*30);
				
			}
			
//			ats = new AttributedString(remark);
			
//			ats = new AttributedString(tb);
//			ats1 = new AttributedString(bd);
//			ats2 = new AttributedString(xz);
//			
//			f = new Font(fontType, Font.PLAIN, 22);
//			ats.addAttribute(TextAttribute.FONT, f, 0, remark.length());
			
//			ats.addAttribute(TextAttribute.FONT, f, 0, tb.length());
//			ats1.addAttribute(TextAttribute.FONT, f, 0, bd.length());
//			ats2.addAttribute(TextAttribute.FONT, f, 0, xz.length());
			
//			iter = ats.getIterator();
			
//			iter = ats.getIterator();
//			iter1 = ats1.getIterator();
//			iter2 = ats2.getIterator();
//			
//			
//			g.setColor(blueColor);
//			g.drawString(iter, 875, 710);
//			g.drawString(iter1, 875, 740);
//			g.drawString(iter2, 875, 770);
			
		}
		g.dispose();
		try {
			String fileName = "";
			if("0".equals(vatType)){
				fileName = "Z";
			}else{
				fileName = "P";
			}//WebRoot/WEB-INF/classes 
			//Users\tom\workspaces3\apache-tomcat-6.0.44-windows-x64\apache-tomcat-6.0.44\webapps\vmss\WEB-INF\classes/template
			//D:\Users\tom\workspaces3\apache-tomcat-6.0.44-windows-x64\apache-tomcat-6.0.44\webapps\vmss\template
			//nt loc = str.indexOf("字符");//首先获取字符的位置

			//然后调用字符串截取 
			//String newStr = str.substring(0,loc);//再对字符串进行截取，获得想要得到的字符串 
			fileName = fileName+System.currentTimeMillis() + ".png";
			String nF = rootPath+"/template/bill/outBill/" + fileName;
			System.err.println("输出路径"+nF);
			FileOutputStream out = new FileOutputStream(nF);
			ImageIO.write(bimage, "png", new File(nF));
			out.close();
			return fileName;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 给红冲发票添加信息
	 * 
	 * @param Map
	 * @return String
	 */
	public String createRedMark(Map map) {
		String vatType = map.get("vatType")==null?"0":map.get("vatType") + "";
		String billCode = map.get("billCode")==null?"":map.get("billCode") + "";
		String billNo = map.get("billNo")==null?"":map.get("billNo") + "";
		String billDate = map.get("billDate")==null?"":map.get("billDate") + "";
		String customerName = map.get("customerName")==null?"":map.get("customerName") + "";
		String customerTaxno = map.get("customerTaxno")==null?"":map.get("customerTaxno") + "";
		String customerAddressandphone = map.get("customerAddressandphone")==null?"":map.get("customerAddressandphone") + "";
		String customerBankandaccount = map.get("customerBankandaccount")==null?"":map.get("customerBankandaccount") + "";
		String billPasswd = map.get("billPasswd")==null?"":map.get("billPasswd") + "";
		List billItemList = (List) map.get("billItemList");//货物list
		BillItemInfo billItemInfo = new BillItemInfo();
		String cancelName = map.get("cancelName")==null?"":map.get("cancelName") + "";
		String cancelTaxno = map.get("cancelTaxno")==null?"":map.get("cancelTaxno") + "";
		String cancelAddressandphone = map.get("cancelAddressandphone")==null?"":map.get("cancelAddressandphone") + "";
		String cancelBankandaccount = map.get("cancelBankandaccount")==null?"":map.get("cancelBankandaccount") + "";
		String payeeName = map.get("payeeName")==null?"":map.get("payeeName") + "";
		String reviewerName = map.get("reviewerName")==null?"":map.get("reviewerName") + "";
		String drawerName = map.get("drawerName")==null?"":map.get("drawerName") + "";
		String remark = map.get("remark")==null?"":map.get("remark") + "";
		
		//String classPath = this.getClass().getClassLoader().getResource("").getPath();
		 //java.net.URLDecoder.decode(getClassPathFile(this.getClass()).getAbsolutePath(),"UTF-8");
		String classPath=getClassPath(this.getClass());
		//System.out.println(classPath);
		String rooPath = classPath.replaceAll("WEB-INF/classes/","");
		int j=rooPath.indexOf("WEB-INF");
		String rootPath=rooPath.substring(0,j);
		System.out.println(""+rootPath);
		String filePath = "";
		if("0".equals(vatType)){
			filePath = rootPath+"/template/bill/zzszy_invoice.png";
		}else{
			filePath = rootPath+"/template/bill/zzspy_invoice.png";
		}
		System.out.println("模板路径"+filePath);
		// 字体颜色
		Color blackColor = Color.black;
		Color blueColor = Color.blue;
		String fontType = "宋体";
		String fontType2 = "";
		ImageIcon imgIcon = new ImageIcon(filePath);
		Image theImg = imgIcon.getImage();
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);
		BufferedImage bimage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bimage.createGraphics();
		g.setBackground(Color.white);
		g.drawImage(theImg, 0, 0, null);
		AttributedString ats = new AttributedString("");
		Font f = new Font(fontType, Font.PLAIN, 30);
		AttributedCharacterIterator iter = ats.getIterator();
		// 销项负数
		ats = new AttributedString("销项负数");
		f = new Font(fontType, Font.PLAIN, 30);
		ats.addAttribute(TextAttribute.FONT, f, 0, 4);
		iter = ats.getIterator();
		g.setColor(blueColor);
		g.drawString(iter, 250, 130);
		if(!"".equals(billCode)){
			// 左上角发票代码
			ats = new AttributedString(billCode);
			f = new Font(fontType, Font.PLAIN, 30);
			ats.addAttribute(TextAttribute.FONT, f, 0, billCode.length());
			iter = ats.getIterator();
			g.setColor(blackColor);
			g.drawString(iter, 300, 80);
			// 右上角发票代码
			f = new Font(fontType, Font.PLAIN, 16);
			ats.addAttribute(TextAttribute.FONT, f, 0, billCode.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1280, 55);
		}
		
		if(!"".equals(billNo)){
			// 右上角发票号码(大)
			ats = new AttributedString(billNo);
			f = new Font(fontType, Font.BOLD, 40);
			ats.addAttribute(TextAttribute.FONT, f, 0, billNo.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1080, 63);
			// 右上角发票号码（小）
			ats = new AttributedString(billNo);
			f = new Font(fontType, Font.PLAIN, 16);
			ats.addAttribute(TextAttribute.FONT, f, 0, billNo.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1270, 80);
		}
		// 开票日期
		if(!"".equals(billDate)){
			if(billDate.length()>10)
				billDate = billDate.substring(0,10);
			ats = new AttributedString(billDate);
			f = new Font(fontType, Font.PLAIN, 20);
			ats.addAttribute(TextAttribute.FONT, f, 0, billDate.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1200, 137);
		}
		// 名称
		if(!"".equals(cancelName)){
			ats = new AttributedString(cancelName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, cancelName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 197);
		}
		// 纳税人识别号
		if(!"".equals(cancelTaxno)){
			ats = new AttributedString(cancelTaxno);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, cancelTaxno.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 285, 233);
		}
		// 地址、电话
		if(!"".equals(cancelAddressandphone)){
			ats = new AttributedString(cancelAddressandphone);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					cancelAddressandphone.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 269);
		}
		// 开户行及账号
		if(!"".equals(cancelBankandaccount)){
			ats = new AttributedString(cancelBankandaccount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					cancelBankandaccount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 305);
		}
		// 密码区
		if(!"".equals(billPasswd)){
			List pwdList = stringToList(billPasswd,27);
			for (int i = 0; i < pwdList.size(); i++) {
				String tmp = (String) pwdList.get(i);
				ats = new AttributedString(tmp);
				f = new Font(fontType, Font.PLAIN, 22);
				ats.addAttribute(TextAttribute.FONT, f, 0, tmp.length());
				iter = ats.getIterator();
				g.setColor(blueColor);
				int h = 200 + i * 35;
				g.drawString(iter, 900, h);
			}
		}
		//货物列表
		if(billItemList!=null && billItemList.size()>0){
			BigDecimal amtSum = new BigDecimal(0);
			BigDecimal taxAmtSum = new BigDecimal(0);
			for(int h=0;h<billItemList.size();h++){
				billItemInfo = (BillItemInfo) billItemList.get(h);
				// 货物名称
				String goodsName = billItemInfo.getGoodsName();
				if(!"".equals(goodsName)){
					ats = new AttributedString(goodsName);
					f = new Font(fontType, Font.PLAIN, 17);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsName.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 55, 375+h*25);
				}
				// 规格
				String specandmodel = billItemInfo.getSpecandmodel();
				if(!"".equals(specandmodel)){
					ats = new AttributedString(specandmodel);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, specandmodel.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 410, 375+h*25);
				}
				// 单位
				String goodsUnit = billItemInfo.getGoodsUnit();
				if(!"".equals(goodsUnit)){
					ats = new AttributedString(goodsUnit);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsUnit.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 585, 375+h*25);
				}
				// 数量
				String goodsNo = "-"+billItemInfo.getGoodsNo()+"";
				if(!"".equals(goodsNo)){
					ats = new AttributedString(goodsNo);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsNo.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 780 - (goodsNo.length() * 10), 375+h*25);
				}
				// 单价
				String goodsPrice = billItemInfo.getGoodsPrice()+"";
				if(!"".equals(goodsPrice)){
					ats = new AttributedString(goodsPrice);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, goodsPrice.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 930 - (goodsPrice.length() * 10), 375+h*25);
				}
				// 金额
				String amt = "-"+billItemInfo.getAmt()+"";
				if(!"".equals(amt)){
					ats = new AttributedString(amt);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, amt.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 1130 - (amt.length() * 10), 375+h*25);
					amtSum = amtSum.add(billItemInfo.getAmt());
				}
				// 税率
				String taxRate = billItemInfo.getTaxRate().multiply(new BigDecimal(100))+"%";
				if(!"".equals(taxRate)){
					ats = new AttributedString(taxRate);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, taxRate.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 1200 - (taxRate.length() * 10), 375+h*25);
				}
				// 税额
				String taxAmt = "-"+billItemInfo.getTaxAmt()+"";
				if(!"".equals(taxAmt)){
					ats = new AttributedString(taxAmt);
					f = new Font(fontType, Font.PLAIN, 22);
					ats.addAttribute(TextAttribute.FONT, f, 0, taxAmt.length());
					iter = ats.getIterator();
					g.setColor(blueColor);
					g.drawString(iter, 1410 - (taxAmt.length() * 10), 375+h*25);
				}
				taxAmtSum = taxAmtSum.add(billItemInfo.getTaxAmt());
			}
			// 金额合计
			String amtSumTotal = "¥"+"-"+amtSum+"";
			ats = new AttributedString(amtSumTotal);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, amtSumTotal.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 970, 610);
			// 税额合计
			String taxAmtSumTotal = "¥"+"-"+taxAmtSum+"";
			ats = new AttributedString(taxAmtSumTotal);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, taxAmtSumTotal.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1250, 610);
			// 合计大写
			String tq = "⊗";
			ats = new AttributedString(tq);
			f = new Font(fontType2, Font.PLAIN, 32);
			ats.addAttribute(TextAttribute.FONT, f, 0, tq.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 430, 661);
			BigDecimal numberOfMoney = amtSum.add(taxAmtSum);
			String hjdx = "（负数）"+number2CNMontrayUnit(numberOfMoney);
			ats = new AttributedString(hjdx);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, hjdx.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 465, 660);
			// 合计小写
			String amount = "¥"+"-"+numberOfMoney+"";
			ats = new AttributedString(amount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, amount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 1150, 655);
		}
		//销方
		// 名称
		if(!"".equals(customerName)){
			ats = new AttributedString(customerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, customerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 709);
		}
		// 纳税人识别号
		if(!"".equals(customerTaxno)){
			ats = new AttributedString(customerTaxno);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, customerTaxno.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 285, 743);
		}
		// 地址、电话
		if(!"".equals(customerAddressandphone)){
			ats = new AttributedString(customerAddressandphone);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					customerAddressandphone.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 773);
		}
		// 开户行及账号
		if(!"".equals(customerBankandaccount)){
			ats = new AttributedString(customerBankandaccount);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0,
					customerBankandaccount.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 270, 806);
		}
		//收款人
		if(!"".equals(payeeName)){
			ats = new AttributedString(payeeName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, payeeName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 190, 847);
		}
		//复核
		if(!"".equals(reviewerName)){
			ats = new AttributedString(reviewerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, reviewerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 545, 847);
		}
		//开票人
		if(!"".equals(drawerName)){
			ats = new AttributedString(drawerName);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, drawerName.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 870, 847);
		}
		//备注
		if (!"".equals(remark)) {
			ats = new AttributedString(remark);
			f = new Font(fontType, Font.PLAIN, 22);
			ats.addAttribute(TextAttribute.FONT, f, 0, remark.length());
			iter = ats.getIterator();
			g.setColor(blueColor);
			g.drawString(iter, 875, 710);
		}
		g.dispose();
		try {
			String fileName = "";
			if("0".equals(vatType)){
				fileName = "Z";
			}else{
				fileName = "P";
			}//WebRoot/WEB-INF/classes 
			//Users\tom\workspaces3\apache-tomcat-6.0.44-windows-x64\apache-tomcat-6.0.44\webapps\vmss\WEB-INF\classes/template
			//D:\Users\tom\workspaces3\apache-tomcat-6.0.44-windows-x64\apache-tomcat-6.0.44\webapps\vmss\template
			//nt loc = str.indexOf("字符");//首先获取字符的位置

			//然后调用字符串截取 
			//String newStr = str.substring(0,loc);//再对字符串进行截取，获得想要得到的字符串 
			fileName = fileName+System.currentTimeMillis() + ".png";
			String nF = rootPath+"/template/bill/outBill/" + fileName;
			System.err.println("输出路径"+nF);
			FileOutputStream out = new FileOutputStream(nF);
			ImageIO.write(bimage, "png", new File(nF));
			out.close();
			return fileName;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 生成WORD信息
	 * 
	 * @param Map
	 * @return String
	 */
	public String createWord(Map map){
		Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        Map dataMap=new HashMap();
        String tkDate = map.get("tkDate")==null?"  年   月  日":map.get("tkDate") + "";//填开日期
        dataMap.put("tkDate", tkDate); 
        String cancelName = map.get("cancelName")==null?"":map.get("cancelName") + "";//销售方名称
        dataMap.put("customerName", cancelName); 
        String cancelTaxno = map.get("cancelTaxno")==null?"":map.get("cancelTaxno") + "";//销售方纳税人识别号
        dataMap.put("customerTaxno", cancelTaxno); 
        String customerName = map.get("customerName")==null?"":map.get("customerName") + "";//购买方名称
        dataMap.put("cancelName", customerName); 
        String customerTaxno = map.get("customerTaxno")==null?"":map.get("customerTaxno") + "";//购买方纳税人识别号
        dataMap.put("cancelTaxno", customerTaxno); 
        String customerBillCode = map.get("customerBillCode")==null?"          ":map.get("customerBillCode") + "";//购买方发票代码
        dataMap.put("customerBillCode", customerBillCode); 
        String customerBillNo = map.get("customerBillNo")==null?"        ":map.get("customerBillNo") + "";//购买方号码
        dataMap.put("customerBillNo", customerBillNo); 
        String cancelBillCode = map.get("cancelBillCode")==null?"          ":map.get("cancelBillCode") + "";//销售方发票代码
        dataMap.put("cancelBillCode", cancelBillCode); 
        String cancelBillNo = map.get("cancelBillNo")==null?"        ":map.get("cancelBillNo") + "";//销售方号码
        dataMap.put("cancelBillNo", cancelBillNo); 
        String dealNo = map.get("dealNo")==null?"":map.get("dealNo") + "";//编号
        dataMap.put("dealNo", dealNo); 
        String buySellInd = map.get("buySellInd")==null?"":map.get("buySellInd") + "";//购方/销方
        String level1Option = map.get("level1Option")==null?"":map.get("level1Option") + "";//一级选项
        String level2Option = map.get("level2Option")==null?"":map.get("level2Option") + "";//二级选项
        String charSelect = "<w:sym w:font='Wingdings 2' w:char='F052'/>";
        String charNoSelect = "<w:sym w:font='Wingdings 2' w:char='F0A3'/>";
        dataMap.put("select1", charNoSelect);
        dataMap.put("select1_1", charNoSelect);
        dataMap.put("select1_2", charNoSelect);
        dataMap.put("select1_2_1", charNoSelect);
        dataMap.put("select1_2_2", charNoSelect);
        dataMap.put("select1_2_3", charNoSelect);
        dataMap.put("select1_2_4", charNoSelect);
        dataMap.put("select1_2_4", charNoSelect);
        dataMap.put("select2", charNoSelect);
        dataMap.put("select2_1", charNoSelect);
        dataMap.put("select2_2", charNoSelect);
        if("0".equals(buySellInd)){//购方
        	dataMap.put("select1", charSelect);
        	if("0".equals(level1Option)){
        		dataMap.put("select1_1", charSelect);
        	}else if("1".equals(level1Option)){
        		dataMap.put("select1_2", charSelect);
        	}
        	if("0".equals(level2Option)){
        		dataMap.put("select1_2_1", charSelect);
        	}else if("1".equals(level2Option)){
        		dataMap.put("select1_2_2", charSelect);
        	}else if("2".equals(level2Option)){
        		dataMap.put("select1_2_3", charSelect);
        	}else if("3".equals(level2Option)){
        		dataMap.put("select1_2_4", charSelect);
        	}
        }else if("1".equals(buySellInd)){//销方
        	dataMap.put("select2", charSelect);
        	if("0".equals(level1Option)){
        		dataMap.put("select2_1", charSelect);
        	}else if("1".equals(level1Option)){
        		dataMap.put("select2_2", charSelect);
        	}
        }
        //合计 
        BigDecimal amtSum = new BigDecimal(0);
		BigDecimal taxAmtSum = new BigDecimal(0);
        
        //货物
        List billItemList = (List) map.get("billItemList");//货物list
        List goodsList = new ArrayList();
        if(billItemList==null){
        	billItemList = new ArrayList();
        }
        BillItemInfo billItemInfo = new BillItemInfo();
        if(billItemList.size()>0){
        	for (int i = 0; i < billItemList.size(); i++) {
        		billItemInfo = (BillItemInfo) billItemList.get(i);
	        	Map goodsMap = new HashMap();
	        	goodsMap.put("goodsName", billItemInfo.getGoodsName());
	        	goodsMap.put("goodsNo", billItemInfo.getGoodsNo());
	        	goodsMap.put("goodsPrice", billItemInfo.getGoodsPrice());
	        	goodsMap.put("amt", billItemInfo.getAmt());
	        	goodsMap.put("taxRate", billItemInfo.getTaxRate());
	        	goodsMap.put("taxAmt", billItemInfo.getTaxAmt());
	        	amtSum = amtSum.add(billItemInfo.getAmt());
	        	taxAmtSum = taxAmtSum.add(billItemInfo.getTaxAmt());
	        	goodsList.add(goodsMap);
        	}
        }
        if(billItemList.size()<8){
        	int k =  8 - billItemList.size();
        	for (int t = 0; t < k; t++) {
	        	Map goodsMap = new HashMap();
	        	goodsMap.put("goodsName", "");
	        	goodsMap.put("goodsNo", "");
	        	goodsMap.put("goodsPrice", "");
	        	goodsMap.put("amt", "");
	        	goodsMap.put("taxRate", "");
	        	goodsMap.put("taxAmt", "");
	        	goodsList.add(goodsMap);
        	}
        }
    	dataMap.put("goodsList", goodsList);
    	if(amtSum.intValue()==0){
    		dataMap.put("amtSum", ""); 
    	}else{
    		dataMap.put("amtSum", amtSum); 
    	}
    	if(taxAmtSum.intValue()==0){
    		dataMap.put("taxAmtSum", ""); 
    	}else{
    		dataMap.put("taxAmtSum", taxAmtSum); 
    	}
        try {
        	String classPath=getClassPath(this.getClass());
    		String rootPath = classPath.toString().replace("WEB-INF\\classes","");
        	//String classPath = this.getClass().getClassLoader().getResource("").getPath();
        	//String rootPath = classPath.replaceAll("WEB-INF/classes/","");
    		String tplPath = rootPath+"/template/bill";
			configuration.setDirectoryForTemplateLoading(new File(tplPath));
			Template t = configuration.getTemplate("kjhzzzszy.ftl"); //模板文件名
			String fileName = System.currentTimeMillis()+".doc";
			File outFile = new File(tplPath+"/outWord/"+fileName);
	        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"),10240);
	        t.process(dataMap, out);
            out.flush();
            out.close();
            return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
	/**
     * 数字金额大写转换
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
    	String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆","伍", "陆", "柒", "捌", "玖" };
    	String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "圆","拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾","佰", "仟" };
    	
        String CN_FULL = "整";
        String CN_NEGATIVE = "负";
        /**
         * 金额的精度，默认值为2
         */
        int MONEY_PRECISION = 2;
        /**
         * 特殊字符：零圆整
         */
        String CN_ZEOR_FULL = "零圆" + CN_FULL;
        
        StringBuffer strBuf = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                	strBuf.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                	strBuf.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                strBuf.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                strBuf.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                	strBuf.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                    	strBuf.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                	strBuf.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
        	strBuf.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
        	strBuf.append(CN_FULL);
        }
        return strBuf.toString();
    }
    
    /**
	 * 将String按照固定长度截取，返回List
	 * 
	 * @param String
	 * @param int
	 * @return List
	 */
    public List stringToList(String str,int len){
    	List strList = new ArrayList(); 
        int start = 0;  
        int end = start+len;  
          
        while(true){  
            if(start<str.length()){
	            String temp = str.substring(start, end);  
	            strList.add(temp);
	            start = end;  
	            end = end+len;
	            if(end>=str.length()){  
	                end = str.length();  
	            }
            }else{
            	return strList;
            }
        }
    }
    
	/**
	 * 纳税人名称的取得
	 * 
	 * @param info
	 * @return
	 * @author lee
	 */
	public List getAjaxTaxperNameList(Organization info) {
		Map map = new HashMap();

		// 纳税人名称
		map.put("taxperName", info.getTaxperName());
		// 纳税人识别号
		map.put("taxperNumber", info.getTaxperNumber());
		// 机构编号
		map.put("id", info.getId());
		
		return find("getAjaxTaxperNameList", map);
	}
    public static File getClassFile(Class clazz){
        URL path = clazz.getResource(clazz.getName().substring(
                clazz.getName().lastIndexOf(".")+1)+".classs");
        if(path == null){
            String name = clazz.getName().replaceAll("[.]", "/");
            path = clazz.getResource("/"+name+".class");
        }
        return new File(path.getFile());
    }
    /**
     * 得到当前类的路径
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class clazz){
        try{
            return java.net.URLDecoder.decode(getClassFile(clazz).getAbsolutePath(),"UTF-8");
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 取得当前类所在的ClassPath目录，比如tomcat下的classes路径
     * @param clazz
     * @return
     */
    public static File getClassPathFile(Class clazz){
        File file = getClassFile(clazz);
        for(int i=0,count = clazz.getName().split("[.]").length; i<count; i++)
            file = file.getParentFile();
        if(file.getName().toUpperCase().endsWith(".JAR!")){
            file = file.getParentFile();
        }
        return file;
    }
    /**
     * 取得当前类所在的ClassPath路径
     * @param clazz
     * @return
     */
    public static String getClassPath(Class clazz){
        try{
            return java.net.URLDecoder.decode(getClassPathFile(clazz).getAbsolutePath(),"UTF-8");
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }
}
