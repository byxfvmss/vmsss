package com.cjit.gjsz.validate.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cjit.gjsz.validate.model.Validate;

public class PreValidateUtil{

	private String validateFilePath;
	private String validateFeedBack;
	private static final String TOKEN_LOCK_FILE_NAME = "Token.validate";
	private List validateFileList;
	private Map map;

	public Map getMap(){
		map = new HashMap();
		map.put("CFAAA", "外债双边贷款—签约信息");
		map.put("CFAAB", "外债买方信贷—签约信息");
		map.put("CFAAC", "外债境外同业拆借—签约信息");
		map.put("CFAAD", "外债海外代付—签约信息");
		map.put("CFAAE", "外债卖出回购—签约信息");
		map.put("CFAAF", "外债远期信用证—签约信息");
		map.put("CFAAG", "外债银团贷款—签约信息");
		map.put("CFAAH", "外债贵金属拆借—签约信息");
		map.put("CFAAI", "外债其他贷款—签约信息");
		map.put("CFAAJ", "外债货币市场工具—签约信息");
		map.put("CFAAK", "外债债券和票据—签约信息");
		map.put("CFAAL", "外债境外同业存放—签约信息");
		map.put("CFAAM", "外债境外联行及附属机构往来—签约信息");
		map.put("CFAAN", "外债非居民机构存款—签约信息");
		map.put("CFAAP", "外债非居民个人存款—签约信息");
		map.put("CFAAQ", "外债其他外债—签约信息");
		map.put("CFAAR", "外债—变动信息");
		map.put("CFAAS", "外债—余额信息");
		map.put("CFABA", "对外担保—签约信息");
		map.put("CFABB", "对外担保—责任余额信息");
		map.put("CFABC", "对外担保-履约信息");
		map.put("CFACA", "国内外汇贷款—签约信息");
		map.put("CFACB", "国内外汇贷款—变动信息");
		map.put("CFADA", "境外担保项下境内贷款—签约信息");
		map.put("CFADB", "境外担保项下境内贷款—变动及履约信息");
		map.put("CFAEA", "外汇质押人民币贷款—签约信息");
		map.put("CFAEB", "外汇质押人民币贷款—变动信息");
		map.put("CFAFA", "商业银行人民币结构性存款—签约信息");
		map.put("CFAFB", "商业银行人民币结构性存款—终止信息");
		map.put("CFAFC", "商业银行人民币结构性存款—利息给付信息");
		map.put("CFAFD", "商业银行人民币结构性存款—资金流出入和结购汇信息");
		map.put("CFAA", "exdebtcode,t_cfa_a_exdebt");
		map.put("CFAB", "EXGUARANCODE,t_cfa_b_exguaran");
		map.put("CFAC", "DOFOEXLOCODE,t_cfa_c_dofoexlo");
		map.put("CFAD", "LOUNEXGUCODE,t_cfa_d_lounexgu");
		map.put("CFAE", "EXPLRMBLONO,t_cfa_e_explrmblo");
		map.put("CFAF", "strdecode,t_cfa_f_strde");
		return map;
	}

	public void setMap(Map map){
		this.map = map;
	}

	public PreValidateUtil(){
		getMap();
	}

	public PreValidateUtil(String validateFilePath, List validateFileList){
		this.setValidateFilePath(validateFilePath);
		this.setValidateFileList(validateFileList);
	}

	public String getValidateFilePath(){
		return validateFilePath;
	}

	public void setValidateFilePath(String validateFilePath){
		this.validateFilePath = validateFilePath;
	}

	public String getValidateFeedBack(){
		return validateFeedBack;
	}

	public void setValidateFeedBack(String validateFeedBack){
		this.validateFeedBack = validateFeedBack;
	}

	public List getValidateFileList(){
		return validateFileList;
	}

	public void setValidateFileList(List validateFileList){
		this.validateFileList = validateFileList;
	}

	// 还是需要传递过来文件名来判断需要取哪些文件的错误校验；
	public int startPreValidate() throws Exception{
		// ---如果返回false会导致一直执行
		int flg = 0;
		long size = this.getFileSize(new File(validateFilePath));
		// ---文件夹内没有文件，标志我们可以获取校验后的结果了；
		if(size == 0){
			// --生成锁文件失败，说明有其他用户在使用中
			if(!this.makeLockFile(validateFilePath, TOKEN_LOCK_FILE_NAME)){
				flg = 0;
			}else{
				flg = 1;
			}
		}else
			flg = -1;
		return flg;
	}

