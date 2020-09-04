<!--file: <%=request.getRequestURI() %> -->
<%@page import="com.cjit.vms.input.model.InputMonitor"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>进项监控</title>
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
	<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/page/js/window.js" charset="UTF-8"></script>
	<script type="text/javascript">
			function submit(){
	
			submitAction(document.forms[0], "listMonitor1.action");
			document.forms[0].action="listMonitor1.action";
		}
	
		// [提交]按钮
		function submitBill(parma){
				document.getElementById("parma").value=parma;
				submitAction(document.forms[0], "listMonitor1.action");
				document.forms[0].action="listMonitor1.action";
			
		}
		
		// [提交]按钮submitExport
		function submitExport(){
				submitAction(document.forms[0], "exportMonitorInput.action");
				document.forms[0].action="listMonitor1.action";
			
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listMonitor1.action" id="Form1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
		<s:component template="rocketMessage" />
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
			<span class="current_status_menu">当前位置：</span> 
			<span class="current_status_submenu1">参数管理</span>
			<span class="current_status_submenu">增值税监控</span>
			<span class="current_status_submenu">进项监控</span>
		</div>
		<div class="widthauto1">
		<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
			<tr align="left">
				<td>时间</td>
				<td><input id="startTime" name="monitorInput.startDate" type="text"  value="<s:property value='monitorInput.startDate' />" 
				class="tbl_query_time" onFocus="WdatePicker()" />&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;<input id="endTime" name="monitorInput.endDate" type="text"	
				value="<s:property value='monitorInput.endDate' />" class="tbl_query_time" onFocus="WdatePicker()" /></td>
				<td>机构</td>
				<td><select id="inst_id" name="monitorInput.instName" ><option value="" <s:if test='inst_id==""'>selected</s:if><s:else></s:else>>全部</option>
						<s:iterator value="lstAuthInstId" id="org">  
						<option value="<s:property value="id"/>" <s:if test='monitorInput.instName==#org.id'>selected</s:if><s:else></s:else>><s:property value="name"/></option>
				       </s:iterator>
				      
				</select></td>
				<td>供应商</td>
				<td><input id="instId" class="tbl_query_text" name="monitorInput.vendorName" type="text" value="<s:property value='monitorInput.vendorName' />" /></td>
				<td style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" 
				onclick="submitBill('<s:property value="#request.parma"/>')" name="cmdFilter" value="查询" id="cmdFilter" />				
				</td>	<input type="hidden" name="monitorInput.flag" value="true"/>
				<input id="parma" type="hidden"  name="parma" value="<s:property value='#request.parma'/>"/>
			</tr><%--
				<s:property value="#request.parma"/>
		--%></table>
		</div>
<!--		<table id="tbl_tools" width="100%" border="0">-->
<!--		<tr>-->
<!--			<td align="left"><%--instCode transType vendorTaxno-->
<!--				--%><input type="button" class="tbl_query_button" value="机构"-->
<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
<!--					 id="BtnView" onclick="submitBill('instCode')" />-->
<!--				<input type="button" class="tbl_query_button" value="供应商"-->
<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
<!--					 id="BtnView" onclick="submitBill('vendorTaxno')" />-->
<!--				 <input type="button" class="tbl_query_button" value="交易种类"-->
<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
<!--					id="BtnView" onclick="submitBill('transType')" /> -->
<!--				<input type="button" class="tbl_query_button" value="导出"-->
<!--					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"-->
<!--					id="BtnView" onclick="submitExport()" />-->
<!--			</td>-->
<!--		</tr>-->
<!--		</table> -->
 		<table id="tbl_tools" width="100%" border="0">
			<tr>
				<td align="left">
				<a href="#" onclick="submitBill('instCode')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1038.png"/>机构</a>
				<a href="#" onclick="submitBill('vendorTaxno')"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1039.png"/>供应商</a>
				<a href="#" onclick="submitExport()"><img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1003.png"/>导出</a>
				</td>
			</tr>
		</table>
  	<div id="lessGridList1" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
	<tr>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" colspan="6">申报抵扣的进项税额</th>
		<th style="text-align: center" colspan="3">进项税额转出额</th>
		<th style="text-align: center" colspan="9">待抵扣进项税额</th>
	</tr>
	<tr>
		<th style="text-align: center" >序号</th>
		<th style="text-align: center" >机构</th>
		<th style="text-align: center" >供应商</th>
		<th style="text-align: center" colspan="3">本期认证相符且本期申报抵扣</th>
		<th style="text-align: center" colspan="3">前期认证相符且本期申报抵扣</th>
		<th style="text-align: center" colspan="3">本期进项税转出额</th>
		<th style="text-align: center" colspan="3">期初已认证相符但未申报抵扣</th>
		<th style="text-align: center" colspan="3">本期认证相符且本期未申报抵扣</th>
		<th style="text-align: center" colspan="3">期末已认证相符但未申报抵扣</th>
	</tr>
	<tr>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
		<th style="text-align: center">分数</th>
		<th style="text-align: center">金额</th>
		<th style="text-align: center">税额</th>
	</tr>
	<%PaginationList paginationList = (PaginationList)request.getAttribute("paginationList");
	if (paginationList != null){
		List monList = paginationList.getRecordList();
	
			if (monList != null && monList.size() > 0){
				for(int i=0; i<monList.size(); i++){
					InputMonitor mon = (InputMonitor)monList.get(i);
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
		
		<td align="center"><%=i + 1%></td>
		<td align="center"><%=mon.getInstName()==null ? "-" : mon.getInstName()%></td>
		<td align="center"><%=mon.getVendorName()==null ? "-" : mon.getVendorName()%></td>
		<td align="center"><%=mon.getCount1()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum1()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum1(),"",2)%></td>
		<td align="center"><%=mon.getCount2()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum2()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum2(),"",2)%></td>
		<td align="center"><%=mon.getCount3()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum3()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum3(),"",2)%></td>
		<td align="center"><%=mon.getCount4()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum4()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum4(),"",2)%></td>
		<td align="center"><%=mon.getCount5()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum5()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum5(),"",2)%></td>
		<td align="center"><%=mon.getCount6()%></td>
		<td align="left"><%=NumberUtils.format((mon.getAmtSum6()),"",2)%></td>
		<td align="left"><%=NumberUtils.format(mon.getTaxAmtSum6(),"",2)%></td>
		</tr>
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
	</form>
</body>
</html>