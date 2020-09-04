<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputInvoiceInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
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
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/window.js"></script>
	<link href="<c:out value="${bopTheme2}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script type="text/javascript">
		// [查询]按钮
		function submitBill(){
				submitAction(document.forms[0], "listInputCompare.action");
		}
		
		function submitExport(){
				submitAction(document.forms[0], "exportinputInvoiceInfo.action");
		}
		
		function goBack(){
			  window.opener.window.location.reload(true);  
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
		#contenttable td{ border-bottom:#D4D5D7 solid 1px; line-height:30px;}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listInputCompare.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">进项税管理</span>
			<span class="current_status_submenu">发票勾稽</span>
			<span class="current_status_submenu">发票勾稽</span>
			<span class="current_status_submenu">查看</span>
		</div>
		<div id="lessGridList16" style="overflow: auto; width: 100%;">
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
				<tr>
					<input type="hidden" name="billId" value="<s:property value="inputInvoiceInfo.billId"/>"/>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票代码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.billCode'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">发票号码:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.billNo'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">开票日期:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.billDate'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商中文名称:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorName'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商纳税人识别号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorTaxno'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商地址 电话:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorAddressandphone'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">供应商银行账号:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.vendorBankandaccount'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计金额:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.amtSum'/>
					</td>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">合计税额:&nbsp;&nbsp;&nbsp;</td>
					<td style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.taxAmtSum'/>
					</td>
				</tr>
				<tr>
					<td width="15%" style="background-color:#F0F0F0;font-weight:bold;color:#727375;text-align: right;">价税合计金额:&nbsp;&nbsp;&nbsp;</td>
					<td colspan="5" style="background-color:#F0F0F0;color:#727375;">
						<s:property value='inputInvoiceInfo.sumAmt'/>
					</td>
				</tr>
			</table>
			<div>
				<iframe id="subTableFrame" scrolling="auto" src="listBillItem.action?flag='query'&billId=<s:property value='inputInvoiceInfo.billId'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>
			<div>
				<iframe id="subTableFrame" scrolling="auto" src="listInputTransByVen.action?vendorId=<s:property value='inputInvoiceInfo.vendorTaxno'/>&billId=	<s:property value='inputInvoiceInfo.billId'/>" height="160px" width="100%" frameborder="0"></iframe>
			</div>			
		</div>		
		<div id="ctrlbutton" class="mtop10" align="Right">
			<input type="button" class="tbl_query_button" value="返回"
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
				name="btGoBack" id="btGoBack" onclick="javascript:history.go(-1)" />
		</div>
		</td>
	</tr>
	</table>
	<script language="javascript" type="text/javascript" charset="utf-8">
	</script>
	</form>
</body>
</html>