/**
 * 
 */
package com.cjit.gjsz.datadeal.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cjit.bop.xml2txt.FtpFileImport;
import com.cjit.common.action.BaseAction;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.interfacemanager.service.impl.FmssDataServiceImpl;

/**
 * @author luxiangjiu XML报文文件转换 手动运行跑批
 */
public class DataTranAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private FtpFileImport xml2txtSyc;

	public String init(){
		String action = request.getParameter("action");
		if(action != null && action.equals("viewLog")){
			String path = "";
			try{
				path = new String(request.getParameter("path").getBytes(
						"ISO-8859-1"));
				path = java.net.URLDecoder.decode(path, "utf-8 ");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			response.setContentType("text/plain;charset=GB18030");
			try{
				File fff = new File(xml2txtSyc.getLogpath() + path);
				String s = FileUtils.readFileToString(fff);
				fff = null;
				response.getWriter().write(s);
			}catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}else if(action != null && action.equals("execute")){
			FmssDataServiceImpl fmssDataService = (FmssDataServiceImpl) SpringContextUtil
					.getApplicationContext().getBean("fmssDataService");
			String currentDate = DateUtils
					.serverCurrentDate(DateUtils.ORA_DATES_FORMAT);
			boolean isHoliday = fmssDataService.isHoliday(currentDate);
			if(isHoliday){
				this.addFieldToRequest("msg", "当前日期为节假日,无需进行转换");
			}else{
				Runnable t = new Runnable(){

					public void run(){
						xml2txtSyc.executeByHand();
					}
				};
				Thread th = new Thread(t);
				th.start();
				this
						.addFieldToRequest("msg",
								"手动执行已经提交到后台,要了解执行详细情况,请点击最新日志查看");
			}
		}
		request.setAttribute("logs", getLogFileList());
		return SUCCESS;
	}

	private List getLogFileList(){
		File fs = new File(xml2txtSyc.getLogpath());
		if(!fs.exists() || fs.isFile()){
			return new ArrayList();
		}
		String[] names = fs.list();
		// File[] fff=fs.listFiles(new FilenameFilter(){
		// public boolean accept(File dir, String name) {
		// return name.startsWith(FtpFileImport.LOGFILENAME);
		// }
		// }
		// );
		//		
		List aaa = new ArrayList();
		for(int i = 0; i < names.length; i++){
			if(names[i].startsWith(FtpFileImport.LOGFILENAME)){
				Map fm = new HashMap();
				fm.put("filename", names[i]);
				fm.put("lastModified", "");// (new SimpleDateFormat("yyyy-MM-dd
				// hh:mm:ss")).format(new Date(
				// fff[i].lastModified())));
				aaa.add(fm);
				// fff[i]=null;
			}
		}
		return aaa;
	}

	public FtpFileImport getXml2txtSyc(){
		return xml2txtSyc;
	}

	public void setXml2txtSyc(FtpFileImport xml2txtSyc){
		this.xml2txtSyc = xml2txtSyc;
	}
}
