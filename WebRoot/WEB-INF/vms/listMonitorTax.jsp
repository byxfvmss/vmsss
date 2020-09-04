<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.MonitorInput"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>监控</title>
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

	<script type="text/javascript">
	
	
		// [提交]按钮
		function submitBill(){
				submitAction(document.forms[0], "listMonitorTax.action");
				document.forms[0].action="listMonitorTax.action";
		}
		function submitExport(){
				submitAction(document.forms[0], "payTaxExport.action");
				document.forms[0].action="listMonitorTax.action";
			
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action=listMonitorTax.action id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">税项监控</span>
			<span class="current_status_submenu">增值税监控</span>
			<span class="current_status_submenu">应缴税金</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr align="left">
				<td>时间</td>
				<td><input id="startTime" name="monitorInput.startDate" type="text"  value="<s:property value='monitorInput.startDate' />" class="tbl_query_time" onFocus="WdatePicker()" />
				      &nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;<input id="endTime" name="monitorInput.endDate" type="text"	value="<s:property value='monitorInput.endDate' />" class="tbl_query_time" onFocus="WdatePicker()" /></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机构</td>
				<td><input id="instId" class="tbl_query_text" name="monitorInput.instCode" type="text" value="<s:property value='monitorInput.instCode' />" /></td>
				<td style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitBill('<s:property value="#request.parma"/>')" name="cmdFilter" value="查询" id="cmdFilter" />				
				</td>
				<!-- <td><input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					id="BtnView" onclick="submitExport()" /></td> -->
			</tr><%--<s:property value="#request.parma"/>--%>
		</table>
		</div>
			<table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left">
					<a href="#" onclick="submitExport()" ><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
					</td>
				</tr>
			</table>
		

  	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
		
				
		<th width="5%" style="text-align: center">序号</th>
		<th width="5%" style="text-align: center">机构</th>
		<th width="5%" style="text-align: center">销项税额</th>
		<th width="5%" style="text-align: center">进项税额</th>
		<th width="20%" style="text-align: center">进项转出</th>
		<th width="10%" style="text-align: center">应缴税金</th>
		
	</tr>
	 
	<%PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
	if (paginationList != null){
		List monList = paginationList.getRecordList();
	
			if (monList != null && monList.size() > 0){
				for(int i=0; i<monList.size(); i++){
					MonitorInput mon = (MonitorInput)monList.get(i);
					if(i%2==0){					

				%>
				<tr class="lessGrid rowA">
				<%
								}else{
				%>
				<tr class="lessGrid rowB">
				<%
								}
				%>
		
			
		<td align="center"><%=i+1%></td>
		<td align="center"><%=mon.getInstCode()%></td>
		<td align="center"><%=NumberUtils.format(mon.getOutTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getInputTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getBillVatOutAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format((mon.getOutTaxAmtSum().subtract(mon.getInputTaxAmtSum()).add(mon.getBillVatOutAmtSum())),"",2)%></td>
		
		
		</tr>
	<%
				}
			}
		}
	%>
	</tr>
	<input type="hidden" name="monitorInput.flag" value="true"/>
	</table>
	</div>
	<div id="anpBoud" align="Right" style="overflow:auto;width:100%;">
	<table width="100%" cellspacing="0" border="0">
	<tr>
		<td align="right"><s:component template="pagediv"/></td>
	</tr>
	</table>
	</div>
		</td>
	</tr>
	</table>
	</form>
</body>
</html>