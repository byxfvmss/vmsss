package com.cjit.vms.trans.util;

import java.util.List;

public class SqlUtil {
	public static String arr2String(Object[] arr,String s){
		if(arr==null){
			return "";
		}
		String retStr="";
		for(int i=0;i<arr.length;i++){
			if((i+1)!=arr.length){
				retStr+="'"+arr[i]+"'"+s;
			}else{
				retStr+="'"+arr[i]+"'";
			}
		}
		return retStr;
	}

	public static String arr2StringPlain(Object[] arr,String s){
		if(arr==null){
			return "";
		}
		String retStr="";
		for(int i=0;i<arr.length;i++){
			if((i+1)!=arr.length){
				retStr+=arr[i]+s;
			}else{
				retStr+=arr[i];
			}
		}
		return retStr;
	}

	public static String list2String(List lst,String s){
		if(lst==null){
			return "";
		}
		String retStr="";
		for(int i=0;i<lst.size();i++){
			if((i+1)!=lst.size()){
				retStr+=lst.get(i)+s;
			}else{
				retStr+=lst.get(i); 
			}
		}
		return retStr;
	}
}
