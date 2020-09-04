<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script charset="gbk" language="javascript" type="text/javascript" src="<%=bopTheme%>/js/validator.js"></script>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function() {

		$("#addBtn").click(function() {
			var customerId = $("#customerId").val();
			if ("" == customerId) {
				alert("丢失客户编号");
				return false;
			}
			var url = "createSubCustomer.action";
			url += "?subCustomerSearch.customerId=" + customerId;
			OpenModalWindowSubmit(encodeURI(url), 700, 500, true,'add');
		});
		$("#delBtn").click(function() {
			submitForm("deleteSubCustomer.action");
		});
		$("#returnBtn").click(function() {
			var url = "listCustomer.action";
			window.location.href = url;
		});
		
		$(".editLink").click(function() {
			var subCustomerId = $(this).closest("td").find(".subCustomerId").val();
			var customerId = $("#customerId").val();
			url="editSubCustomer.action";
			url+="?subCustomerSearch.subCustomerId="+subCustomerId;
			url+="&subCustomerSearch.customerId="+customerId;
			OpenModalWindowSubmit(url,700, 500,'view')
		});

		$("#searchBtn").click(function() {
			//var form = document.getElementById("main");
			//form.action = "listSubCustomer.action";
			//form.submit();
			//form.action = "listSubCustomer.action";
			
			var subCustomerName = $("#subCustomerName").val();
			subCustomerName = encodeURI(encodeURI(subCustomerName));
			var customerId = $("#customerId").val();

			var url = "listSubCustomer.action";
			url += "?subCustomerSearch.customerId=" + customerId;
			url += "&subCustomerSearch.subCustomerName=" + subCustomerName;
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
	<form id="main" action="listSubCustomer.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<img src="<%=bopTheme%>/themes/images/icons/icon13.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">销项税管理</span>
						<span class="current_status_submenu">客户管理</span>
						<span class="current_status_submenu">子公司管理</span>
						<s:hidden id="customerId" name="subCustomerSearch.customerId"></s:hidden>
					</div>
					<div class="widthauto1">
						<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td align="left">子公司名称:</td>
								<td>
									<s:textfield id="subCustomerName" cssClass="tbl_query_text" name="subCustomerSearch.subCustomerName"></s:textfield>
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
								<th>子公司ID</th>
								<th>子公司名称</th>
								<th>子公司识别号</th>
								<th>子公司地址</th>
								<th>子公司电话</th>
								<th>子公司开户行</th>
								<th>子公司开户行账号</th>
								<th>客户纳税人类别</th>
								<th>操作</th>
							</tr>
							<s:iterator value="paginationList.recordList" id="iList" status="stuts">
								<tr align="center" class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td align="center" style="width:30px;">
										<input type="checkbox" name="checkedlineNo" value="<s:property value='subCustomerId'/>" />
									</td>
									<td align="center" >
										<s:property value='subCustomerId' />
									</td>
									</td>
									<td align="center"  >
										<s:property value='subCustomerName' />
									</td>
									<td align="center"  >
										<s:property value='subCustomerTaxno' />
									</td>
									<td align="center"  >
										<s:property value='subCustomerAddressand' />
									</td>
									<td align="center">
										<s:property value='subCustomerPhone' />
									</td>
									<td align="center">
										<s:property value='subCustomerBankand' />
									</td>
									<td align="center">
										<s:property value='subCustomerAccount' />
									</td>
									<td align="center">
										<s:if test='taxpayerType=="G"'>
											一般纳税人
										</s:if>
										<s:if test='taxpayerType=="S"'>
											小规模纳税人
										</s:if>
										<s:if test='taxpayerType=="O"'>
											其他
										</s:if>
									</td>
									<td style="width: 10%">
										<s:hidden cssClass="subCustomerId" name="subCustomerId"></s:hidden>
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
<script language="javascript">
	function OpenModalWindowSubmit(newURL, width, height, needReload, s) {
		var retData = false;
		if (typeof (width) == 'undefined') {
			width = screen.width * 0.9;
		}
		if (typeof (height) == 'undefined') {
			height = screen.height * 0.9;
		}
		if (typeof (needReload) == 'undefined') {
			needReload = false;
		}
		retData = showModalDialog(newURL, window, "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
		if (needReload && retData) {
			var reurl="listSubCustomer.action";
			submitAction(document.forms[0], reurl);
			document.forms[0].action = reurl;

		}
	}
	
	function beforeDeleteCustomerlk(actionName) {
		var t = "";
		var inputs = document.getElementsByName('customerIdList');
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请先选择要删除的客户！");
			return;
		}

		if (confirm('确认删除所选客户？')) {
			document.forms[0].action = actionName;
			document.forms[0].submit();
			document.forms[0].action = "listSubCustomer.action";
		}

	}
	function beforeDeleteCustomer(actionName) {
		var t = "";
		var inputs = document.getElementsByName('customerIdList');
		for (var i = 0; i < inputs.length; i++) {
			if (inputs[i].checked == true) {
				t += inputs[i].value + ",";
			}
		}
		if (t.length == 0) {
			alert("请先选择要删除的客户！");
			return;
		}

		if (confirm('确认删除所选客户？')) {
			//document.forms[0].action = actionName;
			deletebefore(t);

		}

	}
	function deletebefore(t) {
		$.ajax({
			url : 'deleteSubCustomer.action',

			type : 'POST',
			async : false,
			data : {
				customerIdList : t
			},
			dataType : 'text',
			//	timeout: 1000,
			error : function() {
				return false;
			},
			success : function(result) {

				alert(result);
				document.forms[0].submit();
				document.forms[0].action = "listSubCustomer.action";

			}
		});
	}
</script>
</html>