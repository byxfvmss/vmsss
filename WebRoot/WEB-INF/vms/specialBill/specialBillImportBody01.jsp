<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="../../../page/include.jsp"%>
<link href="<%=bopTheme2%>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<%=webapp%>/page/common.css" type="text/css"
	rel="stylesheet">

<script src="<%=bopTheme%>/js/MessageBox/js/messageBox.js"
	type="text/javascript"></script>
<link href="<%=bopTheme%>/js/MessageBox/css/messageBox.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<%=webapp%>/page/js/jquery/jquery_1.42.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/window.js"></script>
<script type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=bopTheme%>/js/main.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>附加税管理</title>

<script type="text/javascript">
	
</script>
</head>
<body>
	
		<div id="lessGridList4" style="overflow: auto; width: 100%;">
			<table class="lessGrid" cellspacing="0" rules="all" border="0"
				cellpadding="0" style="border-collapse: collapse;width: 100%;">
				<tr class="lessGrid head">
					<th width="3%" style="text-align:center"><input id="CheckAll"
						style="width:13px;height:13px;" type="checkbox" /></th>
					<th style="text-align: center">序号</th>
					<th style="text-align: center">税票票号</th>
					<th style="text-align: center">缴款单位代码</th>
					<th style="text-align: center">缴款单位全称</th>
					<th style="text-align: center">科目编码</th>
					<th style="text-align: center">科目名称</th>
					<th style="text-align: center">填发日期</th>
					<th style="text-align: center">税款开始时间</th>
					<th style="text-align: center">税款结束时间</th>
					<th style="text-align: center">税款限缴日期</th>
					<th style="text-align: center">实缴金额</th>
					<th style="text-align: center">操作</th>

				</tr>
				<s:iterator value="paginationList.recordList" id="iList"
					status="stuts">

					<tr align="center"
						class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
						<td><input type="checkbox" style="width:13px;height:13px;"
							name="checkedlineNo" id="checkedlineNo"
							value="<s:property value="#stuts.count-1"/>" /></td>
						<td align="center"><s:property value='#stuts.count' /></td>
								<td><s:property value='billNo' /></td>
								<td><s:property value='taxNo' /></td>
								<td><s:property value='taxInstChn' /></td>
								<%-- <td><s:property value='bankandname' /></td> --%>
								<%-- <td><s:property value='bankandaccount' /></td> --%>
								<td><s:property value='subjectId' /></td>
								<td><s:property value='subjectName' /></td>
								<%-- <td><s:property value='subjectClass' /></td>
								<td><s:property value='nationalTre' /></td> --%>
								<td><s:property value='writeData' /></td>
								<td><s:property value='belongDataS' /></td>
								<td><s:property value='belongDataE' /></td>
								<td><s:property value='payData' /></td>
								<td><s:property value='taxAmtSum' /></td>
								<%-- <td><s:property value='dataStatus' /></td>
								<td><s:property value='remark' /></td> --%>
								<td></td>

								</tr>
				</s:iterator>
			</table>

		</div>
		

</body>
</html>















