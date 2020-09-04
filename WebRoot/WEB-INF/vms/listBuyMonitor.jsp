<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.system.model.Monitor"
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
		function submitexport(){
			
				
			submitAction(document.forms[0], "exportMonitor.action");
			document.forms[0].action="listBuyMonitor.action";
		}
	
		// [提交]按钮
		function submitBill(parma){
				document.getElementById("parma").value=parma;
				submitAction(document.forms[0], "listBuyMonitor.action");
				document.forms[0].action="listBuyMonitor.action";
			
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBuyMonitor.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">税项监控</span>
			<span class="current_status_submenu">增值税监控</span>
			<span class="current_status_submenu">销项监控</span>
		</div>

		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr align="left">
				<td>时间</td>
				<td><input id="startTime" name="monitor.beginDate" type="text"  value="<s:property value='monitor.beginDate' />" class="tbl_query_time" onFocus="WdatePicker()" />
				      &nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;<input id="endTime" name="monitor.endDate" type="text"	value="<s:property value='monitor.endDate' />" class="tbl_query_time" onFocus="WdatePicker()" /></td>
				<td>机构</td>
				<td>
				<input type="hidden" id="inst_id" name="monitor.instName" value='<s:property value="instId"/>'>
									<input type="text" class="tbl_query_text" id="inst_Name" name="instName" value='<s:property value="instName"/>' onclick="setOrg(this);" readonly="readonly">
								
		<%--		<input id="instId" class="tbl_query_text" name="monitor.instName" type="text" value="<s:property value='monitor.instName' />" /></td>
				<td>交易种类</td>
				<td><input id="instId" class="tbl_query_text" name="monitor.transName" type="text" value="<s:property value='monitor.transName' />" /></td>
				--%><td>客户名称</td>
				<td><input id="instId" class="tbl_query_text" name="monitor.customerName" type="text" value="<s:property value='monitor.customerName' />" /></td>
				   
				<td style="width:80px;" align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submitBill('<s:property value="#request.parma"/>')" />
			</td>
			<%--
				<s:property value="#request.parma"/>
		--%>
			</tr>
		</table>
		</div>
		<!-- <table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="机构"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					 id="BtnView" onclick="submitBill('instId')" /><input id="parma" type="hidden"  name="parma" value="<s:property value='#request.parma'/>"/>
				<input type="button" class="tbl_query_button" value="客户"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					 id="BtnView" onclick="submitBill('customerName')" />
				<input type="button" class="tbl_query_button" value="交易种类"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					id="BtnView" onclick="submitBill('trans_type')" />
				<input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					id="BtnView" onclick="submitexport()" />
			</td>
		</tr>
		</table> -->
		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left"><input id="parma" type="hidden"  name="parma" value="<s:property value='#request.parma'/>"/>
				<a href="#" onclick="submitBill('instId')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1038.png"/>机构</a>
				<a href="#" onclick="submitBill('customerName')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1041.png"/>客户</a><%--
				<a href="#" onclick="submitBill('trans_type')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1040.png"/>交易种类</a>
				--%><a href="#" onclick="submitexport()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
  	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" 
		border="0"  cellpadding="0" 
		style="border-collapse: collapse; width: 100%;">
	<tr class="lessGrid head">
	<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th  style="text-align: center" colspan='2'>专用发票</th>
		<th style="text-align: center" colspan='2'>普通发票</th>
		<th  style="text-align: center" colspan='2'>未开票金额</th>
		<th  style="text-align: center" colspan='2'>应纳税</th>
		<th  style="text-align: center" colspan='3'>差异</th>
	</tr>
	 <tr class="lessGrid head" style="text-align: center">
	 <th style="text-align: center" >序号</th>
		
	<s:if test="#request.parma=='instId'"><th style="text-align: center" >机构</th></s:if>
		<s:if test="#request.parma=='customerName'"><th style="text-align: center" >客户名称</th></s:if>	
		 <th>金额</th>
		 <th>税额</th>
		 <th>金额</th>
		 <th>税额</th>
		 <th>金额</th>
		 <th>税额</th>
		 <th>金额</th>
		 <th>税额</th>
		 <th>金额</th>
		 <th>税额</th>
		 <th>价稅合计</th>
		 
	 </tr>
	<%PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
	if (paginationList != null){
		List monList = paginationList.getRecordList();
	
			if (monList != null && monList.size() > 0){
				for(int i=0; i<monList.size(); i++){
					Monitor mon = (Monitor)monList.get(i);
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
		
		<td align="center"><%=mon.getNumber() %></td>
		<s:if test="#request.parma=='instId'"><td align="center"><%=mon.getInstName()%></td></s:if>
		<s:if test="#request.parma=='customerName'"><td align="center"><%=mon.getCustomerName()%></td></s:if>
		<td align="center"><%=NumberUtils.format(mon.getzAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getzTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getpAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getpTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getBalance(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getBalanceTax(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getDiffAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getDiffTaxAmtSum(),"",2)%></td>
		<td align="center"><%=NumberUtils.format(mon.getDiffSumAmt(),"",2)%></td>
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
		</td>
	</tr>
	</table>
	<script type="text/javascript">
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 355 - msgHight;
	</script>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp" charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="inst_Name" />
		<c:param name="bankId_tree" value="inst_id" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
</html>