	// 取得文件夹大小
	public long getFileSize(File f) throws Exception{
		long size = 0;
		File flist[] = f.listFiles();
		for(int i = 0; i < flist.length; i++){
			if(flist[i].isDirectory()){
				size = size + getFileSize(flist[i]);
			}else{
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 生成锁文件，并返回生成结果；若锁文件已经存在，直接返回false
	 * @param filePath 文件路径
	 * @param lockFileName 锁文件名
	 * @return
	 * @throws IOException
	 */
	private boolean makeLockFile(String filePath, String lockFileName)
			throws IOException{
		String lockFileFullName = filePath + File.separator + lockFileName;
		File lockFile = new File(lockFileFullName);
		if(lockFile.exists()){
			return false;
		}else{
			return (lockFile.createNewFile());
		}
	}

	/**
	 * 删除锁文件
	 * @param filePath
	 * @param lockFileName
	 * @return
	 */
	public boolean deleteLockFile(String filePath){
		String lockFileFullName = filePath + File.separator
				+ TOKEN_LOCK_FILE_NAME;
		File lockFile = new File(lockFileFullName);
		return lockFile.delete();
	}

	/*
	 * 读取XML文件列表，判断文件是否在本次操作中
	 */
	public List readErrorList(){
		List outList = new ArrayList();
		try{
			File f = new File(getValidateFeedBack());
			// ---默认validateFilePath是一个文件夹，就下面1层是文件夹了；
			File flist[] = f.listFiles();
			for(int i = 0; i < flist.length; i++){
				if(flist[i].isDirectory()){
					// ---按照规则应该是预校验是在原来的规范文件夹名称后面加ERR作为校验后的文件夹名称
					// ---找到的是文件夹；
					// ---按照文档中的说法，正确的也返回一个文件，所以错误的返回大于1个文件
					if(flist[i].listFiles().length > 1){
						outList.addAll(readValidateData(flist[i].getName()));
					}
				}else{
					System.out.println("无需考虑");
				}
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outList;
	}

	/*
	 * 读取XML中的核心元素
	 */
	public List readValidateData(String file){
		List errorList = new ArrayList();
		try{
			file = validateFeedBack + File.separator + file;
			File f = new File(file);
			// ---其实就是当天都放在一个文件夹中
			// ---基本思路就是：
			/*
			 * 控制文件和数据文件在一个文件夹中，因为   当天   生成的多次校验反馈文件都放在一个文件夹中
			 * 所以，取控制文件，然后根据控制文件中制定的数据文件的名称，再去取数据文件中反馈的错误；
			 * 因为每次都生成，所以会不停的覆盖这个文件夹；
			 * 但是因为之前生成文件的时候和做校验的时候，都做了一个文件锁，所以从某种角度控制了同一时刻发生冲突；
			 */
			String d1 = org.apache.commons.lang.time.DateFormatUtils.format(
					new Date(), "yyyyMMdd");
			d1 = d1.substring(2, d1.length());
			System.out.println("d1===" + d1);
			if(f.isDirectory() && f.getName().indexOf(d1) > 0){
				File xmls[] = f.listFiles();
				List dataFiles = new ArrayList();
				// ---找控制文件，并获得数据文件名称LIST
				for(int j = 0; j < xmls.length; j++){
					if(xmls[j].getName().startsWith("CFATT")){ // ---此处固定写死--控制文件
						SAXReader reader = new SAXReader();
						Document doc = reader.read(xmls[j]);
						List tmpList = doc.selectNodes("//FILES/FILENAME");
						for(int k = 0; k < tmpList.size(); k++){
							Node node = (Node) tmpList.get(k);
							dataFiles.add(node.getStringValue());
						}
					}
				}
				// ---错误文件的大循环
				for(int i = 0; i < xmls.length; i++){
					if(dataFiles.contains(xmls[i].getName())){
						System.out.println("=======" + xmls[i].getName());
						SAXReader reader = new SAXReader();
						Document doc = reader.read(xmls[i]);
						// Element root = doc.getRootElement();
						String tableId = null;
						List currentfile = doc.selectNodes("//CURRENTFILE");// --用来判断是哪个文件校验错误
						Node node = (Node) currentfile.get(0);
						String key = node.getStringValue();
						String tablecn = (String) this.getMap().get(key);
						String column = "";
						if(key.startsWith("CFAA")){// --固定写死
							tableId = "t_cfa_a_exdebt";
							column = "exdebtcode";
						}else if(key.startsWith("CFAC")){
							tableId = "t_cfa_c_dofoexlo";
							column = "dofoexlocode";
						}else if(key.startsWith("CFAB")){
							tableId = "t_cfa_b_exguaran";
							column = "exguarancode";
						}else if(key.startsWith("CFAD")){
							tableId = "t_cfa_d_lounexgu";
							column = "lounexgucode";
						}else if(key.startsWith("CFAE")){
							tableId = "t_cfa_e_explrmblo";
							column = "explrmblono";
						}else if(key.startsWith("CFAF")){
							tableId = "t_cfa_f_strde";
							column = "strdecode";
						}
						List list2 = doc.selectNodes("//REC");// ---默认生成的XML格式不会变化，使用XPATH来定位；
						errorList.addAll(dealWith(list2, tableId, tablecn,
								column));
					}
				}
			}
		}catch (DocumentException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorList;
	}

	public List dealWith(List recList, String tableId, String tablecn,
			String column){
		List outList = new ArrayList();
		for(int i = 0; i < recList.size(); i++){
			Node node = (Element) recList.get(i);
			List bussno = node.selectNodes("BUSSNO");// --REC下面 会有1个bussno节点
			List errfield = node.selectNodes("ERRFIELDS/ERR/ERRFIELD");
			List errfieldcn = node.selectNodes("ERRFIELDS/ERR/ERRFIELDCN");
			List errdesc = node.selectNodes("ERRFIELDS/ERR/ERRDESC");
			Node node2 = (Element) bussno.get(0); // --默认只有一个
			String buss = node2.getText();// ---错误的字段名称
			for(int n = 0; n < errfield.size(); n++){
				Validate obj = new Validate();// ----临时使用RptData做为VO对象而传值
				obj.setTablecn(tablecn);
				obj.setBussno(buss);
				obj.setColumn(column);
				obj.setTableId(tableId);// ---这个地方先放下，回头再来；
				Node node3 = (Element) errfield.get(n);
				String value_errfield = node3.getText();// ---错误的字段名称
				obj.setErrfield(value_errfield);
				Node node4 = (Element) errfieldcn.get(n);
				String value_errfieldcn = node4.getText();// ---错误的字段中文名称
				obj.setErrfieldcn(value_errfieldcn);
				Node node5 = (Element) errdesc.get(n);
				String value_errdesc = node5.getText();// ---错误的字段中文描述
				obj.setErrdesc(value_errdesc);
				outList.add(obj);
			}
		}
		return outList;
	}
}
