<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
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
	<script type="text/javascript">
		// [查询]按钮
		function submit(){
			document.forms[0].submit();
		}
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="listBill.action" id="Form1">
	<%
		String currMonth = (String) request.getAttribute("currMonth");
	%>
	<input type="hidden" name="currMonth" id="currMonth" value="<%=currMonth%>"/>
	<input type="hidden" name="reasionInfo" id="reasionInfo" value=""/>
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<s:component template="rocketMessage" />
		<table id="tbl_current_status">
		<tr>
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" /> 
				<span class="current_status_menu">当前位置：</span> 
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>销项税查询
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
				<input class="tbl_query_time" id="billBeginDate" type="text" name="billBeginDate"
					value="<s:property value='billBeginDate'/>"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'billEndDate\')}'})"
					size='11'"/>
				至
				<input class="tbl_query_time" id="billEndDate" type="text" name="billEndDate"
					value="<s:property value='billEndDate'/>"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'billBeginDate\')}'})"
					size='11'"/>
			</td>
			<td align="left">
				发票代码:
			</td>
			<td>
				<input type="text" name="billCode" value="<s:property value='billInfo.billCode'/>" />
			</td>
			<td align="left">
				发票号码:
			</td>
			<td>
				<input type="text" name="billNo" value="<s:property value='billInfo.billNo'/>" />
			</td>
			<td align="left">
			</td>
			<td>
			</td>
		</tr>
		<tr>
			<td align="left">
				合计金额:
			</td>
			<td>
				<input type="text" name="sumAmtBegin" id="sumAmtBegin" value="<s:property value='sumAmtBegin'/>"
					onkeypress="checkkey(value);" />
				至
				<input type="text" name="sumAmtEnd" id="sumAmtEnd" value="<s:property value='sumAmtEnd'/>"
					onkeypress="checkkey(value);" />
			</td>
			<td align="left">
				客户纳税人名称:
			</td>
			<td>
				<input type="text" name="customerName" value="<s:property value='billInfo.customerName'/>" />
			</td>
			<td align="left">
				客户银行账号:
			</td>
			<td>
				<input type="text" name="customerBankandaccount" value="<s:property value='billInfo.customerBankandaccount'/>" />
			</td>
			<td align="right">
				<input type="button" class="tbl_query_button" value="查询"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submit()" />
				<input type="button" class="tbl_query_button" value="导出"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnView" onclick="submit()" />
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
		<th style="text-align:center">编辑</th>
		<th style="text-align:center">开票日期</th>
		<th style="text-align:center">票据代码</th>
		<th style="text-align:center">票据号码</th>
		<th style="text-align:center">客户纳税人名称</th>
		<th style="text-align:center">客户纳税人识别号</th>
		<th style="text-align:center">合计金额</th>
		<th style="text-align:center">合计税额</th>
		<th style="text-align:center">价税合计</th>
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
	%>
		<td align="center">
			<input style="width:13px;height:13px;" type="checkbox" name="selectBillIds" value="<%=BeanUtils.getValue(bill,"billId")%>" />
			<input type="hidden" name="selectBillDates" value="<%=bill.getBillDate()%>"/>
		</td>
		<td align="center">
			<a href="editBill.action?curPage=<s:property value='paginationList.currentPage'/>&instCode=<s:property value='instCode'/>&billId=<%=bill.getBillId()%>&fromFlag=editList"><img src ="<%=bopTheme2%>/img/jes/icon/edit.png" title="编辑票据" style="border-width: 0px;" /></a>
		</td>
		<!--
		<td align="center"><%//=bill.getBillNo()%></td>
		-->
		<td align="center"><%=bill.getBillDate()%></td>
		<td align="center"><%=bill.getBillCode()%></td>
		<td align="center"><%=bill.getBillNo()%></td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="left"><%=bill.getCustomerTaxno()%></td>
		<td align="right"><%=NumberUtils.format(bill.getAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(),"",2)%></td>
		<td align="right"><%=NumberUtils.format(bill.getSumAmt(),"",2)%></td>
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
		var msgHight = document.getElementById("anpBoud").offsetHeight;
		document.getElementById("list1").style.height = screenHeight - 440 - msgHight;
	</script>
	<script language="javascript">
	</script>
	</form>
</body>
</html>