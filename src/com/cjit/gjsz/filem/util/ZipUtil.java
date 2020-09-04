package com.cjit.gjsz.filem.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * @author huangjiong
 */
public class ZipUtil{

	private static List list = new ArrayList();
	private static String TOKEN_LOCK_FILE_NAME = "Token.lock";

	/**
	 * describe <p> 生成zip文件
	 * @param zipFile：生成zip的文件
	 * @param filepath：将要打包的目录
	 * @throws IOException
	 * @author HJ
	 * @since 2007-12-27
	 */
	public static void doZipFile(String zipFile, String filepath)
			throws IOException{
		try{
			byte b[] = new byte[512];
			// 压缩文件的保存路径
			// String zipFile = "D:/test/test.zip";
			// 压缩文件目录
			// String filepath = "D:/test/";
			List fileList = allFile(filepath);
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			// 使用输出流检查
			CheckedOutputStream cs = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			// 声明输出zip流
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					cs));
			for(int i = 0; i < fileList.size(); i++){
				InputStream in = new FileInputStream((String) fileList.get(i));
				String fileName = ((String) (fileList.get(i))).replace(
						File.separatorChar, '/');
				// System.out.println("ziping " + fileName);
				fileName = fileName.substring(fileName.indexOf("/") + 1);
				ZipEntry e = new ZipEntry(fileName);
				out.putNextEntry(e);
				int len = 0;
				while((len = in.read(b)) != -1){
					out.write(b, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * describe <p>
	 * @param path
	 * @return
	 * @author HJ
	 * @since 2007-12-27
	 */
	private static List allFile(String path){
		File file = new File(path);
		String[] array = null;
		String sTemp = "";
		if(file.isDirectory()){
		}else{
			return null;
		}
		array = file.list();
		if(array.length > 0){
			for(int i = 0; i < array.length; i++){
				sTemp = path + array[i];
				file = new File(sTemp);
				if(file.isDirectory()){
					allFile(sTemp + "/");
				}else{
					list.add(sTemp);
				}
			}
		}else{
			return null;
		}
		return list;
	}

	public void zip(String inputFileName, String outFileName){
		String zipFileName = outFileName; // 打包后文件名字
		System.out.println(zipFileName);
		zip(zipFileName, new File(inputFileName));
	}

	private void zip(String zipFileName, File inputFile){
		ZipOutputStream out = null;
		try{
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zip(out, inputFile, "");
			System.out.println("zip done");
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}finally{
			try{
				if(out != null)
					out.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private void zip(ZipOutputStream out, File f, String base){
		if(f.isDirectory()){
			File[] fl = f.listFiles();
			try{
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			}catch (IOException e){
				e.printStackTrace();
			}
			base = base.length() == 0 ? "" : base + "/";
			for(int i = 0; i < fl.length; i++){
				if(TOKEN_LOCK_FILE_NAME.equalsIgnoreCase(fl[i].getName())){
					continue;
				}
				zip(out, fl[i], base + fl[i].getName());
			}
		}else{
			try{
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			}catch (IOException e1){
				e1.printStackTrace();
			}
			FileInputStream in = null;
			try{
				in = new FileInputStream(f);
				int b;
				System.out.println(base);
				while((b = in.read()) != -1){
					out.write(b);
				}
			}catch (FileNotFoundException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(in != null)
						in.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public void zipDir(String inputFileName, String ditName, String outFileName){
		String zipFileName = outFileName; // 打包后文件名字
		System.out.println(zipFileName);
		zipDir(ditName, zipFileName, new File(inputFileName));
	}

	private void zipDir(String dirName, String zipFileName, File inputFile){
		ZipOutputStream out = null;
		try{
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zipDir(dirName, out, inputFile, "");
			System.out.println("zip done");
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}finally{
			try{
				if(out != null)
					out.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private void zipDir(String dirName, ZipOutputStream out, File f, String base){
		if(f.isDirectory()){
			File[] fl = f.listFiles();
			if(dirName.equals(f.getName())){
				try{
					out.putNextEntry(new org.apache.tools.zip.ZipEntry(base
							+ "/"));
				}catch (IOException e){
					e.printStackTrace();
				}
			}
			base = base.length() == 0 ? "" : base + "/";
			for(int i = 0; i < fl.length; i++){
				zipDir(dirName, out, fl[i], base + fl[i].getName());
			}
		}else{
			if(dirName.equals(f.getParentFile().getName())){
				try{
					out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
				}catch (IOException e1){
					e1.printStackTrace();
				}
				FileInputStream in = null;
				try{
					in = new FileInputStream(f);
					int b;
					System.out.println(base);
					while((b = in.read()) != -1){
						out.write(b);
					}
				}catch (FileNotFoundException e){
					e.printStackTrace();
				}catch (IOException e){
					e.printStackTrace();
				}finally{
					try{
						if(in != null)
							in.close();
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		}
	}

	// zt change end
	public static void main(String args[]) throws IOException{
		ZipUtil zu = new ZipUtil();
		zu.zip("D:/localpro/zhaotan", "/testZT.zip");
		System.out.println("over");
	}
}
