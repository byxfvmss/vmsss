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
<%-- <script type="text/javascript" src="<%=webapp%>/page/js/supertable/jquery.superTable.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/supertable/superTables.js"></script>
<link  type="text/css" rel="stylesheet" href="<%=webapp%>/page/js/supertable/superTables.css"> --%>
<script type="text/javascript">
	$(function() {

		$("#addBtn").click(function() {
			var customerId = $("#customerId").val();
			if ("" == customerId) {
				alert("丢失客户编号");
				return false;
			}
			var url = "editCustomerAddress.action";
			url += "?customerAddressSearch.customerId=" + customerId;
			OpenModalWindow(encodeURI(url), 450, 500, true);
		});
		$("#delBtn").click(function() {
			submitForm("deleteCustomerAddress.action");
		});
		$("#returnBtn").click(function() {
			var url = "listCustomer.action";
			window.location.href = url;
		});
		
		$(".editLink").click(function() {
			var id = $(this).closest("td").find(".id").val();
			var customerId = $("#customerId").val();
			url="editCustomerAddress.action";
			url+="?customerAddressSearch.id="+id;
			url+="&customerAddressSearch.customerId="+customerId;
			OpenModalWindow(url,450, 500,true)
		});

		$("#searchBtn").click(function() {
			var addressTag = $("#addressTag").val();
			var customerId = $("#customerId").val();

			var url = "listCustomerAddress.action";
			url += "?customerAddressSearch.customerId=" + customerId;
			url += "&customerAddressSearch.addressTag=" + addressTag;
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
		
		/* $("#lessGrid").toSuperTable({width: "100%",height: "280px"}); */
	})
</script>
<style type="text/css">
.style1 {
	width: 242px;
}
</style>
</head>
<body>
	<form id="main" action="listCustomerAddress.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">客户管理</span>
						<span class="current_status_submenu">客户地址管理</span>
						<s:hidden id="customerId" name="customerAddressSearch.customerId"></s:hidden>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td align="left">地址别名:</td>
								<td>
									<s:textfield id="addressTag" cssClass="tbl_query_text" name="customerAddressSearch.addressTag"></s:textfield>
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
						<table class="lessGrid" id="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;width: 100%;">
							<tr class="lessGrid head" id="head">
								<th>
									<input id="CheckAll" type="checkbox" />
								</th>
								<th>收件人地址</th>
								<th>地址别名</th>
								<th>联系人</th>
								<th>联系人电话</th>
								<th>联系人邮箱</th>
								<th>收件人</th>
								<th>收件人电话</th>
								<th>邮编</th>
								<th>操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width:30px;">
										<input type="checkbox" name="checkedlineNo" value="<s:property value='id'/>" />
									</td>
									<td align="center" >
										<s:property value='receiverAddress' />
									</td>
									</td>
									<td align="center"  >
										<s:property value='addressTag' />
									</td>
									<td align="center"  >
										<s:property value='contactPerson' />
									</td>
									<td align="center"  >
										<s:property value='contactPhone' />
									</td>
									<td align="center">
										<s:property value='contactEmail' />
									</td>
									<td align="center">
										<s:property value='receiver' />
									</td>
									<td align="center">
										<s:property value='receiverPhone' />
									</td>
									<td align="center">
										<s:property value='postCode' />
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