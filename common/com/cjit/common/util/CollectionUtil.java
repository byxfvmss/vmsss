/************************************************************

 ***********************************************************/
package com.cjit.common.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author huboA
 * @since 2008-7-15
 * @version 1.0
 */
public class CollectionUtil extends CollectionUtils{

	/**
	 * 判断一个集合是否为NULL
	 * @param collection
	 * @return
	 */
	public static boolean isNull(Collection collection){
		return collection == null ? true : false;
	}

	/**
	 * 判断一个集合是否不为NULL
	 * @param collection
	 * @return
	 */
	public static boolean isNotNull(Collection collection){
		return !isNull(collection);
	}

	/**
	 * 判断一个集合是否为空
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection collection){
		if(isNull(collection)){
			return true;
		}
		if(collection.size() == 0){
			return true;
		}
		return false;
	}

	/**
	 * 判断一个集合是否不为空
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection collection){
		return !isEmpty(collection);
	}

	/**
	 * 根据Collection得到以主键","间隔的字符串(仅用于数字类型)
	 * @param collection
	 * @return
	 */
	public static String getNumberSQLIds(Collection collection){
		if(isNotEmpty(collection)){
			StringBuffer sb = new StringBuffer();
			Iterator it = collection.iterator();
			while(it.hasNext()){
				String id = (String) it.next();
				sb.append(id + ",");
			}
			return sb.substring(0, sb.length() - 1).toString();
		}
		return "";
	}

	/**
	 * 根据Collection得到以主键","间隔的字符串(仅用于字符串类型)
	 * @param collection
	 * @return
	 */
	public static String getStringSQLIds(Collection collection){
		if(isNotEmpty(collection)){
			StringBuffer sb = new StringBuffer();
			Iterator it = collection.iterator();
			while(it.hasNext()){
				String id = (String) it.next();
				sb.append("'" + id + "',");
			}
			return sb.substring(0, sb.length() - 1).toString();
		}
		return "";
	}
}
