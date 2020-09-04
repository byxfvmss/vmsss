package com.cjit.gjsz.common.homenote.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.util.FileCopyUtils;

import com.cjit.common.constant.Constants;

public class HomeDataXmlSerial {

	private static final Logger logger = Logger
			.getLogger(HomeDataXmlSerial.class);

	private String systemId = "";

	public HomeDataXmlSerial(String systemId) {
		this.systemId = systemId;
	}

	public Document buildDocument(HomeDataDO homeDataDO) {
		Document document = DocumentHelper.createDocument();
		// 生成root的一个接点
		Element rootNode = document.addElement("root");
		if (homeDataDO != null) {
			// 添加result节点
			Element resultNode = rootNode.addElement("result");
			// 添加属性
			resultNode.addAttribute("code", homeDataDO.getResultCode());
			resultNode.addAttribute("userid", homeDataDO.getResultUserId());
			// 添加label节点
			Element labelNode = resultNode.addElement("label");
			List items4Label = homeDataDO.getLabel();
			// 添加item子节点
			if (items4Label != null) {
				for (int i = 0; i < items4Label.size(); i++) {
					HomeDataItemDO item = (HomeDataItemDO) items4Label.get(i);
					Element itemNode = labelNode.addElement("item");
					itemNode.addAttribute("name", item.getName());
					itemNode.addAttribute("value", item.getValue());
					itemNode.addAttribute("url", item.getUrl());
					itemNode.addAttribute("sysid", this.systemId);
					itemNode.addAttribute("menuid", item.getMenuId());
				}
			}
			// 添加table节点
			Element tableNode = resultNode.addElement("table");
			// 添加table节点属性
			tableNode.addAttribute("url", homeDataDO.getTableUrl());
			// 添加thead子节点
			Element theadNode = tableNode.addElement("thead");
			List cells4Thead = homeDataDO.getThead();
			for (int cellN = 0; cellN < cells4Thead.size(); cellN++) {
				HomeDataCellDO cell = (HomeDataCellDO) cells4Thead.get(cellN);
				// 添加cell子节点
				Element cellNode = theadNode.addElement("cell");
				if (cell.getName() != null)
					cellNode.addAttribute("name", cell.getName());
			}
			Element tbodyNode = tableNode.addElement("tbody");
			List rows4Tbody = homeDataDO.getTbody();
			for (int rowN = 0; rowN < rows4Tbody.size(); rowN++) {
				// 添加row子节点
				Element rowNode = tbodyNode.addElement("row");
				List cells4Row = (List) rows4Tbody.get(rowN);
				for (int colN = 0; colN < cells4Row.size(); colN++) {
					HomeDataCellDO cell = (HomeDataCellDO) cells4Row.get(colN);
					// 添加cell子节点
					Element cellNode = rowNode.addElement("cell");
					if (cell.getValue() != null)
						cellNode.addAttribute("value", cell.getValue());
					if (cell.getTarget() != null)
						cellNode.addAttribute("target", cell.getTarget());
					if (cell.getKey() != null)
						cellNode.addAttribute("key", cell.getKey());
					if (cell.getUrl() != null)
						cellNode.addAttribute("url", cell.getUrl());
					cellNode.addAttribute("sysid", this.systemId);
					cellNode.addAttribute("menuid", Constants.MENU_ID);
				}
			}
			String itemComment2 = "name：用于显示；value：当前待处理项目的数量；url：点击数据时目标列表页面链接；此处数据项条数和内容由各子系统根据系统情况自定义，数据格式不变";
			writeComment4Element(labelNode, itemComment2);
			String tableComment = "列表结果集 url：点击项目时转到处理页面的链接地址（此处为默认链接，可在列表项单独设置，为空时列表项必须进行设置）；";
			writeComment4Element(tableNode, tableComment);
			String theadComment = "列表头信息，指明列表列数及列头显示信息";
			writeComment4Element(theadNode, theadComment);
			String resultComment = "注：code：系统编号，用于校验是否与请求的子系统对应；userid：；列表信息（表头信息及列表数据）可由各子系统自行定义，列数及展示数据根据各子系统情况设置，但数据格式不变；需要添加链接的字段可加入“url”属性，“target”设置为“true”，如果未设置“url”属性则使用默认链接；";
			writeComment4Element(resultNode, resultComment);
		}
		return document;
	}

	private void writeComment4Element(Element element, String comment) {
		if (comment != null && comment.trim().length() > 0) {
			element.addComment(comment);
		}
	}

	/**
	 * //输出到文件
	 * 
	 * @param document
	 * @param outFile
	 */
	public void write2File(HttpServletResponse response, InputStream is,
			String filename) {
		response.addHeader("Content-Disposition", "attachment;filename="
				+ filename + ".xml");
		response.setContentType("text/plain");
		try {
			FileCopyUtils.copy(is, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出到InputStream
	 * 
	 * @param document
	 * @return
	 */
	public InputStream write2InputStream(HomeDataDO homeDataDO, String encoding) {
		InputStream is = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Document temp = buildDocument(homeDataDO);
			OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
			xmlFormat.setEncoding(encoding);
			XMLWriter xmlWriter = new XMLWriter(os, xmlFormat);
			xmlWriter.write(temp);
			is = new ByteArrayInputStream(os.toByteArray());
			// 关闭
			if (logger.isInfoEnabled()) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				OutputFormat xf = OutputFormat.createPrettyPrint();
				xf.setEncoding("GBK");
				new XMLWriter(byteArrayOutputStream, xf).write(temp);
				logger.info(byteArrayOutputStream.toString());
			}
			xmlWriter.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 输出到InputStream
	 * 
	 * @param document
	 * @return
	 */
	public InputStream write2InputStream(HomeDataDO homeDataDO) {
		InputStream is = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Document temp = buildDocument(homeDataDO);
			// 设置文件编码
			OutputFormat xmlFormat = new OutputFormat();
			xmlFormat.setEncoding("UTF-8");
			XMLWriter xmlWriter = new XMLWriter(os, xmlFormat);
			xmlWriter.write(temp);
			is = new ByteArrayInputStream(os.toByteArray());
			// 关闭
			xmlWriter.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
}
