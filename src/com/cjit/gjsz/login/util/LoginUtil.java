package com.cjit.gjsz.login.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cjit.common.util.PropertiesUtil;

public class LoginUtil{

	String user = "";
	String password = "";
	String url = "";
	String driverName = "";
	String dbName = "";
	String dbFileName = "";
	String dataPath = "";
	String LogPath = "";

	/**
	 * connMaster true：连接master false：连接BOP
	 * @param filePath
	 * @param connMaster
	 * @return
	 */
	public boolean readProperties(String filePath, boolean connMaster){
		this.user = PropertiesUtil.readProperties(filePath, "jdbc.username");
		this.password = PropertiesUtil
				.readProperties(filePath, "jdbc.password");
		this.driverName = PropertiesUtil
				.readProperties(filePath, "jdbc.driver");
		this.dbName = PropertiesUtil.readProperties(filePath, "databaseName");
		this.dbFileName = PropertiesUtil.readProperties(filePath, "dbFileName");
		this.dataPath = PropertiesUtil.readProperties(filePath, "dataPath");
		this.LogPath = PropertiesUtil.readProperties(filePath, "LogPath");
		String tmpurl = PropertiesUtil.readProperties(filePath, "jdbc.url");
		if(connMaster){
			this.url = tmpurl.replaceAll(dbName, "master");
		}else{
			this.url = tmpurl;
		}
		return true;
	}

	/**
	 * dbFilePath 是jdbc.properties路径 返回 true 已经存在该数据库
	 * @param dbFilePath
	 * @return
	 */
	public boolean testDbExit(String dbFilePath){
		readProperties(dbFilePath, true);
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			Class.forName(driverName).newInstance();
			// 查询是否有名称为BOP的数据库 有的话 直接跳转
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			String query = "select sysdate from dual";
			// 去掉MS_SQL的判断
			// String query = "select name from master..sysdatabases";
			rs = stmt.executeQuery(query);
			conn.commit();
			while(rs.next()){
				return true;
			}
			conn.close();
		}catch (InstantiationException e){
			e.printStackTrace();
		}catch (IllegalAccessException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt != null){
					stmt.close();
				}
				if(conn != null){
					conn.close();
				}
				if(rs != null){
					rs.close();
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		System.out.println("database no exit!");
		return false;
	}

	/**
	 * 恢复数据库
	 * @return
	 */
	public boolean restoreDb(String dbFilePath, String webRootPath,
			String newUser, String newPassword, String newIp){
		readProperties(dbFilePath, false);
		this.user = newUser;
		this.password = newPassword;
		String[] urls = url.split("//");
		this.url = urls[0] + "//" + newIp + ":1433/master";
		System.out.println(this.url);
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try{
			Class.forName(driverName).newInstance();
			// 首先创建BOP数据库
			conn = DriverManager.getConnection(url, user, password);
			if(conn != null){
				conn.setAutoCommit(false);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				String sql = "create database BOP "
						+ "ON (NAME='BOP_Data',FILENAME='" + this.dataPath
						+ "') " + "LOG ON(NAME= 'Sales_log',FILENAME='"
						+ this.LogPath + "');";
				stmt.execute(sql);
				conn.commit();
				conn.close();
				// 然后恢复数据库 未确保恢复时没有其它连接 先对conn关闭后重建
				conn = DriverManager.getConnection(url, user, password);
				conn.setAutoCommit(false);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				String sql2 = "use master; restore database BOP from disk = '"
						+ webRootPath + "\\data\\database\\" + dbFileName
						+ "' " + "WITH MOVE 'BOP_Data' TO '" + this.dataPath
						+ "', " + "MOVE 'BOP_Log' TO '" + this.LogPath + "'";
				// System.out.println(sql2);
				stmt.execute(sql2);
				conn.commit();
				this.url = this.url.replaceAll("master", this.dbName);
				// 写文件
				String basePath = dbFilePath.replaceAll("/config.properties",
						"/");
				PropertiesUtil.writeProp(basePath, "config.properties",
						"jdbc.url", this.url, "jdbc.password", this.password);
				System.out.println("database restore success!");
				return true;
			}else{
				return false;
			}
		}catch (InstantiationException e){
			e.printStackTrace();
		}catch (IllegalAccessException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt != null){
					stmt.close();
				}
				if(conn != null){
					conn.close();
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		return false;
	}
}
