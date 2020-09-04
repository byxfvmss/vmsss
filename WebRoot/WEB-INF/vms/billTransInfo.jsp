<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@ include file="../../../page/modalPage.jsp"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<title>进项税查看发票信息明细</title>
	<!-- <link type="text/css" href="<c:out value="${bopTheme}"/>/css/subWindow.css" rel="stylesheet"> -->
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/subWindow.css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/inputInvoice.css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<body>
<div class="showBoxDiv">
<form name="formBusiness" id="formBusiness" style="background-color:#fff">

<div id="editsubpanel" class="editsubpanel" > 
<input type="hidden" name="editType" id="editType" value="add" />
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	
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
				<td align="center"><%=trans.getFapiaoType()==null?"":trans.getFapiaoType().equals("0")?"专":"普"%></td>
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
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px" width="120%">		

	<input type="button" class="tbl_query_button" 
		onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
		onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
	<script language="javascript" type="text/javascript">
	</script>
</form>
</div>
</body>
</html>
