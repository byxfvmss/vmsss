<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<html>
<head>
<title>增值税管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript" charset="UTF-8">
	window.onload = function() {
		var hightValue = screen.availHeight - 270;
		var hightValueStr = "height:" + hightValue + "px";

		if (typeof (eval("document.all.blankbox")) != "undefined") {
			document.getElementById("blankbox").setAttribute("style",
					hightValueStr);
		}
	}

	function cancel() {
		var cancelReason = encodeURI(document.getElementById("cancelReason").value);
		cancelReason=encodeURI(cancelReason);
		if (cancelReason == '') {
			alert("退回原因不能为空");
			return false;
		}
		alert(cancelReason);
		var from = document.getElementById("Form1");
		from.action = "addAuditDataNo.action?cancelReason=" + cancelReason;
		from.submit();
		from.action="listAuditImpdata.action";
		close();
		window.dialogArguments.closeWindow();
	}
</script>
</head>
<body onLoad="" onmousemove="MM(event)" onmouseout="MO(event)">
	<form name="Form1" method="post" action="" id="Form1">
		<div id="tbl_current_status">
			<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
				class="current_status_menu">当前位置：</span> <span
				class="current_status_submenu1">销项税管理</span> <span
				class="current_status_submenu">开票管理</span> <span
				class="current_status_submenu">数据导入审核</span> <span
				class="current_status_submenu">添加退回原因</span>
		</div>
		<div class="centercondition">

			<div id="bankbox" style="width: 100%;height: 150px;">
				<table id="contenttable" class="lessGridS" cellspacing="0"
					width="100%" align="center" cellpadding="0"
					style="border-collapse: collapse;">
					<tr>
						<input type="hidden" id="batchID" name="batchID"
							value='<s:property value="batchID"/>' />
						<td align="right" style="vertical-align: middle" width="15%">退回原因</td>
						<td><textarea id="cancelReason" cols="100" rows="8"></textarea>
						</td>
					</tr>
				</table>
				<div class="bottombtn">
					<input type="button" class="tbl_query_button" value="保存"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" onclick="cancel()" />
					<input type="button" class="tbl_query_button" value="返回"
						onMouseMove="this.className='tbl_query_button_on'"
						onMouseOut="this.className='tbl_query_button'" name="btCancel"
						id="btCancel" onclick="window.close();" />
				</div>
			</div>
	</form>
</body>
</html>