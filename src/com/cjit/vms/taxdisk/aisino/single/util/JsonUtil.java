package com.cjit.vms.taxdisk.aisino.single.util;

import com.alibaba.fastjson.JSON;

/**
 * 对象和json互相转换工具类
 * @author john
 *
 */
public class JsonUtil {

	/**
	 * 对象转json字符串
	 * @param bean
	 * @return
	 */
	public static String toJson(Object bean) {
		return JSON.toJSONString(bean);
	}
	
	/**
	 * 将json字符串转换成对象
	 * @param json 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String json,Class clazz){
		return (T) JSON.parseObject(json,clazz);
	}
}
