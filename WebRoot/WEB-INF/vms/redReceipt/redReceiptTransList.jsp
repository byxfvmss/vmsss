<!--file: <%=request.getRequestURI()%> -->
<%@page import="com.cjit.vms.trans.model.RedReceiptTransInfo"%>
<%@page import="com.cjit.vms.trans.model.RedReceiptApplyInfo"%>
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal" import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/calendar_notime.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/XmlHttp.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/comm.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<script type="text/javascript">
	// [确定]按钮
	function commit() {
		if (checkChkBoxesSelected("selectBillIds")) {
			var billIds = document.Form1.selectBillIds;
			var billDates = document.Form1.selectBillDates;
			var flag = document.getElementById("flag").value;
			var canCancel = true;
			var billId = document.getElementById("billId").value;
			var chkBoexes = document.getElementsByName("selectBillIds");
			var j = 0;
			var ids = "";
			for (i = 0; i < chkBoexes.length; i++) {
				if (chkBoexes[i].checked) {
					ids += "," + chkBoexes[i].value;
				}
			}
			if (!confirm("确定添加选中交易？")) {
				return false;
			}

			submitAction(document.forms[0], "billInfoAndTransList.action?ids="
					+ ids + "&billId=" + billId + "&type=part&ticket=" + flag+"&fromFlag=first");
		} else {
			alert("请选择交易记录！");
		}
	}
	function cancel() {
		window.history.back(-1);
		/*var billId = document.getElementById("billId").value;
		submitAction(document.forms[0], "billInfoAndTransList.action?billId="+billId);*/
	}
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body onkeydow="enterkey(event)">
	<form name="Form1" method="post" action="redReceiptTransList.action"
		id="Form1">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition"><s:component template="rocketMessage" />
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> 
						<span class="current_status_menu">当前位置：</span> 
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">红冲管理</span>
						<span class="current_status_submenu">红冲申请</span>
						<span class="current_status_submenu">申请红冲</span>
						<span class="current_status_submenu">交易列表</span>
					</div>
	</br>
<!--		<div style="overflow: auto; width: 100%;" id="list1">-->
<!--			<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">-->
  	<div id="lessGridList" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align: center"><input
						style="width: 13px; height: 13px;" id="CheckAll" type="checkbox"
						onclick="cbxselectall(this,'selectBillIds')" /></th>
					<input type="hidden" value="<s:property value="flag"/>" id="flag" name="flag"/>
					<th style="text-align: center">序号</th>
					<th style="text-align: center">交易ID</th>
					<th style="text-align: center">交易时间</th>
					<th style="text-align: center">客户号</th>
					<th style="text-align: center">客户纳税人名称</th>
					<th style="text-align: center">交易类型</th>
					<th style="text-align: center">合计金额</th>
					<th style="text-align: center">合计税额</th>
					<th style="text-align: center">价税合计</th>
					<th style="text-align: center">税率</th>
					<th style="text-align: center">是否冲账</th>
					<th style="text-align: center">发票类型</th>
					<th style="text-align: center">交易标志</th>
					<th style="text-align: center">状态</th>
				</tr>
				<%
					PaginationList paginationList = (PaginationList) request.getAttribute("paginationList");
					if (paginationList != null) {
						List billInfoList = paginationList.getRecordList();
						if (billInfoList != null && billInfoList.size() > 0) {
							for (int i = 0; i < billInfoList.size(); i++) {
								RedReceiptTransInfo bill = (RedReceiptTransInfo) billInfoList.get(i);
								if (i % 2 == 0) {
				%>
				<tr class="lessGrid rowA">
					<%
						} else {
					%>
				<tr class="lessGrid rowB">
					<%
						}
					%>
					<td align="center"><input style="width: 13px; height: 13px;"
						type="checkbox" name="selectBillIds"
						value="<%=BeanUtils.getValue(bill, "transId")%>" /></td>
					<td align="center"><%=(i + 1)%></td>
					<td align="center"><%=bill.getTransId()%></td>
					<td align="center"><%=bill.getTransDate() != null ? bill
								.getTransDate() : ""%>
					</td>
					<input type="hidden" id="billId" value="<%=bill.getBillId()%>" name="billId"/>
					<td align="center"><%=bill.getCustomerId()%></td>
					<td align="center"><%=bill.getCustomerName()%></td>
					<td align="center"><%=bill.getTransType()%></td>
					<td align="right"><%=NumberUtils.format(bill.getAmtSum(), "", 2)%></td>
					<td align="right"><%=NumberUtils.format(bill.getTaxAmtSum(), "", 2)%></td>
					<td align="right"><%=NumberUtils.format(bill.getSumAmt(), "", 2)%></td>
					<td align="right"><%=NumberUtils.format(bill.getTaxRate(), "", 2)%></td>
					<td align="center"><%=DataUtil.getIsReverseCH(bill.getIsReverse())%></td>
					<td align="center"><%=DataUtil.getFapiaoTypeCH(bill.getFapiaoType())%></td>
					<td align="right"><%=DataUtil.getTransFlag(bill.getTransFlag())%></td>
					<td align="right"><%=DataUtil.getDataStatusCH(bill.getDatastatus(), "TRANS")%></td>
				</tr>
				<%
					}
						}
					}
				%>
			</table>
		</div>
		<div id="anpBoud" align="Right" style="overflow: auto; width: 100%;">
			<table width="100%" cellspacing="0" border="0">
				<tr>
					<td align="right"><s:component template="pagediv" /></td>
				</tr>
			</table>
		</div>
		<table id="tbl_tools" width="100%" border="0">
				<tr>
					<td align="left">
					<input type="button" class="tbl_query_button" value="确定"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" name="BtnView"
						id="BtnView" onclick="commit()" />
					<input type="button" class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" name="BtnView"
						id="BtnView" onclick="cancel()" />
					</td>
				</tr>
			</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>