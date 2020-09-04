<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@include file="../../../../page/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@ include file="../../../../page/include.jsp"%>

<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<link href="<c:out value="${bopTheme2}"/>/css/main.css" rel="stylesheet"
	type="text/css" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/window.js"></script>
<script charset="gb2312" language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/validator.js"></script>

<!-- MessageBox -->
<script
	src="<c:out value="${bopTheme}"/>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link
	href="<c:out value="${bopTheme}"/>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<title>新增分录参数设置</title>
<link type="text/css"
	href="<c:out value="${bopTheme2}"/>/css/subWindow.css" rel="stylesheet">

<%--
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	--%>
<script language="javascript" type="text/javascript">
	//标识页面是否已提交
	var subed = false;
	function createSubmit() {

		submitForm("saveAccEntry.action");
	}

	function submitForm(actionUrl) {
		var form = document.getElementById("formAccEntry");
		form.action = actionUrl;
		form.submit();
	}
</script>
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
		<form name="formAccEntry" id="formAccEntry"
			action="createAccEntry.action" method="post">
			<div id="editsubpanel" class="editsubpanel">
				<div style="overflow: auto; width: 100%;">
					<table id="contenttable" class="lessGrid" cellspacing="0"
						width="100%" align="center" cellpadding="0">
						<tr class="header">
							<th colspan="4">新增分录参数设置</th>
						</tr>
						<tr>
							<td align="right" class="listbar">科目</td>
							<td colspan="3"><s:select  cssStyle="width:100px"
									name="accEntry.AccTitB" list="accTitList" listKey="accTitleCode" listValue="accTitleName"  /></td>
						</tr>
						<tr>
							<td  align="right" class="listbar">币种</td>
							<td colspan="3"><s:select  cssStyle="width:100px"
									name="accEntry.currency" list="currencyList" listKey="value" listValue="text" value="#{'CNY'}" /></td>
						</tr>
						<tr>
							<td idth="20%" align="right" class="listbar">借方科目</td>
							<td width="30%"><s:select cssStyle="width:100px"
									name="accEntry.accTitD" list="accTitList" listKey="accTitleCode"
									listValue="accTitleName" ></s:select></td>
							<td idth="20%" align="right" class="listbar">取值</td>
							<td width="30%"><s:select cssStyle="width:100px"
									name="accEntry.transNumTypD" list="#{'1':'价税合计', '2':'收入','3':'税额'}"
									listKey="key" listValue="value"  headerKey="1" headerValue="价税合计"/></td>

						</tr>
						<tr>
							<td  align="right" class="listbar">货方科目1</td>
							<td ><s:select cssStyle="width:100px"
									name="accEntry.accTitC1" list="accTitList" listKey="accTitleCode"
									listValue="accTitleName" ></s:select></td>
							<td  align="right" class="listbar">取值</td>
							<td ><s:select cssStyle="width:100px"
									name="accEntry.transNumTypC1" list="#{'1':'价税合计', '2':'收入','3':'税额'}"
									listKey="key" listValue="value" headerKey="2" headerValue="收入"/></td>

						</tr>
						<tr>
							<td  align="right" class="listbar">货方科目2</td>
							<td ><s:select cssStyle="width:100px"
									name="accEntry.accTitC2" list="accTitList" listKey="accTitleCode"
									listValue="accTitleName" ></s:select></td>
							<td  align="right" class="listbar">取值</td>
							<td ><s:select cssStyle="width:100px"
									name="accEntry.transNumTypC2" list="#{'1':'价税合计', '2':'收入','3':'税额'}"
									listKey="key" listValue="value" headerKey="3" headerValue="税额"/></td>
						</tr>
					</table>
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" id="btn_save" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="createSubmit()" name="BtnSave" value="保存" id="BtnSave" />
				<input type="button" class="tbl_query_button"
					onMouseMove="this.className='tbl_query_button_on'"
					onMouseOut="this.className='tbl_query_button'"
					onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn" />
			</div>
		</form>
	</div>
</body>
</html>