package cjit.crms.util;

import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;

public class BeanUtilsVMS extends BeanUtils {
	private static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal("0");    
	static {    
	   // 这里一定要注册默认值，使用null也可以    
	    BigDecimalConverter bd = new BigDecimalConverter(BIGDECIMAL_ZERO);    
	    ConvertUtils.register(bd, java.math.BigDecimal.class);    
	}   

}
