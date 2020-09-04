<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.vms.trans.model.BillInfo"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.input.model.InputVatInfo"
	import="com.cjit.vms.trans.model.BillItemInfo"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../page/include.jsp"%>
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/editview.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/vms.js"></script>
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<link href="<%=bopTheme2%>/css/editView.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		function cancelBillSubmit() {
			var newStatuses = document.getElementsByTagName("select");
			var billIds = document.getElementsByName("billId");
			var statuses = "";
			var ids = "";
			for (var i=0; i< newStatuses.length; i++){
				statuses = statuses == "" ? newStatuses[i].value : statuses + "," + newStatuses[i].value;
				ids = ids == "" ? billIds[i].value : ids + "," + billIds[i].value;
			}
			$.ajax({url: 'backBill.action',
					type: 'POST',
					async:false,
					data:{newStatuses:statuses, ids:ids},
					dataType: 'html',
					timeout: 1000,
					error: function(){return false;},
					success: function(result){
						window.dialogArguments.cancelBillSuccess();
						window.close();
						//document.getElementById("BtnSubmit").disabled = true;
					}
					});
//			Form1.action = "backBill.action?newStatuses=" + statuses;
//			Form1.submit();
//			window.dialogArguments.cancelBillSuccess();
//			document.getElementById("BtnSubmit").disabled = true;
		}
		
	</script>
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="backBill.action" id="Form1">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
					<span class="actionIcon">-&gt;</span>发票跟踪
					<span class="actionIcon">-&gt;</span>撤销
					<span class="actionIcon">-&gt;</span>选择撤销状态
				</span>
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<div style="overflow:auto;width:100%;" id="list1">
	<table id="lessGridList" width="100%" class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" display="none" style="border-collapse: collapse;">
	<tr class="lessGrid head">
		<th style="text-align:center">序号</th>
		<th style="text-align:center">票据ID</th>
		<th style="text-align:center">客户纳税人名称</th>
		<th style="text-align:center">发票代码</th>
		<th style="text-align:center">发票号码</th>
		<th style="text-align:center">发票原状态</th>
		<th style="text-align:center">发票新状态</th>
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
		<td align="center"><%=i + 1%></td>
		<td align="center">
			<%=bill.getBillId()%>
			<input type="hidden" name="billId" value="<%=bill.getBillId()%>"/>
		</td>
		<td align="left"><%=bill.getCustomerName()%></td>
		<td align="center"><%=bill.getBillCode()%></td>
		<td align="center"><%=bill.getBillNo()%></td>
		<td align="center"><%=DataUtil.getDataStatusCH(bill.getDataStatus(), "BILL")%></td>
		<td align="center">
			<select name="status" style="width:135px">
				<option value="1">编辑待提交</option>
				<%if ("3".equals(bill.getDataStatus())) {
				%>	
				<option value="2">提交待审核</option>
				<%
				}%>
				
				<option value="3">票据删除</option>
			</select>
		</td>
	</tr>
	<%
				}
			}
		}
	%>
	</tr>
	</table>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="撤销提交"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnSubmit" onclick="cancelBillSubmit()"/>
				<input type="button" class="tbl_query_button" value="关闭"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="BtnView" id="BtnClose" onclick="window.close();" />
			</td>
		</tr>
		</table>
		</td>
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
//		var msgHight = document.getElementById("anpBoud").offsetHeight;
//		document.getElementById("list1").style.height = screenHeight - 360 - msgHight;
	</script>
	</form>
</body>
</html>