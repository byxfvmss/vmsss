package com.cjit.gjsz.filem.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ftp.FTPFile;

import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.filem.service.ReceiveTemplate;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class ReceiveHistorySend extends ReceiveTemplate{

	public ReceiveHistorySend(ReceiveReportService service1){
		super(service1);
		this.localFolder = "historysend";
		this.packType = DataUtil.PACKTYPE_HISTORYSEND;
	}

	public String getFunctionName(){
		return "历史文件接收";
	}

	protected String doRecieve(List downForder){
		// 循环下载下来的包，仅记录包名
		for(int i = 0; i < downForder.size(); i++){
			File dstFolder = new File((String) downForder.get(i));
			receiveReportService.saveReceivePack(dstFolder.getName(), null,
					this.packType);
		}
		return "ok";
	}

	// 重载接收文件条件，永远接受当前日期的数据
	protected boolean ftpIsReceive(FTPFile ftpFile){
		try{
			if(ftpFile.isDirectory() && !ftpFile.getName().equals(".")
					&& !ftpFile.getName().equals("..")){
				Date dateFolder = DateUtils.parseDate(ftpFile.getName(),
						new String[] {"yyyyMMdd"});
				return !receiveReportService.isReceivePackExists(ftpFile
						.getName(), packType)
						|| DateUtils.isSameDay(new Date(), dateFolder);
			}
		}catch (Exception e){
			log.error(e);
		}
		return false;
	}

	public boolean sftpIsRecieve(LsEntry le){
		try{
			if(le.getAttrs().isDir() && !le.getFilename().equals(".")
					&& !le.getFilename().equals("..")){
				Date dateFolder = DateUtils.parseDate(le.getFilename(),
						new String[] {"yyyyMMdd"});
				return !receiveReportService.isReceivePackExists(le
						.getFilename(), packType)
						|| DateUtils.isSameDay(new Date(), dateFolder);
			}
		}catch (Exception e){
			log.error(e);
		}
		return false;
	}
}
