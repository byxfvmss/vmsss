package com.cjit.vms.trans.util;


import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class BigDecimalConverter extends StrutsTypeConverter {

	public Object convertFromString(Map context, String[] values, Class toClass) {
		BigDecimal bd = null;
		if(BigDecimal.class ==toClass){
			String bdStr = values[0];
			if(bdStr!=null&&!"".equals(bdStr)){
				bd = new BigDecimal(bdStr);
			}else{
				//bd = BigDecimal.valueOf(-1);
			}
			return bd;
		}		
		return BigDecimal.ZERO;
	}

	public String convertToString(Map context, Object o) {
		
		if(o instanceof BigDecimal){
			BigDecimal b = new BigDecimal(o.toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			return b.toString();
		}		
		return o.toString();
	}

}
