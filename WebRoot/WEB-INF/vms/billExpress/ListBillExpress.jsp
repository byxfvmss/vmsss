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

		function checkChecekedLine() {
			if ($("[name=checkedlineNo]:checked").size() == 0) {
				alert("请选择数据");
				return false;
			}
			return true;
		}
		function checkCustomer() {
			var customerIdList = $("[name=checkedlineNo]:checked")
					.closest("tr").find(".customerId");

			var customerId = $(customerIdList[0]).val();
			for (var j = 1; j < customerIdList.size(); j++) {
				var customerId2 = $(customerIdList[j]).val();
				if (customerId != customerId2) {
					alert("请选择相同的客户");
					return false;
				}
			}
			return true;
		}

		function checkReceiveStatus() {
			var customerIdList = $("[name=checkedlineNo]:checked")
					.closest("tr").find(".receiveStatus");
			for (var i = 0; i < customerIdList.size(); i++) {
				if ("0" == $(customerIdList[i]).val()
						|| "2" == $(customerIdList[i]).val()) {

				} else {
					alert("选择的数据存在签收完成或未录入的数据");
					return false;
				}

			}
			return true;
		}
		$("#addBtn").click(
				function() {
					if (!checkChecekedLine()) {
						return false;
					}
					if (!checkCustomer()) {
						return false;
					}

					var customerId = $("[name=checkedlineNo]:checked").closest(
							"tr").find(".customerId").val();

					$("#customerId").val(customerId);
					submitForm("editBillExpress.action");
				});
		$("#delBtn").click(function() {
			if (!checkChecekedLine()) {
				return false;
			}
			submitForm("removeBillExpress.action");
		});

		$("#finishBtn").click(function() {
			if (!checkChecekedLine()) {
				return false;
			}
			if (!checkReceiveStatus()) {
				return false;
			}

			var url = "signBillExpress.action";
			submitForm(url);
		});
		$(".editLink").click(function() {
			var id = $(this).closest("td").find(".id").val();
			var customerId = $(this).closest("td").find(".customerId").val();
			url = "editBillExpress.action";
			url += "?customerId=" + customerId;
			url += "&billExpressSearch.billId=" + id;
			window.location.href = url
		});

		$("#searchBtn").click(function() {

			var url = "listBillExpress.action";

			submitForm(url);
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
	<form id="main" action="listBillExpress.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">开票管理</span>
						<span class="current_status_submenu">发票快递</span>
						<s:hidden name="customerId"></s:hidden>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td align="left">客户编号</td>
								<td>
									<s:textfield cssClass="tbl_query_text2" name="billExpressSearch.customerId"></s:textfield>
								</td>
								<td align="left">客户名称</td>
								<td>
									<s:textfield cssClass="tbl_query_text2" name="billExpressSearch.customerName"></s:textfield>
								</td>
								<td align="left">取票方式</td>
								<td>
									<s:select id="receiveType" cssStyle="width:200px" name="billExpressSearch.receiveType" list="receiveTypeList" listKey="valueStandardLetter" listValue="name" headerKey="" headerValue=""></s:select>
								</td>
								<td align="left">收件状态</td>
								<td>
									<s:select id="receiveType" cssStyle="width:200px" name="billExpressSearch.receiveStatus" list="receiveStatusList" listKey="valueStandardLetter" listValue="name" headerKey="" headerValue=""></s:select>
								</td>
								<td style="text-align:left">
									<input type="button" class="tbl_query_button" value="查询" id="searchBtn" />
								</td>
							</tr>
						</table>
						<table id="tbl_tools" width="100%" border="0">
							<tr>
								<td align="left">
									<a href="#" id="addBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon16.png" />
										新增
									</a>
									<a href="#" id="delBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1002.png" />
										清除
									</a>
									<a href="#" id="finishBtn">
										<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1006.png" />
										签收
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
								<th>发票代码</th>
								<th>发票号码</th>
								<th>开票金额</th>
								<th>客户编号</th>
								<th>客户名称</th>
								<th>取票方式</th>
								<!-- <th>取件人类型</th> -->
								<th>取件人 / 联系人名称</th>
								<th>取件人证件号码</th>
								<th>寄送地址</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width:30px;">
										<input type="checkbox" name="checkedlineNo" value="<s:property value='billId'/>" />
									</td>
									<td align="center">
										<s:property value='billCode' />
									</td>
									<td align="center">
										<s:property value='billNo' />
									</td>
									<td align="right">
										<%-- <fmt:formatNumber value="<s:property value='sumAmt' />" pattern="#,##0.00"/>  --%>
										<fmt:formatNumber value="${sumAmt}" pattern="#,##0.00" />
									</td>
									<td align="center">
										<s:property value='customerId' />
									</td>
									<td align="center">
										<s:property value='customerName' />
									</td>
									<td align="center">
										<s:hidden name="receiveType"></s:hidden>
										<s:property value="receiveTypeList.{^#this.valueStandardLetter==#iList.receiveType}.{name}[0]" />
									</td>
									<%-- <td align="center">
										<s:property value='receiverType' />
									</td> --%>
									<td align="center">
										<s:property value='contactPerson' />
										<s:property value='receiverName' />
									</td>
									<td align="center">
										<s:property value='documentsCode' />
									</td>
									<td align="center">
										<s:property value='receiverAddress' />
									</td>
									<td align="center">
										<%-- <s:property value='receiveStatus' /> --%>
										<%-- <s:iterator value="receiveStatusList" var="i">
											<s:property value="#i.valueStandardLetter ==receiveStatus?#i.name:'' " />
										</s:iterator> --%>
										<s:hidden cssClass="receiveStatus" name="receiveStatus"></s:hidden>
										<s:property value="receiveStatusList.{^#this.valueStandardLetter==#iList.receiveStatus}.{name}[0]" />
									</td>
									<td>
										<s:hidden cssClass="id" name="billId"></s:hidden>
										<s:hidden cssClass="customerId" value="%{customerId}"></s:hidden>
										<a href="#" class="editLink">
											<img src="<c:out value="${bopTheme}"/>/themes/images/icons/icon1005.png" title="编辑" style="border-width: 0px;" />
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</div>
					<div id="anpBoud" align="Right" style="width:100%;">
						<input type="hidden" value="false" name="paginationList.showCount">
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