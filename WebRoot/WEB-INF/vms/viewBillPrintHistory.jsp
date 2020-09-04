<!--file: <%=request.getRequestURI()%> -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
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
</head>
<body onLoad="javascript:tips()" onmousemove="MM(event)"
	onmouseout="MO(event)">
	<div id="tbl_current_status">
		<img src="<%=bopTheme%>/themes/images/icons/icon13.png" /> <span
			class="current_status_menu">当前位置：</span> <span
			class="current_status_submenu1">销项税管理</span> <span
			class="current_status_submenu">开票管理</span> <span
			class="current_status_submenu">发票补打</span> <span
			class="current_status_submenu">查看补打信息</span>
	</div>
	<div class="centercondition">
		<div class="blankbox" id="blankbox"
			style="overflow:auto;height: 100%;">
			<s:if test="#request.bphList.size()==0">
				暂无补打记录
			</s:if>
			<s:else>
				<table id="contenttable" class="lessGrid" cellspacing="0"
					width="100%" align="center" cellpadding="0"
					style="border-collapse: collapse;">
					<tr class="lessGrid head">

						<th style="text-align: center">补打编号</th>
						<th style="text-align: center">补打日期</th>
						<th style="text-align: center">补打用户</th>
						<th style="text-align: center">状态</th>

					</tr>
					<s:iterator id="bphList" value="#request.bphList" status="stuts">
						<tr align="center"
							class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td><s:property value='#bphList.billID' /></td>
							<td><s:property value='#bphList.printTime' /></td>
							<td><s:property value='#bphList.printer' /></td>
							<td><s:property value='#bphList.flag' /></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
		</div>
		<div class="bottombtn">
			<input type="button" class="tbl_query_button" value="返回"
				onMouseMove="this.className='tbl_query_button_on'"
				onMouseOut="this.className='tbl_query_button'" name="btCancel"
				id="btCancel" onclick="window.close();" />
		</div>
	</div>
	<script language="javascript" type="text/javascript" charset="UTF-8">
		window.onload = function() {
			var hightValue = screen.availHeight - 270;
			var hightValueStr = "height:" + hightValue + "px";

			if (typeof (eval("document.all.blankbox")) != "undefined") {
				document.getElementById("blankbox").setAttribute("style",
						hightValueStr);
			}
		}
	</script>
</body>
</html>