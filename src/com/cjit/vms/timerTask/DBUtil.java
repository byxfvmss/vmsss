


package com.cjit.vms.timerTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil
{
  public static Connection getConnection()
    throws Exception
  {
    URL classUrl = DBUtil.class.getResource("DBUtil.class");
    String path = classUrl.getPath();
    path = path.substring(0, path.indexOf("timerTask"));
    path = path + "config/db.properties";
    File ffFile = new File(path);
    String ss = ffFile.getAbsolutePath();
    InputStream inputStrem = new BufferedInputStream(new FileInputStream(ss));
    Properties properties = new Properties();
    properties.load(inputStrem);
    String name = properties.getProperty("driver");
    String url = properties.getProperty("url");
    String userName = properties.getProperty("userName");
    String userPwd = properties.getProperty("userPwd");
    Class.forName(name);
    Connection connection = DriverManager.getConnection(url, userName, userPwd);
    System.out.println("已获得数据库连接-----");
    return connection;
  }
  
  public static void close(Connection connection, Statement statement)
  {
    if (statement != null) {
      try
      {
        statement.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
        if (connection != null) {
          try
          {
            connection.close();
          }
          catch (SQLException e1)
          {
            e1.printStackTrace();
          }
        }
      }
      finally
      {
        if (connection != null) {
          try
          {
            connection.close();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
/*
 * package com.cjit.vms.timerTask;
 * 
 * import java.io.BufferedInputStream; import java.io.FileInputStream; import
 * java.io.InputStream; import java.sql.Connection; import
 * java.sql.DriverManager; import java.sql.SQLException; import
 * java.sql.Statement; import java.util.Properties;
 * 
 * public class DBUtil {
 * 
 * public static Connection getConnection() throws Exception{ // InputStream
 * inputStrem = new BufferedInputStream(new FileInputStream("db.properties"));
 * // Properties properties = new Properties();10.162.1.218 //
 * properties.load(inputStrem); String name = "oracle.jdbc.OracleDriver"; String
 * url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL"; String userName = "zzs";
 * String userPwd = "zzs"; Class.forName(name); Connection connection =
 * DriverManager.getConnection(url, userName, userPwd);
 * System.out.println("获得链接"); return connection; } public static void
 * close(Connection connection,Statement statement){ if (null!=statement) { try
 * { statement.close(); } catch (SQLException e) { // TODO Auto-generated catch
 * block e.printStackTrace(); }finally{ if(null!=connection){ try {
 * connection.close(); } catch (SQLException e) { // TODO Auto-generated catch
 * block e.printStackTrace(); } } } } } }
 */