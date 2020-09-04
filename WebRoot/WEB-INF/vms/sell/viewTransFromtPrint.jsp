<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
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
	<script>  
	function revoke(){
		submitAction(document.forms[0], "listBillPrint.action?fromFlag=view");
	}
	</script>
	
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="" id="Form1">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">销项税管理</span>
			<span class="current_status_submenu">开票管理</span>
			<span class="current_status_submenu">发票打印</span>
			<span class="current_status_submenu">查看交易</span>
		</div>
  	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<%
		List transList = (List)request.getAttribute("transList");
		if(transList!=null&&transList.size()>0){
	%>	
					<tr>
						<th style="text-align:center">序号</th>
						<th style="text-align:center">交易时间</th>
						<th style="text-align:center">客户名称</th>
						<th style="text-align:center">交易类型</th>
						<th style="text-align:center">交易金额</th>
						<th style="text-align:center">税率</th>
						<th style="text-align:center">税额</th>
						<th style="text-align:center">价税合计</th>
						<th style="text-align:center">未开票金额</th>
						<th style="text-align:center">发票类型</th>
						<th style="text-align:center">交易状态</th>
					</tr>
			<%
						for(int i=0; i<transList.size(); i++){
							TransInfo trans = (TransInfo)transList.get(i);
							if(i%2==0){
			%>
			<tr class="lessGrid rowA">
			<%
							}else if(i%2!=0){
			%>
			<tr class="lessGrid rowB" >
			<%
							}
			%>
				<td align="center"><%=i+1%></td>
				<td align="center"><%=trans.getTransDate()==null?"":trans.getTransDate()%></td>
				<td align="center"><%=trans.getCustomerName()==null?"":trans.getCustomerName()%></td>
				<td align="center"><%=trans.getTransType()==null?"":trans.getTransType()%></td>
				<td align="center"><%=NumberUtils.format(trans.getAmt(),"",2)%></td>
				<td align="center"><%=NumberUtils.format(trans.getTaxRate(),"",2)%></td>
				<td align="center"><%=NumberUtils.format(trans.getTaxAmt(),"",2)%></td>
				<td align="center"><%=NumberUtils.format(trans.getAmt(),"",2)%></td>
				<td align="center"><%=NumberUtils.format(trans.getBalance(),"",2)%></td>
				<td align="center"><%=trans.getFapiaoType()==null?"":trans.getFapiaoType().equals("0")?"增值税专用发票":"增值税普通发票"%></td>
				<!-- 状态 1-未开票/2-开票编辑锁定中/3-开票中/4-删除/5-被冲账/6-已冲账/99-开票完成 -->
				<td align="center"><%=trans.getDataStatus()==null?"":trans.getDataStatus().equals("1")?"未开票":
				trans.getDataStatus().equals("2")?"开票编辑锁定中":trans.getDataStatus().equals("3")?"开票中":trans.getDataStatus().equals("4")?"删除":
				trans.getDataStatus().equals("5")?"被冲账":trans.getDataStatus().equals("6")?"已冲账":trans.getDataStatus().equals("99")?"开票完成":""%></td>
			</tr>
			<%
						}
		}else{
	%>
		<td align="center">无法根据发票代码、发票号码找到对应的交易信息！</td>
	
	<%} %>
	
	</table>
	</div>
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
		<table width="100%" cellspacing="0" border="0">
			<tr>
				<td align="right"><s:component template="pagediv"/></td>
			</tr>
		</table>
	</div>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="返回"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btCancel" id="btCancel" onclick="revoke()" />
			</td>
		</tr>
	</table>
			</td>
	</tr>
	</table>
	</form>
</body>
</html>