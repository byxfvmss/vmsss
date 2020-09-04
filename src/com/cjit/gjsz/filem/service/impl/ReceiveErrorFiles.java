package com.cjit.gjsz.filem.service.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.cjit.common.util.StringUtil;
import com.cjit.gjsz.datadeal.model.RptData;
import com.cjit.gjsz.datadeal.service.DataDealService;
import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.filem.service.ReceiveTemplate;
import com.jcraft.jsch.ChannelSftp;

public class ReceiveErrorFiles extends ReceiveTemplate{

	public ReceiveErrorFiles(ReceiveReportService service1,
			DataDealService service2){
		super(service1, service2);
		this.localFolder = "errfile";
		this.packType = DataUtil.PACKTYPE_ERRORFILES;
	}

	public String getFunctionName(){
		return "错误文件接收";
	}

	protected String doRecieve(List downForder){
		for(int i = 0; i < downForder.size(); i++){
			File dstFolder = new File((String) downForder.get(i));
			// 解析Error目录信息 Begin 将所对应报文包中所有文件打回为已审核通过状态
			// 查询t_rpt_send_commit表，得到Error目录中报文包所包含的所有报文
			if(StringUtil.isNotEmpty(dstFolder.getName())){
				String packName = dstFolder.getName().substring(
						dstFolder.getName().lastIndexOf("CFATT"),
						dstFolder.getName().length());
				List tableList = dataDealService
						.findTableListFromSendCommit(packName);
				for(int t = 0; t < tableList.size(); t++){
					String tableId = (String) tableList.get(t);
					RptData rptData = new RptData();
					StringBuffer sbUpdate = new StringBuffer();
					sbUpdate.append(" datastatus = ").append(
							DataUtil.SHYTG_STATUS_NUM);
					StringBuffer sbCondition = new StringBuffer();
					sbCondition
							.append(
									" businessid in (select businessid from t_rpt_send_commit where packname = '")
							.append(packName).append("' and tableid='").append(
									tableId).append("') ");
					rptData.setTableId(tableId);
					rptData.setUpdateSql(sbUpdate.toString());
					rptData.setUpdateCondition(sbCondition.toString());
					dataDealService.updateRptData(rptData);
					dataDealService.deleteRefuseReasion(rptData, "2",
							sbCondition.toString());
				}
				dataDealService.insertRefuseReasionFromSendCommit(
						"接收到ErrorFiles信息，打回" + dstFolder.getName() + "包中记录",
						"2", packName);
				/*
				List rptSendList = dataDealService.findRptSendCommit(null,
						null, packName, null, -1);
				if(CollectionUtil.isNotEmpty(rptSendList)){
					for(int s = 0; s < rptSendList.size(); s++){
						RptSendCommit rsc = (RptSendCommit) rptSendList.get(s);
						if(rsc != null
								&& StringUtil.isNotEmpty(rsc.getTableId())
								&& StringUtil.isNotEmpty(rsc.getBusinessId())){
							// 修改对应报文记录状态为6-审核已通过
							StringBuffer sql = new StringBuffer(
									" datastatus = "
											+ DataUtil.SHYTG_STATUS_NUM + " ");
							RptData rd = new RptData(rsc.getTableId(), sql
									.toString(), rsc.getBusinessId(), null,
									null, true);
							rd.setReasionInfo("接收到ErrorFiles信息，打回"
									+ dstFolder.getName() + "包中记录");
							dataDealService.updateRptDataForLowerStatus(rd);
						}
					}
				}*/
				// 解析Error目录信息 End
				receiveReportService.saveReceivePack(dstFolder.getName(), null,
						this.packType);
			}
		}
		return "ok";
	}

	protected void downloadFTPByFolder(String destination, String source,
			FTPClient ftpclient){
		return;
	}

	protected void downloadSFTPByFolder(String destination, String source,
			ChannelSftp sftp){
		return;
	}
}
