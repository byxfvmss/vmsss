package com.cjit.gjsz.system.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.cjit.gjsz.common.action.BaseListAction;
import com.cjit.gjsz.system.service.RptTemplateService;

public class RptTemplateAction extends BaseListAction{
	private static final long serialVersionUID = 1L;
	private String message;
	private List rptTemplateList;
	private RptTemplateService rptTemplateService;
	
	public String search(){
		rptTemplateList=getRptTemplateService().findRptTemplate();
		return SUCCESS;
	}

	public String update(){
//		if(!validateConfig())
//			return SUCCESS;
		String t_id=(String)request.getParameter("template_id");
		MultiPartRequestWrapper mRequest=(MultiPartRequestWrapper)request;  
		File[] files=mRequest.getFiles("template_file");
		if(files!=null && files.length>0)
		{
			InputStream in=null;
			try {
				in = new FileInputStream(files[0]);
				ByteArrayOutputStream fileByte =new ByteArrayOutputStream();
				byte[] bufferByte=new byte[1024];
				while(in.read(bufferByte)>0){
					fileByte.write(bufferByte);
				}
				getRptTemplateService().saveRptTemplate(t_id, null, fileByte.toByteArray());
				this.message="模板更新完成!";
			} catch (Exception e) {
					try {
						in.close();
					} catch (IOException e1) {
					}
				log.error("读取文件失败",e);
				this.message="模板更新失败!";
			}
		}
		return search();
	}
	
	public void export()
	{
		OutputStream out = null;
		try{
			out = response.getOutputStream();
			String t_id=(String)request.getParameter("export_id");
			Map map=getRptTemplateService().findRptTemplate(t_id,null);
			oracle.sql.BLOB blob = (oracle.sql.BLOB)map.get("TEMPLATE_CONTENT");
			InputStream is =blob.getBinaryStream();
			HSSFWorkbook workbook = new HSSFWorkbook(is); 
			String fileName =(String)map.get("TEMPLATE_NAME");
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName + ".xls");
			workbook.write(out);
			out.flush();
		}
		catch(Exception e){
			log.error(e);
			try{
				if(out != null)
					out.close();
			}catch (IOException e1){
			}
		}
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public List getRptTemplateList() {
		return rptTemplateList;
	}

	public void setRptTemplateList(List rptTemplateList) {
		this.rptTemplateList = rptTemplateList;
	}

	public RptTemplateService getRptTemplateService() {
		return rptTemplateService;
	}

	public void setRptTemplateService(RptTemplateService rptTemplateService) {
		this.rptTemplateService = rptTemplateService;
	}

}
