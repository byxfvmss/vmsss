package com.cjit.gjsz.access.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cjit.gjsz.access.model.AccessMetaData;

public class AccessUtil{

	private Connection connection;
	private Statement statement;

	public AccessUtil(){
	}

	/**
	 * <p> Description: 删除已经存在的mdb文件 </p>
	 */
	public void deleteOldMdbFile(String fielRealPath) throws Exception{
		File oldTargetFile = new File(fielRealPath);
		if(oldTargetFile.exists()){
			System.out.println(fielRealPath + " delete:"
					+ oldTargetFile.delete());
		}
	}

	public void deleteOtherOldMdbFile(String fielRealPath) throws Exception{
		File oldTargetFile = new File(fielRealPath);
		if(oldTargetFile.exists()){
			File parentDic = new File(oldTargetFile.getParent());
			System.out.println(parentDic.getAbsolutePath());
			String[] allFiles = parentDic.list();
			for(int i = 0; i < allFiles.length; i++){
				System.out.println(allFiles[i]);
				if(!(allFiles[i].trim().equals("blank.mdb"))){
					deleteOldMdbFile(parentDic.getAbsolutePath() + "\\"
							+ allFiles[i]);
				}
			}
		}
	}

	/**
	 * <p> Description: 将空白mdb文件拷贝到特定目录 </p>
	 */
	public void copyBlankMdbFile(String blankFileRealPath, String targetRealPath)
			throws Exception{
		InputStream is = new FileInputStream(new File(blankFileRealPath));
		OutputStream out = new FileOutputStream(targetRealPath);
		byte[] buffer = new byte[1024];
		int numRead;
		while((numRead = is.read(buffer)) != -1){
			out.write(buffer, 0, numRead);
		}
		is.close();
		out.close();
	}

	/**
	 * <p> Description: 打开对mdb文件的jdbc-odbc连接 </p>
	 */
	public void connetAccessDB(String dbFileRealPath) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="
				+ dbFileRealPath.trim() + ";DriverID=22;READONLY=true}";
		System.out.println(database);
		connection = DriverManager.getConnection(database, "", "");
		statement = connection.createStatement();
	}

	/**
	 * <p> Description: 执行特定sql语句 </p>
	 */
	public void executeSql(String sql) throws Exception{
		statement.execute(sql);
	}

	public void executeUpdateSql(String sql) throws Exception{
		statement.executeUpdate(sql);
		statement.executeBatch();
	}

	public ResultSet executeQuery(String sql) throws Exception{
		ResultSet rs = statement.executeQuery(sql);
		return rs;
	}

	public List getMetaData(String sql) throws Exception{
		List result = new ArrayList();
		ResultSet rs = statement.executeQuery(sql);
		while(rs.next()){
			AccessMetaData amd = new AccessMetaData();
			amd.setType(rs.getString("TYPE"));
			amd.setCondition(rs.getString("CONDITION"));
			result.add(amd);
		}
		rs.close();
		return result;
	}

	/**
	 * <p> Description: 关闭连接 </p>
	 */
	public void closeConnection() throws Exception{
		statement.close();
		connection.close();
	}
}