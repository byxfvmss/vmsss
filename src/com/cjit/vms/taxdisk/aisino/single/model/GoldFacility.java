package com.cjit.vms.taxdisk.aisino.single.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ClassName: GoldFacilityReq 
 * @Description: 金税设备查询
 * @author Napoléon 
 * @date 2016-4-13
 */
public class GoldFacility {
	
	private String ID;

	private String DATA;
	
	private static final String sidparam_ch="SIDPARAM";
	private static final String id_ch="ID";
	private static final String data_ch="DATA";
	private static final String fpxt_com_input_ch="FPXT_COM_INPUT";
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDATA() {
		return DATA;
	}

	public void setDATA(String dATA) {
		DATA = dATA;
	}

	/**
	 * @Description: 生成xml报文字符串
	 * @param @return
	 * @param @throws Exception   
	 * @return String  
	 * @throws
	 * @author Napoléon 
	 * @date 2016-4-13
	 */
	public String createSetParametersXml() throws Exception{
		Element input=new Element(fpxt_com_input_ch);
		Document Doc = new Document(input);
		String[] label={id_ch, data_ch};
		String[] value={ID,DATA};
		for(int i=0;i<label.length;i++){
			addChildElementText(input, label[i], value[i]);
		}
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setEncoding("GBK"));
		String outString = xmlOut.outputString(Doc);
		System.out.println(outString);
		return outString;
	}
	
	/**
	 * @Description: 创建json字符串
	 * @param @return
	 * @param @throws Exception   
	 * @return Object  
	 * @throws
	 * @author Napoléon 
	 * @date 2016-4-13
	 */
	public Object createSetParametersJSon() throws Exception{
		GoldFacility SetParameters=new GoldFacility();
		JSONObject jsonObject = new JSONObject();  
		jsonObject.put(sidparam_ch, SetParameters.createSetParametersXml());
		System.out.println(jsonObject);
		return jsonObject;
	}

	/**
	 * @param elements 元素
	 * @param label 标签 
	 * @param value text值 添加子元素
	 */
	public void addChildElementText(Element elements,String label,String value){
		elements.addContent(new Element(label).setText(value));
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) throws Exception {
		GoldFacility SetParameters=new GoldFacility();
		SetParameters.setID("0400");
		SetParameters.setDATA("");
//		System.out.println(SetParameters.createSetParametersXml());
		JSONObject jsonObject = new JSONObject();  
		jsonObject.put("param", SetParameters.createSetParametersXml());
//		System.out.println(jsonObject);
		System.out.println(JSON.toJSONString(jsonObject));
	}
	
	
	
}



