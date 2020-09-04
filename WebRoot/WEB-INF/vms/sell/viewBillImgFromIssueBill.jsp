<!-- file: <%=request.getRequestURI() %> -->
<%@ page language="java" import="java.util.*"
	import="java.math.BigDecimal"
	import="com.opensymphony.util.BeanUtils"
	import="com.cjit.common.constant.ScopeConstants"
	import="com.cjit.common.util.NumberUtils"
	import="com.cjit.common.util.PaginationList"
	import="com.cjit.vms.trans.model.TransInfo"
	import="com.cjit.vms.trans.util.DataUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../../page/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<title>增值税管理平台</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=bopTheme2%>/css/main.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/calendar_notime.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/webctrl/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/XmlHttp.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/search.js"></script>
	<script language="javascript" type="text/javascript" src="<%=webapp%>/page/js/ylb.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/main.js"></script>
	<script language="javascript" type="text/javascript" src="<%=bopTheme%>/js/search.js"></script>
	<script>  
	function revoke(){
		submitAction(document.forms[0], "listBillTrack.action?fromFlag=menu&flag=track");
	}
	</script>
	
	<style type="text/css">
		.style1{
			width: 242px;
		}
	</style>
</head>
<body onkeydow="enterkey(event)">
	<table id="tbl_main" cellpadding=0 cellspacing=0>
	<tr>
		<td align="left">
		<table id="tbl_current_status" >
		<tr style="margin-left:-100px">
			<td>
				<img src="<%=bopTheme2%>/img/jes/icon/home.png" />
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu">销项税管理
			
				
					<span class="actionIcon">-&gt;</span>发票红冲
					<span class="actionIcon">-&gt;</span>红票开具
					<span class="actionIcon">-&gt;</span>查看票样
				</span>
				
			</td>
		</tr>
		</table>
		</td>
	</tr>
	</table>
	<form name="Form1" method="post" action="" id="Form1">
	<div style="width:100%;height:100%;vertical-align: middle;" id="list1" align="center" >
		<img width="100%" title="发票票样" src="<%=webapp%><s:property value='filePath'/>"/><br>
	</div>
	<table id="tbl_tools" width="100%" border="0">
		<tr>
			<td align="left">
				<input type="button" class="tbl_query_button" value="关闭"
					onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
					name="btCancel" id="btCancel" onclick="window.close()" />
			</td>
		</tr>
	</table>
	</form>
</body>
</html>