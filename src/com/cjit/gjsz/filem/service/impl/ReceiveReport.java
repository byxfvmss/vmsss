package com.cjit.gjsz.filem.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.gjsz.datadeal.util.DataUtil;
import com.cjit.gjsz.filem.service.ReceiveReportService;
import com.cjit.gjsz.filem.service.ReceiveTemplate;

public class ReceiveReport extends ReceiveTemplate{

	private String changeDataStatus = null;
	private String configLowerStatusLinkage = null;

	public ReceiveReport(ReceiveReportService service1,
			String changeDataStatus, String configLowerStatusLinkage){
		super(service1);
		this.localFolder = "feedback";
		this.packType = DataUtil.PACKTYPE_FEEDBACK;
		this.changeDataStatus = changeDataStatus;
		this.configLowerStatusLinkage = configLowerStatusLinkage;
	}

	public String getFunctionName(){
		return "反馈接收";
	}

	public String doRecieve(List downForder){
		StringBuffer tempBuffer = new StringBuffer();
		Map backMap = new HashMap();
		for(int i = 0; i < downForder.size(); i++){
			File dstFolder = new File((String) downForder.get(i));
			log.info("开始解析文件夹" + dstFolder.getName());
			File[] files = dstFolder.listFiles();
			if(files != null)
				for(int j = 0; j < files.length; j++){
					// errormsg =
					// receiveReportService.saveImportXmlFile(files[j], files[j]
					// .getName(), tempBuffer, backMap);
					// if(StringUtils.isNotEmpty(errormsg))
					log.info("开始解析文件" + files[j].getName());
					receiveReportService.saveImportXmlFile(files[j], files[j]
							.getName(), tempBuffer, backMap);
					if(backMap != null
							&& !"yes".equalsIgnoreCase((String) backMap
									.get("hasDuplicate"))){
						receiveReportService.parseFeedbackReport(backMap,
								this.changeDataStatus,
								this.configLowerStatusLinkage);
					}
					receiveReportService.saveReceivePack(dstFolder.getName(),
							files[j].getName(), this.packType);
					backMap.clear();
				}
			removeFolder(dstFolder);
		}
		return "ok";
	}
}
