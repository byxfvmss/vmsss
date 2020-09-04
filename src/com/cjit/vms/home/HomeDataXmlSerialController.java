package com.cjit.vms.home;

import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cjit.gjsz.common.homenote.xml.HomeDataDO;
import com.cjit.gjsz.common.homenote.xml.HomeDataXmlSerial;

import cjit.fmss.vms.Version;

public class HomeDataXmlSerialController extends AbstractController{
	
	private Map homeDataBuilderFactory;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		if("getVersion".equalsIgnoreCase(request.getParameter("type"))){
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(Version.getVersion());
		}else{
			//因usys模块开发双语功能占用了ename字段，从今日起该参数的内容由ename字段改为systemid
			//modify by yuanshihong 20110615
			String sysName = request.getParameter("systemId");
			if(StringUtils.isEmpty(sysName)){
				throw new NullArgumentException("systemId");
			}

			IBuildHomeData builder = (IBuildHomeData) homeDataBuilderFactory.get(sysName);
			if(null == builder){
				throw new InvalidConfigurationException("缺少子系统[" + sysName + "]的首页数据构建类");
			}

			HomeDataDO homeDataDO = builder.buildHomeData(request);
			HomeDataXmlSerial serial = new HomeDataXmlSerial(sysName);

			// 输出到输入流
			InputStream is = serial.write2InputStream(homeDataDO);
			// 输出到文件
			serial.write2File(response, is, "homeData");
		}

		return null;

	}
	
	public void setHomeDataBuilderFactory(Map homeDataBuilderFactory){
		this.homeDataBuilderFactory = homeDataBuilderFactory;
	}

}
