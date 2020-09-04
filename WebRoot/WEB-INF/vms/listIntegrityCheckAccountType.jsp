<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">

<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/editview.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>完整性对账</title>

<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =         90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("main");
		form.action = actionUrl;
		form.submit();
	}
</script>
</head>
<body>
	<form id="main" action="integrityCheckAccountType.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0"
			class="tablewh100">
			<input type="hidden" name="instCode" value="<s:property value='instCode' />" />
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
							class="current_status_menu">当前位置：</span> <span
							class="current_status_submenu1">税项监控</span> <span
							class="current_status_submenu">增值税对账</span> <span
							class="current_status_submenu">完整性对账</span> <span
							class="current_status_submenu">查看交易类型</span>
					</div>
					<div id="lessGridList4" class="mtop10"
						style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0"
							cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th style="text-align: center">序号</th>
								<th style="text-align: center">商品名称</th>
								<th style="text-align: center">金额</th>
								<th style="text-align: center">净价</th>
								<th style="text-align: center">税额</th>
								<th style="text-align: center">差异</th>
								<th style="text-align: center">操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList"
								status="stuts">
								<tr align="center"
									class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><s:property value='#stuts.count' /></td>
									<td><s:property value='goodsName' /></td>
									<td><s:property value='amount' /></td>
									<td><s:property value='netPrice' /></td>
									<td><s:property value='taxAmt' /></td>
									<td><s:property value='devAmt' /></td>
									<td>
									<a href="javascript:;"
									onClick='OpenModalWindow("<c:out value='${webapp}'/>/integrityCheckAccountDetail.action?goodsNo=<s:property value='goodsNo' />&instCode=<s:property value='instCode' />",900, 500, false)'>明细</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right"><s:component template="pagediv" /></td>
							</tr>
						</table>
				<table id="tbl_tools" width="100%" border="0">
					<tr>
						<td align="left"><input type="button"
							class="tbl_query_button" value="返回"
							onMouseMove="this.className='tbl_query_button_on'"
							onMouseOut="this.className='tbl_query_button'" name="btCancel"
							id="btCancel" onclick="history.back(-1)" />
						</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
	</form>
	<c:import url="${webapp}/page/webctrl/instTree/tree_include.jsp"
		charEncoding="UTF-8">
		<c:param name="treeType" value="radio" />
		<c:param name="bankName_tree" value="receiveInstName" />
		<c:param name="bankId_tree" value="receiveInstId" />
		<c:param name="taskId_tree" value="" />
		<c:param name="task_tree" value="task_tree" />
		<c:param name="webapp" value="${webapp}" />
	</c:import>
</body>
<script type="text/javascript">
	$(function() {
		$(".history_bt")
				.click(
						function() {
							$diff = $(this).attr("rel");
							if ($diff == '0') {
								alert("无差异");
								return false;
							}
							OpenModalWindow(
									"<c:out value='${webapp}'/>/integrityCheckAccountDetail.action",
									900, 500, false);
						});
	})
</script>
</html>















