package com.cjit.vms.trans.model.taxDisk;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.parseXml.BillQueryReturnXml;

public class BillQuery extends BaseDiskModel {
	/*
	*纳税人识别号  是否必须：是
	即承运人识别号
	*/
	private static final String tax_no_ch="nsrsbh";
	/*
	*税控盘编号  是否必须：是

	*/
	private static final String tax_disk_no_ch="skpbh";
	/*
	*税控盘口令  是否必须：是

	*/
	private static final String tax_disk_pwd_ch="skpkl";
	/*
	*税务数字证书密码  是否必须：是

	*/
	private static final String tax_cert_pwd_ch="keypwd";
	/*
	*发票类型代码  是否必须：是

	*/
	private static final String fapiao_type_ch="fplxdm";
	/*
	*查询方式  是否必须：是
	0：按发票号码段来读1：按时间段来读
	*/
	private static final String query_way_ch="cxfs";
	/*
	*查询条件  是否必须：是
	cxfs为0时：10位发票代码+8位起始号码+8位终止号码 cxfs为1是：起始日期（YYYYMMDD）+终止日期（YYYYMMDD）
	*/
	private static final String query_condition_ch="cxtj";
	/*
	*查询类型  是否必须：是
	0所有票， 1未上传
	*/
	private static final String query_type_ch="cxlx";
	private static final String filename="发票查询.xml";


	
	/*
	*纳税人识别号 是否必须：是
	即承运人识别号
	*/
	private String taxNo;
	/*
	*税控盘编号 是否必须：是

	*/
	private String taxDiskNo;
	/*
	*税控盘口令 是否必须：是

	*/
	private String taxDiskPwd;
	/*
	*税务数字证书密码 是否必须：是

	*/
	private String taxCertPwd;
	/*
	*发票类型代码 是否必须：是

	*/
	private String fapiaoType;
	/*
	*查询方式 是否必须：是
	0：按发票号码段来读1：按时间段来读
	*/
	private String queryWay;
	/*
	*查询条件 是否必须：是
	cxfs为0时：10位发票代码+8位起始号码+8位终止号码 cxfs为1是：起始日期（YYYYMMDD）+终止日期（YYYYMMDD）
	*/
	private String queryCondition;
	/*
	*查询类型 是否必须：是
	0所有票， 1未上传
	*/
	private String queryType;
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getTaxDiskNo() {
		return taxDiskNo;
	}
	public void setTaxDiskNo(String taxDiskNo) {
		this.taxDiskNo = taxDiskNo;
	}
	public String getTaxDiskPwd() {
		return taxDiskPwd;
	}
	public void setTaxDiskPwd(String taxDiskPwd) {
		this.taxDiskPwd = taxDiskPwd;
	}
	public String getTaxCertPwd() {
		return taxCertPwd;
	}
	public void setTaxCertPwd(String taxCertPwd) {
		this.taxCertPwd = taxCertPwd;
	}
	public String getFapiaoType() {
		return fapiaoType;
	}
	public void setFapiaoType(String fapiaoType) {
		this.fapiaoType = fapiaoType;
	}
	public String getQueryWay() {
		return queryWay;
	}
	public void setQueryWay(String queryWay) {
		this.queryWay = queryWay;
	}
	public String getQueryCondition() {
		return queryCondition;
	}
	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	/*<?xml version="1.0" encoding="gbk"?>
	<business comment="发票查询" id="FPCX">
	<body yylxdm="1">
	<input>
	<nsrsbh>纳税人识别号</nsrsbh>
	<skpbh>税控盘编号</skpbh>
	<skpkl>税控盘口令</skpkl>
	<keypwd>税务数字证书密码</keypwd>
	<fplxdm>发票类型代码</fplxdm>
	<cxfs>查询方式</csfs>
	<cxtj>查询条件</cstj>
	<cxlx>查询类型</cxlx>
	</input>
	</body>
	</business>*/
	public String createBillQueryXMl() throws Exception{
		Element root =CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements =CreateBodyElement();
		Element input=createInputElement();
		addChildElementText(input, tax_no_ch, taxNo);
		addChildElementText(input, tax_disk_pwd_ch, taxDiskPwd);
		addChildElementText(input, tax_cert_pwd_ch, taxCertPwd);
		addChildElementText(input, fapiao_type_ch, fapiaoType);
		addChildElementText(input, query_way_ch, queryWay);
		addChildElementText(input, query_condition_ch, queryCondition);
		addChildElementText(input, query_type_ch, queryType);
		elements.addContent(input);
		root.addContent(elements);
		String outString=CreateDocumentFormt(Doc, path_ch,filename);
		System.out.println(outString);
		return outString;
	}
	
public BillQuery(String taxNo, String taxDiskNo, String taxDiskPwd,
			String taxCertPwd, String fapiaoType, String queryWay,
			String queryCondition, String queryType) {
		super();
		this.taxNo = taxNo;
		this.taxDiskNo = taxDiskNo;
		this.taxDiskPwd = taxDiskPwd;
		this.taxCertPwd = taxCertPwd;
		this.fapiaoType = fapiaoType;
		this.queryWay = queryWay;
		this.queryCondition = queryCondition;
		this.queryType = queryType;
	}
/*public static void main(String[] args) throws Exception {
	BillQuery bill=new BillQuery("110001",
			"111", "888888", "12345678", "0", "00", "1", "1");
	bill.setApplyTypeCode("1");
	bill.setComment("11");
	bill.setId("1");
	bill.createBillQueryXMl();
}
*/
public static void main(String[] args) {
	String StringXml="<?xml version=\"1.0\" encoding=\"gbk\"?>"+
	"<business comment=\"发票查询\" id=\"FPCX\">"+
	"<body yylxdm=\"1\">"+
	"<output>"+
	"<fplxdm>发票类型代码</fplxdm>"+
	"<returncode>0</returncode>"+
	"<returnmsg>成功</returnmsg>"+
	"<fpxx count=\"1\">"+
	"<group xh=\"1\">"+
	"<fpdm>发票代码</fpdm>"+
	"<fphm>发票号码</fphm>"+
	"<fpzt>发票状态</fpzt>"+
	"<scbz>上传标志<scbz>"+
	"<kprq>开票日期</kprq>"+
	"<kpsj>开票时间</kpsj>"+
	"<tspz>特殊票种标识<tspz>"+
	"<skpbh>税控盘编号</skpbh>"+
	"<skm>税控码</skm>"+
	"<jym>校验码</jym>"+
	"<xhdwsbh>销货单位识别号</xhdwsbh>"+
	"<xhdwmc>销货单位名称</xhdwmc>"+
	"<xhdwdzdh>销货单位地址电话</xhdwdzdh>"+
	"<xhdwyhzh>销货单位银行帐号</xhdwyhzh>"+
	"<ghdwsbh>购货单位识别号</ghdwsbh>"+
	"<ghdwmc>购货单位名称</ghdwmc>"+
	"<ghdwdzdh>购货单位地址电话</ghdwdzdh>"+
	"<ghdwyhzh>购货单位银行帐号</ghdwyhzh>"+
	"<fyxm count=\"1\">"+
	"<group xh=\"1\">"+
	"<fphxz>发票行性质</fphxz>"+
	"<spmc>商品名称</spmc>"+
	"<spsm>商品税目</spsm>"+
	"<ggxh>规格型号</ggxh>"+
	"<dw>单位</dw>"+
	"<spsl>商品数量</spsl>"+
	"<dj>单价</dj>"+
	"<je>金额</je>"+
	"<sl>税率</sl>"+
	"<se>税额</se>"+
	"<hsbz>含税标志</hsbz>"+
	"</group>"+
	"</fyxm>"+
	"<qdxm count=\"1\">"+
	"<group xh=\"1\">"+
	"<fphxz>发票行性质</fphxz>"+
	"<spmc>商品名称</spmc>"+
	"<spsm>商品税目</spsm>"+
	"<ggxh>规格型号</ggxh>"+
	"<dw>单位</dw>"+
	"<spsl>商品数量</spsl>"+
	"<dj>单价</dj>"+
	"<je>金额</je>"+
	"<sl>税率</sl>"+
	"<se>税额</se>"+
	"<hsbz>含税标志</hsbz>"+
	"</group>"+
	"</qdxm>"+
	"<qtxm count=\"1\">"+
	"<group xh=\"1\">"+
	"<sl>税率</sl>"+
	"<je>金额</je>"+
	"<se>税额</se>"+
	"</group>"+
	"</qtxm>"+
	"<zhsl>综合税率</zhsl>"+
	"<hjje>合计金额</hjje>"+
	"<hjse>合计税额</hjse>"+
	"<jshj>价税合计</jshj>"+
	"<bz>备注</bz>"+
	"<skr>收款人</skr>"+
	"<fhr>复核人</fhr>"+
	"<kpr>开票人</kpr>"+
	"<jmbbh>加密版本号</jmbbh>"+
	"<zyspmc>主要商品名称</zyspmc>"+
	"<spsm>商品税目</spsm>"+
	"<qdbz>清单标志</qdbz>"+
	"<ssyf>所属月份</ssyf>"+
	"<kpjh>开票机号</kpjh>"+
	"<tzdbh>通知单编号</tzdbh>"+
	"<yfpdm>原发票代码</yfpdm>"+
	"<yfphm>原发票号码</yfphm>"+
	"<zfrq>作废日期</zfrq>"+
	"<zfr>作废人</zfr>"+
	"<qmcs>签名参数</qmcs>"+
	"<qmz>签名值</qmz>"+
	"<ykfsje>已开负数金额</ykfsje>"+
	"</group>"+
	"</fpxx>"+
	"</output>"+
	"</body>"+
	"</business>";
	String data="fapiao_type_ch  					, fapiaoType  							,fplxdm	 		,发票类型代码			,是,  ;"+
	"bill_code_ch  						, billCode  								,fpdm				,发票代码					,是,  ;"+
	"bill_no  									, billNo  									,fphm				,发票号码					,是,  ;"+
	"bill_status_ch  					, billStatus  							,fpzt				,发票状态					,是, 0：已开具的正数发票 1:已开具的负数发票2 :未开具发票的作废发票3：已开正数票的作废发票4:已开负数票的作废发票  ;"+
	"upLoad_flag_ch  					, uploadFlag  							,scbz				,上传标志					,是, 0：未上传 1:已上传  ;"+
	"issue_date_ch  						, issueDate  								,kprq				,开票日期					,是, YYYYMMDD  ;"+
	"issue_time_ch  						, issueTime 		 						,kpsj	    	,开票时间					,否, HHMMSS  ;"+
	"special_ticket_flag_ch  	, specialTicketFlag  				,tspz	    	,特殊票种标识			,是,专票区分是否稀土发票 普票区分是否农产品收购或销售发票  ;"+
	"tax_disk_no_ch						, taxDiskNo  								,skpbh			,税控盘编号			 ,是,  ;"+
	"tax_control_code_ch				, taxConTrolCode  					,skm				,税控码					 ,否,  ;"+
	"check_code_ch							, checkCode  								,jym				,校验码					,否,  ;"+
	""+
	"payee_ch									, payee  										,skr				,收款人					,否,  ;"+
	"reviewer_ch								, reviewer  								,fhr				,复核人		      ,否,  ;"+
	"darwer_ch									, darwer  									,kpr				,开票人		      ,否,  ;"+
	"ori_bill_code_ch					, oriBillCode  							,yfpdm			,原发票代码		  ,否,	负数发票时有效  ;"+
	"ori_bill_no_ch						, oriBillNo  								,yfphm			,原发票号码		  ,否,	负数发票时有效  ;"+
	"cancel_date_ch						, cancelDate  							,zfrq				,作废日期				 ,否,		作废发票时有效，YYYYMMDD  ;"+
	"cancel_people_ch					, cancelPeople  						,zfr				,作废人				  ,否,		作废发票时有效  ;"+
	"sign_param_ch 						, signParam  								,qmcs				,签名参数				 ,否,  ;"+
	"sign_value_ch							, signValue  								,qmz				,签名值					,否,  ;"+
	"has_neg_amt								, hasNegAmt  								,ykfsje			,已开负数金额		 ,否,  ;"+
	"return_code_ch						, returnCode  							,returncode	,返回代码				 ,是,	00000000成功，其它失败  ;"+
	"return_msg_ch	 						, returnMsg  								,returnmsg	,返回信息				 ,是,;";
	String className="BillQueryReturnXml";
new BillQuery().CreatParseXMl(StringXml, data, className);
}

/*<?xml version="1.0" encoding="gbk"?>
<business comment="发票查询" id="FPCX">
<body yylxdm="1">
<output>
<fplxdm>发票类型代码</fplxdm>
<returncode>0</returncode>
<returnmsg>成功</returnmsg>
<fpxx count="1">
<group xh="1">
<fpdm>发票代码</fpdm>
<fphm>发票号码</fphm>
<fpzt>发票状态</fpzt>
<scbz>上传标志<scbz>
<kprq>开票日期</kprq>
<kpsj>开票时间</kpsj>
<tspz>特殊票种标识<tspz>
<skpbh>税控盘编号</skpbh>
<skm>税控码</skm>
<jym>校验码</jym>
<xhdwsbh>销货单位识别号</xhdwsbh>
<xhdwmc>销货单位名称</xhdwmc>
<xhdwdzdh>销货单位地址电话</xhdwdzdh>
<xhdwyhzh>销货单位银行帐号</xhdwyhzh>
<ghdwsbh>购货单位识别号</ghdwsbh>
<ghdwmc>购货单位名称</ghdwmc>
<ghdwdzdh>购货单位地址电话</ghdwdzdh>
<ghdwyhzh>购货单位银行帐号</ghdwyhzh>
<fyxm count="1">
<group xh="1">
<fphxz>发票行性质</fphxz>
<spmc>商品名称</spmc>
<spsm>商品税目</spsm>
<ggxh>规格型号</ggxh>
<dw>单位</dw>
<spsl>商品数量</spsl>
<dj>单价</dj>
<je>金额</je>
<sl>税率</sl>
<se>税额</se>
<hsbz>含税标志</hsbz>
</group>
</fyxm>
<qdxm count="1">
<group xh="1">
<fphxz>发票行性质</fphxz>
<spmc>商品名称</spmc>
<spsm>商品税目</spsm>
<ggxh>规格型号</ggxh>
<dw>单位</dw>
<spsl>商品数量</spsl>
<dj>单价</dj>
<je>金额</je>
<sl>税率</sl>
<se>税额</se>
<hsbz>含税标志</hsbz>
</group>
</qdxm>
<qtxm count="1">
<group xh="1">
<sl>税率</sl>
<je>金额</je>
<se>税额</se>
</group>
</qtxm>
<zhsl>综合税率</zhsl>
<hjje>合计金额</hjje>
<hjse>合计税额</hjse>
<jshj>价税合计</jshj>
<bz>备注</bz>
<skr>收款人</skr>
<fhr>复核人</fhr>
<kpr>开票人</kpr>
<jmbbh>加密版本号</jmbbh>
<zyspmc>主要商品名称</zyspmc>
<spsm>商品税目</spsm>
<qdbz>清单标志</qdbz>
<ssyf>所属月份</ssyf>
<kpjh>开票机号</kpjh>
<tzdbh>通知单编号</tzdbh>
<yfpdm>原发票代码</yfpdm>
<yfphm>原发票号码</yfphm>
<zfrq>作废日期</zfrq>
<zfr>作废人</zfr>
<qmcs>签名参数</qmcs>
<qmz>签名值</qmz>
<ykfsje>已开负数金额</ykfsje> 
</group>
</fpxx>*/



/**
*发票代码  是否必须：是

*/
private static final String bill_code_ch="fpdm";
/**
*发票号码  是否必须：是

*/
private static final String bill_no="fphm";
/**
*发票状态  是否必须：是
0：已开具的正数发票 1:已开具的负数发票2 :未开具发票的作废发票3：已开正数票的作废发票4:已开负数票的作废发票
*/
private static final String bill_status_ch="fpzt";
/**
*上传标志  是否必须：是
0：未上传 1:已上传
*/
private static final String upLoad_flag_ch="scbz";
/**
*开票日期  是否必须：是
YYYYMMDD
*/
private static final String issue_date_ch="kprq";
/**
*开票时间  是否必须：否
HHMMSS
*/
private static final String issue_time_ch="kpsj";
/**
*特殊票种标识  是否必须：是
专票区分是否稀土发票 普票区分是否农产品收购或销售发票
*/
private static final String special_ticket_flag_ch="tspz";

/**
*税控码  是否必须：否

*/
private static final String tax_control_code_ch="skm";
/**
*校验码  是否必须：否

*/
private static final String check_code_ch="jym";
/**
*收款人  是否必须：否

*/
private static final String payee_ch="skr";
/**
*复核人  是否必须：否

*/
private static final String reviewer_ch="fhr";
/**
*开票人  是否必须：否

*/
private static final String darwer_ch="kpr";
/**
*原发票代码  是否必须：否
负数发票时有效
*/
private static final String ori_bill_code_ch="yfpdm";
/**
*原发票号码  是否必须：否
负数发票时有效
*/
private static final String ori_bill_no_ch="yfphm";
/**
*作废日期  是否必须：否
作废发票时有效，YYYYMMDD
*/
private static final String cancel_date_ch="zfrq";
/**
*作废人  是否必须：否
作废发票时有效
*/
private static final String cancel_people_ch="zfr";
/**
*签名参数  是否必须：否

*/
private static final String sign_param_ch="qmcs";
/**
*签名值  是否必须：否

*/
private static final String sign_value_ch="qmz";
/**
*已开负数金额  是否必须：否

*/
private static final String has_neg_amt="ykfsje";
/**
*返回代码  是否必须：是
00000000成功，其它失败
*/
private static final String return_code_ch="returncode";
/**
*返回信息  是否必须：是

*/
private static final String return_msg_ch="returnmsg";

public BillQuery() {
	super();
	// TODO Auto-generated constructor stub
}


}
