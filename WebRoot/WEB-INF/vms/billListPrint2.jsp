<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.filem.util.FileUtil"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<OBJECT ID=sk CLASSID="clsid: 003BD8F2-A6C3-48EF-9B72-ECFD8FC4D49F" codebase="NISEC_SKSCX.ocx#version=1,0,0,1" style="display: none;">
</OBJECT>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/lhb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
	<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<script type="text/javascript">
		function submitQuery(){
		submitAction(document.forms[0], "listBillPrint.action");
		}
		function SetParameter()
		{
				var sInputInfo = "<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"20001\" comment=\"参数设置\">\r\n<body yylxdm=\"1\">\r\n<servletip>123.124.177.178</servletip>\r\n<servletport>10009</servletport>\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>";
		alert(sInputInfo);
		try
		    {
		var ret = sk.Operate("<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"20001\" comment=\"参数设置\">\r\n<body yylxdm=\"1\">\r\n<servletip>123.124.177.178</servletip>\r\n<servletport>10009</servletport>\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>");
		alert(ret);
		    }
		catch(e)
		    {
		alert(e.message + ",errno:" + e.number);
		    }	
}
function KeyQuery()
{
var sInputInfo = "<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"20002\" comment=\"税控钥匙信息查询\">\r\n<body yylxdm=\"1\">\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>";
alert(sInputInfo);
try
    {
ret = sk.Operate(sInputInfo);
	alert(ret);
    }
catch(e)
    {
	alert(e.message + ",errno:" + e.number);
    }
}

		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post"  id="Form1">
	
	<%
		String currMonth = (String) request.getAttribute("currMonth");
		String projectCode = (String) request.getAttribute("projectCode");
		String SKPXXCX = FileUtil.createXmlStringForSKPXXCX(FileUtil.skpkl);
		String res = "";
		System.out.println("SKPXXCX:");
		System.out.println(SKPXXCX);
		
		String findz = (String) request.getAttribute("zbillInfo");
		String findp = (String) request.getAttribute("pbillInfo");
	%>
	<input type="hidden" id="printLimitValue" name="printLimitValue" value="<s:property value="printLimitValue"/>">
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<input type="hidden" name="reasionInfo" id="reasionInfo" value=""/>
	<input type="hidden" name=findz id="findz" value="<%=findz%>"/>
	<input type="hidden" name="findp" id="findp" value="<%=findp%>"/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>发票打印
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="left">
		<table id="tbl_query" cellpadding="0" cellspacing="0" width="80%" border="0">
		<tr>
			<td align="left">
				开票日期:
			</td>
			<td>
				<input class="tbl_query_time" type="text" name="billInfo.billBeginDate"
					value="<s:property value='billInfo.billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billInfo.billEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" type="text" name="billInfo.billEndDate"
					value="<s:property value='billInfo.billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billInfo.billBeginDate\')}'})"
					size='11'"/>
			</td>
			
			<td align="left">
				客户名称:
			</td>
			<td>
				<input type="text" name="billInfo.customerName" value="<s:property value='billInfo.customerName'/>" maxlength="100"/>
			</td>
			
			<td align="left">
				发票类型:
			</td>
			<td>
				<select id="billInfo.fapiaoType" name="billInfo.fapiaoType"  style="width:120px">
				<option value="0" <s:if test='billInfo.fapiaoType=="0"'>selected</s:if><s:else></s:else>>增值税专用发票</option>
				<option value="1" <s:if test='billInfo.fapiaoType=="1"'>selected</s:if><s:else></s:else>>增值税普通发票</option>
			</select>
			</td>
			
		</tr>
		<tr>
			<td align="left">
				是否手工录入:
			</td>
			<td>
				<select id="billInfo.isHandiwork" name="billInfo.isHandiwork"  style="width:120px">
				<option value="" <s:if test='billInfo.isHandiwork==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="1" <s:if test='billInfo.isHandiwork=="1"'>selected</s:if><s:else></s:else>>自动开票</option>
				<option value="2" <s:if test='billInfo.isHandiwork=="2"'>selected</s:if><s:else></s:else>>人工审核</option>
				<option value="3" <s:if test='billInfo.isHandiwork=="3"'>selected</s:if><s:else></s:else>>人工开票</option>
			</select>
			</td>
			
			<td align="left">
				开具类型:
			</td>
			<td>
				<select id="billInfo.issueType" name="billInfo.issueType"  style="width:120px">
				<option value="" <s:if test='billInfo.issueType==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="1" <s:if test='billInfo.issueType=="1"'>selected</s:if><s:else></s:else>>单笔</option>
				<option value="2" <s:if test='billInfo.issueType=="2"'>selected</s:if><s:else></s:else>>合并</option>
				<option value="3" <s:if test='billInfo.issueType=="3"'>selected</s:if><s:else></s:else>>拆分</option>
			</select>
			</td>
			
			<td align="left">
				状态:
			</td>
			<td>
				<select id="billInfo.dataStatus" name="billInfo.dataStatus"  style="width:120px">
				<option value="" <s:if test='billInfo.dataStatus==""'>selected</s:if><s:else></s:else>>全部</option>
				<option value="5" <s:if test='billInfo.dataStatus=="5"'>selected</s:if><s:else></s:else>>已开具未上传</option>
				<option value="6" <s:if test='billInfo.dataStatus=="6"'>selected</s:if><s:else></s:else>>已开具已上传</option>
				<option value="9" <s:if test='billInfo.dataStatus=="9"'>selected</s:if><s:else></s:else>>打印失败</option>
			</select>
			</td>
			
		<td style="width:80px;" align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitQuery()" />
				<input type="button" class="tbl_query_button" value="导出xml文件"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitQuery1()" />
			</td>
		</tr>
		</table>
		<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
			
				<input type="button" class="tbl_query_button" value="参数设置"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="SetParameter()" />
				<input type="button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" value="税控钥匙信息查询" onclick="KeyQuery();">
				
			<%--<%
				if ("MUFG".equals(projectCode)){
			%>
					onclick="toExcel()" />
					
					<input type="button" class="tbl_query_button" value="增值税专票导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView"  onclick="billsToXml('1')" />
			 
					<input type="button" class="tbl_query_button" value="增值税普票导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="billsToXml('4')" />
					
			<%
				} else if ("HSBC".equals(projectCode)){
			%>
					onclick="printNew()" />
					
					<input type="button" class="tbl_query_button" value="导出EXCEL"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="toExcelNew()" />
			<%
				}
			%>--%>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<div style="overflow:auto;width:100%;" id="list1">
	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	<tr class="lessGrid head">
		<th style="text-align:center">
			<input style="width:13px;height:13px;" id="CheckAll" type="checkbox" onclick="cbxselectall(this,'selectBillIds')" />
		</th>
		<th style="text-align:center">序号</th>
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">客户名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票号码</th>
		<th style="text-align:center">商品名称</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
		<th style="text-align:center">是否手工录入</th>
		<th style="text-align:center">开具类型</th>
		<th style="text-align:center">发票类型</th>
		<th style="text-align:center">状态</th>
		<th style="text-align:center">操作</th>
		
	</tr>
	<%
		PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
		if (paginationList != null){
			List billInfoList = paginationList.getRecordList();
			if (billInfoList != null && billInfoList.size() > 0){
				for(int i=0; i<billInfoList.size(); i++){
					BillInfo bill = (BillInfo)billInfoList.get(i);
					if(i%2==0){
	%>
	<tr class="lessGrid rowA">
	<%
					}else{
	%>
	<tr class="lessGrid rowB">
	<%
					}
					String billAuditStatus = "";
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" id="selectBillIds" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
			<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
			<input type="hidden" name="billId" value="<%=bill.getBillId()%>"/>
		</td>
		<td align="left"><%=i+1%></td>
		<td align="left"><%=bill.getBillDate()%></td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="left"><%=bill.getCustomerTaxno()%></td>
		<td align="left"><%=bill.getBillCode()%></td>
		<td align="left"><%=bill.getBillNo()%></td>
		<td align="left"><%=bill.getGoodsName()%></td>
		<td align="left"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="left"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="left"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
		<td align="left"><%=(bill.getIsHandiwork()==null)?"":bill.getIsHandiwork().equals("1")?"自动开票":bill.getIsHandiwork().equals("2")?"人工审核":"人工开票"%></td>
		<td align="left"><%=(bill.getIssueType()==null)?"":bill.getIssueType().equals("1")?"单笔":bill.getIssueType().equals("2")?"合并":"拆分"%></td>
		<td align="left"><%=(bill.getFapiaoType()==null)?"":bill.getFapiaoType().equals("0")?"增值税专用发票":"增值税普通发票"%></td>
		<td align="left"><%=(bill.getDataStatus()==null)?"":bill.getDataStatus().equals("5")?"已开具":bill.getDataStatus().equals("6")?"已开具":"打印失败"%></td> 
		<td align="center">
				<a href='<%=webapp%>/inputBillTrans.action?billCode=<%=bill.getBillCode()%>&billNo=<%=bill.getBillNo()%>'>
			    <img src="<%=bopTheme%>/theme/default//img/jes/icon/view.png" alt="查看" style="border-width: 0px;"/></a>
			    <a href="javascript:void(0);" 
			onClick="OpenModalWindowSubmit('viewImgFromBillPrint.action?billId=<%=bill.getBillId()%>',1000,650,true)">
			<img src ="<%=bopTheme2%>/img/jes/icon/view.png" alt="查看票样" style="border-width: 0px;" /></a>
		</td>
	</tr>
	<%
				}
			}
		}
	%>
	</tr>
	</table>
	</div>
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
	<table width="100%" cellspacing="0" border="0">
	<tr>
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
	<script type="text/javascript">
			var tophight = document.getElementById("tbl_main").offsetHeight;
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screen.availHeight -240-msgHight-tophight
	</script>
	</form>
</body>
</html>