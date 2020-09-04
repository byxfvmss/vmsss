package com.cjit.vms.filem.util;

import java.util.List;

import com.cjit.common.util.DateUtils;
import com.cjit.vms.filem.model.TaxDisk;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.BillItemInfo;

public class FileUtil {

	// 税控盘口令
	public static String skpkl = "2f83835af4346517f55c47a6892469f6f3a146474f7bf39384500795db6bf601e71933bf6a19a14c6280c92b5d61fcc2eaa2e3cf40ae0a8b3977620e05fab6f5f7b14b36a24b03d7a1e7a2928067a0f2f245a2f9ef619e93235ec5372bb4dd48f07efc77a837cb75a4a3aa9115b29cea6ffacbdf5d86f4f7762d3597d4a27245057f03e4c110b4ebbc6e3422a570a6f47b0cd3f246f66f316fd385d23154a66b06aef4df3addf4e26c86822e4d8bbef8b306fc7b0891cd30f74807a8fe51f9a037737d87f95df6fb32cea1bb63b15990f602a4798e3d0c527e397cc60594b200f2ca584d589baf8e97d65dd08fb4076aafef37348b666a97cacda23d76a2dcf1";

	/**
	 * 导入注册码信息，用来验证是否有操作权限
	 * 
	 * @param zcmxx
	 *            注册码信息
	 * @return String
	 */
	public static String createXmlStringForZCMDR(String zcmxx) {
		String xml = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business comment=\"注册码信息导入\" id=\"ZCMDR\">");
		sb.append("<body yylxdm=\"1\">");
		sb.append("<input>");
		sb.append("<zcmxx>");
		sb.append(zcmxx);
		sb.append("</zcmxx>");
		sb.append("</input>");
		sb.append("</body>");
		sb.append("</business>");
		xml = sb.toString();
		return xml;
	}

	/**
	 * 读取税控盘基本信息，接口调用无需注册码验证
	 * 
	 * @param skpkl
	 *            税控盘口令
	 * @return String
	 */
	public static String createXmlStringForSKPXXCX(String skpkl) {
		String xml = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business comment=\"税控盘信息查询\" id=\"SKPXXCX\">");
		sb.append("<body yylxdm=\"1\">");
		sb.append("<input>");
		sb.append("<skpkl>");
		sb.append(skpkl);
		sb.append("</skpkl>");
		sb.append("</input>");
		sb.append("</body>");
		sb.append("</business>");
		xml = sb.toString();
		return xml;
	}

	/**
	 * 
	 * @param billInfo
	 *            票据信息
	 * @param taxDisk
	 *            税控盘信息
	 * @param keypwd
	 *            证书口令
	 * @param jmbbh
	 *            加密版本号
	 * @return
	 */
	public static String createXmlStringForFPKJ004(BillInfo billInfo,
			TaxDisk taxDisk, String keypwd, String jmbbh) {
		String xml = "";
		StringBuffer sb = new StringBuffer();
		List billItemList = billInfo.getBillItemList();
		String mainGoodsName = ""; // 主商品名称
		String mainGoodsTaxItem = ""; // 商品税目
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<business comment=\"发票开具\" id=\"FPKJ\">");
		sb.append("<body yylxdm=\"1\">");
		sb.append("<input>");
		// <skpbh>税控盘编号</skpbh>
		sb.append("<skpbh>").append(taxDisk.getSkpbh()).append("</skpbh>");
		// <skpkl>税控盘口令</skpkl>
		sb.append("<skpkl>").append(taxDisk.getSkpkl()).append("</skpkl>");
		// <keypwd>证书口令</keypwd>
		sb.append("<keypwd>").append(keypwd).append("</keypwd>");
		// <fplxdm>发票类型代码</fplxdm>
		sb.append("<fplxdm>004</fplxdm>");
		// <kplx>开票类型</kplx>
		sb.append("<kplx>0</kplx>");
		// <tspz>特殊票种标识<tspz>
		sb.append("<tspz>00</tspz>");
		// <xhdwsbh>销货单位识别号</xhdwsbh>
		sb.append("<xhdwsbh>").append(billInfo.getTaxno()).append("</xhdwsbh>");
		// <xhdwmc>销货单位名称</xhdwmc>
		sb.append("<xhdwmc>").append(billInfo.getName()).append("</xhdwmc>");
		// <xhdwdzdh>销货单位地址电话</xhdwdzdh>
		sb.append("<xhdwdzdh>").append(billInfo.getAddressandphone()).append(
				"</xhdwdzdh>");
		// <xhdwyhzh>销货单位银行帐号</xhdwyhzh>
		sb.append("<xhdwyhzh>").append(billInfo.getBankandaccount()).append(
				"</xhdwyhzh>");
		// <ghdwsbh>购货单位识别号</ghdwsbh>
		sb.append("<ghdwsbh>").append(billInfo.getCustomerTaxno()).append(
				"</ghdwsbh>");
		// <ghdwmc>购货单位名称</ghdwmc>
		sb.append("<ghdwmc>").append(billInfo.getCustomerName()).append(
				"</ghdwmc>");
		// <ghdwdzdh>购货单位地址电话</ghdwdzdh>
		sb.append("<ghdwdzdh>").append(billInfo.getCustomerAddressandphone())
				.append("</ghdwdzdh>");
		// <ghdwyhzh>购货单位银行帐号</ghdwyhzh>
		sb.append("<ghdwyhzh>").append(billInfo.getCustomerBankandaccount())
				.append("</ghdwyhzh>");
		if (billItemList != null && billItemList.size() > 0) {
			// <qdbz>清单标志</qdbz>
			if (billItemList.size() > 8) {
				sb.append("<qdbz>1</qdbz>");
			} else {
				sb.append("<qdbz>0</qdbz>");
			}
			sb.append("<fyxm count=\"").append(billItemList.size()).append(
					"\">");
			for (int i = 0; i < billItemList.size(); i++) {
				BillItemInfo billItem = (BillItemInfo) billItemList.get(i);
				if ("Y".equals(billItem.getIsMainGoods())) {
					mainGoodsName = billItem.getGoodsName();
					mainGoodsTaxItem = billItem.getTaxItem();
				}
				sb.append("<group xh=\"").append(i + 1).append("\">");
				// <fphxz>发票行性质</fphxz>
				sb.append("<fphxz>").append(billItem.getRowNature()).append(
						"</fphxz>");
				// <spmc>商品名称</spmc>
				sb.append("<spmc>").append(billItem.getGoodsName()).append(
						"</spmc>");
				// <spsm>商品税目</spsm>
				sb.append("<spsm>").append(billItem.getTaxItem()).append(
						"</spsm>");
				// <ggxh>规格型号</ggxh>
				sb.append("<ggxh>").append(billItem.getSpecandmodel()).append(
						"</ggxh>");
				// <dw>单位</dw>
				sb.append("<dw>").append(billItem.getGoodsUnit()).append(
						"</dw>");
				// <spsl>商品数量</spsl>
				sb.append("<spsl>").append(billItem.getGoodsNo().toString())
						.append("</spsl>");
				// <dj>单价</dj>
				if (billItem.getGoodsPrice() != null) {
					sb.append("<dj>").append(
							billItem.getGoodsPrice().toString())
							.append("</dj>");
				} else {
					sb.append("<dj></dj>");
				}
				// <je>金额</je>
				sb.append("<je>").append(billItem.getAmt().toString()).append(
						"</je>");
				// <sl>税率</sl>
				sb.append("<sl>").append(billItem.getTaxRate().toString())
						.append("</sl>");
				// <se>税额</se>
				sb.append("<se>").append(billItem.getTaxAmt().toString())
						.append("</se>");
				// <hsbz>含税标志</hsbz>
				sb.append("<hsbz>").append(billItem.getTaxFlag().toString())
						.append("</hsbz>");
				sb.append("</group>");
			}
			sb.append("</fyxm>");
		}
		// <bz>备注</bz>
		sb.append("<bz>").append(billInfo.getRemark()).append("</bz>");
		// <skr>收款人</skr>
		sb.append("<skr>").append(billInfo.getPayee()).append("</skr>");
		// <fhr>复核人</fhr>
		sb.append("<fhr>").append(billInfo.getReviewer()).append("</fhr>");
		// <kpr>开票人</kpr>
		sb.append("<kpr>").append(billInfo.getDrawer()).append("</kpr>");
		// <jmbbh>加密版本号</jmbbh>
		sb.append("<jmbbh>").append(jmbbh).append("</jmbbh>");
		// <zyspmc>主要商品名称</zyspmc>
		sb.append("<zyspmc>").append(mainGoodsName).append("</zyspmc>");
		// <spsm>商品税目</spsm>
		sb.append("<spsm>").append(mainGoodsTaxItem).append("</spsm>");
		// <ssyf>所属月份</ssyf>
		sb.append("<ssyf>").append(billInfo.getBillDateYM()).append("</ssyf>");
		// <kpjh>开票机号</kpjh>
		sb.append("<kpjh>").append(taxDisk.getKpjh()).append("</kpjh>");
		
		//TODO 
		if (!"18".equals(billInfo.getDataStatus())) {
			// <tzdbh>通知单编号</tzdbh>
			sb.append("<tzdbh></tzdbh>");
			// <yfpdm>原发票代码</yfpdm>
			sb.append("<yfpdm></yfpdm>");
			// <yfphm>原发票号码</yfphm>
			sb.append("<yfphm></yfphm>");
		} else {
			// <tzdbh>通知单编号</tzdbh>
			sb.append("<tzdbh>").append(billInfo.getNoticeNo()).append(
					"</tzdbh>");
			// <yfpdm>原发票代码</yfpdm>
			sb.append("<yfpdm>").append(billInfo.getOriBillCode()).append(
					"</yfpdm>");
			// <yfphm>原发票号码</yfphm>
			sb.append("<yfphm>").append(billInfo.getOriBillNo()).append(
					"</yfphm>");
		}
		// <qmcs>签名参数</qmcs>
		sb.append("<qmcs>0000004282000000</qmcs>");
		sb.append("</input>");
		sb.append("</body>");
		sb.append("</business>");
		xml = sb.toString();
		return xml;
	}

	public static String createXmlForBillPrint(BillInfo billInfo) {
		String xml = "";
		StringBuffer sb = new StringBuffer();
		List billItemList = billInfo.getBillItemList();
		sb.append("<?xml version=\"1.0\" encoding=\"gbk\"?>");
		sb.append("<InvoiceData>");
		sb.append("<body>");
		sb.append("<input>");
		// <SwiftNumber>流水号</SwiftNumber>
		sb.append("<SwiftNumber></SwiftNumber>");
		// <InvType>发票种类</InvType>
		// <!--发票种类 0-专用发票；1-普通发票-->
		sb.append("<InvType>0</InvType>");
		// <CreditNoteInv>红冲标志</CreditNoteInv>
		// <!-- 0-正常发票;1-红冲发票-->
		sb.append("<CreditNoteInv>0</CreditNoteInv>");
		// <CancelInvType>作废类型</CancelInvType>
		// <!--作废类型 0：正常发票 1 空白发票作废 2：已开发票作废-->
		sb.append("<CancelInvType>0</CancelInvType>");
		// <VenderTaxNo>销货单位识别号</VenderTaxNo>
		sb.append("<VenderTaxNo>").append(billInfo.getTaxno()).append(
				"</VenderTaxNo>");
		// <VenderName>销货单位名称</VenderName>
		sb.append("<VenderName>").append(billInfo.getName()).append(
				"</VenderName>");
		// <VenderAddressTel>销货单位地址电话</VenderAddressTel>
		sb.append("<VenderAddressTel>").append(billInfo.getAddressandphone())
				.append("</VenderAddressTel>");
		// <VenderBankNameNo>销货单位银行帐号</VenderBankNameNo>
		sb.append("<VenderBankNameNo>").append(billInfo.getBankandaccount())
				.append("</VenderBankNameNo>");
		// <CustomerTaxNo>客户税号</CustomerTaxNo>
		sb.append("<CustomerTaxNo>").append(billInfo.getCustomerTaxno())
				.append("</CustomerTaxNo>");
		// <CustomerName>客户名称</CustomerName>
		sb.append("<CustomerName>").append(billInfo.getCustomerName()).append(
				"</CustomerName>");
		// <CustomerAddressTel>客户电话地址</CustomerAddressTel>
		sb.append("<CustomerAddressTel>").append(
				billInfo.getCustomerAddressandphone()).append(
				"</CustomerAddressTel>");
		// <CustomerBankNameNo>客户银行及帐号</CustomerBankNameNo>
		sb.append("<CustomerBankNameNo>").append(
				billInfo.getCustomerBankandaccount()).append(
				"</CustomerBankNameNo>");
		// <InvoiceDetail count="2">
		sb.append("<InvoiceDetail count=\"").append(billItemList.size())
				.append("\">");
		for (int i = 0; i < billItemList.size(); i++) {
			BillItemInfo billItem = (BillItemInfo) billItemList.get(i);
			int xh = i + 1;
			// <group xh="1">
			sb.append("<group xh=\"").append(xh).append("\">");
			// <ProductName>商品名称1</ProductName>
			sb.append("<ProductName>").append(billItem.getGoodsName()).append(
					"</ProductName>");
			// <ProductSize>规格型号1</ProductSize>
			sb.append("<ProductSize>").append(billItem.getSpecandmodel())
					.append("</ProductSize>");
			// <ProductUnit>计量单位1</ProductUnit>
			sb.append("<ProductUnit>").append(billItem.getGoodsUnit()).append(
					"</ProductUnit>");
			// <ProductAmount>数量1</ProductAmount>
			sb.append("<ProductAmount>").append(billItem.getGoodsNo()).append(
					"</ProductAmount>");
			// <UnitPrice>单价1</UnitPrice>
			if (billItem.getGoodsPrice() != null) {
				sb.append("<UnitPrice>").append(billItem.getGoodsPrice())
						.append("</UnitPrice>");
			} else {
				sb.append("<UnitPrice></UnitPrice>");
			}
			// <TotalAmount>金额1</TotalAmount>
			sb.append("<TotalAmount>").append(billItem.getAmt()).append(
					"</TotalAmount>");
			// <TaxRate>税率1</TaxRate>
			sb.append("<TaxRate>").append(billItem.getTaxRate()).append(
					"</TaxRate>");
			// <TaxAmount>税额1</TaxAmount>
			sb.append("<TaxAmount>").append(billItem.getTaxAmt()).append(
					"</TaxAmount>");
			// <TaxMark>含税标志</ TaxMark >
			sb.append("<TaxMark>").append(billItem.getTaxFlag()).append(
					"</TaxMark>");
			// </group>
			sb.append("</group>");
		}
		// </InvoiceDetail>
		sb.append("</InvoiceDetail>");
		// <SumTotalAmount>合计金额</SumTotalAmount>
		sb.append("<SumTotalAmount>").append(billInfo.getAmtSum()).append(
				"</SumTotalAmount>");
		// <SumTaxAmount>合计税额</SumTaxAmount>
		sb.append("<SumTaxAmount>").append(billInfo.getTaxAmtSum()).append(
				"</SumTaxAmount>");
		// <Total>价税合计</Total>
		sb.append("<Total>").append(billInfo.getSumAmt()).append("</Total>");
		// <Remark>备注</Remark>
		sb.append("<Remark>").append(billInfo.getRemark()).append("</Remark>");
		// <Receiver>收款人</Receiver>
		sb.append("<Receiver>").append(billInfo.getPayee()).append(
				"</Receiver>");
		// <Checker>复核人</Checker>
		sb.append("<Checker>").append(billInfo.getReviewerName()).append(
				"</Checker>");
		// <Issuer>开票人</Issuer>
		sb.append("<Issuer>").append(billInfo.getDrawerName()).append(
				"</Issuer>");
		// <CancelUser>作废人</CancelUser>
		sb.append("<CancelUser>").append(billInfo.getCancelInitiator()).append(
				"</CancelUser>");
		// <Month>所属月份</Month><!--YYYYMM-->
		sb.append("<Month>").append(
				DateUtils.serverCurrentDate(DateUtils.DATE_FORMAT_YYYYMM))
				.append("</Month>");
		// <CNNoticeNo>专用发票红票通知单号</CNNoticeNo>
		sb.append("<CNNoticeNo>").append(billInfo.getNoticeNo()).append(
				"</CNNoticeNo>");
		// <CNDNCode>作废时对应的原始发票号码或红票对应正数发票代码</CNDNCode>
		sb.append("<CNDNCode>").append(billInfo.getOriBillCode()).append(
				"</CNDNCode>");
		// <CNDNNo>作废时对应的原始发票号码或红票对应正数发票号码</CNDNNo>
		sb.append("<CNDNNo>").append(billInfo.getOriBillNo()).append(
				"</CNDNNo>");
		sb.append("</input>");
		sb.append("</body>");
		sb.append("</InvoiceData>");
		xml = sb.toString();
		return xml;
	}
}
