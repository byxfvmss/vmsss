<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/ylb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=webapp%>/page/js/lhb.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/search.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>分录参数配置明细</title>
<script type="text/javascript">
	function submitForm(actionUrl) {
		var form = document.getElementById("conditionForm");
		form.action = actionUrl;
		form.submit();
		form.action = "listAccEntry.action";
	}
	
	function openWindows(url) {
		OpenModalWindow(encodeURI(url), 600,400, true);
	}
	
</script>
</head>
<body>
<form id="conditionForm" action="listAccEntry.action" method="post">
	<div class="windowtitle" style="background:#004C7E; height:45px; line-height:45px; text-align:center; color:#FFF; font-size:14px; font-weight:bold;">分录参数配置明细</div>
	<div id="lessGridList5" style="overflow: auto; width: 100%;">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"
			cellpadding="0" style="border-collapse: collapse;width: 100%;">
			
			<tr class="lessGrid head">

				<th style="text-align: center">序号</th>
				<!-- <th style="text-align: center">分录ID</th> -->
				<th style="text-align: center">科目编号</th>
				<th style="text-align: center">科目名称</th>
				<th style="text-align: center">是否冲账</th>
				<th style="text-align: center">币种</th>
				<th style="text-align: center">借贷标识</th>
				<th style="text-align: center">取值类型</th>
				
			</tr>
			<s:iterator value="paginationList.recordList" id="iList"
				status="stuts">
				<tr align="center"
					class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<td><s:property value='#stuts.count' /></td>
					<td><s:property value='accTitleCode' /></td>
					<td><s:property value='accTitleName' /></td>
					<td><s:property value='isReverseName' /></td>
					<td><s:property value='currencyName' /></td>
					<td><s:property value='cdFlagName' /></td>
					<td><s:property value='transNumTypName' /></td>
					

				</tr>
			</s:iterator>
		</table>


	</div>
	
	</form>
</body>
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 375;
</script>
</html>