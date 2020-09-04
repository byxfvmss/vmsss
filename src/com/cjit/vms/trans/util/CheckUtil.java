package com.cjit.vms.trans.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cjit.common.util.DateUtils;
import com.cjit.gjsz.interfacemanager.model.Dictionary;

import cjit.crms.util.StringUtil;
public class CheckUtil {
	/**
	 * @param date
	 * @return 验证日期格式
	 */
	public static boolean checkDate(String date) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); 
	    try {
	    	if(date.equals(sdf.format(sdf.parse(date)))){
	    		return true;
	    	}else{
	    		return false;
	    	}
	 } catch (java.text.ParseException e) {
		
	  return false;
	 } 
	}
	/**
	 * @param map 需要验证的方法的返回值 boolean 型的
	 * @param i   第几行
	 * @param result  返回的提示信息 初始值为空  验证通过 为空
	 * @param length  list 循环的长度
	 * @return  
	 * 
	 * 括号内 为验证代码 块 验证只要增加它就可以     
	 * 
	 * warning  多个验证 要加个非空判断
	 */
	public static String checkData(Map<?, Boolean> map, int i,String result,int length) {
		boolean current=true;
		Boolean flag=true;
		{
		 flag=(Boolean) map.get("checkDate")==null?true:(Boolean) map.get("checkDate");
		 current=flag==false?false:current;
		}
		{
			flag=(Boolean) map.get("checkNull")==null?true:(Boolean) map.get("checkNull");
			current=flag==false?false:current;
		}
		
		{
			flag=(Boolean) map.get("checkNotNull")==null?true:(Boolean) map.get("checkNotNull");
			current=flag==false?false:current;
		}
		
		result=current==false?result+Integer.toString(i)+",":result;
		return i+1==length&&result.length()>0?"第"+result.substring(0,result.length()-1)+"行信息有误请检查!":result;
	}
	public static boolean checkNotNull(Map<String,String> map){
		boolean falg=true;
		Iterator<String> it=map.keySet().iterator();    
		while(it.hasNext()){    
		     String key;    
		     String value;    
		     key=it.next().toString();    
		     value=map.get(key);    
		     if(StringUtil.IsEmptyStr(value)){
		    	 falg=false;
		    	 break;
		     }
		    // System.out.println(key+"--"+value);    
		}
		return falg;
		
	}
	public static boolean checkNotNull(List<String> list){
		boolean flag=true;
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				if(StringUtil.IsEmptyStr(list.get(i))){
					flag=false;
					break;
				}
			}
			
		}
		return flag;
		
	}
	public static void main(String[] args) throws ParseException {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			System.out.println(sdf.parse("2015-12-12 12:09:12"));

	}
	
	public static Map<String, String> CreatMap(String[]heads,List<Dictionary> headList,String[] row){
		Map<String, String> map = new HashMap<String, String>();
		for (int j = 0; j < heads.length; j++) {
			String columnName = heads[j];
			System.out.println(heads[j]);
			for (int k = 0; k < headList.size(); k++) {
				Dictionary head = headList.get(k);
				if (null != columnName&& columnName.equals(head.getTypeName())) {
					columnName = head.getName();
					System.out.println(columnName);
					continue;
				}
			}
			String value = row[j]==null?"":row[j];
			if (null != columnName && null != value) {
				map.put(columnName, value);
			}
	}
		return map;
	}
	public static Set<String> checkId(Map<String,Boolean> mapa,String id,Set<String> set){
			if(mapa.get(id)==null?false:mapa.get(id)){
				set.add(id);
		}
		return set;
	}
	
	public static Set<String> checkIdNo(Map<String,Boolean> mapa,String id,String no,Set<String> set){
		if(mapa.get(id)==null?false:mapa.get(id)){
			set.add(id);
			set.add(no);
	}
	return set;
}
	public static String checkId(Set<String> set,String id){
		String result="";
		Object[] a = (Object[]) set.toArray();
	if(a.length>0){
		for(int i=0;i<a.length;i++){
			if(i<6){
				result+=a[i]+"|";
			}else{
				result=result.substring(0,result.length()-1)+"........!.";
				break;
			}
		}
		}
		return result.length()>0?id+result.substring(0,result.length()-1)+"重复!":result;
	}
	public static String checkCId(Set<String> set,String id){
		String result="";
		Object[] a = (Object[]) set.toArray();
		if(a.length>0){
			for(int i=0;i<a.length;i++){
				if(i<6){
					result+=a[i]+"|";
				}else{
					result=result.substring(0,result.length()-1)+"........!.";
					break;
				}
			}
		}
		return result.length()>0?id+result.substring(0,result.length()-1)+"不存在!":result;
	}
	public static String checkSysId(List<String> list,String id){
		String result="";
		if(list.size()>0){
			for (int i=0;i<list.size();i++){
				if(i<6){
					result+=list.get(i)+"|";
				}else{
					result=result.substring(0,result.length()-1)+"........已存在!";
					break;
				}
			}
		}
		return result.length()>0?id+result.substring(0,result.length()-1)+"已存在!":result;
	}
	
}
