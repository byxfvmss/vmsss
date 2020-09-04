package com.cjit.vms.taxdisk.aisino.single.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ClassName: SetParameters 
 * @Description: 设置打印参数
 * @author Napoléon 
 * @date 2016-4-13
 */
public class SetParameters {
	
	private String ID;

	private String DATA;
	
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
	 * @Description: 创建xml报文字符串
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
	 * @return String  
	 * @throws
	 * @author Napoléon 
	 * @date 2016-4-13
	 */
	public String createSetParametersJSon() throws Exception{
		SetParameters SetParameters=new SetParameters();
		JSONObject jsonObject = new JSONObject();  
		jsonObject.put("param", SetParameters.createSetParametersXml());
		System.out.println(jsonObject);
		return JSON.toJSONString(jsonObject);
	}

	/**
	 * @param elements 元素
	 * @param label 标签 
	 * @param value text值 添加子元素
	 */
	public void addChildElementText(Element elements,String label,String value){
		elements.addContent(new Element(label).setText(value));
	}
	
	public static void main(String[] args) throws Exception {
		SetParameters SetParameters=new SetParameters();
		SetParameters.setID("0600");
		SetParameters.setDATA("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8+DQo8RlBYRj4NCjxEQVRBPg0KPERZSk1DPrLiytQ8L0RZSk1DPg0KPFFERFlGUz4xPC9RRERZRlM+DQo8TEVGVD41PC9MRUZUPg0KPFRPUD4zPC9UT1A+DQo8L0RBVEE+DQo8L0ZQWEY+");
		System.out.println(SetParameters.createSetParametersXml());
		JSONObject jsonObject = new JSONObject();  
		jsonObject.put("param", "");
		System.out.println(jsonObject);
		System.out.println(JSON.toJSONString(jsonObject));
		;
	}
	
}



