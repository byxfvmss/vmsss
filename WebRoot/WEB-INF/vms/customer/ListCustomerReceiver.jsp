<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript">
	$(function() {

		$("#addBtn").click(function() {
			var customerId = $("#customerId").val();
			if ("" == customerId) {
				alert("丢失客户编号");
				return false;
			}
			var url = "editCustomerReceiver.action";
			url += "?customerReceiverSearch.customerId=" + customerId;
			OpenModalWindow(encodeURI(url), 450, 400, true);
		});
		$("#delBtn").click(function() {
			submitForm("deleteCustomerReceiver.action");
		});
		$("#returnBtn").click(function() {
			var url = "listCustomer.action";
			window.location.href = url;
		});
		$(".editLink").click(function() {
			var id = $(this).closest("td").find(".id").val();
			var customerId = $("#customerId").val();
			url="editCustomerReceiver.action";
			url+="?customerReceiverSearch.id="+id;
			url+="&customerReceiverSearch.customerId="+customerId;
			OpenModalWindow(url,450, 400,true)
		});

		$("#searchBtn").click(function() {
			var customerId = $("#customerId").val();
			
			var documentsCode = $("#documentsCode").val();
			var receiverName = $("#receiverName").val();

			var url = "listCustomerReceiver.action";
			url += "?customerReceiverSearch.documentsCode=" + documentsCode;
			url += "&customerReceiverSearch.receiverName=" + receiverName;
			url += "&customerReceiverSearch.customerId=" + customerId;
			window.location.href = url;
			//submitForm("selectItemRate.action");
		});

		function submitForm(actionUrl) {
			var form = $("#main");
			var oldAction = form.attr("action");
			form.attr("action", actionUrl);
			form.submit();
			form.attr("action", oldAction);
		}
		$("#CheckAll").click(function() {
			$("[name=checkedlineNo]").attr("checked", $(this).attr("checked"))
		});
	})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body>
	<form id="main" action="listCustomerReceiver.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">客户管理</span>
						<span class="current_status_submenu">客户收件人管理</span>
						<s:hidden id="customerId" name="customerReceiverSearch.customerId"></s:hidden>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								
								<td align="left">姓名:</td>
								<td>
									<s:textfield id="receiverName" cssClass="tbl_query_text" name="customerReceiverSearch.receiverName"></s:textfield>
								</td>
								<td align="left">证件号码:</td>
								<td>
									<s:textfield id="documentsCode" cssClass="tbl_query_text" name="customerReceiverSearch.documentsCode"></s:textfield>
								</td>
								<td style="text-align:left">
									<input type="button" class="tbl_query_button" value="查询" id="searchBtn" />
								</td>
							</tr>
						</table>
						<table id="tbl_tools" width="100%" border="0">
							<tr>
								<td align="left">
									<a href="#" id="addBtn" id="addBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
										新增
									</a>
									<a href="#" id="delBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1019.png" />
										删除
									</a>
									<a href="#" id="returnBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1032.png" />
										返回
									</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="lessGridList1" style="overflow: auto; width: 100%;">
						<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head">
								<th>
									<input id="CheckAll" type="checkbox" />
								</th>
								<th>姓名</th>
								<th>证件类型</th>
								<th>证件号码</th>
								<th>收件人类型</th>
								<th>操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width:30px;">
										<input type="checkbox" name="checkedlineNo" value="<s:property value='id'/>" />
									</td>
									<td align="center">
										<s:property value='receiverName' />
									</td>
									<td align="center">
										<s:property value='documentsTypeName' />
									</td>
									<td align="center">
										<s:property value='documentsCode' />
									</td>
									<td align="center">
										<s:property value='receiverTypeName' />
									</td>
									<td style="width: 10%">
										<s:hidden cssClass="id" name="id"></s:hidden>
										<a href="#" class="editLink" >
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<table width="100%" cellspacing="0" border="0">
							<tr>
								<td align="right">
									<s:component template="pagediv" />
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>