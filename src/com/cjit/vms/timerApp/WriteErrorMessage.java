package com.cjit.vms.timerApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

//管浩：文件解析——error.txt生成文档
public class WriteErrorMessage{
	public void WriteError(File fileerror,String errormessage){
		
		BufferedWriter bw = null;
		try {bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileerror,true),"UTF-8"));} 
		catch (FileNotFoundException e2) {e2.printStackTrace();}
		catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
		
		if(!fileerror.exists()){
			
			try {fileerror.createNewFile();} catch (IOException e1) {e1.printStackTrace();}
			try {bw.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+errormessage);} catch (IOException e1) {e1.printStackTrace();}
			finally{try {bw.close();} catch (IOException e1) {e1.printStackTrace();}}
			
		}else{try {bw.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+errormessage);} catch (IOException e1) {e1.printStackTrace();}
		finally{try {bw.close();} catch (IOException e1) {e1.printStackTrace();}}}
		
	}
	
public static void main(String[] args) {
	//解析文件路径
			/*final String path = "C://Users//Administrator//Desktop//进项抵扣//";*/
	        final String path = "D://文件解析//";
			//执行日期
			String filedate = "2016-06-06 16:16:00";
			//表名
			String tablename = "tablename";

			String filename="";
			
			if(filedate.contains("-")){
				filename = filedate.replace("-", "");
				filename = filename.substring(0,8);
				System.out.println(filename);
			}
			
			
			//文件名称  不带扩展名
			filename = "VMS"+filename+tablename;
			String filenameok = filename+"OK";
			String filenameerror = filename+"ERROR";
			File datafile = new File(path+filename+".txt");
			File datafileok= new File(path+filenameok+".txt");
			File datafileerror= new File(path+filenameerror+".txt");
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(datafile)));
			BufferedWriter bw = null;
			
			System.out.println(datafile.exists()&&datafileok.exists());
			System.out.println(datafile.getName());
			
			if(datafile.exists()&&datafileok.exists()){
				try {
					System.out.println(datafile);
					System.out.println(1/0);
				} catch (Exception e) {
					
					try {bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datafileerror,true),"UTF-8"));} 
					catch (FileNotFoundException e2) {e2.printStackTrace();}
					catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
					if(!datafileerror.exists()){
						
						try {datafileerror.createNewFile();} catch (IOException e1) {e1.printStackTrace();}
						try {bw.write(e.toString());} catch (IOException e1) {e1.printStackTrace();}
						finally{try {bw.close();} catch (IOException e1) {e1.printStackTrace();}}
						
					}else{try {bw.write(e.toString()+"\n");} catch (IOException e1) {e1.printStackTrace();}
					finally{try {bw.close();} catch (IOException e1) {e1.printStackTrace();}}}
					e.printStackTrace();
				}
			}
		}
}

