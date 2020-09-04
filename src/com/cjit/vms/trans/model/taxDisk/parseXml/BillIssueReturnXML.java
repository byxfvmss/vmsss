package com.cjit.vms.trans.model.taxDisk.parseXml;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.BaseDiskModel;
import com.cjit.vms.trans.util.taxDisk.TaxDiskUtil;

public class BillIssueReturnXML extends BaseDiskModel{
	public static void main(String[] args) {
		String data="fapiao_type_ch      , fapiaoType         ,fplxdm	     ,发票类型代码		,是	 , ;"+
		"bill_code_ch        , billCode           ,fpdm	       ,发票代码		    ,是 , 	开票成功税控盘返回 ;"+
		"bill_no_ch          , billNo             ,fphm	       ,发票号码		    ,是 , 	开票成功税控盘返回 ;"+
		"issue_date_ch       , issueDate          ,kprq	       ,开票日期		    ,是 , 	开票成功税控盘返回，格式YYYYMMDDHHMMSS ;"+
		"amt_ch              , amt                ,hjje	       ,合计金额		    ,是,	 ;"+
		"tax_control_code_ch , taxControlCode     ,skm	         ,税控码	         ,是,	开票成功税控盘返回 ;"+
		"check_code_ch       , checkCode          ,jym	         ,校验码	         ,否,	开票成功税控盘返回，仅增值税普通发票有效 ;"+
		"return_code_ch      , returnCode         ,returncode	 ,返回代码	      ,是,	00000000成功，其它失败     ;          "+   
		"return_msg_ch       ,  returnmsg         ,returnmsg	   ,返回信息		    ,是,	 ;";
		  String[] datas=data.split(";");
		  StringBuffer entityString= new StringBuffer();
		  for(int i=0;i<datas.length;i++){
		    String[] infos=datas[i].split(",");
		     entityString.append("/*\r\n*"+infos[3].trim()+"  是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private static final String ").append(infos[0].trim()).append("="+"\"").append(infos[2].trim()).append("\";\r\n");
		  }
		  for(int i=0;i<datas.length;i++){
		    String[] infos=datas[i].split(",");
		    //entityString.append("/*\r\n*"+infos[3].trim()+" 是否必须："+infos[4].trim()+"\r\n"+infos[5].trim()).append("\r\n*/").append("\r\n").append("private String ").append(infos[1].trim()+";\r\n");

		  }
		  System.out.println(entityString);
	
}
	public BillIssueReturnXML   (String StringXml) throws Exception{
		  Document doc =StringToDocument(StringXml);
		  Element root=doc.getRootElement();
		  this.comment=root.getAttributeValue(comment_cH);
		  this.Id=root.getAttributeValue(id_cH);
		  Element body=root.getChild(body_cH);
		  Element output=body.getChild(out_put_ch);
		  this.fapiaoType=output.getChildText(fapiao_type_ch);
		  this.billCode=output.getChildText(bill_code_ch);
		  this.billNo=output.getChildText(bill_no_ch);
		  this.issueDate=output.getChildText(issue_date_ch);
		  this.amt=output.getChildText(amt_ch);
		  this.taxControlCode=output.getChildText(tax_control_code_ch);
		  this.checkCode=output.getChildText(check_code_ch);
		  this.returnCode=output.getChildText(return_code_ch);
		  this.returnmsg=output.getChildText(return_msg_ch);
		}
	
	

	/*
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	/*
	*发票代码  是否必须：是
	开票成功税控盘返回
	*/
	private static final String bill_code_ch="fpdm";
	/*
	*发票号码  是否必须：是
	开票成功税控盘返回
	*/
	private static final String bill_no_ch="fphm";
	/*
	*开票日期  是否必须：是
	开票成功税控盘返回，格式YYYYMMDDHHMMSS
	*/
	private static final String issue_date_ch="kprq";
	/*
	*合计金额  是否必须：是

	*/
	private static final String amt_ch="hjje";
	/*
	*税控码  是否必须：是
	开票成功税控盘返回
	*/
	private static final String tax_control_code_ch="skm";
	/*
	*校验码  是否必须：否
	开票成功税控盘返回，仅增值税普通发票有效
	*/
	private static final String check_code_ch="jym";
	/*
	*返回代码  是否必须：是
	00000000成功，其它失败
	*/
	private static final String return_code_ch="returncode";
	/*
	*返回信息  是否必须：是

	*/
	private static final String return_msg_ch="returnmsg";
	private static final String out_put_ch="output";

	
	
	
	/*
	*发票类型代码 是否必须：是

	*/
	private String fapiaoType;
	/*
	*发票代码 是否必须：是
	开票成功税控盘返回
	*/
	private String billCode;
	/*
	*发票号码 是否必须：是
	开票成功税控盘返回
	*/
	private String billNo;
	/*
	*开票日期 是否必须：是
	开票成功税控盘返回，格式YYYYMMDDHHMMSS
	*/
	private String issueDate;
	/*
	*合计金额 是否必须：是

	*/
	private String amt;
	/*
	*税控码 是否必须：是
	开票成功税控盘返回
	*/
	private String taxControlCode;
	/*
	*校验码 是否必须：否
	开票成功税控盘返回，仅增值税普通发票有效
	*/
	private String checkCode;
	/*
	*返回代码 是否必须：是
	00000000成功，其它失败
	*/
	private String returnCode;
	/*
	*返回信息 是否必须：是

	*/
	private String returnmsg;
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getTaxControlCode() {
		return taxControlCode;
	}
	public void setTaxControlCode(String taxControlCode) {
		this.taxControlCode = taxControlCode;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}


}