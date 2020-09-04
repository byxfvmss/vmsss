package com.cjit.vms.aisino.util;

import java.util.Stack;

/**
 * 用StringBuffer生成Xml请求的方法
 * 
 * @author weishuang
 * 
 */
public class WebXmlBase {

	public final static String CTLN = "\r\n";
	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public final static String TAG_START = "<";
	public final static String TAG_END = ">";

	public final static String TAG_END2 = "</";

	/**
	 * 堆栈
	 */
	private Stack<String> stack = null;
	private StringBuilder sb = null;

	/**
	 * 根标签自带一个换行
	 * 
	 * @param rootTag
	 */
	public WebXmlBase(String rootTag) {
		this.stack = new Stack<String>();
		this.sb = new StringBuilder();
		this.sb.append(XML_HEAD);
		this.newLine();
		this.startTag(rootTag);
		this.newLine();
	}

	/**
	 * 开始一个标签
	 * 
	 * @param tag
	 */
	public void startTag(String tag) {
		stack.push(tag);
		this.sb.append(TAG_START);
		this.sb.append(tag);
		this.sb.append(TAG_END);
	}

	public void startTagWithNewLine(String tag) {
		stack.push(tag);
		this.sb.append(TAG_START);
		this.sb.append(tag);
		this.sb.append(TAG_END);
		this.newLine();
	}

	public void setAttribute(String name, String value) {
		if (!isFinished()) {
			String parsedValue = WebXmlFunc.escapeXml(value);
			int start = this.sb.lastIndexOf(TAG_END);
			this.sb.insert(start, " ")
					.insert(start + 1, name)
					.insert(start + 1 + name.length(), "=\"")
					.insert(start + 1 + name.length() + 2, parsedValue)
					.insert(start + 1 + name.length() + 2
							+ parsedValue.length(), "\"");
		}
	}

	public void newLine() {
		this.sb.append(CTLN);
	}

	/**
	 * finish标签不带换行
	 */
	public void finish() {
		if (!isFinished()) {
			this.writeEnd();
		}
	}

	private boolean isFinished() {
		return stack.size() == 0;
	}

	private void writeEnd() {
		String tag = stack.pop();
		this.sb.append(TAG_END2);
		this.sb.append(tag);
		this.sb.append(TAG_END);
	}

	/**
	 * 关闭一个标签,每个关闭标签自带一个换行
	 */
	public void endTag() {
		if (!this.isFinished()) {
			int size = stack.size();
			if (size > 1) {
				this.writeEnd();
				this.sb.append(CTLN);
			} else if (size == 1) {
				this.finish();
			}
		}
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 */
	public void setTagValue(String value) {
		if (isFinished())
			this.sb.append(WebXmlFunc.escapeXml(value));
	}

	/**
	 * 转成
	 */
	public String toString() {
		return this.sb.toString();
	}

	public static void main(String args[]) {
		WebXmlBase base = new WebXmlBase("service");
		base.startTag("sid");
		base.setTagValue("4");
		base.endTag();
		base.startTag("ip");
		base.endTag();
		base.startTag("port");
		base.endTag();
		base.startTagWithNewLine("data");
		base.setAttribute("count", "2");
		base.startTagWithNewLine("record");
		base.startTag("FPZL");
		base.endTag();
		base.startTag("FPHM");
		base.endTag();
		base.startTag("FPDM");
		base.endTag();
		base.endTag();
		base.endTag();
		base.finish();
		System.out.println(base.toString());
	}

}
