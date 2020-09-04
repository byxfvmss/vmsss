<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.vms.input.model.InputInvoice"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="updateRemark.action" id="Form1">
	<input type="hidden" id="billId" value="<s:property value='inputInvoice.billId'/>"/>
	<input type="hidden" id="fapiaoType" value="<s:property value='inputInvoice.fapiaoType'/>"/>
	<div id="tbl_current_status">
		<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
		<span class="current_status_menu">当前位置：</span> 
		<span class="current_status_submenu1">进项税管理</span>
		<span class="current_status_submenu">红冲管理</span>
		<span class="current_status_submenu">红冲申请</span>
		<span class="current_status_submenu">查看退回原因</span>
	</div>
	<div class="centercondition">
		<div class="blankbox" id="blankbox" style="overflow:auto;height: 100%;" >
			<table id="contenttable" class="lessGridS"  cellspacing="0" width="100%" align="center" cellpadding="0" style="border-collapse: collapse;">
			<tr>
				<td align="right" width="20%">
					开票日期
				</td>
				<td align="left" width="50%" >
					<input type="text" class="tbl_query_text_readonly" name="inputInvoice.billDate" id="inputInvoice.billDate" value="<s:property value='inputInvoice.billDate'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" width="20%">
					客户纳税人名称
				</td>
				<td align="left" width="50%">
					<input type="text" class="tbl_query_text_readonly" name="inputInvoice.vendorName" id="inputInvoice.vendorName" value="<s:property value='inputInvoice.vendorName'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" width="20%">
					发票代码
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text_readonly" name="inputInvoice.billCode" id="inputInvoice.billCode" value="<s:property value='inputInvoice.billCode'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" width="20%">
					发票号码
				</td>
				<td align="left">
					<input type="text" class="tbl_query_text_readonly" name="inputInvoice.billNo" id="inputInvoice.billNo" value="<s:property value='inputInvoice.billNo'/>" size="80" readonly />
				</td>
			</tr>
			<tr>
				<td align="right" style="vertical-align: middle">
					退回原因
				</td>
				<td>
					<textarea name="inputInvoice.cancelReason" id="cancelReason" cols="80" rows="5" readonly="readonly"><s:property value='inputInvoice.cancelReason'/></textarea>
				</td>
			</tr>
			</table>
		</div>
		<div class="bottombtn">
			<input type="button" class="tbl_query_button" value="返回"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			name="btCancel" id="btCancel" onclick="window.close();" />
		</div>
	</div>
	</form>
<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 270;
	var hightValueStr = "height:"+ hightValue + "px";
	
	
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
}
</script>
</body>
</html>