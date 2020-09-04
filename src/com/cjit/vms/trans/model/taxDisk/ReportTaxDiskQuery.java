package com.cjit.vms.trans.model.taxDisk;

import org.jdom.Document;
import org.jdom.Element;

import com.cjit.vms.trans.model.taxDisk.parseXml.ReportTaxDiskQueryReturnXml;

/**
 * @author tom 报稅盘查询
 * 
 */
public class ReportTaxDiskQuery extends BaseDiskModel {
	/**
	 * 报税盘编号 是否必须：是
	 */

	private String reportTaxDiskNo;

	public String getReportTaxDiskNo() {
		return reportTaxDiskNo;
	}

	public void setReportTaxDiskNo(String reportTaxDiskNo) {
		this.reportTaxDiskNo = reportTaxDiskNo;
	}

	/**
	 * 报税盘编号 是否必须：是
	 */
	private static final String report_tax_disk_no_ch = "bspkl";

	public String createReportTaxDiskQueryXML() throws Exception {
		Element root = CreateDoocumentHeard();
		Document Doc = new Document(root);
		Element elements = CreateBodyElement();
		Element input = createInputElement();
		addChildElementText(input, report_tax_disk_no_ch, reportTaxDiskNo);
		elements.addContent(input);
		root.addContent(elements);
		String outString = CreateDocumentFormt(Doc, path_ch, filename);
		System.out.println(outString);
		return outString;
	}

	private static final String filename = "报税盘查询.xml";

	public static void main(String[] args) {
		String data = "report_tax_disk_ch    , reportTaxDisk   ,bspbh         ,报税盘编号, , ;"
				+ "tax_no_ch             , taxNo           ,nsrsbh        ,纳税人识别号,   , ;"
				+ "name_ch               , name            ,nsrmc         ,纳税人名称  , , ;"
				+ "tax_organ_code_ch     , taxOrganCode    ,swjgdm        ,税务机关代码, , ;"
				+ "tax_organ_name_ch     , taxOrganName    ,swjgmc        ,税务机关名称, , ;"
				+ "cur_time_ch           , curTime         ,dqsz          ,当前时钟    , , ;"
				+ "enable_time_ch        , enableTime      ,qysj          ,启用时间    , , ;"
				+ "distribute_flag_ch    , distributeFlag  ,ffbz          ,分发标志    , , ;"
				+ "vis_no_ch             , visNo           ,bbh           ,版本号      , , ;"
				+ "issue_no_ch           , issueNo         ,kpjh          ,开票机号    , , ;"
				+ "keep_info_ch          ,  keepInfo       ,blxx          ,保留信息    , , ;"
				+ "company_type_ch       ,  companyType    ,qylx          ,企业类型    , , ;"
				+ "return_code_ch        , returnCode      ,returncode	   ,返回代码		,是 ,	0成功，其它失败 ;"
				+ "return_msg_ch         , returnMsg       ,returnmsg	   ,返回信息		,是 , ;";
		String StringXml = "<?xml version=\"1.0\" encoding=\"gbk\"?>"
				+ "<business comment=\"税控盘信息查询\" id=\"BSPXXCX\">"
				+ "<body yylxdm=\"1\">" + "<output>" + "<bspbh>报税盘编号</bspbh>"
				+ "<nsrsbh>纳税人识别号</nsrsbh>" + "<nsrmc>纳税人名称</nsrmc>"
				+ "<swjgdm>税务机关代码</swjgdm>" + "<swjgmc>税务机关名称</swjgmc>"
				+ "<dqsz>当前时钟</dqsz>" + "<qysj>启用时间</qysj>"
				+ "<ffbz>分发标志</ffbz>" + "<bbh>版本号</bbh>" + "<kpjh>开票机号</kpjh>"
				+ "<blxx></blxx>" + "<qylx>企业类型</qylx>"
				+ "<returncode>00000000</returncode>"
				+ "<returnmsg>成功</returnmsg>" + "</output>" + "</body>"
				+ "</business>";

		String className = "ReportTaxDiskQueryReturnXml";
		new ReportTaxDiskRetrieve().CreatParseXMl(StringXml, data, className);
	}

}
