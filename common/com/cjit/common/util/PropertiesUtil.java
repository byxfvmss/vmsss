package com.cjit.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class PropertiesUtil{

	// 读properties方法
	public static String readProperties(String filePath, String parameterName){
		String value = "";
		InputStream fis = null;
		Properties prop = new Properties();
		try{
			fis = new FileInputStream(filePath);
			prop.load(fis);
			value = prop.getProperty(parameterName);
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(fis != null){
				try{
					fis.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	/**
	 * 写properties
	 * @param title parameter defined in properties file
	 * @param key parameter defined title value
	 * @return String return value
	 */
	public static boolean writeProp(String filePath, String fileName,
			String title, String key, String title2, String key2){
		String pathAddFile = ""; // write file with path and name
		String tempFile = "";
		String strTemp = ""; // use for identify if the modify is success
		// filePath is null the file in the default path ,else file in the
		// filePath+\+fileName
		if(filePath.equals("")){
			pathAddFile = fileName;
			tempFile = "temp.properties";
		}else{
			pathAddFile = filePath + File.separator + fileName;
			tempFile = filePath + File.separator + "temp.properties";
		}
		// properties file
		File aFile = new File(pathAddFile);
		// temp file
		File tFile = new File(tempFile);
		if(!aFile.exists()){
			return false;
		}
		// set property to properties
		try{
			FileReader fr = new FileReader(pathAddFile);
			BufferedReader br = new BufferedReader(fr);
			try{
				FileWriter fw = new FileWriter(tempFile);
				PrintWriter out = new PrintWriter(fw);
				String strLine = br.readLine().trim();
				while(strLine != null){
					// identify if strLine have title,have change key
					if(strLine.startsWith(title)){
						strLine = title + "=" + key;
						strTemp = "1";
					}else if((title2 != null) && !(title2.equals(""))){
						if(strLine.startsWith(title2)){
							strLine = title2 + "=" + key2;
							strTemp = "1";
						}
					}
					out.write(strLine);
					out.println();
					out.flush();
					// read next line
					strLine = br.readLine();
				}
				fw.close();
				out.close();
				// close BufferedReader object
				br.close();
				// close file
				fr.close();
				// delete properties file
				if(aFile.exists()){
					if(!aFile.delete()){
						return false;
					}
				}
				// rename temp file to properties file
				if(!tFile.exists()){
					return false;
				}
				tFile.renameTo(aFile);
				if(!strTemp.equals("1")){
					// there is no title prop exit so modify failed
					return false;
				}
				return true;
			}catch (IOException ex2){
				ex2.printStackTrace();
				return false;
			}
		}catch (FileNotFoundException ex1){
			ex1.printStackTrace();
			return false;
		}
	}

	public static void main(String[] arg){
		// String
		// a=PropertiesUtil.readProperties("D:\\EclipseWorkspace\\gjsz_bop\\common\\config\\jdbc\\jdbc.properties",
		// "dataPath");
		// System.out.println(a);
		// PropertiesUtil.writeProp("D:\\EclipseWorkspace\\gjsz_bop\\common\\config\\jdbc",
		// "jdbc.properties", "dataPath", "b\\a");
	}
}
