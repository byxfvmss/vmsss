package com.cjit.gjsz.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class Sftp{

	/**
	 * 无密钥方式连接远程主机
	 * @param username 用户名
	 * @param userpasswd 密码
	 * @param host 主机名
	 * @param port 端口
	 * @throws JSchException
	 */
	public static ChannelSftp connect(String username, String userpasswd,
			String host, int port) throws JSchException{
		JSch jsch = new JSch();
		Session jschsession = jsch.getSession(username, host, port);
		jschsession.setPassword(userpasswd);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		jschsession.setConfig(config);
		jschsession.connect();
		Channel channel = jschsession.openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
	}

	/**
	 * 使用密钥连接远程主机
	 * @param certfile 密钥文件路径
	 * @param key 密钥口令
	 * @param username 用户名
	 * @param host 主机名
	 * @param port 端口
	 * @throws JSchException
	 */
	public static ChannelSftp connectByCert(String certfile, String key,
			String username, String host, int port) throws JSchException{
		JSch jsch = new JSch();
		jsch.addIdentity(certfile);
		Session jschsession = jsch.getSession(username, host, port);
		UserInfo ui = new MyUserInfo(key);
		jschsession.setUserInfo(ui);
		jschsession.connect();
		Channel channel = jschsession.openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
	}

	/**
	 * 上传文件
	 * @param directory 上传的目录
	 * @param uploadFile 要上传的文件
	 * @param sftp
	 */
	public void upload(String directory, String uploadFile, ChannelSftp sftp){
		try{
			sftp.cd(directory);
			File file = new File(uploadFile);
			sftp.put(new FileInputStream(file), file.getName());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile 存在本地的路径
	 * @param sftp
	 */
	public void download(String directory, String downloadFile,
			String saveFile, ChannelSftp sftp){
		try{
			sftp.cd(directory);
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * @param directory 要删除文件所在目录
	 * @param deleteFile 要删除的文件
	 * @param sftp
	 */
	public void deleteFile(String directory, String deleteFile, ChannelSftp sftp){
		try{
			sftp.cd(directory);
			sftp.rm(deleteFile);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 生成文件
	 * @param directory 要生成文件所在目录
	 * @param createFile 要生成的文件名
	 * @param sftp
	 */
	public void createFile(String directory, String createFile, ChannelSftp sftp){
		try{
			sftp.cd(directory);
			sftp.mkdir(createFile);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 列出目录下的文件
	 * @param directory 要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector listFiles(String directory, ChannelSftp sftp)
			throws SftpException{
		return sftp.ls(directory);
	}
	/*public static void main(String[] args) {
		TT sf = new TT();
		String host = "10.168.169.36";//ftp服务器地址。
		int port = 28;//ftp服务端口
		String username = "lxc";//ftp服务用户名
		String password = "lxc";//ftp服务用户密码
		String directory = "//test2";//FTP上的相对目录。
		String uploadFile = "D:\\ftp\\test.txt";//需要上传的文件。
		String downloadFile = "test.txt";//需要下载的文件
		String saveFile = "D:\\download.txt";//把需要下载的文件下载到的目录及文件名
		String deleteFile = "test1.txt";
		ChannelSftp sftp = sf.connect(host, port, username, password);
		//sf.upload(directory, uploadFile, sftp);//可以通过
		//sf.download(directory, downloadFile, saveFile, sftp);//可以通过
		//sf.delete(directory, deleteFile, sftp);//可以通过
	}*/
}


class MyUserInfo implements UserInfo{

	private String passphrase = null;

	public MyUserInfo(String passphrase){
		this.passphrase = passphrase;
	}

	public String getPassphrase(){
		return passphrase;
	}

	public String getPassword(){
		return null;
	}

	public boolean promptPassphrase(String s){
		return true;
	}

	public boolean promptPassword(String s){
		return true;
	}

	public boolean promptYesNo(String s){
		return true;
	}

	public void showMessage(String s){
		System.out.println(s);
	}
}
